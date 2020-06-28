package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.*;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;



public class Model {
	
	
	
	private Graph<ArtObject, DefaultWeightedEdge> grafo;
	private Map<Integer, ArtObject> idMap; 
	
	public Model() {
	
		this.idMap=new HashMap<Integer, ArtObject>();
		
	}
	
	
	public void creaGrafo() {
//	Crearlo nel metodo e non nel costruttore
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		ArtsmiaDAO dao = new ArtsmiaDAO();
		dao.listObjects(idMap);
		
//	Aggiungere i vertici, in questo caso tutte le righe della tabella objects, i valori della mappa
		Graphs.addAllVertices(this.grafo, idMap.values());
		
//	Creare gli archi
		
//		Metodo 1 : doppio ciclo for sui vertici, dati due vertici controllo se sono collegati in base
//		alle condizioni richieste.
//		Scorro tutte le possibile connessioni, quindi per db grandi non va bene. Questo impiega 67 giorni
//		Inoltre non fa la differenza di connessione a1a2 con a2a1. Devo controllare se non esiste l'arco a priori.
//		for(ArtObject a1 : this.grafo.vertexSet()) {
//			for(ArtObject a2 : this.grafo.vertexSet()) {
//				
//				int peso = dao.getPeso(a1, a2);
//				if(peso>0) {
//					if(!this.grafo.containsEdge(a1, a2)) {
//					Graphs.addEdge(this.grafo, a1, a2, peso);
//					}
//				}
//			}
//		}
//		
//		System.out.println(String.format("Grafo Creato con Vertici %d e Archi %d", this.grafo.vertexSet().size(), this.grafo.edgeSet().size()));
		
		
//		Metodo 2: abbiamo cambiato la query ma ancora non efficiente
		
//		Metodo 3: Query migliorata, nella quale abbiamo anche escluso le coppie che si ripetono quindi evito il controllo
		
		for(Adiacenza a : dao.getAdiacenze()) {
			if(a.getPeso()>0) {
				Graphs.addEdge(this.grafo, idMap.get(a.getObj1()), idMap.get(a.getObj2()), a.getPeso());
			}
		}
				
	}
	
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
}
