package com.ufla.lfapp.activities.graph.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ufla.lfapp.R;
import com.ufla.lfapp.activities.graph.layout.EditGraphLayout;

import java.util.HashSet;
import java.util.Set;

// Color Line #FFCCCC
// #FFCCCC

/**
 * Created by carlos on 9/21/16.
 * Representa a visão de um estado em um autômato ou máquina de estados.
 */
public class VertexView extends View {

    private static int mStateLineColor = Color.parseColor("#968D8D");
    private static int mStateInternColor = Color.parseColor("#FFCCCC");
    private static int mStateInternColorSelect = Color.parseColor("#FF6666");
    private static int mStateInternColorSelectBlue = Color.parseColor("#7f7fff");
    private Paint mStateInternPaint;
    private Paint mStateLinePaint;
    private Paint mStateText;
    private final static int MAX_LENGHT_LABEL = 5;
    private Point gridPoint;
    private static int cont_vertex = 0;
    private String label = "";
    private boolean select;
    private boolean selectBlue;
    private boolean showCursor;
    private boolean cursorShowed;
    private RectF rectLabel;
    private int cursorInd;
    private boolean finalState;
    private boolean initialState;
    private int borderSpace = 5;

    private BorderVertex borderVertex;
    private GestureDetector gestureDetector;
    private Set<EdgeView> edgeDependencies;
    private static int ANGLE_INITIAL_STATE = 30;

