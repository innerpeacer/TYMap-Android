package com.ty.mapproject.activities;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.ty.mapdata.TYBuilding;
import com.ty.mapproject.ui.UIBuildingListAdapter;
import com.ty.mapsdk.TYBuildingManager;

public class AllBuildingListAcitivity extends ListActivity {
	static final String TAG = AllBuildingListAcitivity.class.getSimpleName();
	private List<TYBuilding> allBuildings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		allBuildings = TYBuildingManager
				.parseAllBuildingsFromFiles(getBaseContext());
		UIBuildingListAdapter adapter = new UIBuildingListAdapter(this);
		adapter.setData(allBuildings);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		TYBuilding building = allBuildings.get(position);
		Log.i(TAG, building.toString());

		Intent intent = new Intent();
		intent.putExtra("cityID", building.getCityID());
		intent.putExtra("buildingID", building.getBuildingID());
		setResult(RESULT_OK, intent);
		finish();
	}
}
