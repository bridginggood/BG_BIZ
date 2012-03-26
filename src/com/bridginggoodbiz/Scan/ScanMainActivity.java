package com.bridginggoodbiz.Scan;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bg.google.zxing.client.android.integration.IntentIntegrator;
import com.bridginggoodbiz.Business;
import com.bridginggoodbiz.R;
import com.bridginggoodbiz.DB.StatsJSON;

public class ScanMainActivity extends Activity {
	private LoadAsyncTask mLoadAsyncTask;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan_layout);

		initViewControls();
	}

	private void initViewControls(){
		//Textview header
		TextView txtHeader = (TextView)findViewById(R.id.scan_welcome_message_textview);
		txtHeader.setText(Business.getBizName());

		//Button
		Button btnLoadQRReader = (Button)findViewById(R.id.scan_load_qrreader_button);
		btnLoadQRReader.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Business.setAutoQR(true);	//Update check status
				loadQRScanner();
			}
		});
	}

	//Load scanner
	private void loadQRScanner(){
		//Call QR Scanner
		//The result is handled from ScanActivityGroup!
		IntentIntegrator.initiateScan(getParent());
	}

	private void toggleLayout(boolean isLoading){
		if (isLoading){
			findViewById(R.id.scan_progressbar).setVisibility(View.VISIBLE);
			findViewById(R.id.scan_number_textview).setVisibility(View.GONE);
		} else {
			findViewById(R.id.scan_progressbar).setVisibility(View.GONE);
			findViewById(R.id.scan_number_textview).setVisibility(View.VISIBLE);
		}
	}

	private class LoadAsyncTask extends AsyncTask<Context, String, String>{
		//Display progress dialog
		protected void onPreExecute()
		{
			//Show loading
			toggleLayout(true);
		}

		//Load current location
		protected String doInBackground(Context... contexts)
		{
			return StatsJSON.getDailyCount();
		}
		protected void onPostExecute(String dailyCount)
		{
			if (dailyCount == null)
				dailyCount = "00";
			
			TextView txtDailyCount = (TextView)findViewById(R.id.scan_number_textview);
			txtDailyCount.setText(dailyCount);
			toggleLayout(false);
		}
	}

	@Override
	public void onResume(){
		super.onResume();
		
		//Cancel running thread
		if(mLoadAsyncTask != null && (mLoadAsyncTask.getStatus().equals(AsyncTask.Status.RUNNING)
				|| mLoadAsyncTask.getStatus().equals(AsyncTask.Status.PENDING))){
			mLoadAsyncTask.cancel(true);
		}
		
		//Load daily stat
		mLoadAsyncTask = new LoadAsyncTask();
		mLoadAsyncTask.execute();
	}
}
