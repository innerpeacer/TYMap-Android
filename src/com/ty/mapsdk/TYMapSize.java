package com.ty.mapsdk;

/**
 * 地图尺寸,对应地图所示区域实际大小
 */
public class TYMapSize {
	public double x;
	public double y;

	/**
	 * 地图尺寸构造函数
	 * 
	 * @param x
	 *            宽度
	 * @param y
	 *            高度
	 */
	public TYMapSize(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * 获取地图宽度
	 * 
	 * @return 宽度
	 */
	public double getX() {
		return x;
	}

	/**
	 * 获取地图高度
	 * 
	 * @return 高度
	 */
	public double getY() {
		return y;
	}

}