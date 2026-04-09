plugins {
    // Definimos las versiones AQUÍ, pero NO las aplicamos todavía (apply false)
    id("com.android.application") version "9.1.0" apply false
    id("org.jetbrains.kotlin.android") version "2.2.10" apply false
}

// Tarea para limpiar la carpeta de compilación
tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}