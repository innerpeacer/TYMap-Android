package cn.nephogram.mapsdk.route;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;

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

	public String getLandmarkString() {
		String result = null;
		if (landmark != null) {
			result = String.format("在 %s 附近", landmark.getName());
		}
		return result;
	}

	public String getDirectionString() {
		return String.format("%s %.0fm", getDirection(relativeDirection),
				length);
	}

	public boolean hasLandmark() {
		if (landmark != null) {
			return true;
		}
		return false;
	}

	public NPLandmark getLandmark() {
		return landmark;
	}

	public void setLandmark(NPLandmark landmark) {
		this.landmark = landmark;
	}

	public NPRoutePart getRoutePart() {
		return routePart;
	}

	public void setRoutePart(NPRoutePart routePart) {
		this.routePart = routePart;
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public NPRelativeDirection getRelativeDirection() {
		return relativeDirection;
	}

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

	public double getCurrentAngle() {
		return currentAngle;
	}

	public double getLength() {
		return length;
	}

	public enum NPRelativeDirection {
		NPStraight, NPTurnRight, NPRightForward, NPLeftForward, NPRightBackward, NPLeftBackward, NPTurnLeft, NPBackward
	};
}
