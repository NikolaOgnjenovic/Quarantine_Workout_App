package com.mrmi.quarantineworkout

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.RemoteViews
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class WorkoutActivity : AppCompatActivity()
{
    data class WorkoutClass(var exercises: List<String>, var exerciseTimes: List<Int>)  //class template for any workout

    //Available workout sets consisting of exercise names and their durations
    private val legWorkout: WorkoutClass = WorkoutClass(
        listOf("SQUATS", "HIGH KNEES", "REST", "LUNGES", "HIP BRIDGES", "REST", "SQUATS", "HIGH KNEES", "REST", "LUNGES", "HIP BRIDGES"),
        listOf(30000, 30000, 30000, 30000, 30000, 30000, 30000, 30000, 30000, 30000, 30000))
    private val shouldersWorkout: WorkoutClass = WorkoutClass(
        listOf("PIKE PUSH-UPS", "ARM SCISSORS", "Y RAISES", "REVERSE IRON CROSS PUSH-UPS", "DOOR FRAME HOLDS"),
        listOf(60000, 60000, 60000, 60000, 60000))
    private val chestWorkout: WorkoutClass = WorkoutClass(
        listOf("DIAMOND PUSH-UPS", "NORMAL PUSH-UPS", "REST", "TRICEP DIPS", "WIDE PUSH-UPS", "REST", "DIAMOND PUSH-UPS", "NORMAL PUSH-UPS", "REST", "TRICEP DIPS", "WIDE PUSH-UPS"),
        listOf(30000, 30000, 30000, 30000, 30000, 30000, 20000, 20000, 25000, 20000, 20000))
    private val bicepsWorkout: WorkoutClass = WorkoutClass(
        listOf("LEFT ARM PUSH CURL", "LEFT ARM PUSH HAMMER CURL", "LEFT ARM PUSH REVERSE CURL", "LEFT ARM PUSH HIGH CURL",
            "RIGHT ARM PUSH CURL", "RIGHT ARM PUSH HAMMER CURL", "RIGHT ARM PUSH REVERSE CURL", "RIGHT ARM PUSH HIGH CURL", "PUSH UPRIGHT CURL"),
        listOf(30000, 30000, 30000, 30000, 30000, 30000, 30000, 30000, 60000))
    private val backWorkout: WorkoutClass = WorkoutClass(
        listOf("BACK EXTENSIONS", "BACK HYPEREXTENSIONS", "PULSE ROWS", "SUPERMANS", "REACHERS"),
        listOf(30000, 30000, 30000, 30000, 30000))
    private val tricepsWorkout: WorkoutClass = WorkoutClass(
        listOf("TRICEP EXTENSIONS", "REST", "DIAMOND PUSH-UPS", "REST", "WALK OUT PUSH-UPS", "REST", "TRICEP DIPS"),
        listOf(20000, 10000, 20000, 10000, 20000, 10000, 20000))

    private var timeLeft = 0 //Time left for an exercise, used for pausing and resuming
    private var isPaused = false
    private var lastButtonClickTime = 0 //Prevent double clicking on buttons to mess up the timers

    //Notification variables
    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    private val channelID = "com.mrmi.quarantineworkout"
    private val description = "Quarantine workout notification"

    lateinit var cdt: CountDownTimer //CountDownTimer object so the timer is disabled once the acitvity is destroyed

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(310)

        //Text Views
        val timerText: TextView = findViewById(R.id.timerText)
        val exerciseNameText: TextView = findViewById(R.id.exerciseName)
        val setNumberWarningText: TextView = findViewById(R.id.setNumberWarning)
        setNumberWarningText.visibility = View.GONE
        val setNumberText: TextView = findViewById(R.id.setNumberText)
        val setNumberTitleText: TextView = findViewById(R.id.setNumberTitleText)

        //Buttons and their onClickListeners below
        val startButton: Button = findViewById(R.id.startButton)
        startButton.text = "START"
        isPaused=false
        val pauseButton: Button = findViewById(R.id.pauseButton)
        pauseButton.text = "PAUSE"
        pauseButton.isEnabled = false
        val resumeButton: Button = findViewById(R.id.resumeButton)
        resumeButton.text = "RESUME"
        resumeButton.isEnabled = false
        val minusButton: Button = findViewById(R.id.minusButton)
        val plusButton: Button = findViewById(R.id.plusButton)
        var setNumber = 1

        minusButton.setOnClickListener()
        {
            if(setNumber==1)
            {
                println("Invalid set number input")
                setNumberWarningText.text = "Please set the number of repetitions for this workout to a number greater than 0."
                setNumberWarningText.visibility = View.VISIBLE
            }
            else
            {
                setNumber--
                setNumberText.text = setNumber.toString()
                setNumberWarningText.visibility = View.GONE
            }
        }

        plusButton.setOnClickListener()
        {
            if(setNumber==9)
            {
                println("Too many workouts")
                setNumberWarningText.text = "Please do this workout less than 10 consecutive times. It's for your (and your phone's memory's) health!"
                setNumberWarningText.visibility = View.VISIBLE
            }
            else
            {
                setNumber++
                setNumberText.text = setNumber.toString()
                setNumberWarningText.visibility = View.GONE
            }
        }

        startButton.setOnClickListener()
        {
            //Prevent rapid clicking, using 1 second cooldown between clicks
            if(SystemClock.elapsedRealtime()-lastButtonClickTime>=1000)
            {
                lastButtonClickTime = SystemClock.elapsedRealtime().toInt()

                println("Starting workout.")

                //Enable pause button, disable resume and start button, set isPaused to false, hide set number input field and text, disable minus and plus button and warning text
                it.isEnabled = false
                pauseButton.isEnabled = true
                isPaused = false
                resumeButton.isEnabled = false
                setNumberText.visibility = View.GONE
                setNumberTitleText.visibility = View.GONE
                minusButton.visibility = View.GONE
                plusButton.visibility = View.GONE
                setNumberWarningText.visibility = View.GONE

                //Change number of sets to input if input is bigger than 1 and disable input edit text
                val inputtedSetNumber = (setNumberText.text.toString().toInt())
                setNumber=inputtedSetNumber
                println("Number of sets: " + setNumber)

                val intent = intent //Get intent from workoutMenu
                lateinit var currentWorkout: WorkoutClass //Get selected workout
                when(intent.getStringExtra("Workout")) //Check the workout given in the intent and start it
                {
                    "legs" -> currentWorkout = legWorkout
                    "shoulders" -> currentWorkout = shouldersWorkout
                    "chest" -> currentWorkout = chestWorkout
                    "biceps" -> currentWorkout = bicepsWorkout
                    "back" -> currentWorkout = backWorkout
                    "triceps" -> currentWorkout = tricepsWorkout
                }
                var newWorkout: WorkoutClass = currentWorkout

                //If there is more than 1 set to complete, add 2.5 minute rest between sets and append sets to new workout
                for(set in 2..setNumber)
                {
                    newWorkout.exercises = newWorkout.exercises + "Rest" + currentWorkout.exercises
                    newWorkout.exerciseTimes = newWorkout.exerciseTimes + 150000 + currentWorkout.exerciseTimes //Add 2.5 minute rest between sets
                }

                //Start new workout (selected workout used setNumber times)
                startWorkout(newWorkout, timerText, exerciseNameText, 0)
            }
        }

        pauseButton.setOnClickListener()
        {
            // Prevent rapid clicking, using 1 second cooldown between clicks
            if(SystemClock.elapsedRealtime()-lastButtonClickTime>=1000)
            {
                lastButtonClickTime = SystemClock.elapsedRealtime().toInt()

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
                lastButtonClickTime = SystemClock.elapsedRealtime().toInt()

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

    //Cancel timer and notification once the user leaves the activity
    override fun onDestroy()
    {
        super.onDestroy()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(310)
        cdt.cancel()
    }

    private fun startWorkout(workout: WorkoutClass, timerText: TextView, exerciseNameText: TextView, index: Int)
    {
        val exercise = workout.exercises[index]
        println(exercise)
        exerciseNameText.text = exercise

        var adjustedExerciseTime = workout.exerciseTimes[index]
        //If the user should rest adjust the exercise time to 15 seconds

        if(isPaused)
        {
            //println("Unpaused, set time to reamining: "+ timeLeft)
            adjustedExerciseTime=timeLeft
            isPaused = false
        }

        val contentView = RemoteViews(packageName, R.layout.notificationlayout)
        contentView.setTextViewText(R.id.notificationTitle, "Quarantine Workout")

        cdt = object : CountDownTimer(adjustedExerciseTime.toLong(), 1000) //Start decreasing the adjusted time by 1 second until it hits 0
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

                //Show notification with remaining time of current exercise
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                {
                    contentView.setTextViewText(R.id.notificationContent, "Current exercise: " + exercise + ".\nRemaining time: " + millisUntilFinished/1000 + " seconds.")

                    notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    //Using notificationChannel in android versions after and including Oreo
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    {
                        notificationChannel = NotificationChannel(channelID, description, NotificationManager.IMPORTANCE_HIGH)
                        notificationChannel.enableVibration(false)
                        notificationManager.createNotificationChannel(notificationChannel)

                        builder = Notification.Builder(this@WorkoutActivity, channelID)
                            .setContent(contentView)
                            .setSmallIcon(R.drawable.ic_launcher_round)
                            .setLargeIcon(BitmapFactory.decodeResource(this@WorkoutActivity.resources, R.drawable.ic_launcher))
                    }
                    else
                    {
                        builder = Notification.Builder(this@WorkoutActivity)
                            .setContent(contentView)
                            .setSmallIcon(R.drawable.ic_launcher_round)
                            .setLargeIcon(BitmapFactory.decodeResource(this@WorkoutActivity.resources, R.drawable.ic_launcher))
                    }

                    notificationManager.notify(310, builder.build())
                }
            }

            override fun onFinish()
            {
                notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(310)

                //Once the exercise is finished, if possible start the next one or if not just display "Completed"
                if(index+1<workout.exercises.size)
                    startWorkout(workout, timerText, exerciseNameText, index+1)
                else
                {
                    exerciseNameText.text = "WORKOUT COMPLETE!"
                    timerText.visibility = View.INVISIBLE
                    findViewById<Button>(R.id.pauseButton).visibility = View.INVISIBLE
                    findViewById<Button>(R.id.resumeButton).visibility = View.INVISIBLE
                }
            }
        }.start()
    }
}

