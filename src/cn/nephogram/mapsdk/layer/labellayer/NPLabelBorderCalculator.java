package cn.nephogram.mapsdk.layer.labellayer;

import com.esri.core.geometry.Point;

public class NPLabelBorderCalculator {
	static final String TAG = NPLabelBorderCalculator.class.getSimpleName();

	private static final double DPI_SCALE = 1.0;

	private static final int FACILITY_LOGO_SIZE = 26;

	public static NPLabelBorder getFacilityLabelBorder(Point screenPoint) {
		// Log.i(TAG, "getFacilityLabelBorder");

		if (screenPoint == null) {
			return null;
		}

		double scale = DPI_SCALE;
		double halfSize = FACILITY_LOGO_SIZE * 0.5 * scale;

		double xmin = screenPoint.getX() - halfSize;
		double ymin = screenPoint.getY() - halfSize;

		double xmax = screenPoint.getX() + halfSize;
		double ymax = screenPoint.getY() + halfSize;

		return new NPLabelBorder(xmin, xmax, ymax, ymin);
	}

	public static NPLabelBorder getTextLabelBorder(NPTextLabel textLabel,
			Point screenPoint) {
		double scale = DPI_SCALE;

		double textWidth = textLabel.getTextSize().width * 2;
		double textHeight = textLabel.getTextSize().height * 2;

		double left = screenPoint.getX() - textWidth * 0.5 * scale;
		double right = screenPoint.getX() + textWidth * 0.5 * scale;
		double top = screenPoint.getY() - textHeight * 0.5 * scale;
		double bottom = screenPoint.getY() + textHeight * 0.5 * scale;

		return new NPLabelBorder(left, right, bottom, top);
	}

	// private static Polygon polygonFromLabelBorder(NPLabelBorder border,
	// NPMapView mapView) {
	// float left = (float) border.getLeft();
	// float right = (float) border.getRight();
	// float top = (float) border.getTop();
	// float bottom = (float) border.getBottom();
	//
	// Polygon result = new Polygon();
	//
	// Point pos;
	// pos = mapView.toMapPoint(left, top);
	// result.startPath(pos);
	//
	// pos = mapView.toMapPoint(left, bottom);
	// result.lineTo(pos);
	//
	// pos = mapView.toMapPoint(right, bottom);
	// result.lineTo(pos);
	//
	// pos = mapView.toMapPoint(right, top);
	// result.lineTo(pos);
	//
	// result.closePathWithLine();
	//
	// return result;
	// }
}
