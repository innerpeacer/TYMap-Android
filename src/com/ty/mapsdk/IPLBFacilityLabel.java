package com.ty.mapsdk;

import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;

class IPLBFacilityLabel {

	private static final int FACILITY_SIZE = 26;
	private static final IPLBLabelSize facilitySize = new IPLBLabelSize(
			FACILITY_SIZE, FACILITY_SIZE);

	Point position;
	int categoryID;

	Graphic facilityGraphic;

	TYPictureMarkerSymbol normalFacilitySymbol;
	TYPictureMarkerSymbol highlightedFaciltySymbol;
	TYPictureMarkerSymbol currentSymbol;

	private boolean isHidden;

	boolean isHighlighted;

	public IPLBFacilityLabel(int categoryID, Point pos) {
		isHighlighted = false;
		isHidden = false;
		this.position = pos;
		this.categoryID = categoryID;
	}

	public IPLBLabelSize getFacilityLabelSize() {
		return facilitySize;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public int getCategoryID() {
		return categoryID;
	}

	public Graphic getFacilityGraphic() {
		return facilityGraphic;
	}

	public void setFacilityGraphic(Graphic facilityGraphic) {
		this.facilityGraphic = facilityGraphic;
	}

	public TYPictureMarkerSymbol getNormalFacilitySymbol() {
		return normalFacilitySymbol;
	}

	public void setNormalFacilitySymbol(
			TYPictureMarkerSymbol normalFacilitySymbol) {
		this.normalFacilitySymbol = normalFacilitySymbol;
	}

	public TYPictureMarkerSymbol getHighlightedFaciltySymbol() {
		return highlightedFaciltySymbol;
	}

	public void setHighlightedFaciltySymbol(
			TYPictureMarkerSymbol highlightedFaciltySymbol) {
		this.highlightedFaciltySymbol = highlightedFaciltySymbol;
	}

	public TYPictureMarkerSymbol getCurrentSymbol() {
		return currentSymbol;
	}

	public void setCurrentSymbol(TYPictureMarkerSymbol currentSymbol) {
		this.currentSymbol = currentSymbol;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public boolean isHighlighted() {
		return isHighlighted;
	}

	public void setHighlighted(boolean highlighted) {
		isHighlighted = highlighted;
		if (isHighlighted) {
			currentSymbol = highlightedFaciltySymbol;
		} else {
			currentSymbol = normalFacilitySymbol;
		}
	}

}
