package com.tivic.manager.util;

import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.digest.MessageDigestAlgorithms;

/**
 * Crypto and Security services
 * 
 * @author Maurício Cordeiro
 * @since 09/08/2019
 *
 */
public class SecretServices {	

	public static final String KEY = "Workers of all countries, unite! You have nothing to lose but your chains!";
	
	public static final String HMAC_MD5    = "HmacMD5";
	public static final String HMAC_SHA1   = "HmacSHA1";
	public static final String HMAC_SHA256 = "HmacSHA256";
	public static final String HMAC_SHA512 = "HmacSHA512";
	
	/**
	 * Generates a HMAC hash
	 * 
	 * @param source {@link String}
	 * @param key {@link String}
	 * @param algorithm {@link String}
	 * @return  {@link String}
	 */
	public static String generateHmac(String source, String key, String algorithm) {
		String result = null;
        try {
        	SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), algorithm);
        	
        	Mac mac = Mac.getInstance(algorithm);
        	mac.init(keySpec);
        	            
        	byte[] bytes = mac.doFinal(source.getBytes("UTF-8"));
            return bytesToHex(bytes);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;
	}
	
	public static String generateMD5(String source) {
		return generateHash(source, MessageDigestAlgorithms.MD5);
	}
	
	public static String generateSHA256(String source) {
		return generateHash(source, MessageDigestAlgorithms.SHA_256);		
	}
	
	/**
	 * @param source {@link String}
	 * @param algorithm {@link String} {@link MessageDigestAlgorithms} like
	 * @return hash string {@link String}
	 */
	public static String generateHash(String source, String algorithm) {
		String result = null;
        try {
        	MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] bytes = digest.digest(source.getBytes("UTF-8"));
            return bytesToHex(bytes);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;
	}	
	
//	public static String
	
	private static String bytesToHex(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash);
    }
	
	/**
	 * Build a string with chars from prime number positions
	 * 
	 * @param source {@link String}
	 * @return {@link String}
	 */
	public static String onlyPrimes(String source) {
		try {        	
			String src = new String(source);
			String result = "";			
			for(int i=0; i<src.length(); i++) {
				if(isPrime((i))) {
					result += src.charAt(i);
				}
			}			
			return result;
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
	}
	
	private static boolean isPrime(int number) {
		int count = 0; 		
		for(int i=1; i<=number; i++) {
			if(number%i==0) {
				count++;
			}
		}		
		return count==2;
	}

}
