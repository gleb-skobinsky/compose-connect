package presentation.conversation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chirrio.filepicker.PhotoPicker
import com.chirrio.filepicker.localContext
import di.provideViewModel
import presentation.FunctionalityNotAvailablePopup
import presentation.common.messagesParser.emojiFontFamily
import presentation.common.platform.pointerCursor
import presentation.common.platform.textCursor
import presentation.conversation.ConversationViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserInput(
    viewModel: ConversationViewModel = provideViewModel(),
    onMessageSent: (String) -> Unit,
    modifier: Modifier = Modifier,
    resetScroll: () -> Unit = {},
) {
    val currentInputSelector by viewModel.inputSelector.collectAsState()
    val dismissKeyboard = { viewModel.setInputSelector(InputSelector.NONE) }

    // Intercept back navigation if there's a InputSelector visible
//    if (currentInputSelector != InputSelector.NONE) {
//        BackPressHandler(onBackPressed = dismissKeyboard)
//    }

    var textState by rememberSaveable { mutableStateOf("") }

    // Used to decide if the keyboard should be shown
    var textFieldFocusState by remember { mutableStateOf(false) }

    Surface(elevation = 2.dp) {
        val actualOnMessageSent = {
            onMessageSent(textState.trim())
            // Reset text field and close keyboard
            textState = ""
            // Move scroll to bottom
            dismissKeyboard()
            resetScroll()
        }
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.onBackground)
        ) {
            UserInputText(
                textFieldValue = textState,
                onTextChanged = { textState = it },
                // Only show the keyboard if there's no input selector and text field has focus
                // keyboardShown = currentInputSelector == InputSelector.NONE && textFieldFocusState,
                // Close extended selector if text field receives focus
                onTextFieldFocused = { focused ->
                    if (focused) {
                        viewModel.setInputSelector(InputSelector.NONE)
                        resetScroll()
                    }
                    textFieldFocusState = focused
                },
                focusState = textFieldFocusState,
                onMessageSent = actualOnMessageSent
            )
            UserInputSelector(
                viewModel = viewModel,
                onSelectorChange = { viewModel.setInputSelector(it) },
                sendMessageEnabled = textState.isNotBlank(),
                onMessageSent = actualOnMessageSent,
                currentInputSelector = currentInputSelector
            )
            SelectorExpanded(
                viewModel = viewModel,
                onCloseRequested = dismissKeyboard,
                onTextAdded = { textState += it },
                currentSelector = currentInputSelector
            )
        }
    }
}

@Composable
expect fun ImageLoadingIndicator()

@Composable
private fun SelectorExpanded(
    viewModel: ConversationViewModel,
    currentSelector: InputSelector,
    onCloseRequested: () -> Unit,
    onTextAdded: (String) -> Unit,
) {
    if (currentSelector == InputSelector.NONE) return

    // Request focus to force the TextField to lose it
    val focusRequester = FocusRequester()
    // If the selector is shown, always request focus to trigger a TextField.onFocusChange.
    SideEffect {
        if (currentSelector == InputSelector.EMOJI) {
            focusRequester.requestFocus()
        }
    }

    Surface(elevation = 8.dp) {
        when (currentSelector) {
            InputSelector.EMOJI -> EmojiSelector(onTextAdded, focusRequester)
            InputSelector.DM -> NotAvailablePopup(onCloseRequested)
            InputSelector.PICTURE -> SelectedImagesPanel(viewModel)
            InputSelector.MAP -> FunctionalityNotAvailablePanel()
            InputSelector.PHONE -> FunctionalityNotAvailablePanel()
            else -> {
                throw NotImplementedError()
            }
        }
    }
}

