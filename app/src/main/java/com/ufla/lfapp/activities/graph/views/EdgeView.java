package com.ufla.lfapp.activities.graph.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.util.Pair;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;

import com.ufla.lfapp.activities.graph.layout.EditGraphLayout;
import com.ufla.lfapp.activities.graph.views.edge.EdgeDraw;
import com.ufla.lfapp.activities.graph.views.edge.EdgeDrawFactory;
import com.ufla.lfapp.activities.graph.views.edge.EdgeDrawType;
import com.ufla.lfapp.vo.TransitionFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by carlos on 9/21/16.
 * Representa a visão de uma transição em um autômato ou máquina de estados.
 */


public class EdgeView extends EditText {

    private final static float dpi;
    private static Alphabet alphabet = new Alphabet(Arrays.asList(
            new String[] {"a", "b", "c", "d", "e", "f", "g"} ) );
    private boolean reLoadFastEdition;
    private int indFastEdition;
    private Thread tFastEdition;
    private final static int X_FOR_DISPATCH_TOUCH_EVENT_TO_EDIT_TEXT = 3;
    private static final long TIME_FAST_EDITION = 3000;
    public static final int LINE = 0;
    public static final int ARC = 1;
    private EdgeDraw edgeDraw;
    private int gridBeginHeight;
    private int gridBeginWidth;
    private Pair<VertexView, VertexView> vertices;
    private EdgeView invertedEdge;
    private Paint mTransitionLine;
    private Paint mTransitionText;
    private static final float STROKE_WIDTH_TEXT;
    private static final float STROKE_WIDTH_LINE;
    private static final float TEXT_SIZE;
    private static final float TEXT_SPACE_FROM_EDGE;
    private static final float ARROW_HEAD_LENGHT;
    private static final float ARROW_ANGLE = (float) Math.toRadians(35.0f);
    private String label;
    private int cursorInd;
    private boolean showCursor;
    private boolean cursorShowed;
    private boolean changeLabel = false;
    private GestureDetector gestureDetector;
    private PointF onDown;
    private boolean reflexiveUp;

    static {
        dpi = Resources.getSystem().getDisplayMetrics().densityDpi;

        STROKE_WIDTH_TEXT = dpi / 160.0f;
        STROKE_WIDTH_LINE = dpi / 160.0f;
        TEXT_SIZE = dpi / 9.6f;
        TEXT_SPACE_FROM_EDGE = dpi / 24.0f;
        ARROW_HEAD_LENGHT = dpi / 19.2f;
    }

    public void setOnDown(PointF onDown) {
        this.onDown = onDown;
        invalidate();
    }

