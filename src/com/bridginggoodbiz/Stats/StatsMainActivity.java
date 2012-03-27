package com.bridginggoodbiz.Stats;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.bridginggoodbiz.CONST;
import com.bridginggoodbiz.R;
import com.bridginggoodbiz.DB.StatsJSON;

public class StatsMainActivity extends Activity{
	private int mStatType;
	private LoadAsyncTask mLoadAsyncTask ;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats_layout);

		mStatType = CONST.STATS_RESULT_COUNT_DAILY;
		initButtons();
		loadStat();
	}

	private void loadStat(){
		if(mLoadAsyncTask!= null 
				&& (mLoadAsyncTask.getStatus().equals(AsyncTask.Status.RUNNING)
						|| mLoadAsyncTask.getStatus().equals(AsyncTask.Status.PENDING))){
			mLoadAsyncTask.cancel(true);	//Cancel running thread
		}
		//Run thread
		mLoadAsyncTask = new LoadAsyncTask(mStatType);
		mLoadAsyncTask.execute();
	}

	private void initList(ArrayList<DataRecord> dataArrayList){
		StatsDataListAdapter listAdapter = new StatsDataListAdapter(this, R.layout.stats_datalist_cell, dataArrayList);
		ListView listview = (ListView)findViewById(R.id.stats_data_listview);
		listview.setAdapter(listAdapter);
	}

	private void initButtons(){
		Button btnDaily = (Button)findViewById(R.id.stats_navigation_daily_button);
		Button btnWeekly = (Button)findViewById(R.id.stats_navigation_weekly_button);
		Button btnMonthly = (Button)findViewById(R.id.stats_navigation_monthly_button);

		//Daily button
		btnDaily.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mStatType = CONST.STATS_RESULT_COUNT_DAILY;
				loadStat();
				updateButtonStatus();
			}
		});
		
		//Weekly button
		btnWeekly.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mStatType = CONST.STATS_RESULT_COUNT_WEEKLY;
				loadStat();
				updateButtonStatus();
			}
		});
		
		//Monthly button
		btnMonthly.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mStatType = CONST.STATS_RESULT_COUNT_MONTHLY;
				loadStat();
				updateButtonStatus();
			}
		});
	}

	//Change button status on UI
	private void updateButtonStatus(){
		Button btnDaily = (Button)findViewById(R.id.stats_navigation_daily_button);
		Button btnWeekly = (Button)findViewById(R.id.stats_navigation_weekly_button);
		Button btnMonthly = (Button)findViewById(R.id.stats_navigation_monthly_button);

		switch(mStatType){
		case CONST.STATS_RESULT_COUNT_DAILY:
			btnDaily.setEnabled(false);
			btnWeekly.setEnabled(true);
			btnMonthly.setEnabled(true);
			break;
		case CONST.STATS_RESULT_COUNT_WEEKLY:
			btnDaily.setEnabled(true);
			btnWeekly.setEnabled(false);
			btnMonthly.setEnabled(true);
			break;
		case CONST.STATS_RESULT_COUNT_MONTHLY:
			btnDaily.setEnabled(true);
			btnWeekly.setEnabled(true);
			btnMonthly.setEnabled(false);
			break;
		}
	}

	private void toggleLayout(boolean isLoading){
		if(isLoading){
			findViewById(R.id.stats_data_listview).setVisibility(View.GONE);
			findViewById(R.id.stats_progressbar).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.stats_progressbar).setVisibility(View.GONE);
			findViewById(R.id.stats_data_listview).setVisibility(View.VISIBLE);
		}
	}

	private class LoadAsyncTask extends AsyncTask<Context, ArrayList<DataRecord>, ArrayList<DataRecord>>{
		private int mResultCount;

		public LoadAsyncTask(int resultCount){
			this.mResultCount = resultCount;
		}
		//Display progress dialog
		protected void onPreExecute()
		{
			toggleLayout(true);
		}

		protected ArrayList<DataRecord> doInBackground(Context... contexts)
		{
			return StatsJSON.getStatsDaily(this.mResultCount);
		}
		protected void onPostExecute(final ArrayList<DataRecord> arData)
		{
			initList(arData);
			toggleLayout(false);
		}
	}
}
