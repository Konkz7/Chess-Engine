import java.util.ArrayList;

public class Rook extends Piece{

    int[][] boardValue = {
            {-1 ,  0 ,  1 ,  1 ,  1 ,  1 ,  0 , -1,},
            {0  , 1  , 2  , 2  , 2  , 2  , 1  , 0,},
            {0  , 1  , 2  , 2  , 2  , 2  , 1  , 0,},
            {0  , 1  , 2  , 2  , 2  , 2  , 1  , 0,},
            {0  , 1  , 2  , 2  , 2  , 2  , 1  , 0,},
            {0  , 1  , 2  , 2  , 2  , 2  , 1  , 0,},
            {0  , 1  , 2  , 2  , 2  , 2  , 1  , 0,},
            {-1 ,  0 ,  1 ,  1 ,  1 ,  1 ,  0 , -1 }
    };
    public Rook(boolean white)
    {
        super(white);
        setValue(5);
        setBoardValues(this.boardValue);
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

        // Check if the move is along a straight line (like a rook)
        if ((deltaX == 0 && deltaY != 0) || (deltaY == 0 && deltaX != 0)) {
            // Check for any pieces between start and end along the straight line
            int stepX = Integer.compare(end.getX(), start.getX());
            int stepY = Integer.compare(end.getY(), start.getY());
            for (int i = 1; i < Math.max(deltaX, deltaY); i++) {
                Spot nextSpot = board.getBox(start.getX() + i * stepX, start.getY() + i * stepY);
                if (nextSpot.getPiece() != null) {
                    return false; // There's a piece blocking the path
                }
            }
            return true; // The move is valid along a straight line
        }
        return false;
    }

    @Override
    public ArrayList<Spot> getAttackedSpots(Board board) {
        ArrayList<Spot> attackedSpots = new ArrayList<>();
        Spot currentSpot = this.findSpot(board);


        int x = currentSpot.getX();
        int y = currentSpot.getY();


        // Check horizontally to the right
        for (int i = x + 1; i < 8; i++) {
            Spot spot = board.getBox(i, y);
            if (spot.getPiece() == null) {
                attackedSpots.add(spot);
            } else {

                    attackedSpots.add(spot);

                break; // Stop further searching in this direction if a piece is encountered
            }
        }

        // Check horizontally to the left
        for (int i = x - 1; i >= 0; i--) {
            Spot spot = board.getBox(i, y);
            if (spot.getPiece() == null) {
                attackedSpots.add(spot);
            } else {

                    attackedSpots.add(spot);

                break; // Stop further searching in this direction if a piece is encountered
            }
        }

        // Check vertically upwards
        for (int j = y + 1; j < 8; j++) {
            Spot spot = board.getBox(x, j);
            if (spot.getPiece() == null) {
                attackedSpots.add(spot);
            } else {

                    attackedSpots.add(spot);

                break; // Stop further searching in this direction if a piece is encountered
            }
        }

        // Check vertically downwards
        for (int j = y - 1; j >= 0; j--) {
            Spot spot = board.getBox(x, j);
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