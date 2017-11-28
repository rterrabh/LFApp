package com.ufla.lfapp.views.graph;

import java.util.HashMap;
import java.util.Map;

import static com.ufla.lfapp.views.graph.BorderVertexMask.*;

/**
 * Created by carlos on 11/11/17.
 * <p>
 * Representa os 16 tipos de bordas que podem ser utilizadas em uma célula de um grid.
 * Os 16 tipos de bordas são representados de forma imutável, e suas operações estão mapeadas de
 * forma a retorna a borda resultante sem ser necessário modificar uma borda. Isso foi realizado
 * para economizar memória na representação das bordas.
 *
 * O mapeamento gasta cerca de 8B pela referência, 224B pela estrutura principal, e 448B pelas
 * 16 entradas, portanto cada map gasta 680B. Os 4 maps gastam 2720B. O enum gasta 136B. Portanto,
 * esta estrutura gasta 2856B. Com isso conclui que para um grid com mais de 2856 células esta
 * estrutura teria um consumo de memória mais eficiente.
 */

public enum BorderVertexEnum {

    WITHOUT(WITHOUT_MASK),
    LEFT(LEFT_MASK),
    TOP(TOP_MASK),
    RIGHT(RIGHT_MASK),
    BOTTOM(BOTTOM_MASK),
    LEFT_TOP(LEFT_TOP_MASK),
    LEFT_RIGHT(LEFT_RIGHT_MASK),
    LEFT_BOTTOM(LEFT_BOTTOM_MASK),
    TOP_RIGHT(TOP_RIGHT_MASK),
    TOP_BOTTOM(TOP_BOTTOM_MASK),
    RIGHT_BOTTOM(RIGHT_BOTTOM_MASK),
    LEFT_TOP_RIGHT(LEFT_TOP_RIGHT_MASK),
    LEFT_TOP_BOTTOM(LEFT_TOP_BOTTOM_MASK),
    LEFT_RIGHT_BOTTOM(LEFT_RIGHT_BOTTOM_MASK),
    TOP_RIGHT_BOTTOM(TOP_RIGHT_BOTTOM_MASK),
    LEFT_TOP_RIGHT_BOTTOM(LEFT_TOP_RIGHT_BOTTOM_MASK);

    /**
     * Contém a máscara desta borda.
     */
    private byte mask;
    /**
     * Mapeamento das operações realizadas na parte esquerda de uma borda.
     */
    private static Map<BorderVertexEnum, BorderVertexEnum> transformationsLeft;
    /**
     * Mapeamento das operações realizadas na parte superior de uma borda.
     */
    private static Map<BorderVertexEnum, BorderVertexEnum> transformationsTop;
    /**
     * Mapeamento das operações realizadas na parte direita de uma borda.
     */
    private static Map<BorderVertexEnum, BorderVertexEnum> transformationsRight;
    /**
     * Mapeamento das operações realizadas na parte inferior de uma borda.
     */
    private static Map<BorderVertexEnum, BorderVertexEnum> transformationsBottom;

    /**
     * Cria os mapeamentos das operações que podem ser realizadas em uma borda.
     */
    static {
        createTransformationsLeft();
        createTransformationsTop();
        createTransformationsRight();
        createTransformationsBottom();
    }

    /**
     * Cria o mapeamento das operações realizadas na parte esquerda de uma borda.
     */
    private static void createTransformationsLeft() {
        transformationsLeft = new HashMap<>(BorderVertexEnum.values().length);
        transformationsLeft.put(WITHOUT, LEFT);
        transformationsLeft.put(LEFT, WITHOUT);
        transformationsLeft.put(TOP, LEFT_TOP);
        transformationsLeft.put(RIGHT, LEFT_RIGHT);
        transformationsLeft.put(BOTTOM, LEFT_BOTTOM);
        transformationsLeft.put(LEFT_TOP, TOP);
        transformationsLeft.put(LEFT_RIGHT, RIGHT);
        transformationsLeft.put(LEFT_BOTTOM, BOTTOM);
        transformationsLeft.put(TOP_RIGHT, LEFT_TOP_RIGHT);
        transformationsLeft.put(TOP_BOTTOM, LEFT_TOP_BOTTOM);
        transformationsLeft.put(RIGHT_BOTTOM, LEFT_RIGHT_BOTTOM);
        transformationsLeft.put(LEFT_TOP_RIGHT, TOP_RIGHT);
        transformationsLeft.put(LEFT_TOP_BOTTOM, TOP_BOTTOM);
        transformationsLeft.put(LEFT_RIGHT_BOTTOM, RIGHT_BOTTOM);
        transformationsLeft.put(TOP_RIGHT_BOTTOM, LEFT_TOP_RIGHT_BOTTOM);
        transformationsLeft.put(LEFT_TOP_RIGHT_BOTTOM, TOP_RIGHT_BOTTOM);
    }

