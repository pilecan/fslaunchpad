package com.cfg.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Properties;
import java.util.TreeMap;

import javax.swing.DefaultListModel;

import com.cfg.common.Info;
import com.cfg.model.Area;
import com.cfg.model.LaunchLine;
import com.cfg.net.ManageStatus;
import com.cfg.prog.FSLaunchPad;
import com.cfg.util.FileUtil;
import com.cfg.util.InspectArea;
import com.cfg.util.Util;
public class ManageConfigFile implements Info {
	/**
	 * @param args
	 */
	private String version = "2.35";
	
	private String addonScenery = "C:\\Program Files (x86)\\Microsoft Games\\Microsoft Flight Simulator X\\Addon Scenery";
	private String fsRoot = "C:\\Program Files (x86)\\Microsoft Games\\Microsoft Flight Simulator X\\";
	private String fsProgram = "fsx.exe";
	private String quitAfter = "No";
	private String sceneryRoot = "C:/ProgramData/Microsoft/FSX/";
	private String applicationRoot = "";
    private String sceneryFileName;
    
    private final static String ADDON_SCENERY = "Addon Scenery"; 
	
	private Map<String, List<Object>> mapListArea;
	private String areaName;

    private  Area area = null;
    private  Map<String,Area> mapDestination;
    private  Map<String,Area> mapSource;
    private  NavigableMap<String,Area> mapAllScenery;
    private  Map<String,Area> mapLocalScenery;
    private  Map<String, Area> mapWork;
    
    private Area areaRoot = null;
    
    private File sceneryWorkFile;
    private File sceneryOriginalFile;
    private File sceneryFile;
    private File sceneryBakFile;
    private File sceneryAreaFile;
    private File fslFile;
   
    
    
    private boolean debug;
    
    private Properties prop;
    private Properties prefs;

    public static void main(String[] args) throws IOException  {
    	//ManageConfigFile configFile = new ManageConfigFile();
    	//new FSLauncpad();
	}

    /***
     * 
     * 
     *
     */
    public ManageConfigFile (){
       	mapListArea = new TreeMap<>();
    	mapDestination  = new TreeMap<>();
    	mapAllScenery  = new TreeMap<>();
    	mapLocalScenery = new HashMap<>();
    	mapSource 	= new TreeMap<>();
    	mapWork = new TreeMap<>();
    	prop = new Properties();
    	prefs = new Properties();
	    
    }
    
    public void init(){
     	
 	    readPrefProperties();
    	readLangueProperties(prefs.getProperty("langue"));

    	sceneryRoot = prefs.getProperty("sceneryRoot");
    	fsProgram = prefs.getProperty("fsProgram");
    	sceneryFileName = prefs.getProperty("sceneryFileName");
    	fsRoot = prefs.getProperty("fsRoot");
    	addonScenery = prefs.getProperty("addonScenery");
    	
    	sceneryFile 	= new File(sceneryRoot+"\\"+sceneryFileName);
    	sceneryOriginalFile 	= new File(sceneryRoot+"\\"+sceneryOriginal);
    	sceneryBakFile 	= new File(sceneryRoot+"\\"+sceneryBak);
    	sceneryWorkFile = new File(sceneryRoot+"\\"+sceneryWork);
    	fslFile = new File(fsl);

    	Path currentRelativePath = Paths.get("");
   		String areaSceneryPath = currentRelativePath.toAbsolutePath().toString()+"\\data\\"+sceneryArea;
   		
   		applicationRoot = currentRelativePath.toAbsolutePath().toString();

    	sceneryAreaFile = new File(areaSceneryPath);
    	areaName = "";
    	
   }

    /***
     * 
     * 
     * @throws Exception 
     */
    public void process() throws FileNotFoundException, IOException{
    	mapDestination.clear();
    	mapSource.clear();
    	
       	valideConfig();
            	
		readAllScenery(sceneryFile);
    	debug = !fslFile.exists();
		
    	if (sceneryAreaFile.exists()){
    		areaName = readMapListArea(sceneryAreaFile);
    		
    		if (!"".equals(areaName)){
    			areaName = initMapsWithSenerySelected(areaName);
    			if ("".equals(areaName)){//au cas que le tag areaNam n'existe pas dans le fichier
        			initMap();
    			}
    		} else {
    			initMap();
    		}
    		
			
		} else {
			copyFile(sceneryFile, sceneryWorkFile);
			copyFile(sceneryFile, sceneryBakFile);
			initMap();
		}
    }
    
