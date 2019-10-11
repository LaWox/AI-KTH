//Platon Woxler platon@kth.se and Jussi Kangas jkangas@kth.se
import java.util.Scanner;
import java.lang.Math;

public class Main3 {

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

    // di-gamma function
    static void diGamma(double[][][] diGammaM, double[][] gammaM, double[][] alpha, double[][] beta, int[] emissions, double[][] a, double[][] b){

        int T = emissions.length;
        for(int t=0; t< T-1; t++){
            for(int i=0; i<a.length; i++){
                gammaM[i][t]=0;
                for(int j=0; j<a.length; j++){
                    diGammaM[i][j][t] = alpha[i][t]*a[i][j]*b[j][emissions[t+1]]*beta[j][t+1];
                    gammaM[i][t] += diGammaM[i][j][t];
                }
            }
        }

        for(int i=0; i<a.length; i++){
            gammaM[i][T-1] = alpha[i][T-1];
        }
    }
    // gamma function
    static double[][] gamma(double diGamma[][][], double alpha[][]){
        int T = diGamma[0][0].length;
        double [][] gamma = new double[diGamma.length][T];
        for (int t = 0; t<T-1; t++){
            for(int i = 0; i<diGamma.length; i++){
                gamma[i][t] = 0;
                for( int j=0; j<diGamma.length; j++){
                    gamma[i][t] += diGamma[i][j][t];

                }
            }

        }
        for( int k=0; k<diGamma.length; k++){
            gamma[k][gamma.length-1] = alpha[k][alpha.length-1];
        }
        return gamma;
    }

    static void estPi(double[][] gamma, double[][] pi){
        for(int i=0; i< gamma.length; i++){
            //System.out.println(gamma[i][0]);
            pi[0][i]=gamma[i][0];
        }
    }

    static void estA(double[][][] digamma, double[][] gamma, double[][] A){
        double denom;
        double numer;
        for (int i = 0; i< A.length; i++) {
            denom = 0;
            for (int t= 0; t < digamma[0][0].length-1; t++){
                denom += gamma[i][t];
            }
            for (int j=0; j < A.length; j++ ){
                numer=0;
                for(int k=0; k < digamma[0][0].length-1; k++){
                    numer += digamma[i][j][k];
                }
                A[i][j]= numer/denom;
            }
        }
    }

    static void estB(double[][][] digamma, double[][] gamma, double[][] B, int[] emis ){
        double denom;
        double numer;

        for (int i = 0; i< B.length; i++) {
            denom = 0;
            for (int t= 0; t < digamma[0][0].length; t++){
                denom += gamma[i][t];
            }
            for (int j=0; j < B[0].length; j++ ){
                numer=0;
                for(int k=0; k < digamma[0][0].length; k++){
                    if(emis[k]==j) {
                        numer += gamma[i][k];
                    }
                }
                B[i][j]= numer/denom;
            }
        }
    }

    static double logProb(double[] c){
        double logP = 0;
        for (int i=0; i< c.length; i++){
            logP += Math.log10(c[i]);
        }
        return -logP;
    }

    static void alphaP2(double[][] A, double[][] alpha, double[][] B, int[] emis, double[] c, double[][] pi ){
        double[][] observation;
        c[0]=0;
        observation = getCol(B, emis[0]);

        for(int i= 0; i<A.length; i++){
            alpha[i][0] = pi[0][i]*observation[0][i];
            c[0]+= alpha[i][0];
        }

        for(int t=1; t < emis.length; t++) {
            c[t] = 0;
            observation = getCol(B, emis[t]);
            for (int i = 0; i < A.length; i++) {
                alpha[i][t] = 0;
                for (int j = 0; j < A.length; j++) {
                    alpha[i][t] += alpha[j][t - 1] * A[j][i];
                }
                alpha[i][t] *= observation[0][i];
                c[t] += alpha[i][t];
            }
            c[t] = 1/c[t];
            for (int k = 0; k < A.length; k++) {
                alpha[k][t] *= c[t];
            }
        }
    }

