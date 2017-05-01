import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Toposort {
    private static ArrayList<Integer>[] adj;
    private static boolean[] visited;
    private static int[] order;
    private static int o;

    private static void toposort(ArrayList<Integer>[] adj) {
        int n=adj.length;
        visited = new boolean[n];
        order = new int[n];
        o=0;

        for(int i=0; i<n; i++){
            if(!visited[i]){
                explore(i);
            }
        }


    }


    private static void explore(int i){
        visited[i]=true;
        for(int e : adj[i]){
            if(!visited[e]){
                explore(e);
            }
        }
        order[o++]=i;
    }


    private static void stressTest(){
        int maxGraphSize=100;
        int edgeFactor=2;

        Random random = new Random();

        while(true){
            int n = random.nextInt(maxGraphSize)+1;

            adj = (ArrayList<Integer>[])new ArrayList[n];
            visited = new boolean[n];

            for (int i = 0; i < n; i++) {
                adj[i] = new ArrayList<Integer>();
            }

            int m = random.nextInt(n*edgeFactor);

            for (int i = 0; i < m; i++) {
                int x, y;
                x = random.nextInt(n);
                y = random.nextInt(n);
                adj[x].add(y);
            }
            toposort(adj);

            for (int x=n-1; x>=0; x--) {
                System.out.print( order[x]  + " ");
            }
            System.out.println();


        }
    }



    public static void main(String[] args) {

        //stressTest();

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        adj = (ArrayList<Integer>[])new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y;
            x = scanner.nextInt();
            y = scanner.nextInt();
            adj[x - 1].add(y - 1);
        }
        toposort(adj);
        for (int x=n-1; x>=0; x--) {
            System.out.print( order[x]+1  + " ");
        }
    }
}

