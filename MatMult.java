//Multithreaded java program that multiples three matrices of higher order (upto 10^6)


import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;

class MatrixInitialisation {
    Scanner sc;
    int n;
    BigInteger A[][];
    BigInteger B[][];
    BigInteger C[][];
    BigInteger PartialAns[][];
    BigInteger finalAns[][];

    MatrixInitialisation(int n) {
        this.n = n;
        this.A = new BigInteger[n][n];
        this.B = new BigInteger[n][n];
        this.C = new BigInteger[n][n];
        this.PartialAns = new BigInteger[n][n];
        this.finalAns = new BigInteger[n][n];
    }

    public void InitialiseMatrices(Scanner sc) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = sc.nextBigInteger();
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                B[i][j] = sc.nextBigInteger();
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = sc.nextBigInteger();
            }
        }
    }

    public BigInteger[][] getA() {
        return A;
    }

    public BigInteger[][] getB() {
        return B;
    }

    public BigInteger[][] getC() {
        return C;
    }

    public BigInteger[][] getPartialAns() {
        return PartialAns;
    }

    public BigInteger[][] getfinalAns() {
        return finalAns;
    }
}

class MatrixMultiThread implements Runnable {
    MatrixInitialisation M;
    Thread t;
    int n;
    int CurrentRow;
    BigInteger A[][];
    BigInteger B[][];
    BigInteger D[][];

    MatrixMultiThread(MatrixInitialisation M, int n, int CurrentRow) {
        this.n = n;
        this.M = M;
        this.CurrentRow = CurrentRow;
        t = new Thread(this);
    }

    public void run() {
        ABMult();
    }

    public void ABMult() {
        BigInteger tempSum = BigInteger.ZERO;
        D = M.getPartialAns();
        A = M.getA();
        B = M.getB();

        for (int l = 0; l < n; l++) {
            for (int j = 0; j < n; j++) {
                tempSum = tempSum.add(A[CurrentRow][j].multiply(B[j][l]));
            }
            D[CurrentRow][l] = tempSum;
            tempSum = BigInteger.ZERO;
        }
    }
}

class finalMultiThread implements Runnable {
    Thread t;
    int n;
    BigInteger D[][];
    BigInteger C[][];
    int CurrentRow;
    BigInteger finalAns[][];

    finalMultiThread(MatrixInitialisation M, int n, int CurrentRow) {
        t = new Thread(this);
        this.C = M.getC();
        this.D = M.getPartialAns();
        this.n = n;
        this.CurrentRow = CurrentRow;
        this.finalAns = M.getfinalAns();
    }

    public void run() {
        BigInteger tempSum = BigInteger.ZERO;
        for (int l = 0; l < n; l++) {
            for (int j = 0; j < n; j++) {
                tempSum = tempSum.add(D[CurrentRow][j].multiply(C[j][l]));
            }
            finalAns[CurrentRow][l] = tempSum;
            tempSum = BigInteger.ZERO;
        }
    }
}

public class MatMult {
    public static void main(String[] args) {

        try {
            if (args.length == 0) {
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e + ": Error opening the specified file");
            return;
        }

        String filename = args[0];
        File file = new File(filename);
        Scanner sc = null;
        try {
            sc = new Scanner(file);
            int n = sc.nextInt();

            if (n < 1) {
                throw new IllegalArgumentException();
            }

            MatrixInitialisation M = new MatrixInitialisation(n);
            M.InitialiseMatrices(sc);

            MatrixMultiThread[] M1 = new MatrixMultiThread[n];
            finalMultiThread[] M2 = new finalMultiThread[n];

            try {
                for (int i = 0; i < n; i++) {
                    M1[i] = new MatrixMultiThread(M, n, i);
                    if (i > 0) {
                        M1[i - 1].t.join();
                    }
                    M1[i].t.start();
                }
                M1[n - 1].t.join();

                for (int i = 0; i < n; i++) {
                    M2[i] = new finalMultiThread(M, n, i);
                    if (i > 0) {
                        M2[i - 1].t.join();
                    }
                    M2[i].t.start();
                }
                M2[n - 1].t.join();

                BigInteger[][] finalAns = M.getfinalAns();
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        System.out.print(finalAns[i][j]);
                        if(i == n-1 && j == n-1){

                        }else{
                            System.out.print(" ");
                        }
                    }

                    //System.out.println();
                }

                //System.out.print("hellpo");
            } catch (InterruptedException e) {
                System.out.println(e + ": Thread interrupted");
            }

        } catch (FileNotFoundException e) {
            System.out.println(e + ": Error opening the specified file");
        } catch (IllegalArgumentException e) {
            System.out.println(e + ": Wrong argument provided");
        } finally {
            if (sc != null) {
                sc.close();
            }
        }
    }
}
