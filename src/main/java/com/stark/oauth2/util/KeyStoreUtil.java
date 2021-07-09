package com.stark.oauth2.util;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

import org.apache.commons.codec.binary.Base64;

public class KeyStoreUtil {
	
	public static String getPublicKey(InputStream in, String type, String alias, String password) throws Exception {
		KeyStore ks = KeyStore.getInstance(type);
		ks.load(in, password.toCharArray());
		Certificate cert = ks.getCertificate(alias);
		PublicKey publicKey = cert.getPublicKey();
		String publicKeyStr = "-----BEGIN PUBLIC KEY-----\n"
				+ Base64.encodeBase64String(publicKey.getEncoded())
				+ "\n-----END PUBLIC KEY-----";
		return publicKeyStr;
	}
	
	public static String getPrivateKey(InputStream in, String type, String alias, String password) throws Exception {
		KeyStore ks = KeyStore.getInstance(type);
		ks.load(in, password.toCharArray());
		PrivateKey privateKey = (PrivateKey) ks.getKey(alias, password.toCharArray());
		String privateKeyStr = "-----BEGIN PRIVATE KEY-----\n"
				+ Base64.encodeBase64String(privateKey.getEncoded())
				+ "\n-----END PRIVATE KEY-----";
		return privateKeyStr;
	}

}
