package com.tivic.manager.mob.radar.crypto;

import java.io.*;


public class LocalDecrypt {
	
	public static byte[] decrypt(byte[] file) throws Exception {
		
		byte[] buf = new byte[1024];
		//FileInputStream fis = new FileInputStream(fromfile);
		ByteArrayInputStream bais = new ByteArrayInputStream(file);
		InputStream is = new RadarCryptoInputStream(bais);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int n;
		while ((n = is.read(buf)) > 0) {
			baos.write(buf, 0, n);
		}
		is.close();
		baos.close();
		return baos.toByteArray();
	}

}
