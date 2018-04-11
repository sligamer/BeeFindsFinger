package com.lab73.sligamer.beefindsfinger

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.constraint.ConstraintLayout
import android.view.*
import android.widget.ImageView

/**
 * Created by Justin Freres on 4/10/2018.
 * Lab 7-3 Bee Finds Finger
 * Plugin Support with kotlin_version = '1.2.31'
 */
class MainActivity : AppCompatActivity() {

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // TASK 1: IDENTIFY THE TOUCH ACTION BEING PERFORMED
        var touchAction = event!!.actionMasked

        // TASK 2: RESPOND TO POSSIBLE TOUCH EVENTS
        when(touchAction){
            MotionEvent.ACTION_DOWN -> {
                xLocation = event.x.toInt()
                yLocation = event.y.toInt()
            }
            MotionEvent.ACTION_UP -> {
                xLocation = mFlower.getX()
                yLocation = mFlower.getY()
            }
            MotionEvent.ACTION_MOVE -> {
                xLocation = event.x.toInt()
                yLocation = event.y.toInt()
            }
        }
        return true
    }

    // ACTIVITY WORK IS SPLIT INTO TWO THREADS
    // CALC BEE MOVEMENT    - BACKGROUND
    // POSITIONING THE BEE - UI THREAD

    // DECLARE VARIABLES
    private lateinit var calculateThread: Thread

    private lateinit var mainLayout: ConstraintLayout
    private lateinit var beeImageView: ImageView
    private lateinit var flowerImageView: ImageView

    private lateinit var mFlower: Flower
    private lateinit var mBee: Bee

    private var xLocation: Int = 0
    private var yLocation: Int = 0

    companion object{
        const val DELAY = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TASK 1: WINDOW PROP SET
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // TASK 2: SET THE LAYOUT
        setContentView(R.layout.activity_main)
        mainLayout = findViewById(R.id.constrainLayout)


        // TASK 3: INSTANTIATE THE FLOWER AND BEE
        xLocation = 200
        yLocation = 200
        addFlower()
        buildBee()

        // TASK 4: INITIALIZE THE CALC THREAD
        calculateThread = Thread(calcAction)

    }

    private fun addFlower() {

        // TASK 1: CREATE A LAYOUT INFLATER
        layoutInflater.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)

        // TASK 2: SPECIFY FLOWER POSITION
        var initialXPosition = xLocation
        var initialYPosition = yLocation

        mFlower = Flower()
        mFlower.setX(initialXPosition)
        mFlower.setY(initialYPosition)

        // TASK 3: ADD FLOWER
        flowerImageView = layoutInflater.inflate(R.layout.flower_image, null) as ImageView
        flowerImageView.x = mFlower.getX().toFloat()
        flowerImageView.y = mFlower.getY().toFloat()

        mainLayout.addView(flowerImageView,0)
    }

    private fun buildBee() {

        // TASK 1: CREATE A LAYOUT INFLATER TO
        // ADD VISUAL VIEW TO THE LAYOUT
        layoutInflater.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)

        // TASK 2:  SPECIFY BEE ATTRIBUTES
        var initialXPosition = xLocation
        var initialYPosition = yLocation
        var proportionalVelocity = 10

        mBee = Bee()
        mBee.setX(initialXPosition)
        mBee.setY(initialYPosition)
        mBee.setVelocity(proportionalVelocity)

        // TASK 3: ADD BEE
        beeImageView = layoutInflater.inflate(R.layout.bee_image, null) as ImageView
        beeImageView.x = mBee.getX().toFloat()
        beeImageView.y = mBee.getY().toFloat()

        mainLayout.addView(beeImageView, 0)

    }



    override fun onResume() {
        calculateThread.start()
        super.onResume()
    }

    override fun onPause() {
        finish()
        super.onPause()
    }

    override fun onDestroy() {
        finish()
        super.onDestroy()
    }


    private val calcAction: Runnable = Runnable {
        try {
            while (true) {
                mBee.move(xLocation, yLocation)
                Thread.sleep(DELAY.toLong())
                threadHandler.sendEmptyMessage(0)
            }
        } catch (e: InterruptedException){
            e.printStackTrace()
        }
    }

    private val threadHandler: Handler = object: Handler() {
        override fun handleMessage(msg: Message?) {
            // SET THE BEE AT TEH CORRECT XY LOCATIONS
            beeImageView.x = mBee.getX().toFloat()
            beeImageView.y = mBee.getY().toFloat()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item!!.itemId
        if(id == R.string.action_settings){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
