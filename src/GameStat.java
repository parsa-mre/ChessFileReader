
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class GameStat {
    PGN pgn = new PGN("./pgns/Dortmund2019");

    GameStat(String path) {
        pgn = new PGN(path);
    }

    ArrayList<Game> getGames(String playerName) {
        ArrayList<Game> res = new ArrayList<>();

        for (Game g: pgn.games) {
            if (g.tags.containsValue(playerName))
                res.add(g);
        }
        return res;
    }

    double getWinLoseRatio(String playerName) {

        double win = 0, lose = 0;
        for (Game g: pgn.games) {
            if (g.tags.containsValue(playerName)) {
                if ((g.tags.get("White").equals(playerName) && g.tags.get("result").equals("1-0")) ||
                        g.tags.get("Black").equals(playerName) && g.tags.get("result").equals("0-1"))
                    ++win;
                if ((g.tags.get("White").equals(playerName) && g.tags.get("result").equals("0-1")) ||
                        g.tags.get("Black").equals(playerName) && g.tags.get("result").equals("1-0"))
                    ++lose;
            }
        }

        return win/lose;
    }

    double openingWinRate(String opening) {
        double win = 0, total = 0;
        for (Game g: pgn.games) {
            if (g.tags.containsValue(opening)) {
                ++total;
                if (g.tags.containsValue("1-0"))
                    ++win;
            }
        }
        return win/total;
    }

    int numberOfGamesWithOpening(String opening) {
        int total = 0;
        for (Game g: pgn.games) {
            if (g.tags.containsValue(opening)) {
                ++total;
            }
        }
        return total;
    }

    ArrayList<Move> MostUsedMoves(int n) {
        ArrayList<MoveWrapped> arr = new ArrayList<>();
        for (Game g: pgn.games) {
            for (Move m: g.board.moves) {
                boolean found = false;

                for (MoveWrapped mw: arr) {
                    if (mw.move.equals(m)) {
                        ++mw.count;
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    arr.add(new MoveWrapped(m));
                }
            }
        }
        Collections.sort(arr);
        ArrayList<Move> res = new ArrayList<>();
        for (int i = 0; i < n && i < arr.size(); ++i) {
            res.add(arr.get(i).move);
        }
        return res;
    }

    double expectedMovesBeforeFirstCapture() {
        int sum = 0;
        for (Game g: pgn.games) {
            int i = 0;
            for(i = 0; i < g.board.moves.size(); ++i)
                if(g.board.moves.get(i).capturedPiece != Piece.NONE)
                    break;
            sum += i;
        }
        return 1.0 * sum/pgn.games.size();
    }

    private class MoveWrapped implements Comparable<MoveWrapped> {
        int count = 0;
        Move move;

        MoveWrapped (Move m) {
            move = m;
        }

        @Override
        public int compareTo(MoveWrapped o) {
            return o.count - this.count;
        }
    }
}

