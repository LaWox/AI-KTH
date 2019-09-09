import java.util.Scanner;
import java.util.ArrayList;

public class Main2 {

    static ArrayList[] <String> states;

    static double[] multiplication(double[][] a, double[][] b){
        int aRow = a.length;
        int aCol = a[0].length;
        int bRow = b.length;
        int bCol = b[0].length;
        double[] output = new double[aRow*bCol];
        int counter=2;
        double prob=0;

        for (int i=0; i<aRow; i++ ){
            for( int j=0; j<bCol; j++){
                for( int k=0; k<aCol; k++){
                    if (prob < a[i][k]*b[k][j]){
                        prob = a[i][k]*b[k][j];
                        states.set(Integer.toString(k));
                    }


                }
                output[counter]=prob;
                prob=0;
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

    static double[][] getCol(double[][] m, int index){
        double[][] outCol = new double[1][m.length];
        for(int i = 0; i < m.length; i++){
            outCol[0][i] = m[i][index];
        }

        return outCol;
    }

    static int[][] viterbi(double[][] a, double[][] b, double[][] pi, int[][] emissions ){
        double[][] delta = new double[emissions.length][a.length];
        double [0] delta = (elementMultiplication(getCol(b,emissions[0]), pi))[0];
        double [][] deltam = new double[1][a.length];
        for (int i=0; i<emissions.length-1, i++ ){
            deltam[0]=delta[i];
            double [] deltaTemp = multiplication(deltam, a);
            deltaTemp = elementMultiplication(deltaTemp[0],getCol(b, emissions[i+1]));
            delta[i]=deltaTemp[0]

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
        states= new ArrayList<String>();


    }
}