import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class InverseBWT {
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


    int ch2num(char ch){
        switch (ch){
            case 'A' : return 0;
            case 'C' : return 1;
            case 'G' : return 2;
            case 'T' : return 3;

        }
        return -1;
    }

    int findPosition(char ch, int bwtIndex, ArrayList[] positions){
        return (int) positions[ ch2num(ch) ].get(bwtIndex);
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

    static public void main(String[] args) throws IOException {
        new InverseBWT().run();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String bwt = scanner.next();
        System.out.println(inverseBWT(bwt));
    }
}
