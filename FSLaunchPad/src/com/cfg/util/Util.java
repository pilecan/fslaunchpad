package com.cfg.util;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.cfg.common.Info;
import com.cfg.model.Area;
import com.cfg.prog.FSLaunchPad;

public class Util implements Info{
	
	private static String decoration = "style=\"text-decoration: none;background-color:white;\" onMouseOver=\"this.style.backgroundColor='#999999'\" onMouseOut=\"this.style.backgroundColor='#FFFFFF'\"";
	
	public static String aviationWeather = " <a "+decoration+" href=\"http://www.aviationweather.gov/adds/tafs?station_ids=xxxx&std_trans=translated&submit_taf=Get+TAFs\">(Weather)"+"</a>";
	
	static private String[] WEB_PAGE =  {"https://www.google.com/search?q=",
		"https://www.google.com/maps/search/",
		"http://www.flightradar24.com/data/airports/",
		"http://www.checkplane.com",
		"http://www.fslauncher.com",
		"http://en.wikipedia.org/wiki/Special:Search?search=",
		"http://library.avsim.net/search.php?CatID=root&SearchTerm=xxxx&Sort=Added&ScanMode=1",
		"http://www.freewarescenery.com/"
		};

    private static Map<String, String> COUNTRY_MAP = new HashMap<String, String>();

	static {
        COUNTRY_MAP.put("dominican depublic", "dominicanrepublic");
        COUNTRY_MAP.put("new zealand", "newzealand");
        COUNTRY_MAP.put("australia", "australia");
        COUNTRY_MAP.put("denmark", "denmark");
        COUNTRY_MAP.put("czech republic", "czech");
        COUNTRY_MAP.put("alaska", "alaska");
        COUNTRY_MAP.put("costa rica", "costarica");
        COUNTRY_MAP.put("south Africa", "southafrica");
        COUNTRY_MAP.put("north Korea", "northkorea");
        COUNTRY_MAP.put("korea", "southkorea");
        COUNTRY_MAP.put("united kingom", "uk");
        COUNTRY_MAP.put("united states", "us");
        COUNTRY_MAP.put("spain", "spain");
        COUNTRY_MAP.put("antilles", "antilles");
        COUNTRY_MAP.put("argentina", "argentina");
        COUNTRY_MAP.put("austria", "austria");
        COUNTRY_MAP.put("bahamas", "bahamas");
        COUNTRY_MAP.put("belgium", "belgium");
        COUNTRY_MAP.put("brazil", "brazil");
        COUNTRY_MAP.put("canada", "canada");
        COUNTRY_MAP.put("chile", "chile");
        COUNTRY_MAP.put("china", "china");
        COUNTRY_MAP.put("colombia", "colombia");
        COUNTRY_MAP.put("denmark", "denmark");
        COUNTRY_MAP.put("dominicanrepublic", "dominicanrepublic");
        COUNTRY_MAP.put("estonia", "estonia");
        COUNTRY_MAP.put("finland", "finland");
        COUNTRY_MAP.put("france", "france");
        COUNTRY_MAP.put("germany", "germany");
        COUNTRY_MAP.put("greece", "greece");
        COUNTRY_MAP.put("guatemala", "guatemala");
        COUNTRY_MAP.put("hawaii", "hawaii");
        COUNTRY_MAP.put("hungary", "hungary");
        COUNTRY_MAP.put("india", "india");
        COUNTRY_MAP.put("indonesia", "indonesia");
        COUNTRY_MAP.put("ireland", "ireland");
        COUNTRY_MAP.put("israel", "israel");
        COUNTRY_MAP.put("italy", "italy");
        COUNTRY_MAP.put("japan", "japan");
        COUNTRY_MAP.put("mexico", "mexico");
        COUNTRY_MAP.put("netherlands", "netherlands");
        COUNTRY_MAP.put("newzealand", "newzealand");
        COUNTRY_MAP.put("norway", "norway");
        COUNTRY_MAP.put("poland", "poland");
        COUNTRY_MAP.put("portugal", "portugal");
        COUNTRY_MAP.put("russia", "russia");
        COUNTRY_MAP.put("spain", "spain");
        COUNTRY_MAP.put("sweden", "sweden");
        COUNTRY_MAP.put("switzerland", "switzerland");
        COUNTRY_MAP.put("turkey", "turkey");
        COUNTRY_MAP.put("ukraine", "ukraine");
        COUNTRY_MAP.put("venezuela", "venezuela");
        COUNTRY_MAP.put("puerto rico", "puertorico");

        
        Collections.unmodifiableMap(COUNTRY_MAP);
    }
    
	public static String addZero(int i){
		String areaNum = "";
		if (i < 10) {
			areaNum = "00"+i;
		} else if (i < 100) {
			areaNum = "0"+i;
		} else {
			areaNum = ""+i;
		}
		
		return areaNum;
	}
	
    public static String formatAreaNum(String num, int max){
    	String formatted;
    	int i = Integer.parseInt(num); 
    	
    	i = (max - i)+1;
    	
		formatted = String.format("%04d", i);
    	
    	return formatted;
    }

