package presentation.conversation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import di.provideViewModel
import domain.model.User
import presentation.SharedAppDataImpl
import presentation.common.themes.ChirrioAppTheme
import presentation.login_screen.components.AuthScreen

private const val DURATION_MILLIS = 1000


@Composable
fun ChatApplication(
    sharedViewModel: SharedAppDataImpl = provideViewModel()
) {
    Column(
        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        val user by sharedViewModel.user.collectAsState()
        ChirrioAppTheme {
            Column {
                AnimatedContent(
                    targetState = user != User.Empty,
                    transitionSpec = {
                        if (targetState != initialState && targetState) {
                            slideInVertically(tween(DURATION_MILLIS)) { it } togetherWith slideOutVertically(tween(
                                DURATION_MILLIS
                            )) { -it }
                        } else {
                            slideInVertically(tween(DURATION_MILLIS)) { -it } togetherWith slideOutVertically(tween(
                                DURATION_MILLIS
                            )) { it }
                        }.using(
                            SizeTransform(clip = true)
                        )
                    }
                ) { loggedIn ->
                    if (loggedIn) MainBody() else AuthScreen()
                }
            }
        }
    }
}