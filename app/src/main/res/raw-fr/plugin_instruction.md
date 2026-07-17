# Instructions du plugin

## Identité

- Action: `org.autojs.plugin.PADDLE_OCR`
- Moteur: `rapid-ocr`
- Variante: `pp-ocrv6`
- ID du plugin: `rapid-ocr-pp-ocrv6`

## Reconnaissance

- `recognizeText` renvoie une liste de chaînes de texte reconnues.
- `detect` renvoie les blocs détectés avec le texte, le niveau de confiance et les limites.

## Entrée

- Les images encodées sont décodées depuis le descripteur de fichier fourni.
- L'entrée raw utilise des pixels ARGB_8888 avec les métadonnées de largeur, de hauteur et de pas de ligne.

## Options

- `maxSideLen` correspond à `OcrOptions.detLongSize` et contrôle la longueur maximale du côté de l'image utilisé pour la détection. La valeur par défaut est 1024.
- `scoreThreshold` correspond à `OcrOptions.scoreThreshold` et contrôle le score de détection minimal accepté. La valeur par défaut est 0.5.
