import java.util.*;
public class Topological {
    private LinkedList<Integer> order;     // vertices in topological order
    private int[] ranks;              // ranks[v] = order where vertex v appers in order
    private int[] levels;              // ranks[v] = order where vertex v appers in order
    private int floor =0;

    /**
     * Determines whether the digraph {@code G} has a topological order and, if so,
     * finds such a topological order.
     * @param G the digraph
     */
    
    public Topological(EdgeWeightedDigraph G) {

        // indegrees of remaining vertices
        int[] indegree = new int[G.V()];
        for (int v = 0; v < G.V(); v++) {
            indegree[v] = G.indegree(v);
        }

        // initialize 
        ranks = new int[G.V()];
        levels = new int[G.V()]; 
        order = new LinkedList<Integer>();
        int count = 0;

        // initialize queue to contain all vertices with indegree = 0
        LinkedList<Integer> queue = new LinkedList<Integer>();
        for (int v = 0; v < G.V(); v++)
            if (indegree[v] == 0) queue.addLast(v);

        while (!queue.isEmpty()) {
            int v = queue.poll();
            order.addLast(v);
            ranks[v] = count++;
            for (DirectedEdge e : G.adj(v)) {
                levels[e.to()]= levels[v]+1;
                floor=levels[e.to()];
                int w = e.to();
                indegree[w]--;
                if (indegree[w] == 0) queue.addLast(w);
            }
        }
        // there is a directed cycle in subgraph of vertices with indegree >= 1.
        if (count != G.V()) {
            order = null;
        }

        assert check(G);
    }

    /**
     * Returns a topological order if the digraph has a topologial order,
     * and {@code null} otherwise.
     * @return a topological order of the vertices (as an interable) if the
     *    digraph has a topological order (or equivalently, if the digraph is a DAG),
     *    and {@code null} otherwise
     */
    public LinkedList<Integer> order() {
        return order;
    }

    /**
     * Does the digraph have a topological order?
     * @return {@code true} if the digraph has a topological order (or equivalently,
     *    if the digraph is a DAG), and {@code false} otherwise
     */
    public boolean hasOrder() {
        return order != null;
    }

    /**
     * The the rank of vertex {@code v} in the topological order;
     * -1 if the digraph is not a DAG
     *
     * @param v vertex
     * @return the position of vertex {@code v} in a topological order
     *    of the digraph; -1 if the digraph is not a DAG
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int rank(int v) {
        validateVertex(v);
        if (hasOrder()) return ranks[v];
        else            return -1;
    }
    public int level(int v) {
        validateVertex(v);
        if (hasOrder()) return levels[v];
        else            return -1;
    }

    public int floor(){
        return floor;
    }
    // certify that digraph is acyclic
    private boolean check(EdgeWeightedDigraph G) {

        // digraph is acyclic
        if (hasOrder()) {
            // check that ranks are a permutation of 0 to V-1
            boolean[] found = new boolean[G.V()];
            for (int i = 0; i < G.V(); i++) {
                found[rank(i)] = true;
            }
            for (int i = 0; i < G.V(); i++) {
                if (!found[i]) {
                    System.err.println("No vertex with rank " + i);
                    return false;
                }
            }

            // check that ranks provide a valid topological order
            for (int v = 0; v < G.V(); v++) {
                for (DirectedEdge e : G.adj(v)) {
                    int w = e.to();
                    if (rank(v) > rank(w)) {
                        System.err.printf("%d-%d: rank(%d) = %d, rank(%d) = %d\n",
                                          v, w, v, rank(v), w, rank(w));
                        return false;
                    }
                }
            }

            // check that order() is consistent with rank()
            int r = 0;
            for (int v : order()) {
                if (rank(v) != r) {
                    System.err.println("order() and rank() inconsistent");
                    return false;
                }
                r++;
            }
        }


        return true;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = ranks.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

}