package com.cfg.plan;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import com.geo.util.Geoinfo;

public class ReadFsxPlan extends DefaultHandler{

	private String tempVal;
	private String flightPlan;

	private List<ATCWaypoint> atcWaypoints;
	
	private ATCWaypoint atcWaypoint;
	
	private Stack currentElement = new Stack();

	public ReadFsxPlan(String flightPlan){
		this.flightPlan = flightPlan;
		parseDocument();
	}
	
	
	public static void main(String[] args) throws FileNotFoundException, NullPointerException, IOException{
		ReadFsxPlan readPlan = new ReadFsxPlan("C:\\Users\\Pierre\\Documents\\Flight Simulator X Files\\EGLLVTCC.pln");
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
		if(qName.equalsIgnoreCase("ATCWaypoint")) {
			atcWaypoint = new ATCWaypoint();
			atcWaypoint.setId(attributes.getValue("id"));
		}
	}
	

	public void characters(char[] ch, int start, int length) throws SAXException {
		tempVal = new String(ch,start,length);
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		currentElement.pop();
		if(qName.equalsIgnoreCase("ATCWaypoint")) {
			atcWaypoints.add(atcWaypoint);
			//System.out.println(atcWaypoint.toString());
		}else if (qName.equalsIgnoreCase("ATCWaypointType")) {
			atcWaypoint.setType(tempVal);
		}else if (qName.equalsIgnoreCase("WorldPosition")) {
			String coords[] = tempVal.split(",");
			atcWaypoint.setPosition(Geoinfo.convertDMSToDD(coords[1])+","+Geoinfo.convertDMSToDD(coords[0])+","+coords[2]);
		}
	}
	


	public List<ATCWaypoint> getAtcWaypoints() {
		return atcWaypoints;
	}

	public void setAtcWaypoints(List<ATCWaypoint> points) {
		this.atcWaypoints = points;
	}

}



