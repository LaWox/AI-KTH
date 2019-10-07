//Platon Woxler platon@kth.se and Jussi Kangas jkangas@kth.se
import java.util.Scanner;
import java.util.Arrays;

public class Main2 {

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

    static void viterbiDynamic(double[][] a, double[][] b, int[] eSeq, double[] pi, double[][] delta, int[][] maxStates){
        double max;
        double deltaTemp;
        for(int[] arr: maxStates){
            Arrays.fill(arr,-1);
        }

        //System.out.println(delta.length);
        for(int i = 0; i < delta.length; i++){
            delta[i][0] = pi[i]*b[i][eSeq[0]];
        }
        for(int t = 1; t < delta[0].length; t++){
            for(int i = 0; i < delta.length; i++){
                max = 0;
                int count=0;
                for(int j = 0; j < delta.length; j++){
                    deltaTemp = delta[j][t-1]*a[j][i]*b[i][eSeq[t]];


                        //System.out.println(delta[j][t-1] + " " + a[j][i] + " " + b[i][eSeq[t]] + " ---- " + deltaTemp);

                    /*if(t==6){
                        //System.out.println(delta[j][t-1] + " " + a[j][i] + " " + b[i][eSeq[t]] + " ---- " + deltaTemp);
                        System.out.println(deltaTemp);
                    } */


                    if(deltaTemp >= max){
                        delta[i][t] = deltaTemp;
                        maxStates[i][t] = j;
                        max = deltaTemp;


                        if(t==6){
                            //System.out.println(delta[j][t-1] + " " + a[j][i] + " " + b[i][eSeq[t]] + " ---- " + deltaTemp);
                            System.out.println("count "+count+" i "+i+" t "+t+" j "+j+" max "+max);

                        }



                    }
                    count++;
                }
                //System.out.println(delta[i][t] +" "+maxStates[i][t]);
            }
        }
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

    static int[] getPath2(double[][] delta, int[][] stateMatrix){
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


    static void printMatrix(double[][] m){
        for(int i = 0; i < m.length; i++){
            for(int j = 0; j < m[0].length; j++){
                System.out.print(m[i][j] + " ");
            }
            System.out.println(" ");
        }
    }

    static void printMatrix(int[][] m){
        for(int i = 0; i < m.length; i++){
            for(int j = 0; j < m[0].length; j++){
                System.out.print(m[i][j] + " ");
            }
            System.out.println(" ");
        }

    }

    public static void main(String[] args){
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
        int[] emiSeq;

        Scanner sc = new Scanner(System.in);

        aMatrixStr = sc.nextLine().split(" ");
        bMatrixStr = sc.nextLine().split(" ");
        piStr = sc.nextLine().split(" ");
        emiStr = sc.nextLine().split(" ");

        aList = new double[aMatrixStr.length];
        bList = new double[bMatrixStr.length];
        pi = new double[piStr.length];
        emiSeq = new int[Integer.parseInt(emiStr[0])];

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

        for(int i=0; i<emiStr.length-1; i++){
            emiSeq[i]=Integer.parseInt(emiStr[i+1]);
        }

        // create matrixes
        AMatrix = createMatrix(aList);
        BMatrix = createMatrix(bList);
        piMatrix = createMatrix(pi);

        double[][] delta = new double[AMatrix.length][emiSeq.length];
        int[][] states = new int[AMatrix.length][emiSeq.length];

        int [] maxStates = new int[delta[0].length];

        //System.out.println(0.05*0.1*0.1);

        viterbiDynamic(AMatrix, BMatrix, emiSeq, piMatrix[0], delta, states);
        maxStates = getPath(delta, states);

        //printMatrix(delta);

        //printMatrix(states);
        //System.out.println("------------------------");

        for(int i: maxStates){
            System.out.print(i+" ");
        }
        //System.out.println();

    }
}
