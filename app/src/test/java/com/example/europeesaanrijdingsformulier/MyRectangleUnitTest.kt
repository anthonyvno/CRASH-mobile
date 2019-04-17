package com.example.europeesaanrijdingsformulier

import android.graphics.PointF
import com.example.europeesaanrijdingsformulier.drawview.MyRectangle
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MyRectangleUnitTest {
    //imports
    fun testRectangle() {
        var rectangle1: MyRectangle
        var rectangle2: MyRectangle
        var rectangle3: MyRectangle
        var rectangle4: MyRectangle
        var rectangle5: MyRectangle


        @Before
        fun setup() {
            rectangle1 = MyRectangle(4F, 2F, FakePointF(4F, 3F), 0F)
            rectangle2 = MyRectangle(7.07F,7.07F , PointF(5, 10), 45F)
            rectangle3 = MyRectangle(4F, 2F, PointF(4, 3), 0F)
            rectangle4 = MyRectangle(4F, 2F, PointF(4, 3), 0F)
            rectangle5 = MyRectangle(4F, 2F, PointF(4, 3), 0F)
c
        }

        @Test
        fun test() {
            assertEquals(PointF(2F,4F), rectangle1.getTopLeft())
            assertEquals(PointF(6F,4F), rectangle1.getTopRight())
            assertEquals(PointF(2F,2F), rectangle1.getBottomLeft())
            assertEquals(PointF(6F,2F), rectangle1.getBottomRight())


            assertEquals(PointF(0F,10F), rectangle2.getTopLeft())
            assertEquals(PointF(5F,15F), rectangle2.getTopRight())
            assertEquals(PointF(5F,5F), rectangle2.getBottomLeft())
            assertEquals(PointF(10F,10F), rectangle2.getBottomRight())
        }


    }

    class FakePointF internal constructor(x: Float, y: Float) : PointF() {
        init {
            this.x = x
            this.y = y
        }
    }