package volo.heroku_volo;

import java.util.List;

public class Compagnia {
	public Compagnia( List<Aereo> flotta, int id,String password, String nome, String dataIscrizione, String nazione,
			double valoreAzionario, List<String> partner) {
		super();
		this.id = id;
		this.nome = nome;
		this.password = password;
		this.dataIscrizione = dataIscrizione;
		this.nazione = nazione;
		this.valoreAzionario = valoreAzionario;
		this.partner = partner;
		this.flotta = flotta;
	}
	public Compagnia(int id,String nome, String password, String dataIscrizione, String nazione, double valoreAzionario,
			List<Integer> aerei, List<String> partner) {
		super();
		this.id=id;
		this.nome = nome;
		this.password = password;
		this.dataIscrizione = dataIscrizione;
		this.nazione = nazione;
		this.valoreAzionario = valoreAzionario;
		this.aerei = aerei;
		this.partner = partner;
	}
	public Compagnia(int id,String nome, String dataIscrizione, String nazione, double valoreAzionario,
			List<Integer> aerei, List<String> partner) {
		super();
		this.id=id;
		this.nome = nome;
		this.dataIscrizione = dataIscrizione;
		this.nazione = nazione;
		this.valoreAzionario = valoreAzionario;
		this.aerei = aerei;
		this.partner = partner;
	}
	public Compagnia(int id,String nome, String password, String dataIscrizione, String nazione, double valoreAzionario,
			List<Integer> aerei) {
		super();
		this.id=id;
		this.nome = nome;
		this.password = password;
		this.dataIscrizione = dataIscrizione;
		this.nazione = nazione;
		this.valoreAzionario = valoreAzionario;
		this.aerei = aerei;
	}
	public Compagnia(int id,String nome, String password, String dataIscrizione, String nazione, double valoreAzionario) {
		super();
		this.id=id;
		this.nome = nome;
		this.password = password;
		this.dataIscrizione = dataIscrizione;
		this.nazione = nazione;
		this.valoreAzionario = valoreAzionario;
	}
	public Compagnia(int id,String nome, String dataIscrizione, String nazione, double valoreAzionario) {
		super();
		this.id=id;
		this.nome = nome;
		this.dataIscrizione = dataIscrizione;
		this.nazione = nazione;
		this.valoreAzionario = valoreAzionario;
	}
	public Compagnia(String nome, String password, String dataIscrizione, String nazione, double valoreAzionario) {
		super();
		this.nome = nome;
		this.password = password;
		this.dataIscrizione = dataIscrizione;
		this.nazione = nazione;
		this.valoreAzionario = valoreAzionario;
	}
	private int id;
	private String nome;
	private String password;
	private String dataIscrizione;
	private String nazione;
	private double valoreAzionario;
	List<Integer> aerei;
	List<String> partner;
	List<Aereo> flotta;
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDataIscrizione() {
		return dataIscrizione;
	}
	public void setDataIscrizione(String dataIscrizione) {
		this.dataIscrizione = dataIscrizione;
	}
	public String getNazione() {
		return nazione;
	}
	public void setNazione(String nazione) {
		this.nazione = nazione;
	}
	public double getValoreAzionario() {
		return valoreAzionario;
	}
	public void setValoreAzionario(double valoreAzionario) {
		this.valoreAzionario = valoreAzionario;
	}
	public List<Integer> getAerei() {
		return aerei;
	}
	public void setAerei(List<Integer> aerei) {
		this.aerei = aerei;
	}
	public List<String> getPartner() {
		return partner;
	}
	public void setPartner(List<String> partner) {
		this.partner = partner;
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
				"\"name\": \""+this.nome+"\","+
				"\"password\": \""+this.password+"\","+
				"\"data iscrizione\": \""+this.dataIscrizione+"\","+
				"\"nazione\": \""+this.nazione+"\","+
				"\"valoreAzionario\": "+this.valoreAzionario+","+
				"\"partner\": "+this.partnerListToJson(this.partner)+","+
				"\"flotta\": "+this.flottaListToJson(this.flotta)+" }";
	}
	public String partnerListToJson(List<String> partners) {
		int i=1;
		String result= "[";
		for (String partner: partners) {
		    result+= "\""+partner+"\"";
		    if(i++ != partners.size()) {
		    	result+=" ,";
		    }
		}
		result+="]";
		return result;
	}
	public String flottaListToJson(List<Aereo> flotta) {
		int i=1;
		String result= "[";
		for (Aereo aereo: flotta) {
		    result+= aereo.toJson();
		    if(i++ != flotta.size()) {
		    	result+=" ,";
		    }
		}
		result+="]";
		return result;
	}
	
}
