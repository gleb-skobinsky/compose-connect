package presentation.drawer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import di.provideViewModel
import presentation.drawer.DrawerViewModel
import presentation.login_screen.components.ShowOrHideSnackbar

@Composable
fun AppScaffold(
    scaffoldState: ScaffoldState,
    viewModel: DrawerViewModel = provideViewModel(),
    onProfileClicked: (String) -> Unit,
    onChatClicked: (String) -> Unit,
    onLogoutClicked: () -> Unit,
    hasDrawer: Boolean = true,
    content: @Composable (PaddingValues) -> Unit,
) {
    ShowOrHideSnackbar(viewModel, scaffoldState)
    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = if (hasDrawer) {
            {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxHeight()
                        .width(300.dp)
                ) {
                    AppDrawer(
                        onProfileClicked = onProfileClicked,
                        onChatClicked = onChatClicked,
                        onLogoutClicked = onLogoutClicked,
                        viewModel = viewModel
                    )
                }
            }
        } else null,
        content = content,
        drawerShape = NavShape(300.dp, 0f)
    )
}

class NavShape(
    private val widthOffset: Dp,
    private val scale: Float,
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        return Outline.Rectangle(
            Rect(
                Offset.Zero,
                Offset(
                    size.width * scale + with(density) { widthOffset.toPx() },
                    size.height
                )
            )
        )
    }
}