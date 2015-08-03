package cn.nephogram.mapsdk;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.util.Log;
import cn.nephogram.data.NPLocalPoint;
import cn.nephogram.mapsdk.entity.NPPictureMarkerSymbol;
import cn.nephogram.mapsdk.route.NPRoutePart;
import cn.nephogram.mapsdk.route.NPRouteResult;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.Proximity2DResult;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.CompositeSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleLineSymbol.STYLE;
import com.esri.core.symbol.Symbol;

/**
 * 路径导航层，用于显示导航路径
 */
class NPRouteLayer extends GraphicsLayer {
	static final String TAG = NPRouteLayer.class.getSimpleName();

	NPMapView mapView;

	NPPictureMarkerSymbol startSymbol;
	NPPictureMarkerSymbol endSymbol;
	NPPictureMarkerSymbol switchSymbol;

	NPLocalPoint startPoint;
	NPLocalPoint endPoint;

	Symbol routeSymbol;

	NPRouteResult routeResult;

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

	public List<Polyline> showRouteResultOnFloor(int floor) {
		removeAll();

		List<Polyline> linesToReturn = showLinesForRouteResultOnFloor(floor);
		showSwitchSymbolForRouteResultOnFloor(floor);
		showStartSymbol(startPoint);
		showEndSymbol(endPoint);
		return linesToReturn;
	}

	public List<Polyline> showRemainingRouteResultOnFloor(int floor,
			NPLocalPoint location) {
		removeAll();

		List<Polyline> linesToReturn = showRemainingLinesForRouteResultOnFloor(
				floor, location);
		showSwitchSymbolForRouteResultOnFloor(floor);
		showStartSymbol(startPoint);
		showEndSymbol(endPoint);

		return linesToReturn;
	}

	public NPRoutePart getNearestRoutePartWithLocation(NPLocalPoint location) {
		NPRoutePart result = null;

		if (routeResult != null) {
			List<NPRoutePart> routePartArray = routeResult
					.getRoutePartsOnFloor(location.getFloor());
			if (routePartArray != null && routePartArray.size() > 0) {
				double nearestDistance = Double.MAX_VALUE;

				Point pos = new Point(location.getX(), location.getY());
				for (NPRoutePart rp : routePartArray) {
					Proximity2DResult pr = GeometryEngine.getNearestCoordinate(
							rp.getRoute(), pos, false);
					double distance = GeometryEngine.distance(
							pr.getCoordinate(), pos, null);
					if (distance < nearestDistance) {
						nearestDistance = distance;
						result = rp;
					}
				}
			}
		}
		return result;
	}

	private List<Polyline> showRemainingLinesForRouteResultOnFloor(int floor,
			NPLocalPoint location) {
		List<Polyline> linesToReturn = new ArrayList<Polyline>();
		NPRoutePart nearestRoutPart = getNearestRoutePartWithLocation(location);

		if (routeResult != null) {
			List<NPRoutePart> routePartArray = routeResult
					.getRoutePartsOnFloor(floor);
			if (routePartArray != null && routePartArray.size() > 0) {
				for (NPRoutePart rp : routePartArray) {
					if (rp == nearestRoutPart) {
						Polyline remainingLine = getRemainLine(rp.getRoute(),
								new Point(location.getX(), location.getY()));
						if (remainingLine != null) {
							addGraphic(new Graphic(remainingLine, routeSymbol));
							linesToReturn.add(remainingLine);
						}
					} else {
						addGraphic(new Graphic(rp.getRoute(), routeSymbol));
						linesToReturn.add(rp.getRoute());
					}
				}
			}
		}

		return linesToReturn;
	}

	private Polyline getRemainLine(Polyline originalLine, Point point) {

		Polyline result = null;

		Proximity2DResult proximityResult = GeometryEngine
				.getNearestCoordinate(originalLine, point, false);
		Point cutPoint = proximityResult.getCoordinate();
		int index = proximityResult.getVertexIndex();

		for (int i = index + 1; i < originalLine.getPointCount(); ++i) {
			if (result == null) {
				result = new Polyline();
				result.startPath(cutPoint);
			}
			result.lineTo(originalLine.getPoint(i));
		}
		return result;
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
					if (rp.getRoute().getPointCount() <= 0) {
						continue;
					}

					if (rp.isFirstPart() && !rp.isLastPart()) {
						addGraphic(new Graphic(rp.getLastPoint(), switchSymbol));
					} else if (!rp.isFirstPart() && rp.isLastPart()) {
						addGraphic(new Graphic(rp.getFirstPoint(), switchSymbol));
					} else if (!rp.isFirstPart() && !rp.isLastPart()) {
						Log.i(TAG, rp.getRoute().getPointCount() + "");
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

	public void setRouteResult(NPRouteResult routeResult) {
		this.routeResult = routeResult;
	}

	public NPRouteResult getRouteResult() {
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
