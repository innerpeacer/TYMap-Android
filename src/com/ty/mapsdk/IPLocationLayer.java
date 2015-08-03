package com.ty.mapsdk;

import android.graphics.Color;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.ty.mapsdk.TYMapView.TYMapViewMode;

class IPLocationLayer extends GraphicsLayer {
	private MarkerSymbol locationSymbol;

	public IPLocationLayer() {
		super();

		locationSymbol = new SimpleMarkerSymbol(Color.GREEN, 5, STYLE.CIRCLE);
		SimpleRenderer render = new SimpleRenderer(locationSymbol);
		setRenderer(render);
	}

	public void setLcoationSymbol(MarkerSymbol symbol) {

		locationSymbol = symbol;
		SimpleRenderer render = new SimpleRenderer(locationSymbol);
		setRenderer(render);

	}

	public void updateDeviceHeading(double deviceHeading, double initAngle,
			TYMapViewMode mode) {
		switch (mode) {
		case TYMapViewModeDefault:
			locationSymbol.setAngle((float) (deviceHeading + initAngle));
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
		removeAll();
		addGraphic(new Graphic(location, locationSymbol));
	}

	public void removeLocation() {
		removeAll();
	}
}
