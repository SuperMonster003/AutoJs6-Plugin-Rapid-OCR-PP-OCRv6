# -*- coding: utf-8 -*-
import json
import re
from pathlib import Path


LANGUAGE_CODES = [
    "zh-Hans",
    "zh-Hant-HK",
    "zh-Hant-TW",
    "en",
    "fr",
    "es",
    "ja",
    "ko",
    "ru",
    "ar",
]
LANGUAGE_CODE_DEFAULT = "zh-Hans"
README_COMPAT_ROOT_LANGUAGES = set()
ANDROID_CHANGELOG_ALIASES = {
    "zh-Hans": ["zh", "zh-Hans"],
    "zh-Hant-HK": ["zh-rHK", "zh-Hant-HK"],
    "zh-Hant-TW": ["zh-rTW", "zh-Hant-TW"],
}
FORBIDDEN_FULLWIDTH_SYMBOLS = re.compile(
    r"[\u3000-\u303F\uFF01-\uFF60\uFFE0-\uFFEE]"
)


def project_root() -> Path:
    return Path(__file__).resolve().parents[1]


ROOT = project_root()
README_DIR = ROOT / ".readme"
CHANGELOG_DIR = ROOT / ".changelog"
ANDROID_CHANGELOG_DIR = ROOT / "app" / "src" / "main" / "assets" / "doc"


def validate_symbols(value, source: Path):
    if isinstance(value, dict):
        for child in value.values():
            validate_symbols(child, source)
    elif isinstance(value, list):
        for child in value:
            validate_symbols(child, source)
    elif isinstance(value, str):
        match = FORBIDDEN_FULLWIDTH_SYMBOLS.search(value)
        if match:
            code_point = f"U+{ord(match.group()):04X}"
            raise ValueError(
                f"Forbidden fullwidth symbol {match.group()!r} ({code_point}) in "
                f"{source.relative_to(ROOT)}"
            )


def load_json(path: Path):
    with path.open("r", encoding="utf-8") as f:
        value = json.load(f)
    validate_symbols(value, path)
    return value


def read_template(path: Path) -> str:
    text = path.read_text(encoding="utf-8")
    validate_symbols(text, path)
    return text


def render_template(text: str, values: dict) -> str:
    def repl(match):
        key = match.group(1).strip()
        if key not in values:
            raise KeyError(f"Missing template value: {key}")
        return str(values[key])

    return re.sub(r"\{\{\s*([A-Za-z0-9_$.-]+)\s*\}\}", repl, text)


def render_dynamic(value, values: dict):
    if isinstance(value, dict):
        return {key: render_dynamic(child, values) for key, child in value.items()}
    if isinstance(value, list):
        return [render_dynamic(child, values) for child in value]
    if isinstance(value, str):
        return render_template(value, values)
    return value


def bullet_list(items):
    return "\n".join(f"- {item}" for item in items)


def markdown_link(label, url):
    return f"[{label}]({url})"


def load_languages():
    common = load_json(README_DIR / "common.json")
    languages = {}
    changelogs = {}
    expected_versions = None
    for code in LANGUAGE_CODES:
        raw_language = load_json(README_DIR / f"lang_{code}.json")
        merged_language = {**common, **raw_language}
        languages[code] = render_dynamic(merged_language, merged_language)

        raw_changelog = load_json(CHANGELOG_DIR / f"lang_{code}.json")
        versions = list(raw_changelog["$data"])
        if expected_versions is None:
            expected_versions = versions
        elif versions != expected_versions:
            raise ValueError(
                f"Changelog versions for {code} do not match {LANGUAGE_CODE_DEFAULT}"
            )
        changelog_values = {
            key: value for key, value in raw_changelog.items() if key != "$data"
        }
        changelog_values = render_dynamic(changelog_values, changelog_values)
        changelog_data = render_dynamic(raw_changelog["$data"], changelog_values)
        changelogs[code] = {
            "values": changelog_values,
            "data": changelog_data,
        }
    return languages, changelogs


def format_changelog_items(changelog, limit=None):
    values = changelog["values"]
    data = changelog["data"]
    chunks = []
    for index, (version_name, item) in enumerate(data.items()):
        if limit is not None and index >= limit:
            break
        lines = [
            f"# {version_name}",
            "",
            f"###### {item['released_date']}",
            "",
        ]
        for category in ["hint", "feature", "fix", "improvement", "dependency"]:
            for item_text in item.get(category, []):
                label = values[f"changelog_label_{category}"]
                lines.append(f"* \u0060{label}\u0060 {item_text}")
        chunks.append("\n".join(lines).rstrip())
    return "\n\n".join(chunks).rstrip() + "\n"


def build_language_list(target_code, languages):
    lines = []
    repo_url = languages[target_code]["repo_url"]
    for code in LANGUAGE_CODES:
        content = languages[code]
        label = f"{content['$name']} [{code}]"
        if code == target_code:
            lines.append(f"- {label} # {content['text_current_lowercase']}")
        else:
            url = f"{repo_url}/blob/master/.readme/README-{code}.md"
            lines.append(f"- {markdown_link(label, url)}")
    return "\n".join(lines)


def build_readme_values(code, languages, changelogs):
    content = dict(languages[code])
    repo_url = content["repo_url"]
    content["placeholder_ul_languages_all_supported"] = build_language_list(
        code, languages
    )
    content["placeholder_features"] = bullet_list(content["features"])
    content["placeholder_option_lines"] = bullet_list(content["option_lines"])
    content["placeholder_model_asset_lines"] = bullet_list(
        content["model_asset_lines"]
    )
    content["placeholder_latest_release_history"] = format_changelog_items(
        changelogs[code], limit=3
    ).rstrip()
    content["placeholder_read_more_in_changelog_md"] = markdown_link(
        f"CHANGELOG-{code}.md",
        f"{repo_url}/blob/master/app/src/main/assets/doc/CHANGELOG-{code}.md",
    )
    return content


def write_text(path: Path, text: str):
    validate_symbols(text, path)
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(text, encoding="utf-8", newline="\n")
    print(f"Generated {path.relative_to(ROOT)}")


def generate_readmes(languages, changelogs):
    template = read_template(README_DIR / "template_readme.md")
    for code in LANGUAGE_CODES:
        output = render_template(
            template, build_readme_values(code, languages, changelogs)
        )
        write_text(README_DIR / f"README-{code}.md", output)
        if code == LANGUAGE_CODE_DEFAULT:
            write_text(ROOT / "README.md", output)
        if code in README_COMPAT_ROOT_LANGUAGES:
            write_text(ROOT / f"README-{code}.md", output)


def generate_changelogs(languages, changelogs):
    template = read_template(CHANGELOG_DIR / "template_changelog.md")
    for code in LANGUAGE_CODES:
        values = dict(languages[code])
        values["placeholder_release_history"] = format_changelog_items(
            changelogs[code]
        ).rstrip()
        output = render_template(template, values)
        names = ANDROID_CHANGELOG_ALIASES.get(code, [code])
        for name in names:
            write_text(ANDROID_CHANGELOG_DIR / f"CHANGELOG-{name}.md", output)
        if code == LANGUAGE_CODE_DEFAULT:
            write_text(ANDROID_CHANGELOG_DIR / "CHANGELOG.md", output)


def main():
    if LANGUAGE_CODE_DEFAULT not in LANGUAGE_CODES:
        raise ValueError(
            f"Default language code {LANGUAGE_CODE_DEFAULT!r} is not supported"
        )
    languages, changelogs = load_languages()
    generate_changelogs(languages, changelogs)
    generate_readmes(languages, changelogs)


if __name__ == "__main__":
    main()
