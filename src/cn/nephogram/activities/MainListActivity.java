package cn.nephogram.activities;

import java.io.File;
import java.util.Map;

import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import cn.nephogram.app.FileHelper;
import cn.nephogram.map.R;
import cn.nephogram.mapsdk.NPMapEnvironment;
import cn.nephogram.mapsdk.NPMapLanguage;
import cn.nephogram.settings.AppSettings;

public class MainListActivity extends HelperListActivity {
	static final String TAG = MainListActivity.class.getSimpleName();

	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String mapRootDir = Environment.getExternalStorageDirectory()
				+ "/NeophogramMapProject/Map";
		NPMapEnvironment.setRootDirectoryForMapFiles(mapRootDir);

		NPMapEnvironment.setMapLanguage(NPMapLanguage.NPTraditionalChinese);

		copyFileIfNeeded();

		AppSettings settings = new AppSettings(this);

		settings.setDefaultCityID("0021");
		settings.setDefaultBuildingID("002100002");

		settings.setDefaultCityID("H852");
		settings.setDefaultBuildingID("H85200001");

		setTitle(getResources().getString(R.string.app_name));

	};

	void copyFileIfNeeded() {
		String sourcePath = "NephogramMapResource";
		String targetPath = NPMapEnvironment.getRootDirectoryForMapFiles();

		Log.i(TAG, "source path: " + sourcePath);
		Log.i(TAG, "target path: " + targetPath);

		FileHelper.deleteFile(new File(targetPath));
		FileHelper.copyFolderFromAsset(this, sourcePath, targetPath);

	}

	protected void constructList() {
		intents = new IntentPair[] {
				new IntentPair(getResources().getString(
						R.string.map_activity_title), new Intent(this,
						NephogramMapActivity.class)),
				new IntentPair(getResources().getString(
						R.string.map_route_activity_title), new Intent(this,
						NephogramMapRouteActivity.class)),
				new IntentPair(getResources().getString(
						R.string.map_callout_activity_title), new Intent(this,
						NephogramMapCalloutActivity.class)),
				new IntentPair(getResources().getString(
						R.string.location_activity_title), new Intent(this,
						NephogramLocationActivity.class)) };
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