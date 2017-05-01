import java.util.*;
import java.io.*;

public class SuffixArrayLong {
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

    public class Suffix implements Comparable {
        String suffix;
        int start;

        Suffix(String suffix, int start) {
            this.suffix = suffix;
            this.start = start;
        }

        @Override
        public int compareTo(Object o) {
            Suffix other = (Suffix) o;
            return suffix.compareTo(other.suffix);
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

        //for(int i=0; i< n; i++)            System.out.println(text.substring(order[i]));

        /*
        while (L<n){

            order = sortDoubled(L, order, dense_rank);
            dense_rank = update_dense_rank(order, dense_rank, L);
            L = L*2;
        }
        */
        // write your code here

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



    int[] update_dense_rank(int[] newOrder, int[] dense_rank, int L){
        int n = newOrder.length;
        int[] new_dense_rank = new int[n];
        new_dense_rank[ newOrder[0] ] = 0;
        int left, left_prev, right, right_prev;

        for(int i=1; i<n; i++){
            left = newOrder[i]; left_prev = newOrder[i-1];
            right = (left+L)%n; right_prev = (left_prev+L)%n;

            if( dense_rank[left]!=dense_rank[left_prev] || dense_rank[right]!=dense_rank[right_prev] ){
                new_dense_rank[left] = new_dense_rank[left_prev]+1;
            }else{
                new_dense_rank[left] = new_dense_rank[left_prev];
            }

        }

        return new_dense_rank;
    }
    int[] sortDoubled(int L, int[] order, int[] dense_rank){
        int n = order.length;
        int[] count = new int[n];
        int[] newOrder = new int[n];

        for(int i=0; i<n; i++){
            count[ dense_rank[i] ]++;
        }

        for(int j=1; j<n; j++){
            count[j] += count[j-1];
        }

        for(int i=n-1; i>=0; i--){

            int start = ( order[i] - L + n ) % n;
            int cl = dense_rank[start];
            count[cl]--;
            newOrder[count[cl]] = start;
        }

        return newOrder;
    }

    private String countingSort(String text){
        int n = text.length();
        char[] ch = text.toCharArray();
        char[] sorted = new char[n];
        int[] count = new int[5];
        int i=0,j=0;

        for(char c : ch){
            count[ ch2int( c ) ]++;
        }

        for(int k=1; k<count.length; k++){
            count[k] += count[k-1];
        }


        while(i<n){
            while (i < count[j]){
                sorted[i] = int2ch(j);
                i++;
            }
            j++;
        }

        return new String(sorted);
    }
    private String bubbleSort(String text){
        int n = text.length();
        char[] ch = text.toCharArray();
        char temp;
        boolean inversed=true;


        for(int i=0; i<n && inversed==true; i++){
            inversed=false;
            for(int j=0; j<n-i-1; j++){
                if( ch[j] > ch[j+1] ){
                    temp = ch[j];
                    ch[j]=ch[j+1];
                    ch[j+1]=temp;
                    inversed=true;
                }
            }
        }

        return new String(ch);
    }
    int ch2int(char letter) {
        switch (letter)
        {
            case '$': return 0;
            case 'A': return 1;
            case 'C': return 2;
            case 'G': return 3;
            case 'T': return 4;
            default: return -1;
        }
    }
    char int2ch(int i){
        switch(i){
            case 0: return '$';
            case 1: return 'A';
            case 2: return 'C';
            case 3: return 'G';
            case 4: return 'T';
            default: return '#';

        }
    }


    // Build suffix array of the string text and
    // return an int[] result of the same length as the text
    // such that the value result[i] is the index (0-based)
    // in text where the i-th lexicographically smallest
    // suffix of text starts.




    private void stressTest(){

        String text = "ACATA$";
        int n=text.length();
        int[] order = sortOrder(text);
        int[] dense_rank = dense_rank(text, order);
        int L=1;


        for(int i=0; i< n; i++){                    /*id of pattern*/
            System.out.print(order[i] + " " + dense_rank[ order[i] ] + " " + text.substring( order[i] ) + text.substring(0, order[i]) );
            System.out.println();
        }


        int count[] = new int[n];

        for(int i=0; i<n; i++){
            count[ dense_rank[i] ] ++;
        }

        for(int i=1; i<n; i++){
            count[i] = count[i] + count[i-1];
        }



        System.out.println("=========-sorted by 2nd-------------===========------------============");
        System.out.println("pattern_id \t dense_rank \t count");
        for(int i=0; i<n; i++){
            int pattern_id = (order[i] - L+n)%n;
            System.out.print( pattern_id + " " + dense_rank[ pattern_id] + " " + count[ dense_rank[ pattern_id] ] + " " + text.substring( pattern_id  ) + text.substring(0, (pattern_id ) ));
            System.out.println();

        }


        int newOrder[] = new int[n];
        for(int i=n-1; i>=0; i--){
            /*pattern id*/

            int pattern_id = (order[i] - L +n)%n;
            count[ dense_rank[ pattern_id] ]--;
            newOrder[ count[ dense_rank[ pattern_id] ] ] = pattern_id;

        }




        System.out.println("=========-sorted by 1-2-------------===========------------============");
        System.out.println("pattern_id \t dense_rank ");
        for(int i=0; i< n; i++){                    /*id of pattern*/
            System.out.print(newOrder[i] + " " + dense_rank[i] + " " + text.substring( newOrder[i] ) + text.substring(0, newOrder[i]) );
            System.out.println();
        }

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


        System.out.println("=========-sorted by 1-2-------------===========------------============");
        for(int i=0; i< n; i++){                    /*id of pattern*/
            System.out.print(newOrder[i] + " " + new_dense_rank[ newOrder[i] ] + " " + text.substring( newOrder[i] ) + text.substring(0, newOrder[i]) );
            System.out.println();
        }



        while (L < n){
            order = sortDoubled(L, order, dense_rank);
            dense_rank = update_dense_rank(order, dense_rank, L);
            L = L*2;
        }



        /*
        int STRING_SIZE = 100000;
        Random random = new Random();

         while(false){
            char ch[] = new char[ STRING_SIZE];
            for(int i=0; i<STRING_SIZE; i++){
                ch[i] = int2ch( random.nextInt(5) );
            }

            String text =  new String(ch) ;


            String s = countingSort(text);
            String s1 = bubbleSort(text);

            System.out.println(s);
            System.out.println(s1);
            assert s.equals(s1);
        } */
    }

    static public void main(String[] args) throws IOException {
        new SuffixArrayLong().run();
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
        String text = scanner.next();
        int[] suffix_array = computeSuffixArray(text);
        print(suffix_array);
    }
}
