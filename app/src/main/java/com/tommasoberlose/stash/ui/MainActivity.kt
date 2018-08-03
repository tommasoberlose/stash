package com.tommasoberlose.stash.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.tommasoberlose.stash.R
import android.content.Intent
import com.tommasoberlose.stash.utils.NotificationIntentService
import com.tommasoberlose.stash.utils.PreferenceStorage
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // TODO spiegare e chiedere attivazione lettura notifiche
    // TODO elenco delle notiche arrivate con possibilità di cancellarle

    // TODO filter app, priority, timing, tutte anche quelle già presenti

//    text.setOnClickListener {
//      val intent = Intent(
//          "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
//      startActivity(intent)
//    }


  }

  override fun onResume() {
    super.onResume()
    if (PreferenceStorage.isStashing(this)) {
      text.setOnClickListener {
        NotificationIntentService.stopStashing(this)
      }
      // TODO check if stash is enabled e una volta sbloccato spegnerlo
    } else {
      NotificationIntentService.startStashing(this)
    }
  }
}
