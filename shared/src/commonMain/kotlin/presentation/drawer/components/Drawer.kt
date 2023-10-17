package presentation.drawer.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import data.exampleAccountsState
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import presentation.common.platform.pointerCursor
import presentation.common.resourceBindings.drawable_jetchat_icon_mpp
import presentation.conversation.components.ChirrioIcon
import presentation.conversation.components.LocalScaffold
import presentation.drawer.DrawerViewModel
import presentation.login_screen.components.AuthButton

@Composable
fun AppDrawer(
    onProfileClicked: (String) -> Unit,
    onChatClicked: (String) -> Unit,
    onLogoutClicked: () -> Unit,
    viewModel: DrawerViewModel,
) {
    val scope = rememberCoroutineScope()
    val currentUser by viewModel.user.collectAsState()
    val chats by viewModel.chats.collectAsState()
    val selectedChat by viewModel.chatId.collectAsState()
    val selectedUser by viewModel.userId.collectAsState()
    Box {
        Column {
            Spacer(Modifier.height(3.dp))
            DrawerHeader()
            DividerItem()
            DrawerItemHeader("Chats")
            val scaffoldState = LocalScaffold.current
            chats.entries.forEach { (id, chat) ->
                ChatItem(
                    text = chat,
                    selected = selectedChat == id
                ) {
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                    onChatClicked(id)
                }
            }
            RoomCreationButton(viewModel)
            DividerItem(modifier = Modifier.padding(horizontal = 28.dp))
            DrawerItemHeader("Recent Profiles")
            exampleAccountsState.entries.forEach { (profileId, profile) ->
                ProfileItem(profile.name, profile.photo, profileId == selectedUser) {
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                    onProfileClicked(profileId)
                }
            }
            ThemeSwitch(viewModel)
        }
        Row(
            Modifier
                .shadow(32.dp)
                .background(MaterialTheme.colorScheme.onBackground)
                .padding(24.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(
                text = "Logged in as ${currentUser?.firstName ?: ""} ${currentUser?.lastName ?: ""}",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(12.dp))
            LogoutButton(viewModel, Modifier.weight(1f), additionalAction = onLogoutClicked)
        }
    }
}

@Composable
private fun RoomCreationButton(viewModel: DrawerViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            Modifier
                .size(48.dp)
                .shadow(18.dp, RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(50))
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable {
                    viewModel.openRoomDialog()
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add room",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun LogoutButton(
    viewModel: DrawerViewModel,
    modifier: Modifier = Modifier,
    additionalAction: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val scaffold = LocalScaffold.current
    AuthButton(true, "Log out", modifier) {
        scope.launch {
            scaffold.drawerState.close()
        }
        viewModel.logoutUser()
        additionalAction()
    }
}


@Composable
private fun DrawerHeader() {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        ChirrioIcon(
            contentDescription = "Open navigation drawer",
            modifier = Modifier
                .size(64.dp)
                .padding(16.dp)
        )
    }
}

@Composable
private fun DrawerItemHeader(text: String) {
    Box(
        modifier = Modifier
            .heightIn(min = 52.dp)
            .padding(horizontal = 28.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ChatItem(text: String, selected: Boolean, onChatClicked: () -> Unit) {
    val background = if (selected) {
        Modifier.background(MaterialTheme.colorScheme.tertiary)
    } else {
        Modifier
    }
    Row(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .pointerCursor()
            .clip(CircleShape)
            .then(background)
            .clickable(onClick = onChatClicked),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val iconTint = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
        Icon(
            painter = painterResource(drawable_jetchat_icon_mpp),
            tint = iconTint,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
            contentDescription = null
        )
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ProfileItem(
    text: String,
    profilePic: String?,
    selected: Boolean = false,
    onProfileClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(CircleShape)
            .background(if (selected) MaterialTheme.colorScheme.tertiary else Color.Transparent)
            .pointerCursor()
            .clickable(onClick = onProfileClicked),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val paddingSizeModifier = Modifier
            .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
            .size(24.dp)
        if (profilePic != null) {
            Image(
                painter = painterResource(profilePic),
                modifier = paddingSizeModifier.then(Modifier.clip(CircleShape)),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        } else {
            Spacer(modifier = paddingSizeModifier)
        }
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
fun DividerItem(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    )
}