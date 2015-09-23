package com.ty.mapsdk;

import android.content.Context;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.Renderer;
import com.esri.core.renderer.SimpleRenderer;

class IPFloorLayer extends GraphicsLayer {
	static final String TAG = IPFloorLayer.class.getSimpleName();

	Context context;
	TYRenderingScheme renderingScheme;

	public IPFloorLayer(Context context, TYRenderingScheme renderingScheme,
			SpatialReference spatialReference, Envelope envelope) {
		super(spatialReference, envelope);
		this.context = context;
		this.renderingScheme = renderingScheme;
		setRenderer(createRenderer());
	}

	public void setRenderScheme(TYRenderingScheme rs) {
		this.renderingScheme = rs;
		setRenderer(createRenderer());
	}

	private Renderer createRenderer() {
		return new SimpleRenderer(renderingScheme.getDefaultFillSymbol());
		// return new SimpleRenderer(new SimpleFillSymbol(Color.WHITE));
	}

	public void loadContents(Graphic[] graphics) {
		removeAll();
		addGraphics(graphics);
	}

}
