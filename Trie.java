import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Trie {
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

    List<Map<Character, Integer>> buildTrie(String[] patterns) {
        List<Map<Character, Integer>> trie = new ArrayList<Map<Character, Integer>>();

        int current, next;
        trie.add(new HashMap<Character, Integer>());

        for(String s : patterns){
            char[] ch = s.toCharArray();
            current=0;
            for(int i=0; i< ch.length; i++){

                if( !trie.get(current).containsKey(ch[i]) ){

                    next = trie.size();
                    trie.get(current).put(ch[i], next);
                    trie.add(new HashMap<Character, Integer>());
                    current = next;

                }else{
                    current=trie.get(current).get(ch[i]);
                }
            }
        }

        // write your code here

        return trie;
    }

    private static void stressTest(){
        String[] patterns = new String[]{"ATA"};

        Trie trie = new Trie();
        List<Map<Character, Integer>> t = trie.buildTrie(patterns);
        trie.print(t);

        patterns = new String[]{"AT", "AG", "AC"};
        t = trie.buildTrie(patterns);
        trie.print(t);
        System.out.println();


        patterns = new String[]{"panamabananas","ananas", "mama", "nana"};
        t = trie.buildTrie(patterns);
        trie.print(t);

    }


    static public void main(String[] args) throws IOException {
        //stressTest();

        new Trie().run();
    }

    public void print(List<Map<Character, Integer>> trie) {
        for (int i = 0; i < trie.size(); ++i) {
            Map<Character, Integer> node = trie.get(i);
            for (Map.Entry<Character, Integer> entry : node.entrySet()) {
                System.out.println(i + "->" + entry.getValue() + ":" + entry.getKey());
            }
        }
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        int patternsCount = scanner.nextInt();
        String[] patterns = new String[patternsCount];
        for (int i = 0; i < patternsCount; ++i) {
            patterns[i] = scanner.next();
        }
        List<Map<Character, Integer>> trie = buildTrie(patterns);
        print(trie);
    }
}
