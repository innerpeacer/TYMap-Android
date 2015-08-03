package com.ty.mapsdk.data;

import org.json.JSONException;
import org.json.JSONObject;

public class TYLocalPoint {
	private static final String KEY_X = "x";
	private static final String KEY_Y = "y";
	private static final String KEY_FLOOR = "floor";

	private double x;
	private double y;
	private int floor;

	public TYLocalPoint(double x, double y) {
		this.x = x;
		this.y = y;
		this.floor = 1;
	}

	public TYLocalPoint(double x, double y, int floor) {
		this.x = x;
		this.y = y;
		this.floor = floor;
	}

	public boolean equal(TYLocalPoint p) {
		if (p == null)
			return false;

		if (this.x == p.x && this.y == p.y && this.floor == p.floor) {
			return true;
		}
		return false;
	}

	public void parseJson(JSONObject jsonObject) {
		if (jsonObject != null) {
			setX(jsonObject.optDouble(KEY_X));
			setY(jsonObject.optDouble(KEY_Y));
			setFloor(jsonObject.optInt(KEY_FLOOR));
		}
	}

	public JSONObject buildJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(KEY_X, x);
			jsonObject.put(KEY_Y, y);
			jsonObject.put(KEY_FLOOR, floor);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("(").append(x).append(", ").append(y).append(")")
				.append(" in Floor: ").append(floor);
		return buffer.toString();
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public double distanceWithPoint(TYLocalPoint lp) {
		if (lp == null) {
			return -1;
		}

		// if (floor != lp.getFloor()) {
		// return Double.POSITIVE_INFINITY;
		// }

		return Math.sqrt((x - lp.getX()) * (x - lp.getX()) + (y - lp.getY())
				* (y - lp.getY()));
	}

	public static double distanceWithPoints(TYLocalPoint lp1, TYLocalPoint lp2) {
		if (lp1 == null) {
			return -1;
		}
		return lp1.distanceWithPoint(lp2);
	}
}
