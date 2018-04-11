package com.lab73.sligamer.beefindsfinger

/**
 * Created by Justin Freres on 4/10/2018.
 * Lab 7-3 Bee Finds Finger
 * Bee Class
 * Plugin Support with kotlin_version = '1.2.31'
 */
class Bee {

    // DECLARE VARIABLES
    private var mX: Int = 0
    private var mY: Int = 0
    private var mVelocity: Int = 0


    // GET SET METHODS
    fun setVelocity(velocity: Int){
        mVelocity = velocity
    }

    fun getVelocity(): Int{
        return mVelocity
    }

    fun setX(x: Int){
        mX = x
    }

    fun getX(): Int{
        return mX
    }

    fun setY(y: Int){
        mY = y
    }

    fun getY(): Int{
        return mY
    }

    fun move(destX: Int, destY: Int){
        var distX = destX - mX
        var distY = destY - mY

        mX += distX / mVelocity
        mY += distY / mVelocity
    }

}