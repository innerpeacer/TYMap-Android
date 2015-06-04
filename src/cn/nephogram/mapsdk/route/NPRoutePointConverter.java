package cn.nephogram.mapsdk.route;

import cn.nephogram.data.NPLocalPoint;
import cn.nephogram.mapsdk.data.NPMapInfo.MapExtent;
import cn.nephogram.mapsdk.data.NPMapSize;

import com.esri.core.geometry.Point;

/**
 * 导航点转换类
 */
public class NPRoutePointConverter {

	private MapExtent baseExtent;
	private NPMapSize baseOffset;

	/**
	 * 导航点转换类初始化方法
	 * 
	 * @param extent
	 *            基础地图范围
	 * @param offset
	 *            地图偏移量
	 * 
	 */
	public NPRoutePointConverter(MapExtent extent, NPMapSize offset) {
		this.baseExtent = extent;
		this.baseOffset = offset;
	}

	/**
	 * 将实际坐标点转换成导航坐标点
	 * 
	 * @param lp
	 *            目标坐标点
	 * 
	 * @return 相应的导航坐标点
	 */
	public Point getRoutePointFromLocalPoint(NPLocalPoint lp) {
		double newX = lp.getX() + baseOffset.getX() * (lp.getFloor() - 1);
		return new Point(newX, lp.getY());
	}

	/**
	 * 将导航坐标眯转换成实际坐标点
	 * 
	 * @param routePoint
	 *            目标导航坐标点
	 * 
	 * @return 相应的实际坐标点
	 */
	public NPLocalPoint getLocalPointFromRoutePoint(Point routePoint) {
		double xOffset = routePoint.getX() - baseExtent.getXmin();

		double grid = xOffset / baseOffset.getX();
		int index = (int) Math.floor(grid);

		double originX = routePoint.getX() - index * baseOffset.getX();
		double originY = routePoint.getY();
		int floor = index + 1;

		return new NPLocalPoint(originX, originY, floor);
	}

	/**
	 * 检测坐标点的有效性
	 * 
	 * @param point
	 *            目标坐标点
	 * 
	 * @return 是否有效
	 */
	public boolean checkPointValidity(NPLocalPoint point) {
		if (point.getX() >= baseExtent.getXmin()
				&& point.getX() <= baseExtent.getXmax()
				&& point.getY() >= baseExtent.getYmin()
				&& point.getY() <= baseExtent.getYmax()) {
			return true;
		} else {
			return false;
		}
	}
}