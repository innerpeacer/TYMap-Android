package com.ty.mapsdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.util.Log;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.Proximity2DResult;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.symbol.TextSymbol.HorizontalAlignment;
import com.esri.core.symbol.TextSymbol.VerticalAlignment;
import com.ty.mapdata.TYLocalPoint;

public class TYLitsoTraceLayer extends GraphicsLayer {
	static final String TAG = TYLitsoTraceLayer.class.getSimpleName();
	private int targetFloor;

	private int currentFloor;
	private int currentStartIndex;

	SimpleLineSymbol outlineSymbol;
	SimpleLineSymbol traceSymbol1;
	SimpleLineSymbol traceSymbol2;

	SimpleLineSymbol lineSymbol;
	SimpleMarkerSymbol pointSymbol;

	List<Integer> traceFloorArray = new ArrayList<Integer>();
	List<Integer> traceStartIndexArray = new ArrayList<Integer>();
	List<List<TYTraceLocalPoint>> tracePointArray = new ArrayList<List<TYTraceLocalPoint>>();
	List<List<double[]>> traceCoordinateArray = new ArrayList<List<double[]>>();
	List<Polyline> traceLineArray = new ArrayList<Polyline>();

	Map<String, TYLocalPoint> themeLocations = new HashMap<String, TYLocalPoint>();

	public TYLitsoTraceLayer() {
		outlineSymbol = new SimpleLineSymbol(Color.WHITE, 1.0f);

		pointSymbol = new SimpleMarkerSymbol(Color.RED, 10, STYLE.CIRCLE);
		pointSymbol.setOutline(outlineSymbol);
		setRenderer(new SimpleRenderer(pointSymbol));

		lineSymbol = new SimpleLineSymbol(Color.RED, 2.0f,
				com.esri.core.symbol.SimpleLineSymbol.STYLE.DASH);

		traceSymbol1 = new SimpleLineSymbol(Color.WHITE, 8.0f);
		traceSymbol2 = new SimpleLineSymbol(Color.argb(255, 255, 89, 89), 6.0f);
	}

	public void setFloor(int floor) {
		targetFloor = floor;
	}

	public void addTracePoints(List<TYTraceLocalPoint> pointArray,
			Map<String, TYLocalPoint> themes) {
		themeLocations.clear();
		themeLocations.putAll(themes);

		for (int i = 0; i < pointArray.size(); ++i) {
			addTracePoint(pointArray.get(i));
		}
	}

	public void addTracePoint(TYTraceLocalPoint point) {
		if (currentFloor != point.location.getFloor()) {
			currentFloor = point.location.getFloor();

			traceFloorArray.add(currentFloor);
			traceStartIndexArray.add(currentStartIndex);
			tracePointArray.add(new ArrayList<TYTraceLocalPoint>());
			traceCoordinateArray.add(new ArrayList<double[]>());
			traceLineArray.add(new Polyline());
		}

		currentStartIndex++;

		List<TYTraceLocalPoint> tracePoints = tracePointArray
				.get(tracePointArray.size() - 1);
		tracePoints.add(point);

		List<double[]> traceCoordinates = traceCoordinateArray
				.get(traceCoordinateArray.size() - 1);
		traceCoordinates.add(new double[] { point.location.getX(),
				point.location.getY() });

		traceLineArray.remove(traceLineArray.size() - 1);
		traceLineArray.add(createLineFromCoordinates(traceCoordinates));
	}

	public void reset() {
		removeAll();

		currentFloor = 0;
		currentStartIndex = 0;

		traceFloorArray.clear();
		traceStartIndexArray.clear();
		tracePointArray.clear();
		traceCoordinateArray.clear();
		traceLineArray.clear();
	}

