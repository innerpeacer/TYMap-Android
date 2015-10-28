package com.ty.mapsdk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.os.Environment;

import com.ty.mapdata.TYBuilding;
import com.ty.mapsdk.swig.IPMapSDK;

class IPMapFileManager {

	static String FILE_MAP_DATABASE = "TYMap.db";
	static String FILE_MAP_INFO_DATABASE = "%s.tymap";

	static String JSON_FILE_MAP_DATA = "%s.data";
	static String FILE_MAP_DB = "%s.tymap";

	static String JSON_FILE_LANDMARK = "%s_LANDMARK.json";
	static String JSON_FILE_BRANDS = "Brands_Building_%s.json";

	static String JSON_FILE_RENDERING_SCHEME = "%s_RenderingScheme.json";
	static String JSON_FILE_DEFAULT_RENDERING_SCHEME = "RenderingScheme.json";

	static String JSON_FILE_CITY = "Cities.json";
	static String JSON_FILE_BUILDING = "Buildings_City_%s.json";
	static String JSON_FILE_MAPINFO = "MapInfo_Building_%s.json";

	static String FILE_POI_DB = "%s_POI.db";

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

	public static String getMapDBPath() {
		String mapRootDir = TYMapEnvironment.getRootDirectoryForMapFiles();
		String fileName = FILE_MAP_DATABASE;
		return (new File(mapRootDir, fileName)).toString();
	}

	public static String getMapInfoDBPath(TYBuilding building) {
		String buildingDir = getBuildingDir(building.getCityID(),
				building.getBuildingID());
		String fileName = String.format(FILE_MAP_INFO_DATABASE,
				building.getBuildingID());
		return (new File(buildingDir, fileName).toString());
	}

	public static String getMapInfoDBPath(String cityID, String buildingID) {
		String buildingDir = getBuildingDir(cityID, buildingID);
		String fileName = String.format(FILE_MAP_INFO_DATABASE, buildingID);
		return (new File(buildingDir, fileName).toString());

	}

	public static String getCityJsonPath() {
		String mapRootDir = TYMapEnvironment.getRootDirectoryForMapFiles();
		String fileName = JSON_FILE_CITY;
		return (new File(mapRootDir, fileName)).toString();
	}

	public static String getBuildingJsonPath(String cityID) {
		String mapRootDir = TYMapEnvironment.getRootDirectoryForMapFiles();
		File cityDir = new File(mapRootDir, cityID);
		String fileName = String.format(JSON_FILE_BUILDING, cityID);
		return (new File(cityDir, fileName).toString());
	}

	public static String getMapInfoJsonPath(String cityID, String buildingID) {
		String mapRootDir = TYMapEnvironment.getRootDirectoryForMapFiles();
		File cityDir = new File(mapRootDir, cityID);
		File buildingDir = new File(cityDir, buildingID);
		String fileName = String.format(JSON_FILE_MAPINFO, buildingID);
		return (new File(buildingDir, fileName).toString());
	}

	public static String getMapDataPath(TYMapInfo info) {
		// String buildingDir = getBuildingDir(info.getCityID(),
		// info.getBuildingID());
		// String fileName = String.format(JSON_FILE_MAP_DATA, info.getMapID());
		// return (new File(buildingDir, fileName).toString());

		String buildingDir = getBuildingDir(info.getCityID(),
				info.getBuildingID());
		String fileName = String.format(JSON_FILE_MAP_DATA, info.getMapID());
		fileName = String.format("%s.tymap", IPMapSDK.md5(fileName));
		return (new File(buildingDir, fileName).toString());
	}

	public static String getMapDBPath(TYBuilding building) {
		String buildingDir = getBuildingDir(building.getCityID(),
				building.getBuildingID());
		String fileName = String.format(FILE_MAP_DB, building.getBuildingID());
		return (new File(buildingDir, fileName).toString());
	}

	public static String getLandmarkJsonPath(TYMapInfo info) {
		String buildingDir = getBuildingDir(info.getCityID(), info.getCityID());
		String fileName = String.format(JSON_FILE_LANDMARK, info.getMapID());
		return (new File(buildingDir, fileName).toString());
	}

	public static String getBrandJsonPath(TYBuilding building) {
		String buildingDir = getBuildingDir(building.getCityID(),
				building.getCityID());
		String fileName = String.format(JSON_FILE_BRANDS,
				building.getBuildingID());
		return (new File(buildingDir, fileName).toString());
	}

	public static String getRenderingScheme(TYBuilding building) {
		String buildingDir = getBuildingDir(building.getCityID(),
				building.getBuildingID());
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
		String mapRootDir = TYMapEnvironment.getRootDirectoryForMapFiles();
		String fileName = JSON_FILE_DEFAULT_RENDERING_SCHEME;
		return (new File(mapRootDir, fileName).toString());
	}

	public static String getPOIDBPath(TYBuilding building) {
		String buildingDir = getBuildingDir(building.getCityID(),
				building.getCityID());
		String fileName = String.format(FILE_POI_DB, building.getBuildingID());
		return (new File(buildingDir, fileName).toString());

	}

	static String getBuildingDir(String cityID, String buildingID) {
		File cityDir = new File(TYMapEnvironment.getRootDirectoryForMapFiles(),
				cityID);
		return (new File(cityDir, buildingID)).toString();
	}

	static String getCityDir(String cityID) {
		return (new File(TYMapEnvironment.getRootDirectoryForMapFiles(), cityID))
				.toString();

	}

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
