package com.ty.mapproject.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ty.mapproject.R;
import com.ty.mapsdk.TYBuilding;

public class UIBuildingItem extends RelativeLayout {
	private Context context;

	private TYBuilding building;

	private TextView buildingName;

	public UIBuildingItem(Context context) {
		super(context);
		this.context = context;
		initLayout();
	}

	public UIBuildingItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initLayout();
	}

	private void initLayout() {
		LayoutInflater.from(context).inflate(R.layout.list_item_building, this,
				true);
		buildingName = (TextView) findViewById(R.id.building_name);
	}

	private void showLayout() {
		buildingName.setText(building.getName());
	}

	public void setBuilding(TYBuilding building) {
		this.building = building;
		showLayout();
	}

}
