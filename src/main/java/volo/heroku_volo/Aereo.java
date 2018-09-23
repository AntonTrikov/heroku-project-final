package volo.heroku_volo;

import java.util.List;

public class Aereo {
	public Aereo(int id, String nomeAereo, double lunghezza, double aperturaAlare, int vel, int velMax,
			double superficieAlare, int pesoVuoto, int pesoMax, double capacitaCombustibile, String motore,
			List<Integer> passeggeri) {
		super();
		this.id = id;
		this.nomeAereo = nomeAereo;
		this.lunghezza = lunghezza;
		this.aperturaAlare = aperturaAlare;
		this.vel = vel;
		this.velMax = velMax;
		this.superficieAlare = superficieAlare;
		this.pesoVuoto = pesoVuoto;
		this.pesoMax = pesoMax;
		this.capacitaCombustibile = capacitaCombustibile;
		this.motore = motore;
		this.passeggeri = passeggeri;
	}
	public Aereo(String nomeAereo, double lunghezza, double aperturaAlare, int vel, int velMax, double superficieAlare,
			int pesoVuoto, int pesoMax, double capacitaCombustibile, String motore, List<Integer> passeggeri) {
		super();
		this.nomeAereo = nomeAereo;
		this.lunghezza = lunghezza;
		this.aperturaAlare = aperturaAlare;
		this.vel = vel;
		this.velMax = velMax;
		this.superficieAlare = superficieAlare;
		this.pesoVuoto = pesoVuoto;
		this.pesoMax = pesoMax;
		this.capacitaCombustibile = capacitaCombustibile;
		this.motore = motore;
		this.passeggeri = passeggeri;
	}
	private int id;
	private String nomeAereo;
	private double lunghezza;
	private double aperturaAlare;
	private int vel;
	private int velMax;
	private double superficieAlare;
	private int pesoVuoto;
	private int pesoMax;
	private double capacitaCombustibile;
	private String motore;
	private List<Integer> passeggeri;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNomeAereo() {
		return nomeAereo;
	}
	public void setNomeAereo(String nomeAereo) {
		this.nomeAereo = nomeAereo;
	}
	public double getLunghezza() {
		return lunghezza;
	}
	public void setLunghezza(double lunghezza) {
		this.lunghezza = lunghezza;
	}
	public double getAperturaAlare() {
		return aperturaAlare;
	}
	public void setAperturaAlare(double aperturaAlare) {
		this.aperturaAlare = aperturaAlare;
	}
	public int getVel() {
		return vel;
	}
	public void setVel(int vel) {
		this.vel = vel;
	}
	public int getVelMax() {
		return velMax;
	}
	public void setVelMax(int velMax) {
		this.velMax = velMax;
	}
	public double getSuperficieAlare() {
		return superficieAlare;
	}
	public void setSuperficieAlare(double superficieAlare) {
		this.superficieAlare = superficieAlare;
	}
	public int getPesoVuoto() {
		return pesoVuoto;
	}
	public void setPesoVuoto(int pesoVuoto) {
		this.pesoVuoto = pesoVuoto;
	}
	public int getPesoMax() {
		return pesoMax;
	}
	public void setPesoMax(int pesoMax) {
		this.pesoMax = pesoMax;
	}
	public double getCapacitaCombustibile() {
		return capacitaCombustibile;
	}
	public void setCapacitaCombustibile(double capacitaCombustibile) {
		this.capacitaCombustibile = capacitaCombustibile;
	}
	public String getMotore() {
		return motore;
	}
	public void setMotore(String motore) {
		this.motore = motore;
	}
	public List<Integer> getPasseggeri() {
		return passeggeri;
	}
	public void setPasseggeri(List<Integer> passeggeri) {
		this.passeggeri = passeggeri;
	}
	public String toJson() {
		return "{"+
				"\"id\": "+this.id+","+
				"\"name\": \""+this.nomeAereo+"\","+
				"\"lunghezza\": "+this.lunghezza+","+
				"\"apertura alare\": "+this.aperturaAlare+","+
				"\"vel\": "+this.vel+","+
				"\"vel max\": "+this.velMax+","+
				"\"superficie alare\": "+this.superficieAlare+","+
				"\"peso a vuoto\": "+this.pesoVuoto+","+
				"\"peso max al decollo\": "+this.pesoMax+","+
				"\"passeggeri\": "+this.passeggeri.toString()+","+
				"\"capacità combustibile\": "+this.capacitaCombustibile+","+	
				"\"motore\": \""+this.motore+"\""+"}";
	}
	
}
