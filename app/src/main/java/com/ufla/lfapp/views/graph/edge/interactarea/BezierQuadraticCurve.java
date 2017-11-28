package com.ufla.lfapp.views.graph.edge.interactarea;

import android.graphics.Path;
import android.graphics.PointF;
import android.support.v4.util.Pair;

import com.ufla.lfapp.utils.PointUtils;
import com.ufla.lfapp.views.graph.layout.EditGraphLayout;

/**
 * Created by carlos on 1/27/17.
 */

public class BezierQuadraticCurve implements InteractArea {

    private PointF pointInitial;
    private PointF pointControl;
    private PointF pointEnd;
    private Pair<PointF, PointF> controlPoints;
    private PointF nearestPoint;

    // B(t) = (1 - t)²P0 + 2(1 - t)tP1 + t²P2, 0 <= t <= 1
    // dist(B(t), PK) = ((1 - t)²P0x + 2(1 - t)tP1x + t²P2x - PKx)² +
    //                  ((1 - t)²P0y + 2(1 - t)tP1y + t²P2y - PKy)²
    // minDist(B(t), PK) = dist(B(t), PK)'
    public BezierQuadraticCurve(PointF pointInitial, PointF pointControl, PointF pointEnd,
                                Pair<PointF, PointF> controlPoints, int radiusVertex) {
        this.pointInitial = pointInitial;
        this.pointControl = pointControl;
        this.pointEnd = pointEnd;
        this.controlPoints = controlPoints;
    }

    public PointF getNearestPoint() {
        return nearestPoint;
    }

    public PointF getPointInitial() {
        return pointInitial;
    }

    public void setPointInitial(PointF pointInitial) {
        this.pointInitial = pointInitial;
    }

    public PointF getPointControl() {
        return pointControl;
    }

    public void setPointControl(PointF pointControl) {
        this.pointControl = pointControl;
    }

    public PointF getPointEnd() {
        return pointEnd;
    }

    public void setPointEnd(PointF pointEnd) {
        this.pointEnd = pointEnd;
    }

    private final float ZERO = 0.00001F;

    public float[] thirdDegreeEquation(float a, float b, float c, float d) {
        if (Math.abs(a) > ZERO) {
            // let's adopt form: x3 + ax2 + bx + d = 0
            float z =  a; // multi-purpose util variable
            a = b / z;
            b = c / z;
            c = d / z;
            // we solve using Cardan formula: http://fr.wikipedia.org/wiki/M%C3%A9thode_de_Cardan
            float p = b - a * a / 3.0f;
            float q = a * (2.0f * a * a - 9.0f * b) / 27.0f + c;
            float p3 = p * p * p;
            float D = q * q + 4.0f * p3 / 27.0f;
            float offset = -a / 3.0f;
            if (D > ZERO) {
                // D positive
                z = (float) Math.sqrt(D);
                float u = (-q + z) / 2.0f;
                float v = (-q - z) / 2.0f;
                u = (float) ((u >= 0) ? Math.pow(u, 1.0f / 3.0f) : -Math.pow( -u, 1.0f / 3.0f));
                v = (float) ((v >= 0) ? Math.pow(v, 1.0f / 3.0f) : -Math.pow( -v, 1.0f / 3.0f));
                float[] solution = new float[1];
                solution[0] = u + v + offset;
                return solution;
            } else if (D < -ZERO) {
                // D negative
                float u = (float) (2.0f * Math.sqrt( -p / 3.0f));
                float v = (float) (Math.acos( -Math.sqrt( -27.0f / p3) * q / 2.0f) / 3.0f);
                float[] solution = new float[3];
                solution[0] = (float) (u * Math.cos(v) + offset);
                solution[1] = (float) (u * Math.cos(v + 2.0f * Math.PI / 3.0f) + offset);
                solution[2] = (float) (u * Math.cos(v + 4.0f * Math.PI / 3.0f) + offset);
                return solution;
            } else {
                // D zero
                float u;
                if (q < 0) {
                    u = (float) (Math.pow(-q / 2.0f, 1.0f / 3.0f));
                } else {
                    u = (float) (-Math.pow(q / 2.0f, 1.0f / 3.0f));
                }
                float[] solution = new float[2];
                solution[0] = 2.0f * u + offset;
                solution[1] = -u + offset;
                return solution;
            }
        } else {
            // a = 0, then actually a 2nd degree equation:
            // form : ax2 + bx + c = 0;
            a = b;
            b = c;
            c = d;
            if (Math.abs(a) <= ZERO)
            {
                if (Math.abs(b) <= ZERO) {
                    return new float[0];
                }
                else {
                    float[] solution = new float[1];
                    solution[0] = -c / b;
                    return solution;
                }
            }
            float D = b * b - 4.0f * a * c;
            if (D <= -ZERO) {
                return new float[0];
            }
            if (D > ZERO) {
                // D positive
                D = (float) Math.sqrt(D);
                float[] solution = new float[2];
                solution[0] = ( -b - D) / (2.0f * a);
                solution[1] = ( -b + D) / (2.0f * a);
                return solution;
            } else if (D < - ZERO) {
                // D negative
                return new float[0];
            } else {
                // D zero
                float[] solution = new float[1];
                solution[0] = -b / (2.0f * a);
                return solution;
            }
        }
    }

