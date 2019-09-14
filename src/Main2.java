//Platon Woxler platon@kth.se and Jussi Kangas jkangas@kth.se
import java.util.Scanner;
import java.util.ArrayList;

public class Main2 {


    static double[][] multiplication(double[][] a, double[] b, double[] o){
        int aRow = a.length;
        int aCol = a[0].length;
        double[][] output = new double[aRow][aCol];

        for (int i=0; i<aRow; i++ ){
            for( int j=0; j<aCol; j++){
                //System.out.println(a[j][i]+" * "+ b[j] +" multiplication");
                output[i][j] = a[j][i]*b[j]*o[i];
            }
        }

        return output;
    }

    static double[][] elementMultiplication(double[] a, double[] b){
        // elementwive multiplication
        double[][] outMatrix = new double[1][a.length];

        for (int i = 0; i < a.length; i++){
            //System.out.println(a.length + " : " + i);
            outMatrix[0][i] = a[i]*b[i];
            //System.out.println(outMatrix[0][i]);
        }
        //System.out.println("--------------------------------");
        return outMatrix;
    }

    static double[][] createMatrix(double[] a){
        int aRow = (int) a[0];
        int aCol = (int) a[1];
        //System.out.println(aRow + " : " + aCol);
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

    static int[] getStates(double[][] delta){
        int[] states = new int[delta.length];
        double max = 0;
        //printMatrix(delta);
        for(int i = 0; i < delta.length; i++){
            for(int j = 0; j < delta[0].length; j++){
                if(delta[i][j] > max){
                    states[i] = j;
                    max=delta[i][j];
                }
            }
            if(max==0){
                states[i]=-1;
            }
            max = 0;
        }
        return states;
    }

    static double[] getMax(double[][] delta){
        double maxProb [] = new double[delta.length];
        double max = 0;

        for(int i = 0; i < delta.length; i++){
            for(int j = 0; j < delta[0].length; j++){
                if(delta[i][j] > max){
                    max= delta[i][j];
                    maxProb[i] = delta[i][j];
                }
            }
            max = 0;
            //System.out.println("Maxprob: "+maxProb[i]);
        }

        return maxProb;
    }

    static int getMaxIndx(double[] list){
        int indx=0;
        double max=0;
        for(int i=0; i<list.length; i++){
            if (max<list[i]){
                max=list[i];
                indx=i;
            }
        }
        return indx;
    }

    static int[] viterbi(double[][] a, double[][] b, double[] pi, int[] emissions ){
        int[][] statesMatrix= new int[a.length][emissions.length];
        double[][] delta = new double[emissions.length][a.length];
        double [] observation = (getCol(b, emissions[0]))[0];
        int[] states=new int[emissions.length];

        delta[0] = (elementMultiplication(observation, pi))[0];
        double [][] deltaTemp;

        //printMatrix(delta);

        for (int i=0; i < emissions.length-1; i++ ){

            // Get new delta value
            observation=(getCol(b, emissions[i+1]))[0];
            // A*delta*observations
            deltaTemp = multiplication(a, delta[i], observation);

            //printMatrix(deltaTemp);
            //System.out.println("-----------");
            // Find max state
            statesMatrix[i]=getStates(deltaTemp);


            //System.out.println("observation");
           // for(double p:observation){
           //     System.out.print(p+" ");
           // }

            delta[i+1]= getMax(deltaTemp);
            //System.out.println("inne i första for loopen");

            //System.out.println("delta");
            //printMatrix(delta);
            //System.out.println("statesmatrix");
            //printMatrix(statesMatrix);
        }

        // Get correct states
        double max=0;
        for(int i=delta.length-1; i>0; i--){
            for(int j=0; j<delta[0].length; j++){
                //System.out.println(statesMatrix[i][j] + " stateMatrix");
                //System.out.println(delta[i][j]);
                if (delta[i][j]> max){
                    max=delta[i][j];
                    states[i-1]=statesMatrix[i-1][j];
                }
            }
            max=0;
            //System.out.println("inne i andra for loopen");
        }
        states[emissions.length-1]=getMaxIndx(delta[delta.length-1]);

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
            //System.out.println(emiSeq[i]);
        }

        // create matrixes
        AMatrix = createMatrix(aList);
        BMatrix = createMatrix(bList);
        piMatrix = createMatrix(pi);

        int[] states = viterbi(AMatrix, BMatrix, piMatrix[0], emiSeq);
        for(int i: states){
            System.out.print(i+" ");
        }




    }
}
