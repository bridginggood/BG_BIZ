package com.bridginggoodbiz;

import android.content.Context;
import android.util.Log;

public class BusinessAuthentication{
	public static boolean isBusinessLoginSuccess(Context context, Business business){
		//TODO:Check from the server.
		String cryptSeedString = (String) context.getResources().getString(R.string.CRYPT_SEED_STRING);
		
		String encBizId = business.getEncBizId();
		String encBizPassword = business.getEncBizPassword();
		
		try{
			if(Crypto.decrypt(cryptSeedString, encBizId).equals("test"))	//TODO: implement authentication
				return true;
			else
				return false;
		}
		catch(Exception e){
			Log.d("BGB", "Error occured from isValidBiz:"+e.getLocalizedMessage());
		}
		return false;
	}
}
