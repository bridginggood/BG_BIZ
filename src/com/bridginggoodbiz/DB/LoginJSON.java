package com.bridginggoodbiz.DB;

import org.json.JSONObject;

import android.util.Log;

import com.bridginggoodbiz.Business;
import com.bridginggoodbiz.CONST;
import com.bridginggoodbiz.CRYPTO;

public class LoginJSON {
	private static final String PARAM_USERNAME = "Username";
	private static final String PARAM_PASSWORD = "Password";
	private static final String PARAM_ID = "BusinessId";
	private static final String PARAM_RESULT_CODE = "resultCode";
	private static final String PARAM_RESULT_MSG = "resultMsg";

	public static boolean doLogin(final String username, final String password){
		try {
			String targetURL = CONST.API_LOGIN_URL;
			String requestParam = "";

			String[][] param = { 
					{PARAM_USERNAME, CRYPTO.decrypt(CONST.CRYPT_SEED, username)}, 
					{PARAM_PASSWORD, CRYPTO.decrypt(CONST.CRYPT_SEED, password)}
			};
			requestParam = BgHttpHelper.generateParamData(param);

			String jsonStr = BgHttpHelper.requestHttpRequest(targetURL, requestParam, "POST");

			JSONObject jsonObject = new JSONObject(jsonStr);

			if(jsonObject.getString(PARAM_RESULT_CODE).charAt(0) == 'S'){
				updateBusienssWithJSON(jsonObject);
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

	private static void updateBusienssWithJSON(JSONObject jsonObj) throws Exception{
		//Only need to update business id?
		Business.setBizId(jsonObj.getString(PARAM_ID));
	}
}
