package com.mrmi.quarantineworkout

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.RemoteViews
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity


class WorkoutActivity : AppCompatActivity()
{
    data class WorkoutClass(var exercises: List<String>, var exerciseTimes: List<Int>, var exerciseVideos: List<Uri>)  //class template for any workout

    //Available workout sets consisting of exercise names and their durations
    private val legWorkout: WorkoutClass = WorkoutClass(
        listOf("SQUATS", "HIGH KNEES", "REST", "LUNGES", "HIP BRIDGES", "REST", "SQUATS", "HIGH KNEES", "REST", "LUNGES", "HIP BRIDGES"),
        listOf(30000, 30000, 30000, 30000, 30000, 30000, 30000, 30000, 30000, 30000, 30000),
        listOf(
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/squats"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/high_knees"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/rest"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/lunge"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/hip_bridge"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/rest"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/squats"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/high_knees"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/rest"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/lunge"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/hip_bridge"))
    )
    private val shouldersWorkout: WorkoutClass = WorkoutClass(
        listOf("PIKE PUSH-UPS", "ARM SCISSORS", "Y RAISES", "REVERSE IRON CROSS PUSH-UPS", "DOOR FRAME HOLDS"),
        listOf(60000, 60000, 60000, 60000, 60000),
        listOf(
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/pike_pushups"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/arm_scissors"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/y_raises"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/iron_cross"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/door_frame_hold"))
    )
    private val chestWorkout: WorkoutClass = WorkoutClass(
        listOf("DIAMOND PUSH-UPS", "NORMAL PUSH-UPS", "REST", "TRICEP DIPS", "WIDE PUSH-UPS", "REST", "DIAMOND PUSH-UPS", "NORMAL PUSH-UPS", "REST", "TRICEP DIPS", "WIDE PUSH-UPS"),
        listOf(30000, 30000, 30000, 30000, 30000, 30000, 20000, 20000, 25000, 20000, 20000),
        listOf(
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/diamond_pushup"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/normal_pushup"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/rest"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/tricep_dips"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/wide_pushups"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/rest"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/diamond_pushup"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/normal_pushup"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/rest"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/tricep_dips"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/wide_pushups"))
    )
    private val bicepsWorkout: WorkoutClass = WorkoutClass(
        listOf("LEFT ARM PUSH CURL", "LEFT ARM PUSH HAMMER CURL", "LEFT ARM PUSH REVERSE CURL", "LEFT ARM PUSH HIGH CURL",
            "RIGHT ARM PUSH CURL", "RIGHT ARM PUSH HAMMER CURL", "RIGHT ARM PUSH REVERSE CURL", "RIGHT ARM PUSH HIGH CURL", "PUSH UPRIGHT CURL"),
        listOf(30000, 30000, 30000, 30000, 30000, 30000, 30000, 30000, 60000),
        listOf(
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/one_arm_push_curl"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/one_arm_push_hammer_curl"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/one_arm_push_reverse_curl"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/one_arm_push_high_curl"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/one_arm_push_curl"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/one_arm_push_hammer_curl"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/one_arm_push_reverse_curl"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/one_arm_push_high_curl"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/push_upright_curl"))
    )
    private val backWorkout: WorkoutClass = WorkoutClass(
        listOf("BACK EXTENSIONS", "BACK HYPEREXTENSIONS", "PULSE ROWS", "SUPERMANS", "REACHERS"),
        listOf(30000, 30000, 30000, 30000, 30000),
        listOf(
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/back_extension"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/back_hyperextension"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/pulse_rows"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/supermans"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/reachers"))
    )
    private val tricepsWorkout: WorkoutClass = WorkoutClass(
        listOf("TRICEP EXTENSIONS", "REST", "DIAMOND PUSH-UPS", "REST", "WALK OUT PUSH-UPS", "REST", "TRICEP DIPS"),
        listOf(5000, 5000, 5000, 5000, 5000, 5000, 5000),
        listOf(
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/tricep_extensions"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/rest"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/diamond_pushup"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/rest"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/walkout_pushups"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/rest"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/tricep_dips"))
    )
    private val absWorkout: WorkoutClass = WorkoutClass(
        listOf("REGULAR CRUNCHES", "PLANK", "LYING KNEE TUCKS", "LEG UP CRUNCHES"),
        listOf(30000, 150000, 30000, 30000),
        listOf(
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/crunches"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/plank"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/lying_knee_tucks"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/leg_up_crunches"))
    )
    private var timeLeft = 0 //Time left for an exercise, used for pausing and resuming
    private var isPaused = false
    private var lastButtonClickTime = 0 //Prevent double clicking on buttons to mess up the timers

    //Notification variables
    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    private val channelID = "com.mrmi.quarantineworkout"
    private val description = "Quarantine workout notification"

    private lateinit var cdt: CountDownTimer //CountDownTimer object so the timer is disabled once the acitvity is destroyed

    private lateinit var videoView: VideoView

    var selectedMode = ""

    var currentIndex = 0

    private lateinit var newWorkout : WorkoutClass

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(310)

        //Check which mode the system is using (regular or night mode)
        when (this@WorkoutActivity?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK))
        {
            Configuration.UI_MODE_NIGHT_YES -> selectedMode="night"
            Configuration.UI_MODE_NIGHT_NO -> selectedMode="day"
            Configuration.UI_MODE_NIGHT_UNDEFINED -> selectedMode="undefined"
        }

        //Text Views
        val timerText: TextView = findViewById(R.id.timerText)
        val exerciseNameText: TextView = findViewById(R.id.exerciseName)
        val setNumberWarningText: TextView = findViewById(R.id.setNumberWarning)
        setNumberWarningText.visibility = View.GONE
        val setNumberText: TextView = findViewById(R.id.setNumberText)
        val setNumberTitleText: TextView = findViewById(R.id.setNumberTitleText)

        //Video view
        videoView = findViewById(R.id.exerciseVideo)
        videoView.visibility = View.GONE
        videoView.setOnPreparedListener { mp -> mp.isLooping = true }

        //Buttons and their onClickListeners below
        val startButton: Button = findViewById(R.id.startButton)
        startButton.text = "START"
        isPaused=false
        val pauseButton: Button = findViewById(R.id.pauseButton)
        pauseButton.text = "PAUSE"
        pauseButton.visibility = View.GONE
        val resumeButton: Button = findViewById(R.id.resumeButton)
        resumeButton.text = "RESUME"
        resumeButton.visibility = View.GONE
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

                videoView.visibility = View.VISIBLE
                videoView.start()
                //Enable pause button, disable resume and start button, set isPaused to false, hide set number input field and text, disable minus and plus button and warning text
                it.visibility = View.GONE
                pauseButton.visibility = View.VISIBLE
                isPaused = false
                resumeButton.visibility = View.GONE
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
                    "abs" -> currentWorkout = absWorkout
                }
                newWorkout = currentWorkout

                //If there is more than 1 set to complete, add 2.5 minute rest between sets and append sets to new workout
                for(set in 2..setNumber)
                {
                    newWorkout.exercises = newWorkout.exercises + "Rest" + currentWorkout.exercises
                    newWorkout.exerciseTimes = newWorkout.exerciseTimes + 15000 + currentWorkout.exerciseTimes //Add 2.5 minute rest between sets
                    newWorkout.exerciseVideos = newWorkout.exerciseVideos + Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/rest") + currentWorkout.exerciseVideos
                }

                //Start new workout (selected workout used setNumber times)
                startWorkout(newWorkout, timerText, exerciseNameText, currentIndex)
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
                it.visibility = View.GONE
                isPaused = true
                resumeButton.visibility = View.VISIBLE
            }
        }

        resumeButton.setOnClickListener()
        {
            // Prevent rapid clicking, using 1 second cooldown between clicks
            if (SystemClock.elapsedRealtime() - lastButtonClickTime >= 1000) {
                lastButtonClickTime = SystemClock.elapsedRealtime().toInt()

                println("Resuming")
                println("Current set: " + currentIndex+1)
                //Disable resume button, enable pause button and resume workout
                it.visibility = View.GONE
                pauseButton.visibility = View.VISIBLE

                val intent = intent
                /*(intent.getStringExtra("Workout")) //Check the workout given in the intent and start it
                {
                    "legs" -> currentWorkout = legWorkout
                    "shoulders" -> currentWorkout = shouldersWorkout
                    "chest" -> currentWorkout = chestWorkout
                    "biceps" -> currentWorkout = bicepsWorkout
                    "back" -> currentWorkout = backWorkout
                    "triceps" -> currentWorkout = tricepsWorkout
                    "abs" -> currentWorkout = absWorkout
                }
                //var newWorkout: WorkoutClass = currentWorkout

                //If there is more than 1 set to complete, add 2.5 minute rest between sets and append sets to new workout
                //for(set in (setNumber-currentIndex)..setNumber)
                //{
                 //   newWorkout.exercises = newWorkout.exercises + "Rest" + currentWorkout.exercises
                 //   newWorkout.exerciseTimes = newWorkout.exerciseTimes + 150000 + currentWorkout.exerciseTimes //Add 2.5 minute rest between sets
                //}
                    */
                println("Resuming new workout at index: " + currentIndex)
                startWorkout(newWorkout, timerText, exerciseNameText, currentIndex)
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

        videoView.setVideoURI(workout.exerciseVideos[index])
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
                    //Change notification text color depending on if the phone's on night or day mode (or undefined)
                    if(selectedMode=="night")
                    {
                        contentView.setTextColor(R.id.notificationContent, Color.WHITE)
                        contentView.setTextColor(R.id.notificationTitle, Color.WHITE)
                    }
                    else if(selectedMode=="day")
                    {
                        contentView.setTextColor(R.id.notificationContent, Color.BLACK)
                        contentView.setTextColor(R.id.notificationTitle, Color.BLACK)
                    }
                    else
                    {
                        contentView.setTextColor(R.id.notificationContent, resources.getColor(R.color.colorPrimary))
                        contentView.setTextColor(R.id.notificationTitle, resources.getColor(R.color.colorPrimary))
                    }

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
                            .setLargeIcon(BitmapFactory.decodeResource(this@WorkoutActivity.resources,  R.drawable.notification_icon))
                            .setSmallIcon(R.drawable.notification_icon)
                    }
                    else
                    {
                        builder = Notification.Builder(this@WorkoutActivity)
                            .setContent(contentView)
                            .setLargeIcon(BitmapFactory.decodeResource(this@WorkoutActivity.resources,  R.drawable.notification_icon))
                            .setSmallIcon(R.drawable.notification_icon)
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
                {
                    currentIndex++
                    startWorkout(workout, timerText, exerciseNameText, index+1)
                }
                else
                {
                    exerciseNameText.text = "WORKOUT COMPLETE!"
                    timerText.visibility = View.INVISIBLE
                    findViewById<VideoView>(R.id.exerciseVideo).visibility = View.INVISIBLE
                    findViewById<Button>(R.id.pauseButton).visibility = View.INVISIBLE
                    findViewById<Button>(R.id.resumeButton).visibility = View.INVISIBLE
                }
            }
        }.start()
    }
}

