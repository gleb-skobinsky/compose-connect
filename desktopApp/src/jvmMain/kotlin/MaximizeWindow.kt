import androidx.compose.material.icons.materialIcon
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.DefaultFillType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path


val MaximizeWindow: ImageVector
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
                close()
            }
        }
        return close!!
    }

private var close: ImageVector? = null

inline fun ImageVector.Builder.outlinePath(
    fillAlpha: Float = 1f,
    strokeAlpha: Float = 1f,
    pathFillType: PathFillType = DefaultFillType,
    pathBuilder: PathBuilder.() -> Unit,
) =
// TODO: b/146213225
// Some of these defaults are already set when parsing from XML, but do not currently exist
    // when added programmatically. We should unify these and simplify them where possible.
    path(
        fill = null,
        fillAlpha = fillAlpha,
        stroke = SolidColor(Color.Black),
        strokeAlpha = strokeAlpha,
        strokeLineWidth = 2f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Bevel,
        strokeLineMiter = 1f,
        pathFillType = pathFillType,
        pathBuilder = pathBuilder
    )