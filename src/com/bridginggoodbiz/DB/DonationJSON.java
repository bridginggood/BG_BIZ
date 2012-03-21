/**
 * Created By: Junsung Lim
 * 
 * Calls server for user login. 
 * Calls to server are done in POST method.
 * Retrieves JSON from the server in return.
 */
package com.bridginggoodbiz.DB;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bridginggoodbiz.CONST;

import android.util.Log;

public class DonationJSON {

	private static final String PARAM_QR_ID = "QrId";
	private static final String PARAM_BIZ_ID = "BusinessId";
	private static final String PARAM_RESULT_CODE = "resultCode";
	private static final String PARAM_RESULT_MSG = "resultMsg";

	public static boolean makeDonation(final String qrId_str, final String bizId_str){
		try {
			String targetURL = CONST.API_DONATE_URL;
			String requestParam = "";

			String[][] param = { 
					{PARAM_QR_ID, qrId_str}, 
					{PARAM_BIZ_ID, bizId_str}
			};
			requestParam = BgHttpHelper.generateParamData(param);

			String jsonStr = BgHttpHelper.requestHttpRequest(targetURL, requestParam, "POST");

			JSONArray jsonArray = new JSONArray(jsonStr);
			JSONObject jsonObject = (JSONObject) jsonArray.get(0);

			if(jsonObject.getString(PARAM_RESULT_CODE).charAt(0) == 'S'){
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
