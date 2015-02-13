package cn.nephogram.mapsdk.route;

import com.esri.android.map.GraphicsLayer;

public class NPRouteLayer extends GraphicsLayer {
	static final String TAG = NPRouteLayer.class.getSimpleName();

	// private CompositeSymbol routeSymbol;

	public NPRouteLayer() {
		super();
		// routeSymbol = createRouteSymbol();
		// setRenderer(new SimpleRenderer(routeSymbol));

		// UniqueValueRenderer render = new UniqueValueRenderer();
		// render.setDefaultSymbol(routeSymbol);
		// setRenderer(render);
	}

	// private CompositeSymbol createRouteSymbol() {
	// CompositeSymbol cs = new CompositeSymbol();
	//
	// SimpleLineSymbol sls1 = new SimpleLineSymbol(Color.YELLOW, 8,
	// STYLE.SOLID);
	// cs.add(sls1);
	//
	// SimpleLineSymbol sls2 = new SimpleLineSymbol(Color.BLUE, 4, STYLE.SOLID);
	// cs.add(sls2);
	// return cs;
	// }
}
