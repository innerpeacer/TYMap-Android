package cn.nephogram.mapsdk.layer.labellayer;

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
import cn.nephogram.mapsdk.NPMapEnvironment;
import cn.nephogram.mapsdk.NPMapLanguage;
import cn.nephogram.mapsdk.NPMapType;
import cn.nephogram.mapsdk.entity.NPPictureMarkerSymbol;
import cn.nephogram.mapsdk.poi.NPBrand;

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

public class NPTextLabelLayer extends GraphicsLayer {
	static final String TAG = NPTextLabelLayer.class.getSimpleName();

	Context context;
	private NPLabelGroupLayer groupLayer;

	List<NPTextLabel> allTextLabels = new ArrayList<NPTextLabel>();
	Map<Graphic, Integer> graphicGidDict = new ConcurrentHashMap<Graphic, Integer>();

	private Map<String, NPBrand> allBrandDict = new HashMap<String, NPBrand>();

	public NPTextLabelLayer(Context context, NPLabelGroupLayer groupLayer,
			SpatialReference spatialReference, Envelope envelope) {
		super(spatialReference, envelope);
		this.groupLayer = groupLayer;
		this.context = context;
	}

	public void setBrandDict(Map<String, NPBrand> dict) {
		allBrandDict = dict;
	}

	public void updateLabels(List<NPLabelBorder> array) {
		updateLabelBorders(array);
		updateLabelState();
	}

	private synchronized void updateLabelBorders(List<NPLabelBorder> array) {
		for (NPTextLabel tl : allTextLabels) {
			Point screenPoint = groupLayer.getMapView().toScreenPoint(
					tl.getPosition());
			if (screenPoint == null) {
				continue;
			}

			NPLabelBorder border = NPLabelBorderCalculator.getTextLabelBorder(
					tl, screenPoint);

			boolean isOverlapping = false;
			for (NPLabelBorder visibleBorder : array) {
				if (NPLabelBorder.CheckIntersect(border, visibleBorder)) {
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
		for (NPTextLabel tl : allTextLabels) {
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

			NPMapLanguage language = NPMapEnvironment.getMapLanguage();
			String field = getNameFieldForLanguage(language);

			Graphic[] graphics = set.getGraphics();
			for (Graphic graphic : graphics) {
				String name = (String) graphic.getAttributeValue(field);

				if (name != null && name.length() > 0) {
					Point pos = (Point) graphic.getGeometry();
					NPTextLabel textLabel = new NPTextLabel(name, pos);

					String poiID = (String) graphic
							.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_POI_ID);
					if (allBrandDict.containsKey(poiID)) {
						NPBrand brand = allBrandDict.get(poiID);
						NPLabelSize logoSize = brand.getLogoSize();
						String logoName = brand.getLogo();

						int resourceID = context.getResources().getIdentifier(
								logoName, "drawable", context.getPackageName());
						NPPictureMarkerSymbol pms = new NPPictureMarkerSymbol(
								context.getResources().getDrawable(resourceID));
						pms.setWidth((float) logoSize.width);
						pms.setHeight((float) logoSize.height);

						textLabel.setTextSymbol(pms);
						textLabel.setTextSize(brand.getLogoSize());

					} else {
						TextSymbol ts = new TextSymbol(10, name, Color.BLACK);
						ts.setFontFamily("DroidSansFallback.ttf");
						// ts.setFontFamily("DroidSans.ttf");

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

	private String getNameFieldForLanguage(NPMapLanguage l) {
		String result = null;
		switch (l) {
		case NPSimplifiedChinese:
			result = NPMapType.NAME_FIELD_SIMPLIFIED_CHINESE;
			break;

		case NPTraditionalChinese:
			result = NPMapType.NAME_FIELD_TRADITIONAL_CHINESE;
			break;

		case NPEnglish:
			result = NPMapType.NAME_FIELD_ENGLISH;
			break;

		default:
			break;
		}
		return result;
	}
}
