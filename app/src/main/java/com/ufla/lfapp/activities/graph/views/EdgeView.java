package com.ufla.lfapp.activities.graph.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.ufla.lfapp.activities.graph.views.edge.ArcEdgeDraw;
import com.ufla.lfapp.activities.graph.views.edge.EdgeDraw;
import com.ufla.lfapp.activities.graph.views.edge.ReflexiveEdgeDraw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by carlos on 9/21/16.
 * Representa a visão de uma transição em um autômato ou máquina de estados.
 */

public class EdgeView extends View {

    public static final List<Character> alphabet = new ArrayList<>(Arrays.asList(
            new Character[] {'?', 'a', 'b', 'c', 'd', 'e', 'f', 'g'} ) );
    public static final int LINE = 0;
    public static final int ARC = 1;
    private EdgeDraw edgeDraw;
    private boolean directed;
    private Pair<RectF, RectF> verticesRectF;
    private Pair<VertexView, VertexView> vertices;
    private Paint mTransitionLine;
    private Paint mTransitionText;
    private static final float STROKE_WIDTH_TEXT = 3.0f;
    private static final float STROKE_WIDTH_LINE = 3.0f;
    private static final float TEXT_SIZE = 50.0f;
    private static final float ARROW_HEAD_LENGHT = 25.0f;
    private static final float ARROW_ANGLE = (float) Math.toRadians(45.0f);
    private static final float ARROW_ANGLE_INTERN = (float) Math.toRadians(30.0f);
    private static final float TEXT_SPACE_FROM_EDGE = 20.0f;
    private String label = "";
    private int indLabel = 0;
    private boolean changeLabel = false;
    public Thread tRed;
    private GestureDetector gestureDetector;

    public void setLabel(String label) {
        this.label = label;
    }

