package cn.nephogram.mapsdk.route;

import cn.nephogram.mapsdk.data.NPMapInfo;

import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;

public class NPRoutePart {

	Polyline route = null;
	NPMapInfo info = null;

	NPRoutePart previousPart = null;
	NPRoutePart nextPart = null;

	public NPRoutePart(Polyline polyline, NPMapInfo info) {
		this.route = polyline;
		this.info = info;
	}

	public boolean isFirstPart() {
		return previousPart == null;
	}

	public boolean isLastPart() {
		return nextPart == null;
	}

	public boolean isMiddlePart() {
		return ((previousPart != null) && (nextPart != null));
	}

	public Point getFirstPoint() {
		Point result = null;
		if (route != null) {
			result = route.getPoint(0);
		}
		return result;
	}

	public Point getLastPoint() {
		Point result = null;
		if (route != null) {
			int numPoint = route.getPointCount();
			result = route.getPoint(numPoint - 1);
		}
		return result;
	}

	public Polyline getRoute() {
		return route;
	}

	public NPMapInfo getMapInfo() {
		return info;
	}

	public NPRoutePart getPreviousPart() {
		return previousPart;
	}

	public void setPreviousPart(NPRoutePart previousPart) {
		this.previousPart = previousPart;
	}

	public NPRoutePart getNextPart() {
		return nextPart;
	}

	public void setNextPart(NPRoutePart nextPart) {
		this.nextPart = nextPart;
	}

}
