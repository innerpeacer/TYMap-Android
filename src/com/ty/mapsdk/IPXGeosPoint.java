/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.ty.mapsdk;

class IPXGeosPoint {
	private long swigCPtr;
	protected boolean swigCMemOwn;

	protected IPXGeosPoint(long cPtr, boolean cMemoryOwn) {
		swigCMemOwn = cMemoryOwn;
		swigCPtr = cPtr;
	}

	protected static long getCPtr(IPXGeosPoint obj) {
		return (obj == null) ? 0 : obj.swigCPtr;
	}

	protected void finalize() {
		delete();
	}

	public synchronized void delete() {
		if (swigCPtr != 0) {
			if (swigCMemOwn) {
				swigCMemOwn = false;
				IPMapSDKJNI.delete_IPXGeosPoint(swigCPtr);
			}
			swigCPtr = 0;
		}
	}

	public double getX() {
		return IPMapSDKJNI.IPXGeosPoint_getX(swigCPtr, this);
	}

	public double getY() {
		return IPMapSDKJNI.IPXGeosPoint_getY(swigCPtr, this);
	}

}
