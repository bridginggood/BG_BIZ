package com.bridginggoodbiz.Scan;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bg.google.zxing.client.android.integration.IntentIntegrator;
import com.bridginggoodbiz.Business;
import com.bridginggoodbiz.R;

public class ScanMainActivity extends Activity {
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


}
