package com.example.europeesaanrijdingsformulier.drawview


import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF

class MyRectangle(
    val carWidth: Float,
    val carLength: Float,
    val center: PointF,
    private val angle: Float? = 0F
) {

    fun getTopLeft(): PointF {

        val point = PointF()
        point.x =
            (center.x + ((-carWidth / 2) * Math.cos(angle!! * Math.PI / 180)) - ((carLength / 2) * Math.sin(angle * Math.PI / 180))).toFloat()
        point.y =
            (center.y + ((-carWidth / 2) * Math.sin(angle * Math.PI / 180)) + ((carLength / 2) * Math.cos(angle * Math.PI / 180))).toFloat()
        return point
    }

    fun getTopRight(): PointF {

        val point = PointF()
        point.x =
            (center.x + ((carWidth / 2) * Math.cos(angle!! * Math.PI / 180)) - ((carLength / 2) * Math.sin(angle * Math.PI / 180))).toFloat()
        point.y =
            (center.y + ((carWidth / 2) * Math.sin(angle * Math.PI / 180)) + ((carLength / 2) * Math.cos(angle * Math.PI / 180))).toFloat()
        return point
    }

    fun getBottomLeft(): PointF {

        val point = PointF()
        point.x =
            (center.x + ((-carWidth / 2) * Math.cos(angle!! * Math.PI / 180)) - ((-carLength / 2) * Math.sin(angle * Math.PI / 180))).toFloat()
        point.y =
            (center.y + ((-carWidth / 2) * Math.sin(angle * Math.PI / 180)) + ((-carLength / 2) * Math.cos(angle * Math.PI / 180))).toFloat()
        return point
    }

    fun getBottomRight(): PointF {

        val point = PointF()
        point.x =
            (center.x + ((carWidth / 2) * Math.cos(angle!! * Math.PI / 180)) - ((-carLength / 2) * Math.sin(angle * Math.PI / 180))).toFloat()
        point.y =
            (center.y + ((carWidth / 2) * Math.sin(angle * Math.PI / 180)) + ((-carLength / 2) * Math.cos(angle * Math.PI / 180))).toFloat()
        return point
    }

    fun getPoints(): Array<PointF> {
        return arrayOf(getTopLeft(), getTopRight(), getBottomRight(), getBottomLeft())
    }

    fun calculateMove(x: Float, y: Float): MyRectangle {
        return MyRectangle(this.carWidth, this.carLength, PointF(center.x + x, center.y + y), this.angle)
    }

    fun calculateRotate(newAngle: Float): MyRectangle {
        return MyRectangle(this.carWidth, this.carLength, center, -newAngle)
    }

    fun contains(point: PointF): Boolean {
        val noAngleRect = MyRectangle(carWidth, carLength, center)
        val tempRect = RectF(
            noAngleRect.getTopLeft().x,
            noAngleRect.getBottomRight().y,
            noAngleRect.getBottomRight().x,
            noAngleRect.getTopLeft().y
        )
        val newPoint = PointF(
            (center.x + ((point.x - center.x) * Math.cos(-angle!! * Math.PI / 180)) -
                    ((point.y - center.y) * Math.sin(-angle * Math.PI / 180))).toFloat(),
            (center.y + ((point.x - center.x) * Math.sin(-angle * Math.PI / 180)) +
                    ((point.y - center.y) * Math.cos(-angle * Math.PI / 180))).toFloat()

        )
        return tempRect.contains(newPoint.x.toInt().toFloat(),newPoint.y.toInt().toFloat())
       }

    fun drawOnCanvas(canvas: Canvas, paint: Paint) {
        val noAngleRect = MyRectangle(carWidth, carLength, center)
        val tempRect = RectF(
            noAngleRect.getTopLeft().x,
            noAngleRect.getBottomRight().y,
            noAngleRect.getBottomRight().x,
            noAngleRect.getTopLeft().y
        )

        canvas.save()
        canvas.rotate(angle!!, center.x, center.y)
        tempRect.offset(center.x - carWidth / 2 - tempRect.left, center.y - carLength / 2 - tempRect.top)
        canvas.drawRect(tempRect, paint)
        canvas.restore()

    }


}