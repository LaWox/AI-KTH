import java.util.Scanner;
import java.util.ArrayList;

public class Main {


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

    static float[][] createMatrix(float[] a){
        int aRow = (int) a[0];
        int aCol = (int) a[1];
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

    public static void main(String[] args) {
        String[] aMatrixStr;
        String[] bMatrixStr;
        String[] piStr;

        float[] aList;
        float[] bList;
        float[] pi;
        float[][] AMatrix;
        float[][] BMatrix;
        float[][] piMatrix;


        Scanner sc = new Scanner(System.in);
        System.out.println("Printing the file passed in:");

        aMatrixStr = sc.nextLine().split(" ");
        bMatrixStr = sc.nextLine().split(" ");
        piStr = sc.nextLine().split(" ");

        aList = new float[aMatrixStr.length];
        bList = new float[bMatrixStr.length];
        pi = new float[piStr.length];

        for(int i = 0; i < aList.length; i ++){
            aList[i] = Float.parseFloat(aMatrixStr[i]);
        }
        for(int i = 0; i < bList.length; i ++){
            bList[i] = Float.parseFloat(bMatrixStr[i]);
        }

        for(int i = 0; i < pi.length; i ++){
            pi[i] = Float.parseFloat(piStr[i]);
        }

        AMatrix=createMatrix(aList);
        BMatrix=createMatrix(bList);
        piMatrix=createMatrix(pi);

        float[] firstTransition = multiplication(piMatrix, AMatrix);
        float[] lastTransistion = multiplication(createMatrix(firstTransition),BMatrix);

        for(float i:lastTransistion){
            System.out.println(i);
        }
    }
}
