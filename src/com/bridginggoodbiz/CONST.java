package com.bridginggoodbiz;

public class CONST {
	private static final String API_URL = "http://api.bridginggood.com:8080";
	
	//SplashController: Splash time
	public static final long SPLASH_DELAY = 1500;
	
	public static final String CRYPT_SEED = "zkdltmxm0607!"; //kaist0607
	public static final String QRCODE_URL = "http://www.bridginggood.com/?qr";
	public static final int QRCODE_ID_LENGTH = 16;
	
	//50 days, 52 weeks, 36 months
	public static final int STATS_RESULT_COUNT_DAILY = 50;
	public static final int STATS_RESULT_COUNT_WEEKLY = 52;
	public static final int STATS_RESULT_COUNT_MONTHLY = 36;
	
	//For BridgingGood Database API
	public static final String API_DONATE_URL = API_URL+"/donation/MakeDonation.json";
	public static final String API_LOGIN_URL = API_URL+"/auth/LoginByBusinessFromMobile.json";

	//Stat
	public static final String API_STAT_TODAY_COUNT_URL = API_URL+"/stats/BusinessTodayCount.json";
	public static final String API_STAT_ACCOUNT_DETAIL_URL = API_URL+"/stats/BusinessAccountDetail.json";
	
	public static final String API_STAT_DAILY_COUNT_URL = API_URL+"/stats/BusinessStatsDaily.json";
	public static final String API_STAT_WEEKLY_COUNT_URL = API_URL+"/stats/BusinessStatsDaily.json";
	public static final String API_STAT_MONTHLY_COUNT_URL = API_URL+"/stats/BusinessStatsDaily.json";
	
}
