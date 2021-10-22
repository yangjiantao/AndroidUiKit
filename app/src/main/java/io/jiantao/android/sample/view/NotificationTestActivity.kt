package io.jiantao.android.sample.view

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import io.jiantao.android.sample.R
import io.jiantao.android.uikit.view.NotificationHelper
import kotlinx.android.synthetic.main.activity_notificationtest_layout.*

/**
 * 通知使用测试
 * @author jiantao
 * @date 2018/1/27
 */
class NotificationTestActivity : AppCompatActivity() {

    // 通知渠道 一旦被创建，修改方法：1. 通过引导用户到设置中更新。
    // 2. 删除已存在channelid ,但只要创建是只要id相同，会返回之前删除的配置。
    // https://www.jianshu.com/p/92afa56aee05
    val channelId = "androiduikit"
    lateinit var notificationManager: NotificationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        setContentView(R.layout.activity_notificationtest_layout)
        checkBox_for_sound.isChecked = true
        checkBox_vibrate.isChecked = true

        send_btn.setOnClickListener {
            // send a notification
//            sendNotification()
            userHelper()
        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun userHelper() {
        val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(editText_title.text)
                .setContentText(editText_content.text)
                .setAutoCancel(true)

        builder.setTicker(" 通知 ticker 测试")

        val name = "medlinker"
        val channel = NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_DEFAULT)
        channel.enableLights(true)
        channel.setShowBadge(true)
        channel.description = "description"

        NotificationHelper.notify(notificationManager, builder, channel, 1111)
    }

    private fun sendNotification() {
        val name = "medlinker"
        val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(editText_title.text)
                .setContentText(editText_content.text)
                .setAutoCancel(true)

        builder.setTicker(" 通知 ticker 测试")
        val notification = builder.build()

        val vibrateData = longArrayOf(0, 500, 1000, 1000)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                notificationManager.deleteNotificationChannel(channelId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val channel = NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableLights(true)
            channel.enableVibration(checkBox_vibrate.isChecked)
            channel.vibrationPattern = vibrateData
            channel.setShowBadge(true)
            channel.description = "description"
            if (!checkBox_for_sound.isChecked) {
                channel.setSound(null, null)
            }
            notificationManager.createNotificationChannel(channel)
        } else {
            if (checkBox_vibrate.isChecked) {
                notification.defaults = notification.defaults.or(Notification.DEFAULT_VIBRATE)
            }
            if (checkBox_for_sound.isChecked) {
                notification.defaults = notification.defaults.or(Notification.DEFAULT_SOUND)
            }
        }

        val id = 110
        try {
            notificationManager.notify(id, notification)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}