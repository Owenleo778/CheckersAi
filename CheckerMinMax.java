

// Doesn't seem to take any notice of best move, tends to move right most piece only (unless has to make jump)


import java.util.ArrayList;
import java.util.Collections;

/**
 * An AI using min-max and alpha beta-pruning to play checkers. Always takes the role of black
 */
public class CheckerMinMax {

    private final static int maxDepth = 6;
    private Board board;

    public CheckerMinMax(Board b){
        board = b;
    }

    public void takeTurn(BoardState bS){
        BoardState b1 = null;
        int v = -1001;
        ArrayList<BoardState> moves = board.allMoves(bS,Board.AIPLAYER);
        Collections.shuffle(moves);
        for (BoardState b2 : moves){
            int temp = Integer.max(v, minValue(b2, 1));
            if (v < temp){
                v = temp;
                b1 = b2;
            }
        }
        System.out.println(v);
        // Sometimes b1 is never overwritten? Look into,
        //causes null pointer exception \/
        //Can't move kings
        board.copyState(b1);
    }

    private int maxValue(BoardState bS, int depth){
        if (depth >= maxDepth){
            return evaluation(bS);
        }
        Integer v = -1000;
        ArrayList<BoardState> moves = board.allMoves(bS,Board.AIPLAYER);
        for (BoardState b : moves){
            v = Integer.max(v, minValue(b, depth + 1));
        }
        return v;
    }

    private int minValue(BoardState bS, int depth){
        if (depth >= maxDepth){
            return evaluation(bS);
        }
        Integer v = 1000;
        ArrayList<BoardState> moves = board.allMoves(bS,Board.AIPLAYER * -1);
        for (BoardState b : moves){
            v = Integer.min(v, maxValue(b, depth + 1));

        }
        return v;
    }

    private int evaluation(BoardState b) {
        int goalTotal = 0;
        int avoidTotal = 0;
        if (b != null) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    Piece p = b.board[x][y];
                    if (p != null) {
                        if (p.isKing()) {
                            if (p.getColour() == Board.AIPLAYER) {
                                goalTotal++;
                                goalTotal++;
                            } else {
                                avoidTotal--;
                                avoidTotal--;
                            }
                        } else {
                            if (p.getColour() == Board.AIPLAYER) {
                                goalTotal++;
                            } else {
                                avoidTotal--;
                            }
                        }
                    }
                }
            }
        } else {
            System.out.println("b is null");
            return -1000;
        }
        if (avoidTotal == 0){
            return 1002;
        }
        if (goalTotal == 0){
            return -1002;
        }
        return avoidTotal + goalTotal;
    }

}