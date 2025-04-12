import java.util.ArrayList;
import java.util.Scanner;

public class Pawn extends Piece{

    int [][] boardValues = {
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 4, 4, 0, 0, 0},
            {0, 0, 0, 4, 4, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0}
    };
    private int direction;

    private boolean justTwoStepped = false;

    private boolean justEnPassed = false ,RorL = false;

    Spot s1 = null;
    Spot s2 = null;

    public Pawn(boolean white)
    {
        super(white);
        if(!this.isWhite()){
            direction = 1;
        }else{
            direction = -1;
        }
        setValue(1);
        setBoardValues(this.boardValues);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end)
    {

        if(start.getX() > 0) {
             s1 = board.boxes[start.getX() - 1][start.getY()];
        }
        if(start.getX() < 7) {
             s2 = board.boxes[start.getX() + 1][start.getY()];
        }
        // we can't move the piece to a spot that has
        // a piece of the same colour
        if(end.getPiece() != null) {
            if (end.getPiece().isWhite() == this.isWhite()) {
                return false;
            }
        }

        int x = Math.abs(start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());

        if(end.getY() - start.getY() != direction * y){

            return false;
        }

        if( y == 1 && x == 1 && end.getPiece() != null){
            if(end.getPiece().isWhite() != this.isWhite()){
                return true;
            }
        } else if (x == 0 && y == 2 && hasMoved() == false) {
            for(int i = 1 ; i < 3; i ++){
                if(board.boxes[start.getX()][start.getY() + (i*direction)].getPiece() != null){

                    return false;
                }
            }
            return true;
        } else if (x == 0 && y == 1 && board.boxes[start.getX()][start.getY() + direction].getPiece() == null) {
            return true;
        }else if(y == 1 && x == 1){
            if(s1 != null && s1.getPiece() instanceof Pawn && ((Pawn) s1.getPiece()).isJustTwoStepped() && s1.getPiece().isWhite() != this.isWhite() && end.getX() < start.getX()){
                justEnPassed = true;
                RorL = true;
                return true;
            }
            if(s2 != null && s2.getPiece() instanceof Pawn && ((Pawn) s2.getPiece()).isJustTwoStepped() && s2.getPiece().isWhite() != this.isWhite() && end.getX() > start.getX()){
                justEnPassed = true;
                return true;
            }
        }

        return false;
    }

    public void enPassant(){
        //add kill function
        if(RorL){
            s1.getPiece().setKilled(true);
            s1.setPiece(null);
        }else{
            s2.getPiece().setKilled(true);
            s2.setPiece(null);
        }
    }

    public void Promotion(Spot currentSpot) {
        Scanner promotionPiece = new Scanner(System.in);  // Create a Scanner object
        String pieceName = " ";

        while (true){
            System.out.println("Enter the piece you want to promote to: Kn / Q / R / B");

            pieceName = promotionPiece.nextLine();
            System.out.println(pieceName);

            if(pieceName.equals("Kn")){
                currentSpot.setPiece(new Knight(this.isWhite()));
                break;
            }else if(pieceName.equals("Q")){
                currentSpot.setPiece(new Queen(this.isWhite()));
                break;
            }else if(pieceName.equals("R")){
                currentSpot.setPiece(new Rook(this.isWhite()));
                break;
            }else if(pieceName.equals("B")){
                currentSpot.setPiece(new Bishop(this.isWhite()));
                break;
            }
        }



    }

    @Override
    public ArrayList<Spot> getAttackedSpots(Board board) {

        ArrayList<Spot>attackedSpots = new ArrayList<>();
        Spot currentSpot = this.findSpot(board);

        int x = currentSpot.getX();
        int y = currentSpot.getY();


        // Check diagonal left
        if (x > 0 && y + direction >= 0 && y + direction < 8) {
            Spot diagonalLeft = board.getBox(x - 1, y + direction);
            attackedSpots.add(diagonalLeft);

        }

        // Check diagonal right
        if (x < 7 && y + direction >= 0 && y + direction < 8) {
            Spot diagonalRight = board.getBox(x + 1, y + direction);
            attackedSpots.add(diagonalRight);
        }

        // Check  right
        if (x < 7) {
            Spot Right = board.getBox(x + 1, y);
            if (Right.getPiece() != null &&  Right.getPiece().isWhite() != this.isWhite() && Right.getPiece() instanceof Pawn && ((Pawn) Right.getPiece()).justTwoStepped) {
                attackedSpots.add(Right);
            }
        }

        //check left
        if (x > 0) {
            Spot Left = board.getBox(x - 1, y);
            if (Left.getPiece() != null &&  Left.getPiece().isWhite() != this.isWhite() && Left.getPiece() instanceof Pawn && ((Pawn) Left.getPiece()).justTwoStepped) {
                attackedSpots.add(Left);
            }
        }

        return attackedSpots;
    }




    public boolean isJustTwoStepped() {
        return justTwoStepped;
    }

    public void setJustTwoStepped(boolean justTwoStepped) {
        this.justTwoStepped = justTwoStepped;
    }

    public boolean isJustEnPassed() {
        return justEnPassed;
    }

    public void setJustEnPassed(boolean justEnPassed) {
        this.justEnPassed = justEnPassed;
    }
}