@Composable
fun SelectedImagesPanel(viewModel: ConversationViewModel) {
    val images by viewModel.currentImages.collectAsState()
    if (images.images.isNotEmpty() || images.isLoading) {
        if (images.isLoading) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxWidth()
                    .height(112.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ImageLoadingIndicator()
            }
        } else {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(12.dp),
            ) {
                items(
                    count = images.size,
                    key = { images[it].id },
                    contentType = { images[it] }
                ) {
                    val image = images[it]
                    Image(
                        bitmap = image.imageBitmap,
                        contentDescription = "Image attachment",
                        modifier = Modifier
                            .size(150.dp, 100.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .padding(end = if (it != images.size - 1) 24.dp else 0.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Composable
fun FunctionalityNotAvailablePanel() {
    AnimatedVisibility(
        visibleState = remember { MutableTransitionState(false).apply { targetState = true } },
        // Remove if https://issuetracker.google.com/190816173 is fixed
        enter = expandHorizontally() + fadeIn(),
        exit = shrinkHorizontally() + fadeOut()
    ) {
        Column(
            modifier = Modifier
                .height(320.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Functionality currently not available",
                style = androidx.compose.material.MaterialTheme.typography.body1
            )
            Text(
                text = "Grab a beverage and check back later",
                modifier = Modifier.paddingFrom(FirstBaseline, before = 32.dp),
                style = androidx.compose.material.MaterialTheme.typography.body2,
                color = Color.DarkGray
            )
        }
    }
}


@ExperimentalFoundationApi
@Composable
private fun UserInputText(
    keyboardType: KeyboardType = KeyboardType.Text,
    onTextChanged: (String) -> Unit,
    textFieldValue: String,
    onTextFieldFocused: (Boolean) -> Unit,
    focusState: Boolean,
    onMessageSent: () -> Unit,
) {
    val textColor = MaterialTheme.colorScheme.onSecondary
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .semantics {
                contentDescription = "Text input"
            },
        horizontalArrangement = Arrangement.End
    ) {
        Surface {
            Box(
                modifier = Modifier
                    .height(64.dp)
                    .background(MaterialTheme.colorScheme.onBackground)
                    .weight(1f)
                    .align(Alignment.Bottom)
            ) {
                var lastFocusState by remember { mutableStateOf(false) }
                var ctrlPressed by remember { mutableStateOf(false) }
                BasicTextField(
                    value = textFieldValue,
                    onValueChange = { if (!ctrlPressed) onTextChanged(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp)
                        .align(Alignment.CenterStart)
                        .textCursor()
                        .onFocusChanged { state ->
                            if (lastFocusState != state.isFocused) {
                                onTextFieldFocused(state.isFocused)
                            }
                            lastFocusState = state.isFocused
                        }
                        .sendOnCtrl(ctrlPressed, { ctrlPressed = it }, onMessageSent),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyboardType,
                        imeAction = ImeAction.Send
                    ),
                    maxLines = 100,
                    cursorBrush = SolidColor(textColor),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = textColor,
                        fontSize = 16.sp
                    ),
                    visualTransformation = getTransformation(emojiFontFamily().fontFamily)
                )

                val disableContentColor = Color.LightGray
                if (textFieldValue.isEmpty() && !focusState) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 32.dp),
                        text = "Enter message...",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 16.sp,
                        color = disableContentColor
                    )
                }
            }
        }
    }
}

expect fun getTransformation(fontFamily: FontFamily?): VisualTransformation

fun Modifier.sendOnCtrl(
    ctrlPressed: Boolean,
    setCtrlPressed: (Boolean) -> Unit,
    onMessageSent: () -> Unit
) = onKeyEvent {
    when (it.key) {
        Key.Enter -> {
            if (it.type == KeyEventType.KeyDown && ctrlPressed) {
                onMessageSent()
                true
            } else false
        }

        Key.CtrlLeft,
        Key.CtrlRight,
        -> {
            when (it.type) {
                KeyEventType.KeyDown -> {
                    setCtrlPressed(true)
                    true
                }

                KeyEventType.KeyUp -> {
                    setCtrlPressed(false)
                    true
                }

                else -> false
            }
        }

        else -> false
    }
}

@Composable
private fun UserInputSelector(
    viewModel: ConversationViewModel,
    onSelectorChange: (InputSelector) -> Unit,
    sendMessageEnabled: Boolean,
    onMessageSent: () -> Unit,
    currentInputSelector: InputSelector,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(72.dp)
            .wrapContentHeight()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        InputSelectorButton(
            onClick = { onSelectorChange(InputSelector.EMOJI) },
            icon = Icons.Outlined.Mood,
            selected = currentInputSelector == InputSelector.EMOJI,
            description = "Show emoji selector"
        )
        InputSelectorButton(
            onClick = { onSelectorChange(InputSelector.DM) },
            icon = Icons.Outlined.AlternateEmail,
            selected = currentInputSelector == InputSelector.DM,
            description = "Direct message"
        )
        PicturesSelectorButton(
            viewModel = viewModel,
            currentSelector = currentInputSelector,
            onSelectorChange = onSelectorChange
        )
        InputSelectorButton(
            onClick = { onSelectorChange(InputSelector.MAP) },
            icon = Icons.Outlined.Place,
            selected = currentInputSelector == InputSelector.MAP,
            description = "Location selector"
        )
        InputSelectorButton(
            onClick = { onSelectorChange(InputSelector.PHONE) },
            icon = Icons.Outlined.Duo,
            selected = currentInputSelector == InputSelector.PHONE,
            description = "Start videochat"
        )

        val border = if (!sendMessageEnabled) {
            BorderStroke(
                width = 1.dp,
                color = Color.Gray
            )
        } else {
            null
        }
        Spacer(modifier = Modifier.weight(1f))

        val disabledContentColor = Color.DarkGray

        val buttonColors = ButtonDefaults.buttonColors(
            disabledBackgroundColor = Color.Transparent,
            disabledContentColor = disabledContentColor
        )

        // Send button
        Button(
            modifier = Modifier.height(36.dp).pointerCursor(),
            enabled = sendMessageEnabled,
            onClick = onMessageSent,
            colors = buttonColors,
            border = border,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                "Send",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun PicturesSelectorButton(
    viewModel: ConversationViewModel,
    currentSelector: InputSelector,
    onSelectorChange: (InputSelector) -> Unit
) {
    var photoPickerOpen by rememberSaveable { mutableStateOf(false) }
    InputSelectorButton(
        onClick = {
            onSelectorChange(InputSelector.PICTURE)
            photoPickerOpen = true
        },
        icon = Icons.Outlined.InsertPhoto,
        selected = currentSelector == InputSelector.PICTURE,
        description = "Attach photo"
    )
    val context = localContext()
    PhotoPicker(
        show = photoPickerOpen,
    ) { images ->
        photoPickerOpen = false
        viewModel.setImages(images, context)
    }
}

@Composable
private fun InputSelectorButton(
    onClick: () -> Unit,
    icon: ImageVector,
    description: String,
    selected: Boolean,
) {
    val backgroundModifier = if (selected) {
        Modifier.background(
            color = Color.Gray,
            shape = RoundedCornerShape(14.dp)
        )
    } else {
        Modifier
    }
    IconButton(
        onClick = onClick,
        modifier = backgroundModifier
    ) {
        val tint = if (selected) {
            Color.LightGray
        } else {
            Color.Gray
        }
        Icon(
            icon,
            tint = tint,
            modifier = Modifier.padding(16.dp),
            contentDescription = description
        )
    }
}

@Composable
private fun NotAvailablePopup(onDismissed: () -> Unit) {
    FunctionalityNotAvailablePopup(onDismissed)
}

// val KeyboardShownKey = SemanticsPropertyKey<Boolean>("KeyboardShownKey")
// var SemanticsPropertyReceiver.keyboardShownProperty by KeyboardShownKey

enum class EmojiStickerSelector {
    EMOJI,
    STICKER
}

@Composable
fun EmojiSelector(
    onTextAdded: (String) -> Unit,
    focusRequester: FocusRequester,
) {
    var selected by remember { mutableStateOf(EmojiStickerSelector.EMOJI) }

    val a11yLabel = "Emoji selector"
    Column(
        modifier = Modifier
            .focusRequester(focusRequester) // Requests focus when the Emoji selector is displayed
            // Make the emoji selector focusable, so it can steal focus from TextField
            .focusTarget()
            .semantics { contentDescription = a11yLabel }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            ExtendedSelectorInnerButton(
                text = "Emojis",
                onClick = { selected = EmojiStickerSelector.EMOJI },
                selected = true,
                modifier = Modifier.weight(1f)
            )
            ExtendedSelectorInnerButton(
                text = "Stickers",
                onClick = { selected = EmojiStickerSelector.STICKER },
                selected = false,
                modifier = Modifier.weight(1f)
            )
        }
        Row(modifier = Modifier.verticalScroll(rememberScrollState())) {
            EmojiTable(onTextAdded, modifier = Modifier.padding(8.dp))
        }
    }
    if (selected == EmojiStickerSelector.STICKER) {
        NotAvailablePopup(onDismissed = { selected = EmojiStickerSelector.EMOJI })
    }
}

@Composable
fun ExtendedSelectorInnerButton(
    text: String,
    onClick: () -> Unit,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = ButtonDefaults.buttonColors(
        backgroundColor = Color.White.copy(alpha = 0.08f),
        disabledBackgroundColor = Color.Transparent,
        contentColor = Color.DarkGray,
        disabledContentColor = Color.LightGray.copy(alpha = 0.74f)
    )
    TextButton(
        onClick = onClick,
        modifier = modifier
            .padding(8.dp)
            .height(36.dp),
        enabled = selected,
        colors = colors,
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = text,
            style = androidx.compose.material.MaterialTheme.typography.h6
        )
    }
}

@Composable
fun EmojiTable(
    onTextAdded: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxWidth()) {
        repeat(4) { x ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(EMOJI_COLUMNS) { y ->
                    val emoji = emojis[x * EMOJI_COLUMNS + y]
                    Text(
                        modifier = Modifier
                            .clickable(onClick = { onTextAdded(emoji) })
                            .sizeIn(minWidth = 42.dp, minHeight = 42.dp)
                            .padding(8.dp),
                        text = emoji,
                        style = emojiFontFamily(),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

private const val EMOJI_COLUMNS = 10


enum class InputSelector {
    NONE,
    MAP,
    DM,
    EMOJI,
    PHONE,
    PICTURE
}