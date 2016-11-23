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

import java.util.HashSet;
import java.util.Set;

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
    private GestureDetector gestureDetector;
    private boolean onSelectState;
    private VertexView stateSelect;
    private MyOnDragListener myOnDragListener = new MyOnDragListener();
    private boolean creationEdge;
    private Pair<PointF, PointF> createEdge;
    private VertexView createEdgeFirstVertex;
    private Paint mTransitionLine;
    private boolean defineInitialState;

    public int getMode() {
        return mode;
    }

    public boolean onDefineInitialState() {
        return defineInitialState;
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

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setInitialState(VertexView vertexView) {
        if (initialState != null) {
            removeInitialState();
        }
        initialState = vertexView;
        Point gridPoint = initialState.getGridPoint();
        if (gridPoint.x - 1 < 0 ||
                viewsOnGrid[gridPoint.y][gridPoint.x - 1] instanceof VertexView) {
            Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT)
                    .show();
        } else {
            viewsOnGrid[gridPoint.y][gridPoint.x - 1] = vertexView;
            removeView(initialState);
            initialState.setInitialState(true);
            addView(initialState, new LayoutParams(GridLayout.spec(gridPoint.y),
                    GridLayout.spec(gridPoint.x - 1, 2)));
        }
        defineInitialState = false;
        if (mode == CREATION_MODE) {
            Toast.makeText(getContext(), "Em modo de criação!", Toast.LENGTH_SHORT)
                    .show();
        } else if (mode == EDITION_MODE) {
            Toast.makeText(getContext(), "Em modo de edição!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void removeInitialState() {
        removeView(initialState);
        initialState.setInitialState(false);
        Point gridPoint = initialState.getGridPoint();
        addView(initialState, new LayoutParams(GridLayout.spec(gridPoint.y),
                GridLayout.spec(gridPoint.x)));
        SpaceWithBorder space = new SpaceWithBorder(getContext());
        space.setMinimumHeight(VertexView.squareDimension());
        space.setMinimumWidth(VertexView.squareDimension());
        viewsOnGrid[gridPoint.y][gridPoint.x - 1] = space;
        addView(space, new LayoutParams(GridLayout.spec(gridPoint.y),
                GridLayout.spec(gridPoint.x - 1)));
        initialState = null;
    }


    /**
     * Inicializa o AutomataViewB criando os scrolls horizontal e vertical, adicionando-os como
     * pais do AutomaViewB. Também realiza o preenchimento do <i>grid</i> com espaços em branco.
     */
    public void init() {
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
        this.mTransitionLine = new Paint();
        this.mTransitionLine.setAntiAlias(true);
        this.mTransitionLine.setStyle(Paint.Style.STROKE);
        this.mTransitionLine.setColor(Color.BLACK);
        float dpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        this.mTransitionLine.setStrokeWidth(dpi / 160.0f);
        createEdge = new Pair<>(new PointF(), new PointF());
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
                SpaceWithBorder space = new SpaceWithBorder(getContext());
                space.setMinimumHeight(VertexView.squareDimension());
                space.setMinimumWidth(VertexView.squareDimension());

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
                SpaceWithBorder space = new SpaceWithBorder(getContext());
                space.setMinimumHeight(VertexView.squareDimension());
                space.setMinimumWidth(VertexView.squareDimension());

                viewsOnGrid[i][j] = space;
                addView(space, new LayoutParams(GridLayout.spec(i), GridLayout.spec(j)));
            }
        }
        invalidate();
    }

    private VertexView viewOnDrag;
    private String msg;

    /**
     * Adiciona um estado nas coordenadas informadas por parâmetros.
     *
     * @param gridPoint linha do estado a ser adicionado.
     */
    public void addVertexView(final Point gridPoint) {
        final VertexView vertexView = new VertexView(getContext());
        vertexView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        vertexView.setGridPoint(gridPoint);
        removeView(viewsOnGrid[gridPoint.y][gridPoint.x]);
        viewsOnGrid[gridPoint.y][gridPoint.x] = vertexView;
        addView(vertexView, new LayoutParams(GridLayout.spec(gridPoint.y),
                GridLayout.spec(gridPoint.x)));
        if (actualNumRows - gridPoint.y < DIST_FREE_SPACE_MIN) {
            growRows();
        }
        if (actualNumColumns - gridPoint.x < DIST_FREE_SPACE_MIN) {
            growColumns();
        }

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
                    gridPoint.x = (int) (x_cord / VertexView.squareDimension());
                    gridPoint.y = (int) (y_cord / VertexView.squareDimension());
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
                    gridPointA.x = (int) (x_cord / VertexView.squareDimension());
                    gridPointA.y = (int) (y_cord / VertexView.squareDimension());
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
        removeView(viewsOnGrid[gridPoint.y][gridPoint.x]);
        SpaceWithBorder space = new SpaceWithBorder(getContext());
        space.setMinimumHeight(VertexView.squareDimension());
        space.setMinimumWidth(VertexView.squareDimension());
        for (EdgeView edgeView : ((VertexView) viewsOnGrid[gridPoint.y][gridPoint.x])
                .getEdgeDependencies()) {
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

    /**
     * Adiciona uma transição entre dois estados. Por parâmetro é recebido as coordenadas dos
     * estado atual e futuro, respectivamente, também recebe o símbolo da transição.
     */
    public void addEdgeView(VertexView sourceVertex, VertexView targetVertex) {
        final EdgeView edgeView = new EdgeView(getContext());
        edgeView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Point gridPointSourceVertex = sourceVertex.getGridPoint();
        Point gridPointTargetVertex = targetVertex.getGridPoint();
        final GridLayout.Spec rowSpec;
        final GridLayout.Spec columnSpec;
        if (sourceVertex.equals(targetVertex)) {
            if (gridPointSourceVertex.y - 1 >= 0) {
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
                Log.d("EdgeView", "EdgeView repetida");
                Toast.makeText(getContext(), "Transição já existe!", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            if (edgeView.isInvertedEdge(edgeView1)) {
                invertedEdge = edgeView1;
            }
        }
        if (invertedEdge != null) {
            edgeView.setInvertedEdge(invertedEdge);
            invertedEdge.setInvertedEdge(edgeView);
        }
        sourceVertex.addEdgeDependencies(edgeView);
        targetVertex.addEdgeDependencies(edgeView);
        edgeViews.add(edgeView);
        System.out.println(gridPointSourceVertex + " " + gridPointTargetVertex);
        setEdgesOnView(edgeView, gridPointSourceVertex, gridPointTargetVertex);
        addView(edgeView, new GridLayout.LayoutParams(rowSpec, columnSpec));
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
                SpaceWithBorder space = new SpaceWithBorder(getContext());
                space.setMinimumHeight(VertexView.squareDimension());
                space.setMinimumWidth(VertexView.squareDimension());

                viewsOnGrid[j][i] = space;
                addView(space, new LayoutParams(GridLayout.spec(j), GridLayout.spec(i)));
            }
        }
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
        return (View) getParent().getParent();
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
        gridPoint.x = (int) (x / VertexView.squareDimension());
        gridPoint.y = (int) (y / VertexView.squareDimension());
        return gridPoint;
    }

    private MotionEvent getMotionEventForVertexView(MotionEvent e) {
        e.setLocation(e.getX() % VertexView.squareDimension(),
                e.getY() % VertexView.squareDimension());
        return e;
    }

    private MotionEvent getMotionEventForEdgeView(MotionEvent e, int height, int width) {
        e.setLocation(e.getX() - (VertexView.squareDimension() * width),
                e.getY() - (VertexView.squareDimension() * height));
        return e;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            Point gridPoint = gridPoint(e.getX(), e.getY());
            View view = viewsOnGrid[gridPoint.y][gridPoint.x];
            if (view instanceof  VertexView) {
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
                    pointF.x = e.getX() - (VertexView.squareDimension() * edgeView.getGridBeginWidth());
                    pointF.y = e.getY() - (VertexView.squareDimension() * edgeView.getGridBeginHeight());
                    edgeView.setOnDown(pointF);
                    if (edgeView.isOnInteractArea(pointF)) {
                        return edgeView.onDownAction(getMotionEventForEdgeView(e,
                                edgeView.getGridBeginHeight(), edgeView.getGridBeginWidth()));
                    }
                }
            }
            Log.d("Layout - onDown", "Layout - onDown");
            return true;
        }

        @Override
        public boolean onContextClick(MotionEvent e) {
            Point gridPoint = gridPoint(e.getX(), e.getY());
            View view = viewsOnGrid[gridPoint.y][gridPoint.x];
            if (view instanceof  VertexView) {
                return ((VertexView) view).onDownAction(getMotionEventForVertexView(e));
            }
            Set<EdgeView> edgeOnGridPoint = edgesOnGrid[gridPoint.y][gridPoint.x];
            PointF pointF = new PointF();
            if (edgeOnGridPoint != null) {
                for (EdgeView edgeView : edgeOnGridPoint) {
                    pointF.x = e.getX() - (VertexView.squareDimension() * edgeView.getGridBeginWidth());
                    pointF.y = e.getY() - (VertexView.squareDimension() * edgeView.getGridBeginHeight());
                    if (edgeView.isOnInteractArea(pointF)) {
                        return edgeView.onContextClickAction(getMotionEventForEdgeView(e,
                                edgeView.getGridBeginHeight(), edgeView.getGridBeginWidth()));
                    }
                }
            }
            Log.d("Layout - onContextClick", "Layout - onContextClick");
            return super.onContextClick(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Point gridPoint = gridPoint(e.getX(), e.getY());
            View view = viewsOnGrid[gridPoint.y][gridPoint.x];
            if (view instanceof  VertexView) {
                if (view == initialState &&
                        viewsOnGrid[gridPoint.y][gridPoint.x + 1] == initialState) {
                    return true;
                }
                ((VertexView) view).onLongPressAction(getMotionEventForVertexView(e));
                return;
            }
            Set<EdgeView> edgeOnGridPoint = edgesOnGrid[gridPoint.y][gridPoint.x];
            PointF pointF = new PointF();
            if (edgeOnGridPoint != null) {
                for (EdgeView edgeView : edgeOnGridPoint) {
                    pointF.x = e.getX() - (VertexView.squareDimension() * edgeView.getGridBeginWidth());
                    pointF.y = e.getY() - (VertexView.squareDimension() * edgeView.getGridBeginHeight());
                    if (edgeView.isOnInteractArea(pointF)) {
                        edgeView.onLongPressAction(getMotionEventForEdgeView(e,
                                edgeView.getGridBeginHeight(), edgeView.getGridBeginWidth()));
                        return;
                    }
                }
            }
            Log.d("Layout - onLongPress", "Layout - onLongPress");
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Point gridPoint = gridPoint(e.getX(), e.getY());
            View view = viewsOnGrid[gridPoint.y][gridPoint.x];
            if (view instanceof  VertexView) {
                if (view == initialState &&
                        viewsOnGrid[gridPoint.y][gridPoint.x + 1] == initialState) {
                    return true;
                }
                return ((VertexView) view).onDoubleTapAction(getMotionEventForVertexView(e));
            }
            Set<EdgeView> edgeOnGridPoint = edgesOnGrid[gridPoint.y][gridPoint.x];
            PointF pointF = new PointF();
            if (edgeOnGridPoint != null) {
                for (EdgeView edgeView : edgeOnGridPoint) {
                    pointF.x = e.getX() - (VertexView.squareDimension() * edgeView.getGridBeginWidth());
                    pointF.y = e.getY() - (VertexView.squareDimension() * edgeView.getGridBeginHeight());
                    if (edgeView.isOnInteractArea(pointF)) {
                        return edgeView.onDoubleTapAction(getMotionEventForEdgeView(e,
                                edgeView.getGridBeginHeight(), edgeView.getGridBeginWidth()));
                    }
                }
            }
            if (mode == CREATION_MODE) {
                addVertexView(gridPoint);
            }

            Log.d("Layout - Double Tap", "Layout - Tapped at: (" + e.getX() + "," + e.getY() + ")");

            return true;
        }
    }

}
