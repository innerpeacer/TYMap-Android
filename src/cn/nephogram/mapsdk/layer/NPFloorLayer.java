package cn.nephogram.mapsdk.layer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;

import android.content.Context;
import cn.nephogram.mapsdk.CARenderColor;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.Renderer;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;

public class NPFloorLayer extends GraphicsLayer {
	static final String TAG = NPFloorLayer.class.getSimpleName();
	private Context context;

	public NPFloorLayer(Context context, SpatialReference spatialReference,
			Envelope envelope) {
		super(spatialReference, envelope);
		this.context = context;

		setRenderer(createRenderer());
	}

	private Renderer createRenderer() {
		SimpleFillSymbol floorSymbol = new SimpleFillSymbol(
				CARenderColor.getFloorColor1());
		SimpleLineSymbol outlineSymbol = new SimpleLineSymbol(
				CARenderColor.getOutlineColor(), 1);
		floorSymbol.setOutline(outlineSymbol);
		return new SimpleRenderer(floorSymbol);
	}

	public void loadContentsFromFileWithInfo(String path) {
		removeAll();

		JsonFactory factory = new JsonFactory();

		try {
			JsonParser parser = factory.createJsonParser(new File(path));
			FeatureSet set = FeatureSet.fromJson(parser);

			Graphic[] graphics = set.getGraphics();
			addGraphics(graphics);
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
			addGraphics(graphics);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
