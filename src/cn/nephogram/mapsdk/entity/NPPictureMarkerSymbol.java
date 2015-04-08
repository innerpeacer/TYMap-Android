package cn.nephogram.mapsdk.entity;

import android.graphics.drawable.Drawable;

import com.esri.core.symbol.PictureMarkerSymbol;

public class NPPictureMarkerSymbol extends PictureMarkerSymbol {
	private static final long serialVersionUID = 1L;

	public NPPictureMarkerSymbol(Drawable d) {
		super(d);
	}

	public void setWidth(float w) {
		super.setWidth(w);
	}

	public void setHeight(float h) {
		super.setHeight(h);
	}
}
