import androidx.compose.ui.window.ComposeUIViewController
import di.startKoinApp
import navigation.SharedNavigatedApp
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    startKoinApp()
    return ComposeUIViewController { SharedNavigatedApp() }
}