    public static Map<Integer, Area>  createMapWork(Map<String, Area> mapAllScenery){
		Map<Integer, Area> mapWork = new TreeMap<Integer, Area> ();
	    for(Area area: mapAllScenery.values()){
	    	mapWork.put(Integer.valueOf(area.getNum()), area);
	    }
		
    	return mapWork;
    }
    
    public static String formatAreaNum(int num, int max){
    	String formatted;
    	
    	formatted = String.format("%04d", num);
/*    	if ( max > 999){
    		formatted = String.format("%04d", num);
    	} else {
        	formatted = String.format("%03d", num);
    	}
*/    
    	
    	return formatted;
    }

	
	
	
	public static String createHref(String title, String search, int numPage){
		if (numPage == 7){
			if (COUNTRY_MAP.get(search) != null){
				search = COUNTRY_MAP.get(search);
				search = "fsx/"+search+".html";
			} else {
				search = "fsx.html#"+search;
			}
		}
		
		return "<a "+decoration+" href=\""+WEB_PAGE[numPage]+search.replace(" ", "+")+"\">"+title+"</a>" ;
		
	}

	public static void openWebpage(String search,int numPage) {
	    try {
	    	
	        Desktop.getDesktop().browse(new URL(WEB_PAGE[numPage]+search.replace(" ", "+")  ).toURI());
	    } catch (Exception e) {
	    }
	}	
	
	public static boolean isHaveNumber(String str)
	{
		return (str.matches(".*\\d.*"));
		
	}

	/**
	 * @return the googleSearch
	 */
	public static String getFlight24() {
		return WEB_PAGE[2];
	}

	
	public static void copyFolder(File src, File dest) throws IOException {

		if (src.isDirectory()) {

			// if directory not exists, create it
			if (!dest.exists()) {
				dest.mkdir();
				System.out.println("Directory copied from " + src + "  to "
						+ dest);
			}

			// list all the directory contents
			String files[] = src.list();

			for (String file : files) {
				// construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// recursive copy
				copyFolder(srcFile, destFile);
			}

		} else {
			// if file, then copy it
			// Use bytes stream to support all file types
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();
			out.close();
			System.out.println("File copied from " + src + " to " + dest);
		}
	}
	
	   /**
     * 
     * @param num
     * @throws Exception
     */
    public static void pause(long num){
		try {
			Thread.sleep(num);
		} catch (InterruptedException e) {
			System.err.println(e);
		}

	}
    
    /**
     * 
     * 
     * @param folder
     * @return
     */
    public static boolean isDirectoryIsMovable(File folder) {
    	FileSystemModel fileSystemModel = new FileSystemModel(folder);
	    String[] files = fileSystemModel.getChilds(folder);
	    boolean isSelectable = true && files != null;
		if (isSelectable) {
	        for (String str:files){
	        	if ("scenery".equalsIgnoreCase(str) && !ADDON_SCENERY.toUpperCase().equals(folder.getName().toUpperCase())){
	        		isSelectable = false;
	        	}
	        }
	    }    
        isSelectable = isSelectable && ((!folder.toString().contains("scenery")) && !folder.toString().contains("texture"));
		return isSelectable;
    }
    
 
    
    
    public static JLabel getLabel(String text,int numColor){
    	JLabel label = new JLabel();
    	label.setText(text);
    	label.setForeground(colorForground[numColor]);
    	label.setFont(fontText);
    	return label;
    }
    
	   /**
	    * 
	    * @param localkey
	    */
	   public static void adjustMaplistArea(String localkey, Map<String, List<Object>> mapListArea){
		   Map<String, List<Object>> mapListWork = new TreeMap<String, List<Object>>();
		   List<Object> listWork;
		   
		   for (Map.Entry<String, List<Object>> entry : mapListArea.entrySet()) {
				String key = entry.getKey();
				List<Object> list = (List<Object>) entry.getValue();
				listWork = new ArrayList<Object>();
				
				for (int i = 0; i < list.size(); i++) {
					if (localkey.compareTo((String) list.get(i)) != 0){
						listWork.add(list.get(i));
					}
				}
				mapListWork.put(key,listWork);
		   }
		   
		   mapListArea.clear();
		   mapListArea.putAll(mapListWork);
	   }

		public static String getComputerName(){
			
			String computerName = System.getenv("COMPUTERNAME");
			if ("".equals(computerName) || computerName == null){
				computerName = "unknkow";
			}
			return computerName;
		}

		public static void setIcon(JFrame frame,String image){
		    //jframe.setIconImage(new ImageIcon("C:/projects/fsx/ReadCfgFile/job/FSLauncpad/src/images/bnc-logo.png").getImage() );
		     try {
		   	  java.net.URL u = FSLaunchPad.class.getResource(image);
			  //System.out.println(u);
			  BufferedImage myPicture = ImageIO.read(u);
			  frame.setIconImage(myPicture);
			} catch (IOException e1) {
			}
		}
		
		public static JLabel setMoi() {
	      ImageIcon image = new ImageIcon(FSLaunchPad.class.getResource("/images/moi.jpg")); //imports the image
	      return new JLabel(image);
	  }
		     
		
}
