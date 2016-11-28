package com.ty.mapsdk;

import android.content.Context;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.MarkerSymbol;
import com.ty.mapsdk.TYMapView.TYMapViewMode;

class IPLALocationLayer extends GraphicsLayer {
	static final String TAG = IPLALocationLayer.class.getSimpleName();
	private MarkerSymbol locationSymbol;

	private Graphic locationGraphic;
	private int locationGraphicID;

	public IPLALocationLayer(Context context) {
		super();

		TYPictureMarkerSymbol defaultSymbol = new TYPictureMarkerSymbol(context
				.getResources().getDrawable(
						context.getResources().getIdentifier("l7", "drawable",
								context.getPackageName())));
		defaultSymbol.setWidth(80);
		defaultSymbol.setHeight(80);
		locationSymbol = defaultSymbol;

		locationGraphic = new Graphic(null, locationSymbol);
		locationGraphicID = addGraphic(locationGraphic);
	}

	public void setLocationSymbol(MarkerSymbol symbol) {
		locationSymbol = symbol;
		updateGraphic(locationGraphicID, locationSymbol);
	}

	public void updateDeviceHeading(double deviceHeading, double initAngle,
			TYMapViewMode mode) {

		switch (mode) {
		case TYMapViewModeDefault:
			locationSymbol.setAngle((float) (deviceHeading + initAngle));
			updateGraphic(locationGraphicID, locationSymbol);
			break;

		case TYMapViewModeFollowing:
			locationSymbol.setAngle(0);
			break;

		default:
			break;
		}
	}

	public void showLocation(Point location, double deviceHeading,
			double initAngle, TYMapViewMode mode) {
		// removeAll();
		// addGraphic(new Graphic(location, locationSymbol));
		updateGraphic(locationGraphicID, location);
	}

	public void removeLocation() {
		// removeAll();
		Point location = null;
		updateGraphic(locationGraphicID, location);
	}
}
