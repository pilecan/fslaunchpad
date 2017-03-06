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

public class ReadPlanGPlan extends DefaultHandler{

	private String tempVal;
	private String latitude;
	private String longitude;
	private String flightPlan;

	private List<ATCWaypoint> atcWaypoints;
	
	private ATCWaypoint atcWaypoint;
	
	private Stack currentElement = new Stack();

	public ReadPlanGPlan(String flightPlan){
		this.flightPlan = flightPlan;
		parseDocument();
	}
	
	
	public static void main(String[] args) throws FileNotFoundException, NullPointerException, IOException{
		ReadPlanGPlan readPlan = new ReadPlanGPlan("C:\\Users\\Pierre\\Documents\\Plan-G Files\\VFR Montreal-Pierre-Elliott-Trudea to Logan Intl.plg");
		
		System.out.println("points = "+readPlan.getAtcWaypoints().size());
		
		for(ATCWaypoint waypoint: readPlan.getAtcWaypoints()){
			System.out.println(waypoint.toString());
		}
		
	}
	
	
	private void parseDocument() {
		
		atcWaypoints = new ArrayList<ATCWaypoint>();
		
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			sp.parse(flightPlan, this);
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}
		
		
	}

	//Event Handlers
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		//reset
		currentElement.push(qName);
		tempVal = "";
		if(qName.equalsIgnoreCase("Waypoint")) {
			atcWaypoint = new ATCWaypoint();
		}
	}
	

	public void characters(char[] ch, int start, int length) throws SAXException {
		tempVal = new String(ch,start,length);
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		currentElement.pop();
		if(qName.equalsIgnoreCase("Waypoint")) {
			atcWaypoints.add(atcWaypoint);
			latitude = "";
			longitude = "";
			//System.out.println(atcWaypoint.toString());
		}else if (qName.equalsIgnoreCase("Identifier")) {
			atcWaypoint.setId(tempVal);;
		}else if (qName.equalsIgnoreCase("Latitude")) {
			latitude = tempVal;
			System.out.println(latitude);
		}else if (qName.equalsIgnoreCase("Longitude")) {
			longitude= tempVal;
			System.out.println(longitude);
			atcWaypoint.setPosition(longitude+","+latitude+","+0);
		}
	}
	
	public List<ATCWaypoint> getAtcWaypoints() {
		return atcWaypoints;
	}

	public void setAtcWaypoints(List<ATCWaypoint> points) {
		this.atcWaypoints = points;
	}

}



