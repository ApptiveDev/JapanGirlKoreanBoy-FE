package com.apptive.japkor.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.apptive.japkor.ui.theme.CustomColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

enum class ToastType {
    INFO, SUCCESS, ERROR
}

data class ToastMessage(
    val id: String = UUID.randomUUID().toString(),
    val message: String,
    val type: ToastType,
    val duration: Long = 2000L
)

class ToastManager {
    private val _toastMessages = MutableStateFlow<List<ToastMessage>>(emptyList())
    val toastMessages: StateFlow<List<ToastMessage>> = _toastMessages.asStateFlow()

    fun show(type: ToastType, message: String) {
        android.util.Log.d("ToastManager", "show toast type=$type message=$message")
        _toastMessages.update {
            it + ToastMessage(message = message, type = type)
        }
    }

    fun info(message: String) = show(ToastType.INFO, message)
    fun success(message: String) = show(ToastType.SUCCESS, message)
    fun error(message: String) = show(ToastType.ERROR, message)

    fun dismiss(id: String) {
        _toastMessages.update { messages ->
            messages.filterNot { it.id == id }
        }
    }
}

val LocalToastManager = staticCompositionLocalOf<ToastManager> {
    error("No ToastManager provided")
}

@Composable
fun ToastProvider(
    manager: ToastManager,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalToastManager provides manager) {
        content()
    }
}

@Composable
fun CustomToastContainer(
    manager: ToastManager,
    modifier: Modifier = Modifier
) {
    val messages = manager.toastMessages.collectAsState().value

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            messages.forEach { message ->
                CustomToast(
                    message = message,
                    onDismiss = { manager.dismiss(message.id) }
                )
            }
        }
    }
}


@Composable
private fun CustomToast(
    message: ToastMessage,
    onDismiss: () -> Unit
) {
    val (backgroundColor, textColor) = when (message.type) {
        ToastType.INFO -> Color(0xFF1F2937) to Color.White
        ToastType.SUCCESS -> Color(0xFF065F46) to Color.White
        ToastType.ERROR -> Color(0xFFB91C1C) to Color.White
    }

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        delay(message.duration)
        visible = false
        delay(300) // fade-out animation time
        onDismiss()
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier = Modifier
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = message.message,
                color = textColor
            )
        }
    }
}
