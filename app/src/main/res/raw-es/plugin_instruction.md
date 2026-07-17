# Instrucciones del plugin

## Identidad

- Acción: `org.autojs.plugin.PADDLE_OCR`
- Motor: `rapid-ocr`
- Variante: `pp-ocrv6`
- ID del plugin: `rapid-ocr-pp-ocrv6`

## Reconocimiento

- `recognizeText` devuelve una lista de cadenas de texto reconocidas.
- `detect` devuelve los bloques detectados con texto, confianza y límites.

## Entrada

- Las imágenes codificadas se decodifican desde el descriptor de archivo proporcionado.
- La entrada raw utiliza píxeles ARGB_8888 con metadatos de ancho, alto y paso de fila.

## Opciones

- `maxSideLen` corresponde a `OcrOptions.detLongSize` y controla la longitud máxima del lado de la imagen usada para la detección. El valor predeterminado es 1024.
- `scoreThreshold` corresponde a `OcrOptions.scoreThreshold` y controla la puntuación mínima de detección aceptada. El valor predeterminado es 0.5.
