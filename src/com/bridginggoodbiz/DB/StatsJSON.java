package com.bridginggoodbiz.DB;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.bridginggoodbiz.Business;
import com.bridginggoodbiz.CONST;
import com.bridginggoodbiz.Stats.DataRecord;

public class StatsJSON {
	private static final String PARAM_BUSINESS_ID = "BusinessId";
	private static final String PARAM_DAILY_COUNT = "DailyCount";
	private static final String PARAM_CHARITY_NAME = "CharityName";
	private static final String PARAM_BEGIN_DATE = "BeginDate";
	private static final String PARAM_END_DATE = "EndDate";
	private static final String PARAM_DONATION_AMOUNT = "DonationAmount";
	private static final String PARAM_RESULT_COUNT = "ResultCount";
	private static final String PARAM_DONATION_DATE = "DonationDate";
	private static final String PARAM_SCANS = "Scans";

	private static final String PARAM_RESULT_CODE = "resultCode";
	private static final String PARAM_RESULT_MSG = "resultMsg";

	public static String getTodayCount(){
		try {
			String targetURL = com.bridginggoodbiz.CONST.API_STAT_TODAY_COUNT_URL;
			String requestParam = "";

			String[][] param = {{PARAM_BUSINESS_ID, Business.getBizId()+""}};
			requestParam = BgHttpHelper.generateParamData(param);

			String jsonStr = BgHttpHelper.requestHttpRequest(targetURL, requestParam, "POST");

			//If retreived empty string, return empty ArrayList
			if (jsonStr.equals("[]") || jsonStr.equals("{}"))
				return null;

			JSONArray jsonArray = new JSONArray(jsonStr);
			JSONObject jsonObject = (JSONObject) jsonArray.get(0);

			if(jsonObject.getString(PARAM_RESULT_CODE).charAt(0) == 'S'){
				return jsonObject.getString(PARAM_DAILY_COUNT);
			} else {
				Log.d("BgDB", "Login failed: "+jsonObject.getString(PARAM_RESULT_MSG));
				return null;
			}
		} catch (Exception e){
			// Handle all exception
			Log.d("BgDB", "Exception occured: "+e.getLocalizedMessage());
		}
		return null;
	}

	public static String[] getAccountDetail(){
		try {
			String targetURL = com.bridginggoodbiz.CONST.API_STAT_ACCOUNT_DETAIL_URL;
			String requestParam = "";

			String[][] param = {{PARAM_BUSINESS_ID, Business.getBizId()+""}};
			requestParam = BgHttpHelper.generateParamData(param);

			String jsonStr = BgHttpHelper.requestHttpRequest(targetURL, requestParam, "POST");

			//If retreived empty string, return empty ArrayList
			if (jsonStr.equals("[]") || jsonStr.equals("{}"))
				return null;

			JSONArray jsonArray = new JSONArray(jsonStr);
			JSONObject jsonObject = (JSONObject) jsonArray.get(0);

			if(jsonObject.getString(PARAM_RESULT_CODE).charAt(0) == 'S'){
				String[] ar = new String[3];
				ar[0] = jsonObject.getString(PARAM_CHARITY_NAME);
				ar[1] = jsonObject.getString(PARAM_BEGIN_DATE)+" - "+jsonObject.getString(PARAM_END_DATE);
				ar[2] = jsonObject.getString(PARAM_DONATION_AMOUNT);
				return ar;
			} else {
				Log.d("BgDB", "Login failed: "+jsonObject.getString(PARAM_RESULT_MSG));
				return null;
			}
		} catch (Exception e){
			// Handle all exception
			Log.d("BgDB", "Exception occured: "+e.getLocalizedMessage());
		}
		return null;
	}

	public static ArrayList<DataRecord> getStatsDaily(int resultCount){
		ArrayList<DataRecord> arData = new ArrayList<DataRecord>();
		try {
			String targetURL = null;

			switch(resultCount){
			case CONST.STATS_RESULT_COUNT_DAILY:
				targetURL = CONST.API_STAT_DAILY_COUNT_URL;
				break;
			case CONST.STATS_RESULT_COUNT_WEEKLY:
				targetURL = CONST.API_STAT_WEEKLY_COUNT_URL;
				break;
			case CONST.STATS_RESULT_COUNT_MONTHLY:
				targetURL = CONST.API_STAT_MONTHLY_COUNT_URL;
				break;
			default: 
				targetURL = CONST.API_STAT_DAILY_COUNT_URL;
			}

			String requestParam = "";

			String[][] param = {{PARAM_BUSINESS_ID, Business.getBizId()+""},
					{PARAM_RESULT_COUNT, resultCount+""}};
			requestParam = BgHttpHelper.generateParamData(param);

			String jsonStr = BgHttpHelper.requestHttpRequest(targetURL, requestParam, "POST");

			//If retreived empty string, return empty ArrayList
			if (jsonStr.equals("[]") || jsonStr.equals("{}"))
				return null;

			JSONArray jsonArray = new JSONArray(jsonStr);
			for(int i=0; i<jsonArray.length(); i++){
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);

				if(jsonObject.getString(PARAM_RESULT_CODE).charAt(0) == 'S'){
					String date = jsonObject.getString(PARAM_DONATION_DATE);
					String scans = jsonObject.getString(PARAM_SCANS);
					String total = jsonObject.getString(PARAM_DONATION_AMOUNT);

					DataRecord dr = new DataRecord(date, scans, total);
					arData.add(dr);
				} else {
					Log.d("BgDB", "Login failed: "+jsonObject.getString(PARAM_RESULT_MSG));
					return null;
				}
			}
			return arData;
		} catch (Exception e){
			// Handle all exception
			Log.d("BgDB", "Exception occured: "+e.getLocalizedMessage());
		}
		return null;
	}
}
