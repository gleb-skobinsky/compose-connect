import androidx.compose.material.icons.materialIcon
import androidx.compose.ui.graphics.vector.ImageVector

val FloatingWindow: ImageVector
    get() {
        if (close != null) {
            return close!!
        }
        close = materialIcon(name = "MaximizeWindow") {
            outlinePath {
                moveTo(6.41f, 6.41f)
                lineTo(6.41f, 19.0f)
                lineTo(19.0f, 19.0f)
                lineTo(19.0f, 6.41f)
                lineTo(6.41f, 6.41f)
                moveTo(10.41f, 6.41f)
                lineTo(10.41f, 2.41f)
                lineTo(23.0f, 2.41f)
                lineTo(23.0f, 15.0f)
                lineTo(19.0f, 15.0f)
                moveTo(6.41f, 6.41f)
                close()
            }
        }
        return close!!
    }

private var close: ImageVector? = null