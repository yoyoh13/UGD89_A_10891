package com.yohanes.ugd89_a_10891_project2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var square: TextView
    private val notification = "notification"
    private val notificationId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        square = findViewById(R.id.tv_square)
        setUpSensorStuff()
        createNotification()
    }

    private fun createNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Modul89_A_10891_PROJECT2"
            val descriptionText = "Selamat anda sudah berhasil mengerjakan Modul 8 dan 9"

            val notification2 = NotificationChannel(notification, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager : NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notification2)
        }
    }

    private fun sendNotification(){
        val builder = NotificationCompat.Builder(this, notification)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle("Modul89_A_10891_PROJECT2")
            .setContentText("Selamat anda sudah berhasil mengerjakan Modul 8 dan 9")
            .setPriority(NotificationCompat.PRIORITY_LOW)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId, builder.build())
        }
    }

    private fun setUpSensorStuff() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
                accelerometer ->
                    sensorManager.registerListener(
                        this,
                        accelerometer,
                        SensorManager.SENSOR_DELAY_FASTEST,
                        SensorManager.SENSOR_DELAY_FASTEST
                    )
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val sides = event.values[0]
            val upDown = event.values[1]

            square.apply {
                rotationX = upDown * 3f
                rotationY = sides * 3f
                rotation = -sides
                translationX = sides * -10
                translationY = upDown * 10
            }

            val color = if (upDown.toInt() == 0 && sides.toInt() == 0)
                Color.GREEN else Color.RED
            if (upDown.toInt() == 0 && sides.toInt() == 0)
                sendNotification()
            square.setBackgroundColor(color)
            square.text = "up/down ${upDown.toInt()}\nleft/right ${sides.toInt()}"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
}