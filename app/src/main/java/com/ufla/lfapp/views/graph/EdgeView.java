package com.ufla.lfapp.views.graph;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ufla.lfapp.R;
import com.ufla.lfapp.core.grammar.Grammar;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.fsa.FSATransitionFunction;
import com.ufla.lfapp.utils.Symbols;
import com.ufla.lfapp.views.graph.edge.EdgeDraw;
import com.ufla.lfapp.views.graph.edge.EdgeDrawFactory;
import com.ufla.lfapp.views.graph.edge.EdgeDrawType;
import com.ufla.lfapp.views.graph.layout.EditGraphLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by carlos on 9/21/16.
 * Representa a visão de um vértice em um gráfico.
 */


public class EdgeView extends View {

    public static String LABEL_ITEM_SEP = ",\n";
    public static String EMPTY_LABEL = Grammar.LAMBDA;
    private static Alphabet alphabet = new Alphabet(Arrays.asList(
            new String[] { "a", "0" } ) );
    private static final long TIME_FAST_EDITION = 3000;
    private static final float ARROW_ANGLE = (float) Math.toRadians(35.0f);
    private final static String INITIAL_LABEL = "";

    private Thread tFastEdition;
    private EdgeDraw edgeDraw;
    private int gridBeginHeight;
    private int gridBeginWidth;
    protected Pair<VertexView, VertexView> vertices;
    private EdgeView invertedEdge;
    private Paint mEdgeLine;
    private Paint mEdgeText;
    protected String label;
    protected List<Path> labelPaths;
    private boolean changeLabel = false;
    private GestureDetector gestureDetector;
    private boolean reflexiveUp;
    private boolean fastEdition;
    private PointF nearestPoint;



    public String[] getAlphabet() {
        return getLabelLines();
    }

    public void setInitialLabel() {
        label = INITIAL_LABEL;
    }

    public String getLabel() {
        return label;
    }

    public Pair<VertexView, VertexView> getVertices() {
        return vertices;
    }

    public void setLabel(String label) {
        this.label = label;
        dimensionLabelPath(label.split(LABEL_ITEM_SEP).length);
        invalidate();
    }

    public float distanceToObject(PointF point) {
        return edgeDraw.distanceToObject(point);
    }

    public boolean isOnInteractLabelArea(PointF point) {
        return edgeDraw.isOnInteractLabelArea(point);
    }

