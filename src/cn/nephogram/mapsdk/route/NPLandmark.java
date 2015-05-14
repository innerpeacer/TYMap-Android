package cn.nephogram.mapsdk.route;

import cn.nephogram.data.NPLocalPoint;

public class NPLandmark {

	String name;
	NPLocalPoint location;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public NPLocalPoint getLocation() {
		return location;
	}

	public void setLocation(NPLocalPoint location) {
		this.location = location;
	}

	public String toString() {
		return String.format("Landmark: %s", name);
	}
}
