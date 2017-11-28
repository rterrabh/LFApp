package com.ufla.lfapp.views.graph.layout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.ufla.lfapp.R;
import com.ufla.lfapp.core.grammar.Grammar;
import com.ufla.lfapp.core.grammar.parser.NodeDerivationPosition;
import com.ufla.lfapp.core.grammar.parser.TreeDerivationPosition;
import com.ufla.lfapp.core.machine.State;
import com.ufla.lfapp.core.machine.dotlang.Edge;
import com.ufla.lfapp.core.machine.dotlang.GraphAdapter;
import com.ufla.lfapp.core.machine.fsa.FSATransitionFunction;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomaton;
import com.ufla.lfapp.core.machine.fsa.FiniteStateAutomatonGUI;
import com.ufla.lfapp.utils.MyPoint;
import com.ufla.lfapp.views.graph.BorderVertex;
import com.ufla.lfapp.views.graph.EdgeView;
import com.ufla.lfapp.views.graph.SpaceWithBorder;
import com.ufla.lfapp.views.graph.VertexView;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by carlos on 06/10/16.
 * Representa um <i>layout</i> para a criação e edição de autômatos ou máquinas de estados.
 * Permitindo criar, mover e editar estados em um <i>grid</i> e conectá-los com transições.
 */

public class EditGraphLayout extends GridLayout {

    public class Alphabet {

        private Deque<String> alphabet;
        private Set<String> setAlphabet;

        public Alphabet() {
            List<String> list = Arrays.asList(new String[]{"a", "0"});
            alphabet = new ArrayDeque<>(list);
            setAlphabet = new HashSet<>(Arrays.asList(new String[]{"a", "0"}));

        }

        public String nextSymbol() {
            String symbol = alphabet.pollFirst();
            alphabet.offerLast(symbol);
            return symbol;
        }

        public void stopFastEdition() {
            alphabet.offerFirst(alphabet.pollLast());
        }

        public void updateAlphabet(List<String> updates) {
            int n = updates.size() - 1;
            for (int i = 0; i < n; i++) {
                String symbol = updates.get(i);
                if (!setAlphabet.contains(symbol)) {
                    setAlphabet.add(symbol);
                    alphabet.offerFirst(symbol);
                }
            }
            String lastSymbol = updates.get(n);
            if (setAlphabet.contains(lastSymbol)) {
                alphabet.remove(lastSymbol);
            } else {
                setAlphabet.add(lastSymbol);
            }
            alphabet.offerFirst(lastSymbol);
        }


    }

    public void stopFastEdition() {
        alphabet.stopFastEdition();
    }

    public String nextSymbolFastEdition() {
        return alphabet.nextSymbol();
    }

    public void updateAlphabet(List<String> updates) {
        alphabet.updateAlphabet(updates);
    }

    public static int MAX_DISTANCE_FROM_EDGE;
    private Alphabet alphabet = new Alphabet();
    public static final int CREATION_MODE = 0;
    public static final int EDITION_MODE = 1;
    private int mode = 0;
    private static final int INITIAL_NUM_COLUMNS = 20;
    private static final int INITIAL_NUM_ROWS = 15;
    private static final int DIST_FREE_SPACE_MIN = 3;
    private boolean border = true;
    private int actualNumColumns;
    private int actualNumRows;
    protected VertexView initialState;
    protected View viewsOnGrid[][];
    private Set<EdgeView> edgesOnGrid[][];
    protected Set<EdgeView> edgeViews;
    private Set<EdgeView> edgeReflexiveViews;
    protected final GestureDetector gestureDetector;
    private boolean onSelectState;
    private VertexView stateSelect;
    private MyOnDragListener myOnDragListener = new MyOnDragListener();
    private boolean creationEdge;
    private Pair<PointF, PointF> createEdge;
    private VertexView createEdgeFirstVertex;
    private Paint mTransitionLine;
    private boolean defineInitialState;
    private String errorStateLabel;
    public VertexView onMove;
    private Set<FSATransitionFunction> FSATransitionFunctionsToCompAuto;
    private float sizeReference = 1.0f;
    private final static float dpi = Resources.getSystem().getDisplayMetrics().densityDpi;
    // Atributos relacionados ao tamanho dos vértices
    private int vertexRadius;
    private int vertexSpace;
    private float vertexTextSize;
    private float vertexTextStrokeWidth;
    private float vertexLineStrokeWidth;
    private float vertexErrorRectLabel;
    private float vertexInitialStateSize;
    // Atributos relacionados ao tamanho das arestas
    private float edgeTextStrokeWidth;
    private float edgeLineStrokeWidth;
    private float edgeTextSize;
    private float edgeArrowHeadLenght;
    // Atributos relacionados aos estilos
    private Paint mVertexBorderPaint;
    private float vertexBorderStrokeWidth;
    protected final ScaleGestureDetector scaleGestureDetector;


    public void defineInitialAlphabetSymbol() {
        if (edgeViews.size() != 1) {
            return;
        }
        for (EdgeView edgeView : edgeViews) {

        }
    }

    // COMEÇO - Getters para os atributos relacionados ao tamanho dos vértices
    public int getVertexRadius() {
        return vertexRadius;
    }

    public int getVertexSpace() {
        return vertexSpace;
    }

    public float getVertexTextSize() {
        return vertexTextSize;
    }

    public float getVertexTextStrokeWidth() {
        return vertexTextStrokeWidth;
    }

    public float getVertexLineStrokeWidth() {
        return vertexLineStrokeWidth;
    }

    public float getVertexErrorRectLabel() {
        return vertexErrorRectLabel;
    }

    public float getVertexInitialStateSize() {
        return vertexInitialStateSize;
    }
    // FIM - Getters para os atributos relacionados ao tamanho dos vértices

    // COMEÇO - Getters para os atributos relacionados ao tamanho das arestas
    public float getEdgeTextStrokeWidth() {
        return edgeTextStrokeWidth;
    }

    public float getEdgeLineStrokeWidth() {
        return edgeLineStrokeWidth;
    }

    public float getEdgeTextSize() {
        return edgeTextSize;
    }

    public float getEdgeArrowHeadLenght() {
        return edgeArrowHeadLenght;
    }
    // FIM - Getters para os atributos relacionados ao tamanho das arestas

    private void setmVertexBorderPaint() {
        mVertexBorderPaint = new Paint();
        mVertexBorderPaint.setAntiAlias(true);
        mVertexBorderPaint.setStyle(Paint.Style.STROKE);
        mVertexBorderPaint.setStrokeWidth(vertexBorderStrokeWidth);
        mVertexBorderPaint.setColor(Color.BLACK);
    }

    public Paint getmVertexBorderPaint() {
        return mVertexBorderPaint;
    }

    /**
     * Retorna o lado dos quadrados onde os vértices são desenhados.
     *
     * @return lado dos quadrados onde os vértices são desenhados
     */
    public int getVertexSquareDimension() {
        return (vertexRadius + vertexSpace) * 2;
    }

    /**
     * Retorna o valor x que representa as coordenadas (x, x) do ponto central dos vértices
     * desenhados.
     *
     * @return valor x que representa as coordenadas (x, x) do ponto central dos vértices desenhados
     */
    public int getVertexCenterPoint() {
        return vertexRadius + vertexSpace;
    }

    private void setSizeAndStyleOfViews() {
        setSizeOfViews();
        setStyleOfViews();
        if (getParent() != null && getParent() instanceof HorizontalScrollView) {
            HorizontalScrollView parentView = (HorizontalScrollView) getParent();
            //parentView.removeView(this);
            //parentView.addView(this);
        }
        updateDraw();
        invalidate();
    }

    private void updateDraw() {
        for (int y = 0; y < viewsOnGrid.length; y++) {
            for (int x = 0; x < viewsOnGrid[y].length; x++) {
                if (viewsOnGrid[y][x] instanceof VertexView) {
                    VertexView vertexView = (VertexView) viewsOnGrid[y][x];
                    vertexView.updateDraw();
                    if (vertexView.isInitialState() && vertexView.equals(viewsOnGrid[y][x])) {
                        removeView(viewsOnGrid[y][x]);
                        addView(viewsOnGrid[y][x], new LayoutParams(GridLayout.spec(y),
                                GridLayout.spec(x, 2)));
                        viewsOnGrid[y][x].invalidate();
                        x++;
                        continue;
                    }
                }
                removeView(viewsOnGrid[y][x]);
                addView(viewsOnGrid[y][x], new LayoutParams(GridLayout.spec(y),
                        GridLayout.spec(x)));
                viewsOnGrid[y][x].invalidate();
            }
        }

        for (EdgeView edgeView : edgeViews) {
            edgeView.updateDraw();
            removeView(edgeView);
        }

        for (EdgeView edgeView : edgeViews) {
            addView(edgeView, recoveryGridLayoutParams(edgeView));
        }
        invalidate();
    }


    public int getHeightLayout() {
        return actualNumRows * getVertexSquareDimension();
    }

    public int getWidthLayout() {
        return actualNumColumns * getVertexSquareDimension();
    }

    private void setStyleOfViews() {
        setmVertexBorderPaint();
        for (int i = 0; i < viewsOnGrid.length; i++) {
            for (int j = 0; j < viewsOnGrid[i].length; j++) {
                if (viewsOnGrid[i][j] instanceof VertexView) {
                    ((VertexView) viewsOnGrid[i][j]).setStyle();
                } else {
                    viewsOnGrid[i][j].invalidate();
                }
            }
        }
        for (EdgeView edgeView : edgeViews) {
            edgeView.setStyle();
        }
    }

    private void setSizeOfViews() {
        setSizeOfVertexViews();
        setSizeOfEdgeViews();
    }

    private void setSizeOfVertexViews() {
        vertexRadius = (int) ((dpi / 5.0f) * sizeReference);
        vertexSpace = (int) ((dpi / 40.0f) * sizeReference);
        vertexTextSize = (dpi / 8.0f) * sizeReference;
        vertexTextStrokeWidth = (dpi / 240.0f) * sizeReference;
        vertexLineStrokeWidth = (dpi / 96.0f) * sizeReference;
        vertexErrorRectLabel = (dpi / 32.0f) * sizeReference;
        vertexInitialStateSize = (int) ((dpi / 8.0f) * sizeReference);
        vertexBorderStrokeWidth = (dpi / 800.0f) * sizeReference;
        setMaxDistanceFromEdge();
    }

