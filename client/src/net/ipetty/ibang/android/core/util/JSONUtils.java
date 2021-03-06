/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ipetty.ibang.android.core.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Locale;

import net.ipetty.ibang.android.sdk.exception.ApiException;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 * 
 * @author Administrator
 */
public class JSONUtils {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

	public static String toJson(Object o) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(dateFormat);
		Writer str = new StringWriter();
		try {
			mapper.writeValue(str, o);
		} catch (IOException ex) {
			throw new ApiException(ex);
		}
		return str.toString();
	}

	/**
	 * TypeReference<List<String>> valueTypeRef = new
	 * TypeReference<List<String>>(){}; fromJSON(valueTypeRef);
	 * 
	 * @param <T>
	 * @param str
	 * @param valueTypeRef
	 * @return
	 */
	public static <T> T fromJSON(String str, TypeReference<T> valueTypeRef) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setDateFormat(dateFormat);
		T t;
		try {
			t = mapper.readValue(str, valueTypeRef);
		} catch (IOException ex) {
			throw new ApiException(ex);
		}
		return t;
	}

	/**
	 * fromJSON(String.class); fromJSON(Class.forName("xx.yy.ZZ"))
	 * 
	 * @param <T>
	 * @param str
	 * @param type
	 * @return
	 */
	public static <T> T fromJSON(String str, Class<T> type) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setDateFormat(dateFormat);
		T t;
		try {
			t = mapper.readValue(str, type);
		} catch (IOException ex) {
			throw new ApiException(ex);
		}
		return t;
	}

	public static <T> T fromJSON(String str, String classType) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setDateFormat(dateFormat);
		Class clazz;
		try {
			clazz = Class.forName(classType);
		} catch (ClassNotFoundException ex) {
			throw new ApiException("无法找到类型", ex);
		}
		T t;
		try {
			t = (T) mapper.readValue(str, clazz);
		} catch (IOException ex) {
			throw new ApiException(ex);
		}
		return t;
	}
}
