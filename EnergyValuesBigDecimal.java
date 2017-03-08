import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

class Position1 {
    Position1(int column, int row) {
        this.column = column;
        this.row = row;
    }

    int column;
    int row;
}
class EquationBigDecimal {

    EquationBigDecimal(BigDecimal a[][], BigDecimal b[]) {
        this.a = a;
        this.b = b;
    }

    BigDecimal a[][];
    BigDecimal b[];

}


class EnergyValuesBigDecimal {



    static double bigNumber = 1000000000;

    static EquationBigDecimal ReadEquation() throws IOException {
        Scanner scanner = new Scanner(System.in);
        int size = scanner.nextInt();

        //double a[][] = new double[size][size];
        BigDecimal a[][] = new BigDecimal[size][size];
        //double b[] = new double[size];
        BigDecimal b[] = new BigDecimal[size];
        for (int row = 0; row < size; ++row) {
            for (int column = 0; column < size; ++column)
                //a[row][column] = scanner.nextInt();
                a[row][column] = BigDecimal.valueOf( scanner.nextInt() );
            //b[row] = scanner.nextInt();
            b[row] = BigDecimal.valueOf(scanner.nextInt());
        }
        return new EquationBigDecimal(a, b);
    }

    static Position1 SelectPivotElement(BigDecimal a[][], boolean used_raws[], boolean used_columns[]) {
        // This algorithm selects the first free element.
        // You'll need to improve it to pass the problem.
        Position1 pivot_element = new Position1(0, 0);
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

    static void SwapLines(BigDecimal a[][], BigDecimal b[], boolean used_rows[], Position1 pivot_element) {
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

    static void ProcessPivotElement(BigDecimal a[][], BigDecimal b[], Position1 pivot) {

        //if( a[pivot.row][pivot.column] == 0) return;
        if( a[pivot.row][pivot.column].equals(BigDecimal.ZERO)) return;

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
                    BigDecimal temp = a[pivot.row][j].multiply( coeff );
                    a[i][j] = a[i][j].subtract( temp );
                }

            }

            if(!b[i].equals(BigDecimal.ZERO) && !b[pivot.row].equals(BigDecimal.ZERO)){
                b[i] = b[i].subtract( b[pivot.row].multiply( coeff ) );
            }

        }


    }

    static void MarkPivotElementUsed(Position1 pivot_element, boolean used_rows[], boolean used_columns[]) {
        used_rows[pivot_element.row] = true;
        used_columns[pivot_element.column] = true;
    }

    static BigDecimal[] SolveEquation(EquationBigDecimal equation) {

        //double a[][] = equation.a;
        //double b[] = equation.b;

        BigDecimal a[][] = equation.a;
        BigDecimal b[] = equation.b;
        int size = a.length;

        boolean[] used_columns = new boolean[size];
        boolean[] used_rows = new boolean[size];

        for (int i = 0; i < size; ++i) {
            Position1 pivot_element = SelectPivotElement(a, used_rows, used_columns);
            SwapLines(a, b, used_rows, pivot_element);
            ProcessPivotElement(a, b, pivot_element);
            MarkPivotElementUsed(pivot_element, used_rows, used_columns);
        }

        for (int i=size-1; i>=0; i--){
            for (int j=i+1; j<size; j++){

                if(!a[i][j].equals(BigDecimal.ZERO)){
                    b[i] = b[i].subtract( a[i][j].multiply( b[j] ));
                }


                //a[i][j] = 0;
                a[i][j] = BigDecimal.ZERO;
            }

            //if((a[i][i]==0)&& (b[i] != 0)){
            if( a[i][i].equals(BigDecimal.ZERO) && !b[i].equals(BigDecimal.ZERO)){
                return null;
            }

        }

        return b;
    }

    private static void stressTest(){

        int size = 3;

        double a0[][] = new double[size][size];
        double b0[];

        a0[0] = new double[]{ 0,  0, 1 };
        a0[1] = new double[]{ -1, 0, 0 };
        a0[2] = new double[]{ 1, 1, 1 };

        b0 = new double[]{3, 0, bigNumber};

        BigDecimal[][] a = new BigDecimal[size][size];
        BigDecimal[] b = new BigDecimal[size];

        for(int i=0; i<size; i++){

            if (b0[i] == 0)
                b[i] = BigDecimal.ZERO;
            else
                b[i] = BigDecimal.valueOf(b0[i]);

            for(int j=0; j<size; j++){
                if(a0[i][j] == 0)
                    a[i][j] = BigDecimal.ZERO;
                else
                    a[i][j] = BigDecimal.valueOf(a0[i][j]);
            }
        }

/*
        a[0] = new double[]{1, 2, 3 };
        a[1] = new double[]{2, 4, 7};
        a[2] = new double[]{3, 6, 14};

        b = new double[]{6, 13, 23};
*/

        EquationBigDecimal equation = new EquationBigDecimal(a, b);
        BigDecimal[] solution = SolveEquation(equation);
        PrintColumn(solution);


    }

    static void PrintColumn(BigDecimal column[]) {
        if( column != null){
            int size = column.length;
            for (int raw = 0; raw < size; ++raw)
                System.out.printf("%.20f\n", column[raw]);
        }
    }

    public static void main(String[] args) throws IOException {

        stressTest();

        EquationBigDecimal equation = ReadEquation();
        BigDecimal[] solution = SolveEquation(equation);
        PrintColumn(solution);
    }
}
