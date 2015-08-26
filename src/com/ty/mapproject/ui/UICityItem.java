package com.ty.mapproject.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ty.mapproject.R;
import com.ty.mapsdk.TYCity;

public class UICityItem extends RelativeLayout {
	private Context context;

	private TYCity city;

	private TextView cityName;

	public UICityItem(Context context) {
		super(context);
		this.context = context;
		initLayout();
	}

	public UICityItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initLayout();
	}

	private void initLayout() {
		LayoutInflater.from(context).inflate(R.layout.list_item_city, this,
				true);
		cityName = (TextView) findViewById(R.id.city_name);
	}

	private void showLayout() {
		cityName.setText(city.getSname());
	}

	public void setCity(TYCity city) {
		this.city = city;
		showLayout();
	}

}
