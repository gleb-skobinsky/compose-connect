
import com.chirrio.filepicker.calculateDownscale
import kotlin.test.Test
import kotlin.test.assertEquals


class TestDownScale {
    @Test
    fun test9000() {
        val initialW = 5000
        val initialH = 3000
        val (newW, newH) = calculateDownscale(initialW, initialH)
        assertEquals(1000, newW)
        assertEquals(600, newH)
    }
}