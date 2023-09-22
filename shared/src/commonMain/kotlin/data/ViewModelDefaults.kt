package data

object ViewModelDefaults {
    inline operator fun <reified T : Any?> invoke(): T {
        return when (T::class) {
            LoginScreenState::class -> LoginScreenState.LOGIN as T
            Resource.Error::class -> null as T
            AppScreenState::class -> AppScreenState.CHAT as T
            Map::class -> emptyMap<String, ConversationUiState>() as T
            ConversationUiState::class -> ConversationUiState.Empty as T
            ProfileScreenState::class -> null as T
            List::class -> emptyList<User>() as T
            Set::class -> emptySet<String>() as T
            else -> null as T
        }
    }
}