
import lpsolve.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.StringTokenizer;


class Pivot7{

        Pivot7(int row, int column) {
            this.row = row;
            this.column = column;
        }

        int row;
        int column;

}


public class LpBigDecimal {

    static BufferedReader br;
    static PrintWriter out;
    static StringTokenizer st;
    static boolean eof;


    static MathContext mc = new MathContext(64, RoundingMode.FLOOR );  //half+up  brings bugs
    //static double bigNumber = 1000000000;
    static BigDecimal bigNumber = new BigDecimal(1000000000, mc);
    //static double tinyBit = 0.000001;
    static BigDecimal tinyBit = new BigDecimal(0.000000001, mc);
    static BigDecimal zeroMc = new BigDecimal(0, mc);
    static BigDecimal oneMc = new BigDecimal(1, mc);

    //double[][] a;
    BigDecimal[][] A;
    //double[] b;
    BigDecimal[] B;
    //double[] c;
    BigDecimal[] C;
    //double[] w; // objective with artificials in phase1
    BigDecimal[] W;
    //double w_value;  //value of objective function with artificials
    BigDecimal W_value;
    //double c_value;  //value of objective
    BigDecimal C_value;
    //double[] x;
    BigDecimal[] X;


    boolean phase1 = true;
    Pivot7 pivot;
    boolean debug = false;


    int n;
    int m;

    public LpBigDecimal(    BigDecimal[][] a0, BigDecimal[] b0, BigDecimal[] c0 ){
        n = a0.length;
        m = a0[0].length - 1;

        //a = new double[n][1+m+n+n];
        A = new BigDecimal[n][1+m+n+n];
        //b = new double[n];
        B = new BigDecimal[n];
        //c = new double[1+m+n+n];
        C = new BigDecimal[1+m+n+n];
        //x = new double[m];
        X = new BigDecimal[m];
        //w = new double[1+m+n+n];  //objective function phase1
        W = new BigDecimal[1+m+n+n];


        Arrays.fill(W, zeroMc);
        Arrays.fill(C, zeroMc);
        Arrays.fill(X, zeroMc);
        W_value = zeroMc;
        C_value = zeroMc;



        for(int i=0; i<n; i++){
            Arrays.fill(A[i], zeroMc);
            //a[i] = Arrays.copyOf(a0[i], 1 + m + n + n);
            //a[i][n+i+1] = 1;

            for(int j=0; j<1+m; j++){
                A[i][j] = a0[i][j];
            }
            A[i][n+i+1] = BigDecimal.valueOf(1);
        }

        B = b0.clone();

        for(int j=0; j<=m; j++){
            //c[j] = -c0[j];
            C[j] = c0[j].multiply(BigDecimal.valueOf(-1), mc);
        }
        C[0] = oneMc;


        //inverting negative , introducing artificial variables
        for(int i=0; i<n; i++){
            //if(b[i]<0){
            //    b[i] = -b[i];
            //    for(int j=1; j< 1+m+n; j++){
            //        a[i][j] = -a[i][j];
            //    }
            //    a[i][1+m+n+i] = 1;
            //}

            if(B[i].compareTo(zeroMc) < 0 ){
                B[i] = B[i].multiply(BigDecimal.valueOf(-1), mc);
                for(int j=1; j< 1+m+n; j++){
                    A[i][j] = A[i][j].multiply(BigDecimal.valueOf(-1), mc);
                }
                A[i][1+m+n+i] = oneMc;

            }

        }

        //calculating W  objective function with artificials
        for(int i=0; i<n; i++){
            //if(a[i][1+m+n+i] == 1 ){
            //    w_value -= b[i];
            //    for(int j=0; j<1+m+n; j++){
            //        w[j] -= a[i][j];
            //    }
            //}

            if(A[i][1+m+n+i].compareTo(oneMc) == 0 ){
                W_value = W_value.subtract(B[i], mc);
                for(int j=0; j<1+m+n; j++){
                    W[j] = W[j].subtract(A[i][j], mc );
                }
            }

        }
        W[0] = oneMc;

    }

