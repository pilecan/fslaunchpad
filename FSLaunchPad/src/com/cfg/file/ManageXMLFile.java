package com.cfg.file;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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

import com.cfg.model.Placemark;

public class ManageXMLFile extends DefaultHandler{

	private Map<String,Placemark> hashPlacemark;
	
	private String tempVal;
	private String tempDesc;
	private String kmlFile = "/data/resultSearch.kml";
	private String googleEarthExec = "C:/Program Files (x86)/Google/Google Earth/client/googleearth.exe";
	private String fileXML = "/data/airport3.xml";

	private List<Placemark> placemarks = new ArrayList<Placemark>();
	
	//to maintain context
	private Placemark placemark;
	
	private Stack currentElement = new Stack();

	public ManageXMLFile(){
		hashPlacemark = new TreeMap<String,Placemark>();
		parseDocument();
	}
	
	
	public List<Placemark>  seachAirport(String criteria, int searchNumber){
		placemarks.clear();
		criteria = criteria.toLowerCase();
		//general criteria
		if (searchNumber == 2 ){
			for(Placemark placemark: hashPlacemark.values()){
				if (placemark.toString().toLowerCase().contains(criteria)){
					placemarks.add(placemark);
				}
			}
		//iata
		} else if (searchNumber == 1 ){
				for(Placemark placemark: hashPlacemark.values()){
					if (placemark.toString().toLowerCase().contains("iata: "+criteria)){
						placemarks.add(placemark);
					}
				}
		//ICAOs
		} else if (searchNumber == 0) { 
		   String[] search = criteria.split(" ");
		   for(String key:search){
			   if (hashPlacemark.get(key) != null){
				   placemarks.add(hashPlacemark.get(key));
			   }
		   }
		   
		}
		
		return placemarks;
	}

	
	public void createKMLFile(){
  	
 		Path currentRelativePath = Paths.get("");
   		String kmlRelative = currentRelativePath.toAbsolutePath().toString()+kmlFile.replace("\\", "/");
		
		saveKMLFile(placemarks,kmlRelative);
		
		File file = new File(kmlRelative);
		
		launchGoogleEarth(file);
		//System.out.println(file.getAbsolutePath());

	}
	
	public void launchGoogleEarth(File file){
		try {
			Runtime.getRuntime().exec(new String[] {
					googleEarthExec ,
					file.getAbsolutePath()
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		
	}
	

	public void parseDocument() {
		
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			sp.parse((getClass().getResourceAsStream(fileXML)), this);
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
		if(qName.equalsIgnoreCase("Placemark")) {
			//create a new instance of employee
			placemark = new Placemark();
			//tempEmp.setType(attributes.getValue("type"));
		}
	}
	

	public void characters(char[] ch, int start, int length) throws SAXException {
		tempVal = new String(ch,start,length);
/*		if ("description".equals(currentElement.peek())){
			if (!tempVal.contains("BGL file")){
			tempDesc += tempVal;
			}
		} else {
			tempDesc = "";
		}
*/	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		currentElement.pop();
		
	 // System.out.println(qName);
		
		if(qName.equalsIgnoreCase("Placemark")) {
			hashPlacemark.put(placemark.getName().split(" ")[0].toLowerCase(), placemark);
		}else if (qName.equalsIgnoreCase("name")) {
			placemark.setName(tempVal);
		}else if (qName.equalsIgnoreCase("description")) {
			placemark.setDescription(tempVal.replaceAll("(\r\n|\n\r|\r|\n)", ""));
		}else if (qName.equalsIgnoreCase("styleUrl")) {
			placemark.setStyleUrl(tempVal);
		}else if (qName.equalsIgnoreCase("Point")) {
			placemark.setPoint(tempVal);
		}else if (qName.equalsIgnoreCase("coordinates")) {
			placemark.setCoordinates(tempVal);
		}
	}
	
	public static void main(String[] args){
		ManageXMLFile spe = new ManageXMLFile();
		spe.parseDocument();
	}
	
    public  synchronized void saveKMLFile(List<Placemark> placemarks, String kmlRelative){
		Writer writer = null;
						
 		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(kmlRelative), "utf-8"));
		    
		    writer.write(createKMLHeader(placemarks));
		    
		    for(Placemark placemark:placemarks){
		    	writer.write(placemark.buildXML("fsx_airport"));
		    }
		    
		    writer.write("</Folder></Document></kml>");
	   
		} catch (IOException ex) {
		  System.err.println(ex.getMessage());
		} finally {
		   try {writer.close();} catch (Exception ex) {}
		}			
    	
    }
    
    
	public String createKMLHeader(List<Placemark> placemarks) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<kml xmlns=\"http://earth.google.com/kml/2.0\">"
				+ "<Document><Style id=\"fsx_airport\"><IconStyle><Icon><href>http://maps.google.com/mapfiles/kml/pal2/icon48.png</href></Icon></IconStyle></Style><Folder>"
				+ "<name>LaunchScenery GoogleEarth Search</name>"
				+ "<description>"+placemarks.size()+" airport(s) found</description>";
	}	

	/**
	 * @return the hashPlacemark
	 */
	public Map<String, Placemark> getHashPlacemark() {
		return hashPlacemark;
	}

	/**
	 * @param hashPlacemark the hashPlacemark to set
	 */
	public void setHashPlacemark(Map<String, Placemark> hashPlacemark) {
		this.hashPlacemark = hashPlacemark;
	}
	
}



