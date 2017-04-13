import java.io.*;
import java.util.*;

public class CircuitDesign {
    private final InputReader reader;
    private final OutputWriter writer;
    TwoSatisfiability twoSat;
    int result[];
    int resultNaive[];
    int numVars;

    public CircuitDesign(InputReader reader, OutputWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static void main(String[] args) {
        InputReader reader = new InputReader(System.in);
        OutputWriter writer = new OutputWriter(System.out);
        new CircuitDesign(reader, writer).run();
        writer.writer.flush();
    }

    int vIndex(int v){
        if(v==0) return -1;
        return (v>0) ? v-1 : numVars-v-1;
    }

    int pair(int v){
        return (v<numVars) ? v+numVars : v-numVars;
    }

    int vertex(int v){
        if(v<0) return -1;
        return (v<numVars) ? v+1 : v-numVars+1;
    }


    class Clause {
        int firstVar;
        int secondVar;
    }


    class Graph {
        List<Set<Integer>> vertices;
        int topOrder[];
        int currentOrder;
        boolean visited[];
        Set<Integer> scc;


        public Graph(int numVars) {
            vertices = new ArrayList<>(numVars*2);
            topOrder = new int[numVars*2];
            visited = new boolean[numVars*2];
            currentOrder = 0;
            scc = new HashSet<>();

            for(int i=1; i<=numVars*2; i++){
                vertices.add(new HashSet<Integer>());
            }
        }


        void dfs(Integer u){
            if(!visited[u]){
                visited[u] = true;
                scc.add(u);
                for(Integer v : vertices.get(u)){
                    dfs(v);
                }
                topOrder[currentOrder++] = u;
            }
        }

    }

    class TwoSatisfiability {
        Clause[] clauses;
        List<Set<Integer>> sccList;

        TwoSatisfiability(int n, int m) {
            numVars = n;
            clauses = new Clause[m];
            for (int i = 0; i < m; ++i) {
                clauses[i] = new Clause();
            }
            sccList = new ArrayList<>();
        }

        boolean isSatisfiable(int[] result){

            Graph graph = new Graph(numVars);
            Graph gRev  = new Graph(numVars);

            populateGraphs(graph, gRev);

            //fill topOrder
            for(int i=0; i<numVars*2; i++){
                gRev.dfs( i );
            }

            //find scc's
            for(int i=numVars*2-1; i>=0; i--){
                if(graph.visited[ gRev.topOrder[i] ]) continue;

                graph.scc = new HashSet<>();
                graph.dfs( gRev.topOrder[i] );

                sccList.add(graph.scc);
                //for(int j=0; j<graph.scc.size(); j++) System.out.print(graph.scc.get(j));                System.out.println();

            }


            //for(int i=0; i<numVars*2; i++)                System.out.println( i + " pair " + pair(i) );


            //traverse sccList
            int index;
            boolean value = true;
            int resultTemp[] = new int[numVars*2];
            Arrays.fill(resultTemp, -1);

            for(Set<Integer> scc : sccList){
                for(Integer i : scc){
                    if( scc.contains( pair(i) )) return false;

                    resultTemp[i] = value ? 1 : 0;
               }

            }


            return true;
        }


        void populateGraphs(Graph graph, Graph gRev){
            int x, y;

            //for(int i=-numVars; i<=numVars; i++)  System.out.printf("%d %d\n", i, vIndex(i));
            //for(int i=0; i<numVars*2; i++) System.out.printf("%d %d\n", i, vertex(i));



            for(int i=0; i<clauses.length; i++){
                x =  clauses[i].firstVar ;
                y =  clauses[i].secondVar ;

                graph.vertices.get( vIndex(-x) ).add( vIndex(y) );
                graph.vertices.get( vIndex(-y) ).add( vIndex(x) );

                gRev.vertices.get( vIndex(y) ).add(vIndex(-x));
                gRev.vertices.get( vIndex(x) ).add(vIndex(-y));

            }
        }

        boolean isSatisfiableNaive(int[] result) {
            // This solution tries all possible 2^n variable assignments.
            // It is too slow to pass the problem.
            // Implement a more efficient algorithm here.
            for (int mask = 0; mask < (1 << numVars); ++mask) {
                for (int i = 0; i < numVars; ++i) {
                    resultNaive[i] = (mask >> i) & 1;
                }

                boolean formulaIsSatisfied = true;

                for (Clause clause: clauses) {
                    boolean clauseIsSatisfied = false;
                    if ((resultNaive[Math.abs(clause.firstVar) - 1] == 1) == (clause.firstVar < 0)) {
                        clauseIsSatisfied = true;
                    }
                    if ((resultNaive[Math.abs(clause.secondVar) - 1] == 1) == (clause.secondVar < 0)) {
                        clauseIsSatisfied = true;
                    }
                    if (!clauseIsSatisfied) {
                        formulaIsSatisfied = false;
                        break;
                    }
                }

                if (formulaIsSatisfied) {
                    return true;
                }
            }
            return false;
        }
    }

    public void run() {
        int n = reader.nextInt();
        int m = reader.nextInt();

        twoSat = new TwoSatisfiability(n, m);
        for (int i = 0; i < m; ++i) {
            twoSat.clauses[i].firstVar = reader.nextInt();
            twoSat.clauses[i].secondVar = reader.nextInt();
        }

        result = new int[n];
        Arrays.fill(result, -1);
        resultNaive = new int[n];


        if (twoSat.isSatisfiable(result)) {
            writer.printf("SATISFIABLE\n");
            for (int i = 1; i <= n; ++i) {
                if (result[i-1] == 1) {
                    writer.printf("%d", -i);
                } else {
                    writer.printf("%d", i);
                }
                if (i < n) {
                    writer.printf(" ");
                } else {
                    writer.printf("\n");
                }
            }
        } else {
            writer.printf("UNSATISFIABLE\n");
        }
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
