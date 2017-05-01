import java.util.*;
import java.io.*;

public class SuffixTree {
    String text;
    List<String> result;
    List<Node> trie;

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

    // Build a suffix tree of the string text and return a list
    // with all of the labels of its edges (the corresponding 
    // substrings of the text) in any order.
    public List<String> computeSuffixTreeEdges(String text) {
        result = new ArrayList<String>();
        this.text = text;
        trie = buildTrie(text);
        // Implement this function yourself

        process(0, new StringBuilder());

        return result;
    }

    class Node
    {
        public static final int Letters =  5;
        public static final int NA      = -1;
        public int next [];

        Node ()
        {
            next = new int [Letters];
            Arrays.fill (next, NA);

        }

        public int getChildren() {
            int c=0;
            for(int i:next){
                if(i != -1) c++;
            }
            return c;
        }
    }

    int ch2int(char letter) {
        switch (letter)
        {
            case 'A': return 0;
            case 'C': return 1;
            case 'G': return 2;
            case 'T': return 3;
            case '$': return 4;
            default: return Node.NA;
        }
    }

    char int2ch(int i){
        switch(i){
            case 0: return 'A';
            case 1: return 'C';
            case 2: return 'G';
            case 3: return 'T';
            case 4: return '$';
            default: return '#';

        }
    }

    void process(int i, StringBuilder sb){
        Node node = trie.get(i);
        int children = node.getChildren();

        if(children > 1){
            if(i>0) System.out.println(sb.toString());                 //result.add(sb.toString());

            for(int j=0; j< Node.Letters; j++){
                if(node.next[j] != -1){
                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(int2ch(j));
                    process(node.next[j], sb1);
                }

            }
        }else if(node.getChildren() == 1){
            for(int j=0; j<Node.Letters; j++){
                if(node.next[j] != -1 ){
                    sb.append( int2ch( j ) );
                    process(node.next[j], sb);
                }

            }
        }else if(node.getChildren() == 0){
            System.out.println( sb.toString());
            //result.add( sb.toString() );
        }
    }





    List<Node> buildTrie(String text) {
        trie = new ArrayList<Node>();

        int current=0, nextNode;
        trie.add(new Node());

        int n = text.length();
        char[] ch = text.toCharArray();

        for(int i=0; i<n; i++){
            current=0;
            for(int j=i; j< n; j++){

                if( trie.get(current).next[ ch2int(ch[j])] == -1){

                    nextNode = trie.size();
                    trie.get(current).next[ ch2int(ch[j])] = nextNode;
                    trie.add(new Node());
                    current = nextNode;

                }else{
                    current=trie.get(current).next[ch2int(ch[j])];
                }

            }

        }

        // write your code here

        return trie;
    }



    static public void main(String[] args) throws IOException {
        new SuffixTree().run();
    }

    public void print(List<String> x) {
        for (String a : x) {
            System.out.println(a);
        }
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        List<String> edges = computeSuffixTreeEdges(text);
        //print(edges);
    }
}