import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by mobichkin on 07.04.17.
 */
public class DietWA {


    static class Pivot7{

        Pivot7(int row, int column) {
            this.row = row;
            this.column = column;
        }

        int row;
        int column;

    }

    static class Position8 {
        Position8(int column, int row) {
            this.column = column;
            this.row = row;
        }

        int column;
        int row;
    }

    class Equation {
        Equation(BigDecimal a[][], BigDecimal b[]) {
            this.a = a;
            this.b = b;
        }
        BigDecimal a[][];
        BigDecimal b[];
    }


    public static class LpBigDecimal1 {

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

        public LpBigDecimal1(BigDecimal[][] a0, BigDecimal[] b0, BigDecimal[] c0){
            n = a0.length;
            m = c0.length - 1;

            //a = new double[n][1+read_length+n+n];
            A = new BigDecimal[n][1+m+n+n];
            //b = new double[n];
            B = new BigDecimal[n];
            //c = new double[1+read_length+n+n];
            C = new BigDecimal[1+m+n+n];
            //x = new double[read_length];
            X = new BigDecimal[m];
            //w = new double[1+read_length+n+n];  //objective function phase1
            W = new BigDecimal[1+m+n+n];


            Arrays.fill(W, BigDecimal.ZERO);
            Arrays.fill(C, BigDecimal.ZERO);
            Arrays.fill(X, BigDecimal.ZERO);
            W_value = BigDecimal.ZERO;
            C_value = BigDecimal.ZERO;



            for(int i=0; i<n; i++){
                Arrays.fill(A[i], BigDecimal.ZERO);
                //a[i] = Arrays.copyOf(a0[i], 1 + read_length + n + n);
                //a[i][n+i+1] = 1;

                for(int j=0; j<1+m; j++){
                    A[i][j] = a0[i][j];
                }
                A[i][m+i+1] = BigDecimal.ONE;
            }

            B = b0.clone();

            for(int j=0; j<=m; j++){
                //c[j] = -c0[j];
                C[j] = c0[j].multiply(BigDecimal.valueOf(-1), mc);
            }
            C[0] = BigDecimal.ONE;


            //inverting negative , introducing artificial variables
            for(int i=0; i<n; i++){
                //if(b[i]<0){
                //    b[i] = -b[i];
                //    for(int j=1; j< 1+read_length+n; j++){
                //        a[i][j] = -a[i][j];
                //    }
                //    a[i][1+read_length+n+i] = 1;
                //}

                if(B[i].compareTo(BigDecimal.ZERO) < 0 ){
                    B[i] = B[i].multiply(BigDecimal.valueOf(-1), mc);
                    for(int j=1; j< 1+m+n; j++){
                        A[i][j] = A[i][j].multiply(BigDecimal.valueOf(-1), mc);
                    }
                    A[i][1+m+n+i] = BigDecimal.ONE;

                }

            }

            //calculating W  objective function with artificials
            for(int i=0; i<n; i++){
                //if(a[i][1+read_length+n+i] == 1 ){
                //    w_value -= b[i];
                //    for(int j=0; j<1+read_length+n; j++){
                //        w[j] -= a[i][j];
                //    }
                //}

                if(A[i][1+m+n+i].compareTo(BigDecimal.ONE) == 0 ){
                    W_value = W_value.subtract(B[i], mc);
                    for(int j=0; j<1+m+n; j++){
                        W[j] = W[j].subtract(A[i][j], mc );
                    }
                }

            }
            W[0] = BigDecimal.ONE;

        }

