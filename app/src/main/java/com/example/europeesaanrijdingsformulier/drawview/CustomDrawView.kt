package com.example.europeesaanrijdingsformulier.drawview

import android.view.MotionEvent
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

import com.example.europeesaanrijdingsformulier.R
import android.graphics.Bitmap

class CustomDrawView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr),
    RotationGestureDetector.OnRotationGestureListener {


    private var rotationGestureDetector: RotationGestureDetector =
        RotationGestureDetector(this)

    var isDrawing = false
    var mPath = MyPath()
    var drawPaint = Paint()
     var mPaths = LinkedHashMap<MyPath, Paint>()
     var mLastPaths = LinkedHashMap<MyPath, Paint>()
     var mUndonePaths = LinkedHashMap<MyPath, Paint>()
     var mCurX = 0f
     var mCurY = 0f
     var mStartX = 0f
     var mStartY = 0f

    var angle = 0F
    val carWidth = 200
    val carLength = 400
    var canvasW = 0F
    var canvasH = 0F
    val paint = Paint()
    var hasCollided = false
    var noXCollisionA = 0F
    var noYCollisionA = 0F


    //A
    var angleA = 0F
    var oldAngleA = 0F
    var bitmapA: Bitmap
    var rectA = RectF()
    var xcoordA: Float = (-100).toFloat()
    var xcoordDownA: Float = 0.toFloat()
    var ycoordA: Float = (-100).toFloat()
    var ycoordDownA: Float = 0.toFloat()
    var boolA: Boolean = false
    var noXCollisionB = 0F
    var noYCollisionB = 0F

    //B
    var angleB = 0F
    var oldAngleB = 0F
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

        drawPaint.apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 8f
            isAntiAlias = true
        }

        paint.setStyle(Paint.Style.FILL)
        paint.setColor(Color.CYAN)

        this.isDrawingCacheEnabled = true

    }

    override fun OnRotation(rotationDetector: RotationGestureDetector) {
        if (boolA || boolB) {
            angle = rotationDetector.angle
/*
        var rectA = Rectangle(xcoordA.toDouble(),ycoordA.toDouble(),carWidth.toDouble(),carLength.toDouble())
        var transformA = AffineTransform().rotate((-angle/180)*Math.PI,rectA.x,rectA.y)
        var regionA = Region().quick
*/
            val matrix = Matrix()

            matrix.postRotate(-angle, xcoordA + (carWidth / 2), ycoordA + (carLength / 2))
            //matrix.postRotate(-angle)

            if (boolA) {
                angleA = angle
                var bitmapA2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.redcar)
                bitmapA2 = Bitmap.createScaledBitmap(bitmapA2, carWidth, carLength, false)

                bitmapA =
                    Bitmap.createBitmap(bitmapA2, 0, 0, bitmapA2.width, bitmapA2.height, matrix, true)

            }
            if (boolB) {
                angleB = angle
                var bitmapA2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.greencar)
                bitmapA2 = Bitmap.createScaledBitmap(bitmapA2, carWidth, carLength, false)

                bitmapB =
                    Bitmap.createBitmap(bitmapA2, 0, 0, carWidth, carLength, matrix, true)
            }

        }

    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        rotationGestureDetector.onTouchEvent(event);

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

                if (isDrawing) {
                    mStartX = event.x
                    mStartY = event.y
                    actionDown(event.x, event.y)
                    mUndonePaths.clear()

                } else {
                    if (rectA.contains(event.x, event.y)
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

            }

            MotionEvent.ACTION_MOVE -> {

                if (isDrawing) {
                    actionMove(event.x, event.y)
                } else {
                    println(!isCollision(rectA, rectB)&&!isCollision(rectB, rectA))
                    if (boolA) {
                        //println("boolA: " + boolA)
                        if (!isCollision(rectA, rectB)&&!isCollision(rectB, rectA))   {
                          // println("no collision boolA")
                            if (-xcoordDownA + carWidth < canvasW + 1F
                                && event.y.toInt().toFloat() - ycoordDownA + carLength < canvasH + 1F
                                && event.x.toInt().toFloat() - xcoordDownA > -1F
                                && event.y.toInt().toFloat() - ycoordDownA > -1F
                            ) {
                                noXCollisionA = xcoordA
                                noYCollisionA = ycoordA
                                xcoordA = event.x.toInt().toFloat() - xcoordDownA
                                ycoordA = event.y.toInt().toFloat() - ycoordDownA
                            }
                        }
                    }else{
                        hasCollided = true
                    }
                    if (boolB) {
                      //  println("boolB: " + boolB)
                        if (!isCollision(rectA, rectB)&&!isCollision(rectB, rectA))
                         {
                           // println("no collision boolB")
                            if (event.x.toInt().toFloat() - xcoordDownB + carWidth < canvasW + 1F
                                && event.y.toInt().toFloat() - ycoordDownB + carLength < canvasH + 1F
                                && event.x.toInt().toFloat() - xcoordDownB > -1F
                                && event.y.toInt().toFloat() - ycoordDownB > -1F
                            )
                                noXCollisionB = xcoordB
                                noYCollisionB = ycoordB
                                xcoordB = event.x.toInt().toFloat() - xcoordDownB
                                ycoordB = event.y.toInt().toFloat() - ycoordDownB
                            }
                        }else{
                        hasCollided = true

                    }
                }

            }

            MotionEvent.ACTION_UP -> {

                if (isDrawing) {
                    actionUp()
                } else {
                    if (boolA) {
                        boolA = false
                        if(hasCollided && noXCollisionA != 0F && noYCollisionA != 0F){
                            xcoordA = noXCollisionA
                            ycoordA = noYCollisionA
                        }
                        hasCollided = false
                    }
                    if (boolB) {
                        boolB = false
                        if(hasCollided && noXCollisionB != 0F && noYCollisionB != 0F){
                            xcoordB = noXCollisionB
                            ycoordB = noYCollisionB
                        }
                        hasCollided = false
                    }
                }

            }
        }
        invalidate()

        return true
    }

    override fun onDraw(canvas: Canvas) {
        initiateCanvas(canvas)

        for ((key, value) in mPaths) {
            canvas.drawPath(key, value)
        }
        canvas.drawPath(mPath, drawPaint)

        drawCarA(canvas)

        drawCarB(canvas)


    }

    private fun drawCarA(canvas: Canvas) {
        if (angleA != oldAngleA && boolA) {

            canvas.save()
            canvas.translate(xcoordA - bitmapA.width / 2, ycoordA - bitmapA.height / 2)
            canvas.rotate(-angleA, (bitmapA.width / 2).toFloat(), (bitmapA.height / 2).toFloat())
            rectA = RectF(0F, 0F, carWidth.toFloat(), carLength.toFloat())
            if (angleA >= 0F) {
                rectA.offset(
                    ((-carWidth / 2) * Math.sin(-angleA * Math.PI / 180)).toFloat(),
                    ((carLength / 4) * Math.sin(-angleA * Math.PI / 180)).toFloat()
                )

            } else {
                rectA.offset(
                    ((carWidth / 2) * Math.sin(-angleA * Math.PI / 180)).toFloat(),
                    ((-carLength / 4) * Math.sin(-angleA * Math.PI / 180)).toFloat()
                )
            }
            canvas.restore()
            canvas.save()

            canvas.rotate(-angleA, xcoordA, ycoordA)
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

            canvas.rotate(-angleA, xcoordA, ycoordA)
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
        oldAngleA = angleA
    }

    private fun drawCarB(canvas: Canvas) {
        if (angleB != oldAngleB && boolB) {

            canvas.save()
            canvas.translate(xcoordB - bitmapB.width / 2, ycoordB - bitmapB.height / 2)
            canvas.rotate(-angleB, (bitmapB.width / 2).toFloat(), (bitmapB.height / 2).toFloat())
            rectB = RectF(0F, 0F, carWidth.toFloat(), carLength.toFloat())

            if (angleB >= 0F) {
                rectB.offset(
                    ((-carWidth / 2) * Math.sin(-angleB * Math.PI / 180)).toFloat(),
                    ((carLength / 4) * Math.sin(-angleB * Math.PI / 180)).toFloat()
                )

            } else {
                rectB.offset(
                    ((carWidth / 2) * Math.sin(-angleB * Math.PI / 180)).toFloat(),
                    ((-carLength / 4) * Math.sin(-angleB * Math.PI / 180)).toFloat()
                )

            }

            canvas.restore()
            canvas.save()


            canvas.rotate(-angleB, xcoordB, ycoordB)
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

            canvas.rotate(-angleB, xcoordB, ycoordB)
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
        oldAngleB = angleB
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

    private fun isCollision(rect1: RectF, rect2: RectF): Boolean {
        var rect3 = RectF()
        rect3.union(rect1)

        var rect4 = RectF()
        rect4.union((rect2))
        val bool = rect3.intersect(rect4)
        return bool
    }

    fun getBitmap(): Bitmap {
        return this.drawingCache
    }

    fun toggleDrawing() {
        isDrawing = !isDrawing
    }

    private fun actionDown(x: Float, y: Float) {
        mPath.reset()
        mPath.moveTo(x, y)
        mCurX = x
        mCurY = y

    }

    private fun actionMove(x: Float, y: Float) {
        mPath.quadTo(mCurX, mCurY, (x + mCurX) / 2, (y + mCurY) / 2)
        mCurX = x
        mCurY = y
    }

    private fun actionUp() {
        mPath.lineTo(mCurX, mCurY)

        // draw a dot on click
        if (mStartX == mCurX && mStartY == mCurY) {
            mPath.lineTo(mCurX, mCurY + 2)
            mPath.lineTo(mCurX + 1, mCurY + 2)
            mPath.lineTo(mCurX + 1, mCurY)
        }

        mPaths[mPath] = drawPaint
        mPath = MyPath()
        //mPaintOptions = PaintOptions(mPaintOptions.color, mPaintOptions.strokeWidth, mPaintOptions.alpha, mPaintOptions.isEraserOn)
    }
    fun addPath(path: MyPath, options: Paint) {
        mPaths[path] = options
    }

    fun undo() {
        if (mPaths.isEmpty() && mLastPaths.isNotEmpty()) {
            mPaths = mLastPaths.clone() as LinkedHashMap<MyPath, Paint>
            mLastPaths.clear()
            invalidate()
            return
        }
        if (mPaths.isEmpty()) {
            return
        }
        val lastPath = mPaths.values.lastOrNull()
        val lastKey = mPaths.keys.lastOrNull()

        mPaths.remove(lastKey)
        if (lastPath != null && lastKey != null) {
            mUndonePaths[lastKey] = lastPath
        }
        invalidate()
    }

    fun redo() {
        if (mUndonePaths.keys.isEmpty()) {
            return
        }

        val lastKey = mUndonePaths.keys.last()
        addPath(lastKey, mUndonePaths.values.last())
        mUndonePaths.remove(lastKey)
        invalidate()
    }
    fun clearCanvas() {
        mLastPaths = mPaths.clone() as LinkedHashMap<MyPath, Paint>
        mPath.reset()
        mPaths.clear()
        invalidate()
    }

}