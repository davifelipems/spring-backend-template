package com.br.davifelipe.springjwt.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class UrlUtil {
	
	private UrlUtil() {
		throw new IllegalStateException("Utility class");
	}
	
	public static String decodeParam(String p) {
		try {
			return URLDecoder.decode(p,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
}
