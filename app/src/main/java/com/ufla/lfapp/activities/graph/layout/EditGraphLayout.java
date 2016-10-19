package com.ufla.lfapp.activities.graph.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.graph.views.SpaceWithBorder;
import com.ufla.lfapp.activities.graph.views.EdgeView;
import com.ufla.lfapp.activities.graph.views.VertexView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlos on 06/10/16.
 * Representa um <i>layout</i> para a criação e edição de autômatos ou máquinas de estados.
 * Permitindo criar, mover e editar estados em um <i>grid</i> e conectá-los com transições.
 */

public class EditGraphLayout extends GridLayout {

    private static final int INITIAL_NUM_COLUMNS = 100;
    private static final int INITIAL_NUM_ROWS = 100;
    private static final int DIST_FREE_SPACE_MIN = 5;
    private int actualNumColumns;
    private int actualNumRows;
    private int distColumnsFree;
    private int distRowsFree;
    private View viewsOnGrid[][];
    private List<EdgeView> edgeViews;
    private GestureDetector gestureDetector;
    private boolean onSelectState;
    private int rowActual;
    private int colActual;
    private VertexView stateSelect;

    public EditGraphLayout(Context context) {
        super(context);
        context.getResources().getDrawable(R.drawable.border_transparent);
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
        distColumnsFree = actualNumColumns;
        distRowsFree = actualNumRows;
        viewsOnGrid = new View[actualNumRows][actualNumColumns];

        hScrollView.addView(this);
        scrollView.addView(hScrollView);
        fillSpace();
        this.gestureDetector = new GestureDetector(getContext(), new GestureListener());
        onSelectState = false;
        stateSelect = null;
        edgeViews = new ArrayList<>();
    }

    /**
     * Realiza o crescimento do <i>grid</i> em termos de colunas. Aumenta a quantidade de colunas
     * em 1.5 vezes do tamanho atual.
     */
    private void growColumns() {

    }

    /**
     * Realiza o crescimento do <i>grid</i> em termos de linhas. Aumenta a quantidade de linhas
     * em 1.5 vezes do tamanho atual.
     */
    private void growRows() {

    }

    /**
     * Adiciona um estado nas coordenadas informadas por parâmetros.
     *
     * @param gridPoint linha do estado a ser adicionado.
     */
    public void addVertexView(Point gridPoint) {
        VertexView vertexView = new VertexView(getContext());
        vertexView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        vertexView.setGridPoint(gridPoint);
        removeView(viewsOnGrid[gridPoint.y][gridPoint.x]);
        viewsOnGrid[gridPoint.y][gridPoint.x] = vertexView;
        addView(vertexView, new GridLayout.LayoutParams(GridLayout.spec(gridPoint.y),
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
        invalidate();
    }

    public void removeVertexView(Point gridPoint) {
        removeView(viewsOnGrid[gridPoint.y][gridPoint.x]);
        SpaceWithBorder space = new SpaceWithBorder(getContext());
        space.setMinimumHeight(VertexView.squareDimension());
        space.setMinimumWidth(VertexView.squareDimension());
        viewsOnGrid[gridPoint.y][gridPoint.x] = space;
        addView(space, new LayoutParams(GridLayout.spec(gridPoint.y),
                GridLayout.spec(gridPoint.x)));
        invalidate();
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
    public void addTransitionView(VertexView sourceVertex, VertexView targetVertex) {
        EdgeView edgeView = new EdgeView(getContext());
        edgeView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Point gridPointSourceVertex = sourceVertex.getGridPoint();
        Point gridPointTargetVertex = targetVertex.getGridPoint();
        edgeView.setVertices(Pair.create(sourceVertex, targetVertex));
        GridLayout.Spec rowSpec;
        GridLayout.Spec columnSpec;
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
        addView(edgeView, new GridLayout.LayoutParams(rowSpec, columnSpec));
    }

    /**
     * Preenche todo o <i>grid</i> com espa&ccedil;os em branco. Utiliza a classe Space, uma <i>view</i>
     * leve que representa espa&ccedil;os em branco.
     */
    private void fillSpace() {
        for (int i = 0; i < actualNumColumns; i++) {
            for (int j = 0; j < actualNumRows; j++) {
                SpaceWithBorder space = new SpaceWithBorder(getContext());
                space.setMinimumHeight(VertexView.squareDimension());
                space.setMinimumWidth(VertexView.squareDimension());
                viewsOnGrid[i][j] = space;
                addView(space, new LayoutParams(GridLayout.spec(i),
                        GridLayout.spec(j)));
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
    public boolean onTouchEvent(MotionEvent e) {
        return gestureDetector.onTouchEvent(e);
    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            if (onSelectState) {
                float x = e.getX();
                float y = e.getY();
                colActual = (int) (x / VertexView.squareDimension());
                rowActual = (int) (y / VertexView.squareDimension());
                //invalidate();
            }
            return true;
        }

        @Override
        public boolean onContextClick(MotionEvent e) {
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
                    addTransitionView(stateSelect, (VertexView) view);
                    stateSelect.onSelect();
                    onSelectState = false;
                } else {
                    onSelectState = true;
                    stateSelect = (VertexView) view;
                    stateSelect.onSelect();
                }
            }
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


            Log.d("Double Tap", "Tapped at: (" + x + "," + y + ")");

            return true;
        }
    }
}
