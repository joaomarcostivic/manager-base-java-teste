package com.tivic.manager.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class ZlibCompression {
	
	public static byte[] MAGIC = { 'P', 'K', 0x3, 0x4 };
	
    public static void compressFile(File raw, File compressed)
        throws IOException {
        InputStream in = new FileInputStream(raw);
        OutputStream out =
            new DeflaterOutputStream(new FileOutputStream(compressed));
        shovelInToOut(in, out);
        in.close();
        out.close();
    }

    public static byte[] compress(byte[] raw) throws IOException {
    	InputStream in = new ByteArrayInputStream(raw);
    	
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	OutputStream out = new DeflaterOutputStream(baos);
    	shovelInToOut(in, out);
    	
    	in.close();
    	out.close();
    	
    	return baos.toByteArray();
    }
    
    public static void decompressFile(File compressed, File raw) throws IOException  {
        InputStream in = new InflaterInputStream(new FileInputStream(compressed));
        OutputStream out = new FileOutputStream(raw);
        shovelInToOut(in, out);
        in.close();
        out.close();
    }
    
    public static byte[] decompress(byte[] compressed) throws IOException {
    	InputStream in = new InflaterInputStream(new ByteArrayInputStream(compressed));
    	ByteArrayOutputStream bout = new ByteArrayOutputStream();
        shovelInToOut(in, bout);
        in.close();
        bout.close();
        return bout.toByteArray();
    }
    

    private static void shovelInToOut(InputStream in, OutputStream out)
        throws IOException {
        byte[] buffer = new byte[1000];
        int len;
        while((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
    }
    
    public static boolean isZip(byte[] content) {
    	
    	InputStream in = new ByteArrayInputStream(content);
    	if (!in.markSupported()) {
    		in = new BufferedInputStream(in);
    	}
    	
    	boolean isZip = true;
    	try {
    		in.mark(MAGIC.length);
    		for (int i = 0; i < MAGIC.length; i++) {
    			if (MAGIC[i] != (byte) in.read()) {
    				isZip = false;
    				break;
    			}
    		}
    		in.reset();
    	} 
    	catch (IOException e) {
    		isZip = false;
    	}
    	return isZip;
    }
}