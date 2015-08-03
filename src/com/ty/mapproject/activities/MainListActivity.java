package com.ty.mapproject.activities;

import java.io.File;
import java.util.Map;

import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import cn.nephogram.settings.AppSettings;

import com.ty.mapproject.R;
import com.ty.mapproject.app.FileHelper;
import com.ty.mapsdk.TYMapEnvironment;
import com.ty.mapsdk.TYMapLanguage;

public class MainListActivity extends HelperListActivity {
	static final String TAG = MainListActivity.class.getSimpleName();

	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String mapRootDir = Environment.getExternalStorageDirectory()
				+ "/NeophogramMapProject/Map";
		TYMapEnvironment.setRootDirectoryForMapFiles(mapRootDir);

		TYMapEnvironment.setMapLanguage(TYMapLanguage.TYTraditionalChinese);

		if (!new File(mapRootDir).exists()) {
			copyFileIfNeeded();
		}
		// copyFileIfNeeded();

		AppSettings settings = new AppSettings(this);

		settings.setDefaultCityID("0021");
		settings.setDefaultBuildingID("002100002");

		settings.setDefaultCityID("H852");
		settings.setDefaultBuildingID("H85200001");

		setTitle(getResources().getString(R.string.app_name));
	};

	void copyFileIfNeeded() {
		String sourcePath = "NephogramMapResource";
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
						MapLocationActivity.class)) };
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