        void printMatrix(){

            if(phase1){
                //print header
                System.out.printf("%-22s ", "W");
                for(int j=0; j<m; j++) System.out.printf("%-22s ", "X"+j);
                //print slack variables
                for(int j=0; j<n; j++) System.out.printf("%-22s ", "Y"+j);
                //print artificials
                for(int j=0; j<n; j++) System.out.printf("%-22s ", "A"+j);
                System.out.print("|\n");
                System.out.println("------------------------------------------------------------------");

                //print W  objective phase1
                for(int j=0; j<1+m+n+n; j++){
                    System.out.printf("%.20f ", W[j].doubleValue());
                }
                System.out.printf("|%.20f \n", W_value.doubleValue());
                System.out.println("------------------------------------------------------------------");


                //print C  objective
                for(int j=0; j<1+m+n; j++){
                    System.out.printf("%.20f ", C[j].doubleValue());
                }
                System.out.printf("          |%.20f \n", C_value.doubleValue());
                System.out.println("------------------------------------------------------------------");


                //print matrix
                for(int i=0; i<n; i++){
                    // print the matrix

                    for(int j=0; j<1+m+n+n; j++){
                        System.out.printf("%.20f ", A[i][j].doubleValue());
                    }

                    //print b
                    System.out.printf("|%.20f \n", B[i].doubleValue());

                }

                System.out.println("------------------------------------------------------------------");



            }else{
                //print header
                System.out.printf("%-22s ", "C");
                for(int j=0; j<m; j++) System.out.printf("%-22s ", "X"+j);
                //print slack variables
                for(int j=0; j<n; j++) System.out.printf("%-22s ", "Y"+j);
                System.out.print("|\n");
                System.out.println("------------------------------------------------------------------");

                //print C  objective
                for(int j=0; j<1+m+n; j++){
                    System.out.printf("%.20f ", C[j].doubleValue());
                }
                System.out.printf("|%.20f \n", C_value.doubleValue());
                System.out.println("------------------------------------------------------------------");

                //print matrix
                for(int i=0; i<n; i++){
                    // print the matrix
                    for(int j=0; j<1+m+n; j++){
                        System.out.printf("%.20f ", A[i][j].doubleValue());
                    }

                    //print b
                    System.out.printf("|%.20f \n", B[i].doubleValue());

                }

                System.out.println("------------------------------------------------------------------");

            }



        }

        void printMatrix(BigDecimal[][] a, BigDecimal[] b){
            for(int k=0; k< a.length; k++){
                for(int l=0; l< a[0].length; l++){
                    System.out.printf("%-5s ", a[k][l]);
                }
                System.out.printf("|%-5s\n", b[k]);
            }
            System.out.println(" =================================================== ");
        }


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

            for(int i=0; i<n; i++){            if (B[i].compareTo( veryTinyBit.multiply(BigDecimal.valueOf(-1), mc) ) < 0) {                return -1;           }        } //no solution

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
                if(A[i][pivot.column].compareTo(veryTinyBit) >0 ) feasible = 0;
            }

