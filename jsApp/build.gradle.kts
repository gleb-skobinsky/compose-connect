plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.myapplication"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    js(IR) {
        browser {
            testTask {
                testLogging.showStandardStreams = true
                useKarma {
                    useChromeHeadless()
                    useFirefox()
                }
            }
        }
        binaries.executable()
    }
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(compose.web.core)
                implementation(compose.runtime)
                implementation(project(":shared"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

compose.experimental {
    web.application {}
}

tasks.getByName("jsBrowserDevelopmentRun").doFirst {
    val error = "throw new NotImplementedError('An operation is not implemented: implement native toLanguageTag');"
    val implementation = "return \"\";"
    val file = file("../build/js/packages/MyApplication-jsApp/kotlin/androidx-ui-text.js")
    val buffer = StringBuffer()
    file.forEachLine { line ->
        buffer.appendLine(line.replace(error, implementation))
    }
    file.writeText(buffer.toString())
}