
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Agent {

    private Game game;
    public boolean isWhite;
    private Spot [] chosenMove;
    private Random rand = new Random();

    private float  stats[] = {0,0,0,0,0,0,0,0};
    private float mul[];
    public float pastEval = 0 ,currentEval;

    Game.Method method;

    public Agent(Game game, boolean isWhite, float mul[] , Game.Method method){
        this.game = game;
        this.isWhite = isWhite;
        this.mul = mul;
        this.method = method;
    }

    public float evaluate(Board board, boolean attach) {

        float totalMaterial = 0;
        float centerControl = 0;
        float offence = 0;
        float defence = 0;
        float mobility = 0;
        float tropism = 0;
        float safety = 0;
        float structure = 0;



        board.resetDefValue();
        board.resetSpotControl();
        board.setSpotControl();
        ArrayList<Spot> whiteAttacks = board.getAllAttackedSpots( true);
        ArrayList<Spot> blackAttacks = board.getAllAttackedSpots( false);



        mobility -= blackAttacks.size() * 0.1f;
        mobility += whiteAttacks.size() * 0.1f;


        for (Piece piece : board.black) {
            Spot spot = piece.findSpot(board);
            if(piece.isKilled()) defence += piece.getValue() * 5;
            if (spot == null) continue;
            ArrayList<Spot> attackedSpots = piece.getAttackedSpots(board);
            totalMaterial -= calculatePieceValue(piece, board.black.size(),board);
            offence -= calculateADValue(piece, whiteAttacks, attackedSpots, board);
            centerControl -= calculateCenter(piece,attackedSpots, board.black.size(),board);
            tropism -= calculateKingTropism(piece,attackedSpots,true,board);
            if(piece instanceof Pawn){
                structure -= calculatePawnStructure(piece,attackedSpots,board);
            }

        }
        for (Piece piece : board.white) {
            Spot spot = piece.findSpot(board);
            if(piece.isKilled()) defence -= piece.getValue() * 5;
            if (spot == null) continue;
            ArrayList<Spot> attackedSpots = piece.getAttackedSpots(board);
            totalMaterial += calculatePieceValue(piece, board.white.size(),board);
            offence += calculateADValue(piece, blackAttacks, attackedSpots, board);
            centerControl += calculateCenter(piece,attackedSpots, board.white.size(),board);
            tropism += calculateKingTropism(piece,attackedSpots,false,board);
            if(piece instanceof Pawn){
                structure += calculatePawnStructure(piece,attackedSpots,board);
            }
        }

        safety += calculateKingSafety(blackAttacks ,true,board);
        safety -= calculateKingSafety(whiteAttacks,false,board);


        float wdef = 0 ,bdef = 0;
        for (Piece piece : board.black) {
            Spot spot = piece.findSpot(board);
            if (spot == null) continue;

            piece.addDefValue(-spot.getNoAttacked());
            bdef += piece.getDefValue();

            if(piece instanceof Pawn ){
                structure -= spot.getY() ;
            }

        }
        for (Piece piece : board.white) {
            Spot spot = piece.findSpot(board);
            if (spot == null) continue;

            piece.addDefValue(spot.getNoAttacked());
            wdef += piece.getDefValue();

            if(piece instanceof Pawn ){
                structure += 8 - spot.getY() ;
            }

        }

        defence -= bdef;
        defence += wdef;

/*
        System.out.println(totalMaterial);
        System.out.println(offence);
        System.out.println(defence);
        System.out.println(centerControl);
        System.out.println(mobility);


        System.out.println(safety);
        System.out.println(tropism);
        System.out.println("ende" + structure);

 */


        if(attach) {
            stats[0] =totalMaterial * mul[0];
            stats[1] =centerControl * mul[1];
            stats[2] =offence * mul[2];
            stats[3] =defence * mul[3];
            stats[4] =mobility * mul[4];
            stats[5] =tropism * mul[5];
            stats[6] =safety * mul[6];
            stats[7] =structure * mul[7];
        }




        return totalMaterial * mul[0]
                + centerControl * mul[1]
                + offence * mul[2]
                + defence * mul[3]
                + mobility * mul[4]
                + tropism * mul[5]
                + safety * mul[6]
                + structure * mul[7];

    }





    public void Learn(boolean isWhite, float thresh){
        if(!isWhite){
            if(stats[0] >= thresh){
                mul[0] += 1f;
            }

            if(stats[1] >= thresh){
                mul[1] += 1f;
            }

            if(stats[2] >= thresh){
                mul[2] += 1f;
            }

            if(stats[3] >= thresh){
                mul[3] += 1f;
            }

            if(stats[4] >= thresh){
                mul[4] += 1f;
            }

            if(stats[5] >= thresh){
                mul[5] += 1f;
            }
            if(stats[6] >= thresh){
                mul[6] += 1f;
            }
            if(stats[7] >= thresh){
                mul[7] += 1f;

            }
        }else{
            thresh *= -1;
            if(stats[0] <= thresh){
                mul[0] += 1f;

            }

            if(stats[1] <= thresh){
                mul[1] += 1f;


            }

            if(stats[2] <= thresh){
                mul[2] += 1f;


            }

            if(stats[3] <= thresh){
                mul[3] += 1f;


            }

            if(stats[4] <= thresh){
                mul[4] += 1f;

            }

            if(stats[5] <= thresh){
                mul[5] += 1f;

            }
            if(stats[6] <= thresh){
                mul[6] += 1f;

            }
            if(stats[7] <= thresh){
                mul[7] += 1f;
            }
        }

        for (int i = 0; i < mul.length; i++) {
            System.out.println(mul[i]);
        }


    }

    public void printStats(){
        for (int i = 0; i < stats.length; i++) {
            System.out.println(i + "/" + stats[i]);
        }
    }


    private float calculatePieceValue(Piece piece, int size,Board board) {
        Spot spot = piece.findSpot(board);
        float value;
        if (spot == null) return 0;

        int sx = spot.getX();
        int sy = spot.getY();

        value = piece.getValue() + (piece.getBoardValues()[sx][sy] * 0.05f * size);


        return value ;
    }

    private float calculateADValue(Piece piece, ArrayList<Spot> attackedSpotsList , ArrayList<Spot> pieceAttacks, Board board) {
        Spot spot = piece.findSpot(board);
        if (spot == null) return 0;

        float multiplier = 0;
        for (Piece attackedPiece : piece.getAttackedPieces(pieceAttacks)) {

            boolean alrMul = false;

            if (!attackedPiece.isDefended(attackedSpotsList) ) {
                multiplier += attackedPiece.getValue() * 0.3f;
                attackedPiece.addDefValue(-attackedPiece.getValue() * 8);
                alrMul = true;
            }

            if (attackedPiece.getValue() > piece.getValue()) {
                if (!alrMul ) {
                    multiplier += attackedPiece.getValue() * 0.15f;
                }
                attackedPiece.addDefValue( (piece.getValue() - attackedPiece.getValue()) * 4 );
            }

        }
        return (1.5f * multiplier);
    }



    private float calculateCenter(Piece piece,ArrayList<Spot> pieceAttacks ,  int size,Board board) {
        float centerControl = 0;
        Spot spot = piece.findSpot(board);
        if (spot == null) return 0;

        Spot[] center = {board.boxes[3][3] , board.boxes[4][4] , board.boxes[3][4], board.boxes[4][3]};

        for (Spot C: center) {

            for (Spot as:pieceAttacks) {
                if (C == as){
                    centerControl += 1;
                }

            }

            if(spot == C){
                centerControl += 1;
            }

        }

        return centerControl + (0.1f * size);
    }


    private float calculateMobility(ArrayList<Spot> whiteAttacks,ArrayList<Spot> blackAttacks,Board board) {

        float mobility = 0;
        ArrayList<Spot[]> possibleListW = board.getAllPossibleMoves(true);
        ArrayList<Spot[]> possibleListB = board.getAllPossibleMoves(false);

        mobility += possibleListW.size();
        mobility -= possibleListB.size();

        for (Spot[] move: possibleListW) {
            for (Spot s : blackAttacks) {
                if(move[1] == s){
                    mobility -= 1;
                }
            }
        }

        for (Spot[] move: possibleListB) {
            for (Spot s : whiteAttacks) {
                if(move[1] == s){
                    mobility += 1;
                }
            }
        }

        return mobility;
    }

    private float calculateKingSafety(  ArrayList<Spot> attackedSpots, boolean isWhite,Board board) {
        Spot spot = board.findKing(isWhite);
        if (spot == null) return 0;

        float safety = 0;

        ArrayList<Spot> KingRange = spot.getPiece().getAttackedSpots(board);

        for (Spot y: KingRange) {

            if(isWhite) {
                safety += y.getNoAttacked();
            }else{
                safety -= y.getNoAttacked();
            }

            if(y.getPiece() == null) {
                safety -= 1;
                if(!board.ApproveMove(spot,y)){
                    safety -= 6 ;
                }
            }
        }

        for (Spot x: attackedSpots) {
            if (x == spot){
                safety -= 12;
                if(board.getAllPossibleMoves(isWhite).size() == 0){
                    safety -= Float.POSITIVE_INFINITY;
                }
            }
        }

        if(((King)spot.getPiece()).isCastlingDone()){
            safety += 12f;

        }

        return safety;
    }

    private float calculateKingTropism(Piece piece, ArrayList<Spot> pieceAttacks , boolean isWhite,Board board) {
        Spot spot = board.findKing(isWhite);
        if (spot == null) return 0;
        float tropism = 0;
        ArrayList<Spot> KingRange = spot.getPiece().getAttackedSpots(board);


        for (Spot x: pieceAttacks) {
            for (Spot y : KingRange) {

                if (x == y){
                    tropism += piece.getValue() * 0.6f;
                }
            }

            if (x == spot){
                tropism += 12;
                if(board.getAllPossibleMoves(isWhite).size() == 0){
                    tropism += Float.POSITIVE_INFINITY;
                }
            }
        }

        return tropism;
    }

    private float calculatePawnStructure(Piece piece, ArrayList<Spot> pieceAttacks,Board board ) {
        Spot spot = piece.findSpot(board);
        if (spot == null) return 0;

        int sx = spot.getX();


        float structure = 0;

        ArrayList<Piece> pieces;
        if(piece.isWhite()){
            pieces = board.white;
        }else{
            pieces = board.black;
        }


        for (Spot x: pieceAttacks) {
            Piece p = x.getPiece();
            if(p != null){
                if(x.getPiece().isWhite() == p.isWhite() && p instanceof Pawn){
                    structure += 1f;
                }
            }
        }

        for (Piece p: pieces) {

            if(p instanceof Pawn && p != piece ){
                Spot pawnSpot = p.findSpot(board);
                if(pawnSpot == null) continue;
                if(pawnSpot.getX() == sx){
                    structure -= 3;
                }
            }

        }

        return structure;
    }


    private void sortPossibleByTrade(ArrayList<Spot[]> possibleList, ArrayList<Spot> attackedList) {

        Collections.sort(possibleList, new Comparator<Spot[]>() {
            @Override
            public int compare(Spot[] move1, Spot[] move2) {

                boolean def1 = true;
                boolean def2 = true;


                for (Spot s : attackedList) {
                    if (move1[1] == s) {
                        if (move1[1].getPiece() != null) {
                            if (move1[0].getPiece().getValue() > move1[1].getPiece().getValue()) {
                                def1 = false;
                            }
                        } else {
                            def1 = false;
                        }

                    }
                    if (move2[1] == s) {
                        if (move2[1].getPiece() != null) {
                            if (move2[0].getPiece().getValue() > move2[1].getPiece().getValue()) {
                                def2 = false;
                            }
                        } else {
                            def2 = false;
                        }
                    }
                }
                // move1 should come before move2.
                if (def1 && !def2) {
                    return 1;
                }

                // move2 should come before move1.
                else if (!def1 && def2) {
                    return -1;
                }
                // If both are null or both are not null, maintain the original order.
                else {
                    return 0;
                }
            }
        });
    }

    private void sortPossibleByNoAttacked(ArrayList<Spot[]> possibleList, boolean isWhite) {

        Collections.sort(possibleList, new Comparator<Spot[]>() {
            @Override
            public int compare(Spot[] move1, Spot[] move2) {

                boolean def1 = false;
                boolean def2 = false;


                if (isWhite) {
                    if (move1[1].getNoAttacked() >= 0){
                        def1 = true;
                    }

                    if (move2[1].getNoAttacked() >= 0){
                        def2 = true;
                    }
                }else{
                    if (move1[1].getNoAttacked() <= 0){
                        def1 = true;
                    }

                    if (move2[1].getNoAttacked() <= 0){
                        def2 = true;
                    }
                }

                // move1 should come before move2.
                if (def1 && !def2) {
                    return 1;
                }

                // move2 should come before move1.
                else if (!def1 && def2) {
                    return -1;
                }
                // If both are null or both are not null, maintain the original order.
                else {
                    return 0;
                }

            }
        });
    }

    private boolean simulateExtendedMCTS(boolean turn, int depth , float goal , boolean isWhite, Board board){

        ArrayList<Spot[]> possibleList = board.getAllPossibleMoves(turn);


        if(possibleList.size() == 0){
            if(board.isKingInDanger(!isWhite)) {
                return true;
            }else{
                return false;
            }
        }
        if( evaluate(board,false) >= goal && isWhite){
            return true;
        }

        if( evaluate(board,false) <= goal && !isWhite){
            return true;
        }
        if(depth == 0 ){
            return false;
        }

        Spot[] randMove = possibleList.get(rand.nextInt(possibleList.size()));

        Piece randomPieceToMove = randMove[0].getPiece();
        Piece randomPieceToReplace = randMove[1].getPiece();
        boolean randomHasMoved = randomPieceToMove.hasMoved();

        makeMove(randMove,randomPieceToMove,randomPieceToReplace,board);
        boolean result = simulateExtendedMCTS(!turn, depth - 1,goal,isWhite,board);
        undoMove(randMove,randomPieceToMove,randomPieceToReplace,randomHasMoved,board);

        return result;
    }

    private float extendedMCTS(Spot[] move,int depth , float goal,int simulations, Board board) {
        int hits = 0;

        for (int i = 0; i < simulations; i++) {


            // Make the move in the simulated game
            Piece pieceToMove = move[0].getPiece();
            Piece pieceToReplace = move[1].getPiece();
            boolean hasMoved = pieceToMove.hasMoved();
            makeMove(move, pieceToMove, pieceToReplace,board);

            // Check if simulated game resulted in a win
            if (simulateExtendedMCTS(!isWhite,depth,goal,isWhite,board)) {
                hits++;
            }

            // Undo the move in the simulated game
            undoMove(move, pieceToMove, pieceToReplace, hasMoved,board);
        }

        return (float) hits / simulations;
    }



    // Implement the minimax algorithm with alpha-beta pruning
    public float minimax(int depth, float alpha, float beta, boolean maximizingPlayer, Board board) {
        if (depth == 0 ) {
            return evaluate(board,false);
        }

        ArrayList<Spot[]> possibleList = board.getAllPossibleMoves(maximizingPlayer);
        ArrayList<Spot> attackedList = board.getAllAttackedSpots(!maximizingPlayer);

        if (possibleList.size() == 0) {
            if(board.isKingInDanger(maximizingPlayer)){
                if(maximizingPlayer) {
                    return Float.NEGATIVE_INFINITY;
                }else{
                    return Float.POSITIVE_INFINITY;
                }
            }else{
                return 0;
            }
        }

        sortPossibleByTrade(possibleList,attackedList);

        if (maximizingPlayer) {
            float maxEval = Float.NEGATIVE_INFINITY;
            for (Spot[] move : possibleList) {
                Piece pieceToMove = move[0].getPiece();
                Piece pieceToReplace = move[1].getPiece();
                boolean hasMoved = pieceToMove.hasMoved();
                makeMove(move,pieceToMove, pieceToReplace,board);
                float eval = minimax(depth - 1, alpha, beta, false,board);
                undoMove(move,pieceToMove, pieceToReplace,hasMoved,board);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxEval;
        } else {
            float minEval = Float.POSITIVE_INFINITY;
            for (Spot[] move : possibleList) {
                Piece pieceToMove = move[0].getPiece();
                Piece pieceToReplace = move[1].getPiece();
                boolean hasMoved = pieceToMove.hasMoved();
                makeMove(move,pieceToMove, pieceToReplace,board);
                float eval = minimax(depth - 1, alpha, beta, true,board);
                undoMove(move,pieceToMove, pieceToReplace,hasMoved,board);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return minEval;
        }
    }



    public Spot[] getMove(Board board) {

        if (method == Game.Method.MINIMAX) {
            ArrayList<Spot[]> possibleList = board.getAllPossibleMoves(isWhite);
            ArrayList<Spot> attackedList = board.getAllAttackedSpots(!isWhite);
            int range = possibleList.size();
            final float[] bestEval = new float[1];
            long timeTaken ;
            final int[] index = {0};


            if (isWhite) {
                bestEval[0] = Float.NEGATIVE_INFINITY;
            } else {
                bestEval[0] = Float.POSITIVE_INFINITY;
            }

            sortPossibleByTrade(possibleList, attackedList);
            //board.resetSpotControl();
            //board.setSpotControl();
            //sortPossibleByNoAttacked(possibleList,isWhite);



            // Create an array to hold references to threads
            Thread[] threads = new Thread[range];
            timeTaken = System.currentTimeMillis();

            for (int i = 0; i < range; i++) {
                final int currentIndex = i; // Final variable for use in lambda


                // Create a new thread for each move evaluation
                threads[i] = new Thread(() -> {
                    Board temp = new Board();
                    temp.mapBoard(board);

                    Spot [] move = new Spot[2];
                    Spot[] tempMove = possibleList.get(currentIndex);
                    for (int j = 0; j < tempMove.length; j++) {
                        move[j] = temp.boxes[tempMove[j].getX()][tempMove[j].getY()];
                    }


                    Piece pieceToMove = move[0].getPiece();
                    Piece pieceToReplace = move[1].getPiece();
                    boolean hasMoved = pieceToMove.hasMoved();

                    makeMove(move, pieceToMove, pieceToReplace, temp);

                    float eval = minimax(3, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, !isWhite, temp);

                    //System.out.println("GGAAADD " + eval + " / " + move[1].getX() + " / " + move[1].getY());

                    undoMove(move, pieceToMove, pieceToReplace, hasMoved, temp);

                    synchronized (this) {
                        if ((isWhite && eval > bestEval[0]) || (!isWhite && eval < bestEval[0])) {
                            bestEval[0] = eval;
                            index[0] = currentIndex;
                        }
                    }
                });

                threads[i].start();
            }

            // Wait for all threads to finish

            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }





            timeTaken = System.currentTimeMillis() - timeTaken;

            System.out.println(bestEval[0]);
            System.out.println(timeTaken);

            currentEval = bestEval[0];
            chosenMove = possibleList.get(index[0]);

            //System.out.println(chosenMove[0].getX() + "/" + chosenMove[0].getY() + "/" + chosenMove[1].getX() + "/" + chosenMove[1].getY() + "/");

            return chosenMove;
        } else if (method == Game.Method.MCTS) {
            ArrayList<Spot[]> possibleList = board.getAllPossibleMoves(isWhite);
            chosenMove = possibleList.get(0); // Default move in case there are no possible moves
            float bestScore = Float.NEGATIVE_INFINITY;
            long timeTaken ;

            timeTaken = System.currentTimeMillis();

            if (!possibleList.isEmpty()) {
                for (Spot[] move : possibleList) {
                    float score = extendedMCTS(move,20,-50f,1,board);
                    if (score > bestScore) {
                        bestScore = score;
                        chosenMove = move;
                    }
                }
            }

            timeTaken = System.currentTimeMillis() - timeTaken;
            System.out.println(timeTaken);
            System.out.println(bestScore);



            return chosenMove;

        }

        return null;
    }



    private void makeMove(Spot[] move,Piece pieceToMove,Piece pieceToReplace, Board board) {


        Spot start = move[0];
        Spot end = move[1];


        if(pieceToMove instanceof King && pieceToReplace instanceof Rook && pieceToMove.isWhite() == pieceToReplace.isWhite()) {

            ((King) pieceToMove).fauxCastled = true;
            pieceToMove.setMoved(true);
            pieceToReplace.setMoved(true);
            ((King) pieceToMove).setCastlingDone(true);
            ((King) pieceToMove).Castle(board, start, end);
            //System.out.println("tried to castle " + start.getX() + " : "+ start.getY() + " / " + end.getX()+ " : "+ end.getY());
            //System.out.println("tried to castle ");

            return;

        }

        else if(pieceToMove instanceof Pawn && end.getX() != start.getX() && pieceToReplace == null ){
            //System.out.println("tried to en passant");
            //System.out.println("tried to en passant " + start.getX() + " : "+ start.getY() + " / " + end.getX()+ " : "+ end.getY());
            if(pieceToMove.isWhite()) {
                board.white.remove(board.boxes[end.getX()][end.getY()+1].getPiece());
                board.boxes[end.getX()][end.getY()+1].setPiece(null);
            }else{
                board.black.remove(board.boxes[end.getX()][end.getY()-1].getPiece());
                board.boxes[end.getX()][end.getY()-1].setPiece(null);

            }
            ((Pawn) start.getPiece()).setJustEnPassed(true);

        }


        else if(pieceToMove instanceof Pawn && (end.getY() == 0 || end.getY() == 7)){
            //System.out.println("tried to promote");
            if(pieceToMove.isWhite()){
                board.white.remove(pieceToMove);
                pieceToMove = new Queen(true);
                board.white.add(pieceToMove);
            }else{
                board.black.remove(pieceToMove);
                pieceToMove = new Queen(false);
                board.black.add(pieceToMove);
            }
        }

        start.setPiece(null);
        end.setPiece(pieceToMove);

        if (pieceToReplace != null) {
            pieceToReplace.setKilled(true);
        }
        pieceToMove.setMoved(true);

    }

    private void undoMove(Spot[] move,Piece pieceToMove,Piece pieceToReplace, boolean hasMoved, Board board) {
        Spot start = move[0];
        Spot end = move[1];


        if(pieceToMove instanceof King && pieceToReplace instanceof Rook && pieceToMove.isWhite() == pieceToReplace.isWhite()) {

            if (((King) pieceToMove).fauxCastled) {
                //System.out.println("tried to un castle");
                if (pieceToMove.isWhite()) {
                    Spot kingSpot = board.findKing(pieceToMove.isWhite());
                    kingSpot.setPiece(null);
                    if (end.getX() == 0 && end.getY() == 7) {
                        board.boxes[3][7].setPiece(null);
                    } else {
                        board.boxes[5][7].setPiece(null);
                    }
                } else {
                    Spot kingSpot = board.findKing(pieceToMove.isWhite());
                    kingSpot.setPiece(null);
                    if (end.getX() == 0 && end.getY() == 0) {
                        board.boxes[3][0].setPiece(null);
                    } else {
                        board.boxes[5][0].setPiece(null);
                    }
                }
                ((King) pieceToMove).setCastlingDone(false);
                pieceToReplace.setMoved(false);
                ((King) pieceToMove).fauxCastled = false;
            }
        }


        else if(pieceToMove instanceof Pawn && (end.getY() == 0 || end.getY() == 7)){
            //System.out.println("tried to unpromote");
            if(pieceToMove.isWhite()){
                board.white.remove(pieceToMove);
                pieceToMove = new Pawn(true);
                board.white.add(pieceToMove);
            }else{
                board.black.remove(pieceToMove);
                pieceToMove = new Pawn(false);
                board.black.add(pieceToMove);
            }

            pieceToMove.setMoved(true);
        }






        start.setPiece(pieceToMove);
        end.setPiece(pieceToReplace);

        if (pieceToReplace != null) {
            pieceToReplace.setKilled(false);
        }

        if (pieceToMove instanceof Pawn && end.getX() != start.getX() && pieceToReplace == null) {
            //System.out.println("tried to un enpassant");
            ((Pawn) pieceToMove).setJustEnPassed(false);

            int capturedPawnX = end.getX();
            int capturedPawnY = start.getY();

            Pawn pawn = new Pawn(!pieceToMove.isWhite());
            // Restore the captured pawn to its original position
            board.boxes[capturedPawnX][capturedPawnY].setPiece(pawn);

            if(pawn.isWhite()){
                board.white.add(pawn);
            }else{
                board.black.add(pawn);
            }
            pawn.setMoved(true);

        }

        if(!hasMoved) {
            pieceToMove.setMoved(false);
        }

    }


    public void setMul(float[] generateRandomMulArray) {
        this.mul = generateRandomMulArray;
    }
    public String getMulAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < mul.length; i++) {
            sb.append(mul[i]);
            if (i < mul.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public float[] getMul() {
        return this.mul;
    }
}

