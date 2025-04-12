import java.awt.*;
import java.util.ArrayList;

public abstract class Piece {

    private boolean killed = false;
    private boolean white = false;

    private boolean moved = false;
    private Color color;

    private int value;

    private int[][] boardValues;

    private Spot resetSpot;

    private int defValue = 0;


    public Piece(boolean white)
    {
        this.setWhite(white);

        if(this.isWhite()){
            this.color = Color.RED;
        }else{
            this.color = Color.BLACK;
        }
    }

    public void setResetSpot(Spot resetSpot) {
        this.resetSpot = resetSpot;
    }

    public Spot getResetSpot() {
        return resetSpot;
    }

    public boolean isWhite()
    {
        return this.white;
    }

    public void setWhite(boolean white)
    {
        this.white = white;
    }

    public boolean isKilled()
    {
        return this.killed;
    }

    public void setKilled(boolean killed)
    {
        this.killed = killed;
    }

    public abstract boolean canMove(Board board,
                                    Spot start, Spot end);

    public abstract ArrayList<Spot> getAttackedSpots(Board board);

    public void setBoardValues(int[][] boardValues) {
        this.boardValues = boardValues;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int[][] getBoardValues() {
        return boardValues;
    }

    public ArrayList<Piece> getAttackedPieces(ArrayList<Spot> attackedSpots){
        ArrayList<Piece> pieces = new ArrayList<>();

        for (Spot s : attackedSpots){
            if(s.getPiece()!= null && s.getPiece().isWhite() != this.isWhite()){
                pieces.add(s.getPiece());
            }
        }

        return pieces;
    }

    public boolean isDefended (ArrayList<Spot> attackedSpots){

        for (Spot s : attackedSpots){
            if(s.getPiece() == this){
                return true;
            }
        }

        return false;
    }

    public Spot findSpot(Board board){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(board.getBox(i,j).getPiece() == this ){
                    return board.getBox(i,j);
                }
            }
        }

        return null;
    }


    public boolean hasMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public Color getColor() {
        return color;
    }

    public int getDefValue() {
        return defValue;
    }

    public void addDefValue(int defValue) {
        this.defValue += defValue;
    }


}
