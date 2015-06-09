package cn.nephogram.mapsdk.layer.functionlayer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import cn.nephogram.mapsdk.NPMapView;
import cn.nephogram.mapsdk.entity.NPPictureMarkerSymbol;
import cn.nephogram.mapsdk.route.Vector2;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;

public class NPRouteArrowLayer extends GraphicsLayer {
	private static final double ARROW_INTERVAL = 0.005;

	NPMapView mapView;
	Context context;

	public NPRouteArrowLayer(Context context, NPMapView mapView) {
		super(RenderingMode.STATIC);
		this.context = context;
		this.mapView = mapView;
	}

	public void showRouteArrows(List<Polyline> array) {
		removeAll();
		for (Polyline polyline : array) {
			showRouteArrowForLine(polyline);
		}
	}

	private void showRouteArrowForLine(Polyline line) {
		double interval = ARROW_INTERVAL * mapView.getScale();
		double totalLength = line.calculateLength2D();

		int numSegments = line.getPointCount() - 1;
		int numRoutePoints = (int) (totalLength / interval);

		if (numRoutePoints < 1) {
			return;
		}

		Point currentStart = null;
		Point currentEnd = null;

		double accumulativeLength = 0;
		List<Double> accumulativeLengthArray = new ArrayList<Double>();
		accumulativeLengthArray.add(accumulativeLength);
		for (int i = 0; i < numSegments; ++i) {
			currentStart = line.getPoint(i);
			currentEnd = line.getPoint(i + 1);
			double currentLength = GeometryEngine.distance(currentStart,
					currentEnd, null);
			accumulativeLength += currentLength;
			accumulativeLengthArray.add(accumulativeLength);
		}

		int currentSegmentIndex = 0;
		List<Integer> routePointSegmentArray = new ArrayList<Integer>();
		routePointSegmentArray.add(currentSegmentIndex);

		for (int i = 1; i < numRoutePoints; ++i) {
			double offset = interval * i;
			double currentAccumulativeLength = accumulativeLengthArray
					.get(currentSegmentIndex);
			while (currentAccumulativeLength < offset) {
				currentSegmentIndex++;
				currentAccumulativeLength = accumulativeLengthArray
						.get(currentSegmentIndex);
			}
			routePointSegmentArray.add(currentSegmentIndex - 1);
		}

		int resourceIDNormal = context.getResources().getIdentifier(
				"route_arrow", "drawable", context.getPackageName());
		for (int i = 1; i < numRoutePoints; ++i) {
			int currentSegment = routePointSegmentArray.get(i);

			currentStart = line.getPoint(currentSegment);
			currentEnd = line.getPoint(currentSegment + 1);

			double currentSegmentLength = GeometryEngine.distance(currentStart,
					currentEnd, null);
			double currentAccumulativeLength = accumulativeLengthArray
					.get(currentSegment);

			Vector2 v = new Vector2(currentEnd.getX() - currentStart.getX(),
					currentEnd.getY() - currentStart.getY());
			double currentAngle = v.getAngle();

			Point point = getPointWithSegmentLengthAndOffset(currentStart,
					currentEnd, currentSegmentLength, i * interval
							- currentAccumulativeLength);
			NPPictureMarkerSymbol pms = new NPPictureMarkerSymbol(context
					.getResources().getDrawable(resourceIDNormal));
			pms.setWidth(6f);
			pms.setHeight(4.5f);
			pms.setAngle((float) currentAngle);
			addGraphic(new Graphic(point, pms));
		}

	}

	private Point getPointWithSegmentLengthAndOffset(Point start, Point end,
			double length, double offset) {
		double scale = offset / length;

		double x = start.getX() * (1 - scale) + end.getX() * scale;
		double y = start.getY() * (1 - scale) + end.getY() * scale;

		return new Point(x, y);
	}
}
