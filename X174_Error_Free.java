import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by mobichkin on 27.04.17.
 */
public class X174_Error_Free {
    BufferedReader reader;

    public static void main(String[] args) throws IOException {
        new X174_Error_Free( new InputStreamReader(System.in ) ).run();
    }


    //input text already had last character $.
    public String bwt(String text, Integer suffix_array[]){
        int n = text.length();
        char txt[] = text.toCharArray();
        //suffix_array = new Integer[n];
        for(int i=0; i<n; i++){
            suffix_array[i] = i;
        }

        Arrays.sort(suffix_array, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                while(txt[o1%n]==txt[o2%n]){
                    o1++;
                    o2++;
                }
                return txt[o1%n] - txt[o2%n];
            }
        });

        char bwt[] = new char[n];

        for(int i=0; i<n; i++){
            bwt[i] = txt[ (suffix_array[i]+n-1)%n ];
        }


        return new String(bwt);
    }

    void fillPositions(char[] array, ArrayList[] positions){
        for(int i=0; i<array.length; i++){
            switch (array[i]){
                case 'A' :
                    positions[0].add(i);
                    break;
                case 'C' :
                    positions[1].add(i);
                    break;
                case 'G' :
                    positions[2].add(i);
                    break;
                case 'T' :
                    positions[3].add(i);
                    break;
            }
        }
    }
    void fillBwtIndex(char[] bwtarray, int[] bwtIndex){

        int A=0,C=0,G=0,T=0;

        for(int i=0; i<bwtarray.length; i++){
            switch (bwtarray[i]){
                case 'A' :
                    bwtIndex[i] = A;
                    A++;
                    break;
                case 'C' :
                    bwtIndex[i] = C;
                    C++;
                    break;
                case 'G' :
                    bwtIndex[i] = G;
                    G++;
                    break;
                case 'T' :
                    bwtIndex[i] = T;
                    T++;
                    break;
            }


        }
    }
    int findPosition(char ch, int bwtIndex, ArrayList[] positions){
        return (int) positions[ ch2num(ch) ].get(bwtIndex);
    }
    int ch2num(char ch){
        switch (ch){
            case 'A' : return 0;
            case 'C' : return 1;
            case 'G' : return 2;
            case 'T' : return 3;

        }
        return -1;
    }


    // Preprocess the Burrows-Wheeler Transform bwt of some text
    // and compute as a result:
    //   * starts - for each character C in bwt, starts[C] is the first position
    //       of this character in the sorted array of
    //       all characters of the text.
    //   * occ_count_before - for each character C in bwt and each position P in bwt,
    //       occ_count_before[C][P] is the number of occurrences of character C in bwt
    //       from position 0 to position P inclusive.
    public void PreprocessBWT(String bwt, Map<Character, Integer> starts, Map<Character, int[]> occ_counts_before, int[] counts, int[] last2first) {
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
    public int CountOccurrences(String pattern, String bwt, Map<Character, Integer> starts, Map<Character, int[]> occ_counts_before, int[] last2first) {
        // Implement this function yourself
        int n = bwt.length();
        int m = pattern.length() - 2; //last character $ in the pattern doesn't count
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

    String inverseBWT(String bwt) {
        StringBuilder result = new StringBuilder();
        int n = bwt.length();
        int[] bwtIndex = new int[n];

        char[] bwtarray = bwt.toCharArray();
        char[] array = bwtarray.clone();
        Arrays.sort(array);

        ArrayList<Integer>[] positions = new ArrayList[4];
        for(int i=0; i<4; i++) positions[i] = new ArrayList<Integer>();


        fillBwtIndex(bwtarray ,bwtIndex);
        fillPositions(array, positions);


        int i=0;
        char ch;
        result.append('$');

        while((ch = bwtarray[i]) != '$'){
            result.append( ch );
            i = findPosition(ch, bwtIndex[i], positions);

        }



        return result.reverse().toString();
    }

    void run() throws IOException {
        int n = 5;  //number of strings
        int m = 3;  //sting length

        List<String> list = new ArrayList<>();

        for(int i=0; i<n; i++){
            list.add(reader.readLine());
        }



    }

    public X174_Error_Free(InputStreamReader reader) {
        this.reader = new BufferedReader(reader);
    }
}
