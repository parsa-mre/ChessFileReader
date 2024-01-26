import java.util.EnumMap;

public enum Square {

    /**
     * A 1 square.
     */
    A1,
    /**
     * B 1 square.
     */
    B1,
    /**
     * C 1 square.
     */
    C1,
    /**
     * D 1 square.
     */
    D1,
    /**
     * E 1 square.
     */
    E1,
    /**
     * F 1 square.
     */
    F1,
    /**
     * G 1 square.
     */
    G1,
    /**
     * H 1 square.
     */
    H1,
    /**
     * A 2 square.
     */
    A2,
    /**
     * B 2 square.
     */
    B2,
    /**
     * C 2 square.
     */
    C2,
    /**
     * D 2 square.
     */
    D2,
    /**
     * E 2 square.
     */
    E2,
    /**
     * F 2 square.
     */
    F2,
    /**
     * G 2 square.
     */
    G2,
    /**
     * H 2 square.
     */
    H2,
    /**
     * A 3 square.
     */
    A3,
    /**
     * B 3 square.
     */
    B3,
    /**
     * C 3 square.
     */
    C3,
    /**
     * D 3 square.
     */
    D3,
    /**
     * E 3 square.
     */
    E3,
    /**
     * F 3 square.
     */
    F3,
    /**
     * G 3 square.
     */
    G3,
    /**
     * H 3 square.
     */
    H3,
    /**
     * A 4 square.
     */
    A4,
    /**
     * B 4 square.
     */
    B4,
    /**
     * C 4 square.
     */
    C4,
    /**
     * D 4 square.
     */
    D4,
    /**
     * E 4 square.
     */
    E4,
    /**
     * F 4 square.
     */
    F4,
    /**
     * G 4 square.
     */
    G4,
    /**
     * H 4 square.
     */
    H4,
    /**
     * A 5 square.
     */
    A5,
    /**
     * B 5 square.
     */
    B5,
    /**
     * C 5 square.
     */
    C5,
    /**
     * D 5 square.
     */
    D5,
    /**
     * E 5 square.
     */
    E5,
    /**
     * F 5 square.
     */
    F5,
    /**
     * G 5 square.
     */
    G5,
    /**
     * H 5 square.
     */
    H5,
    /**
     * A 6 square.
     */
    A6,
    /**
     * B 6 square.
     */
    B6,
    /**
     * C 6 square.
     */
    C6,
    /**
     * D 6 square.
     */
    D6,
    /**
     * E 6 square.
     */
    E6,
    /**
     * F 6 square.
     */
    F6,
    /**
     * G 6 square.
     */
    G6,
    /**
     * H 6 square.
     */
    H6,
    /**
     * A 7 square.
     */
    A7,
    /**
     * B 7 square.
     */
    B7,
    /**
     * C 7 square.
     */
    C7,
    /**
     * D 7 square.
     */
    D7,
    /**
     * E 7 square.
     */
    E7,
    /**
     * F 7 square.
     */
    F7,
    /**
     * G 7 square.
     */
    G7,
    /**
     * H 7 square.
     */
    H7,
    /**
     * A 8 square.
     */
    A8,
    /**
     * B 8 square.
     */
    B8,
    /**
     * C 8 square.
     */
    C8,
    /**
     * D 8 square.
     */
    D8,
    /**
     * E 8 square.
     */
    E8,
    /**
     * F 8 square.
     */
    F8,
    /**
     * G 8 square.
     */
    G8,
    /**
     * H 8 square.
     */
    H8,
    /**
     * None square.
     */
    NONE;


    /**
     * getting x and y from each Square
     * used for storing pieces in array
     */
    static EnumMap<Square, Integer> toX = new EnumMap<Square, Integer>(Square.class);
    static EnumMap<Square, Integer> toY = new EnumMap<Square, Integer>(Square.class);

    static {
        toX.put(A8, 0); toX.put(B8,0); toX.put(C8, 0); toX.put(D8, 0); toX.put(E8, 0); toX.put(F8, 0); toX.put(G8, 0); toX.put(H8, 0);
        toX.put(A7, 1); toX.put(B7,1); toX.put(C7, 1); toX.put(D7, 1); toX.put(E7, 1); toX.put(F7, 1); toX.put(G7, 1); toX.put(H7, 1);
        toX.put(A6, 2); toX.put(B6,2); toX.put(C6, 2); toX.put(D6, 2); toX.put(E6, 2); toX.put(F6, 2); toX.put(G6, 2); toX.put(H6, 2);
        toX.put(A5, 3); toX.put(B5,3); toX.put(C5, 3); toX.put(D5, 3); toX.put(E5, 3); toX.put(F5, 3); toX.put(G5, 3); toX.put(H5, 3);
        toX.put(A4, 4); toX.put(B4,4); toX.put(C4, 4); toX.put(D4, 4); toX.put(E4, 4); toX.put(F4, 4); toX.put(G4, 4); toX.put(H4, 4);
        toX.put(A3, 5); toX.put(B3,5); toX.put(C3, 5); toX.put(D3, 5); toX.put(E3, 5); toX.put(F3, 5); toX.put(G3, 5); toX.put(H3, 5);
        toX.put(A2, 6); toX.put(B2,6); toX.put(C2, 6); toX.put(D2, 6); toX.put(E2, 6); toX.put(F2, 6); toX.put(G2, 6); toX.put(H2, 6);
        toX.put(A1, 7); toX.put(B1,7); toX.put(C1, 7); toX.put(D1, 7); toX.put(E1, 7); toX.put(F1, 7); toX.put(G1, 7); toX.put(H1, 7);


        toY.put(A8, 0); toY.put(B8, 1); toY.put(C8, 2); toY.put(D8, 3); toY.put(E8, 4); toY.put(F8, 5); toY.put(G8, 6); toY.put(H8, 7);
        toY.put(A7, 0); toY.put(B7, 1); toY.put(C7, 2); toY.put(D7, 3); toY.put(E7, 4); toY.put(F7, 5); toY.put(G7, 6); toY.put(H7, 7);
        toY.put(A6, 0); toY.put(B6, 1); toY.put(C6, 2); toY.put(D6, 3); toY.put(E6, 4); toY.put(F6, 5); toY.put(G6, 6); toY.put(H6, 7);
        toY.put(A5, 0); toY.put(B5, 1); toY.put(C5, 2); toY.put(D5, 3); toY.put(E5, 4); toY.put(F5, 5); toY.put(G5, 6); toY.put(H5, 7);
        toY.put(A4, 0); toY.put(B4, 1); toY.put(C4, 2); toY.put(D4, 3); toY.put(E4, 4); toY.put(F4, 5); toY.put(G4, 6); toY.put(H4, 7);
        toY.put(A3, 0); toY.put(B3, 1); toY.put(C3, 2); toY.put(D3, 3); toY.put(E3, 4); toY.put(F3, 5); toY.put(G3, 6); toY.put(H3, 7);
        toY.put(A2, 0); toY.put(B2, 1); toY.put(C2, 2); toY.put(D2, 3); toY.put(E2, 4); toY.put(F2, 5); toY.put(G2, 6); toY.put(H2, 7);
        toY.put(A1, 0); toY.put(B1, 1); toY.put(C1, 2); toY.put(D1, 3); toY.put(E1, 4); toY.put(F1, 5); toY.put(G1, 6); toY.put(H1, 7);
    }


}
