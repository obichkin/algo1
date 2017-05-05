import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by mobichkin on 27.04.17.
 */
public class X174_Error_FreeTest {

    void singleFileTest(String filename) throws FileNotFoundException {
        X174_Error_Free x174 = new X174_Error_Free( new InputStreamReader(new FileInputStream(filename)));

        String s = "AAACCAAGT$";
        int n = s.length();
        Integer suffix_array[] = new Integer[n];

        System.out.println( s + " " + x174.bwt(s, suffix_array) + " " + x174.inverseBWT( x174.bwt(s, suffix_array) ) );

        for(int i=0; i<n; i++){
            System.out.println( s.substring( suffix_array[i] ) );
        }

    }

    void stressBWTTest(String filename) throws FileNotFoundException {

        int MAX_STRING_LENGTH = 9;
        int n;
        int a = 3; //alphabet size
        char ch[];
        char alphabet[] = "ACGT".toCharArray();
        String s, bwt, pattern;


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
            suffix_array = new Integer[n];
            bwt = x174.bwt(s, suffix_array);
            //System.out.println(s + " " + x174.bwt(s) + " " + x174.inverseBWT( x174.bwt(s) ) );
            assert s.equals( x174.inverseBWT( bwt )  );


            n = random.nextInt( MAX_STRING_LENGTH )+1;
            ch = new char[n];

            for(int i=0; i<n-1; i++){
                ch[i] = alphabet[ random.nextInt(a) ];
            }
            ch[n-1] = '$';

            pattern = new String(ch);

            Map<Character, Integer> starts = new HashMap<Character, Integer>();
            Map<Character, int[]> occ_counts_before = new HashMap<Character, int[]>();
            int[] counts = new int[128];
            int[] last2first = new int[s.length()];

            x174.PreprocessBWT( bwt , starts, occ_counts_before, counts, last2first);
            int overlap = x174.countOverlap(pattern, s, starts, occ_counts_before, last2first, counts);

            if(overlap>=0){
                System.out.printf("%s %d %s\n", pattern, overlap, s );
            }

        }


    }


    void BWMatchTest(String filename) throws FileNotFoundException {
        X174_Error_Free x174 = new X174_Error_Free( new InputStreamReader(new FileInputStream(filename)));


        String s = "CCCGGACGC$";
        String pattern = "GAAGG$";
        int n = s.length();
        Integer suffix_array[] = new Integer[n];
        String bwt = x174.bwt(s, suffix_array);



        Map<Character, Integer> starts = new HashMap<Character, Integer>();
        Map<Character, int[]> occ_counts_before = new HashMap<Character, int[]>();
        int[] counts = new int[128];
        int[] last2first = new int[s.length()];

        x174.PreprocessBWT( bwt , starts, occ_counts_before, counts, last2first);
        int x = x174.countOverlap(pattern, s, starts, occ_counts_before, last2first, counts);
        System.out.println(x);


    }

    @Test
    public void stressTest() throws FileNotFoundException {
        String filename = "c:\\temp\\5";

        //singleFileTest(filename);
        stressBWTTest(filename);

        //BWMatchTest(filename);
    }




}
