package com.bridginggoodbiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashController extends Activity{

	private static final int DELAY_MILLS = 3000;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);

		Handler handlerLoading = new Handler();

		handlerLoading.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent mainController = new Intent(SplashController.this, MainController.class);
				SplashController.this.startActivity(mainController);
				SplashController.this.finish();
			}
		}, DELAY_MILLS);
	}
}
