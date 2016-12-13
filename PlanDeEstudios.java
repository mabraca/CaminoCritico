/**
 * PlanDeEstudios.java
 * Lectura del los archivos de entrada para la solucion del laboratorio 
 * "Ayudante de Plan de Estudios" de CI2693 (SD2016) 
 * Ivette C. Martínez
 * @author Maria Bracamonte 10-11147
 */

import java.util.*;


public class PlanDeEstudios{
	 private static HashMap<Integer,String> entero = new HashMap <Integer,String>();
	 private static HashMap<String, Integer> nodos = new HashMap <String, Integer>();
	 private static HashMap<String, Double> resultado = new HashMap <String, Double>();
	 private static int contador =0;
     private static LinkedList<String> materias; 

     private static int getId(String materia){
     	if(entero.containsValue(materia)==true) return nodos.get(materia);
     	else {
     		contador++;
     		entero.put(contador, materia);
     		nodos.put(materia, contador);
     		return contador;
     	}

     }

     private static void leerArchivoPlan(String n_archivo){

	  In myInput = new In(n_archivo);
	  int m; //Numero de materias
	  int r; // Numero de pares <materia_requisito,materia_prelada>  
	  try{  
	       m = myInput.readInt();
	       r = myInput.readInt();
	       for (int i=0; i<m; i++){
		    String str_materia = myInput.readString();
		    //System.out.println("Materia Actual: " + str_materia);
		    // Agregar materia
		    materias.add(str_materia);
	       }
	       if(m==0)throw new IllegalArgumentException("Ha ingresado 0 materias, no se puede hacer el plan de estudios");
	       if(r==0){
	       		System.out.println(" 1 " );
	       	for(String materia : materias){
	       		System.out.println(materia);
	       	}
	       	for(String materia : materias){
	       		System.out.println(materia +"  0 " );
	       	}
	       }else{
	       EdgeWeightedDigraph G = new EdgeWeightedDigraph (m+2);
	       for (int i=0;i<r; i++){
		    String materia_requisito = myInput.readString();
		    int identificador1 = getId(materia_requisito);
		    String materia_prelada = myInput.readString();
		    int identificador2 = getId(materia_prelada);
		    DirectedEdge lado = new DirectedEdge(identificador1, identificador2, 1);
		    G.addEdge(lado);
	       }

	       ////PREDECESOR FICTICIO
	       LinkedList<Integer> sinPredecesor = new LinkedList<Integer>();
	       for(DirectedEdge i: G.edges()){
	       if( G.tienePredecesor(i.from())==false && sinPredecesor.contains(i.from())==false){
	     			sinPredecesor.add(i.from());
	     		}
	       }
	       while(sinPredecesor.isEmpty()==false){
	     		DirectedEdge lado = new DirectedEdge(0,sinPredecesor.poll(), 1);
	     		G.addEdge(lado);
	        }
	        ////SUCESOR FICTICIO
	       LinkedList<Integer> sinSucesor = new LinkedList<Integer>();
	       for(DirectedEdge i: G.edges()){
	       if( G.tieneSucesor(i.to())==false && sinSucesor.contains(i.to())==false){
	     			sinSucesor.add(i.to());
	     		}
	       }
	       while(sinPredecesor.isEmpty()==false){
	     		DirectedEdge lado = new DirectedEdge(0,sinPredecesor.poll(), 0);
	     		G.addEdge(lado);
	        }
	        while(sinSucesor.isEmpty()==false){
	     		DirectedEdge lado = new DirectedEdge(sinSucesor.poll(), m+1, 0);
	     		G.addEdge(lado);
	        }
	        //ALFORITMO BELLMAN FOR 
	       Bellman(G, m);
	    }

	  } catch(NoSuchElementException e){
	       throw new InputMismatchException("Invalid input format in input file");      	
	  }	
     }

     public static void Bellman(EdgeWeightedDigraph G, int m){
			BellmanFord B = new BellmanFord(G,0);
	        Topological topological = new Topological(G);
	        double costoMax=0;
	        LinkedList<Integer> visitados = new LinkedList<Integer>();
	        visitados.add(0);
	        if (!topological.hasOrder())
	            throw new IllegalArgumentException("Digrafo no es un grafo de precedencia");
	        LinkedList<Integer>  lista = topological.order(); int j=0;
	        while(!lista.isEmpty()){
	        	int cola= lista.pollLast(); 
	        	int nivel= topological.level(cola);
	        	if (nivel== (topological.floor()-1)){
	        		 costoMax= B.distTo(cola);
	        		 if(j==1)StdOut.printf("%2.0f \n", costoMax, cola);
	        		 //Calculo de los caminos
	        		 Imprimir result = new Imprimir();
	        		 result.Imprimir(G, visitados,cola,0);
			     	for(LinkedList<Integer> list: result.result()){
				     	if(list.size()==costoMax+1){	
				     		for(int nodo : list){	
				     			if(entero.get(nodo)==null)continue;
				     			else {
				     				System.out.print(entero.get(nodo)+" ");
				     			}
				     		}System.out.println();
				     	}
			     	}
		     	
	        	}j++;
	        }
	        //Calculo de holgura
	        double holgura=0.0;
	        TAC tac = new TAC(G,m+1,costoMax);
	        for(DirectedEdge e: G.edges()){
	        	double v= B.distTo(e.to());
	        	if(v==Double.NEGATIVE_INFINITY)break;
	        	else{
	        		holgura= (double)tac.distTo(e.to())-(double)B.distTo(e.to());
	        		if(entero.get(e.to())==null)continue;
	        		else {
	        			if(resultado.containsKey(entero.get(e.to()))==false){
	        			 resultado.put(entero.get(e.to()), holgura);
	        			}
	        		}
	        	}
	        }holguras(costoMax); 
     }
     public static void holguras(double c) {
     	for(String materia : materias){
     		if(resultado.get(materia)==null){
     			double costo=c-1;
     			StdOut.printf("%s %2.0f  \n", materia,costo);
     		}else StdOut.printf("%s %2.0f  \n", materia,resultado.get(materia));
	    }
     }

     public static void main(String [] args) {

	  String nombre_archivo  = args[0];
	  materias = new LinkedList<String>();
	  leerArchivoPlan(nombre_archivo);
     

	  // Realizar los procesos necesarios para calcular:
	  // (1) El minimo numero de periodos academicos que debe cursar el estudiante,
	  // (2) la(s) lista(s) con las materias criticas
	  // y (3) la holgura de cada materia

     }
}
