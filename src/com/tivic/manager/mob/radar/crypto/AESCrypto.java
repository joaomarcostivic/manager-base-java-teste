package com.tivic.manager.mob.radar.crypto;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCrypto {
	private static final String TRANSFORMATION = "AES/CBC/PKCS5PADDING";
	private static final String ALGORITHM = "AES";
	private Cipher ecipher;
	private Cipher dcipher;

	public AESCrypto(SecretKey key, IvParameterSpec iv) {
		initCiphers(TRANSFORMATION, key, iv);
	}

	public AESCrypto(byte[] key, byte[] iv) {
		this(new SecretKeySpec(key, ALGORITHM), new IvParameterSpec(iv));
	}

	private Cipher createCipher(String transformation, SecretKey key,
			IvParameterSpec iv, int cipherMode) {
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(cipherMode, key, iv);
			return cipher;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void initCiphers(String transformation, SecretKey key,
			IvParameterSpec iv) {
		ecipher = createCipher(transformation, key, iv, Cipher.ENCRYPT_MODE);
		dcipher = createCipher(transformation, key, iv, Cipher.DECRYPT_MODE);
	}

	private byte[] doFinal(Cipher cipher, byte[] input) {
		try {
			return cipher.doFinal(input);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] encrypt(byte[] input) {
		return doFinal(ecipher, input);
	}

	public byte[] decrypt(byte[] input) {
		return doFinal(dcipher, input);
	}

	private static final SecureRandom GENERATOR = new SecureRandom();

	public static SecretKey generateKey() {
		byte[] bytes = new byte[16];
		GENERATOR.nextBytes(bytes);
		return new SecretKeySpec(bytes, ALGORITHM);
	}

	public static IvParameterSpec generateIv() {
		byte[] bytes = new byte[16];
		GENERATOR.nextBytes(bytes);
		return new IvParameterSpec(bytes);
	}
}