	public void showTraces() {
		// Log.i(TAG, "showTraces");

		removeAll();

		SimpleMarkerSymbol firstMarkerSymbol = new SimpleMarkerSymbol(
				Color.RED, 18, STYLE.CIRCLE);
		firstMarkerSymbol.setOutline(outlineSymbol);

		for (int i = 0; i < traceFloorArray.size(); ++i) {
			int traceFloor = traceFloorArray.get(i);
			if (targetFloor == traceFloor) {
				addGraphic(new Graphic(traceLineArray.get(i), lineSymbol));
				List<TYTraceLocalPoint> tracePoints = tracePointArray.get(i);
				int traceIndex = traceStartIndexArray.get(i);

				for (int j = 0; j < tracePoints.size(); ++j) {
					TYTraceLocalPoint lp = tracePoints.get(j);
					if (j == 0) {
						addGraphic(new Graphic(new Point(lp.location.getX(),
								lp.location.getY()), firstMarkerSymbol));
					} else {
						addGraphic(new Graphic(new Point(lp.location.getX(),
								lp.location.getY()), null));
					}
					TextSymbol ts = new TextSymbol(10, (traceIndex + j + 1)
							+ "", Color.WHITE);
					ts.setHorizontalAlignment(HorizontalAlignment.CENTER);
					ts.setVerticalAlignment(VerticalAlignment.MIDDLE);
					addGraphic(new Graphic(new Point(lp.location.getX(),
							lp.location.getY()), ts));
				}
			}
		}
	}

