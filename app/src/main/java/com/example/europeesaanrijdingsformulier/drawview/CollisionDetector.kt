package com.example.europeesaanrijdingsformulier.drawview


import android.graphics.PointF




class CollisionDetector {
    
    fun isPolygonsIntersecting(a: MyRectangle, b: MyRectangle): Boolean {
        for (x in 0..1) {
            val polygon = if (x == 0) a else b

            for (i1 in 0 until polygon.getPoints().size) {
                val i2 = (i1 + 1) % polygon.getPoints().size
                val p1 = polygon.getPoints()[i1]
                val p2 = polygon.getPoints()[i2]

                val normal = PointF(p2.y - p1.y, p1.x - p2.x)

                var minA = java.lang.Float.POSITIVE_INFINITY
                var maxA = java.lang.Float.NEGATIVE_INFINITY

                for (p in a.getPoints()) {
                    val projected = normal.x * p.x + normal.y * p.y

                    if (projected < minA)
                        minA = projected
                    if (projected > maxA)
                        maxA = projected
                }

                var minB = java.lang.Float.POSITIVE_INFINITY
                var maxB = java.lang.Float.NEGATIVE_INFINITY

                for (p in b.getPoints()) {
                    val projected = normal.x * p.x + normal.y * p.y

                    if (projected < minB)
                        minB = projected
                    if (projected > maxB)
                        maxB = projected
                }

                if (maxA < minB || maxB < minA)
                    return false
            }
        }

        return true
    }

}