    /**
     * Cria o mapeamento das operações realizadas na parte superior de uma borda.
     */
    private static void createTransformationsTop() {
        transformationsTop = new HashMap<>(BorderVertexEnum.values().length);
        transformationsTop.put(WITHOUT, TOP);
        transformationsTop.put(LEFT, LEFT_TOP);
        transformationsTop.put(TOP, WITHOUT);
        transformationsTop.put(RIGHT, TOP_RIGHT);
        transformationsTop.put(BOTTOM, TOP_BOTTOM);
        transformationsTop.put(LEFT_TOP, LEFT);
        transformationsTop.put(LEFT_RIGHT, LEFT_TOP_RIGHT);
        transformationsTop.put(LEFT_BOTTOM, LEFT_TOP_BOTTOM);
        transformationsTop.put(TOP_RIGHT, RIGHT);
        transformationsTop.put(TOP_BOTTOM, BOTTOM);
        transformationsTop.put(RIGHT_BOTTOM, TOP_RIGHT_BOTTOM);
        transformationsTop.put(LEFT_TOP_RIGHT, LEFT_RIGHT);
        transformationsTop.put(LEFT_TOP_BOTTOM, LEFT_BOTTOM);
        transformationsTop.put(LEFT_RIGHT_BOTTOM, LEFT_TOP_RIGHT_BOTTOM);
        transformationsTop.put(TOP_RIGHT_BOTTOM, RIGHT_BOTTOM);
        transformationsTop.put(LEFT_TOP_RIGHT_BOTTOM, LEFT_RIGHT_BOTTOM);
    }

    /**
     * Cria o mapeamento das operações realizadas na parte direita de uma borda.
     */
    private static void createTransformationsRight() {
        transformationsRight = new HashMap<>(BorderVertexEnum.values().length);
        transformationsRight.put(WITHOUT, RIGHT);
        transformationsRight.put(LEFT, LEFT_RIGHT);
        transformationsRight.put(TOP, TOP_RIGHT);
        transformationsRight.put(RIGHT, WITHOUT);
        transformationsRight.put(BOTTOM, RIGHT_BOTTOM);
        transformationsRight.put(LEFT_TOP, LEFT_TOP_RIGHT);
        transformationsRight.put(LEFT_RIGHT, LEFT);
        transformationsRight.put(LEFT_BOTTOM, LEFT_RIGHT_BOTTOM);
        transformationsRight.put(TOP_RIGHT, TOP);
        transformationsRight.put(TOP_BOTTOM, TOP_RIGHT_BOTTOM);
        transformationsRight.put(RIGHT_BOTTOM, BOTTOM);
        transformationsRight.put(LEFT_TOP_RIGHT, LEFT_TOP);
        transformationsRight.put(LEFT_TOP_BOTTOM, LEFT_TOP_RIGHT_BOTTOM);
        transformationsRight.put(LEFT_RIGHT_BOTTOM, LEFT_BOTTOM);
        transformationsRight.put(TOP_RIGHT_BOTTOM, TOP_BOTTOM);
        transformationsRight.put(LEFT_TOP_RIGHT_BOTTOM, LEFT_TOP_BOTTOM);
    }

