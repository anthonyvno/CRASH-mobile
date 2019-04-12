package com.example.europeesaanrijdingsformulier.drawview


import android.graphics.PointF
import android.graphics.RectF

class MyRectangle() : RectF() {


    val carWidth = 200
    val carLength = 400
    var angle = 0F
    var oldAngle = 0F

    //var bitmapA: Bitmap
    var xcoord: Float = (-100).toFloat()
    var xcoordDown: Float = 0.toFloat()
    var ycoord: Float = (-100).toFloat()
    var ycoordDown: Float = 0.toFloat()

    var left = 0F
    var top = 0F
    var right = 0F
    var bottom = 0F

    var lb = PointF(0F, 0F)
    var rb = PointF(0F, 0F)
    var lo = PointF(0F, 0F)
    var ro = PointF(0F, 0F)

    override fun set(left: Float, top: Float, right: Float, bottom: Float) {
        super.set(left, top, right, bottom)

        //lb.set(xcoord+((carWidth/2)*Math.cos(angle))
    }


}