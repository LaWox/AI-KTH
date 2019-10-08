import java.util.Scanner;

public class Viterbi {

    static int[][] stateMatrix;
    static double[][] delta;

    private static class TimeStepNode {
        public int[] v_path;
        public double v_prob;

        public TimeStepNode(int[] v_path, double v_prob) {
            this.v_path = copyIntArray(v_path);
            this.v_prob = v_prob;
        }
    }

    private static int[] copyIntArray(int[] ia) {
        int[] newIa = new int[ia.length];
        for (int i = 0; i < ia.length; i++) {
            newIa[i] = ia[i];
        }
        return newIa;
    }

    private static int[] copyIntArray(int[] ia, int newInt) {
        int[] newIa = new int[ia.length + 1];
        for (int i = 0; i < ia.length; i++) {
            newIa[i] = ia[i];
        }
        newIa[ia.length] = newInt;
        return newIa;
    }

    static void printMatrix(int[][] m){
        for(int i = 0; i < m.length; i++){
            for(int j = 0; j < m[0].length; j++){
                System.out.print(m[i][j] + " ");
            }
            System.out.println(" ");
        }

    }

    static void printMatrix(double[][] m){
        for(int i = 0; i < m.length; i++){
            for(int j = 0; j < m[0].length; j++){
                System.out.print(m[i][j] + " ");
            }
            System.out.println(" ");
        }
    }


    // forwardViterbi(observations, states, start_probability,
    // transition_probability, emission_probability)
    public int[] forwardViterbi(String[] observations, String[] hiddenStates, double[] sp, double[][] tp, double[][] ep) {
        stateMatrix = new int[hiddenStates.length][observations.length];
        delta = new double[hiddenStates.length][observations.length];

        double [][] emission;
        TimeStepNode[] T = new TimeStepNode[hiddenStates.length];
        emission = getCol(ep,Integer.parseInt(observations[0]));

        for (int state = 0; state < hiddenStates.length; state++) {
            //System.out.println(sp[state] +" "+ hiddenStates.length);
            int[] intArray = new int[1];
            intArray[0] = state;
            T[state] = new TimeStepNode( intArray, sp[state] * emission[0][state]);
        }

        for (int output = 1; output < observations.length; output++) {
            TimeStepNode[] U = new TimeStepNode[hiddenStates.length];
            emission=getCol(ep,Integer.parseInt(observations[output]));

            for (int next_state = 0; next_state < hiddenStates.length; next_state++) {
                int[] argmax = new int[0];
                double valmax = 0;
                int maxstate =-1;

                for (int state = 0; state < hiddenStates.length; state++) {
                    int[] v_path = copyIntArray(T[state].v_path);

                    double v_prob = T[state].v_prob;
                    double p = emission[0][next_state] * tp[state][next_state];

                    v_prob *= p;


                    if (v_prob > valmax) {
                        if (v_path.length == observations.length) {
                            argmax = copyIntArray(v_path);
                        } else {
                            argmax = copyIntArray(v_path, next_state);
                            maxstate = next_state;

                        }
                        valmax = v_prob;


                    }
                }
                U[next_state] = new TimeStepNode( argmax, valmax);
                delta[next_state][output]=valmax;
                stateMatrix[next_state][output]=maxstate;


                if(output==8 || output==9)
                {
                System.out.print("valmax "+valmax+": max_state "+maxstate+": ");
                if(argmax.length>0){
                    System.out.print("argmax[length] "+argmax[argmax.length-1]+": ");
                }
                }

            }
            if(output==8 || output==9){
                System.out.println();
            }

            T = U;
        }
        // apply sum/max to the final states:
        int[] argmax = new int[0];
        double valmax = 0;
        for (int state = 0; state < hiddenStates.length; state++) {
            int[] v_path = copyIntArray(T[state].v_path);
            double v_prob = T[state].v_prob;
            if (v_prob > valmax) {
                argmax = copyIntArray(v_path);
                valmax = v_prob;
            }
        }

        return argmax;
    }