    void printMatrix(){

        if(phase1){
            //print header
            System.out.printf("%-20s ", "W");
            for(int j=0; j<m; j++) System.out.printf("%-20s ", "X"+j);
            //print slack variables
            for(int j=0; j<n; j++) System.out.printf("%-20s ", "Y"+j);
            //print artificials
            for(int j=0; j<n; j++) System.out.printf("%-20s ", "A"+j);
            System.out.print("|\n");
            System.out.println("------------------------------------------------------------------");

            //print W  objective phase1
            for(int j=0; j<1+m+n+n; j++){
                System.out.printf("%.20f ", W[j]);
            }
            System.out.printf("|%.20f \n", W_value);
            System.out.println("------------------------------------------------------------------");


            //print C  objective
            for(int j=0; j<1+m+n; j++){
                System.out.printf("%.20f ", C[j]);
            }
            System.out.printf("          |%.20f \n", C_value);
            System.out.println("------------------------------------------------------------------");


            //print matrix
            for(int i=0; i<n; i++){
                // print the matrix

                for(int j=0; j<1+m+n+n; j++){
                    System.out.printf("%.20f ", A[i][j]);
                }

                //print b
                System.out.printf("|%.20f \n", B[i]);

            }

            System.out.println("------------------------------------------------------------------");



        }else{
            //print header
            System.out.printf("%-20s ", "C");
            for(int j=0; j<m; j++) System.out.printf("%-20s ", "X"+j);
            //print slack variables
            for(int j=0; j<n; j++) System.out.printf("%-20s ", "Y"+j);
            System.out.print("|\n");
            System.out.println("------------------------------------------------------------------");

            //print C  objective
            for(int j=0; j<1+m+n; j++){
                System.out.printf("%.20f ", C[j]);
            }
            System.out.printf("|%.20f \n", C_value);
            System.out.println("------------------------------------------------------------------");

            //print matrix
            for(int i=0; i<n; i++){
                // print the matrix
                for(int j=0; j<1+m+n; j++){
                    System.out.printf("%.20f ", A[i][j]);
                }

                //print b
                System.out.printf("|%.20f \n", B[i]);

            }

            System.out.println("------------------------------------------------------------------");

        }



    }

    static void printMatrix(BigDecimal[][] a, BigDecimal[] b){
        for(int k=0; k< a.length; k++){
            for(int l=0; l< a[0].length; l++){
                System.out.printf("%-5s ", a[k][l]);
            }
            System.out.printf("|%-5s\n", b[k]);
        }
        System.out.println(" =================================================== ");
    }



