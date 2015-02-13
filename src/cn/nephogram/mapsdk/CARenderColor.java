package cn.nephogram.mapsdk;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;

@SuppressLint("UseSparseArrays")
public class CARenderColor {
	private static Map<Integer, Integer> shopColorMap = null;
	private static Map<Integer, Integer> areaColorMap = null;

	private static Integer outlineColor = null;
	private static Integer floorColor1 = null;
	private static Integer floorColor2 = null;

	public static Map<Integer, Integer> getShopRenderColor() {
		if (shopColorMap == null) {
			shopColorMap = new HashMap<Integer, Integer>();

			shopColorMap.put(0, 0xFFB8B9FC);
			shopColorMap.put(1, 0xFFFCFCB3);
			shopColorMap.put(2, 0xFFB3EFFC);
			shopColorMap.put(3, 0xFFD4E6FC);
			shopColorMap.put(4, 0xFFFCBBEB);
			shopColorMap.put(5, 0xFFE1BBFC);
			shopColorMap.put(6, 0xFF457EBD);
			shopColorMap.put(7, 0xFFFCD6B6);
			shopColorMap.put(8, 0xFFCFFCDA);
			shopColorMap.put(9, 0xFFF5F3F0);
		}

		return shopColorMap;
	}

	public static Map<Integer, Integer> getAreaRenderColor() {
		if (areaColorMap == null) {
			areaColorMap = new HashMap<Integer, Integer>();

			areaColorMap.put(1, 0xFFA7A7A7);
			areaColorMap.put(2, 0xFFE8BCBC);
		}
		return areaColorMap;
	}

	public static Integer getFloorColor1() {
		if (floorColor1 == null) {
			floorColor1 = 0xFFF5F3F0;
		}
		return floorColor1;
	}

	public static Integer getFloorColor2() {
		if (floorColor2 == null) {
			floorColor2 = 0xFFD3D3D3;
		}
		return floorColor2;
	}

	public static Integer getOutlineColor() {
		if (outlineColor == null) {
			outlineColor = 0xFF888888;
		}
		return outlineColor;
	}

}
