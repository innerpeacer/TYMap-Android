package com.ty.mapproject.activities;

import java.util.ArrayList;
import java.util.List;

public class TYMapToFengMapConverter {
	static final double FENG_MAP_X0 = 13522258.000000;
	static final double FENG_MAP_Y0 = 3664066.000000;
	static final double FENG_MAP_X1 = 13522298.000000;
	static final double FENG_MAP_Y1 = 3664080.500000;
	static final double FENG_MAP_X2 = 13522246.000000;
	static final double FENG_MAP_Y2 = 3664098.250000;

	static final double FENG_DELTA_1_X = (FENG_MAP_X1 - FENG_MAP_X0);
	static final double FENG_DELTA_1_Y = (FENG_MAP_Y1 - FENG_MAP_Y0);

	static final double FENG_DELTA_2_X = (FENG_MAP_X2 - FENG_MAP_X0);
	static final double FENG_DELTA_2_Y = (FENG_MAP_Y2 - FENG_MAP_Y0);

	static final double TY_MAP_X0 = 13523499.143225;
	static final double TY_MAP_Y0 = 3642465.552261;
	static final double TY_MAP_X1 = 13523509.682853;
	static final double TY_MAP_Y1 = 3642465.582722;
	static final double TY_MAP_X2 = 13523499.193993;
	static final double TY_MAP_Y2 = 3642474.172823;

	static final double TY_DELTA_1_X = (TY_MAP_X1 - TY_MAP_X0);
	static final double TY_DELTA_1_Y = (TY_MAP_Y1 - TY_MAP_Y0);

	static final double TY_DELTA_2_X = (TY_MAP_X2 - TY_MAP_X0);
	static final double TY_DELTA_2_Y = (TY_MAP_Y2 - TY_MAP_Y0);

	public static List<Double> TYMapToFengMap(List<Double> coord) {
		double ty_x = coord.get(0);
		double ty_y = coord.get(1);
		double delta_x = getTYDeltaX(ty_x);
		double delta_y = getTYDeltaY(ty_y);

		double lamda = (delta_x * TY_DELTA_2_Y - delta_y * TY_DELTA_2_X)
				/ (TY_DELTA_1_X * TY_DELTA_2_Y - TY_DELTA_1_Y * TY_DELTA_2_X);
		double miu = (delta_x * TY_DELTA_1_Y - delta_y * TY_DELTA_1_X)
				/ (TY_DELTA_2_X * TY_DELTA_1_Y - TY_DELTA_1_X * TY_DELTA_2_Y);

		double feng_x = FENG_MAP_X0 + lamda * FENG_DELTA_1_X + miu
				* FENG_DELTA_2_X;
		double feng_y = FENG_MAP_Y0 + lamda * FENG_DELTA_1_Y + miu
				* FENG_DELTA_2_Y;

		List<Double> resultList = new ArrayList<Double>();
		resultList.add(feng_x);
		resultList.add(feng_y);
		return resultList;
	}

	public static double getTYDeltaX(double x) {
		return x - TY_MAP_X0;
	}

	public static double getTYDeltaY(double y) {
		return y - TY_MAP_Y0;
	}
}
