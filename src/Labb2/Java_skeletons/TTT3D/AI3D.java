import java.util.*;
import java.lang.Math;

public class AI3D{

    // return the "best" next gameState
    public static GameState getBestMove(Vector<GameState> states){
        int depth = 10;

        int index = 0;
        double max = 0;
        double v = 0;
        int round = (int) Math.floor((64 - states.size())/2) + 1;


        if(round > 7){
            depth = 0;
        }


        // Make a "random" move first round

        if(round == 1){
            return states.get((int) Math.floor(states.size())/2-10);
        }
        // Array to store points in
        int[][] points = new int[8][10];

        // find state with max value and save its index
        for(int i = 0; i < states.size(); i++){
            v = minMaxPruning(states.get(i), depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, points, round);
            if(max < v){
                max = v;
                index = i;
            }
        }
        return states.get(index);
    }

    // minMax-alg with pruning, recursive
    private static double minMaxPruning(GameState state, int depth, double alpha, double beta, int[][] points, int round){
        int localRound = round + 1;
        double v = 0;

        // A vector containing all possible children states if this current state
        Vector<GameState> nextStates = new Vector<GameState>();
        state.findPossibleMoves(nextStates);

        if (depth==0 || nextStates.size()==0){
            v = evalState(state, points, round);
        }

        // 2 is 'O' so if nextPlayer is 2 it means that the current player is 'X'
        else if (state.getNextPlayer() == 1){
            v=Double.NEGATIVE_INFINITY;

            for(GameState s: nextStates){
                v = Math.max(v, minMaxPruning(s,depth-1, alpha, beta, points, round));
                alpha = Math.max(alpha, v);

                if(beta <= alpha){
                    return v;
                }
            }
        }
        // 1 is 'X' so if nextplayer is 1 it means that the current player is 'O'
        else if (state.getNextPlayer() == 2){
            v=Double.POSITIVE_INFINITY;

            for(GameState s: nextStates){
                v= Math.min(v,minMaxPruning(s,depth-1, alpha, beta, points, round));
                beta = Math.min(beta, v);

                if(beta <= alpha || v == 0){
                    return v;
                }
            }
        }
        return v;
    }

    // eval a state and return its value
    private static int evalState(GameState state, int[][] points, int round){
        int counter;
        int player = 0;

        for(int[] arr: points){
            Arrays.fill(arr, 1);
        }

        int scaling = 10;
        int value = 0;

        for(int layer = 0; layer < 4; layer ++){
            counter = 0;
            for(int row = 0; row < 4; row ++){
                for(int col = 0; col < 4; col ++){
                    player = state.at(row, col, layer);

                    // add points to the vector, increase by a factor for every new entry
                    if(player == 1){
                        points[layer][row] *= scaling;
                        points[layer][4 + col] *= scaling;
                        points[col + 4][row] *= scaling;
                        points[col + 4][4 + layer] *= scaling;

                        // if it's in the first diagonal
                        if((counter%(5)) == 0){
                            points[layer][8] *= scaling;
                        }

                        // checking other diagonal
                        if(counter%(3) == 0 && row+col != 0){
                            points[layer][9] *= scaling;
                        }
                    }

                    else if(player == 2){
                        points[layer][row] = 0;
                        points[layer][4 + col] = 0;
                        points[col + 4][row] = 0;
                        points[col + 4][4 + layer] = 0;

                        // if it's in the first diagonal
                        if((counter%(5)) == 0){
                            points[layer][8] = 0;
                        }
                        // checking other diagonal
                        if(counter%(3) == 0 && row+col != 0){
                            points[layer][9] = 0;
                        }
                    }
                    counter++;
                }
            }
            // if a layer contains a win
            for(int i = 0; i < 10; i++){
                if(points[layer][i] == 10000){
                     return 1000000;
                }
                else if(points[layer][i] > 1){
                    value += (points[layer][i]);
                }
            }
        }
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 10; j++){
                if(points[i+4][j] == (10000)){
                    return 1000000;
                }
                else if(points[i+4][j] > 1){
                    value += points[i+4][j];
                }

            }
        }
        return value;
    }

}
