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
) : View(context, attrs, defStyleAttr),RotationGestureDetector.OnRotationGestureListener {





    private var rotationGestureDetector: RotationGestureDetector = RotationGestureDetector(this)

    var angle = 0F
    val carWidth = 200
    val carLength = 350
    var canvasW = 0F
    var canvasH = 0F


    //A
    var bitmapA: Bitmap
    var xcoordA: Float = (-100).toFloat()
    var xcoordDownA: Float = (-100).toFloat()
    var ycoordA: Float = (-100).toFloat()
    var ycoordDownA: Float = (-100).toFloat()
    var boolA: Boolean = false

    //B

    var bitmapB: Bitmap
    var xcoordB: Float = (-100).toFloat()
    var xcoordDownB: Float = (-100).toFloat()
    var ycoordB: Float = (-100).toFloat()
    var ycoordDownB: Float = (-100).toFloat()
    var boolB: Boolean = false


    init {
        bitmapA = BitmapFactory.decodeResource(context.getResources(), R.drawable.redcar)
        bitmapA = Bitmap.createScaledBitmap(bitmapA, carWidth, carLength, false)

        bitmapB = BitmapFactory.decodeResource(context.getResources(), R.drawable.greencar)
        bitmapB = Bitmap.createScaledBitmap(bitmapB, carWidth, carLength, false)

        this.isDrawingCacheEnabled = true

    }

    override fun OnRotation(rotationDetector: RotationGestureDetector) {
        angle = rotationDetector.angle
        Log.d("Angle: ", angle.toString())
/*
        var rectA = Rectangle(xcoordA.toDouble(),ycoordA.toDouble(),carWidth.toDouble(),carLength.toDouble())
        var transformA = AffineTransform().rotate((-angle/180)*Math.PI,rectA.x,rectA.y)
        var regionA = Region().quick
*/
        val matrix = Matrix()
        
        matrix.postRotate(-angle,xcoordA+(carWidth/2),ycoordA+(carLength/2))
        //matrix.postRotate(-angle)

        if(boolA){
            var bitmapA2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.redcar)
            bitmapA2 = Bitmap.createScaledBitmap(bitmapA2, carWidth, carLength, false)

            bitmapA =
                Bitmap.createBitmap(bitmapA2, 0, 0, bitmapA2.width, bitmapA2.height, matrix, true)
/*
            println("angle: $angle")
            println("X voor rotation: $xcoordA")
            println("Y voor rotation: $ycoordA")



            xcoordA = ((xcoordA+carLength/2)+
                    ((Math.sqrt(Math.pow(carLength.toDouble(),2.0)+Math.pow(carLength.toDouble(),2.0)))/2)
                    *Math.cos(angle2.toDouble()*Math.PI)).toFloat()
            ycoordA = ((ycoordA+carLength/2)+
                    ((Math.sqrt(Math.pow(carLength.toDouble(),2.0)+Math.pow(carLength.toDouble(),2.0)))/2)
                    *Math.sin(angle2.toDouble()*Math.PI)).toFloat()

            println("X na rotation: $xcoordA")
            println("Y na rotation: $ycoordA")*/
        }
        if(boolB){
            var bitmapA2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.greencar)
            bitmapA2 = Bitmap.createScaledBitmap(bitmapA2, carWidth, carLength, false)

            bitmapB =
                Bitmap.createBitmap(bitmapA2, 0, 0, carWidth, carLength, matrix, true)
        }



        Log.d("RotationGestureDetector", "Rotation: " + java.lang.Float.toString(angle))
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        rotationGestureDetector.onTouchEvent(event);

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (event.x.toInt().toFloat() >= xcoordA && event.x.toInt().toFloat() <= xcoordA + carWidth
                    && event.y.toInt().toFloat() <= ycoordA + carLength && event.y.toInt().toFloat() >= ycoordA
                ) {
                    xcoordDownA = event.x.toInt().toFloat() - xcoordA
                    ycoordDownA = event.y.toInt().toFloat() - ycoordA
                    boolA = true
                }
                if (event.x.toInt().toFloat() >= xcoordB && event.x.toInt().toFloat() <= xcoordB + carWidth
                    && event.y.toInt().toFloat() <= ycoordB + carLength && event.y.toInt().toFloat() >= ycoordB
                ) {
                    xcoordDownB = event.x.toInt().toFloat() - xcoordB
                    ycoordDownB = event.y.toInt().toFloat() - ycoordB
                    boolB = true
                }
            }

            MotionEvent.ACTION_MOVE -> {


                if (boolA) {
                    if (event.x.toInt().toFloat() - xcoordDownA + carWidth < xcoordB
                        || event.y.toInt().toFloat() - ycoordDownA + carLength < ycoordB
                        || event.x.toInt().toFloat() - xcoordDownA > xcoordB + carWidth
                        || event.y.toInt().toFloat() - ycoordDownA > ycoordB + carLength
                    ) {
                        if (event.x.toInt().toFloat() - xcoordDownA + carWidth < canvasW + 1F
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
                    if (event.x.toInt().toFloat() - xcoordDownB + carWidth < xcoordA
                        || event.y.toInt().toFloat() - ycoordDownB + carLength < ycoordA
                        || event.x.toInt().toFloat() - xcoordDownB > xcoordA + carWidth
                        || event.y.toInt().toFloat() - ycoordDownB > ycoordA + carLength
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
        if (xcoordA <= -100 && ycoordA <= -100) {
            xcoordA = (canvas.width / 5).toFloat()
            ycoordA = (canvas.height / 3).toFloat()
        }
        if (xcoordB <= -100 && ycoordB <= -100) {
            xcoordB = ((canvas.width / 5) * 3).toFloat()
            ycoordB = (canvas.height / 3).toFloat()
        }
        canvasH = canvas.height.toFloat()
        canvasW = canvas.width.toFloat()
        //println("$canvasW $canvasH")
        val paint = Paint()
        paint.setStyle(Paint.Style.FILL)
        paint.setColor(Color.CYAN)
        canvas.drawBitmap(bitmapB, xcoordB, ycoordB, paint)

        canvas.drawBitmap(bitmapA, xcoordA, ycoordA, paint)  //originally bitmap draw at x=o and y=0

    }

    fun getBitmap(): Bitmap{
        return this.drawingCache
    }


}