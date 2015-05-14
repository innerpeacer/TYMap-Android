package cn.nephogram.mapsdk.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;

@SuppressLint("UseSparseArrays")
public class NPRouteResultV2 {

	private List<NPRoutePart> allRoutePartArray = new ArrayList<NPRoutePart>();
	private Map<Integer, List<NPRoutePart>> allFloorRoutePartDict = new HashMap<Integer, List<NPRoutePart>>();

	public NPRouteResultV2(List<NPRoutePart> routePartArray) {
		allRoutePartArray.addAll(routePartArray);

		for (int i = 0; i < routePartArray.size(); ++i) {
			NPRoutePart rp = routePartArray.get(i);
			int floor = rp.getMapInfo().getFloorNumber();

			if (!allFloorRoutePartDict.containsKey(floor)) {
				List<NPRoutePart> array = new ArrayList<NPRoutePart>();
				allFloorRoutePartDict.put(floor, array);
			}
			List<NPRoutePart> array = allFloorRoutePartDict.get(floor);
			array.add(rp);
		}
	}

	// public boolean isDeviatingFromRoute(NPLocalPoint point, double distance)
	// {
	// return false;
	// }

	// public NPRoutePart getNearestRoutePart(NPLocalPoint location) {
	// return null;
	// }

	// public List<NPRoutePart> getRoutePartsOnFloor(int floor) {
	// return allFloorRoutePartDict.get(floor);
	// }
	//
	// public NPRoutePart getRoutePart(int index) {
	// return allRoutePartArray.get(index);
	// }
	//
	// public List<NPDirectionalHint> getRouteDirectionalHint(NPRoutePart rp) {
	// return null;
	// }
	//
	// public NPDirectionalHint getDirectionalHintForLocationFromHints(
	// NPLocalPoint location, List<NPDirectionalHint> directions) {
	// return null;
	// }
	//
	// public static Polyline getSubPolyline(Polyline originalLine, Point start,
	// Point
	// end) {
	// return null;
	// }
}
