package com.ufla.lfapp.activities.graph.layout;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.ufla.lfapp.activities.graph.views.EdgeView;
import com.ufla.lfapp.activities.graph.views.SpaceWithBorder;
import com.ufla.lfapp.activities.graph.views.VertexView;
import com.ufla.lfapp.vo.machine.Automaton;
import com.ufla.lfapp.vo.machine.AutomatonGUI;
import com.ufla.lfapp.vo.machine.TransitionFunction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by carlos on 06/10/16.
 * Representa um <i>layout</i> para a criação e edição de autômatos ou máquinas de estados.
 * Permitindo criar, mover e editar estados em um <i>grid</i> e conectá-los com transições.
 */

public class EditGraphLayout extends GridLayout {

    public static final int CREATION_MODE = 0;
    public static final int EDITION_MODE = 1;
    private int mode = 0;
    private static final int INITIAL_NUM_COLUMNS = 20;
    private static final int INITIAL_NUM_ROWS = 15;
    private static final int DIST_FREE_SPACE_MIN = 3;
    private int actualNumColumns;
    private int actualNumRows;
    private VertexView initialState;
    private View viewsOnGrid[][];
    private Set<EdgeView> edgesOnGrid[][];
    private Set<EdgeView> edgeViews;
    private Set<EdgeView> edgeReflexiveViews;
    private GestureDetector gestureDetector;
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
    private Set<TransitionFunction> transitionFunctionsToCompAuto;
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
    private float edgeSpaceFromTextToEdge;
    private float edgeArrowHeadLenght;
    // Atributos relacionados aos estilos
    private Paint mVertexBorderPaint;
    private float vertexBorderStrokeWidth;

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

