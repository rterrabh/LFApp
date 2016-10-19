package com.ufla.lfapp.activities.graph.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import com.ufla.lfapp.activities.graph.views.edge.ArcEdgeDraw;
import com.ufla.lfapp.activities.graph.views.edge.EdgeDraw;
import com.ufla.lfapp.activities.graph.views.edge.ReflexiveEdgeDraw;

/**
 * Created by carlos on 9/21/16.
 * Representa a visão de uma transição em um autômato ou máquina de estados.
 */

public class EdgeView extends View {

    public static final int LINE = 0;
    public static final int ARC = 1;
    private EdgeDraw edgeDraw;
    private boolean directed;
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
    }

    public void setVertices(Pair<VertexView, VertexView> vertices) {
        this.vertices = vertices;
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
        this.mTransitionLine.setColor(Color.BLACK);
        this.mTransitionLine.setStrokeWidth(EdgeView.STROKE_WIDTH_LINE);
        this.mTransitionText.setColor(Color.BLACK);
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
        String symbol = "a";

        canvas.drawTextOnPath(symbol, edgeDraw.getLabelPath(), 0.0f, spaceFromLine, mTransitionText);
        canvas.drawPath(edgeDraw.getEdge(), mTransitionLine);
        //canvas.drawPath(edgeDraw.getArrowHead(), mTransitionLine);

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

}
