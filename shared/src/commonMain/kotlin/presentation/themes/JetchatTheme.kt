package presentation.themes

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import presentation.typography.JetchatTypography

private object JetChatTheme {
    val AppDarkColorScheme = darkColorScheme(
        primary = Color(185, 206, 139),
        onPrimary = Color(29, 47, 0),
        primaryContainer = Color(196, 202, 174),
        onPrimaryContainer = Color(29, 39, 5),
        inversePrimary = Blue40,
        secondary = Color(70, 72, 61),
        onSecondary = Color(201, 203, 190),
        secondaryContainer = Color(27, 28, 23),
        onSecondaryContainer = Color(207, 208, 200),
        tertiary = Color(60, 76, 29),
        onTertiary = Yellow20,
        tertiaryContainer = Yellow30,
        onTertiaryContainer = Yellow90,
        error = Red80,
        onError = Red20,
        errorContainer = Red30,
        onErrorContainer = Red90,
        background = Color(27, 28, 23),
        onBackground = Color(39, 40, 32),
        surface = Color(186, 206, 145),
        onSurface = Grey80,
        inverseSurface = Grey90,
        inverseOnSurface = Grey20,
        surfaceVariant = Color(80, 96, 49),
        onSurfaceVariant = BlueGrey80,
        outline = BlueGrey60
    )

    val AppLightColorScheme = lightColorScheme(
        primary = Color(83, 101, 49),
        onPrimary = Color(254, 255, 245),
        primaryContainer = Color(92, 97, 75),
        onPrimaryContainer = Color(254, 255, 245),
        inversePrimary = Blue80,
        secondary = Color(226, 228, 214),
        onSecondary = Color(66, 68, 54),
        secondaryContainer = Color(252, 253, 247),
        onSecondaryContainer = Color(57, 56, 52),
        tertiary = Color(214, 237, 169),
        onTertiary = Color.White,
        tertiaryContainer = Yellow90,
        onTertiaryContainer = Yellow10,
        error = Red40,
        onError = Color.White,
        errorContainer = Red90,
        onErrorContainer = Red10,
        background = Color(253, 252, 247),
        onBackground = Color(241, 241, 231),
        surface = Color(186, 206, 145),
        onSurface = Grey10,
        inverseSurface = Grey20,
        inverseOnSurface = Grey95,
        surfaceVariant = Color(80, 96, 49),
        onSurfaceVariant = BlueGrey30,
        outline = BlueGrey50
    )

    operator fun get(key: ThemeMode) = when (key) {
        ThemeMode.DARK -> AppDarkColorScheme
        ThemeMode.LIGHT -> AppLightColorScheme
    }
}

@Composable
fun ApplicationTheme(
    theme: ThemeMode = isSystemInDarkTheme().toTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = JetChatTheme[theme],
        typography = JetchatTypography
    ) {
        val rippleIndication = rememberRipple()
        CompositionLocalProvider(
            LocalIndication provides rippleIndication,
            content = content
        )
    }
}
