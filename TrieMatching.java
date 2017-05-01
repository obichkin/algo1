import java.io.*;
import java.util.*;

class Node0
{
	public static final int Letters =  4;
	public static final int NA      = -1;
	public int next [];

	Node0 ()
	{
		next = new int [Letters];
		Arrays.fill (next, NA);
	}
}

public class TrieMatching implements Runnable {
	int letterToIndex (char letter)
	{
		switch (letter)
		{
			case 'A': return 0;
			case 'C': return 1;
			case 'G': return 2;
			case 'T': return 3;
			default: assert (false); return Node.NA;
		}
	}

    List<Map<Character, Integer>> buildTrie(List<String> patterns) {
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



	List <Integer> solve (String text, int n, List <String> patterns) {
        List<Map<Character, Integer>> trie = buildTrie(patterns);
        if(trie.isEmpty()) return new ArrayList<Integer>(0);

        List <Integer> result = new ArrayList <Integer> ();
        int current, j;
        char[] ch = text.toCharArray();
        int m = ch.length;

        for(int i=0; i<m; i++){
            current = 0;
            j=i;

            while(true){
                if(trie.get(current).isEmpty()){
                    result.add(i);
                    break;
                }else if(j>=m){
                    break;

                }else if(trie.get(current).containsKey( ch[j] ) ){
                    current = trie.get(current).get( ch[j] );
                    j++;

                }else{
                    break;
                }
            }

        }

		// write your code here

		return result;
	}

	public void run () {
		try {
			BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
			String text = in.readLine ();
		 	int n = Integer.parseInt (in.readLine ());
		 	List <String> patterns = new ArrayList <String> ();
			for (int i = 0; i < n; i++) {
				patterns.add (in.readLine ());
			}

			List <Integer> ans = solve (text, n, patterns);

			for (int j = 0; j < ans.size (); j++) {
				System.out.print ("" + ans.get (j));
				System.out.print (j + 1 < ans.size () ? " " : "\n");
			}
		}
		catch (Throwable e) {
			e.printStackTrace ();
			System.exit (1);
		}
	}

	public static void main (String [] args) {
		new Thread (new TrieMatching ()).start ();
	}
}
