package cn.nephogram.mapsdk.layer.labellayer;

import android.text.TextPaint;

import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.Symbol;

public class NPTextLabel {
	private static final int TEXT_SIZE = 10;
	private static TextPaint textPaint = new TextPaint();
	static {
		textPaint.setTextSize(TEXT_SIZE);
	}

	Point position;
	String text;
	double textLength;
	NPLabelSize textSize;

	Graphic textGraphic;
	Symbol textSymbol;

	private boolean isHidden;

	public NPTextLabel(String name, Point pos) {
		isHidden = false;
		this.position = pos;
		this.text = name;
		textLength = textPaint.measureText(name);
		textSize = new NPLabelSize(textLength, TEXT_SIZE);
	}

	public Point getPosition() {
		return position;
	}

	public double getTextLength() {
		return textLength;
	}

	public NPLabelSize getTextSize() {
		return textSize;
	}

	public void setTextSize(NPLabelSize textSize) {
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
