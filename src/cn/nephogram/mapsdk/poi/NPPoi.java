package cn.nephogram.mapsdk.poi;

import com.esri.core.geometry.Geometry;

/**
 * @brief POI类：用于表示POI相关数据，主要包含POI地理信息，及相应POI ID
 */
public class NPPoi {

	private String geoID;

	private String poiID;

	private String floorID;

	private String buildingID;

	private String name;

	private Geometry geometry;

	private int categoryID;

	private POI_TYPE type;

	/**
	 * POI地理ID
	 */
	public String getGeoID() {
		return geoID;
	}

	/**
	 * @brief POI ID
	 */
	public String getPoiID() {
		return poiID;
	}

	/**
	 * POI所在楼层ID
	 */
	public String getFloorID() {
		return floorID;
	}

	/**
	 * POI所在建筑ID
	 */
	public String getBuildingID() {
		return buildingID;
	}

	/**
	 * POI名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * POI几何数据
	 */
	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * POI分类类型ID
	 */
	public int getCategoryID() {
		return categoryID;
	}

	/**
	 * POI类型
	 */
	public POI_TYPE getType() {
		return type;
	}

	/**
	 * POI类型，当前按层来分类：房间层（ROOM）、资产层（ASSET）、公共设施层（FACILITY）
	 */
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
		return String.format("GeoID: %s, PoiID: %s, Name: %s TYPE: %s", geoID,
				poiID, name, typeString(type));
	}

	private String typeString(POI_TYPE type) {
		String result = null;
		switch (type) {
		case POI_ROOM:
			result = "ROOM";
			break;

		case POI_ASSET:
			result = "ASSET";
			break;

		case POI_FACILITY:
			result = "FACILITY";
			break;
		default:
			break;
		}

		return result;
	}
}