    static void infeasibleStressTest() throws LpSolveException {
        int size=8;
        int tmp;
        BigDecimal temp;
        Random random = new Random();
        int RANDOM_UPPERBOUND = 200;
        int HALF_UPPERBOUND = 100;

        for(int z=0; z<1000000000; z++){

            LpSolve lpSolve = LpSolve.makeLp(0, size);
            lpSolve.setVerbose(1);

            //fill array a[][]
            //fill array b[]
            double a1[][] = new double[size][size+1];
            BigDecimal a2[][] = new BigDecimal[size][size+1];
            double b1[] = new double[size];
            BigDecimal b2[] = new BigDecimal[size];
            double c1[] = new double[size+1];
            BigDecimal c2[] = new BigDecimal[size+1];
            double x1[] = new double[size];
            BigDecimal x2[] = new BigDecimal[size];

            for(int i=0; i<size; i++){
                a2[i][0] = zeroMc;
                for(int j=0; j<size; j++){
                    tmp = random.nextInt(RANDOM_UPPERBOUND) - HALF_UPPERBOUND;
                    a1[i][j+1] = tmp;
                    a2[i][j+1] = BigDecimal.valueOf(tmp);
                }

                tmp = random.nextInt(2000000) - 1000000 ;
                b1[i] = tmp;
                b2[i] = BigDecimal.valueOf( tmp);

                lpSolve.addConstraint(a1[i], LpSolve.LE, tmp);

            }


            //fill array c[]

            c2[0] = zeroMc;
            for(int j=0; j<size; j++){
                tmp = random.nextInt(RANDOM_UPPERBOUND) - HALF_UPPERBOUND ;
                c1[j+1] = tmp;
                c2[j+1] = BigDecimal.valueOf(tmp);

            }

            lpSolve.setMaxim();
            lpSolve.setObjFn(c1);




            //get result -1: no solution   0:bounded solution      1:unbounded solution
            LpBigDecimal lp = new LpBigDecimal(a2, b2, c2);


            int ans2 = lp.phase1();
            if(ans2 != -1){
                ans2 = lp.phase2();
            }
            x2 = lp.getSolution();

            int ans1 = lpSolve.solve();
            x1 = lpSolve.getPtrVariables();

            if((ans2 == -1) && (ans1 == 2)){
                //System.out.println("MATCH! No solution");
            }else if(ans2 == 1 &&
                    ((ans1 == 3) || lp.containInf(x1) )
                    ){
                //System.out.println("MATCH! unbounded solution");
            }else if(ans2==0 && ans1==0 ){
                //System.out.println("MATCH! feasible solution");
                //System.out.println(Arrays.toString(x1));
                //System.out.println(Arrays.toString(x2));
                temp = lp.C_value.subtract( BigDecimal.valueOf( lpSolve.getObjective() ));
                if( temp.abs().compareTo( BigDecimal.valueOf(0.001) ) > 0 ){
                    System.out.println("NO  MATCH!    values" );
                    System.out.printf("%.30f        ", temp.doubleValue());
                    System.out.printf("%.30f        ", lp.C_value.doubleValue());
                    System.out.printf("%.30f        ", lpSolve.getObjective() );
                    System.out.println();
                    //lpSolve.printLp();
                    //lp.printMatrix();
                }
            }
            else{
                System.out.println("NO  MATCH!    " + ans2 + " ;  " + ans1);
                lpSolve.printLp();
                printMatrix(a2, b2);

            }

        }
    }


/*
    static void feasibleTest() throws LpSolveException {

        int n=10;
        int m=9;

        double[][] a0 = new double[n][m+1];
        BigDecimal[][] A0 = new BigDecimal[n][m+1];
        double[] b0 = new double[n];
        BigDecimal[] B0 = new BigDecimal[n];
        double[] c0 = new double[m+1];
        BigDecimal[] C0 = new BigDecimal[m+1];

        a0[0] = new double[]{0, 17, 39, 69, 62, 72, -76, -57, -37, -16};      b0[0] = 4839;
        a0[1] = new double[]{0, 19, 11, -84, -30, -69, 65, 29, -9, -67 };    b0[1] = -7430;
        a0[2] = new double[]{0, -17, -37, -84, 49, 72, 27, -60, -96, -30};     b0[2] = -5842;
        a0[3] = new double[]{0,  46, -44, 8, -93, 39, 57, -98, 5, -24};     b0[3] = -10630;
        a0[4] = new double[]{0, 11, -99, -35, -9, 73, 90, -51, -53, 100};     b0[4] = 3291;
        a0[5] = new double[]{0, 35, -41, 54, -96, -38, -44, 40, -38, -42};     b0[5] = -8238;
        a0[6] = new double[]{0, 55, -27, 53, -56, -42, 77, -38, 20, -33};     b0[6] = -5307;
        a0[7] = new double[]{0, 18, -26, 5, 77, 100, -70, -73, 75, -36};     b0[7] = 5007;
        a0[8] = new double[]{0, -66, 81, 29, 58, -69, -67, 96, 48, 50};     b0[8] = 9231;
        a0[9] = new double[]{0, 85, -76, -11, -29, 96, -24, 79, -12, -63 };     b0[9] = 12393;


        c0 = new double[]{0, -91, -63, 43, 55, 86, -9, 11, 71, -64};


        for(int i=0; i<n; i++){

            for(int j=0; j<=m; j++){
                A0[i][j] = new BigDecimal(a0[i][j], mc);
            }
            B0[i] = new BigDecimal(b0[i], mc);

        }

        for(int j=0; j<=m; j++){
            C0[j] = new BigDecimal(c0[j], mc);
        }


        LpSolve lpSolve = LpSolve.makeLp(0, m);
        lpSolve.setVerbose(1);

        for(int i=0; i<n; i++){
            lpSolve.addConstraint(a0[i], LpSolve.LE, b0[i]);
        }

        lpSolve.setMaxim();
        lpSolve.setObjFn(c0);

        int res1=lpSolve.solve();
        //lpSolve.printLp();





        LpBigDecimal lp = new LpBigDecimal(A0, B0, C0);
        lp.debug = true;

        int res = lp.phase1();

        if(res == -1){
            System.out.println("Infeasible");
        }else{
            res = lp.phase2();
        }


        System.out.print( Arrays.toString( lpSolve.getPtrVariables() ));  System.out.printf("%.30f\n", lpSolve.getObjective());
        System.out.print(Arrays.toString(lp.getSolution()));  System.out.printf("%.30f\n", lp.C_value);

    }


    static void lpTest() throws LpSolveException {
        LpSolve lp = LpSolve.makeLp(0, 3);

        lp.addConstraint( new double[]{0, 2, 1, 1}, LpSolve.LE, 180 );
        lp.addConstraint( new double[]{0, 1, 3, 2}, LpSolve.LE, 300 );
        lp.addConstraint( new double[]{0, 2, 1, 2}, LpSolve.LE, 240 );

        lp.setMaxim();
        lp.setObjFn(new double[]{0, 6, 5, 4});

        lp.setVerbose(5);
        int res = lp.solve();
        lp.printLp();

        System.out.println(Arrays.toString( lp.getPtrVariables() ));

    }




    static void unboundedTest() throws LpSolveException {

        int n=2;
        int m=2;

        double[][] a0 = new double[n][m+1];
        double[] b0 = new double[n];
        double[] c0 = new double[m+1];

        a0[0] = new double[]{0, 0, -6};    b0[0] = -6;
        a0[1] = new double[]{0, 0, 6};     b0[1] = 6;


        c0 = new double[]{0, 5, 4};


        LpSolve lpSolve = LpSolve.makeLp(0, m);
        lpSolve.setVerbose(1);

        lpSolve.addConstraint(a0[0], LpSolve.LE, b0[0]);
        lpSolve.addConstraint(a0[1], LpSolve.LE, b0[1]);

        lpSolve.setMaxim();
        lpSolve.setObjFn(c0);

        int res1=lpSolve.solve();

        if(res1 == 3){           System.out.println(res1 + " Unbounded");   }else{            System.out.println(res1 + " ");        }
        lpSolve.printLp();

        System.out.println( Arrays.toString( lpSolve.getPtrVariables() ));



        Lp lp = new Lp(a0, b0, c0);
        lp.debug = true;

        int res = lp.phase1();

        if(res == -1){
            System.out.println("Infeasible");
        }else{
            lp.phase1 = false;
            res = lp.phase2();

            if(res == 0){
                System.out.println(" Feasible");
            }else if(res == 1){
                System.out.println(" Unbounded");
            }

        }



    }

    static void infeasibleTest() throws LpSolveException {


        int n=2;
        int m=2;

        double[][] a0 = new double[n][m+1];
        double[] b0 = new double[n];
        double[] c0 = new double[m+1];

        a0[0] = new double[]{0, -5, -5};    b0[0] = -4;
        a0[1] = new double[]{0, 6, -1};     b0[1] = 0;


        c0 = new double[]{0, 1, -1};


        LpSolve lpSolve = LpSolve.makeLp(0, m);
        lpSolve.setVerbose(1);

        lpSolve.addConstraint(a0[0], LpSolve.LE, b0[0]);
        lpSolve.addConstraint(a0[1], LpSolve.LE, b0[1]);

        lpSolve.setMaxim();
        lpSolve.setObjFn(c0);

        int res1=lpSolve.solve();

        if(res1 == 2){           System.out.println(res1 + " Infeasible");   }else{            System.out.println(res1 + " Feasible");        }
        lpSolve.printLp();

        System.out.println( Arrays.toString( lpSolve.getPtrVariables() ));



        Lp lp = new Lp(a0, b0, c0);
        lp.debug = true;

        int res = lp.phase1();

        if(res == -1){
            System.out.println("Infeasible");
        }else{
            System.out.println("Feasible");
        }



    }


    static void degeneracyTest() throws LpSolveException {

        int n=3;
        int m=4;

        double[][] a0 = new double[n][m+1];
        double[] b0 = new double[n];
        double[] c0 = new double[m+1];

        a0[0] = new double[]{0, 0.5, -5.5, -2.5, 9};      b0[0] = 0;
        a0[1] = new double[]{0, 0.5, -1.5, -0.5, 1};    b0[1] = 0;
        a0[2] = new double[]{0, 1, 0, 0, 0};     b0[2] = 1;


        c0 = new double[]{0, 10, -57, -9, -24};


        LpSolve lpSolve = LpSolve.makeLp(0, m);
        lpSolve.setVerbose(1);

        lpSolve.addConstraint(a0[0], LpSolve.LE, b0[0]);
        lpSolve.addConstraint(a0[1], LpSolve.LE, b0[1]);
        lpSolve.addConstraint(a0[2], LpSolve.LE, b0[1]);

        lpSolve.setMaxim();
        lpSolve.setObjFn(c0);

        int res1=lpSolve.solve();

        if(res1 == 2){           System.out.println(res1 + " Infeasible");   }else{            System.out.println(res1 + " Feasible");        }
        lpSolve.printLp();

        System.out.println( Arrays.toString( lpSolve.getPtrVariables() ));



        Lp lp = new Lp(a0, b0, c0);
        lp.debug = true;

        int res = lp.phase1();


        if(res == -1){
            System.out.println("Infeasible");
        }else{
            res = lp.phase2();
            if(res == 0){
                System.out.println( Arrays.toString( lp.getSolution() ));
            }else{
                System.out.println(res);
            }

        }



    }
*/