    private void setSizeOfEdgeViews() {
        edgeTextStrokeWidth = (dpi / 160.0f) * sizeReference;
        edgeLineStrokeWidth = (dpi / 160.0f) * sizeReference;
        edgeTextSize = (dpi / 9.6f) * sizeReference;
        edgeArrowHeadLenght = (dpi / 19.2f) * sizeReference;
    }

    public void setSizeReferenceForChildViews(float sizeReference) {
        if (sizeReference < 0) {
            throw new IllegalArgumentException(getContext().getString(R.string.exception_size_reference));
        }
        this.sizeReference = sizeReference;
        setSizeAndStyleOfViews();
    }

    public void selectErrorState(String errorStateLabel,
                                 Set<FSATransitionFunction> FSATransitionFunctionsToCompAuto) {
        this.errorStateLabel = errorStateLabel;
        this.FSATransitionFunctionsToCompAuto = FSATransitionFunctionsToCompAuto;
    }

    public void removeSpaces() {
        removeSpaces(0, 0);
    }

    public void betterVerticalVisualisation() {
        if (actualNumColumns > actualNumRows) {
            transpose();
        }
    }

    public void betterHorizontalVisualisation() {
        if (actualNumRows > actualNumColumns) {
            transpose();
        }
    }

    public void transpose() {
        removeAllViews();
        int yLength = viewsOnGrid.length;
        int xLength = viewsOnGrid[0].length;
        actualNumRows = xLength;
        actualNumColumns = yLength;
        setColumnCount(actualNumColumns);
        setRowCount(actualNumRows);
        invalidate();
        View newViewsOnGrid[][] = new View[xLength][yLength];
        Set<EdgeView> newEdgesOnGrid[][] = new HashSet[actualNumRows][actualNumColumns];
        for (int i = 0; i < yLength; i++) {
            for (int j = 0; j < xLength; j++) {
                if (viewsOnGrid[i][j] instanceof VertexView) {
                    if (viewsOnGrid[i][j].equals(initialState)) {
                        if (j > 0 && viewsOnGrid[i][j].equals(viewsOnGrid[i][j - 1])) {
                            VertexView vertexView = (VertexView) viewsOnGrid[i][j];
                            Point gridPoint = new Point(i, j);
                            vertexView.setGridPoint(gridPoint);
                            vertexView.setBorderVertex(getBorderVertex(gridPoint));
                            vertexView.setLeft(gridPoint.x - 1 == 0);
                            addView(vertexView, new LayoutParams(GridLayout.spec(gridPoint.y),
                                    GridLayout.spec(gridPoint.x - 1, 2)));
                            vertexView.setVertexDraw();
                            vertexView.setStyle();
                        } else {
                            newViewsOnGrid[j][i] = viewsOnGrid[i][j];
                            newEdgesOnGrid[j][i] = edgesOnGrid[i][j];
                            continue;
                        }
                    } else {
                        VertexView vertexView = (VertexView) viewsOnGrid[i][j];
                        Point gridPoint = new Point(i, j);
                        vertexView.setGridPoint(gridPoint);
                        vertexView.setBorderVertex(getBorderVertex(gridPoint));
                        addView(vertexView, new LayoutParams(GridLayout.spec(gridPoint.y),
                                GridLayout.spec(gridPoint.x)));
                        vertexView.setVertexDraw();
                        vertexView.setStyle();
                    }
                } else {
                    SpaceWithBorder spaceWithBorder = (SpaceWithBorder) viewsOnGrid[i][j];
                    Point gridPoint = new Point(i, j);
                    spaceWithBorder.setBorderVertex(getBorderVertex(gridPoint));
                    addView(spaceWithBorder, new LayoutParams(GridLayout.spec(gridPoint.y),
                            GridLayout.spec(gridPoint.x)));
                }
                newViewsOnGrid[j][i] = viewsOnGrid[i][j];
                newEdgesOnGrid[j][i] = edgesOnGrid[i][j];
            }
        }
        viewsOnGrid = newViewsOnGrid;
        edgesOnGrid = newEdgesOnGrid;
        for (EdgeView edgeView : edgeViews) {
            Pair<Point, Point> gridPoints = edgeView.getGridPoints();
            addView(edgeView, getGridLayoutParams(gridPoints));
            edgeView.setStyle();
            edgeView.setEdgeDraw();
        }
    }

    public void resizeTo(int width, int height) {
        removeSpaces(width, height);
//        betterVerticalVisualisation();
        width += (actualNumColumns / (float) width) * 1.10f;
        height += (actualNumRows / (float) height) * 1.10f;
        float newSizeReference = (Math.min(width / (float) actualNumColumns,
                height / (float) actualNumRows)) / (2.0f * (dpi / 5.0f + dpi / 40.0f));
        setSizeReferenceForChildViews(newSizeReference);
    }

    public void removeSpaces(int minWidth, int minHeight) {
        Point minPoint = new Point();
        minPoint.y = viewsOnGrid.length;
        minPoint.x = viewsOnGrid[0].length;
        Point maxPoint = new Point();
        maxPoint.y = 0;
        maxPoint.x = 0;
        for (int i = 0; i < viewsOnGrid.length; i++) {
            for (int j = 0; j < viewsOnGrid[i].length; j++) {
                if (viewsOnGrid[i][j] instanceof VertexView) {
                    if (i < minPoint.y) {
                        minPoint.y = i;
                    }
                    if (j < minPoint.x) {
                        minPoint.x = j;
                    }
                    if (i > maxPoint.y) {
                        maxPoint.y = i;
                    }
                    if (j > maxPoint.x) {
                        maxPoint.x = j;
                    }
                }
            }
        }
        if (minPoint.x > 0) {
            minPoint.x--;
        }
        if (minPoint.y > 0) {
            minPoint.y--;
        }
        maxPoint.y++;
        maxPoint.x++;
//        for (EdgeView reflexiveView : edgeReflexiveViews) {
//            if (reflexiveView.isReflexiveUp() &&
//                    reflexiveView.getGridPoints().first.y == minPoint.y) {
//                minPoint.y--;
//            }
//            if (!reflexiveView.isReflexiveUp() &&
//                    reflexiveView.getGridPoints().first.y == maxPoint.y) {
//                maxPoint.y++;
//            }
//        }
        int squareDimension = getVertexSquareDimension();
        int minHeightSquares = minHeight / squareDimension;
        if ((maxPoint.y - minPoint.y + 1) < minHeightSquares) {
            maxPoint.y = minHeightSquares + minPoint.y - 1;
            if (maxPoint.y > viewsOnGrid.length - 1) {
                growRows(maxPoint.y - (viewsOnGrid.length - 1));
            }
        }
        int minWidthSquares = minWidth / squareDimension;
        if ((maxPoint.x - minPoint.x + 1) < minWidthSquares) {
            maxPoint.x = minWidthSquares + minPoint.x - 1;
            if (maxPoint.x > viewsOnGrid[0].length - 1) {
                growColumns(maxPoint.x - (viewsOnGrid[0].length - 1));
            }
        }
//        for (int i = 0; i < viewsOnGrid.length; i++) {
//            for (int j = 0; j < viewsOnGrid[i].length; j++) {
//                if (j == minPoint.x && i >= minPoint.y && i <= maxPoint.y) {
//                    j = maxPoint.x;
//                } else {
//                    removeView(viewsOnGrid[i][j]);
//                }
//            }
//        }
        maxPoint.x++;
        maxPoint.y++;
        actualNumColumns = maxPoint.x - minPoint.x;
        actualNumRows = maxPoint.y - minPoint.y;
        removeAllViews();
        setRowCount(actualNumRows);
        setColumnCount(actualNumColumns);
        invalidate();
        View newViewsOnGrid[][] = new View[actualNumRows][actualNumColumns];
        for (int i = minPoint.y; i < maxPoint.y; i++) {
            for (int j = minPoint.x; j < maxPoint.x; j++) {
                newViewsOnGrid[i - minPoint.y][j - minPoint.x] = viewsOnGrid[i][j];
                if (viewsOnGrid[i][j] instanceof VertexView) {
                    if (viewsOnGrid[i][j].equals(initialState)) {
                        if (j > 0 && viewsOnGrid[i][j].equals(viewsOnGrid[i][j - 1])) {
                            VertexView vertexView = (VertexView) viewsOnGrid[i][j];
                            Point gridPoint = new Point(j - minPoint.x, i - minPoint.y);
                            vertexView.setGridPoint(gridPoint);
                            vertexView.setBorderVertex(getBorderVertex(gridPoint));
                            vertexView.setLeft(gridPoint.x - 1 == 0);
                            addView(vertexView, new LayoutParams(GridLayout.spec(gridPoint.y),
                                    GridLayout.spec(gridPoint.x - 1, 2)));
                            vertexView.setVertexDraw();
                            vertexView.setStyle();
                        } else {
                            continue;
                        }
                    } else {
                        VertexView vertexView = (VertexView) viewsOnGrid[i][j];
                        Point gridPoint = new Point(j - minPoint.x, i - minPoint.y);
                        vertexView.setGridPoint(gridPoint);
                        vertexView.setBorderVertex(getBorderVertex(gridPoint));
                        addView(vertexView, new LayoutParams(GridLayout.spec(gridPoint.y),
                                GridLayout.spec(gridPoint.x)));
                        vertexView.setVertexDraw();
                        vertexView.setStyle();
                    }
                } else {
                    SpaceWithBorder spaceWithBorder = (SpaceWithBorder) viewsOnGrid[i][j];
                    Point gridPoint = new Point(j - minPoint.x, i - minPoint.y);
                    spaceWithBorder.setBorderVertex(getBorderVertex(gridPoint));
                    addView(spaceWithBorder, new LayoutParams(GridLayout.spec(gridPoint.y),
                            GridLayout.spec(gridPoint.x)));
                }
            }
        }
        viewsOnGrid = newViewsOnGrid;
        Set<EdgeView> newEdgesOnGrid[][] = new HashSet[actualNumRows][actualNumColumns];
        for (int i = minPoint.y; i < maxPoint.y; i++) {
            for (int j = minPoint.x; j < maxPoint.x; j++) {
                newEdgesOnGrid[i - minPoint.y][j - minPoint.x] = edgesOnGrid[i][j];
            }
        }
        edgesOnGrid = newEdgesOnGrid;
        for (EdgeView edgeView : edgeViews) {
            Pair<Point, Point> gridPoints = edgeView.getGridPoints();
            addView(edgeView, getGridLayoutParams(gridPoints));
            edgeView.setStyle();
            edgeView.setEdgeDraw();
        }
        setSizeAndStyleOfViews();
    }

