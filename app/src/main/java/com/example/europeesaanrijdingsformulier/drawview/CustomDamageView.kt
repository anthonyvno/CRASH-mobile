package com.example.europeesaanrijdingsformulier.drawview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import com.example.europeesaanrijdingsformulier.R

class CustomDamageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var drawManager: DrawManager = DrawManager()
    val carWidth: Float
    val carLength: Float
    var bitmapCar: Bitmap
    var drawable:Int = R.drawable.redcar
    var size: Point

    init {
        var wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var dis = wm.defaultDisplay
        size = Point()
        dis.getSize(size)
        carWidth = size.x.toFloat() / 4
        carLength = (size.y / 3).toFloat()

        bitmapCar = BitmapFactory.decodeResource(context.getResources(), drawable)
        bitmapCar = Bitmap.createScaledBitmap(bitmapCar, carWidth.toInt(), carLength.toInt(), false)

        this.isDrawingCacheEnabled = true


    }

    fun setVehicle(drawab:Int){
        this.drawable = drawab
        bitmapCar = BitmapFactory.decodeResource(context.getResources(), drawable)
        bitmapCar = Bitmap.createScaledBitmap(bitmapCar, carWidth.toInt(), carLength.toInt(), false)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

                drawManager.mStartX = event.x
                drawManager.mStartY = event.y
                actionDown(event.x, event.y)
                drawManager.mUndonePaths.clear()


            }

            MotionEvent.ACTION_MOVE -> {

                actionMove(event.x, event.y)

            }

            MotionEvent.ACTION_UP -> {

                actionUp()

            }
        }
        invalidate()

        return true
    }

    override fun onDraw(canvas: Canvas) {
        initiateCanvas(canvas)
        drawManager.drawOnCanvas(canvas)

    }
    fun initiateCanvas(canvas:Canvas){
        canvas.drawBitmap(
            bitmapCar,
            canvas.width/2 - carWidth/2,
            canvas.height/2 - carLength/2,
            Paint()
        )
    }

    fun getBitmap(): Bitmap {
        return this.drawingCache
    }

    private fun actionDown(x: Float, y: Float) {
        drawManager.actionDown(x, y)

    }

    private fun actionMove(x: Float, y: Float) {
        drawManager.actionMove(x, y)
    }

    private fun actionUp() {
        drawManager.actionUp()
    }

    fun addPath(path: MyPath, options: Paint) {
        drawManager.addPath(path, options)
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

}