package com.bridginggoodbiz;

public class CONST {
	private static final String API_URL = "http://api.bridginggood.com:8080";
	
	//SplashController: Splash time
	public static final long SPLASH_DELAY = 1500;
	
	public static final String CRYPT_SEED = "zkdltmxm0607!"; //kaist0607
	public static final String QRCODE_URL = "http://www.bridginggood.com/?qr";
	public static final int QRCODE_ID_LENGTH = 16;
	
	//For BridgingGood Database API
	public static final String API_DONATE_URL = API_URL+"/donation/MakeDonation.json";
	
	public static final String API_LOGIN_URL = API_URL+"/auth/LoginByBusinessFromMobile.json";

	
}
