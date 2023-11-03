package presentation.common.typography

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun MontserratFontFamily(): FontFamily {
    return LoadingFontFamily(
        fontResources("montserrat_light.ttf", FontWeight.Light),
        fontResources("montserrat_medium.ttf", FontWeight.Medium),
        fontResources("montserrat_regular.ttf", FontWeight.W500),
        fontResources("montserrat_semibold.ttf", FontWeight.SemiBold)
    )
}

@Composable
fun KarlaFontFamily(): FontFamily {
    return LoadingFontFamily(
        fontResources("karla_bold.ttf", FontWeight.Bold),
        fontResources("karla_regular.ttf", FontWeight.W500)
    )
}

@Composable
fun EmojisFontFamily(): FontFamily {
    return LoadingFontFamily(
        fontResources("noto_bw.ttf", FontWeight.W500)
    )
}

@Composable
fun LoadingFontFamily(vararg font: Font?): FontFamily {
    var family: FontFamily by remember { mutableStateOf(FontFamily.Default) }
    val fonts = font.asList().filterNotNull()
    if (fonts.isNotEmpty()) {
        family = FontFamily(fonts)
    }
    return family
}

@Composable
expect fun fontResources(
    font: String,
    weight: FontWeight,
    style: FontStyle = FontStyle.Normal
): Font?

@Composable
fun ChirrioTypography(): Typography {
    val montserrat = MontserratFontFamily()
    val karla = KarlaFontFamily()
    val emojis = EmojisFontFamily()
    return Typography(
        displayLarge = TextStyle(
            fontFamily = montserrat,
            fontWeight = FontWeight.Light,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            letterSpacing = 0.sp
        ),
        displayMedium = TextStyle(
            fontFamily = montserrat,
            fontWeight = FontWeight.Light,
            fontSize = 45.sp,
            lineHeight = 52.sp,
            letterSpacing = 0.sp
        ),
        displaySmall = TextStyle(
            fontFamily = montserrat,
            fontWeight = FontWeight.Normal,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            letterSpacing = 0.sp
        ),
        headlineLarge = TextStyle(
            fontFamily = montserrat,
            fontWeight = FontWeight.SemiBold,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = 0.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = montserrat,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = montserrat,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp
        ),
        titleLarge = TextStyle(
            fontFamily = montserrat,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        ),
        titleMedium = TextStyle(
            fontFamily = montserrat,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp
        ),
        titleSmall = TextStyle(
            fontFamily = emojis,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.15.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = karla,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = montserrat,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp
        ),
        bodySmall = TextStyle(
            fontFamily = karla,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp
        ),
        labelLarge = TextStyle(
            fontFamily = montserrat,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),
        labelMedium = TextStyle(
            fontFamily = montserrat,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        ),
        labelSmall = TextStyle(
            fontFamily = montserrat,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
    )
}
