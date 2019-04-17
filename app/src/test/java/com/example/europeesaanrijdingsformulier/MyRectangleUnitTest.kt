package com.example.europeesaanrijdingsformulier

import android.graphics.PointF
import com.example.europeesaanrijdingsformulier.drawview.MyRectangle
import junit.framework.Assert.assertEquals
import org.junit.Test


class MyRectangleUnitTest {
    //imports

    var rectangle1 = MyRectangle(4F, 2F, PointF(4F, 3F), 0F)
    var rectangle2 = MyRectangle(7.07F, 7.07F, PointF(5F, 10F), 45F)
    var point = PointF(1F,2F)
    /*lateinit var rectangle3: MyRectangle
    lateinit var rectangle4: MyRectangle
    lateinit var rectangle5: MyRectangle*/
/*
    @Before
    fun setup() {
        rectangle1 = MyRectangle(4F, 2F, PointF(4F, 3F), 0F)
        rectangle2 = MyRectangle(7.07F, 7.07F, PointF(5F, 10F), 45F)
        rectangle3 = MyRectangle(4F, 2F, PointF(4F, 3F), 0F)
        rectangle4 = MyRectangle(4F, 2F, PointF(4F, 3F), 0F)
        rectangle5 = MyRectangle(4F, 2F, PointF(4F, 3F), 0F)

    }*/

    @Test
    fun test() {
        println(point.x)
        println(rectangle1.getTopLeft().x.toString() + " " +rectangle1.getTopLeft().y.toString())
        /*
        assertEquals(PointF(2F, 4F), rectangle1.getTopLeft())
        assertEquals(PointF(6F, 4F), rectangle1.getTopRight())
        assertEquals(PointF(2F, 2F), rectangle1.getBottomLeft())
        assertEquals(PointF(6F, 2F), rectangle1.getBottomRight())


        assertEquals(PointF(0F, 10F), rectangle2.getTopLeft())
        assertEquals(PointF(5F, 15F), rectangle2.getTopRight())
        assertEquals(PointF(5F, 5F), rectangle2.getBottomLeft())
        assertEquals(PointF(10F, 10F), rectangle2.getBottomRight())*/

    }
}