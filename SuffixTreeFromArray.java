import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SuffixTreeFromArray {

    static final char[] ACGT = new char[]{'$', 'A', 'C', 'G', 'T'};

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

    // Data structure to store edges of a suffix tree.
    public class Node {
        // The ending node of this edge.
        // Starting position of the substring of the text
        // corresponding to the label of this edge.
        int start;
        // Position right after the end of the substring of the text 
        // corresponding to the label of this edge.
        int end;

        int depth;

        int parent;

        Map<Character, Integer> children;


        Node(int parent, int start, int end, int depth) {

            this.parent = parent;
            this.start = start;
            this.end = end;
            this.depth = depth;
            this.children = new HashMap<Character, Integer>();

        }
    }


    // Build suffix tree of the string text given its suffix array suffix_array
    // and LCP array lcp_array. Return the tree as a mapping from a node ID
    // to the list of all outgoing edges of the corresponding node. The edges in the
    // list must be sorted in the ascending order by the first character of the edge label.
    // Root must have node ID = 0, and all other node IDs must be different
    // nonnegative integers.
    //
    // For example, if text = "ACACAA$", an edge with label "$" from root to a node with ID 1
    // must be represented by new Node(1, 6, 7). This edge must be present in the list tree.get(0)
    // (corresponding to the root node), and it should be the first edge in the list
    // (because it has the smallest first character of all edges outgoing from the root).
    Map<Integer, Node> SuffixTreeFromSuffixArray(
            int[] suffixArray,
            int[] lcpArray,
            final String text) {

        int n = text.length();
        int suffix, middle;
        Map<Integer, Node> tree = new HashMap<Integer, Node>();

        int current = 0;
        int lcp=0;

        tree.put(0, new Node(-1, -1, -1, 0));



        for(int i=0; i<n; i++){

            suffix = suffixArray[i];

            while( tree.get(current).depth > lcp ){
                current = tree.get(current).parent;
            }

            if( tree.get(current).depth == lcp ){
                current = createNewLeaf( current, tree, suffix, text );
            }else{
                int offset = lcp - tree.get(current).depth;
                int start = suffixArray[i-1] + tree.get(current).depth;

                middle = breakEdge( current, tree, start, offset, text );
                current = createNewLeaf( middle, tree, suffix, text);
            }

            if(i<n-1){
                lcp = lcpArray[i];
            }

        }

        return tree;
    }


    int breakEdge(int current, Map<Integer, Node> tree, int start, int offset, String text){

        int middle = tree.size();
        int depth = tree.get(current).depth + offset;
        int end = start + offset;



        tree.put( middle, new Node(current, start, end, depth));


        char startChar = text.charAt( start );
        char midChar = text.charAt( end );


        //pointer from middle to Child
        int child = tree.get(current).children.get(startChar);
        int childStart = start + offset;
        int childEnd = start + tree.get(child).depth - tree.get(current).depth;

        tree.get(middle).children.put( midChar, child);
        tree.get(child).parent = middle;
        tree.get(child).start = childStart;
        tree.get(child).end = childEnd;



        //pointer from Current to Middle
        tree.get(current).children.put( startChar, middle);




        return middle;
    }

    int createNewLeaf(int current, Map<Integer, Node> tree, int suffix, String text){

        int leaf = tree.size();
        int depth = text.length() - suffix;
        int start = suffix + tree.get(current).depth;
        int end = text.length();

        tree.put(leaf, new Node(current, start, end, depth));

        char ch = text.charAt(start);
        tree.get(current).children.put( ch, leaf);

        return leaf;
    }


    private void stressTest(){
        //String text = "GTAGT$";
        //int[] suffixArray = new int[]{5, 2, 3, 0, 4, 1};
        //int[] lcpArray = new int[]{0, 0, 2, 0, 1};


        String text = "ATAAATG$";
        int[] suffixArray = new int[]{7, 2, 3, 0, 4, 6, 1, 5};
        int[] lcpArray = new int[]{0, 2, 1, 2, 0, 0, 1};


        //String text = "AAA$";
        //int[] suffixArray = new int[]{3, 2, 1, 0};
        //int[] lcpArray = new int[]{0, 1, 2};

        for(int i=0; i< suffixArray.length; i++){
            System.out.println( suffixArray[i] + " " + text.substring( suffixArray[i] ) );
        }
        System.out.println();

        Map<Integer, Node> tree = new SuffixTreeFromArray().SuffixTreeFromSuffixArray(suffixArray, lcpArray,text);

        OutputEdges(tree, 0);

    }

    static public void main(String[] args) throws IOException {
        new SuffixTreeFromArray().run();
    }

    public void print(ArrayList<String> x) {
        for (String a : x) {
            System.out.println(a);
        }
    }


    public void OutputEdges(Map<Integer, Node> tree, int nodeId) {
        Integer i;

        Map<Character, Integer> children = tree.get(nodeId).children;
        for ( Character ch : ACGT ) {

            if( ( i = children.get(ch)) != null  ){
                System.out.println( tree.get(i).start + " " + tree.get(i).end );
                OutputEdges(tree, i);
            }
        }
    }


    public void run() throws IOException {

        stressTest();

        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        int[] suffixArray = new int[text.length()];
        for (int i = 0; i < suffixArray.length; ++i) {
            suffixArray[i] = scanner.nextInt();
        }
        int[] lcpArray = new int[text.length() - 1];
        for (int i = 0; i + 1 < text.length(); ++i) {
            lcpArray[i] = scanner.nextInt();
        }
        System.out.println(text);

        // Build the suffix tree and get a mapping from 
        // suffix tree node ID to the list of outgoing Edges.
        //Map<Integer, List<Node>> suffixTree = SuffixTreeFromSuffixArray(suffixArray, lcpArray, text);

        Map<Integer, Node> tree = SuffixTreeFromSuffixArray(suffixArray, lcpArray, text);
        ArrayList<String> result = new ArrayList<String>();


        // Output the edges of the suffix tree in the required order.
        // Note that we use here the contract that the root of the tree
        // will have node ID = 0 and that each vector of outgoing edges
        // will be sorted by the first character of the corresponding edge label.
        //
        // The following code avoids recursion to avoid stack overflow issues.
        // It uses two stacks to convert recursive function to a while loop.
        // This code is an equivalent of 
        //
            OutputEdges(tree, 0);
        //
        // for the following _recursive_ function OutputEdges:
        //


        /*
        int[] nodeStack = new int[text.length()];
        int[] edgeIndexStack = new int[text.length()];
        nodeStack[0] = 0;
        edgeIndexStack[0] = 0;
        int stackSize = 1;
        while (stackSize > 0) {
            int node = nodeStack[stackSize - 1];
            int edgeIndex = edgeIndexStack[stackSize - 1];
            stackSize -= 1;
            if (suffixTree.get(node) == null) {
                continue;
            }
            if (edgeIndex + 1 < suffixTree.get(node).size()) {
                nodeStack[stackSize] = node;
                edgeIndexStack[stackSize] = edgeIndex + 1;
                stackSize += 1;
            }
            result.add(suffixTree.get(node).get(edgeIndex).start + " " + suffixTree.get(node).get(edgeIndex).end);
            nodeStack[stackSize] = suffixTree.get(node).get(edgeIndex).node;
            edgeIndexStack[stackSize] = 0;
            stackSize += 1;
        }


        print(result);
        */
    }
}
