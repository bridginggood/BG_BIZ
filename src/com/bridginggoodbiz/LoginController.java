package com.bridginggoodbiz;

import java.io.Serializable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginController extends Activity {
	private ProgressDialog mLoadingDialog; //Loading Dialog
	private boolean mIsLoginSucc = false;	//Login flag

	private Business mBusiness = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);

		initViews();

		loadBusinessSettings();
	}

	//Initialize views (controls)
	private void initViews(){
		final EditText edtBizId = (EditText)findViewById(R.id.edtBizId);
		final EditText edtBizPassword = (EditText)findViewById(R.id.edtBizPassword);

		//Add button listener for Login button
		Button btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Log.d("BGB", "Login button clicked");

				//Encrypt using mCryptSeedString
				try{
					String cryptSeedString = (String)getResources().getText(R.string.CRYPT_SEED_STRING);
					String encBizId = Crypto.encrypt(cryptSeedString, edtBizId.getText().toString());
					String encBizPassword = Crypto.encrypt(cryptSeedString, edtBizPassword.getText().toString());

					Log.d("BGB", "Encryption successful. encBizId:"+encBizId+", encBizPassword"+encBizPassword);

					//Create new business object to authenticate
					mBusiness = new Business(encBizId, encBizPassword);

					//Authenticate user
					authenticateBusiness();
				}
				catch(Exception e){
					Log.d("BGB", "Exception occured while encrypting data: "+e.getLocalizedMessage());
				}
			}
		});
	}

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
				Intent intentQRReader = new Intent(LoginController.this, QRReaderController.class);
				//Load bizToken on the intent to pass it onto QRReaderController
				Bundle extra = new Bundle();
				extra.putSerializable((String)getResources().getString(R.string.BUNDLE_TOKEN), (Serializable) mBusiness);
				intentQRReader.putExtras(extra);
				//Start QRReader and end current activity
				LoginController.this.startActivity(intentQRReader);
				LoginController.this.finish();
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
			String encBizPassword = Crypto.encrypt(cryptSeedString, edtBizPassword.getText().toString());
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
				String bizPassword = Crypto.decrypt(cryptSeedString, encBizPassword);
				edtBizPassword.setText(bizPassword);
			}
			catch(Exception e){
				Log.d("BGB", "An error occured in loadBusinessSettings: "+e.getLocalizedMessage());
			}
		}
	}
}
