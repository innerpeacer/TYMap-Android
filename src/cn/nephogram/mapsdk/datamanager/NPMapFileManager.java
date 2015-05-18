package cn.nephogram.mapsdk.datamanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.os.Environment;
import cn.nephogram.mapsdk.NPMapEnvironment;
import cn.nephogram.mapsdk.data.NPBuilding;
import cn.nephogram.mapsdk.data.NPMapInfo;

public class NPMapFileManager {

	private static String JSON_FILE_FLOOR = "%s_FLOOR.json";
	private static String JSON_FILE_ROOM = "%s_ROOM.json";
	private static String JSON_FILE_ASSET = "%s_ASSET.json";
	private static String JSON_FILE_FACILITY = "%s_FACILITY.json";
	private static String JSON_FILE_LABEL = "%s_LABEL.json";

	private static String JSON_FILE_LANDMARK = "%s_LANDMARK.json";

	// private static String JSON_FILE_AOI = "AOI.json";

	private static String JSON_FILE_RENDERING_SCHEME = "%s_RenderingScheme.json";
	private static String JSON_FILE_DEFAULT_RENDERING_SCHEME = "RenderingScheme.json";

	private static String JSON_FILE_CITY = "Cities.json";
	private static String JSON_FILE_BUILDING = "Buildings_City_%s.json";
	private static String JSON_FILE_MAPINFO = "MapInfo_Building_%s.json";

	private static String FILE_POI_DB = "%s_POI.db";

	public static boolean fileExist(String path) {
		return new File(path).exists() ? true : false;
	}

	public static File getFileFromFilePath(String filename) {
		File file = new File(filename);
		if (!file.isAbsolute()) {
			File root = Environment.getExternalStorageDirectory();
			file = new File(root, filename);
		}
		return file;
	}

	public static String getCityJsonPath() {
		String mapRootDir = NPMapEnvironment.getRootDirectoryForMapFiles();
		String fileName = JSON_FILE_CITY;
		return (new File(mapRootDir, fileName)).toString();
	}

	public static String getBuildingJsonPath(String cityID) {
		String mapRootDir = NPMapEnvironment.getRootDirectoryForMapFiles();
		File cityDir = new File(mapRootDir, cityID);
		String fileName = String.format(JSON_FILE_BUILDING, cityID);
		return (new File(cityDir, fileName).toString());
	}

	public static String getMapInfoJsonPath(String cityID, String buildingID) {
		String mapRootDir = NPMapEnvironment.getRootDirectoryForMapFiles();
		File cityDir = new File(mapRootDir, cityID);
		File buildingDir = new File(cityDir, buildingID);
		String fileName = String.format(JSON_FILE_MAPINFO, buildingID);
		return (new File(buildingDir, fileName).toString());
	}

	public static String getFloorFilePath(NPMapInfo info) {
		String mapRootDir = NPMapEnvironment.getRootDirectoryForMapFiles();
		File cityDir = new File(mapRootDir, info.getCityID());
		File buildingDir = new File(cityDir, info.getBuildingID());
		String fileName = String.format(JSON_FILE_FLOOR, info.getMapID());
		return (new File(buildingDir, fileName).toString());
	}

	public static String getRoomFilePath(NPMapInfo info) {

		String mapRootDir = NPMapEnvironment.getRootDirectoryForMapFiles();
		File cityDir = new File(mapRootDir, info.getCityID());
		File buildingDir = new File(cityDir, info.getBuildingID());
		String fileName = String.format(JSON_FILE_ROOM, info.getMapID());
		return (new File(buildingDir, fileName).toString());
	}

	public static String getAssetFilePath(NPMapInfo info) {
		String mapRootDir = NPMapEnvironment.getRootDirectoryForMapFiles();
		File cityDir = new File(mapRootDir, info.getCityID());
		File buildingDir = new File(cityDir, info.getBuildingID());
		String fileName = String.format(JSON_FILE_ASSET, info.getMapID());
		return (new File(buildingDir, fileName).toString());
	}

	public static String getFacilityFilePath(NPMapInfo info) {
		String mapRootDir = NPMapEnvironment.getRootDirectoryForMapFiles();
		File cityDir = new File(mapRootDir, info.getCityID());
		File buildingDir = new File(cityDir, info.getBuildingID());
		String fileName = String.format(JSON_FILE_FACILITY, info.getMapID());
		return (new File(buildingDir, fileName).toString());
	}

	public static String getLabelFilePath(NPMapInfo info) {
		String mapRootDir = NPMapEnvironment.getRootDirectoryForMapFiles();
		File cityDir = new File(mapRootDir, info.getCityID());
		File buildingDir = new File(cityDir, info.getBuildingID());
		String fileName = String.format(JSON_FILE_LABEL, info.getMapID());
		return (new File(buildingDir, fileName).toString());
	}

	public static String getLandmarkJsonPath(NPMapInfo info) {
		String mapRootDir = NPMapEnvironment.getRootDirectoryForMapFiles();
		File cityDir = new File(mapRootDir, info.getCityID());
		File buildingDir = new File(cityDir, info.getBuildingID());
		String fileName = String.format(JSON_FILE_LANDMARK, info.getMapID());
		return (new File(buildingDir, fileName).toString());
	}

	public static String getRenderingScheme(NPBuilding building) {
		String mapRootDir = NPMapEnvironment.getRootDirectoryForMapFiles();
		File cityDir = new File(mapRootDir, building.getCityID());
		File buildingDir = new File(cityDir, building.getBuildingID());
		String fileName = String.format(JSON_FILE_RENDERING_SCHEME,
				building.getBuildingID());

		File result = new File(buildingDir, fileName);
		if (result.exists()) {
			return result.toString();
		} else {
			return getDefaultRenderingScheme();
		}
	}

	public static String getDefaultRenderingScheme() {
		String mapRootDir = NPMapEnvironment.getRootDirectoryForMapFiles();
		String fileName = JSON_FILE_DEFAULT_RENDERING_SCHEME;
		return (new File(mapRootDir, fileName).toString());
	}

	public static String getPOIDBPath(NPBuilding building) {
		String mapRootDir = NPMapEnvironment.getRootDirectoryForMapFiles();
		File cityDir = new File(mapRootDir, building.getCityID());
		File buildingDir = new File(cityDir, building.getBuildingID());
		String fileName = String.format(FILE_POI_DB, building.getBuildingID());
		return (new File(buildingDir, fileName).toString());

	}

	// + (NSString *)getPrimitiveDBPath:(NPBuilding *)building;

	// public static String getAOIJsonPath() {
	// return JSON_FILE_AOI;
	// }

	public static byte[] readByteFromFile(String path) {
		RandomAccessFile input;
		try {
			input = new RandomAccessFile(path, "rw");
			long filelength = input.length();
			byte output[] = new byte[(int) filelength];
			byte buffer[] = new byte[1024];

			int c;
			int offset = 0;
			while ((c = input.read(buffer)) != -1) {
				System.arraycopy(buffer, 0, output, offset, c);
				offset += c;
			}

			input.close();
			return output;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
