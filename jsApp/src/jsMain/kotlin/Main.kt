import di.startKoinApp
import presentation.conversation.components.ChatApplication
import org.jetbrains.skiko.wasm.onWasmReady


fun main() {
    startKoinApp()
    onWasmReady {
        BrowserViewportWindow("ComposeConnect") {
            ChatApplication()
        }
    }
}