    public PointF getPos(float t) {
        float a = (1 - t) * (1 - t);
        float b = 2 * t * (1 - t);
        float c = t * t;
        PointF pos = new PointF();
        pos.x = a * pointInitial.x + b * pointControl.x + c * pointEnd.x;
        pos.y = a * pointInitial.y + b * pointControl.y + c * pointEnd.y;
        return pos;
    }

    public void normalize(PointF point) {
        float dist = PointUtils.dist(new PointF(0f, 0f), point);
        point.x = point.x / dist;
        point.y = point.y / dist;
    }

    @Override
    public boolean isOnInteractArea(PointF point) {
        float dist = distanceToObject(point);
        return dist <= EditGraphLayout.MAX_DISTANCE_FROM_EDGE;
    }

    @Override
    public boolean isOnInteractLabelArea(PointF point) {
        return false;
    }

    @Override
    public Path getInteractArea() {
        // Equation -> B(t) = (1 - t)²P0 + 2(1 - t)tP1 + t²P2
        Path interactArea = new Path();
        PointF point = new PointF();
        for (float t = 0; t <= 1.0; t += 0.1) {
            float ti = (1 - t);
            float ti_2 = ti * ti;
            float ti2 = 2 * ti;
            float ti2t = ti2 * t;
            float t_2 = t * t;
            point.x = ti_2 * pointInitial.x + ti2t * pointControl.x + t_2 * pointEnd.x;
            point.y = ti_2 * pointInitial.y + ti2t * pointControl.y + t_2 * pointEnd.y;
            interactArea.addCircle(point.x, point.y, EditGraphLayout.MAX_DISTANCE_FROM_EDGE, Path.Direction.CW);
        }
        return interactArea;
    }

    @Override
    public InteractArea clone() {
        return this;
    }

    @Override
    public float distanceToCircumferenceOfSourceVertex(PointF point) {
        return PointUtils.dist(pointInitial, point);
    }

    @Override
    public float distanceFromCircumferences() {
        return PointUtils.dist(pointInitial, pointEnd);
    }


    public PointF getAPoint() {
        return new PointF(pointControl.x - pointInitial.x,
                pointControl.y - pointInitial.y);
    }

    public PointF getBPoint() {
        return new PointF(pointInitial.x - 2 * pointControl.x + pointEnd.x,
                pointInitial.y - 2 * pointControl.y + pointEnd.y);
    }

    @Override
    public float distanceToObject(PointF point) {
        // a temporary util vect = p0 - (x,y)
        PointF pos = new PointF(pointInitial.x - point.x,
                pointInitial.y - point.y);
        // search points P of bezier curve with PM.(dP / dt) = 0
        // a calculus leads to a 3d degree equation :
        PointF A = getAPoint();
        PointF B = getBPoint();
        float[] sol;
        {
            float a = B.x * B.x + B.y * B.y;
            float b = 3 * (A.x * B.x + A.y * B.y);
            float c = 2 * (A.x * A.x + A.y * A.y) + pos.x * B.x + pos.y * B.y;
            float d = pos.x * A.x + pos.y * A.y;
            sol = thirdDegreeEquation(a, b, c, d);
        }

        float t;
        float dist;
        Float tMin = null;
        float distMin = Float.MAX_VALUE;
        float d0 = PointUtils.dist(point, pointInitial);
        float d2 = PointUtils.dist(point, pointEnd);
        float orientedDist;
        PointF posMin = new PointF();
        PointF nor = new PointF();
        PointF nearest = new PointF();

        if (sol != null && sol.length > 0) {
            // find the closest point:
            for (int i = 0; i < sol.length; i++) {
                t = sol[i];
                if (t >= 0 && t <= 1) {
                    pos = getPos(t);
                    dist = PointUtils.dist(point, pos);
                    if (dist < distMin) {
                        // minimum found!
                        tMin = t;
                        distMin = dist;
                        posMin.x = pos.x;
                        posMin.y = pos.y;
                        nearestPoint = posMin;
                    }
                }
            }
            if (tMin != null && distMin < d0 && distMin < d2)
            {
                // the closest point is on the curve
                nor.x = A.y + tMin * B.y;
                nor.y = -(A.x + tMin * B.x);
                normalize(nor);
                orientedDist = distMin;
                if ((point.x - posMin.x) * nor.x + (point.y - posMin.y) * nor.y < 0) {
                    nor.x *= -1;
                    nor.y *= -1;
                    orientedDist *= -1;
                }

//                nearest.t = tMin;
//                nearest.pos = posMin;
//                nearest.nor = nor;
//                nearest.dist = distMin;
//                nearest.orientedDist = orientedDist;
//                nearest.onCurve = true;
                return distMin;
            }

        }
        // the closest point is one of the 2 end points
        if (d0 < d2) {
            distMin = d0;
            tMin = 0f;
            posMin.x = pointInitial.x;
            posMin.y = pointInitial.y;
            nearestPoint = posMin;
        } else {
            distMin = d2;
            tMin = 1f;
            posMin.x = pointEnd.x;
            posMin.y = pointEnd.y;
            nearestPoint = posMin;
        }
        nor.x = point.x - posMin.x;
        nor.y = point.y - posMin.y;
        normalize(nor);

//        nearest.t = tMin;
//        nearest.pos = posMin;
//        nearest.nor = nor;
//        nearest.orientedDist = nearest.dist = distMin;
//        nearest.onCurve = false;
        return distMin;
    }

}
