package volo.heroku_volo;
import java.util.List;

import volo.heroku_volo.Slot;
public class Tratta {
	public Tratta(int id, String name, String type, String partenza, List<Slot> slotObjectList,String arrivo) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.partenza = partenza;
		this.arrivo = arrivo;
		this.slotObjectList = slotObjectList;
	}
	public Tratta(String name, String type, String partenza, String arrivo) {
		super();
		this.name = name;
		this.type = type;
		this.partenza = partenza;
		this.arrivo = arrivo;
	}
	public Tratta(String name, String type, String partenza, String arrivo, List<Integer> slot) {
		super();
		this.name = name;
		this.type = type;
		this.partenza = partenza;
		this.arrivo = arrivo;
		this.slot = slot;
	}
	public Tratta(int id, String name, String type, String partenza, String arrivo, List<Integer> slot) {
		super();
		this.id=id;
		this.name = name;
		this.type = type;
		this.partenza = partenza;
		this.arrivo = arrivo;
		this.slot = slot;
	}
	
	public Tratta(int id, String name, String type, String partenza, String arrivo) {
		super();
		this.id=id;
		this.name = name;
		this.type = type;
		this.partenza = partenza;
		this.arrivo = arrivo;
	}
	private int id;
	private String name;
	private String type;
	private String partenza;
	private String arrivo;
	private List<Integer> slot;
	private List<Slot> slotObjectList;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPartenza() {
		return partenza;
	}
	public void setPartenza(String partenza) {
		this.partenza = partenza;
	}
	public String getArrivo() {
		return arrivo;
	}
	public void setArrivo(String arrivo) {
		this.arrivo = arrivo;
	}
	public String toJson(List<Slot> slotTratta) {
		return "{"+
				"\"id\": "+this.id+","+
				"\"name\": \""+this.name+"\","+
				"\"type\": \""+this.type+"\","+
				"\"from\": \""+this.partenza+"\","+
				"\"to\": \""+this.arrivo+"\","+
				"\"slot\": "+slotListToJson(slotTratta)+" }";
				
	}
	public String toJson() {
		return "{"+
				"\"id\": "+this.id+","+
				"\"name\": \""+this.name+"\","+
				"\"type\": \""+this.type+"\","+
				"\"from\": \""+this.partenza+"\","+
				"\"to\": \""+this.arrivo+"\","+
				"\"slot\": "+this.slotListToJson(this.slotObjectList)+" }";
	}
	public static String tratteToJson(List<Tratta> tratte) {
		int i=1;
		String result="[";
		for(Tratta tratta:tratte) {
			result+=tratta;
			if(i++ != tratte.size()) {
		    	result+=" ,";
		    }
		}
		result+="]";
		return result;
	}
	public String slotListToJson(List<Slot> slotList) {
		int i=1;
		String result= "[";
		for (Slot slot : slotList) {
		    result+= Slot.slotToJson(slot);
		    if(i++ != slotList.size()) {
		    	result+=" ,";
		    }
		}
		result+="]";
		return result;
	}
	public List<Integer> getSlot() {
		return slot;
	}
	public void setSlot(List<Integer> slot) {
		this.slot = slot;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<Slot> getSlotObjectList() {
		return slotObjectList;
	}
	public void setSlotObjectList(List<Slot> slotObjectList) {
		this.slotObjectList = slotObjectList;
	}
}
