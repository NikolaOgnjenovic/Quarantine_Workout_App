package com.mrmi.quarantineworkout

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder

//Service class used for destroying notification on app kill (app is removed from recent apps, stopped or killed in any other way)
class OnAppKillService : Service()
{
    override fun onBind(intent: Intent?): IBinder?
    {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent?)
    {
        println("ClearFromRecentService" + "END")
        var notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(310)
        stopSelf()
    }
}