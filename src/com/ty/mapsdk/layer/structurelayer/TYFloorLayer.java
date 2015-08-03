package com.ty.mapsdk.layer.structurelayer;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;

import android.content.Context;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.Renderer;
import com.esri.core.renderer.SimpleRenderer;
import com.ty.mapsdk.TYRenderingScheme;

public class TYFloorLayer extends GraphicsLayer {
	static final String TAG = TYFloorLayer.class.getSimpleName();

	Context context;
	private TYRenderingScheme renderingScheme;

	public TYFloorLayer(Context context, TYRenderingScheme renderingScheme,
			SpatialReference spatialReference, Envelope envelope) {
		super(spatialReference, envelope);
		this.context = context;
		this.renderingScheme = renderingScheme;
		setRenderer(createRenderer());
	}

	private Renderer createRenderer() {
		return new SimpleRenderer(renderingScheme.getDefaultFillSymbol());
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

}
