package com.mrmi.quarantineworkout


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startWorkout(v: View)
    {
        val selectedWorkout = v.tag.toString()
        println(selectedWorkout)

        val serviceIntent = Intent(this@MainActivity, OnAppKillService::class.java)
        this@MainActivity.startService(serviceIntent)

        //Launch workout activity with selected workout
        val intent = Intent(this@MainActivity, WorkoutActivity::class.java)
        intent.putExtra("Workout", selectedWorkout)
        startActivity(intent)
    }
}
