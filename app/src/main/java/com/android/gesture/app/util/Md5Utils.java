package com.android.gesture.app.util;

/* Copyright (c) ViaCube.  All worldwide rights reserved. */ 


import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {

	// first argument is the password need MD5
	// second argument is algorithm
	// third argument is separate symbol
	public String toMd5(String original, String separator) {
		try {
			String result;
			byte[] bytes = original.getBytes();
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(bytes);
			result = toHexString(algorithm.digest(), separator);
			return result;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public String toHexString(byte[] bytes, String separator) {
		StringBuilder hexString = new StringBuilder();
		for (byte b : bytes) {
			hexString.append(String.format("%02x", 0xFF & b)).append(separator);
		}
		return hexString.toString();
	}
	
	/** Calculate MD5 sum of a file */
	static final public String calcMD5(File file){
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");

			FileInputStream input = new FileInputStream(file);
			byte[] buf = new byte[1024];
	
			while (input.available() > 0) {
				int res = input.read(buf, 0, buf.length);
	
				md.update(buf, 0, res);
			}
			input.close();
			
			byte[] md5 = md.digest();
	
			return bytesToHexString(md5);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return ""+file.length();
	}

	/**
	 * Convert an array of bytes to a string of hexadecimal numbers
	 */
	static final private String bytesToHexString(byte[] array) {
		StringBuffer res = new StringBuffer();

		for (int i = 0; i < array.length; i++) {
			int val = array[i] + 256;
			String b = "00" + Integer.toHexString(val);
			int len = b.length();
			String sub = b.substring(len - 2);

			res.append(sub);
		}

		return res.toString();
	}

	public static String toMd5(String str) {
		StringBuffer md5Code = new StringBuffer();
		try {
			//获取加密方式为md5的算法对象
			MessageDigest instance = MessageDigest.getInstance("MD5");
			byte[] digest = instance.digest(str.getBytes());
			for (byte b : digest) {
				String hexString = Integer.toHexString(b & 0xff);
				if (hexString.length() < 2) {
					hexString = "0"+hexString;
				}
				md5Code = md5Code.append(hexString);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md5Code.toString();
	}
}
