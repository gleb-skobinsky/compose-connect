package navigation

class CommonNavigator {
    private val navigationStack = mutableListOf<CommonBackStackEntry>()

    fun popFromStack()
}

data class CommonBackStackEntry(
    val route: String
)