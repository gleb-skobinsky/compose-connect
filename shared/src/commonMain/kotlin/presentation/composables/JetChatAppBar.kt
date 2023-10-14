package presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import presentation.platform.pointerCursor
import presentation.resourceBindings.drawable_jetchat_icon_mpp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JetchatAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavIconPressed: () -> Unit = { },
    title: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val foregroundColors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = Color.Transparent,
        scrolledContainerColor = Color.Transparent
    )
    Box(modifier = Modifier.background(backgroundColor)) {
        CenterAlignedTopAppBar(
            modifier = modifier,
            actions = actions,
            title = title,
            scrollBehavior = scrollBehavior,
            colors = foregroundColors,
            navigationIcon = {
                JetchatIcon(
                    contentDescription = "Open navigation drawer",
                    modifier = Modifier
                        .size(64.dp)
                        .clickable(onClick = onNavIconPressed)
                        .pointerCursor()
                        .padding(16.dp)
                )
            }
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun JetchatIcon(
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    val semantics = if (contentDescription != null) {
        Modifier.semantics {
            this.contentDescription = contentDescription
            this.role = Role.Image
        }
    } else {
        Modifier
    }
    Box(modifier = modifier.then(semantics)) {
        Image(
            painter = painterResource(drawable_jetchat_icon_mpp),
            contentDescription = null,
            // tint = MaterialTheme.colorScheme.primaryContainer
        )
    }
}