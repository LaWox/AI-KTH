import java.util.*;
import java.lang.Math;

public class AI{
    public int depth;

    // return the "best" next gameState
    public GameState getBestMove(Vector<GameState> states, Player player){

        return null;
    }

    // minMax-alg with pruning, recursive
    private double minMaxPruning(GameState state, int depth, double alpha, double beta){

        double v=0;

        // A vector containing all possible children states if this current state
        Vector<GameState> nextStates = new Vector<GameState>();
        state.findPossibleMoves(nextStates);

        if (depth==0 || nextStates.size()==0){
            v= evalState(state);
        }


        // 2 is 'O' so if nextPlayer is 2 it means that the current player is 'X'
        else if (state.getNextPlayer() == 2){
            v=Double.NEGATIVE_INFINITY;

            for(GameState s: nextStates){
                v= Math.max(v,minMaxPruning(s,depth-1, alpha, beta));
                alpha = Math.max(alpha, v);
                if( beta <= alpha ) {
                    break;
                }
            }
        }
        // 1 is 'X' so if nextplayer is 1 it means that the current player is 'O'
        else if (state.getNextPlayer() == 1){
            v=Double.POSITIVE_INFINITY;

            for(GameState s: nextStates){
                v= Math.max(v,minMaxPruning(s,depth-1, alpha, beta));
                beta = Math.min(beta, v);
                if( beta <= alpha ) {
                    break;
                }
            }
        }

        return v;
    }

    // eval a state and return its value
    private int evalState(GameState gameState){


        return 0;
    }

}
