import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

public class App extends JFrame implements ActionListener {

    final private Color LAVENDER_GRAY = Color.decode("#CAC4CE");
    final private Color DARK_PURPLE = Color.decode("#242038");
    final private Color LINEN = Color.decode("#F7ECE1");
    final private Color MIDDLE_BLUE_PURPLE = Color.decode("#8D86C9");


    ChessPanel chessPanel = new ChessPanel();
    JPanel sidePanel = new JPanel(), controlPanel = new JPanel();
    JScrollPane infoPanel = new JScrollPane(), movesPanel = new JScrollPane();
    JButton nextMove, previousMove, openGame, nextGame, previousGame;
    int moveIndex = 0, gameIndex = 0;


    JTextPane movesArea = new JTextPane(), infoArea = new JTextPane();


    PGN pgn;

    App() {
        pgn = new PGN();
        setLayout(new BorderLayout());
        add(chessPanel, BorderLayout.WEST);

        sidePanel.setSize(300, 550);
        add(sidePanel, BorderLayout.CENTER);

        GridBagLayout gridBag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();


        controlPanel.setLayout(gridBag);
        controlPanel.setBackground(LAVENDER_GRAY);
        controlPanel.setPreferredSize(new Dimension(300, 100));

        c.gridx = 0; c.gridy = 0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        openGame = new JButton("open pgn");
        openGame.addActionListener(this);
        controlPanel.add(openGame, c);

        // previous and next Move buttons
        c.gridx = 0; c.gridy = 1;
        c.gridwidth = 1;
        previousMove = new JButton("previous move");
        previousMove.addActionListener(this);
        controlPanel.add(previousMove, c);
        c.gridx = 1; c.gridy = 1;
        nextMove = new JButton("next move");
        nextMove.addActionListener(this);
        controlPanel.add(nextMove, c);

        c.gridx = 0; c.gridy = 2;
        previousGame = new JButton("previous game");
        previousGame.addActionListener(this);
        controlPanel.add(previousGame, c);
        c.gridx = 1; c.gridy = 2;
        nextGame = new JButton("next game");
        nextGame.addActionListener(this);
        controlPanel.add(nextGame, c);

        c.gridx = 0; c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;

        infoArea.setEditable(false);
        infoArea.setBackground(LINEN);
        infoArea.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 15));
        JScrollPane sp2 = new JScrollPane(infoArea);
        sp2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp2.setPreferredSize(new Dimension(300, 120));
        sidePanel.add(sp2, c);

        c.gridx = 0; c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;

        movesArea.setEditable(false);

        movesArea.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 20));
        JScrollPane sp1 = new JScrollPane(movesArea);
        sp1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp1.setPreferredSize(new Dimension(300, 200));
        sidePanel.add(sp1, c);

        c.gridx = 0; c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        sidePanel.add(controlPanel, c);

        sidePanel.setBackground(LAVENDER_GRAY);
        load();


        setSize(800, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
    }


    private void makeInfoPanel() {
        StringBuilder res = new StringBuilder();
        for (Map.Entry<String, String> pair : pgn.games.get(gameIndex).tags.entrySet()) {
            res.append(pair.getKey()).append(":\t").append(pair.getValue()).append("\n");
        }

        infoArea.setText(res.toString());
    }
    private void makeMovesPanel() {
        StringBuilder res = new StringBuilder();
        for(String s: pgn.games.get(gameIndex).chessNotation) {

            String h = s.replace('K', Piece.uiNotation.get(Piece.BLACK_KING).toCharArray()[0]);
            h = h.replace('Q', Piece.uiNotation.get(Piece.BLACK_QUEEN).toCharArray()[0]);
            h = h.replace('B', Piece.uiNotation.get(Piece.BLACK_BISHOP).toCharArray()[0]);
            h = h.replace('R', Piece.uiNotation.get(Piece.BLACK_ROOK).toCharArray()[0]);
            h = h.replace('N', Piece.uiNotation.get(Piece.BLACK_KNIGHT).toCharArray()[0]);


            res.append(h).append(" ");
        }

        movesArea.setText(res.toString());
    }

    void load() {

        // update chess panel
        moveIndex = pgn.games.get(gameIndex).board.moves.size() - 1;
        chessPanel.game = new Game();
        for(Move m: pgn.games.get(gameIndex).board.moves) {
            chessPanel.game.board.doMove(m);
        }
        chessPanel.updateChess();

        // update moves panel
        makeMovesPanel();
        // update info panel
        makeInfoPanel();
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(nextMove)) {
            if (moveIndex < pgn.games.get(gameIndex).board.moves.size() - 1) {
                ++moveIndex;
                chessPanel.game.board.doMove(pgn.games.get(gameIndex).board.moves.get(moveIndex));
            }
        }
        else if(e.getSource().equals(previousMove)) {
            if (moveIndex >= 0) {
                chessPanel.game.board.undoMove();
                --moveIndex;
            }
        }
        else if(e.getSource().equals(nextGame)) {
            if (gameIndex < pgn.games.size() - 1) {
                ++gameIndex;
                load();
            }
        }
        else if(e.getSource().equals(previousGame)) {
            if(gameIndex > 0) {
                --gameIndex;
                load();
            }
        }
        else if(e.getSource().equals(openGame)) {
            JFileChooser fc = new JFileChooser();
            int i = fc.showOpenDialog(this);
            if(i == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                pgn = new PGN(f.getPath());
                gameIndex = 0;
                load();
            }
        }
        chessPanel.updateChess();
    }
}
