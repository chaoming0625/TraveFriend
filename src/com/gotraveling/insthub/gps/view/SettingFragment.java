package com.gotraveling.insthub.gps.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.gotraveling.insthub.gps.utils.FloatPrefs;
import com.gotraveling.insthub.gps.utils.NaviPrefs;
import com.gotraveling.insthub.gps.utils.PreferenceFragment;
import com.insthub.ecmobile.R;

/**
 * Created by ChaoMing on 2015/4/20.
 */
public class SettingFragment extends PreferenceFragment implements
		OnSharedPreferenceChangeListener, DataFlush {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getPreferenceManager().setSharedPreferencesName("LogGPS");
		addPreferencesFromResource(R.xml.preferences);

		PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences,
				false);
		ListPreference tmp = (ListPreference) findPreference("FloatStyle");
		tmp.setSummary(tmp.getEntry());
		tmp = (ListPreference) findPreference("FloatBackground");
		tmp.setSummary(tmp.getEntry());
		tmp = (ListPreference) findPreference("FloatTextColor");
		tmp.setSummary(tmp.getEntry());
		tmp = (ListPreference) findPreference("FloatMode");
		tmp.setSummary(tmp.getEntry());
		tmp = (ListPreference) findPreference("UnitSpeed");
		tmp.setSummary(tmp.getEntry());
		tmp = (ListPreference) findPreference("UnitDistance");
		tmp.setSummary(tmp.getEntry());
		tmp = (ListPreference) findPreference("UnitAltitude");
		tmp.setSummary(tmp.getEntry());
	}

	@Override
	public void onDataChange(String act) {}

	@Override
	public void onSelected() {}

	@Override
	public void onResume() {
		super.onResume();
		getPreferenceManager().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
		CheckBoxPreference pr = (CheckBoxPreference) getPreferenceManager()
				.findPreference("FloatShow");
		if (pr != null) {
			pr.setChecked(NaviPrefs.mFloatShow);
		}
	}

	@Override
	public void onPause() {
		getPreferenceManager().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}


	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Preference connectionPref = findPreference(key);
		if (connectionPref instanceof ListPreference) {
			ListPreference tmp = (ListPreference) connectionPref;
			tmp.setSummary(tmp.getEntry());
		}
		if ("FloatShow".equals(key)) {
			NaviPrefs.mFloatShow = sharedPreferences.getBoolean(key, true);
			Activity parent = getActivity();
			Intent intent = new Intent(parent, FloatWindowService.class);
			if (NaviPrefs.mFloatShow) {
				intent.setAction(FloatWindowService.Cmd_Start);
				parent.startService(intent);
			} else {
				intent.setAction(FloatWindowService.Cmd_Stop);
				parent.stopService(intent);
			}
		}
		if ("FloatStyle".equals(key) || "FloatBackground".equals(key)
				|| "FloatTextColor".equals(key)) {
			FloatPrefs.Load(this.getActivity());
			if (NaviPrefs.mFloatShow) {
				Activity parent = getActivity();
				Intent intent = new Intent(parent, FloatWindowService.class);
				intent.setAction(FloatWindowService.Cmd_Restart);
				parent.startService(intent);
			}
		}
		if ("FloatMode".equals(key)) {
			FloatPrefs.Load(this.getActivity());
			if (NaviPrefs.mFloatShow) {
				Activity parent = getActivity();
				Intent intent = new Intent(parent, FloatWindowService.class);
				intent.setAction(FloatWindowService.Cmd_Stop);
				parent.stopService(intent);

				intent = new Intent(parent, FloatWindowService.class);
				intent.setAction(FloatWindowService.Cmd_Start);
				parent.startService(intent);
			}
		} else {
			NaviPrefs.Load(this.getActivity());
		}
	}
}