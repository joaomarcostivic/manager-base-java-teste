package com.tivic.manager.eCarta;

import java.io.File;
import java.util.ArrayList;

public class ManipulateFolder {
	
	public void createFolder(String path) 
	{
		if (!new File(path).exists()) 
		{
            new File(path).mkdir();
        } 
		else 
        {
        	File fileExist = new File(path);
        	remove(fileExist);      	
        	new File(path).mkdir();
        }
	}
	
	public ArrayList<String> listFiles(File file) 
	{
		ArrayList<String> filesArray = new ArrayList<String>();		   
		File[] files = file.listFiles();
		for(File data : files) 
		{
			filesArray.add(data.getAbsolutePath());
		}
		
		return filesArray;
	}

	public void remove(File fileExist) 
	{
		if (fileExist.isDirectory()) 
		{
			File[] files = fileExist.listFiles();
	        for (int i = 0; i < files.length; ++i)
	        {
	        	remove(files[i]);
	        }
	    }
		
		fileExist.delete();
	}
}
