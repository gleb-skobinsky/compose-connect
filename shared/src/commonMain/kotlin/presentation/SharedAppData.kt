package presentation

import common.Resource
import common.viewmodel.IODispatcher
import data.repository.RemoteUserRepository
import domain.model.User
import domain.use_case.users.refreshTokenUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import presentation.common.themes.ThemeMode
import kotlin.time.Duration.Companion.minutes

interface SharedAppData {

    val user: StateFlow<User?>

    fun setUser(user: User?)

    val errorMessage: StateFlow<String?>

    fun setErrorMessage(message: String?)

    val theme: StateFlow<ThemeMode>

    fun switchTheme(theme: ThemeMode)
}

class SharedAppDataImpl : SharedAppData {
    private val credentialsUpdaterScope = CoroutineScope(SupervisorJob() + IODispatcher)

    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    override val user = _user.asStateFlow()
    override fun setUser(user: User?) {
        _user.value = user
    }

    private val _errorMessage = MutableStateFlow<String?>(null)
    override val errorMessage = _errorMessage.asStateFlow()
    override fun setErrorMessage(message: String?) {
        _errorMessage.value = message
    }

    private val _themeMode: MutableStateFlow<ThemeMode> = MutableStateFlow(ThemeMode.DARK)
    override val theme: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    override fun switchTheme(theme: ThemeMode) {
        _themeMode.value = theme
    }

    init {
        user.filterNotNull().onEach {
            println("Starting refresh updater")
            delay(8.minutes)
            println("8 minutes passed")
            when (val result = refreshTokenUseCase(RemoteUserRepository, it)) {
                is Resource.Data -> {
                    val (refresh, access) = result.payload
                    _user.value?.accessToken = access
                    _user.value?.refreshToken = refresh
                }
                is Resource.Error -> {
                    setErrorMessage("Could not confirm user session. Try to log in again.")
                }
                is Resource.Loading -> Unit
            }
        }.launchIn(credentialsUpdaterScope)
    }
}