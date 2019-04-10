package com.example.europeesaanrijdingsformulier.utils

import android.view.MotionEvent
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

import android.util.Log
import com.example.europeesaanrijdingsformulier.R
import android.graphics.Bitmap
import com.itextpdf.awt.geom.AffineTransform
import com.itextpdf.awt.geom.Rectangle


class customDrawView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), RotationGestureDetector.OnRotationGestureListener {


    private var rotationGestureDetector: RotationGestureDetector = RotationGestureDetector(this)

    var angle = 0F
    var oldAngle = 0F
    val carWidth = 200
    val carLength = 400
    var canvasW = 0F
    var canvasH = 0F
    val paint = Paint()


    //A
    var bitmapA: Bitmap
    var rectA = RectF()
    var xcoordA: Float = (-100).toFloat()
    var xcoordDownA: Float = 0.toFloat()
    var ycoordA: Float = (-100).toFloat()
    var ycoordDownA: Float = 0.toFloat()
    var boolA: Boolean = false

    //B
    var bitmapB: Bitmap
    var rectB = RectF()
    var xcoordB: Float = (-100).toFloat()
    var xcoordDownB: Float = 0.toFloat()
    var ycoordB: Float = (-100).toFloat()
    var ycoordDownB: Float = 0.toFloat()
    var boolB: Boolean = false


    init {
        bitmapA = BitmapFactory.decodeResource(context.getResources(), R.drawable.redcar)
        bitmapA = Bitmap.createScaledBitmap(bitmapA, carWidth, carLength, false)

        bitmapB = BitmapFactory.decodeResource(context.getResources(), R.drawable.greencar)
        bitmapB = Bitmap.createScaledBitmap(bitmapB, carWidth, carLength, false)

        paint.setStyle(Paint.Style.FILL)
        paint.setColor(Color.CYAN)

        this.isDrawingCacheEnabled = true

    }

    override fun OnRotation(rotationDetector: RotationGestureDetector) {
        if (boolA || boolB) {
            angle = rotationDetector.angle
            Log.d("Angle: ", angle.toString())
/*
        var rectA = Rectangle(xcoordA.toDouble(),ycoordA.toDouble(),carWidth.toDouble(),carLength.toDouble())
        var transformA = AffineTransform().rotate((-angle/180)*Math.PI,rectA.x,rectA.y)
        var regionA = Region().quick
*/
            val matrix = Matrix()

            matrix.postRotate(-angle, xcoordA + (carWidth / 2), ycoordA + (carLength / 2))
            //matrix.postRotate(-angle)

            if (boolA) {
                var bitmapA2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.redcar)
                bitmapA2 = Bitmap.createScaledBitmap(bitmapA2, carWidth, carLength, false)

                bitmapA =
                    Bitmap.createBitmap(bitmapA2, 0, 0, bitmapA2.width, bitmapA2.height, matrix, true)

            }
            if (boolB) {
                var bitmapA2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.greencar)
                bitmapA2 = Bitmap.createScaledBitmap(bitmapA2, carWidth, carLength, false)

                bitmapB =
                    Bitmap.createBitmap(bitmapA2, 0, 0, carWidth, carLength, matrix, true)
            }



