package io.jiantao.android.uikit.view

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.support.v4.app.NotificationCompat

/**
 * 通知渠道 一旦被创建，修改方法：1. 通过引导用户到设置中更新。 2. 删除已存在channelid ,但只要创建是只要id相同，会返回之前删除的配置。
 * 参考：https://www.jianshu.com/p/92afa56aee05
 * @author jiantao
 * @date 2018/2/1
 */
object NotificationHelper {

    fun notify(manager: NotificationManager, builder: NotificationCompat.Builder, channel: NotificationChannel, id: Int) {
        notify(manager, builder, channel, id, true, true)
    }

    fun notify(manager: NotificationManager, builder: NotificationCompat.Builder, channel: NotificationChannel, id: Int, enableVibrate: Boolean, enableSound: Boolean) {
        val notification = builder.build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.enableVibration(enableVibrate)
            if (!enableSound) {
                channel.setSound(null, null)
            }
            manager.createNotificationChannel(channel)
        } else {

            if (enableSound) {
                notification.defaults = notification.defaults.or(Notification.DEFAULT_SOUND)
            }
            if (enableVibrate) {
                notification.defaults = notification.defaults.or(Notification.DEFAULT_VIBRATE)
            }
        }
        manager.notify(id, notification)
    }
}