    private void valideConfig() throws FileNotFoundException{
    	String message = ""; 
    	if (!new File(fsRoot+"\\"+fsProgram).exists()){
    		message = "(FS Program) ";
    	}

    	if (!new File(addonScenery).exists()){
    		message += "(Addon Scenery path) ";
    	}
    	
       	if (!new File(sceneryRoot+"\\"+sceneryFileName).exists()){
    		message += "(Scenery.cfg) ";
    		
    	}
    	if (!"".equals(message)){
    		throw new FileNotFoundException(message+".<br><br>" );
    	}
    }
    
    
    /***
     * 
     * @throws IOException
     */
    public void initMap(){
    	mapDestination.clear();
    	mapSource.clear();
    	initFromMapAll();
    //	System.out.println(mapSource.size());
		initMaps(mapAllScenery.size());
    }	
   
    public String addArea( File[] files){
    	mapDestination.clear();
    	mapSource.clear();
    	initFromMapAll();
    	String errorMessages = "";
    	String addMessages = "";
    	int cptError = 0;
    	for (File file : files){
           	String errorMessage = null;
    		String directory = file.toString();
			directory = directory.substring(directory.lastIndexOf("\\")).replace("\\", "");
			File f = new File(file + "\\scenery");
			if (!(f.exists() && f.isDirectory())) {
				errorMessage = "- "+directory+" "+prop.getProperty("scen.add.err1");
			}
			if (errorMessage == null && InspectArea.getInstance().isFound(getMapAllScenery(), directory)) {
				errorMessage = "- "+directory+" "+ prop.getProperty("scen.add.err2");
			}
			if (errorMessage != null) {
				errorMessages += errorMessage+"<br>";
				cptError++;
			} else {
				addMessages += "- "+directory+" "+prop.getProperty("scen.added")+"<br>";
				Area newArea = new Area("");
				newArea.setTitle(directory);
				newArea.setActive("TRUE");
				newArea.setRequired("FALSE");
				newArea.setLocal(file.toString().replace(getFsRoot()+"\\", ""));

				//Integer num = Integer.valueOf(getMapAllScenery().lastKey()) + 1;
				Integer num = getMapAllScenery().size() + 1;
				newArea.setLayer(num.toString());
				newArea.setNum(num.toString());
		    	mapSource.put(directory,newArea);
		    	mapAllScenery.put(newArea.getNum(),newArea);
    	  }
    	}
    	String line = "----------------------------------------------<br>";
    	String message = "<html>"+errorMessages+line+addMessages+line+((files.length)-cptError)+" "+prop.getProperty("scen.add.added")+"</html>";

    	if (((files.length+1)-cptError) > 0){
    	  initMaps(mapAllScenery.size());
    	  saveAllScenery();
    	}
    	
    	
    	return message;
    }	

 
    public void deleteListAreaFile(){
    	try{
    		if(sceneryAreaFile.delete()){
    			mapListArea.clear();
    		//	System.out.println(sceneryAreaFile.getName() + " is deleted!");
    		}else{
    			System.err.println("Delete operation is failed.");
    		}
 
    	}catch(Exception e){
    		System.err.println(e);
    	}
    }

    
    /**
     * deleteScenery
     * 
     * 
     * @param title
     */
	public void deleteScenery(String title) {
		Area areaDeleted = getGoodArea(title);

		if (mapLocalScenery.get(areaDeleted.getLocalkey()) != null) {
			mapLocalScenery.remove(areaDeleted.getLocalkey());
		}

		mapWork.clear();

		mapAllScenery.remove(areaDeleted.getNum());

		Map<Integer, Area> mapWorks = Util.createMapWork(mapAllScenery);

		Util.adjustMaplistArea(areaDeleted.getLocalkey(), mapListArea);
		saveMapListArea(mapListArea);
		
		System.out.println(mapAllScenery.size() + " ---- "+ mapWorks.size());

		saveAllScenery();
		readMapListArea(sceneryAreaFile);

		mapDestination.clear();
		mapSource.clear();
		initFromMapAll();

		initMaps(mapAllScenery.size());
	}	   

	   
	   /**
	    * saveModifyScenery
	    * 
	    * @param title
	    * @param area
	    */
	   public void saveModifyScenery( Area area){
	    	mapAllScenery.remove(area.getNum());
	    	mapAllScenery.put(area.getNum(),area);
	    	saveAllScenery();
	   }
	   

