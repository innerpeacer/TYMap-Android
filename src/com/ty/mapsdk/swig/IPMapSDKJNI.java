/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.ty.mapsdk.swig;

public class IPMapSDKJNI {
  public final static native void delete_IPXGeosGeometry(long jarg1);
  public final static native int IPXGeosGeometry_getGeometryTypeId(long jarg1, IPXGeosGeometry jarg1_);
  public final static native void IPXGeosCoordinate_x_set(long jarg1, IPXGeosCoordinate jarg1_, double jarg2);
  public final static native double IPXGeosCoordinate_x_get(long jarg1, IPXGeosCoordinate jarg1_);
  public final static native void IPXGeosCoordinate_y_set(long jarg1, IPXGeosCoordinate jarg1_, double jarg2);
  public final static native double IPXGeosCoordinate_y_get(long jarg1, IPXGeosCoordinate jarg1_);
  public final static native long new_IPXGeosCoordinate();
  public final static native void delete_IPXGeosCoordinate(long jarg1);
  public final static native double IPXGeosPoint_getX(long jarg1, IPXGeosPoint jarg1_);
  public final static native double IPXGeosPoint_getY(long jarg1, IPXGeosPoint jarg1_);
  public final static native void delete_IPXGeosPoint(long jarg1);
  public final static native long IPXGeosGeometryFactory_createPoint(long jarg1, IPXGeosGeometryFactory jarg1_, long jarg2, IPXGeosCoordinate jarg2_);
  public final static native long new_IPXGeosGeometryFactory();
  public final static native void delete_IPXGeosGeometryFactory(long jarg1);
  public final static native void delete_IPXGeosLineString(long jarg1);
  public final static native long IPXGeosLineString_getCoordinateN(long jarg1, IPXGeosLineString jarg1_, int jarg2);
  public final static native long IPXGeosLineString_getNumPoints(long jarg1, IPXGeosLineString jarg1_);
  public final static native long IPXGeosLineString_getCoordinate(long jarg1, IPXGeosLineString jarg1_);
  public final static native double IPXGeosLineString_getLength(long jarg1, IPXGeosLineString jarg1_);
  public final static native void delete_IPXGeosPolygon(long jarg1);
  public final static native long IPXGeosPolygon_getExteriorRing(long jarg1, IPXGeosPolygon jarg1_);
  public final static native long IPXGeosPolygon_getNumInteriorRing(long jarg1, IPXGeosPolygon jarg1_);
  public final static native long IPXGeosPolygon_getInteriorRingN(long jarg1, IPXGeosPolygon jarg1_, long jarg2);
  public final static native void delete_IPXGeosMultiPolygon(long jarg1);
  public final static native long IPXGeosMultiPolygon_getNumGeometries(long jarg1, IPXGeosMultiPolygon jarg1_);
  public final static native long IPXGeosMultiPolygon_getGeometryN(long jarg1, IPXGeosMultiPolygon jarg1_, long jarg2);
  public final static native String md5(String jarg1);
  public final static native String decryptString__SWIG_0(String jarg1);
  public final static native String encryptString__SWIG_0(String jarg1);
  public final static native String decryptFile__SWIG_0(String jarg1);
  public final static native String decryptString__SWIG_1(String jarg1, String jarg2);
  public final static native String encryptString__SWIG_1(String jarg1, String jarg2);
  public final static native void encryptFile(String jarg1, String jarg2, String jarg3);
  public final static native String decryptFile__SWIG_1(String jarg1, String jarg2);
  public final static native boolean checkValidity(String jarg1, String jarg2, String jarg3);
  public final static native String getExpiredDate(String jarg1, String jarg2, String jarg3);
  public final static native long new_IPXRouteNetworkDataset();
  public final static native void delete_IPXRouteNetworkDataset(long jarg1);
  public final static native long IPXRouteNetworkDataset_getShorestPath(long jarg1, IPXRouteNetworkDataset jarg1_, long jarg2, IPXGeosPoint jarg2_, long jarg3, IPXGeosPoint jarg3_);
  public final static native String IPXRouteNetworkDataset_toString(long jarg1, IPXRouteNetworkDataset jarg1_);
  public final static native long new_IPXRouteNetworkDBAdapter(String jarg1);
  public final static native boolean IPXRouteNetworkDBAdapter_open(long jarg1, IPXRouteNetworkDBAdapter jarg1_);
  public final static native boolean IPXRouteNetworkDBAdapter_close(long jarg1, IPXRouteNetworkDBAdapter jarg1_);
  public final static native long IPXRouteNetworkDBAdapter_readRouteNetworkDataset(long jarg1, IPXRouteNetworkDBAdapter jarg1_);
  public final static native void delete_IPXRouteNetworkDBAdapter(long jarg1);
  public final static native void IPXFeatureRecord_geometry_set(long jarg1, IPXFeatureRecord jarg1_, long jarg2, IPXGeosGeometry jarg2_);
  public final static native long IPXFeatureRecord_geometry_get(long jarg1, IPXFeatureRecord jarg1_);
  public final static native void IPXFeatureRecord_point_set(long jarg1, IPXFeatureRecord jarg1_, long jarg2, IPXGeosPoint jarg2_);
  public final static native long IPXFeatureRecord_point_get(long jarg1, IPXFeatureRecord jarg1_);
  public final static native void IPXFeatureRecord_polygon_set(long jarg1, IPXFeatureRecord jarg1_, long jarg2, IPXGeosPolygon jarg2_);
  public final static native long IPXFeatureRecord_polygon_get(long jarg1, IPXFeatureRecord jarg1_);
  public final static native void IPXFeatureRecord_geoID_set(long jarg1, IPXFeatureRecord jarg1_, String jarg2);
  public final static native String IPXFeatureRecord_geoID_get(long jarg1, IPXFeatureRecord jarg1_);
  public final static native void IPXFeatureRecord_poiID_set(long jarg1, IPXFeatureRecord jarg1_, String jarg2);
  public final static native String IPXFeatureRecord_poiID_get(long jarg1, IPXFeatureRecord jarg1_);
  public final static native void IPXFeatureRecord_categoryID_set(long jarg1, IPXFeatureRecord jarg1_, String jarg2);
  public final static native String IPXFeatureRecord_categoryID_get(long jarg1, IPXFeatureRecord jarg1_);
  public final static native void IPXFeatureRecord_name_set(long jarg1, IPXFeatureRecord jarg1_, String jarg2);
  public final static native String IPXFeatureRecord_name_get(long jarg1, IPXFeatureRecord jarg1_);
  public final static native void IPXFeatureRecord_symbolID_set(long jarg1, IPXFeatureRecord jarg1_, int jarg2);
  public final static native int IPXFeatureRecord_symbolID_get(long jarg1, IPXFeatureRecord jarg1_);
  public final static native void IPXFeatureRecord_floorNumber_set(long jarg1, IPXFeatureRecord jarg1_, int jarg2);
  public final static native int IPXFeatureRecord_floorNumber_get(long jarg1, IPXFeatureRecord jarg1_);
  public final static native void IPXFeatureRecord_layer_set(long jarg1, IPXFeatureRecord jarg1_, int jarg2);
  public final static native int IPXFeatureRecord_layer_get(long jarg1, IPXFeatureRecord jarg1_);
  public final static native long new_IPXFeatureRecord();
  public final static native void delete_IPXFeatureRecord(long jarg1);
  public final static native long new_VectorOfFeatureRecord__SWIG_0();
  public final static native long new_VectorOfFeatureRecord__SWIG_1(int jarg1);
  public final static native int VectorOfFeatureRecord_size(long jarg1, VectorOfFeatureRecord jarg1_);
  public final static native int VectorOfFeatureRecord_capacity(long jarg1, VectorOfFeatureRecord jarg1_);
  public final static native void VectorOfFeatureRecord_reserve(long jarg1, VectorOfFeatureRecord jarg1_, int jarg2);
  public final static native boolean VectorOfFeatureRecord_isEmpty(long jarg1, VectorOfFeatureRecord jarg1_);
  public final static native void VectorOfFeatureRecord_clear(long jarg1, VectorOfFeatureRecord jarg1_);
  public final static native void VectorOfFeatureRecord_add(long jarg1, VectorOfFeatureRecord jarg1_, long jarg2, IPXFeatureRecord jarg2_);
  public final static native long VectorOfFeatureRecord_get(long jarg1, VectorOfFeatureRecord jarg1_, int jarg2);
  public final static native void VectorOfFeatureRecord_set(long jarg1, VectorOfFeatureRecord jarg1_, int jarg2, long jarg3, IPXFeatureRecord jarg3_);
  public final static native void delete_VectorOfFeatureRecord(long jarg1);
  public final static native long new_IPXMapDataDBAdapter(String jarg1);
  public final static native boolean IPXMapDataDBAdapter_open(long jarg1, IPXMapDataDBAdapter jarg1_);
  public final static native boolean IPXMapDataDBAdapter_close(long jarg1, IPXMapDataDBAdapter jarg1_);
  public final static native long IPXMapDataDBAdapter_getAllRecordsOnFloor(long jarg1, IPXMapDataDBAdapter jarg1_, int jarg2);
  public final static native void delete_IPXMapDataDBAdapter(long jarg1);
  public final static native long IPXGeosPoint_SWIGUpcast(long jarg1);
  public final static native long IPXGeosLineString_SWIGUpcast(long jarg1);
  public final static native long IPXGeosPolygon_SWIGUpcast(long jarg1);
  public final static native long IPXGeosMultiPolygon_SWIGUpcast(long jarg1);
}
