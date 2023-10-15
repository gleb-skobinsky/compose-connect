package di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import common.viewmodel.ViewModelPlatformImpl

@Composable
actual inline fun <reified T: ViewModelPlatformImpl> provideViewModel() = viewModel<T>()