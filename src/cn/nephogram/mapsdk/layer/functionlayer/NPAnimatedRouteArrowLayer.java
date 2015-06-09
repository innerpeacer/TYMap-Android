package cn.nephogram.mapsdk.layer.functionlayer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import cn.nephogram.mapsdk.NPMapView;
import cn.nephogram.mapsdk.entity.NPPictureMarkerSymbol;
import cn.nephogram.mapsdk.route.Vector2;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;

public class NPAnimatedRouteArrowLayer extends GraphicsLayer {
	static final String TAG = NPAnimatedRouteArrowLayer.class.getSimpleName();

	private static final double ANIMATED_ARROW_INTERVAL = 0.005;
	private static final double OFFSET_INCREASING_INTERVAL = 0.002;

	private static final int ANIMATION_INTERVAL = 500;

	private double currentOffset = 0;

	private boolean isShowing = false;
	private List<Polyline> linesToShow = null;

	private Handler routeArrowAnimationHandler = new Handler();
	private Runnable routeArrowAnimationRunnable = new Runnable() {

		@Override
		public void run() {
			if (isShowing) {
				showArrowForLines();
				routeArrowAnimationHandler.postDelayed(
						routeArrowAnimationRunnable, ANIMATION_INTERVAL);
			}

		}
	};

	NPMapView mapView;
	Context context;

	public NPAnimatedRouteArrowLayer(Context context, NPMapView mapView) {
		super(RenderingMode.STATIC);
		this.context = context;
		this.mapView = mapView;
	}

	public void showRouteArrows(List<Polyline> array) {
		isShowing = true;

		routeArrowAnimationHandler.removeCallbacks(routeArrowAnimationRunnable);

		linesToShow = array;
		if (linesToShow == null || linesToShow.size() == 0) {
			linesToShow = null;
			return;
		}

		showArrowForLines();
		routeArrowAnimationHandler.postDelayed(routeArrowAnimationRunnable,
				ANIMATION_INTERVAL);
	}

	public void stopShowingArrow() {
		isShowing = false;

		linesToShow = null;
		removeAll();
	}

	public void showArrowForLines() {
		Log.i(TAG, "showArrowForLines");

		removeAll();

		if (currentOffset >= ANIMATED_ARROW_INTERVAL) {
			currentOffset = 0;
		}

		currentOffset += OFFSET_INCREASING_INTERVAL;

		if (linesToShow != null) {
			for (Polyline polyline : linesToShow) {
				showRouteArrowForLine(polyline, currentOffset);
			}
		}
	}

	private void showRouteArrowForLine(Polyline line, double translation) {
		double interval = ANIMATED_ARROW_INTERVAL * mapView.getScale();
		double translationInterval = translation * mapView.getScale();

		double totalLength = line.calculateLength2D();

		int numSegments = line.getPointCount() - 1;
		int numRoutePoints = (int) ((totalLength - translationInterval) / interval);

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
		// routePointSegmentArray.add(currentSegmentIndex);

		for (int i = 0; i < numRoutePoints; ++i) {
			double offset = interval * i + translationInterval;
			double currentAccumulativeLength = accumulativeLengthArray
					.get(currentSegmentIndex);
			while (currentAccumulativeLength <= offset) {
				currentSegmentIndex++;
				currentAccumulativeLength = accumulativeLengthArray
						.get(currentSegmentIndex);
			}
			routePointSegmentArray.add(currentSegmentIndex - 1);
		}

		int resourceIDNormal = context.getResources().getIdentifier(
				"route_arrow", "drawable", context.getPackageName());
		for (int i = 0; i < numRoutePoints; ++i) {
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
							- currentAccumulativeLength + translationInterval);
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
