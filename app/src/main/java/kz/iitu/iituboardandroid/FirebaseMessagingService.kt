package kz.iitu.iituboardandroid

import android.annotation.SuppressLint
import android.app.LauncherActivity
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.notification?.let { data ->
            val intent = Intent(this, LauncherActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

            val notificationId = 1
            val mBuilder = NotificationCompat.Builder(this, "important_channel")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColorized(true)
                .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)

            data.title?.let {
                mBuilder.setContentTitle(it)
            }

            data.body?.let {
                mBuilder.setContentText(it)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(it))
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBuilder.setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            }

            with(NotificationManagerCompat.from(this)) {
                notify(notificationId, mBuilder.build())
            }
        }
    }
}