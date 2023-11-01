package presentation.common.resourceBindings

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

object Drawables {
    lateinit var jetchat_icon_mpp: Painter
        private set

    lateinit var user_icon: Painter
        private set

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun load() {
        jetchat_icon_mpp = painterResource("jetchat_icon_mpp.png")
        user_icon = painterResource("user_icon.xml")
    }
}
