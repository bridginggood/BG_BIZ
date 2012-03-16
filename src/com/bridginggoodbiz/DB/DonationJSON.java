/**
 * Created By: Junsung Lim
 * 
 * Calls server for user login. 
 * Calls to server are done in POST method.
 * Retrieves JSON from the server in return.
 */
package com.bridginggoodbiz.DB;

import org.json.JSONObject;

import android.util.Log;

public class DonationJSON {

	private static final String PARAM_USER_ID = "UserId";
	private static final String PARAM_BIZ_ID = "BusinessId";
	private static final String PARAM_DEVICE_ID = "DeviceId";
	private static final String PARAM_RESULT_CODE = "resultCode";
	private static final String PARAM_RESULT_MSG = "resultMsg";

	public static boolean makeDonation(final String userId_str, final String bizId_str, final String deviceId_str){
		try {
			String targetURL = "https://api.bridginggood.com:8080/donation/MakeDonation.json";
			String requestParam = "";

			String[][] param = { {PARAM_USER_ID, userId_str}, {PARAM_BIZ_ID, bizId_str}, {PARAM_DEVICE_ID, deviceId_str}};
			requestParam = BgHttpHelper.generateParamData(param);

			String jsonStr = BgHttpHelper.requestHttpRequest(targetURL, requestParam, "POST");

			JSONObject jsonObject = new JSONObject(jsonStr);

			//TODO: Maybe this needs to be changed later on.
			if(jsonObject.getString(PARAM_RESULT_CODE).charAt(0) == 'S'){
				//Login succeed!

				return true;
			} else {
				Log.d("BgBizDB", "makeDonation failed: "+jsonObject.getString(PARAM_RESULT_MSG));
				return false;
			}
		} catch (Exception e){
			// Handle all exception
			Log.d("BgDB", "Exception occured: "+e.getLocalizedMessage());
		}
		return false;
	}
}
