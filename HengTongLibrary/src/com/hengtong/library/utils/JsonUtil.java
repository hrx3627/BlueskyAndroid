/**    
 * @{#} JsonUtil.java Create on 2016-3-3 上午11:35:32    
 *    
 * Copyright (c) 2013 by BlueSky.    
 *
 *    
 * @author <a href="1084986314@qq.com">BlueSky</a>   
 * @version 1.0    
 */
package com.hengtong.library.utils;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 功能：
 * @ahthor：黄荣星
 * @date:2016-3-3
 * @version::V1.0
 */
public class JsonUtil {
	private static Gson mGson;
	static {
		mGson = new Gson();
	}

	public static Object getObject(String str, Type type) throws JsonSyntaxException {
		return mGson.fromJson(str, type);
	}
}
