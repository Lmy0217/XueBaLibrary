package org.ncu.xuebalibrary.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.ncu.xuebalibrary.config.Strings;

public class SecurityUtil {

	public static String saltGenerate() {
		
		Random ranGen = new SecureRandom();
		byte[] aesKey = new byte[Strings.SECURITY_SALT_LENGTH];
		ranGen.nextBytes(aesKey);
		
		return StringUtil.byte2String(aesKey);
	}
	
	public static String sha(String text) {
		
		if(text == null || text.length() == 0) return null;
		
		byte[] byteArray = null;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(Strings.SECURITY_TYPE);
			messageDigest.update(text.getBytes());
			byteArray = messageDigest.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		
		return StringUtil.byte2String(byteArray);
	}
	
	public static String encrypt(String password, String salt) {
		return SecurityUtil.sha(SecurityUtil.sha(password) + salt);
	}
	
	public static boolean slowEquals(byte[] a, byte[] b) {
		
		if(a == null || b == null) return false;
		
	    int diff = a.length ^ b.length;
	    for(int i = 0; i < a.length && i < b.length; i++) 
	    	diff |= a[i] ^ b[i];
	    
	    return diff == 0;
	}
	
	public static String hash(File file) {
		
		if(file == null) return null;
		
		byte[] byteArray = null;
		try {
			InputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			MessageDigest complete = MessageDigest.getInstance(Strings.SECURITY_HASH);
			for(int numRead = 0; (numRead = fis.read(buffer)) > 0;) complete.update(buffer, 0, numRead);
			fis.close();
			byteArray = complete.digest();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return StringUtil.byte2String(byteArray);
	}
	
	public static long random() {
		Random random = new Random();
		long result = random.nextInt(Strings.RANDOM_SIGN_POINT_LENGTH);
		return result + Strings.RANDOM_SIGN_POINT_BEGIN;
	}
}
