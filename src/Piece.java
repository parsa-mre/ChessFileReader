import java.util.EnumMap;
import java.util.HashMap;

public enum Piece {

    WHITE_PAWN,
    /**
     * White knight piece.
     */
    WHITE_KNIGHT,
    /**
     * White bishop piece.
     */
    WHITE_BISHOP,
    /**
     * White rook piece.
     */
    WHITE_ROOK,
    /**
     * White queen piece.
     */
    WHITE_QUEEN,
    /**
     * White king piece.
     */
    WHITE_KING,
    /**
     * Black pawn piece.
     */
    BLACK_PAWN,
    /**
     * Black knight piece.
     */
    BLACK_KNIGHT,
    /**
     * Black bishop piece.
     */
    BLACK_BISHOP,
    /**
     * Black rook piece.
     */
    BLACK_ROOK,
    /**
     * Black queen piece.
     */
    BLACK_QUEEN,
    /**
     * Black king piece.
     */
    BLACK_KING,
    /**
     * None piece.
     */
    NONE;

    static EnumMap<Piece, Character> toNotation = new EnumMap<Piece, Character>(Piece.class);
    static EnumMap<Piece, String> imgPath = new EnumMap<Piece, String>(Piece.class);
    static HashMap<Character, Piece> fromNotation = new HashMap<>();
    static HashMap<Piece, String> uiNotation = new HashMap<>();


    static {
        toNotation.put(Piece.WHITE_PAWN, 'P');
        toNotation.put(Piece.BLACK_PAWN, 'p');
        toNotation.put(Piece.WHITE_KNIGHT, 'N');
        toNotation.put(Piece.BLACK_KNIGHT, 'n');
        toNotation.put(Piece.WHITE_BISHOP, 'B');
        toNotation.put(Piece.BLACK_BISHOP, 'b');
        toNotation.put(Piece.WHITE_ROOK, 'R');
        toNotation.put(Piece.BLACK_ROOK, 'r');
        toNotation.put(Piece.WHITE_QUEEN, 'Q');
        toNotation.put(Piece.BLACK_QUEEN, 'q');
        toNotation.put(Piece.WHITE_KING, 'K');
        toNotation.put(Piece.BLACK_KING, 'k');
        toNotation.put(Piece.NONE, ' ');


        fromNotation.put('P', Piece.WHITE_PAWN);
        fromNotation.put('p', Piece.BLACK_PAWN);
        fromNotation.put('N', Piece.WHITE_KNIGHT);
        fromNotation.put('n', Piece.BLACK_KNIGHT);
        fromNotation.put('B', Piece.WHITE_BISHOP);
        fromNotation.put('b', Piece.BLACK_BISHOP);
        fromNotation.put('R', Piece.WHITE_ROOK);
        fromNotation.put('r', Piece.BLACK_ROOK);
        fromNotation.put('Q', Piece.WHITE_QUEEN);
        fromNotation.put('q', Piece.BLACK_QUEEN);
        fromNotation.put('K', Piece.WHITE_KING);
        fromNotation.put('k', Piece.BLACK_KING);
        fromNotation.put(' ', Piece.NONE);


        imgPath.put(Piece.WHITE_PAWN, "assets/wp.png");
        imgPath.put(Piece.BLACK_PAWN, "assets/bp.png");
        imgPath.put(Piece.WHITE_KNIGHT, "assets/wn.png");
        imgPath.put(Piece.BLACK_KNIGHT, "assets/bn.png");
        imgPath.put(Piece.WHITE_BISHOP, "assets/wb.png");
        imgPath.put(Piece.BLACK_BISHOP, "assets/bb.png");
        imgPath.put(Piece.WHITE_ROOK, "assets/wr.png");
        imgPath.put(Piece.BLACK_ROOK, "assets/br.png");
        imgPath.put(Piece.WHITE_QUEEN, "assets/wq.png");
        imgPath.put(Piece.BLACK_QUEEN, "assets/bq.png");
        imgPath.put(Piece.WHITE_KING, "assets/wk.png");
        imgPath.put(Piece.BLACK_KING, "assets/bk.png");
        imgPath.put(Piece.NONE, "");



        uiNotation.put(Piece.WHITE_PAWN, "♙");
        uiNotation.put(Piece.BLACK_PAWN, "♟");
        uiNotation.put(Piece.WHITE_KNIGHT, "♘");
        uiNotation.put(Piece.BLACK_KNIGHT, "♞");
        uiNotation.put(Piece.WHITE_BISHOP, "♗");
        uiNotation.put(Piece.BLACK_BISHOP, "♝");
        uiNotation.put(Piece.WHITE_ROOK, "♖");
        uiNotation.put(Piece.BLACK_ROOK, "♜");
        uiNotation.put(Piece.WHITE_QUEEN, "♕");
        uiNotation.put(Piece.BLACK_QUEEN, "♛");
        uiNotation.put(Piece.WHITE_KING, "♔");
        uiNotation.put(Piece.BLACK_KING, "♚");
        uiNotation.put(Piece.NONE, "NONE");


    }

}
