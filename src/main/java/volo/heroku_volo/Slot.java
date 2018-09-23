package volo.heroku_volo;

import java.util.List;

public class Slot {
	public Slot(int id,String name, int slots, String day, String inizio, String fine, List<Integer> aereiAccettati) {
		super();
		this.id =id;
		this.name = name;
		this.slots = slots;
		this.day = day;
		this.inizio = inizio;
		this.fine = fine;
		this.aereiAccettati = aereiAccettati;
	}
	public Slot(int id,String name, int slots, String day, String inizio, String fine) {
		super();
		this.id =id;
		this.name = name;
		this.slots = slots;
		this.day = day;
		this.inizio = inizio;
		this.fine = fine;
	}
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	private String name;
	private int slots;
	private String day;
	private String inizio;
	private String fine;
	private List<Integer> aereiAccettati;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSlots() {
		return slots;
	}
	public void setSlots(int slots) {
		this.slots = slots;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getInizio() {
		return inizio;
	}
	public void setInizio(String inizio) {
		this.inizio = inizio;
	}
	public String getFine() {
		return fine;
	}
	public void setFine(String fine) {
		this.fine = fine;
	}
	
	public static String slotToJson(Slot slot) {
		return "{"+
				"\"id\": \""+slot.getId()+"\","+
				"\"slotName\": \""+slot.getName()+"\","+
				"\"slots\": "+slot.getSlots()+","+
				"\"time\": {"+
				"\"day\": \""+slot.getDay()+"\","+
				"\"from\": \""+slot.getInizio()+"\","+
				"\"to\": \""+slot.getFine()+"\","+
				"\"accepted_planes\": "+slot.getAereiAccettati().toString()
				+"}"
				+" }";
	}
	public List<Integer> getAereiAccettati() {
		return aereiAccettati;
	}
	public void setAereiAccettati(List<Integer> aereiAccettati) {
		this.aereiAccettati = aereiAccettati;
	}
}