	public void showSnappedTraces(TYSnappingManager snappingManager) {
		// Log.i(TAG, "showSnappedTraces");
		removeAll();

		for (int i = 0; i < traceFloorArray.size(); ++i) {
			int traceFloor = traceFloorArray.get(i);
			if (targetFloor == traceFloor) {
				List<TYTraceLocalPoint> tracePoints = tracePointArray.get(i);
				int traceIndex = traceStartIndexArray.get(i);

				Point lastSnappedTracePoint = null;
				int lastSnappedVertexIndex = -1;

				List<Polyline> snappedTraceLineArray = new ArrayList<Polyline>();
				List<Point> snappedTracePointArray = new ArrayList<Point>();

				String lastThemeID = null;

				int testCount = 0;
				for (int j = 0; j < tracePoints.size(); ++j) {
					TYTraceLocalPoint originalTracePoint = tracePoints.get(j);

					TYLocalPoint themeLocation = null;
					Proximity2DResult snappedObject;

					if (originalTracePoint.inTheme) {
						if (lastThemeID != null
								&& lastThemeID
										.equals(originalTracePoint.themeID)) {
							continue;
						}
						lastThemeID = originalTracePoint.themeID;
						themeLocation = themeLocations
								.get(originalTracePoint.themeID);
						snappedObject = snappingManager
								.getSnappedResult(themeLocation);
					} else {
						snappedObject = snappingManager
								.getSnappedResult(originalTracePoint.location);
					}

					// Proximity2DResult snappedObject = snappingManager
					// .getSnappedResult(originalTracePoint.location);
					Point snappedPoint = snappedObject.getCoordinate();

					if (lastSnappedTracePoint == null) {
						lastSnappedTracePoint = snappedPoint;
					} else {
						Envelope envelope = new Envelope(Math.min(
								lastSnappedTracePoint.getX(),
								snappedPoint.getX()), Math.min(
								lastSnappedTracePoint.getY(),
								snappedPoint.getY()), Math.max(
								lastSnappedTracePoint.getX(),
								snappedPoint.getX()), Math.max(
								lastSnappedTracePoint.getY(),
								snappedPoint.getY()));
						Map<Integer, Geometry> geometries = snappingManager
								.getRouteGeometries();
						Polyline cuttedLine = (Polyline) GeometryEngine.clip(
								geometries.get(targetFloor), envelope, null);
						if (cuttedLine == null) {
							cuttedLine = createLineFromCoordinates(new double[] {
									lastSnappedTracePoint.getX(),
									lastSnappedTracePoint.getY(),
									snappedPoint.getX(), snappedPoint.getY() });
						}

						cuttedLine = (Polyline) GeometryEngine.simplify(
								cuttedLine, null);
						snappedTraceLineArray.add(cuttedLine);
						lastSnappedTracePoint = snappedPoint;
					}

					// Log.i(TAG, "VertexIndex: " +
					// snappedObject.getVertexIndex());
					snappedObject.getVertexIndex();
					if (snappedObject.getVertexIndex() != lastSnappedVertexIndex
							|| j == tracePoints.size() - 1) {
						testCount++;
						lastSnappedVertexIndex = snappedObject.getVertexIndex();
						Point snappedTracePoint = new Point(
								snappedPoint.getX(), snappedPoint.getY());
						snappedTracePointArray.add(snappedTracePoint);
					}

					if (originalTracePoint.inTheme) {
						Point themeLocationPoint = new Point(
								themeLocation.getX(), themeLocation.getY());
						snappedTracePointArray.add(themeLocationPoint);

						snappedTraceLineArray
								.add(createLineFromCoordinates(new double[] {
										lastSnappedTracePoint.getX(),
										lastSnappedTracePoint.getY(),
										snappedPoint.getX(),
										snappedPoint.getY() }));
						snappedTraceLineArray
								.add(createLineFromCoordinates(new double[] {
										snappedPoint.getX(),
										snappedPoint.getY(),
										themeLocation.getX(),
										themeLocation.getY() }));
					}
				}

				Graphic[] line1Graphics = new Graphic[snappedTraceLineArray
						.size()];
				Graphic[] line2Graphics = new Graphic[snappedTraceLineArray
						.size()];
				for (int k = 0; k < snappedTraceLineArray.size(); ++k) {
					line1Graphics[k] = new Graphic(
							snappedTraceLineArray.get(k), traceSymbol1);
					line2Graphics[k] = new Graphic(
							snappedTraceLineArray.get(k), traceSymbol2);
				}

				Point currentPoint = null;
				double distanceFilter = 2.0;

				List<Graphic> pointGraphicList = new ArrayList<Graphic>();
				List<Graphic> tsGraphicList = new ArrayList<Graphic>();
				for (int k = 0; k < snappedTracePointArray.size(); ++k) {
					Point newPoint = snappedTracePointArray.get(k);
					if (currentPoint != null
							&& GeometryEngine.distance(currentPoint, newPoint,
									null) < distanceFilter) {
						continue;
					}
					// pointGraphics[k] = new Graphic(
					// snappedTracePointArray.get(k), pointSymbol);
					pointGraphicList.add(new Graphic(snappedTracePointArray
							.get(k), pointSymbol));
					TextSymbol ts = new TextSymbol(10, (traceIndex + k + 1)
							+ "", Color.WHITE);
					ts.setHorizontalAlignment(HorizontalAlignment.CENTER);
					ts.setVerticalAlignment(VerticalAlignment.MIDDLE);
					// tsGraphics[k] = new
					// Graphic(snappedTracePointArray.get(k),
					// ts);
					tsGraphicList.add(new Graphic(
							snappedTracePointArray.get(k), ts));
				}
				addGraphics(line1Graphics);
				addGraphics(line2Graphics);

				Graphic[] pointGraphics = new Graphic[pointGraphicList.size()];
				pointGraphicList.toArray(pointGraphics);
				Graphic[] tsGraphics = new Graphic[tsGraphicList.size()];
				tsGraphicList.toArray(tsGraphics);
				addGraphics(pointGraphics);
				addGraphics(tsGraphics);

				Log.i(TAG, "TestCount: " + testCount);
				Log.i(TAG, "Points: " + pointGraphics.length);
				Log.i(TAG, "Lines: " + line1Graphics.length);
			}
		}
	}

	public void setPointSymbol(int color, int size) {
		pointSymbol = new SimpleMarkerSymbol(color, size, STYLE.CIRCLE);
		pointSymbol.setOutline(outlineSymbol);
		setRenderer(new SimpleRenderer(pointSymbol));
	}

	public void setLineSymbol(int color, float width) {
		lineSymbol = new SimpleLineSymbol(color, width);
	}

	private Polyline createLineFromCoordinates(List<double[]> array) {
		Polyline polyline = new Polyline();
		for (int i = 0; i < array.size(); ++i) {
			double[] xy = array.get(i);
			if (i == 0) {
				polyline.startPath(xy[0], xy[1]);
			} else {
				polyline.lineTo(xy[0], xy[1]);
			}
		}
		return polyline;
	}

	private Polyline createLineFromCoordinates(double[] array) {
		Polyline polyline = new Polyline();
		for (int i = 0; i < array.length; i += 2) {
			if (i == 0) {
				polyline.startPath(array[i], array[i + 1]);
			} else {
				polyline.lineTo(array[i], array[i + 1]);
			}
		}
		return polyline;
	}

}
