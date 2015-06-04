package cn.nephogram.mapsdk.route;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;

/**
 * 导航方向提示，用于导航结果的展示，表示其中的一段
 */
public class NPDirectionalHint {

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

	private NPRelativeDirection relativeDirection;

	private double previousAngle;
	private double currentAngle;
	private double length;

	private NPLandmark landmark;
	private NPRoutePart routePart;

	private Vector2 vector;

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
	public NPDirectionalHint(Point start, Point end, double previousAngle) {
		this.startPoint = start;
		this.endPoint = end;

		vector = new Vector2(endPoint.getX() - startPoint.getX(),
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
	 * 当前段的路标信息
	 */
	public NPLandmark getLandmark() {
		return landmark;
	}

	/**
	 * 当前段的路标信息
	 */
	public void setLandmark(NPLandmark landmark) {
		this.landmark = landmark;
	}

	/**
	 * 包含当前段的路径部分
	 */
	public NPRoutePart getRoutePart() {
		return routePart;
	}

	/**
	 * 包含当前段的路径部分
	 */
	public void setRoutePart(NPRoutePart routePart) {
		this.routePart = routePart;
	}

	/**
	 * 当前段起点
	 */
	public Point getStartPoint() {
		return startPoint;
	}

	/**
	 * 当前段终点
	 */
	public Point getEndPoint() {
		return endPoint;
	}

	/**
	 * 当前段的相对方向
	 */
	public NPRelativeDirection getRelativeDirection() {
		return relativeDirection;
	}

	/**
	 * 前一段的方向角
	 */
	public double getPreviousAngle() {
		return previousAngle;
	}

	public String getDirection(NPRelativeDirection direction) {
		String result = null;
		switch (direction) {
		case NPStraight:
			result = DIRECTIONAL_STRING_STRAIGHT;
			break;

		case NPTurnRight:
			result = DIRECTIONAL_STRING_TURN_RIGHT;
			break;

		case NPTurnLeft:
			result = DIRECTIONAL_STRING_TURN_LEFT;
			break;

		case NPLeftForward:
			result = DIRECTIONAL_STRING_LEFT_FORWARD;
			break;

		case NPLeftBackward:
			result = DIRECTIONAL_STRING_LEFT_BACKWARD;
			break;

		case NPRightForward:
			result = DIRECTIONAL_STRING_RIGHT_FORWARD;
			break;

		case NPRightBackward:
			result = DIRECTIONAL_STRING_RIGHT_BACKWARD;
			break;

		case NPBackward:
			result = DIRECTIONAL_STRING_BACKWARD;
			break;
		}
		return result;
	}

	public NPRelativeDirection calculateRelativeDirection(double angle,
			double preAngle) {
		NPRelativeDirection direction = null;

		double deltaAngle = angle - preAngle;

		if (deltaAngle >= 180) {
			deltaAngle -= 360;
		}

		if (deltaAngle <= -180) {
			deltaAngle += 360;
		}

		if (preAngle == INITIAL_EMPTY_ANGLE) {
			direction = NPRelativeDirection.NPStraight;
			return direction;
		}

		if (deltaAngle < -180 + BACKWARD_REFERENCE_ANGLE
				|| deltaAngle > 180 - BACKWARD_REFERENCE_ANGLE) {
			direction = NPRelativeDirection.NPBackward;
		} else if (deltaAngle >= -180 + BACKWARD_REFERENCE_ANGLE
				&& deltaAngle <= -90 - LEFT_RIGHT_REFERENCE_ANGLE) {
			direction = NPRelativeDirection.NPLeftBackward;
		} else if (deltaAngle >= -90 - LEFT_RIGHT_REFERENCE_ANGLE
				&& deltaAngle <= -90 + LEFT_RIGHT_REFERENCE_ANGLE) {
			direction = NPRelativeDirection.NPTurnLeft;
		} else if (deltaAngle >= -90 + LEFT_RIGHT_REFERENCE_ANGLE
				&& deltaAngle <= -FORWARD_REFERENCE_ANGLE) {
			direction = NPRelativeDirection.NPLeftForward;
		} else if (deltaAngle >= -FORWARD_REFERENCE_ANGLE
				&& deltaAngle <= FORWARD_REFERENCE_ANGLE) {
			direction = NPRelativeDirection.NPStraight;
		} else if (deltaAngle >= FORWARD_REFERENCE_ANGLE
				&& deltaAngle <= 90 - LEFT_RIGHT_REFERENCE_ANGLE) {
			direction = NPRelativeDirection.NPRightForward;
		} else if (deltaAngle >= 90 - LEFT_RIGHT_REFERENCE_ANGLE
				&& deltaAngle <= 90 + LEFT_RIGHT_REFERENCE_ANGLE) {
			direction = NPRelativeDirection.NPTurnRight;
		} else if (deltaAngle >= 90 + LEFT_RIGHT_REFERENCE_ANGLE
				&& deltaAngle <= 180 - BACKWARD_REFERENCE_ANGLE) {
			direction = NPRelativeDirection.NPRightBackward;
		}

		return direction;
	}

	/**
	 * 当前段的方向角
	 */
	public double getCurrentAngle() {
		return currentAngle;
	}

	/**
	 * 当前段的长度
	 */
	public double getLength() {
		return length;
	}

	/**
	 * 相对方向类型，用于导航提示
	 */
	public enum NPRelativeDirection {
		NPStraight, NPTurnRight, NPRightForward, NPLeftForward, NPRightBackward, NPLeftBackward, NPTurnLeft, NPBackward
	};
}
