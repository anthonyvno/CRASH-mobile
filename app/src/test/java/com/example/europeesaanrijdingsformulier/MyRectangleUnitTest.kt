package com.example.europeesaanrijdingsformulier

import android.graphics.PointF
import com.example.europeesaanrijdingsformulier.drawview.MyRectangle
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock

class MyRectangleUnitTest {

        lateinit var rectangle1: MyRectangle
        lateinit var rectangle2: MyRectangle
        lateinit var rectangle3: MyRectangle
        lateinit var rectangle4: MyRectangle
        lateinit var rectangle5: MyRectangle

        val point1 = PointF(4F, 3F)



    @Before
        fun setup() {

            rectangle1 = MyRectangle(4F, 2F, PointF(4F, 3F), 0F)
            rectangle2 = MyRectangle(7.07F,7.07F , PointF(5F, 10F), 45F)
            rectangle3 = MyRectangle(4F, 2F, PointF(4F, 3F), 0F)
            rectangle4 = MyRectangle(4F, 2F, PointF(4F, 3F), 0F)
            rectangle5 = MyRectangle(4F, 2F, PointF(4F, 3F), 0F)
        }

        @Test
        fun test() {

            print(point1.x)
            point1.x = 2F
            assertEquals(PointF(2F,4F).x, rectangle1.getTopLeft().x)
            /*
            assertEquals(PointF(6F,4F), rectangle1.getTopRight())
            assertEquals(PointF(2F,2F), rectangle1.getBottomLeft())
            assertEquals(PointF(6F,2F), rectangle1.getBottomRight())


            assertEquals(PointF(0F,10F), rectangle2.getTopLeft())
            assertEquals(PointF(5F,15F), rectangle2.getTopRight())
            assertEquals(PointF(5F,5F), rectangle2.getBottomLeft())
            assertEquals(PointF(10F,10F), rectangle2.getBottomRight())*/
        }


}
/*
    class FakePointF internal constructor(x: Float, y: Float) : PointF() {
        init {
            this.x = x
            this.y = y
        }
}*/