    public boolean labelDefinied() {
        return !label.equals("?");
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void appendLabel(String append) {
        if (label.equals("?")) {
            label = "";
        }
        if (label.isEmpty()) {
            label = append;
        } else {
            label = label + ", " + append;
        }
        setText(label);
        invalidate();
    }

    public int getGridBeginHeight() {
        return gridBeginHeight;
    }

    public int getGridBeginWidth() {
        return gridBeginWidth;
    }

    public void setReflexiveUp(boolean reflexiveUp) {
        this.reflexiveUp = reflexiveUp;
    }

    public boolean isReflexiveUp() {
        return reflexiveUp;
    }

    public static float getArrowHeadLenght() {
        return ARROW_HEAD_LENGHT;
    }

    public static float getArrowAngle() {
        return ARROW_ANGLE;
    }

    public void loadFastEdition() {
        tFastEdition = new Thread() {
            public void run() {
                try {
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Modo de edição rápida!",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                    if (!reLoadFastEdition) {
                        indFastEdition = 0;
                    }
                    sleep(TIME_FAST_EDITION);
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTransitionText.setColor(Color.BLACK);
                            invalidate();
                            reLoadFastEdition = false;
                        }
                    });
                } catch (InterruptedException e) {

                }
            }
        };
        tFastEdition.start();
    }

    public void reloadFastEdition() {
        reLoadFastEdition = true;
        tFastEdition.interrupt();
        loadFastEdition();
    }

    public boolean haveInvertedEdge() {
        return invertedEdge != null;
    }

    public void setInvertedEdge(EdgeView invertedEdge) {
        this.invertedEdge = invertedEdge;
        if (haveInvertedEdge()) {
            edgeDraw = new EdgeDrawFactory()
                    .createEdgeDraw(EdgeDrawType.ARC_EDGE_DRAW, getGridPoints());
        } else {
            edgeDraw = new EdgeDrawFactory()
                    .createEdgeDraw(EdgeDrawType.LINE_EDGE_DRAW, getGridPoints());
        }
        invalidate();
        Log.d("inverted", "label" + label);
    }

    public boolean isInvertedEdge(EdgeView invertedEdge) {
        if (vertices != null && invertedEdge.vertices != null &&
                vertices.first.equals(invertedEdge.vertices.second) &&
                vertices.second.equals(invertedEdge.vertices.first)) {
            return true;
        }
        return false;
    }

    public EdgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EdgeView(Context context) {
        super(context);
        init();
        defineDefault();
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
        setInputType(InputType.TYPE_CLASS_TEXT);
        label = "?";
        setText(label);
        setEnabled(true);
        changeLabel = true;
        showCursor = false;
        cursorShowed = false;
        setBackgroundColor(Color.TRANSPARENT);
        gestureDetector = new GestureDetector(getContext(), new EdgeView.GestureListener());
    }

    public void setVertices(Pair<VertexView, VertexView> vertices) {
        this.vertices = vertices;
        Pair<Point, Point> gridPoints = getGridPoints();
        gridBeginHeight = Math.min(gridPoints.first.y, gridPoints.second.y);
        gridBeginWidth = Math.min(gridPoints.first.x, gridPoints.second.x);
        if (vertices.first.equals(vertices.second)) {
            if (reflexiveUp) {
                edgeDraw = new EdgeDrawFactory()
                        .createEdgeDraw(EdgeDrawType.REFLEXIVE_UP_EDGE_DRAW, getGridPoints());
                gridBeginHeight--;
            } else {
                edgeDraw = new EdgeDrawFactory()
                        .createEdgeDraw(EdgeDrawType.REFLEXIVE_BOTTOM_EDGE_DRAW, getGridPoints());
            }
        } else {
            edgeDraw = new EdgeDrawFactory()
                    .createEdgeDraw(EdgeDrawType.LINE_EDGE_DRAW, getGridPoints());
        }
    }

    public void removeDependenciesFromVertex() {
        vertices.first.removeEdgeDependencies(this);
        vertices.second.removeEdgeDependencies(this);
    }

    /**
     * Define valores padrões para os objetos Paint da transição.
     */
    private void defineDefault() {
        this.mTransitionLine.setColor(Color.BLACK);
        this.mTransitionLine.setStrokeWidth(EdgeView.STROKE_WIDTH_LINE);
        this.mTransitionText.setColor(Color.RED);
        this.mTransitionText.setTextSize(EdgeView.TEXT_SIZE);
    }

    public boolean isOnInteractArea(PointF pointF) {
        return edgeDraw.isOnInteractArea(pointF);
    }
    public Pair<Point, Point> getGridPoints() {
        Point firstGridPoint = vertices.first.getGridPoint();
        Point secondGridPoint = vertices.second.getGridPoint();
        return Pair.create(firstGridPoint, secondGridPoint);
    }

    private float getSpaceFromLine() {
        Pair<Point, Point> gridPoints = getGridPoints();
        if (gridPoints.first.x < gridPoints.second.x || (gridPoints.first.x == gridPoints.second.x
                && gridPoints.first.y <= gridPoints.second.y)) {
            return -TEXT_SPACE_FROM_EDGE;
        } else {
            return TEXT_SPACE_FROM_EDGE + TEXT_SPACE_FROM_EDGE;
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

    private boolean inFastEdition() {
        return tFastEdition != null;
    }

    private boolean onDown() {
        return onDown != null;
    }

    public Set<TransitionFunction> getTransitionFuctions() {
        String currentState = vertices.first.getLabel();
        String futureState = vertices.second.getLabel();
        Set<TransitionFunction> transitionFunctions = new HashSet<>();
        for (String symbol : label.split("[ ,]")) {
            if (symbol.length() > 0) {
                transitionFunctions.add(new TransitionFunction(currentState, symbol, futureState));
            }
        }
        return transitionFunctions;
    }
    /**
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        label = getText().toString();
        if (cursorInd >= label.length()) {
            cursorInd = label.length() - 1;
        }
        cursorInd = getSelectionStart();
        if (cursorInd < 0) {
            cursorInd = 0;
        }
        setSelection(cursorInd, cursorInd);
        setSelection(cursorInd);
        //setSelection(cursorInd, cursorInd);
        //setSelection(cursorInd);
        alphabet.update(label);
//        canvas.drawTextOnPath(label, edgeDraw.getLabelPath(), 0.0f, getSpaceFromLine(),
//                mTransitionText);
        canvas.drawPath(edgeDraw.getEdge(), mTransitionLine);
        canvas.drawPath(edgeDraw.getArrowHead(), mTransitionLine);
        // TESTE área de interação
//        mTransitionLine.setColor(Color.RED);
//        canvas.drawPath(edgeDraw.getPathInteractArea(), mTransitionLine);
//        if (onDown()) {
//            mTransitionLine.setStrokeWidth(STROKE_WIDTH_LINE * 5.0f);
//            canvas.drawPoint(onDown.x, onDown.y, mTransitionLine);
//            onDown = null;
//            mTransitionLine.setStrokeWidth(STROKE_WIDTH_LINE);
//        }
//        mTransitionLine.setColor(Color.BLACK);
        // TESTE área de interação
        if (!inFastEdition()) {
            loadFastEdition();
        } else if (changeLabel) {
            reloadFastEdition();
        }
        changeLabel = false;
        showCursor = isFocused();
        StringBuilder sb = new StringBuilder(label);
        if (showCursor) {
            if (!cursorShowed) {
                if (cursorInd < 0) {
                    sb.insert(0, '|');
                } else {
                    sb.insert(cursorInd, '|');
                }
                cursorShowed = true;
            } else {
                cursorShowed = false;
            }
        }
        canvas.drawTextOnPath(sb.toString(), edgeDraw.getLabelPath(), 0.0f, getSpaceFromLine(),
                mTransitionText);
    }

    /**
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Point gridPointSourceVertex = vertices.first.getGridPoint();
        Point gridPointTargetVertex = vertices.second.getGridPoint();
        int width = (Math.abs(gridPointSourceVertex.x - gridPointTargetVertex.x) + 1) *
                VertexView.squareDimension();
        int height = (Math.abs(gridPointSourceVertex.y - gridPointTargetVertex.y) + 1) *
                VertexView.squareDimension();
        if (vertices.first.equals(vertices.second)) {
            width = VertexView.squareDimension();
            height = VertexView.squareDimension() * 2;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private MotionEvent getMotionEventForVertexView(MotionEvent e) {
        e.setLocation(e.getX() % VertexView.squareDimension(),
                    e.getY() % VertexView.squareDimension());
        return e;
    }

    public boolean onDownAction(MotionEvent e) {
        EditGraphLayout parentView = ((EditGraphLayout) getParent());
        int mode = parentView.getMode();
        if (mode == EditGraphLayout.CREATION_MODE) {
            onDown = new PointF(e.getX(), e.getY());
            invalidate();
            if (inFastEdition() && tFastEdition.isAlive()) {
                if (alphabet.isEmpty()) {
                    Toast.makeText(getContext(), "Ainda não há caracteres no alfabeto!",
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    if (alphabet.isOutOfIndexBounded(indFastEdition)) {
                        indFastEdition = 0;
                        Toast.makeText(getContext(), "Todos caracteres do alfabeto foram " +
                                "percorridos!", Toast.LENGTH_SHORT)
                                .show();
                    }
                    label = alphabet.get(indFastEdition);
                    setText(label);
                    indFastEdition++;
                    changeLabel = true;
                    invalidate();
                }
            }
        } else if (mode == EditGraphLayout.EDITION_MODE) {


        }
        Log.d(label + " - onDown", label + " - onDown" +
                " (" + e.getX() + ", " + e.getY() + ")");
        return true;
    }

    public boolean onContextClickAction(MotionEvent e) {
        EditGraphLayout parentView = ((EditGraphLayout) getParent());
        int mode = parentView.getMode();
        if (mode == EditGraphLayout.CREATION_MODE) {

        } else if (mode == EditGraphLayout.EDITION_MODE) {

        }
        Log.d(label + " - onContextClick", label + " - onContextClick" +
                " (" + e.getX() + ", " + e.getY() + ")");

        return true;
    }

    public void onLongPressAction(MotionEvent e) {
        EditGraphLayout parentView = ((EditGraphLayout) getParent());
        int mode = parentView.getMode();
        if (mode == EditGraphLayout.CREATION_MODE) {

        } else if (mode == EditGraphLayout.EDITION_MODE) {

        }
        Log.d(label + " - onLongPress", label + " - onLongPress" +
                " (" + e.getX() + ", " + e.getY() + ")");
    }

    public boolean onDoubleTapAction(MotionEvent e) {
        EditGraphLayout parentView = ((EditGraphLayout) getParent());
        int mode = parentView.getMode();
        if (mode == EditGraphLayout.CREATION_MODE) {
            parentView.removeEdgeView(this);
        } else if (mode == EditGraphLayout.EDITION_MODE) {
            onDown = new PointF(e.getX(), e.getY());
            float x = e.getX();
            e.setLocation(X_FOR_DISPATCH_TOUCH_EVENT_TO_EDIT_TEXT, e.getY());
            super.onTouchEvent(e);
            e.setLocation(x, e.getY());
            float distanceToPoint = edgeDraw.distanceToCircumferenceOfSourceVertex(onDown);
            float distanceToOtherCirc = edgeDraw.distanceFromCircumferences();
            float distInit = distanceToOtherCirc / 2.0f -
                    mTransitionText.measureText(label) / 2.0f;
            int indice = 0;
            label = getText().toString();
            while (indice < label.length() && distanceToPoint > distInit +
                    mTransitionText.measureText(label.substring(0, indice))) {
                indice++;
            }
            cursorInd = indice;
            //setSelection(indice, indice);
            //setSelection(indice);
            invalidate();
            Log.d("e", e.getX() + ", " + e.getY());
            Log.d(label + " - onCursor", label + " - onCursor" + " (" + e.getX() + ", " +
                    e.getY() + ")");
        }
        Log.d(label + " - Double Tap", "Tapped at: (" + e.getX() + "," + e.getY() + ")");

        return true;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return onDownAction(e);
        }

        @Override
        public boolean onContextClick(MotionEvent e) {
            return onContextClick(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            onLongPressAction(e);
        }

        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return onDoubleTapAction(e);
        }
    }

    private static class Alphabet {

        private Set<String> setAlphabet;
        private List<String> listAlphabet;

        public Alphabet() {
            setAlphabet = new HashSet<>();
            listAlphabet = new ArrayList<>();
        }

        public Alphabet(Collection<String> alphabet) {
            setAlphabet = new HashSet<>(alphabet);
            listAlphabet = new ArrayList<>(alphabet);
        }

        public boolean contains(String letter) {
            return setAlphabet.contains(letter);
        }

        public String get(int indice) {
            return listAlphabet.get(indice);
        }

        public boolean isEmpty() {
            return listAlphabet.isEmpty();
        }

        public boolean isOutOfIndexBounded(int indice) {
            return indice >= listAlphabet.size();
        }

        public int size() {
            return listAlphabet.size();
        }

        public boolean add(String letter) {
            if (!setAlphabet.contains(letter) && !letter.equals("?")) {
                setAlphabet.add(letter);
                listAlphabet.add(letter);
                return true;
            }
            return false;
        }

        public void update(String word) {
            for (int i = 0; i < word.length(); i++) {
                String letter = word.substring(i, i + 1);
                add(letter);
            }
        }

        public boolean remove(String letter) {
            listAlphabet.remove(letter);
            return setAlphabet.remove(letter);
        }

        public Set<String> getAlphabetSet() {
            return new HashSet<>(setAlphabet);
        }

        public List<String> getAlphabetList() {
            return new ArrayList<>(listAlphabet);
        }

        public int addAll(Collection<String> letters) {
            int cont = 0;
            for (String letter : letters) {
                if (add(letter)) {
                    cont++;
                }
            }
            return cont;
        }

        public int removeAll(Collection<String> letters) {
            int cont = 0;
            for (String letter : letters) {
                if (remove(letter)) {
                    cont++;
                }
            }
            return cont;
        }

    }

}
