plugins {
    kotlin("multiplatform") version "2.0.20"
    id("org.jetbrains.compose") version "1.7.0"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.20"
}

repositories {
    mavenCentral()
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

kotlin {
    js(IR) {
        browser {
            runTask {
                devServer = devServer.copy(port = 3000)  // Измените на 3000
            }
        }
        binaries.executable()

        // Фикс Node.js версии — САМОЕ ГЛАВНОЕ
        nodejs {
            version = "22.20.0"   // ← эта строчка решает вашу основную ошибку
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                // ← ПРАВИЛЬНЫЕ ЗАВИСИМОСТИ ДЛЯ COMPOSE HTML (Web) 1.7.0
                // Базовый HTML + SVG (всё, что нужно для рендеринга)
                implementation(compose.html.core)
                implementation(compose.html.svg)
                // Если нужны расширения (widgets, etc.) — добавьте:
                // implementation(compose.html.ext.core)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:2.3.12")
                implementation("io.ktor:ktor-client-js:2.3.12")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
            }
        }
    }
}