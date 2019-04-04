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


    //A
    var bitmapA: Bitmap
    var xcoordA: Float = 0.toFloat()
    var xcoordDownA: Float = 0.toFloat()
    var ycoordA: Float = 0.toFloat()
    var ycoordDownA: Float = 0.toFloat()
    var boolA: Boolean = false

    //B
    var bitmapB: Bitmap
    var xcoordB: Float = 300.toFloat()
    var xcoordDownB: Float = 0.toFloat()
    var ycoordB: Float = 300.toFloat()
    var ycoordDownB: Float = 0.toFloat()
    var boolB: Boolean = false


    init {
        bitmapA = BitmapFactory.decodeResource(context.getResources(), R.drawable.redcar)
        bitmapA = Bitmap.createScaledBitmap(bitmapA, 150, 200, false)

        bitmapB = BitmapFactory.decodeResource(context.getResources(), R.drawable.greencar)
        bitmapB = Bitmap.createScaledBitmap(bitmapB, 150, 200, false)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (event.x.toInt().toFloat() >= xcoordA && event.x.toInt().toFloat() <= xcoordA + 150
                    && event.y.toInt().toFloat() <= ycoordA + 200 && event.y.toInt().toFloat() >= ycoordA
                ) {
                    xcoordDownA = event.x.toInt().toFloat() - xcoordA
                    ycoordDownA = event.y.toInt().toFloat() - ycoordA
                    boolA = true
                }
                if (event.x.toInt().toFloat() >= xcoordB && event.x.toInt().toFloat() <= xcoordB + 150
                    && event.y.toInt().toFloat() <= ycoordB + 200 && event.y.toInt().toFloat() >= ycoordB
                ) {
                    xcoordDownB = event.x.toInt().toFloat() - xcoordB
                    ycoordDownB = event.y.toInt().toFloat() - ycoordB
                    boolB = true
                }
            }

            MotionEvent.ACTION_MOVE -> {

                if (boolA) {
                    if (event.x.toInt().toFloat()-xcoordDownA + 150 < xcoordB
                        || event.y.toInt().toFloat()-ycoordDownA + 200 < ycoordB
                        || event.x.toInt().toFloat()-xcoordDownA > xcoordB + 150
                        || event.y.toInt().toFloat()-ycoordDownA > ycoordB + 200
                    ) {
                        xcoordA = event.x.toInt().toFloat() - xcoordDownA
                        ycoordA = event.y.toInt().toFloat() - ycoordDownA
                    }


                }
                if (boolB) {
                    xcoordB = event.x.toInt().toFloat() - xcoordDownB
                    ycoordB = event.y.toInt().toFloat() - ycoordDownB


                }
                invalidate()


            }

            MotionEvent.ACTION_UP -> {

                if (boolA) {
                    boolA = false
                }
                if (boolB) {
                    boolB = false
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
        canvas.drawBitmap(bitmapB, xcoordB, ycoordB, paint)
        canvas.drawBitmap(bitmapA, xcoordA, ycoordA, paint)  //originally bitmap draw at x=o and y=0
    }
}