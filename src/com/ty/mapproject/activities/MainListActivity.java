package com.ty.mapproject.activities;

import java.io.File;
import java.util.Map;

import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.ty.mapproject.R;
import com.ty.mapproject.settings.AppSettings;
import com.ty.mapsdk.TYMapEnvironment;

public class MainListActivity extends HelperListActivity {
	static final String TAG = MainListActivity.class.getSimpleName();

	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String mapRootDir = Environment.getExternalStorageDirectory()
				+ "/TuYaMap/MapResource";
		TYMapEnvironment.setRootDirectoryForMapFiles(mapRootDir);

		if (!new File(mapRootDir).exists()) {
			copyFileIfNeeded();
		}
		// copyFileIfNeeded();

		AppSettings settings = new AppSettings(this);
		settings.setDefaultCityID("0021");
		settings.setDefaultBuildingID("00210100");
		settings.setDefaultBuildingID("002100002");

		setTitle(getResources().getString(R.string.app_name));
	};

	void copyFileIfNeeded() {
		String sourcePath = "MapResource";
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
						MapRouteActivity.class)),
				new IntentPair(getResources().getString(
						R.string.map_callout_activity_title), new Intent(this,
						MapCalloutActivity.class)),
				new IntentPair(getResources().getString(
						R.string.location_activity_title), new Intent(this,
						MapLocationActivity.class)),
				new IntentPair(getResources().getString(
						R.string.city_activity_title), new Intent(this,
						AllCityListActivity.class)) };
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) l
				.getItemAtPosition(position);

		Intent intent = (Intent) map.get("Intent");
		startActivity(intent);
	}

}