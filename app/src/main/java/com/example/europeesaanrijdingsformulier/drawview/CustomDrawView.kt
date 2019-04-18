package com.example.europeesaanrijdingsformulier.drawview

import android.view.MotionEvent
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

import com.example.europeesaanrijdingsformulier.R
import android.graphics.Bitmap

import android.view.WindowManager






class CustomDrawView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr),
    RotationGestureDetector.OnRotationGestureListener {


    private var rotationGestureDetector: RotationGestureDetector =
        RotationGestureDetector(this)

    private var situationManager: SituationManager

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



    val carWidth :Float
    val carLength:Float

    init {
        var wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var dis = wm.defaultDisplay
        val size = Point()
        dis.getSize(size)
        carWidth = size.x.toFloat()/8
        carLength = size.y.toFloat()/6


        situationManager = SituationManager(rectA = MyRectangle(carWidth, carLength, PointF(-100F, -100F)),
            rectB = MyRectangle(carWidth, carLength, PointF(-100F, -100F)),
            drawableA = R.drawable.redcar,
            drawableB = R.drawable.greencar,
            context = context)

        drawPaint.apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 8f
            isAntiAlias = true
        }

        this.isDrawingCacheEnabled = true


    }

    override fun OnRotation(rotationDetector: RotationGestureDetector) {
        situationManager.onRotation(rotationDetector.angle)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        rotationGestureDetector.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

                if (isDrawing) {
                    mStartX = event.x
                    mStartY = event.y
                    actionDown(event.x, event.y)
                    mUndonePaths.clear()

                } else {
                    situationManager.onActionDown(event.x,event.y)
                }

            }

            MotionEvent.ACTION_MOVE -> {

                if (isDrawing) {
                    actionMove(event.x, event.y)
                } else {
                    situationManager.onActionMove(event.x,event.y)
                }
            }

            MotionEvent.ACTION_UP -> {

                if (isDrawing) {
                    actionUp()
                } else {
                    situationManager.onActionUp()
                }

            }
        }
        invalidate()

        return true
    }

    override fun onDraw(canvas: Canvas) {
        situationManager.initiateCanvas(canvas)

        for ((key, value) in mPaths) {
            canvas.drawPath(key, value)
        }
        canvas.drawPath(mPath, drawPaint)

        situationManager.drawOnCanvas(canvas)


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

    fun setSituation(drawableA: Int, drawableB: Int){
        situationManager.setSituation(drawableA,drawableB)
    }

}