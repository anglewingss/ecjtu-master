package com.ecjtu.assistant.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.ecjtu.assistant.R;


public class LogoActicity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.logo);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				stop();
			}
		}, 2200);
	}

	public void stop() {
		Intent intent = new Intent(LogoActicity.this, LoginActivity.class);
		startActivity(intent);
		LogoActicity.this.finish();
		finish();
	}



}
