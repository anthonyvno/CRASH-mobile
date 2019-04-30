package com.example.europeesaanrijdingsformulier.drawview

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class DrawManager {

    private var mPath = MyPath()
    private var drawPaint = Paint()
    private var mPaths = LinkedHashMap<MyPath, Paint>()
    private var mLastPaths = LinkedHashMap<MyPath, Paint>()
    var mUndonePaths = LinkedHashMap<MyPath, Paint>()
    private var mCurX = 0f
    private var mCurY = 0f
    var mStartX = 0f
    var mStartY = 0f

    init{
        drawPaint.apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 8f
            isAntiAlias = true
        }
    }

    fun clearCanvas() {
        mLastPaths = mPaths.clone() as LinkedHashMap<MyPath, Paint>
        mPath.reset()
        mPaths.clear()
        //invalidate()
    }
    fun actionDown(x: Float, y: Float) {
        mPath.reset()
        mPath.moveTo(x, y)
        mCurX = x
        mCurY = y

    }

    fun actionMove(x: Float, y: Float) {
        mPath.quadTo(mCurX, mCurY, (x + mCurX) / 2, (y + mCurY) / 2)
        mCurX = x
        mCurY = y
    }

    fun actionUp() {
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

    private fun addPath(path: MyPath, options: Paint) {
        mPaths[path] = options
    }

    fun undo() {
        if (mPaths.isEmpty() && mLastPaths.isNotEmpty()) {
            mPaths = mLastPaths.clone() as LinkedHashMap<MyPath, Paint>
            mLastPaths.clear()
            //invalidate()
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
        //invalidate()
    }

    fun redo() {
        if (mUndonePaths.keys.isEmpty()) {
            return
        }

        val lastKey = mUndonePaths.keys.last()
        addPath(lastKey, mUndonePaths.values.last())
        mUndonePaths.remove(lastKey)
       // invalidate()
    }

    fun drawOnCanvas(canvas: Canvas){
        for ((key, value) in mPaths) {
            canvas.drawPath(key, value)
        }
        canvas.drawPath(mPath, drawPaint)

    }
}