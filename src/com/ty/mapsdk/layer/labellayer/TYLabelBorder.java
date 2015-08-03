package com.ty.mapsdk.layer.labellayer;

public class TYLabelBorder {
	private double left;
	private double right;
	private double bottom;
	private double top;

	private static final double Horizontal_Border_Buffer = -10.0;
	private static final double Vertical_Border_Buffer = -20.0;

	public TYLabelBorder() {

	}

	public TYLabelBorder(double l, double r, double b, double t) {
		this.left = l;
		this.right = r;
		if (l > r) {
			this.left = r;
			this.right = l;
		}

		this.bottom = b;
		this.top = t;
		if (t > b) {
			this.bottom = t;
			this.top = b;
		}
	}

	public static boolean CheckIntersect(TYLabelBorder border1,
			TYLabelBorder border2) {
		if (border1.getRight() - border2.getLeft() < Horizontal_Border_Buffer)
			return false;

		if (border2.getRight() - border1.getLeft() < Horizontal_Border_Buffer)
			return false;

		if (border1.getBottom() - border2.getTop() < Vertical_Border_Buffer)
			return false;

		if (border2.getBottom() - border1.getTop() < Vertical_Border_Buffer)
			return false;

		return true;
	}

	public double getLeft() {
		return this.left;
	}

	public double getRight() {
		return this.right;
	}

	public double getBottom() {
		return this.bottom;
	}

	public double getTop() {
		return this.top;
	}

}
