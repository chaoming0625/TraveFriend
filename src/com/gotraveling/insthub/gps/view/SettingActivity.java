package com.gotraveling.insthub.gps.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.gotraveling.insthub.BeeFramework.activity.BaseActivity;
import com.insthub.ecmobile.R;

public class SettingActivity extends FragmentActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setting);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction beginTransaction = fragmentManager
				.beginTransaction();
		SettingFragment fragment = new SettingFragment();
		beginTransaction.replace(R.id.activity_setting_layout, fragment);
		beginTransaction.commit();
	}
}