	   /**
	    * moveSceneryOrder
	    * 
	    * @param areaModel
	    */
	   public void reorderScenery(DefaultListModel<String> areaModel){
           int total = areaModel.size();
           mapAllScenery.clear();
           
           String numstr;
           for (int i = 0; i < total-1; i++) {
        	  numstr = Util.addZero(total-(i+1));
  
        	  //numstr = Util.formatAreaNum(i+1, total);
        	 // System.out.println(numstr+"-"+areaModel.get(i));
        	  mapWork.get(areaModel.get(i)).setNums(numstr);
           	 mapAllScenery.put(numstr,mapWork.get(areaModel.get(i)));

 		   }
           saveAllScenery();
           saveMapListArea(mapListArea);
	   }
	   
	   /**
	    * moveSceneryLocation
	    * 
	    * @param title
	    * @param newLocation
	 * @throws IOException 
	    */
	   public void moveSceneryLocation(String title,String newLocation) throws Exception{
		   	String oldLocation = getGoodArea(title).getLocal(); 
		   	if (!new File(oldLocation).isDirectory()){
		   		oldLocation = getFsRoot()+"\\"+oldLocation.replace("/", "\\")+"\\";
	    	}

		   File theNewLocation = new File(newLocation+"\\"+new File(oldLocation).getName());
		   
		   Path pathsource = Paths.get(oldLocation); 
		   Path pathdest = Paths.get(theNewLocation.toString()); 
		   
//		   System.out.println(pathsource+" - "+pathdest);
		   
		   try {
			   Files.move(pathsource, pathdest,StandardCopyOption.REPLACE_EXISTING);
			   newLocation = pathdest.toString().replace(getFsRoot()+"\\","");
			   getGoodArea(title).setLocal(pathdest.toString());
			   mapAllScenery.get(getGoodArea(title).getNum()).setLocal(newLocation);
			   saveAllScenery();
		   } catch (Exception e) {
			   throw e;
		   }
	   }
    
    /**
     * 
     *
     */
	   public void initMaps(int len){
    	//int len = mapAllScenery.size();
		Map<Integer, Area> mapWorks = Util.createMapWork(mapAllScenery);
		int cpt = 0;
		

        for (Area area : mapWorks.values()){
    		System.out.println(area.toString());
    		cpt++;
    	   	int i = (len - cpt)+1;
        	
    		String formatted = String.format("%04d", i);
			try {
				//System.out.println(aera.getArea()+" "+aera.getTitle());
				if (mapDestination.get(area.getTitle())!= null){
					mapDestination.remove(area.getTitle());
					mapDestination.put(formatted+"-"+area.getTitle(), area);
				} else if (mapSource.get(area.getTitle())!= null){
					//System.out.println("Util.formatAreaNum(area.getNum(),len+2) = "+Util.formatAreaNum(area.getNum(),len+2));
					
					String str = Util.formatAreaNum(area.getNum(),len);
					if (str.indexOf("0000") != -1){
						System.out.println(str+"-"+area.getTitle() +" - >"+len);
					}
					mapSource.remove(area.getTitle());
					mapSource.put(formatted+"-"+area.getTitle(), area);
				} else{
					System.out.println("Il y a rien des deux----------------------------");
				}
				//System.out.println(area.toString());
			} catch (NullPointerException e) {
				//System.out.println();
				e.printStackTrace();
				continue;
			}
         }

		mapDestination.put((mapAllScenery.size()+1)+"-"+areaRoot.getTitle(), areaRoot);
		
    }
	   public void initMapAlphabetic(boolean isAlpha){
			mapWork.clear();
			
			for (Area source : mapSource.values()) {
				if (isAlpha){
				    mapWork.put(source.getTitle(), source);
	                //selected[i] = Util.formatAreaNum(source.getNum(), getMapAllScenery().size()+1) +"-"+ selected[i];
				} else {
					mapWork.put(Util.formatAreaNum(source.getNum(),getMapAllScenery().size()+1)+"-"+source.getTitle(), source);
				}	
			}

			mapSource.clear();
			mapSource = new HashMap<>(mapWork);
	    }

