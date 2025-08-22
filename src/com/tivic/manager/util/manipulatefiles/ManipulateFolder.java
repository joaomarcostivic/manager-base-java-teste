package com.tivic.manager.util.manipulatefiles;

import java.io.File;
import java.util.ArrayList;

public class ManipulateFolder implements IManipulateFolder{
	
	@Override
	public void createFolder(String path) {
		if (!new File(path).exists()) {
            new File(path).mkdir();
        } 
		else {
        	File fileExist = new File(path);
        	removeAllFiles(fileExist);      	
        	new File(path).mkdir();
        }
	}
	
	@Override
	public ArrayList<String> listFiles(File file) {
		ArrayList<String> filesArray = new ArrayList<String>();		   
		File[] files = file.listFiles();
		for(File data : files) {
			filesArray.add(data.getAbsolutePath());
		}
		
		return filesArray;
	}

	@Override
	public void removeAllFiles(File fileExist) {
		if (fileExist.isDirectory()) {
			File[] files = fileExist.listFiles();
	        for (int i = 0; i < files.length; ++i){
	        	removeAllFiles(files[i]);
	        }
	    }
		fileExist.delete();
	}

}
