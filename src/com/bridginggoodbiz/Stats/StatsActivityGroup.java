package com.bridginggoodbiz.Stats;

import java.util.ArrayList;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class StatsActivityGroup extends ActivityGroup {
	
	private ArrayList<View> historyStatsActivityGroup; 		// ArrayList to manage Views.
	private StatsActivityGroup statsActivityGroup; 			// ScanActivityGroup that Activity can access.
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	//Initialize global variables
    	historyStatsActivityGroup = new ArrayList<View>();
    	statsActivityGroup = this;
    	
    	// Start the root activity within the group and get its view
		View view = getLocalActivityManager().startActivity("StatsActivity", new 
				Intent(this,StatsMainActivity.class)		//First page
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView();
		
		// Replace the view of this ActivityGroup
		replaceView(view);
    }
        
    public void changeView(View v)  { // Changing one activity to another on same level
		historyStatsActivityGroup.remove(historyStatsActivityGroup.size()-1);
		historyStatsActivityGroup.add(v);
		setContentView(v);
	}

	public void replaceView(View v) {   // Changing to new activity level.
		historyStatsActivityGroup.add(v);   
		setContentView(v); 
	}   

	public void back() { // On back key press
		if(historyStatsActivityGroup.size() > 1) {   
			historyStatsActivityGroup.remove(historyStatsActivityGroup.size()-1);   
			setContentView(historyStatsActivityGroup.get(historyStatsActivityGroup.size()-1)); 
		} else {   
			finish(); // Finish tabactivity
			Log.d("BgBiz", "onDestroy called from "+this.getClass().toString());
			System.exit(0);
		}   
	}  

	public void onBackPressed() { // Event Handler
		statsActivityGroup.back();   
		return;
	}
    
    //Accessor methods
    public StatsActivityGroup getStatsActivityGroup(){
    	return statsActivityGroup;
    }
    
    public void setStatsActivityGroup(StatsActivityGroup statsActivityGroup){
    	this.statsActivityGroup = statsActivityGroup;  
    }
    
    public ArrayList<View> getHistoryStatsActivityGroup(){
    	return historyStatsActivityGroup;
    }
    
    public void setHistoryStatsActivityGroup(ArrayList<View> historyStatsActivityGroup){
    	this.historyStatsActivityGroup = historyStatsActivityGroup;
    }
    
    public void onDestory(){
		super.onDestroy();
		System.exit(0);
	}
}