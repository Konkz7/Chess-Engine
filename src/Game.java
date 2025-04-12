import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    public Board board;

    private boolean playerColour = true;

    static boolean GAME_END = false;
    int turnNumber = 0;

    private Random random = new Random();

    private Boolean turn = true;

    public ChessBoardGUI chessBoard;

    private Agent bot1;
    private Agent bot2;

    private boolean whiteLearn = false;
    private boolean blackLearn = false;

    ArrayList<Spot> listOfmoves = new ArrayList<>();
    private int consecutiveWinsWhite = 0;
    private int consecutiveWinsBlack = 0;


    public enum GameResult {
        BLACK_WIN,
        WHITE_WIN,
        DRAW
    }

    private GameResult result;

    public enum Method {
        MCTS,
        MINIMAX
    }

    private Method method = Method.MINIMAX;

    public enum Mode{
        singleBotvBot,
        MultiBotvBot,
        UservBot
    }

    private Mode mode = Mode.UservBot;

    public Game(){
        this.board = new Board();
        //totalMaterial 0/ centerControl1 / offence2 / defence3 / mobility4 / tropism5 / safety6 / structure7
        float mul1[] = { 1 , 7 , 4 , 10 , 1, 5, 10 , 7};
        float mul2[] = { 1 , 1 , 1 , 1 , 1, 1, 1 , 1};
        bot1 = new Agent(this,false,mul1,method);
        bot2 = new Agent(this,true,mul2,method);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                chessBoard = new ChessBoardGUI(board,Game.this);
                // Additional code related to the GUI setup or interaction can go here
            }
        });
        if(mode == Mode.singleBotvBot){
            Play(bot1,bot2);
        } else if (mode == Mode.MultiBotvBot) {
            playXTimes(50);
        }

        if(mode == Mode.UservBot && playerColour == false){
            Spot[] move = bot2.getMove(board);
            move(move[0],move[1],true);
        }
    }


    public void move(Spot start, Spot end,boolean botMove) {

        Piece pieceToMove = start.getPiece();
        Piece pieceToReplace = end.getPiece();


        if(GAME_END){
            return;
        }

        if(pieceToMove.isWhite() != turn){
            return;
        }

        if (!board.ApproveMove(start, end)) {
            System.out.println("error");
            return;
        }



        // add kill function
        if ((start.getPiece() instanceof  King) && ((King) start.getPiece()).isValidCastling(board.getAllAttackedSpots(!turn), board,start,end)){
            ((King) start.getPiece()).setCastlingDone(true);
            end.getPiece().setMoved(true);
            ((King) start.getPiece()).Castle(board,start,end);
        } else if ( start.getPiece() instanceof Pawn && ((Pawn) start.getPiece()).isJustEnPassed() ) {
            ((Pawn) start.getPiece()).enPassant();
            start.setPiece(null);
            pieceToReplace.setKilled(true);
            end.setPiece(pieceToMove);
        } else{
            if(end.getPiece() != null) {
                end.getPiece().setKilled(true);
            }
            if(start.getPiece() instanceof Pawn && Math.abs(start.getY() - end.getY()) == 2){
                ((Pawn) start.getPiece()).setJustTwoStepped(true);
            }
            start.setPiece(null);
            end.setPiece(pieceToMove);
        }

        //resetting en passant flag
        Spot temp = null;

        if(turn){
            for (Piece p : board.black){
                if(p instanceof Pawn ){
                    if(((Pawn) p).isJustTwoStepped()) {
                        ((Pawn) p).setJustTwoStepped(false);
                        ((Pawn) p).setJustEnPassed(false);
                    }
                }
            }

            for (int x = 0; x < 8; x++) {
                if (board.boxes[x][0].getPiece() instanceof Pawn) {
                    if (!botMove) {
                        ((Pawn) board.boxes[x][0].getPiece()).Promotion(board.getBox(x, 0));
                        temp = board.getBox(x, 0);
                        break;
                    } else {
                        board.white.remove(board.boxes[x][0].getPiece());
                        pieceToMove = new Queen(true);
                        board.white.add(pieceToMove);
                        board.boxes[x][0].setPiece(pieceToMove);

                    }
                }

            }



            if(temp != null) {
                board.white.add(temp.getPiece());
            }

        }else{
            for (Piece p : board.white){
                if(p instanceof Pawn ){

                    if(((Pawn) p).isJustTwoStepped()) {
                        ((Pawn) p).setJustTwoStepped(false);
                        ((Pawn) p).setJustEnPassed(false);
                    }
                }
            }


            for (int x = 0; x < 8; x++) {
                if (board.boxes[x][7].getPiece() instanceof Pawn) {
                    if (!botMove) {
                        ((Pawn) board.boxes[x][7].getPiece()).Promotion(board.getBox(x, 7));
                        temp = board.getBox(x, 7);
                        break;
                    } else {
                        board.black.remove(board.boxes[x][7].getPiece());
                        pieceToMove = new Queen(false);
                        board.black.add(pieceToMove);
                        board.boxes[x][7].setPiece(pieceToMove);

                    }
                }

            }



            if(temp != null) {
                board.black.add(temp.getPiece());
            }

        }



        chessBoard.updateChessBoard();
        if(!turn) {
            turnNumber++;
            System.out.println(turnNumber);
        }



        if(!turn && !GAME_END && blackLearn){

            bot1.evaluate(board,true);

            bot1.printStats();

            if(bot1.isWhite){
                if(bot1.currentEval <= bot1.pastEval * 0.75f){
                    bot1.Learn(true,bot1.currentEval / 8);
                }
            }else{
                if(bot1.currentEval >= bot1.pastEval * 1.25f){
                    bot1.Learn(false,bot1.currentEval / 8);
                }
            }

            bot1.pastEval = bot1.currentEval;
        }else if (turn && !GAME_END && whiteLearn){

            bot2.evaluate(board,true);

            bot2.printStats();

            if(bot2.isWhite){
                if(bot2.currentEval <= bot2.pastEval * 0.5f){
                    bot2.Learn(true, bot2.currentEval / 8);
                }
            }else{
                if(bot2.currentEval >= bot2.pastEval * 1.5f){
                    bot2.Learn(false,bot2.currentEval / 8);
                }
            }

            bot2.pastEval = bot2.currentEval;
        }



        listOfmoves.add(start);
        listOfmoves.add(end);


        if (listOfmoves.size() == 24 && turn == false) {
            Spot[] history = new Spot[8];
            int counter = 0;

            for (int i = 0; i < 8; i++) {
                history[i] = listOfmoves.get(i);
            }

            for (int i = 1; i < 3; i++) {
                boolean same = true;

                for (int j = 0; j < 8; j++) {
                    if(listOfmoves.get((i*8) + j) != history[j] ){
                        same = false;
                    }
                }

                if(same){
                    counter ++;
                }else{
                    break;
                }
            }

            if(counter == 2){
                GAME_END = true;
                System.out.println("DRAW");
                result = GameResult.DRAW;
            }

            for (int i = 0; i < 8; i++) {
                listOfmoves.remove(0);
            }
        }

        setTurn(!turn);
        pieceToMove.setMoved(true);


        System.out.println(start.getX() + "/" +  start.getY()+ "/" + end.getX()+ "/" + end.getY());


        if (board.isNoMoreMoves(turn)) {
            GAME_END = true;

            boolean check2stale = false ;

            if (!board.isKingInDanger(turn)) {
                check2stale = true;
            }

            if (check2stale) {
                System.out.println("STALEMATE");
                result = GameResult.DRAW;
            } else {
                System.out.println("CHECKMATE");
                if(turn){
                    result = GameResult.WHITE_WIN;
                    turnNumber ++;
                }else{
                    result = GameResult.BLACK_WIN;
                }
            }
        }



        if(mode == Mode.UservBot){
            if(turn!= playerColour && !GAME_END){

                if(playerColour) {
                    Spot[] move = bot1.getMove(board);
                    move(move[0], move[1], true);
                }else{
                    Spot[] move = bot2.getMove(board);
                    move(move[0], move[1], true);
                }

            }
        }

        if(turnNumber > 100 && mode == Mode.MultiBotvBot){
            GAME_END = true;
            result = GameResult.DRAW;
        }

    }

    private void Play(Agent black, Agent white){

        while(!GAME_END) {
            Spot[] move1 = white.getMove(board);
            move(move1[0], move1[1], true);

            if (GAME_END) {
                break;
            }

            Spot[] move2 = black.getMove(board);
            move(move2[0], move2[1], true);
        }
    }

    private void setTurn(Boolean turn) {
        this.turn = turn;
    }


    // Method to randomly generate a new mul[] array
    private float[] generateRandomMulArray() {
        float[] mul = new float[8];
        for (int i = 0; i < mul.length; i++) {
            int[] numbers = {1,10,0,5};
            int choice  = random.nextInt(4);
            mul[i] = numbers[choice];
            //mul[i] = random.nextFloat() * 20;
        }
        return mul;
    }

    // Method to play the game 100 times with random mul[] arrays
    public void playXTimes(int x) {
        float[] mulR = {1,1,1,1,1,1,1,1};

        for (int i = 0; i < x; i++) {

            // Play the game
            Play(bot1, bot2);

            if (result == GameResult.WHITE_WIN) {
                consecutiveWinsWhite ++;
                consecutiveWinsBlack = 0;
                writeGameResultToFile(result);
                bot1.setMul(generateRandomMulArray());

            } else if(result == GameResult.BLACK_WIN){
                consecutiveWinsBlack ++;
                consecutiveWinsWhite = 0;
                writeGameResultToFile(result);
                bot2.setMul(generateRandomMulArray());

            }else{
                if(consecutiveWinsBlack > consecutiveWinsWhite){
                    consecutiveWinsWhite = 0;
                    writeGameResultToFile(result);
                    bot2.setMul(generateRandomMulArray());

                } else if(consecutiveWinsBlack < consecutiveWinsWhite){
                    consecutiveWinsBlack = 0;
                    writeGameResultToFile(result);
                    bot1.setMul(generateRandomMulArray());
                }else{
                    consecutiveWinsBlack = 0;
                    consecutiveWinsWhite = 0;
                    writeGameResultToFile(result);
                    bot1.setMul(generateRandomMulArray());
                    bot2.setMul(generateRandomMulArray());

                }
            }


            // Reset the game state for the next iteration
            resetGame();
        }
    }

    // Method to reset the game state
    private void resetGame() {
        board.white.clear();
        board.black.clear();
        board.setupBoard(); // Reset the board to its initial state
        GAME_END = false; // Reset the game end flag
        listOfmoves.clear(); // Clear the list of moves
        turn = true; // Reset the turn
        turnNumber = 0; // Reset the turn number
    }

    // Method to write the game result to a file
    private void writeGameResultToFile(GameResult result) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("chess_results.txt", true))) {
            writer.write("Winner: " + result.name());
            writer.newLine();
            writer.write("Mul values:");
            writer.newLine();
            writer.write("Bot 1 (Black): " + bot1.getMulAsString());
            writer.newLine();
            writer.write("Bot 2 (White): " + bot2.getMulAsString());
            writer.newLine();
            writer.write("Consecutive Wins:");
            writer.newLine();
            writer.write("Black: " + consecutiveWinsBlack);
            writer.newLine();
            writer.write("White: " + consecutiveWinsWhite);
            writer.newLine();
            writer.write("Turns " + turnNumber);
            writer.newLine();
            writer.write("---------------------------------------");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }








}
