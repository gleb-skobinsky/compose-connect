package composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import data.MainViewModel

@Composable
fun RoomCreationDialog(viewModel: MainViewModel) {
    val dialogOpen by viewModel.plusRoomDialogOpen.collectAsState()
    var roomName by remember { mutableStateOf("") }
    if (dialogOpen) {
        Dialog(
            onDismissRequest = { viewModel.closeRoomDialog() }
        ) {
            Column(
                Modifier
                    .padding(32.dp)
                    .defaultMinSize(600.dp, 300.dp)
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
                BasicTextField(
                    value = roomName,
                    onValueChange = {
                        roomName = it
                    },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                        .width(300.dp)
                        .height(42.dp)
                        .padding(8.dp),
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    maxLines = 1,
                    singleLine = true,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurfaceVariant)
                )
                RoomFormSpacer()
                Button({
                    viewModel.createRoom(roomName)
                }) {
                    Text("Create")
                }
            }
        }
    }
}

@Composable
private fun RoomFormSpacer() = Spacer(Modifier.height(24.dp))