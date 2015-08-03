package com.ty.mapsdk;

import com.ty.mapdata.TYLocalPoint;

/**
 * 路标类，用于导航的提示
 */
public class TYLandmark {

	String name;
	TYLocalPoint location;

	/**
	 * 当前路标的名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 当前路标的名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 当前路标的位置
	 */
	public TYLocalPoint getLocation() {
		return location;
	}

	/**
	 * 当前路标的位置
	 */
	public void setLocation(TYLocalPoint location) {
		this.location = location;
	}

	public String toString() {
		return String.format("Landmark: %s", name);
	}
}