    /**
     * Cria o mapeamento das operações realizadas na parte inferior de uma borda.
     */
    private static void createTransformationsBottom() {
        transformationsRight = new HashMap<>(BorderVertexEnum.values().length);
        transformationsRight.put(WITHOUT, BOTTOM);
        transformationsRight.put(LEFT, LEFT_BOTTOM);
        transformationsRight.put(TOP, TOP_BOTTOM);
        transformationsRight.put(RIGHT, RIGHT_BOTTOM);
        transformationsRight.put(BOTTOM, WITHOUT);
        transformationsRight.put(LEFT_TOP, LEFT_TOP_BOTTOM);
        transformationsRight.put(LEFT_RIGHT, LEFT_RIGHT_BOTTOM);
        transformationsRight.put(LEFT_BOTTOM, LEFT);
        transformationsRight.put(TOP_RIGHT, TOP_RIGHT_BOTTOM);
        transformationsRight.put(TOP_BOTTOM, TOP);
        transformationsRight.put(RIGHT_BOTTOM, RIGHT);
        transformationsRight.put(LEFT_TOP_RIGHT, LEFT_TOP_RIGHT_BOTTOM);
        transformationsRight.put(LEFT_TOP_BOTTOM, LEFT_TOP);
        transformationsRight.put(LEFT_RIGHT_BOTTOM, LEFT_RIGHT);
        transformationsRight.put(TOP_RIGHT_BOTTOM, TOP_RIGHT);
        transformationsRight.put(LEFT_TOP_RIGHT_BOTTOM, LEFT_TOP_RIGHT);
    }

    BorderVertexEnum(byte mask) {
        this.mask = mask;
    }

    /**
     * Verifica se a borda contém o lado esquerdo.
     *
     * @return true se a borda contém o lado esquerdo, caso contrário false.
     */
    public boolean isLeft() {
        return (this.mask & LEFT_MASK) > 0;
    }

    /**
     * Aplica uma operação de modificação do lado esquerdo da borda.
     * Porém, as bordas estão enumeradas de forma imutável, portanto a borda não será modificada
     * e sim será retornada a borda que representa o resultado da modificação.
     *
     * @param left representação do lado esquerdo da borda
     * @return borda resultante da operação
     */
    public BorderVertexEnum changeLeft(boolean left) {
        if (isLeft() ^ left) {
            return transformationsLeft.get(this);
        }
        return this;
    }

    /**
     * Verifica se a borda contém o lado superior.
     *
     * @return true se a borda contém o lado superior, caso contrário false.
     */
    public boolean isTop() {
        return (this.mask & TOP_MASK) > 0;
    }

    /**
     * Aplica uma operação de modificação do lado superior da borda.
     * Porém, as bordas estão enumeradas de forma imutável, portanto a borda não será modificada
     * e sim será retornada a borda que representa o resultado da modificação.
     *
     * @param top representação do lado superior da borda
     * @return borda resultante da operação
     */
    public BorderVertexEnum changeTop(boolean top) {
        if (isTop() ^ top) {
            return transformationsTop.get(this);
        }
        return this;
    }

    /**
     * Verifica se a borda contém o lado direito.
     *
     * @return true se a borda contém o lado direito, caso contrário false.
     */
    public boolean isRight() {
        return (this.mask & RIGHT_MASK) > 0;
    }

    /**
     * Aplica uma operação de modificação do lado direito da borda.
     * Porém, as bordas estão enumeradas de forma imutável, portanto a borda não será modificada
     * e sim será retornada a borda que representa o resultado da modificação.
     *
     * @param right representação do lado direito da borda
     * @return borda resultante da operação
     */
    public BorderVertexEnum changeRight(boolean right) {
        if (isRight() ^ right) {
            return transformationsRight.get(this);
        }
        return this;
    }

    /**
     * Verifica se a borda contém o lado inferior.
     *
     * @return true se a borda contém o lado inferior, caso contrário false.
     */
    public boolean isBottom() {
        return (this.mask & BOTTOM_MASK) > 0;
    }

    /**
     * Aplica uma operação de modificação do lado inferior da borda.
     * Porém, as bordas estão enumeradas de forma imutável, portanto a borda não será modificada
     * e sim será retornada a borda que representa o resultado da modificação.
     *
     * @param bottom representação do lado inferior da borda
     * @return borda resultante da operação
     */
    public BorderVertexEnum changeBottom(boolean bottom) {
        if (isBottom() ^ bottom) {
            return transformationsBottom.get(this);
        }
        return this;
    }

}
