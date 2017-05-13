import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SuffixArrayMatching {
    class fastscanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        fastscanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextint() throws IOException {
            return Integer.parseInt(next());
        }
    }

    public int[] computeSuffixArray(String text) {

        int n=text.length();
        int[] order = sortOrder(text);
        int[] dense_rank = dense_rank(text, order);
        int L=1;


        while(L<n){
            order = my_update_order(L, order, dense_rank);
            dense_rank = my_update_dense_rank(L, order, dense_rank);
            L*=2;

        }

        return order;
    }
    int[] sortOrder(String text){
        int n = text.length();
        char[] ch = text.toCharArray();
        //int[] count = new int[5];
        int[] count = new int[128];
        int[] order = new int[n];


        for(int i=0; i<n; i++){
            //count[ ch2int( ch[i] ) ]++;
            count[ ch[i] ]++;
        }

        for(int i=1; i<count.length; i++){
            count[i] += count[i-1];
        }

        for(int i=n-1; i>=0; i--){
            //count[ ch2int( ch[i] ) ]--;
            //order[ count[ ch2int(ch[i]) ] ] = i;

            count[ ch[i] ]--;
            order[ count[ ch[i] ] ] = i;
        }

        return order;
    }
    int[] dense_rank(String text, int[] order){
        int n = text.length();
        char[] ch = text.toCharArray();
        int[] dense_rank = new int[n];
        dense_rank[ order[0] ] =0;

        for(int i=1; i<n; i++){
            if(ch[ order[i]] != ch[ order[i-1] ]){
                dense_rank[order[i]] = dense_rank[ order[i-1]]+1;
            }else{
                dense_rank[order[i]] = dense_rank[ order[i-1]];
            }
        }
        return dense_rank;

    }
    private int[] my_update_order(int L, int[] order, int[] dense_rank) {
        int n = order.length;
        int[] newOrder = new int[n];
        int[] count = new int[n];

        for(int i=0; i<n; i++){
            count[ dense_rank[i] ] ++;
        }

        for(int i=1; i<n; i++){
            count[i] = count[i] + count[i-1];
        }

        for(int i=n-1; i>=0; i--){
            /*pattern id*/

            int pattern_id = (order[i] - L +n)%n;
            count[ dense_rank[ pattern_id] ]--;
            newOrder[ count[ dense_rank[ pattern_id] ] ] = pattern_id;

        }




        return newOrder;
    }
    private int[] my_update_dense_rank(int L, int[] newOrder, int[] dense_rank) {
        int n = newOrder.length;

        int[] new_dense_rank = new int[n];
        new_dense_rank[ newOrder[0] ] = 0;

        for(int i=1; i<n; i++){
            int left = newOrder[i];
            int right = (left + L)%n;

            int leftPrev = newOrder[i-1];
            int rightPrev = (leftPrev+L)%n;



            if(( dense_rank[left] != dense_rank[leftPrev]  ) || (dense_rank[right] != dense_rank[rightPrev] )){
                new_dense_rank[left] = new_dense_rank[leftPrev] + 1;
            }else{
                new_dense_rank[left] = new_dense_rank[leftPrev];
            }

        }

        return new_dense_rank;
    }

    private void stressTest(){

        String text = "TCCTCTATGAGATCCTATTCTATGAAACCTTCAGACCAAAATTCTCCGGC$";
        int n = text.length();

        int[] suffixArray = computeSuffixArray(text);


        //assert comparePatternTo("ATC", text, 6 ) < 0;
        assert comparePatternTo("ATC", "A$", 0) > 0;
        assert comparePatternTo("ATC", "ATCDE$", 0) == 0;

        List<Integer> list = findOccurrencesLogarithmic("ATC", text, suffixArray);
        for(Integer i : list) System.out.print(i + " ");
        System.out.println();
    }


    public List<Integer> findOccurrencesLinear(String pattern, String text, int[] suffixArray) {
        int n = suffixArray.length;
        int m = pattern.length();
        int start;
        List<Integer> result = new ArrayList<Integer>();

        for(int i=0; i< n; i++){                    /*id of pattern*/
            start = suffixArray[i];

            if( (start+m < n) && pattern.equals( text.substring(start, start+m) ) ){
                result.add( start );
                //System.out.println(text.substring(start, start+read_length));
            }

        }


        return result;
    }

    int comparePatternTo(String pattern, String text, int start){
        int i=0;
        int p_length = pattern.length()-1;
        while(
                (pattern.charAt(i)  ==  text.charAt(start+i))
                &&
                (i < p_length)
             ){
            i++;
        }
        return pattern.charAt(i) -  text.charAt(start+i);
    }



    public List<Integer> findOccurrencesLogarithmic(String pattern, String text, int[] suffixArray) {
        int n = suffixArray.length;
        int m = pattern.length();
        int start, end, minIndex = 0, middleIndex, maxIndex = n;
        List<Integer> result = new ArrayList<Integer>();

        //for(int i=0; i<n; i++)           System.out.println(i + " " + suffixArray[i] + " " + text.substring( suffixArray[i] ) );



        while(minIndex < maxIndex){
            middleIndex = (minIndex + maxIndex)/2;
                /* pattern greater than middleIndex */
//            if ( pattern.compareTo(text.substring( suffixArray[middleIndex] )) > 0){
            if( comparePatternTo( pattern, text, suffixArray[middleIndex] ) > 0 ){
                minIndex = middleIndex+1;
            }else{
                maxIndex = middleIndex;
            }
        }
        start = minIndex;
        maxIndex = n;


        while(minIndex < maxIndex){
            middleIndex = (minIndex+maxIndex)/2;

            /*pattern less than middleIndex */
            int pos = suffixArray[middleIndex];

            //if( pattern.compareTo(text.substring(pos)) < 0 ){
            if( comparePatternTo(pattern, text, suffixArray[middleIndex]) < 0 ){
                maxIndex = middleIndex;

            }else{
                minIndex = middleIndex+1;
            }
        }
        end = maxIndex;

        for(int i=start; i<end; i++){
            result.add( suffixArray[i] );
        }

        return result;
    }


    static public void main(String[] args) throws IOException {
        new SuffixArrayMatching().run();
    }

    public void print(boolean[] x) {
        for (int i = 0; i < x.length; ++i) {
            if (x[i]) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
    }

    public void run() throws IOException {

        //stressTest();

        fastscanner scanner = new fastscanner();
        String text = scanner.next() + "$";
        int[] suffixArray = computeSuffixArray(text);
        int patternCount = scanner.nextint();
        boolean[] occurs = new boolean[text.length()];
        for (int patternIndex = 0; patternIndex < patternCount; ++patternIndex) {
            String pattern = scanner.next();
            List<Integer> occurrences = findOccurrencesLogarithmic(pattern, text, suffixArray);
            for (int x : occurrences) {
                occurs[x] = true;
            }
        }
        print(occurs);
    }
}
