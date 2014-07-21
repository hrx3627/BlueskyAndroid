package com.hengtong.library.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 功能：DES算法加解密
 * @ahthor：黄荣星
 * @date:2013-11-12
 * @version::V1.0
 */
public class DesTool {

	/*
	 * 定义加密偏移常量
	 */
	private static byte[] iv1 = { (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF };

	/*
	 * 加密算法，传人需要加密的字符串和KEY进行加密，返回加密后的字符串
	 */
	public static String encrypt(String input, String keys) {
		if (keys.length() > 8) {
			keys = keys.substring(0, 8);
		}
		String result = "input";
		try {
			result = StringUtils.base64_encode(desEncrypt(input.getBytes(), keys));
		} catch (Exception e) {
			LogControl.e("encrypt(): error=" + e.getMessage(), "DesTool");
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * 解密算法，传人需要解密的字符串和KEY进行解密，返回解密后的字符串
	 */
	public String decrypt(String output, String keys) {
		String easytom = "http://www.easytom.cn/download/app.html?";
		int back = output.indexOf(easytom);

		if (keys.length() > 8) {
			keys = keys.substring(0, 8);
		} else if (back != -1) {
			output = output.substring(40);
		}

		String decryptstr = null;
		try {
			decryptstr = new String(desDecrypt(output, keys));
		} catch (Exception e) {
			LogControl.e("decrypt(): error=" + e.getMessage(), "DesTool");
			return null;
		}
		return decryptstr;
	}

	/*
	 * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[] hexStr2ByteArr(String strIn) 互为可逆的转换过程
	 */
	public static String byteArr2HexStr(byte[] arrB) throws Exception {
		int iLen = arrB.length;
		// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			// 把负数转换为正数
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}
			// 小于0F的数需要在前面补0
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}

	/*
	 * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB) 互为可逆的转换过程
	 */
	public static byte[] hexStr2ByteArr(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;
		// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}

	/*
	 * 加密算法
	 */
	private static byte[] desEncrypt(byte[] plainText, String keys) throws Exception {
		IvParameterSpec iv = new IvParameterSpec(iv1);
		DESKeySpec dks = new DESKeySpec(keys.getBytes("ASCII"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		byte data[] = plainText;
		byte encryptedData[] = cipher.doFinal(data);
		return encryptedData;
	}

	/*
	 * 解密算法
	 */
	private static byte[] desDecrypt(String plainText, String keys) throws Exception {
		byte[] decryptedData = null;
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(keys.getBytes("ASCII"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(iv1);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
		byte data[] = StringUtils.base64_decode(plainText);
		decryptedData = cipher.doFinal(data);
		return decryptedData;
	}
}
