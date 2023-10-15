package di

import androidx.compose.runtime.Composable

@Composable
expect inline fun <reified T> provideViewModel(): T