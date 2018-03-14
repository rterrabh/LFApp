package com.ufla.lfapp.views.machine.configuration;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.ufla.lfapp.views.graph.vertex.VertexDrawType;

/**
 * Created by carlos on 4/6/17.
 */

public abstract class AbstractTapeView extends View {

    protected static final int ARROW_ANGLE = 30;
    protected static final int PADDING_TOP = 3;
//    protected static final int mStateLineColor = ResourcesContext.getColor(R.color.SomeGray1);
//    protected static final int mStateInternColor = ResourcesContext.getColor(R.color.SomePink1);
//    protected static final int mSelectedCellColor = ResourcesContext.getColor(R.color.SomeGreen1);
    protected static final int mStateLineColor = Color.parseColor("#968D8D");
    protected static final int mStateInternColor = Color.parseColor("#FFCCCC");
    protected static final int mSelectedCellColor = Color.parseColor("#4FD5D6");
    protected static float paddingLeftAndRight;
    protected static float paddingDown;
    protected static float distTapeState;
    protected static float distElements;
    protected static float cellHeight;
    protected static Paint mStateInternPaint;
    protected static Paint mStateLinePaint;
    protected static Paint paintTape;
    protected static Paint paintText;
    protected static Paint paintSelectedCell;
    protected static float arrowLength;

    protected String state;
    protected boolean finalState;
    protected boolean initialState;
    protected float cellWidth;


    // Inicializa objetos Paint, usados para desenhar a View.
    static {
        paintTape = new Paint();
        paintTape.setColor(Color.BLACK);
        paintTape.setAntiAlias(true);
        paintTape.setStyle(Paint.Style.STROKE);
        paintText = new Paint();
        paintText.setColor(Color.BLACK);
        paintText.setAntiAlias(true);
        paintText.setTextAlign(Paint.Align.CENTER);
        mStateInternPaint = new Paint();
        mStateInternPaint.setStyle(Paint.Style.FILL);
        mStateInternPaint.setColor(mStateInternColor);
        mStateLinePaint = new Paint();
        mStateLinePaint.setAntiAlias(true);
        mStateLinePaint.setStyle(Paint.Style.STROKE);
        mStateLinePaint.setColor(mStateLineColor);
        paintSelectedCell = new Paint();
        paintSelectedCell.setStyle(Paint.Style.FILL);
        paintSelectedCell.setColor(mSelectedCellColor);
    }

    protected AbstractTapeView(Context context) {
        super(context);
        init();
    }

    protected AbstractTapeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected AbstractTapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected AbstractTapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /**
     * Inicializa TapeView
     */
    private void init() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        paintTape.setStrokeWidth(metrics.densityDpi * 0.01f);
        float textSize = (float) Math.sqrt(metrics.densityDpi) * 2.0f;
        paintText.setTextSize(textSize);
        arrowLength = textSize * 0.25f;
        cellHeight = textSize * 1.5f;
        paddingLeftAndRight = cellHeight * 0.5f;
        paddingDown = cellHeight * 0.25f;
        distTapeState = cellHeight / 3.0f;
        distElements = cellHeight * 0.15f;
    }

    /**
     * Cria tipo de desenho do estado (normal, inicial, final).
     * @return tipo de desenho do estado
     */
    protected VertexDrawType createStateDrawType() {
        if (initialState) {
            if (finalState) {
                return VertexDrawType.INITIAL_FINAL;
            } else {
                return VertexDrawType.INITIAL;
            }
        } else if (finalState) {
            return VertexDrawType.FINAL;
        }
        return VertexDrawType.DEFAULT;
    }

    protected int getStateSpace() {
        return (int) (getStateRadius() * 0.1f);
    }

    /**
     * Calcula raio do desenho do estado.
     * @return raio do desenho do estado
     */
    protected int getStateRadius() {
        return (int) (cellWidth * 0.75f);
    }

    /**
     * Recupera tamanho do desenho do estado inicial.
     * @return tamanho do desenho do estado inicial.
     */
    protected float getInitialStateSize() {
        return getStateRadius() * 0.35f;
    }

    protected abstract void initCellSize();


    // MÉTODOS ACESSORES

    public void setState(String state) {
        this.state = state;
    }

    public void setFinalState(boolean finalState) {
        this.finalState = finalState;
    }

    public void setInitialState(boolean initialState) {
        this.initialState = initialState;
    }

}
