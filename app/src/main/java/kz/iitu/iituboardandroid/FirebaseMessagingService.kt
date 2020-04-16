package kz.iitu.iituboardandroid

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kz.iitu.iituboardandroid.api.LoginResponse
import kz.iitu.iituboardandroid.ui.record.RecordActivity
import wiki.depasquale.mcache.obtain

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        obtain<LoginResponse>().build().getNow() ?: return
        val shouldRememberUser = PreferenceManager
            .getDefaultSharedPreferences(applicationContext)
            .getBoolean(Constants.REMEMBER_ME, false)

        if (!shouldRememberUser) return

        message.data.let { data ->
            val resultIntent = Intent(this, RecordActivity::class.java).apply {
                putExtra(Constants.EXTRA_RECORD_ID, data["id"]?.toIntOrNull())
            }
            val resultingPendingIntent = TaskStackBuilder.create(this)

            resultingPendingIntent.addNextIntentWithParentStack(resultIntent)
            val pendingIntent =
                resultingPendingIntent.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

            val mBuilder = NotificationCompat.Builder(this, "important_channel")
                .setTicker(data["title"])
                .setWhen(0)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setShowWhen(true)
                .setColorized(true)
                .setContentTitle(data["title"])
                .setContentText(data["body"])
                .setStyle(NotificationCompat.BigTextStyle().bigText(data["body"]))
                .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                mBuilder.color = ContextCompat.getColor(this, R.color.colorAccent)
            } else {
                mBuilder.setSmallIcon(R.mipmap.ic_launcher)
            }

            val notificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    "important_channel",
                    "ic_notification",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.cancel(1)
            notificationManager.notify(1, mBuilder.build())
        }
    }
}