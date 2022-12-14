package com.ty.mapsdk;

import android.content.Context;
import android.graphics.Color;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.CompositeSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleLineSymbol.STYLE;
import com.esri.core.symbol.Symbol;

class IPLARouteHintLayer extends GraphicsLayer {
	Context context;
	Symbol routeHintSymbol;

	public IPLARouteHintLayer(Context context) {
		super(RenderingMode.STATIC);
		this.context = context;

		routeHintSymbol = createRouteHintSymbol();
	}

	public void showRouteHint(Polyline line) {
		removeAll();
		addGraphic(new Graphic(line, routeHintSymbol));

		int resourceIDNormal = context.getResources().getIdentifier(
				"route_hint_arrow", "drawable", context.getPackageName());
		TYPictureMarkerSymbol pms = new TYPictureMarkerSymbol(context
				.getResources().getDrawable(resourceIDNormal));
		pms.setWidth(10);
		pms.setHeight(10);
		Point start = line.getPoint(0);
		Point end = line.getPoint(line.getPointCount() - 1);

		IPRTRouteVector2 v = new IPRTRouteVector2(end.getX() - start.getX(), end.getY()
				- start.getY());
		pms.setAngle((float) v.getAngle());

		addGraphic(new Graphic(start, pms));
		addGraphic(new Graphic(end, pms));
	}

	public Symbol createRouteHintSymbol() {
		CompositeSymbol cs = new CompositeSymbol();

		SimpleLineSymbol sls1 = new SimpleLineSymbol(Color.argb(255, 255, 255,
				255), 9);
		sls1.setStyle(STYLE.SOLID);
		cs.add(sls1);

		SimpleLineSymbol sls2 = new SimpleLineSymbol(
				Color.argb(255, 30, 255, 0), 6);
		sls2.setStyle(STYLE.SOLID);
		cs.add(sls2);

		return cs;
	}
}
