package com.ufla.lfapp.utils;

import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.util.Pair;

/**
 * Created by carlos on 11/17/16.
 */

public class PointUtils {

    /**
     * Calcula a distância entre dois pontos.
     *
     * @param points par de pontos em que será calculada a distância
     * @return distância entre os dois pontos
     */
    public static float dist(Pair<PointF, PointF> points) {
        float distX = points.second.x - points.first.x;
        float distY = points.second.y - points.first.y;
        return (float) Math.sqrt(distX * distX + distY * distY);
    }


    /**
     * Calcula o ângulo para o ponto p2, usando o ponto p1 como referência.
     *
     * @param p1 ponto referência para o cálculo do ângulo
     * @param p2 ponto destino para o cálculo do ângulo
     *
     * @return ângulo do ponto p1 para o ponto p2
     */
    public static float angleFromP1ToP2(Point p1, Point p2) {
        return (float) Math.atan2((p2.y - p1.y), (p2.x - p1.x));
    }

    public static float angleFromP1ToP2(PointF p1, PointF p2) {
        return (float) Math.atan2((p2.y - p1.y), (p2.x - p1.x));
    }

    public static float dist(PointF point1, PointF point2) {
        float distX = point2.x - point1.x;
        float distY = point2.y - point1.y;
        return (float) Math.sqrt(distX * distX + distY * distY);
    }

    /**
     * Calcula a distância mínima entre um ponto e uma reta. A reta é informada via um par de
     * pontos.
     *
     * @param line  par de pontos que definem a reta
     * @param point ponto que será calculada a distância entre ele e a reta
     * @return distância mínima entre o ponto e a reta
     */
    public static float distFromAPointToALine(Pair<PointF, PointF> line, PointF point) {
        return Math.abs((line.second.y - line.first.y) * point.x - (line.second.x - line.first.x) * point.y +
                line.second.x * line.first.y - line.second.y * line.first.x) / dist(line);
    }

    public static Point clonePoint(Point point) {
        Point pointClone = new Point();
        pointClone.set(point.x, point.y);
        return pointClone;
    }

    public static Pair<Point, Point> clonePairPoints(Pair<Point, Point> pairPoints) {
        Point pointFirstClone = clonePoint(pairPoints.first);
        Point pointSecondClone = clonePoint(pairPoints.second);
        return Pair.create(pointFirstClone, pointSecondClone);
    }

    public static PointF clonePointF(PointF pointF) {
        PointF pointFClone = new PointF();
        pointFClone.set(pointF.x, pointF.y);
        return pointFClone;
    }

    public static Pair<PointF, PointF> clonePairPointsF(Pair<PointF, PointF> pairPointsF) {
        PointF pointFFirstClone = clonePointF(pairPointsF.first);
        PointF pointFSecondClone = clonePointF(pairPointsF.second);
        return Pair.create(pointFFirstClone, pointFSecondClone);
    }

    public static Pair<Point, Point> getPointsOnOrigin(Pair<Point, Point> points) {
        Pair<Point, Point> gridPointsOnOrigin = clonePairPoints(points);
        int minx = Math.min(gridPointsOnOrigin.first.x, gridPointsOnOrigin.second.x);
        int miny = Math.min(gridPointsOnOrigin.first.y, gridPointsOnOrigin.second.y);
        gridPointsOnOrigin.first.x -= minx;
        gridPointsOnOrigin.second.x -= minx;
        gridPointsOnOrigin.first.y -= miny;
        gridPointsOnOrigin.second.y -= miny;
        return gridPointsOnOrigin;
    }

    public static PointF getMiddlePoint(Pair<PointF, PointF> pointsF) {
        PointF middlePoint = new PointF();
        middlePoint.x = (pointsF.first.x + pointsF.second.x) / 2.0f;
        middlePoint.y = (pointsF.first.y + pointsF.second.y) / 2.0f;
        return middlePoint;
    }

    public static PointF getMiddlePoint(PointF point1, PointF point2) {
        PointF middlePoint = new PointF();
        middlePoint.x = (point1.x + point2.x) / 2.0f;
        middlePoint.y = (point1.y + point2.y) / 2.0f;
        return middlePoint;
    }

}
