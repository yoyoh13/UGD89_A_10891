package com.yohanes.ugd89_a_10891_project1

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    lateinit var proximitySensor: Sensor
    lateinit var sensorManager: SensorManager

    private var mCamera: Camera? = null
    private var mCameraView: CameraView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            mCamera = Camera.open()
        } catch (e: Exception) {
            Log.d("Error", "Failed to get Camera" + e.message)
        }
        if (mCamera!= null){
            mCameraView = CameraView( this, mCamera!!)
            val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
            camera_view.addView(mCameraView)
        }

        @SuppressLint("MissingInfalteId", "LocalSuppress")
        val imageClose = findViewById<View>(R.id.imgClose) as ImageButton
        imageClose.setOnClickListener { view: View? -> System.exit(0)}

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        if (proximitySensor == null) {
            Toast.makeText(this, "No proximity sensor found in device..", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            sensorManager.registerListener(
                proximitySensorEventListener,
                proximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    var proximitySensorEventListener: SensorEventListener? = object  : SensorEventListener{
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

        }

        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
                if (event.values[0] == 0f) {
                    mCameraView?.changeCamera()
                } else {
                }

            }
        }
    }
}
