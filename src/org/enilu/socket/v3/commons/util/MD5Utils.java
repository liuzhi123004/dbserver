package org.enilu.socket.v3.commons.util;

import java.security.MessageDigest;

public class MD5Utils {

	public static String getMd5(String text) {
		try {
			MessageDigest alga = MessageDigest.getInstance("MD5");
			alga.update(text.getBytes());
			byte[] digesta = alga.digest();
			return byte2hex(digesta);
		} catch (Exception e) {
			e.printStackTrace();
			return text;
		}

	}

	public static String byte2hex(byte[] b) // 二行制转字符串
	{
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}
}
