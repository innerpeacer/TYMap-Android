package cn.nephogram.mapsdk;

import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.io.UserCredentials;

/**
 * 地图环境
 * 
 * @author innerpeacer 地图环境
 */
public class NPMapEnvironment {

	private static final SpatialReference SPATIALR_REFERENCE = SpatialReference
			.create(3395);
	private static UserCredentials userCredentials = null;

	private static String mapFileRootDirectory = null;

	private static NPMapLanguage mapLanguage = NPMapLanguage.NPSimplifiedChinese;

	/**
	 * 默认坐标系空间参考
	 * 
	 * @return WKID:3395
	 */
	public static SpatialReference defaultSpatialReference() {
		return SPATIALR_REFERENCE;
	}

	/**
	 * 初始化运行时环境，在调用任何地图SDK方法前调用此方法
	 */
	public static void initMapEnvironment() {
		ArcGISRuntime.setClientId("aBYcI5ED55pNyHQ8");
	}

	/**
	 * 访问地图服务的默认用户验证
	 * 
	 * @return [user:password] --> ["arcgis":"666666"]
	 */
	public static UserCredentials defaultUserCredentials() {
		if (userCredentials == null) {
			userCredentials = new UserCredentials();
			userCredentials.setUserAccount("arcgis", "666666");
		}
		return userCredentials;
	}

	/**
	 * 设置当前地图文件的根目录
	 * 
	 * @param dir
	 *            文件根目录
	 */
	public static void setRootDirectoryForMapFiles(String dir) {
		mapFileRootDirectory = dir;
	}

	/**
	 * 获取当前地图文件的根目录
	 * 
	 * @return 根目录字符串
	 */
	public static String getRootDirectoryForMapFiles() {
		return mapFileRootDirectory;
	}

	/**
	 * 设置当前地图显示的语言类型
	 * 
	 * @param language
	 *            目标语言类型
	 */
	public static void setMapLanguage(NPMapLanguage language) {
		mapLanguage = language;
	}

	/**
	 * 获取当前地图显示的语言类型
	 * 
	 * @return 当前语言类型
	 */
	public static NPMapLanguage getMapLanguage() {
		return mapLanguage;
	}
}
