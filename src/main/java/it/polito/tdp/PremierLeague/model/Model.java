package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	private SimpleWeightedGraph<Match, DefaultWeightedEdge> grafo;
	private Map<Integer, Match> idMap;
	private PremierLeagueDAO dao;
	private List<Match> vertici;
	private List<Adiacenza> archi;
	private List<Match> percorsoBest;
	private double pesoMax;
	
	public Model() {
		idMap = new HashMap<>();
		dao = new PremierLeagueDAO();
		pesoMax = 0;
		
		for(Match match : dao.listAllMatches()) {
			idMap.put(match.getMatchID(), match);
		}
	}
	
	public void creaGrafo(int min, Mese mese) {
		vertici = new ArrayList<>();
		archi = new ArrayList<>();
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, dao.getVertici(idMap, mese));
		vertici.addAll(dao.getVertici(idMap, mese));
		archi = dao.getArchi(idMap, mese, min);
		for(Adiacenza adiacenza : archi) {
			if(vertici.contains(adiacenza.getM1()) && vertici.contains(adiacenza.getM2())) {
				Graphs.addEdgeWithVertices(grafo, adiacenza.getM1(), adiacenza.getM2(), adiacenza.getPeso());
			}
		}
		
	}
	
	public Integer getNVertici() {
		return grafo.vertexSet().size();
	}
	 
	public Integer getNArchi() {
		return grafo.edgeSet().size();
	}
	
	public List<Match> getVertici() {
		return vertici;
	}
	
	public List<Adiacenza> getConnMax(int min) {
		List<Adiacenza> coppie = new ArrayList<>();
		double max = 0.0;
		
		for(Adiacenza adiacenza : archi) {
			if(adiacenza.getPeso() > max) {
				max = adiacenza.getPeso();
			}
		}
	
		for(Adiacenza adiacenza : archi) {
			if(adiacenza.getPeso() == max) {
				coppie.add(adiacenza);
			}
		}
		
		return coppie;
	}
	
	public List<Match> calcolaPercorso(Match m1, Match m2) {
		this.percorsoBest = null ;
		
		List<Match> parziale = new ArrayList<Match>();
		parziale.add(m1) ;
		
		cerca(parziale, 1, m2) ;
		
		return this.percorsoBest ;
	}
	
	public void cerca(List<Match> parziale, int livello, Match destinazione) {
		if(parziale.get(parziale.size()-1).equals(destinazione)) {
			if(calcolaPeso(parziale) > pesoMax) {
				pesoMax = calcolaPeso(parziale);
				percorsoBest = new ArrayList<>(parziale);
			}
			return;
		}
		
		Match lastMatch = parziale.get(parziale.size()-1);
		for(Match match : Graphs.neighborListOf(grafo, lastMatch)) {
			if(!parziale.contains(match)) {
				int teamHome1 = parziale.get(parziale.size()-1).teamHomeID;
				int teamHome2 = match.teamHomeID;
				int teamAway1 = parziale.get(parziale.size()-1).teamAwayID;
				int teamAway2 = match.teamAwayID;
				
				if(teamAway1 != teamAway2 && teamHome1 != teamHome2 && teamHome1 != teamAway1 && teamHome2 != teamAway2) {
					parziale.add(match);
					cerca(parziale, livello+1, destinazione);
					parziale.remove(parziale.size()-1);
				}
			}
		}
		
	}

	private double calcolaPeso(List<Match> lista) {
		double somma = 0;
		for(int i = 1; i < lista.size(); i++) {
			if(grafo.getEdge(lista.get(i-1), lista.get(i)) != null)
				somma += grafo.getEdgeWeight(grafo.getEdge(lista.get(i-1), lista.get(i)));
		}
		return somma;
	}
	
}
