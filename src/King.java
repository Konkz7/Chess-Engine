import java.util.ArrayList;

public class King extends Piece {

    int [][] boardValues = {
            { 1 ,  2 ,  1 ,  0 ,  0 ,  1,   2,   1},
            { -1 ,  -1 ,  -1 ,  -1 ,  -1 ,  -1,   -1,   -1},
            { -1 ,  -1 ,  -1 ,  -1 ,  -1 ,  -1,   -1,   -1},
            { -1 ,  -1 ,  -1 ,  -1 , -1 , -1,   -1,   -1},
            { -1 ,  -1 ,  -1 ,  -1 , -1 , -1,   -1,   -1},
            { -1 ,  -1 ,  -1 ,  -1 ,  -1, -1,   -1,   -1},
            { -1 ,  -1 ,  -1 ,  -1 ,  -1 ,  -1,   -1,   -1},
            { 1 ,  2 ,  1 ,  0 ,  0 ,  1,   2,   1}
        };
    private boolean castlingDone = false;

    public boolean fauxCastled = false;

    public King(boolean white)
    {
        super(white);
        setValue(0);
        setBoardValues(this.boardValues);
    }

    public boolean isCastlingDone()
    {
        return this.castlingDone;
    }

    public void setCastlingDone(boolean castlingDone)
    {
        this.castlingDone = castlingDone;
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end)
    {
        // we can't move the piece to a Spot that
        // has a piece of the same color
        if(end.getPiece() != null) {
            if (end.getPiece().isWhite() == this.isWhite()) {
                return false;
            }
        }

        int x = Math.abs(start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());
        return (x + y == 1 || (x == 1 && y == 1));
    }

    @Override
    public ArrayList<Spot> getAttackedSpots(Board board) {
        ArrayList<Spot> attackedSpots = new ArrayList<>();
        Spot currentSpot = this.findSpot(board);


        int x = currentSpot.getX();
        int y = currentSpot.getY();
        boolean isWhite = this.isWhite();

        // King's possible moves relative to its current position
        int[][] moves = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1},{0, 1}, {1, -1},
                {1, 0}, {1, 1}};

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


    public boolean isValidCastling(ArrayList<Spot> attackedSpots,Board board, Spot start, Spot end)
    {

        if (this.isCastlingDone()) {
            return false;
        }

        for(Spot s : attackedSpots) {
            if (s == start || s == end) {
                return false;
            }
        }

        int x = Math.abs(start.getX() - end.getX());

        if(end.getPiece() instanceof Rook && !this.hasMoved() && !end.getPiece().hasMoved() && end.getPiece().isWhite() == this.isWhite() ){
            for (int xx = 1 ; xx < x  ; xx ++ ) {
                if (start.getX() < end.getX()) {
                    for(Spot s : attackedSpots) {
                        if (board.boxes[start.getX() + xx][start.getY()].getPiece() != null
                        || s == board.boxes[start.getX() + xx][start.getY()]) {
                            return false;
                        }
                    }
                } else if (start.getX() > end.getX()) {
                    for(Spot s : attackedSpots) {
                        if (board.boxes[start.getX() - xx][start.getY()].getPiece() != null
                        || s == board.boxes[start.getX() - xx][start.getY()]) {
                            return false;
                        }
                    }
                }
            }
        }else{
            return false;
        }
        return true;
    }

    public void Castle(Board board , Spot start, Spot end)
    {

        Piece temp = start.getPiece();
        if (start.getX() < end.getX()) {

            start.setPiece(null);
            board.boxes[6][start.getY()].setPiece(temp);

            temp = end.getPiece();
            end.setPiece(null);
            board.boxes[5][start.getY()].setPiece(temp);

        } else if (start.getX() > end.getX()) {

            start.setPiece(null);
            board.boxes[2][start.getY()].setPiece(temp);

            temp = end.getPiece();
            end.setPiece(null);
            board.boxes[3][start.getY()].setPiece(temp);
        }

    }
}
