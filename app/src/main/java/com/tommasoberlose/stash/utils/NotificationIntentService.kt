package com.tommasoberlose.stash.utils

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import com.tommasoberlose.stash.R
import com.tommasoberlose.stash.objects.StashedNotification
import com.tommasoberlose.stash.ui.MainActivity
import io.realm.Realm

private const val ACTION_START_STASHING_NOTIFICATIONS = "com.tommasoberlose.stash.action.START_STASHING_NOTIFICATIONS"
private const val ACTION_STOP_STASHING_NOTIFICATIONS = "com.tommasoberlose.stash.action.STOP_STASHING_NOTIFICATIONS"
private const val ACTION_UPDATE_SAVED_NOTIFICATIONS = "com.tommasoberlose.stash.action.UPDATE_SAVED_NOTIFICATIONS"

class NotificationIntentService : IntentService("NotificationIntentService") {

  override fun onHandleIntent(intent: Intent) {
    when (intent.action) {
      ACTION_START_STASHING_NOTIFICATIONS -> {
        startStashing()
      }
      ACTION_STOP_STASHING_NOTIFICATIONS -> {
        stopStashing()
      }
      ACTION_UPDATE_SAVED_NOTIFICATIONS -> {
        updateStashingNotification()
      }
    }
  }

  private fun startStashing() {
    PreferenceStorage.enableStash(this)
    updateStashingNotification()
  }

  private fun stopStashing() {
    PreferenceStorage.disableStash(this)
    updateStashingNotification()
  }

  private fun updateStashingNotification() {
    val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val i = Intent(this, MainActivity::class.java)
    val pi = PendingIntent.getActivity(this, 1, i, PendingIntent.FLAG_UPDATE_CURRENT)

    val n = NotificationCompat.Builder(this, "stash")
        .setSmallIcon(R.drawable.ic_stat_stash)
        .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
        .setContentIntent(pi)
        .setOnlyAlertOnce(true)
        .setOngoing(true)
        .setAutoCancel(false)
        .setPriority(NotificationCompat.PRIORITY_MIN)

    val stashedNotificationsCount = Realm.getDefaultInstance().where(StashedNotification::class.java).greaterThanOrEqualTo("date", PreferenceStorage.getStashingStartTime(this)).findAll().size
    n.setContentTitle("Notifiche cancellate: " + stashedNotificationsCount)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      mNotificationManager.createNotificationChannel(NotificationChannel("stash", "Stashed Notifications", NotificationManager.IMPORTANCE_MIN))
    }

    mNotificationManager.notify(1, n.build())
  }

  companion object {
    fun startStashing(context: Context) {
      val intent = Intent(context, NotificationIntentService::class.java).apply {
        action = ACTION_START_STASHING_NOTIFICATIONS
      }
      context.startService(intent)
    }

    fun stopStashing(context: Context) {
      val intent = Intent(context, NotificationIntentService::class.java).apply {
        action = ACTION_STOP_STASHING_NOTIFICATIONS
      }
      context.startService(intent)
    }

    fun updateStashNotification(context: Context) {
      val intent = Intent(context, NotificationIntentService::class.java).apply {
        action = ACTION_UPDATE_SAVED_NOTIFICATIONS
      }
      context.startService(intent)
    }
  }
}
