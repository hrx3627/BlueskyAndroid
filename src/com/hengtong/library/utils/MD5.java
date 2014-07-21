/*******************************************************************************
 * Copyright (C) 2009-2010 eoeMobile. 
 * All rights reserved.
 * http://www.eoeMobile.com/
 * 
 * CHANGE LOG:
 *  DATE			AUTHOR			COMMENTS
 * =============================================================================
 *  2010MAY11		Waznheng Ma		Refine for Constructor and error handler.
 *
 *******************************************************************************/

package com.hengtong.library.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.impl.auth.UnsupportedDigestAlgorithmException;

import android.util.Log;

/**
 * 功能：md5加密工具
 * @ahthor：黄荣星
 * @date:2013-11-12
 * @version::V1.0
 */
public final class MD5 {
	private static final String LOG_TAG = "MD5";
	private static final String ALGORITHM = "MD5";

	private static MessageDigest sDigest;

	static {
		try {
			sDigest = MessageDigest.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			Log.e(LOG_TAG, "Get MD5 Digest failed.");
			throw new UnsupportedDigestAlgorithmException(ALGORITHM, e);
		}
	}

	private MD5() {
	}

	final public static String encode(String source) {
		byte[] btyes = source.getBytes();
		byte[] encodedBytes = sDigest.digest(btyes);

		return Utility.hexString(encodedBytes);
	}
}