    int phase1(){
        int feasible=0;

        if(debug) printMatrix();

        pivot = selectPivot();

        while( pivot.row > -1 && pivot.column > -1 ){
            if(debug)System.out.println(" pivot : " + pivot.row + "," + pivot.column + "\n");
            processPivot(pivot);
            if(debug)printMatrix();
            pivot = selectPivot();
        }

        if(W_value.abs().compareTo(tinyBit) > 0){
            return -1;   //no solution
        }

        for(int i=0; i<n; i++){            if (B[i].compareTo( tinyBit.multiply(BigDecimal.valueOf(-1), mc) ) < 0) {                return -1;           }        } //no solution

        return feasible;
    }

    int phase2(){
        int feasible = 1; //unbounded
        phase1 = false;

        if(debug)printMatrix();

        pivot = selectPivot();

        while( pivot.row > -1 && pivot.column > -1 ){
            if(debug)System.out.println(" pivot : " + pivot.row + "," + pivot.column + "\n");
            processPivot(pivot);
            if(debug)printMatrix();
            pivot = selectPivot();
        }

        if(pivot.column == -1){
            return 0;
        }

        for(int i=0; i<n; i++){
            if(A[i][pivot.column].compareTo(tinyBit) >0 ) feasible = 0;
        }

        return feasible;
    }

