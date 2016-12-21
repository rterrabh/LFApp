package com.ufla.lfapp.activities.graph.views.edge;

import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.util.Pair;
import android.util.Log;

import com.ufla.lfapp.activities.graph.layout.EditGraphLayout;
import com.ufla.lfapp.activities.graph.views.PointUtils;

/**
 * Created by carlos on 17/10/16.
 */
public class ArcEdgeDraw extends AbstractArcEdgeDraw {

    private static final double ANGLE = Math.toRadians(20.0f);
    private float LENGHT;
    private float ERROR_RECT_F_LABEL_OUT;
    private float ERROR_RECT_F_LABEL_INTERN;
    private Pair<PointF, PointF> centralCircPoints;

    public ArcEdgeDraw(Pair<Point, Point> gridPoints, EditGraphLayout editGraphLayout) {
        super(gridPoints, editGraphLayout);
        Log.d("ARC_EDGE", "vertexRadius -> " + vertexRadius);
    }

    @Override
    protected void setCircPointsOnCircumference() {
        float angle1To2 = PointUtils.angleFromP1ToP2(circPoints.first, circPoints.second);
        float angle2To1 = PointUtils.angleFromP1ToP2(circPoints.second, circPoints.first);
//        float angle2To1 = (float) Math.atan2((circPoints.first.y - circPoints.second.y),
//                (circPoints.first.x - circPoints.second.x));
//        float angle1To2 = (float) Math.atan2((circPoints.second.y - circPoints.first.y),
//                (circPoints.second.x - circPoints.first.x));
        PointF centralCircPoint1 = new PointF();
        PointF centralCircPoint2 = new PointF();
        centralCircPoint1.x = (float) (circPoints.first.x + vertexRadius * Math.cos(angle1To2));
        centralCircPoint1.y = (float) (circPoints.first.y + vertexRadius * Math.sin(angle1To2));
        centralCircPoint2.x = (float) (circPoints.second.x + vertexRadius * Math.cos(angle2To1));
        centralCircPoint2.y = (float) (circPoints.second.y + vertexRadius * Math.sin(angle2To1));
        centralCircPoints = Pair.create(centralCircPoint1, centralCircPoint2);
        circPoints.first.x += vertexRadius * Math.cos(angle1To2 - ANGLE);
        circPoints.first.y += vertexRadius * Math.sin(angle1To2 - ANGLE);
        circPoints.second.x += vertexRadius * Math.cos(angle2To1 + ANGLE);
        circPoints.second.y += vertexRadius * Math.sin(angle2To1 + ANGLE);
    }

    @Override
    protected void setPointControl() {
        LENGHT = vertexRadius / 2.0f;
        ERROR_RECT_F_LABEL_OUT = LENGHT * 1.50f;
        ERROR_RECT_F_LABEL_INTERN = LENGHT * 0.65f;
        PointF middlePoint = PointUtils.getMiddlePoint(circPoints);
        pointControl = new PointF();
//        float angle = (float) Math.atan2((circPoints.second.y - middlePoint.y),
//                (circPoints.second.x - middlePoint.x));
        float angle = PointUtils.angleFromP1ToP2(middlePoint, circPoints.second);
        Log.d("POINTCONTRL", (LENGHT * Math.cos(angle - ANGLE_90))+","+
                (LENGHT * Math.sin(angle - ANGLE_90)));
        pointControl.x = (float) (middlePoint.x + LENGHT * Math.cos(angle - ANGLE_90));
        pointControl.y = (float) (middlePoint.y + LENGHT * Math.sin(angle - ANGLE_90));
    }

    @Override
    public Pair<PointF, PointF> getPointsControlInteractArea() {
        LENGHT = vertexRadius / 2.0f;
        ERROR_RECT_F_LABEL_OUT = LENGHT * 1.50f;
        ERROR_RECT_F_LABEL_INTERN = LENGHT * 0.65f;
        PointF middlePoint = PointUtils.getMiddlePoint(circPoints);
        float angle = PointUtils.angleFromP1ToP2(middlePoint, circPoints.second);
//        float angle = (float) Math.atan2((circPoints.second.y - middlePoint.y),
//                (circPoints.second.x - middlePoint.x));
        PointF pointControl1 = new PointF();
        PointF pointControl2 = new PointF();
        pointControl1.x = (float) (middlePoint.x + (LENGHT + ERROR_RECT_F_LABEL_OUT) *
                    Math.cos(angle - ANGLE_90));
        pointControl1.y = (float) (middlePoint.y + (LENGHT + ERROR_RECT_F_LABEL_OUT) *
                Math.sin(angle - ANGLE_90));
        pointControl2.x = (float) (middlePoint.x + ERROR_RECT_F_LABEL_INTERN *
                Math.cos(angle + ANGLE_90));
        pointControl2.y = (float) (middlePoint.y + ERROR_RECT_F_LABEL_INTERN *
                Math.sin(angle + ANGLE_90));
        return Pair.create(pointControl1, pointControl2);
    }

}
