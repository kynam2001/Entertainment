package com.example.ringtuneandwallpaper.utility

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

open class OnSwipeTouchListener(context: Context): View.OnTouchListener {

    private val gestureDetector = GestureDetector(context, GestureListenter())

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(motionEvent)
    }
    private inner class GestureListenter: GestureDetector.SimpleOnGestureListener(){
        private val SWIPE_THRESHOLD = 150
        private val SWIPE_VELOCITY_THRESHOLD = 150

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val diffX = e2.x - e1!!.x
            val diffY = e2.y - e1.y
            if(diffX > 0 && Math.abs(diffX) > Math.abs(diffY) && Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD){
                onSwipeRight()
                return true
            }
            if(diffX < 0 && Math.abs(diffX) > Math.abs(diffY) && Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD){
                onSwipeLeft()
                return true
            }
            return false
        }


    }

    open fun onSwipeRight(){}
    open fun onSwipeLeft(){}

}