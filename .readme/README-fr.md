<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <source srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/res/mipmap-night/ic_launcher.png?raw=true" media="(prefers-color-scheme: dark)" />
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-rapid-ocr-pp-ocrv6-ic-launcher" border="0" width="128" />
    </picture>
  </p>

  <p>Plugin Rapid OCR pour la reconnaissance de texte PP-OCRv6 Small dans AutoJs6</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6?color=534BAE&label=License"/></a>
  </p>
</div>

******

### Langues (Languages)

******

Le README.md actuel prend en charge les langues suivantes:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-en.md)
- Français [fr] # actuel
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ar.md)

******

### Introduction

******

Le plugin AutoJs6 Rapid OCR PP-OCRv6 Small fournit la détection et la reconnaissance de texte avec RapidOCR, ONNX Runtime et OpenCV Mobile. Il peut renvoyer le texte reconnu, les valeurs de confiance et les limites du texte.

******

### Fonctions

******

- Fournit le service OCR partagé avec l'ID de plugin `rapid-ocr-pp-ocrv6`, le moteur `rapid-ocr` et la variante `pp-ocrv6`.
- Expose l'interface AIDL OCR via `org.autojs.plugin.PADDLE_OCR` et fonctionne avec le mécanisme de découverte des plugins OCR de l'hôte AutoJs6.
- Prend en charge `ocr.rapid.recognizeText` pour les listes de chaînes et `ocr.rapid.detect` pour le texte, les valeurs de confiance et les limites.
- Prend en charge les images encodées et les tampons d'image `ARGB_8888` bruts.
- Fournit des APK pour `arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64` et `universal`.
- Les métadonnées du plugin, les instructions d'utilisation, le README et le CHANGELOG sont localisés en espagnol, français, russe, arabe, japonais, coréen, anglais, chinois simplifié, chinois traditionnel de Hong Kong et chinois traditionnel de Taïwan.
- Construit avec RapidOCR, ONNX Runtime et OpenCV Mobile.
- Les images peuvent contenir jusqu'à 16777216 pixels; les tampons bruts sont limités à 64 MiB
- Les images encodées sont limitées à 64 MiB avec prise en charge des fichiers et des tubes

******

### Exemples d'utilisation

******

Après avoir lu une image, utilisez le module Rapid OCR pour reconnaître le texte ou obtenir des résultats détaillés:

```js
let image = images.read("./sample.png");

let texts = ocr.rapid.recognizeText(image, {
    variant: "pp-ocrv6",
});
console.log(texts);

let results = ocr.rapid.detect(image, {
    variant: "pp-ocrv6",
    maxSideLen: 1024,
    scoreThreshold: 0.5,
});
console.log(results);

image.recycle();
```

`recognizeText` renvoie une liste de chaînes, tandis que `detect` renvoie une liste de résultats détaillés.

******

### Interface du plugin

******

L'hôte peut découvrir et appeler le plugin avec l'identité suivante:

```text
action: org.autojs.plugin.PADDLE_OCR
engine: rapid-ocr
variant: pp-ocrv6
plugin id: rapid-ocr-pp-ocrv6
supported ABIs: arm64-v8a, armeabi-v7a, x86, x86_64, universal
```

Chaque résultat de `detect` contient `text`, `confidence` et `bounds`; l'interface actuelle ne fournit pas de champ quad.

******

### Options de reconnaissance

******

L'implémentation actuelle du plugin lit les options de reconnaissance suivantes:

- `maxSideLen`: Longueur latérale maximale utilisée pendant la détection. La valeur par défaut est `1024`.
- `scoreThreshold`: Seuil de score des zones de texte. La valeur par défaut est `0.5`.

******

### Ressources des modèles

******

La compilation télécharge et vérifie les 4 ressources suivantes depuis ModelScope:

- `PP-OCRv6_det_small.onnx`: Modèle de détection de texte PP-OCRv6 Small.
- `PP-OCRv6_rec_small.onnx`: Modèle de reconnaissance de texte PP-OCRv6 Small.
- `ch_ppocr_mobile_v2.0_cls_mobile.onnx`: Ressource de modèle auxiliaire requise pendant l'initialisation du moteur.
- `ppocrv6_dict.txt`: Dictionnaire de reconnaissance PP-OCRv6.