    public void reloadBlack() {
        tRed = new Thread() {
            public void run() {
                try {
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Modo de edição rápida!", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                    sleep(4000);
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTransitionText.setColor(Color.BLACK);
                            invalidate();
                        }
                    });
                } catch (InterruptedException e) {

                }
            }
        };
        tRed.start();
    }

    public void reReloadBlack() {
        tRed.interrupt();
        reloadBlack();
    }

    public static float getArrowHeadLenght() {
        return ARROW_HEAD_LENGHT;
    }

    public static float getArrowAngle() {
        return ARROW_ANGLE;
    }

    public EdgeView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public EdgeView(Context context) {
        super(context);
        init();
        defineDefault();
    }

    public Pair<Point, Point> getPairGridPoint() {
        Point firstGridPoint = vertices.first.getGridPoint();
        Point secondGridPoint = vertices.second.getGridPoint();
        return Pair.create(firstGridPoint, secondGridPoint);
    }

    private Pair<Point, Point> getGridPointsOnOrigin() {
        Pair<Point, Point> gridPointsOnOrigin = clonePairPoints(getPairGridPoint());
        int minx = Math.min(gridPointsOnOrigin.first.x, gridPointsOnOrigin.second.x);
        int miny = Math.min(gridPointsOnOrigin.first.y, gridPointsOnOrigin.second.y);
        gridPointsOnOrigin.first.x -= minx;
        gridPointsOnOrigin.second.x -= minx;
        gridPointsOnOrigin.first.y -= miny;
        gridPointsOnOrigin.second.y -= miny;
        return gridPointsOnOrigin;
    }


    /**
     * Inicializa os objetos Paint da transição.
     */
    private void init() {
        this.mTransitionLine = new Paint();
        this.mTransitionLine.setAntiAlias(true);
        this.mTransitionLine.setStyle(Paint.Style.STROKE);
        this.mTransitionText = new Paint();
        this.mTransitionText.setAntiAlias(true);
        this.mTransitionText.setStrokeWidth(EdgeView.STROKE_WIDTH_TEXT);
        this.mTransitionText.setStyle(Paint.Style.FILL);
        this.mTransitionText.setTextAlign(Paint.Align.CENTER);
        gestureDetector = new GestureDetector(getContext(), new EdgeView.GestureListener());
    }

    public void setVertices(Pair<VertexView, VertexView> vertices) {
        this.vertices = vertices;
        Pair<Point, Point> verticesOnGridOrigin = getGridPointsOnOrigin();
        verticesRectF = new Pair<>(new RectF(), new RectF());
        verticesRectF.first.left = verticesOnGridOrigin.first.x * VertexView.squareDimension();
        verticesRectF.first.right = (verticesOnGridOrigin.first.x + 1) *
                VertexView.squareDimension();
        verticesRectF.first.top = verticesOnGridOrigin.first.y * VertexView.squareDimension();
        verticesRectF.first.bottom = (verticesOnGridOrigin.first.y + 1) *
                VertexView.squareDimension();

        verticesRectF.second.left = verticesOnGridOrigin.second.x * VertexView.squareDimension();
        verticesRectF.second.right = (verticesOnGridOrigin.second.x + 1) *
                VertexView.squareDimension();
        verticesRectF.second.top = verticesOnGridOrigin.second.y * VertexView.squareDimension();
        verticesRectF.second.bottom = (verticesOnGridOrigin.second.y + 1) *
                VertexView.squareDimension();

        if (vertices.first.equals(vertices.second)) {
            edgeDraw = new ReflexiveEdgeDraw(getGridPoints());
        } else {
            edgeDraw = new ArcEdgeDraw(getGridPoints());
        }
    }

    /**
     * Define valores padrões para os objetos Paint da transição.
     */
    private void defineDefault() {
        label = alphabet.get(indLabel++).toString();
        changeLabel = true;
        this.mTransitionLine.setColor(Color.BLACK);
        this.mTransitionLine.setStrokeWidth(EdgeView.STROKE_WIDTH_LINE);
        this.mTransitionText.setColor(Color.RED);
        this.mTransitionText.setTextSize(EdgeView.TEXT_SIZE);
    }


    /**
     * Calcula a distância mínima entre um ponto e uma reta. A reta é informada via um par de
     * pontos.
     *
     * @param line  par de pontos que definem a reta
     * @param point ponto que será calculada a distância entre ele e a reta
     * @return distância mínima entre o ponto e a reta
     */
    public float distFromAPointToALine(Pair<PointF, PointF> line, PointF point) {
        return Math.abs((line.second.y - line.first.y) * point.x - (line.second.x - line.first.x) * point.y +
                line.second.x * line.first.y - line.second.y * line.first.x) / dist(line);
    }

    /**
     * Calcula a distância entre dois pontos.
     *
     * @param points par de pontos em que será calculada a distância
     * @return distância entre os dois pontos
     */
    public float dist(Pair<PointF, PointF> points) {
        float distX = points.second.x - points.first.x;
        float distY = points.second.y - points.first.y;
        return (float) Math.sqrt(distX * distX + distY * distY);
    }

    private Point clonePoint(Point point) {
        Point pointClone = new Point();
        pointClone.set(point.x, point.y);
        return pointClone;
    }

    private Pair<Point, Point> clonePairPoints(Pair<Point, Point> pairPoints) {
        Point pointFirstClone = clonePoint(pairPoints.first);
        Point pointSecondClone = clonePoint(pairPoints.second);
        return Pair.create(pointFirstClone, pointSecondClone);
    }


    public Pair<Point, Point> getGridPoints() {
        Point firstGridPoint = vertices.first.getGridPoint();
        Point secondGridPoint = vertices.second.getGridPoint();
        return Pair.create(firstGridPoint, secondGridPoint);
    }


    /**
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        Pair<Point, Point> gridPoints = edgeDraw.getGridPoints();
        float spaceFromLine;
        if (gridPoints.first.x < gridPoints.second.x || (gridPoints.first.x == gridPoints.second.x
                && gridPoints.first.y <= gridPoints.second.y)) {
            spaceFromLine = -TEXT_SPACE_FROM_EDGE;
        } else {
            spaceFromLine = TEXT_SPACE_FROM_EDGE + TEXT_SPACE_FROM_EDGE;
        }
        canvas.drawTextOnPath(label, edgeDraw.getLabelPath(), 0.0f, spaceFromLine, mTransitionText);
        canvas.drawPath(edgeDraw.getEdge(), mTransitionLine);
        if (!(edgeDraw instanceof ReflexiveEdgeDraw)) {
            canvas.drawPath(edgeDraw.getArrowHead(), mTransitionLine);
        }
        if (tRed == null) {
            reloadBlack();
        } else if (changeLabel) {
            reReloadBlack();
        }
        changeLabel = false;
    }

    /**
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Point gridPointSourceVertex = vertices.first.getGridPoint();
        Point gridPointTargetVertex = vertices.second.getGridPoint();
        System.out.println("OLA(" + gridPointSourceVertex.x + "," + gridPointSourceVertex.y + ") -> (" +
                gridPointTargetVertex.x + "," + gridPointTargetVertex.y + ")");
        int width = (Math.abs(gridPointSourceVertex.x - gridPointTargetVertex.x) + 1) *
                VertexView.squareDimension();
        int height = (Math.abs(gridPointSourceVertex.y - gridPointTargetVertex.y) + 1) *
                VertexView.squareDimension();
        if (vertices.first.equals(vertices.second)) {
            width = VertexView.squareDimension();
            height = VertexView.squareDimension() * 3;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return gestureDetector.onTouchEvent(event);
    }


    private MotionEvent getMotioEventForVertexView(MotionEvent e) {
        e.setLocation(e.getX() % VertexView.squareDimension(),
                    e.getY() % VertexView.squareDimension());
        return e;
    }
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            if (verticesRectF.first.contains(e.getX(), e.getY())) {
                return vertices.first.onDownAction(getMotioEventForVertexView(e));
            }
            if (verticesRectF.second.contains(e.getX(), e.getY())) {
                return vertices.second.onDownAction(getMotioEventForVertexView(e));
            }

            if (tRed != null && tRed.isAlive()) {
                if (indLabel == alphabet.size()) {
                    indLabel = 1;
                }
                label = alphabet.get(indLabel++).toString();
                changeLabel = true;
                invalidate();
            }
            Log.d(label + " - onDown", label + " - onDown" +
                    " (" + e.getX() + ", " + e.getY() + ")");
            return true;
        }



        @Override
        public boolean onContextClick(MotionEvent e) {
            Log.d(label + " - onContextClick", label + " - onContextClick" +
                    " (" + e.getX() + ", " + e.getY() + ")");
            if (verticesRectF.first.contains(e.getX(), e.getY())) {
                return vertices.first.onTouchEvent(getMotioEventForVertexView(e));
            }
            if (verticesRectF.second.contains(e.getX(), e.getY())) {
                return vertices.second.onTouchEvent(getMotioEventForVertexView(e));
            }

            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (verticesRectF.first.contains(e.getX(), e.getY())) {
                vertices.first.onLongPressAction(getMotioEventForVertexView(e));
            }
            if (verticesRectF.second.contains(e.getX(), e.getY())) {
                vertices.second.onLongPressAction(getMotioEventForVertexView(e));
            }
            Log.d(label + " - onLongPress", label + " - onLongPress" +
                    " (" + e.getX() + ", " + e.getY() + ")");
        }

        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (verticesRectF.first.contains(e.getX(), e.getY())) {
                return vertices.first.onDoubleTapAction(getMotioEventForVertexView(e));
            }
            if (verticesRectF.second.contains(e.getX(), e.getY())) {
                return vertices.second.onDoubleTapAction(getMotioEventForVertexView(e));
            }

            Log.d(label + " - Double Tap", "Tapped at: (" + e.getX() + "," + e.getY() + ")");

            return true;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EdgeView edgeView = (EdgeView) o;

        return vertices != null ? vertices.equals(edgeView.vertices) : edgeView.vertices == null;
    }

    @Override
    public int hashCode() {
        return vertices != null ? vertices.hashCode() : 0;
    }
}
