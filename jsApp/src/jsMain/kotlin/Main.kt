import org.jetbrains.skiko.wasm.onWasmReady
import presentation.composables.ChatApplication


fun main() {
    onWasmReady {
        BrowserViewportWindow("ComposeConnect") {
            ChatApplication()
        }
    }
}

