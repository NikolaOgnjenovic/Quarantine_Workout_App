package com.mrmi.quarantineworkout

import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
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
    data class WorkoutClass(var exercises: MutableList<String>, var exerciseTimes: MutableList<Int>, var exerciseVideos: MutableList<Uri>)  //class template for any workout

    //Available workout sets consisting of exercise names and their durations
    private val legWorkout: WorkoutClass = WorkoutClass(
        mutableListOf("SQUATS", "HIGH KNEES", "REST", "LUNGES", "HIP BRIDGES", "REST", "SQUATS", "HIGH KNEES", "REST", "LUNGES", "HIP BRIDGES"),
        mutableListOf(30000, 30000, 30000, 30000, 30000, 30000, 30000, 30000, 30000, 30000, 30000),
        mutableListOf(
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/squats"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/high_knees"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/rest"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/lunge"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/hip_bridge"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/rest"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/squats"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/high_knees"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/rest"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/lunge"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/hip_bridge"))
    )
    private val shouldersWorkout: WorkoutClass = WorkoutClass(
        mutableListOf("PIKE PUSH-UPS", "ARM SCISSORS", "Y RAISES", "REVERSE IRON CROSS PUSH-UPS", "DOOR FRAME HOLDS"),
        mutableListOf(60000, 60000, 60000, 60000, 60000),
        mutableListOf(
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/pike_pushups"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/arm_scissors"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/y_raises"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/iron_cross"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/door_frame_hold"))
    )
    private val chestWorkout: WorkoutClass = WorkoutClass(
        mutableListOf("DIAMOND PUSH-UPS", "NORMAL PUSH-UPS", "REST", "TRICEP DIPS", "WIDE PUSH-UPS", "REST", "DIAMOND PUSH-UPS", "NORMAL PUSH-UPS", "REST", "TRICEP DIPS", "WIDE PUSH-UPS"),
        mutableListOf(30000, 30000, 30000, 30000, 30000, 30000, 20000, 20000, 25000, 20000, 20000),
        mutableListOf(
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/diamond_pushup"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/normal_pushup"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/rest"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/tricep_dips"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/wide_pushups"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/rest"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/diamond_pushup"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/normal_pushup"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/rest"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/tricep_dips"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/wide_pushups"))
    )
    private val bicepsWorkout: WorkoutClass = WorkoutClass(
        mutableListOf("LEFT ARM PUSH CURL", "LEFT ARM PUSH HAMMER CURL", "LEFT ARM PUSH REVERSE CURL", "LEFT ARM PUSH HIGH CURL",
            "RIGHT ARM PUSH CURL", "RIGHT ARM PUSH HAMMER CURL", "RIGHT ARM PUSH REVERSE CURL", "RIGHT ARM PUSH HIGH CURL", "PUSH UPRIGHT CURL"),
        mutableListOf(30000, 30000, 30000, 30000, 30000, 30000, 30000, 30000, 60000),
        mutableListOf(
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/one_arm_push_curl"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/one_arm_push_hammer_curl"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/one_arm_push_reverse_curl"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/one_arm_push_high_curl"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/one_arm_push_curl"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/one_arm_push_hammer_curl"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/one_arm_push_reverse_curl"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/one_arm_push_high_curl"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/push_upright_curl"))
    )
    private val backWorkout: WorkoutClass = WorkoutClass(
        mutableListOf("BACK EXTENSIONS", "BACK HYPEREXTENSIONS", "PULSE ROWS", "SUPERMANS", "REACHERS"),
        mutableListOf(30000, 30000, 30000, 30000, 30000),
        mutableListOf(
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/back_extension"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/back_hyperextension"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/pulse_rows"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/supermans"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/reachers")) 
    )
    private val tricepsWorkout: WorkoutClass = WorkoutClass(
        mutableListOf("TRICEP EXTENSIONS", "REST", "DIAMOND PUSH-UPS", "REST", "WALK OUT PUSH-UPS", "REST", "TRICEP DIPS"),
        mutableListOf(20000, 10000, 20000, 10000, 20000, 10000, 20000),
        mutableListOf(
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/tricep_extensions"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/rest"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/diamond_pushup"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/rest"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/walkout_pushups"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/rest"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/tricep_dips"))
    )
    private val absWorkout: WorkoutClass = WorkoutClass(
        mutableListOf("REGULAR CRUNCHES", "PLANK", "LYING KNEE TUCKS", "LEG UP CRUNCHES"),
        mutableListOf(30000, 90000, 30000, 30000),
        mutableListOf(
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/crunches"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/plank"),
            Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/lying_knee_tucks"), Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/leg_up_crunches"))
    )

    //Notification variables
    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    private val channelID = "com.mrmi.quarantineworkout"
    private val description = "Quarantine workout notification"

    private lateinit var cdt: CountDownTimer //CountDownTimer object so the timer is disabled once the acitvity is destroyed
    private var timeLeft = 0 //Time left for an exercise, used for pausing and resuming
    private var isPaused = false
    private var lastButtonClickTime = 0 //Prevent double clicking on buttons to mess up the timers

    private lateinit var videoView: VideoView //Video View which plays exercise videos

    var selectedMode = "" //Day/night system mode used for notification text color

    var currentIndex = 0 //Index of current exercise in selected workout (expanded by number of sets)

    private var newWorkout : WorkoutClass = WorkoutClass(mutableListOf(), mutableListOf(), mutableListOf())

    private lateinit var timerText : TextView
    private lateinit var exerciseNameText: TextView

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
        timerText = findViewById(R.id.timerText)
        exerciseNameText = findViewById(R.id.exerciseName)
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
                //Invalid set number input
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
                //Too many set workouts inputted
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

                videoView.visibility = View.VISIBLE
                videoView.start()

                //Enable pause button, disable resume and start button, set isPaused to false, hide set number input field and text, disable minus and plus button and warning text
                it.visibility = View.GONE
                isPaused = false
                pauseButton.visibility = View.VISIBLE
                resumeButton.visibility = View.GONE
                setNumberText.visibility = View.GONE
                setNumberTitleText.visibility = View.GONE
                minusButton.visibility = View.GONE
                plusButton.visibility = View.GONE
                setNumberWarningText.visibility = View.GONE

                //Change number of sets to input if input is bigger than 1 and disable input edit text
                currentIndex = 0
                val intent = intent //Get intent from workoutMenu
                lateinit var currentWorkout: WorkoutClass //Get selected workout
                when(intent.getStringExtra("Workout")) //Check the workout given in the intent and assign it to currentWorkout
                {
                    "legs" -> currentWorkout = legWorkout
                    "shoulders" -> currentWorkout = shouldersWorkout
                    "chest" -> currentWorkout = chestWorkout
                    "biceps" -> currentWorkout = bicepsWorkout
                    "back" -> currentWorkout = backWorkout
                    "triceps" -> currentWorkout = tricepsWorkout
                    "abs" -> currentWorkout = absWorkout
                }

                //Add first set to workout (by adding all elements of currently selected workout to empty new workout)
                newWorkout.exercises.addAll(currentWorkout.exercises)
                newWorkout.exerciseTimes.addAll(currentWorkout.exerciseTimes)
                newWorkout.exerciseVideos.addAll(currentWorkout.exerciseVideos)

                //If there is more than 1 set to complete, add 2.5 minute rest between sets and append selected workout setNumber-1 times to newWorkout
                repeat(setNumber-1)
                {
                    //Add rest and next set's exercises
                    newWorkout.exercises.add("Rest")
                    newWorkout.exercises.addAll(currentWorkout.exercises)

                    //Add rest time and next set's exercises timers
                    newWorkout.exerciseTimes.add(15000)
                    newWorkout.exerciseTimes.addAll(currentWorkout.exerciseTimes)

                    //Add rest video and next set's exercise videos
                    newWorkout.exerciseVideos.add(Uri.parse("android.resource://com.mrmi.quarantineworkout/raw/rest"))
                    newWorkout.exerciseVideos.addAll(currentWorkout.exerciseVideos)
                }
                //Start new workout (selected workout done setNumber times)
                startWorkout()
            }
        }

        pauseButton.setOnClickListener()
        {
            //Prevent rapid clicking, using 1 second cooldown between clicks
            if(SystemClock.elapsedRealtime()-lastButtonClickTime>=1000)
            {
                lastButtonClickTime = SystemClock.elapsedRealtime().toInt()

                //Disable pause and start buttons, enable resume button, set paused to true
                it.visibility = View.GONE
                isPaused = true
                resumeButton.visibility = View.VISIBLE
            }
        }

        resumeButton.setOnClickListener()
        {
            //Prevent rapid clicking, using 1 second cooldown between clicks
            if (SystemClock.elapsedRealtime() - lastButtonClickTime >= 1000)
            {
                lastButtonClickTime = SystemClock.elapsedRealtime().toInt()

                //Disable resume button, enable pause button and resume workout
                it.visibility = View.GONE
                pauseButton.visibility = View.VISIBLE

                startWorkout()
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

    //Resume video on activity resume (video used to display a black image once the user reopened the app via the recent apps screen)
    override fun onResume()
    {
        super.onResume()
        if(newWorkout.exercises.size>1)
        {
            videoView.setVideoURI(newWorkout.exerciseVideos[currentIndex])
            videoView.start()
        }
    }

    //Alert user if he wants to quit the workout on back button press
    override fun onBackPressed()
    {
        if(currentIndex!=newWorkout.exercises.size)
        {
            var builder = AlertDialog.Builder(this@WorkoutActivity)
                .setMessage("Are you sure you want to exit this workout?")
                .setCancelable(true)
                .setNegativeButton("No") {DI: DialogInterface, i: Int -> DI.cancel()}
                .setPositiveButton("Yes") {DI: DialogInterface, i: Int -> finish()}
            builder.create()
            builder.show()
        }
    }

    private fun startWorkout()
    {
        val exercise = newWorkout.exercises[currentIndex]
        exerciseNameText.text = exercise

        videoView.setVideoURI(newWorkout.exerciseVideos[currentIndex])
        var adjustedExerciseTime = newWorkout.exerciseTimes[currentIndex]

        if(isPaused)
        {
            adjustedExerciseTime=timeLeft
            isPaused = false
        }

        val contentView = RemoteViews(packageName, R.layout.notificationlayout)
        contentView.setTextViewText(R.id.notificationTitle, "Quarantine Workout")

        cdt = object : CountDownTimer(adjustedExerciseTime.toLong(), 1000) //Start decreasing the adjusted time by 1 second until it hits 0
        {
            override fun onTick(millisUntilFinished: Long)
            {
                //Set timer text to remaining exercise time
                if(millisUntilFinished>=60000)
                    timerText.text = (millisUntilFinished/60000).toString() + "m" + ((millisUntilFinished%60000)/1000).toString() + "s"
                else
                    timerText.text = (millisUntilFinished/1000).toString() + "s"

                if(isPaused)
                {
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

                    //Set notification content text to remaining exercise time
                    var notificationText = "Current exercise: " + exercise + ".\nRemaining time: "
                    if(millisUntilFinished>=60000)
                        notificationText += (millisUntilFinished/60000).toString() + " min. and " + ((millisUntilFinished%60000)/1000).toString() + " seconds."
                    else
                        notificationText += (millisUntilFinished/1000).toString() + " seconds."
                    contentView.setTextViewText(R.id.notificationContent, notificationText)

                    notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                    //Using notificationChannel in android versions after and including Oreo
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    {
                        notificationChannel = NotificationChannel(channelID, description, NotificationManager.IMPORTANCE_MIN)
                        notificationChannel.enableVibration(false)
                        notificationManager.createNotificationChannel(notificationChannel)

                        builder = Notification.Builder(this@WorkoutActivity, channelID)
                            .setContent(contentView)
                            .setSmallIcon(R.drawable.notification_image)
                            .setLargeIcon(BitmapFactory.decodeResource(this@WorkoutActivity.resources,  R.mipmap.ic_launcher))
                    }
                    else
                    {
                        builder = Notification.Builder(this@WorkoutActivity)
                            .setContent(contentView)
                            .setSmallIcon(R.drawable.notification_image)
                            .setLargeIcon(BitmapFactory.decodeResource(this@WorkoutActivity.resources,  R.mipmap.ic_launcher))
                    }

                    notificationManager.notify(310, builder.build())
                }
            }

            override fun onFinish()
            {
                notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(310)

                //Once the exercise is finished, if possible start the next one or if not just display "Completed"
                if(currentIndex+1<newWorkout.exercises.size)
                {
                    currentIndex++
                    startWorkout()
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

