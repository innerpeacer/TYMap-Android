package com.ty.mapsdk;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;

import android.content.Context;
import android.graphics.Color;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;

public class IPPointLayer extends GraphicsLayer {
	Context context;
	MarkerSymbol markerSymbol;

	public IPPointLayer(Context context, SpatialReference sr) {
		super();

		this.context = context;

		SimpleMarkerSymbol sms = new SimpleMarkerSymbol(Color.RED, 10,
				STYLE.CIRCLE);
		sms.setOutline(new SimpleLineSymbol(Color.YELLOW, 2));
		markerSymbol = sms;

		setRenderer(new SimpleRenderer(markerSymbol));
	}

	public void loadContents(TYMapInfo info) {
		removeAll();

		String buildingDir = IPMapFileManager.getBuildingDir(info.getCityID(),
				info.getBuildingID());
		String fileName = String.format("%s_Point.json", info.getMapID());
		File file = new File(buildingDir, fileName);

		if (file.exists()) {
			JsonFactory factory = new JsonFactory();
			try {
				JsonParser parser = factory.createJsonParser(file);
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

	public void setMarkerSymbol(MarkerSymbol ms) {
		markerSymbol = ms;
		setRenderer(new SimpleRenderer(markerSymbol));
	}

}
