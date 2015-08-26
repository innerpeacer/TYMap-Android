package com.ty.mapproject.ui;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ty.mapsdk.TYCity;

public class UICityListAdapter extends BaseAdapter {

	private Context context;
	private List<TYCity> mData;

	public UICityListAdapter(Context context) {
		this.context = context;
	}

	public void setData(List<TYCity> mData) {
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
		UICityItem ce = null;

		if (null == convertView) {
			ce = new UICityItem(context);
			convertView = ce;
		} else {
			ce = (UICityItem) convertView;
		}

		ce.setCity((TYCity) getItem(position));

		return convertView;
	}

}
