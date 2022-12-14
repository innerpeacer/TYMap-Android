/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.ty.mapsdk.swig;

public class IPXGeosMutliPoint extends IPXGeosGeometryCollection {
  private long swigCPtr;

  protected IPXGeosMutliPoint(long cPtr, boolean cMemoryOwn) {
    super(IPMapSDKJNI.IPXGeosMutliPoint_SWIGUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(IPXGeosMutliPoint obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        IPMapSDKJNI.delete_IPXGeosMutliPoint(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public long getNumGeometries() {
    return IPMapSDKJNI.IPXGeosMutliPoint_getNumGeometries(swigCPtr, this);
  }

  public IPXGeosGeometry getGeometryN(long arg0) {
    long cPtr = IPMapSDKJNI.IPXGeosMutliPoint_getGeometryN(swigCPtr, this, arg0);
    return (cPtr == 0) ? null : new IPXGeosGeometry(cPtr, false);
  }

}
