import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ChessPanel extends JPanel {

    private class Cell extends JPanel {
        JLabel piece;
        String path = "";
        Cell(Color c, String imgPath) {
            setSize(60, 60);
            setLayout(new BorderLayout());
            setBackground(c);
            path = imgPath;
            piece = new JLabel( new ImageIcon(imgPath) );
            add(piece);
        }
    }

    Cell[][] cells = new Cell[8][8];


    public Game game = new Game();

    ChessPanel() {

        setSize(600, 600);
        setLayout( new GridLayout(8, 8) );

        for(int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                cells[i][j] = new Cell(((i+j)% 2) == 0 ? Color.decode("#F7ECE1") : Color.decode("#725AC1"),
                        Piece.imgPath.get(Piece.fromNotation.get(game.board.finalPositions[i][j])));
                add(cells[i][j]);
            }
        }
        updateChess();

    }


    // updates icons
    public void updateChess() {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                String tmp = Piece.imgPath.get(Piece.fromNotation.get(game.board.finalPositions[i][j]));
                if (!cells[i][j].path.equals(tmp)) {
                    cells[i][j].path = tmp;
                    cells[i][j].piece.setIcon(new ImageIcon(tmp));
                }
            }
        }
    }

}