            Log.d("RotationGestureDetector", "Rotation: " + java.lang.Float.toString(angle))
        }

    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        rotationGestureDetector.onTouchEvent(event);

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

                if (rectA.contains(event.x,event.y)
                ) {
                    xcoordDownA = event.x.toInt().toFloat() - xcoordA
                    ycoordDownA = event.y.toInt().toFloat() - ycoordA
                    boolA = true
                }
                if (rectB.contains(event.x, event.y)
                ) {
                    xcoordDownB = event.x.toInt().toFloat() - xcoordB
                    ycoordDownB = event.y.toInt().toFloat() - ycoordB
                    boolB = true
                }
            }

            MotionEvent.ACTION_MOVE -> {


                if (boolA) {
                    if (!isCollision(rectA,rectB)


                    ) {
                        if (-xcoordDownA + carWidth < canvasW + 1F
                            && event.y.toInt().toFloat() - ycoordDownA + carLength < canvasH + 1F
                            && event.x.toInt().toFloat() - xcoordDownA > -1F
                            && event.y.toInt().toFloat() - ycoordDownA > -1F
                        ) {
                            xcoordA = event.x.toInt().toFloat() - xcoordDownA
                            ycoordA = event.y.toInt().toFloat() - ycoordDownA
                        }
                    }


                }
                if (boolB) {
                    if (!isCollision(rectB,rectA)

                    ) {
                        if (event.x.toInt().toFloat() - xcoordDownB + carWidth < canvasW + 1F
                            && event.y.toInt().toFloat() - ycoordDownB + carLength < canvasH + 1F
                            && event.x.toInt().toFloat() - xcoordDownB > -1F
                            && event.y.toInt().toFloat() - ycoordDownB > -1F
                        ) {
                            xcoordB = event.x.toInt().toFloat() - xcoordDownB
                            ycoordB = event.y.toInt().toFloat() - ycoordDownB
                        }

                    }

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
        initiateCanvas(canvas)
        //println("$canvasW $canvasH")
        drawCarA(canvas)

        drawCarB(canvas)

        oldAngle = angle

    }

    private fun drawCarA(canvas: Canvas) {
        if (angle != oldAngle && boolA) {

            canvas.save()
            canvas.translate(xcoordA - bitmapA.width / 2, ycoordA - bitmapA.height / 2)
            canvas.rotate(-angle, (bitmapA.width / 2).toFloat(), (bitmapA.height / 2).toFloat())
            rectA = RectF(0F, 0F, carWidth.toFloat(), carLength.toFloat())
            println("er wordt gedraaid")
            if (angle >= 0F) {
                rectA.offset(
                    ((-carWidth / 2) * Math.sin(-angle * Math.PI / 180)).toFloat(),
                    ((carLength / 4) * Math.sin(-angle * Math.PI / 180)).toFloat()
                )

            } else {
                rectA.offset(
                    ((carWidth / 2) * Math.sin(-angle * Math.PI / 180)).toFloat(),
                    ((-carLength / 4) * Math.sin(-angle * Math.PI / 180)).toFloat()
                )

            }
            //canvas.drawRect(rectA, paint)
            //println("de gedraaide rect wordt getekend")
            canvas.restore()
            canvas.save()

            //println("de rect wordt bewogen")
            canvas.rotate(-angle, xcoordA, ycoordA)
            rectA.offset(xcoordA - carWidth / 2 - rectA.left, ycoordA - carLength / 2 - rectA.top)
            canvas.drawRect(rectA, paint)
            canvas.restore()

        } else if (boolA) {
            /*
            rectA = RectF(
                xcoordA - bitmapA.width / 2, ycoordA - bitmapA.height / 2
                , (xcoordA + bitmapA.width) - bitmapA.width / 2, (ycoordA + bitmapA.height) - bitmapA.height / 2
            )*/
            canvas.save()

            println("de rect wordt bewogen")
            canvas.rotate(-angle, xcoordA, ycoordA)
            rectA.offset(xcoordA - carWidth / 2 - rectA.left, ycoordA - carLength / 2 - rectA.top)
            canvas.drawRect(rectA, paint)
            canvas.restore()
        }

        canvas.drawBitmap(
            bitmapA,
            xcoordA - bitmapA.width / 2,
            ycoordA - bitmapA.height / 2,
            paint
        )
    }

    private fun drawCarB(canvas: Canvas) {
        if (angle != oldAngle && boolB) {

            canvas.save()
            canvas.translate(xcoordB - bitmapB.width / 2, ycoordB - bitmapB.height / 2)
            canvas.rotate(-angle, (bitmapB.width / 2).toFloat(), (bitmapB.height / 2).toFloat())
            rectB = RectF(0F, 0F, carWidth.toFloat(), carLength.toFloat())
            println("er wordt gedraaid")
            if (angle >= 0F) {
                rectB.offset(
                    ((-carWidth / 2) * Math.sin(-angle * Math.PI / 180)).toFloat(),
                    ((carLength / 4) * Math.sin(-angle * Math.PI / 180)).toFloat()
                )

            } else {
                rectB.offset(
                    ((carWidth / 2) * Math.sin(-angle * Math.PI / 180)).toFloat(),
                    ((-carLength / 4) * Math.sin(-angle * Math.PI / 180)).toFloat()
                )

            }
            //canvas.drawRect(rectA, paint)
            //println("de gedraaide rect wordt getekend")
            canvas.restore()
            canvas.save()

            //println("de rect wordt bewogen")
            canvas.rotate(-angle, xcoordB, ycoordB)
            rectB.offset(xcoordB - carWidth / 2 - rectB.left, ycoordB - carLength / 2 - rectB.top)
            canvas.drawRect(rectB, paint)
            canvas.restore()

        } else if (boolB) {
            /*
            rectA = RectF(
                xcoordA - bitmapA.width / 2, ycoordA - bitmapA.height / 2
                , (xcoordA + bitmapA.width) - bitmapA.width / 2, (ycoordA + bitmapA.height) - bitmapA.height / 2
            )*/
            canvas.save()

            println("de rect wordt bewogen")
            canvas.rotate(-angle, xcoordB, ycoordB)
            rectB.offset(xcoordB - carWidth / 2 - rectB.left, ycoordB - carLength / 2 - rectB.top)
            canvas.drawRect(rectB, paint)
            canvas.restore()
        }

        canvas.drawBitmap(
            bitmapB,
            xcoordB - bitmapB.width / 2,
            ycoordB - bitmapB.height / 2,
            paint
        )
    }

    private fun initiateCanvas(canvas: Canvas) {
        if (xcoordA <= -100 && ycoordA <= -100) {
            xcoordA = (canvas.width / 5).toFloat()
            ycoordA = (canvas.height / 3).toFloat()
            rectA = RectF(
                xcoordA - bitmapA.width / 2, ycoordA - bitmapA.height / 2
                , (xcoordA + bitmapA.width) - bitmapA.width / 2, (ycoordA + bitmapA.height) - bitmapA.height / 2
            )
        }
        if (xcoordB <= -100 && ycoordB <= -100) {
            xcoordB = ((canvas.width / 5) * 3).toFloat()
            ycoordB = (canvas.height / 3).toFloat()
            rectB = RectF(
                xcoordB - bitmapB.width / 2, ycoordB - bitmapA.height / 2
                , (xcoordB + bitmapB.width) - bitmapB.width / 2, (ycoordB + bitmapB.height) - bitmapB.height / 2
            )
        }
        canvasH = canvas.height.toFloat()
        canvasW = canvas.width.toFloat()

    }

    private fun isCollision(rect1: RectF, rect2:RectF):Boolean{
        val rect3 =  RectF(rect1)
        
        val rect4 = RectF(rect2)
        val bool = rect3.intersect(rect4)
        println(rect3.left)
        println(rect3.right)
        println(rect3.top)
        println(rect3.bottom)
        return bool
    }

    fun getBitmap(): Bitmap {
        return this.drawingCache
    }


}