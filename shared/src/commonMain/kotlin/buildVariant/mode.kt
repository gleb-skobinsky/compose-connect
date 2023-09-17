package buildVariant

enum class RuntimeMode {
    DEVELOPMENT,
    PRODUCTION;

    fun toPort() = when (this) {
        DEVELOPMENT -> 80
        PRODUCTION -> 443
    }
}

val mode = RuntimeMode.PRODUCTION