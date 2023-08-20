package edu.mirea.onebeattrue.services

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.util.Log

class MyIntentService : IntentService(NAME) {
    override fun onCreate() {
        super.onCreate()
        log("onCreate")
        setIntentRedelivery(false) // если система убьет наш сервис - он не будет перезапущен
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
    }

    override fun onHandleIntent(intent: Intent?) {
        log("onHandleIntent")
        for (i in 1..10) {
            Thread.sleep(1000)
            log("Timer: $i")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun createNotification(): Notification = Notification.Builder(this, CHANNEL_ID)
        .setContentTitle("foreground_service")
        .setContentText("aboba")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .build()

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyIntentService: $message")
    }

    companion object {
        private const val NAME = "MyIntentService"
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
        private const val NOTIFICATION_ID = 1
        fun newIntent(context: Context): Intent = Intent(context, MyIntentService::class.java)
    }
}