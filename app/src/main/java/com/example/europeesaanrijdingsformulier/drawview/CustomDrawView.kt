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
    var drawManager: DrawManager = DrawManager()



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
                    drawManager.mStartX = event.x
                    drawManager.mStartY = event.y
                    actionDown(event.x, event.y)
                    drawManager.mUndonePaths.clear()

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

        drawManager.drawOnCanvas(canvas)

        situationManager.drawOnCanvas(canvas)


    }


    fun getBitmap(): Bitmap {
        return this.drawingCache
    }

    fun toggleDrawing() {
        isDrawing = !isDrawing
    }

    private fun actionDown(x: Float, y: Float) {
        drawManager.actionDown(x,y)

    }

    private fun actionMove(x: Float, y: Float) {
        drawManager.actionMove(x,y)
    }

    private fun actionUp() {
        drawManager.actionUp()
    }

    fun addPath(path: MyPath, options: Paint) {
        drawManager.addPath(path,options)
    }

    fun undo() {
        drawManager.undo()
        invalidate()
    }

    fun redo() {
        drawManager.redo()
        invalidate()
    }

    fun clearCanvas() {
        drawManager.clearCanvas()
        invalidate()
    }

    fun setSituation(drawableA: Int, drawableB: Int){
        situationManager.setSituation(drawableA,drawableB)
    }

}