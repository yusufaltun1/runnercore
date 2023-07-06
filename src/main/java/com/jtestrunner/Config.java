package com.jtestrunner;

public class Config
{
	public static String URL;
	public static String BASIC_URL;
	public static String BROWSER;
	public static String DRIVER;
	public static boolean BASIC_AUTH;
	public static String BASIC_AUTH_USERNAME;
	public static String BASIC_AUTH_PASSWORD;
	public static boolean SECURE;
	public static int CUSTOMER_ID;
	public static String RESOLUTION;
	public static Long EXECUTION_ID;
	public static Boolean SYNC_API = true;

	public static String HAC_USERNAME = "admin";
	public static String HAC_PASSWORD = "passs";
	public static String HAC_URL;

	static
	{
		URL = System.getProperty("baseUrl");
		BROWSER = System.getProperty("browser");
		DRIVER = System.getProperty("driver");
		SECURE = Boolean.valueOf(System.getProperty("secure"));
		BASIC_AUTH = Boolean.valueOf(System.getProperty("basicAuth"));
		HAC_URL = BASIC_URL + "/hac";

		RESOLUTION = System.getProperty("resolution");
		EXECUTION_ID = Long.valueOf(System.getProperty("executionId", "1"));

		if(BASIC_AUTH)
		{
			BASIC_AUTH_USERNAME = System.getProperty("basicAuthUserName");
			BASIC_AUTH_PASSWORD = System.getProperty("basicAuthPassword");
		}
	}
}
