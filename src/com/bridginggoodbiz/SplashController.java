package com.bridginggoodbiz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bridginggoodbiz.DB.LoginJSON;

public class SplashController extends Activity{
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

		SplashLoadingAsyncTask splashLoadingAsyncTask = new SplashLoadingAsyncTask(SplashController.this);
		splashLoadingAsyncTask.execute();
	}

	//SplashLoadingAsyncTask
	private class SplashLoadingAsyncTask extends AsyncTask<Context, Boolean, Boolean>
	{
		private ProgressDialog mProgressDialog;
		private Context mContext;
		private long mStartTime;
		private String mErrStr;

		public SplashLoadingAsyncTask(Context context){
			this.mContext = context;
		}

		//Display progress dialog
		protected void onPreExecute()
		{
			mProgressDialog = ProgressDialog.show(this.mContext, "", "Loading... Please wait", true, false);
			Business.init();
		}

		//Do login
		protected Boolean doInBackground(Context... contexts)
		{
			try{
				mStartTime = android.os.SystemClock.uptimeMillis();

				//Load stored data from the device
				BusinessStore.loadUserSession(getApplicationContext());
				
				//Check if saved user token exists or not
				boolean isLoginSuccess = LoginJSON.doLogin(Business.getBizName(), Business.getBizPassword());
				Log.d("BGB", "isBusinessAutoLogin:  "+isLoginSuccess);
			} catch(Exception e) {
				mErrStr = e.getLocalizedMessage();
			}
			return false;
		}
		protected void onPostExecute(final Boolean isLoginSuccess)
		{
			if(mProgressDialog.isShowing())
			{
				mProgressDialog.dismiss();
			}

			// Decide where to redirect user depending on isLoginSuccess
			Class<?> targetClass = isLoginSuccess? MainActivity.class:LoginActivity.class;

			long durationTime = android.os.SystemClock.uptimeMillis() - mStartTime;
			Log.d("BGB", "Login time taken: "+durationTime);
			if (durationTime <=CONST.SPLASH_DELAY) {
				try {
					//Pause the application for remaining time to meet SPLASH_DELAY
					wait(CONST.SPLASH_DELAY-durationTime);
				} catch (Exception e) {
					Log.d("BG", "spalashThread Exception: "+e.getLocalizedMessage());
					mErrStr = e.getLocalizedMessage();
				}
			}
			mProgressDialog.dismiss();

			if(!isLoginSuccess && mErrStr == null)
				Toast.makeText(mContext, "Error occured:"+mErrStr, Toast.LENGTH_SHORT).show();

			finish();
			startActivity(new Intent().setClass(SplashController.this, targetClass));
		}
	}
}
