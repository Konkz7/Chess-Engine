import java.util.ArrayList;

public class Board {
    Spot[][] boxes = new Spot[8][8];

    ArrayList<Piece> white = new ArrayList<>();
    ArrayList<Piece> black = new ArrayList<>();



    public Board()
    {
        this.setupBoard();
    }

    public Spot getBox(int x, int y) {

        if (x < 0 || x > 7 || y < 0 || y > 7) {
            try {
                throw new Exception("Index out of bound");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return boxes[x][y];
    }

    public void setupBoard()
    {

        /*
        boxes[0][0] = new Spot(0, 0, null);
        boxes[1][0] = new Spot(1, 0, null);
        boxes[2][0] = new Spot(2, 0, null);
        boxes[3][0] = new Spot(3, 0, null);
        boxes[4][0] = new Spot(4, 0, new King(false));
        boxes[5][0] = new Spot(5, 0, null);
        boxes[6][0] = new Spot(6, 0, null);
        boxes[7][0] = new Spot(7, 0, null);

        for (int i = 0 ; i < 8 ; i++){
            boxes[i][1] = new Spot(i, 1, null);
        }
        black.add(boxes[4][0].getPiece());
        */

        // initialize black pieces
        boxes[0][0] = new Spot(0, 0, new Rook(false));
        boxes[1][0] = new Spot(1, 0, new Knight(false));
        boxes[2][0] = new Spot(2, 0, new Bishop(false));

        boxes[3][0] = new Spot(3, 0, new Queen(false));
        boxes[4][0] = new Spot(4, 0, new King(false));

        boxes[5][0] = new Spot(5, 0, new Bishop(false));
        boxes[6][0] = new Spot(6, 0, new Knight(false));
        boxes[7][0] = new Spot(7, 0, new Rook(false));


        for (int i = 0 ; i < 8 ; i++){
            boxes[i][1] = new Spot(i, 1, new Pawn(false));
        }



        for (int i = 0 ; i < 8 ; i++){
            for (int j = 0 ; j < 2 ; j++){
                black.add(boxes[i][j].getPiece());
            }
        }



        //...
/*
        boxes[0][7] = new Spot(0, 7, null);
        boxes[1][7] = new Spot(1, 7, null);
        boxes[2][7] = new Spot(2, 7, null);
        boxes[3][7] = new Spot(3, 7, null);
        boxes[4][7] = new Spot(4, 7, new King(true));
        boxes[5][7] = new Spot(5, 7, null);
        boxes[6][7] = new Spot(6, 7, null);
        boxes[7][7] = new Spot(7, 7, null);

        for (int i = 0 ; i < 8 ; i++){
            boxes[i][6] = new Spot(i, 6, null);
        }

        white.add(boxes[4][7].getPiece());

 */
        // initialize white pieces

        boxes[0][7] = new Spot(0, 7, new Rook(true));
        boxes[1][7] = new Spot(1, 7, new Knight(true));
        boxes[2][7] = new Spot(2, 7, new Bishop(true));

        boxes[3][7] = new Spot(3, 7, new Queen(true));
        boxes[4][7] = new Spot(4, 7, new King(true));

        boxes[5][7] = new Spot(5, 7, new Bishop(true));
        boxes[6][7] = new Spot(6, 7, new Knight(true));
        boxes[7][7] = new Spot(7, 7, new Rook(true));

        for (int i = 0 ; i < 8 ; i++){
            boxes[i][6] = new Spot(i, 6, new Pawn(true));
        }

        for (int i = 0 ; i < 8 ; i++){
            for (int j = 6 ; j < 8 ; j++){
                white.add(boxes[i][j].getPiece());
            }
        }



        // initialize remaining boxes without any piece
        for (int i = 0; i < 8; i++) {
            for (int j = 2; j < 6; j++) {
                boxes[i][j] = new Spot(i, j, null);
            }
        }
    }



    public void mapBoard(Board otherBoard) {
        black.clear();
        white.clear();
        // Iterate over each spot on the other board
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Spot otherSpot = otherBoard.getBox(x, y); // Get the spot from the other board
                Piece piece = otherSpot.getPiece(); // Get the piece from the other spot
                if (piece != null) { // If there is a piece on the other spot
                    // Add the piece to the appropriate list based on its color

                    Piece newP = null;
                    boolean col = piece.isWhite();

                    if(piece instanceof Pawn){

                        newP = new Pawn(col);
                        boxes[x][y] = new Spot(x, y, newP);

                        if(((Pawn) piece).isJustEnPassed()){
                            ((Pawn)newP).setJustEnPassed(true);
                        }
                        if(((Pawn) piece).isJustTwoStepped()){
                            ((Pawn)newP).setJustTwoStepped(true);
                        }

                    } else if (piece instanceof Bishop) {

                        newP = new Bishop(col);
                        boxes[x][y] = new Spot(x, y, newP);

                    }else if (piece instanceof King) {

                        newP = new King(col);
                        boxes[x][y] = new Spot(x, y, newP);
                        if(((King) piece).isCastlingDone()){
                            ((King)newP).setCastlingDone(true);
                        }

                    }else if (piece instanceof Queen) {

                        newP = new Queen(col);
                        boxes[x][y] = new Spot(x, y, newP);

                    }else if (piece instanceof Rook) {

                        newP = new Rook(col);
                        boxes[x][y] = new Spot(x, y, newP);

                    }else if (piece instanceof Knight) {

                        newP = new Knight(col);
                        boxes[x][y] = new Spot(x, y, newP);

                    }

                    if(piece.hasMoved()){
                        newP.setMoved(true);
                    }

                    if (piece.isWhite()) {
                        white.add(newP);
                    } else {
                        black.add(newP);
                    }
                } else { // If there is no piece on the other spot
                    // Create an empty spot on the current board
                    boxes[x][y] = new Spot(x, y, null);
                }
            }
        }
    }

