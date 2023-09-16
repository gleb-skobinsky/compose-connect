package themes

enum class ThemeMode {
    DARK,
    LIGHT;

    operator fun not() = when (this) {
        DARK -> LIGHT
        LIGHT -> DARK
    }

    operator fun invoke() = when (this) {
        DARK -> true
        LIGHT -> false
    }
}

fun Boolean.toTheme() = if (this) ThemeMode.DARK else ThemeMode.LIGHT