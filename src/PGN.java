import javax.swing.plaf.metal.MetalIconFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class PGN {

    public ArrayList<Game> games = new ArrayList<>();
    private Game currentGame = new Game();

    PGN() {
        games.add(new Game());
    }

    PGN(String path) {
        read(path);
    }

    /**
     *   read file
     */
    void read(String path) {
        games.clear();
        String content = fileContent(path);
        Scanner sc = new Scanner(content);

        boolean readStatus = false;
        StringBuilder currentMoves = new StringBuilder();

        while(sc.hasNextLine()) {
            String line = sc.nextLine();

            if(line.equals("")) {
                if (!readStatus)
                    readStatus = true;
                else {
                    boardMaker(currentMoves.toString());
                    games.add(currentGame);
                    currentGame = new Game();
                    readStatus = false;
                    currentMoves = new StringBuilder();
                }
            }

            else if(line.charAt(0) == '[') {
                int valueIndex = line.indexOf('"');
                String key = line.substring(1, valueIndex - 1);
                String value = line.substring(valueIndex + 1, line.length() - 2);
                currentGame.tags.put(key, value);
            } else {
                currentMoves.append(line).append(" ");
            }
        }
        boardMaker(currentMoves.toString());
        games.add(currentGame);
    }

    /**
     *   process raw moves
     */
    private void boardMaker(String moves) {
        String[] movesSplited = moves.split(" ");
        currentGame.chessNotation = movesSplited;
        for(int i = 0; i < movesSplited.length; ++i) {
            if (i % 3 == 0)
                continue;
            boolean isWhite = (i % 3) == 1;
            interpretMove(movesSplited[i], isWhite);
        }
    }

    /**
     *   process a single move
     */
    private void interpretMove(String move, boolean isWhite) {

        boolean capture = false;
        char promotion = ' ';

        // checks capture
        if(move.contains("x")) {
            int xIndex = move.indexOf("x");
            move = move.substring(0, xIndex) + move.substring(xIndex + 1, move.length());
            capture = true;
        }

        // checks promotion
        if (move.contains("=")) {
            int equalIndex = move.indexOf("=");
            promotion = move.substring(equalIndex + 1, equalIndex + 2).charAt(0);
            move = move.substring(0, equalIndex);
        }

        // remove unnecessary characters
        move = removeCharacter("=", move);
        move = removeCharacter("#", move);
        move = removeCharacter("+", move);
        move = removeCharacter("?", move);
        move = removeCharacter("!", move);
        move = removeCharacter("\n", move);
        move = removeCharacter("-", move);

        if (move.length() == 0)
            return;

        char firstChar = move.charAt(0);

        if (move.equals("10") || move.equals("01") || move.equals("1/21/2") || move.equals("*"))
            return;

        if (Character.isUpperCase(firstChar)) {

            move = move.substring(1);

            if (firstChar == 'N') {
                knightMove(move, isWhite ? Piece.WHITE_KNIGHT : Piece.BLACK_KNIGHT, capture);
            } else if (firstChar == 'B') {
                bishopMove(move, isWhite ? Piece.WHITE_BISHOP : Piece.BLACK_BISHOP, capture);
            } else if (firstChar == 'R') {
                rookMove(move, isWhite ? Piece.WHITE_ROOK : Piece.BLACK_ROOK, capture);
            } else if (firstChar == 'Q') {
                queenMove(move, isWhite ? Piece.WHITE_QUEEN : Piece.BLACK_QUEEN, capture);
            } else if (firstChar == 'K') {
                kingMove(move, isWhite ? Piece.WHITE_KING : Piece.BLACK_KING, capture);
            } else if (move.equals("OO") || move.equals("O")) {
                castleMove(move, isWhite ? Piece.WHITE_KING : Piece.BLACK_KING);
            }
        } else {
            pawnMove(move, isWhite ? Piece.WHITE_PAWN : Piece.BLACK_PAWN, capture, promotion);
        }
    }


    /**
     *   moves
     */

    private void castleMove(String move, Piece movingPiece) {
        if(move.equals("O"))
            currentGame.board.doMove(new Move(true, false, movingPiece));
        if (move.equals("OO"))
            currentGame.board.doMove(new Move(false, true, movingPiece));
    }
    private void queenMove(String move, Piece movingPiece, boolean capture) {
        if (!rookMove(move, movingPiece, capture))
            bishopMove(move, movingPiece, capture);
    }
    private void kingMove(String move, Piece movingPiece, boolean capture) {
        Square from = Square.NONE, to = Square.NONE, capturedSquare = Square.NONE;
        Piece capturedPiece = Piece.NONE;

        int[] row = new int[]{1, 0, -1, 0, 1, -1, 1, -1};
        int[] col = new int[]{0, 1, 0, -1, 1, -1, -1, 1};
        char movingChar = Piece.toNotation.get(movingPiece);

        int i = 8 - ((int)(move.charAt(1)) - 48), j = (int)(move.charAt(0) - 97);
        to = toSquare(i, j);

        for (int k = 0; k < 8; ++k) {
            int x = row[k] + i, y = col[k] + j;
            if (x > 7 || x < 0 || y > 7 || y < 0)
                continue;

            if (currentGame.board.finalPositions[x][y] == movingChar) {
                from = toSquare(x, y);
                break;
            }
        }

        if (capture) {
            capturedPiece = Piece.fromNotation.get(currentGame.board.finalPositions[Square.toX.get(to)][Square.toY.get(to)]);
            capturedSquare = to;
        }

        currentGame.board.doMove(new Move(from, to, movingPiece, Piece.NONE, Square.NONE, capturedPiece, capturedSquare));
    }
    private void pawnMove(String move, Piece movingPiece, boolean capture, char promotion) {

        Square from = Square.NONE, to = Square.NONE, capturedSquare = Square.NONE, promotedSquare = Square.NONE;
        Piece capturedPiece = Piece.NONE, promotedPiece = Piece.NONE;

        if (movingPiece == Piece.WHITE_PAWN) {

            if (move.length() == 2) {

                int i = 8 - ((int)(move.charAt(1)) - 48), j = (int)(move.charAt(0) - 97);
                to = toSquare(i, j);

                // first pawn move
                if (i == 4) {
                    if (currentGame.board.finalPositions[i+1][j] == 'P')
                        from = toSquare(i+1, j);
                    else if (currentGame.board.finalPositions[i+2][j] == 'P')
                        from = toSquare(i+2, j);
                }

                if (i == 0) {
                    if (currentGame.board.finalPositions[i+1][j] == 'P')
                        from = toSquare(i+1, j);

                    // promotion at the end of board
                    promotedPiece = Piece.fromNotation.get(promotion);
                    promotedSquare = to;
                }

                if ((i == 1 || i == 2 || i == 3 || i == 5) && currentGame.board.finalPositions[i+1][j] == 'P')
                        from = toSquare(i+1, j);
            }
            if (move.length() == 3) {
                char firstChar = move.charAt(0);
                move = move.substring(1);
                int i = 8 - ((int)(move.charAt(1)) - 48), j = (int)(move.charAt(0) - 97);
                to = toSquare(i, j);
                from = toSquare(i+1, ((int) firstChar - 97));

                if (i == 0) {
                    promotedPiece = Piece.fromNotation.get(promotion);
                    promotedSquare = to;
                }

                // if en passant
                if (currentGame.board.finalPositions[Square.toX.get(to)][Square.toY.get(to)] == ' ') {
                    capturedPiece = Piece.BLACK_PAWN;
                    capturedSquare = toSquare(i+1, j);
                }
                else {
                    capturedPiece = Piece.fromNotation.get(currentGame.board.finalPositions[Square.toX.get(to)][Square.toY.get(to)]);
                    capturedSquare = to;
                }
            }
        }
        else {

            if (move.length() == 2) {
                int i = 8 - ((int)(move.charAt(1)) - 48), j = (int)(move.charAt(0) - 97);
                to = toSquare(i, j);

                // first pawn move
                if (i == 3) {
                    if (currentGame.board.finalPositions[i-1][j] == 'p')
                        from = toSquare(i-1, j);
                    else if (currentGame.board.finalPositions[i-2][j] == 'p')
                        from = toSquare(i-2, j);
                }

                if (i == 7) {
                    if (currentGame.board.finalPositions[i-1][j] == 'p')
                        from = toSquare(i-1, j);

                    // promotion at the end of board
                    promotedPiece = Piece.fromNotation.get(Character.toLowerCase(promotion));
                    promotedSquare = to;
                }

                if ((i == 2 || i == 4 || i == 5 || i == 6) && currentGame.board.finalPositions[i-1][j] == 'p')
                        from = toSquare(i-1, j);

            }
            if (move.length() == 3) {
                char firstChar = move.charAt(0);
                move = move.substring(1);
                int i = 8 - ((int)(move.charAt(1)) - 48), j = (int)(move.charAt(0) - 97);
                to = toSquare(i, j);
                from = toSquare(i-1, ((int) firstChar - 97));

                if (i == 7) {
                    promotedPiece = Piece.fromNotation.get(Character.toLowerCase(promotion));
                    promotedSquare = to;
                }

                // if en passant
                if (currentGame.board.finalPositions[Square.toX.get(to)][Square.toY.get(to)] == ' ') {
                    capturedPiece = Piece.WHITE_PAWN;
                    capturedSquare = toSquare(i-1, j);
                }
                else {
                    capturedPiece = Piece.fromNotation.get(currentGame.board.finalPositions[Square.toX.get(to)][Square.toY.get(to)]);
                    capturedSquare = to;
                }
            }
        }

        currentGame.board.doMove(new Move(from, to, movingPiece, promotedPiece, promotedSquare, capturedPiece, capturedSquare));
    }
    private void knightMove(String move, Piece movingPiece, boolean capture) {
        Square from = Square.NONE, to = Square.NONE, capturedSquare = Square.NONE;
        Piece capturedPiece = Piece.NONE;

        boolean isWhite = movingPiece == Piece.WHITE_KNIGHT;
        int[] row = new int[]{2, 1, -1, -2, -2, -1, 1, 2};
        int[] col = new int[]{1, 2, 2, 1, -1, -2, -2, -1};

        char movingChar = Piece.toNotation.get(movingPiece);


        if (move.length() == 2) {
            int i = 8 - ((int)(move.charAt(1)) - 48), j = (int)(move.charAt(0) - 97);
            to = toSquare(i, j);

            if (capture) {
                capturedPiece = Piece.fromNotation.get(currentGame.board.finalPositions[Square.toX.get(to)][Square.toY.get(to)]);
                capturedSquare = to;
            }

            for (int k = 0; k < 8; ++k) {
                int x = row[k] + i, y = col[k] + j;
                if (x > 7 || x < 0 || y > 7 || y < 0)
                    continue;
                if (currentGame.board.finalPositions[x][y] == movingChar)
                    if (checkTest(new Move(toSquare(x, y), to, movingPiece, Piece.NONE, Square.NONE, capturedPiece, capturedSquare), isWhite)) {
                        from = toSquare(x, y);
                        break;
                    }
            }
        }

        else if (move.length() == 3) {
            char firstChar = move.charAt(0);
            move = move.substring(1);
            int i = 8 - ((int)(move.charAt(1)) - 48), j = (int)(move.charAt(0) - 97);
            to = Square.valueOf(move.toUpperCase());

            if (capture) {
                capturedPiece = Piece.fromNotation.get(currentGame.board.finalPositions[Square.toX.get(to)][Square.toY.get(to)]);
                capturedSquare = to;
            }

            if ("12345678".contains("" + firstChar)) {
                int x = 8 - ((int)(firstChar) - 48), y = 0;
                for (y = 0; y < 8; ++y) {
                    if (currentGame.board.finalPositions[x][y] == movingChar) {
                        if (checkTest(new Move(toSquare(x, y), to, movingPiece, Piece.NONE, Square.NONE, capturedPiece, capturedSquare), isWhite)) {
                            from = toSquare(x, y);
                            break;
                        }

                    }
                }
            }
            else {
                int y = (int)(firstChar - 97), x = 0;
                for (x = 0; x < 8; ++x) {
                    if (currentGame.board.finalPositions[x][y] == movingChar) {
                        if (checkTest(new Move(toSquare(x, y), to, movingPiece, Piece.NONE, Square.NONE, capturedPiece, capturedSquare), isWhite)) {
                            from = toSquare(x, y);
                            break;
                        }
                    }
                }
            }
        }

        else if (move.length() == 4) {
            String f = move.substring(0, 2);
            from = Square.valueOf(move.substring(0, 2).toUpperCase());
            to = Square.valueOf(move.substring(2, 4).toUpperCase());

            if (capture) {
                capturedPiece = Piece.fromNotation.get(currentGame.board.finalPositions[Square.toX.get(to)][Square.toY.get(to)]);
                capturedSquare = to;
            }
        }

        currentGame.board.doMove(new Move(from, to, movingPiece, Piece.NONE, Square.NONE, capturedPiece, capturedSquare));
    }
    private void bishopMove(String move, Piece movingPiece, boolean capture) {

        Square from = Square.NONE, to = Square.NONE, capturedSquare = Square.NONE;
        Piece capturedPiece = Piece.NONE;

        boolean isWhite = movingPiece == Piece.WHITE_BISHOP || movingPiece == Piece.WHITE_QUEEN;
        char movingChar = Piece.toNotation.get(movingPiece);

        if (move.length() == 2) {
            int i = 8 - ((int)(move.charAt(1)) - 48), j = (int)(move.charAt(0) - 97);
            to = toSquare(i, j);


            if (capture) {
                capturedPiece = Piece.fromNotation.get(currentGame.board.finalPositions[Square.toX.get(to)][Square.toY.get(to)]);
                capturedSquare = to;
            }


            boolean notFound = true;

            int x = i+1, y = j+1;
            while(y < 8 && x < 8) {
                if (currentGame.board.finalPositions[x][y] == movingChar) {
                    if (checkTest(new Move(toSquare(x, y), to, movingPiece, Piece.NONE, Square.NONE, capturedPiece, capturedSquare), isWhite)) {
                        from = toSquare(x, y);
                        notFound = false;
                        break;
                    }
                } else if (currentGame.board.finalPositions[x][y] != ' ')
                    break;
                ++y; ++x;
            }
            x = i-1; y = j+1;
            while(y < 8 && x >= 0 && notFound) {
                if (currentGame.board.finalPositions[x][y] == movingChar) {
                    if (checkTest(new Move(toSquare(x, y), to, movingPiece, Piece.NONE, Square.NONE, capturedPiece, capturedSquare), isWhite)) {
                        from = toSquare(x, y);
                        notFound = false;
                        break;
                    }
                } else if (currentGame.board.finalPositions[x][y] != ' ')
                    break;
                ++y; --x;
            }
            x = i-1; y = j-1;
            while(y >= 0 && x >= 0 && notFound) {
                if (currentGame.board.finalPositions[x][y] == movingChar) {
                    if (checkTest(new Move(toSquare(x, y), to, movingPiece, Piece.NONE, Square.NONE, capturedPiece, capturedSquare), isWhite)) {
                        from = toSquare(x, y);
                        notFound = false;
                        break;
                    }
                } else if (currentGame.board.finalPositions[x][y] != ' ')
                    break;
                --y; --x;
            }
            x = i+1; y = j-1;
            while(y >= 0 && x < 8 && notFound) {
                if (currentGame.board.finalPositions[x][y] == movingChar) {
                    if (checkTest(new Move(toSquare(x, y), to, movingPiece, Piece.NONE, Square.NONE, capturedPiece, capturedSquare), isWhite)) {
                        from = toSquare(x, y);
                        notFound = false;
                        break;
                    }
                } else if (currentGame.board.finalPositions[x][y] != ' ')
                    break;
                --y; ++x;
            }

        }
        else if (move.length() == 3) {
            char firstChar = move.charAt(0);
            move = move.substring(1);
            int i = 8 - ((int)(move.charAt(1)) - 48), j = (int)(move.charAt(0) - 97);
            to = Square.valueOf(move.toUpperCase());

            if (capture) {
                capturedPiece = Piece.fromNotation.get(currentGame.board.finalPositions[Square.toX.get(to)][Square.toY.get(to)]);
                capturedSquare = to;
            }

            if ("12345678".contains("" + firstChar)) {
                int x = 8 - ((int)(firstChar) - 48), y = 0;
                while(y < 8) {
                    if (currentGame.board.finalPositions[x][y] == movingChar && x - i == y - j) {
                        int p = x, q = y;
                        while (p - i == q - j && currentGame.board.finalPositions[p][q] == ' ' && (p != i && q != j)) {
                            p += Integer.signum(i - p);
                            q += Integer.signum(j - q);
                        }
                        if (p == i && q == j) {
                            if (checkTest(new Move(toSquare(x, y), to, movingPiece, Piece.NONE, Square.NONE, capturedPiece, capturedSquare), isWhite)) {
                                from = toSquare(x, y);
                            }
                        }
                        break;
                    }
                    ++y;
                }

            } else {
                int y = (int)(firstChar - 97), x = 0;
                while(x < 8) {
                    if (currentGame.board.finalPositions[x][y] == movingChar && x - i == y - j) {
                        int p = x, q = y;
                        while (p - i == q - j && currentGame.board.finalPositions[p][q] == ' ' && (p != i && q != j)) {
                            p += Integer.signum(i - p);
                            q += Integer.signum(j - q);
                        }
                        if (p == i && q == j) {
                            if (checkTest(new Move(toSquare(x, y), to, movingPiece, Piece.NONE, Square.NONE, capturedPiece, capturedSquare), isWhite))
                                from = toSquare(x, y);
                        }
                        break;
                    }
                    ++x;
                }
            }
        }
        else if (move.length() == 4) {
            String f = move.substring(0, 2);
            from = Square.valueOf(move.substring(0, 2).toUpperCase());
            to = Square.valueOf(move.substring(2, 4).toUpperCase());
            if (capture) {
                capturedPiece = Piece.fromNotation.get(currentGame.board.finalPositions[Square.toX.get(to)][Square.toY.get(to)]);
                capturedSquare = to;
            }
        }

        if (from != Square.NONE)
            currentGame.board.doMove(new Move(from, to, movingPiece, Piece.NONE, Square.NONE, capturedPiece, capturedSquare));
    }
    private boolean rookMove(String move, Piece movingPiece, boolean capture) {
        Square from = Square.NONE, to = Square.NONE, capturedSquare = Square.NONE;
        Piece capturedPiece = Piece.NONE;

        char movingChar = Piece.toNotation.get(movingPiece);
        boolean isWhite = movingPiece == Piece.WHITE_ROOK || movingPiece == Piece.WHITE_QUEEN;

        if(move.length() == 2) {
            int i = 8 - ((int)(move.charAt(1)) - 48), j = (int)(move.charAt(0) - 97);
            to = toSquare(i, j);


            if(capture) {
                capturedPiece = Piece.fromNotation.get(currentGame.board.finalPositions[Square.toX.get(to)][Square.toY.get(to)]);
                capturedSquare = to;
            }

            int x = i, y = j;
            x = x + 1;
            while(x < 8) {
                if (currentGame.board.finalPositions[x][y] == movingChar) {
                    if (checkTest(new Move(toSquare(x, y), to, movingPiece, Piece.NONE, Square.NONE, capturedPiece, capturedSquare), isWhite)) {
                        from = toSquare(x, y);
                        break;
                    }
                } else if (currentGame.board.finalPositions[x][y] != ' ')
                    break;
                ++x;
            }
            x = i - 1;
            while(x >= 0) {
                if (currentGame.board.finalPositions[x][y] == movingChar) {
                    if (checkTest(new Move(toSquare(x, y), to, movingPiece, Piece.NONE, Square.NONE, capturedPiece, capturedSquare), isWhite)) {
                        from = toSquare(x, y);
                        break;
                    }
                } else if (currentGame.board.finalPositions[x][y] != ' ')
                    break;
                --x;
            }
            x = i; y -= 1;
            while(y >= 0) {
                if (currentGame.board.finalPositions[x][y] == movingChar) {
                    if (checkTest(new Move(toSquare(x, y), to, movingPiece, Piece.NONE, Square.NONE, capturedPiece, capturedSquare), isWhite)) {
                        from = toSquare(x, y);
                        break;
                    }
                } else if (currentGame.board.finalPositions[x][y] != ' ')
                    break;
                --y;
            }
            y = j + 1;
            while(y < 8) {
                if (currentGame.board.finalPositions[x][y] == movingChar) {
                    if (checkTest(new Move(toSquare(x, y), to, movingPiece, Piece.NONE, Square.NONE, capturedPiece, capturedSquare), isWhite)) {
                        from = toSquare(x, y);
                        break;
                    }
                } else if (currentGame.board.finalPositions[x][y] != ' ')
                    break;
                ++y;
            }

        }

        else if (move.length() == 3) {

            char firstChar = move.charAt(0);
            move = move.substring(1);
            int i = 8 - ((int)(move.charAt(1)) - 48), j = (int)(move.charAt(0) - 97);
            to = Square.valueOf(move.toUpperCase());

            if(capture) {
                capturedPiece = Piece.fromNotation.get(currentGame.board.finalPositions[Square.toX.get(to)][Square.toY.get(to)]);
                capturedSquare = to;
            }


            if ("12345678".contains("" + firstChar)) {
                int x = 8 - ((int)(firstChar) - 48), y = j;
                while(y < 8) {
                    if (currentGame.board.finalPositions[x][y] == movingChar) {
                        if (checkTest(new Move(toSquare(x, y), to, movingPiece, Piece.NONE, Square.NONE, capturedPiece, capturedSquare), isWhite)) {
                            from = toSquare(x, y);
                            break;
                        }
                    } else if (currentGame.board.finalPositions[x][y] != ' ')
                        break;
                    ++y;
                }
                y = j;
                while(y >= 0) {

                    if (currentGame.board.finalPositions[x][y] == movingChar) {
                        if (checkTest(new Move(toSquare(x, y), to, movingPiece, Piece.NONE, Square.NONE, capturedPiece, capturedSquare), isWhite)) {
                            from = toSquare(x, y);
                            break;
                        }
                    } else if (currentGame.board.finalPositions[x][y] != ' ')
                        break;
                    --y;
                }
            } else {

                int y = (int)(firstChar - 97), x = i;
                while(x < 8) {
                    if (currentGame.board.finalPositions[x][y] == movingChar) {
                        if (checkTest(new Move(toSquare(x, y), to, movingPiece, Piece.NONE, Square.NONE, capturedPiece, capturedSquare), isWhite)) {
                            from = toSquare(x, y);
                            break;
                        }
                    } else if (currentGame.board.finalPositions[x][y] != ' ')
                        break;
                    ++x;
                }
                y = (int)(firstChar - 97); x = i-1;
                while(x >= 0) {
                    if (currentGame.board.finalPositions[x][y] == movingChar) {
                        if (checkTest(new Move(toSquare(x, y), to, movingPiece, Piece.NONE, Square.NONE, capturedPiece, capturedSquare), isWhite)) {
                            from = toSquare(x, y);
                            break;
                        }
                    } else if (currentGame.board.finalPositions[x][y] != ' ')
                        break;
                    --x;
                }
            }
        }

        else if (move.length() == 4) {

            String f = move.substring(0, 2);
            from = Square.valueOf(move.substring(0, 2).toUpperCase());
            to = Square.valueOf(move.substring(2, 4).toUpperCase());

            if(capture) {
                capturedPiece = Piece.fromNotation.get(currentGame.board.finalPositions[Square.toX.get(to)][Square.toY.get(to)]);
                capturedSquare = to;
            }
        }


        if (from != Square.NONE) {
            currentGame.board.doMove(new Move(from, to, movingPiece, Piece.NONE, Square.NONE, capturedPiece, capturedSquare));
            return true;
        }
        return false;
    }


    // checks if the king is in check after the move
    private boolean checkTest(Move move, boolean isWhite) {
        currentGame.board.doMove(move);

        final char king = isWhite ? 'K' : 'k';
        final char rook = isWhite ? 'r' : 'R';
        final char bishop = isWhite ? 'b' : 'B';
        final char queen = isWhite ? 'q' : 'Q';

        // finding king's location
        int i = 0, j = 0, x = 0, y = 0;
        boolean notFound = true;
        for (x = 0; x < 8 && notFound; ++x)
            for (y = 0; y < 8; ++y) {
                if (currentGame.board.finalPositions[x][y] == king) {
                    i = x; j = y;
                    notFound = false;
                    break;
                }
            }


        x = i + 1; y = j;
        while (x < 8) {
            if (currentGame.board.finalPositions[x][y] == rook || currentGame.board.finalPositions[x][y] == queen) {
                currentGame.board.undoMove();
                return false;
            }
            else if (currentGame.board.finalPositions[x][y] != ' ')
                break;
            ++x;
        }


        x = i - 1; y = j;
        while (x >= 0) {
            if (currentGame.board.finalPositions[x][y] == rook || currentGame.board.finalPositions[x][y] == queen) {
                currentGame.board.undoMove();
                return false;
            }
            else if (currentGame.board.finalPositions[x][y] != ' ')
                break;
            --x;
        }


        x = i; y = j + 1;
        while (y < 8) {
            if (currentGame.board.finalPositions[x][y] == rook || currentGame.board.finalPositions[x][y] == queen) {
                currentGame.board.undoMove();
                return false;
            }
            else if (currentGame.board.finalPositions[x][y] != ' ')
                break;
            ++y;
        }


        x = i; y = j - 1;
        while (y >= 0) {
            if (currentGame.board.finalPositions[x][y] == rook || currentGame.board.finalPositions[x][y] == queen) {
                currentGame.board.undoMove();
                return false;
            }
            else if (currentGame.board.finalPositions[x][y] != ' ')
                break;
            --y;
        }



        x = i + 1; y = j + 1;
        while (x < 8 && y < 8) {
            if (currentGame.board.finalPositions[x][y] == bishop || currentGame.board.finalPositions[x][y] == queen) {
                currentGame.board.undoMove();
                return false;
            }
            else if (currentGame.board.finalPositions[x][y] != ' ')
                break;
            ++x; ++y;
        }


        x = i - 1; y = j + 1;
        while (x >= 0 && y < 8) {
            if (currentGame.board.finalPositions[x][y] == bishop || currentGame.board.finalPositions[x][y] == queen) {
                currentGame.board.undoMove();
                return false;
            }
            else if (currentGame.board.finalPositions[x][y] != ' ')
                break;
            --x; ++y;
        }

        x = i - 1; y = j - 1;
        while (x >= 0 && y >= 0) {
            if (currentGame.board.finalPositions[x][y] == bishop || currentGame.board.finalPositions[x][y] == queen) {
                currentGame.board.undoMove();
                return false;
            }
            else if (currentGame.board.finalPositions[x][y] != ' ')
                break;
            --x; --y;
        }


        x = i + 1; y = j - 1;
        while (x < 8 && y >= 0) {
            if (currentGame.board.finalPositions[x][y] == bishop || currentGame.board.finalPositions[x][y] == queen) {
                currentGame.board.undoMove();
                return false;
            }
            else if (currentGame.board.finalPositions[x][y] != ' ')
                break;
            ++x; --y;
        }


        currentGame.board.undoMove();
        return true;
    }


    /**
    *   utils
     */
    private Square toSquare(int i, int j) {
        return Square.valueOf("" + (char)(j + 65) + (char)(8 - i + 48));
    }
    private String fileContent(String path) {
        Path file = Paths.get(path);
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                // Add the \n that's removed by readline()
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
            System.exit(1);
        }
        return sb.toString();
    }
    private static String removeCharacter(String c, String move) {
        while (move.contains(c)) {
            int charIndex = move.indexOf(c);
            move = move.substring(0, charIndex) + move.substring(charIndex + 1,
                    move.length());
        }
        return move;
    }
}


