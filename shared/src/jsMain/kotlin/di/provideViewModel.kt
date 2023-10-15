package di

import androidx.compose.runtime.Composable
import common.viewmodel.ViewModelPlatformImpl

@Composable
actual inline fun <reified T: ViewModelPlatformImpl> provideViewModel(): T = getKoinInstance()