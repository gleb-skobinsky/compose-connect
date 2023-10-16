package navigation

sealed class Screens(open val name: String) {
    abstract fun toRoute(): String
    data class Login(override val name: String = "login") : Screens(name) {
        override fun toRoute() = name
    }

    data class Signup(override val name: String = "signup") : Screens(name) {
        override fun toRoute() = name
    }

    data class Chat(override val name: String = "chat", val id: String) : Screens(name) {
        override fun toRoute() = "$name/$id"
    }

    data class Profile(override val name: String, val id: String) : Screens(name) {
        override fun toRoute(): String = "$name/$id"
    }

    data class Main(override val name: String) : Screens(name) {
        override fun toRoute(): String = name
    }
}