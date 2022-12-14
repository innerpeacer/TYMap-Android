package com.ty.mapproject.activities;

import java.io.File;
import java.util.Map;

import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.ty.mapproject.R;
import com.ty.mapproject.settings.TYUserDefaults;
import com.ty.mapsdk.TYMapEnvironment;

public class MainListActivity extends HelperListActivity {
	static final String TAG = MainListActivity.class.getSimpleName();

	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Log.i(TAG, "Generate License:");
		// Base64.encodeBase64String(new byte[] { 1, 2, 3 });
		// Log.i(TAG, TYLicenseGenerator.generateBase64License40(
		// "ty4e13f85911a44a75", "00210100", "20180801"));

		String mapRootDir = Environment.getExternalStorageDirectory()
				+ "/TuYaMap/MapEncrypted";
		TYMapEnvironment.setRootDirectoryForMapFiles(mapRootDir);

		if (!new File(mapRootDir).exists()) {
			copyFileIfNeeded();
		}
		// copyFileIfNeeded();

		LicenseManager.loadContent(FileHelper.readStringFromAsset(this,
				"Licenses.json"));
		TYUserDefaults settings = new TYUserDefaults(this);

		// if (settings.getDefaultBuildingID() == null) {
		settings.setDefaultCityID("0021");
		settings.setDefaultBuildingID("00210025");
		// }
		setTitle(getResources().getString(R.string.app_name));
	};

	void copyFileIfNeeded() {

		String sourcePath = "MapEncrypted";
		String targetPath = TYMapEnvironment.getRootDirectoryForMapFiles();

		Log.i(TAG, "source path: " + sourcePath);
		Log.i(TAG, "target path: " + targetPath);

		FileHelper.deleteFile(new File(targetPath));
		FileHelper.copyFolderFromAsset(this, sourcePath, targetPath);
	}

	protected void constructList() {
		intents = new IntentPair[] {
				new IntentPair(getResources().getString(
						R.string.map_activity_title), new Intent(this,
						MapActivity.class)),
				new IntentPair(getResources().getString(
						R.string.map_route_activity_title), new Intent(this,
						MapOfflineRouteActivity.class)),
				new IntentPair(getResources().getString(
						R.string.switch_map_activity_title), new Intent(this,
						SwitchMapActivity.class)),
				new IntentPair(getResources().getString(
						R.string.map_callout_activity_title), new Intent(this,
						MapCalloutActivity.class)),
				new IntentPair(getResources().getString(
						R.string.location_activity_title), new Intent(this,
						MapLocationActivity.class)),
				new IntentPair(getResources().getString(
						R.string.path_calibraion_activity_title), new Intent(
						this, PathCalibrationActivity.class)),
				new IntentPair(getResources().getString(
						R.string.city_activity_title), new Intent(this,
						AllCityListActivity.class)),
				new IntentPair("?????????",
						new Intent(this, TraceLayerActivity.class)) };
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) l
				.getItemAtPosition(position);

		Intent intent = (Intent) map.get("Intent");
		startActivity(intent);
	}

	static {
		System.loadLibrary("TYMapSDK");
	}

}