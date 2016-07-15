package com.ty.mapsdk;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.Renderer;
import com.esri.core.renderer.UniqueValue;
import com.esri.core.renderer.UniqueValueRenderer;
import com.esri.core.symbol.SimpleFillSymbol;

public class IPLAShadeLayer extends GraphicsLayer {
	static final String TAG = IPLAShadeLayer.class.getSimpleName();
	Context context;
	private TYRenderingScheme renderingScheme;

	public IPLAShadeLayer(Context context, TYRenderingScheme renderingScheme,
			SpatialReference spatialReference, Envelope envelope) {
		super(spatialReference, envelope);
		this.context = context;
		this.renderingScheme = renderingScheme;

		setRenderer(createShadeRenderer());
	}

	public void setRenderScheme(TYRenderingScheme rs) {
		this.renderingScheme = rs;
		setRenderer(createShadeRenderer());
	}

	private Renderer createShadeRenderer() {
		UniqueValueRenderer shadeRenderer = new UniqueValueRenderer();
		List<UniqueValue> uvInfo = new ArrayList<UniqueValue>();

		for (Integer colorID : renderingScheme.getFillSymbolDictionary()
				.keySet()) {
			SimpleFillSymbol sfs = renderingScheme.getFillSymbolDictionary()
					.get(colorID);
			UniqueValue uv = new UniqueValue();
			uv.setSymbol(sfs);
			uv.setValue(new Integer[] { colorID });
			uvInfo.add(uv);
		}
		shadeRenderer.setDefaultSymbol(renderingScheme.getDefaultFillSymbol());

		shadeRenderer.setField1("COLOR");
		shadeRenderer.setUniqueValueInfos(uvInfo);
		return shadeRenderer;
	}

	public void loadContents(Graphic[] graphics) {
		removeAll();
		addGraphics(graphics);
	}

}
