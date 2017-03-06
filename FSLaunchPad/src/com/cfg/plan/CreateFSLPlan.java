package com.cfg.plan;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JCheckBox;

import org.xml.sax.helpers.DefaultHandler;

import com.cfg.file.ManageConfigFile;
import com.cfg.file.ManageXMLFile;
import com.cfg.model.ATCWaypoint;
import com.cfg.model.Area;
import com.cfg.model.Placemark;
import com.cfg.util.Util;
import com.geo.util.Geoinfo;

public class CreateFSLPlan extends DefaultHandler{

	private Map<String, Placemark> fsxPlacemarks ;
	private Map<String, Placemark> addonPlacemarks ;
	private List <String> addonList;
	
	private int totalFsxPlacemarks;
	private int totalPlacemarks;
	private int totalAddonPlacemarks;

	private String flightPlan;
	private String kmlFlightPlanFile = "/data/fsl_flightplan.kml";

	private ManageXMLFile xmlfile;

	private ManageConfigFile configFile;
	
	private List<ATCWaypoint> points;
	
	private int nbAirports;
	
	private ATCWaypoint atcWaypoint;
	
	private double distanceRequired;
	
	private ReadFs9Plan fs9Plan;
	
	private ReadFsxPlan fsxPlan;
	
	private ReadPlanGPlan planGPlan;
	
	private Map<String, JCheckBox> boxChecked;
	
	private boolean isGoogleEarth;
	
	private boolean isDone;
	
	private int current;
	
