package com.tommasoberlose.stash.objects

import android.content.Intent
import android.os.Parcel
import android.support.v4.app.NotificationCompat
import io.realm.RealmObject

open class StashedNotification: RealmObject() {
  var id: String = ""
  var date: Long = 0
  var notification: ByteArray? = null
}