package com.ty.mapsdk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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
import android.util.Log;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.Symbol;

class IPLATextLabelLayer extends GraphicsLayer {
	static final String TAG = IPLATextLabelLayer.class.getSimpleName();

	Context context;
	private IPLALabelGroupLayer groupLayer;

	// List<IPTextLabel> allTextLabels = new ArrayList<IPTextLabel>();
	List<IPLBTextLabel> allTextLabels = new CopyOnWriteArrayList<IPLBTextLabel>();

	Map<Graphic, Integer> graphicGidDict = new ConcurrentHashMap<Graphic, Integer>();

	private Map<String, IPHPBrand> allBrandDict = new HashMap<String, IPHPBrand>();

	public IPLATextLabelLayer(Context context, IPLALabelGroupLayer groupLayer,
			SpatialReference spatialReference, Envelope envelope) {
		super(spatialReference, envelope);
		this.groupLayer = groupLayer;
		this.context = context;
	}

	public void setBrandDict(Map<String, IPHPBrand> dict) {
		allBrandDict = dict;
	}

	public void updateLabels(List<IPLBLabelBorder> array) {
		updateLabelBorders(array);
		updateLabelState();
	}

	private synchronized void updateLabelBorders(List<IPLBLabelBorder> array) {
		int currentLevel = groupLayer.getMapView().getCurrentLevel();

		if (groupLayer.getMapView().isLabelOverlapDetectingEnabled()) {
			for (IPLBTextLabel tl : allTextLabels) {
				if (!(currentLevel <= tl.maxLevel && currentLevel >= tl.minLevel)) {
					tl.setHidden(true);
					continue;
				}

				Point screenPoint = groupLayer.getMapView().toScreenPoint(
						tl.getPosition());
				if (screenPoint == null) {
					continue;
				}

				IPLBLabelBorder border = IPLBLabelBorderCalculator
						.getTextLabelBorder(tl, screenPoint);

				boolean isOverlapping = false;
				for (IPLBLabelBorder visibleBorder : array) {
					if (IPLBLabelBorder.CheckIntersect(border, visibleBorder)) {
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
		} else {
			for (IPLBTextLabel tl : allTextLabels) {
				if (currentLevel <= tl.maxLevel && currentLevel >= tl.minLevel) {
					tl.setHidden(false);
				} else {
					tl.setHidden(true);
				}
				// tl.setHidden(false);
			}
		}

	}

	private void updateLabelState() {
		try {
			for (IPLBTextLabel tl : allTextLabels) {
				if (tl.isHidden()) {
					Symbol symbol = null;
					updateGraphic(graphicGidDict.get(tl.getTextGraphic()),
							symbol);
				} else {
					updateGraphic(graphicGidDict.get(tl.getTextGraphic()),
							tl.getTextSymbol());
				}
			}
		} catch (Exception e) {
			Log.i(TAG, e.toString());
		}

	}

	public void loadContents(Graphic[] graphics) {
		// Log.i(TAG, "loadContents");

		removeAll();
		allTextLabels.clear();
		graphicGidDict.clear();

		TYMapLanguage language = TYMapEnvironment.getMapLanguage();
		String field = getNameFieldForLanguage(language);
		// Log.i(TAG, field);

		for (Graphic graphic : graphics) {
			// Log.i(TAG, graphic + "");
			String name = (String) graphic.getAttributeValue(field);

			int maxLevel = (Integer) graphic
					.getAttributeValue(IPHPMapType.GRAPHIC_ATTRIBUTE_LEVEL_MAX);
			int minLevel = (Integer) graphic
					.getAttributeValue(IPHPMapType.GRAPHIC_ATTRIBUTE_LEVEL_MIN);

			if (name != null && name.length() > 0) {
				Point pos = (Point) graphic.getGeometry();
				IPLBTextLabel textLabel = new IPLBTextLabel(name, pos);

				if (maxLevel != 0) {
					textLabel.maxLevel = maxLevel;
				}

				if (minLevel != 0) {
					textLabel.minLevel = minLevel;
				}

				String poiID = (String) graphic
						.getAttributeValue(IPHPMapType.GRAPHIC_ATTRIBUTE_POI_ID);
				if (allBrandDict.containsKey(poiID)) {
					IPHPBrand brand = allBrandDict.get(poiID);
					IPLBLabelSize logoSize = brand.getLogoSize();
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

	@SuppressWarnings("deprecation")
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
			result = IPHPMapType.NAME_FIELD_SIMPLIFIED_CHINESE;
			break;

		case TYTraditionalChinese:
			result = IPHPMapType.NAME_FIELD_TRADITIONAL_CHINESE;
			break;

		case TYEnglish:
			result = IPHPMapType.NAME_FIELD_ENGLISH;
			break;

		default:
			break;
		}
		return result;
	}
}
