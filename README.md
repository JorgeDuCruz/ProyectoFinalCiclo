# ⚠️ Disclaimer: Dependencias Externas

Este proyecto utiliza librerías de terceros para la arquitectura MVVM, la interfaz gráfica y la persistencia de datos. El uso de estas dependencias implica:

* **Mantenimiento:** Es necesario revisar actualizaciones periódicas para evitar vulnerabilidades de seguridad.
* **Rendimiento:** El catálogo extendido de iconos aumenta ligeramente el tamaño del APK.
* **Persistencia:** La gestión de la base de datos SQLite se realiza mediante la abstracción de Room para garantizar la integridad de los datos.

## 📦 Instalación

Para el correcto funcionamiento del proyecto, asegúrate de configurar los siguientes bloques en tus archivos de Gradle.

### 1. Configuración de Plugins (Project `build.gradle.kts`)

Añade el plugin de KSP en el bloque de plugins de nivel de proyecto para gestionar el procesado de anotaciones de Room:

```kotlin
plugins {
    // ... otros plugins existentes
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
}
```
### 2. Configuración del Módulo (App build.gradle.kts)
Aplica el plugin y añade las dependencias necesarias para MVVM, UI, SQLite (Room) y Corrutinas:

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") // Aplicar el procesador KSP
}

dependencies {
    // --- Arquitectura y UI ---
    // Integración de ViewModel con Compose (MVVM)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
    
    // Catálogo completo de Material Icons
    implementation("androidx.compose.material:material-icons-extended:1.6.0")

    // --- Persistencia (SQLite con Room) ---
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion") // Soporte para Corrutinas en Room
    ksp("androidx.room:room-compiler:$roomVersion")      // Procesador de anotaciones vía KSP

    // --- Asincronía (Corrutinas de Kotlin) ---
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
}