package com.ufla.lfapp.activities.automata.graph.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.ufla.lfapp.activities.automata.graph.layout.EditGraphLayout;


/**
 * Created by carlos on 10/10/16.
 */
public class SpaceWithBorder extends View {

    private BorderVertex borderVertex;
    private int borderSpace = 5;

    private SpaceWithBorder(Context context) {
        super(context);
    }


    public void setTop(boolean top) {
        borderVertex.setTop(top);
    }

    public void setBottom(boolean bottom) {
        borderVertex.setBottom(bottom);
    }

    public void setLeft(boolean left) {
        borderVertex.setLeft(left);
    }

    public void setRight(boolean right) {
        borderVertex.setRight(right);
    }

    public void setBorderVertex(BorderVertex borderVertex) {
        this.borderVertex = borderVertex;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        EditGraphLayout parentView = (EditGraphLayout) getParent();
        int squareDimension = parentView.getVertexSquareDimension();
        Paint mBorderPaint = parentView.getmVertexBorderPaint();
        if (mBorderPaint != null && borderVertex != null) {
            canvas.drawRect( borderVertex.isLeft() ? borderSpace : 0,
                    borderVertex.isTop() ? borderSpace : 0,
                    borderVertex.isRight() ? squareDimension - borderSpace : squareDimension,
                    borderVertex.isBottom() ? squareDimension - borderSpace : squareDimension,
                    mBorderPaint );
        }
    }

    /**
     * Define a largura e a altura do estado a ser desenhado.
     *
     * @param widthMeasureSpec  largura.
     * @param heightMeasureSpec altura.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        EditGraphLayout parentView = (EditGraphLayout) getParent();
        int squareDimension = parentView.getVertexSquareDimension();
        setMeasuredDimension(squareDimension, squareDimension);
    }

    public static SpaceWithBorder getSpaceWithBorder(EditGraphLayout editGraphLayout,
                                                     BorderVertex borderVertex) {
        SpaceWithBorder spaceWithBorder = new SpaceWithBorder(editGraphLayout.getContext());
        spaceWithBorder.borderVertex = borderVertex;
        return spaceWithBorder;
    }

    public static SpaceWithBorder getSpaceWithBorder(Context context, Paint mBorderPaint,
                                                     int squareDimension, BorderVertex borderVertex) {
        SpaceWithBorder spaceWithBorder = new SpaceWithBorder(context);
        spaceWithBorder.setMinimumHeight(squareDimension);
        spaceWithBorder.setMinimumWidth(squareDimension);
        spaceWithBorder.borderVertex = borderVertex;
        return spaceWithBorder;
    }
}
