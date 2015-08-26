package com.ty.mapsdk;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.Renderer;
import com.esri.core.renderer.UniqueValue;
import com.esri.core.renderer.UniqueValueRenderer;
import com.esri.core.symbol.SimpleFillSymbol;

class IPAssetLayer extends GraphicsLayer {
	static final String TAG = IPAssetLayer.class.getSimpleName();

	Context context;
	private TYRenderingScheme renderingScheme;

	public IPAssetLayer(Context context, TYRenderingScheme renderingScheme,
			SpatialReference spatialReference, Envelope envelope) {
		super(spatialReference, envelope);
		this.context = context;
		this.renderingScheme = renderingScheme;

		setRenderer(createAssetRenderer());
	}

	private Renderer createAssetRenderer() {
		UniqueValueRenderer assetRenderer = new UniqueValueRenderer();
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
		assetRenderer.setDefaultSymbol(renderingScheme.getDefaultFillSymbol());

		assetRenderer.setField1("COLOR");
		assetRenderer.setUniqueValueInfos(uvInfo);
		return assetRenderer;
	}

	public void loadContents(FeatureSet set) {
		removeAll();

		if (set != null) {
			Graphic[] graphics = set.getGraphics();
			addGraphics(graphics);
		}
	}

}
