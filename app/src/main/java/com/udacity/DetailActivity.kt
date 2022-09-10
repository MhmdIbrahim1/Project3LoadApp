package com.udacity

import android.app.NotificationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.Const.DETAIL_ACTIVITY_FILENAME
import com.udacity.Const.DETAIL_ACTIVITY_STATUS
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*


class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val fileName = intent.getStringExtra(DETAIL_ACTIVITY_STATUS)
        textViwFileName.text = fileName

        val status = intent.getStringExtra(DETAIL_ACTIVITY_FILENAME)
        textViewStatus.text = status

        button.setOnClickListener {
            // Dismiss Mode
            finish()
        }
        val notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelAllNotifications()
    }

}