            return feasible;
        }

        Pivot7 selectPivot(){
            Pivot7 pivot = new Pivot7(-1, -1);
            BigDecimal min;
            BigDecimal tmp, tmp2;


            if(phase1){
                min = BigDecimal.ZERO;

                for(int j=0; j<W.length; j++){
                    tmp = W[j].subtract(min);

                    if(tmp.abs().compareTo(veryTinyBit) > 0 && W[j].compareTo(min) < 0){
                        min = W[j];
                        pivot.column = j;
                    }
                }

            }else{
                min = BigDecimal.ZERO;
                for(int j=0; j<1+m+n; j++){
                    tmp = C[j].subtract(min);

                    if(tmp.abs().compareTo(veryTinyBit) > 0 &&  C[j].compareTo(min) < 0){
                        min = C[j];
                        pivot.column = j;
                    }
                }
            }

            if (pivot.column == -1) return pivot;

            min = bigNumber;
            for(int i=0; i<A.length; i++){
                if( A[i][pivot.column].compareTo(veryTinyBit) > 0 ) {
                    tmp = B[i].divide(A[i][pivot.column], mc);
                    tmp2 = tmp.subtract(min);

                    if( tmp2.abs().compareTo(veryTinyBit) > 0 && tmp.compareTo(min) < 0 ){
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
            A[pivot.row][pivot.column] = BigDecimal.ONE;
            B[pivot.row] = B[pivot.row].divide(coeff, mc);

            for(int i=0; i<A.length; i++){
                if((i != pivot.row) && (A[i][pivot.column].abs().compareTo(veryTinyBit) > 0) ){
                    coeff = A[i][pivot.column];
                    for(int j=0; j<A[0].length; j++){
                        A[i][j] = A[i][j].subtract( coeff.multiply( A[pivot.row][j], mc ), mc  );
                        if(A[i][j].abs().compareTo(veryTinyBit) < 0 ) A[i][j] = BigDecimal.ZERO;
                    }
                    A[i][pivot.column] = BigDecimal.ZERO;

                    B[i] = B[i].subtract( coeff.multiply(B[pivot.row], mc), mc );
                    if(B[i].abs().compareTo(veryTinyBit) < 0) B[i] = BigDecimal.ZERO;
                }
            }


            if(phase1){
                if(W[pivot.column].abs().compareTo(veryTinyBit) > 0){
                    coeff = W[pivot.column];
                    for(int j=0; j<W.length; j++){
                        W[j] = W[j].subtract( coeff.multiply(A[pivot.row][j], mc), mc );
                        if(W[j].abs().compareTo(veryTinyBit) < 0) W[j] = BigDecimal.ZERO;
                    }
                    W[pivot.column] = BigDecimal.ZERO;
                    W_value = W_value.subtract(coeff.multiply(B[pivot.row], mc), mc);
                    if(W_value.abs().compareTo(veryTinyBit) < 0) W_value = BigDecimal.ZERO;
                }
            }

            if(C[pivot.column].abs().compareTo(veryTinyBit) > 0){
                coeff = C[pivot.column];
                for(int j=0; j<C.length; j++){
                    C[j] = C[j].subtract( coeff.multiply(A[pivot.row][j], mc), mc );
                    if(C[j].abs().compareTo(veryTinyBit) < 0) C[j] = BigDecimal.ZERO;
                }
                C[pivot.column] = BigDecimal.ZERO;
                C_value = C_value.subtract( coeff.multiply(B[pivot.row], mc), mc );
                if(C_value.abs().compareTo(veryTinyBit) < 0) C_value = BigDecimal.ZERO;
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
                    x[j] = BigDecimal.ZERO;
                }

            }

            return x;
        }

        int basicRow(BigDecimal a[][], int j){
            int count=0;
            int rownum=-1;
            for(int i=0; i<a.length; i++){
                if( a[i][j].abs().compareTo(veryTinyBit) > 0 ){
                    count++;
                    if( a[i][j].compareTo(BigDecimal.ONE) == 0){

                        rownum = i;
                    }

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


    }

    static BufferedReader br;
    static PrintWriter out;
    static StringTokenizer st;
    static boolean eof;

    static BigDecimal bigNumber0 = BigDecimal.valueOf(1000000000L);
    static BigDecimal tinyBit0 = BigDecimal.valueOf(0.0001);
    static BigDecimal tinyTinyBit0 = BigDecimal.valueOf(0.000000001);
    static MathContext mc0 = new MathContext(16, RoundingMode.HALF_UP );


    static MathContext mc = new MathContext(64, RoundingMode.HALF_UP );  //half+up  brings bugs
    static BigDecimal bigNumber = new BigDecimal(10e16, mc);
    static BigDecimal tinyBit = new BigDecimal(0.00001, mc);
    static BigDecimal veryTinyBit = new BigDecimal(10e-16, mc);



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


    int solveDietProblem(int n, int m, BigDecimal[][] A, BigDecimal[] b, BigDecimal[] c, BigDecimal[] x) {


        BigDecimal[][] a_full = new BigDecimal[ n + m + 1 ][m];
        BigDecimal[] b_full = new BigDecimal[ n + m + 1 ];
        BigDecimal[][] a_current = new BigDecimal[m][m];
        BigDecimal[] b_current = new BigDecimal[m];
        BigDecimal[] x_current = new BigDecimal[m];
        BigDecimal result_current;
        BigDecimal result = bigNumber0.multiply(BigDecimal.valueOf(-1));
        boolean hasInfinity = false;

        for(int i=0; i<n; i++){
            a_full[i] = A[i];
            b_full[i] = b[i];
        }

        for(int i=0; i<m; i++ ){
            for(int j=0; j<m; j++){
                a_full[n+i][j] = BigDecimal.ZERO;
            }
        }

        for(int i=0; i<m; i++ ){
            a_full[n+i][i] = BigDecimal.valueOf(-1);
            b_full[n+i] = BigDecimal.ZERO;
        }

        for(int i=0; i<a_full[n+m].length; i++){
            a_full[n+m][i] = BigDecimal.valueOf(1);
        }
        b_full[n+m] = bigNumber0;


        List<int[]> subsets = listSubsets(a_full.length,m);

        // loop for arrays combination
        hasInfinity = false;
        for(int[] subset : subsets ){


            //System.out.println( Arrays.toString(subset) );
            for(int j=0; j<m; j++ ){
                for(int k=0; k<m; k++){
                    a_current[j][k] = a_full[ subset[j] ][k];
                }
                b_current[j] = b_full[ subset[j] ];
            }

            x_current = SolveEquation(new Equation(a_current, b_current ));
            //PrintRow(x_current);


            //verify the solution x_current
            if(verify(x_current, a_full, b_full )){

                result_current = BigDecimal.ZERO;
                for(int j=0; j<m; j++){
                    //result_current += c[j]*x_current[j];
                    result_current = result_current.add( c[j].multiply(x_current[j], mc0), mc0);
                }


                BigDecimal result_plus_tinyBit = result.add(tinyTinyBit0);

                if(
                        ( result_current.compareTo( result_plus_tinyBit ) > 0  && (subset[m-1] == n+m) )
                                || ( (result_current.compareTo(result) >=0 ) && (subset[m-1] != n+m))
                        )
                {


                    if(subset[m-1] == n+m){
                        hasInfinity = true;
                    }else{
                        hasInfinity = false;
                    }

                    result = result_current;
                    for(int j=0; j<m; j++) x[j] = x_current[j];

                }

            }
        }



        //if(result <= -bigNumber0 + 0.0001){

        //int comp = result.abs().compareTo( tinyTinyBit0 );

        if(hasInfinity && result.compareTo(tinyTinyBit0) > 0 ){
            return 1;
        }else if(result.subtract(tinyBit0).compareTo( bigNumber0.multiply(BigDecimal.valueOf(-1)) ) <= 0){
            return -1;
        } else {
            return 0;
        }



    }

    List<int[]> listSubsets(int n, int m){
        List<int[]> result = new ArrayList<>();

        for( int i=0; i<(1<<n); i++){

            if( Long.bitCount(i) == m){
                int[] subset = new int[m];
                int k=0;

                for(int j=0; j<n; j++){
                    if( (i & (1<<j)) > 0){

                        subset[k++] = j;
                    }
                }

                result.add(subset);

            }

        }

        return result;
    }

    boolean verify(BigDecimal[] x_current, BigDecimal[][] a_full, BigDecimal[] b_full ){
        if(x_current == null) return false;
        BigDecimal result;

        for(int i=0; i<a_full.length; i++){
            //result = 0;
            result = BigDecimal.ZERO;
            for(int j=0; j<x_current.length; j++){
                //result += a_full[i][j] * x_current[j];
                result = result.add( a_full[i][j].multiply( x_current[j], mc0), mc0);

                //System.out.print( a_full[i][j] + "*" + x_current[j] + " + ");
            }
            //if( result > b_full[i] + 0.0001){
            //System.out.println( " <= " + b_full[i] );
            if( result.subtract(tinyBit0, mc0).compareTo( b_full[i])  > 0){

                return false;
            }

        }
        return true;
    }

    static Position8 SelectPivotElement(BigDecimal a[][], int used_row, int used_column) {
        // This algorithm selects the first free element.
        // You'll need to improve it to pass the problem.
        Position8 pivot_element = new Position8(used_row, used_column);

        for ( int i = used_row; i < a.length; i++ ){
            if ( a[i][pivot_element.column].abs().compareTo( a[pivot_element.row][pivot_element.column].abs())  > 0 ){
                pivot_element.row = i;
            }

//            for ( int j = used_column; j < a[0].length; j++){
//                if ( a[i][j].abs().compareTo( a[pivot_element.row][pivot_element.column].abs())  > 0 ){

            //if( Math.abs(a[i][pivot_element.column]) > Math.abs(a[pivot_element.row][pivot_element.column] )  ){
            //pivot_element.row = i;
            //pivot_element.column = j;
//                }
//            }
        }

        return pivot_element;
    }

    static void SwapLines(BigDecimal a[][], BigDecimal b[], int used_row, int used_column, Position8 pivot, int order[]) {
        BigDecimal tmp;

        //swap   used_row   and    pivot.row
        for(int j=0; j<a[0].length; j++){
            tmp = a[used_row][j];
            a[used_row][j] = a[pivot.row][j];
            a[pivot.row][j] = tmp;
        }

        //swap   used_column   and  pivot.column
        //for(int i=0; i<a.length; i++){
//            tmp = a[i][used_column];
//            a[i][used_column] = a[i][pivot.column];
//            a[i][pivot.column] = tmp;
//        }

        //swap  b[used_row]  and  b[pivot.row]
        tmp = b[used_row];
        b[used_row] = b[pivot.row];
        b[pivot.row] = tmp;

        //change order,  order[ used_column ]  & pivot.column
//        int t = order[used_column];
//        order[used_column] = order[pivot.column];
//        order[pivot.column] = t;

        //change pivot
        pivot.row = used_row;
        pivot.column = used_column;

    }

    static void ProcessPivotElement(BigDecimal a[][], BigDecimal b[], Position8 pivot) {

        //if( a[pivot.row][pivot.column] == 0) return;
        if( a[pivot.row][pivot.column].abs().compareTo(tinyTinyBit0) < 0) return;

        //BigDecimal coeff = a[pivot.row][pivot.column];
        BigDecimal coeff = BigDecimal.ONE.divide( a[pivot.row][pivot.column] , mc0);

        a[pivot.row][pivot.column] = BigDecimal.ONE;
        for(int j=pivot.column+1; j<a[0].length; j++){
            if( a[pivot.row][j].abs().compareTo(tinyTinyBit0) > 0 ){
                //a[pivot.row][j] = a[pivot.row][j].divide(coeff, mc0);
                a[pivot.row][j] = a[pivot.row][j].multiply(coeff, mc0);
            }
        }

        if( b[pivot.row].abs().compareTo(tinyTinyBit0) > 0){
            //b[pivot.row] = b[pivot.row].divide(coeff, mc0 );
            b[pivot.row] = b[pivot.row].multiply(coeff, mc0);
        }


        for(int i=pivot.row+1; i<a.length; i++){
            //coeff = a[i][pivot.column]  / a[pivot.row][pivot.column];
            if( a[i][pivot.column].abs().compareTo(tinyTinyBit0) < 0) continue;

            coeff = a[i][pivot.column].divide( a[pivot.row][pivot.column], mc0);
            a[i][pivot.column] = BigDecimal.ZERO;
            for(int j = pivot.column+1; j<a[0].length; j++ ){

                if(a[pivot.row][j].abs().compareTo(tinyTinyBit0) > 0){
                    a[i][j] = a[i][j].subtract( a[pivot.row][j].multiply( coeff, mc0), mc0);
                }

            }

            if(b[pivot.row].abs().compareTo(tinyTinyBit0) > 0){
                b[i] = b[i].subtract( b[pivot.row].multiply( coeff, mc0), mc0);
            }

        }


    }

    static void MarkPivotElementUsed(Position8 pivot_element, boolean used_rows[], boolean used_columns[]) {
        used_rows[pivot_element.row] = true;
        used_columns[pivot_element.column] = true;
    }

    BigDecimal[] SolveEquation(Equation equation) {

        BigDecimal a[][] = new BigDecimal[equation.a.length][];
        for(int i=0; i<equation.a.length; i++){
            a[i] =  equation.a[i].clone();
        }

        BigDecimal b[] = equation.b.clone();

        int size = a.length;
        BigDecimal b1[] = new BigDecimal[size];

        //boolean[] used_columns = new boolean[size];
        //boolean[] used_rows = new boolean[size];
        int used_row = 0;
        int used_column = 0;

        int[] order = new int[size];
        for(int k=0; k<size; k++) { order[k] = k; }


        for (int i = 0; i < size; ++i) {

            Position8 pivot_element = SelectPivotElement(a, used_row, used_column);
            SwapLines(a, b, used_row, used_column, pivot_element, order);
            ProcessPivotElement(a, b, pivot_element);

            used_row++;                //MarkPivotElementUsed(pivot_element, used_row, used_column);
            used_column++;

        }

        for (int i=size-1; i>=0; i--){
            for (int j=i+1; j<size; j++){
                //b[i] -= a[i][j] * b[j];
                b[i] = b[i].subtract(a[i][j].multiply(b[j], mc0), mc0);

                //a[i][j] = 0;
                a[i][j] = BigDecimal.ZERO;
            }

            //if((a[i][i]==0)&& (b[i] != 0)){
            if(( a[i][i].abs().compareTo(tinyTinyBit0) < 0) && b[i].abs().compareTo(tinyTinyBit0) > 0 ){
                return null;
            }

        }

        //for(int i=0; i<size; i++){                      b1[order[i]] = b[ i ];        }
        //return b1;

        return b;

    }


    public static void main(String[] args) throws IOException {

        DietWA dietWA = new DietWA();

        dietWA.br = new BufferedReader(new InputStreamReader(System.in));
        dietWA.out = new PrintWriter(System.out);

        int n = nextInt();
        int m = nextInt();

        if(n<=6 && m<=6){
            int x;
            BigDecimal[][] A = new BigDecimal[n][m];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    //A[i][j] = nextInt();
                    x = nextInt();
                    if(x==0)
                        A[i][j] = BigDecimal.ZERO;
                    else
                        A[i][j] = BigDecimal.valueOf( x );
                }
            }

            BigDecimal[] b = new BigDecimal[n];
            for (int i = 0; i < n; i++) {
                //b[i] = nextInt();
                x = nextInt();
                if( x== 0)
                    b[i] = BigDecimal.ZERO;
                else
                    b[i] = BigDecimal.valueOf( x );
            }

            BigDecimal[] c = new BigDecimal[m];
            for (int i = 0; i < m; i++) {
                //c[i] = nextInt();
                c[i] = BigDecimal.valueOf( nextInt());
            }

            BigDecimal[] ansx = new BigDecimal[m];

            int anst = dietWA.solveDietProblem(n, m, A, b, c, ansx);
            if (anst == -1) {
                System.out.printf("No solution\n");
                return;
            }
            if (anst == 0) {
                System.out.printf("Bounded solution\n");
                for (int i = 0; i < m; i++) {
                    System.out.printf("%.18f%c", ansx[i], i + 1 == m ? '\n' : ' ');
                }
                return;
            }
            if (anst == 1) {
                System.out.printf("Infinity\n");
                return;
            }


        }else{
            //LpBigDecimal
            int x;
            BigDecimal[][] A = new BigDecimal[n][m+1];


            for (int i = 0; i < n; i++) {
                A[i][0] = BigDecimal.ZERO;
                for (int j = 1; j <= m; j++) {
                    A[i][j] = new BigDecimal(nextInt(), mc);
                }
            }

            BigDecimal[] b = new BigDecimal[n];
            for (int i = 0; i < n; i++) {
                b[i] = new BigDecimal(nextInt(), mc);
            }

            BigDecimal[] c = new BigDecimal[m+1];
            c[0] = BigDecimal.ONE;
            for (int i = 1; i <= m; i++) {
                c[i] = new BigDecimal(nextInt(), mc);
            }

            BigDecimal[] ansx = new BigDecimal[m];

            LpBigDecimal1 lp = new LpBigDecimal1(A, b, c );
            int anst = lp.phase1();
            if(anst != -1){
                anst = lp.phase2();
                ansx = lp.getSolution();
            }


            if (anst == -1) {
                System.out.printf("No solution\n");
                return;
            }
            if (anst == 0) {
                System.out.printf("Bounded solution\n");
                for (int i = 0; i < m; i++) {
                    System.out.printf("%.18f%c", ansx[i], i + 1 == m ? '\n' : ' ');
                }
                return;
            }
            if (anst == 1) {
                System.out.printf("Infinity\n");
                return;
            }


        }


        dietWA.out.close();

    }
}


