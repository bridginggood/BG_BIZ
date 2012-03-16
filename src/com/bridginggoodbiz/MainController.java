package com.bridginggoodbiz;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainController extends Activity {
	private String mEncBizId="", mEncBizPassword="";	//Encrypted strings

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		//If biz session exists
		if(hasBizSession()){
			//Create Business object
			Business business = new Business(mEncBizId, mEncBizPassword);
			
			if(BusinessAuthentication.isBusinessLoginSuccess(getApplicationContext(), business)){
				//launch QR Reader
				Intent intentQRReader = new Intent(MainController.this, QRReaderController.class);
				//Load bizToken on the intent to pass it onto QRReaderController
				Bundle extra = new Bundle();
				extra.putSerializable((String)getResources().getString(R.string.BUNDLE_TOKEN), (Serializable) business);
				intentQRReader.putExtras(extra);
				//Start QRReader and end current activity
				MainController.this.startActivity(intentQRReader);
				MainController.this.finish();
			}
		}
		else{
			//Go to Biz Login 
			Intent intentLogin = new Intent(MainController.this, LoginController.class);
			MainController.this.startActivity(intentLogin);
			MainController.this.finish();
		}
	}

	//Check BizSession
	private boolean hasBizSession(){
		String prefsName = (String) getResources().getText(R.string.PREFS_NAME);
		SharedPreferences bizSettings = getSharedPreferences(prefsName, MODE_PRIVATE);
		mEncBizId = bizSettings.getString("bizId", "");
		mEncBizPassword = bizSettings.getString("bizPassword", "");

		if(mEncBizId=="" || mEncBizPassword=="")	//If either is empty, there is no session
			return false;
		else
			return true;
	}
}