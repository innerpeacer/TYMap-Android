package com.ty.mapsdk;

import com.ty.mapdata.TYLocalPoint;

/**
 * 路标类，用于导航的提示
 */
public class TYLandmark {

	String name;
	TYLocalPoint location;

	/**
	 * 获取当前路标的名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置当前路标的名称
	 * 
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取当前路标的位置
	 */
	public TYLocalPoint getLocation() {
		return location;
	}

	/**
	 * 设置当前路标的位置
	 * 
	 * @param location
	 *            位置
	 */
	public void setLocation(TYLocalPoint location) {
		this.location = location;
	}

	public String toString() {
		return String.format("Landmark: %s", name);
	}
}
