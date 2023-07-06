package com.jtestrunner.util;


import com.jtestrunner.core.resolver.UrlResolver;

import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil 
{
	private static char[] CHARSET = "abcdefghijklmnoprstuvyz123456789".toCharArray();

	public static String randomString(final int length)
	{
		Random random = new SecureRandom();
		char[] result = new char[length];
		for (int i = 0; i < result.length; i++) {
			int randomCharIndex = random.nextInt(CHARSET.length);
			result[i] = CHARSET[randomCharIndex];
		}
		return new String(result);
	}

	public static String randomStringOnlyNumber(final int length)
	{
		Random r = new Random();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < length; i++)
		{
			String str = String.valueOf(r.nextInt(9));
			sb.append(str);
		}

		return sb.toString();
	}

	public static boolean regexMatch(String pattern, String str)
	{
		Pattern r = Pattern.compile(pattern);

		Matcher m = r.matcher(str);
		if (m.find())
		{
			return m.group(0).toString().equals(str);
		}

		return false;
	}

	public static String generateEmail()
	{
		return StringUtil.randomString(5) +"@testmail.com".toLowerCase();
	}

	public static String generatePhoneNumber()
	{
		return StringUtil.randomStringOnlyNumber(10);
	}


	public static String generateBasicUrl(boolean secure, String url, UrlResolver urlResolver)
	{
		return urlResolver.getBasicUrl(secure, url);
	}

	public static String generateUrl(boolean secure, String url, UrlResolver urlResolver)
	{
		return urlResolver.getFullUrl(secure, url);
	}
}
