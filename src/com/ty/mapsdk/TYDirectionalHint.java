package com.ty.mapsdk;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;

/**
 * 导航方向提示，用于导航结果的展示，表示其中的一段
 */
public class TYDirectionalHint {

	private static final String DIRECTIONAL_STRING_STRAIGHT = "直行";
	private static final String DIRECTIONAL_STRING_BACKWARD = "向后方";

	private static final String DIRECTIONAL_STRING_TURN_RIGHT = "右转向前行进";
	private static final String DIRECTIONAL_STRING_TURN_LEFT = "左转向前行进";
	private static final String DIRECTIONAL_STRING_RIGHT_FORWARD = "向右前方行进";
	private static final String DIRECTIONAL_STRING_LEFT_FORWARD = "向左前方行进";
	private static final String DIRECTIONAL_STRING_RIGHT_BACKWARD = "向右后方行进";
	private static final String DIRECTIONAL_STRING_LEFT_BACKWARD = "向左后方行进";

	private static final int FORWARD_REFERENCE_ANGLE = 30;
	private static final int BACKWARD_REFERENCE_ANGLE = 10;
	private static final int LEFT_RIGHT_REFERENCE_ANGLE = 30;
	public static final int INITIAL_EMPTY_ANGLE = 1000;

	private Point startPoint;
	private Point endPoint;

	private TYRelativeDirection relativeDirection;

	private double previousAngle;
	private double currentAngle;
	private double length;

	private TYLandmark landmark;
	private TYRoutePart routePart;

	private IPRouteVector2 vector;

	/**
	 * 导航方向提示的初始化方法，一般不需要直接调用，由导航管理类调用生成
	 * 
	 * @param start
	 *            当前导航段的起点
	 * @param end
	 *            当前导航段的终点
	 * @param previousAngle
	 *            前一导航段的方向角
	 */
	public TYDirectionalHint(Point start, Point end, double previousAngle) {
		this.startPoint = start;
		this.endPoint = end;

		vector = new IPRouteVector2(endPoint.getX() - startPoint.getX(),
				endPoint.getY() - startPoint.getY());
		length = GeometryEngine.distance(startPoint, endPoint, null);
		currentAngle = vector.getAngle();
		this.previousAngle = previousAngle;

		relativeDirection = calculateRelativeDirection(currentAngle,
				previousAngle);
	}

	/**
	 * 生成当前段的路标提示
	 * 
	 * @return 当前的路径提示字符串
	 */
	public String getLandmarkString() {
		String result = null;
		if (landmark != null) {
			result = String.format("在 %s 附近", landmark.getName());
		}
		return result;
	}

	/**
	 * 生成当前段的方向提示
	 * 
	 * @return 当前的方向提示字符串
	 */
	public String getDirectionString() {
		return String.format("%s %.0fm", getDirection(relativeDirection),
				length);
	}

	/**
	 * 判断当前段是否有路标信息
	 * 
	 * @return 是否有路标信息
	 */
	public boolean hasLandmark() {
		if (landmark != null) {
			return true;
		}
		return false;
	}

	/**
	 * 获取当前段的路标信息
	 */
	public TYLandmark getLandmark() {
		return landmark;
	}

	/**
	 * 设置当前段的路标信息
	 * 
	 * @param landmark
	 *            路标信息
	 */
	public void setLandmark(TYLandmark landmark) {
		this.landmark = landmark;
	}

	/**
	 * 获取包含当前段的路径部分
	 */
	public TYRoutePart getRoutePart() {
		return routePart;
	}

	/**
	 * 设置包含当前段的路径部分
	 * 
	 * @param routePart
	 *            路径部分
	 */
	public void setRoutePart(TYRoutePart routePart) {
		this.routePart = routePart;
	}

	/**
	 * 获取当前段起点
	 */
	public Point getStartPoint() {
		return startPoint;
	}

	/**
	 * 获取当前段终点
	 */
	public Point getEndPoint() {
		return endPoint;
	}

	/**
	 * 获取当前段的相对方向
	 */
	public TYRelativeDirection getRelativeDirection() {
		return relativeDirection;
	}

