package presentation.login_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.model.LoginScreenState
import presentation.SharedViewModel
import presentation.composables.ThemeSwitch
import presentation.login_screen.LoginViewModel

@Composable
fun AuthScreen(viewModel: LoginViewModel) {
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
                        viewModel.switchTheme(!viewModel.theme.value)
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
fun ShowOrHideSnackbar(viewModel: SharedViewModel, scaffoldState: ScaffoldState) {
    val error by viewModel.errorMessage.collectAsState()
    LaunchedEffect(error) {
        error?.let {
            scaffoldState.snackbarHostState.showSnackbar(
                message = it,
                actionLabel = null,
                duration = SnackbarDuration.Short
            )
        }
    }
}

@Composable
fun BoxScope.SignupScreen(viewModel: LoginViewModel) {
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
            ClickableSecondaryLoginText("Log in") { viewModel.setLoginMode(LoginScreenState.LOGIN) }
        }
    }
}

@Composable
fun BoxScope.LoginScreen(viewModel: LoginViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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
            ClickableSecondaryLoginText("Register") { viewModel.setLoginMode(LoginScreenState.REGISTER) }
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
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
            .height(32.dp)
            .background(MaterialTheme.colorScheme.tertiary, CircleShape)
            .padding(8.dp),
        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurfaceVariant)
    )
}

@Composable
fun ClickableSecondaryLoginText(
    text: String,
    modifier: Modifier = Modifier,
    onCLick: () -> Unit,
) = SecondaryLoginText(
    text = text,
    modifier = modifier
        .clickable {
            onCLick()
        }
        .pointerHoverIcon(PointerIcon.Hand)
)


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
        Text(text, color = if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
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