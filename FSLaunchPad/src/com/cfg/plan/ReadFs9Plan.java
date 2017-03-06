package com.cfg.plan;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.cfg.model.ATCWaypoint;
import com.cfg.model.Area;
import com.cfg.model.Placemark;
import com.cfg.util.Util;
import com.geo.util.Geoinfo;

public class ReadFs9Plan extends DefaultHandler{
	private String flightPlan;
	private List<ATCWaypoint> atcWaypoints;
	
	private ATCWaypoint atcWaypoint;
	

	public ReadFs9Plan(String flightPlan) throws FileNotFoundException, NullPointerException, IOException {
		this.flightPlan = flightPlan;
		atcWaypoints = new ArrayList<>();
		readFlightPlan(flightPlan);
	}
	
	
	public static void main(String[] args) throws FileNotFoundException, NullPointerException, IOException{
		
		
		ReadFs9Plan readPlan = new ReadFs9Plan("C:\\Users\\Pierre\\Documents\\Flight Simulator Files\\VFR Ataturk to Orly.PLN");
		
		System.out.println("points = "+readPlan.getAtcWaypoints().size());
		
		for(ATCWaypoint waypoint: readPlan.getAtcWaypoints()){
			System.out.println(waypoint.toString());
		}
		
	}
	

	   public void readFlightPlan(String flightPlan) throws FileNotFoundException, IOException, NullPointerException{
		   	String[] data;
			  
			  try {
				  BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(flightPlan), "UTF8"));
					String line = br.readLine();
					
					while (line != null) {
						
						if (line.toLowerCase().contains("waypoint")){
							data = line.split(",");
							try {
								atcWaypoint = new ATCWaypoint();
								atcWaypoint.setId(data[1]);
								atcWaypoint.setPosition(Geoinfo.convertDMtoDD(data[data.length-3])+","+Geoinfo.convertDMtoDD(data[data.length-4])+",0");
								atcWaypoints.add(atcWaypoint);
							} catch (ArrayIndexOutOfBoundsException e) {
								//System.out.println(e);
							} catch (NullPointerException e1) {
								//System.out.println(e1);
								throw e1;
							}
							
						}
						
						line = br.readLine();
		 		
					
					}

					    
					    
				
				try {
						br.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					throw e;
				} catch (NullPointerException e) {
					throw e;
				} catch (IOException e) {
					throw e;
				} catch (Exception e){
					throw e;
				}

			}

	public List<ATCWaypoint> getAtcWaypoints() {
		return atcWaypoints;
	}

	public void setAtcWaypoints(List<ATCWaypoint> points) {
		this.atcWaypoints = points;
	}

}



