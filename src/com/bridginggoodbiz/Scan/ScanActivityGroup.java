package com.bridginggoodbiz.Scan;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.app.ActivityGroup;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bg.google.zxing.client.android.integration.IntentIntegrator;
import com.bg.google.zxing.client.android.integration.IntentResult;
import com.bridginggoodbiz.Business;
import com.bridginggoodbiz.CONST;
import com.bridginggoodbiz.DB.DonationJSON;

public class ScanActivityGroup extends ActivityGroup {

	private ArrayList<View> historyScanActivityGroup; 		// ArrayList to manage Views.
	private ScanActivityGroup scanActivityGroup; 			// ScanActivityGroup that Activity can access.

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Initialize global variables
		historyScanActivityGroup = new ArrayList<View>();
		scanActivityGroup = this;

		// Start the root activity within the group and get its view
		View view = getLocalActivityManager().startActivity("ScanActivity", new 
				Intent(this,ScanMainActivity.class)		//First page
		.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
		.getDecorView();

		// Replace the view of this ActivityGroup
		replaceView(view);
	}

	public void changeView(View v)  { // Changing one activity to another on same level
		historyScanActivityGroup.remove(historyScanActivityGroup.size()-1);
		historyScanActivityGroup.add(v);
		setContentView(v);
	}

	public void replaceView(View v) {   // Changing to new activity level.
		historyScanActivityGroup.add(v);   
		setContentView(v); 
	}

	public void back() { // On back key press
		if(historyScanActivityGroup.size() > 1) {   
			historyScanActivityGroup.remove(historyScanActivityGroup.size()-1);   
			setContentView(historyScanActivityGroup.get(historyScanActivityGroup.size()-1)); 
		} else {   
			finish(); // Finish tabactivity
			Log.d("BgBiz", "onDestroy called from "+this.getClass().toString());
			System.exit(0);
		}   
	}  

	public void onBackPressed() { // Event Handler
		scanActivityGroup.back();   
		return;
	}

	//Accessor methods
	public ScanActivityGroup getScanActivityGroup(){
		return scanActivityGroup;
	}

	public void setScanActivityGroup(ScanActivityGroup scanActivityGroup){
		this.scanActivityGroup = scanActivityGroup;  
	}

	public ArrayList<View> getHistoryScanActivityGroup(){
		return historyScanActivityGroup;
	}

	public void setHistoryScanActivityGroup(ArrayList<View> historyBizActivityGroup){
		this.historyScanActivityGroup = historyBizActivityGroup;
	}

	public void onDestory(){
		super.onDestroy();
		Log.d("BgBiz", "onDestroy called from "+this.getClass().toString());
		System.exit(0);
	}

	//Called to handle scanner result
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		//Toast.makeText(ScanActivityGroup.this, "RESULTCODE:"+resultCode, Toast.LENGTH_SHORT).show();
		if(resultCode == 0){	//Back key pressed
			return;
		}

		// Handle QR Scan result
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		updateQRCodeResult(result);
	}

	private void updateQRCodeResult(IntentResult result){
		if (result == null){
			//Quit, when user has pressed back button
			Log.d("BGB", "IntentResult is null");
			return;
		}else{
			HandleQRCodeResultAsyncTask handleQRCodeResultAsyncTask = new HandleQRCodeResultAsyncTask(ScanActivityGroup.this, result);
			handleQRCodeResultAsyncTask.execute();
		}

	}

	//Async to handle qrcode result. Requests donation to server
	private class HandleQRCodeResultAsyncTask extends AsyncTask<Context, Boolean, Boolean>
	{
		private ProgressDialog mProgressDialog;
		private Context mContext;
		private IntentResult mResult;

		public HandleQRCodeResultAsyncTask(Context context, IntentResult result){
			this.mContext = context;
			this.mResult = result;
		}


		//Display progress dialog
		protected void onPreExecute()
		{
			this.mProgressDialog = ProgressDialog.show(this.mContext, "", "Verifying...", true, false);		
		}

		//Do login
		protected Boolean doInBackground(Context... contexts)
		{
			try{
				//Parse result
				String content = mResult.getContents();
				StringTokenizer st = new StringTokenizer(content, "=");
				String url = st.nextToken();
				String qrId = st.nextToken();
				Log.d("BGB", "URL:"+url+" | QRID:"+qrId);

				if(url.contains(CONST.QRCODE_URL) && qrId.length() == CONST.QRCODE_ID_LENGTH){
					Log.d("BGB", "Valid QRCode:"+qrId);
					//Make donation via server
					return DonationJSON.makeDonation(qrId, Business.getBizId());
				}
				else{
					Log.d("BGB", "Invalid QRCode:"+content);
					return false;
				}

			}catch(Exception e){
				Log.d("BGBIZ", "StartBusinessLoginAsyncTask error: "+e.getLocalizedMessage());
			}
			return false;
		}
		protected void onPostExecute(final Boolean isDonationSuccess)
		{
			if(this.mProgressDialog.isShowing())
			{
				this.mProgressDialog.dismiss();
			}

			if(isDonationSuccess){
				//Donation is complete
				ScanMainActivity._this.updateDailyCounter();
				Log.d("BGB", "Donation made! Thank you!");
				Toast.makeText(this.mContext, "Donation made! Thank you!", Toast.LENGTH_SHORT).show();
			}else{
				//Donation failed. Print error message
				Log.d("BGB", "Invalid QRCode. Please try again later.");
				Toast.makeText(this.mContext, "Invalid QRCode. Please try again later.", Toast.LENGTH_SHORT).show();
			}

			//Check if QR reader should start again or not
			if(Business.isAutoQR()){
				IntentIntegrator.initiateScan(ScanActivityGroup.this);
			}
			else{
				return;
			}
		}
	}
}