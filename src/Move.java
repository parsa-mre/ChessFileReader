import java.util.Objects;

public class Move {

    Square from;
    Square to;
    Piece movingPiece;

    Piece promotionPiece;
    Square promotionSquare;

    Piece capturedPiece;
    Square capturedSquare;

    boolean kingSideCastle = false;
    boolean queenSideCastle = false;

    Move(boolean kingSideCastle, boolean queenSideCastle, Piece movingPiece) {
        this.kingSideCastle = kingSideCastle;
        this.queenSideCastle = queenSideCastle;
        this.movingPiece = movingPiece;
    }

    Move(Square from, Square to, Piece movingPiece, Piece promotionPiece,
         Square promotionSquare, Piece capturedPiece, Square capturedSquare) {

        this.from = from;
        this.to = to;
        this.movingPiece = movingPiece;

        this.promotionPiece = promotionPiece;
        this.promotionSquare = promotionSquare;

        this.capturedPiece = capturedPiece;
        this.capturedSquare = capturedSquare;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return kingSideCastle == move.kingSideCastle && queenSideCastle == move.queenSideCastle && from == move.from && to == move.to && movingPiece == move.movingPiece && promotionPiece == move.promotionPiece && promotionSquare == move.promotionSquare && capturedPiece == move.capturedPiece && capturedSquare == move.capturedSquare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, movingPiece, promotionPiece, promotionSquare, capturedPiece, capturedSquare, kingSideCastle, queenSideCastle);
    }
}
