import androidx.compose.ui.window.ComposeUIViewController
import di.startKoinApp
import platform.UIKit.UIViewController
import presentation.conversation.components.ThemeWrapper

fun MainViewController(): UIViewController {
    startKoinApp()
    return ComposeUIViewController { ThemeWrapper() }
}