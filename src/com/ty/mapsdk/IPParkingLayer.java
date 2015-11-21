package com.ty.mapsdk;

import android.graphics.Color;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;

class IPParkingLayer extends GraphicsLayer {

	private int occupiedColor;
	private int availableColor;

	private SimpleLineSymbol outlineSymbol;
	private SimpleFillSymbol occupiedFillSymbol;
	private SimpleFillSymbol availableFillSymbol;

	public IPParkingLayer() {
		super();

		occupiedColor = Color.RED;
		availableColor = Color.GREEN;

		outlineSymbol = new SimpleLineSymbol(Color.WHITE, 1);
		occupiedFillSymbol = new SimpleFillSymbol(occupiedColor);
		occupiedFillSymbol.setOutline(outlineSymbol);

		availableFillSymbol = new SimpleFillSymbol(availableColor);
		availableFillSymbol.setOutline(outlineSymbol);
	}

	public void setOccupiedParkingColor(int color) {
		occupiedColor = color;
		occupiedFillSymbol = new SimpleFillSymbol(occupiedColor);
		occupiedFillSymbol.setOutline(outlineSymbol);
	}

	public void setAvaiableParkingColor(int color) {
		availableColor = color;
		availableFillSymbol = new SimpleFillSymbol(availableColor);
		availableFillSymbol.setOutline(outlineSymbol);
	}

	public SimpleFillSymbol getOccupiedParkingSymbol() {
		return occupiedFillSymbol;
	}

	public SimpleFillSymbol getAvailableParkingSymbol() {
		return availableFillSymbol;
	}

}