    static double[][] createMatrix(double[] a){
        int aRow = (int) a[0];
        int aCol = (int) a[1];
        int counter= 2;
        double[][] matrix = new double[aRow][aCol];
        for (int i= 0; i<aRow; i++){
            for(int j= 0; j<aCol; j++){
                matrix[i][j]=a[counter];
                counter++;
            }
        }
        return matrix;
    }

    static double[][] getCol(double[][] m, int index){
        double[][] outCol = new double[1][m.length];
        for(int i = 0; i < m.length; i++){
            outCol[0][i] = m[i][index];
        }

        return outCol;
    }

    static int[] getPath(double[][] delta, int[][] stateMatrix){
        int[] states = new int[delta[0].length];
        double max;
        int maxState = -1;

        for(int col = delta[0].length-1; col > 0; col--){
            max = 0;
            for(int row = 0; row < delta.length; row++){

                //System.out.println("Delta "+delta[row][col]+" state "+stateMatrix[row][col]);

                if(delta[row][col] >= max){
                    maxState = stateMatrix[row][col];
                    max = delta[row][col];
                }
            }
            states[col-1] = maxState;
            //System.out.println(maxState);
            //System.out.println("maxdelta "+ max);
        }
        max = 0;
        // set last col
        for(int i = 0; i < delta.length; i++){
            if(delta[i][delta[0].length-1] > max){
                max = delta[i][delta[0].length-1];
                maxState = i;
            }
        }
        states[states.length-1] = maxState;
        return states;
    }


    public static void main(String[] args) throws Exception {
        String[] aMatrixStr;
        String[] bMatrixStr;
        String[] piStr;
        String[] emiStr;

        double[] aList;
        double[] bList;
        double[] pi;
        double[][] AMatrix;
        double[][] BMatrix;
        double[][] piMatrix;
        //int[] emiSeq;
        String [] statesList;
        String [] emissions;

        Scanner sc = new Scanner(System.in);

        aMatrixStr = sc.nextLine().split(" ");
        bMatrixStr = sc.nextLine().split(" ");
        piStr = sc.nextLine().split(" ");
        emiStr = sc.nextLine().split(" ");

        emissions = new String[emiStr.length-1];

        for(int i=1; i<emiStr.length; i++){
            emissions[i-1]=emiStr[i];
        }

        aList = new double[aMatrixStr.length];
        bList = new double[bMatrixStr.length];
        pi = new double[piStr.length];
        //emiSeq = new int[Integer.parseInt(emiStr[0])];

        statesList = new String[Integer.parseInt(aMatrixStr[0])];
        for(int i=0; i<statesList.length; i++){
            statesList[i]=Integer.toString(i);
        }

        for(int i = 0; i < aList.length; i ++){
            aList[i] = Double.parseDouble(aMatrixStr[i]);

            //System.out.println(aList[i]);
        }

        for(int i = 0; i < bList.length; i ++){
            bList[i] = Double.parseDouble(bMatrixStr[i]);
            //System.out.println(bList[i]);
        }

        for(int i = 0; i < pi.length; i ++){
            pi[i] = Double.parseDouble(piStr[i]);
            //System.out.println(pi[i]);
        }


        // create matrixes
        AMatrix = createMatrix(aList);
        BMatrix = createMatrix(bList);
        piMatrix = createMatrix(pi);

        Viterbi v=new Viterbi();
        int[] argmax;
        argmax=v.forwardViterbi(emissions, statesList, piMatrix[0], AMatrix, BMatrix);


        //System.out.print("Viterbi path: ");
        for (int i = 0; i < argmax.length; i++) {
            System.out.print(statesList[argmax[i]] + " ");
        }
        System.out.println();

        printMatrix(stateMatrix);

        int [] maxStates = new int[delta[0].length];
        maxStates = getPath(delta, stateMatrix);

        for(int i: maxStates){
            System.out.print(i+" ");
        }
        System.out.println();


    }
}