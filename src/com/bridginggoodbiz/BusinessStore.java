/**
 * Created By: Junsung Lim
 * 
 * Static class to make managing SharedPreferences easier for BridgingGood.
 * This class is called to verify if login token exists on the device.
 * 
 * NOTE: Do not store Device Id on the database.
 */
package com.bridginggoodbiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class BusinessStore {

	private static final String BIZ_USERNAME = "BusinessUsername";
	private static final String BIZ_PW = "BusinessPw";
	private static final String BIZ_ID = "BusinessId";
	private static final String BIZ_AUTOQR = "BusinessAutoQR";
	private static final String BIZ_NAME = "BusinessName";
	private static final String KEY = "BridgingGoodBizStore";			//SharedPreference Key Value

	/**
	 * Save user session object to SharedPreference
	 * 
	 * @param context 		Application context to call SharedPreference in
	 */
	public static boolean saveUserSession(Context context) {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
		editor.putString(BIZ_ID, Business.getBizId());
		editor.putString(BIZ_USERNAME, Business.getBizUsername());
		editor.putString(BIZ_PW, Business.getBizPassword());
		editor.putString(BIZ_NAME, Business.getBizName());
		editor.putBoolean(BIZ_AUTOQR, Business.isAutoQR());
		return editor.commit();
	}

	public static void loadUserSession(Context context){
		SharedPreferences savedSession = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
		Business.setBizId(savedSession.getString(BIZ_ID, null));
		Business.setBizUsername(savedSession.getString(BIZ_USERNAME, null));
		Business.setBizPassword(savedSession.getString(BIZ_PW, null));
		Business.setBizName(savedSession.getString(BIZ_NAME, null));
		Business.setAutoQR(savedSession.getBoolean(BIZ_AUTOQR, false));
	}

	public static void clearSession(Context context) {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
		editor.clear();
		editor.commit();
	}
}
