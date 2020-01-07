package fr.harkame.amazontrackerapp.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import fr.harkame.amazontrackerapp.MainActivity


/**
 * AmazonTrackerAppService class
 *
 */
class AmazonTrackerAppService : FirebaseMessagingService() {
    private val CHANEL_ID = "AmazonTrackerAppService ForegroundService"

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

        val TAG = AmazonTrackerAppService.javaClass.canonicalName
    }

    override fun onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification = NotificationCompat.Builder(this, CHANEL_ID)
            .setContentTitle(getString(R.string.amazontrackerapp_is_running))
            .setSmallIcon(R.drawable.ic_app_foreground)
            .setContentIntent(pendingIntent)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()

        //startForeground(1, notification)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) { // ...
// TODO(developer): Handle FCM messages here.
// Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.from)

        val notificationManager = NotificationManagerCompat.from(this)

        val notification = NotificationCompat.Builder(this, CHANEL_ID)
            .setContentTitle(getString(R.string.amazontrackerapp_is_running))
            .setSmallIcon(R.drawable.ic_app_foreground)
            .setContentIntent(pendingIntent)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()


        notificationManager.notify(565, bnotification)
        // Check if message contains a data payload.
        if (remoteMessage.data.size > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            if ( /* Check if data needs to be processed by long running job */true) { // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.

            } else { // Handle message within 10 seconds

            }
        }
        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification!!.body)
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
// message, here is where that should be initiated. See sendNotification method below.
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            CHANEL_ID, "Blacklister Foreground Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager!!.createNotificationChannel(serviceChannel)
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        // If you want to send messages to this application instance or
// manage this apps subscriptions on the server side, send the
// Instance ID token to your app server.

    }
}