package cn.nephogram.mapsdk;

import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.io.UserCredentials;

public class NPMapEnvironment {

	private static final SpatialReference SPATIALR_REFERENCE = SpatialReference
			.create(3395);
	private static UserCredentials userCredentials = null;

	public static SpatialReference defaultSpatialReference() {
		return SPATIALR_REFERENCE;
	}

	public static void initMapEnvironment() {
		ArcGISRuntime.setClientId("aBYcI5ED55pNyHQ8");
	}

	public static UserCredentials defaultUserCredentials() {
		if (userCredentials == null) {
			userCredentials = new UserCredentials();
			userCredentials.setUserAccount("arcgis", "666666");
		}
		return userCredentials;
	}

}
