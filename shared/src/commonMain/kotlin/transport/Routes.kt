package transport

import buildVariant.RuntimeMode

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

object Routes {
    val development = PlatformHosts(
        desktop = "127.0.0.1",
        js = "127.0.0.1",
        android = "10.0.2.2",
        ios = "127.0.0.1"
    )
    val production = PlatformHosts(
        desktop = "https://chirrio.mooo.com",
        js = "https://chirrio.mooo.com",
        android = "https://chirrio.mooo.com",
        ios = "https://chirrio.mooo.com"
    )

    operator fun get(mode: RuntimeMode) = when (mode) {
        RuntimeMode.DEVELOPMENT -> development
        RuntimeMode.PRODUCTION -> production
    }
}