package com.example.orientationviewer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.orientationsensor.service.OrientationManager
import com.example.orientationsensor.util.AppConstants

class MainActivity : AppCompatActivity() {
    lateinit var pitch: TextView
    lateinit var roll: TextView
    lateinit var mIntentFilter: IntentFilter
    lateinit var orientationManager: OrientationManager
    lateinit var mReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pitch = findViewById(R.id.pitch)
        roll = findViewById(R.id.roll)

        orientationManager = OrientationManager(this@MainActivity)
        orientationManager.connectToRemoteService()

        mIntentFilter = IntentFilter()
        mIntentFilter.addAction(AppConstants.ACTION_ORIENTATION)

        mReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                checkFilterAndSetValue(intent)
            }
        }
    }

    //register receiver
    override fun onResume() {
        super.onResume()
        registerReceiver(mReceiver, mIntentFilter)
    }

    //disconnect service after leaving page and remove braodcast listener
    override fun onPause() {
        unregisterReceiver(mReceiver)
        if(orientationManager!=null)
            orientationManager.disconnectToRemoteService()
        super.onPause()
    }

    //filter data get from broadcast listener of sensor service
    private fun checkFilterAndSetValue(intent: Intent){
        if (intent.action == AppConstants.ACTION_ORIENTATION) {
            var rollValue = intent.getStringExtra(AppConstants.ROLL_VALUE)
            val pitchValue = intent.getStringExtra(AppConstants.PITCH_VALUE)
            pitch.text = pitchValue
            roll.text = rollValue
        }
    }
}