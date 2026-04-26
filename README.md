# ⚠️ Disclaimer: Dependencias Externas

Este proyecto utiliza librerías de terceros para la arquitectura MVVM y la interfaz gráfica. El uso de estas dependencias implica:
* **Mantenimiento:** Es necesario revisar actualizaciones para evitar vulnerabilidades.
* **Rendimiento:** El catálogo extendido de iconos aumenta ligeramente el tamaño del APK.

## 📦 Instalación
Añade estas líneas en tu archivo `build.gradle.kts` (Módulo `:app`) dentro del bloque `dependencies`:

```kotlin
// Integración de ViewModel con Compose (MVVM)
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")

// Catálogo completo de Material Icons
implementation("androidx.compose.material:material-icons-extended:1.6.0")