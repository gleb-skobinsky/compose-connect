import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*

fun main() {
    application {
        Window(
            title = "ComposeConnect",
            onCloseRequest = ::exitApplication,
            undecorated = true,
            state = WindowState(WindowPlacement.Maximized)
        ) {
            SetAppIcon()
            Column {
                with(window) {
                    ToolingHeader(::exitApplication)
                }
                ChatApplication()
            }
        }
    }
}

private val hover = Color.Gray.copy(alpha = 0.2f)

@Composable
fun ComposeWindow.ToolingHeader(
    onWindowClose: () -> Unit,
) {
    var composePlacement by remember { mutableStateOf(WindowPlacement.Maximized) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(42.dp)
            .background(Color(39, 39, 42)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        ToolingHeaderIcon(
            imageVector = MinimizeWindow,
            hoverColor = hover,
        ) {
            isMinimized = true
        }

        if (composePlacement == WindowPlacement.Maximized) {
            ToolingHeaderIcon(
                imageVector = FloatingWindow,
                hoverColor = hover
            ) {
                placement = WindowPlacement.Floating
                composePlacement = WindowPlacement.Floating
            }
        } else {
            ToolingHeaderIcon(
                imageVector = MaximizeWindow,
                hoverColor = hover,
            ) {
                placement = WindowPlacement.Maximized
                composePlacement = WindowPlacement.Maximized
            }
        }
        ToolingHeaderIcon(
            imageVector = Icons.Outlined.Close,
            hoverColor = Color.Red,
            onClick = onWindowClose
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ToolingHeaderIcon(
    imageVector: ImageVector,
    hoverColor: Color,
    hovered: MutableState<Boolean> = remember { mutableStateOf(false) },
    onClick: () -> Unit,
) {
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        modifier = Modifier
            .clickable(onClick = onClick)
            .onPointerEvent(PointerEventType.Enter) {
                hovered.value = true
            }
            .onPointerEvent(PointerEventType.Exit) {
                hovered.value = false
            }
            .background(if (hovered.value) hoverColor else Color.Transparent)
            .padding(12.dp),
        tint = if (hovered.value) Color.White else Color.Gray
    )
}

private fun WindowPlacement.toResizable() = when (this) {
    WindowPlacement.Maximized -> false
    WindowPlacement.Floating -> true
    WindowPlacement.Fullscreen -> false
}

fun WindowPlacement.toIcon() = when (this) {
    WindowPlacement.Maximized -> FloatingWindow
    WindowPlacement.Floating -> MaximizeWindow
    WindowPlacement.Fullscreen -> FloatingWindow
}

@Composable
fun FrameWindowScope.SetAppIcon() {
    val density = LocalDensity.current
    val iconPainter = BitmapPainter(
        useResource(
            resourcePath = "jetchat_icon.png",
            block = ::loadImageBitmap
        )
    )
    SideEffect {
        window.iconImage = iconPainter.toAwtImage(
            density = density,
            layoutDirection = LayoutDirection.Ltr,
            size = Size(128f, 128f)
        )
    }
}