package presentation.profile.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import data.ProfileScreenState
import di.provideViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import presentation.FunctionalityNotAvailablePopup
import presentation.common.platform.statusBarsPaddingMpp
import presentation.conversation.components.JetchatAppBar
import presentation.conversation.components.baselineHeight
import presentation.drawer.DrawerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    drawerViewModel: DrawerViewModel = provideViewModel()
) {
    val selectedUser by drawerViewModel.selectedUserProfile.collectAsState()
    selectedUser?.let { userData ->
        var functionalityNotAvailablePopupShown by remember { mutableStateOf(false) }
        if (functionalityNotAvailablePopupShown) {
            FunctionalityNotAvailablePopup { functionalityNotAvailablePopupShown = false }
        }

        val scrollState = rememberScrollState()
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            JetchatAppBar(
                // Use statusBarsPadding() to move the app bar content below the status bar
                modifier = Modifier.statusBarsPaddingMpp(),
                scrollBehavior = scrollBehavior,
                title = { },
                actions = {
                    // More icon
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .clickable(onClick = { functionalityNotAvailablePopupShown = true })
                            .padding(horizontal = 12.dp, vertical = 16.dp)
                            .height(24.dp),
                        contentDescription = "More options"
                    )
                }
            )
            BoxWithConstraints(modifier = Modifier.weight(1f)) {
                Surface {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .verticalScroll(scrollState),
                    ) {
                        ProfileHeader(
                            scrollState,
                            userData,
                            this@BoxWithConstraints.maxHeight
                        )
                        UserInfoFields(userData, this@BoxWithConstraints.maxHeight)
                    }
                }
                ProfileFab(
                    userIsMe = userData.isMe(),
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) { functionalityNotAvailablePopupShown = true }
            }
        }
    }
}

@Composable
private fun UserInfoFields(userData: ProfileScreenState, containerHeight: Dp) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))

        NameAndPosition(userData)

        ProfileProperty("Display name", userData.displayName)

        ProfileProperty("Status", userData.status)

        ProfileProperty("Twitter", userData.twitter, isLink = true)

        userData.timeZone?.let {
            ProfileProperty("Timezone", userData.timeZone)
        }

        // Add a spacer that always shows part (320.dp) of the fields list regardless of the device,
        // in order to always leave some content at the top.
        Spacer(Modifier.height((containerHeight - 320.dp).coerceAtLeast(0.dp)))
    }
}

@Composable
private fun NameAndPosition(
    userData: ProfileScreenState,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Name(
            userData,
            modifier = Modifier.baselineHeight(32.dp)
        )
        Position(
            userData,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .baselineHeight(24.dp)
        )
    }
}

@Composable
private fun Name(userData: ProfileScreenState, modifier: Modifier = Modifier) {
    Text(
        text = userData.name,
        modifier = modifier,
        style = MaterialTheme.typography.headlineSmall
    )
}

@Composable
private fun Position(userData: ProfileScreenState, modifier: Modifier = Modifier) {
    Text(
        text = userData.position,
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ProfileHeader(
    scrollState: ScrollState,
    data: ProfileScreenState,
    containerHeight: Dp,
) {
    val offset = (scrollState.value / 2) + 32
    val offsetDp = with(LocalDensity.current) { offset.toDp() }
    val imageHeight = containerHeight / 2
    data.photo?.let {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .height(imageHeight)
                    .width(imageHeight)
                    .padding(
                        start = 16.dp,
                        top = offsetDp,
                        end = 16.dp
                    )
                    .clip(CircleShape),
                painter = painterResource(it),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }
    }
}

@Composable
fun ProfileProperty(label: String, value: String, isLink: Boolean = false) {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
        Divider()
        Text(
            text = label,
            modifier = Modifier.baselineHeight(24.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        val style = if (isLink) {
            MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
        } else {
            MaterialTheme.typography.bodyLarge
        }
        Text(
            text = value,
            modifier = Modifier.baselineHeight(24.dp),
            style = style
        )
    }
}

@Composable
fun ProfileFab(
    userIsMe: Boolean,
    modifier: Modifier = Modifier,
    onFabClicked: () -> Unit = { },
) {
    key(userIsMe) { // Prevent multiple invocations to execute during composition
        FloatingActionButton(
            onClick = onFabClicked,
            modifier = modifier
                .padding(16.dp)
                // .navigationBarsPadding()
                .height(48.dp)
                .widthIn(min = 48.dp),
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ) {
            Row {
                Icon(
                    imageVector = if (userIsMe) Icons.Outlined.Create else Icons.Outlined.Chat,
                    contentDescription = if (userIsMe) "Edit profile" else "Message"
                )
                Text(
                    text = if (userIsMe) "Edit profile" else "Message",
                )
            }
        }
    }
}