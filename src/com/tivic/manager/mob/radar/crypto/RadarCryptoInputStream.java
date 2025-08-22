package com.tivic.manager.mob.radar.crypto;

import java.io.FilterInputStream;
import java.io.InputStream;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class RadarCryptoInputStream extends FilterInputStream {
	private static InputStream wrapInputStream(InputStream is) {
		return new CipherInputStream(is, newAESCipher(is));
	}

	private static Cipher newAESCipher(InputStream is) {
		try {
			byte[] encryptedSecret = new byte[256];
			is.read(encryptedSecret);
			RSACrypto rsaCrypto;
//			if (privateKeyFile != null)
//				rsaCrypto = RadarCrypto.getPrivateCrypto(privateKeyFile);
//			else
//				rsaCrypto = RadarCrypto.getPrivateCrypto();
			rsaCrypto = RadarCrypto.getPrivateCryptoByResource();
			byte[] rawSecret = rsaCrypto.decrypt(encryptedSecret);
			byte[] rawKey = Arrays.copyOfRange(rawSecret, 0, 16);
			byte[] rawIv = Arrays.copyOfRange(rawSecret, 16, 32);
			SecretKey key = new SecretKeySpec(rawKey, "AES");
			IvParameterSpec iv = new IvParameterSpec(rawIv);
			//
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, key, iv);
			return cipher;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public RadarCryptoInputStream(InputStream is) {
		super(wrapInputStream(is));
	}

}
