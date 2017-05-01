import java.io.*;
import java.util.*;

public class CleaningApartment {
    private final InputReader reader;
    private final OutputWriter writer;
    ConvertHampathToSat converter;

    public CleaningApartment(InputReader reader, OutputWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static void main(String[] args) {
        InputReader reader = new InputReader(System.in);
        OutputWriter writer = new OutputWriter(System.out);
        new CleaningApartment(reader, writer).run();
        writer.writer.flush();
    }

    class Edge {
        int from;
        int to;
    }

    class Pair {
        int x;
        int y;

        void add(int i){
            if(x==0){
                x=i;
            }else {
                y=i;
            }

        }
    }

    class ConvertHampathToSat {
        int numVertices;
        Edge[] edges;
        Set<Integer> set = new HashSet<Integer>();
        List<List<Integer>> list = new ArrayList<>();
        ArrayList<Integer> arraylist;
        Integer[] array;
        ArrayList<Pair> pairs;

        Map<Integer, Set<Integer>> map;


        int x(int i, int j){  //vertex i         position j
            return i + numVertices*j;
        }

        ConvertHampathToSat(int n, int m) {
            numVertices = n;
            edges = new Edge[m];
            for (int i = 0; i < m; ++i) {
                edges[i] = new Edge();
            }

            map = new HashMap<>(numVertices);
            for(int i=1; i<=numVertices; i++){
                map.put(i, new HashSet<Integer>());
            }
        }

        void subset(int[] A, int k, int start, int currLen, boolean[] used) {

            if (currLen == k) {
                Pair pair = new Pair();
                for (int i = 0; i < A.length; i++) {

                    if (used[i] == true) {
                        pair.add(A[i]);
                        //System.out.print(A[i] + " ");
                    }

                }
                pairs.add(pair);
                //System.out.println();
                return;
            }
            if (start == A.length) {
                return;
            }
            // For every index we have two options,
            // 1.. Either we select it, means put true in used[] and make currLen+1
            used[start] = true;
            subset(A, k, start + 1, currLen + 1, used);
            // 2.. OR we dont select it, means put false in used[] and dont increase
            // currLen
            used[start] = false;
            subset(A, k, start + 1, currLen, used);
        }

        void fillPairs(){
            pairs = new ArrayList<Pair>();

            int A[] = new int[numVertices];
            for(int i=0; i< numVertices; i++){
                A[i] = i;
            }

            boolean[] B = new boolean[numVertices];
            subset(A, 2, 0, 0, B);
        }

        void addVertex(int i){
            if(!set.contains(i)){
                set.add(i);

                // all vertices must be on the path,          // (A|B|C)
                arraylist = new ArrayList<>();
                for(int j=0; j<numVertices; j++){
                    arraylist.add(x(i, j));
                }
                arraylist.add(0);
                list.add(arraylist);








            }
        }

        void calculateFormula() {
            //x(i,j)   vertex i  ;  position in the path j
            fillPairs();

            for(Edge e: edges){
                addVertex(e.from);
                addVertex(e.to);

                map.get(e.from).add(e.to);
                map.get(e.to).add(e.from);
            }

            // each vertex must be visited exactly once,
            // (^A|^B) (^B|^C) (^A|^C)       list the subsets of size2
            for(int i=1; i<=numVertices; i++){
                for(Pair pair: pairs){
                    array = new Integer[]{ -x(i, pair.x), -x(i, pair.y), 0 };
                    list.add( Arrays.asList(array) );
                }
            }





            //there is only one vertex on each position in the path,
            //position p
            for(int p=0; p<numVertices; p++){
                //subsets2 of vertices 1..n
                for(Pair pair: pairs){
                    array = new Integer[]{ -x(pair.x+1 , p), -x(pair.y+1 , p) , 0 };
                    list.add(Arrays.asList(array));
                }

            }


            //two successive vertices must be connected by an edge.
            // ^x(from, j) x(to, j+1)

            for(int j=0; j<numVertices-1; j++){  //for each position
                for(int from=1; from<=numVertices; from++){   //each vertex
                    arraylist = new ArrayList<Integer>();

                    arraylist.add( -x(from,j) );

                    if(map.get(from).isEmpty()){
                        list.add( Arrays.asList( new Integer[]{from, 0}) );
                        list.add( Arrays.asList( new Integer[]{-from, 0}) );
                        return;
                    }

                    for(int to: map.get(from)){
                        arraylist.add( x( to  , j+1 ));
                    }
                    arraylist.add(0);

                    list.add(arraylist);
                }
            }
        }

    void printEquisatisfiableSatFormula() {
                                    //list size      //number of variables, vertex N in position N  N*N
            writer.printf("%d %d\n",list.size(), numVertices*numVertices );
            for(List l: list){
                for(Object i: l){
                    writer.printf("%d ", (Integer)i);
                }
                writer.printf("\n");
            }

        }
    }

    public void run() {
        int n = reader.nextInt();
        int m = reader.nextInt();

        converter = new ConvertHampathToSat(n, m);


        for (int i = 0; i < m; ++i) {
            converter.edges[i].from = reader.nextInt();
            converter.edges[i].to = reader.nextInt();
        }

        converter.calculateFormula();
        converter.printEquisatisfiableSatFormula();
    }

    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public double nextDouble() {
            return Double.parseDouble(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    static class OutputWriter {
        public PrintWriter writer;

        OutputWriter(OutputStream stream) {
            writer = new PrintWriter(stream);
        }

        public void printf(String format, Object... args) {
            writer.print(String.format(Locale.ENGLISH, format, args));
        }
    }
}
