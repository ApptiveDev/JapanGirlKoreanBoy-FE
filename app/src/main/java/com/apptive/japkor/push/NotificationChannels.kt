package com.apptive.japkor.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.apptive.japkor.R

fun ensureDefaultNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

    val channelId = context.getString(R.string.fcm_default_channel_id)
    val manager = context.getSystemService(NotificationManager::class.java) ?: return
    if (manager.getNotificationChannel(channelId) != null) return

    val channelName = context.getString(R.string.fcm_default_channel_name)
    val channelDescription = context.getString(R.string.fcm_default_channel_description)
    val importance = NotificationManager.IMPORTANCE_HIGH

    val channel = NotificationChannel(channelId, channelName, importance).apply {
        description = channelDescription
    }
    manager.createNotificationChannel(channel)
}
