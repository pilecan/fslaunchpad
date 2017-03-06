package com.cfg.util;

import java.util.Map;

import com.cfg.model.Area;

public class InspectArea {
	private static InspectArea instance = new InspectArea();

	private InspectArea() {
	}

	public static InspectArea getInstance() {
		return instance;
	}

	public boolean isInside(Area area, String value, boolean isSensitive) throws NullPointerException {
		if (area.getLayer() == null){
			return false;
		}
		String title = area.getTitle();
		String local = area.getLocal();
		
		if (!isSensitive){
			title = title.toLowerCase();
			local = local.toLowerCase();
			value = value.toLowerCase();
		}
		 
		return title.indexOf(value) != -1
			   || local.indexOf(value) != -1;
	}
	
	public boolean isFound(Map<String, Area> mapArea,String searchText){
		boolean isFound = false;
 	    for (Area area : mapArea.values()){
			try {
				
				if (searchText.equalsIgnoreCase(area.getTitle())){
					isFound = true;
					break;
				}
			} catch (NullPointerException e1) {
				System.err.println(area.toString());
				//e1.printStackTrace();
			}
 	    	
 	    }
 	    return isFound;
		
	}

}
