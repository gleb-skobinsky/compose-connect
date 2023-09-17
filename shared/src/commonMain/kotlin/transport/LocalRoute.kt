package transport

import buildVariant.RuntimeMode
import buildVariant.mode

data class PlatformHosts(
    val desktop: String,
    val js: String,
    val android: String,
    val ios: String,
) {
    operator fun get(label: String): String {
        return when (label) {
            "desktop" -> desktop
            "js" -> js
            "android" -> android
            "ios" -> ios
            else -> ""
        }
    }
}

object LocalRoute {
    val development = PlatformHosts(
        desktop = "127.0.0.1:8000",
        js = "127.0.0.1",
        android = "10.0.2.2",
        ios = "127.0.0.1"
    )
    val production = PlatformHosts(
        desktop = "chirrio.mooo.com",
        js = "chirrio.mooo.com",
        android = "chirrio.mooo.com",
        ios = "chirrio.mooo.com"
    )

    operator fun get(mode: RuntimeMode) = when (mode) {
        RuntimeMode.DEVELOPMENT -> development
        RuntimeMode.PRODUCTION -> production
    }

    val current = LocalRoute[mode][platformName]

    private val protocol = when (mode) {
        RuntimeMode.DEVELOPMENT -> "http://"
        RuntimeMode.PRODUCTION -> "https://"
    }

    val currentUrl = protocol + current
}