	   /**
	    * 
	    * @param areaName
	    */
   public String initMapsWithSenerySelected(String areaName){
	   
	   List<Object>listArea =  mapListArea.get(areaName);
	   
	   if (listArea == null){
		   return "";
	   }
	   
	   int len = mapAllScenery.size();
   		mapDestination.clear();
   		mapSource.clear();
   		
   		for (Area area : mapAllScenery.values()){
   			if (!listArea.contains(area.getLocalkey())){
   				mapSource.put(Util.formatAreaNum(area.getNum(),len+1)+"-"+area.getTitle(), area );
   			}
   		}
   	
   		for (Object localKey: listArea){
   			try {
				mapDestination.put(Util.formatAreaNum(mapLocalScenery.get(localKey).getNum(),len+1)+"-"+mapLocalScenery.get(localKey).getTitle(), mapLocalScenery.get(localKey) );
			} catch (NullPointerException e) {
				System.err.println("localKey ("+localKey+") not valide. "+e);
			}
   		}
		mapDestination.put("0"+(mapAllScenery.size()+1)+"-"+areaRoot.getTitle(), areaRoot);
		return areaName;
   }
   
   
	private void initFromMapAll() {
		boolean isMandatory = true;
		
		Map<Integer, Area> mapWork = Util.createMapWork(mapAllScenery);
		
		for (Area areaAll : mapWork.values()) {
			try {
				if (areaAll.getTitle() == null){
					if (areaAll != null && areaAll.getLocal() != null)
					{
						String tile = areaAll.getLocal().substring(areaAll.getLocal().lastIndexOf("\\")+1);
						areaAll.setTitle(tile);
					} else {
						continue;
					}
				}
				if (isMandatory || "TRUE".equals(areaAll.getRequired().toUpperCase())) {
					mapDestination.put(areaAll.getTitle(), areaAll);
				} else {
					mapSource.put(areaAll.getTitle(), areaAll);
				}
				
    // System.out.println(areaAll.toString());
				if (isMandatory && ADDON_SCENERY.toUpperCase().equals(areaAll.getLocal().toUpperCase()) 
					|| getPrefs().getProperty("addonScenery").toUpperCase().equals(areaAll.getLocal().toUpperCase())) {
					isMandatory = false;
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		
	}  
      
	
    /***
     * 
     * @param file
     */
    public void readAllScenery(File file) throws FileNotFoundException, IOException, NullPointerException{
    	
	  int ind = 1;
	  try {
		  BufferedReader br = new BufferedReader(new InputStreamReader(
                  new FileInputStream(file), "UTF8"));
			String line = br.readLine();
			boolean isHaveAddonScenery = false;
			area = null;
			areaRoot = new Area(line);
			
			while (line != null && !"[AREA.001]".equals(line.toUpperCase().trim())) {
			    //System.out.println(line);	
				line = br.readLine();
				if (line.contains("Title")){
			        areaRoot.setTitle(line.substring(line.indexOf("=")+1));
				} else if (line.contains("Description")){
			        areaRoot.setDescription(line.substring(line.indexOf("=")+1));
				} else if (line.contains("Cache_Size")){
			        areaRoot.setCashSize(line.substring(line.indexOf("=")+1));
				} else if (line.contains("Clean_on_Exit")){
			        areaRoot.setCleanOnExit(line.substring(line.indexOf("=")+1));
				}
 			}

	        while (line != null) {
			    //System.out.println(line);
			    if (line != null && !"".equals(line.trim())){
		    		
			    	if (line.toUpperCase().indexOf("[AREA") == 0){
			    		if (area != null && area.getLocal() != null){
			    			if (!isHaveAddonScenery){
				    			isHaveAddonScenery = area.getLocal().toLowerCase().equals(ADDON_SCENERY.toLowerCase()) ;
			    			}
			    			area.setNums(Util.addZero(ind++));
		    				mapAllScenery.put(area.getNum(), area);
		    				mapLocalScenery.put(area.getLocalkey(), area);
			    		}
			        	area = new Area(Util.addZero(ind));
			        	//area = new Area();
			    	}
			    	setArea(area,line.trim());
			    }
			    line = br.readLine();
			}

	        if (area != null && area.getLocal() != null){
				mapAllScenery.put(area.getNum(), area);
				mapLocalScenery.put(area.getLocalkey(), area);
	        }
	        
	        if (!isHaveAddonScenery){
	        	area = new Area();
	        	mapAllScenery = addAddonScenery(mapAllScenery, area);
				mapLocalScenery.put(area.getLocalkey(),area);
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
    /**
     * 
     * @param mapScenery
     * @return
     */
    private NavigableMap<String, Area> addAddonScenery(NavigableMap <String, Area>mapScenery,Area addonArea){
    	NavigableMap <String, Area>newMapScenery = new TreeMap<>();

    	boolean isBeforeFound = false;
    	String scenery = "scenery";
    	
    	for (int i = 1; i < mapScenery.size(); i++) {
    		Area area = new Area(mapScenery.get(Util.formatAreaNum(Integer.toString(i), mapScenery.size()+1)));

    		//System.out.println(area.getLocal());
    		
    		if (!isBeforeFound && area.getLocal().toLowerCase().contains(scenery) && area.getLocal().toLowerCase().substring(0,scenery.length()).equals(scenery)){
				addonArea.setNums(Util.formatAreaNum(Integer.toString(i), mapScenery.size()+1));
				addonArea.setTitle(ADDON_SCENERY);
				addonArea.setLocal(ADDON_SCENERY);
				addonArea.setRequired("TRUE");
				addonArea.setActive("TRUE");
				newMapScenery.put(addonArea.getNum(),addonArea);
				isBeforeFound = true;
    		}

    		if (isBeforeFound){
    			String num = Util.formatAreaNum(Integer.toString(i+2), mapScenery.size()+2);
    			area.setNums(num);
    			newMapScenery.put(num,area);
    		} else {
    			newMapScenery.put(Util.formatAreaNum(Integer.toString(i), mapScenery.size()+1),area);
    		}
    	}
		return newMapScenery;
    }

    /**
     * 
     * @param area
     * @param line
     */
    private void setArea(Area area, String line){
        if (area.getNum().indexOf("General") == -1){
        	if (line.toUpperCase().indexOf("TITLE") == 0){
        		area.setTitle(line.substring(line.indexOf("=")+1));
        	} else if (line.toUpperCase().indexOf("LOCAL") == 0){
        		area.setLocal(line.substring(line.indexOf("=")+1));
        	} else if (line.toUpperCase().indexOf("LAYER") != -1){
        		//String str = new Integer(line.substring(line.indexOf("=")+1)).toString();
        		//System.out.println("-------------------------------->"+str);
        		
        		area.setLayer(line.substring(line.indexOf("=")+1));
        	} else if (line.toUpperCase().indexOf("REQUIRED") != -1){
        		area.setRequired(line.substring(line.indexOf("=")+1));
        	} else if (line.toUpperCase().indexOf("TEXTURE") != -1){
        		area.setTexture(line.substring(line.indexOf("=")+1));
        	} else if (line.toUpperCase().indexOf("ACTIVE") != -1){
        		area.setActive(line.substring(line.indexOf("=")+1));
           	} else if (line.toUpperCase().indexOf("EXCLUDE") != -1){
        		area.setExclude(line.substring(line.indexOf("=")+1));
           	} else if (line.toUpperCase().indexOf("REMOTE") != -1){
        		area.setRemote(line.substring(line.indexOf("=")+1));
        	}
        }
  	
    }
	
	/**
	 * 
	 * @param sourceFile
	 * @param destFile
	 * @throws IOException
	 */
	public synchronized void copyFile(File sourceFile, File destFile) {

	    FileChannel source = null;
	    FileChannel destination = null;

	    try {
		    if(!destFile.exists()) {
		        destFile.createNewFile();
		    }
	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();
	        destination.transferFrom(source, 0, source.size());
	    } catch (IOException | NullPointerException e){
	    	System.err.println(e);
			 ManageStatus.sendError("message/", prefs.getProperty("fsProgram").replace(".", "_")+":"+(e.getMessage().replaceAll(" ", "%20")));

	    }

	    finally {
	        try {
				if(source != null) {
				    source.close();
				}
				if(destination != null) {
				    destination.close();
				}
			} catch (IOException | NullPointerException e ) {
				 ManageStatus.sendError("message/", prefs.getProperty("fsProgram").replace(".", "_")+":"+(e.getMessage().replaceAll(" ", "%20")));

			}
	    }
	}
	
	/**
	 * 
	 * @param list
	 */
    public  synchronized void saveScenery(List<Object> list){
		Writer writer = null;
		
 		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(sceneryRoot+"\\"+sceneryFileName), "utf-8"));
		    
		    writer.write(areaRoot.buildGeneral()+"\n");
		    
			for (int i = 1; i < list.size(); i++) {
				writer.write(getGoodArea((list.get(i))).buildArea(i)+"\n");
				//System.out.println(getGoodArea((list.get(i))).buildArea(i)+"\n");
			}
		} catch (IOException | NullPointerException ex) {
		  //System.err.println(ex.getMessage());
			 ManageStatus.sendError("message/", prefs.getProperty("fsProgram").replace(".", "_")+":"+(ex.getMessage().replaceAll(" ", "%20")));

		} finally {
		   try {writer.close();} catch (Exception ex) {}
		}			
    	
    }
 
    
    /**
     * 
     * 
     */
    public  synchronized void saveAllScenery(){
		Writer writer = null;
		Map<Integer, Area> mapWorks = Util.createMapWork(mapAllScenery);
		
 		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(sceneryRoot+"\\"+sceneryFileName), "utf-8"));
		    
		    writer.write(areaRoot.buildGeneral()+"\n");
		    
		    for(Area area: mapWorks.values()){
		    	writer.write(area.buildArea()+"\n");
		    	//System.out.println(area.toString());
		    }
		    
	} catch (IOException | NullPointerException ex ) {
		  System.err.println(ex.getMessage());
			 ManageStatus.sendError("message/", prefs.getProperty("fsProgram").replace(".", "_")+":"+(ex.getMessage().replaceAll(" ", "%20")));
		} finally {
		   try {writer.close();} catch (Exception ex) {}
		}			
    	
    }
   
    
    
