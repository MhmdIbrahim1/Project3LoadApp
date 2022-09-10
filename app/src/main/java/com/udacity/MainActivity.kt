package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.udacity.Const.CHANNEL_ID
import com.udacity.Const.GLIDE_URL
import com.udacity.Const.LOAD_APP_URL
import com.udacity.Const.RETROFIT_URL
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*



class MainActivity : AppCompatActivity() {
    private var downloadID: Long = 0
    private var selectedURL: String? = null
    private var selectedFile: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        createChannel(CHANNEL_ID, getString(R.string.app_name))
        custom_button.setOnClickListener {

            if(selectedURL != null) {
                custom_button.buttonState = ButtonState.Loading
                download()

            } else {
                val toast = Toast.makeText(
                    applicationContext,
                    getString(R.string.please_select_file),
                    Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

private val receiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        custom_button.buttonState = ButtonState.Completed
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        val query = DownloadManager.Query()
        query.setFilterById(id!!)

        val cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {
            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

            var downloadStatus = "Fail"
            if (DownloadManager.STATUS_SUCCESSFUL == status) {
                downloadStatus = "Success"
            }
            showToast()
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.sendNotification(
                CHANNEL_ID,
                getString(R.string.notification_description),
                applicationContext,
                downloadStatus,
                selectedFile!!
            )
        }
    }
}

    private fun showToast(){
        val toast = Toast.makeText(
            applicationContext,
            getString(R.string.notification_description),
            Toast.LENGTH_LONG)
        toast.show()
    }

    private fun download() {
        try {
            val request =
                DownloadManager.Request(Uri.parse(selectedURL))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Download Failed", Toast.LENGTH_SHORT).show()
        }
    }


    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.description = resources.getString(R.string.notification_description)
            val notificationManager = getSystemService(
                NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton && view.isChecked) {

            when (view.getId()) {
                R.id.radioButtonGlide ->
                    selectedURL = GLIDE_URL
                R.id.radioButtonLoadApp ->
                    selectedURL = LOAD_APP_URL
                R.id.radioButtonRetrofit ->
                    selectedURL = RETROFIT_URL
            }

            selectedFile = view.text.toString()
        }
    }

}


