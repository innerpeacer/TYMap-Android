package cn.nephogram.mapsdk.layer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;

import android.content.Context;
import cn.nephogram.map.R;
import cn.nephogram.mapsdk.NPPictureSymbol;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;

public class NPFacilityLayer extends GraphicsLayer {
	static final String TAG = NPFacilityLayer.class.getSimpleName();
	private Context context;

	public NPFacilityLayer(Context context, SpatialReference spatialReference,
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

			Graphic[] graphics = set.getGraphics();
			for (Graphic graphic : graphics) {
				int category = (Integer) graphic
						.getAttributeValue("CATEGORY_ID");
				NPPictureSymbol ps;
				if (category == 25134) {
					ps = new NPPictureSymbol(context.getResources()
							.getDrawable(R.drawable.icon_stair));
					ps.setWidth(16);
					ps.setHeight(16);
				} else if (category == 25136) {
					ps = new NPPictureSymbol(context.getResources()
							.getDrawable(R.drawable.icon_lift));
					ps.setWidth(16);
					ps.setHeight(16);
				} else if (category == 25000) {
					ps = new NPPictureSymbol(context.getResources()
							.getDrawable(R.drawable.icon_exit));
					ps.setWidth(16);
					ps.setHeight(16);
				} else {
					continue;
				}

				Graphic g = new Graphic(graphic.getGeometry(), ps);
				addGraphic(g);
			}

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadContentsFromAssetsWithInfo(String path) {
		removeAll();
		JsonFactory factory = new JsonFactory();

		try {
			InputStream inStream = context.getAssets().open(path);
			JsonParser parser = factory.createJsonParser(inStream);
			FeatureSet set = FeatureSet.fromJson(parser);

			Graphic[] graphics = set.getGraphics();
			for (Graphic graphic : graphics) {
				int category = (Integer) graphic
						.getAttributeValue("CATEGORY_ID");
				NPPictureSymbol ps;
				if (category == 25134) {
					ps = new NPPictureSymbol(context.getResources()
							.getDrawable(R.drawable.icon_stair));
					ps.setWidth(16);
					ps.setHeight(16);
				} else if (category == 25136) {
					ps = new NPPictureSymbol(context.getResources()
							.getDrawable(R.drawable.icon_lift));
					ps.setWidth(16);
					ps.setHeight(16);
				} else if (category == 25000) {
					ps = new NPPictureSymbol(context.getResources()
							.getDrawable(R.drawable.icon_exit));
					ps.setWidth(16);
					ps.setHeight(16);
				} else {
					continue;
				}

				Graphic g = new Graphic(graphic.getGeometry(), ps);
				addGraphic(g);
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
