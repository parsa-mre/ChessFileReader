import java.util.ArrayList;
import java.util.Arrays;

public class Board {

    public ArrayList<Move> moves;

    public char[][] finalPositions = new char[8][8];

    public Board() {
        moves = new ArrayList<>();
        resetBoard();
    }

    public Board(ArrayList<Move> moves) {
        this.moves = moves;
        resetBoard();
    }

    public ArrayList<Move> capturedPieces() {
        ArrayList<Move> result = new ArrayList<>();
        for (Move move: moves) {
            if (move.capturedPiece != null) {
                result.add(move);
            }
        }
        return result;
    }

    public void doMove(Move move) {
        // add move to move stack
        moves.add(move);

        if (move.kingSideCastle) {

            if (move.movingPiece == Piece.WHITE_KING) {

                finalPositions[7][4] = ' ';
                finalPositions[7][6] = 'K';
                finalPositions[7][7] = ' ';
                finalPositions[7][5] = 'R';

            } else {
                finalPositions[0][4] = ' ';
                finalPositions[0][6] = 'k';
                finalPositions[0][7] = ' ';
                finalPositions[0][5] = 'r';
            }
            return;
        }

        if (move.queenSideCastle) {
            if (move.movingPiece == Piece.WHITE_KING) {
                finalPositions[7][4] = ' ';
                finalPositions[7][0] = ' ';
                finalPositions[7][2] = 'K';
                finalPositions[7][3] = 'R';
            } else {
                finalPositions[0][4] = ' ';
                finalPositions[0][0] = ' ';
                finalPositions[0][2] = 'k';
                finalPositions[0][3] = 'r';
            }
            return;
        }


        // remove the captured piece if there is one
        if (move.capturedPiece != Piece.NONE)
            finalPositions[Square.toX.get(move.capturedSquare)][Square.toY.get(move.capturedSquare)] = ' ';

        // move piece from "from" to "to"
        finalPositions[Square.toX.get(move.to)][Square.toY.get(move.to)] = finalPositions[Square.toX.get(move.from)][Square.toY.get(move.from)];
        finalPositions[Square.toX.get(move.from)][Square.toY.get(move.from)] = ' ';

        // add promotion piece if there is one
        if (move.promotionPiece != Piece.NONE)
            finalPositions[Square.toX.get(move.promotionSquare)][Square.toY.get(move.promotionSquare)] = Piece.toNotation.get(move.promotionPiece);
    }

    public void undoMove() {
        // pop last move from move stack
        Move lastMove = moves.remove(moves.size() - 1);

        if (lastMove.kingSideCastle) {
            if (lastMove.movingPiece == Piece.WHITE_KING) {

                finalPositions[7][4] = 'K';
                finalPositions[7][6] = ' ';
                finalPositions[7][7] = 'R';
                finalPositions[7][5] = ' ';

            } else {
                finalPositions[0][4] = 'k';
                finalPositions[0][6] = ' ';
                finalPositions[0][7] = 'r';
                finalPositions[0][5] = ' ';
            }

            return;
        }

        if (lastMove.queenSideCastle) {
            if (lastMove.movingPiece == Piece.WHITE_KING) {
                finalPositions[7][4] = 'K';
                finalPositions[7][0] = 'R';
                finalPositions[7][2] = ' ';
                finalPositions[7][3] = ' ';
            } else {
                finalPositions[0][4] = 'k';
                finalPositions[0][0] = 'r';
                finalPositions[0][2] = ' ';
                finalPositions[0][3] = ' ';
            }

            return;
        }


        // get the information about last move
        Square to = lastMove.to, from = lastMove.from;

        // undoing the move
        finalPositions[Square.toX.get(from)][Square.toY.get(from)] = finalPositions[Square.toX.get(to)][Square.toY.get(to)];
        finalPositions[Square.toX.get(to)][Square.toY.get(to)] = ' ';

        // removing promotion if there is one
        if (lastMove.promotionSquare != Square.NONE) {
            finalPositions[Square.toX.get(lastMove.promotionSquare)][Square.toY.get(lastMove.promotionSquare)] = ' ';
            finalPositions[Square.toX.get(from)][Square.toY.get(from)] = Piece.toNotation.get(lastMove.movingPiece);
        }

        // returning captured piece if there is one
        if (lastMove.capturedPiece != Piece.NONE)
            finalPositions[Square.toX.get(lastMove.capturedSquare)][Square.toY.get(lastMove.capturedSquare)] = Piece.toNotation.get(lastMove.capturedPiece);
    }

    void resetBoard() {
        finalPositions = new char[][]
                {{'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r' },
                {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p' },
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
                {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P' },
                {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R' }};
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                res.append(finalPositions[i][j]);
            }
            res.append("\n");
        }
        return res.toString();
    }
}
