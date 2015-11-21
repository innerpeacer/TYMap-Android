/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.ty.mapsdk.swig;

public class IPXPathCalibration {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected IPXPathCalibration(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(IPXPathCalibration obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        IPMapSDKJNI.delete_IPXPathCalibration(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public IPXPathCalibration(String dbPath) {
    this(IPMapSDKJNI.new_IPXPathCalibration(dbPath), true);
  }

  public void setBufferWidth(double w) {
    IPMapSDKJNI.IPXPathCalibration_setBufferWidth(swigCPtr, this, w);
  }

  public IPXGeosCoordinate calibratePoint(IPXGeosCoordinate c) {
    return new IPXGeosCoordinate(IPMapSDKJNI.IPXPathCalibration_calibratePoint(swigCPtr, this, IPXGeosCoordinate.getCPtr(c), c), true);
  }

  public int getPathCount() {
    return IPMapSDKJNI.IPXPathCalibration_getPathCount(swigCPtr, this);
  }

  public IPXGeosGeometry getUnionPaths() {
    long cPtr = IPMapSDKJNI.IPXPathCalibration_getUnionPaths(swigCPtr, this);
    return (cPtr == 0) ? null : new IPXGeosGeometry(cPtr, false);
  }

  public IPXGeosGeometry getUnionPathBuffer() {
    long cPtr = IPMapSDKJNI.IPXPathCalibration_getUnionPathBuffer(swigCPtr, this);
    return (cPtr == 0) ? null : new IPXGeosGeometry(cPtr, false);
  }

}