package com.bridginggoodbiz.Scan;

import java.util.ArrayList;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
}