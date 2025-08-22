package com.tivic.manager.mob.radar.crypto;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

public class RSACrypto {
	private static final String TRANSFORMATION = "RSA";
	private Cipher ecipher;
	private Cipher dcipher;

	public RSACrypto(RSAPublicKey key) {
		initCiphers(TRANSFORMATION, key);
	}

	public RSACrypto(RSAPrivateKey key) {
		initCiphers(TRANSFORMATION, key);
	}

	private Cipher createCipher(String transformation, Key key, int cipherMode) {
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(cipherMode, key);
			return cipher;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void initCiphers(String transformation, Key key) {
		ecipher = createCipher(transformation, key, Cipher.ENCRYPT_MODE);
		dcipher = createCipher(transformation, key, Cipher.DECRYPT_MODE);
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

	public static RSAPrivateKey loadPrivateKeyFromStream(InputStream in) {
		try {
			ObjectInputStream oin = new ObjectInputStream(
					new BufferedInputStream(in));
			try {
				BigInteger m = (BigInteger) oin.readObject();
				BigInteger e = (BigInteger) oin.readObject();
				RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
				KeyFactory fact = KeyFactory.getInstance("RSA");
				PrivateKey key = fact.generatePrivate(keySpec);
				return (RSAPrivateKey) key;
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				oin.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static RSAPrivateKey loadPrivateKeyFromFile(String filename) {
		try {
			InputStream in = new FileInputStream(filename);
			return loadPrivateKeyFromStream(in);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static RSAPublicKey loadPublicKeyFromStream(InputStream in) {
		try {
			ObjectInputStream oin = new ObjectInputStream(
					new BufferedInputStream(in));
			try {
				BigInteger m = (BigInteger) oin.readObject();
				BigInteger e = (BigInteger) oin.readObject();
				RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
				KeyFactory fact = KeyFactory.getInstance("RSA");
				PublicKey key = fact.generatePublic(keySpec);
				return (RSAPublicKey) key;
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				oin.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static RSAPublicKey loadPublicKeyFromFile(String filename) {
		try {
			InputStream in = new FileInputStream(filename);
			return loadPublicKeyFromStream(in);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
