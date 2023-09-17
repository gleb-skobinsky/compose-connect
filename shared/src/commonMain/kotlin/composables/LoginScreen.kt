package composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.LoginScreenState
import data.MainViewModel

@Composable
fun AuthScreen(viewModel: MainViewModel) {
    val screenMode by viewModel.loginScreenMode.collectAsState()
    Scaffold(
        floatingActionButton = {
            Surface(
                modifier = Modifier
                    .requiredSize(120.dp, 48.dp)
                    .pointerHoverIcon(PointerIcon.Hand)
                    .clickable {
                        viewModel.switchTheme(!viewModel.themeMode.value)
                    },
                shape = RoundedCornerShape(50),
                elevation = 18.dp,
                color = MaterialTheme.colorScheme.secondary
            ) {
                ThemeSwitch(viewModel)
            }
        }
    ) {
        Box(
            Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (screenMode) {
                LoginScreenState.LOGIN -> LoginScreen(viewModel)
                LoginScreenState.REGISTER -> SignupScreen(viewModel)
            }
        }
    }
}

@Composable
fun BoxScope.SignupScreen(viewModel: MainViewModel) {
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    val signupButtonEnabled = email.isNotBlank() && firstName.isNotBlank() && lastName.isNotBlank()

    Column(
        modifier = Modifier.align(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginHeaderText("Register")
        LoginTextField(
            label = "Email:",
            value = email,
            onValueChange = { email = it },
        )
        LoginTextField(
            label = "First name:",
            value = firstName,
            onValueChange = { firstName = it }
        )
        LoginTextField(
            label = "Last name:",
            value = lastName,
            onValueChange = { lastName = it }
        )
        AuthButton(
            enabled = signupButtonEnabled,
            text = "Sign up"
        ) {

        }
        Row(Modifier.padding(top = 32.dp)) {
            SecondaryLoginText("Already have an account?", Modifier.padding(end = 20.dp))
            SecondaryLoginText("Log in", Modifier.clickable { viewModel.setLoginMode(LoginScreenState.LOGIN) })
        }
    }
}

@Composable
fun BoxScope.LoginScreen(viewModel: MainViewModel) {
    var email by remember { mutableStateOf("glebgytnik@gmail.com") }
    var password by remember { mutableStateOf("LiuRuis5968!") }
    val loginButtonEnabled = email.isNotBlank() && password.isNotBlank()
    Column(
        modifier = Modifier.align(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginHeaderText("Please log in to start messaging")
        LoginTextField(
            label = "Email:",
            value = email,
            onValueChange = { email = it },
        )
        LoginTextField(
            label = "Password:",
            value = password,
            onValueChange = { password = it },
        )
        AuthButton(
            enabled = loginButtonEnabled,
            text = "Log in"
        ) {
            viewModel.loginUser(email, password)
        }
        Row(Modifier.padding(top = 32.dp)) {
            SecondaryLoginText("Don't have an account?", Modifier.padding(end = 20.dp))
            SecondaryLoginText("Register", Modifier.clickable { viewModel.setLoginMode(LoginScreenState.REGISTER) })
        }
    }
}

@Composable
fun LoginTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) = Column {
    SecondaryLoginText(
        text = label,
        modifier = Modifier.padding(top = 12.dp, bottom = 6.dp)
    )
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .padding(bottom = 24.dp)
            .width(300.dp)
            .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(12.dp))
            .padding(6.dp)
    )
}

@Composable
fun SecondaryLoginText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier
    )
}

@Composable
fun AuthButton(
    enabled: Boolean,
    text: String,
    modifier: Modifier = Modifier,
    action: () -> Unit,
) {
    Button(
        enabled = enabled,
        modifier = modifier.pointerHoverIcon(PointerIcon.Hand),
        onClick = action,
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
    ) {
        Text(text, color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Composable
fun LoginHeaderText(
    text: String,
) = Text(
    text = text,
    fontSize = 56.sp,
    color = MaterialTheme.colorScheme.primary,
    textAlign = TextAlign.Center,
    modifier = Modifier.padding(bottom = 32.dp)
)