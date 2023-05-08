package composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
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
import data.AdditionalUiState

@Composable
@Suppress("FunctionName")
fun JetchatScaffold(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    uiState: AdditionalUiState,
    onProfileClicked: (String) -> Unit,
    onChatClicked: (String) -> Unit,
    onThemeChange: (Boolean) -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxHeight()
                    .width(300.dp)
            ) {
                JetchatDrawer(
                    onProfileClicked = onProfileClicked,
                    onChatClicked = onChatClicked,
                    onThemeChange = onThemeChange,
                    uiState = uiState
                )
            }
        },
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