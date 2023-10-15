package di

import androidx.compose.runtime.Composable
import common.viewmodel.ViewModelPlatformImpl
import org.koin.compose.koinInject

@Composable
actual inline fun <reified T : ViewModelPlatformImpl> provideViewModel(): T = koinInject()