package com.tivic.manager.mob.radar.crypto;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class RadarCryptoOutputStream extends FilterOutputStream {
	private static OutputStream wrapOutputStream(boolean cryptoEnabled,
			OutputStream os) {
		if (cryptoEnabled) {
			return new CipherOutputStream(os, newAESCipher(os));
		} else {
			return os;
		}
	}

	private static Cipher newAESCipher(OutputStream os) {
		SecretKey key = AESCrypto.generateKey();
		IvParameterSpec iv = AESCrypto.generateIv();
		try {
			ByteArrayOutputStream secret = new ByteArrayOutputStream(32);
			secret.write(key.getEncoded());
			secret.write(iv.getIV());
			secret.close();
			RSACrypto rsaCrypto = RadarCrypto.getPublicCrypto();
			byte[] encryptedSecret = rsaCrypto.encrypt(secret.toByteArray());
			os.write(encryptedSecret);
			os.flush();
			//
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);
			return cipher;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public RadarCryptoOutputStream(boolean cryptoEnabled, OutputStream os) {
		super(wrapOutputStream(cryptoEnabled, os));
	}
}
