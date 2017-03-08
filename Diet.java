import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Diet {

    class Position {
        Position(int column, int row) {
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

/*
        Equation(double a[][], double b[]) {
            this.a = a;
            this.b = b;
        }

        double a[][];
        double b[];
*/
    }


    //double bigNumber = 1000000000;
    BigDecimal bigNumber = BigDecimal.valueOf(1000000000);

    BufferedReader br;
    PrintWriter out;
    StringTokenizer st;
    boolean eof;

    int solveDietProblem(int n, int m, BigDecimal[][] A, BigDecimal[] b, BigDecimal[] c, BigDecimal[] x) {

        //double[][] a_full = new double[ n + m + 1 ][m];
        BigDecimal[][] a_full = new BigDecimal[ n + m + 1 ][m];
        //double[] b_full = new double[ n + m + 1 ];
        BigDecimal[] b_full = new BigDecimal[ n + m + 1 ];
        //double[][] a_current = new double[m][m];
        BigDecimal[][] a_current = new BigDecimal[m][m];
        //double[] b_current = new double[m];
        BigDecimal[] b_current = new BigDecimal[m];
        //double[] x_current = new double[m];
        BigDecimal[] x_current = new BigDecimal[m];

        //double result_current;
        BigDecimal result_current;
        //double result = -bigNumber;
        BigDecimal result = bigNumber.multiply(BigDecimal.valueOf(-1));

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


        //Arrays.fill(a_full[n+m], 1);
        for(int i=0; i<a_full[n+m].length; i++){
            a_full[n+m][i] = BigDecimal.valueOf(1);
        }
        b_full[n+m] = bigNumber;



        List<int[]> subsets = listSubsets(a_full.length,m);
        // loop for arrays combination
        for(int[] subset : subsets ){
            //System.out.println( Arrays.toString(subset) );
            for(int j=0; j<m; j++ ){
                for(int k=0; k<m; k++){
                    a_current[j][k] = a_full[ subset[j] ][k];
                }
                b_current[j] = b_full[ subset[j] ];
            }

            x_current = SolveEquation(new Equation(a_current, b_current ));


            //verify the solution x_current
            //if(verify(x_current, a_full, b_full )){
            if(verify(x_current, a_full, b_full )){
                //result_current = 0;
                result_current = BigDecimal.ZERO;

                for(int j=0; j<m; j++){
                    //result_current += x_current[j];
                    result_current = result_current.add( x_current[j] );
                }
                //if( result_current >= bigNumber - 0.0001) {    // if unbounded solution
                if( result_current.compareTo(  bigNumber ) >= 0 ) {    // if unbounded solution
                    return 1;
                }


                //result_current = 0;
                result_current = BigDecimal.ZERO;

                for(int j=0; j<m; j++){
                    //result_current += c[j]*x_current[j];
                    result_current = result_current.add( c[j].multiply(x_current[j] ));
                }

                //if( result_current > result ){
                if( result_current.compareTo( result ) > 0 ){
                    result = result_current;
                    for(int j=0; j<m; j++) x[j] = x_current[j];

                }

            }
        }



        //if(result <= -bigNumber + 0.0001){
        if(result.compareTo( bigNumber.multiply(BigDecimal.valueOf(-1)) ) <= 0){
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

    //boolean verify(double[] x_current, double[][] a_full, double[] b_full ){
    boolean verify(BigDecimal[] x_current, BigDecimal[][] a_full, BigDecimal[] b_full ){
        if(x_current == null) return false;

        //double result;
        BigDecimal result;

        for(int i=0; i<a_full.length; i++){
            //result = 0;
            result = BigDecimal.ZERO;
            for(int j=0; j<x_current.length; j++){
                 //result += a_full[i][j] * x_current[j];
                result = result.add( a_full[i][j].multiply( x_current[j]) );

                //System.out.print( a_full[i][j] + "*" + x_current[j] + " + ");
            }
            //if( result > b_full[i] + 0.0001){
            //System.out.println( " <= " + b_full[i] );
            if( result.compareTo( b_full[i] ) > 0){

                return false;
            }

        }
        return true;
    }

//    Position SelectPivotElement(double a[][], boolean used_raws[], boolean used_columns[]) {
      Position SelectPivotElement(BigDecimal a[][], boolean used_raws[], boolean used_columns[]) {
        // This algorithm selects the first free element.
        // You'll need to improve it to pass the problem.
        Position pivot_element = new Position(0, 0);
        while (used_raws[pivot_element.row])
            ++pivot_element.row;
        while (used_columns[pivot_element.column])
            ++pivot_element.column;

        int i = pivot_element.row;
        while ( i < a.length ){

            if ( a[i][pivot_element.column].abs().compareTo( a[pivot_element.row][pivot_element.column].abs())  > 0 ){

            //if( Math.abs(a[i][pivot_element.column]) > Math.abs(a[pivot_element.row][pivot_element.column] )  ){
                pivot_element.row = i;
            }
            i++;
        }

        return pivot_element;
    }

    //static void SwapLines(double a[][], double b[], boolean used_rows[], Position pivot_element) {
    static void SwapLines(BigDecimal a[][], BigDecimal b[], boolean used_rows[], Position pivot_element) {
        int size = a.length;

        for (int column = 0; column < size; ++column) {
            //double tmpa = a[pivot_element.column][column];
            BigDecimal tmpa = a[pivot_element.column][column];
            a[pivot_element.column][column] = a[pivot_element.row][column];
            a[pivot_element.row][column] = tmpa;
        }

        //double tmpb = b[pivot_element.column];
        BigDecimal tmpb = b[pivot_element.column];

        b[pivot_element.column] = b[pivot_element.row];
        b[pivot_element.row] = tmpb;

        boolean tmpu = used_rows[pivot_element.column];
        used_rows[pivot_element.column] = used_rows[pivot_element.row];
        used_rows[pivot_element.row] = tmpu;

        pivot_element.row = pivot_element.column;
    }

    //static void ProcessPivotElement(double a[][], double b[], Position pivot) {
    static void ProcessPivotElement(BigDecimal a[][], BigDecimal b[], Position pivot) {

        //if( a[pivot.row][pivot.column] == 0) return;
        if( a[pivot.row][pivot.column].compareTo( BigDecimal.ZERO ) == 0) return;

        //double coeff = 1 / a[pivot.row][pivot.column];
        BigDecimal coeff = BigDecimal.valueOf(1).divide(a[pivot.row][pivot.column], 18, RoundingMode.HALF_UP);

        for(int j=pivot.column; j<a[0].length; j++){

            if( !a[pivot.row][j].equals(BigDecimal.ZERO) ){
                a[pivot.row][j] = a[pivot.row][j].multiply(coeff);
            }
        }

        if(!b[pivot.row].equals(BigDecimal.ZERO)){
            b[pivot.row].multiply( coeff);
        }


        for(int i=pivot.row+1; i<a.length; i++){
            //coeff = a[i][pivot.column]  / a[pivot.row][pivot.column];
            if( a[i][pivot.column].equals(BigDecimal.ZERO)) continue;

            coeff = a[i][pivot.column].divide( a[pivot.row][pivot.column], 18, RoundingMode.HALF_UP );
            for(int j = pivot.column; j<a[0].length; j++ ){

                if(!a[pivot.row][j].equals(BigDecimal.ZERO)){
                    a[i][j] = a[i][j].subtract( a[pivot.row][j].multiply( coeff ) );
                }

            }

            if(!b[i].equals(BigDecimal.ZERO)){
                b[i] = b[i].subtract( b[pivot.row].multiply( coeff ) );
            }

        }


    }

    static void MarkPivotElementUsed(Position pivot_element, boolean used_rows[], boolean used_columns[]) {
        used_rows[pivot_element.row] = true;
        used_columns[pivot_element.column] = true;
    }

//    double[] SolveEquation(Equation equation) {
      BigDecimal[] SolveEquation(Equation equation) {

        //double a[][] = equation.a;
        //double b[] = equation.b;

        BigDecimal a[][] = equation.a;
        BigDecimal b[] = equation.b;
        int size = a.length;

        boolean[] used_columns = new boolean[size];
        boolean[] used_rows = new boolean[size];

        for (int i = 0; i < size; ++i) {
            Position pivot_element = SelectPivotElement(a, used_rows, used_columns);
            SwapLines(a, b, used_rows, pivot_element);
            ProcessPivotElement(a, b, pivot_element);
            MarkPivotElementUsed(pivot_element, used_rows, used_columns);
        }

        for (int i=size-1; i>=0; i--){
            for (int j=i+1; j<size; j++){
                //b[i] -= a[i][j] * b[j];
                b[i] = b[i].subtract( a[i][j].multiply( b[j] ));

                //a[i][j] = 0;
                a[i][j] = BigDecimal.ZERO;
            }

            //if((a[i][i]==0)&& (b[i] != 0)){
            if( a[i][i].equals( BigDecimal.ZERO ) && !b[i].equals( BigDecimal.ZERO )){
                return null;
            }

        }

        return b;
    }

    void solve() throws IOException {
        int n = nextInt();
        int m = nextInt();
        int x;
        //double[][] A = new double[n][m];
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
        //double[] b = new double[n];
        BigDecimal[] b = new BigDecimal[n];
        for (int i = 0; i < n; i++) {
            //b[i] = nextInt();
            x = nextInt();
            if( x== 0)
                b[i] = BigDecimal.ZERO;
            else
                b[i] = BigDecimal.valueOf( x );
        }

        //double[] c = new double[m];
        BigDecimal[] c = new BigDecimal[m];
        for (int i = 0; i < m; i++) {
            //c[i] = nextInt();
            c[i] = BigDecimal.valueOf( nextInt());
        }

        //double[] ansx = new double[m];
        BigDecimal[] ansx = new BigDecimal[m];

        int anst = solveDietProblem(n, m, A, b, c, ansx);
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

    private void stressTest() throws IOException {

        //for( int[] subset : listSubsets(28, 16))            System.out.println( Arrays.toString(subset) );



        //File file = new File("src\\diet_tests\\003");
        //br = new BufferedReader(new FileReader( file ));
        //solve();


        try  {
            out = new PrintWriter(System.out);
            File folder = new File("src\\diet_tests\\");

            File[] fileList = folder.listFiles();

            for( File f : fileList){

                if(!f.getName().contains(".") ){

                    System.out.println("============= " + f.getName() + "=================="  );
                    br = new BufferedReader(new FileReader( f ));
                    solve();

                    br = new BufferedReader(new FileReader( f.getPath().concat(".a") ));
                    String line = br.readLine() + br.readLine();
                    System.out.println( f.getName() + " answer ============" + line);

                }
            }
        }catch (AssertionError e){
            e.printStackTrace();
            throw new AssertionError();
        }catch (IOException e){
            e.printStackTrace();

        }finally {
            out.close();
        }


    }

    public static void main(String[] args) throws IOException {
        Diet diet = new Diet();

        diet.stressTest();

        diet.br = new BufferedReader(new InputStreamReader(System.in));
        diet.out = new PrintWriter(System.out);
        diet.solve();
        diet.out.close();

    }

    String nextToken() {
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

    int nextInt() throws IOException {
        return Integer.parseInt(nextToken());
    }
}
