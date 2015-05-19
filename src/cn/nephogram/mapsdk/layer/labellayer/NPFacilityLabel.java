package cn.nephogram.mapsdk.layer.labellayer;

import cn.nephogram.mapsdk.entity.NPPictureMarkerSymbol;

import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;

public class NPFacilityLabel {

	private static final int FACILITY_SIZE = 26;
	private static final NPLabelSize facilitySize = new NPLabelSize(
			FACILITY_SIZE, FACILITY_SIZE);

	Point position;
	int categoryID;

	Graphic facilityGraphic;

	NPPictureMarkerSymbol normalFacilitySymbol;
	NPPictureMarkerSymbol highlightedFaciltySymbol;
	NPPictureMarkerSymbol currentSymbol;

	private boolean isHidden;

	boolean isHighlighted;

	public NPFacilityLabel(int categoryID, Point pos) {
		isHighlighted = false;
		isHidden = false;
		this.position = pos;
		this.categoryID = categoryID;
	}

	public NPLabelSize getFacilityLabelSize() {
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

	public NPPictureMarkerSymbol getNormalFacilitySymbol() {
		return normalFacilitySymbol;
	}

	public void setNormalFacilitySymbol(
			NPPictureMarkerSymbol normalFacilitySymbol) {
		this.normalFacilitySymbol = normalFacilitySymbol;
	}

	public NPPictureMarkerSymbol getHighlightedFaciltySymbol() {
		return highlightedFaciltySymbol;
	}

	public void setHighlightedFaciltySymbol(
			NPPictureMarkerSymbol highlightedFaciltySymbol) {
		this.highlightedFaciltySymbol = highlightedFaciltySymbol;
	}

	public NPPictureMarkerSymbol getCurrentSymbol() {
		return currentSymbol;
	}

	public void setCurrentSymbol(NPPictureMarkerSymbol currentSymbol) {
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
