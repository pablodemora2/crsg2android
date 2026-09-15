package com.istudio.crsurfguide.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.istudio.crsurfguide.ModernMainActivity
import com.istudio.crsurfguide.R
import com.istudio.crsurfguide.ui.debug.LogBuffer

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        LogBuffer.d("FCM_Service", "Notificación Push Recibida de Firebase.")

        val title = remoteMessage.notification?.title ?: remoteMessage.data["title"] ?: "Alerta de Oleaje"
        val body = remoteMessage.notification?.body ?: remoteMessage.data["body"] ?: "Hay nuevas condiciones en tu spot favorito"
        val spotId = remoteMessage.data["spotId"] ?: ""
        
        LogBuffer.d("FCM_Service", "Payload -> Title: $title, SpotId: $spotId")

        sendNotification(title, body, spotId)
    }

    private fun sendNotification(title: String, body: String, spotId: String) {
        val channelId = "surf_alerts_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Alertas de Surf",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones de alertas de oleaje y swells para tus playas favoritas"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, ModernMainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("spotId", spotId)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_eagle) // Usamos el recurso drawable ic_eagle existente
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        LogBuffer.d("FCM_Service", "Nuevo Token FCM generado: $token")
    }
}
