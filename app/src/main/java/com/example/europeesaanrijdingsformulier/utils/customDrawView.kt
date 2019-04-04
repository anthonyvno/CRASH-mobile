package com.example.europeesaanrijdingsformulier.utils

import android.view.MotionEvent
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.europeesaanrijdingsformulier.R


class customDrawView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var bitmap: Bitmap


    var xcoord: Float = 0.toFloat()
    var ycoord: Float = 0.toFloat()
    var boolA: Boolean = false


    init {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.redcar)
        bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, false)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (event.x.toInt().toFloat() >= xcoord && event.x.toInt().toFloat() <= xcoord + 200
                    && event.y.toInt().toFloat() <= ycoord + 200 && event.y.toInt().toFloat() >= ycoord
                ) {
                    boolA = true
                }
                println("..................$xcoord......$ycoord") //x= 345 y=530
            }

            MotionEvent.ACTION_MOVE -> {

                if (boolA) {
                    xcoord = event.x.toInt().toFloat()
                    ycoord = event.y.toInt().toFloat()
                    println("..................$xcoord......$ycoord") //x= 345 y=530

                    invalidate()
                }


            }

            MotionEvent.ACTION_UP -> {

                if(boolA){
                    boolA=false
                }
                invalidate()
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        val paint = Paint()
        paint.setStyle(Paint.Style.FILL)
        paint.setColor(Color.CYAN)
        canvas.drawBitmap(bitmap, xcoord, ycoord, paint)  //originally bitmap draw at x=o and y=0
    }
}