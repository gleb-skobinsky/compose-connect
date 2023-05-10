import androidx.compose.ui.window.Window
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.skiko.wasm.onWasmReady
import org.w3c.dom.HTMLCanvasElement


fun main() {
    resizeCanvas()
    onWasmReady {
        Window {
            Application()
        }
    }
}

private fun resizeCanvas() {
    val wasmCanvas = document.getElementById("ComposeTarget") as HTMLCanvasElement
    wasmCanvas.width = document.body?.clientWidth ?: window.innerWidth
    wasmCanvas.height = document.body?.scrollHeight ?: window.innerHeight
}