	public CreateFSLPlan(String flightPlan,boolean isGoogleEarth, double distanceRequired, ManageXMLFile xmlfile, ManageConfigFile configFile,Map<String, JCheckBox> boxChecked) throws FileNotFoundException,NoPoints, NullPointerException, IOException{
		this.flightPlan = flightPlan;
		this.distanceRequired = distanceRequired/2;
		this.configFile = configFile;
		this.xmlfile = xmlfile;
		this.fsxPlacemarks = new HashMap<String, Placemark>();
		this.addonPlacemarks = new HashMap<String, Placemark>();
		this.boxChecked = boxChecked;
		this.isGoogleEarth = isGoogleEarth;
		this.isDone = false;
		this.nbAirports = xmlfile.getHashPlacemark().size();
		
		current = 0;
		
		Path currentRelativePath = Paths.get("");
   		kmlFlightPlanFile = currentRelativePath.toAbsolutePath().toString()+kmlFlightPlanFile.replace("\\", "/");
		
		String text = new String(Files.readAllBytes(Paths.get(flightPlan)), StandardCharsets.UTF_8);
		
		if (text.contains("[flightplan]")){
			fs9Plan = new ReadFs9Plan(flightPlan);
			points = fs9Plan.getAtcWaypoints();
		} else if (text.contains("<ATCWaypoint")){
			fsxPlan = new ReadFsxPlan(flightPlan);
			points = fsxPlan.getAtcWaypoints();
		} else if (text.contains("<Waypoint>")){
			planGPlan = new ReadPlanGPlan(flightPlan);
			points = planGPlan.getAtcWaypoints();
		}
		
		if (points.size() > 0){
			makeFlightPlan();
		} else {
			//System.out.println("points = "+points.size());
			throw  new NoPoints("Your Flight Plan don't return any Waypoints...");
		}
		
		
		
	}
	
	
	public void makeFlightPlan() throws FileNotFoundException, NullPointerException, IOException, NoPoints{
		
		if (points.size() == 0){
			throw  new NoPoints("Your Flight Plan don't return any Waypoints...");
		} 
		
		
		System.out.println(xmlfile.getHashPlacemark().size());
		System.out.println(configFile.getMapSource().size());
		System.out.println(points.size());
		
		
		
		long start = System.currentTimeMillis();
		
		//fsx
		for(Placemark placemark : xmlfile.getHashPlacemark().values()){
			Double[] dd1 = Geoinfo.convertDoubleLongLat(placemark.getCoordinates());
			current++;
			for(ATCWaypoint point : points){
				Double[] dd2 = Geoinfo.convertDoubleLongLat(point.getPosition());
				
				if (Geoinfo.distance(dd1[1], dd1[0], dd2[1], dd2[0], 'N') < distanceRequired){
					fsxPlacemarks.put(placemark.getName(),new Placemark(placemark));
				}
				
			}
		}
		
		// search addon
		String icao;
		Map <String,List<Area>> hashAreaList  = new HashMap<>();

		Map <String,Area> hashArea  = new TreeMap<>();
		for (Area area : configFile.getMapAllScenery().values()){
		//	hashIcao  = new HashSet<>();
			icao = Geoinfo.getIcao(area.getTitle());

			if (!"".equals(icao))
				for (Placemark placemark: fsxPlacemarks.values()){
					if (placemark.getName().equals(icao) ){
						
						addonPlacemarks.put(placemark.getName(),new Placemark(placemark));
						hashArea.put(placemark.getName(),area);
						//break;
					}
				}
				
		//	}
			

		}
		
		addonList = new ArrayList<String>();
		for (Area area : hashArea.values()){
			addonList.add(Util.formatAreaNum(area.getNum(),configFile.getMapAllScenery().size()+1)+"-"+area.getTitle());
		}
		
		Collections.sort(addonList);
		for(String str: addonList){
			System.out.println(str);
		}
		
		totalPlacemarks = xmlfile.getHashPlacemark().size();
		totalFsxPlacemarks = fsxPlacemarks.size();
		totalAddonPlacemarks = hashArea.size();

		System.out.println("Seconds = "+(System.currentTimeMillis()-start)/1000);
		System.out.println("Total = "+points.size() );
		System.out.println("Total addon = "+addonPlacemarks.size());
		System.out.println("Total hashArea addon = "+hashArea.size());

		if (isGoogleEarth){
			saveFlightPlan();
			xmlfile.launchGoogleEarth(new File(kmlFlightPlanFile));
		}
		
		isDone = true;

	}
	
	
	public boolean done(){
		return isDone;
	}
	
	
	private void readLocalArea(Area area, Map <String,List<Area>> hashAreaList){

		List <Area>areaList = new ArrayList<>();		
		Path dir = Paths.get("");
		if (area.getLocal().indexOf(":") == 1 ){
			dir = Paths.get(area.getLocal());
		} else {
			dir = Paths.get(configFile.getPrefs().getProperty("fsRoot")+"\\"+area.getLocal()+"\\scenery");
		}
		
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
		    for (Path file: stream) {
		    	String icao = Geoinfo.getIcao(file.getFileName().toString());
		    	
				if (hashAreaList.get(icao) == null){
					areaList = new ArrayList<>();
				}
				areaList.add(area);
				hashAreaList.put(icao,areaList);

		    }
		} catch (IOException | DirectoryIteratorException x) {
		    // IOException can never be thrown by the iteration.
		    // In this snippet, it can only be thrown by newDirectoryStream.
		    System.err.println(x);
		} catch (Exception e){
		    System.err.println(e);

		}
	}
	
	

	public void saveFlightPlan(){
	  	
 		
   		saveFlightPlan(kmlFlightPlanFile);
		
/*		File file = new File(kmlRelative);
		try {
			Runtime.getRuntime().exec(new String[] {
					"googleearth" ,
					file.getAbsolutePath()
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
*/		
	}
	

	
	public  synchronized void saveFlightPlan(String file){
		Writer writer = null;
				
 		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(file), "utf-8"));
		    
		    writer.write(createKMLHeader());

		    
		    for (ATCWaypoint atcWaypoint : points){
		    	writer.write(atcWaypoint.buildPoint());
		    }
		    
		    createPlan(writer);
		    writer.write("</Folder>");
		    
		    if (boxChecked.get("fsx").isSelected()){
			    writer.write("<Folder><name> FSX Airports ("+fsxPlacemarks.size()+") </name>");
			    
			    for(Placemark placemark:fsxPlacemarks.values()){
			    	writer.write(placemark.buildXML("fsx_airport"));
			    }
		    	
			    writer.write("</Folder>"); 
		    }
		    

		    if (boxChecked.get("addon").isSelected()){
			    writer.write("<Folder><name> Your Addon Airports ("+addonPlacemarks.size()+") </name>");
		    
			    for(Placemark placemark:addonPlacemarks.values()){
			    	writer.write(placemark.buildXML("addon_airport"));
			    }
			    writer.write("</Folder>");
		    }
		    
		    writer.write("</Document></kml>");
	   
		} catch (IOException ex) {
		  System.err.println(ex.getMessage());
		} finally {
		   try {writer.close();} catch (Exception ex) {}
		}			
    	
    }
  
	public void createPlan(Writer writer) throws IOException{
		writer.write("<Placemark> <styleUrl>#msn_ylw-pushpin</styleUrl><LineString><extrude>1</extrude><tessellate>1</tessellate><altitudeMode>clampToGround</altitudeMode><coordinates>"); 

	    for (ATCWaypoint atcWaypoint : points){
	    	writer.write(atcWaypoint.getPosition()+"\n");
	    }

	    writer.write("</coordinates></LineString></Placemark>");

	}
    
	public String createKMLHeader() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\">"	
				+ "<Document>"
				+ "<name>"+new File(flightPlan).getName()+"</name>"
				+ "<open>1</open><Style id=\"s_ylw-pushpin\"><IconStyle><color>b200ffff</color><Icon><href>http://maps.google.com/mapfiles/kml/paddle/ylw-stars.png</href></Icon><hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/></IconStyle><LabelStyle><color>7fffff55</color></LabelStyle><ListStyle><ItemIcon><href>http://maps.google.com/mapfiles/kml/paddle/ylw-stars-lv.png</href></ItemIcon></ListStyle></Style><StyleMap id=\"m_ylw-pushpin\"><Pair><key>normal</key><styleUrl>#s_ylw-pushpin</styleUrl></Pair><Pair><key>highlight</key><styleUrl>#s_ylw-pushpin_hl</styleUrl></Pair></StyleMap><StyleMap id=\"msn_ylw-pushpin\"><Pair><key>normal</key><styleUrl>#sn_ylw-pushpin</styleUrl></Pair><Pair><key>highlight</key><styleUrl>#sh_ylw-pushpin</styleUrl></Pair></StyleMap>"
				+ "<Style id=\"sh_ylw-pushpin\"><IconStyle><scale>1.2</scale></IconStyle><LineStyle><color>7f0000ff</color><width>3</width></LineStyle><PolyStyle><color>7f00ff55</color></PolyStyle></Style>"
				+ "<Style id=\"s_ylw-pushpin_hl\"><IconStyle><color>7fffffff</color><scale>1.18182</scale><Icon><href>http://maps.google.com/mapfiles/kml/paddle/ylw-stars.png</href></Icon><hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/></IconStyle><LabelStyle><color>7fffff55</color></LabelStyle><ListStyle><ItemIcon><href>http://maps.google.com/mapfiles/kml/paddle/ylw-stars-lv.png</href></ItemIcon></ListStyle></Style>"
				+ "<Style id=\"sn_ylw-pushpin\"><LineStyle><color>7f0000ff</color><width>4</width></LineStyle><PolyStyle><color>7f00ff55</color></PolyStyle></Style>"
				+ "<Style id=\"fsx_airport\"><IconStyle><Icon><href>http://maps.google.com/mapfiles/kml/shapes/airports.png</href></Icon></IconStyle></Style>"
				+ "<Style id=\"addon_airport\"><IconStyle><Icon><href>http://maps.google.com/mapfiles/kml/shapes/airports.png</href></Icon></IconStyle></Style>"
				+ "<Folder><name> Waypoints </name>";	
		}	


	public List<ATCWaypoint> getPoints() {
		return points;
	}

	public void setPoints(List<ATCWaypoint> points) {
		this.points = points;
	}


	public Map<String, Placemark> getAddonPlacemarks() {
		return addonPlacemarks;
	}


	public void setAddonPlacemarks(Map<String, Placemark> addonPlacemarks) {
		this.addonPlacemarks = addonPlacemarks;
	}
	
	public class NoPoints extends Exception {
	    public NoPoints(String message) {
	        super(message);
	    }
	}

	public List<String> getAddonList() {
		return addonList;
	}


	public void setAddonList(List<String> addonList) {
		this.addonList = addonList;
	}

	public int getTotalFsxPlacemarks() {
		return totalFsxPlacemarks;
	}

	public void setTotalFsxPlacemarks(int totalFsxPlacemarks) {
		this.totalFsxPlacemarks = totalFsxPlacemarks;
	}

	public int getTotalAddonPlacemarks() {
		return totalAddonPlacemarks;
	}

	public void setTotalAddonPlacemarks(int totalAddonPlacemarks) {
		this.totalAddonPlacemarks = totalAddonPlacemarks;
	}

	public int getTotalPlacemarks() {
		return totalPlacemarks;
	}

	public void setTotalPlacemarks(int totalPlacemarks) {
		this.totalPlacemarks = totalPlacemarks;
	}

	public int getNbAirports() {
		return nbAirports;
	}

	public void setNbAirports(int nbAirports) {
		this.nbAirports = nbAirports;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}
	
}



