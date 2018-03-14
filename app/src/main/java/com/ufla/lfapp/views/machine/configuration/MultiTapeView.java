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

/**
 * Created by carlos on 4/6/17.
 */

public class MultiTapeView extends AbstractTapeView {

    protected String[][] tapes;
    protected int[] index;
    protected VertexDraw[] stateDraws;

    public MultiTapeView(Context context) {
        super(context);
    }

    public MultiTapeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiTapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MultiTapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void initCellSize() {
        for (String[] tape : tapes) {
            for (String cell : tape) {
                float cellSize = paintText.measureText(cell);
                if (cellSize > this.cellWidth) {
                    this.cellWidth = cellSize;
                }
            }
        }
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        this.cellWidth += metrics.densityDpi * 0.1f;
    }

    /**
     * Recupera valor de y no ponto central do estado na fita com índice indTape.
     * @param indTape fita em que estado será desenhado
     * @return valor de y no ponto central do estado na fita indTape
     */
    public int getStateCenterPointY(int indTape) {
        int height = (int) (cellHeight + distTapeState
                + ((getStateSpace() + getStateRadius()) * 2)
                + distElements);
        int desloc = PADDING_TOP + indTape * height;
        return (int) (cellHeight + distTapeState + getStateRadius()
                + getStateSpace() + desloc);
    }

    /**
     * Recupera valor de x no ponto central do estado na fita com índice indTape.
     * @param indTape fita em que estado será desenhado
     * @return valor de x no ponto central do estado na fita indTape
     */
    public int getStateCenterPointX(int indTape) {
        return (int) (cellWidth * (index[indTape] + 1));
    }


    /**
     * Cria objeto que define o desenho do estado na fita de índice _indTape_.
     * @param indTape índice da fita referente ao estado
     * @return objeto que define o desenho do estado na fita de índice _indTape_
     */
    private VertexDraw createStateDraw(int indTape) {
        return new VertexDrawnFactory()
                .createVertexDraw(createStateDrawType(),
                        getStateRadius(),
                        getStateCenterPointX(indTape),
                        getStateCenterPointY(indTape),
                        getInitialStateSize());
    }

    public void setTapes(String[] tapes) {
        this.tapes = new String[tapes.length][];
        for (int i = 0; i < tapes.length; i++) {
            this.tapes[i] = TapeView.tapeToArrayWithDots(tapes[i]);
        }
    }

    public void finalize() {
        initCellSize();
        stateDraws = new VertexDraw[tapes.length];
        for (int i = 0; i < tapes.length; i++) {
            stateDraws[i] = createStateDraw(i);
        }
        setArrowsPaths();
    }

    private void setArrowsPaths() {
        arrowsPath = new Path();
        int height = (int) (cellHeight + distTapeState
                + ((getStateSpace() + getStateRadius()) * 2)
                + distElements);
        float cellY = PADDING_TOP + cellHeight;
        for (int i = 0; i < tapes.length; i++) {
            float arrowX = getStateCenterPointX(i);
            PointF cellPoint = new PointF(arrowX, cellY);
            PointF statePoint = new PointF(arrowX, getStateCenterPointY(i) - getStateRadius());
            arrowsPath.moveTo(statePoint.x, statePoint.y);
            arrowsPath.lineTo(cellPoint.x, cellPoint.y);
            float angle = PointUtils.angleFromP1ToP2(cellPoint, statePoint);
            arrowsPath.lineTo(cellPoint.x + arrowLength *
                            (float) Math.cos(angle + Math.toRadians(ARROW_ANGLE)),
                    cellPoint.y + arrowLength *
                            (float) Math.sin(angle + Math.toRadians(ARROW_ANGLE)));
            arrowsPath.moveTo(cellPoint.x, cellPoint.y);
            arrowsPath.lineTo(cellPoint.x + arrowLength *
                            (float) Math.cos(angle - Math.toRadians(ARROW_ANGLE)),
                    cellPoint.y + arrowLength *
                            (float) Math.sin(angle - Math.toRadians(ARROW_ANGLE)));
            cellY += height;
        }
    }

    public void setIndex(int[] index) {
        this.index = index;
    }

    private Path arrowsPath;

    /**
     * Desenha o TapeView.
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float textSize = paintText.getTextSize();
        float beginX = paddingLeftAndRight;
        float endX = (cellWidth * tapes[0].length) + beginX;
        float beginY = PADDING_TOP;
        int height = (int) (cellHeight + distTapeState
                + ((getStateSpace() + getStateRadius()) * 2)
                + distElements);
        for (int i = 0; i < tapes.length; i++) {
            beginX = paddingLeftAndRight;
            float endY = beginY + cellHeight;
            float beginXSelected = beginX + ((cellWidth * index[i]));
            canvas.drawRect(beginXSelected, beginY, beginXSelected + cellWidth, endY,
                    paintSelectedCell);
            canvas.drawRect(beginX, beginY, endX, endY, paintTape);
            for (int j = 0; j < tapes[i].length; j++) {
                canvas.drawLine(beginX, beginY, beginX, endY, paintTape);
                float deslocX = (cellWidth - paintText.measureText(tapes[i][j])) * 0.75f;
                canvas.drawText(tapes[i][j], beginX + deslocX, beginY + textSize, paintText);
                beginX += cellWidth;
            }
            canvas.drawPath(stateDraws[i].getInternVertexPath(), mStateInternPaint);
            canvas.drawTextOnPath(state, stateDraws[i].getLabelPath(), 0.0f,
                    paintText.getTextSize() * 0.25f, paintText);
            canvas.drawPath(stateDraws[i].getExternVertexPath(), mStateLinePaint);
            beginY += height;
        }
        beginY -= distElements;
        beginX = paddingLeftAndRight;
        canvas.drawLine(beginX, beginY, endX, beginY, paintTape);
        canvas.drawPath(arrowsPath, paintTape);

    }

    /**
     * Define o tamanho do TapeView.
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightElem = (int) (cellHeight + distTapeState
                + ((getStateSpace() + getStateRadius()) * 2)
                + distElements);
        int height = (int) (heightElem * tapes.length - distElements + paddingDown);
        int width = (int) (cellWidth * (tapes[0].length + 1));
        setMeasuredDimension(width, height);
    }

}
