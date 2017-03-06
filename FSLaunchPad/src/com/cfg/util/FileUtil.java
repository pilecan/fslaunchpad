package com.cfg.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Calendar;
import java.util.Date;

import com.cfg.common.Info;

public class FileUtil implements Info{
	
	public static void createDirectory(String directoryName){
		File theDir = new File(directoryName);

		  // if the directory does not exist, create it
		  if (!theDir.exists()) {
		    System.out.println("creating directory: " + directoryName);
		    boolean result = false;

		    try{
		        theDir.mkdir();
		        result = true;
		     } catch(SecurityException se){
		        System.err.println(se);
		     }        
		     if(result) {    
		       System.out.println("DIR created");  
		     }
		  }		
	}
	
	
	public static String getDirectoryDate(String folder) {
		String dateStr = "2015-01-03T22:40:13";
		try {
			Path path = Paths.get(folder);
			BasicFileAttributes attr = Files.readAttributes(path,BasicFileAttributes.class);
			dateStr = attr.creationTime().toString().substring(0, 19);
		} catch (IOException e) {
		}
		return dateStr;
	}
	
	
	public static boolean isDateSame(File one, File second){
		return (one.lastModified() == one.lastModified());
	}

	public static void setFileCreationDate(String filePath, Date creationDate) throws IOException{
        BasicFileAttributeView attributes = Files.getFileAttributeView(Paths.get(filePath), BasicFileAttributeView.class);
        FileTime time = FileTime.fromMillis(creationDate.getTime());
        attributes.setTimes(time, time, time);
    }

   public static void setFileCreationDate(String filePath, FileTime parentTime) throws IOException{
       BasicFileAttributeView attributes = Files.getFileAttributeView(Paths.get(filePath), BasicFileAttributeView.class);
      /// FileTime time = FileTime.fromMillis(creationDate.getTime());
       attributes.setTimes(parentTime, parentTime, parentTime);
   }
   
	   
   public static void setHiddenProperty(File file, boolean isHidden)  {
	    try {
			Process p = Runtime.getRuntime().exec("attrib "+(isHidden?"+H ":"-H ")+file.getPath());
			p.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }
		
   public static void checkFile(Path path){
		try {
		    DosFileAttributes attr = Files.readAttributes(path,DosFileAttributes.class);
		    System.out.println("isReadOnly is " + attr.isReadOnly());
		    System.out.println("isHidden is " + attr.isHidden());
		    System.out.println("isArchive is " + attr.isArchive());
		    System.out.println("isSystem is " + attr.isSystem());
		} catch (IOException x) {
		    System.err.println("DOS file attributes not supported:" + x);
		}		
   }
   
   public static long calculateDays(Date dateEarly, Date dateLater) {  
	   return (dateLater.getTime() - dateEarly.getTime()) / (24 * 60 * 60 * 1000);  
	} 

	/** Using Calendar - THE CORRECT WAY **/
	public static long daysBetween(Calendar startDate, Calendar endDate) {
		
		Calendar date = (Calendar) startDate.clone();
		long daysBetween = 0;
		while (date.before(endDate)) {
			date.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		return daysBetween;
	}
	
	public static Calendar DateToCalendar(Date date){ 
		  Calendar cal = Calendar.getInstance();
		  cal.setTime(date);
		  return cal;
	}
	
	public static boolean isFileExist(String filePathString){
		Path path = Paths.get(filePathString);
		return Files.exists(path);
		
	}
	
	public static boolean isAdmin() {
		boolean isAdmin = false;
   		String file = Paths.get("").toAbsolutePath().toString()+"\\data\\_";
   		
   		long lastDate= new File(file).lastModified();
   		
   		Writer writer = null;
	
 		try {
		    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		    writer.write(".");
		} catch (Exception ex) {
			 isAdmin = false;
		} finally {
		   try {writer.close();} catch (Exception ex) {}
		}	
 		
 		if (new File(file).lastModified() != lastDate){
 			isAdmin = true;
 		}
	
 		//System.out.println(System.getProperty("os.name"));
 		
		return isAdmin;
	}	
	
    public static boolean setCurrentDirectory(String directory_name)
    {
        boolean result = false;  // Boolean indicating whether directory was set
        File    directory;       // Desired current working directory

        directory = new File(directory_name).getAbsoluteFile();
        if (directory.exists() || directory.mkdirs())
        {
            result = (System.setProperty("user.dir", directory.getAbsolutePath()) != null);
        }

        return result;
    }

	public static String[] getPathAndName(String fileName){
	  	String path = new File(fileName).getAbsolutePath();
    	path = path.substring(0,path.lastIndexOf(File.separator));
    	String fName = new File(fileName).getName();
		return new String[]{path,fName} ;
	}
	
}