    public void appendLabel(String append) {
        if (label.isEmpty() || label.equals(Symbols.LAMBDA)) {
            label = append;
        } else {
            label = label + "," + append;
        }
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

    public static float getArrowAngle() {
        return ARROW_ANGLE;
    }

    public void loadFastEdition() {
        tFastEdition = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Activity activityContext = (Activity) getContext();
                    activityContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), R.string.fast_edition_mode,
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                    Thread.sleep(TIME_FAST_EDITION);
                    activityContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mEdgeText.setColor(Color.BLACK);
                            EditGraphLayout editGraphLayout = getParentEditGraphLayout();
                            if (editGraphLayout != null) {
                                editGraphLayout.stopFastEdition();
                            }
                            invalidate();
                        }
                    });
                } catch (InterruptedException e) {

                }
            }
        });
        tFastEdition.start();
    }

    public void reloadFastEdition() {
        tFastEdition.interrupt();
        loadFastEdition();
    }

    public boolean haveInvertedEdge() {
        return invertedEdge != null;
    }

    public void setInvertedEdge(EdgeView invertedEdge) {
        this.invertedEdge = invertedEdge;
        if (haveInvertedEdge()) {
            setEdgeDraw(EdgeDrawType.ARC_EDGE_DRAW);
        } else {
            setEdgeDraw(EdgeDrawType.LINE_EDGE_DRAW);
        }
        invalidate();
    }

    public boolean isInvertedEdge(EdgeView invertedEdge) {
        if (vertices != null && invertedEdge.vertices != null &&
                vertices.first.equals(invertedEdge.vertices.second) &&
                vertices.second.equals(invertedEdge.vertices.first)) {
            return true;
        }
        return false;
    }

    public EdgeView(Context context, boolean fastEdition) {
        super(context);
        this.fastEdition = fastEdition;
        init();
        defineDefault();
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
        this.mEdgeLine = new Paint();
        this.mEdgeLine.setAntiAlias(true);
        this.mEdgeLine.setStyle(Paint.Style.STROKE);
        mEdgeText = new Paint();
        this.mEdgeText.setAntiAlias(true);
        this.mEdgeText.setStyle(Paint.Style.FILL);
        this.mEdgeText.setTextAlign(Paint.Align.CENTER);
//        setInitialLabel();
        changeLabel = true;
        setBackgroundColor(Color.TRANSPARENT);
        gestureDetector = new GestureDetector(getContext(), new EdgeView.GestureListener());
        nearestPoint = null;
    }


    public void setVertices(Pair<VertexView, VertexView> vertices) {
        this.vertices = vertices;
    }

    public void setEdgeDraw(EdgeDrawType edgeDrawType) {
        EditGraphLayout parentView = getParentEditGraphLayout();
        this.edgeDraw = new EdgeDrawFactory()
                .createEdgeDraw(edgeDrawType, getGridPoints(), parentView);
        labelPaths = new ArrayList<>();
        labelPaths.add(edgeDraw.getLabelPath());
        invalidate();
    }

    public void setEdgeDraw() {
        Pair<Point, Point> gridPoints = getGridPoints();
        gridBeginHeight = Math.min(gridPoints.first.y, gridPoints.second.y);
        gridBeginWidth = Math.min(gridPoints.first.x, gridPoints.second.x);
        if (vertices.first.equals(vertices.second)) {
            if (reflexiveUp) {
                setEdgeDraw(EdgeDrawType.REFLEXIVE_UP_EDGE_DRAW);
                gridBeginHeight--;
            } else {
                setEdgeDraw(EdgeDrawType.REFLEXIVE_BOTTOM_EDGE_DRAW);
            }
        } else {
            setEdgeDraw(EdgeDrawType.LINE_EDGE_DRAW);
            if (gridPoints.first.y == gridPoints.second.y) {
                gridBeginHeight--;
            } else if (gridPoints.first.x == gridPoints.second.x) {
                gridBeginWidth--;
            }
        }
        invalidate();
    }

    public void removeDependenciesFromVertex() {
        vertices.first.removeEdgeDependencies(this);
        vertices.second.removeEdgeDependencies(this);
    }

    public void setStyle() {
        EditGraphLayout parentView = getParentEditGraphLayout();
        this.mEdgeText.setStrokeWidth(parentView.getEdgeTextStrokeWidth());
        this.mEdgeLine.setStrokeWidth(parentView.getEdgeLineStrokeWidth());
        this.mEdgeText.setTextSize(parentView.getEdgeTextSize());
        invalidate();
    }

    /**
     * Define valores padrões para os objetos Paint da transição.
     */
    private void defineDefault() {
        this.mEdgeLine.setColor(Color.BLACK);
        this.mEdgeText.setColor(Color.BLACK);
        //this.mEdgeText.setColor(Color.RED);
    }

    public boolean isOnInteractArea(PointF pointF) {
        boolean result = edgeDraw.isOnInteractArea(pointF);
        invalidate();
        return result;
    }

    public Pair<Point, Point> getGridPoints() {
        Point firstGridPoint = vertices.first.getGridPoint();
        Point secondGridPoint = vertices.second.getGridPoint();
        return Pair.create(firstGridPoint, secondGridPoint);
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

    public Set<FSATransitionFunction> getTransitionFuctions() {
        String currentState = vertices.first.getLabel();
        String futureState = vertices.second.getLabel();
        Set<FSATransitionFunction> FSATransitionFunctions = new HashSet<>();
        for (String symbol : label.split("[ ,\n]")) {
            if (symbol.length() > 0) {
                FSATransitionFunctions.add(new FSATransitionFunction(new State(currentState), symbol,
                        new State(futureState)));
            }
        }
        return FSATransitionFunctions;
    }

    private void addLabelPath() {
        int lines = labelPaths.size();
        float lineHeigh = mEdgeText.getTextSize() * lines;
        Pair<PointF, PointF> labelLine = edgeDraw.getLabelLine();
        Path path = new Path();
        path.moveTo(labelLine.first.x, labelLine.first.y - lineHeigh);
        path.lineTo(labelLine.second.x, labelLine.second.y - lineHeigh);
        labelPaths.add(path);
    }

    protected void dimensionLabelPath(int dimen) {
        while (labelPaths.size() < dimen) {
            addLabelPath();
        }
        if (labelPaths.size() > dimen) {
            labelPaths = labelPaths.subList(0, dimen);
        }
    }

    private Path getLabelPath(int index) {
        if (index <= labelPaths.size()) {
            dimensionLabelPath(index + 1);
        }
        return labelPaths.get(index);
    }

    protected String[] getLabelLines() {
        String[] lineLabels = new String[1];
        lineLabels[0] = label;
        return lineLabels;
    }

    private static int[] colors = new int[] { Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW };
    private static Random random = new Random();

    /**
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
//        mEdgeLine.setColor(colors[random.nextInt(colors.length)]);
//        mEdgeLine.setStyle(Paint.Style.FILL_AND_STROKE);
//        canvas.drawPath(edgeDraw.getPathInteractArea(), mEdgeLine);
//        mEdgeLine.setStyle(Paint.Style.STROKE);
//        mEdgeLine.setColor(Color.BLACK);

        canvas.drawPath(edgeDraw.getEdge(), mEdgeLine);
        canvas.drawPath(edgeDraw.getArrowHead(), mEdgeLine);
        if (fastEdition) {
            if (!inFastEdition()) {
                mEdgeText.setColor(Color.RED);
                loadFastEdition();
            } else if (changeLabel) {
                reloadFastEdition();
            }
        }
        changeLabel = false;
        mEdgeText.setTextAlign(edgeDraw.getPaintAlign());
        String labelLines[] = getLabelLines();
        int lenght = labelLines.length;
        for (int i = 0; i < lenght; i++) {
            canvas.drawTextOnPath(labelLines[i], getLabelPath(i), 0.0f, 0.0f,
                    mEdgeText);
        }



//        PointF newNearestPoint = edgeDraw.getInteractArea().getNearestPoint();
//        if (newNearestPoint != null) {
//            nearestPoint = newNearestPoint;
//        }
//        if (nearestPoint != null) {
//            EditGraphLayout parentView = getParentEditGraphLayout();
//            float strokeLineEdge = parentView.getEdgeLineStrokeWidth();
//            mEdgeLine.setColor(Color.RED);
//            mEdgeLine.setStrokeWidth(strokeLineEdge * 4);
//            canvas.drawCircle(nearestPoint.x, nearestPoint.y, 20, mEdgeLine);
//            canvas.drawPoint(nearestPoint.x, nearestPoint.y, mEdgeLine);
//            mEdgeLine.setColor(Color.BLACK);
//            mEdgeLine.setStrokeWidth(strokeLineEdge);
//        }
    }

    public EditGraphLayout getParentEditGraphLayout() {
        return (EditGraphLayout) getParent();
    }

    /**
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        EditGraphLayout parentLayout = getParentEditGraphLayout();
        int squareDimension = parentLayout.getVertexSquareDimension();
        int width = parentLayout.getColumnCount() * squareDimension;
        int height = parentLayout.getRowCount() * squareDimension;
//        int squareDimension = getParentEditGraphLayout().getVertexSquareDimension();
//        Point gridPointSourceVertex = vertices.first.getGridPoint();
//        Point gridPointTargetVertex = vertices.second.getGridPoint();
//        int width = (Math.abs(gridPointSourceVertex.x - gridPointTargetVertex.x) + 1) *
//                squareDimension;
//        int height = (Math.abs(gridPointSourceVertex.y - gridPointTargetVertex.y) + 1) *
//                squareDimension;
//        if (vertices.first.equals(vertices.second)) {
//            width = squareDimension;
//            height = squareDimension * 2;
//        } else if (gridPointSourceVertex.y == gridPointTargetVertex.y) {
//            height += squareDimension * 2;
//        } else if (gridPointSourceVertex.x == gridPointTargetVertex.x) {
//            width += squareDimension * 2;
//        }
        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public boolean onDownAction(MotionEvent e) {
        invalidate();
        if (fastEdition && inFastEdition() && tFastEdition.isAlive()) {
            label = getParentEditGraphLayout().nextSymbolFastEdition();
            changeLabel = true;
            invalidate();
        }
        return true;
    }

    public boolean onContextClickAction(MotionEvent e) {
        return true;
    }

    public void onLongPressAction(MotionEvent e) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE );
        final LinearLayout dialogEdge = (LinearLayout) inflater.inflate(R.layout.dialog_label_fsa_transition,
                null);
        final EditText labelEdge = (EditText) dialogEdge.findViewById(R.id.label_fsa_transition);
        labelEdge.setText(label);
        labelEdge.setEnabled(true);
        labelEdge.setSelectAllOnFocus(true);
        final AlertDialog alertDialog = builder.setView(dialogEdge)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        label = labelEdge.getText().toString();
                        if (label.isEmpty()) {
                            label = EMPTY_LABEL;
                        }
                        EdgeView.this.invalidate();
                        dialog.cancel();

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        //  Your code when user clicked on Cancel
                    }
                })
                .create();
        labelEdge.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    alertDialog.getWindow().setSoftInputMode(WindowManager
                            .LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });



        alertDialog.show();
    }

    public EdgeDrawType getEdgeDrawType() {
        return edgeDraw.getEdgeDrawType();
    }

    public boolean onDoubleTapAction(MotionEvent e) {
        EditGraphLayout parentView = ((EditGraphLayout) getParent());
        parentView.removeEdgeView(this);
        return true;
    }

    public void updateDraw() {
        setEdgeDraw(edgeDraw.getEdgeDrawType());
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return onDownAction(e);
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
        private Iterator<String> itAlphabet;
        private List<String> listAlphabet;

        public Alphabet() {
            setAlphabet = new HashSet<>();
            listAlphabet = new ArrayList<>();
            itAlphabet = setAlphabet.iterator();
        }

        public String nextSymbol() {
            if (!itAlphabet.hasNext()) {
                itAlphabet = setAlphabet.iterator();
            }
            return itAlphabet.next();
        }

        public Alphabet(Collection<String> alphabet) {
            setAlphabet = new HashSet<>(alphabet);
            listAlphabet = new ArrayList<>(alphabet);
            itAlphabet = setAlphabet.iterator();
        }

        public boolean contains(String letter) {
            return setAlphabet.contains(letter);
        }

        public String get(int indice) {
            return listAlphabet.get(indice);
        }

        public boolean isEmpty() {
            return setAlphabet.isEmpty();
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
