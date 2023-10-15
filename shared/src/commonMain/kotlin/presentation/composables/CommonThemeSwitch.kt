package presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Switch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import presentation.SharedViewModel
import presentation.themes.toTheme

@Composable
fun ThemeSwitch(viewModel: SharedViewModel) {
    Box(
        Modifier
            .defaultMinSize(300.dp, 48.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .clip(CircleShape)
        ) {

            val checkedState by viewModel.theme.collectAsState()
            val iconColor = MaterialTheme.colorScheme.onSecondary
            val commonModifier = Modifier.align(Alignment.CenterVertically)
            Icon(
                imageVector = Icons.Outlined.LightMode,
                contentDescription = "Light theme",
                modifier = commonModifier,
                tint = iconColor
            )
            Switch(
                checked = checkedState(),
                onCheckedChange = {
                    viewModel.switchTheme(it.toTheme())
                },
                modifier = commonModifier
            )
            Icon(
                imageVector = Icons.Outlined.DarkMode,
                contentDescription = "Dark theme",
                modifier = commonModifier,
                tint = iconColor
            )
        }
    }
}