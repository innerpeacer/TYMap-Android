//package com.ty.mapsdk;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import android.graphics.Color;
//
//import com.esri.android.map.GraphicsLayer;
//import com.esri.core.geometry.Envelope;
//import com.esri.core.geometry.Geometry;
//import com.esri.core.geometry.GeometryEngine;
//import com.esri.core.geometry.Point;
//import com.esri.core.geometry.Polyline;
//import com.esri.core.geometry.Proximity2DResult;
//import com.esri.core.map.Graphic;
//import com.esri.core.renderer.SimpleRenderer;
//import com.esri.core.symbol.SimpleLineSymbol;
//import com.esri.core.symbol.SimpleMarkerSymbol;
//import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
//import com.esri.core.symbol.TextSymbol;
//import com.esri.core.symbol.TextSymbol.HorizontalAlignment;
//import com.esri.core.symbol.TextSymbol.VerticalAlignment;
//import com.ty.mapdata.TYLocalPoint;
//
//public class TYTraceLayer extends GraphicsLayer {
//	static final String TAG = TYTraceLayer.class.getSimpleName();
//	private int targetFloor;
//
//	private int currentFloor;
//	private int currentStartIndex;
//
//	SimpleLineSymbol outlineSymbol;
//	SimpleLineSymbol traceSymbol1;
//	SimpleLineSymbol traceSymbol2;
//
//	SimpleLineSymbol lineSymbol;
//	SimpleMarkerSymbol pointSymbol;
//
//	List<Integer> traceFloorArray = new ArrayList<Integer>();
//	List<Integer> traceStartIndexArray = new ArrayList<Integer>();
//	List<List<TYLocalPoint>> tracePointArray = new ArrayList<List<TYLocalPoint>>();
//	// List<List<List<Double>>> traceCoordinateArray = new
//	// ArrayList<List<List<Double>>>();
//	List<List<double[]>> traceCoordinateArray = new ArrayList<List<double[]>>();
//
//	List<Polyline> traceLineArray = new ArrayList<Polyline>();
//
//	public TYTraceLayer() {
//		outlineSymbol = new SimpleLineSymbol(Color.WHITE, 1.0f);
//
//		pointSymbol = new SimpleMarkerSymbol(Color.RED, 10, STYLE.CIRCLE);
//		pointSymbol.setOutline(outlineSymbol);
//		setRenderer(new SimpleRenderer(pointSymbol));
//
//		lineSymbol = new SimpleLineSymbol(Color.RED, 2.0f,
//				com.esri.core.symbol.SimpleLineSymbol.STYLE.DASH);
//
//		traceSymbol1 = new SimpleLineSymbol(Color.WHITE, 8.0f);
//		traceSymbol2 = new SimpleLineSymbol(Color.argb(255, 255, 89, 89), 6.0f);
//	}
//
//	public void setFloor(int floor) {
//		targetFloor = floor;
//	}
//
//	public void addTracePoints(List<TYLocalPoint> pointArray) {
//		for (int i = 0; i < pointArray.size(); ++i) {
//			addTracePoint(pointArray.get(i));
//		}
//	}
//
//	public void addTracePoint(TYLocalPoint point) {
//		if (currentFloor != point.getFloor()) {
//			currentFloor = point.getFloor();
//
//			traceFloorArray.add(currentFloor);
//			traceStartIndexArray.add(currentStartIndex);
//			tracePointArray.add(new ArrayList<TYLocalPoint>());
//			traceCoordinateArray.add(new ArrayList<double[]>());
//			traceLineArray.add(new Polyline());
//		}
//
//		currentStartIndex++;
//
//		List<TYLocalPoint> tracePoints = tracePointArray.get(tracePointArray
//				.size() - 1);
//		tracePoints.add(point);
//
//		List<double[]> traceCoordinates = traceCoordinateArray
//				.get(traceCoordinateArray.size() - 1);
//		traceCoordinates.add(new double[] { point.getX(), point.getY() });
//
//		traceLineArray.remove(traceLineArray.size() - 1);
//		traceLineArray.add(createLineFromCoordinates(traceCoordinates));
//	}
//
//	public void reset() {
//		removeAll();
//
//		currentFloor = 0;
//		currentStartIndex = 0;
//
//		traceFloorArray.clear();
//		traceStartIndexArray.clear();
//		tracePointArray.clear();
//		traceCoordinateArray.clear();
//		traceLineArray.clear();
//	}
//
//	public void showTraces() {
//		// Log.i(TAG, "showTraces");
//
//		removeAll();
//
//		SimpleMarkerSymbol firstMarkerSymbol = new SimpleMarkerSymbol(
//				Color.RED, 18, STYLE.CIRCLE);
//		firstMarkerSymbol.setOutline(outlineSymbol);
//
//		for (int i = 0; i < traceFloorArray.size(); ++i) {
//			int traceFloor = traceFloorArray.get(i);
//			if (targetFloor == traceFloor) {
//				addGraphic(new Graphic(traceLineArray.get(i), lineSymbol));
//				List<TYLocalPoint> tracePoints = tracePointArray.get(i);
//				int traceIndex = traceStartIndexArray.get(i);
//
//				for (int j = 0; j < tracePoints.size(); ++j) {
//					TYLocalPoint lp = tracePoints.get(j);
//					if (j == 0) {
//						addGraphic(new Graphic(new Point(lp.getX(), lp.getY()),
//								firstMarkerSymbol));
//					} else {
//						addGraphic(new Graphic(new Point(lp.getX(), lp.getY()),
//								null));
//					}
//					TextSymbol ts = new TextSymbol(10, (traceIndex + j + 1)
//							+ "", Color.WHITE);
//					ts.setHorizontalAlignment(HorizontalAlignment.CENTER);
//					ts.setVerticalAlignment(VerticalAlignment.MIDDLE);
//					addGraphic(new Graphic(new Point(lp.getX(), lp.getY()), ts));
//				}
//			}
//		}
//	}
//
//	public void showSnappedTraces(TYSnappingManager snappingManager) {
//		// Log.i(TAG, "showSnappedTraces");
//		removeAll();
//
//		for (int i = 0; i < traceFloorArray.size(); ++i) {
//			int traceFloor = traceFloorArray.get(i);
//			if (targetFloor == traceFloor) {
//				List<TYLocalPoint> tracePoints = tracePointArray.get(i);
//				int traceIndex = traceStartIndexArray.get(i);
//
//				Point lastSnappedTracePoint = null;
//				int lastSnappedVertexIndex = -1;
//
//				List<Polyline> snappedTraceLineArray = new ArrayList<Polyline>();
//				List<Point> snappedTracePointArray = new ArrayList<Point>();
//
//				for (int j = 0; j < tracePoints.size(); ++j) {
//					TYLocalPoint originalTracePoint = tracePoints.get(j);
//					Proximity2DResult snappedObject = snappingManager
//							.getSnappedResult(originalTracePoint);
//					Point snappedPoint = snappedObject.getCoordinate();
//
//					if (lastSnappedTracePoint == null) {
//						lastSnappedTracePoint = snappedPoint;
//					} else {
//						Envelope envelope = new Envelope(Math.min(
//								lastSnappedTracePoint.getX(),
//								snappedPoint.getX()), Math.min(
//								lastSnappedTracePoint.getY(),
//								snappedPoint.getY()), Math.max(
//								lastSnappedTracePoint.getX(),
//								snappedPoint.getX()), Math.max(
//								lastSnappedTracePoint.getY(),
//								snappedPoint.getY()));
//						Map<Integer, Geometry> geometries = snappingManager
//								.getRouteGeometries();
//						Polyline cuttedLine = (Polyline) GeometryEngine.clip(
//								geometries.get(targetFloor), envelope, null);
//						if (cuttedLine == null) {
//							cuttedLine = createLineFromCoordinates(new double[] {
//									lastSnappedTracePoint.getX(),
//									lastSnappedTracePoint.getY(),
//									snappedPoint.getX(), snappedPoint.getY() });
//						}
//
//						cuttedLine = (Polyline) GeometryEngine.simplify(
//								cuttedLine, null);
//						if (!checkFakeSimpleForPolyline(cuttedLine)) {
//							cuttedLine = createLineFromCoordinates(new double[] {
//									lastSnappedTracePoint.getX(),
//									lastSnappedTracePoint.getY(),
//									snappedPoint.getX(), snappedPoint.getY() });
//							snappedTraceLineArray.add(cuttedLine);
//						} else {
//							if (!GeometryEngine.touches(cuttedLine,
//									lastSnappedTracePoint, null)
//									&& !GeometryEngine.touches(cuttedLine,
//											snappedPoint, null)) {
//								cuttedLine = createLineFromCoordinates(new double[] {
//										lastSnappedTracePoint.getX(),
//										lastSnappedTracePoint.getY(),
//										snappedPoint.getX(),
//										snappedPoint.getY() });
//								snappedTraceLineArray.add(cuttedLine);
//							}
//						}
//
//						snappedTraceLineArray.add(cuttedLine);
//						lastSnappedTracePoint = snappedPoint;
//					}
//
//					if (snappedObject.getVertexIndex() != lastSnappedVertexIndex
//							|| j == tracePoints.size() - 1) {
//						lastSnappedVertexIndex = snappedObject.getVertexIndex();
//						Point snappedTracePoint = new Point(
//								snappedPoint.getX(), snappedPoint.getY());
//						snappedTracePointArray.add(snappedTracePoint);
//					}
//				}
//
//				for (int k = 0; k < snappedTraceLineArray.size(); ++k) {
//					addGraphic(new Graphic(snappedTraceLineArray.get(k),
//							traceSymbol1));
//					addGraphic(new Graphic(snappedTraceLineArray.get(k),
//							traceSymbol2));
//				}
//
//				for (int k = 0; k < snappedTracePointArray.size(); ++k) {
//					addGraphic(new Graphic(snappedTracePointArray.get(k),
//							pointSymbol));
//					TextSymbol ts = new TextSymbol(10, (traceIndex + k + 1)
//							+ "", Color.WHITE);
//					ts.setHorizontalAlignment(HorizontalAlignment.CENTER);
//					ts.setVerticalAlignment(VerticalAlignment.MIDDLE);
//					addGraphic(new Graphic(snappedTracePointArray.get(k), ts));
//				}
//
//			}
//		}
//	}
//
//	public boolean checkFakeSimpleForPolyline(Polyline line) {
//		if (line.getPathCount() == 1) {
//			return true;
//		}
//
//		List<Polyline> pathArray = new ArrayList<Polyline>();
//		for (int i = 0; i < line.getPathCount(); ++i) {
//			List<double[]> pointArray = new ArrayList<double[]>();
//			for (int j = 0; j < line.getPathSize(i); ++j) {
//				Point p = line.getPoint(line.getPathStart(i) + j);
//				pointArray.add(new double[] { p.getX(), p.getY() });
//			}
//			pathArray.add(createLineFromCoordinates(pointArray));
//		}
//
//		for (int i = 0; i < pathArray.size(); ++i) {
//			double minDistanceBetweenTwoPath = 10000000;
//			for (int j = 0; j < pathArray.size(); ++j) {
//				if (i == j)
//					continue;
//
//				double distance = GeometryEngine.distance(pathArray.get(i),
//						pathArray.get(j), null);
//				if (distance < minDistanceBetweenTwoPath) {
//					minDistanceBetweenTwoPath = distance;
//				}
//			}
//
//			if (minDistanceBetweenTwoPath > 0.1) {
//				return false;
//			}
//		}
//		return true;
//	}
//
//	public void setPointSymbol(int color, int size) {
//		pointSymbol = new SimpleMarkerSymbol(color, size, STYLE.CIRCLE);
//		pointSymbol.setOutline(outlineSymbol);
//		setRenderer(new SimpleRenderer(pointSymbol));
//	}
//
//	public void setLineSymbol(int color, float width) {
//		lineSymbol = new SimpleLineSymbol(color, width);
//	}
//
//	private Polyline createLineFromCoordinates(List<double[]> array) {
//		Polyline polyline = new Polyline();
//		for (int i = 0; i < array.size(); ++i) {
//			double[] xy = array.get(i);
//			if (i == 0) {
//				polyline.startPath(xy[0], xy[1]);
//			} else {
//				polyline.lineTo(xy[0], xy[1]);
//			}
//		}
//		return polyline;
//	}
//
//	private Polyline createLineFromCoordinates(double[] array) {
//		Polyline polyline = new Polyline();
//		for (int i = 0; i < array.length; i += 2) {
//			if (i == 0) {
//				polyline.startPath(array[i], array[i + 1]);
//			} else {
//				polyline.lineTo(array[i], array[i + 1]);
//			}
//		}
//		return polyline;
//	}
//
// }
