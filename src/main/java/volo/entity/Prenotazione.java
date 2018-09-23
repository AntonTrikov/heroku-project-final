package volo.entity;

import java.util.List;

public class Prenotazione {
	public Prenotazione(int id, int slot, int compagnia, int tratta) {
		super();
		this.id = id;
		this.slot = slot;
		this.compagnia = compagnia;
		this.tratta = tratta;
	}
	public Prenotazione(int slot, int compagnia, int tratta) {
		super();
		this.id = id;
		this.slot = slot;
		this.compagnia = compagnia;
		this.tratta = tratta;
	}
	private int id;
	private int slot;
	private int compagnia;
	private int tratta;
	public int getSlot() {
		return slot;
	}
	public void setSlot(int slot) {
		this.slot = slot;
	}
	public int getCompagnia() {
		return compagnia;
	}
	public void setCompagnia(int compagnia) {
		this.compagnia = compagnia;
	}
	public int getTratta() {
		return tratta;
	}
	public void setTratta(int tratta) {
		this.tratta = tratta;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String toJson() {
		return "{"+
				"\"id\": "+this.id+","+
				"\"compagnia\": \""+this.compagnia+"\","+
				"\"slot\": \""+this.slot+"\","+
				"\"tratta\": "+this.tratta+" }";
	}
	public static String prenotazioniToJson(List<Prenotazione> prenotazioni) {
		int i=1;
		String result="[";
		for(Prenotazione prenotazione:prenotazioni) {
			result+=prenotazione.toJson();
			if(i++ != prenotazioni.size()) {
		    	result+=" ,";
		    }
		}
		result+="]";
		return result;
	}
}

