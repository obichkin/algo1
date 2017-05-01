import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class StronglyConnected {
    private static ArrayList<Integer>[] adj;
    private static ArrayList<Integer>[] adjR;
    private static int[] order;
    private static boolean[] visited;
    private static int o;

    private static int numberOfStronglyConnectedComponents() {
        int n=adj.length;
        visited = new boolean[n];
        order = new int[n];
        o=0;
        int scc=0;

        for(int i=0; i<n; i++){
            if(!visited[i]){
                exploreR(i);
            }
        }

        visited = new boolean[n];

        for (int x=n-1; x>=0; x--) {
            if(!visited[order[x]]){
                explore( order[x] );
                scc++;
            }
        }
        return scc;
    }


    private static void exploreR(int i){
        visited[i]=true;
        for(int e : adjR[i]){
            if(!visited[e]){
                exploreR(e);
            }
        }
        order[o++]=i;
    }

    private static void explore(int i){
        visited[i]=true;
        for(int e : adj[i]){
            if(!visited[e]){
                explore(e);
            }
        }
    }


    private static void stressTest(){
        int maxGraphSize=100;
        int edgeFactor=2;

        Random random = new Random();

        while(true){
            int n = random.nextInt(maxGraphSize)+1;

            adj = (ArrayList<Integer>[])new ArrayList[n];
            adjR = (ArrayList<Integer>[])new ArrayList[n];


            for (int i = 0; i < n; i++) {
                adj[i] = new ArrayList<Integer>();
                adjR[i] = new ArrayList<Integer>();
            }

            int m = random.nextInt(n*edgeFactor);

            for (int i = 0; i < m; i++) {
                int x, y;
                x = random.nextInt(n);
                y = random.nextInt(n);
                adj[x].add(y);
                adjR[y].add(x);
            }
            System.out.println(numberOfStronglyConnectedComponents());

        }
    }
     

    public static void main(String[] args) {

        //stressTest();

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        adj = (ArrayList<Integer>[])new ArrayList[n];
        adjR = (ArrayList<Integer>[])new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
            adjR[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y;
            x = scanner.nextInt();
            y = scanner.nextInt();
            adj[x - 1].add(y - 1);
            adjR[y - 1].add(x - 1);

        }

        System.out.println(numberOfStronglyConnectedComponents());
    }
}

