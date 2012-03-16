package com.bridginggoodbiz;

import java.util.StringTokenizer;

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
import android.widget.TextView;
import android.widget.Toast;

import com.bridginggoodbiz.DB.DonationJSON;
import com.google.zxing.client.android.integration.IntentIntegrator;
import com.google.zxing.client.android.integration.IntentResult;

public class QRReaderController extends Activity{

	private ProgressDialog mLoadingDialog = null;
	private boolean mLoadQRAgain = false;
	private boolean mDonationSuccess = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qrreader_layout);
		mLoadQRAgain = false;

		Business business = (Business) getIntent().getExtras().get((String)getResources().getString(R.string.BUNDLE_TOKEN));
		initViews(business);
	}

	//Initialize views (controls)
	private void initViews(Business business){
		try{
			//Change the welcome message
			TextView lblWelcome = (TextView)findViewById(R.id.lblWelcomeMessage);
			String cryptSeedString = (String)getResources().getText(R.string.CRYPT_SEED_STRING);
			String bizId = Crypto.decrypt(cryptSeedString, business.getEncBizId());
			lblWelcome.setText("Welcome "+bizId);

			//Add button listener for Login button
			Button btnLoadQRReader = (Button) findViewById(R.id.btnLoadQRReader);
			btnLoadQRReader.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					Log.d("BGB", "QR button clicked");
					loadQRScanner();
				}
			});

			//Add button listener for Signout button
			Button btnSignoutAndQuit = (Button)findViewById(R.id.btnSignoutAndQuit);
			btnSignoutAndQuit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d("BGB", "Signout button clicked");
					signoutAndQuit();
				}
			});

			//Add button listener for quit button
			Button btnQuit = (Button)findViewById(R.id.btnQuit);
			btnQuit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d("BGB", "Quit button clicked");
					finish();
				}
			});
		}
		catch(Exception e){
			Log.d("BGB", "An error occured in initViews: "+e.getLocalizedMessage());
		}
	}

	//Signout the business
	private void signoutAndQuit(){
		//Clear the SharedPreference for logout
		String prefsName = (String) getResources().getText(R.string.PREFS_NAME);
		SharedPreferences bizSettings = getSharedPreferences(prefsName, MODE_PRIVATE);

		SharedPreferences.Editor bizSettingsEditor = bizSettings.edit();
		bizSettingsEditor.putString("bizId", "");
		bizSettingsEditor.putBoolean("isRememberLogin", false);
		bizSettingsEditor.putString("encBizPass", "");
		bizSettingsEditor.commit();

		//finish current activity
		finish();
	}


	//Load scanner
	private void loadQRScanner(){
		//Call QR Scanner
		IntentIntegrator.initiateScan(QRReaderController.this);
	}

	//Called to handle scanner result
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// Handle QR Scan result
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		updateQRCodeResult(result);
	}

	//TODO:Implement storing QRCode read data to the server
	private void updateQRCodeResult(final IntentResult result){
		//Show result using AlertDialog
		mLoadingDialog = ProgressDialog.show(this, "Authenticating", "Loading. Please wait...", true, false);

		//Create loading dialog
		runOnUiThread(new Runnable(){
			@Override
			public void run() {
				Log.d("BGB", "updateQRCodeResult called");
				Log.d("BGB", "Scanned Result:"+result.getContents());

				//Change mLoadQRAgain so that QR reader starts again
				if(result.getContents()!=null)
					mLoadQRAgain = true;
				else
					mLoadQRAgain = false;

				//handlerAuth.sendEmptyMessage(0);
				Message msg = new Message();
				msg.obj = result.getContents();
				
				/*
				StringTokenizer st = new StringTokenizer(result.getContents(),",");
				String userId = st.nextToken();
				st.nextToken();
				String devId = st.nextToken();
				String bizId = "2000000001";
				//Toast.makeText(getApplicationContext(), userId+","+devId+","+bizId, Toast.LENGTH_SHORT).show();
				mDonationSuccess = DonationJSON.makeDonation(userId, bizId, devId);
				if(mDonationSuccess)
					Toast.makeText(getApplicationContext(), "Succeeded!", Toast.LENGTH_SHORT).show();
				
				Log.d("BGB", "mDonationSuccess: "+mDonationSuccess);
				*/
				//handlerLoading.sendEmptyMessageDelayed(, 3000); //TODO:Use line above when the isBusinessLoginSuccess is correctly impelemented.
				handlerLoading.sendMessageDelayed(msg, 5000);
			}
		});
	}
	private Handler handlerLoading = new Handler(){
		public void handleMessage(Message msg){
			TextView t = (TextView)findViewById(R.id.txtQRCodeContent);
			t.setText((CharSequence) msg.obj);
			if(mLoadQRAgain){
				Log.d("BGB", "Loading Scanner again by the handler");
				loadQRScanner();
			}

			mLoadingDialog.dismiss();	//Dismiss loading dialog
		}
	};
}
