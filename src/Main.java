import java.util.Scanner;
import java.util.ArrayList;

public class Main {

    /*
    static Float[] multiplication(String[] a, String[] b,){
        int aRow = Integer.parseInt(a[0]);
        int aCol = Integer.parseInt(a[1]);
        int bRow = Integer.parseInt(b[0]);
        int bCol = Integer.parseInt(b[1]);
        Float[] output = new Float[aRow*bCol+2];
        output[0] = (float) aRow;
        output[1] = (float) bCol;

        for (int i=0; i<aRow; i++ ){
            int r

        }
    }
    */
    public static void main(String[] args) {
        String[] aMatrixStr;
        String[] bMatrixStr;
        String[] piStr;

        Float[] aMatrix;
        Float[] bMatrix;
        Float[] pi;

        Scanner sc = new Scanner(System.in);
        System.out.println("Printing the file passed in:");

        aMatrixStr = sc.nextLine().split(" ");
        bMatrixStr = sc.nextLine().split(" ");
        piStr = sc.nextLine().split(" ");

        aMatrix = new Float[aMatrixStr.length];
        bMatrix = new Float[bMatrixStr.length];
        pi = new Float[piStr.length];

        for(int i = 0; i < aMatrix.length; i ++){
            aMatrix[i] = Float.parseFloat(aMatrixStr[i]);
        }
        for(int i = 0; i < bMatrix.length; i ++){
            bMatrix[i] = Float.parseFloat(bMatrixStr[i]);
        }

        for(int i = 0; i < pi.length; i ++){
            pi[i] = Float.parseFloat(piStr[i]);
        }



    }
}
