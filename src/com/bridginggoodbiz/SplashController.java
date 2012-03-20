package com.bridginggoodbiz;

import com.bridginggoodbiz.DB.LoginJSON;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SplashController extends Activity{
	private ProgressDialog mProgressDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
		
		ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = manager.getActiveNetworkInfo();
		if(ni== null || !ni.isConnected())
		{
			Toast.makeText(getApplicationContext(), "No network connection found. Please check your network status.", Toast.LENGTH_LONG).show();
		} 

		mProgressDialog = ProgressDialog.show(this, "", "Loading... Please wait", true, false);
		Business.init();
		
		Thread splashTread = new Thread() {
			@Override
			public void run() {
				long startTime = android.os.SystemClock.uptimeMillis();

				/*
				 *===============START Loading Job==================== 
				 */
				BusinessStore.loadUserSession(getApplicationContext());
				
				//Check if saved user token exists or not
				boolean isLoginSuccess = isBusinessAutoLoginSuccessful();
				Log.d("BGB", "isBusinessAutoLogin:  "+isLoginSuccess);

				// Decide where to redirect user depending on isLoginSuccess
				Class<?> targetClass = isLoginSuccess? MainActivity.class:LoginActivity.class;

				long durationTime = android.os.SystemClock.uptimeMillis() - startTime;
				Log.d("BGB", "Login time taken: "+durationTime);
				if (durationTime <=CONST.SPLASH_DELAY) {
					try {
						synchronized(this){
							//Pause the application for remaining time to meet SPLASH_DELAY
							wait(CONST.SPLASH_DELAY-durationTime);
						}
					} catch (Exception e) {
						Log.d("BG", "spalashThread Exception: "+e.getLocalizedMessage());
					}
				}

				/*
				 *===============END Loading Job==================== 
				 */

				mProgressDialog.dismiss();
				finish();
				startActivity(new Intent().setClass(SplashController.this, targetClass));
			}
		};
		splashTread.start();
	}

	/**
	 * Returns whether login attempt using stored credentials was successful or not.
	 * @return True if login is success
	 */
	private boolean isBusinessAutoLoginSuccessful(){
		LoginJSON.doLogin(Business.getBizName(), Business.getBizPassword());
		return false;
	}

}
