import org.junit.Test;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mobichkin on 11.04.17.
 */
public class CleaningApartmentTest {

    class NullOutputStream extends OutputStream {
        @Override
        public void write(int b){
        }
    }

    public static int[] convertIntegers(List<Integer> integers)    {
        int[] ret = new int[integers.size()-1];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }

    @Test
    public void testCase3() throws FileNotFoundException, TimeoutException, ContradictionException {

        CleaningApartment.InputReader reader = new CleaningApartment.InputReader(new FileInputStream("src\\apartment_tests\\3" ));
        //CleaningApartment.OutputWriter writer = new CleaningApartment.OutputWriter( new NullOutputStream() );
        CleaningApartment.OutputWriter writer = new CleaningApartment.OutputWriter( System.out );

        CleaningApartment apartment = new CleaningApartment(reader, writer);
        apartment.run();
        //writer.writer.flush();


        ISolver solver = SolverFactory.newDefault();
        solver.setTimeout(3600); // 1 hour timeout
        solver.newVar(apartment.converter.numVertices * apartment.converter.numVertices);
        solver.setExpectedNumberOfClauses( apartment.converter.list.size());

        for(int i=0; i<apartment.converter.list.size(); i++){

            int[] clause =  convertIntegers(apartment.converter.list.get(i));
            solver.addClause(
                    new VecInt(
                           clause
                    )

            );
        }


        IProblem problem = solver;

        if (problem.isSatisfiable()) {
            System.out.println("\nSatisfiable !");
        } else {
            System.out.println("\nUnsatisfiable !");
        }

    }

    @Test
    public void testCaseEmpty() throws FileNotFoundException, TimeoutException, ContradictionException{
        try{

            CleaningApartment.InputReader reader = new CleaningApartment.InputReader(new FileInputStream("src\\apartment_tests\\0" ));
            //CleaningApartment.OutputWriter writer = new CleaningApartment.OutputWriter( new NullOutputStream() );
            CleaningApartment.OutputWriter writer = new CleaningApartment.OutputWriter( System.out );

            CleaningApartment apartment = new CleaningApartment(reader, writer);
            apartment.run();
            writer.writer.flush();


            ISolver solver = SolverFactory.newDefault();
            solver.setTimeout(3600); // 1 hour timeout
            solver.newVar(apartment.converter.numVertices * apartment.converter.numVertices);
            solver.setExpectedNumberOfClauses( apartment.converter.list.size());

            for(int i=0; i<apartment.converter.list.size(); i++){

                int[] clause =  convertIntegers(apartment.converter.list.get(i));

                solver.addClause(
                        new VecInt(
                                clause
                        )

                );
            }


            IProblem problem = solver;

            if (problem.isSatisfiable()) {
                System.out.println("Satisfiable !");
            } else {
                System.out.println("Unsatisfiable !");
            }


        } catch (ContradictionException e) {
            System.out.println("Unsatisfiable (trivial)!");
        } catch (TimeoutException e) {
            System.out.println("Timeout, sorry!");
        } catch (Exception e){
                e.printStackTrace();
        }


    }

}
