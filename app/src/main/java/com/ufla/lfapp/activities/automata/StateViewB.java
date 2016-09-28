package com.ufla.lfapp.activities.automata;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.ufla.lfapp.R;

/**
 * Created by carlos on 9/21/16.
 */
public class StateViewB extends View {

    private Paint mStateInternPaint;
    private Paint mStateLinePaint;
    private Paint mStateText;
    private float stateRadius;
    private State state = new State("q0", 70, 100);

    public StateViewB(Context context) {
        super(context);
        init();
        initDefault();
    }

    public StateViewB(Context context, AttributeSet attrs) {
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
            state.setName(a.getString(R.styleable.StateView_name));
            state.setName((state.getName() == null) ? "q?" :state.getName());
            state.setX(a.getFloat(R.styleable.StateView_posX, 100.0f));
            state.setY(a.getFloat(R.styleable.StateView_posY, 100.0f));
        } finally {
            // release the TypedArray so that it can be reused.
            a.recycle();
        }

    }

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
        state = new State();
    }

    private void initDefault() {
        mStateLinePaint.setColor(Color.BLACK);
        mStateLinePaint.setStrokeWidth(5.0f);
        mStateInternPaint.setColor(Color.TRANSPARENT);
        mStateText.setColor(Color.BLACK);
        mStateText.setTextSize(40.0f);
        stateRadius = 60.0f;
        state.setName("q?");
        state.setX(100.0f);
        state.setY(100.0f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(state.getX(), state.getY(), stateRadius, mStateInternPaint);
        canvas.drawText(state.getName(), state.getX(), state.getY(), mStateText);
        canvas.drawCircle(state.getX(), state.getY(), stateRadius, mStateLinePaint);
    }



}