	/**
	 * 获取前一段的方向角
	 */
	public double getPreviousAngle() {
		return previousAngle;
	}

	String getDirection(TYRelativeDirection direction) {
		String result = null;
		switch (direction) {
		case TYStraight:
			result = DIRECTIONAL_STRING_STRAIGHT;
			break;

		case TYTurnRight:
			result = DIRECTIONAL_STRING_TURN_RIGHT;
			break;

		case TYTurnLeft:
			result = DIRECTIONAL_STRING_TURN_LEFT;
			break;

		case TYLeftForward:
			result = DIRECTIONAL_STRING_LEFT_FORWARD;
			break;

		case TYLeftBackward:
			result = DIRECTIONAL_STRING_LEFT_BACKWARD;
			break;

		case TYRightForward:
			result = DIRECTIONAL_STRING_RIGHT_FORWARD;
			break;

		case TYRightBackward:
			result = DIRECTIONAL_STRING_RIGHT_BACKWARD;
			break;

		case TYBackward:
			result = DIRECTIONAL_STRING_BACKWARD;
			break;
		}
		return result;
	}

	TYRelativeDirection calculateRelativeDirection(double angle, double preAngle) {
		TYRelativeDirection direction = null;

		double deltaAngle = angle - preAngle;

		if (deltaAngle >= 180) {
			deltaAngle -= 360;
		}

		if (deltaAngle <= -180) {
			deltaAngle += 360;
		}

		if (preAngle == INITIAL_EMPTY_ANGLE) {
			direction = TYRelativeDirection.TYStraight;
			return direction;
		}

		if (deltaAngle < -180 + BACKWARD_REFERENCE_ANGLE
				|| deltaAngle > 180 - BACKWARD_REFERENCE_ANGLE) {
			direction = TYRelativeDirection.TYBackward;
		} else if (deltaAngle >= -180 + BACKWARD_REFERENCE_ANGLE
				&& deltaAngle <= -90 - LEFT_RIGHT_REFERENCE_ANGLE) {
			direction = TYRelativeDirection.TYLeftBackward;
		} else if (deltaAngle >= -90 - LEFT_RIGHT_REFERENCE_ANGLE
				&& deltaAngle <= -90 + LEFT_RIGHT_REFERENCE_ANGLE) {
			direction = TYRelativeDirection.TYTurnLeft;
		} else if (deltaAngle >= -90 + LEFT_RIGHT_REFERENCE_ANGLE
				&& deltaAngle <= -FORWARD_REFERENCE_ANGLE) {
			direction = TYRelativeDirection.TYLeftForward;
		} else if (deltaAngle >= -FORWARD_REFERENCE_ANGLE
				&& deltaAngle <= FORWARD_REFERENCE_ANGLE) {
			direction = TYRelativeDirection.TYStraight;
		} else if (deltaAngle >= FORWARD_REFERENCE_ANGLE
				&& deltaAngle <= 90 - LEFT_RIGHT_REFERENCE_ANGLE) {
			direction = TYRelativeDirection.TYRightForward;
		} else if (deltaAngle >= 90 - LEFT_RIGHT_REFERENCE_ANGLE
				&& deltaAngle <= 90 + LEFT_RIGHT_REFERENCE_ANGLE) {
			direction = TYRelativeDirection.TYTurnRight;
		} else if (deltaAngle >= 90 + LEFT_RIGHT_REFERENCE_ANGLE
				&& deltaAngle <= 180 - BACKWARD_REFERENCE_ANGLE) {
			direction = TYRelativeDirection.TYRightBackward;
		}

		return direction;
	}

	/**
	 * 获取当前段的方向角
	 */
	public double getCurrentAngle() {
		return currentAngle;
	}

	/**
	 * 获取当前段的长度
	 */
	public double getLength() {
		return length;
	}

	/**
	 * 相对方向类型，用于导航提示
	 */
	public enum TYRelativeDirection {
		TYStraight, TYTurnRight, TYRightForward, TYLeftForward, TYRightBackward, TYLeftBackward, TYTurnLeft, TYBackward
	};
}
