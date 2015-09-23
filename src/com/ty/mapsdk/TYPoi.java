package com.ty.mapsdk;

import com.esri.core.geometry.Geometry;

/**
 * POI类：用于表示POI相关数据，主要包含POI地理信息，及相应POI ID
 */
public class TYPoi {

	private String geoID;

	private String poiID;

	private String floorID;

	private String buildingID;

	private String name;

	private Geometry geometry;

	private int categoryID;

	private POI_LAYER layer;

	/**
	 * 获取POI地理ID
	 */
	public String getGeoID() {
		return geoID;
	}

	/**
	 * 获取 POI ID
	 */
	public String getPoiID() {
		return poiID;
	}

	/**
	 * 获取POI所在楼层ID
	 */
	public String getFloorID() {
		return floorID;
	}

	/**
	 * 获取POI所在建筑ID
	 */
	public String getBuildingID() {
		return buildingID;
	}

	/**
	 * 获取POI名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获取POI几何数据
	 */
	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * 获取POI分类类型ID
	 */
	public int getCategoryID() {
		return categoryID;
	}

	/**
	 * 获取POI所在层
	 */
	public POI_LAYER getLayer() {
		return layer;
	}

	/**
	 * POI类型，当前按层来分类：房间层（ROOM）、资产层（ASSET）、公共设施层（FACILITY）
	 */
	public enum POI_LAYER {
		POI_ROOM(1), POI_ASSET(2), POI_FACILITY(3);

		private int layer;

		private POI_LAYER(int layer) {
			this.layer = layer;
		}

		public int layer() {
			return layer;
		}
	}

	/**
	 * POI构造函数
	 * 
	 * @param gid
	 *            地理ID
	 * @param pid
	 *            POI ID
	 * @param fid
	 *            楼层ID
	 * @param bid
	 *            建筑ID
	 * @param pname
	 *            POI名称
	 * @param geometry
	 *            POI几何数据
	 * @param cid
	 *            POI类型ID
	 * @param ptype
	 *            POI所在层
	 */
	public TYPoi(String gid, String pid, String fid, String bid, String pname,
			Geometry geometry, int cid, POI_LAYER ptype) {
		this.geoID = gid;
		this.poiID = pid;
		this.floorID = fid;
		this.buildingID = bid;
		this.name = pname;
		this.geometry = geometry;
		this.categoryID = cid;
		this.layer = ptype;
	}

	@Override
	public String toString() {
		return String.format("GeoID: %s, PoiID: %s, Name: %s TYPE: %s", geoID,
				poiID, name, layerString(layer));
	}

	private String layerString(POI_LAYER layer) {
		String result = null;
		switch (layer) {
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
