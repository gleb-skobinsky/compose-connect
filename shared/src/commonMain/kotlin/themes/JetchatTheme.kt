package themes

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import typography.JetchatTypography

private val JetchatDarkColorScheme = darkColorScheme(
    primary = myMessageBackgroundDark,
    onPrimary = myMessageTextDark,
    primaryContainer = myCodeBackgroundDark,
    onPrimaryContainer = myCodeTextDark,
    inversePrimary = Blue40,
    secondary = othersMessageBackgroundDark,
    onSecondary = othersMessageTextDark,
    secondaryContainer = othersCodeBackgroundDark,
    onSecondaryContainer = othersCodeTextDark,
    tertiary = navigationBackgroundDark,
    onTertiary = Yellow20,
    tertiaryContainer = Yellow30,
    onTertiaryContainer = Yellow90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = defaultBackgroundDark,
    onBackground = elevatedBackgroundDark,
    surface = myHighlightedText,
    onSurface = Grey80,
    inverseSurface = Grey90,
    inverseOnSurface = Grey20,
    surfaceVariant = othersHighlightedText,
    onSurfaceVariant = BlueGrey80,
    outline = BlueGrey60
)

private val JetchatLightColorScheme = lightColorScheme(
    primary = myMessageBackgroundLight,
    onPrimary = myMessageTextLight,
    primaryContainer = myCodeBackgroundLight,
    onPrimaryContainer = myCodeTextLight,
    inversePrimary = Blue80,
    secondary = othersMessageBackgroundLight,
    onSecondary = othersMessageTextLight,
    secondaryContainer = othersCodeBackgroundLight,
    onSecondaryContainer = othersCodeTextLight,
    tertiary = navigationBackgroundLight,
    onTertiary = Color.White,
    tertiaryContainer = Yellow90,
    onTertiaryContainer = Yellow10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = defaultBackgroundLight,
    onBackground = elevatedBackgroundLight,
    surface = myHighlightedText,
    onSurface = Grey10,
    inverseSurface = Grey20,
    inverseOnSurface = Grey95,
    surfaceVariant = othersHighlightedText,
    onSurfaceVariant = BlueGrey30,
    outline = BlueGrey50
)

@Suppress("NewApi")
@Composable
fun JetchatTheme(
    theme: ThemeMode = isSystemInDarkTheme().toTheme(),
    content: @Composable () -> Unit,
) {
    val myColorScheme = when (theme) {
        ThemeMode.DARK -> JetchatDarkColorScheme
        ThemeMode.LIGHT -> JetchatLightColorScheme
    }

    MaterialTheme(
        colorScheme = myColorScheme,
        typography = JetchatTypography
    ) {
        // TODO (M3): MaterialTheme doesn't provide LocalIndication, remove when it does
        val rippleIndication = rememberRipple()
        CompositionLocalProvider(
            LocalIndication provides rippleIndication,
            content = content
        )
    }
}
