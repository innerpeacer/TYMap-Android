package cn.nephogram.mapsdk.route;

public class Vector2 {
	private static final double PI = 3.1415926;

	double x;
	double y;

	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getAngle() {
		if (y == 0 && x >= 0) {
			return 90.0;
		}

		if (y == 0 && x < 0) {
			return -90.0;
		}

		double rad = Math.atan(x / y);
		double angle = (rad * 180) / PI;
		if (y < 0) {
			if (x > 0) {
				angle = angle + 180;
			} else {
				angle = angle - 180;
			}
		}
		return angle;
	}
}
