package com.ty.mapproject.activities;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.ty.mapproject.ui.UICityListAdapter;
import com.ty.mapsdk.IPMapFileManager;
import com.ty.mapsdk.TYCity;

public class AllCityListActivity extends ListActivity {

	private List<TYCity> allCities;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		allCities = TYCity.parseCityFromFiles(getBaseContext(),
				IPMapFileManager.getCityJsonPath());

		UICityListAdapter adapter = new UICityListAdapter(this);
		adapter.setData(allCities);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		TYCity city = allCities.get(position);

		Intent intent = new Intent(this, BuildingListAcitivity.class);
		intent.putExtra("cityID", city.getCityID());
		startActivity(intent);
	}
}
