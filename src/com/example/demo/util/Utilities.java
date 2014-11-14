package com.example.demo.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

public class Utilities {
	 private static final boolean DEBUG_ON = true;

	    public static void printCookieStore(CookieStore cookieStore) {
	        debug("printCookies");
	        List<Cookie> cookies = cookieStore.getCookies();
	        if (cookies.isEmpty()) {
	            System.out.println("None");
	        } else {
	            for (int i = 0; i < cookies.size(); i++) {
	                System.out.println("- " + cookies.get(i).toString());
	            }
	        }
	        debug("end printCookies");
	    }

	    public static String md5(String pass) {
	        String md5 = "";
	        try {
	            MessageDigest m = MessageDigest.getInstance("MD5");
	            byte[] data = pass.getBytes(); 
	            m.update(data, 0, data.length);
	            BigInteger i = new BigInteger(1, m.digest());
	            md5 = String.format("%1$032x", i);
	        } catch (NoSuchAlgorithmException e) {
	            System.out.println(e);
	        } 
	        return md5;
	    }

	    public static long unixTime() {
	        return System.currentTimeMillis() / 1000L;
	    }

	    public static void debug(String msg) {
	        //TODO: add levels of debug
	        if (DEBUG_ON) {
	            System.out.println("Debug: " + msg);
	        }
	    }
}
