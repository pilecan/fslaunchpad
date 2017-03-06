package com.geo.util;

public class Geoinfo {
	public static double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
		  double theta = lon1 - lon2;
		  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		  dist = Math.acos(dist);
		  dist = rad2deg(dist);
		  dist = dist * 60 * 1.1515;
		  if (unit == 'K') {
		    dist = dist * 1.609344;
		  } else if (unit == 'N') {
		  	dist = dist * 0.8684;
		    }
		  return (dist);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts decimal degrees to radians             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double deg2rad(double deg) {
	  return (deg * Math.PI / 180.0);
	}

	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::  This function converts radians to decimal degrees             :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	private static double rad2deg(double rad) {
	  return (rad * 180 / Math.PI);
	}

	public static String convertDMSToDD(String input) {
		String direction = ""; 
		input = input.replace("°", "");
		input = input.replace("'", "");
		input = input.replace("\"", "");

	    String[] parts = input.split(" ");
	    
		String deg = parts[0];
		String min = parts[1];
		String sec = parts[2];

		if (deg.contains("E")){
			deg = deg.replace("E", "");
			direction = "E";
		} else if (deg.contains("W")){
			deg = deg.replace("W", "");
			direction = "W";
		} else if (deg.contains("N")){
			deg = deg.replace("N","");
			direction = "N";
		} else if (deg.contains("S")){
			deg = deg.replace("S", "");
			direction = "S";
		}
		
	    Double dd = Double.parseDouble(deg) + Double.parseDouble(min)/60 + Double.parseDouble(sec)/(60*60);
	    if (direction == "S" || direction == "W") {
	        dd = dd * -1;
	    } // Don't do anything for N or E
	    
       return dd.toString();
	    
	}	
	
	public static Double[] convertDoubleLongLat(String value){
		Double[] dd = new Double[3];
		String[] coords = value.split(",");
		
		dd[0] = Double.parseDouble(coords[0]);
		dd[1] = Double.parseDouble(coords[1]);
		dd[2] = Double.parseDouble(coords[2]);
		
		return dd;
	}
	
	
	public static String convertDMtoDD(String input) {
		String direction = ""; 
		input = input.trim().replace("*", "");
		input = input.replace("'", "");

	    String[] parts = input.split(" ");
	    
		String deg = parts[0];
		String min = parts[1];

		if (deg.contains("E")){
			deg = deg.replace("E", "");
			direction = "E";
		} else if (deg.contains("W")){
			deg = deg.replace("W", "");
			direction = "W";
		} else if (deg.contains("N")){
			deg = deg.replace("N","");
			direction = "N";
		} else if (deg.contains("S")){
			deg = deg.replace("S", "");
			direction = "S";
		}
		
	    Double dd = Double.parseDouble(deg) + Double.parseDouble(min)/60;
	    if (direction == "S" || direction == "W") {
	        dd = dd * -1;
	    } // Don't do anything for N or E
	    
       return dd.toString();
	    
	}	

	
	public static boolean isIcaoValid(String icao){
		boolean isValid = icao.length() == 4 && icao.toUpperCase().equals(icao);
		if (isValid) isValid = !icao.matches("[+-]?\\d*(\\.\\d+)?");
		return isValid;
		
	}
	
	public static String stripSearchText(String text){
		text = text.replace("."," ");
		text = text.replace("-"," ");
		text = text.replace("_"," ");
		text = text.replace("("," ");
		text = text.replace(")"," ");
		text = text.replace(","," ");
		
		return text;
		
	}
	
	public static String getIcao(String title){
		String[] elementTitle = stripSearchText(title).split(" ");

		String icao = "";
		for (int i=0; i<elementTitle.length;i++){
			//System.out.print(elementTitle[i]+" ");
			if (isIcaoValid(elementTitle[i])){
				icao = elementTitle[i];
				break;
			}
		}
		
		return icao;
	}
	
	
}
