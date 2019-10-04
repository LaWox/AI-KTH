import java.util.*;
import java.lang.Math;

public class AI3D{
    public static int depth = 0;

    // return the "best" next gameState
    public static GameState getBestMove(Vector<GameState> states){
        int index = 0;
        double max = 0;
        double v = 0;
        // Array to store points in
        int[][] points = new int[states.get(0).BOARD_SIZE*2][states.get(0).BOARD_SIZE*2+2];
        // find state with max value and save its index
        for(int i = 0; i < states.size(); i++){
            v = minMaxPruning(states.get(i), depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, points);
            if(max < v){
                max = v;
                index = i;
            }
        }
        return states.get(index);
    }

    // minMax-alg with pruning, recursive
    private static double minMaxPruning(GameState state, int depth, double alpha, double beta, int[][] points){

        double v = 0;

        // A vector containing all possible children states if this current state
        Vector<GameState> nextStates = new Vector<GameState>();
        state.findPossibleMoves(nextStates);

        if (depth==0 || nextStates.size()==0){
            v = evalState(state, points);
        }

        // 2 is 'O' so if nextPlayer is 2 it means that the current player is 'X'
        else if (state.getNextPlayer() == 1){
            v=Double.NEGATIVE_INFINITY;

            for(GameState s: nextStates){
                v = Math.max(v, minMaxPruning(s,depth-1, alpha, beta, points));
                alpha = Math.max(alpha, v);

                // if we find win
                if(v == 100000){
                    return 1000000;
                }
                if(beta <= alpha){
                    return v;
                }
            }
        }
        // 1 is 'X' so if nextplayer is 1 it means that the current player is 'O'
        else if (state.getNextPlayer() == 2){
            v=Double.POSITIVE_INFINITY;

            for(GameState s: nextStates){
                v= Math.min(v,minMaxPruning(s,depth-1, alpha, beta, points));
                beta = Math.min(beta, v);
                if(beta <= alpha || v == 0){
                    return v;
                }
            }
        }
        return v;
    }

    // eval a state and return its value
    private static int evalState(GameState state, int[][] points){
        int counter = 0;
        // TODO: create this once instead
        int player;
        for(int[] arr: points){
            Arrays.fill(arr, 1);
        }
        int scaling = 10;
        int value = 0;

        for(int layer = 0; layer < state.BOARD_SIZE; layer ++){
            counter = 0;
            for(int row = 0; row < state.BOARD_SIZE; row ++){
                for(int col = 0; col < state.BOARD_SIZE; col ++){
                    player = state.at(row, col, layer);
                    // add points to the vector, increase by a factor for every new entry
                    if(player == 1){
                        points[layer][row] *= scaling;
                        points[layer][state.BOARD_SIZE + col] *= scaling;
                        points[col + state.BOARD_SIZE][row] *= scaling;
                        points[col + state.BOARD_SIZE][layer] *= scaling;

                        // if it's in the first diagonal
                        if(counter%state.BOARD_SIZE+1 == 0){
                            points[layer][state.BOARD_SIZE*2] *= scaling;
                        }

                        // checking other diagonal
                        if(counter%state.BOARD_SIZE-1 == 0 && row+col != 0){
                            points[layer][state.BOARD_SIZE*2 + 1] *= scaling;
                        }
                    }
                    // if an enemy player is encoutered null the value of the row/col
                    else if(player == 2){
                        points[layer][row] = 0;
                        points[layer][state.BOARD_SIZE + col] = 0;

                        if(counter%state.BOARD_SIZE+1 == 0){
                            points[layer][state.BOARD_SIZE*2] = 0;
                        }
                        if(counter%state.BOARD_SIZE-1 == 0){
                            points[layer][state.BOARD_SIZE*2 + 1] = 0;
                        }
                    }
                    counter++;
                }
            }

            // if a latyer contains a win
            for(int i: points[layer]){
                if(i == 10000){
                    return 1000000;
                }
                else if(i > 1){
                    value += i;
                }

            }
        }

        // if we have nulled the point we don't count it for the value of the state

        for(int i = 0; i < state.BOARD_SIZE; i++){
            for(int point: points[i+state.BOARD_SIZE]){
                if(point == 10000){
                    System.err.print(point + " ");
                    return 1000000;
                }
                else if(point > 1){
                    value += point;
                }

            }
        }
        return value;
    }

}
