package presentation.conversation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import common.util.*
import domain.model.ConversationUiState
import domain.model.Message
import domain.model.User
import io.kamel.image.KamelImage
import kotlinx.datetime.Clock
import navigation.LocalNavigator
import navigation.Screens
import navigation.navigateTo
import org.jetbrains.compose.resources.ExperimentalResourceApi
import presentation.common.messagesParser.SymbolAnnotationType
import presentation.common.messagesParser.messageFormatter
import presentation.common.resourceBindings.Drawables
import presentation.profile.components.UserImage

private val messagesPadding = PaddingValues(
    start = 10.dp,
    end = 10.dp,
    top = 20.dp,
    bottom = 20.dp
)

@Composable
fun Messages(
    conversationUiState: ConversationUiState?,
    user: User?,
    scrollState: LazyListState,
    modifier: Modifier = Modifier
) {
    val currentDate = Clock.System.now().toLocal().date
    val messages = remember(conversationUiState) { conversationUiState?.messages }
    val navHost = LocalNavigator.current
    Box(modifier = modifier) {
        messages?.let {
            LazyColumn(
                reverseLayout = true,
                contentPadding = messagesPadding,
                modifier = Modifier
                    .padding(top = 50.dp)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                state = scrollState
            ) {
                items(
                    count = messages.size,
                    key = { messages[it].id },
                    contentType = { messages[it] }
                ) { index ->
                    val msg = messages[index]
                    val prevMessage = messages.getOrNull(index - 1)
                    val nextMessage = messages.getOrNull(index + 1)
                    Column {
                        if (nextMessage?.timestamp?.date != msg.timestamp.date) {
                            if (msg.timestamp.date == currentDate) {
                                DayHeader("Today")
                            } else {
                                DayHeader(msg.timestamp.date.toLabel())
                            }
                        }
                        MessageWidget(
                            onAuthorClick = { name -> navHost?.navigateTo(Screens.Profile(id = name)) },
                            msg = msg,
                            isUserMe = msg.author.email == user?.email,
                            isFirstMessageByAuthor = prevMessage?.author != msg.author,
                            isLastMessageByAuthor = nextMessage?.author != msg.author,
                        )
                    }
                }
            }
        }
        JumpToBottom(
            scrollState = scrollState,
            enabled = scrollState.firstVisibleItemIndex != 0
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MessageWidget(
    onAuthorClick: (String) -> Unit,
    msg: Message,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
) {
    val borderColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSecondary
    }

    val spaceBetweenAuthors = if (isLastMessageByAuthor) Modifier.padding(top = 8.dp) else Modifier
    Row(modifier = spaceBetweenAuthors) {
        if (isLastMessageByAuthor) {
            msg.author.image?.let {
                UserImage(
                    modifier = Modifier
                        .clickable(onClick = { onAuthorClick(msg.author.email) })
                        .padding(horizontal = 16.dp)
                        .size(42.dp)
                        .border(1.5.dp, borderColor, CircleShape)
                        .clip(CircleShape)
                        .align(Alignment.Top),
                    resource = ioPainterResource(msg.author.image.toResourceUrl()),
                    contentScale = ContentScale.Crop,
                )
            } ?: run {
                Image(
                    modifier = Modifier
                        .clickable(onClick = { onAuthorClick(msg.author.email) })
                        .padding(horizontal = 16.dp)
                        .size(42.dp)
                        .border(1.5.dp, borderColor, CircleShape)
                        .clip(CircleShape)
                        .align(Alignment.Top),
                    painter = Drawables.user_icon,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                )
            }
        } else {
            // Space under avatar
            Spacer(modifier = Modifier.width(74.dp))
        }
        AuthorAndTextMessage(
            msg = msg,
            isUserMe = isUserMe,
            isFirstMessageByAuthor = isFirstMessageByAuthor,
            isLastMessageByAuthor = isLastMessageByAuthor,
            authorClicked = onAuthorClick,
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(1f),
        )
    }
}

@Composable
fun AuthorAndTextMessage(
    msg: Message,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
    authorClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        if (isLastMessageByAuthor) {
            AuthorNameTimestamp(msg, isUserMe)
        }
        ChatItemBubble(msg, isUserMe, authorClicked = authorClicked)
        if (isFirstMessageByAuthor) {
            // Last bubble before next author
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            // Between bubbles
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun AuthorNameTimestamp(msg: Message, isUserMe: Boolean) {
    val textColor = MaterialTheme.colorScheme.onSurfaceVariant
    Row(modifier = Modifier.semantics(mergeDescendants = true) {}) {
        Text(
            text = if (isUserMe) "Me" else msg.author.fullName,
            style = MaterialTheme.typography.titleMedium,
            color = textColor,
            modifier = Modifier
                .alignBy(LastBaseline)
                .paddingFrom(LastBaseline, after = 8.dp) // Space to 1st bubble
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = msg.timestamp.time(),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.alignBy(LastBaseline),
            color = textColor
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ChatItemBubble(
    message: Message,
    isUserMe: Boolean,
    authorClicked: (String) -> Unit,
) {

    val backgroundBubbleColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.secondary
    }

    Column {
        Surface(
            color = backgroundBubbleColor,
            shape = chatBubbleShape
        ) {
            ClickableMessage(
                message = message,
                isUserMe = isUserMe,
                authorClicked = authorClicked,
            )
        }
        if (message.images.isNotEmpty()) {
            for (image in message.images) {
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = backgroundBubbleColor,
                    shape = chatBubbleShape
                ) {
                    KamelImage(
                        resource = ioPainterResource(image.toResourceUrl()),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(160.dp),
                        contentDescription = "Image"
                    )
                }
            }
        }
    }
}

private val chatBubbleShape = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)

@Composable
fun ClickableMessage(
    message: Message,
    isUserMe: Boolean,
    authorClicked: (String) -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    val mainTextColor = when (isUserMe) {
        true -> MaterialTheme.colorScheme.onPrimary
        false -> MaterialTheme.colorScheme.onSecondary
    }

    val styledMessage = messageFormatter(
        text = message.content,
        primary = isUserMe,
    )

    ClickableText(
        text = styledMessage,
        style = MaterialTheme.typography.bodyMedium.copy(color = mainTextColor),
        modifier = Modifier.padding(16.dp),
        onClick = {
            styledMessage
                .getStringAnnotations(start = it, end = it)
                .firstOrNull()
                ?.let { annotation ->
                    when (annotation.tag) {
                        SymbolAnnotationType.LINK.name -> uriHandler.openUri(annotation.item)
                        SymbolAnnotationType.PERSON.name -> authorClicked(annotation.item)
                        else -> Unit
                    }
                }
        }
    )
}
