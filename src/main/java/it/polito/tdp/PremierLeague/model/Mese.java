package it.polito.tdp.PremierLeague.model;

public class Mese {
	private int numMese;
	private String nomeMese;
	
	public Mese(int numMese, String nomeMese) {
		super();
		this.numMese = numMese;
		this.nomeMese = nomeMese;
	}
	public int getNumMese() {
		return numMese;
	}
	public void setNumMese(int numMese) {
		this.numMese = numMese;
	}
	public String getNomeMese() {
		return nomeMese;
	}
	public void setNomeMese(String nomeMese) {
		this.nomeMese = nomeMese;
	}
	@Override
	public String toString() {
		return nomeMese;
	}
	
}
