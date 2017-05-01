import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mobichkin on 17.04.17.
 */
public class SchoolBusTest {


    public void generateRandomFile(String filename) throws FileNotFoundException {

        final int UPPERBOUND = 8;
        final int EDGE_UPPERBOUND = 1000000;

        PrintWriter out = new PrintWriter(filename);
        Random random = new Random();

        int n = random.nextInt(UPPERBOUND)+2;
        int m = n*(n-1)/2;
        int i, j, z;

        out.printf("%d %d\n", n, m);
        for(int k=0; k<m; k++){
            do{
                i = random.nextInt(n) + 1;
                j = random.nextInt(n) + 1;
                z = random.nextInt(EDGE_UPPERBOUND) + 1;
            } while (i == j);

            out.printf("%d %d %d\n", i, j, z );
        }

        out.close();
    }


    public void testSingleFile(String filename) throws IOException {
        SchoolBus bus = new SchoolBus();
        bus.in = new SchoolBus.FastScanner(new BufferedReader(new InputStreamReader(new FileInputStream(filename))));

        int[][] graph = bus.readData();
        bus.bestResult = bus.INF;
        bus.myBestPath = new ArrayList<>();
        int pathLength = -1;

        SchoolBus.Answer myAnswer = bus.myTSP(graph);
        SchoolBus.Answer answer = bus.schoolBus(graph);

        if(myAnswer.weight != -1){
            pathLength = graph[myAnswer.path.get( myAnswer.path.size()-1 ) ][ myAnswer.path.get(0) ] ;
            for(int i=1; i<myAnswer.path.size(); i++){
                pathLength += graph[ myAnswer.path.get(i-1) ][ myAnswer.path.get(i) ];
            }
        }



        try{
            System.out.println(myAnswer.weight + " " + answer.weight);
            assert myAnswer.weight == answer.weight;
            assert pathLength == myAnswer.weight;


        }catch (AssertionError e){
            System.out.println("my answer = " + myAnswer.weight + " ; pathLengh = " + pathLength);
            for(Integer i : myAnswer.path) System.out.print(i + " ");            System.out.println();

            throw new AssertionError(e);
        }


    }

    public void testMyTSP(String filename) throws IOException {
        SchoolBus bus = new SchoolBus();
        bus.in = new SchoolBus.FastScanner(new BufferedReader(new InputStreamReader(new FileInputStream(filename))));

        int[][] graph = bus.readData();
        bus.bestResult = bus.INF;
        bus.myBestPath = new ArrayList<>();

        SchoolBus.Answer myAnswer = bus.myTSP( graph );
    }


    @Test
    public void stressTest() throws IOException {
        String filename = "c:\\temp\\3";


        for(int i=0; i<100000; i++){
            generateRandomFile(filename);
            testSingleFile(filename);
        }
    }


}
