

object Dependencies {
    const val datetime = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.datetime}"
    const val kamel = "media.kamel:kamel-image:${Versions.kamel}"
    object Android {
        const val androidx_core = "androidx.core:core-ktx:${Versions.core_ktx}"
        const val accompanist = "com.google.accompanist:accompanist-insets:${Versions.accompanist}"
        const val compose_navigation = "androidx.navigation:navigation-compose:${Versions.compose_navigation}"
        const val compose_activity = "androidx.activity:activity-compose:${Versions.compose_activity}"
        const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    }
    object Ktor {
        const val core = "io.ktor:ktor-client-core:${Versions.ktor}"
        const val websocket = "io.ktor:ktor-client-websockets:${Versions.ktor}"
        const val json = "io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}"
        const val contentNegotiation = "io.ktor:ktor-client-content-negotiation:${Versions.ktor}"
        object Client {
            const val ios = "io.ktor:ktor-client-ios:${Versions.ktor}"
            const val cio = "io.ktor:ktor-client-cio:${Versions.ktor}"
            const val js = "io.ktor:ktor-client-js:${Versions.ktor}"
            const val okhttp = "io.ktor:ktor-client-okhttp:${Versions.ktor}"
        }
    }

    object Coroutines {
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
    }

    object Serialization {
        const val core = "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.serialization}"
    }

    object Koin {
        const val core = "io.insert-koin:koin-core:${Versions.koin}"
        const val android = "io.insert-koin:koin-android:${Versions.koin}"
        object Android {
            const val compat = "io.insert-koin:koin-android-compat:${Versions.koin}"
            const val workmanager = "io.insert-koin:koin-androidx-workmanager:${Versions.koin}"
            const val navigation = "io.insert-koin:koin-androidx-navigation:${Versions.koin}"
            const val compose = "io.insert-koin:koin-androidx-compose:${Versions.koin}"
        }
    }

    object Firebase {
        const val storage = "dev.gitlive:firebase-storage:${Versions.firebase}"
    }
}