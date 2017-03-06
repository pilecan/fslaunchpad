package com.cfg.model;

public class ATCWaypoint {
	private String id;
	private String type;
	private String position;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}

	public String buildPoint(){
		return "<Placemark><name>"+id+"</name>\n"
				+ "<styleUrl>#m_ylw-pushpin</styleUrl>\n"
				+ "<Point><coordinates>"+position+"</coordinates></Point>\n"
				+ "</Placemark>\n";
	}
	
	
	@Override
	public String toString() {
		return "ATCWaypoint [id=" + id + ", type=" + type + ", position="
				+ position + "]";
	}

}
