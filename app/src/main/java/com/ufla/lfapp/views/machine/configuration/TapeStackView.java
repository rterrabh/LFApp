package com.ufla.lfapp.views.machine.configuration;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

/**
 * Created by carlos on 4/6/17.
 */

public class TapeStackView extends TapeView {

    private String[] stack;

    public TapeStackView(Context context) {
        super(context);
    }

    public TapeStackView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TapeStackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TapeStackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void initCellSize() {
        tape[tape.length-1] = "";
        super.initCellSize();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        this.cellWidth -= metrics.densityDpi * 0.1f;
        for (String cell : stack) {
            float cellSize = paintText.measureText(cell);
            if (cellSize > this.cellWidth) {
                this.cellWidth = cellSize;
            }
        }
        this.cellWidth += metrics.densityDpi * 0.1f;
    }

    // MÉTODOS ACESSORES
    public String[] getStack() {
        return stack;
    }

    public void setStack(String[] stack) {
        if (stack.length == 0) {
            this.stack = new String[] { "" };
        } else {
            this.stack = stack;
        }
    }

    public void setStack(String stack) {
        setStack(tapeToArray(stack));
    }

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
        float endX = (cellWidth * stack.length) + beginX;
        float beginY = getStateCenterPointY() + getStateRadius() + getStateSpace()
                + distElements;
        float endY = beginY + cellHeight;
        canvas.drawLine(beginX, beginY, beginX, endY, paintTape);
        canvas.drawLine(beginX, beginY, endX, beginY, paintTape);
        canvas.drawLine(beginX, endY, endX, endY, paintTape);
        for (int i = 0; i < stack.length; i++) {
            canvas.drawLine(beginX, beginY, beginX, endY, paintTape);
            float deslocX = (cellWidth - paintText.measureText(stack[i])) / 2.0f;
            canvas.drawText(stack[i], beginX + deslocX, beginY + textSize, paintText);
            beginX += cellWidth;
        }
    }

    /**
     * Define o tamanho do TapeView.
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = (int) (getStateCenterPointY() + getStateRadius() + getStateSpace()
                + distElements + cellHeight + paddingDown);
        int width = (int) (cellWidth * Math.max(tape.length + 1, stack.length + 1));
        setMeasuredDimension(width, height);
    }


}
