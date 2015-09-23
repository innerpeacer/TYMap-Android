package com.ty.mapsdk;

import android.graphics.drawable.Drawable;

import com.esri.core.symbol.PictureMarkerSymbol;

/**
 * 图片标识
 * 
 * @author innerpeacer
 * 
 */
public class TYPictureMarkerSymbol extends PictureMarkerSymbol {
	private static final long serialVersionUID = 1L;

	/**
	 * 图片标识构造函数
	 * 
	 * @param d
	 *            图片资源
	 */
	public TYPictureMarkerSymbol(Drawable d) {
		super(d);
	}

	@Override
	public void setWidth(float w) {
		super.setWidth(w);
	}

	@Override
	public void setHeight(float h) {
		super.setHeight(h);
	}
}
