import common.util.loadImage
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class TestImageStorage {

    @Test
    fun imageLoadTest() = runTest {
        launch {
            loadImage()
        }
    }
}