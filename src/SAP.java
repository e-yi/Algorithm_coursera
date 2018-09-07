import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


//Corner cases.  Throw a java.lang.IllegalArgumentException in the following situations:
//
//        Any argument is null
//        Any vertex argument is outside its prescribed range
//        Any iterable argument contains a null item
//Performance requirements.  All methods (and the constructor) should take time at most
// proportional to E + V in the worst case, where E and V are the number of edges and
// vertices in the digraph, respectively. Your data type should use space proportional to E + V.



public class SAP {

    private Digraph digraph;
    private int numV;

    public SAP(Digraph G){
    // constructor takes a digraph (not necessarily a DAG)
        digraph = new Digraph(G);
        numV = digraph.V();
    }

    public int length(int v, int w){
    // length of shortest ancestral path between v and w; -1 if no such path
        MyBFS pathV = new MyBFS(digraph,v);
        MyBFS pathW = new MyBFS(digraph,w);
        int min = Integer.MAX_VALUE;
        int anc = -1;
        for (int i=0;i<numV;i++){
            if (pathV.marked[i]&&pathW.marked[i]){
                int temp = pathV.distTo[i]+pathW.distTo[i];
                if (temp<min){
                    min = temp;
                    anc = i;
                }
            }
        }
        return min==Integer.MAX_VALUE?-1:min;
    }

    public int ancestor(int v, int w){
    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
        MyBFS pathV = new MyBFS(digraph,v);
        MyBFS pathW = new MyBFS(digraph,w);
        int min = Integer.MAX_VALUE;
        int anc = -1;
        for (int i=0;i<numV;i++){
            if (pathV.marked[i]&&pathW.marked[i]){
                int temp = pathV.distTo[i]+pathW.distTo[i];
                if (temp<min){
                    min = temp;
                    anc = i;
                }
            }
        }
        return anc;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w){
    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
        MyBFS pathV = new MyBFS(digraph,v);
        MyBFS pathW = new MyBFS(digraph,w);
        int min = Integer.MAX_VALUE;
        int anc = -1;
        for (int i=0;i<numV;i++){
            if (pathV.marked[i]&&pathW.marked[i]){
                int temp = pathV.distTo[i]+pathW.distTo[i];
                if (temp<min){
                    min = temp;
                    anc = i;
                }
            }
        }
        return min==Integer.MAX_VALUE?-1:min;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
        MyBFS pathV = new MyBFS(digraph,v);
        MyBFS pathW = new MyBFS(digraph,w);
        int min = Integer.MAX_VALUE;
        int anc = -1;
        for (int i=0;i<numV;i++){
            if (pathV.marked[i]&&pathW.marked[i]){
                int temp = pathV.distTo[i]+pathW.distTo[i];
                if (temp<min){
                    min = temp;
                    anc = i;
                }
            }
        }
        return anc;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph g = new Digraph(in);
        SAP sap = new SAP(g);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}