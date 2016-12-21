package com.ufla.lfapp.vo.grammar;

import android.graphics.PointF;
import android.provider.Settings;
import android.support.v4.util.Pair;
import android.util.Log;

import com.ufla.lfapp.activities.graph.views.PointUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by carlos on 11/18/16.
 */

public class Test {

    @org.junit.Test
    public void test() {
        PointF point1 = new PointF();
        PointF point2 = new PointF();
        PointF point = new PointF();
        point1.x = 0;
        point1.y = 1;
        point2.x = 5;
        point2.y = 1;
        point.x = 100;
        point.y = 4;
        Pair<PointF, PointF> line = Pair.create(point1, point2);
        System.out.println(Float.toString(PointUtils.dist(Pair.create(point, point2))));
        System.out.println(Float.toString(PointUtils.distFromAPointToALine(line,
                point)));
    }

}
