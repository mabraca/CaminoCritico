import java.util.*;
public class TAC {
    private double[] distTo;               // distTo[v] = distance  of longest s->v path
    private DirectedEdge[] edgeTo;         // edgeTo[v] = last edge on longest s->v path
    private boolean[] onQueue;             // onQueue[v] = is v currently on the queue?
    private LinkedList<Integer> queue;          // queue of vertices to relax

   
    public TAC(EdgeWeightedDigraph G, int s, double costoMax) {
        distTo  = new double[G.V()];
        edgeTo  = new DirectedEdge[G.V()];
        onQueue = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++){
            distTo[v] = Double.POSITIVE_INFINITY;
        }

        distTo[s] = costoMax;
        queue = new LinkedList<Integer>();
        queue.addLast(s);
        onQueue[s] = true;
        while (!queue.isEmpty()) {
            int v = queue.poll();
            onQueue[v] = false;
            relax(G, v);
        }
    }

    private void relax(EdgeWeightedDigraph G, int v) {
        for (DirectedEdge e : G.sucesores(v)) {
            int w = e.from();
            if (distTo[w] > distTo[v] - e.weight()) {
                distTo[w] = distTo[v] - e.weight();
                edgeTo[w] = e;
                if (!onQueue[w]) {
                    queue.addLast(w);
                    onQueue[w] = true;
                }
            }
        }
    }

    
   
    public double distTo(int v) {
        validateVertex(v);
        return distTo[v];
    }

    
    public boolean hasPathTo(int v) {
        validateVertex(v);
        return distTo[v] > Double.POSITIVE_INFINITY;
    }

    
    public Iterable<DirectedEdge> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) return null;
        LinkedList<DirectedEdge> path = new LinkedList<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }
    
   
    private void validateVertex(int v) {
        int V = distTo.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    public static void main(String[] args) {
       
    }

}