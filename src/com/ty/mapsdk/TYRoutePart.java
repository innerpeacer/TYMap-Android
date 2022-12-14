package com.ty.mapsdk;

import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;

/**
 * 路径导航段，用于表示跨层导航中的一段路径
 */
public class TYRoutePart {

	Polyline route = null;
	TYMapInfo info = null;

	TYRoutePart previousPart = null;
	TYRoutePart nextPart = null;
	int partIndex;

	/**
	 * 路径导航段的初始化方法，一般不需要直接调用，由导航管理类调用生成
	 * 
	 * @param polyline
	 *            导航线
	 * @param info
	 *            导航线所在楼层的地图信息
	 * 
	 */
	public TYRoutePart(Polyline polyline, TYMapInfo info) {
		this.route = polyline;
		this.info = info;
	}

	/**
	 * 判断当前段是否为跨层导航的第一段
	 * 
	 * @return 是否为第一段
	 */
	public boolean isFirstPart() {
		return previousPart == null;
	}

	/**
	 * 判断当前段是否为跨层导航的最后一段
	 * 
	 * @return 是否为最后一段
	 */
	public boolean isLastPart() {
		return nextPart == null;
	}

	/**
	 * 判断当前段是否为中间段
	 * 
	 * @return 是否为中间段
	 */
	public boolean isMiddlePart() {
		return ((previousPart != null) && (nextPart != null));
	}

	/**
	 * 返回当前段的第一个点
	 * 
	 * @return 第一个点
	 */
	public Point getFirstPoint() {
		Point result = null;
		if (route != null) {
			result = route.getPoint(0);
		}
		return result;
	}

	/**
	 * 返回当前段的最后一个点
	 * 
	 * @return 最后一个点
	 */
	public Point getLastPoint() {
		Point result = null;
		if (route != null) {
			int numPoint = route.getPointCount();
			result = route.getPoint(numPoint - 1);
		}
		return result;
	}

	/**
	 * 返回当前段的几何数据
	 */
	public Polyline getRoute() {
		return route;
	}

	/**
	 * 返回当前段的地图信息
	 */
	public TYMapInfo getMapInfo() {
		return info;
	}

	/**
	 * 返回当前段的前一段
	 */
	public TYRoutePart getPreviousPart() {
		return previousPart;
	}

	/**
	 * 设置当前段的前一段
	 * 
	 * @param previousPart
	 *            前一段
	 */
	public void setPreviousPart(TYRoutePart previousPart) {
		this.previousPart = previousPart;
	}

	/**
	 * 返回当前段的下一段
	 */
	public TYRoutePart getNextPart() {
		return nextPart;
	}

	/**
	 * 设置当前段的后一段
	 * 
	 * @param nextPart
	 *            后一段
	 */
	public void setNextPart(TYRoutePart nextPart) {
		this.nextPart = nextPart;
	}

	protected void setPartIndex(int i) {
		partIndex = i;
	}

	protected int getPartIndex() {
		return partIndex;
	}

}
