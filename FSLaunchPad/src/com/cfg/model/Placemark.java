package com.cfg.model;

import com.cfg.util.Util;
import com.geo.util.Geoinfo;


public class Placemark {
	private String name;
	private String description;
	private String styleUrl;
	private String point;
	private String coordinates;
	
	public Placemark() {
	}

	public Placemark(Placemark placemark) {
		this.name = placemark.getName();
		this.description = placemark.getDescription();
		this.styleUrl = placemark.getStyleUrl();
		this.point = placemark.getPoint();
		this.coordinates = placemark.getCoordinates();
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the styleUrl
	 */
	public String getStyleUrl() {
		return styleUrl;
	}
	/**
	 * @param styleUrl the styleUrl to set
	 */
	public void setStyleUrl(String styleUrl) {
		this.styleUrl = styleUrl;
	}
	/**
	 * @return the point
	 */
	public String getPoint() {
		return point;
	}
	/**
	 * @param point the point to set
	 */
	public void setPoint(String point) {
		this.point = point;
	}
	/**
	 * @return the coordinates
	 */
	public String getCoordinates() {
		return coordinates;
	}
	/**
	 * @param coordinates the coordinates to set
	 */
	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Placemark [name=" + name + ", description=" + description
				+ ", styleUrl=" + styleUrl + ", Point=" + point
				+ ", coordinates=" + coordinates + "]";
	}
	
	public String buildXML(String styleUrl){
		String airportName = "";
		String country= "";

		String[] descs = description.split("\\|");
		description = "";
		for(String desc:descs){
			if ("".equals(desc)){
				description += "<div style='padding: 5px;'></div>";
			} else {

				if (desc.contains("Country:") && !desc.contains(name)){
					country = desc.substring(desc.indexOf(":")+1);
				}
				if (desc.contains("Airport Name:") && !desc.contains(name)){
					airportName = desc.substring(desc.indexOf(":")+1);
					desc +=  Util.aviationWeather.replace("xxxx", name);
					desc = desc.replace(airportName, Util.createHref(airportName,name+" "+airportName+" wikipedia", 0));
				} else if (desc.contains("BGL file:")) {
					desc += "<br><br>Other Searches: "+Util.createHref("(freewarescenery.com)",country.trim().toLowerCase(), 7);
				}
				
				description += "<div style=\"width: 300px; font-size: 12px;\">"+desc+"</div>";
			}
		}

		return "<Placemark><name>"+name+airportName+"</name>\n"
				+ "<description><![CDATA["+description+"]]></description>\n"
				+ "<styleUrl>"+styleUrl+"</styleUrl>\n"
				+ "<Point><coordinates>"+coordinates+"</coordinates></Point>\n"
				+ "</Placemark>\n";
	}

	
	public String getIata(){
		String iata = "";
		String[] descs = description.split("\\+");
		description = "";
		for(String desc:descs){
			if (desc.contains("IATA")){
				iata = desc.substring(6, desc.length());
				break;
			}
		}
		return iata;
	}
	
	
}
