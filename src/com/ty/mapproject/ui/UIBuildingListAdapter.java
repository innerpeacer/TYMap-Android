package com.ty.mapproject.ui;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ty.mapdata.TYBuilding;

public class UIBuildingListAdapter extends BaseAdapter {

	private Context context;
	private List<TYBuilding> mData;

	public UIBuildingListAdapter(Context context) {
		this.context = context;
	}

	public void setData(List<TYBuilding> mData) {
		this.mData = mData;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	public int index;

	public void setSelect(int index) {
		this.index = index;
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		UIBuildingItem ce = null;

		if (null == convertView) {
			ce = new UIBuildingItem(context);
			convertView = ce;
		} else {
			ce = (UIBuildingItem) convertView;
		}

		ce.setBuilding((TYBuilding) getItem(position));

		return convertView;
	}
}
