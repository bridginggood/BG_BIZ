package com.bridginggoodbiz;


import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.bridginggoodbiz.Pref.PrefActivityGroup;
import com.bridginggoodbiz.Scan.ScanActivityGroup;
import com.bridginggoodbiz.Stats.StatsActivityGroup;

public class MainActivity extends TabActivity {
	private TabHost mTabHost;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		//Apply customed tab layout
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		setupTab("Scan", R.drawable.ic_launcher, new Intent().setClass(this, ScanActivityGroup.class));
		setupTab("Stats", R.drawable.ic_launcher, new Intent().setClass(this, PrefActivityGroup.class));
		setupTab("Preferences", R.drawable.ic_launcher, new Intent().setClass(this, StatsActivityGroup.class));
		
		mTabHost.setCurrentTab(0);
	}

	private void setupTab(final String tag, final int drawableImg, final Intent intent) {
		View tabview = createTabView(mTabHost.getContext(), tag, drawableImg);

		TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview);
		setContent.setContent(intent);

		mTabHost.addTab(setContent);
	}

	private static View createTabView(final Context context, final String text, final int drawableImg) {
		View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
		ImageView img = (ImageView) view.findViewById(R.id.tabsImage);
		img.setImageResource(drawableImg);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}

	public void onDestory(){
		super.onDestroy();
		Log.d("BGB", "onDestroy called");
		System.exit(0);
	}
}