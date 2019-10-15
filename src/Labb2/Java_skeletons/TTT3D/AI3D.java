import java.util.*;
import java.lang.Math;

public class AI3D{
    public static int depth = 1;

    // return the "best" next gameState
    public static GameState getBestMove(Vector<GameState> states){
        int index = 0;
        double max = 0;
        double v = 0;

        // find round to better eval state
        int round = (int) Math.floor((64 - states.size())/2) + 1;
        //System.err.println(round);

        // Make a "random move first round"
        if(round == 1){
            return states.get((int) Math.floor(states.size())/2 - 5);
        }
        // Array to store points in
        int[][] points = new int[states.get(0).BOARD_SIZE*2][states.get(0).BOARD_SIZE*2+2];
        int[][] enemyPoints = new int[states.get(0).BOARD_SIZE*2][states.get(0).BOARD_SIZE*2+2];


        // find state with max value and save its index
        for(int i = 0; i < states.size(); i++){
            v = minMaxPruning(states.get(i), depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, points, enemyPoints, round);
            if(max < v){
                max = v;
                index = i;
            }
        }
        return states.get(index);
    }

    // minMax-alg with pruning, recursive
    private static double minMaxPruning(GameState state, int depth, double alpha, double beta, int[][] points, int[][] enemyPoints, int round){

        double v = 0;

        // A vector containing all possible children states if this current state
        Vector<GameState> nextStates = new Vector<GameState>();
        state.findPossibleMoves(nextStates);

        if (depth==0 || nextStates.size()==0){
            v = evalState(state, points, enemyPoints, round);
        }

        // 2 is 'O' so if nextPlayer is 2 it means that the current player is 'X'
        else if (state.getNextPlayer() == 1){
            v=Double.NEGATIVE_INFINITY;

            for(GameState s: nextStates){
                v = Math.max(v, minMaxPruning(s,depth-1, alpha, beta, points, enemyPoints, round));
                alpha = Math.max(alpha, v);

                // if we find win
                if(v == 1000000){
                    System.err.println("found opt");
                    return v;
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
                v= Math.min(v,minMaxPruning(s,depth-1, alpha, beta, points, enemyPoints, round));
                beta = Math.min(beta, v);

                /**
                if(v == -1000000){
                    return v;
                }
                 */

                if(beta <= alpha || v == 0){
                    return v;
                }
            }
        }
        return v;
    }

    // eval a state and return its value
    private static int evalState(GameState state, int[][] points, int[][] enemyPoints, int round){
        int counter;
        int player = 0;

        for(int[] arr: points){
            Arrays.fill(arr, 1);
        }
        for(int[] arr: enemyPoints){
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
                        points[col + state.BOARD_SIZE][state.BOARD_SIZE + layer] *= scaling;

                        // if it's in the first diagonal
                        if(counter%state.BOARD_SIZE+1 == 0){
                            points[layer][state.BOARD_SIZE*2] *= scaling;
                        }

                        // checking other diagonal
                        if(counter%state.BOARD_SIZE-1 == 0 && row+col != 0){
                            points[layer][state.BOARD_SIZE*2 + 1] *= scaling;
                        }
                    }

                    else if(player == 2){
                        points[layer][row] = 0;
                        points[layer][state.BOARD_SIZE + col] = 0;
                        points[col + state.BOARD_SIZE][row] = 0;
                        points[col + state.BOARD_SIZE][state.BOARD_SIZE + layer] = 0;

                        if(counter%state.BOARD_SIZE+1 == 0){
                            enemyPoints[layer][state.BOARD_SIZE*2] = 0;
                        }
                        if(counter%state.BOARD_SIZE-1 == 0){
                            enemyPoints[layer][state.BOARD_SIZE*2 + 1] = 0;
                        }
                    }
                     /**
                    // if an enemy player is encoutered null the value of the row/col
                    else if(player == 2){
                        enemyPoints[layer][row] *= scaling;
                        enemyPoints[layer][state.BOARD_SIZE + col] *= scaling;
                        enemyPoints[col + state.BOARD_SIZE][row] *= scaling;
                        enemyPoints[col + state.BOARD_SIZE][state.BOARD_SIZE + layer] *= scaling;

                        if(counter%state.BOARD_SIZE+1 == 0){
                            enemyPoints[layer][state.BOARD_SIZE*2] *= scaling;
                        }
                        if(counter%state.BOARD_SIZE-1 == 0){
                            enemyPoints[layer][state.BOARD_SIZE*2 + 1] *= scaling;
                        }
                    }*/

                    counter++;
                }
            }


            // if a layer contains a win
            if(round < 4 || player == 1){
                double maxPoints = Math.pow(10, round);
                for(int i = 0; i < points[0].length; i++){
                    if(points[layer][i] == maxPoints){
                        /**
                        System.err.println(maxPoints);
                        System.err.println("Winning");
                        */
                         return 1000000;
                    }
                    else{
                        value += (points[layer][i] - enemyPoints[layer][i]);
                    }
                }
            }
            else{
                for(int i = 0; i < points[0].length; i++){
                    if(points[layer][i] == (10000)){
                        //System.err.println("Winning");
                        return 1000000;
                    }
                    else{
                        value += (points[layer][i] - enemyPoints[layer][i]);
                    }
                }
            }

            /**
            for(int i = 0; i < points[0].length; i++){
                if(points[layer][i] - enemyPoints[layer][i] == (10000)){
                    //System.err.println("Winning");
                    return 1000000;
                }
                else if(points[layer][i] - enemyPoints[layer][i] == (10000)){
                    return -1000000;
                }
                else{
                    value += (points[layer][i] - enemyPoints[layer][i]);
                }
            }
             */
        }
        for(int i = 0; i < state.BOARD_SIZE; i++){
            for(int j = 0; j < state.BOARD_SIZE; j++){
                if(points[i+state.BOARD_SIZE][j] == (10000)){
                    //System.err.println("Winning");
                    return 1000000;
                }
                else{
                    value += points[i+state.BOARD_SIZE][j];
                }

            }
        }
        /**
        // if we have nulled the point we don't count it for the value of the state
        for(int i = 0; i < state.BOARD_SIZE; i++){
            for(int j = 0; j < state.BOARD_SIZE; j++){
                if(points[i+state.BOARD_SIZE][j] - enemyPoints[i+state.BOARD_SIZE][j] == (15)){
                    //System.err.println("Winning");
                    return 1000000;
                }
                else if(points[i+state.BOARD_SIZE][j] - enemyPoints[i+state.BOARD_SIZE][j] == (-15)){
                    return -1000000;
                }
                else{
                    value += (points[i+state.BOARD_SIZE][j] - enemyPoints[i+state.BOARD_SIZE][j]);
                }

            }
        }
         */
        //System.err.println(value);
        return value;
    }

}
