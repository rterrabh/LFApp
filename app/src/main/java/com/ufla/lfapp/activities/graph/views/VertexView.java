package com.ufla.lfapp.activities.graph.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;

import com.ufla.lfapp.activities.graph.layout.EditGraphLayout;

import java.util.ArrayList;
import java.util.List;

// Color Line #FFCCCC
// #FFCCCC

/**
 * Created by carlos on 9/21/16.
 * Representa a visão de um estado em um autômato ou máquina de estados.
 */
public class VertexView extends EditText {

    private final static float dpi;
    private static int mStateLineColor = Color.parseColor("#968D8D");
    private static int mStateInternColor = Color.parseColor("#FFCCCC");
    private static int mStateInternColorSelect = Color.parseColor("#FF6666");
    private Paint mStateInternPaint;
    private Paint mStateLinePaint;
    private Paint mStateText;
    public final static int stateRadius;
    public final static int SPACE;
    private final static int X_FOR_DISPATCH_TOUCH_EVENT_TO_EDIT_TEXT = 3;
    private final static int MAX_LENGHT_LABEL = 5;
    private static float textSize;
    private static float stateTextStrokeWidth;
    private static float stateLinePaintStrokeWidth;
    private static float errorRectLabel;
    private Point gridPoint;
    private static int cont_vertex = 0;
    private String label = "";
    private boolean select;
    private boolean showCursor;
    private boolean cursorShowed;
    private RectF rectLabel;
    private int cursorInd;
    private boolean finalState;
    private boolean initialState;
    private GestureDetector gestureDetector;
    private List<EdgeView> edgeDependencies;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
        setText(label);
        invalidate();
    }

    public boolean isInitialState() {
        return initialState;
    }

    public void setInitialState(boolean initialState) {
        this.initialState = initialState;
        invalidate();
    }

    public boolean isFinalState() {
        return finalState;
    }

    public void setFinalState(boolean finalState) {
        this.finalState = finalState;
        invalidate();
    }


    static {
        dpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        stateRadius = (int) (dpi / 5.0f);
        SPACE = (int) (dpi / 40.0f);
        textSize = dpi / 8.0f;
        stateTextStrokeWidth = dpi / 240.0f;
        stateLinePaintStrokeWidth = dpi / 96.0f;
        errorRectLabel = (dpi / 32.0f);
    }

    public VertexView(Context context) {
        super(context);
        init();
        defineDefault();
    }

    public VertexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        defineDefault();
    }

    public VertexView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        defineDefault();
    }

    public Point getGridPoint() {
        return PointUtils.clonePoint(gridPoint);
    }

    public void setGridPoint(Point gridPoint) {
        this.gridPoint = PointUtils.clonePoint(gridPoint);
    }

    /**
     * Retorna o lado do quadrado onde está desenhado o estado.
     *
     * @return lado do quadrado onde está desenhado o estado.
     */
    public static int squareDimension() {
        return (VertexView.stateRadius + VertexView.SPACE) * 2;
    }

    /**
     * Retorna o valor x que representa as coordenadas (x, x) do ponto central do estado desenhado.
     *
     * @return valor x que representa as coordenadas (x, x) do ponto central do estado desenhado.
     */
    public static int centerPoint() {
        return VertexView.stateRadius + VertexView.SPACE;
    }

    /*public StateViewB(Context context, AttributeSet attrs) {
        super(context);

        // attrs contains the raw values for the XML attributes
        // that were specified in the layout, which don't include
        // attributes set by styles or themes, and which may have
        // unresolved references. Call obtainStyledAttributes()
        // to get the final values for each attribute.
        //
        // This call uses R.styleable.StateView, which is an array of
        // the custom attributes that were declared in attrs.xml.
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StateView,
                0, 0
        );

        init();
        try {
            // Retrieve the values from the TypedArray and store into
            // fields of this class.
            //
            // The R.styleable.StateView_* constants represent the index for
            // each custom attribute in the R.styleable.StateView array.
            mStateLinePaint.setColor(a.getColor(R.styleable.StateView_lineColor, Color.BLACK));
            mStateLinePaint.setStrokeWidth(a.getFloat(R.styleable.StateView_lineWidth, 5.0f));
            mStateInternPaint.setColor(a.getColor(R.styleable.StateView_internColor, Color.TRANSPARENT));
            mStateText.setColor(a.getColor(R.styleable.StateView_nameColor, Color.BLACK));
            mStateText.setTextSize(a.getFloat(R.styleable.StateView_nameSize, 40.0f));
            stateRadius = a.getFloat(R.styleable.StateView_radius, 60.0f);
            String stateName = a.getString(R.styleable.StateView_name);
            int stateX = a.getInt(R.styleable.StateView_posX, 100);
            int stateY = a.getInt(R.styleable.StateView_posY, 100);
            if (stateName == null) {
                this.vertex = new State(stateX, stateY);
            } else {
                this.vertex = new State(stateName, stateX, stateY);
            }
        } finally {
            // release the TypedArray so that it can be reused.
            a.recycle();
        }

    }*/

    /**
     * Inicializa os objetos Paint do estado.
     */
    private void init() {
        mStateInternPaint = new Paint();
        mStateInternPaint.setStyle(Paint.Style.FILL);
        mStateLinePaint = new Paint();
        mStateLinePaint.setAntiAlias(true);
        mStateLinePaint.setStyle(Paint.Style.STROKE);
        mStateText = new Paint();
        mStateText.setAntiAlias(true);
        mStateText.setStrokeWidth(stateTextStrokeWidth);
        mStateText.setStyle(Paint.Style.FILL);
        mStateText.setTextAlign(Paint.Align.CENTER);
        select = false;
        label = "q" + cont_vertex;
        cont_vertex++;
        setInputType(InputType.TYPE_CLASS_TEXT);
        setText(label);
        setEnabled(true);
        showCursor = false;
        cursorShowed = false;
        setBackgroundColor(Color.TRANSPARENT);
        gestureDetector = new GestureDetector(getContext(), new VertexView.GestureListener());
        edgeDependencies = new ArrayList<>();
    }

    public void onSelect() {
        select = !select;
        invalidate();
    }

    /**
     * Define valores padrões para os objetos Paint do estado.
     */
    private void defineDefault() {
        mStateLinePaint.setColor(VertexView.mStateLineColor);
        mStateLinePaint.setStrokeWidth(stateLinePaintStrokeWidth);
        mStateInternPaint.setColor(VertexView.mStateInternColor);
        mStateText.setColor(Color.BLACK);
        mStateText.setTextSize(textSize);
        rectLabel = new RectF(0,
                VertexView.centerPoint() - mStateText.getTextSize(),
                VertexView.squareDimension(),
                VertexView.centerPoint() + mStateText.getTextSize());
    }

    public void addEdgeDependencies(EdgeView edgeView) {
        edgeDependencies.add(edgeView);
    }

    public void removeEdgeDependencies(EdgeView edgeView) {
        edgeDependencies.remove(edgeView);
    }

    public List<EdgeView> getEdgeDependencies() {
        return edgeDependencies;
    }

    /**
     * Desenha o estado na tela.
     *
     * @param canvas objeto para desenhar.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        cursorInd = getSelectionStart();
        if (cursorInd < 0) {
            cursorInd = 0;
        }
        setSelection(cursorInd, cursorInd);
        setSelection(cursorInd);
        // Borda
        canvas.drawRect(0, 0, VertexView.squareDimension(), VertexView.squareDimension(),
                SpaceWithBorder.mBorderPaint);
        if (initialState) {
            canvas.drawRect(VertexView.squareDimension(), 0, VertexView.squareDimension() * 2,
                    VertexView.squareDimension(), SpaceWithBorder.mBorderPaint);
        }
        if (select) {
            mStateInternPaint.setColor(mStateInternColorSelect);
        }
        // Círculo interno
        mStateText.getTextSize();
        if (initialState) {
            canvas.drawCircle(VertexView.centerPoint() + VertexView.squareDimension(),
                    VertexView.centerPoint(), VertexView.stateRadius, mStateInternPaint);
        } else {
            canvas.drawCircle(VertexView.centerPoint(), VertexView.centerPoint(),
                    VertexView.stateRadius, mStateInternPaint);
        }
        if (select) {
            mStateInternPaint.setColor(mStateInternColor);
        }
        // Texto
        String label = getText().toString();
        if (label.length() <= MAX_LENGHT_LABEL) {
            //setSelection(cursorInd, cursorInd);
            //setSelection(cursorInd);
            this.label = label;
            float textLenght = mStateText.measureText(label);
            if (initialState) {
                canvas.drawText(label, VertexView.centerPoint() + VertexView.squareDimension(),
                        VertexView.centerPoint() + mStateText.getTextSize() * 0.3f, mStateText);
            } else {
                canvas.drawText(label, VertexView.centerPoint(), VertexView.centerPoint() +
                        mStateText.getTextSize() * 0.3f, mStateText);
            }
            showCursor = isFocused();
            if (showCursor) {
                if (!cursorShowed) {
                    float cursor = mStateText.measureText(label.substring(0, getSelectionStart())) +
                            VertexView.centerPoint() - textLenght / 2.0f;
                    if (initialState) {
                        canvas.drawLine(cursor + VertexView.squareDimension(),
                                VertexView.centerPoint() + - mStateText.getTextSize() * 0.7f,
                                cursor + VertexView.squareDimension(),
                                VertexView.centerPoint()  + mStateText.getTextSize() * 0.7f,
                                mStateLinePaint);
                    } else {
                        canvas.drawLine(cursor, VertexView.centerPoint() - mStateText.getTextSize() * 0.7f,
                                cursor, VertexView.centerPoint() + mStateText.getTextSize() * 0.7f,
                                mStateLinePaint);
                    }
                    cursorShowed = true;
                } else {
                    cursorShowed = false;
                }
            }
        } else {
            setText(this.label);
            Toast.makeText(getContext(), "Tamanho máximo para nome de estado é " +
                    MAX_LENGHT_LABEL + " caracteres!",
                    Toast.LENGTH_SHORT).show();
        }

        // Círculo contorno
        if (initialState) {
            canvas.drawCircle(VertexView.centerPoint() + VertexView.squareDimension(),
                    VertexView.centerPoint(), VertexView.stateRadius, mStateLinePaint);
        } else {
            canvas.drawCircle(VertexView.centerPoint(), VertexView.centerPoint(),
                    VertexView.stateRadius, mStateLinePaint);
        }

        // Estado final
        if (finalState) {
            if (initialState) {
                canvas.drawCircle(VertexView.centerPoint() + VertexView.squareDimension(),
                        VertexView.centerPoint() , VertexView.stateRadius - VertexView.SPACE,
                        mStateLinePaint);
            } else {
                canvas.drawCircle(VertexView.centerPoint(), VertexView.centerPoint(),
                        VertexView.stateRadius - VertexView.SPACE, mStateLinePaint);
            }
        }

        if (initialState) {
            Path initialState = new Path();
            initialState.moveTo(VertexView.centerPoint(), VertexView.centerPoint() + VertexView.stateRadius);
            initialState.lineTo(VertexView.squareDimension() + VertexView.SPACE, VertexView.centerPoint());
            initialState.lineTo(VertexView.centerPoint(), VertexView.centerPoint() - VertexView.stateRadius);
            initialState.lineTo(VertexView.centerPoint(), VertexView.centerPoint() + VertexView.stateRadius);
            canvas.drawPath(initialState, mStateLinePaint);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("onKeyDown", "keycode "+keyCode);
        if (getText().toString().length() < MAX_LENGHT_LABEL || keyCode == KeyEvent.KEYCODE_DEL ||
                keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_CLEAR
                || keyCode == KeyEvent.KEYCODE_ESCAPE) {
            return super.onKeyDown(keyCode, event);
        }
        Toast.makeText(getContext(), "Tamanho máximo para nome de estado é " + MAX_LENGHT_LABEL
                + " caracteres!",
                Toast.LENGTH_SHORT)
                .show();
        return false;
    }

    /**
     * Define a largura e a altura do estado a ser desenhado.
     *
     * @param widthMeasureSpec  largura.
     * @param heightMeasureSpec altura.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (initialState) {
            setMeasuredDimension(VertexView.squareDimension() * 2, VertexView.squareDimension());
        } else {
            setMeasuredDimension(VertexView.squareDimension(), VertexView.squareDimension());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public boolean onDownAction(MotionEvent e) {
        Log.d(label + " - onDown", label + " - onDown" +
                " (" + e.getX() + ", " + e.getY() + ")");
        EditGraphLayout parentView = ((EditGraphLayout) getParent());
        int mode = parentView.getMode();
        if (mode == EditGraphLayout.CREATION_MODE) {
            if (parentView.isStateSelected()) {
                parentView.addEdgeView(parentView.getStateSelect(), VertexView.this);
                parentView.getStateSelect().onSelect();
                parentView.setOnStateSelected(false);
            }
        } else if (mode == EditGraphLayout.EDITION_MODE) {
        }
        return true;
    }

    public void onLongPressAction(MotionEvent e) {
        EditGraphLayout parentView = ((EditGraphLayout) getParent());
        if (parentView.onDefineInitialState()) {
            parentView.setInitialState(this);
            return;
        }
        int mode = parentView.getMode();
        if (mode == EditGraphLayout.CREATION_MODE) {
            if (select) {
                parentView.setOnStateSelected(false);
                parentView.setStateSelect(null);
                onSelect();
            } else {
                parentView.setOnStateSelected(true);
                parentView.setStateSelect(VertexView.this);
                onSelect();
            }
        } else if (mode == EditGraphLayout.EDITION_MODE) {
            setFinalState(!finalState);
        }
        Log.d(label + " - onLongPress", label + " - onLongPress" +
                " (" + e.getX() + ", " + e.getY() + ")");
    }

    public boolean onDoubleTapAction(MotionEvent e) {
        EditGraphLayout parentView = ((EditGraphLayout) getParent());
        int mode = parentView.getMode();
        if (mode == EditGraphLayout.CREATION_MODE) {
            parentView.removeVertexView(gridPoint);
        } else if (mode == EditGraphLayout.EDITION_MODE) {
            float x = e.getX();
            e.setLocation(X_FOR_DISPATCH_TOUCH_EVENT_TO_EDIT_TEXT, e.getY());
            super.onTouchEvent(e);
            e.setLocation(x, e.getY());
            if (rectLabel.contains(e.getX(), e.getY())) {
                int indice = 0;
                label = getText().toString();
                while (indice < label.length() && rectLabel.left + errorRectLabel +
                        mStateText.measureText(label.substring(0, indice)) < e.getX()) {
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
            Log.d(label + " - onContextClick", label + " - onContextClick" +
                    " (" + e.getX() + ", " + e.getY() + ")");
            return true;
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

}