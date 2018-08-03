package com.tommasoberlose.stash.services

import android.app.*
import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.support.v4.content.LocalBroadcastManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.util.Log
import java.io.ByteArrayOutputStream
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Context
import android.os.SystemClock
import android.support.v4.content.ContextCompat
import com.tommasoberlose.stash.R
import com.tommasoberlose.stash.objects.StashedNotification
import com.tommasoberlose.stash.ui.MainActivity
import com.tommasoberlose.stash.utils.NotificationIntentService
import com.tommasoberlose.stash.utils.PreferenceStorage
import io.realm.Realm
import java.io.ObjectOutput
import java.io.ObjectOutputStream


class SnifferNotificationService : NotificationListenerService() {

  override fun onNotificationPosted(sbn: StatusBarNotification) {
    val notification = sbn.notification
    val pack = sbn.packageName

    if (PreferenceStorage.isStashing(this) && pack != this.packageName && notification.priority >= NotificationCompat.PRIORITY_DEFAULT) {
      val bos = ByteArrayOutputStream()
      var out: ObjectOutput? = null
      try {
        out = ObjectOutputStream(bos)
        out.writeObject(notification.contentIntent)
        out.flush()
        val yourBytes = bos.toByteArray()

        Realm.getDefaultInstance().executeTransaction {
          val obj = StashedNotification()
          obj.id = String.format("%s_%s_%s", sbn.packageName, sbn.id, SystemClock.currentThreadTimeMillis())
          obj.date = SystemClock.currentThreadTimeMillis()
          obj.notification = yourBytes
          it.insertOrUpdate(obj)

          NotificationIntentService.updateStashNotification(this)
        }
      } catch (ex: Exception) {
        ex.printStackTrace()
      } finally {
        try {
          bos.close()
        } catch (e: Exception) {
          e.printStackTrace()
        }
      }
    }
  }

  override fun onNotificationRemoved(sbn: StatusBarNotification) {
  }
}
