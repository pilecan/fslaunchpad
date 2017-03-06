package com.cfg.factory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cfg.model.LauncherInfo;
import com.cfg.util.FileUtil;
import com.cfg.util.Util;
import com.google.gson.Gson;


public class LauncherFactory  {
	private static LauncherFactory instance = new LauncherFactory();
	
	private LauncherInfo launcherInfo;
	private Gson gson;
	private SimpleDateFormat sdf;
	private String json;
	
	private LauncherFactory(){
		sdf = new SimpleDateFormat("yyyyMMdd");
		gson = new Gson();
	}
	
	public static LauncherFactory getInstance(){
		return instance;
	}
	
	public void createLauncher(String key, String folder, String version,String lang, String sim){
   		Path currentRelativePath = Paths.get("");
   		String currentPath = currentRelativePath.toAbsolutePath().toString();
		
		launcherInfo = new LauncherInfo(key, 
										Util.getComputerName(), 
										FileUtil.getDirectoryDate(folder), 
										"", //order id... 
										sdf.format(new Date()),
										FileUtil.getDirectoryDate(currentPath), 
										null,
										version,
										lang,
										sim);
		 // System.out.println("==>"+launcherInfo.toString());
		 json =  gson.toJson(launcherInfo);
		 json = json.replaceAll(" ", "%20");
		 json = json.replaceAll("\\.", "_");
		 json = json.replaceAll("/", "-");
		 json = json.replaceAll("%", "-");
		 json = json.replaceAll("#", "-");
		 json = json.replace("\"a\":", "\"keyId\":");
		 json = json.replace("\"b\":", "\"computerName\":");
		 json = json.replace("\"c\":", "\"createdDate\":");
		 json = json.replace("\"d\":", "\"orderCode\":");
		 json = json.replace("\"e\":", "\"lastDate\":");
		 json = json.replace("\"f\":", "\"fsDate\":");
		 json = json.replace("\"h\":", "\"version\":");
		 json = json.replace("\"i\":", "\"lang\":");
		 json = json.replace("\"j\":", "\"sim\":");
	//    System.out.println("==>"+json);
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public LauncherInfo getLauncherInfo() {
		return launcherInfo;
	}

	public void setLauncherInfo(LauncherInfo launcherInfo) {
		this.launcherInfo = launcherInfo;
	}


}
