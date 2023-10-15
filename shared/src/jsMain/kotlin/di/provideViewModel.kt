package di

import androidx.compose.runtime.Composable

@Composable
actual inline fun <reified T> provideViewModel(): T = getKoinInstance()