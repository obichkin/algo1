import java.io.IOException;
import java.util.Scanner;

class Equation {
    Equation(double a[][], double b[]) {
        this.a = a;
        this.b = b;
    }

    double a[][];
    double b[];
}

class Position {
    Position(int column, int row) {
        this.column = column;
        this.row = row;
    }

    int column;
    int row;
}

class EnergyValues {

    static double bigNumber = 1000000000;

    static Equation ReadEquation() throws IOException {
        Scanner scanner = new Scanner(System.in);
        int size = scanner.nextInt();

        double a[][] = new double[size][size];
        double b[] = new double[size];
        for (int row = 0; row < size; ++row) {
            for (int column = 0; column < size; ++column)
                a[row][column] = scanner.nextInt();
            b[row] = scanner.nextInt();
        }
        return new Equation(a, b);
    }

    static Position SelectPivotElement(double a[][], boolean used_raws[], boolean used_columns[]) {
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

        if( a[pivot.row][pivot.column] == 0) return;

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

    static double[] SolveEquation(Equation equation) {
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

            if((a[i][i]==0)&& (b[i] != 0)){
                    return null;
            }

        }
        return b;
    }

    private static void stressTest(){

        int size = 3;

        double a[][] = new double[size][size];
        double b[] = new double[size];
/*
        a[0] = new double[]{1, 1, -1 };
        a[1] = new double[]{0, 1, 3};
        a[2] = new double[]{-1, 0, -2};

        b = new double[]{9, 3, 2};
*/

        a[0] = new double[]{1, 2, 3 };
        a[1] = new double[]{2, 4, 7};
        a[2] = new double[]{3, 6, 11};

        b = new double[]{6, 13, 25};



        Equation equation = new Equation(a, b);
        double[] solution = SolveEquation(equation);
        PrintColumn(solution);


    }

    static void PrintColumn(double column[]) {
        if( column != null){
            int size = column.length;
            for (int raw = 0; raw < size; ++raw)
                System.out.printf("%.20f\n", column[raw]);
        }
    }

    public static void main(String[] args) throws IOException {

        stressTest();

        Equation equation = ReadEquation();
        double[] solution = SolveEquation(equation);
        PrintColumn(solution);
    }
}
