package composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.LoginScreenState
import data.MainViewModel

@Composable
fun AuthScreen(viewModel: MainViewModel) {
    val screenMode by viewModel.loginScreenMode.collectAsState()
    val scaffoldState = rememberScaffoldState()
    ShowOrHideSnackbar(viewModel, scaffoldState)
    Scaffold(
        scaffoldState = scaffoldState,
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
fun ShowOrHideSnackbar(viewModel: MainViewModel, scaffoldState: ScaffoldState) {
    val error by viewModel.errorMessage.collectAsState()
    LaunchedEffect(error) {
        error?.let {
            scaffoldState.snackbarHostState.showSnackbar(
                message = "${it.message} Http status: ${it.status.value} - ${it.status.description}",
                actionLabel = null,
                duration = SnackbarDuration.Short
            )
        }
    }
}

@Composable
fun BoxScope.SignupScreen(viewModel: MainViewModel) {
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val signupButtonEnabled = email.isNotBlank() &&
            firstName.isNotBlank() &&
            lastName.isNotBlank() &&
            password.isNotBlank() &&
            confirmPassword.isNotBlank() &&
            password == confirmPassword
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
        LoginTextField(
            label = "Password:",
            value = password,
            isPassword = true,
            onValueChange = { password = it }
        )
        LoginTextField(
            label = "Confirm password:",
            value = confirmPassword,
            isPassword = true,
            onValueChange = { confirmPassword = it }
        )
        AuthButton(
            enabled = signupButtonEnabled,
            text = "Sign up"
        ) {
            viewModel.signupUser(email, firstName, lastName, password)
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
            isPassword = true
        ) { password = it }
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
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit,
) = Column {
    var passwordVisible by remember { mutableStateOf(false) }
    SecondaryLoginText(
        text = label,
        modifier = Modifier.padding(top = 12.dp, bottom = 6.dp)
    )
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        decorationBox = { innerTextField ->
            if (isPassword) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = "Show password",
                        tint = Color.Black,
                        modifier = Modifier
                            .pointerHoverIcon(PointerIcon.Hand)
                            .clickable { passwordVisible = !passwordVisible }
                    )
                }

            } else Unit
            innerTextField()
        },
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