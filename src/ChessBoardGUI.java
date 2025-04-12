import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChessBoardGUI extends JFrame {

    private JPanel chessBoardPanel;
    private JLabel[][] chessBoardLabels;
    private Spot selectedSpot;

    private Color tempCol;
    private Board board;
    private Game game;


    public ChessBoardGUI(Board board, Game game) {
        this.board = board;
        this.game = game;
        initialize();
    }

    private Color convertCol(boolean white) {
        if (white) {
            return Color.RED;
        } else {
            return Color.BLACK;
        }
    }

    private void initialize() {
        setTitle("Chess Board");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        chessBoardLabels = new JLabel[8][8];
        selectedSpot = null;

        chessBoardPanel = new JPanel(new GridLayout(8, 8));
        add(chessBoardPanel);

        // Create the chessboard
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JLabel label = new JLabel();
                label.setOpaque(true);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                if ((i + j) % 2 == 0) {
                    label.setBackground(Color.WHITE);
                } else {
                    label.setBackground(Color.LIGHT_GRAY);
                }
                label.addMouseListener(new ChessPieceClickListener(j, i));
                chessBoardPanel.add(label);
                chessBoardLabels[j][i] = label;
            }
        }

        // Update the chessboard with pieces
        updateChessBoard();

        setVisible(true);
    }

    public void updateChessBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Spot spot = board.getBox(j, i);
                if (spot.getPiece() != null) {
                    String pieceSymbol = getPieceSymbol(spot.getPiece());
                    chessBoardLabels[j][i].setText(pieceSymbol);

                    chessBoardLabels[j][i].setForeground(spot.getPiece().getColor());

                } else {
                    chessBoardLabels[j][i].setText("");
                }
            }
        }
    }

    private String getPieceSymbol(Piece piece) {

        if (piece instanceof Pawn) {
            return "P";
        } else if (piece instanceof Rook) {
            return "R";
        } else if (piece instanceof Bishop) {
            return "B";
        } else if (piece instanceof Queen) {
            return "Q";
        } else if (piece instanceof King) {
            return "K";
        } else if (piece instanceof Knight) {
            return "Kn";
        }

        return "N";
    }

    private class ChessPieceClickListener extends MouseAdapter {
        private int x;
        private int y;

        public ChessPieceClickListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (selectedSpot == null) {
                selectedSpot = board.getBox(x, y);

                System.out.println(selectedSpot.getX() + " / " + selectedSpot.getY() );
                if(selectedSpot.getPiece() != null){
                    for(Spot s : selectedSpot.getPiece().getAttackedSpots(board)){
                        System.out.println("attacked:  " + s.getX() + " / " + s.getY() );
                    }
                }
                tempCol = chessBoardLabels[x][y].getBackground();
                chessBoardLabels[x][y].setBackground(Color.YELLOW);
            } else {
                Spot targetSpot = board.getBox(x, y);
                if (selectedSpot != targetSpot) {
                    // Move the piece to the target spot
                    if (selectedSpot.getPiece() != null) {
                        game.move(selectedSpot, targetSpot, false);
                    }
                }
                chessBoardLabels[selectedSpot.getX()][selectedSpot.getY()].setBackground(tempCol);
                selectedSpot = null;
            }
        }

    }
}




