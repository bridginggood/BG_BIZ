package com.bridginggoodbiz.Pref;

import java.util.ArrayList;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class PrefActivityGroup extends ActivityGroup {
	
	private ArrayList<View> historyPrefActivityGroup; 		// ArrayList to manage Views.
	private PrefActivityGroup prefActivityGroup; 			// PrefActivityGroup that Activity can access.
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	//Initialize global variables
    	historyPrefActivityGroup = new ArrayList<View>();
    	prefActivityGroup = this;
    	
    	// Start the root activity within the group and get its view
		View view = getLocalActivityManager().startActivity("PrefActivity", new 
				Intent(this,PrefMainActivity.class)		//First page
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView();
		
		// Replace the view of this ActivityGroup
		replaceView(view);
    }
        
    public void changeView(View v)  { // Changing one activity to another on same level
		historyPrefActivityGroup.remove(historyPrefActivityGroup.size()-1);
		historyPrefActivityGroup.add(v);
		setContentView(v);
	}

	public void replaceView(View v) {   // Changing to new activity level.
		historyPrefActivityGroup.add(v);   
		setContentView(v); 
	}   

	public void back() { // On back key press
		if(historyPrefActivityGroup.size() > 1) {   
			historyPrefActivityGroup.remove(historyPrefActivityGroup.size()-1);   
			setContentView(historyPrefActivityGroup.get(historyPrefActivityGroup.size()-1)); 
		} else {   
			finish(); // Finish tabactivity
			Log.d("BgBiz", "onDestroy called from "+this.getClass().toString());
			System.exit(0);
		}   
	}  

	public void onBackPressed() { // Event Handler
		prefActivityGroup.back();   
		return;
	}
    
    //Accessor methods
    public PrefActivityGroup getPrefActivityGroup(){
    	return prefActivityGroup;
    }
    
    public void setPrefActivityGroup(PrefActivityGroup prefActivityGroup){
    	this.prefActivityGroup = prefActivityGroup;  
    }
    
    public ArrayList<View> getHistoryPrefActivityGroup(){
    	return historyPrefActivityGroup;
    }
    
    public void setHistoryPrefActivityGroup(ArrayList<View> historyPrefActivityGroup){
    	this.historyPrefActivityGroup = historyPrefActivityGroup;
    }
    
    public void onDestory(){
		super.onDestroy();
		System.exit(0);
	}
}