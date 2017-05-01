import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class SchoolBus {
    FastScanner in = new FastScanner( new BufferedReader(new InputStreamReader(System.in)) );
    int INF = 999999999;
    int bestResult = INF;
    List<Integer> myBestPath = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        //in = new FastScanner( new BufferedReader(new InputStreamReader(System.in)) );
        //in = new FastScanner( new BufferedReader(new InputStreamReader( new FileInputStream("c:\\temp\\3"))) );

            new Thread(null, () -> {  new SchoolBus().run();   }, "1", 1 << 26).start();

    }


    public void run(){
        //printAnswer(schoolBus(readData()));
        printAnswer(myTSP(readData()));

    }


    Answer myTSP(int[][] graph){
        int n = graph.length;
        int m = (1 << n);
        int set, subset, lastVertex=0, prevVertex=0;

        int distance[][] = new int[m][n];
        int path[][] = new int[m][n];

        for(int i=0; i<m; i++){
            Arrays.fill(distance[i], INF);
        }

        distance[1][0] = 0;

        Integer order[] = new Integer[m];
        for(int i=0; i<m; i++){
            order[i] = i;
        }

        Arrays.sort(order, (first, second) -> Integer.bitCount(first) - Integer.bitCount(second));

        for(int k = 1; k<m; k++){  //traverse all sets S
            set = order[k];
            //System.out.println("\n\n Set : " + printSet(set));

            for(int i=0; i<=n; i++){

                if( (set  & (1<<i)) != 0){  //bit i   included into set S

                    subset = set ^ (1<<i);  //set S without  j

                    for(int j=0; j<=n; j++){
                        if((subset & (1<<j)) != 0){  //bit j   included into Subset S-i
                            //subset  +  j->i

                             if (distance[subset][j] + graph[j][i] < distance[ set ][i]){
                                 //System.out.printf("distance[set][%d] %d\n", i, distance[set][i]);
                                 //System.out.printf("subset %s ; distance[%d][%d] %d ; distance (%d,%d): %d \n", printSet(subset), subset, j, distance[subset][j], j, i, graph[j][i]);
                                 distance[ set ][i] = distance[subset][j] + graph[j][i];
                                 path[set][i] = j;

                             }

                        }

                    }
                }
            }
        }

        for(int j=1; j<n; j++){  //from complete Set   0 1 2 3    return to vertex 0
            if( distance[m-1][j] + graph[j][0] < bestResult  ){
                bestResult = distance[m-1][j] + graph[j][0];
                lastVertex = j;
            }

        }

        if (bestResult == INF) return new Answer(-1, new ArrayList<>());

        //for(int i=0; i<m; i++)            System.out.printf("%s \t\t\t %s\n", printSet(i), Arrays.toString(path[i]));
         
        set = m-1;
        myBestPath.add(lastVertex + 1);

        do{

            //System.out.printf("%s \t\t\t %s  last vertex %d\n", printSet(set), Arrays.toString(path[set]), lastVertex);

            prevVertex = lastVertex;
            lastVertex = path[set][lastVertex];

            set = set ^ (1<<prevVertex);

            myBestPath.add(lastVertex + 1);

        }
        while( lastVertex > 0);



        return new Answer(bestResult, myBestPath);
    }

    String printSet(int set){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<32; i++){
            if(( set & (1<<i) ) != 0) sb.append(i + " ");
        }
        //sb.append("\n");
        return sb.toString();
    }

    void swap(Integer[] array, int i, int j){
        Integer tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    void process(int[][] graph, Integer[] array, int i ){
        if( i == array.length-1){
            //System.out.println( Arrays.toString( array ) );

            int currentAnswer = 0;
            for(int j=1; j<array.length; j++){
                currentAnswer += graph[ array[j-1] ][ array[j] ];
            }
            currentAnswer += graph[ array[ array.length-1 ] ][ array[0] ];

            if(currentAnswer<bestResult){
                bestResult = currentAnswer;
                myBestPath = new ArrayList<>();
                for(int j=0; j<array.length; j++){
                    myBestPath.add( array[j] + 1 );
                }

            }

        }else{
            for(int j=i; j<array.length; j++){


                swap(array, i, j);
                process(graph, array, i+1);
                swap(array, j, i);

            }
        }
    }




    Answer myNaiveTSP(int[][] graph) {


        int n = graph.length;
        Integer[] array = new Integer[n];
        for(int i=0; i<n; i++){
            array[i] = i;
        }

        process(graph, array, 0);
        bestResult = (bestResult >= INF) ? -1 : bestResult;

        return new Answer(bestResult, myBestPath);
    }

    Answer schoolBus(int[][] graph) {
        // This solution tries all the possible sequences of stops.
        // It is too slow to pass the problem.
        // Implement a more efficient algorithm here.
        int n = graph.length;
        Integer[] p = new Integer[n];
        for (int i = 0; i < n; ++i)
            p[i] = i;

        class Solver {
            int bestAnswer = INF;
            List<Integer> bestPath;

            public void solve(Integer[] a, int n) {
                if (n == 1) {
                    boolean ok = true;
                    int curSum = 0;
                    for (int i = 1; i < a.length && ok; ++i)
                        if (graph[a[i - 1]][a[i]] == INF)
                            ok = false;
                        else
                            curSum += graph[a[i - 1]][a[i]];
                    if (graph[a[a.length - 1]][a[0]] == INF)
                        ok = false;
                    else
                        curSum += graph[a[a.length - 1]][a[0]];

                    if (ok && curSum < bestAnswer) {
                        bestAnswer = curSum;
                        bestPath = new ArrayList<Integer>(Arrays.asList(a));
                    }
                    return;
                }
                for (int i = 0; i < n; i++) {
                    swap(a, i, n - 1);
                    solve(a, n - 1);
                    swap(a, i, n - 1);
                }
            }

            private void swap(Integer[] a, int i, int j) {
                int tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;
            }
        }

        Solver solver = new Solver();
        solver.solve(p, n);
        if (solver.bestAnswer == INF)
            return new Answer(-1, new ArrayList<>());
        List<Integer> bestPath = solver.bestPath;
        for (int i = 0; i < bestPath.size(); ++i)
            bestPath.set(i, bestPath.get(i) + 1);

        return new Answer(solver.bestAnswer, bestPath);
    }

    int[][] readData() {
        try{
        int n = in.nextInt();
        int m = in.nextInt();
        int[][] graph = new int[n][n];

        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                graph[i][j] = INF;

        for (int i = 0; i < m; ++i) {
            int u = in.nextInt() - 1;
            int v = in.nextInt() - 1;
            int weight = in.nextInt();
            graph[u][v] = graph[v][u] = weight;
        }
        return graph;
        }catch (IOException e){
            e.printStackTrace();
        }
        return new int[0][0];
    }

    private void printAnswer(final Answer answer) {
        System.out.println(answer.weight);
        if (answer.weight == -1)
            return;
        for (int x : answer.path)
            System.out.print(x + " ");
        System.out.println();
    }

    class Answer {
        int weight;
        List<Integer> path;

        public Answer(int weight, List<Integer> path) {
            this.weight = weight;
            this.path = path;
        }
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner(BufferedReader reader) {
            this.reader = reader;
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

}
