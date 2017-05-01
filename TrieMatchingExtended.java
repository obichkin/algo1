import java.io.*;
import java.util.*;

class Node
{
	public static final int Letters =  4;
	public static final int NA      = -1;
	public int next [];
	public boolean patternEnd;

	Node ()
	{
		next = new int [Letters];
		Arrays.fill (next, NA);
		patternEnd = false;
	}
}

public class TrieMatchingExtended implements Runnable {
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

	List <Integer> solve (String text, int n, List <String> patterns) {

        List<Node> trie = buildTrie(patterns);
        List <Integer> result = new ArrayList <Integer> ();
        if(trie.get(0).patternEnd) return new ArrayList<Integer>(0);


        int current, j;
        char[] ch = text.toCharArray();
        int m = ch.length;

        for(int i=0; i<m; i++){
            current = 0;
            j=i;

            while(true){
                if(trie.get(current).patternEnd){
                    result.add(i);
                    break;
                }else if(j>=m){
                    break;

                }else if(trie.get(current).next[letterToIndex(ch[j])] != -1 ){
                    current = trie.get(current).next[letterToIndex(ch[j])];
                    j++;
                }else{
                    break;
                }
            }

        }

		// write your code here

		return result;
	}


    List<Node> buildTrie(List<String> patterns) {
        List<Node> trie = new ArrayList<Node>();

        int current=0, nextNode;
        trie.add(new Node());
        if(patterns.isEmpty()) trie.get(current).patternEnd=true;

        for(String s : patterns){
            char[] ch = s.toCharArray();
            current=0;
            for(int i=0; i< ch.length; i++){

                if( trie.get(current).next[ letterToIndex(ch[i])] == -1){

                    nextNode = trie.size();
                    trie.get(current).next[ letterToIndex( ch[i] )] = nextNode;
                    trie.add(new Node());
                    current = nextNode;



                }else{
                    current=trie.get(current).next[letterToIndex(ch[i])];
                }
            }
            trie.get(current).patternEnd=true;
        }

        // write your code here

        return trie;
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
		new Thread (new TrieMatchingExtended ()).start ();
	}
}
