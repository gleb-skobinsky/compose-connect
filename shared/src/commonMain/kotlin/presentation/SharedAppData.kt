package presentation

import common.Resource
import common.viewmodel.IODispatcher
import common.viewmodel.ViewModelPlatformImpl
import data.repository.RemoteUserRepository
import domain.model.User
import domain.use_case.users.refreshTokenUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import presentation.common.themes.ThemeMode
import kotlin.time.Duration.Companion.minutes

interface SharedAppData {

    val user: StateFlow<User?>

    fun setUser(user: User?)

    val errorMessage: StateFlow<String?>

    fun setErrorMessage(message: String?)

    fun withSuccess(block: () -> Unit) {
        setErrorMessage(null)
        block()
    }

    val theme: StateFlow<ThemeMode>

    fun switchTheme(theme: ThemeMode)
}

class SharedAppDataImpl : SharedAppData, ViewModelPlatformImpl() {

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
        user.onEach {
            if (it != null) {
                while (true) {
                    yield()
                    withContext(IODispatcher) {
                        delay(8.minutes)
                        when (val result = refreshTokenUseCase(RemoteUserRepository, it)) {
                            is Resource.Data -> withSuccess {
                                val (refresh, access) = result.payload
                                _user.value?.accessToken = access
                                _user.value?.refreshToken = refresh
                            }

                            is Resource.Error -> {
                                setErrorMessage("Could not confirm user session. Try to log in again.")
                            }

                            is Resource.Loading -> Unit
                        }
                    }
                }
            }
        }.launchIn(vmScope)
    }
}