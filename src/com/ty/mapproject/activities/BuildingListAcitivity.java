package com.ty.mapproject.activities;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.ty.mapproject.ui.UIBuildingListAdapter;
import com.ty.mapsdk.IPMapFileManager;
import com.ty.mapsdk.TYBuilding;

public class BuildingListAcitivity extends ListActivity {

	private List<TYBuilding> allBuildings;
	private String currentCityID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		currentCityID = getIntent().getStringExtra("cityID");
		allBuildings = TYBuilding.parseBuildingFromFiles(getBaseContext(),
				IPMapFileManager.getBuildingJsonPath(currentCityID),
				currentCityID);

		UIBuildingListAdapter adapter = new UIBuildingListAdapter(this);
		adapter.setData(allBuildings);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		TYBuilding building = allBuildings.get(position);

		Intent intent = new Intent(this, BuildingMapViewActivity.class);
		intent.putExtra("cityID", building.getCityID());
		intent.putExtra("buildingID", building.getBuildingID());

		startActivity(intent);
	}
}
