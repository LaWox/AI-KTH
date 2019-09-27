import java.util.*;
import java.lang.Math;

public class AI{
    public static int depth = 4;

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

        /**
        // Try testing win/loss before going into depth search
        Move m = state.getMove();

        if(m.isXWin()){
            return 1000+depth;
        }

        else if(m.isOWin()){
            return -1;
        }
         */
        if (depth==0 || nextStates.size()==0){
            v = evalState(state);
        }

        // 2 is 'O' so if nextPlayer is 2 it means that the current player is 'X'
        else if (state.getNextPlayer() == 1){
            v=Double.NEGATIVE_INFINITY;

            for(GameState s: nextStates){
                v = Math.max(v, minMaxPruning(s,depth-1, alpha, beta));
                alpha = Math.max(alpha, v);
                if(beta <= alpha) {
                    return v;
                }
            }
        }
        // 1 is 'X' so if nextplayer is 1 it means that the current player is 'O'
        else if (state.getNextPlayer() == 2){
            v=Double.POSITIVE_INFINITY;

            for(GameState s: nextStates){
                v= Math.min(v,minMaxPruning(s,depth-1, alpha, beta));
                beta = Math.min(beta, v);
                if( beta <= alpha ) {
                    return v;
                }
            }
        }
        return v;
    }

    // eval a state and return its value
    private static int evalState(GameState state){
        Move m = state.getMove();
        int counter = 0;

        // return directly if we encouter win or loss
        if(m.isXWin()){
            return 10000;
        }
        else if(m.isOWin()){
            return -1;
        }

        Vector<Integer> points = new Vector<Integer>();
        points.setSize(state.BOARD_SIZE*2+2);
        int player;
        Collections.fill(points, 1);
        int scaling = 4;
        int value = 0;

        for(int row = 0; row < state.BOARD_SIZE; row ++){
            for(int col = 0; col < state.BOARD_SIZE; col ++){
                player = state.at(row, col);

                // add points to the vector, increase by a factor for every new entry
                if(player == 1){
                    points.set(row, points.get(row)*scaling);
                    points.add(state.BOARD_SIZE + col, points.get(state.BOARD_SIZE - 1 + col)*scaling);

                    // if it's in the first diagonal
                    if(counter%state.BOARD_SIZE+1 == 0){
                        points.add(state.BOARD_SIZE*2, points.get(state.BOARD_SIZE*2)*scaling);
                    }

                    // checking other diagonal
                    if(counter%state.BOARD_SIZE-1 == 0 && row+col != 0){
                        points.add(state.BOARD_SIZE*2 + 1, points.get(state.BOARD_SIZE*2)*scaling);
                    }
                }
                // if an enemy player is encoutered null the value of the row/col
                else if(player == 2){
                    points.set(row, 0);
                    points.set(state.BOARD_SIZE + col, 0);
                    if(counter%state.BOARD_SIZE+1 == 0){
                        points.add(state.BOARD_SIZE*2, 0);
                    }
                    if(counter%state.BOARD_SIZE-1 == 0){
                        points.add(state.BOARD_SIZE*2 + 1, 0);
                    }
                }
                counter++;
            }
        }

        // if we have nulled the point we don't count it for the value of the state
        for(Integer i: points){
            if(i > 1){
                value += i;
            }
        }
        //System.err.println("Value: " + value);
        return value;
    }

}