    public float getEdgeSpaceFromTextToEdge() {
        return edgeSpaceFromTextToEdge;
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
        mVertexBorderPaint.setColor(Color.parseColor("#60000000"));
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
                if (viewsOnGrid[i][j] instanceof  VertexView) {
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
    }

    private void setSizeOfEdgeViews() {
        edgeTextStrokeWidth = (dpi / 160.0f) * sizeReference;
        edgeLineStrokeWidth = (dpi / 160.0f) * sizeReference;
        edgeTextSize = (dpi / 9.6f) * sizeReference;
        edgeSpaceFromTextToEdge = (dpi / 24.0f) * sizeReference;
        edgeArrowHeadLenght = (dpi / 19.2f) * sizeReference;
    }

    public void setSizeReferenceForChildViews(float sizeReference) {
        if (sizeReference < 0) {
            throw new IllegalArgumentException("O tamanho de referência deve ser positivo");
        }
        this.sizeReference = sizeReference;
        setSizeAndStyleOfViews();
    }

    public void selectErrorState(String errorStateLabel,
                                 Set<TransitionFunction> transitionFunctionsToCompAuto) {
        this.errorStateLabel = errorStateLabel;
        this.transitionFunctionsToCompAuto = transitionFunctionsToCompAuto;
    }

    public void removeSpaces() {
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
        for (int i = 0; i < viewsOnGrid.length; i++) {
            for (int j = 0; j < viewsOnGrid[i].length; j++) {
                if (j == minPoint.x && i >= minPoint.y && i <= maxPoint.y) {
                    j = maxPoint.x;
                } else {
                    removeView(viewsOnGrid[i][j]);
                }
            }
        }
        maxPoint.x = maxPoint.x + 1;
        maxPoint.y = maxPoint.y + 1;
        actualNumColumns = maxPoint.x - minPoint.x;
        actualNumRows = maxPoint.y - minPoint.y;
        View newViewsOnGrid[][] = new View[actualNumRows][actualNumColumns];
        for (int i = minPoint.y; i <maxPoint.y; i++) {
            for (int j = minPoint.x; j < maxPoint.x; j++) {
                newViewsOnGrid[i-minPoint.y][j-minPoint.x] = viewsOnGrid[i][j];
            }
        }
        viewsOnGrid = newViewsOnGrid;
        Set<EdgeView> newEdgesOnGrid[][] = new HashSet[actualNumRows][actualNumColumns];
        for (int i = minPoint.y; i <maxPoint.y; i++) {
            for (int j = minPoint.x; j < maxPoint.x; j++) {
                newEdgesOnGrid[i-minPoint.y][j-minPoint.x] = edgesOnGrid[i][j];
            }
        }
        edgesOnGrid = newEdgesOnGrid;
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

    public void drawAutomaton(AutomatonGUI automatonGUI) {
        clear();
        for (Map.Entry<String, Point> entry : automatonGUI.getStateGridPositions().entrySet()) {
            addVertexView(entry.getValue(), entry.getKey());
        }
        String initialState = automatonGUI.getInitialState();
        if (initialState != null) {
            setVertexViewAsInitial(automatonGUI.getGridPosition(initialState));
        }
        for (String state : automatonGUI.getFinalStates()) {
            setVertexViewAsFinal(automatonGUI.getGridPosition(state));
        }
        for (Map.Entry<Pair<String, String>, SortedSet<String>> entry :
                automatonGUI.getTransitionsAFD().entrySet()) {
            StringBuilder sb = new StringBuilder();
            for (String str : entry.getValue()) {
                sb.append(str)
                        .append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
            Log.d("AUTOMATON10", entry.getKey().first + "," + entry.getKey().second);
            addEdgeView(automatonGUI.getGridPosition(entry.getKey().first),
                    automatonGUI.getGridPosition(entry.getKey().second), sb.toString());
        }
    }

    public void clear() {
        for (int i = 0; i < viewsOnGrid.length; i++) {
            for (int j = 0; j < viewsOnGrid[i].length; j++) {
                if (viewsOnGrid[i][j] instanceof VertexView &&
                        !viewsOnGrid[i][j+1].equals(initialState)) {
                    VertexView vertexView = (VertexView) viewsOnGrid[i][j];
                    removeVertexView(vertexView.getGridPoint());
                }
            }
        }
        VertexView.clearContVertex();
        invalidate();
    }

    public AutomatonGUI getCompleteAutomatonGUI() {
        if (initialState == null) {
            Toast.makeText(getContext(), "Erro! Não possui estado inicial!",
                    Toast.LENGTH_SHORT)
                    .show();
            return null;
        }
        AutomatonGUI automatonGUI = getAutomatonGUI();
        if (automatonGUI.getFinalStates().isEmpty()) {
            Toast.makeText(getContext(), "Erro. Não possui estado final!", Toast.LENGTH_SHORT)
                    .show();
            return null;
        }
        return automatonGUI;
    }

    public AutomatonGUI getAutomatonGUI() {
        Map<String, Point> stateGridPositions = new HashMap<>();
        String initialStateStr = (initialState == null) ? null : initialState.getLabel();
        SortedSet<String> states = new TreeSet<>();
        SortedSet<String> finalStates = new TreeSet<>();
        SortedSet<TransitionFunction> transitionFunctions = new TreeSet<>();
        for (int i = 0; i < viewsOnGrid.length; i++) {
            for (int j = 0; j < viewsOnGrid[i].length; j++) {
                if (viewsOnGrid[i][j] instanceof VertexView) {
                    VertexView vertexView = (VertexView) viewsOnGrid[i][j];
                    String label = vertexView.getLabel();
                    if (states.contains(label) && !vertexView.equals(initialState)) {
                        Toast.makeText(getContext(), "Erro. Estados com mesmo nome (" + label +
                                ")!", Toast.LENGTH_SHORT)
                                .show();
                        return null;
                    }
                    stateGridPositions.put(label, new Point(j, i));
                    states.add(label);
                    if (vertexView.isFinalState()) {
                        finalStates.add(label);
                    }
                }
            }
        }
        for (EdgeView edgeView : edgeViews) {
            transitionFunctions.addAll(edgeView.getTransitionFuctions());
        }
        return new AutomatonGUI(new Automaton(states, initialStateStr, finalStates,
                transitionFunctions), stateGridPositions);
    }

    public int getMode() {
        return mode;
    }

    public boolean onDefineInitialState() {
        return defineInitialState;
    }

    public boolean onSelectErrorState() {
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
        init();
    }
    public EditGraphLayout(Context context) {
        super(context);
        init();
    }

    public EditGraphLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditGraphLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setInitialState(VertexView vertexView) {
        Point point = vertexView.getGridPoint();
        point.x--;
        if (initialState != null) {
            removeInitialState();
        }
        initialState = vertexView;
        Point gridPoint = initialState.getGridPoint();
        if (gridPoint.x - 1 < 0 ||
                viewsOnGrid[gridPoint.y][gridPoint.x - 1] instanceof VertexView) {
            Toast.makeText(getContext(), "Erro!", Toast.LENGTH_SHORT)
                    .show();
        } else {
            viewsOnGrid[gridPoint.y][gridPoint.x - 1] = vertexView;
            removeView(initialState);
            initialState.setInitialState(true);
            addView(initialState, new LayoutParams(GridLayout.spec(gridPoint.y),
                    GridLayout.spec(gridPoint.x - 1, 2)));
            initialState.setStyle();
        }
        defineInitialState = false;
    }

    public void removeInitialState() {
        removeView(initialState);
        initialState.setInitialState(false);
        Point gridPoint = initialState.getGridPoint();
        addView(initialState, new LayoutParams(GridLayout.spec(gridPoint.y),
                GridLayout.spec(gridPoint.x)));
        initialState.setStyle();
        SpaceWithBorder space = SpaceWithBorder.getSpaceWithBorder(getContext(), mVertexBorderPaint,
                getVertexSquareDimension());
        viewsOnGrid[gridPoint.y][gridPoint.x - 1] = space;
        addView(space, new LayoutParams(GridLayout.spec(gridPoint.y),
                GridLayout.spec(gridPoint.x - 1)));
        initialState = null;
    }

    public void removeScrollViews() {
        if (getParent() != null) {
            HorizontalScrollView parentView = (HorizontalScrollView) (getParent());
            parentView.removeView(this);
        }
    }


    /**
     * Inicializa o AutomataViewB criando os scrolls horizontal e vertical, adicionando-os como
     * pais do AutomaViewB. Também realiza o preenchimento do <i>grid</i> com espaços em branco.
     */
    public void init() {
        VertexView.clearContVertex();
        setSizeOfViews();
        setmVertexBorderPaint();
        ScrollView scrollView = new ScrollView(getContext());
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        HorizontalScrollView hScrollView = new HorizontalScrollView(getContext());
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
        this.gestureDetector = new GestureDetector(getContext(), new GestureListener());
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

    /**
     * Realiza o crescimento do <i>grid</i> em termos de colunas. Aumenta a quantidade de colunas
     * em 1.5 vezes do tamanho atual.
     */
    private void growColumns() {
        View viewsOnGridOld[][] = viewsOnGrid;
        Set<EdgeView> edgesOnGridOld[][] = edgesOnGrid;
        int oldNumColumns = actualNumColumns;
        actualNumColumns += INITIAL_NUM_COLUMNS;
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
                SpaceWithBorder space = SpaceWithBorder.getSpaceWithBorder(getContext(), mVertexBorderPaint,
                        getVertexSquareDimension());

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
    private void growRows() {
        View viewsOnGridOld[][] = viewsOnGrid;
        Set<EdgeView> edgesOnGridOld[][] = edgesOnGrid;
        int oldNumRows = actualNumRows;
        actualNumRows += INITIAL_NUM_ROWS;
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
                SpaceWithBorder space = SpaceWithBorder.getSpaceWithBorder(getContext(), mVertexBorderPaint,
                        getVertexSquareDimension());

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
        while (actualNumRows - gridPoint.y < DIST_FREE_SPACE_MIN ) {
            growRows();
        }
        while (actualNumColumns - gridPoint.x < DIST_FREE_SPACE_MIN) {
            growColumns();
        }
    }

    /**
     * Adiciona um estado nas coordenadas informadas por parâmetros.
     *
     * @param gridPoint linha do estado a ser adicionado.
     */
    public VertexView addVertexView(final Point gridPoint) {
        validatePoint(gridPoint);
        final VertexView vertexView = new VertexView(getContext());
        vertexView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        vertexView.setGridPoint(gridPoint);
        removeView(viewsOnGrid[gridPoint.y][gridPoint.x]);
        viewsOnGrid[gridPoint.y][gridPoint.x] = vertexView;
        addView(vertexView, new LayoutParams(GridLayout.spec(gridPoint.y),
                GridLayout.spec(gridPoint.x)));
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

        vertexView.setOnDragListener(myOnDragListener);
        return vertexView;

    }

    public boolean isDefiniedLabel(String label) {
        return false;
    }

    class MyOnDragListener implements OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch(event.getAction())
            {
                case DragEvent.ACTION_DRAG_STARTED:
                    Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");
                    System.out.println("COMECOU");
                    // Do nothing
                    break;

                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                    int x_cord = (int) event.getX();
                    int y_cord = (int) event.getY();
                    break;

                case DragEvent.ACTION_DRAG_EXITED :
                    Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");
                    System.out.println("SAIU");
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

                case DragEvent.ACTION_DRAG_LOCATION  :
                    Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
                    x_cord = (int) event.getX();
                    y_cord = (int) event.getY();
                    break;

                case DragEvent.ACTION_DRAG_ENDED   :
                    Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");

                    // Do nothing
                    break;

                case DragEvent.ACTION_DROP:
                    Log.d(msg, "ACTION_DROP event");
                    System.out.println("DROP");
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
                default: break;
            }
            return true;
        }
    }

    public void removeVertexView(Point gridPoint) {
        VertexView vertexView = (VertexView) viewsOnGrid[gridPoint.y][gridPoint.x];
        if (vertexView.equals(onMove)) {
            onMove = null;
        }
        if (vertexView.equals(initialState)) {
            removeInitialState();
        }
        removeView(vertexView);
        SpaceWithBorder space = SpaceWithBorder.getSpaceWithBorder(getContext(), mVertexBorderPaint,
                getVertexSquareDimension());
        for (EdgeView edgeView : vertexView.getEdgeDependencies()) {
            nullableEdgesOnView(edgeView);
            removeView(edgeView);
            edgeViews.remove(edgeView);
        }
        viewsOnGrid[gridPoint.y][gridPoint.x] = space;
        addView(space, new LayoutParams(GridLayout.spec(gridPoint.y),
                GridLayout.spec(gridPoint.x)));

    }

    public void removeEdgeView(EdgeView edgeView) {
        nullableEdgesOnView(edgeView);
        removeView(edgeView);
        edgeViews.remove(edgeView);
        edgeView.removeDependenciesFromVertex();
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
        edgeView.appendLabel(label);
        return edgeView;
    }

    public EdgeView addEdgeView(VertexView sourceVertex, VertexView targetVertex) {
        return addEdgeView(sourceVertex, targetVertex, true);
    }

    /**
     * Adiciona uma transição entre dois estados. Por parâmetro é recebido as coordenadas dos
     * estado atual e futuro, respectivamente, também recebe o símbolo da transição.
     */
    public EdgeView addEdgeView(VertexView sourceVertex, VertexView targetVertex, boolean toast) {
        final EdgeView edgeView = new EdgeView(getContext(), toast);
        edgeView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Point gridPointSourceVertex = sourceVertex.getGridPoint();
        Point gridPointTargetVertex = targetVertex.getGridPoint();
        final GridLayout.Spec rowSpec;
        final GridLayout.Spec columnSpec;
        if (sourceVertex.equals(targetVertex)) {
            edgeReflexiveViews.add(edgeView);
            if (gridPointSourceVertex.y - 1 >= 0 &&
                    ( edgesOnGrid[gridPointSourceVertex.y - 1][gridPointSourceVertex.x] == null ||
                    edgesOnGrid[gridPointSourceVertex.y - 1][gridPointSourceVertex.x].isEmpty()) &&
                    viewsOnGrid[gridPointSourceVertex.y - 1][gridPointSourceVertex.x]
                            instanceof SpaceWithBorder) {
                rowSpec = GridLayout.spec(gridPointSourceVertex.y - 1, 2);
                edgeView.setReflexiveUp(true);
                if (edgesOnGrid[gridPointSourceVertex.y-1][gridPointSourceVertex.x] == null) {
                    edgesOnGrid[gridPointSourceVertex.y-1][gridPointSourceVertex.x] =
                            new HashSet<>();
                }
                edgesOnGrid[gridPointSourceVertex.y-1][gridPointSourceVertex.x].add(edgeView);
            } else {
                rowSpec = GridLayout.spec(gridPointSourceVertex.y, 2);
                edgeView.setReflexiveUp(false);
                if (edgesOnGrid[gridPointSourceVertex.y+1][gridPointSourceVertex.x] == null) {
                    edgesOnGrid[gridPointSourceVertex.y+1][gridPointSourceVertex.x] =
                            new HashSet<>();
                }
                edgesOnGrid[gridPointSourceVertex.y+1][gridPointSourceVertex.x].add(edgeView);
            }
            columnSpec = GridLayout.spec(gridPointSourceVertex.x);
            System.out.println("1");
        } else if (gridPointSourceVertex.x == gridPointTargetVertex.x) {
            rowSpec = GridLayout.spec(Math.min(gridPointSourceVertex.y,
                    gridPointTargetVertex.y), Math.abs(gridPointSourceVertex.y -
                    gridPointTargetVertex.y) + 1);
            columnSpec = GridLayout.spec(gridPointSourceVertex.x);
            System.out.println("2");
        } else if (gridPointSourceVertex.y == gridPointTargetVertex.y) {
            rowSpec = GridLayout.spec(gridPointSourceVertex.y);
            columnSpec = GridLayout.spec(Math.min(gridPointSourceVertex.x, gridPointTargetVertex.x),
                    Math.abs(gridPointSourceVertex.x - gridPointTargetVertex.x) + 1);
            System.out.println("3");
        } else {
            rowSpec = GridLayout.spec(Math.min(gridPointSourceVertex.y,
                    gridPointTargetVertex.y), Math.abs(gridPointSourceVertex.y -
                    gridPointTargetVertex.y) + 1);
            columnSpec = GridLayout.spec(Math.min(gridPointSourceVertex.x, gridPointTargetVertex.x),
                    Math.abs(gridPointSourceVertex.x - gridPointTargetVertex.x) + 1);
            System.out.println("4");
        }
        edgeView.setVertices(Pair.create(sourceVertex, targetVertex));

        EdgeView invertedEdge = null;
        for (EdgeView edgeView1 : edgeViews) {
            if (edgeView.equals(edgeView1)) {
                if (toast) {
                    Log.d("EdgeView", "EdgeView repetida");
                    Toast.makeText(getContext(), "Transição já existe!", Toast.LENGTH_SHORT)
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
        System.out.println(gridPointSourceVertex + " " + gridPointTargetVertex);
        setEdgesOnView(edgeView, gridPointSourceVertex, gridPointTargetVertex);
        addView(edgeView, new GridLayout.LayoutParams(rowSpec, columnSpec));
        edgeView.setStyle();
        edgeView.setEdgeDraw();
        if (invertedEdge != null) {
            edgeView.setInvertedEdge(invertedEdge);
            invertedEdge.setInvertedEdge(edgeView);
        }
//        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
//        builder.setView(inflater.inflate(R.layout.dialog_label_edge, null))
//                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        Dialog f = (Dialog) dialog;
//                        EditText label = (EditText) f.findViewById(R.id.labelEdge);
//                        edgeView.setLabel(label.getText().toString());
//                        addView(edgeView, new GridLayout.LayoutParams(rowSpec, columnSpec));
//                        dialog.cancel();
//
//                    }
//                })
//                .create()
//                .show();
        return edgeView;
    }

    public void setEdgesOnView(EdgeView edgeView, Point gridPointSourceVertex,
                               Point gridPointTargetVertex) {
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

        for (int j = menorY; j <= maiorY; j++) {
            for (int i = menorX; i <= maiorX; i++) {
                if (edgesOnGrid[j][i] == null) {
                    edgesOnGrid[j][i] = new HashSet<>();
                }
                edgesOnGrid[j][i].add(edgeView);
                if (!edgeReflexiveViews.contains(edgeView)) {
                    Set<EdgeView> edgeViewsAux = new HashSet<>(edgesOnGrid[j][i]);
                    edgeViewsAux.retainAll(edgeReflexiveViews);
                    for (EdgeView edgeView1 : edgeViewsAux) {
                        Pair<VertexView, VertexView> vertices = edgeView1.getVertices();
                        removeEdgeView(edgeView1);
                        addEdgeView(vertices.first, vertices.second, edgeView1.getLabel());
                    }
                }

            }
        }
    }

    public void nullableEdgesOnView(EdgeView edgeView) {
        Pair<Point, Point> gridPoints = edgeView.getGridPoints();
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

        for (int j = menorY; j <= maiorY; j++) {
            for (int i = menorX; i <= maiorX; i++) {
                edgesOnGrid[j][i].remove(edgeView);
            }
        }
        if (gridPointSourceVertex.equals(gridPointTargetVertex)) {
            if (edgeView.isReflexiveUp()) {
                edgesOnGrid[menorY-1][menorX].remove(edgeView);
            } else {
                edgesOnGrid[menorY+1][menorX].remove(edgeView);
            }
        }
    }

    /**
     * Preenche todo o <i>grid</i> com espa&ccedil;os em branco. Utiliza a classe Space, uma <i>view</i>
     * leve que representa espa&ccedil;os em branco.
     */
    private void fillSpace() {
        System.out.println(actualNumColumns + ";" + actualNumRows);
        for (int i = 0; i < actualNumColumns; i++) {
            for (int j = 0; j < actualNumRows; j++) {
                SpaceWithBorder space = SpaceWithBorder.getSpaceWithBorder(getContext(), mVertexBorderPaint,
                        getVertexSquareDimension());

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
        if (getParent() != null) {
            return (View) getParent().getParent();
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
        return gestureDetector.onTouchEvent(e);
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
        e.setLocation(e.getX() - (getVertexSquareDimension() * width),
                e.getY() - (getVertexSquareDimension() * height));
        return e;
    }

    public void moveVertexView(Point newPoint) {
        if (onMove.equals(initialState) && newPoint.x == 0) {
            Toast.makeText(getContext(), "Não foi possível mover este estado!",
                    Toast.LENGTH_LONG)
                    .show();
            onMove.onSelect();
            onMove = null;
            return;
        }
        VertexView onMoveCp = onMove;
        boolean isInitialState = onMoveCp.isInitialState();
        removeVertexView(onMoveCp.getGridPoint());
        VertexView newOnMove = addVertexView(newPoint, onMoveCp.getLabel(), isInitialState,
                onMoveCp.isFinalState());
        Iterator<EdgeView> edgeViewIterator = onMoveCp.getEdgeDependencies().iterator();
        while (edgeViewIterator.hasNext()) {
            EdgeView edgeView = edgeViewIterator.next();
            Pair<VertexView, VertexView> vertices = edgeView.getVertices();
            if (vertices.first.equals(onMoveCp)) {
                addEdgeView(newOnMove, vertices.second, edgeView.getLabel());
            } else {
                addEdgeView(vertices.first, newOnMove, edgeView.getLabel());
            }
        }
        onMove = null;
    }

    public void addErrorState(Point point) {
        VertexView errorState = addVertexView(point);
        errorState.setLabel(errorStateLabel);
        Map<String, VertexView> vertexViewByLabel = getVertexViewByLabel();
        vertexViewByLabel.put(errorStateLabel, errorState);
        for (TransitionFunction transitionFunction : transitionFunctionsToCompAuto) {
            addEdgeView(vertexViewByLabel.get(transitionFunction.getCurrentState()),
                    vertexViewByLabel.get(transitionFunction.getFutureState()),
                    transitionFunction.getSymbol());
        }
        transitionFunctionsToCompAuto = null;
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
                    Log.d("Gesture", "vertex");
                    vertexView.onDownAction(e);
                }
            };
        }

        public Runnable getThreadEdgeOneClick(final EdgeView edgeView, final MotionEvent e) {
            return new Runnable() {
                @Override
                public void run() {
                    Log.d("Gesture", "entrou_edge");
                    edgeView.onDownAction(getMotionEventForEdgeView(e,
                            edgeView.getGridBeginHeight(), edgeView.getGridBeginWidth()));
                }
            };
        }

        public Runnable getThreadVertexErrorOneClick(final Point point) {
            return new Runnable() {
                @Override
                public void run() {
                    Log.d("Gesture", "vertex_error");
                    addErrorState(point);
                }
            };
        }

        public Runnable getThreadMoveVertexOneClick(final Point point) {
            return new Runnable() {
                @Override
                public void run() {
                    Log.d("Gesture", "vertex");
                    moveVertexView(point);
                }
            };
        }

        public Runnable getThreadAddVertexOneClick(final Point point) {
            return new Runnable() {
                @Override
                public void run() {
                    Log.d("Gesture", "add_vertex");
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
                        Log.d("Gesture", "sdada");
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
            Log.d("Layout - onDown", "Layout - onDown");
            return true;
        }*/


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Point gridPoint = gridPoint(e.getX(), e.getY());
            View view = viewsOnGrid[gridPoint.y][gridPoint.x];
            if (view instanceof VertexView) {
                if (view == initialState &&
                        viewsOnGrid[gridPoint.y][gridPoint.x + 1] == initialState) {
                    return true;
                }
                return ((VertexView) view).onDownAction(getMotionEventForVertexView(e));
            }
            Set<EdgeView> edgeOnGridPoint = edgesOnGrid[gridPoint.y][gridPoint.x];
            PointF pointF = new PointF();
            if (edgeOnGridPoint != null) {
                for (EdgeView edgeView : edgeOnGridPoint) {
                    pointF.x = e.getX() - (getVertexSquareDimension() * edgeView.getGridBeginWidth());
                    pointF.y = e.getY() - (getVertexSquareDimension() * edgeView.getGridBeginHeight());
                    edgeView.setOnDown(pointF);
                    if (edgeView.isOnInteractArea(pointF)) {
                        return edgeView.onDownAction(getMotionEventForEdgeView(e,
                                edgeView.getGridBeginHeight(), edgeView.getGridBeginWidth()));
                    }
                }
            }
            if (errorStateLabel != null) {
                VertexView errorState = addVertexView(gridPoint);
                errorState.setLabel(errorStateLabel);
                Map<String, VertexView> vertexViewByLabel = getVertexViewByLabel();
                vertexViewByLabel.put(errorStateLabel, errorState);
                for (TransitionFunction transitionFunction : transitionFunctionsToCompAuto) {
                    addEdgeView(vertexViewByLabel.get(transitionFunction.getCurrentState()),
                            vertexViewByLabel.get(transitionFunction.getFutureState()),
                            transitionFunction.getSymbol());
                }
                transitionFunctionsToCompAuto = null;
                errorStateLabel = null;
                return true;
            }
            if (isOnMove()) {
                moveVertexView(gridPoint);
                return true;
            }
            addVertexView(gridPoint);
            Log.d("Layout - onDown", "Layout - onDown");
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
//            Log.d("Layout - onContextClick", "Layout - onContextClick");
//            return super.onContextClick(e);
//    }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.d("ondown", "ondown");
            /*
            if (!threadControl.isAlive()) {
                onOneClick(e);
                threadControl = getThreadControl(runnableOnOneClick);
                threadControl.start();
            }*/
            return true;
        }

        public void onLongPressAction(MotionEvent e) {
//            if (threadControl.isAlive()) {
//                threadControl.interrupt();
//            }
            Point gridPoint = gridPoint(e.getX(), e.getY());
            View view = viewsOnGrid[gridPoint.y][gridPoint.x];
            if (view instanceof  VertexView) {
                if (!(view == initialState &&
                        viewsOnGrid[gridPoint.y][gridPoint.x + 1] == initialState)) {
                    ((VertexView) view).onLongPressAction(getMotionEventForVertexView(e));
                }
            } else {
                Set<EdgeView> edgeOnGridPoint = edgesOnGrid[gridPoint.y][gridPoint.x];
                PointF pointF = new PointF();
                if (edgeOnGridPoint != null) {
                    for (EdgeView edgeView : edgeOnGridPoint) {
                        pointF.x = e.getX() - (getVertexSquareDimension() * edgeView.getGridBeginWidth());
                        pointF.y = e.getY() - (getVertexSquareDimension() * edgeView.getGridBeginHeight());
                        if (edgeView.isOnInteractArea(pointF)) {
                            edgeView.onLongPressAction(getMotionEventForEdgeView(e,
                                    edgeView.getGridBeginHeight(), edgeView.getGridBeginWidth()));
                        }
                    }
                }
            }
//            if (threadControl.isAlive()) {
//                threadControl.interrupt();
//            }
            Log.d("Layout - onLongPress", "Layout - onLongPress");
        }

        @Override
        public void onLongPress(MotionEvent e) {
            onLongPressAction(e);
//            synchronized (EditGraphLayout.this) {
//                onLongPressAction(e);
//            }
        }

        public boolean onDoubleTapAction(MotionEvent e) {
//            if (threadControl.isAlive()) {
//                threadControl.interrupt();
//            }
            boolean ret = true;
            Point gridPoint = gridPoint(e.getX(), e.getY());
            View view = viewsOnGrid[gridPoint.y][gridPoint.x];
            if (view instanceof  VertexView) {
                if (view == initialState &&
                        viewsOnGrid[gridPoint.y][gridPoint.x + 1] == initialState) {
                    ret =  true;
                } else {
                    ret = ((VertexView) view).onDoubleTapAction(getMotionEventForVertexView(e));
                }
            } else {
                Set<EdgeView> edgeOnGridPoint = edgesOnGrid[gridPoint.y][gridPoint.x];
                PointF pointF = new PointF();
                if (edgeOnGridPoint != null) {
                    for (EdgeView edgeView : edgeOnGridPoint) {
                        pointF.x = e.getX() - (getVertexSquareDimension() * edgeView.getGridBeginWidth());
                        pointF.y = e.getY() - (getVertexSquareDimension() * edgeView.getGridBeginHeight());
                        if (edgeView.isOnInteractArea(pointF)) {
//                            if (threadControl.isAlive()) {
//                                threadControl.interrupt();
//                            }
                            ret = edgeView.onDoubleTapAction(getMotionEventForEdgeView(e,
                                    edgeView.getGridBeginHeight(), edgeView.getGridBeginWidth()));
                        }
                    }
                }
            }
            /*if (errorStateLabel != null) {
                VertexView errorState = addVertexView(gridPoint);
                errorState.setLabel(errorStateLabel);
                Map<String, VertexView> vertexViewByLabel = getVertexViewByLabel();
                vertexViewByLabel.put(errorStateLabel, errorState);
                for (TransitionFunction transitionFunction : transitionFunctionsToCompAuto) {
                    addEdgeView(vertexViewByLabel.get(transitionFunction.getCurrentState()),
                            vertexViewByLabel.get(transitionFunction.getFutureState()),
                            transitionFunction.getSymbol());
                }
                transitionFunctionsToCompAuto = null;
                errorStateLabel = null;
            }
            if (mode == CREATION_MODE) {
                addVertexView(gridPoint);
            }*/
            Log.d("Layout - Double Tap", "Layout - Tapped at: (" + e.getX() + "," + e.getY() + ")");

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.sleep(50);
//                        if (threadControl.isAlive()) {
//                            threadControl.interrupt();
//                        }
//                    } catch (InterruptedException e1) {
//
//                    }
//                }
//            }).start();

            return ret;
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
