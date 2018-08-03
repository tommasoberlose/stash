package com.tommasoberlose.stash

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class CustomApplication: Application() {
  override fun onCreate() {
    super.onCreate()

    // Realm
    Realm.init(this)
    val realmConfig = RealmConfiguration.Builder()
        .deleteRealmIfMigrationNeeded()
        .build()
    Realm.setDefaultConfiguration(realmConfig)
  }
}