    public static void clearContVertex() {
        cont_vertex = 0;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
        invalidate();
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

    public EditGraphLayout getParentEditGraphLayout() {
        return (EditGraphLayout) getParent();
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
            vertexRadius = a.getFloat(R.styleable.StateView_radius, 60.0f);
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
        mStateText.setStyle(Paint.Style.FILL);
        mStateText.setTextAlign(Paint.Align.CENTER);
        select = false;
        selectBlue = false;
        label = "q" + cont_vertex;
        initialState = false;
        cont_vertex++;
        setEnabled(true);
        showCursor = false;
        cursorShowed = false;
        setBackgroundColor(Color.TRANSPARENT);
        gestureDetector = new GestureDetector(getContext(), new VertexView.GestureListener());
        edgeDependencies = new HashSet<>();
    }

    public void setStyle() {
        EditGraphLayout parentView = getParentEditGraphLayout();
        mStateText.setStrokeWidth(parentView.getVertexTextStrokeWidth());
        mStateLinePaint.setStrokeWidth(parentView.getVertexLineStrokeWidth());
        mStateText.setTextSize(parentView.getVertexTextSize());
        rectLabel = new RectF(0,
                parentView.getVertexCenterPoint() - mStateText.getTextSize(),
                parentView.getVertexSquareDimension(),
                parentView.getVertexCenterPoint() + mStateText.getTextSize());
        invalidate();
    }

    public void onSelect() {
        select = !select;
        invalidate();
    }

    public void onSelectBlue() {
        selectBlue = !selectBlue;
        invalidate();
    }

    /**
     * Define valores padrões para os objetos Paint do estado.
     */
    private void defineDefault() {
        mStateLinePaint.setColor(VertexView.mStateLineColor);
        mStateInternPaint.setColor(VertexView.mStateInternColor);
        mStateText.setColor(Color.BLACK);
    }

    public void addEdgeDependencies(EdgeView edgeView) {
        edgeDependencies.add(edgeView);
    }

    public void removeEdgeDependencies(EdgeView edgeView) {
        edgeDependencies.remove(edgeView);
    }

    public Set<EdgeView> getEdgeDependencies() {
        return edgeDependencies;
    }

    /**
     * Desenha o estado na tela.
     *
     * @param canvas objeto para desenhar.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        EditGraphLayout parentView = getParentEditGraphLayout();
        int vertexSquareDimension = parentView.getVertexSquareDimension();
        int vertexCenterPoint = parentView.getVertexCenterPoint();
        int vertexRadius = parentView.getVertexRadius();
        int vertexSpace = parentView.getVertexSpace();
        Paint mVertexBorderPaint = parentView.getmVertexBorderPaint();
        float vertexInitialStateSize = parentView.getVertexInitialStateSize();
        /*
        cursorInd = getSelectionStart();
        if (cursorInd < 0) {
            cursorInd = 0;
        }
        setSelection(cursorInd, cursorInd);
        setSelection(cursorInd);
        // Borda*/
        if (initialState) {
            canvas.drawRect(borderVertex.isLeft() ? borderSpace : 0,
                    borderVertex.isTop() ? borderSpace : 0,
                    borderVertex.isRight() ? vertexSquareDimension * 2 - borderSpace : vertexSquareDimension * 2,
                    borderVertex.isBottom() ? vertexSquareDimension - borderSpace : vertexSquareDimension,
                    mVertexBorderPaint);
            canvas.drawLine(vertexSquareDimension,
                    borderVertex.isTop() ? borderSpace : 0,
                    vertexSquareDimension,
                    borderVertex.isBottom() ? vertexSquareDimension - borderSpace : vertexSquareDimension,
                    mVertexBorderPaint);
        } else {
            canvas.drawRect( borderVertex.isLeft() ? borderSpace : 0,
                    borderVertex.isTop() ? borderSpace : 0,
                    borderVertex.isRight() ? vertexSquareDimension - borderSpace : vertexSquareDimension,
                    borderVertex.isBottom() ? vertexSquareDimension - borderSpace : vertexSquareDimension,
                    mVertexBorderPaint );
        }
        if (select) {
            mStateInternPaint.setColor(mStateInternColorSelect);
        }
        if (selectBlue) {
            mStateInternPaint.setColor(mStateInternColorSelectBlue);
        }
        // Círculo interno
        mStateText.getTextSize();
        if (initialState) {
            canvas.drawCircle(vertexCenterPoint + vertexSquareDimension, vertexCenterPoint,
                    vertexRadius, mStateInternPaint);
        } else {
            canvas.drawCircle(vertexCenterPoint, vertexCenterPoint, vertexRadius,
                    mStateInternPaint);
        }
        mStateInternPaint.setColor(mStateInternColor);
        // Texto
        //if (label.length() <= MAX_LENGHT_LABEL) {
            //setSelection(cursorInd, cursorInd);
            //setSelection(cursorInd);
            this.label = label;
            //float textLenght = mStateText.measureText(label);
            if (initialState) {
                canvas.drawText(label, vertexCenterPoint + vertexSquareDimension,
                        vertexCenterPoint + mStateText.getTextSize() * 0.3f, mStateText);
            } else {
                canvas.drawText(label, vertexCenterPoint, vertexCenterPoint +
                        mStateText.getTextSize() * 0.3f, mStateText);
            }
            /*
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
        }*/

        // Círculo contorno
        if (initialState) {
            canvas.drawCircle(vertexCenterPoint + vertexSquareDimension, vertexCenterPoint,
                    vertexRadius, mStateLinePaint);
        } else {
            canvas.drawCircle(vertexCenterPoint, vertexCenterPoint, vertexRadius, mStateLinePaint);
        }

        // Estado final
        if (finalState) {
            if (initialState) {
                canvas.drawCircle(vertexCenterPoint + vertexSquareDimension, vertexCenterPoint,
                        vertexRadius - vertexSpace, mStateLinePaint);
            } else {
                canvas.drawCircle(vertexCenterPoint, vertexCenterPoint, vertexRadius - vertexSpace,
                        mStateLinePaint);
            }
        }

        if (initialState) {
            Path initialStatePath = new Path();
            Point referencePoint  = new Point(vertexCenterPoint, vertexCenterPoint);
            Point onStatePoint = new Point(vertexSquareDimension + vertexSpace, vertexCenterPoint);
            float angle = PointUtils.angleFromP1ToP2(onStatePoint, referencePoint);
            initialStatePath.moveTo(onStatePoint.x, onStatePoint.y);
            initialStatePath.lineTo(onStatePoint.x + vertexInitialStateSize *
                    (float) Math.cos(angle + Math.toRadians(30)),
                    onStatePoint.y + vertexInitialStateSize *
                            (float) Math.sin(angle + Math.toRadians(30)));
            initialStatePath.moveTo(onStatePoint.x, onStatePoint.y);
            initialStatePath.lineTo(onStatePoint.x + vertexInitialStateSize *
                            (float) Math.cos(angle - Math.toRadians(30)),
                    onStatePoint.y + vertexInitialStateSize *
                            (float) Math.sin(angle - Math.toRadians(30)));
            canvas.drawPath(initialStatePath, mStateLinePaint);
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
        int vertexSquareDimension = getParentEditGraphLayout().getVertexSquareDimension();
        if (initialState) {
            setMeasuredDimension(vertexSquareDimension * 2, vertexSquareDimension);
        } else {
            setMeasuredDimension(vertexSquareDimension, vertexSquareDimension);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    public void onDownRun(MotionEvent e) {
        Log.d(label + " - onDown", label + " - onDown" +
                " (" + e.getX() + ", " + e.getY() + ")");
        EditGraphLayout parentView = ((EditGraphLayout) getParent());
        if (parentView == null) {
            return;
        }
        if (parentView.isStateSelected()) {
            parentView.addEdgeView(parentView.getStateSelect(), VertexView.this);
            parentView.getStateSelect().onSelectBlue();
            parentView.setOnStateSelected(false);
        } else {
            parentView.setOnStateSelected(true);
            parentView.setStateSelect(VertexView.this);
            onSelectBlue();
        }
        /*int mode = parentView.getMode();
        if (mode == EditGraphLayout.CREATION_MODE) {
            if (parentView.isStateSelected()) {
                parentView.addEdgeView(parentView.getStateSelect(), VertexView.this);
                parentView.getStateSelect().onSelect();
                parentView.setOnStateSelected(false);
            }
        } else if (mode == EditGraphLayout.EDITION_MODE) {
        }*/

    }

    public boolean onDownAction(MotionEvent e) {
        EditGraphLayout parentView = ((EditGraphLayout) getParent());
        onDownRun(e);
        return true;
    }

    public void onLongPressAction(MotionEvent e) {
        final EditGraphLayout parentView = ((EditGraphLayout) getParent());
        if (selectBlue) {
            parentView.setOnStateSelected(false);
            parentView.setStateSelect(null);
            onSelectBlue();
        }
        if (!select) {
            onSelect();
        }

        /*if (parentView.onDefineInitialState()) {
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
        }*/
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE );
        final LinearLayout dialogState = (LinearLayout) inflater.inflate(R.layout.dialog_state,
                null);
        final EditText stateName = (EditText) dialogState.findViewById(R.id.stateName);
        final CheckBox checkBoxInitial = (CheckBox) dialogState.findViewById(R.id.chkInitial);
        final CheckBox checkBoxFinal = (CheckBox) dialogState.findViewById(R.id.chkFinal);
        final CheckBox checkBoxMove = (CheckBox) dialogState.findViewById(R.id.chkMove);
        VertexView initialState = parentView.getInitialState();
        final boolean finalIsDefinied = !(initialState == null || initialState.equals(this));
        stateName.setText(label);
        stateName.setEnabled(true);
        stateName.setSelection(0, stateName.length());

        checkBoxInitial.setChecked(false);
        if (isInitialState()) {
            checkBoxInitial.setChecked(true);
        }
        checkBoxInitial.setEnabled(true);
        checkBoxFinal.setChecked(false);
        if (isFinalState()) {
            checkBoxFinal.setChecked(true);
        }
        checkBoxFinal.setEnabled(true);
        checkBoxMove.setChecked(false);
        checkBoxMove.setEnabled(true);
        final boolean[] confirmDialogCancel = { false };
        stateName.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d("EDITOR", ((TextView) v).length() + ",");
                if (((TextView) v).length() > MAX_LENGHT_LABEL) {
                    Toast.makeText(getContext(), "Aviso! Tamanho máximo para nome de estado são " +
                                    MAX_LENGHT_LABEL + " caracteres!",
                            Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        stateName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d("EDITOR", v.getText().length() + "," + v.length());
                if (v.length() > MAX_LENGHT_LABEL) {
                    Toast.makeText(getContext(), "Aviso! Tamanho máximo para nome de estado são " +
                                    MAX_LENGHT_LABEL + " caracteres!",
                            Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (!confirmDialogCancel[0]) {
                    VertexView.this.onSelect();
                }
            }
        });
        builder.setView(dialogState)
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog f = (Dialog) dialog;
                        String newLabel = stateName.getText().toString();
                        if (newLabel.length() > MAX_LENGHT_LABEL) {
                            Toast.makeText(getContext(), "Erro! Tamanho máximo para nome de estado são " +
                                            MAX_LENGHT_LABEL + " caracteres!",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (parentView.isDefiniedLabel(newLabel)) {
                            Toast.makeText(getContext(), "Erro! Máquina já possui estado com esse " +
                                    "nome!",
                                    Toast.LENGTH_LONG)
                                    .show();
                            return;
                        }
//                        if (finalIsDefinied && checkBoxInitial.isChecked()) {
//                            Toast.makeText(getContext(), "Erro! Máquina já possui estado inicial!",
//                                    Toast.LENGTH_SHORT)
//                                    .show();
//                            return;
//                        }
                        label = newLabel;
                        if (checkBoxInitial.isChecked() && !VertexView.this.equals(
                                parentView.getInitialState())) {
                            parentView.setInitialState(VertexView.this);
                        }
                        if (!checkBoxInitial.isChecked() && VertexView.this.equals(
                                parentView.getInitialState())) {
                            parentView.removeInitialState();
                        }
                        if (checkBoxFinal.isChecked()) {
                            parentView.setVertexViewAsFinal(VertexView.this.getGridPoint());
                        }
                        if (!checkBoxFinal.isChecked() &&
                                VertexView.this.isFinalState()) {
                            VertexView.this.setFinalState(false);
                        }
                        if (checkBoxMove.isChecked()) {
                            if (parentView.isOnMove()) {
                                parentView.onMove.onSelect();
                            }
                            parentView.setOnMove(VertexView.this);
                            Toast.makeText(getContext(), "Indique para onde " +
                                    VertexView.this.label + " deve ser movido.",
                                    Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            VertexView.this.onSelect();
                        }
                        VertexView.this.invalidate();
                        confirmDialogCancel[0] = true;
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        //  Your code when user clicked on Cancel
                    }
                })
                .create()
                .show();
        Log.d(label + " - onLongPress", label + " - onLongPress" +
                " (" + e.getX() + ", " + e.getY() + ")");
    }

    public boolean onDoubleTapAction(MotionEvent e) {
        EditGraphLayout parentView = ((EditGraphLayout) getParent());
        if (selectBlue) {
            parentView.setOnStateSelected(false);
            parentView.setStateSelect(null);
            onSelectBlue();
        }
        parentView.removeVertexView(gridPoint);
        /*int mode = parentView.getMode();
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
        }*/

        Log.d(label + " - Double Tap", "Tapped at: (" + e.getX() + "," + e.getY() + ")");

        return true;
    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return onDownAction(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }



//        @Override
//        public boolean onContextClick(MotionEvent e) {
//            Log.d(label + " - onContextClick", label + " - onContextClick" +
//                    " (" + e.getX() + ", " + e.getY() + ")");
//            return true;
//        }

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