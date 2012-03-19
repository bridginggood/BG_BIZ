package com.bridginggoodbiz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bridginggoodbiz.DB.LoginJSON;

public class LoginActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);

		initViews();
	}

	//Initialize views (controls)
	private void initViews(){
		final EditText edtBizUsername = (EditText)findViewById(R.id.edtBizUsername);
		final EditText edtBizPassword = (EditText)findViewById(R.id.edtBizPassword);

		//Add button listener for Login button
		Button btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				//Encrypt to Business
				try{
					StartBusinessLoginAsyncTask startBusinessLoginAsyncTask = new StartBusinessLoginAsyncTask(LoginActivity.this, edtBizUsername.getText().toString(), edtBizPassword.getText().toString());
					startBusinessLoginAsyncTask.execute();
				}
				catch(Exception e){
					Log.d("BGB", "Exception occured while encrypting data: "+e.getLocalizedMessage());
				}
			}
		});
	}

	//AsyncTask to perform login	
	private class StartBusinessLoginAsyncTask extends AsyncTask<Context, Boolean, Boolean>
	{
		private ProgressDialog mProgressDialog;
		private Context mContext;
		private String mUsername, mPassword;

		public StartBusinessLoginAsyncTask(Context context, String username, String password){
			this.mContext = context;
			this.mUsername = username;
			this.mPassword = password;
		}


		//Display progress dialog
		protected void onPreExecute()
		{
			mProgressDialog = ProgressDialog.show(this.mContext, "", "Please wait...", true, false);		
		}

		//Do login
		protected Boolean doInBackground(Context... contexts)
		{
			try{
				Log.d("BGB", "USERNAME: "+this.mUsername);
				//Insert to Business
				String encUsername = CRYPTO.encrypt(CONST.CRYPT_SEED, this.mUsername);
				String encPassword = CRYPTO.encrypt(CONST.CRYPT_SEED, this.mPassword);
				Business.setBizUsername(encUsername);
				Business.setBizPassword(encPassword);

				//Do Login
				return LoginJSON.doLogin(encUsername, encPassword);
			}catch(Exception e){
				Log.d("BGBIZ", "StartBusinessLoginAsyncTask error: "+e.getLocalizedMessage());
			}
			return false;
		}
		protected void onPostExecute(final Boolean isLoginSuccess)
		{
			if(mProgressDialog.isShowing())
			{
				mProgressDialog.dismiss();
			}

			if (isLoginSuccess)
			{
				BusinessStore.saveUserSession(mContext);	//Save
				finish();	
				startActivity(new Intent().setClass(LoginActivity.this, MainActivity.class));	//Start Main
			}
			else
			{
				Log.d("BGBIZ", "Login failed");
				Toast.makeText(this.mContext, "Login failed. Please check your username and password.", Toast.LENGTH_SHORT).show();
			}
		}
	}

	/*
	//Create loading dialog and authenticate business
	private void authenticateBusiness(){
		mLoadingDialog = ProgressDialog.show(this, "Logging in", "Loading. Please wait...", true, false);

		//Create loading dialog
		runOnUiThread(new Runnable(){
			@Override
			public void run() {
				Log.d("BGB", "authBiz called");
				mIsLoginSucc = BusinessAuthentication.isBusinessLoginSuccess(getApplicationContext(), mBusiness);
				//handlerAuth.sendEmptyMessage(0);
				handlerAuth.sendEmptyMessageDelayed(0, 3000); //TODO:Use line above when the isBusinessLoginSuccess is correctly impelemented.
			}
		});
	}

	//Handler for biz authentication. 
	//Load new Activity from here
	private Handler handlerAuth = new Handler(){
		public void handleMessage(Message msg){
			mLoadingDialog.dismiss();	//Dismiss loading dialog

			if(mIsLoginSucc){
				//TODO:Go back to MainController so that QRCode can be loaded.
				Toast.makeText(getApplicationContext(), "Login Success!", Toast.LENGTH_SHORT).show();
				Log.d("BGB", "Login Success!");

				//Save login crendentials
				saveBusinessSettings();

				//launch QR Reader
				Intent intentQRReader = new Intent(LoginActivity.this, QRReaderActivity.class);
				//Load bizToken on the intent to pass it onto QRReaderController
				Bundle extra = new Bundle();
				extra.putSerializable((String)getResources().getString(R.string.BUNDLE_TOKEN), (Serializable) mBusiness);
				intentQRReader.putExtras(extra);
				//Start QRReader and end current activity
				LoginActivity.this.startActivity(intentQRReader);
				LoginActivity.this.finish();
			}
			else{
				//Login failed
				Toast.makeText(getApplicationContext(), "Login Failed. Please check login crendential", Toast.LENGTH_SHORT).show();
				Log.d("BGB", "Login Failed!");
			}
		}
	};

	//Method to store business settings
	private void saveBusinessSettings(){
		final EditText edtBizId = (EditText)findViewById(R.id.edtBizId);
		final EditText edtBizPassword = (EditText)findViewById(R.id.edtBizPassword);
		final CheckBox chkRememberLogin = (CheckBox)findViewById(R.id.chkRememberLogin);

		try{
			String cryptSeedString = (String)getResources().getText(R.string.CRYPT_SEED_STRING);

			String bizId = edtBizId.getText().toString();
			String encBizPassword = CRYPTO.encrypt(cryptSeedString, edtBizPassword.getText().toString());
			boolean isChkRememberLogin = chkRememberLogin.isChecked();

			//SharedPreference
			String prefsName = (String) getResources().getText(R.string.PREFS_NAME);
			SharedPreferences bizSettings = getSharedPreferences(prefsName, MODE_PRIVATE);

			SharedPreferences.Editor bizSettingsEditor = bizSettings.edit();
			bizSettingsEditor.putString("bizId", bizId);
			bizSettingsEditor.putBoolean("isRememberLogin", isChkRememberLogin);
			if(isChkRememberLogin){
				bizSettingsEditor.putString("encBizPass", encBizPassword);
			}
			bizSettingsEditor.commit();
		}
		catch(Exception e){
			Log.d("BGB", "An error occured in saveBuinessSettings(): "+e.getLocalizedMessage());
		}
	}

	//Load business credentials
	private void loadBusinessSettings(){
		final EditText edtBizId = (EditText)findViewById(R.id.edtBizId);
		final EditText edtBizPassword = (EditText)findViewById(R.id.edtBizPassword);
		final CheckBox chkRememberLogin = (CheckBox)findViewById(R.id.chkRememberLogin);

		//SharedPreference
		String prefsName = (String) getResources().getText(R.string.PREFS_NAME);
		SharedPreferences bizSettings = getSharedPreferences(prefsName, MODE_PRIVATE);

		String bizId = bizSettings.getString("bizId", "");
		boolean isRememberLogin = bizSettings.getBoolean("isRememberLogin", false);

		edtBizId.setText(bizId);
		chkRememberLogin.setChecked(isRememberLogin);
		if(isRememberLogin){
			try{
				String cryptSeedString = (String)getResources().getText(R.string.CRYPT_SEED_STRING);
				String encBizPassword = bizSettings.getString("encBizPass", "");
				String bizPassword = CRYPTO.decrypt(cryptSeedString, encBizPassword);
				edtBizPassword.setText(bizPassword);
			}
			catch(Exception e){
				Log.d("BGB", "An error occured in loadBusinessSettings: "+e.getLocalizedMessage());
			}
		}
	}*/
}
