package com.ufla.lfapp.views.machine.configuration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import com.ufla.lfapp.utils.PointUtils;
import com.ufla.lfapp.views.graph.vertex.VertexDraw;
import com.ufla.lfapp.views.graph.vertex.VertexDrawnFactory;

import java.util.List;

/**
 * Created by carlos on 4/6/17.
 */

public class TapeView extends  AbstractTapeView {

    private static final String THREE_DOTS = "...";
    protected String[] tape;
    protected int index;
    protected VertexDraw stateDraw;

    public TapeView(Context context) {
        super(context);
    }

    public TapeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void initCellSize() {
        for (String cell : tape) {
            float cellSize = paintText.measureText(cell);
            if (cellSize > this.cellWidth) {
                this.cellWidth = cellSize;
            }
        }
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        this.cellWidth += metrics.densityDpi * 0.1f;
    }

    /**
     * Recupera valor de y no ponto central do estado.
     * @return valor de y no ponto central do estado
     */
    public int getStateCenterPointY() {
        return (int) (cellHeight + distTapeState + getStateRadius()
                + getStateSpace() + PADDING_TOP);
    }

    /**
     * Recupera valor de x no ponto central do estado.
     * @return valor de x no ponto central do estado
     */
    public int getStateCenterPointX() {
        return (int) (cellWidth * (index + 1));
    }

    /**
     * Cria objeto que define o desenho do estado.
     * @return objeto que define o desenho do estado
     */
    private VertexDraw createStateDraw() {
        return new VertexDrawnFactory()
                .createVertexDraw(createStateDrawType(),
                        getStateRadius(),
                        getStateCenterPointX(),
                        getStateCenterPointY(),
                        getInitialStateSize());
    }

    // MÉTODOS ACESSORES

    public void setTape(List<String> tape) {
        this.tape = new String[tape.size()];
        this.tape = tape.toArray(this.tape);
    }

    public static String[] tapeToArray(String tape) {
        int n = tape.length();
        String[] tapeArray = new String[n];
        for (int i = 0; i < n; i++) {
            tapeArray[i] = Character.toString(tape.charAt(i));
        }
        return tapeArray;
    }

    public static String[] tapeToArrayWithDots(String tape) {
        int n = tape.length();
        String[] tapeArray = new String[n+1];
        for (int i = 0; i < n; i++) {
            tapeArray[i] = Character.toString(tape.charAt(i));
        }
        tapeArray[n] = THREE_DOTS;
        return tapeArray;
    }

    public void setTapeALL(String tape) {
        this.tape = tapeToArray(tape);
    }

    public void setTape(String tape) {
        this.tape = tapeToArrayWithDots(tape);
    }

    public void finalize() {
        initCellSize();
        stateDraw = createStateDraw();
        setArrowPath();
    }

    private float getStateRadiusWithSpace() {
        if (finalState) {
            return getStateRadius() + getStateSpace();
        }
        return getStateRadius();
    }

    private void setArrowPath() {
        arrowPath = new Path();
        float arrowX = getStateCenterPointX();
        PointF cellPoint = new PointF(arrowX, PADDING_TOP + cellHeight);
        PointF statePoint = new PointF(arrowX, getStateCenterPointY() - getStateRadiusWithSpace());
        arrowPath.moveTo(statePoint.x, statePoint.y);
        arrowPath.lineTo(cellPoint.x, cellPoint.y);
        float angle = PointUtils.angleFromP1ToP2(cellPoint, statePoint);
        arrowPath.lineTo(cellPoint.x + arrowLength *
                        (float) Math.cos(angle + Math.toRadians(ARROW_ANGLE)),
                cellPoint.y + arrowLength *
                        (float) Math.sin(angle + Math.toRadians(ARROW_ANGLE)));
        arrowPath.moveTo(cellPoint.x, cellPoint.y);
        arrowPath.lineTo(cellPoint.x + arrowLength *
                        (float) Math.cos(angle - Math.toRadians(ARROW_ANGLE)),
                cellPoint.y + arrowLength *
                        (float) Math.sin(angle - Math.toRadians(ARROW_ANGLE)));
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private Path arrowPath;

    /**
     * Desenha o TapeView.
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float textSize = paintText.getTextSize();
        // padding left
        float beginX = paddingLeftAndRight;
        float endX = (cellWidth * tape.length) + beginX;
        float beginY = PADDING_TOP;
        float endY = beginY + cellHeight;
        float beginXSelected = beginX + ((cellWidth * index));
        canvas.drawRect(beginXSelected, beginY, beginXSelected + cellWidth, endY,
                paintSelectedCell);
        canvas.drawRect(beginX, beginY, endX, endY, paintTape);
        for (int i = 0; i < tape.length; i++) {
            canvas.drawLine(beginX, beginY, beginX, endY, paintTape);
            float deslocX = (cellWidth - paintText.measureText(tape[i])) * 0.75f;
            canvas.drawText(tape[i], beginX + deslocX, beginY + textSize, paintText);
            beginX += cellWidth;
        }
        canvas.drawPath(arrowPath, paintTape);
        canvas.drawPath(stateDraw.getInternVertexPath(), mStateInternPaint);
        canvas.drawTextOnPath(state, stateDraw.getLabelPath(), 0.0f,
                paintText.getTextSize() * 0.25f, paintText);
        canvas.drawPath(stateDraw.getExternVertexPath(), mStateLinePaint);
    }

    /**
     * Define o tamanho do TapeView.
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = (int) (cellHeight + distTapeState
                + ((getStateSpace() + getStateRadius()) * 2)
                + paddingDown + PADDING_TOP);
        int width = (int) (cellWidth * (tape.length + 1));
        setMeasuredDimension(width, height);
    }

}
