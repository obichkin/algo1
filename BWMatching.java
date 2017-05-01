import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class BWMatching {
    class FastScanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        FastScanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

    // Preprocess the Burrows-Wheeler Transform bwt of some text
    // and compute as a result:
    //   * starts - for each character C in bwt, starts[C] is the first position
    //       of this character in the sorted array of
    //       all characters of the text.
    //   * occ_count_before - for each character C in bwt and each position P in bwt,
    //       occ_count_before[C][P] is the number of occurrences of character C in bwt
    //       from position 0 to position P inclusive.
    private void PreprocessBWT(String bwt, Map<Character, Integer> starts, Map<Character, int[]> occ_counts_before, int[] counts, int[] last2first) {
        // Implement this function yourself
        int n = bwt.length();
        char[] alphabet = new char[]{'$', 'A', 'C', 'G', 'T' };

        for(int i=n-1; i>=0; i--){
            starts.put( bwt.charAt(i) , i);
        }


        for(char ch: starts.keySet()){
            occ_counts_before.put(ch, new int[n+1]);
        }

        for(int i=0; i<n; i++){
            occ_counts_before.get( bwt.charAt(i) )[i+1] = 1;
        }

        for(char ch: starts.keySet()){
            for(int i=1; i<=n; i++){
                occ_counts_before.get( ch )[i] += occ_counts_before.get( ch )[i-1];
            }
        }


        for(char ch: bwt.toCharArray()){
            counts[ch]++;
        }

        for(int i=1; i<counts.length; i++){
            counts[i] += counts[i-1];
        }

        for(int i=n-1; i>=0; i--){
            counts[ bwt.charAt(i) ]--;
            last2first[i] = counts[ bwt.charAt(i) ];
        }




    }

    // Compute the number of occurrences of string pattern in the text
    // given only Burrows-Wheeler Transform bwt of the text and additional
    // information we get from the preprocessing stage - starts and occ_counts_before.
    int CountOccurrences(String pattern, String bwt, Map<Character, Integer> starts, Map<Character, int[]> occ_counts_before, int[] last2first) {
        // Implement this function yourself
        int n = bwt.length();
        int m = pattern.length() - 1;
        int top=0, bottom = n-1;

        char character;





        while(top <= bottom){
            if(m > -1){
                character = pattern.charAt(m);
                m--;

                if(!starts.containsKey(character)){
                    return 0;
                }

                top = last2first[ starts.get(character) ] + occ_counts_before.get(character)[top];
                bottom = last2first[starts.get(character)] + occ_counts_before.get(character)[bottom+1] - 1;

            }else{
                return bottom - top + 1;
            }
        }
        return 0;

    }

    private void stressTest(){
        String bwt = "G$";
        String pattern = "T";
        Map<Character, Integer> starts = new HashMap<Character, Integer>();
        Map<Character, int[]> occ_counts_before = new HashMap<Character, int[]>();
        int[] counts = new int[128];
        int[] last2first = new int[bwt.length()];
        PreprocessBWT(bwt, starts, occ_counts_before, counts, last2first);
        int x = CountOccurrences(pattern, bwt, starts, occ_counts_before, last2first);
        System.out.println(x);

    }



    static public void main(String[] args) throws IOException {
        new BWMatching().run();
    }

    public void print(int[] x) {
        for (int a : x) {
            System.out.print(a + " ");
        }
        System.out.println();
    }



    public void run() throws IOException {

        //stressTest();

        FastScanner scanner = new FastScanner();
        String bwt = scanner.next();
        // Start of each character in the sorted list of characters of bwt,
        // see the description in the comment about function PreprocessBWT
        Map<Character, Integer> starts = new HashMap<Character, Integer>();
        // Occurrence counts for each character and each position in bwt,
        // see the description in the comment about function PreprocessBWT
        Map<Character, int[]> occ_counts_before = new HashMap<Character, int[]>();
        int[] counts = new int[128];
        int[] last2first = new int[bwt.length()];
        // Preprocess the BWT once to get starts and occ_count_before.
        // For each pattern, we will then use these precomputed values and
        // spend only O(|pattern|) to find all occurrences of the pattern
        // in the text instead of O(|pattern| + |text|).
        PreprocessBWT(bwt, starts, occ_counts_before, counts, last2first);

        int patternCount = scanner.nextInt();
        String[] patterns = new String[patternCount];
        int[] result = new int[patternCount];
        for (int i = 0; i < patternCount; ++i) {
            patterns[i] = scanner.next();
            result[i] = CountOccurrences(patterns[i], bwt, starts, occ_counts_before, last2first);
        }
        print(result);
    }
}
