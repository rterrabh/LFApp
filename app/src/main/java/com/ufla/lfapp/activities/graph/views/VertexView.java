package com.ufla.lfapp.activities.graph.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

// Color Line #FFCCCC
// #FFCCCC

/**
 * Created by carlos on 9/21/16.
 * Representa a visão de um estado em um autômato ou máquina de estados.
 */
public class VertexView extends EditText {

    private static float dpi;
    private static int mStateLineColor = Color.parseColor("#968D8D");
    private static int mStateInternColor = Color.parseColor("#FFCCCC");
    private static int mStateInternColorSelect = Color.parseColor("#FF6666");
    private Paint mStateInternPaint;
    private Paint mStateLinePaint;
    private Paint mStateText;
    private final static int textSize;
    public final static int stateRadius;
    public final static int SPACE;
    private Point gridPoint;
    private static int cont_views = 0;
    private String label = "";
    private boolean select;
    private boolean showCursor;
    private boolean cursorShowed;
    private RectF rectLabel;
    private GestureDetector gestureDetector;
    private boolean delegateTouchEvent;
    private int cursorInd;

    static {
        dpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        //dpi = 1.0f;
//        if (dpi == 160.0) {
//            stateRadius = (int) (Math.sqrt(dpi));
//        } else {
//            stateRadius = (int) (Math.sqrt(dpi) * 2.0f);
//
//        }
        textSize = (int) (dpi / 8.0f);
        stateRadius = (int) (dpi / 5.0);
        SPACE = (int) (0.025f * dpi);
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


    public void setLabel(String label) {
        this.label = label;
    }
    public Point getGridPoint() {
        return gridPoint;
    }

    public void setGridPoint(Point gridPoint) {
        this.gridPoint = gridPoint;
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

    /**
     * Define o nome do estado a ser desenhado.
     *
     * @param vertexLabel nome do vértice a ser desenhado.
     */
    public void setVertexLabel(String vertexLabel) {

    }

    public String getVertexLabel() {
        return null;
    }

//    /**
//     * Define o estado a ser desenhado.
//     * @param row coordenada x do estado a ser desenhado.
//     * @param col coordenada y do estado a ser desenhado.
//     */
//    public void defineState(int row, int col) {
//        vertex = new State(row, col);
//    }

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
    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void init() {
        mStateInternPaint = new Paint();
        mStateInternPaint.setStyle(Paint.Style.FILL);
        mStateLinePaint = new Paint();
        mStateLinePaint.setAntiAlias(true);
        mStateLinePaint.setStyle(Paint.Style.STROKE);
        mStateText = new Paint();
        mStateText.setAntiAlias(true);
        mStateText.setStrokeWidth(2.0f);
        mStateText.setStyle(Paint.Style.FILL);
        mStateText.setTextAlign(Paint.Align.CENTER);
        select = false;
        label = "q" + cont_views;
        cont_views++;
        setInputType(InputType.TYPE_CLASS_TEXT);
        setText(label);
        setEnabled(true);
        showCursor = false;
        cursorShowed = false;
        setBackgroundColor(Color.TRANSPARENT);
        gestureDetector = new GestureDetector(getContext(), new VertexView.GestureListener());
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
        mStateLinePaint.setStrokeWidth(5.0f);
        mStateInternPaint.setColor(VertexView.mStateInternColor);
        mStateText.setColor(Color.BLACK);
        mStateText.setTextSize(textSize);
    }


    /**
     * Desenha o estado na tela.
     *
     * @param canvas objeto para desenhar.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //Borda
        canvas.drawRect(0, 0, VertexView.squareDimension(), VertexView.squareDimension(),
                SpaceWithBorder.mBorderPaint);
        if (select) {
            mStateInternPaint.setColor(mStateInternColorSelect);
        }
        //Círculo interno
        mStateText.getTextSize();
        canvas.drawCircle(VertexView.centerPoint(), VertexView.centerPoint(),
                VertexView.stateRadius, mStateInternPaint);
        if (select) {
            mStateInternPaint.setColor(mStateInternColor);
        }
        //Texto
        String label = getText().toString();
        if (label.length() <= 5) {
            setSelection(cursorInd, cursorInd);
            setSelection(cursorInd);
            this.label = label;
            float textLenght = mStateText.measureText(label);
            canvas.drawText(label, VertexView.centerPoint(), VertexView.centerPoint() +
                    mStateText.getTextSize() * 0.30f, mStateText);
            rectLabel = new RectF(VertexView.centerPoint() - textLenght / 2.0f - 15.0f,
                    VertexView.centerPoint() - mStateText.getTextSize(),
                    VertexView.centerPoint() + textLenght / 2.0f + 15.0f,
                    VertexView.centerPoint() + mStateText.getTextSize());
            showCursor = isFocused();
            if (showCursor) {
                if (!cursorShowed) {
                    float cursor = mStateText.measureText(label.substring(0, getSelectionStart())) +
                            VertexView.centerPoint() - textLenght / 2.0f;
                    canvas.drawLine(cursor, VertexView.centerPoint() - mStateText.getTextSize() * 0.7f,
                            cursor, VertexView.centerPoint() + mStateText.getTextSize() * 0.7f, mStateLinePaint);
                    cursorShowed = true;
                } else {
                    cursorShowed = false;
                }
            }
        } else {
            setText(this.label);
            Toast.makeText(getContext(), "Tamanho máximo para nome de estado é 5 caracteres!",
                    Toast.LENGTH_SHORT).show();
        }

        //Círculo contorno
        canvas.drawCircle(VertexView.centerPoint(), VertexView.centerPoint(),
                VertexView.stateRadius, mStateLinePaint);

        //Estado final
        canvas.drawCircle(VertexView.centerPoint(), VertexView.centerPoint(),
                VertexView.stateRadius + VertexView.SPACE, mStateLinePaint);
        //super.onDraw(canvas);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("onKeyDown", "keycode "+keyCode);
        if (getText().toString().length() < 5 || keyCode == KeyEvent.KEYCODE_DEL ||
                keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_CLEAR
                || keyCode == KeyEvent.KEYCODE_ESCAPE) {
            return super.onKeyDown(keyCode, event);
        }
        Toast.makeText(getContext(), "Tamanho máximo para nome de estado é 5 caracteres!",
                Toast.LENGTH_SHORT).show();
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
        setMeasuredDimension(VertexView.squareDimension(), VertexView.squareDimension());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public boolean onDownAction(MotionEvent e) {
        Log.d(label + " - onDown", label + " - onDown" +
                " (" + e.getX() + ", " + e.getY() + ")");
        float x = e.getX();
        e.setLocation(3, e.getY());
        super.onTouchEvent(e);
        e.setLocation(x, e.getY());
        if (rectLabel.contains(e.getX(), e.getY())) {
            int indice = 0;
            label = getText().toString();
            while (indice < label.length() && rectLabel.left + 15.0f + mStateText.measureText(label
                    .substring(0, indice)) < e.getX()) {
                indice++;
            }
            cursorInd = indice;
            setSelection(indice, indice);
            setSelection(indice);
            invalidate();
            Log.d("e", e.getX() + ", " + e.getY());
                Log.d(label + " - onCursor", label + " - onCursor" +
                        " (" + e.getX() + ", " + e.getY() + ")");
        }
        return true;
    }

    public void onLongPressAction(MotionEvent e) {
        EditGraphLayout parentView = ((EditGraphLayout) getParent());
        if (parentView.isStateSelected()) {
            parentView.addEdgeView(parentView.getStateSelect(), VertexView.this);
            parentView.getStateSelect().onSelect();
            parentView.setOnStateSelected(false);
        } else {
            parentView.setOnStateSelected(true);
            parentView.setStateSelect(VertexView.this);
            onSelect();
        }
        Log.d(label + " - onLongPress", label + " - onLongPress" +
                " (" + e.getX() + ", " + e.getY() + ")");
    }

    public boolean onDoubleTapAction(MotionEvent e) {
        ((EditGraphLayout) getParent()).removeVertexView(gridPoint);

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