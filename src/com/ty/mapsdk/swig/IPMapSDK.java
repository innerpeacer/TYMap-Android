/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.ty.mapsdk.swig;

public class IPMapSDK {
  public static String decryptString(String str) {
    return IPMapSDKJNI.decryptString__SWIG_0(str);
  }

  public static String decryptString(String str, String key) {
    return IPMapSDKJNI.decryptString__SWIG_1(str, key);
  }

  public static String encryptString(String str) {
    return IPMapSDKJNI.encryptString__SWIG_0(str);
  }

  public static String encryptString(String originalString, String key) {
    return IPMapSDKJNI.encryptString__SWIG_1(originalString, key);
  }

  public static void encryptFile(String originalPath, String encryptedFile) {
    IPMapSDKJNI.encryptFile__SWIG_0(originalPath, encryptedFile);
  }

  public static void encryptFile(String originalPath, String encryptedFile, String key) {
    IPMapSDKJNI.encryptFile__SWIG_1(originalPath, encryptedFile, key);
  }

  public static String decryptFile(String file) {
    return IPMapSDKJNI.decryptFile__SWIG_0(file);
  }

  public static String decryptFile(String file, String key) {
    return IPMapSDKJNI.decryptFile__SWIG_1(file, key);
  }

  public static void encryptBytes(String originalBytes, String encryptedByte, int length) {
    IPMapSDKJNI.encryptBytes__SWIG_0(originalBytes, encryptedByte, length);
  }

  public static void encryptBytes(String originalBytes, String encryptedByte, int length, String key) {
    IPMapSDKJNI.encryptBytes__SWIG_1(originalBytes, encryptedByte, length, key);
  }

  public static void decryptBytes(String encryptedBytes, String originalBytes, int length) {
    IPMapSDKJNI.decryptBytes__SWIG_0(encryptedBytes, originalBytes, length);
  }

  public static void decryptBytes(String encryptedBytes, String originalBytes, int length, String key) {
    IPMapSDKJNI.decryptBytes__SWIG_1(encryptedBytes, originalBytes, length, key);
  }

  public static boolean checkValidity(String userID, String license, String buildingID) {
    return IPMapSDKJNI.checkValidity(userID, license, buildingID);
  }

  public static String getExpiredDate(String userID, String license, String buildingID) {
    return IPMapSDKJNI.getExpiredDate(userID, license, buildingID);
  }

  public static IPXGeosPoint CastedPoint(IPXGeosGeometry g) {
    long cPtr = IPMapSDKJNI.CastedPoint(IPXGeosGeometry.getCPtr(g), g);
    return (cPtr == 0) ? null : new IPXGeosPoint(cPtr, false);
  }

  public static IPXGeosMutliPoint CastedMultiPoint(IPXGeosGeometry g) {
    long cPtr = IPMapSDKJNI.CastedMultiPoint(IPXGeosGeometry.getCPtr(g), g);
    return (cPtr == 0) ? null : new IPXGeosMutliPoint(cPtr, false);
  }

  public static IPXGeosLineString CastedLineString(IPXGeosGeometry g) {
    long cPtr = IPMapSDKJNI.CastedLineString(IPXGeosGeometry.getCPtr(g), g);
    return (cPtr == 0) ? null : new IPXGeosLineString(cPtr, false);
  }

  public static IPXGeosMultiLineString CastedMultiLineString(IPXGeosGeometry g) {
    long cPtr = IPMapSDKJNI.CastedMultiLineString(IPXGeosGeometry.getCPtr(g), g);
    return (cPtr == 0) ? null : new IPXGeosMultiLineString(cPtr, false);
  }

  public static IPXGeosPolygon CastedPolygon(IPXGeosGeometry g) {
    long cPtr = IPMapSDKJNI.CastedPolygon(IPXGeosGeometry.getCPtr(g), g);
    return (cPtr == 0) ? null : new IPXGeosPolygon(cPtr, false);
  }

  public static IPXGeosMultiPolygon CastedMultiPolygon(IPXGeosGeometry g) {
    long cPtr = IPMapSDKJNI.CastedMultiPolygon(IPXGeosGeometry.getCPtr(g), g);
    return (cPtr == 0) ? null : new IPXGeosMultiPolygon(cPtr, false);
  }

  public static IPXGeosPoint getPointN(IPXGeosMutliPoint mp, long n) {
    long cPtr = IPMapSDKJNI.getPointN(IPXGeosMutliPoint.getCPtr(mp), mp, n);
    return (cPtr == 0) ? null : new IPXGeosPoint(cPtr, false);
  }

  public static IPXGeosLineString getLineStringN(IPXGeosMultiLineString ml, long n) {
    long cPtr = IPMapSDKJNI.getLineStringN(IPXGeosMultiLineString.getCPtr(ml), ml, n);
    return (cPtr == 0) ? null : new IPXGeosLineString(cPtr, false);
  }

  public static IPXGeosPolygon getPolygonN(IPXGeosMultiPolygon mp, long n) {
    long cPtr = IPMapSDKJNI.getPolygonN(IPXGeosMultiPolygon.getCPtr(mp), mp, n);
    return (cPtr == 0) ? null : new IPXGeosPolygon(cPtr, false);
  }

}
