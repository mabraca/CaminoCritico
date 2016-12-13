import java.util.*;
public class Imprimir {
    private LinkedList<LinkedList<Integer>> resultado = new LinkedList<LinkedList<Integer>>();
    /** Funcion depthFirst busca todos los caminos posibles de un nodo a otro nodo con el modo de DFS
     * @param graph Grafo no dirigido qu contiende todos los caminos
     * @param visited Lista de los nodos que han sido visitados
     * @param fin nodo terminal del camino
     * @param ini nodo inicial del camino
     * */
    public void Imprimir(EdgeWeightedDigraph graph, LinkedList<Integer> visited,int fin,int ini) {
        LinkedList<Integer> nodes = graph.ady(visited.getLast());
        // examine adjacent nodes
        for (int node : nodes) {
            if (visited.contains(node)) {
                continue;
            }
            if (node==fin) {  
                visited.add(node);
                printPath(visited);                
                visited.removeLast();
                break;
            }
        }

        for (int node : nodes) {
            if (visited.contains(node) || node==fin) {
                continue;
            }
                visited.addLast(node);
                Imprimir(graph, visited,fin,ini);
                visited.removeLast();
        }
    }
    /**
     * Funcion de prueba para imprimir los caminos posibles, adicional anade a una lista auxiliar 
     * los nodos obtenidos para una vez realizado agragarlo a la lista de lista <tt>resultados</t>
     * @param visited Lista de visitados
     */
    public void printPath(LinkedList<Integer> visited) {
        LinkedList<Integer> aux =new LinkedList<Integer>();
        for (int node : visited) {
            aux.add(node);  
        }
        resultado.add(aux);
        
    }
    /**
     * Lista de resultados de caminos posibles          
     * @return Lista de lista de caminos posibles obtenidos con el DFS
     */
    public LinkedList<LinkedList<Integer>> result(){ 
        return resultado;
    }
}