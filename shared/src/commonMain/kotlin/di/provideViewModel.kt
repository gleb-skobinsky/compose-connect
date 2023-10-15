package di

import androidx.compose.runtime.Composable
import common.viewmodel.ViewModelPlatformImpl

@Composable
expect inline fun <reified T: ViewModelPlatformImpl> provideViewModel(): T