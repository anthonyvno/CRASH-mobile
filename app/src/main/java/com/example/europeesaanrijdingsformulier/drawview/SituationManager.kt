package com.example.europeesaanrijdingsformulier.drawview

import android.content.Context
import android.graphics.*

class SituationManager(
    var rectA: MyRectangle,
    var rectB: MyRectangle,
    var drawableA: Int,
    var drawableB: Int,
    val context: Context
) {

    private var paint = Paint()
    private var collisionDetector = CollisionDetector()

    private var bitmapA: Bitmap
    private var bitmapB: Bitmap
    private var xDeltaFingerCenterA: Float = 0.toFloat()
    private var yDeltaFingerCenterA: Float = 0.toFloat()
    private var vehicleASelected: Boolean = false

    private var xDeltaFingerCenterB: Float = 0.toFloat()
    private var yDeltaFingerCenterB: Float = 0.toFloat()
    private var vehicleBSelected: Boolean = false

    private var canvasH = 0F
    private var canvasW = 0F

    init {
        paint.style = Paint.Style.FILL
        paint.color = Color.CYAN

        bitmapA = BitmapFactory.decodeResource(context.resources, drawableA)
        bitmapA = Bitmap.createScaledBitmap(bitmapA, rectA.carWidth.toInt(), rectA.carLength.toInt(), false)

        bitmapB = BitmapFactory.decodeResource(context.resources, drawableB)
        bitmapB = Bitmap.createScaledBitmap(bitmapB, rectB.carWidth.toInt(), rectB.carLength.toInt(), false)

    }

    fun drawOnCanvas(canvas: Canvas) {

        if (vehicleASelected) {

            rectA.drawOnCanvas(canvas, paint)
        }

        canvas.drawBitmap(
            bitmapA,
            rectA.center.x - bitmapA.width / 2,
            rectA.center.y - bitmapA.height / 2,
            paint
        )

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

    fun onActionDown(x: Float, y: Float) {

        if (rectA.contains(PointF(x, y))
        ) {
            xDeltaFingerCenterA = x.toInt().toFloat() - rectA.center.x
            yDeltaFingerCenterA = y.toInt().toFloat() - rectA.center.y
            vehicleASelected = true
        }
        if (rectB.contains(PointF(x, y)) && !vehicleASelected
        ) {
            xDeltaFingerCenterB = x.toInt().toFloat() - rectB.center.x
            yDeltaFingerCenterB = y.toInt().toFloat() - rectB.center.y
            vehicleBSelected = true
        }
    }

    fun onActionMove(x: Float, y: Float) {

        if (vehicleASelected) {
            if (!collisionDetector.isRectanglesIntersecting(
                    rectA.calculateMove(
                        x - xDeltaFingerCenterA - rectA.center.x,
                        y - yDeltaFingerCenterA - rectA.center.y
                    ), rectB
                )
            ) {
                if (!isOutOfBounds(
                        rectA.calculateMove(
                            x - xDeltaFingerCenterA - rectA.center.x,
                            y - yDeltaFingerCenterA - rectA.center.y
                        )
                    )
                ) {
                    rectA = rectA.calculateMove(
                        x - xDeltaFingerCenterA - rectA.center.x,
                        y - yDeltaFingerCenterA - rectA.center.y
                    )
                }
            }
        }
        if (vehicleBSelected) {
            if (!collisionDetector.isRectanglesIntersecting(
                    rectB.calculateMove(
                        x - xDeltaFingerCenterB - rectB.center.x,
                        y - yDeltaFingerCenterB - rectB.center.y
                    ), rectA
                )
            ) {
                if (!isOutOfBounds(
                        rectB.calculateMove(
                            x - xDeltaFingerCenterB - rectB.center.x,
                            y - yDeltaFingerCenterB - rectB.center.y
                        )
                    )
                ) {
                    rectB = rectB.calculateMove(
                        x - xDeltaFingerCenterB - rectB.center.x,
                        y - yDeltaFingerCenterB - rectB.center.y
                    )
                }


            }
        }
    }

    fun onActionUp() {
        if (vehicleASelected) {
            vehicleASelected = false
        }
        if (vehicleBSelected) {
            vehicleBSelected = false
        }
    }

    fun onRotation(angle: Float) {

        if (vehicleASelected || vehicleBSelected) {
            val matrix = Matrix()
            matrix.postRotate(-angle, 0F, 0F)

            if (vehicleASelected && !collisionDetector.isRectanglesIntersecting(rectA.calculateRotate(angle), rectB)) {
                rectA = rectA.calculateRotate(angle)
                var bitmapA2 = BitmapFactory.decodeResource(context.resources, drawableA)
                bitmapA2 = Bitmap.createScaledBitmap(
                    bitmapA2,
                    rectA.carWidth.toInt() + 30,
                    rectA.carLength.toInt() + 30,
                    false
                )

                bitmapA =
                    Bitmap.createBitmap(bitmapA2, 0, 0, bitmapA2.width, bitmapA2.height, matrix, true)

            }
            if (vehicleBSelected && !collisionDetector.isRectanglesIntersecting(rectB.calculateRotate(angle), rectA)) {
                rectB = rectB.calculateRotate(angle)
                var bitmapA2 = BitmapFactory.decodeResource(context.resources, drawableB)
                bitmapA2 = Bitmap.createScaledBitmap(
                    bitmapA2,
                    rectB.carWidth.toInt() + 30,
                    rectB.carLength.toInt() + 30,
                    false
                )

                bitmapB =
                    Bitmap.createBitmap(bitmapA2, 0, 0, bitmapA2.width, bitmapA2.height, matrix, true)
            }

        }
    }

    fun initiateCanvas(canvas: Canvas) {
        if (rectA.center.x <= -100 && rectA.center.y <= -100) {

            rectA =
                MyRectangle(
                    rectA.carWidth - 30,
                    rectB.carLength - 30,
                    PointF(
                        (canvas.width / 5).toFloat(), (canvas.height / 2).toFloat()
                    )
                )
            bitmapA = BitmapFactory.decodeResource(context.resources, drawableA)
            bitmapA =
                Bitmap.createScaledBitmap(bitmapA, rectA.carWidth.toInt() + 30, rectA.carLength.toInt() + 30, false)

            canvasH = canvas.height.toFloat()
            canvasW = canvas.width.toFloat()

        }
        if (rectB.center.x <= -100 && rectB.center.y <= -100) {

            rectB = MyRectangle(
                rectB.carWidth - 30,
                rectB.carLength - 30,
                PointF(((canvas.width / 5) * 3).toFloat(), (canvas.height / 2).toFloat())
            )

            bitmapB = BitmapFactory.decodeResource(context.resources, drawableB)
            bitmapB =
                Bitmap.createScaledBitmap(bitmapB, rectB.carWidth.toInt() + 30, rectB.carLength.toInt() + 30, false)

        }

    }

    fun setSituation(drawableA: Int, drawableB: Int) {
        this.drawableA = drawableA
        this.drawableB = drawableB
    }

    private fun isOutOfBounds(rect: MyRectangle): Boolean {
        return (rect.getPoints().map { p -> p.x }.max()!! >= canvasW
                || rect.getPoints().map { p -> p.y }.max()!! >= canvasH
                || rect.getPoints().map { p -> p.x }.min()!! <= 0
                || rect.getPoints().map { p -> p.y }.min()!! <= 0)
    }


}