    private String toStringPoint(Point p) {
        return "(" + p.x + ", " + p.y + ")";
    }

    public VertexView getInitialState() {
        return initialState;
    }


    public void setOnMove(VertexView vertexView) {
        onMove = vertexView;
    }

    public boolean isOnMove() {
        return onMove != null;
    }


    private void drawDerivationTreeVertex(TreeDerivationPosition tree) {
        List<NodeDerivationPosition> nodes = tree.getNodes();
        for (NodeDerivationPosition node : nodes) {
            addVertexView(node.getPosition().toPoint(), node.getNode());
        }
    }

    private void drawDerivationTreeEdges(TreeDerivationPosition tree) {
        Map<Integer, List<List<NodeDerivationPosition>>> nodesByLevel =
                tree.getNodesByLevel();
        for (Map.Entry<Integer, List<List<NodeDerivationPosition>>> entry :
                nodesByLevel.entrySet()) {
            if (entry.getKey() == 0) {
                continue;
            }
            for (List<NodeDerivationPosition> nodesSingleFather : entry.getValue()) {
                NodeDerivationPosition father = nodesSingleFather.get(0).getFather();
                Point fatherPosition = father.getPosition().toPoint();
                for (NodeDerivationPosition node : nodesSingleFather) {
                    addEdgeView(fatherPosition, node.getPosition().toPoint(), "");
                }
            }
        }
    }

    public void drawDerivationTree(TreeDerivationPosition tree) {
        EdgeView.EMPTY_LABEL = "";
        border = false;
        drawDerivationTreeVertex(tree);
        drawDerivationTreeEdges(tree);
    }

    public void drawGraph(GraphAdapter graphAdapter) {
        clear();
        for (Map.Entry<State, MyPoint> entry : graphAdapter.stateMyPointMap.entrySet()) {
            addVertexView(entry.getValue().toPoint(), entry.getKey().getName());
        }
        State initialState = graphAdapter.startState;
        if (initialState != null) {
            setVertexViewAsInitial(graphAdapter.stateMyPointMap.get(initialState).toPoint());
        }
        for (State state : graphAdapter.stateFinals) {
            setVertexViewAsFinal(graphAdapter.stateMyPointMap.get(state).toPoint());
        }
        for (Edge edge : graphAdapter.edgeList) {
            addEdgeView(
                    graphAdapter.stateMyPointMap.get(edge.current).toPoint(),
                    graphAdapter.stateMyPointMap.get(edge.future).toPoint(),
                    edge.label);
        }
        naiveVerifyReflexives();
        invalidate();
    }

    public void drawAutomaton(FiniteStateAutomatonGUI automatonGUI) {
        clear();
        for (Map.Entry<State, Point> entry : automatonGUI.getStateGridPoint().entrySet()) {
            addVertexView(entry.getValue(), entry.getKey().getName());
        }
        State initialState = automatonGUI.getInitialState();
        if (initialState != null) {
            setVertexViewAsInitial(automatonGUI.getGridPositionPoint(initialState));
        }
        for (State state : automatonGUI.getFinalStates()) {
            setVertexViewAsFinal(automatonGUI.getGridPositionPoint(state));
        }
        for (Map.Entry<Pair<State, State>, SortedSet<String>> entry :
                automatonGUI.getTransitionsAFD().entrySet()) {
            StringBuilder sb = new StringBuilder();
            for (String str : entry.getValue()) {
                sb.append(str)
                        .append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
            addEdgeView(automatonGUI.getGridPositionPoint(entry.getKey().first),
                    automatonGUI.getGridPositionPoint(entry.getKey().second), sb.toString());
        }
        naiveVerifyReflexives();
        invalidate();
    }

    public void clear() {
        for (int i = 0; i < viewsOnGrid.length; i++) {
            for (int j = 0; j < viewsOnGrid[i].length; j++) {
                if (viewsOnGrid[i][j] instanceof VertexView &&
                        !viewsOnGrid[i][j + 1].equals(initialState)) {
                    VertexView vertexView = (VertexView) viewsOnGrid[i][j];
                    removeVertexView(vertexView.getGridPoint());
                }
            }
        }
        VertexView.clearContVertex();
        invalidate();
    }

    public FiniteStateAutomatonGUI getCompleteAutomatonGUI() {
        if (initialState == null) {
            Toast.makeText(getContext(), R.string.exception_not_find_initial_state,
                    Toast.LENGTH_SHORT)
                    .show();
            return null;
        }
        FiniteStateAutomatonGUI automatonGUI = getAutomatonGUI();
        if (automatonGUI.getFinalStates().isEmpty()) {
            Toast.makeText(getContext(), R.string.exception_not_find_final_state, Toast.LENGTH_SHORT)
                    .show();
            return null;
        }
        return automatonGUI;
    }

    public FiniteStateAutomatonGUI getAutomatonGUI() {
        SortedMap<State, Point> stateGridPositions = new TreeMap<>();
        State initialState = (this.initialState == null) ? null :
                new State(this.initialState.getLabel());
        SortedSet<State> states = new TreeSet<>();
        SortedSet<State> finalStates = new TreeSet<>();
        SortedSet<FSATransitionFunction> FSATransitionFunctions = new TreeSet<>();
        for (int i = 0; i < viewsOnGrid.length; i++) {
            for (int j = 0; j < viewsOnGrid[i].length; j++) {
                if (viewsOnGrid[i][j] instanceof VertexView) {
                    VertexView vertexView = (VertexView) viewsOnGrid[i][j];
                    State label = new State(vertexView.getLabel());
//                    if (states.contains(label) && !vertexView.equals(this.initialState)) {
//                        Toast.makeText(getContext(), getContext().getString(R.string.exception_states_same_name) + label +
//                                ")!", Toast.LENGTH_SHORT)
//                                .show();
//                        return null;
//                    }
                    stateGridPositions.put(label, new Point(j, i));
                    states.add(label);
                    if (vertexView.isFinalState()) {
                        finalStates.add(label);
                    }
                }
            }
        }
        for (EdgeView edgeView : edgeViews) {
            FSATransitionFunctions.addAll(edgeView.getTransitionFuctions());
        }
        return new FiniteStateAutomatonGUI(new FiniteStateAutomaton(states, initialState, finalStates,
                FSATransitionFunctions), stateGridPositions);
    }

    public int getMode() {
        return mode;
    }

    public boolean onDefineInitialState() {
        return defineInitialState;
    }

    public boolean isOnSelectErrorState() {
        return errorStateLabel != null;
    }

    public void defineInitialStateMode() {
        defineInitialState = true;
    }

    public boolean isStateSelected() {
        return onSelectState;
    }

    public void setOnStateSelected(boolean onSelectState) {
        this.onSelectState = onSelectState;
    }

    public VertexView getStateSelect() {
        return stateSelect;
    }

    public void setStateSelect(VertexView stateSelect) {
        this.stateSelect = stateSelect;
    }

    public EditGraphLayout(Context context, float sizeReference) {
        super(context);
        this.sizeReference = sizeReference;
        this.gestureDetector = new GestureDetector(getContext(), new GestureListener());
        this.scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        init();
    }

    public EditGraphLayout(Context context) {
        super(context);
        this.gestureDetector = new GestureDetector(getContext(), new GestureListener());
        this.scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        init();
    }

    public EditGraphLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.gestureDetector = new GestureDetector(getContext(), new GestureListener());
        this.scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        init();
    }

