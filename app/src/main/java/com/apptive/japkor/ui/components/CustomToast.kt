package com.apptive.japkor.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apptive.japkor.ui.localization.AppLocalizer
import com.apptive.japkor.ui.localization.LocalAppLanguage
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
    val style = when (message.type) {
        ToastType.INFO -> ToastVisualStyle(
            startColor = Color(0xFFFEF8F3),
            endColor = Color(0xFFF7E8DF),
            borderColor = Color(0xFFE7D4C4),
            textColor = Color(0xFF4A3B31),
            accentColor = Color(0xFFD9B185)
        )
        ToastType.SUCCESS -> ToastVisualStyle(
            startColor = Color(0xFFF2F8F2),
            endColor = Color(0xFFE5F0E7),
            borderColor = Color(0xFFBFD4C5),
            textColor = Color(0xFF2F4738),
            accentColor = Color(0xFF8BB89A)
        )
        ToastType.ERROR -> ToastVisualStyle(
            startColor = Color(0xFFFDF4F4),
            endColor = Color(0xFFF5E3E3),
            borderColor = Color(0xFFE4B8B8),
            textColor = Color(0xFF5B2C2C),
            accentColor = Color(0xFFCF8D8D)
        )
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
        val appLanguage = LocalAppLanguage.current
        val localizedMessage = AppLocalizer.translate(message.message, appLanguage)
        val gradient = Brush.linearGradient(colors = listOf(style.startColor, style.endColor))
        val shape = RoundedCornerShape(14.dp)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 10.dp, shape = shape)
                .background(
                    brush = gradient,
                    shape = shape
                )
                .border(width = 1.dp, color = style.borderColor, shape = shape)
                .background(
                    color = Color.Transparent,
                    shape = shape
                )
                .padding(horizontal = 22.dp, vertical = 18.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .height(4.dp)
                        .background(color = style.accentColor, shape = RoundedCornerShape(50))
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = localizedMessage,
                    color = style.textColor,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private data class ToastVisualStyle(
    val startColor: Color,
    val endColor: Color,
    val borderColor: Color,
    val textColor: Color,
    val accentColor: Color
)
