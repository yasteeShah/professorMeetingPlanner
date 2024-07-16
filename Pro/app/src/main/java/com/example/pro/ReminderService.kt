package com.example.professormeetingplanner

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.firebase.database.*

class ReminderService : Service() {

    private lateinit var database: DatabaseReference

    override fun onCreate() {
        super.onCreate()
        database = FirebaseDatabase.getInstance().reference
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        checkForReminders()
        return START_STICKY
    }

    private fun checkForReminders() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        database.child("appointments").child(userId).get().addOnSuccessListener {
            // Logic to check for upcoming appointments and create notifications
        }
    }

    private fun createNotification(message: String) {
        val channelId = "reminder_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Reminder Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Appointment Reminder")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notification)
            .build()

        notificationManager.notify(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