    /**
     * 
     * @param list
     */
    public void launchFlightSimulator(List<Object> list) throws Exception{
   		copyFile(sceneryFile, sceneryOriginalFile);

    	try {
			saveAreaDatList(list);
			Collections.reverse(list);
			saveScenery(list);	
			
			try {
			FileUtil.setCurrentDirectory(fsRoot);
				
				Runtime rt = Runtime.getRuntime();
				Process ps = rt.exec(fsRoot+"\\"+fsProgram, null, new File(fsRoot));
				
				
//				 Process p = Runtime.getRuntime().exec("cmd /c "+ new File(fsRoot+"\\"+fsProgram).getAbsolutePath());
//				  ps.waitFor();
				
				Util.pause((Long.parseLong(prefs.getProperty("elapse"))*1000));
				
			} catch (IOException e1) {
				 ManageStatus.sendError("message/", prefs.getProperty("fsProgram").replace(".", "_")+":"+(e1.getMessage().replaceAll(" ", "%20")));
				throw e1;
			} finally{
				FileUtil.setCurrentDirectory(applicationRoot);
				copyFile(sceneryOriginalFile, sceneryFile);
			}
			
		} catch (Exception e) {
			 ManageStatus.sendError("message/", prefs.getProperty("fsProgram").replace(".", "_")+":"+(e.getMessage().replaceAll(" ", "%20")));
			throw e;
		} finally{
			copyFile(sceneryOriginalFile, sceneryFile);
		}

    }
    
