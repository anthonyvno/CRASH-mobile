package com.example.europeesaanrijdingsformulier;

import android.graphics.Point;
import android.graphics.PointF;
import com.example.europeesaanrijdingsformulier.drawview.MyRectangle;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class MyRectangleUnitTestJava {

    private MyRectangle rec1;
    private MyRectangle rec2;


    @Mock
    private PointF point1;

    @Before
    public void setup() {
        point1 =  new PointF(2F, 2F);
        when(point1.x).thenReturn(2F);
        when(point1.y).thenReturn(2F);
        rec1 = new MyRectangle(4F, 2F, point1, 0F);
      //  rec2 = new MyRectangle(7.07F, 7.07F, new PointF(5F, 10F), 45F);

    }

    @Test
    public void test() {

        assertEquals(point1, rec1.getTopLeft());
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
