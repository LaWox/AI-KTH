import java.util.Scanner;
import java.util.ArrayList;

public class Main1 {

    static float[] multiplication(float[][] a, float[][] b){
        int aRow = a.length;
        int aCol = a[0].length;
        int bRow = b.length;
        int bCol = b[0].length;
        float[] output = new float[aRow*bCol+2];
        output[0] = (float) aRow;
        output[1] = (float) bCol;
        int counter=2;
        float sum=0;

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

    static float[][] elementMultiplication(float[] a, float[] b){
        // elementwive multiplication
        float [][] outMatrix = new float[1][a.length];

        for (int i = 0; i < a.length; i++){
            outMatrix[1][i] = a[i]*b[i];
        }
        return outMatrix;
    }

    static float[][] createMatrix(float[] a){
        int aRow = (int) a[0];
        int aCol = (int) a[1];
        System.out.println(aRow + " : " + aCol);
        int counter= 2;
        float[][] matrix = new float[aRow][aCol];
        for (int i= 0; i<aRow; i++){
            for(int j= 0; j<aCol; j++){
                matrix[i][j]=a[counter];
                counter++;
            }
        }
        return matrix;
    }

    // returns specific col from a matrix
    static float[][] getCol(float[][] m, int index){
        float [][] outCol = new float[1][m.length];
        for(int i = 0; i < m.length; i++){
            outCol[0][i] = m[i][index];
        }

        return outCol;
    }

    // the alphapassalgorithm
    static float[][] alphaPass(float[][] pi, float[][] a, float[][] b, int[] emission){
        float[] stateProb;
        float[][] col;

        //System.out.println(getCol(b, emission[0]).length);

        col = getCol(b, emission[0]);
        float[][] alpha = elementMultiplication(pi[0], col[0]);

        for(int i = 0; i < emission.length; i++){
            col = getCol(b, emission[i+1]);
            stateProb = multiplication(alpha, a);
            alpha = elementMultiplication(stateProb, col[0]);
        }
        return alpha;
    }

    public static void main(String[] arg){

        String[] aMatrixStr;
        String[] bMatrixStr;
        String[] piStr;
        String[] emiStr;

        float[] aList;
        float[] bList;
        float[] pi;
        float[][] AMatrix;
        float[][] BMatrix;
        float[][] piMatrix;
        int[] emiSeq;

        Scanner sc = new Scanner(System.in);

        aMatrixStr = sc.nextLine().split(" ");
        bMatrixStr = sc.nextLine().split(" ");
        piStr = sc.nextLine().split(" ");
        emiStr = sc.nextLine().split(" ");

        aList = new float[aMatrixStr.length];
        bList = new float[bMatrixStr.length];
        pi = new float[piStr.length];
        emiSeq = new int[Integer.parseInt(emiStr[0])];

        for(int i = 0; i < aList.length; i ++){
            aList[i] = Float.parseFloat(aMatrixStr[i]);
            //System.out.println(aList[i]);
        }
        for(int i = 0; i < bList.length; i ++){
            bList[i] = Float.parseFloat(bMatrixStr[i]);
            //System.out.println(bList[i]);
        }

        for(int i = 0; i < pi.length; i ++){
            pi[i] = Float.parseFloat(piStr[i]);
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

        float[][] ans = alphaPass(piMatrix, AMatrix, BMatrix, emiSeq);
        System.out.println(ans);
    }
}
