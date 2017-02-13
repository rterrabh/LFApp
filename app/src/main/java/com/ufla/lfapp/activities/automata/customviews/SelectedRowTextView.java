package com.ufla.lfapp.activities.automata.customviews;

/**
 * Created by carlos on 2/7/17.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Representa um TextView com opção de desenhar um quadrado cheio à esquerda do texto.
 * Esse quadrado cheio é usado para indicar a seleção do objeto.
 */
public class SelectedRowTextView extends TextView {

    /**
     * Configurações de pintura do quadrado de seleção
     */
    private Paint paintSquare;
    /**
     * Armazena se objeto está em estado de seleção ou não
     */
    private boolean select;

    public SelectedRowTextView(Context context) {
        super(context);
        init();
    }

    public SelectedRowTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SelectedRowTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Inicializa as variáveis da classe com valores padrões.
     */
    public void init() {
        setSelect(false);
        definePaintSquare();
    }

    /**
     * Define paintSquare com valores padrões.
     */
    public void definePaintSquare() {
        paintSquare = new Paint();
        paintSquare.setColor(Color.BLACK);
        paintSquare.setStyle(Paint.Style.FILL);
    }

    /**
     * Verifica se objeto está selecionado.
     * @return se está selecionado retorna <code>true</code>, caso contrário <code>false</code>
     */
    public boolean isSelect() {
        return select;
    }

    /**
     * Configura objeto como selecionado ou não.
     * @param select booleano para indicar se objeto está selecionado ou não
     */
    public void setSelect(boolean select) {
        this.select = select;
        invalidate();
    }

    /**
     * Chama o desenho da superclasse (TextView) e se objeto está selecionado desenha o quadrado
     * cheio que indica que o objeto está selecionado.
     * @param canvas objeto que realiza os desenhos
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isSelect()) {
            int height = (int) (getHeight() * 0.6f);
            int desloc = (int) (getHeight() * 0.2f);
            canvas.drawRect(0, desloc, height, height + desloc, paintSquare);
            Paint textPaint = getPaint();
            int newWidth = (int) textPaint.measureText(getText().toString());
            newWidth += height * 3;
            setWidth(newWidth);
        }
    }
}