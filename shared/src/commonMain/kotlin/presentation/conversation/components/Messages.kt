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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import common.util.invoke
import common.util.toLabel
import domain.model.ConversationUiState
import domain.model.Message
import domain.model.User
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import presentation.common.messagesParser.SymbolAnnotationType
import presentation.common.messagesParser.messageFormatter
import presentation.common.resourceBindings.drawable_ali
import presentation.common.resourceBindings.drawable_someone_else

@Composable
fun Messages(
    conversationUiState: ConversationUiState?,
    user: User?,
    scrollState: LazyListState,
    modifier: Modifier = Modifier,
    currentDate: LocalDate
) {
    val messages = conversationUiState?.messages
    Box(modifier = modifier) {
        messages?.let {
            LazyColumn(
                reverseLayout = true,
                contentPadding = PaddingValues(
                    start = 10.dp,
                    end = 10.dp,
                    top = 20.dp,
                    bottom = 20.dp
                ),
                modifier = Modifier
                    .padding(top = 50.dp)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                state = scrollState
            ) {
                items(count = messages.size, { messages[it].id }) { index ->
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
                            onAuthorClick = { name -> println(name) },
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
            Image(
                modifier = Modifier
                    .clickable(onClick = { onAuthorClick(msg.author.email) })
                    .padding(horizontal = 16.dp)
                    .size(42.dp)
                    .border(1.5.dp, borderColor, CircleShape)
                    .clip(CircleShape)
                    .align(Alignment.Top),
                painter = painterResource(if (isUserMe) drawable_ali else drawable_someone_else),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
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

        message.image?.let {
            val msgImage = painterResource(it)
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                color = backgroundBubbleColor,
                shape = chatBubbleShape
            ) {
                Image(
                    painter = msgImage,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(160.dp),
                    contentDescription = "Image"
                )
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
        style = androidx.compose.material.MaterialTheme.typography.body1.copy(color = mainTextColor),
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
