package cn.nephogram.mapsdk.route;

import cn.nephogram.mapsdk.data.NPMapInfo;

import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;

/**
 * 路径导航段，用于表示跨层导航中的一段路径
 */
public class NPRoutePart {

	Polyline route = null;
	NPMapInfo info = null;

	NPRoutePart previousPart = null;
	NPRoutePart nextPart = null;

	/**
	 * 路径导航段的初始化方法，一般不需要直接调用，由导航管理类调用生成
	 * 
	 * @param route
	 *            导航线
	 * @param mapInfo
	 *            导航线所在楼层的地图信息
	 * 
	 */
	public NPRoutePart(Polyline polyline, NPMapInfo info) {
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
	 * 当前段的几何数据
	 */
	public Polyline getRoute() {
		return route;
	}

	/**
	 * 当前段的地图信息
	 */
	public NPMapInfo getMapInfo() {
		return info;
	}

	/**
	 * 当前段的前一段
	 */
	public NPRoutePart getPreviousPart() {
		return previousPart;
	}

	/**
	 * 当前段的前一段
	 */
	public void setPreviousPart(NPRoutePart previousPart) {
		this.previousPart = previousPart;
	}

	/**
	 * 当前段的下一段
	 */
	public NPRoutePart getNextPart() {
		return nextPart;
	}

	/**
	 * 当前段的下一段
	 */
	public void setNextPart(NPRoutePart nextPart) {
		this.nextPart = nextPart;
	}

}
