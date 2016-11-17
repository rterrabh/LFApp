package com.ufla.lfapp.activities.graph.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import com.ufla.lfapp.activities.graph.views.SpaceWithBorder;
import com.ufla.lfapp.activities.graph.views.EdgeView;
import com.ufla.lfapp.activities.graph.views.VertexView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by carlos on 06/10/16.
 * Representa um <i>layout</i> para a criação e edição de autômatos ou máquinas de estados.
 * Permitindo criar, mover e editar estados em um <i>grid</i> e conectá-los com transições.
 */

public class EditGraphLayout extends GridLayout {

    private static final int INITIAL_NUM_COLUMNS = 20;
    private static final int INITIAL_NUM_ROWS = 15;
    private static final int DIST_FREE_SPACE_MIN = 3;
    private int actualNumColumns;
    private int actualNumRows;
    private int distColumnsFree;
    private int distRowsFree;
    private VertexView initialState;
    private View viewsOnGrid[][];
    private EdgeView edgesOnGrid[][];
    private List<EdgeView> edgeViews;
    private Map<VertexView, List<EdgeView>> edgeDependecies;
    private GestureDetector gestureDetector;
    private boolean onSelectState;
    private int rowActual;
    private int colActual;
    private VertexView stateSelect;
    private Context context;
    private MyOnDragListener myOnDragListener = new MyOnDragListener();


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
        this.context = context;
        init();
    }

    public EditGraphLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public EditGraphLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    /**
     * Inicializa o AutomataViewB criando os scrolls horizontal e vertical, adicionando-os como
     * pais do AutomaViewB. Também realiza o preenchimento do <i>grid</i> com espaços em branco.
     */
    public void init() {
        edgeDependecies = new HashMap<>();
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
        distColumnsFree = actualNumColumns;
        distRowsFree = actualNumRows;
        viewsOnGrid = new View[actualNumRows][actualNumColumns];
        edgesOnGrid = new EdgeView[actualNumRows][actualNumColumns];

        hScrollView.addView(this);
        scrollView.addView(hScrollView);
        fillSpace();
        fillEdges();
        this.gestureDetector = new GestureDetector(getContext(), new GestureListener());
        onSelectState = false;
        stateSelect = null;
        edgeViews = new ArrayList<>();
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
        int oldNumColumns = actualNumColumns;
        actualNumColumns += actualNumColumns << 1;
        viewsOnGrid = new View[actualNumRows][actualNumColumns];
        setColumnCount(actualNumColumns);
        for (int i = 0; i < viewsOnGridOld.length; i++) {

        }
    }

    /**
     * Realiza o crescimento do <i>grid</i> em termos de linhas. Aumenta a quantidade de linhas
     * em 1.5 vezes do tamanho atual.
     */
    private void growRows() {
        actualNumRows += actualNumRows << 1;
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
        int distRowsFreeFromState = Math.max(gridPoint.y, actualNumRows - gridPoint.y);
        if (distRowsFreeFromState < distRowsFree) {
            distRowsFree = distRowsFreeFromState;
        }
        int distColumnsFreeFromState = Math.max(gridPoint.x, actualNumColumns - gridPoint.x);
        if (distColumnsFreeFromState < distColumnsFree) {
            distColumnsFree = distColumnsFreeFromState;
        }
        if (distColumnsFreeFromState < DIST_FREE_SPACE_MIN) {
            growColumns();
        }
        if (distRowsFreeFromState < DIST_FREE_SPACE_MIN) {
            growRows();
        }
        edgeDependecies.put(vertexView, new ArrayList<EdgeView>());

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
        for (EdgeView edgeView : edgeDependecies.get(viewsOnGrid[gridPoint.y][gridPoint.x])) {
            nullableEdgesOnView(edgeView);
            nullableEdgesOnView(edgeView);
            removeView(edgeView);
        }
        edgeDependecies.remove(viewsOnGrid[gridPoint.y][gridPoint.x]);
        viewsOnGrid[gridPoint.y][gridPoint.x] = space;
        addView(space, new LayoutParams(GridLayout.spec(gridPoint.y),
                GridLayout.spec(gridPoint.x)));

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
        edgeView.setVertices(Pair.create(sourceVertex, targetVertex));
        for (EdgeView edgeView1 : edgeViews) {
            if (edgeView.equals(edgeView1)) {
                Log.d("EdgeView", "EdgeView repetida");
                return;
            }
        }
        final GridLayout.Spec rowSpec;
        final GridLayout.Spec columnSpec;
        if (sourceVertex.equals(targetVertex)) {
            rowSpec = GridLayout.spec(gridPointSourceVertex.y - 1, 3);
            columnSpec = GridLayout.spec(gridPointSourceVertex.x);
        } else {
            rowSpec = GridLayout.spec(Math.min(gridPointSourceVertex.y,
                    gridPointTargetVertex.y), Math.max(gridPointSourceVertex.y,
                    gridPointTargetVertex.y) + 1);
            columnSpec = GridLayout.spec(Math.min(gridPointSourceVertex.x, gridPointTargetVertex.x),
                    Math.max(gridPointSourceVertex.x, gridPointTargetVertex.x) + 1);
        }
        edgeDependecies.get(sourceVertex).add(edgeView);
        edgeDependecies.get(targetVertex).add(edgeView);
        edgeViews.add(edgeView);
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
                edgesOnGrid[j][i] = edgeView;
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
                edgesOnGrid[j][i] = null;
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

    public void drawArrow(Canvas canvas) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        float x = ev.getX();
        float y = ev.getY();
        int xGrid = (int) (x / VertexView.squareDimension());
        int yGrid = (int) (y / VertexView.squareDimension());
        View view = viewsOnGrid[yGrid][xGrid];
        if (view instanceof  VertexView) {
            return false;
        }
        EdgeView edgeView = edgesOnGrid[yGrid][xGrid];
        if (edgeView != null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
//        if (onSelectState && e.getAction() == MotionEvent.ACTION_UP) {
//            float x = e.getX();
//            float y = e.getY();
//            Point gridPoint = new Point();
//            gridPoint.x = (int) (x / VertexView.squareDimension());
//            gridPoint.y = (int) (y / VertexView.squareDimension());
//            View view = viewsOnGrid[gridPoint.y][gridPoint.x];
//            if (view instanceof VertexView) {
//                addEdgeView(stateSelect, (VertexView) view);
//                onSelectState = false;
//                return true;
//            }
//        }
        return gestureDetector.onTouchEvent(e);
    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
//            float x = e.getX();
//            float y = e.getY();
//            Point gridPoint = new Point();
//            gridPoint.x = (int) (x / VertexView.squareDimension());
//            gridPoint.y = (int) (y / VertexView.squareDimension());
//            View view = viewsOnGrid[gridPoint.y][gridPoint.x];
//            if (view instanceof VertexView) {
//            }
            Log.d("Layout - onDown", "Layout - onDown");
            return true;
        }



        @Override
        public boolean onContextClick(MotionEvent e) {
            Log.d("Layout - onContextClick", "Layout - onContextClick");
            return super.onContextClick(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();
            Point gridPoint = new Point();
            gridPoint.x = (int) (x / VertexView.squareDimension());
            gridPoint.y = (int) (y / VertexView.squareDimension());
            View view = viewsOnGrid[gridPoint.y][gridPoint.x];
            if (view instanceof VertexView) {
                if (onSelectState) {
                    addEdgeView(stateSelect, (VertexView) view);
                    stateSelect.onSelect();
                    onSelectState = false;
                } else {
                    onSelectState = true;
                    stateSelect = (VertexView) view;
                    stateSelect.onSelect();
                }
            }
            Log.d("Layout - onLongPress", "Layout - onLongPress");
        }

        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();
            Point gridPoint = new Point();
            gridPoint.x = (int) (x / VertexView.squareDimension());
            gridPoint.y = (int) (y / VertexView.squareDimension());
            View view = viewsOnGrid[gridPoint.y][gridPoint.x];
            if (view instanceof VertexView) {
                removeVertexView(gridPoint);
            } else {
                addVertexView(gridPoint);
            }


            Log.d("Layout - Double Tap", "Layout - Tapped at: (" + x + "," + y + ")");

            return true;
        }
    }
}
