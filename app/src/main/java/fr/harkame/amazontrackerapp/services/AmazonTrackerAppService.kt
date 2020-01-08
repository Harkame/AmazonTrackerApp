package fr.harkame.amazontrackerapp.services

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.system.Os.link
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.text.SimpleDateFormat
import java.util.*


/**
 * AmazonTrackerAppService class
 *
 */
class AmazonTrackerAppService : FirebaseMessagingService() {
    private val CHANNEL_ID = "AmazonTrackerAppChannelID"

    companion object {
        fun startService(context: Context) {
            val startIntent = Intent(context, AmazonTrackerAppService::class.java)
            //ContextCompat.startForegroundService(context, startIntent)
            context.startService(Intent(context, AmazonTrackerAppService::class.java))
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, AmazonTrackerAppService::class.java)
            context.stopService(stopIntent)
        }

        val TAG = AmazonTrackerAppService::class.java.canonicalName
    }

    override fun onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) { // [START_EXCLUDE]
        Log.d(TAG, "From: " + remoteMessage.from)

        val notification = remoteMessage.notification

        if (remoteMessage.data.isNotEmpty() && notification != null) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

            val title = notification.title
            val body = notification.body
            val url = if (remoteMessage.data.containsKey("url")) remoteMessage.data["url"] else null

            if (title != null && body != null && url != null)
                sendNotification(title, body, url)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            CHANNEL_ID, CHANNEL_ID,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager!!.createNotificationChannel(serviceChannel)
    }

    private fun sendNotification(title: String, body: String, url: String) {
        val notificationIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        val pendingIntent =
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT)

        val channelId = CHANNEL_ID
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_dialog_alert)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(getID(), notificationBuilder.build())
    }

    private fun getID(): Int {
        val now = Date()
        return SimpleDateFormat("ddHHmmss", Locale.FRANCE).format(now).toInt()
    }
}