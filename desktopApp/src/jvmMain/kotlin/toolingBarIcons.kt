import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector


val MinimizeWindow: ImageVector
    get() {
        if (_close != null) {
            return _close!!
        }
        _close = materialIcon(name = "Outlined.Close") {
            materialPath {
                moveTo(6.41f, 10.59f)
                lineTo(6.41f, 12.97f)
                lineTo(19.0f, 12.97f)
                lineTo(19.0f, 10.59f)
                lineTo(6.41f, 10.59f)
                close()
            }
        }
        return _close!!
    }

private var _close: ImageVector? = null
