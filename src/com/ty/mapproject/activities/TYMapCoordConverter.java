package com.ty.mapproject.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;

@SuppressLint("UseSparseArrays")
public class TYMapCoordConverter {

	Map<Integer, TYCoordTransform> transformDict = new HashMap<Integer, TYMapCoordConverter.TYCoordTransform>();
	Map<Integer, String> floorMap = new HashMap<Integer, String>();

	public TYMapCoordConverter(String path) {
		// System.out.println(path);
		try {
			FileInputStream inStream = new FileInputStream(new File(path));
			InputStreamReader inputReader = new InputStreamReader(inStream);
			BufferedReader bufReader = new BufferedReader(inputReader);

			String line = "";
			StringBuffer buffer = new StringBuffer();
			while ((line = bufReader.readLine()) != null)
				buffer.append(line);

			JSONArray array = new JSONArray(buffer.toString());
			for (int i = 0; i < array.length(); ++i) {
				JSONObject dict = array.getJSONObject(i);
				Integer floorNumber = dict.getInt("tyfloor");
				JSONArray tyCoordArray = dict.getJSONArray("tyCoords");
				double[] tyCoords = new double[6];
				for (int k = 0; k < 6; ++k) {
					tyCoords[k] = tyCoordArray.getDouble(k);
				}

				String thirdFloor = dict.getString("thirdfloor");
				JSONArray thirdCoordArray = dict.getJSONArray("thirdCoords");
				double[] thirdCoords = new double[6];
				for (int k = 0; k < 6; ++k) {
					thirdCoords[k] = thirdCoordArray.getDouble(k);
				}

				TYCoordTransform transform = new TYCoordTransform(tyCoords,
						thirdCoords);
				transformDict.put(floorNumber, transform);
				floorMap.put(floorNumber, thirdFloor);
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

	}

	public List<Object> getTransformedDataFromTYMap(List<Object> tyData) {
		assert (tyData != null && tyData.size() == 3);

		Integer floorNumber = (Integer) tyData.get(2);
		TYCoordTransform transform = transformDict.get(floorNumber);
		if (transform == null) {
			return tyData;
		}

		double[] thirdCoord = transform.TYMapToThirdMap(new double[] {
				(Double) tyData.get(0), (Double) tyData.get(1) });
		List<Object> result = new ArrayList<Object>();
		result.add(thirdCoord[0]);
		result.add(thirdCoord[1]);
		result.add(floorMap.get(floorNumber));
		return result;
	}

	public Map<Integer, String> getFloorMap() {
		return floorMap;
	}

	class TYCoordTransform {
		double tyCoords[];
		double tyDelta[];

		double thirdCoords[];
		double thirdDelta[];

		TYCoordTransform(double[] tyCoordArray, double[] thirdCoordArray) {
			assert (tyCoordArray != null && tyCoordArray.length == 6
					&& thirdCoordArray != null && thirdCoordArray.length == 6);

			tyCoords = new double[6];
			tyDelta = new double[4];

			thirdCoords = new double[6];
			thirdDelta = new double[4];

			System.arraycopy(tyCoordArray, 0, tyCoords, 0, 6);
			System.arraycopy(thirdCoordArray, 0, thirdCoords, 0, 6);

			thirdDelta[0] = (thirdCoords[2] - thirdCoords[0]);
			thirdDelta[1] = (thirdCoords[3] - thirdCoords[1]);
			thirdDelta[2] = (thirdCoords[4] - thirdCoords[0]);
			thirdDelta[3] = (thirdCoords[5] - thirdCoords[1]);

			tyDelta[0] = (tyCoords[2] - tyCoords[0]);
			tyDelta[1] = (tyCoords[3] - tyCoords[1]);
			tyDelta[2] = (tyCoords[4] - tyCoords[0]);
			tyDelta[3] = (tyCoords[5] - tyCoords[1]);
		}

		public double[] TYMapToThirdMap(double[] array) {
			double delta_x = getTYDeltaX(array[0]);
			double delta_y = getTYDeltaY(array[1]);

			double lamda = (delta_x * tyDelta[3] - delta_y * tyDelta[2])
					/ (tyDelta[0] * tyDelta[3] - tyDelta[1] * tyDelta[2]);
			double miu = (delta_x * tyDelta[1] - delta_y * tyDelta[0])
					/ (tyDelta[2] * tyDelta[1] - tyDelta[0] * tyDelta[3]);

			double third_x = thirdCoords[0] + lamda * thirdDelta[0] + miu
					* thirdDelta[2];
			double third_y = thirdCoords[1] + lamda * thirdDelta[1] + miu
					* thirdDelta[3];
			return new double[] { third_x, third_y };
		}

		public double[] ThirdMapToTYMap(double[] array) {
			double delta_x = getThirdDeltaX(array[0]);
			double delta_y = getThirdDeltaY(array[1]);

			double lamda = (delta_x * thirdDelta[3] - delta_y * thirdDelta[2])
					/ (thirdDelta[0] * thirdDelta[3] - thirdDelta[1]
							* thirdDelta[2]);
			double miu = (delta_x * thirdDelta[1] - delta_y * thirdDelta[0])
					/ (thirdDelta[2] * thirdDelta[1] - thirdDelta[0]
							* thirdDelta[3]);

			double ty_x = tyCoords[0] + lamda * tyDelta[0] + miu * tyDelta[2];
			double ty_y = tyCoords[1] + lamda * tyDelta[1] + miu * tyDelta[3];
			return new double[] { ty_x, ty_y };
		}

		private double getTYDeltaX(double x) {
			return (x - tyCoords[0]);
		}

		private double getTYDeltaY(double y) {
			return (y - tyCoords[1]);
		}

		private double getThirdDeltaX(double x) {
			return (x - thirdCoords[0]);
		}

		private double getThirdDeltaY(double y) {
			return (y - thirdCoords[1]);
		}

	}
}
