package di

import androidx.compose.runtime.Composable
import org.koin.compose.koinInject

@Composable
actual inline fun <reified T> provideViewModel(): T = koinInject()