    public static void main(String[] args) throws IOException {

        //feasibleTest();
        //infeasibleTest();
        //unboundedTest();
        //degeneracyTest();
        try {            infeasibleStressTest();        } catch (LpSolveException e) {            e.printStackTrace();        }
        System.exit(0);


        br = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(System.out);
        solve();
        out.close();


    }

    static void solve() throws IOException {
        int n = nextInt();
        int m = nextInt();
        int x;
        BigDecimal[][] A = new BigDecimal[n][m+1];


        for (int i = 0; i < n; i++) {
            A[i][0] = zeroMc;
            for (int j = 1; j <= m; j++) {
                A[i][j] = new BigDecimal(nextInt(), mc);
            }
        }

        BigDecimal[] b = new BigDecimal[n];
        for (int i = 0; i < n; i++) {
            b[i] = new BigDecimal(nextInt(), mc);
        }

        BigDecimal[] c = new BigDecimal[m+1];
        c[0] = oneMc;
        for (int i = 1; i <= m; i++) {
            c[i] = new BigDecimal(nextInt(), mc);
        }

        BigDecimal[] ansx = new BigDecimal[m];

        LpBigDecimal lp = new LpBigDecimal(A, b, c );
        int anst = lp.phase1();
        if(anst != -1){
            anst = lp.phase2();
            ansx = lp.getSolution();
        }


        if (anst == -1) {
            out.printf("No solution\n");
            return;
        }
        if (anst == 0) {
            out.printf("Bounded solution\n");
            for (int i = 0; i < m; i++) {
                out.printf("%.18f%c", ansx[i], i + 1 == m ? '\n' : ' ');
            }
            return;
        }
        if (anst == 1) {
            out.printf("Infinity\n");
            return;
        }
    }

