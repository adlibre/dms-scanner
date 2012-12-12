package com.modernsoftware.scanapp;

import com.modernsoftware.scanapp.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ScanAppActivity extends Activity {

	/** Called when the activity is first created. */
	private Button scan_button;
	private static final int QRCODE_REQUEST = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qr_form);

		scan_button = (Button) findViewById(R.id.launch_qr);
		scan_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						"com.scanapp.zxing.client.android.SCAN");
//				intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
				startActivityForResult(intent, QRCODE_REQUEST);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (resultCode == RESULT_OK) {
			String contents = intent.getStringExtra("SCAN_RESULT");
			String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
			showDialog(R.string.result_succeeded, "Format: " + format + "\nContents: " + contents);

		} else if (resultCode == RESULT_CANCELED) {
			showDialog(R.string.result_failed, getString(R.string.result_failed_why));
		}
	}

	private void showDialog(int title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("Ok", null);
		builder.show();
	}
}