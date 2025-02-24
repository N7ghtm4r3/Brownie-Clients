
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.util.UUID

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version "2.1.0"
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_18)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Brownie"
            isStatic = true
        }
    }
    
    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_18)
        }
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "Brownie.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.equinox.compose)
            implementation(libs.equinox.core)
            implementation(libs.precompose)
            implementation(libs.browniecore)
            implementation(libs.lazy.pagination.compose)
            implementation(libs.kotlinx.serialization.json)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

android {
    namespace = "com.tecknobit.brownie"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.tecknobit.brownie"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.tecknobit.brownie.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            modules(
                "java.compiler", "java.instrument", "java.management", "java.naming", "java.net.http", "java.prefs",
                "java.rmi", "java.scripting", "java.security.jgss", "java.sql", "jdk.jfr", "jdk.unsupported",
                "jdk.security.auth"
            )
            packageName = "Brownie"
            packageVersion = "1.0.0"
            version = "1.0.0"
            description = "" // TODO: TO SET
            copyright = "© 2025 Tecknobit"
            vendor = "Tecknobit"
            licenseFile.set(project.file("src/desktopMain/resources/LICENSE"))
            macOS {
                iconFile.set(project.file("src/desktopMain/resources/logo.icns"))
                bundleID = "com.tecknobit.brownie"
            }
            windows {
                iconFile.set(project.file("src/desktopMain/resources/logo.ico"))
                upgradeUuid = UUID.randomUUID().toString()
            }
            linux {
                iconFile.set(project.file("src/desktopMain/resources/logo.png"))
                packageName = "com-tecknobit-brownie"
                debMaintainer = "infotecknobitcompany@gmail.com"
                appRelease = "1.0.0"
                appCategory = "PERSONALIZATION"
                rpmLicenseType = "MIT"
            }
            buildTypes.release.proguard {
                configurationFiles.from(project.file("compose-desktop.pro"))
                obfuscate.set(true)
            }
        }
    }
}
