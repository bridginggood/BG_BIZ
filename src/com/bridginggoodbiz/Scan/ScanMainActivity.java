package com.bridginggoodbiz.Scan;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

import com.bg.google.zxing.client.android.integration.IntentIntegrator;
import com.bridginggoodbiz.Business;
import com.bridginggoodbiz.R;

public class ScanMainActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qrreader_layout);

		initViewControls();
	}

	private void initViewControls(){
		CheckBox chkAutoQR = (CheckBox)findViewById(R.id.chkQRAutoStart);
		chkAutoQR.setChecked(Business.isAutoQR());	//Load saved value

		Button btnLoadQRReader = (Button)findViewById(R.id.btnLoadQRReader);
		btnLoadQRReader.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox chkAutoQR = (CheckBox)findViewById(R.id.chkQRAutoStart);
				Business.setAutoQR(chkAutoQR.isChecked());	//Update check status
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