    Pivot7 selectPivot(){
        Pivot7 pivot = new Pivot7(-1, -1);
        BigDecimal min;
        BigDecimal tmp;


        if(phase1){
            min = zeroMc;

            for(int j=0; j<W.length; j++){
                if(W[j].compareTo(min) < 0){
                    min = W[j];
                    pivot.column = j;
                }
            }

        }else{
            min = zeroMc;
            for(int j=0; j<1+m+n; j++){
                if(C[j].compareTo(min) < 0){
                    min = C[j];
                    pivot.column = j;
                }
            }
        }

        if (pivot.column == -1) return pivot;

        min = bigNumber;
        for(int i=0; i<A.length; i++){
            if( A[i][pivot.column].compareTo(tinyBit) > 0 ) {
                tmp = B[i].divide(A[i][pivot.column], mc);
                if( tmp.compareTo(min) < 0 ){
                    min = tmp;
                    pivot.row = i;
                }
            }
        }

        //if(pivot.row == -1) return new pivot(-1, -1);

        return pivot;

    }

    void processPivot(Pivot7 pivot){
        BigDecimal coeff = A[pivot.row][pivot.column];

        for(int j=0; j<A[0].length; j++){
            A[pivot.row][j] = A[pivot.row][j].divide(coeff, mc);
        }
        A[pivot.row][pivot.column] = oneMc;
        B[pivot.row] = B[pivot.row].divide(coeff, mc);

        for(int i=0; i<A.length; i++){
            if((i != pivot.row) && (A[i][pivot.column].abs().compareTo(tinyBit) > 0) ){
                coeff = A[i][pivot.column];
                for(int j=0; j<A[0].length; j++){
                    A[i][j] = A[i][j].subtract( coeff.multiply( A[pivot.row][j], mc ), mc  );
                    if(A[i][j].abs().compareTo(tinyBit) < 0 ) A[i][j] = zeroMc;
                }
                A[i][pivot.column] = zeroMc;

                B[i] = B[i].subtract( coeff.multiply(B[pivot.row], mc), mc );
                if(B[i].abs().compareTo(tinyBit) < 0) B[i] = zeroMc;
            }
        }


        if(phase1){
            if(W[pivot.column].abs().compareTo(tinyBit) > 0){
                coeff = W[pivot.column];
                for(int j=0; j<W.length; j++){
                    W[j] = W[j].subtract( coeff.multiply(A[pivot.row][j], mc), mc );
                    if(W[j].abs().compareTo(tinyBit) < 0) W[j] = zeroMc;
                }
                W[pivot.column] = zeroMc;
                W_value = W_value.subtract(coeff.multiply(B[pivot.row], mc), mc);
                if(W_value.abs().compareTo(tinyBit) < 0) W_value = zeroMc;
            }
        }

        if(C[pivot.column].abs().compareTo(tinyBit) > 0){
            coeff = C[pivot.column];
            for(int j=0; j<C.length; j++){
                C[j] = C[j].subtract( coeff.multiply(A[pivot.row][j], mc), mc );
                if(C[j].abs().compareTo(tinyBit) < 0) C[j] = zeroMc;
            }
            C[pivot.column] = zeroMc;
            C_value = C_value.subtract( coeff.multiply(B[pivot.row], mc), mc );
            if(C_value.abs().compareTo(tinyBit) < 0) C_value = zeroMc;
        }

    }

    BigDecimal[] getSolution(){
        BigDecimal x[] = new BigDecimal[m];
        int i=0;

        for(int j=0; j<m; j++){
            i = basicRow(A, j+1);
            if ( i != -1 ){
                x[j] = B[i];
            }else{
                x[j] = zeroMc;
            }

        }

        return x;
    }

    static int basicRow(BigDecimal a[][], int j){
        int count=0;
        int rownum=0;
        for(int i=0; i<a.length; i++){
            if( a[i][j].abs().compareTo(tinyBit) > 0 ){
                count++;
                rownum = i;
            }
        }

        if(count==1){
            return rownum;
        }else{
            return -1;
        }
    }

    boolean containInf( double anslp[]){
        for(double d : anslp){
            if( d >= 1.0E29 ) return true;
        }
        return false;
    }

    static String nextToken() {
        while (st == null || !st.hasMoreTokens()) {
            try {
                st = new StringTokenizer(br.readLine());
            } catch (Exception e) {
                eof = true;
                return null;
            }
        }
        return st.nextToken();
    }

    static int nextInt() throws IOException {
        return Integer.parseInt(nextToken());
    }




}
