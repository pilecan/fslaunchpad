package com.cfg.model;

import java.util.List;

public class Arealist {
	private String area;
	private List<String> list;
	
	
	
	/**
	 * @return the aera
	 */
	public String getArea() {
		return area;
	}
	public Arealist(String aera) {
		super();
		this.area = aera;
	}
	/**
	 * @param aera the aera to set
	 */
	public void setArea(String aera) {
		this.area = aera;
	}
	/**
	 * @return the list
	 */
	public List<String> getList() {
		return list;
	}
	/**
	 * @param list the list to set
	 */
	public void setList(List<String> list) {
		this.list = list;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Arealist [area=" + area + ", list=" + list + "]";
	}

}
