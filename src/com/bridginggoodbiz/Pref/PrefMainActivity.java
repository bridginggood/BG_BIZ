package com.bridginggoodbiz.Pref;

import java.text.DecimalFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bridginggoodbiz.BusinessStore;
import com.bridginggoodbiz.LoginActivity;
import com.bridginggoodbiz.R;
import com.bridginggoodbiz.DB.StatsJSON;

public class PrefMainActivity extends Activity{
	private static final int SIZE_OF_ACCOUNT_DETAIL_ARRAY = 3;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pref_layout);

		initButtons();
		
		LoadAsyncTask loadAsyncTask = new LoadAsyncTask();
		loadAsyncTask.execute();
	}

	private void initButtons(){
		Button btnLogout = (Button)findViewById((R.id.pref_logout_button));
		btnLogout.setOnClickListener(new  OnClickListener() {
			@Override
			public void onClick(View v) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
				builder.setMessage("Are you sure?")
				.setTitle("Logout")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, final int id) {
						BusinessStore.clearSession(getParent());
						//Start login activity
						startActivity(new Intent().setClass(PrefMainActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
						finish();	//Finish this activity
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, final int id) {
						dialog.cancel();
					}
				});
				final AlertDialog alert = builder.create();
				alert.show();

			}
		});
	}
	
	private void toggleLayout(boolean isLoading){
		if (isLoading){
			findViewById(R.id.pref_progressbar).setVisibility(View.VISIBLE);
			findViewById(R.id.pref_account_detail_layout).setVisibility(View.GONE);
		} else {
			findViewById(R.id.pref_progressbar).setVisibility(View.GONE);
			findViewById(R.id.pref_account_detail_layout).setVisibility(View.VISIBLE);
		}
	}
	
	private class LoadAsyncTask extends AsyncTask<Context, String[], String[]>{
		//Display progress dialog
		protected void onPreExecute()
		{
			//Show loading
			toggleLayout(true);
		}

		//Load current location
		protected String[] doInBackground(Context... contexts)
		{
			return StatsJSON.getAccountDetail();
		}
		protected void onPostExecute(String[] arDetail)
		{
			if(arDetail == null || arDetail.length != SIZE_OF_ACCOUNT_DETAIL_ARRAY){
				//TODO: unable to load
			} else {
				TextView txtCharity = (TextView)findViewById(R.id.pref_account_supporting_charity_textview);
				TextView txtDate = (TextView)findViewById(R.id.pref_account_date_textview);
				TextView txtDonationAmount = (TextView)findViewById(R.id.pref_account_donation_amount_textview);
				
				String valCharity = arDetail[0];
				String valDate = arDetail[1];
				
				//Change display format
				String valDonationAmount = arDetail[2];
				DecimalFormat dFormat = new DecimalFormat("#0.00");
				valDonationAmount = "$"+dFormat.format(Float.parseFloat(valDonationAmount));
				valDonationAmount = valDonationAmount+" per scan";
				
				txtCharity.setText(valCharity);
				txtDate.setText(valDate);
				txtDonationAmount.setText(valDonationAmount);
			}
			
			toggleLayout(false);
		}
	}
}
