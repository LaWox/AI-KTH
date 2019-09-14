//Platon Woxler platon@kth.se and Jussi Kangas jkangas@kth.se
import java.util.Scanner;

public class Main3 {

    static double[][] multiplication(double[][] a, double[] b, double[] o){
        int aRow = a.length;
        int aCol = a[0].length;
        double[][] output = new double[aRow][aCol];

        for (int i=0; i<aRow; i++ ){
            for( int j=0; j<aCol; j++){
                output[i][j] = a[j][i]*b[j]*o[i];
            }
        }
        return output;
    }

    static double[] matrixMultiplication(double[][] a, double[][] b){
        int aRow = a.length;
        int aCol = a[0].length;
        int bRow = b.length;
        int bCol = b[0].length;
        double[] output = new double[aRow*bCol+2];
        output[0] = (double) aRow;
        output[1] = (double) bCol;
        int counter=2;
        double sum=0;

        for (int i=0; i<aRow; i++ ){
            for( int j=0; j<bCol; j++){
                for( int k=0; k<aCol; k++){
                    sum += a[i][k]*b[k][j];
                }
                output[counter]=sum;
                sum=0;
                counter++;
            }
        }
        return output;
    }

    static double[][] elementMultiplication(double[] a, double[] b){
        // elementwive multiplication
        double[][] outMatrix = new double[1][a.length];

        for (int i = 0; i < a.length; i++){
            outMatrix[0][i] = a[i]*b[i];
        }
        return outMatrix;
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

    static int[] getStates(double[][] delta){
        int[] states = new int[delta.length];
        double max = 0;
        for(int i = 0; i < delta.length; i++){
            for(int j = 0; j < delta[0].length; j++){
                if(delta[i][j] >= max){
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

    // the alphapassalgorithm
    static double[][] alphaPass(double[][] pi, double[][] a, double[][] b, int[] emission){
        double[][] stateProb;
        double[][] col;

        col = getCol(b, emission[0]);
        double[][] alpha = elementMultiplication(pi[0], col[0]);

        for(int i = 0; i < emission.length-1; i++){
            col = getCol(b, emission[i+1]);
            stateProb = createMatrix(multiplication(alpha, a));
            alpha = elementMultiplication(stateProb[0], col[0]);
        }
        return alpha;
    }

    // beta pass
    static double[][] betaPass(double[][] pi, double[][] a, double[][] b, int[] emission){
        double[][] beta  = new double[][];
        return beta;
    }

    // di-gamma function
    static double[][] diGamma(double[][] alpha, double[][] beta){
        double[][] diGamma = new double[][];
        return diGamma;
    }

    // gamma function
    static double[] gamma(double[][] alpha, double[][] beta){
        double[] gamma = new double[];
        return gamma;
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
        System.out.println();

    }
}
