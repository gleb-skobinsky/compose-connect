import kotlinx.datetime.Clock
import kotlin.test.Test


class InstantTest {
    @Test
    fun printInstant() {
        val now = Clock.System.now()
        println(now.toString())
    }
}