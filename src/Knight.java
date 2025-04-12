import java.util.ArrayList;


public class Knight extends Piece{

    int[][] boardValues = {
            {-2,-1,0,0,0,0,-1,-2},
            {-1,0,1,1,1,1,0,-1},
            {0, 1, 2, 2, 2, 2, 1, 0},
            {0, 1, 2, 2, 2, 2, 1, 0},
            {0, 1, 2, 2, 2, 2, 1, 0},
            {0, 1, 2, 2, 2, 2, 1, 0},
            {-1, 0, 1, 1, 1, 1, 0, -1},
            {-2, -1, 0, 0, 0, 0, -1, -2}
    };
    public Knight(boolean white)
    {
        super(white);
        setValue(3);
        setBoardValues(this.boardValues);
    }

    @Override
    public boolean canMove(Board board, Spot start,
                           Spot end)
    {
        // we can't move the piece to a spot that has
        // a piece of the same colour
        if(end.getPiece() != null) {
            if (end.getPiece().isWhite() == this.isWhite()) {
                return false;
            }
        }

        int x = Math.abs(start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());
        return x * y == 2;
    }

    @Override
    public ArrayList<Spot> getAttackedSpots(Board board) {
        ArrayList<Spot> attackedSpots = new ArrayList<>();
        Spot currentSpot = this.findSpot(board);


        int x = currentSpot.getX();
        int y = currentSpot.getY();

        // Knight's possible moves relative to its current position
        int[][] moves = {
                {-2, -1}, {-2, 1},
                {-1, -2}, {-1, 2},
                {1, -2}, {1, 2},
                {2, -1}, {2, 1}
        };

        for (int[] move : moves) {
            int newX = x + move[0];
            int newY = y + move[1];
            if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8) {
                Spot spot = board.getBox(newX, newY);

                attackedSpots.add(spot);

            }
        }

        return attackedSpots;
    }
}