******

### Historique des versions

******

# v1.0.4

###### 2026/09/13

* `Correctif` Accumulation de références locales JNI lors de la conversion de nombreux blocs de texte, pouvant saturer la table de références sous Android 7.x _[`issue #575`](http://issues.autojs6.com/575)_
* `Correctif` Tampons natifs et références JNI temporaires conservés lors de la conversion des chaînes pendant l'initialisation, et traitement incorrect des chaînes vides _[`issue #575`](http://issues.autojs6.com/575)_
* `Correctif` Les informations de version et d'ABI du centre des plugins correspondent à l'APK installé
* `Correctif` Les images encodées sont limitées à 64 MiB avec prise en charge des fichiers et des tubes
* `Correctif` Les dates de version utilisent un format anglais uniforme
* `Amélioration` Validation des versions, signatures et variantes complètes des APK avant la création des fichiers à télécharger
* `Amélioration` Les images peuvent contenir jusqu'à 16777216 pixels; les tampons bruts sont limités à 64 MiB
* `Amélioration` Étendre les ABI natives et les métadonnées du plugin à arm64-v8a, armeabi-v7a, x86 et x86_64, avec des APK universels et par ABI cohérents

# v1.0.3

###### 2026/09/12

* `Correctif` Texte reconnu incorrect après la recréation du service ou des initialisations répétées du moteur, causé par l'ajout répété du dictionnaire OCR
* `Correctif` Anciennes sessions ONNX conservées lors de la réinitialisation et accès concurrents entre l'initialisation et la reconnaissance dans le même processus

# v1.0.2

###### 2026/09/12

* `Correctif` Correction de l'absence de `libc++_shared.so` dans les bibliothèques natives empaquetées : CMake utilise désormais explicitement la STL `c++_shared`, AGP embarque la libc++ du NDK r28.2 pour les 4 ABI et le moteur OCR se charge de nouveau sur les appareils à pages de 16 Ko
* `Correctif` Échec de la tâche `clean` lorsque l'option de nettoyage des dépendances natives de Rapid OCR n'était pas initialisée
* `Amélioration` Bibliothèque native OpenCV 4.8.0 synchronisée avec la recompilation NDK r28c (Clang 19.0.1) (donneur : AutoJs6-Plugin-OpenCV) ; `libopencv_java4.so` des 4 ABI conserve l'alignement `PT_LOAD` de 16 Ko et embarque un manifeste de provenance

##### Pour consulter davantage d'historique des versions

* [CHANGELOG-fr.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/assets/doc/CHANGELOG-fr.md)

******

### Compilation

******

```powershell
.\gradlew.bat :app:assembleDebug
```

Compilation Release:

```powershell
.\gradlew.bat :app:assembleRelease
```

Les paramètres de compilation proviennent de `version.properties`; le SDK minimum actuel est 24 et le SDK cible est 36. `preBuild` télécharge et vérifie les ressources des modèles, puis synchronise les bibliothèques natives ONNX Runtime.

******

### Structure des ressources

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/assets/doc/CHANGELOG-*.md
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
libs/rapidocr/src/main/assets/models/
```

`strings.xml` contient les descriptions localisées du plugin et `plugin_instruction.md` contient les instructions d'utilisation affichées par l'hôte. Les fichiers README et CHANGELOG sont générés depuis les sources JSON par `.python/generate_markdown.py`. Seul `README.md` est généré à la racine du dépôt et aucun `CHANGELOG.md` racine n'est généré.

******

### Liens

******

- Documentation OCR AutoJs6: https://docs.autojs6.com/#/ocr
- Projet RapidOCR: https://github.com/RapidAI/RapidOCR
- Ressources de modèles ModelScope: https://www.modelscope.cn/models/RapidAI/RapidOCR
- ONNX Runtime: https://onnxruntime.ai/
- OpenCV Mobile: https://github.com/nihui/opencv-mobile


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/docs/16kb.md)
