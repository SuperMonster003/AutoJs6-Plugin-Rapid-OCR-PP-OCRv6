******

### Historique des versions

******

# v1.0.2

###### 2026/09/12

* `Correctif` Correction de l'absence de `libc++_shared.so` dans les bibliothèques natives empaquetées : CMake utilise désormais explicitement la STL `c++_shared`, AGP embarque la libc++ du NDK r28.2 pour les 4 ABI et le moteur OCR se charge de nouveau sur les appareils à pages de 16 Ko
* `Amélioration` Bibliothèque native OpenCV 4.8.0 synchronisée avec la recompilation NDK r28c (Clang 19.0.1) (donneur : AutoJs6-Plugin-OpenCV) ; `libopencv_java4.so` des 4 ABI conserve l'alignement `PT_LOAD` de 16 Ko et embarque un manifeste de provenance

# v1.0.1

###### 2026/09/11

* `Correctif` Un plantage lors de l'utilisation successive de Paddle OCR et Rapid OCR dans la même application empaquetée
* `Amélioration` Vérification à la compilation de l'alignement des pages de 16 KB des bibliothèques natives 64 bits, avec contrôle du contrat manifest et rapports JSON
* `Dépendance` Mise a niveau de ONNX Runtime (com.microsoft.onnxruntime:onnxruntime-android) version 1.18.0 -> 1.21.1
* `Dépendance` Mise à jour de OpenCV version 4.5.3 à 4.8.0

# v1.0.0

###### 2026/09/01

* `Fonctionnalité` Ajout des informations identifiant le plugin Rapid OCR (PP-OCRv6 Small) avec ID de plugin `rapid-ocr-pp-ocrv6`, moteur `rapid-ocr`, et variante `pp-ocrv6`.
* `Fonctionnalité` Ajout du service AIDL `PADDLE_OCR` et des interfaces `recognizeText` et `detect`. `recognizeText` renvoie des chaînes de texte, tandis que `detect` renvoie `text`, `confidence` et `bounds` pour chaque résultat.
* `Fonctionnalité` Prise en charge des images encodées et des images `ARGB_8888` brutes. Sous Android 13, `SharedMemory` est utilisé quand il reste disponible, avec une lecture en flux comme solution de repli quand `SharedMemory` reste indisponible ou échoue.
* `Fonctionnalité` Ajout de la détection et de la reconnaissance locales de texte avec les modèles de détection et de reconnaissance `PP-OCRv6 Small`, `ONNX Runtime` et `OpenCV Mobile`.
* `Fonctionnalité` Prise en charge des options `maxSideLen` et `scoreThreshold` pour la reconnaissance.
* `Fonctionnalité` Connexion de `preBuild` au téléchargement des ressources de modèles, à la vérification de leurs sommes `SHA-256` et à la synchronisation des bibliothèques natives `ONNX Runtime`.
* `Fonctionnalité` Ajout des métadonnées de plugin et des instructions pour espagnol, français, russe, arabe, japonais, coréen, anglais, chinois simplifié, chinois traditionnel de Hong Kong et chinois traditionnel de Taïwan.
* `Fonctionnalité` Ajout de sources `JSON` et du générateur `Python` pour les documents `README` et `CHANGELOG` multilingues.
* `Fonctionnalité` Ajout de variantes APK pour `arm64-v8a`, `armeabi-v7a`, `x86_64` et `universal`. Les noms des fichiers publiés incluent la version et la variante `ABI`, et les copies archivées incluent une somme `CRC32`.
* `Amélioration` Uniformiser la mise en page du README et la gestion des versions de la plateforme Gradle