    public void openExplorer(String pathScenery){
    	if (!new File(pathScenery).isDirectory()){
    		pathScenery = getFsRoot()+"\\"+pathScenery.replace("/", "\\")+"\\";
    	}
    	
		 try {
			Runtime.getRuntime().exec("explorer /select,  " + pathScenery);
		 } catch (IOException e) {
				//System.err.println(e);
		 } 
    	
    }
 
	public  void readLangueProperties( String lang){
	     try {
			   InputStream is = FSLaunchPad.class.getResourceAsStream("/ressources/messages_"+lang+".properties");
			   prop.load(is);
				try {
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
	     } catch (IOException e) { 
	    	 System.err.println ("Properties error "+e);
	     } catch (NullPointerException e) { 
	         System.err.println ("Properties null "+e);
	     }
	     
	}
  
	public  void readPrefProperties(){
    	
	     Path currentRelativePath = Paths.get("");
	     String file = currentRelativePath.toAbsolutePath().toString()+"\\data\\preferences.properties";
	     try {
	    	 BufferedReader is = new BufferedReader(new InputStreamReader(
                     new FileInputStream(file), "UTF8"));
			   prefs.load(is);
				try {
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
	     } catch (IOException e) { 
	         System.err.println ("Properties error "+e);
	     } catch (NullPointerException e) { 
	         System.err.println ("Properties null "+e);
	     }
	     
	     if (prefs.getProperty("areaName") == null){
	    	 prefs.setProperty("areaName", "none");
	     }
	     
	}
   
	   public void savePrefProperties(){
		   Writer out = null;
	   		Path currentRelativePath = Paths.get("");
	   		String file = currentRelativePath.toAbsolutePath().toString()+"\\data\\preferences.properties";
		    File f = new File(file);
	   		
	 		try {
		        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "utf-8"));
		        prefs.store(out, "FsLaunchpad User Preferences");

			} catch (IOException ex) {
				 ManageStatus.sendError("message/", prefs.getProperty("fsProgram").replace(".", "_")+":"+(ex.getMessage().replaceAll(" ", "%20")));
				  System.err.println(ex.getMessage());
			} finally {
			   try {out.close();} catch (Exception ex) {}
			}			
	    }

