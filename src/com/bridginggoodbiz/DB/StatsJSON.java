package com.bridginggoodbiz.DB;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.bridginggoodbiz.Business;

public class StatsJSON {
	private static final String PARAM_BUSINESS_ID = "BusinessId";
	private static final String PARAM_DAILY_COUNT = "DailyCount";
	private static final String PARAM_RESULT_CODE = "resultCode";
	private static final String PARAM_RESULT_MSG = "resultMsg";
	
	public static String getDailyCount(){
		try {
			String targetURL = com.bridginggoodbiz.CONST.API_STAT_DAILY_COUNT_URL;
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
	
}