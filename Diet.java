import java.io.*;
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
        Equation(double a[][], double b[]) {
            this.a = a;
            this.b = b;
        }

        double a[][];
        double b[];
    }


    double bigNumber = 1000000000;
    BufferedReader br;
    PrintWriter out;
    StringTokenizer st;
    boolean eof;

    int solveDietProblem(int n, int m, double A[][], double[] b, double[] c, double[] x) {

        double[][] a_full = new double[ n + m + 1 ][m];
        double[] b_full = new double[ n + m + 1 ];
        double[][] a_current = new double[m][m];
        double[] b_current = new double[m];
        double[] x_current = new double[m];
        double result_current;
        double result = Double.MIN_VALUE;

        for(int i=0; i<n; i++){
            a_full[i] = A[i];
            b_full[i] = b[i];
        }

        for(int i=0; i<m; i++ ){
            a_full[n+i][i] = -1;
            b_full[n+i] = 0;
        }

        Arrays.fill(a_full[n+m], 1);
        b_full[n+m] = bigNumber;



        // loop for arrays combination
        for(int[] subset : listSubsets(a_full.length,m)){
            for(int j=0; j<m; j++ ){
                a_current[j] = Arrays.copyOf(a_full[ subset[j] ], m);
                b_current[j] = b_full[ subset[j] ];
            }

            x_current = SolveEquation(new Equation(a_current, b_current ));


            //verify the solution x_current
            if(verify(x_current, a_full, b_full )){
                System.out.println(" Verified !!!");
                result_current = 0;
                for(int j=0; j<m; j++){
                    result_current += x[j];
                }
                if( result_current >= bigNumber - 0.0001) {    // if unbounded solution
                    return 1;
                }


                result_current = 0;
                for(int j=0; j<m; j++){
                    result_current += c[j]*x[j];
                }

                if( result_current > result ){
                    result = result_current;
                    x = Arrays.copyOf(x_current, m);
                }

            }
        }



        if(result == Double.MIN_VALUE){
            return -1;
        } else {
            return 0;
        }



    }

    List<int[]> listSubsets(int n, int m){
        List<int[]> result = new ArrayList<>();

        for( int i=0; i<(1<<n); i++){

            if( Integer.bitCount(i) == m){
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

    boolean verify(double[] x_current, double[][] a_full, double[] b_full ){
        double result;
        for(int i=0; i<a_full.length; i++){
            result = 0;
            for(int j=0; j<x_current.length; j++){
                 result += a_full[i][j] * x_current[j];
            }
            if( result > b_full[i] - 0.0001){
                return false;
            }

        }
        return true;
    }

    Position SelectPivotElement(double a[][], boolean used_raws[], boolean used_columns[]) {
        // This algorithm selects the first free element.
        // You'll need to improve it to pass the problem.
        Position pivot_element = new Position(0, 0);
        while (used_raws[pivot_element.row])
            ++pivot_element.row;
        while (used_columns[pivot_element.column])
            ++pivot_element.column;

        while (( a[pivot_element.row][pivot_element.column] == 0 )
                && (pivot_element.row < a.length -1 ))
            ++pivot_element.row;

        return pivot_element;
    }

    static void SwapLines(double a[][], double b[], boolean used_rows[], Position pivot_element) {
        int size = a.length;

        for (int column = 0; column < size; ++column) {
            double tmpa = a[pivot_element.column][column];
            a[pivot_element.column][column] = a[pivot_element.row][column];
            a[pivot_element.row][column] = tmpa;
        }

        double tmpb = b[pivot_element.column];
        b[pivot_element.column] = b[pivot_element.row];
        b[pivot_element.row] = tmpb;

        boolean tmpu = used_rows[pivot_element.column];
        used_rows[pivot_element.column] = used_rows[pivot_element.row];
        used_rows[pivot_element.row] = tmpu;

        pivot_element.row = pivot_element.column;
    }

    static void ProcessPivotElement(double a[][], double b[], Position pivot) {

        double coeff = 1 / a[pivot.row][pivot.column];
        for(int j=pivot.column; j<a[0].length; j++){
            a[pivot.row][j] *= coeff;
        }
        b[pivot.row] *= coeff;

        for(int i=pivot.row+1; i<a.length; i++){
            coeff = a[i][pivot.column]  / a[pivot.row][pivot.column];
            for(int j = pivot.column; j<a[0].length; j++ ){
                a[i][j] -= a[pivot.row][j] * coeff;
            }
            b[i] -= b[pivot.row] * coeff;
        }


    }

    static void MarkPivotElementUsed(Position pivot_element, boolean used_rows[], boolean used_columns[]) {
        used_rows[pivot_element.row] = true;
        used_columns[pivot_element.column] = true;
    }

    double[] SolveEquation(Equation equation) {
        double a[][] = equation.a;
        double b[] = equation.b;
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

                b[i] -= a[i][j] * b[j];
                a[i][j] = 0;

            }


        }

        return b;
    }

    void solve() throws IOException {
        int n = nextInt();
        int m = nextInt();
        double[][] A = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                A[i][j] = nextInt();
            }
        }
        double[] b = new double[n];
        for (int i = 0; i < n; i++) {
            b[i] = nextInt();
        }
        double[] c = new double[m];
        for (int i = 0; i < m; i++) {
            c[i] = nextInt();
        }
        double[] ansx = new double[m];
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

    private void stressTest(){
        //for(int[] subset : listSubsets(5, 2 ))   System.out.println( Arrays.toString(subset));

        try  {
            out = new PrintWriter(System.out);
            File folder = new File("src\\diet_tests\\");

            File[] fileList = folder.listFiles();

            for( File f : fileList){

                if(!f.getName().contains(".") ){

                    //System.out.println("============= " + f.getName() + "=================="  );
                    br = new BufferedReader(new FileReader( f ));
                    solve();

                    br = new BufferedReader(new FileReader( f.getPath().concat(".a") ));
                    String line = br.readLine();
                    //System.out.println( f.getName() + " answer ============" + line);

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
