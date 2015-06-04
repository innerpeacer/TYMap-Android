package cn.nephogram.mapsdk.route;

import cn.nephogram.data.NPLocalPoint;

/**
 * 路标类，用于导航的提示
 */
public class NPLandmark {

	String name;
	NPLocalPoint location;

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
	public NPLocalPoint getLocation() {
		return location;
	}

	/**
	 * 当前路标的位置
	 */
	public void setLocation(NPLocalPoint location) {
		this.location = location;
	}

	public String toString() {
		return String.format("Landmark: %s", name);
	}
}
