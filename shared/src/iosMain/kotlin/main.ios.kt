import androidx.compose.ui.window.ComposeUIViewController
import di.startKoinApp
import platform.UIKit.UIViewController
import presentation.conversation.components.ChatApplication

fun MainViewController(): UIViewController {
    startKoinApp()
    return ComposeUIViewController { ChatApplication() }
}