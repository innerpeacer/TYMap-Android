package com.ty.mapsdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class IPBrand {
	static final String TAG = IPBrand.class.getSimpleName();

	private static final String KEY_BRANDS = "Brands";
	private static final String KEY_BRAND_POI_ID = "poiID";
	private static final String KEY_BRAND_NAME = "name";
	private static final String KEY_BRAND_LOGO = "logo";
	private static final String KEY_BRAND_SIZE_X = "width";
	private static final String KEY_BRAND_SIZE_Y = "height";

	private String poiID;
	private String name;
	private String logo;
	private IPLabelSize logoSize;

	public String toString() {
		return String
				.format("PoiID: %s, Name: %s, Logo: %s", poiID, name, logo);
	}

	public String getPoiID() {
		return poiID;
	}

	public String getName() {
		return name;
	}

	public String getLogo() {
		return logo;
	}

	public IPLabelSize getLogoSize() {
		return logoSize;
	}

	void setPoiID(String poiID) {
		this.poiID = poiID;
	}

	void setName(String name) {
		this.name = name;
	}

	void setLogo(String logo) {
		this.logo = logo;
	}

	void setLogoSize(IPLabelSize logoSize) {
		this.logoSize = logoSize;
	}

	public static List<IPBrand> parseAllBrands(TYBuilding building) {
		List<IPBrand> toReturn = new ArrayList<IPBrand>();
		if (building == null) {
			return toReturn;
		}

		String path = IPMapFileManager.getBrandJsonPath(building);
		if (!new File(path).exists()) {
			return toReturn;
		}

		try {
			FileInputStream inStream = new FileInputStream(new File(path));
			InputStreamReader inputReader = new InputStreamReader(inStream);
			BufferedReader bufReader = new BufferedReader(inputReader);

			String line = "";
			StringBuffer jsonStr = new StringBuffer();
			while ((line = bufReader.readLine()) != null)
				jsonStr.append(line);

			JSONObject jsonObject = new JSONObject(jsonStr.toString());
			if (jsonObject != null && !jsonObject.isNull(KEY_BRANDS)) {
				JSONArray array = jsonObject.getJSONArray(KEY_BRANDS);
				for (int i = 0; i < array.length(); i++) {
					IPBrand brand = new IPBrand();
					JSONObject object = array.optJSONObject(i);
					if (!object.isNull(KEY_BRAND_POI_ID)) {
						brand.setPoiID(object.optString(KEY_BRAND_POI_ID));
					}

					if (!object.isNull(KEY_BRAND_NAME)) {
						brand.setName(object.optString(KEY_BRAND_NAME));
					}

					if (!object.isNull(KEY_BRAND_LOGO)) {
						brand.setLogo(object.optString(KEY_BRAND_LOGO));
					}

					if ((!object.isNull(KEY_BRAND_SIZE_X))
							&& (!object.isNull(KEY_BRAND_SIZE_Y))) {
						double width = object.optDouble(KEY_BRAND_SIZE_X);
						double height = object.optDouble(KEY_BRAND_SIZE_Y);
						IPLabelSize size = new IPLabelSize(width, height);
						brand.setLogoSize(size);
					}

					toReturn.add(brand);
				}
			}
			inputReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return toReturn;
	}
}
