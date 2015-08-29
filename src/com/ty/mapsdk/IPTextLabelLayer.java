package com.ty.mapsdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.Symbol;

class IPTextLabelLayer extends GraphicsLayer {
	static final String TAG = IPTextLabelLayer.class.getSimpleName();

	Context context;
	private IPLabelGroupLayer groupLayer;

	List<IPTextLabel> allTextLabels = new ArrayList<IPTextLabel>();
	Map<Graphic, Integer> graphicGidDict = new ConcurrentHashMap<Graphic, Integer>();

	private Map<String, IPBrand> allBrandDict = new HashMap<String, IPBrand>();

	public IPTextLabelLayer(Context context, IPLabelGroupLayer groupLayer,
			SpatialReference spatialReference, Envelope envelope) {
		super(spatialReference, envelope);
		this.groupLayer = groupLayer;
		this.context = context;
	}

	public void setBrandDict(Map<String, IPBrand> dict) {
		allBrandDict = dict;
	}

	public void updateLabels(List<IPLabelBorder> array) {
		updateLabelBorders(array);
		updateLabelState();
	}

	private synchronized void updateLabelBorders(List<IPLabelBorder> array) {
		for (IPTextLabel tl : allTextLabels) {
			Point screenPoint = groupLayer.getMapView().toScreenPoint(
					tl.getPosition());
			if (screenPoint == null) {
				continue;
			}

			IPLabelBorder border = IPLabelBorderCalculator.getTextLabelBorder(
					tl, screenPoint);

			boolean isOverlapping = false;
			for (IPLabelBorder visibleBorder : array) {
				if (IPLabelBorder.CheckIntersect(border, visibleBorder)) {
					isOverlapping = true;
					break;
				}
			}

			if (isOverlapping) {
				tl.setHidden(true);
			} else {
				tl.setHidden(false);
				array.add(border);
			}
		}
	}

	private void updateLabelState() {
		for (IPTextLabel tl : allTextLabels) {
			if (tl.isHidden()) {
				Symbol symbol = null;
				updateGraphic(graphicGidDict.get(tl.getTextGraphic()), symbol);
			} else {
				updateGraphic(graphicGidDict.get(tl.getTextGraphic()),
						tl.getTextSymbol());
			}
		}
	}

	public void loadContents(Graphic[] graphics) {
		removeAll();
		allTextLabels.clear();
		graphicGidDict.clear();

		TYMapLanguage language = TYMapEnvironment.getMapLanguage();
		String field = getNameFieldForLanguage(language);

		for (Graphic graphic : graphics) {
			String name = (String) graphic.getAttributeValue(field);

			if (name != null && name.length() > 0) {
				Point pos = (Point) graphic.getGeometry();
				IPTextLabel textLabel = new IPTextLabel(name, pos);

				String poiID = (String) graphic
						.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_POI_ID);
				if (allBrandDict.containsKey(poiID)) {
					IPBrand brand = allBrandDict.get(poiID);
					IPLabelSize logoSize = brand.getLogoSize();
					String logoName = brand.getLogo();

					int resourceID = context.getResources().getIdentifier(
							logoName, "drawable", context.getPackageName());
					TYPictureMarkerSymbol pms = new TYPictureMarkerSymbol(
							context.getResources().getDrawable(resourceID));
					pms.setWidth((float) logoSize.width);
					pms.setHeight((float) logoSize.height);

					textLabel.setTextSymbol(pms);
					textLabel.setTextSize(brand.getLogoSize());

				} else {
					// TextSymbol ts = new TextSymbol(10, name, Color.BLACK);
					// ts.setFontFamily("DroidSansFallback.ttf");
					// // ts.setFontFamily("DroidSans.ttf");
					//
					// ts.setHorizontalAlignment(HorizontalAlignment.CENTER);
					// ts.setVerticalAlignment(VerticalAlignment.MIDDLE);

					PictureMarkerSymbol ts = new PictureMarkerSymbol(
							createMapBitMap(name));

					textLabel.setTextSymbol(ts);
				}

				textLabel.setTextGraphic(graphic);
				int gid = addGraphic(graphic);

				allTextLabels.add(textLabel);
				graphicGidDict.put(graphic, gid);
			}
		}

	}

	public static Drawable createMapBitMap(String text) {

		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextSize(30);
		paint.setAntiAlias(true);
		paint.setTextAlign(Align.CENTER);

		float textLength = paint.measureText(text);

		int width = (int) textLength + 10;
		int height = 60;

		Bitmap newb = Bitmap.createBitmap(width, height, Config.ARGB_8888);

		Canvas cv = new Canvas(newb);
		cv.drawColor(Color.parseColor("#00000000"));

		cv.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG));

		cv.drawText(text, width / 2, 30, paint);

		cv.save(Canvas.ALL_SAVE_FLAG);
		cv.restore();

		return new BitmapDrawable(newb);
	}

	private String getNameFieldForLanguage(TYMapLanguage l) {
		String result = null;
		switch (l) {
		case TYSimplifiedChinese:
			result = IPMapType.NAME_FIELD_SIMPLIFIED_CHINESE;
			break;

		case TYTraditionalChinese:
			result = IPMapType.NAME_FIELD_TRADITIONAL_CHINESE;
			break;

		case TYEnglish:
			result = IPMapType.NAME_FIELD_ENGLISH;
			break;

		default:
			break;
		}
		return result;
	}
}
