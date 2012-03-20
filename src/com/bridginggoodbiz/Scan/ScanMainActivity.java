package com.bridginggoodbiz.Scan;

import java.util.StringTokenizer;

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
import android.widget.CheckBox;
import android.widget.Toast;

import com.bridginggoodbiz.Business;
import com.bridginggoodbiz.CONST;
import com.bridginggoodbiz.R;
import com.bridginggoodbiz.DB.DonationJSON;
import com.google.zxing.bg.client.android.integration.IntentIntegrator;
import com.google.zxing.bg.client.android.integration.IntentResult;

public class ScanMainActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qrreader_layout);

		initViewControls();
	}

	private void initViewControls(){
		CheckBox chkAutoQR = (CheckBox)findViewById(R.id.chkQRAutoStart);
		chkAutoQR.setChecked(Business.isAutoQR());	//Load saved value

		Button btnLoadQRReader = (Button)findViewById(R.id.btnLoadQRReader);
		btnLoadQRReader.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox chkAutoQR = (CheckBox)findViewById(R.id.chkQRAutoStart);
				Business.setAutoQR(chkAutoQR.isChecked());	//Update check status
				loadQRScanner();
			}
		});
	}

	//Load scanner
	private void loadQRScanner(){
		//Call QR Scanner
		IntentIntegrator.initiateScan(ScanMainActivity.this);
	}

	//Called to handle scanner result
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// Handle QR Scan result
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		updateQRCodeResult(result);
	}

	private void updateQRCodeResult(IntentResult result){
		if (result == null){
			//Quit, when user has pressed back button
			return;
		}else{
			HandleQRCodeResultAsyncTask handleQRCodeResultAsyncTask = new HandleQRCodeResultAsyncTask(getParent(), result);
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
			mProgressDialog = ProgressDialog.show(this.mContext, "", "Verifying...", true, false);		
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
			if(mProgressDialog.isShowing())
			{
				mProgressDialog.dismiss();
			}

			if(isDonationSuccess){
				//Donation is complete
				Log.d("BGB", "Donation made! Thank you!");
				Toast.makeText(mContext, "Donation made! Thank you!", Toast.LENGTH_SHORT);
			}else{
				//Donation failed. Print error message
				Log.d("BGB", "Invalid QRCode. Please retry again later.");
				Toast.makeText(mContext, "Invalid QRCode. Please retry again later.", Toast.LENGTH_SHORT);
			}

			//Check if QR reader should start again or not
			if(Business.isAutoQR()){
				loadQRScanner();
			}
			else{
				return;
			}
		}
	}
}
