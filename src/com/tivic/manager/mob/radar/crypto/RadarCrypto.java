package com.tivic.manager.mob.radar.crypto;


import java.io.*;

import com.tivic.manager.util.Util;

public class RadarCrypto {
	private static InputStream getResourceAsStream(String name) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		return cl.getResourceAsStream(name);
	}

	public static RSACrypto getPublicCrypto() {
		return new RSACrypto(
				RSACrypto.loadPublicKeyFromStream(getResourceAsStream("keys/public.key")));
	}

	public static RSACrypto getPrivateCrypto() {
		return new RSACrypto(
				RSACrypto.loadPrivateKeyFromStream(getResourceAsStream("keys/private.key")));
	}

	public static RSACrypto getPrivateCrypto(String privateKeyFile) throws FileNotFoundException {
		return new RSACrypto(RSACrypto.loadPrivateKeyFromStream(new FileInputStream(privateKeyFile)));
	}
	
	public static RSACrypto getPrivateCryptoByResource() throws FileNotFoundException {
		return new RSACrypto(RSACrypto.loadPrivateKeyFromStream(RadarCrypto.class.getResourceAsStream("/"+Util.getPackageRootPath()+"/mob/radar/crypto/keys/private.key")));
	}


}
