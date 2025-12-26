package com.apptive.japkor.ui.components.step4

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.apptive.japkor.ui.localization.AppLocalizer
import com.apptive.japkor.ui.localization.LocalAppLanguage
import com.apptive.japkor.ui.requiredinfo.ProfileImageState
import com.apptive.japkor.ui.requiredinfo.UploadStatus

@Composable
fun PhotoUploadButton(
    modifier: Modifier = Modifier,
    state: ProfileImageState? = null,
    onClick: () -> Unit = {}
) {
    val appLanguage = LocalAppLanguage.current
    val shape = RoundedCornerShape(10.dp)
    val containerColor = when (state?.status) {
        UploadStatus.Success -> Color(0xFFF2F6FF)
        UploadStatus.Failed -> Color(0xFFFFF5F5)
        else -> Color.White
    }

    Box(
        modifier = modifier
            .shadow(2.dp, shape, clip = false)
            .clip(shape)
            .background(containerColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        when (state?.status) {
            UploadStatus.Uploading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(28.dp),
                    strokeWidth = 3.dp,
                    color = Color(0xFFB8BDC7)
                )
            }

            UploadStatus.Success -> {
                Log.d("IMAGE_URL", state.uploadedUrl ?: "null")

                AsyncImage(
                    model = state.uploadedUrl,
                    contentDescription = AppLocalizer.translate("uploaded image", appLanguage),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            UploadStatus.Failed -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = AppLocalizer.translate("다시 시도", appLanguage),
                        tint = Color(0xFFE57373)
                    )
                    Text(
                        text = AppLocalizer.translate("재시도", appLanguage),
                        fontSize = 12.sp,
                        color = Color(0xFF5B2C2C)
                    )
                }
            }

            null -> {
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .background(
                            color = Color(0xFFF5F6F8),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        fontSize = 22.sp,
                        color = Color(0xFFB8BDC7)
                    )
                }
            }
        }
    }
}
