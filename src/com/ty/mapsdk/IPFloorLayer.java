package com.ty.mapsdk;

import android.content.Context;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.Renderer;
import com.esri.core.renderer.SimpleRenderer;

class IPFloorLayer extends GraphicsLayer {
	static final String TAG = IPFloorLayer.class.getSimpleName();

	Context context;
	private TYRenderingScheme renderingScheme;

	public IPFloorLayer(Context context, TYRenderingScheme renderingScheme,
			SpatialReference spatialReference, Envelope envelope) {
		super(spatialReference, envelope);
		this.context = context;
		this.renderingScheme = renderingScheme;
		setRenderer(createRenderer());
	}

	private Renderer createRenderer() {
		return new SimpleRenderer(renderingScheme.getDefaultFillSymbol());
	}

	public void loadContents(FeatureSet set) {
		removeAll();

		if (set != null) {
			Graphic[] graphics = set.getGraphics();
			addGraphics(graphics);
		}
	}

	// public void loadContentsFromFileWithInfo(String path) {
	// removeAll();
	//
	// JsonFactory factory = new JsonFactory();
	// try {
	// JsonParser parser = factory.createJsonParser(new File(path));
	// FeatureSet set = FeatureSet.fromJson(parser);
	//
	// Graphic[] graphics = set.getGraphics();
	// addGraphics(graphics);
	// } catch (JsonParseException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

}
