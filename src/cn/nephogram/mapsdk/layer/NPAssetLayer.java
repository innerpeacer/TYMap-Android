package cn.nephogram.mapsdk.layer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;

import android.content.Context;
import android.graphics.Color;
import cn.nephogram.mapsdk.CARenderColor;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.Renderer;
import com.esri.core.renderer.UniqueValue;
import com.esri.core.renderer.UniqueValueRenderer;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;

public class NPAssetLayer extends GraphicsLayer {
	static final String TAG = NPAssetLayer.class.getSimpleName();

	private Context context;

	public NPAssetLayer(Context context, SpatialReference spatialReference,
			Envelope envelope) {
		super(spatialReference, envelope);
		this.context = context;

		setRenderer(createAssetRenderer());
	}

	private Renderer createAssetRenderer() {
		UniqueValueRenderer shopRenderer = new UniqueValueRenderer();
		List<UniqueValue> uvInfo = new ArrayList<UniqueValue>();

		SimpleLineSymbol sls = new SimpleLineSymbol(
				CARenderColor.getOutlineColor(), 0.5f);

		{
			SimpleFillSymbol parklotFillSymbol = new SimpleFillSymbol(
					Color.argb(255, 104, 220, 0));
			parklotFillSymbol.setOutline(sls);
			UniqueValue uvParklot = new UniqueValue();
			uvParklot.setSymbol(parklotFillSymbol);
			uvParklot.setValue(new Integer[] { 28001 });
			uvInfo.add(uvParklot);
		}

		{
			SimpleFillSymbol deskFillSymbol = new SimpleFillSymbol(Color.argb(
					255, 104, 220, 0));
			deskFillSymbol.setOutline(sls);
			UniqueValue uvDesk = new UniqueValue();
			uvDesk.setSymbol(deskFillSymbol);
			uvDesk.setValue(new Integer[] { 27002 });
			uvInfo.add(uvDesk);
		}
		shopRenderer.setField1("CATEGORY_ID");
		shopRenderer.setUniqueValueInfos(uvInfo);

		SimpleFillSymbol defaultFillSymbol = new SimpleFillSymbol(Color.argb(
				255, 255, 253, 231));
		SimpleLineSymbol defaultLineSymbol = new SimpleLineSymbol(Color.argb(
				255, 195, 141, 115), 0.5f);
		defaultFillSymbol.setOutline(defaultLineSymbol);
		shopRenderer.setDefaultSymbol(defaultFillSymbol);

		return shopRenderer;
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
