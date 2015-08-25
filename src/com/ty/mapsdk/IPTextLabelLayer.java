package com.ty.mapsdk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;

import android.content.Context;
import android.graphics.Color;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.Symbol;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.symbol.TextSymbol.HorizontalAlignment;
import com.esri.core.symbol.TextSymbol.VerticalAlignment;

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

	public void loadContentsFromFileWithInfo(String path) {
		removeAll();

		allTextLabels.clear();
		graphicGidDict.clear();

		JsonFactory factory = new JsonFactory();
		try {
			JsonParser parser = factory.createJsonParser(new File(path));
			FeatureSet set = FeatureSet.fromJson(parser);

			TYMapLanguage language = TYMapEnvironment.getMapLanguage();
			String field = getNameFieldForLanguage(language);

			Graphic[] graphics = set.getGraphics();
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
						TextSymbol ts = new TextSymbol(10, name, Color.BLACK);
						// ts.setFontFamily("DroidSansFallback.ttf");
						ts.setFontFamily("DroidSans.ttf");

						ts.setHorizontalAlignment(HorizontalAlignment.CENTER);
						ts.setVerticalAlignment(VerticalAlignment.MIDDLE);

						textLabel.setTextSymbol(ts);
					}

					textLabel.setTextGraphic(graphic);
					int gid = addGraphic(graphic);

					allTextLabels.add(textLabel);
					graphicGidDict.put(graphic, gid);
				}
			}

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
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