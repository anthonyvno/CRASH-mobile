package com.example.europeesaanrijdingsformulier

import android.graphics.PointF
import com.example.europeesaanrijdingsformulier.drawview.MyRectangle
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.internal.util.reflection.Whitebox

class MyRectangleUnitTest {

        lateinit var rectangle1: MyRectangle
        lateinit var rectangle2: MyRectangle
        lateinit var rectangle3: MyRectangle
        lateinit var rectangle4: MyRectangle
        lateinit var rectangle5: MyRectangle

        @Mock
        val point1 = mock<PointF>()

        @Mock
        val point2 = mock<PointF>()



    @Before
        fun setup() {
        Whitebox.setInternalState(point1, "x", 4F);
        Whitebox.setInternalState(point1, "y", 3F);
        Whitebox.setInternalState(point2, "x", 5F);
        Whitebox.setInternalState(point2, "y", 10F);


            rectangle1 = MyRectangle(4F, 2F, point1, 0F)
            rectangle2 = MyRectangle(7.07F,7.07F , point2, 45F)
            rectangle3 = MyRectangle(4F, 2F, PointF(4F, 3F), 0F)
            rectangle4 = MyRectangle(4F, 2F, PointF(4F, 3F), 0F)
            rectangle5 = MyRectangle(4F, 2F, PointF(4F, 3F), 0F)
        }

        @Test
        fun test() {

            assertEquals(2F, rectangle1.getTopLeft().x)
            assertEquals(4F, rectangle1.getTopLeft().y)
            assertEquals(6F, rectangle1.getTopRight().x)
            assertEquals(4F, rectangle1.getTopRight().y)
            assertEquals(2F, rectangle1.getBottomLeft().x)
            assertEquals(2F, rectangle1.getBottomLeft().y)
            assertEquals(6F, rectangle1.getBottomRight().x)
            assertEquals(2F, rectangle1.getBottomRight().y)

            assertEquals(0, Math.round(rectangle2.getTopLeft().x))
            assertEquals(10, Math.round(rectangle2.getTopLeft().y))
            assertEquals(5, Math.round(rectangle2.getTopRight().x))
            assertEquals(15, Math.round(rectangle2.getTopRight().y))
            assertEquals(5, Math.round(rectangle2.getBottomLeft().x))
            assertEquals(5, Math.round(rectangle2.getBottomLeft().y))
            assertEquals(10, Math.round(rectangle2.getBottomRight().x))
            assertEquals(10, Math.round(rectangle2.getBottomRight().y))
            
        }


}
