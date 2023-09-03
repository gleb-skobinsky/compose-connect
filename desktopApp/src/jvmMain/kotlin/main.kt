import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import java.awt.Point

fun main() {
    application {
        val state = WindowState(WindowPlacement.Maximized)
        val composePlacement = remember { mutableStateOf(WindowPlacement.Maximized) }
        Window(
            title = "ComposeConnect",
            onCloseRequest = ::exitApplication,
            undecorated = true,
            state = state,
            transparent = true,
        ) {
            SetAppIcon()
            Surface(
                shape = if (state.placement == WindowPlacement.Maximized) RectangleShape else RoundedCornerShape(10.dp),
                color = Color.Transparent
            ) {
                Column {
                    WindowDraggableArea(
                        Modifier
                            .pointerInput(Unit) {
                                detectDragGestures { _, _ ->
                                    if (state.placement == WindowPlacement.Maximized) {
                                        state.placement = WindowPlacement.Floating
                                        composePlacement.value = WindowPlacement.Floating
                                    }
                                }
                            }
                    ) {
                        ToolingHeader(composePlacement, ::exitApplication)
                    }
                    ChatApplication()
                }
            }
        }
    }
}

operator fun Point.plus(value: Offset) = Point(x + value.x.toInt(), y + value.y.toInt())

private val hover = Color.Gray.copy(alpha = 0.2f)


@Composable
fun FrameWindowScope.ToolingHeader(
    composePlacement: MutableState<WindowPlacement>,
    onWindowClose: () -> Unit,
) {
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
            window.isMinimized = true
        }

        if (composePlacement.value == WindowPlacement.Maximized) {
            ToolingHeaderIcon(
                imageVector = FloatingWindow,
                hoverColor = hover
            ) {
                window.placement = WindowPlacement.Floating
                composePlacement.value = WindowPlacement.Floating
            }
        } else {
            ToolingHeaderIcon(
                imageVector = MaximizeWindow,
                hoverColor = hover,
            ) {
                window.placement = WindowPlacement.Maximized
                composePlacement.value = WindowPlacement.Maximized
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