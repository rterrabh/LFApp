package com.ufla.lfapp.activities.graph.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.View;

// Color Line #FFCCCC
// #FFCCCC

/**
 * Created by carlos on 9/21/16.
 * Representa a visão de um estado em um autômato ou máquina de estados.
 */
public class VertexView extends View {

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
        SPACE = (int) (0.02f * dpi);
    }

    public VertexView(Context context) {
        super(context);
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
        canvas.drawCircle(VertexView.centerPoint(), VertexView.centerPoint(),
                VertexView.stateRadius, mStateInternPaint);
        if (select) {
            mStateInternPaint.setColor(mStateInternColor);
        }
        //Texto
        canvas.drawText(label, VertexView.centerPoint(), VertexView.centerPoint(),
                mStateText);
        //Círculo contorno
        canvas.drawCircle(VertexView.centerPoint(), VertexView.centerPoint(),
                VertexView.stateRadius, mStateLinePaint);
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


}