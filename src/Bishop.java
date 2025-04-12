import java.util.ArrayList;

public class Bishop extends Piece{

    int[][] boardValues={
            {-2,-1 ,  0,   1,   1,  0,  -1,  -2, },
            {-1 ,  0,   1,   1  , 1 ,  1,   0 , -1,},
            {  0 ,  1,   2 ,  2 ,  2 ,  2 ,  1 ,  0,},
            {  1 ,  1,   2 ,  2 ,  2 ,  2 ,  1 ,  1,},
            { 1  , 1 ,  2  , 2  , 2  , 2  , 1  , 1,},
            {  0 ,  1,   2 ,  2 ,  2 ,  2 ,  1 ,  0,},
            {-1  , 0 ,  1  , 1  , 1  , 1  , 0  ,-1,},
            { -2 , -1,   0 ,  1 ,  1 ,  0 , -1 , -2,}
    };
    public Bishop(boolean white)
    {
        super(white);
        setValue(3);
        setBoardValues(this.boardValues);
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end)
    {
        // we can't move the piece to a spot that has
        // a piece of the same colour
        if(end.getPiece() != null) {
            if (end.getPiece().isWhite() == this.isWhite()) {
                return false;
            }
        }

        int deltaX = Math.abs(end.getX() - start.getX());
        int deltaY = Math.abs(end.getY() - start.getY());

        if (deltaX == deltaY) {
            // Check for any pieces between start and end along the diagonal
            int stepX = Integer.compare(end.getX(), start.getX());
            int stepY = Integer.compare(end.getY(), start.getY());
            for (int i = 1; i < deltaX; i++) {
                Spot nextSpot = board.getBox(start.getX() + i * stepX, start.getY() + i * stepY);
                if (nextSpot.getPiece() != null) {
                    return false; // There's a piece blocking the path
                }
            }
            return true; // The move is valid along a diagonal
        }

        return false;
    }

    @Override
    public ArrayList<Spot> getAttackedSpots(Board board) {
        ArrayList<Spot> attackedSpots = new ArrayList<>();
        Spot currentSpot = this.findSpot(board);


        int x = currentSpot.getX();
        int y = currentSpot.getY();


        // Check diagonally upwards to the right
        for (int i = 1; x + i < 8 && y + i < 8; i++) {
            Spot spot = board.getBox(x + i, y + i);
            if (spot.getPiece() == null) {
                attackedSpots.add(spot);
            } else {

                    attackedSpots.add(spot);

                break; // Stop further searching in this direction if a piece is encountered
            }
        }

        // Check diagonally upwards to the left
        for (int i = 1; x - i >= 0 && y + i < 8; i++) {
            Spot spot = board.getBox(x - i, y + i);
            if (spot.getPiece() == null) {
                attackedSpots.add(spot);
            } else {

                    attackedSpots.add(spot);

                break; // Stop further searching in this direction if a piece is encountered
            }
        }

        // Check diagonally downwards to the right
        for (int i = 1; x + i < 8 && y - i >= 0; i++) {
            Spot spot = board.getBox(x + i, y - i);
            if (spot.getPiece() == null) {
                attackedSpots.add(spot);
            } else {

                    attackedSpots.add(spot);

                break; // Stop further searching in this direction if a piece is encountered
            }
        }

        // Check diagonally downwards to the left
        for (int i = 1; x - i >= 0 && y - i >= 0; i++) {
            Spot spot = board.getBox(x - i, y - i);
            if (spot.getPiece() == null) {
                attackedSpots.add(spot);
            } else {

                    attackedSpots.add(spot);

                break; // Stop further searching in this direction if a piece is encountered
            }
        }

        return attackedSpots;
    }

}