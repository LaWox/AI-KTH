import java.util.*;
import java.lang.Math;

public class AI{
    public static int depth = 3;

    // return the "best" next gameState
    public static GameState getBestMove(Vector<GameState> states){
        int index = 0;
        double max = 0;
        double v = 0;

        // find state with max value and save its index
        for(int i = 0; i < states.size(); i++){
            v = minMaxPruning(states.get(i), depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            if(max < v){
                max = v;
                index = i;
            }
        }
        return states.get(index);
    }

    // minMax-alg with pruning, recursive
    private static double minMaxPruning(GameState state, int depth, double alpha, double beta){

        double v = 0;

        // A vector containing all possible children states if this current state
        Vector<GameState> nextStates = new Vector<GameState>();
        state.findPossibleMoves(nextStates);

        if (depth==0 || nextStates.size()==0){
            v = evalState(state);
        }

        // 2 is 'O' so if nextPlayer is 2 it means that the current player is 'X'
        else if (state.getNextPlayer() == 2){
            v=Double.NEGATIVE_INFINITY;

            for(GameState s: nextStates){
                v = Math.max(v, minMaxPruning(s,depth-1, alpha, beta));
                alpha = Math.max(alpha, v);
                if(beta <= alpha) {
                    break;
                }
            }
        }
        // 1 is 'X' so if nextplayer is 1 it means that the current player is 'O'
        else if (state.getNextPlayer() == 1){
            v=Double.POSITIVE_INFINITY;

            for(GameState s: nextStates){
                v= Math.min(v,minMaxPruning(s,depth-1, alpha, beta));
                beta = Math.min(beta, v);
                if( beta <= alpha ) {
                    break;
                }
            }
        }
        return v;
    }

    // eval a state and return its value
    private static int evalState(GameState state){
        Move m = state.getMove();

        if(m.isXWin()){
            return 100;
        }
        else if(m.isOWin()){
            return -1;
        }
        int player;
        Vector<Integer> points = new Vector<Integer>();
        points.setSize(state.BOARD_SIZE*2+2);
        Collections.fill(points, 1);
        int scaling = 3;
        int value = 0;

        // where there is an enemy pllayer in the way we want to set the value to zero
        Vector<Integer> obstructions = new Vector<Integer>();

        for(int row = 0; row < state.BOARD_SIZE; row ++){
            for(int col = 0; col < state.BOARD_SIZE; col ++){
                player = state.at(row, col);

                // add points to the vector, increase by a factor for every new entry
                if(player == 1){
                    points.set(row, points.get(row)*scaling);
                    points.add(state.BOARD_SIZE - 1 + col, points.get(state.BOARD_SIZE - 1 + col)*scaling);
                }

                // if an enemy player is encoutered null the value of the row/col
                else if(player == 2){
                    points.set(row, -100);
                    points.set(state.BOARD_SIZE - 1 + col, -100);
                }
            }
        }

        // if we have nulled the point we don't count it for the value of the state
        for(Integer i: points){
            if(i > 0){
                value += i;
            }
        }
        return value;
    }

}
