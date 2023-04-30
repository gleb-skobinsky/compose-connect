package themes

fun Boolean.toTheme() = if (this) ThemeMode.DARK else ThemeMode.LIGHT

fun ThemeMode.toBoolean() = when (this) {
    ThemeMode.DARK -> true
    ThemeMode.LIGHT -> false
}

