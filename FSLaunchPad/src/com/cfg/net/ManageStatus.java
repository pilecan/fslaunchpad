package com.cfg.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.cfg.model.Status;
import com.cfg.util.FileUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ManageStatus {
	//private static String URL_LAUNCHER = "http://localhost:8080/fslaunchpad.com/launch/";
	private static String URL_LAUNCHER = "http://fslaunch-pilecan.rhcloud.com/launch/";
	
	public static Status getWebStatus(String action,String value){
		Status status = null;
		Gson gson = new Gson();
		
		try {
			URL url = new URL(URL_LAUNCHER+action+value);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF8"));

			String output = "";
			String str;
			while ((str = br.readLine()) != null) {
				output = str;
			}
			
			java.lang.reflect.Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
			Map<String, Object> categoryicons = gson.fromJson(output, mapType );
			
			status = new Status(((Double)categoryicons.get("code")).intValue(), (String)categoryicons.get("key"), (String)categoryicons.get("message"));
			
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			if (e.toString().contains("Connection refused: connect") || e.toString().contains("UnknownHostException")){
				status = new Status(66,"server.not.available");			
			}
			
		} catch (Exception e){
			status = new Status(66,"server.not.available");		
		}
		
		
		return status;
	}
	
	public static void sendError(String action,String value){
		
		try {
			URL url = new URL(URL_LAUNCHER+action+value);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "text/html, text/*");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output = "";
			String str;
			while ((str = br.readLine()) != null) {
				output = str;
			}
			
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
	}
	
	/**
	 * 
	 * @return
	 */
	public static Status getLocalStatus() {
		Status status = null;
		Gson gson = new Gson();

		Path currentRelativePath = Paths.get("");
		String prefFile = currentRelativePath.toAbsolutePath().toString()
				+ "/_";
		try {
			BufferedReader br = new BufferedReader(new FileReader(prefFile));
			status = gson.fromJson( br.readLine(), Status.class);
			try {
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			System.err.println(e);
			// e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return status;
	}	   
	
    public static void setAndsaveLocalStatus(Status status,String f){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
   		Gson gson = new Gson();
   		Path currentRelativePath = Paths.get("");
   		String prefFile = currentRelativePath.toAbsolutePath().toString()+ "/"+f;
   		String today = sdf.format(new Date());
   		
   		if (!today.equals(status.getLastDate())){
   	   		status.setCptUsage(status.getCptUsage()+1);
   		}
   		status.setLastDate(today);
   		
 		FileUtil.setHiddenProperty(new File(prefFile), false);
   		Writer writer = null;
 		try {
		    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(prefFile), "utf-8"));
			writer.write(gson.toJson(status)+"\n");
		} catch (IOException ex) {
			  System.err.println(ex.getMessage());
		} finally {
		   try {writer.close();} catch (Exception ex) {}
		}
 		
 		FileUtil.setHiddenProperty(new File(prefFile), true);
    }

}
