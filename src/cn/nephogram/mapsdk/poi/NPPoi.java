package cn.nephogram.mapsdk.poi;

import com.esri.core.geometry.Geometry;

/**
 * @brief POI类：用于表示POI相关数据，主要包含POI地理信息，及相应POI ID
 */
public class NPPoi {

	/**
	 * POI地理ID
	 */
	private String geoID;

	/**
	 * @brief POI ID
	 */
	private String poiID;

	/**
	 * POI所在楼层ID
	 */
	private String floorID;

	/**
	 * POI所在建筑ID
	 */
	private String buildingID;

	/**
	 * POI名称
	 */
	private String name;

	/**
	 * POI几何数据
	 */
	private Geometry geometry;

	/**
	 * POI分类类型ID
	 */
	private int categoryID;

	/**
	 * POI类型
	 */
	private POI_TYPE type;

	public String getGeoID() {
		return geoID;
	}

	public String getPoiID() {
		return poiID;
	}

	public String getFloorID() {
		return floorID;
	}

	public String getBuildingID() {
		return buildingID;
	}

	public String getName() {
		return name;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public int getCategoryID() {
		return categoryID;
	}

	public POI_TYPE getType() {
		return type;
	}

	public enum POI_TYPE {
		POI_ROOM, POI_ASSET, POI_FACILITY
	}

	public NPPoi(String gid, String pid, String fid, String bid, String pname,
			Geometry geometry, int cid, POI_TYPE ptype) {
		this.geoID = gid;
		this.poiID = pid;
		this.floorID = fid;
		this.buildingID = bid;
		this.name = pname;
		this.geometry = geometry;
		this.categoryID = cid;
		this.type = ptype;
	}

	@Override
	public String toString() {
		return String.format("GeoID: %s, PoiID: %s, Name: %s", geoID, poiID,
				name);
	}
}
