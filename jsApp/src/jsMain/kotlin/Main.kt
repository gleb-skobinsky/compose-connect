import org.jetbrains.skiko.wasm.onWasmReady
import presentation.conversation.components.ChatApplication


fun main() {
    onWasmReady {
        BrowserViewportWindow("ComposeConnect") {
            ChatApplication()
        }
    }
}

