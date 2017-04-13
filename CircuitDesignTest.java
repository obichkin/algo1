import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by mobichkin on 13.04.17.
 */
public class CircuitDesignTest {


    public void testFile(String filename) throws FileNotFoundException {
        CircuitDesign.InputReader reader = new CircuitDesign.InputReader( new FileInputStream(filename) );
        CircuitDesign.OutputWriter writer = new CircuitDesign.OutputWriter( System.out );
        CircuitDesign design = new CircuitDesign(reader, writer);
        boolean sat, naiveSat;
        design.run();

        sat = design.twoSat.isSatisfiable(design.result);
        System.out.println(sat + " : " + Arrays.toString(design.result));

        naiveSat = design.twoSat.isSatisfiableNaive(design.resultNaive);

        System.out.println(sat + " " + naiveSat);
        assert sat == naiveSat;

    }

    void generateRandomFile(String filename) {

        PrintWriter out = null;

        try{

            out = new PrintWriter(filename);
            final int UPPERBOUND = 24;
            Random random = new Random();

            int numVars = random.nextInt(UPPERBOUND)+1;
            int numClauses = random.nextInt(UPPERBOUND)+1;
            int x, y;


            out.println(numVars + " " + numClauses);

            for(int i=0; i<numClauses; i++){
                x = random.nextInt(numVars) + 1;
                x = random.nextBoolean() ? x : -x;

                y = random.nextInt(numVars) + 1;
                y = random.nextBoolean() ? y : -y;

                out.println(x + " " + y);
            }

        }catch(FileNotFoundException fe){
            System.err.println(filename + " not found!");
            fe.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            out.close();
        }

    }

    @Test
    public void stressTest() throws FileNotFoundException {
        String filename = "c:\\temp\\2";


        for(int i=0; i<1000000; i++){
            generateRandomFile(filename);
            testFile(filename);

        }

    }



    @Test
    public void singleFileTest(){
        String filename = "c:\\temp\\2";
        try {
            testFile(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


}
