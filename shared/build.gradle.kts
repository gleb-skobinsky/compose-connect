plugins {
    kotlin(Plugins.kmp)
    kotlin(Plugins.cocoapods)
    id(Plugins.android_library)
    id(Plugins.compose)
    kotlin(Plugins.serialization) version Versions.kotlin
}

kotlin {
    androidTarget()

    jvm("desktop")

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    js(IR) {
        browser()
        binaries.executable()
    }

    cocoapods {
        version = "1.0.0"
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
//        extraSpecAttributes["resources"] = "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation(compose.materialIconsExtended)
                implementation(compose.material3)
                implementation(Dependencies.Ktor.core)
                implementation(Dependencies.Ktor.websocket)
                implementation(Dependencies.Serialization.core)
                implementation(Dependencies.Ktor.json)
                implementation(Dependencies.Ktor.contentNegotiation)
                implementation(Dependencies.Koin.core)
                implementation(Dependencies.datetime)
                implementation(Dependencies.kamel)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(Dependencies.Coroutines.test)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Dependencies.Android.androidx_core)
                implementation(Dependencies.Android.accompanist)
                implementation(Dependencies.Ktor.Client.okhttp)
                implementation(Dependencies.Koin.android)
                implementation(Dependencies.Koin.Android.compat)
                implementation(Dependencies.Koin.Android.workmanager)
                implementation(Dependencies.Koin.Android.navigation)
                implementation(Dependencies.Koin.Android.compose)
                implementation(Dependencies.Android.compose_navigation)
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(Dependencies.Ktor.Client.ios)
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.common)
                implementation(Dependencies.Ktor.Client.cio)
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(Dependencies.Ktor.Client.js)
                implementation(npm("uuid", "9.0.1"))
            }
        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.chirrio.common"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
    packaging {
        resources.excludes.add("META-INF/versions/**")
    }
}