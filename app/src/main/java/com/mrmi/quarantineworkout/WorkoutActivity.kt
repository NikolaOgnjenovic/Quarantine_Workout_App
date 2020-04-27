package com.mrmi.quarantineworkout

import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class WorkoutActivity : AppCompatActivity()
{
    data class WorkoutClass(val exercises: List<String>, val exerciseTime: Int)  //class template for any workout

    private val legWorkout: WorkoutClass = WorkoutClass(listOf("Squats", "Rest", "High Knees", "Rest", "Lunges", "Rest", "Hip Bridges"), 30000)
    private val shouldersWorkout: WorkoutClass = WorkoutClass(listOf("Shoulders", "2"), 30000)
    private val chestWorkout: WorkoutClass = WorkoutClass(listOf("chest", "2", "3"), 15000)
    private val bicepsWorkout: WorkoutClass = WorkoutClass(listOf("biceps"), 30000)
    private val backWorkout: WorkoutClass = WorkoutClass(listOf("back", "Rest"), 30000)
    private val tricepsWorkout: WorkoutClass = WorkoutClass(listOf("triceps", "Rest"), 3000)

    private var timeLeft = 0 //Time left for an exercise, used for pausing and resuming
    private var isPaused = false
    private var lastButtonClickTime = 0 //Prevent double clicking on buttons to mess up the timers

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

        //Necessary Text Views
        val timerText: TextView = findViewById(R.id.timerText)
        val exerciseNameText: TextView = findViewById(R.id.exerciseName)

        val startButton: Button = findViewById(R.id.startButton)
        startButton.text = "START"
        isPaused=false
        val pauseButton: Button = findViewById(R.id.pauseButton)
        pauseButton.text = "PAUSE"
        pauseButton.isEnabled=false
        val resumeButton: Button = findViewById(R.id.resumeButton)
        resumeButton.text = "RESUME"
        resumeButton.isEnabled=false

        startButton.setOnClickListener()
        {
            // Prevent rapid clicking, using 1 second cooldown between clicks
            if(SystemClock.elapsedRealtime()-lastButtonClickTime>=1000)
            {
                lastButtonClickTime = SystemClock.elapsedRealtime().toInt();

                println("Starting workout.")

                //Get intent from workoutMenu
                val intent = intent
                when(intent.getStringExtra("Workout")) //Check the workout given in the intent and start it
                {
                    "legs" -> startWorkout(legWorkout, timerText, exerciseNameText, 0)
                    "shoulders" -> startWorkout(shouldersWorkout, timerText, exerciseNameText, 0)
                    "chest" -> startWorkout(chestWorkout, timerText, exerciseNameText, 0)
                    "biceps" -> startWorkout(bicepsWorkout, timerText, exerciseNameText, 0)
                    "back" -> startWorkout(backWorkout, timerText, exerciseNameText, 0)
                    "triceps" -> startWorkout(tricepsWorkout, timerText, exerciseNameText, 0)
                }

                //Enable pause button, disable resume and start button and set isPaused to false
                it.isEnabled = false
                pauseButton.isEnabled = true
                isPaused = false
                resumeButton.isEnabled = false
            }
        }

        pauseButton.setOnClickListener()
        {
            // Prevent rapid clicking, using 1 second cooldown between clicks
            if(SystemClock.elapsedRealtime()-lastButtonClickTime>=1000)
            {
                lastButtonClickTime = SystemClock.elapsedRealtime().toInt();

                println("Pausing")
                //Disable pause and start buttons, enable resume button, set paused to true
                it.isEnabled = false
                isPaused = true
                resumeButton.isEnabled = true
            }
        }

        resumeButton.setOnClickListener()
        {
            // Prevent rapid clicking, using 1 second cooldown between clicks
            if (SystemClock.elapsedRealtime() - lastButtonClickTime >= 1000) {
                lastButtonClickTime = SystemClock.elapsedRealtime().toInt();

                println("Resuming")

                //Disable resume button, enable pause button and resume workout
                it.isEnabled = false
                pauseButton.isEnabled = true

                val intent = intent
                when (intent.getStringExtra("Workout")) //Check the workout given in the intent and start it
                {
                    "legs" -> startWorkout(legWorkout, timerText, exerciseNameText, 0)
                    "shoulders" -> startWorkout(shouldersWorkout, timerText, exerciseNameText, 0)
                    "chest" -> startWorkout(chestWorkout, timerText, exerciseNameText, 0)
                    "biceps" -> startWorkout(bicepsWorkout, timerText, exerciseNameText, 0)
                    "back" -> startWorkout(backWorkout, timerText, exerciseNameText, 0)
                    "triceps" -> startWorkout(tricepsWorkout, timerText, exerciseNameText, 0)
                }
            }
        }
    }

    private fun startWorkout(workout: WorkoutClass, timerText: TextView, exerciseNameText: TextView, index: Int)
    {
        val exercise = workout.exercises[index]
        println(exercise)
        var adjustedExerciseTime = workout.exerciseTime

        //If the user should rest adjust the exercise time to 15 seconds
        if(exercise=="Rest")
            adjustedExerciseTime = 15000
        exerciseNameText.text = exercise
        if(isPaused)
        {
            println("Unpaused, set time to reamining: "+ timeLeft)
            adjustedExerciseTime=timeLeft
            isPaused = false
        }
        object : CountDownTimer(adjustedExerciseTime.toLong(), 1000) //Start decreasing the adjusted time by 1 second until it hits 0
        {
            override fun onTick(millisUntilFinished: Long)
            {
                timerText.text = (millisUntilFinished/1000).toString() + "s" //Set timer text to time left
                if(isPaused)
                {
                    println("Paused. Time left: "+ millisUntilFinished)
                    timeLeft = millisUntilFinished.toInt()
                    this.cancel()
                }
            }
            override fun onFinish()
            {
                //Once the exercise is finished, if possible start the next one or if not just display "Completed"
                if(index+1<legWorkout.exercises.size)
                    startWorkout(workout, timerText, exerciseNameText, index+1)
            }
        }.start()
    }
}