    public EditGraphLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.gestureDetector = new GestureDetector(getContext(), new GestureListener());
        this.scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        init();
    }

    public void setInitialState(VertexView vertexView) {
        Point gridPoint = vertexView.getGridPoint();
        if (gridPoint.x - 1 >= 0 &&
                viewsOnGrid[gridPoint.y][gridPoint.x - 1] instanceof VertexView) {
            Toast.makeText(getContext(), "Não é possível definir como inicial,\n" +
                    "pois há um estado ao lado esquerdo.\nMova um dos estados.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (gridPoint.x - 1 < 0) {
            growColumnsLeft(3);
            vertexView = (VertexView) viewsOnGrid[gridPoint.y][gridPoint.x];
        }
        if (gridPoint.x - 1 == 0) {
            vertexView.setLeft(true);
        }
        if (initialState != null) {
            removeInitialState();
        }
        initialState = vertexView;
        removeView(viewsOnGrid[gridPoint.y][gridPoint.x - 1]);
        viewsOnGrid[gridPoint.y][gridPoint.x - 1] = vertexView;
        removeView(initialState);
        addView(initialState, new LayoutParams(GridLayout.spec(gridPoint.y),
                GridLayout.spec(gridPoint.x - 1, 2)));
        initialState.setInitialState(true);
        initialState.setStyle();
        defineInitialState = false;
    }

    private SpaceWithBorder getSpaceWithBorder(Point gridPoint) {
        SpaceWithBorder space = SpaceWithBorder.getSpaceWithBorder(this,
                getBorderVertex(gridPoint));
        return space;
    }

    public void removeInitialState() {
        removeView(initialState);
        Point gridPoint = initialState.getGridPoint();
        if (gridPoint.x - 1 == 0) {
            initialState.setLeft(false);
        }
        addView(initialState, new LayoutParams(GridLayout.spec(gridPoint.y),
                GridLayout.spec(gridPoint.x)));
        initialState.setInitialState(false);
        initialState.setStyle();
        SpaceWithBorder space = getSpaceWithBorder(new Point(gridPoint.x - 1, gridPoint.y));
        viewsOnGrid[gridPoint.y][gridPoint.x - 1] = space;
        addView(space, new LayoutParams(GridLayout.spec(gridPoint.y),
                GridLayout.spec(gridPoint.x - 1)));
        initialState = null;
    }

    public void removeScrollViews() {
        if (getParent() != null) {
            HorizontalScrollView parentView = (HorizontalScrollView) getParent();
            parentView.removeView(this);
        }
    }

    private void setMaxDistanceFromEdge() {
        MAX_DISTANCE_FROM_EDGE = getVertexRadius();
    }


    /**
     * Inicializa o AutomataViewB criando os scrolls horizontal e vertical, adicionando-os como
     * pais do AutomaViewB. Também realiza o preenchimento do <i>grid</i> com espaços em branco.
     */
    public void init() {
        EdgeView.EMPTY_LABEL = Grammar.LAMBDA;
        border = false;
        VertexView.clearContVertex();
        setSizeOfViews();
        setmVertexBorderPaint();
        ScrollView scrollView = new ScrollView(getContext()) {
            @Override
            public boolean onTouchEvent(MotionEvent e) {
                float oldSizeReference = sizeReference;
                boolean touch = EditGraphLayout.this.scaleGestureDetector.onTouchEvent(e);
                if (oldSizeReference == sizeReference) {
                    touch = super.onTouchEvent(e);
                }
                return touch;
            }
        };
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        HorizontalScrollView hScrollView = new HorizontalScrollView(getContext()) {
            @Override
            public boolean onTouchEvent(MotionEvent e) {
                float oldSizeReference = sizeReference;
                boolean touch = EditGraphLayout.this.scaleGestureDetector.onTouchEvent(e);
                if (oldSizeReference == sizeReference) {
                    touch = super.onTouchEvent(e);
                }
                return touch;
            }
        };
        hScrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        if (actualNumColumns < EditGraphLayout.INITIAL_NUM_COLUMNS) {
            actualNumColumns = EditGraphLayout.INITIAL_NUM_COLUMNS;
        }
        if (actualNumRows < EditGraphLayout.INITIAL_NUM_ROWS) {
            actualNumRows = EditGraphLayout.INITIAL_NUM_ROWS;
        }
        setColumnCount(actualNumColumns);
        setRowCount(actualNumRows);
        viewsOnGrid = new View[actualNumRows][actualNumColumns];
        edgesOnGrid = new Set[actualNumRows][actualNumColumns];
        hScrollView.addView(this);
        scrollView.addView(hScrollView);
        fillSpace();
        onSelectState = false;
        stateSelect = null;
        edgeViews = new HashSet<>();
        setSizeAndStyleOfViews();
        this.mTransitionLine = new Paint();
        this.mTransitionLine.setAntiAlias(true);
        this.mTransitionLine.setStyle(Paint.Style.STROKE);
        this.mTransitionLine.setColor(Color.BLACK);
        float dpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        this.mTransitionLine.setStrokeWidth(dpi / 160.0f);
        createEdge = new Pair<>(new PointF(), new PointF());
        edgeReflexiveViews = new HashSet<>();
    }

    public void fillEdges() {
        for (int i = 0; i < actualNumColumns; i++) {
            for (int j = 0; j < actualNumRows; j++) {
                edgesOnGrid[j][i] = null;
            }
        }
    }

    private void growColumns() {
        growColumns(INITIAL_NUM_COLUMNS);
    }

    /**
     * Realiza o crescimento do <i>grid</i> em termos de colunas. Aumenta a quantidade de colunas
     * em 1.5 vezes do tamanho atual.
     */
    private void growColumns(int newColumns) {
        if (newColumns < 1) {
            return;
        }
        for (int i = 0; i < viewsOnGrid.length; i++) {
            if (viewsOnGrid[i][actualNumColumns - 1] instanceof VertexView) {
                ((VertexView) viewsOnGrid[i][actualNumColumns - 1]).setRight(false);
            }
            if (viewsOnGrid[i][actualNumColumns - 1] instanceof SpaceWithBorder) {
                ((SpaceWithBorder) viewsOnGrid[i][actualNumColumns - 1]).setRight(false);
            }
        }
        View viewsOnGridOld[][] = viewsOnGrid;
        Set<EdgeView> edgesOnGridOld[][] = edgesOnGrid;
        int oldNumColumns = actualNumColumns;
        actualNumColumns += newColumns;
        viewsOnGrid = new View[actualNumRows][actualNumColumns];
        edgesOnGrid = new Set[actualNumRows][actualNumColumns];
        for (int i = 0; i < viewsOnGridOld.length; i++) {
            for (int j = 0; j < viewsOnGridOld[i].length; j++) {
                viewsOnGrid[i][j] = viewsOnGridOld[i][j];
                edgesOnGrid[i][j] = edgesOnGridOld[i][j];
            }
        }
        setColumnCount(actualNumColumns);
        invalidate();
        for (int i = 0; i < actualNumRows; i++) {
            for (int j = oldNumColumns; j < actualNumColumns; j++) {
                SpaceWithBorder space = getSpaceWithBorder(new Point(j, i));

                viewsOnGrid[i][j] = space;
                addView(space, new LayoutParams(GridLayout.spec(i), GridLayout.spec(j)));
            }
        }
        invalidate();
    }

    private void growRowsTop(int newRows) {
        if (newRows < 1) {
            return;
        }
        View viewsOnGridOld[][] = viewsOnGrid;
        Set<EdgeView> edgesOnGridOld[][] = edgesOnGrid;
        int oldNumRows = actualNumRows;
        actualNumRows += newRows;
        setRowCount(actualNumRows);
        viewsOnGrid = new View[actualNumRows][actualNumColumns];
        edgesOnGrid = new Set[actualNumRows][actualNumColumns];

        for (int j = 0; j < actualNumColumns; j++) {
            for (int i = 0; i < newRows; i++) {
                SpaceWithBorder space = getSpaceWithBorder(new Point(j, i));
                viewsOnGrid[i][j] = space;
                addView(space, new LayoutParams(GridLayout.spec(i), GridLayout.spec(j)));
            }
            for (int i = oldNumRows; i < actualNumRows; i++) {
                SpaceWithBorder space = getSpaceWithBorder(new Point(j, i));
                viewsOnGrid[i][j] = space;
                addView(space, new LayoutParams(GridLayout.spec(i), GridLayout.spec(j)));
            }
        }
        invalidate();

        for (int i = newRows; i < actualNumRows; i++) {
            for (int j = 0; j < actualNumColumns; j++) {
                viewsOnGrid[i][j] = viewsOnGridOld[i - newRows][j];
                edgesOnGrid[i][j] = edgesOnGridOld[i - newRows][j];
                if (viewsOnGrid[i][j] instanceof VertexView &&
                        viewsOnGrid[i][j] != viewsOnGridOld[i - newRows][j + 1]) {
                    VertexView vertexView = (VertexView) viewsOnGrid[i][j];
                    Point gridPoint = vertexView.getGridPoint();
                    gridPoint.y += newRows;
                }
            }
        }
        for (int i = newRows; i < actualNumRows; i++) {
            for (int j = actualNumColumns - 1; j >= 0; j--) {
                if (viewsOnGrid[i][j] instanceof VertexView) {
                    VertexView vertexView = (VertexView) viewsOnGrid[i][j];
                    if (viewsOnGrid[i][j + 1] == initialState) {
                        continue;
                    }
                    onMove = vertexView;
                    moveVertexViewWithoutVerification(vertexView.getGridPoint());
                } else {
                    removeView(viewsOnGrid[i][j]);
                    SpaceWithBorder space = getSpaceWithBorder(new Point(j, i));
                    viewsOnGrid[i][j] = space;
                    addView(space, new LayoutParams(GridLayout.spec(i), GridLayout.spec(j)));
                }
            }
        }
        invalidate();
    }

    private void growColumnsLeft(int newColumns) {
        if (newColumns < 1) {
            return;
        }
        View viewsOnGridOld[][] = viewsOnGrid;
        Set<EdgeView> edgesOnGridOld[][] = edgesOnGrid;
        int oldNumColumns = actualNumColumns;
        actualNumColumns += newColumns;
        setColumnCount(actualNumColumns);
        viewsOnGrid = new View[actualNumRows][actualNumColumns];
        edgesOnGrid = new Set[actualNumRows][actualNumColumns];

        for (int i = 0; i < actualNumRows; i++) {
            for (int j = 0; j < newColumns; j++) {
                SpaceWithBorder space = getSpaceWithBorder(new Point(j, i));
                viewsOnGrid[i][j] = space;
                addView(space, new LayoutParams(GridLayout.spec(i), GridLayout.spec(j)));
            }
            for (int j = oldNumColumns; j < actualNumColumns; j++) {
                SpaceWithBorder space = getSpaceWithBorder(new Point(j, i));
                viewsOnGrid[i][j] = space;
                addView(space, new LayoutParams(GridLayout.spec(i), GridLayout.spec(j)));
            }
        }
        invalidate();

        for (int i = 0; i < actualNumRows; i++) {
            for (int j = newColumns; j < actualNumColumns; j++) {
                viewsOnGrid[i][j] = viewsOnGridOld[i][j - newColumns];
                edgesOnGrid[i][j] = edgesOnGridOld[i][j - newColumns];
                if (viewsOnGrid[i][j] instanceof VertexView &&
                        viewsOnGrid[i][j] != viewsOnGridOld[i][j - newColumns + 1]) {
                    VertexView vertexView = (VertexView) viewsOnGrid[i][j];
                    Point gridPoint = vertexView.getGridPoint();
                    gridPoint.x += newColumns;
                }
            }
        }
        for (int i = 0; i < actualNumRows; i++) {
            for (int j = actualNumColumns - 1; j >= newColumns; j--) {
                if (viewsOnGrid[i][j] instanceof VertexView) {
                    VertexView vertexView = (VertexView) viewsOnGrid[i][j];
                    if (viewsOnGrid[i][j + 1] == initialState) {
                        continue;
                    }
                    onMove = vertexView;
                    moveVertexViewWithoutVerification(vertexView.getGridPoint());
                } else {
                    removeView(viewsOnGrid[i][j]);
                    SpaceWithBorder space = getSpaceWithBorder(new Point(j, i));
                    viewsOnGrid[i][j] = space;
                    addView(space, new LayoutParams(GridLayout.spec(i), GridLayout.spec(j)));
                }
            }
        }
        invalidate();
    }

    private void growRows() {
        growRows(INITIAL_NUM_ROWS);
    }

    /**
     * Realiza o crescimento do <i>grid</i> em termos de linhas. Aumenta a quantidade de linhas
     * em 1.5 vezes do tamanho atual.
     */
    private void growRows(int newRows) {
        if (newRows < 1) {
            return;
        }
        for (int i = 0; i < viewsOnGrid[actualNumRows - 1].length; i++) {
            if (viewsOnGrid[actualNumRows - 1][i] instanceof VertexView) {
                ((VertexView) viewsOnGrid[actualNumRows - 1][i]).setBottom(false);
            }
            if (viewsOnGrid[actualNumRows - 1][i] instanceof SpaceWithBorder) {
                ((SpaceWithBorder) viewsOnGrid[actualNumRows - 1][i]).setBottom(false);
            }
        }
        View viewsOnGridOld[][] = viewsOnGrid;
        Set<EdgeView> edgesOnGridOld[][] = edgesOnGrid;
        int oldNumRows = actualNumRows;
        actualNumRows += newRows;
        viewsOnGrid = new View[actualNumRows][actualNumColumns];
        edgesOnGrid = new Set[actualNumRows][actualNumColumns];
        for (int i = 0; i < viewsOnGridOld.length; i++) {
            for (int j = 0; j < viewsOnGridOld[i].length; j++) {
                viewsOnGrid[i][j] = viewsOnGridOld[i][j];
                edgesOnGrid[i][j] = edgesOnGridOld[i][j];
            }
        }
        setRowCount(actualNumRows);
        invalidate();
        for (int i = oldNumRows; i < actualNumRows; i++) {
            for (int j = 0; j < actualNumColumns; j++) {
                SpaceWithBorder space = getSpaceWithBorder(new Point(j, i));

                viewsOnGrid[i][j] = space;
                addView(space, new LayoutParams(GridLayout.spec(i), GridLayout.spec(j)));
            }
        }
        invalidate();
    }

    /**
     * Realiza o crescimento do <i>grid</i> em termos de linhas. Aumenta a quantidade de linhas
     * em 1.5 vezes do tamanho atual.
     */
    private void growRowsUp(int newRows) {
        if (newRows < 1) {
            return;
        }
        for (int i = 0; i < viewsOnGrid[0].length; i++) {
            if (viewsOnGrid[0][i] instanceof VertexView) {
                ((VertexView) viewsOnGrid[0][i]).setTop(false);
            }
            if (viewsOnGrid[0][i] instanceof SpaceWithBorder) {
                ((SpaceWithBorder) viewsOnGrid[0][i]).setTop(false);
            }
        }
        View viewsOnGridOld[][] = viewsOnGrid;
        Set<EdgeView> edgesOnGridOld[][] = edgesOnGrid;
        int oldNumRows = actualNumRows;
        actualNumRows += newRows;
        viewsOnGrid = new View[actualNumRows][actualNumColumns];
        edgesOnGrid = new Set[actualNumRows][actualNumColumns];
        for (int i = 0; i < viewsOnGridOld.length; i++) {
            for (int j = 0; j < viewsOnGridOld[i].length; j++) {
                viewsOnGrid[i][j] = viewsOnGridOld[i][j];
                edgesOnGrid[i][j] = edgesOnGridOld[i][j];
            }
        }
        setRowCount(actualNumRows);
        invalidate();
        for (int i = oldNumRows; i < actualNumRows; i++) {
            for (int j = 0; j < actualNumColumns; j++) {
                SpaceWithBorder space = getSpaceWithBorder(new Point(j, i));

                viewsOnGrid[i][j] = space;
                addView(space, new LayoutParams(GridLayout.spec(i), GridLayout.spec(j)));
            }
        }
        invalidate();
    }



    private VertexView viewOnDrag;
    private String msg;

    public VertexView addVertexView(final Point gridPoint, String label) {
        VertexView vertexView = addVertexView(gridPoint);
        vertexView.setLabel(label);
        return vertexView;
    }

    public void setVertexViewAsFinal(final Point gridPoint) {
        if (viewsOnGrid[gridPoint.y][gridPoint.x] instanceof VertexView) {
            VertexView vertexView = (VertexView) viewsOnGrid[gridPoint.y][gridPoint.x];
            vertexView.setFinalState(true);
            vertexView.invalidate();
        }

    }

    public void setVertexViewAsInitial(final Point gridPoint) {
        if (viewsOnGrid[gridPoint.y][gridPoint.x] instanceof VertexView) {
            setInitialState((VertexView) viewsOnGrid[gridPoint.y][gridPoint.x]);
        }
    }

    public void validatePoint(final Point gridPoint) {
        while (actualNumRows - gridPoint.y < DIST_FREE_SPACE_MIN) {
            growRows();
        }
        while (actualNumColumns - gridPoint.x < DIST_FREE_SPACE_MIN) {
            growColumns();
        }
    }

    private BorderVertex getBorderVertex(final Point gridPoint) {
        if (!border) {
            return new BorderVertex(false, false, false, false);
        }
        return new BorderVertex(gridPoint.x == 0,
                gridPoint.y == 0,
                gridPoint.x == actualNumColumns - 1,
                gridPoint.y == actualNumRows - 1);
    }

    /**
     * Adiciona um estado nas coordenadas informadas por parâmetros.
     *
     * @param gridPoint linha do estado a ser adicionado.
     */
    public VertexView addVertexView(final Point gridPoint) {
        validatePoint(gridPoint);
        final VertexView vertexView = new VertexView(getContext());
        vertexView.setBorderVertex(getBorderVertex(gridPoint));
        vertexView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        vertexView.setGridPoint(gridPoint);
        removeView(viewsOnGrid[gridPoint.y][gridPoint.x]);
        viewsOnGrid[gridPoint.y][gridPoint.x] = vertexView;
        addView(vertexView, new LayoutParams(GridLayout.spec(gridPoint.y),
                GridLayout.spec(gridPoint.x)));
        vertexView.setVertexDraw();
        vertexView.setStyle();

//        vertexView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                ClipData data = ClipData.newPlainText("", "");
//                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(vertexView);
//                vertexView.startDrag(data, shadowBuilder, vertexView, 0);
//                vertexView.setVisibility(View.INVISIBLE);
//                return true;
//            }
//        });
        naiveVerifyReflexives();
        vertexView.setOnDragListener(myOnDragListener);
        return vertexView;

    }

    public boolean isDefiniedLabel(String label) {
        return false;
    }


    class MyOnDragListener implements OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Do nothing
                    break;

                case DragEvent.ACTION_DRAG_ENTERED:
                    int x_cord = (int) event.getX();
                    int y_cord = (int) event.getY();
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    x_cord = (int) event.getX();
                    y_cord = (int) event.getY();
                    Point gridPoint = new Point();
                    gridPoint.x = (int) (x_cord / getVertexSquareDimension());
                    gridPoint.y = (int) (y_cord / getVertexSquareDimension());
                    View view = viewsOnGrid[gridPoint.y][gridPoint.x];
                    if (view instanceof SpaceWithBorder && v instanceof VertexView) {
                        VertexView vv = (VertexView) v;
                        removeView(vv);
                        removeView(view);
                        Point gridPointVer = vv.getGridPoint();
                        viewsOnGrid[gridPointVer.y][gridPointVer.x] = view;
                        viewsOnGrid[gridPoint.y][gridPoint.x] = vv;
                        vv.setGridPoint(gridPoint);
                        addView(vv, new LayoutParams(GridLayout.spec(gridPoint.y),
                                GridLayout.spec(gridPoint.x)));
                        vv.setStyle();
                        addView(view, new LayoutParams(GridLayout.spec(gridPointVer.y),
                                GridLayout.spec(gridPointVer.x)));
                    }
                    break;

                case DragEvent.ACTION_DRAG_LOCATION:
                    x_cord = (int) event.getX();
                    y_cord = (int) event.getY();
                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    // Do nothing
                    break;

                case DragEvent.ACTION_DROP:
                    x_cord = (int) event.getX();
                    y_cord = (int) event.getY();
                    Point gridPointA = new Point();
                    gridPointA.x = (int) (x_cord / getVertexSquareDimension());
                    gridPointA.y = (int) (y_cord / getVertexSquareDimension());
                    View viewA = viewsOnGrid[gridPointA.y][gridPointA.x];
                    if (viewA instanceof SpaceWithBorder && v instanceof VertexView) {
                        VertexView vv = (VertexView) v;
                        removeView(vv);
                        removeView(viewA);
                        Point gridPointVer = vv.getGridPoint();
                        viewsOnGrid[gridPointVer.y][gridPointVer.x] = viewA;
                        viewsOnGrid[gridPointA.y][gridPointA.x] = vv;
                        vv.setGridPoint(gridPointA);
                        addView(vv, new LayoutParams(GridLayout.spec(gridPointA.y),
                                GridLayout.spec(gridPointA.x)));
                        vv.setStyle();
                        addView(viewA, new LayoutParams(GridLayout.spec(gridPointVer.y),
                                GridLayout.spec(gridPointVer.x)));
                    }
                    // Do nothing
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    public void removeVertexView(Point gridPoint) {
        final VertexView vertexView = (VertexView) viewsOnGrid[gridPoint.y][gridPoint.x];
        if (vertexView.equals(onMove)) {
            onMove = null;
        }
        if (vertexView.equals(initialState)) {
            removeInitialState();
        }
        SpaceWithBorder space = getSpaceWithBorder(gridPoint);
        Set<EdgeView> edgeViewsDependencies = new HashSet<>(vertexView.getEdgeDependencies());
        for (EdgeView edgeView : edgeViewsDependencies) {
            removeEdgeView(edgeView);
        }
        removeView(vertexView);
        viewsOnGrid[gridPoint.y][gridPoint.x] = space;
        addView(space, new LayoutParams(GridLayout.spec(gridPoint.y),
                GridLayout.spec(gridPoint.x)));

        naiveVerifyReflexives();
    }

    /**
     * Remove uma aresta do gráfico.
     *
     * @param edgeView aresta a ser removida
     */
    public void removeEdgeView(EdgeView edgeView) {
        Log.d("REMOVE_EDGE", "REMOVE_EDGE");
        nullableEdgesOnView(edgeView);
        removeView(edgeView);
        edgeViews.remove(edgeView);
        edgeReflexiveViews.remove(edgeView);
        edgeView.removeDependenciesFromVertex();
        naiveVerifyReflexives();
    }

    /**
     * Retorna o estado que está na posição (<i>row</i>, <i>column</i>) do <i>grid</i>. Se não
     * possui estado retorna <i>null</i>.
     *
     * @param gridPoint coluna do estado
     * @return retorna o estado que está na posição (<i>row</i>, <i>column</i>) do <i>grid</i>.
     * Se não possui estado retorna <i>null</i>.
     */
    public VertexView getVertexView(Point gridPoint) {
        return (viewsOnGrid[gridPoint.y][gridPoint.x] instanceof VertexView) ?
                (VertexView) viewsOnGrid[gridPoint.y][gridPoint.x] : null;
    }

    public EdgeView addEdgeView(Point sourcePoint, Point targetPoint, String label) {
        return addEdgeView(getVertexView(sourcePoint), getVertexView(targetPoint), label);
    }

    public EdgeView addEdgeView(VertexView sourceVertex, VertexView targetVertex, String label) {
        EdgeView edgeView = addEdgeView(sourceVertex, targetVertex, false);
        edgeView.setLabel(label);
        return edgeView;
    }

    public EdgeView addEdgeView(VertexView sourceVertex, VertexView targetVertex) {
        return addEdgeView(sourceVertex, targetVertex, true);
    }

    public boolean haveSpace(int x, int y) {
        if (x < 0 || y < 0 || x >= actualNumColumns || y >= actualNumRows) {
            return false;
        }
        return viewsOnGrid[y][x] instanceof SpaceWithBorder && (edgesOnGrid[y][x] == null
                || edgesOnGrid[y][x].isEmpty());
    }

    public int getSpace(int x, int y) {
        if (x < 0 || y < 0 || x >= actualNumColumns || y >= actualNumRows) {
            return -1;
        }
        int space = (viewsOnGrid[y][x] instanceof SpaceWithBorder) ? 0 : 1;
        if (edgesOnGrid[y][x] != null) {
            space += edgesOnGrid[y][x].size();
        }
        return space;
    }

    private GridLayout.Spec getSpec(int limit1, int limit2) {
        return GridLayout.spec(Math.min(limit1, limit2), Math.abs(limit1 - limit2) + 1);
    }

    private GridLayout.LayoutParams recoveryGridLayoutParams(EdgeView edgeView) {
        Pair<Point, Point> gridPoints = edgeView.getGridPoints();
        return getGridLayoutParams(gridPoints);
//        GridLayout.Spec rowSpec;
//        GridLayout.Spec columnSpec;
//
//        switch(edgeView.getEdgeDrawType()) {
//            case ARC_EDGE_DRAW:
//            case LINE_EDGE_DRAW:
//                return getGridLayoutParams(gridPoints);
//            case REFLEXIVE_BOTTOM_EDGE_DRAW:
//                rowSpec = GridLayout.spec(gridPoints.first.y, 2);
//                columnSpec = GridLayout.spec(gridPoints.first.x);
//                break;
//            case REFLEXIVE_UP_EDGE_DRAW:
//                rowSpec = GridLayout.spec(gridPoints.first.y - 1, 2);
//                columnSpec = GridLayout.spec(gridPoints.first.x);
//                break;
//            default:
//                throw new RuntimeException(getContext().getString(R.string.exception_edge_draw_not_definied));
//        }
//        return new GridLayout.LayoutParams(rowSpec, columnSpec);
    }

    private GridLayout.LayoutParams getGridLayoutParams(Pair<Point, Point> gridPoints) {
        GridLayout.Spec rowSpec = GridLayout.spec(0, getRowCount());
        GridLayout.Spec columnSpec = GridLayout.spec(0, getColumnCount());
        return new GridLayout.LayoutParams(rowSpec, columnSpec);
        /*
        if (gridPoints.first.equals(gridPoints.second)) {
            columnSpec = GridLayout.spec(gridPoints.first.x);
            final int rowSize = 2;
            if (isReflexiveUp(gridPoints.first.x, gridPoints.first.y)) {
                rowSpec = GridLayout.spec(gridPoints.first.y - 1, rowSize);
            } else {
                rowSpec = GridLayout.spec(gridPoints.first.y, rowSize);
            }
        } else {
            if (gridPoints.first.x == gridPoints.second.x) {
                rowSpec = getSpec(gridPoints.first.y, gridPoints.second.y);
                columnSpec = getSpec(gridPoints.first.x - 1, gridPoints.second.x + 1);
            } else if (gridPoints.first.y == gridPoints.second.y) {
                rowSpec = getSpec(gridPoints.first.y - 1, gridPoints.second.y + 1);
                columnSpec = getSpec(gridPoints.first.x, gridPoints.second.x);
            } else {
                rowSpec = getSpec(gridPoints.first.y, gridPoints.second.y);
                columnSpec = getSpec(gridPoints.first.x, gridPoints.second.x);
            }
        }
        return new GridLayout.LayoutParams(rowSpec, columnSpec);
        */
    }

    public EdgeView newEdgeView(boolean toast) {
        return new EdgeView(getContext(), toast);
    }

    public void verifyEdgeOnBoards(Point sourceGridPoint, Point targetGridPoint) {
        if (sourceGridPoint.x == 0 && targetGridPoint.x == 0) {
            growColumnsLeft(3);
        } else if (sourceGridPoint.x == actualNumColumns - 1
                && targetGridPoint.x == actualNumColumns - 1) {
            growColumns(3);
        } else if (sourceGridPoint.y == 0 && targetGridPoint.y == 0) {
            growRowsTop(3);
        } else if (sourceGridPoint.y == actualNumRows - 1
                && targetGridPoint.y == actualNumRows - 1) {
            growRows(3);
        }
    }

    /**
     * Adiciona uma transição entre dois estados. Por parâmetro é recebido as coordenadas dos
     * estado atual e futuro, respectivamente, também recebe o símbolo da transição.
     */
    public EdgeView addEdgeView(VertexView sourceVertex, VertexView targetVertex, boolean toast) {
        verifyEdgeOnBoards(sourceVertex.getGridPoint(), targetVertex.getGridPoint());
        final EdgeView edgeView = newEdgeView(toast);
        edgeView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Pair<Point, Point> gridPoints = Pair.create(sourceVertex.getGridPoint(),
                targetVertex.getGridPoint());
        edgeView.setVertices(Pair.create(sourceVertex, targetVertex));
        if (sourceVertex.equals(targetVertex)
                && haveSpace(gridPoints.first.x, gridPoints.first.y - 1)) {
            edgeView.setReflexiveUp(true);
        }
        EdgeView invertedEdge = null;
        for (EdgeView edgeView1 : edgeViews) {
            if (edgeView.equals(edgeView1)) {
                if (toast) {
                    Toast.makeText(getContext(), R.string.exception_transition_exists, Toast.LENGTH_SHORT)
                            .show();
                }
                return edgeView1;
            }
            if (edgeView.isInvertedEdge(edgeView1)) {
                invertedEdge = edgeView1;
            }
        }
        sourceVertex.addEdgeDependencies(edgeView);
        targetVertex.addEdgeDependencies(edgeView);
        edgeViews.add(edgeView);
        addView(edgeView, getGridLayoutParams(gridPoints));
        edgeView.setInitialLabel();
        edgeView.setStyle();
        edgeView.setEdgeDraw();
        setEdgesOnView(edgeView, gridPoints);
        if (invertedEdge != null) {
            edgeView.setInvertedEdge(invertedEdge);
            invertedEdge.setInvertedEdge(edgeView);
        }
        naiveVerifyReflexives();
        return edgeView;
    }

    public boolean isReflexiveUp(int x, int y) {
        int spaceUp = getSpace(x, y - 1);
        int spaceDown = getSpace(x, y + 1);
        if (spaceUp == -1) {
            return false;
        }
        return spaceUp <= spaceDown;
    }

    public void naiveVerifyReflexives() {
        for (EdgeView edgeView1 : edgeReflexiveViews) {
            Pair<Point, Point> gridPoints1 = edgeView1.getGridPoints();
            boolean reflexiveUpLast = edgeView1.isReflexiveUp();
            if (reflexiveUpLast) {
                edgesOnGrid[gridPoints1.first.y - 1][gridPoints1.first.x].remove(edgeView1);
            } else {
                edgesOnGrid[gridPoints1.first.y + 1][gridPoints1.first.x].remove(edgeView1);
            }
            boolean reflexiveUp = isReflexiveUp(gridPoints1.first.x, gridPoints1.first.y);
            if (reflexiveUp ^ reflexiveUpLast) {
                edgeView1.setReflexiveUp(reflexiveUp);
                removeView(edgeView1);
                addView(edgeView1, getGridLayoutParams(gridPoints1));
                edgeView1.setEdgeDraw();
            }
            if (reflexiveUp) {
                if (edgesOnGrid[gridPoints1.first.y - 1][gridPoints1.first.x] == null) {
                    edgesOnGrid[gridPoints1.first.y - 1][gridPoints1.first.x] = new HashSet<>();
                }
                edgesOnGrid[gridPoints1.first.y - 1][gridPoints1.first.x].add(edgeView1);
            } else {
                if (edgesOnGrid[gridPoints1.first.y + 1][gridPoints1.first.x] == null) {
                    edgesOnGrid[gridPoints1.first.y + 1][gridPoints1.first.x] = new HashSet<>();
                }
                edgesOnGrid[gridPoints1.first.y + 1][gridPoints1.first.x].add(edgeView1);
            }
        }
    }

    public void setEdgesOnView(EdgeView edgeView, Pair<Point, Point> gridPoints) {
        int xLimitInf, xLimitSup, yLimitInf, yLimitSup;

        if (gridPoints.first.x < gridPoints.second.x) {
            xLimitInf = gridPoints.first.x;
            xLimitSup = gridPoints.second.x;
        } else {
            xLimitInf = gridPoints.second.x;
            xLimitSup = gridPoints.first.x;
        }

        if (gridPoints.first.y < gridPoints.second.y) {
            yLimitInf = gridPoints.first.y;
            yLimitSup = gridPoints.second.y;
        } else {
            yLimitInf = gridPoints.second.y;
            yLimitSup = gridPoints.first.y;
        }

        switch (edgeView.getEdgeDrawType()) {
            case REFLEXIVE_BOTTOM_EDGE_DRAW:
                yLimitSup++;
                edgeReflexiveViews.add(edgeView);
                break;
            case REFLEXIVE_UP_EDGE_DRAW:
                yLimitInf--;
                edgeReflexiveViews.add(edgeView);
                break;
            case LINE_EDGE_DRAW:
            case ARC_EDGE_DRAW:
                if (gridPoints.first.y == gridPoints.second.y) {
                    yLimitInf--;
                    yLimitSup++;
                } else if (gridPoints.first.x == gridPoints.second.x) {
                    xLimitInf--;
                    xLimitSup++;
                }
                break;
            default:
                throw new RuntimeException(getContext().getString(R.string.exception_edge_draw_not_definied));
        }


        for (int j = yLimitInf; j <= yLimitSup; j++) {
            for (int i = xLimitInf; i <= xLimitSup; i++) {
                if (edgesOnGrid[j][i] == null) {
                    edgesOnGrid[j][i] = new HashSet<>();
                }
                edgesOnGrid[j][i].add(edgeView);
//                if (!edgeReflexiveViews.contains(edgeView)) {
//                    Set<EdgeView> edgeViewsAux = new HashSet<>(edgesOnGrid[j][i]);
//                    edgeViewsAux.retainAll(edgeReflexiveViews);
//                    for (EdgeView edgeView1 : edgeViewsAux) {
//                        Pair<VertexView, VertexView> vertices = edgeView1.getVertices();
//                        removeEdgeView(edgeView1);
//                        addEdgeView(vertices.first, vertices.second, edgeView1.getIndex());
//                    }
//                }

            }
        }
    }

    public void nullableEdgesOnView(EdgeView edgeView) {
        Pair<Point, Point> gridPoints = edgeView.getGridPoints();
        if (gridPoints.first.equals(gridPoints.second)) {
            edgesOnGrid[gridPoints.first.y][gridPoints.first.x].remove(edgeView);
            if (edgeView.isReflexiveUp()) {
                edgesOnGrid[gridPoints.first.y - 1][gridPoints.first.x].remove(edgeView);
            } else {
                edgesOnGrid[gridPoints.first.y + 1][gridPoints.first.x].remove(edgeView);
            }
            return;
        }
        Point gridPointSourceVertex = gridPoints.first;
        Point gridPointTargetVertex = gridPoints.second;

        int menorX, maiorX, menorY, maiorY;

        if (gridPointSourceVertex.x < gridPointTargetVertex.x) {
            menorX = gridPointSourceVertex.x;
            maiorX = gridPointTargetVertex.x;
        } else {
            menorX = gridPointTargetVertex.x;
            maiorX = gridPointSourceVertex.x;
        }

        if (gridPointSourceVertex.y < gridPointTargetVertex.y) {
            menorY = gridPointSourceVertex.y;
            maiorY = gridPointTargetVertex.y;
        } else {
            menorY = gridPointTargetVertex.y;
            maiorY = gridPointSourceVertex.y;
        }

        if (menorX == maiorX) {
            if (menorX > 0) {
                menorX--;
            }
            if (maiorX < actualNumColumns - 1) {
                maiorX++;
            }
        }
        if (menorY == maiorY) {
            if (menorY > 0) {
                menorY--;
            }
            if (maiorY < actualNumColumns - 1) {
                maiorY++;
            }
        }

        for (int j = menorY; j <= maiorY; j++) {
            for (int i = menorX; i <= maiorX; i++) {
                edgesOnGrid[j][i].remove(edgeView);
            }
        }

    }

    /**
     * Preenche todo o <i>grid</i> com espa&ccedil;os em branco. Utiliza a classe Space, uma <i>view</i>
     * leve que representa espa&ccedil;os em branco.
     */
    private void fillSpace() {
        for (int i = 0; i < actualNumColumns; i++) {
            for (int j = 0; j < actualNumRows; j++) {
                SpaceWithBorder space = getSpaceWithBorder(new Point(i, j));

                viewsOnGrid[j][i] = space;
                addView(space, new LayoutParams(GridLayout.spec(j), GridLayout.spec(i)));
            }
        }
    }

    public Map<String, VertexView> getVertexViewByLabel() {
        Map<String, VertexView> vertexViewByLabel = new HashMap<>();
        for (int i = 0; i < viewsOnGrid.length; i++) {
            for (int j = 0; j < viewsOnGrid[i].length; j++) {
                if (viewsOnGrid[i][j] instanceof VertexView) {
                    VertexView vertexView = (VertexView) viewsOnGrid[i][j];
                    String label = vertexView.getLabel();
                    vertexViewByLabel.put(label, vertexView);
                }
            }
        }
        return vertexViewByLabel;
    }

    /**
     * Retorna a <i>view</i> raiz desta <i>view</i>. A <i>view</i> raiz deve ser usada para
     * inserir esta <i>view</i> em um <i>layout</i>. Pois esta view possui dois <i>scroll
     * views</i> como pais, um horizontal e um vertical, portanto não é possível inserir esta
     * <i>view</i> em um <i>layout</i> somente a sua <i>view root</i>.
     *
     * @return <i>view</i> raiz desta <i>view</i>.
     */
    public View getRootView() {
        if (getParent() != null && getParent().getParent() != null) {
            return (View) getParent().getParent();
        }
        if (getParent() != null) {
            return (View) getParent();
        }
        return this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (creationEdge) {
            canvas.drawLine(createEdge.first.x, createEdge.first.y, createEdge.second.x,
                    createEdge.second.y, mTransitionLine);
        }
        super.onDraw(canvas);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float oldSizeReference = sizeReference;
        boolean touch = scaleGestureDetector.onTouchEvent(e);
        if (oldSizeReference == sizeReference) {
            touch = gestureDetector.onTouchEvent(e);
        }
        return touch;
    }

    private Point gridPoint(float x, float y) {
        Point gridPoint = new Point();
        gridPoint.x = (int) (x / getVertexSquareDimension());
        gridPoint.y = (int) (y / getVertexSquareDimension());
        return gridPoint;
    }

    private MotionEvent getMotionEventForVertexView(MotionEvent e) {
        e.setLocation(e.getX() % getVertexSquareDimension(),
                e.getY() % getVertexSquareDimension());
        return e;
    }

    private MotionEvent getMotionEventForEdgeView(MotionEvent e, int height, int width) {
        return e;
    }

    public void moveVertexViewWithoutVerification(Point newPoint) {
        VertexView onMoveCpRef = onMove;
        Set<EdgeView> edgeViewDepencies = new HashSet<>(onMoveCpRef.getEdgeDependencies());
        boolean isInitialState = onMoveCpRef.isInitialState();
        removeVertexView(onMoveCpRef.getGridPoint());
        VertexView newOnMove = addVertexView(newPoint, onMoveCpRef.getLabel(), isInitialState,
                onMoveCpRef.isFinalState());
        Iterator<EdgeView> edgeViewIterator = edgeViewDepencies.iterator();
        while (edgeViewIterator.hasNext()) {
            EdgeView edgeView = edgeViewIterator.next();
            Pair<VertexView, VertexView> vertices = edgeView.getVertices();
            if (vertices.first.equals(onMoveCpRef) && vertices.second.equals(onMoveCpRef)) {
                addEdgeView(newOnMove, newOnMove, edgeView.getLabel());
            } else if (vertices.first.equals(onMoveCpRef)) {
                addEdgeView(newOnMove, vertices.second, edgeView.getLabel());
            } else {
                addEdgeView(vertices.first, newOnMove, edgeView.getLabel());
            }
        }
        naiveVerifyReflexives();
    }

    public void moveVertexView(Point newPoint) {
        if (onMove.equals(initialState) && (newPoint.x == 0 ||
                viewsOnGrid[newPoint.y][newPoint.x - 1] instanceof VertexView) ) {
            Toast.makeText(getContext(), R.string.exception_move_state,
                    Toast.LENGTH_LONG)
                    .show();
            onMove.onSelect();
            onMove = null;
            return;
        }
        moveVertexViewWithoutVerification(newPoint);
    }

    public void addErrorState(Point point) {
        VertexView errorState = addVertexView(point);
        errorState.setLabel(errorStateLabel);
        Map<String, VertexView> vertexViewByLabel = getVertexViewByLabel();
        vertexViewByLabel.put(errorStateLabel, errorState);
        for (FSATransitionFunction FSATransitionFunction : FSATransitionFunctionsToCompAuto) {
            addEdgeView(vertexViewByLabel.get(FSATransitionFunction.getCurrentState().getName()),
                    vertexViewByLabel.get(FSATransitionFunction.getFutureState().getName()),
                    FSATransitionFunction.getSymbol());
        }
        FSATransitionFunctionsToCompAuto = null;
        errorStateLabel = null;
    }

    private VertexView addVertexView(Point gridPoint, String label, boolean initialState,
                                     boolean finalState) {
        VertexView vertexView = addVertexView(gridPoint, label);
        if (initialState) {
            setInitialState(vertexView);
        }
        vertexView.setFinalState(finalState);
        return vertexView;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float newSizeReference = sizeReference * detector.getScaleFactor();
            setSizeReferenceForChildViews(Math.max(0.1f, Math.min(newSizeReference, 5.f)));

            return true;
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        /*
        private Runnable runnableOnOneClick;
        private Thread threadControl;

        public GestureListener() {
            threadControl = new Thread();
        }

        public Runnable getThreadVertexOneClick(final VertexView vertexView, final MotionEvent e) {
            return new Runnable() {
                @Override
                public void run() {
                    vertexView.onDownAction(e);
                }
            };
        }

        public Runnable getThreadEdgeOneClick(final EdgeView edgeView, final MotionEvent e) {
            return new Runnable() {
                @Override
                public void run() {
                    edgeView.onDownAction(getMotionEventForEdgeView(e,
                            edgeView.getGridBeginHeight(), edgeView.getGridBeginWidth()));
                }
            };
        }

        public Runnable getThreadVertexErrorOneClick(final Point point) {
            return new Runnable() {
                @Override
                public void run() {
                    addErrorState(point);
                }
            };
        }

        public Runnable getThreadMoveVertexOneClick(final Point point) {
            return new Runnable() {
                @Override
                public void run() {
                    moveVertexView(point);
                }
            };
        }

        public Runnable getThreadAddVertexOneClick(final Point point) {
            return new Runnable() {
                @Override
                public void run() {
                    addVertexView(point);
                }
            };
        }

        public Thread getThreadControl(final Runnable runnable) {
            return new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(200);
                        synchronized (EditGraphLayout.this) {
                            ((Activity) getContext()).runOnUiThread(runnable);
                        }
                    } catch (InterruptedException e) {
                    }
                }
            };
        }

        public boolean onOneClick(final MotionEvent e) {
            Point gridPoint = gridPoint(e.getX(), e.getY());
            View view = viewsOnGrid[gridPoint.y][gridPoint.x];
            Set<EdgeView> edgeOnGridPoint = edgesOnGrid[gridPoint.y][gridPoint.x];
            PointF pointF = new PointF();
            if (view instanceof VertexView) {
                if (view == initialState &&
                        viewsOnGrid[gridPoint.y][gridPoint.x + 1] == initialState) {
                }
                runnableOnOneClick = getThreadVertexOneClick(((VertexView) view),
                        getMotionEventForVertexView(e));
            } else if (edgeOnGridPoint != null) {
                for (EdgeView edgeView : edgeOnGridPoint) {
                    pointF.x = e.getX() - (getVertexSquareDimension() * edgeView.getGridBeginWidth());
                    pointF.y = e.getY() - (getVertexSquareDimension() * edgeView.getGridBeginHeight());
                    edgeView.setOnDown(pointF);
                    if (edgeView.isOnInteractArea(pointF)) {
                        runnableOnOneClick = getThreadEdgeOneClick(edgeView, getMotionEventForEdgeView(e,
                                edgeView.getGridBeginHeight(), edgeView.getGridBeginWidth()));
                    }
                }
            } else if (errorStateLabel != null) {
                runnableOnOneClick = getThreadVertexErrorOneClick(gridPoint);
                return true;
            } else if (isOnMove()) {
                runnableOnOneClick = getThreadMoveVertexOneClick(gridPoint);
            } else {
                runnableOnOneClick = getThreadAddVertexOneClick(gridPoint);
            }

            return true;
        }*/


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Point gridPoint = gridPoint(e.getX(), e.getY());
            View view = viewsOnGrid[gridPoint.y][gridPoint.x];
            if (isOnSelectErrorState()) {
                if (view instanceof VertexView) {
                    Toast.makeText(getContext(), R.string.exception_lock_state,
                            Toast.LENGTH_LONG)
                            .show();
                    return true;
                }
                VertexView errorState = addVertexView(gridPoint);
                errorState.setLabel(errorStateLabel);
                Map<String, VertexView> vertexViewByLabel = getVertexViewByLabel();
                vertexViewByLabel.put(errorStateLabel, errorState);
                Map<Pair<VertexView, VertexView>, String> transitions = new HashMap<>();
                for (FSATransitionFunction FSATransitionFunction : FSATransitionFunctionsToCompAuto) {
                    Pair<VertexView, VertexView> pairVertex =
                            Pair.create(
                                    vertexViewByLabel.get(FSATransitionFunction.getCurrentState().getName()),
                                    vertexViewByLabel.get(FSATransitionFunction.getFutureState().getName())
                            );
                    String label = transitions.get(pairVertex);
                    if (label == null) {
                        label = "";
                    }
                    transitions.put(pairVertex, label + ","
                            + FSATransitionFunction.getSymbol());
                }
                for (Pair<VertexView, VertexView> pairVertex : transitions.keySet()) {
                    addEdgeView(pairVertex.first, pairVertex.second,
                            transitions.get(pairVertex));
                }
                FSATransitionFunctionsToCompAuto = null;
                errorStateLabel = null;
                return true;
            }
            if (isOnMove()) {
                if (view instanceof VertexView) {
                    Toast.makeText(getContext(), R.string.exception_lock_state,
                            Toast.LENGTH_LONG)
                            .show();
                    return true;
                }
                moveVertexView(gridPoint);
                return true;
            }
            if (view instanceof VertexView) {
                if (view == initialState &&
                        viewsOnGrid[gridPoint.y][gridPoint.x + 1] == initialState) {
                    return true;
                }
                return ((VertexView) view).onDownAction(getMotionEventForVertexView(e));
            }
            EdgeView interactEdge = interactEdgeView(e.getX(), e.getY());
            if (interactEdge != null) {
                return interactEdge.onDownAction(getMotionEventForEdgeView(e,
                        interactEdge.getGridBeginHeight(), interactEdge.getGridBeginWidth()));
            }
            addVertexView(gridPoint);
            return true;
        }

//        @Override
//        public boolean onContextClick(MotionEvent e) {
//            Point gridPoint = gridPoint(e.getX(), e.getY());
//            View view = viewsOnGrid[gridPoint.y][gridPoint.x];
//            if (view instanceof  VertexView) {
//                return ((VertexView) view).onDownAction(getMotionEventForVertexView(e));
//            }
//            Set<EdgeView> edgeOnGridPoint = edgesOnGrid[gridPoint.y][gridPoint.x];
//            PointF pointF = new PointF();
//            if (edgeOnGridPoint != null) {
//                for (EdgeView edgeView : edgeOnGridPoint) {
//                    pointF.x = e.getX() - (VertexView.squareDimension() * edgeView.getGridBeginWidth());
//                    pointF.y = e.getY() - (VertexView.squareDimension() * edgeView.getGridBeginHeight());
//                    if (edgeView.isOnInteractArea(pointF)) {
//                        return edgeView.onContextClickAction(getMotionEventForEdgeView(e,
//                                edgeView.getGridBeginHeight(), edgeView.getGridBeginWidth()));
//                    }
//                }
//            }
//            return super.onContextClick(e);
//    }

        @Override
        public boolean onDown(MotionEvent e) {
            /*
            if (!threadControl.isAlive()) {
                onOneClick(e);
                threadControl = getThreadControl(runnableOnOneClick);
                threadControl.start();
            }*/
            return true;
        }

        public EdgeView interactEdgeView(float x, float y) {
            PointF point = new PointF(x, y);
            Point gridPoint = gridPoint(point.x, point.y);
            Set<EdgeView> edgeOnGridPoint = edgesOnGrid[gridPoint.y][gridPoint.x];
            EdgeView nearestEdge = null;
            float nearestDistance = Float.MAX_VALUE;
            if (edgeOnGridPoint != null) {
                for (EdgeView edgeView : edgeOnGridPoint) {
                    if (edgeView.isOnInteractLabelArea(point)) {
                        return edgeView;
                    }
                    float distance = edgeView.distanceToObject(point);
                    if (distance < nearestDistance) {
                        nearestEdge = edgeView;
                        nearestDistance = distance;
                    }
                }
            }
            if (nearestDistance <= MAX_DISTANCE_FROM_EDGE) {
                return nearestEdge;
            }
            return null;
        }

        public void onLongPressAction(MotionEvent e) {
//            if (threadControl.isAlive()) {
//                threadControl.interrupt();
//            }
            Point gridPoint = gridPoint(e.getX(), e.getY());
            View view = viewsOnGrid[gridPoint.y][gridPoint.x];
            if (view instanceof VertexView) {
                if (!(view == initialState &&
                        viewsOnGrid[gridPoint.y][gridPoint.x + 1] == initialState)) {
                    ((VertexView) view).onLongPressAction(getMotionEventForVertexView(e));
                }
            } else {
                EdgeView interactEdge = interactEdgeView(e.getX(), e.getY());
                if (interactEdge != null) {
                    interactEdge.onLongPressAction(getMotionEventForEdgeView(e,
                            interactEdge.getGridBeginHeight(), interactEdge.getGridBeginWidth()));
                }
            }
//            if (threadControl.isAlive()) {
//                threadControl.interrupt();
//            }
        }

        @Override
        public void onLongPress(MotionEvent e) {
            onLongPressAction(e);
//            synchronized (EditGraphLayout.this) {
//                onLongPressAction(e);
//            }
        }

        public boolean onDoubleTapAction(MotionEvent e) {
            Point gridPoint = gridPoint(e.getX(), e.getY());
            View view = viewsOnGrid[gridPoint.y][gridPoint.x];
            if (view instanceof VertexView) {
                if (!(view == initialState &&
                        viewsOnGrid[gridPoint.y][gridPoint.x + 1] == initialState)) {
                    return ((VertexView) view).onDoubleTapAction(getMotionEventForVertexView(e));
                }
            }
            EdgeView interactEdge = interactEdgeView(e.getX(), e.getY());
            if (interactEdge != null) {
                interactEdge.onDoubleTapAction(getMotionEventForEdgeView(e,
                        interactEdge.getGridBeginHeight(), interactEdge.getGridBeginWidth()));
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return onDoubleTapAction(e);
//            synchronized (EditGraphLayout.this) {
//                return onDoubleTapAction(e);
//            }

        }
    }

}
