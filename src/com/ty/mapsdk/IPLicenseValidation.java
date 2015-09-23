package com.ty.mapsdk;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ty.mapdata.TYBuilding;

class IPLicenseValidation {

	IPLicenseValidation() {
	}

	static boolean checkValidity(String userID, String license,
			TYBuilding building) {
		if (userID == null || license == null || building == null) {
			return false;
		}

		return IPMapSDKJNI.checkValidity(userID, license,
				building.getBuildingID());
	}

	static Date evaluateLicense(String userID, String license,
			TYBuilding building) {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String expiredDate = IPMapSDKJNI.getExpiredDate(userID, license,
				building.getBuildingID());

		if (expiredDate == null || expiredDate.length() == 0) {
			return null;
		}

		Date date = null;
		try {
			date = dateFormat.parse(expiredDate);
		} catch (ParseException e) {
			// e.printStackTrace();
		}

		return date;
	}

}