    /**
     * 
     * @param list
     */
    public static void saveLaunchList(Map<String,LaunchLine> launchMap ){
   		Writer writer = null;
   		Path currentRelativePath = Paths.get("");
   		String launchFile = currentRelativePath.toAbsolutePath().toString()+"\\data\\launchInfo.dat";
//   		System.out.println("Current relative path is: " + launchFile);
   		
 		try {
		    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(launchFile), "utf-8"));
			for(LaunchLine line:launchMap.values()){
				writer.write(line.getPath()+"\n");
			}
		} catch (IOException ex) {
			  System.err.println(ex.getMessage());
				 ManageStatus.sendError("message/", ex.getMessage().replaceAll(" ", "%20"));
			} finally {
			   try {writer.close();} catch (Exception ex) {}
			}			
		    
    }
  
    public static  List<String> readLaunchList(){
    	List<String> linePaths = new ArrayList<String>();
    	
 		Path currentRelativePath = Paths.get("");
   		String launchFile = currentRelativePath.toAbsolutePath().toString()+"\\data\\launchInfo.dat";
 		  try {
 			 BufferedReader br = new BufferedReader(new InputStreamReader(
                     new FileInputStream(launchFile), "UTF8"));				
 			 
 			 String line = br.readLine();
		        while (line != null) {
				    linePaths.add(line);
				    line = br.readLine();
				}

			try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		} catch (FileNotFoundException e) {
			System.err.println(e);
			//e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return linePaths;

	}

    
    /**
     * 
     * @param list
     */
    public void saveAreaDatList(List<Object> list){
    	Area area;
		List<Object> newlist = new ArrayList<Object>();
		for (int i = 0; i < list.size()-1; i++) {
			area = getGoodArea(list.get(i));
			newlist.add(area.getLocalkey());
			//newlist.add(getGoodArea(list.get(i)).getNumCopy());
		}
		mapListArea.put(areaName,newlist);
		saveMapListArea(mapListArea);	
		
    }
    
    public void saveMapListArea(Map<String, List<Object>> mapListArea){
    	
    	if (mapListArea.size() > 0){
    		
 		Path currentRelativePath = Paths.get("");
   		String areaSceneryPath = currentRelativePath.toAbsolutePath().toString()+"\\data\\"+sceneryArea;
   		Writer writer = null;
		 
	 		try {
			    writer = new BufferedWriter(new OutputStreamWriter(
			          new FileOutputStream(areaSceneryPath), "utf-8"));
			    
			    for(Map.Entry<String, List<Object>> entry : mapListArea.entrySet()) {
			    	  String key = entry.getKey();
			    	  List<Object> listValue = (List<Object>)entry.getValue();
			    	  
			    	  if (listValue != null){
				    	  writer.write(key+"=");
						  for (int i = 0; i < listValue.size(); i++) {
								try {
									writer.write(listValue.get(i)+"|");
								} catch (Exception e) {
									
									System.err.println(e);
								}
							}
							writer.write("\n");
			    		  
			    	  }
			    }			    
			    
			} catch (IOException | NullPointerException ex) {
			  System.err.println(ex.getMessage());
				 ManageStatus.sendError("message/", prefs.getProperty("fsProgram").replace(".", "_")+":"+(ex.getMessage().replaceAll(" ", "%20")));
			} finally {
			   try {writer.close();} catch (Exception ex) {}
			}			
    	}
   	
    }
    
    
    /**
     * 
     * @param list
     * @return
     */
 /*   private boolean isSame(List<Object> list){
    	boolean isSame = (setOfArea.size()+1 == list.size());
    	
		if (isSame) {
			for (int i = 1; i < list.size()-1; i++) {
				if (!setOfArea.contains(mapWork.get(list.get(i)).getNumScenery())){
					isSame = false;
					break;
				}
			}

		}
    	
    	return isSame;
    	
    }
*/	
     
    /**
     * 
     * @param file
     */
    private  String readMapListArea(File file){
    	List<Object> list = new ArrayList<Object>();

		  try {
			  	BufferedReader br = new BufferedReader(new InputStreamReader(
	                      new FileInputStream(file), "UTF8")); 
				String line = br.readLine();
				
		        while (line != null) {
		        	areaName = line.substring(0,line.indexOf("="));
		        	line = line.replace(areaName+"=","");
		        	list = Arrays.asList((Object[])line.split("\\|"));
		        	mapListArea.put(areaName,list);
				    line = br.readLine();
				}

		        
        		if (list.size() != 0){
    		        if (!"none".equals(prefs.getProperty("areaName"))){
    		        	areaName = prefs.getProperty("areaName");
    		        } else {
    		        	areaName = "";
    		        }
    		      //  mapListArea.put(areaName,list);
        		}
			
			try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				System.err.println(e);
				//e.printStackTrace();
			} catch (NullPointerException e) {
				System.err.println(e);
				//e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		  
		return areaName;

	}

    /**
	 *getGoodArea(Object title)
	 * 
	 * @param title
	 * @return
	 */
	public Area getGoodArea(Object title){
		return (mapDestination.get(title) != null)?mapDestination.get(title):mapSource.get(title);
	}
	
	/**
	 * getBothSide()
	 * 
	 * @return
	 */
	public Map<String,Area> getBothSide(){
		mapSource.putAll(mapDestination);
		return  mapSource;
	}

	
	/**
	 * @return the mapMandatory
	 */
	public Map<String, Area> getMapDestination() {
		return mapDestination;
	}

	public Object[] getMapDestination(boolean isWithDefault){
		Object[] objects = new Object[mapDestination.size()];
		
		if (isWithDefault){
			objects = mapDestination.keySet().toArray();
		} else {
			List <Object> list = new ArrayList<Object>();
			for(String key : mapDestination.keySet()){
				if (key.toUpperCase().contains(ADDON_SCENERY.toUpperCase())){
					break;
				}
				list.add(key);
			}
			
			objects = list.toArray();
		}
		return objects;
	}

	
	/**
	 * @return the mapSource
	 */
	public Map<String, Area> getMapSource() {
		return mapSource;
	}

	/**
	 * @param mapSource the mapSource to set
	 */
	public void setMapSource(Map<String, Area> mapSource) {
		this.mapSource = mapSource;
	}

	/**
	 * @return the listOfArea
	 */
	public String[] getListOfArea() {
		mapListArea.put(" ", null);
		return (String[]) mapListArea.keySet().toArray(new String[mapListArea.keySet().size()]);
	}

	/**
	 * @return the aeraName
	 */
	public String getAreaName() {
		return areaName;
	}

	/**
	 * @param aeraName the aeraName to set
	 */
	public void setAreaName(String aeraName) {
		this.areaName = aeraName;
	}

	/**
	 * @return the mapAllScenery
	 */
	public NavigableMap<String, Area> getMapAllScenery() {
		return mapAllScenery;
	}

	/**
	 * @param mapAllScenery the mapAllScenery to set
	 */
	public void setMapAllScenery(NavigableMap<String, Area> mapAllScenery) {
		this.mapAllScenery = mapAllScenery;
	}

	/**
	 * @return the addonScenery
	 */
	public String getAddonScenery() {
		return addonScenery;
	}

	/**
	 * @return the rootFSX
	 */
	public String getFsRoot() {
		return fsRoot;
	}

	/**
	 * @param rootFSX the rootFSX to set
	 */
	public void setFsRoot(String fsRoot) {
		this.fsRoot = fsRoot;
	}

	public Map<String, Area> getMapWork() {
		return mapWork;
	}

	public void setMapWork(Map<String, Area> mapWork) {
		this.mapWork = mapWork;
	}

	public File getSceneryAreaFile() {
		return sceneryAreaFile;
	}


	/**
	 * @return the mapListArea
	 */
	public Map<String, List<Object>> getMapListArea() {
		return mapListArea;
	}

	/**
	 * @param mapListArea the mapListArea to set
	 */
	public void setMapListArea(Map<String, List<Object>> mapListArea) {
		this.mapListArea = mapListArea;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public Properties getProp() {
		return prop;
	}

	public Properties getPrefs() {
		return prefs;
	}

	public void setPrefs(Properties prefs) {
		this.prefs = prefs;
	}

	public String getSceneryRoot() {
		return sceneryRoot;
	}

	public void setSceneryRoot(String sceneryRoot) {
		this.sceneryRoot = sceneryRoot;
	}

	public String getFsProgram() {
		return fsProgram;
	}

	public void setFsProgram(String fsProgram) {
		this.fsProgram = fsProgram;
	}

	public void setAddonScenery(String addonScenery) {
		this.addonScenery = addonScenery;
	}

	public String getSceneryFileName() {
		return sceneryFileName;
	}

	public void setSceneryFileName(String sceneryFileName) {
		this.sceneryFileName = sceneryFileName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getApplicationRoot() {
		return applicationRoot;
	}

	public void setApplicationRoot(String applicationRoot) {
		this.applicationRoot = applicationRoot;
	}

	public String getQuitAfter() {
		return quitAfter;
	}

	public void setQuitAfter(String quitAfter) {
		this.quitAfter = quitAfter;
	}

}
