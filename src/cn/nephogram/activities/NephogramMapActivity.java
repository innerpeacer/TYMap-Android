package cn.nephogram.activities;

import android.os.Bundle;
import cn.nephogram.map.R;

public class NephogramMapActivity extends BaseMapViewActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initContentViewID() {
		contentViewID = R.layout.activity_map_view;
	}
}
