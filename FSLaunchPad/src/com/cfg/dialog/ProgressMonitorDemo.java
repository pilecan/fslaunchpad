package com.cfg.dialog;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.cfg.file.ManageConfigFile;
import com.cfg.file.ManageXMLFile;
import com.cfg.model.ATCWaypoint;
import com.cfg.model.Area;
import com.cfg.model.Placemark;
import com.cfg.plan.CreateFSLPlan;
import com.cfg.util.Util;
import com.geo.util.Geoinfo;

public class ProgressMonitorDemo extends JFrame implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static ProgressMonitor pbar;
    static int c = 0;
	private ManageXMLFile xmlfile;

	private ManageConfigFile configFile;
	
	private List<ATCWaypoint> points;
	private Map<String, Placemark> fsxPlacemarks ;
	private Map<String, Placemark> addonPlacemarks ;
	private List <String> addonList;

	private double distanceRequired = 60;
    private javax.swing.Timer t = new javax.swing.Timer(500, this);

	private int totalFsxPlacemarks;
	private int totalPlacemarks;
	private int totalAddonPlacemarks;
    
	public ProgressMonitorDemo(ManageXMLFile xmlfile,
			ManageConfigFile configFile, List<ATCWaypoint> points,
			Map<String, Placemark> fsxPlacemarks,
			Map<String, Placemark> addonPlacemarks, List<String> addonList,
			double distanceRequired) throws HeadlessException {
		super();
		this.xmlfile = xmlfile;
		this.configFile = configFile;
		this.points = points;
		this.fsxPlacemarks = fsxPlacemarks;
		this.addonPlacemarks = addonPlacemarks;
		this.addonList = addonList;
		this.distanceRequired = distanceRequired;
	    setSize(250, 100);
	    pbar = new ProgressMonitor(null, "Monitor Progress", "Initializing", 0, 100);
	    t.start();
	    SwingUtilities.invokeLater(new MakeFlightPlan());
		t.stop();
	}
	
	public ProgressMonitorDemo(CreateFSLPlan createFSLPlan) throws HeadlessException {
		super();
	    setSize(250, 100);
	    pbar = new ProgressMonitor(null, "Monitor Progress", "Initializing", 0, 100);
	    t.start();
	    SwingUtilities.invokeLater((Runnable) createFSLPlan);
		t.stop();
	}


	
	
/*	public ProgressMonitorDemo(ManageXMLFile xmlfile,
			ManageConfigFile configFile, List<ATCWaypoint> points)
			throws HeadlessException {
		super();
		this.xmlfile = xmlfile;
		this.configFile = configFile;
		this.points = points;
	    setSize(250, 100);
	    pbar = new ProgressMonitor(null, "Monitor Progress", "Initializing", 0, 100);
	    javax.swing.Timer t = new javax.swing.Timer(500, this);
	    t.start();
	}
*/

@Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(new MakeFlightPlan());
    }

    public static void main(String arg[]) {
        UIManager.put("Progress Monitor", "......?");
        UIManager.put("OptionPane.cancelButtonText", "Go Away");
      //  new ProgressMonitorDemo();
    }
    class Update implements Runnable
    {
        public void run()
        {
            if(pbar.isCanceled())
            {
                pbar.close();
                System.exit(0);
            }
            pbar.setProgress(c);
            pbar.setNote("Operation is "+c+" % Completed");
            c+=2;
        }
    }
    
    
    class MakeFlightPlan  implements Runnable {

		public void run() {
            if(pbar.isCanceled())
            {
                pbar.close();
                System.exit(0);
            }
    		System.out.println(xmlfile.getHashPlacemark().size());
    		System.out.println(configFile.getMapSource().size());
    		System.out.println(points.size());
    		
    		
    		
    		long start = System.currentTimeMillis();
    		
    		//fsx
    		int cpt = 0;
    		for(Placemark placemark : xmlfile.getHashPlacemark().values()){
    	        pbar.setProgress(cpt);
    	        pbar.setNote("Operation is "+cpt/xmlfile.getHashPlacemark().size()+" % Completed");
    	        cpt++;
   		    	Double[] dd1 = Geoinfo.convertDoubleLongLat(placemark.getCoordinates());

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
    
       }
			
		}


	public Map<String, Placemark> getAddonPlacemarks() {
		return addonPlacemarks;
	}


	public void setAddonPlacemarks(Map<String, Placemark> addonPlacemarks) {
		this.addonPlacemarks = addonPlacemarks;
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


	public int getTotalPlacemarks() {
		return totalPlacemarks;
	}


	public void setTotalPlacemarks(int totalPlacemarks) {
		this.totalPlacemarks = totalPlacemarks;
	}


	public int getTotalAddonPlacemarks() {
		return totalAddonPlacemarks;
	}


	public void setTotalAddonPlacemarks(int totalAddonPlacemarks) {
		this.totalAddonPlacemarks = totalAddonPlacemarks;
	}



    } 
    


    


