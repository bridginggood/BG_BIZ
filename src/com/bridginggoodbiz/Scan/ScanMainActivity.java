package com.bridginggoodbiz.Scan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bridginggoodbiz.R;
import com.google.zxing.bg.client.android.integration.IntentIntegrator;
import com.google.zxing.bg.client.android.integration.IntentResult;

public class ScanMainActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qrreader_layout);
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
		//updateQRCodeResult(result);
	}
}
