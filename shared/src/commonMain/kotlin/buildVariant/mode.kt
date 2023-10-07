package buildVariant

enum class RuntimeMode {
    DEVELOPMENT,
    PRODUCTION;
}

val mode = RuntimeMode.DEVELOPMENT