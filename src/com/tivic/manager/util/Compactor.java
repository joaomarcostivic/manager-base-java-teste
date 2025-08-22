package com.tivic.manager.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Compactor {
	private static String nameFile;
	private static int sizeBuffer;
	
	public String getNameFile() {
		return nameFile;
	}
	public void setNameFile(String nameFile) {
		Compactor.nameFile = nameFile;
	}
	
	public static int getSizeBuffer() {
		return sizeBuffer;
	}
	public void setSizeBuffer(int sizeBuffer) {
		Compactor.sizeBuffer = sizeBuffer;
	}
	
	public void packForZip(final String exitFile, final ArrayList<String> listArray) throws IOException {
		
		int cont;
	    final byte[] data = new byte[sizeBuffer];
	    
		final FileOutputStream destiny = new FileOutputStream(new File(nameFile));
		final ZipOutputStream exit = new ZipOutputStream(new BufferedOutputStream(destiny));
		
	    for (final String fileInput : listArray) 
	    {
	        final File file = new File(fileInput);  
	        final FileInputStream streamInput = new FileInputStream(file);
	        final BufferedInputStream origin = new BufferedInputStream(streamInput, sizeBuffer);
	        final ZipEntry entry = new ZipEntry(file.getName());
	        
	        exit.putNextEntry(entry);

	        while ((cont = origin.read(data, 0, sizeBuffer)) != -1) 
	        {
	        	exit.write(data, 0, cont);
	        }
	        origin.close();
	    }

	    exit.close();
	}
}
