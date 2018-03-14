package com.ufla.lfapp.views.graph;

/**
 * Created by carlos on 11/11/17.
 * <p>
 * Representa as máscaras de bits que definem os 16 tipos de bordas que podem ser utilizados para
 * desenhar um grid.
 */

public class BorderVertexMask {

    //                                                      // ---- LTRB
    public static final byte WITHOUT_MASK;                  // 0000 0000
    public static final byte LEFT_MASK;                     // 0000 1000
    public static final byte TOP_MASK;                      // 0000 0100
    public static final byte RIGHT_MASK;                    // 0000 0010
    public static final byte BOTTOM_MASK;                   // 0000 0001
    public static final byte LEFT_TOP_MASK;                 // 0000 1100
    public static final byte LEFT_RIGHT_MASK;               // 0000 1010
    public static final byte LEFT_BOTTOM_MASK;              // 0000 1001
    public static final byte TOP_RIGHT_MASK;                // 0000 0110
    public static final byte TOP_BOTTOM_MASK;               // 0000 0101
    public static final byte RIGHT_BOTTOM_MASK;             // 0000 0011
    public static final byte LEFT_TOP_RIGHT_MASK;           // 0000 1110
    public static final byte LEFT_TOP_BOTTOM_MASK;          // 0000 1101
    public static final byte LEFT_RIGHT_BOTTOM_MASK;        // 0000 1011
    public static final byte TOP_RIGHT_BOTTOM_MASK;         // 0000 0111
    public static final byte LEFT_TOP_RIGHT_BOTTOM_MASK;    // 0000 1111

    static {                                                            // ---- LTRB
        WITHOUT_MASK = 0;                                               // 0000 0000
        LEFT_MASK = 8;                                                  // 0000 1000
        TOP_MASK = 4;                                                   // 0000 0100
        RIGHT_MASK = 2;                                                 // 0000 0010
        BOTTOM_MASK = 1;                                                // 0000 0010
        LEFT_TOP_MASK = (byte)
                (LEFT_MASK | TOP_MASK);                                 // 0000 0001
        LEFT_RIGHT_MASK = (byte)
                (LEFT_MASK | RIGHT_MASK);                               // 0000 1100
        LEFT_BOTTOM_MASK = (byte)
                (LEFT_MASK | BOTTOM_MASK);                              // 0000 1010
        TOP_RIGHT_MASK = (byte)
                (TOP_MASK | RIGHT_MASK);                                // 0000 0110
        TOP_BOTTOM_MASK = (byte)
                (TOP_MASK | BOTTOM_MASK);                               // 0000 0101
        RIGHT_BOTTOM_MASK = (byte)
                (RIGHT_MASK | BOTTOM_MASK);                             // 0000 0011
        LEFT_TOP_RIGHT_MASK = (byte)
                (LEFT_MASK | TOP_MASK | RIGHT_MASK);                    // 0000 1110
        LEFT_TOP_BOTTOM_MASK = (byte)
                (LEFT_MASK | TOP_MASK | BOTTOM_MASK);                   // 0000 1101
        LEFT_RIGHT_BOTTOM_MASK = (byte)
                (LEFT_MASK | RIGHT_MASK | BOTTOM_MASK);                 // 0000 1011
        TOP_RIGHT_BOTTOM_MASK = (byte)
                (TOP_MASK | RIGHT_MASK | BOTTOM_MASK);                  // 0000 0111
        LEFT_TOP_RIGHT_BOTTOM_MASK = (byte)
                (LEFT_MASK | TOP_MASK | RIGHT_MASK | BOTTOM_MASK);      // 0000 1111
    }

    /**
     * Verifica se a borda contém o lado esquerdo.
     *
     * @param mask
     *         máscara de bits da borda
     *
     * @return true se a borda contém o lado esquerdo, caso contrário false.
     */
    public static boolean isLeft(byte mask) {
        return (mask & LEFT_MASK) > 0;
    }

    /**
     * Define a borda esquerdo de acordo com o parâmetro recibido em uma determinada máscara
     * recibida por parâmetro. A máscara resultante é retornada.
     *
     * @param mask
     *         máscara em que a operação será realizada.
     * @param left
     *         lado esquerdo que será definido na borda
     *
     * @return máscara da borda resultante
     */
    public static byte setLeft(byte mask, boolean left) {
        if (isLeft(mask)) {
            return left ? mask : (byte) (mask ^ LEFT_MASK);
        }
        return left ? (byte) (mask | LEFT_MASK) : mask;
    }

    /**
     * Verifica se a borda contém o lado superior.
     *
     * @param mask
     *         máscara de bits da borda
     *
     * @return true se a borda contém o lado superior, caso contrário false.
     */
    public static boolean isTop(byte mask) {
        return (mask & TOP_MASK) > 0;
    }

    /**
     * Define a borda superior de acordo com o parâmetro recibido em uma determinada máscara
     * recibida por parâmetro. A máscara resultante é retornada.
     *
     * @param mask
     *         máscara em que a operação será realizada.
     * @param top
     *         lado superior que será definido na borda
     *
     * @return máscara da borda resultante
     */
    public static byte setTop(byte mask, boolean top) {
        if (isTop(mask)) {
            return top ? mask : (byte) (mask ^ TOP_MASK);
        }
        return top ? (byte) (mask | TOP_MASK) : mask;
    }

    /**
     * Verifica se a borda contém o lado direito.
     *
     * @param mask
     *         máscara de bits da borda
     *
     * @return true se a borda contém o lado direito, caso contrário false.
     */
    public static boolean isRight(byte mask) {
        return (mask & RIGHT_MASK) > 0;
    }

    /**
     * Define a borda do lado direito de acordo com o parâmetro recibido em uma determinada máscara
     * recibida por parâmetro. A máscara resultante é retornada.
     *
     * @param mask
     *         máscara em que a operação será realizada.
     * @param right
     *         lado direito que será definido na borda
     *
     * @return máscara da borda resultante
     */
    public static byte setRight(byte mask, boolean right) {
        if (isRight(mask)) {
            return right ? mask : (byte) (mask ^ RIGHT_MASK);
        }
        return right ? (byte) (mask | RIGHT_MASK) : mask;
    }

    /**
     * Verifica se a borda contém o lado inferior.
     *
     * @param mask
     *         máscara de bits da borda
     *
     * @return true se a borda contém o lado inferior, caso contrário false.
     */
    public static boolean isBottom(byte mask) {
        return (mask & BOTTOM_MASK) > 0;
    }

    /**
     * Define a borda inferior de acordo com o parâmetro recibido em uma determinada máscara
     * recibida por parâmetro. A máscara resultante é retornada.
     *
     * @param mask
     *         máscara em que a operação será realizada.
     * @param bottom
     *         lado inferior que será definido na borda
     *
     * @return máscara da borda resultante
     */
    public static byte setBottom(byte mask, boolean bottom) {
        if (isBottom(mask)) {
            return bottom ? mask : (byte) (mask ^ BOTTOM_MASK);
        }
        return bottom ? (byte) (mask | BOTTOM_MASK) : mask;
    }

}
