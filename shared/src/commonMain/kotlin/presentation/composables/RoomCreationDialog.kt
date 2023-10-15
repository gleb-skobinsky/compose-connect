package presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import presentation.drawer.DrawerViewModel

@Composable
fun RoomCreationDialog(viewModel: DrawerViewModel) {
    val dialogOpen by viewModel.plusRoomDialogOpen.collectAsState()
    val searchedUsers by viewModel.searchedUsers.collectAsState()
    val selectedUsers by viewModel.selectedUsers.collectAsState()
    var roomName by remember { mutableStateOf("") }
    var searchedUser by remember { mutableStateOf("") }
    if (dialogOpen) {
        Dialog(
            onDismissRequest = { viewModel.closeRoomDialog() }
        ) {
            Column(
                Modifier
                    .padding(32.dp)
                    .defaultMinSize(600.dp, 500.dp)
                    .background(MaterialTheme.colorScheme.onBackground, RoundedCornerShape(12.dp))
                    .padding(24.dp)
            ) {
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Create new chat",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 20.sp
                    )
                }
                RoomFormSpacer()
                RoomCreationLabel("Enter room name:")
                RoomCreationField(roomName) {
                    roomName = it
                }
                RoomFormSpacer()
                RoomCreationLabel("Select chat participants by emails:")
                RoomCreationField(searchedUser) {
                    searchedUser = it
                    if (searchedUser.isNotBlank()) {
                        viewModel.searchUsers(searchedUser)
                    } else {
                        viewModel.clearSearch()
                    }
                }
                RoomFormSpacer()
                LazyColumn(modifier = Modifier.defaultMinSize(500.dp, 200.dp)) {
                    items(count = searchedUsers.size) { index ->
                        val user = searchedUsers[index]
                        val selected = user.email in selectedUsers
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(checked = selected, onCheckedChange = {
                                if (it) viewModel.selectUser(user.email) else viewModel.unselectUser(user.email)
                            })
                            Text(
                                text = user.email,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 24.dp)
                            )
                        }
                    }
                }
                RoomFormSpacer()
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    Button({
                        viewModel.createRoom(roomName)
                    }) {
                        Text("Create")
                    }
                }
            }
        }
    }
}

@Composable
private fun RoomCreationField(roomName: String, onValueChange: (String) -> Unit) {
    BasicTextField(
        value = roomName,
        onValueChange = onValueChange,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.tertiary, CircleShape)
            .width(300.dp)
            .height(42.dp)
            .padding(12.dp),
        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant),
        maxLines = 1,
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurfaceVariant),
    )
}

@Composable
private fun RoomCreationLabel(text: String) {
    Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 12.dp, bottom = 12.dp)
        )
    }
}

@Composable
private fun RoomFormSpacer() = Spacer(Modifier.height(24.dp))