    public ArrayList<Spot> getAllAttackedSpots(boolean isWhite) {

        ArrayList<Piece> pieces;


        if(isWhite){
            pieces = white;
        }else{
            pieces = black;
        }

        ArrayList<Spot> attackedSpotsList = new ArrayList<>();
        for (Piece piece : pieces) {
            Spot spot = piece.findSpot(this);
            if(spot == null){
                continue;
            }
            if (piece != null && piece.isWhite() == isWhite) {
                // Get attacked spots for the current piece
                ArrayList<Spot> attackedSpots = piece.getAttackedSpots(this);
                attackedSpotsList.addAll(attackedSpots);
            }
        }


        return attackedSpotsList;
    }
    public void setSpotControl(){
        for(Piece p : black){
            if(p.findSpot(this) == null){
                continue;
            }
            ArrayList<Spot> attacked = p.getAttackedSpots(this);
            for (Spot spot: attacked) {
                spot.addNoAttacked(-1);
            }
        }
        for(Piece p : white){
            if(p.findSpot(this) == null){
                continue;
            }
            ArrayList<Spot> attacked = p.getAttackedSpots(this);
            for (Spot spot: attacked) {
                spot.addNoAttacked(1);
            }
        }
    }

    public void resetSpotControl(){
        for (int x = 0 ; x < 8 ; x++) {
            for (int y = 0; y < 8; y++) {
                boxes[x][y].resetNoAttacked();
            }
        }
    }

    public void resetDefValue(){
        for(Piece p : black){
            if(p.findSpot(this) == null){
                continue;
            }
            p.addDefValue(-p.getDefValue());
        }
        for(Piece p : white){
            if(p.findSpot(this) == null){
                continue;
            }
            p.addDefValue(-p.getDefValue());
        }
    }
    public Spot findKing( boolean isWhite) {
        // Iterate through all spots on the board to find the king's position
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Spot spot = getBox(i, j);
                Piece piece = spot.getPiece();
                if (piece instanceof King && piece.isWhite() == isWhite) {
                    return spot;
                }
            }
        }

        return null; // King not found (this should never happen in a valid chess game)
    }

    public boolean isKingInDanger(boolean isWhite) {
        Spot kingSpot = findKing(isWhite);
        ArrayList<Spot> attackedSpots = getAllAttackedSpots(!isWhite);

        for (Spot attackedSpot : attackedSpots) {
            if (attackedSpot == kingSpot) {
                return true;
            }
        }



        return false;
    }

    public ArrayList<Spot[]> getAllPossibleMoves(boolean isWhite){

        ArrayList<Piece> pieces;

        if(isWhite){
            pieces = white;
        }else{
            pieces = black;
        }

        ArrayList<Spot[]> allSpots = new ArrayList<>();
        for (Piece p : pieces) {
            Spot start = p.findSpot(this);
            if(start == null){
                continue;
            }
            for (int k = 0; k < 8; k++) {
                for (int l = 0; l < 8; l++) {
                    Spot end = getBox(k, l);
                    if (ApproveMove(start, end)) {
                        Spot[] move = new Spot[2];
                        move[0] = start;
                        move[1] = end;
                        allSpots.add(move);
                    }
                    if(p instanceof Pawn){
                        ((Pawn) p).setJustEnPassed(false);
                    }

                }
            }
        }


        return allSpots;
    }



    public boolean ApproveMove(Spot start, Spot end ){

        Piece pieceToMove = start.getPiece();
        Piece pieceToReplace = end.getPiece();

        if(pieceToMove == null){
            return false;
        }

        if(start.getPiece() instanceof King){
            ArrayList<Spot> attackedSpots = getAllAttackedSpots(!pieceToMove.isWhite());
            if(((King) start.getPiece()).isValidCastling(attackedSpots,this,start,end)){

                return true;
            }
        }

        if(!start.getPiece().canMove(this,start,end)){
            //error = 2;
            return false;
        }

        start.setPiece(null);
        end.setPiece(pieceToMove);

        if(isKingInDanger(pieceToMove.isWhite())){
            start.setPiece(pieceToMove);
            end.setPiece(pieceToReplace);
            //error = 3;
            return false;
        }else{
            start.setPiece(pieceToMove);
            end.setPiece(pieceToReplace);
        }



        return true;
    }
    public boolean isNoMoreMoves(boolean turn) {



        ArrayList<Spot[]> allSpots = getAllPossibleMoves(turn);

        if(allSpots.isEmpty()){

            return true;
        }

        return false;
    }


}