    static void betaPass2(double[][] A, double[][] B, double[][] beta, double[] scaling, int[] emis){
        // ini last col of beta
        for(int i = 0; i < A.length; i++){
            beta[i][beta[0].length-1] = scaling[scaling.length-1];
        }
        // init beta
        for(int t = beta[0].length - 2; t > 0; t--) {
            for(int i = 0; i < A.length; i++){
                beta[i][t] = 0;
                for(int j = 0; j < A.length; j++){
                    beta[i][t] += A[i][j]*B[j][emis[t+1]]*beta[j][t+1];
                }
                // scale beta
                beta[i][t] *= scaling[t];
            }
        }
    }

    static void estimateLambda(double[][] A, double[][] B, double[][] pi, int[] emiSeq, double[][] alpha, double[][] beta, double[] c, double[][][] diGamma, double[][] gamma ){
        alphaP2(A, alpha, B, emiSeq, c, pi);
        //printMatrix(alphaM);
        betaPass2(A, B, beta, c, emiSeq);
        //printMatrix(betaM);
        diGamma(diGamma, gamma, alpha, beta, emiSeq, A, B);
        //System.out.println(diGammaM[0][0][0]);
        //printMatrix(gamma);

        estPi(gamma, pi);
        //printMatrix(piMatrix);
        estA(diGamma, gamma, A);
        estB(diGamma, gamma, B, emiSeq);
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

        // loop through len if emySeq
        for(int i=0; i<Integer.parseInt(emiStr[0]); i++){
            emiSeq[i]=Integer.parseInt(emiStr[i+1]);
            //System.out.println(emiSeq[i]);
        }

        // create matrixes
        AMatrix = createMatrix(aList);
        BMatrix = createMatrix(bList);
        piMatrix = createMatrix(pi);


        double logPCurrent = 0;
        double logPTemp = 0;
        int maxIterations = 100000000;
        int iters = 0;

        // alpha, beta, gamma and di-gamma
        double[][] alphaM = new double[AMatrix.length][emiSeq.length];
        double[][] betaM = new double[AMatrix.length][emiSeq.length];
        double[][] gamma = new double [AMatrix.length][emiSeq.length];
        double[] c = new double[emiSeq.length];
        double[][][] diGammaM = new double[AMatrix.length][AMatrix[0].length][emiSeq.length];

        estimateLambda(AMatrix, BMatrix, piMatrix, emiSeq, alphaM, betaM, c, diGammaM, gamma);
        logPTemp=logProb(c);
        estimateLambda(AMatrix, BMatrix, piMatrix, emiSeq, alphaM, betaM, c, diGammaM, gamma);
        logPCurrent=logProb(c);


        // Iterate until it converges
        while(iters < maxIterations && logPCurrent > logPTemp){
            estimateLambda(AMatrix, BMatrix, piMatrix, emiSeq, alphaM, betaM, c, diGammaM, gamma);
            logPTemp=logPCurrent;
            logPCurrent = logProb(c);
            iters++;
        }

        System.out.println(iters);
        // Print the A and B matrixes
        System.out.print(AMatrix.length + " " + AMatrix[0].length + " ");
        for(int i = 0; i < AMatrix.length; i++){
            for(int j = 0; j < AMatrix[0].length; j++){
                System.out.print(AMatrix[i][j] + " ");
            }
        }
        System.out.println();
        System.out.print(BMatrix.length + " " + BMatrix[0].length + " ");
        for(int i = 0; i < BMatrix.length; i++){
            for(int j = 0; j < BMatrix[0].length; j++){
                System.out.print(BMatrix[i][j] + " ");
            }
        }
        System.out.println();
        for(int i = 0; i < piMatrix[0].length; i++){
            System.out.print(piMatrix[0][i] + " ");
        }
    }
}
