package composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.MainViewModel
import data.User

@Composable
fun LoginScreen(viewModel: MainViewModel) {
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    val loginButtonEnabled = email.isNotBlank() && firstName.isNotBlank() && lastName.isNotBlank()
    Box(
        Modifier.fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    0f to Color(26, 73, 255),
                    1f to Color(255, 65, 252)
                )
            )
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginHeaderText("Please log in to start messaging")
            LoginTextField(
                value = email,
                onValueChange = { email = it },
            )
            LoginTextField(
                value = firstName,
                onValueChange = { firstName = it }
            )
            LoginTextField(
                value = lastName,
                onValueChange = { lastName = it }
            )
            Button(
                enabled = loginButtonEnabled,
                onClick = {
                    viewModel.setUser(
                        User(
                            email = email,
                            firstName = firstName,
                            lastName = lastName
                        )
                    )
                }
            ) {
                Text("Log in", color = Color.White)
            }
        }
    }
}

@Composable
fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
) = BasicTextField(
    value = value,
    onValueChange = onValueChange,
    modifier = Modifier
        .padding(bottom = 24.dp)
        .width(300.dp)
        .background(Color(252, 211, 172), RoundedCornerShape(12.dp))
        .padding(6.dp)
)

@Composable
fun LoginHeaderText(
    text: String,
) = Text(
    text = text,
    fontSize = 56.sp,
    color = Color.White,
    textAlign = TextAlign.Center,
    modifier = Modifier.padding(bottom = 32.dp)
)