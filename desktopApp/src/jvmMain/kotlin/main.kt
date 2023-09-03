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
import com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

fun main() = application {
    val windowState = remember { WindowState(WindowPlacement.Maximized) }
    Window(
        title = "ComposeConnect",
        onCloseRequest = ::exitApplication,
        state = windowState,
        undecorated = true,
        resizable = windowState.placement.toResizable()
    ) {
        SetAppIcon()
        Column {
            ToolingHeader(windowState)
            ChatApplication()
        }
    }
}

@Composable
fun ApplicationScope.ToolingHeader(
    windowState: WindowState,
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
            hoverColor = Color.Gray.copy(alpha = 0.2f)
        ) {
            windowState.isMinimized = true
        }
        ToolingHeaderIcon(
            imageVector = Icons.Outlined.Close,
            hoverColor = Color.Red,
            onClick = ::exitApplication
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ToolingHeaderIcon(imageVector: ImageVector, hoverColor: Color, onClick: () -> Unit) {
    var hovered by remember { mutableStateOf(false) }
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        modifier = Modifier
            .clickable(onClick = onClick)
            .onPointerEvent(PointerEventType.Enter) {
                hovered = true
            }
            .onPointerEvent(PointerEventType.Exit) {
                hovered = false
            }
            .background(if (hovered) hoverColor else Color.Transparent)
            .padding(10.dp),
        tint = if (hovered) Color.White else Color.Gray
    )
}

private fun WindowPlacement.toResizable() = when (this) {
    WindowPlacement.Maximized -> false
    WindowPlacement.Floating -> true
    WindowPlacement.Fullscreen -> false
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