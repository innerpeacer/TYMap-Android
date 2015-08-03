package com.ty.mapsdk;

/**
 * 地图尺寸,对应地图所示区域实际大小
 */
public class TYMapSize {
	public double x;
	public double y;

	public TYMapSize(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

}