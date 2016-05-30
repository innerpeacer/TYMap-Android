package com.ty.mapsdk;

import android.text.TextPaint;

import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.Symbol;

class IPLBTextLabel {
	private static final int DEFAULT_MAX_LEVEL = 10000;
	private static final int DEFAULT_MIN_LEVEL = -1;

	private static final int TEXT_SIZE = 10;
	private static TextPaint textPaint = new TextPaint();
	static {
		textPaint.setTextSize(TEXT_SIZE);
	}

	Point position;
	String text;
	double textLength;
	IPLBLabelSize textSize;

	Graphic textGraphic;
	Symbol textSymbol;
	int maxLevel = DEFAULT_MAX_LEVEL;
	int minLevel = DEFAULT_MIN_LEVEL;

	private boolean isHidden;

	public IPLBTextLabel(String name, Point pos) {
		isHidden = false;
		this.position = pos;
		this.text = name;
		textLength = textPaint.measureText(name);
		textSize = new IPLBLabelSize(textLength, TEXT_SIZE);
	}

	public Point getPosition() {
		return position;
	}

	public double getTextLength() {
		return textLength;
	}

	public IPLBLabelSize getTextSize() {
		return textSize;
	}

	public void setTextSize(IPLBLabelSize textSize) {
		this.textSize = textSize;
	}

	public Graphic getTextGraphic() {
		return textGraphic;
	}

	public void setTextGraphic(Graphic textGraphic) {
		this.textGraphic = textGraphic;
	}

	public Symbol getTextSymbol() {
		return textSymbol;
	}

	public void setTextSymbol(Symbol textSymbol) {
		this.textSymbol = textSymbol;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public boolean isHidden() {
		return isHidden;
	}
}
