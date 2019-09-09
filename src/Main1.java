// Platon Woxler platon@kth, Jussi Kangas jkangas@kth.se

import java.util.Scanner;
import java.util.ArrayList;

public class Main1 {

    static double[] multiplication(double[][] a, double[][] b){
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

    // returns specific col from a matrix
    static double[][] getCol(double[][] m, int index){
        double[][] outCol = new double[1][m.length];
        for(int i = 0; i < m.length; i++){
            outCol[0][i] = m[i][index];
        }

        return outCol;
    }

    static double sumMatrix(double[][] matrix){
        double sum = 0;
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[0].length; j++){
                sum += matrix[i][j];
                //System.out.println(sum);
            }
        }
        return sum;
    }

    // the alphapassalgorithm
    static double[][] alphaPass(double[][] pi, double[][] a, double[][] b, int[] emission){
        double[][] stateProb;
        double[][] col;

        //System.out.println(getCol(b, emission[0]).length);

        col = getCol(b, emission[0]);
        //System.out.println(pi[0].length);
        double[][] alpha = elementMultiplication(pi[0], col[0]);

        for(int i = 0; i < emission.length-1; i++){
            col = getCol(b, emission[i+1]);
            stateProb = createMatrix(multiplication(alpha, a));
            alpha = elementMultiplication(stateProb[0], col[0]);
        }
        return alpha;
    }

    public static void main(String[] arg){

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

        // creta matrixes
        AMatrix = createMatrix(aList);
        BMatrix = createMatrix(bList);
        piMatrix = createMatrix(pi);

        //System.out.println(AMatrix.length + " " + BMatrix.length + " " + piMatrix.length);

        double[][] ans = alphaPass(piMatrix, AMatrix, BMatrix, emiSeq);
        System.out.println(sumMatrix(ans));
    }
}
