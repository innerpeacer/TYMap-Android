package cn.nephogram.mapsdk.layer.labellayer;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;

import android.content.Context;
import android.graphics.Color;
import cn.nephogram.mapsdk.NPMapEnvironment;
import cn.nephogram.mapsdk.NPMapLanguage;
import cn.nephogram.mapsdk.NPMapType;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.symbol.TextSymbol.HorizontalAlignment;
import com.esri.core.symbol.TextSymbol.VerticalAlignment;

public class NPTextLabelLayer extends GraphicsLayer {
	static final String TAG = NPTextLabelLayer.class.getSimpleName();

	Context context;

	public NPTextLabelLayer(Context context, SpatialReference spatialReference,
			Envelope envelope) {
		super(spatialReference, envelope);
		this.context = context;
	}

	public void loadContentsFromFileWithInfo(String path) {
		removeAll();

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
					TextSymbol ts = new TextSymbol(10, name, Color.BLACK);
					ts.setFontFamily("DroidSansFallback.ttf");
					ts.setHorizontalAlignment(HorizontalAlignment.CENTER);
					ts.setVerticalAlignment(VerticalAlignment.MIDDLE);

					Graphic g = new Graphic(graphic.getGeometry(), ts);
					addGraphic(g);
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
