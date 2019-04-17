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
    private var collisionDetector = CollisionDetector()

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

    val carWidth = 200F
    val carLength = 400F
    var canvasW = 0F
    var canvasH = 0F
    val paint = Paint()



    //A

    var bitmapA: Bitmap
    var rectA: MyRectangle
    var xDeltaFingerCenterA: Float = 0.toFloat()
    var yDeltaFingerCenterA: Float = 0.toFloat()
    var vehicleASelected: Boolean = false


    //B

    var bitmapB: Bitmap
    var rectB: MyRectangle
    var xDeltaFingerCenterB: Float = 0.toFloat()
    var yDeltaFingerCenterB: Float = 0.toFloat()
    var vehicleBSelected: Boolean = false


    init {
        bitmapA = BitmapFactory.decodeResource(context.getResources(), R.drawable.redcar)
        bitmapA = Bitmap.createScaledBitmap(bitmapA, carWidth.toInt(), carLength.toInt(), false)

        bitmapB = BitmapFactory.decodeResource(context.getResources(), R.drawable.greencar)
        bitmapB = Bitmap.createScaledBitmap(bitmapB, carWidth.toInt(), carLength.toInt(), false)

        rectA = MyRectangle(0F, 0F, PointF(-100F, -100F))
        rectB = MyRectangle(0F, 0F, PointF(-100F, -100F))


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
        if (vehicleASelected || vehicleBSelected) {
            val matrix = Matrix()
            matrix.postRotate(-rotationDetector.angle, 0F, 0F)

            if (vehicleASelected && !collisionDetector.isPolygonsIntersecting(rectA.calculateRotate(rotationDetector.angle),rectB)) {
                rectA = rectA.calculateRotate(rotationDetector.angle)
                var bitmapA2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.redcar)
                bitmapA2 = Bitmap.createScaledBitmap(bitmapA2, carWidth.toInt(), carLength.toInt(), false)

                bitmapA =
                    Bitmap.createBitmap(bitmapA2, 0, 0, bitmapA2.width, bitmapA2.height, matrix, true)

            }
            if (vehicleBSelected && !collisionDetector.isPolygonsIntersecting(rectB.calculateRotate(rotationDetector.angle),rectA)) {
                rectB = rectB.calculateRotate(rotationDetector.angle)
                var bitmapA2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.greencar)
                bitmapA2 = Bitmap.createScaledBitmap(bitmapA2, carWidth.toInt(), carLength.toInt(), false)

                bitmapB =
                    Bitmap.createBitmap(bitmapA2, 0, 0, bitmapA2.width, bitmapA2.height, matrix, true)
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
                    if (rectA.contains(PointF(event.x, event.y))
                    ) {
                        xDeltaFingerCenterA = event.x.toInt().toFloat() - rectA.center.x
                        yDeltaFingerCenterA = event.y.toInt().toFloat() - rectA.center.y
                        vehicleASelected = true
                    }
                    if (rectB.contains(PointF(event.x, event.y))
                    ) {
                        xDeltaFingerCenterB = event.x.toInt().toFloat() - rectB.center.x
                        yDeltaFingerCenterB = event.y.toInt().toFloat() - rectB.center.y
                        vehicleBSelected = true
                    }
                }

            }

            MotionEvent.ACTION_MOVE -> {

                if (isDrawing) {
                    actionMove(event.x, event.y)
                } else {
                    if (vehicleASelected) {
                        if (!collisionDetector.isPolygonsIntersecting(rectA.calculateMove(event.x- xDeltaFingerCenterA-rectA.center.x, event.y- yDeltaFingerCenterA-rectA.center.y), rectB)) {

                            rectA = rectA.calculateMove(event.x- xDeltaFingerCenterA-rectA.center.x, event.y- yDeltaFingerCenterA-rectA.center.y)

                        }
                    }
                    if (vehicleBSelected) {
                        if (!collisionDetector.isPolygonsIntersecting(rectB.calculateMove(event.x- xDeltaFingerCenterB-rectB.center.x, event.y- yDeltaFingerCenterB-rectB.center.y), rectA)) {

                            rectB = rectB.calculateMove(event.x- xDeltaFingerCenterB-rectB.center.x, event.y- yDeltaFingerCenterB-rectB.center.y)

                        }
                    }

                }
            }

            MotionEvent.ACTION_UP -> {

                if (isDrawing) {
                    actionUp()
                } else {
                    if (vehicleASelected) {
                        vehicleASelected = false
                    }
                    if (vehicleBSelected) {
                        vehicleBSelected = false
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

        if (vehicleASelected) {

            rectA.drawOnCanvas(canvas, paint)
        }

        canvas.drawBitmap(
            bitmapA,
            rectA.center.x - bitmapA.width / 2,
            rectA.center.y - bitmapA.height / 2,
            paint
        )
    }

    private fun drawCarB(canvas: Canvas) {
        if (vehicleBSelected) {

            rectB.drawOnCanvas(canvas, paint)
        }

        canvas.drawBitmap(
            bitmapB,
            rectB.center.x - bitmapB.width / 2,
            rectB.center.y - bitmapB.height / 2,
            paint
        )
    }

    private fun initiateCanvas(canvas: Canvas) {
        if (rectA.center.x <= -100 && rectA.center.y <= -100) {

            rectA =
                MyRectangle(carWidth-30, carLength-30, PointF((canvas.width / 5).toFloat(), (canvas.height / 3).toFloat()))
        }
        if (rectB.center.x <= -100 && rectB.center.y <= -100) {

            rectB = MyRectangle(
                carWidth-30,
                carLength-30,
                PointF(((canvas.width / 5) * 3).toFloat(), (canvas.height / 3).toFloat())
            )

        }
        canvasH = canvas.height.toFloat()
        canvasW = canvas.width.toFloat()

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