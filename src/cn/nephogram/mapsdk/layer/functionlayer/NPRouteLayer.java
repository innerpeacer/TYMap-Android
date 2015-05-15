package cn.nephogram.mapsdk.layer.functionlayer;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import cn.nephogram.data.NPLocalPoint;
import cn.nephogram.mapsdk.NPMapView;
import cn.nephogram.mapsdk.entity.NPPictureMarkerSymbol;
import cn.nephogram.mapsdk.route.NPRoutePart;
import cn.nephogram.mapsdk.route.NPRouteResultV2;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.CompositeSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleLineSymbol.STYLE;
import com.esri.core.symbol.Symbol;

/**
 * 路径导航层，用于显示导航路径
 */
public class NPRouteLayer extends GraphicsLayer {
	static final String TAG = NPRouteLayer.class.getSimpleName();

	NPMapView mapView;

	NPPictureMarkerSymbol startSymbol;
	NPPictureMarkerSymbol endSymbol;
	NPPictureMarkerSymbol switchSymbol;

	NPLocalPoint startPoint;
	NPLocalPoint endPoint;

	Symbol routeSymbol;

	NPRouteResultV2 routeResult;

	public NPRouteLayer(NPMapView mapView) {
		super();

		this.mapView = mapView;
		routeSymbol = createRouteSymbol();
	}

	public void reset() {
		removeAll();
		routeResult = null;
		startPoint = null;
		endPoint = null;
	}

	public void showRouteResultOnFloor(int floor) {
		removeAll();

		List<Polyline> linesToReturn = showLinesForRouteResultOnFloor(floor);

		showSwitchSymbolForRouteResultOnFloor(floor);

		showStartSymbol(startPoint);
		showEndSymbol(endPoint);

	}

	private List<Polyline> showLinesForRouteResultOnFloor(int floor) {
		List<Polyline> linesToReturn = new ArrayList<Polyline>();

		if (routeResult != null) {
			List<NPRoutePart> routePartArray = routeResult
					.getRoutePartsOnFloor(floor);
			if (routePartArray != null && routePartArray.size() > 0) {
				for (NPRoutePart rp : routePartArray) {
					addGraphic(new Graphic(rp.getRoute(), routeSymbol));
					linesToReturn.add(rp.getRoute());
				}
			}
		}
		return linesToReturn;
	}

	private void showSwitchSymbolForRouteResultOnFloor(int floor) {
		if (routeResult != null) {
			List<NPRoutePart> routePartArray = routeResult
					.getRoutePartsOnFloor(floor);
			if (routePartArray != null && routePartArray.size() > 0) {
				for (NPRoutePart rp : routePartArray) {
					if (rp.isFirstPart() && !rp.isLastPart()) {
						addGraphic(new Graphic(rp.getLastPoint(), switchSymbol));
					} else if (!rp.isFirstPart() && rp.isLastPart()) {
						addGraphic(new Graphic(rp.getFirstPoint(), switchSymbol));
					} else if (!rp.isFirstPart() && !rp.isLastPart()) {
						addGraphic(new Graphic(rp.getFirstPoint(), switchSymbol));
						addGraphic(new Graphic(rp.getLastPoint(), switchSymbol));
					}
				}
			}
		}
	}

	public void showStartSymbol(NPLocalPoint sp) {
		if (sp != null
				&& sp.getFloor() == mapView.getCurrentMapInfo()
						.getFloorNumber()) {

			addGraphic(new Graphic(new Point(sp.getX(), sp.getY()), startSymbol));
		}
	}

	public void showEndSymbol(NPLocalPoint ep) {
		if (ep != null
				&& ep.getFloor() == mapView.getCurrentMapInfo()
						.getFloorNumber()) {
			addGraphic(new Graphic(new Point(ep.getX(), ep.getY()), endSymbol));
		}
	}

	public void showSwitchSymbol(NPLocalPoint sp) {
		if (sp != null
				&& sp.getFloor() == mapView.getCurrentMapInfo()
						.getFloorNumber()) {
			addGraphic(new Graphic(new Point(sp.getX(), sp.getY()),
					switchSymbol));
		}
	}

	public void setStartSymbol(NPPictureMarkerSymbol startSymbol) {
		this.startSymbol = startSymbol;
	}

	public void setEndSymbol(NPPictureMarkerSymbol endSymbol) {
		this.endSymbol = endSymbol;
	}

	public void setSwitchSymbol(NPPictureMarkerSymbol switchSymbol) {
		this.switchSymbol = switchSymbol;
	}

	public NPPictureMarkerSymbol getStartSymbol() {
		return startSymbol;
	}

	public NPPictureMarkerSymbol getEndSymbol() {
		return endSymbol;
	}

	public NPPictureMarkerSymbol getSwitchSymbol() {
		return switchSymbol;
	}

	public NPLocalPoint getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(NPLocalPoint startPoint) {
		this.startPoint = startPoint;
	}

	public NPLocalPoint getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(NPLocalPoint endPoint) {
		this.endPoint = endPoint;
	}

	public void setRouteResult(NPRouteResultV2 routeResult) {
		this.routeResult = routeResult;
	}

	public NPRouteResultV2 getRouteResult() {
		return routeResult;
	}

	public void setRouteSymbol(Symbol routeSymbol) {
		this.routeSymbol = routeSymbol;
	}

	private CompositeSymbol createRouteSymbol() {
		CompositeSymbol cs = new CompositeSymbol();

		SimpleLineSymbol sls1 = new SimpleLineSymbol(Color.argb(255, 206, 53,
				70), 8, STYLE.SOLID);
		cs.add(sls1);

		SimpleLineSymbol sls2 = new SimpleLineSymbol(Color.argb(255, 255, 89,
				89), 6, STYLE.SOLID);
		cs.add(sls2);
		return cs;
	}

	public Symbol getRouteSymbol() {
		return routeSymbol;
	}

}
