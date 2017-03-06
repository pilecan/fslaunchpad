package com.cfg.test;

import java.io.*;

public class FileUtils
{
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

    public static PrintWriter openOutputFile(String file_name)
    {
        PrintWriter output = null;  // File to open for writing

        try
        {
            output = new PrintWriter(new File(file_name).getAbsoluteFile());
        }
        catch (Exception exception) {
        	System.out.println("not found");
        }

        return output;
    }

    public static void main(String[] args) throws Exception
    {
        openOutputFile("DefaultDirectoryFile.txt");
        setCurrentDirectory("C:/Program Files (x86)/Microsoft Games/Microsoft Flight Simulator X");
        //FileUtils.openOutputFile("CurrentDirectoryFile.txt");
        Runtime rt = Runtime.getRuntime();
		Process ps = rt.exec("fsx.exe");        
    }
}