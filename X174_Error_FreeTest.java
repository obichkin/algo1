import org.junit.Test;

import java.io.*;
import java.util.*;

/**
 * Created by mobichkin on 27.04.17.
 */
public class X174_Error_FreeTest {

    void singleFileTest(String filename) throws IOException {
        X174_Error_Free x174 = new X174_Error_Free( new InputStreamReader(new FileInputStream(filename)));
        x174.run();


        System.out.println("constructed Genome: " + x174.constructedGenome);
        System.out.printf("constructed genome length: %d\n", x174.constructedGenome.length() );
        for(int i=0; i<X174_Error_Free.number_of_reads; i++){
            if(!x174.visited[i]){
                System.out.println("not visited : " + i + " " + x174.reads.get(i));
            }

        }


    }

    void stressGraphTest(String filename) throws IOException {
        PrintWriter out;

        Random random = new Random();

        char genom[] = new char[X174_Error_Free.genom_length];
        char read[] = new char[X174_Error_Free.read_length];
        char alphabet[] = "ACGT".toCharArray();


        int total_not_contains = 0;

        for(int z=0; z<1; z++){
            for(int i=0; i<X174_Error_Free.genom_length; i++){
                genom[i] = alphabet[ random.nextInt(4) ];
            }

            out = new PrintWriter(new FileOutputStream(filename));
            for(int m=0; m<X174_Error_Free.number_of_reads; m++){
                int start = random.nextInt(X174_Error_Free.genom_length);

                for(int j=0; j<X174_Error_Free.read_length; j++){
                    read[j] =  genom[ (start + j)%X174_Error_Free.genom_length ];
                }
                out.println(new String(read));

            }
            out.flush();
            out.close();

            X174_Error_Free x174 = new X174_Error_Free( new InputStreamReader(new FileInputStream(filename)));
            x174.run();
            //x174.graph.printGraph(x174.reads);

            String doubleGenom = x174.constructedGenome.concat(x174.constructedGenome);


            //for(int i=0; i<X174_Error_Free.number_of_reads; i++){
//                if(!x174.visited[i]){
//                    System.out.println("not visited : " + x174.reads.get(i));
//                }
//            }


            int contains=0, not_contains=0;
            for(String s: x174.reads){
                if( doubleGenom.contains(s) ){
                    contains++;
                }else{
                    not_contains++;
                }

            }

            System.out.println("initial Genome: " + new String(genom) );
            System.out.println("constructed Genome: " + x174.constructedGenome);
            System.out.printf("constructed genome length: %d\n", x174.constructedGenome.length() );
            System.out.printf("contains %d ; not contains %d\n", contains, not_contains);
            total_not_contains+=not_contains;

        }



    }

    void stressBWTTest(String filename) throws FileNotFoundException {

        int MAX_STRING_LENGTH = 10000;
        int n;
        int a = 3; //alphabet size
        char ch[];
        char alphabet[] = "ACGT".toCharArray();
        String s;


        Random random = new Random();
        X174_Error_Free x174 = new X174_Error_Free( new InputStreamReader(new FileInputStream(filename)));
        Integer suffix_array[] = new Integer[0];

        while(true){
            n = random.nextInt( MAX_STRING_LENGTH )+1;
            ch = new char[n];

            for(int i=0; i<n-1; i++){
                ch[i] = alphabet[ random.nextInt(a) ];
            }
            ch[n-1] = '$';

            s = new String(ch);
            //System.out.println(s + " " + x174.bwt(s) + " " + x174.inverseBWT( x174.bwt(s) ) );
            assert s.equals( x174.inverseBWT( x174.bwt(s, suffix_array) )  );
        }


    }


    void BWMatchTest(String filename) throws FileNotFoundException {
        X174_Error_Free x174 = new X174_Error_Free( new InputStreamReader(new FileInputStream(filename)));

        String s = "ATTCTTGTT$";
        String pattern = "TT$";
        int n = s.length();
        Integer suffix_array[] = new Integer[n];
        String bwt = x174.bwt(s, suffix_array);



        Map<Character, Integer> starts = new HashMap<Character, Integer>();
        Map<Character, int[]> occ_counts_before = new HashMap<Character, int[]>();
        int[] counts = new int[128];
        int[] last2first = new int[s.length()];

        x174.PreprocessBWT( bwt , starts, occ_counts_before, counts, last2first);
        int x = x174.CountOccurrences(pattern, bwt, starts, occ_counts_before, last2first);
        System.out.println(x);


    }

    @Test
    public void stressTest() throws IOException {
        String filename = "c:\\temp\\5";

        //singleFileTest(filename);
        //stressBWTTest(filename);
        //BWMatchTest(filename);

        stressGraphTest(filename);

    }




}

