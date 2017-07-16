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

public class TapeMultiTrackView extends AbstractTapeView {

    protected String[][] tracks;
    protected int index;
    protected VertexDraw stateDraw;

    public TapeMultiTrackView(Context context) {
        super(context);
    }

    public TapeMultiTrackView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TapeMultiTrackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TapeMultiTrackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void initCellSize() {
        for (String[] track : tracks) {
            for (String cell : track) {
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
     * Recupera valor de y no ponto central do estado.
     * @return valor de y no ponto central do estado
     */
    public int getStateCenterPointY() {
        float height =  (cellHeight + distElements);
        int desloc = (int) (PADDING_TOP + tracks.length * height - distElements);
        return (int) (desloc + distTapeState + getStateSpace() + getStateRadius());
    }

    /**
     * Recupera valor de x no ponto central do estado.
     * @return valor de x no ponto central do estado
     */
    public int getStateCenterPointX() {
        return (int) (cellWidth * (index + 1));
    }


    /**
     * Cria objeto que define o desenho do estado na fita de índice _indTape_.
     * @return objeto que define o desenho do estado na fita de índice _indTape_
     */
    private VertexDraw createStateDraw() {
        return new VertexDrawnFactory()
                .createVertexDraw(createStateDrawType(),
                        getStateRadius(),
                        getStateCenterPointX(),
                        getStateCenterPointY(),
                        getInitialStateSize());
    }

    public void setTracks(String[] tracks) {
        this.tracks = new String[tracks.length][];
        for (int i = 0; i < tracks.length; i++) {
            this.tracks[i] = TapeView.tapeToArrayWithDots(tracks[i]);
        }
    }

    public void finalize() {
        initCellSize();
        stateDraw = createStateDraw();
        setArrowsPaths();
    }

    private void setArrowsPaths() {
        arrowsPath = new Path();
        int height = (int) (cellHeight + distElements);
        float cellY = PADDING_TOP + cellHeight;
        float arrowX = getStateCenterPointX();
        for (int i = 0; i < tracks.length; i++) {
            PointF cellPoint = new PointF(arrowX, cellY);
            PointF nextCellPoint = new PointF(arrowX, cellY + distElements);
            if (i == tracks.length - 1) {
                nextCellPoint.y = getStateCenterPointY() - getStateRadius();
            }
            arrowsPath.moveTo(nextCellPoint.x, nextCellPoint.y);
            arrowsPath.lineTo(cellPoint.x, cellPoint.y);
            float angle = PointUtils.angleFromP1ToP2(cellPoint, nextCellPoint);
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

    public void setIndex(int index) {
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
        float endX = (cellWidth * tracks[0].length) + beginX;
        float beginY = PADDING_TOP;
        int height = (int) (cellHeight + distElements);
        float beginXSelected = beginX + cellWidth * index;
        for (int i = 0; i < tracks.length; i++) {
            beginX = paddingLeftAndRight;
            float endY = beginY + cellHeight;
            canvas.drawRect(beginXSelected, beginY, beginXSelected + cellWidth, endY,
                    paintSelectedCell);
            canvas.drawRect(beginX, beginY, endX, endY, paintTape);
            for (int j = 0; j < tracks[i].length; j++) {
                canvas.drawLine(beginX, beginY, beginX, endY, paintTape);
                float deslocX = (cellWidth - paintText.measureText(tracks[i][j])) * 0.75f;
                canvas.drawText(tracks[i][j], beginX + deslocX, beginY + textSize, paintText);
                beginX += cellWidth;
            }
            beginY += height;
        }
        canvas.drawPath(arrowsPath, paintTape);
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
        int heightElem = (int) (cellHeight + distElements);
        int heightState = (int) distTapeState + (getStateSpace() + getStateRadius()) * 2;
        int height = (int) (heightElem * tracks.length - distElements + paddingDown + heightState);
        int width = (int) (cellWidth * (tracks[0].length + 1));
        setMeasuredDimension(width, height);
    }
}
