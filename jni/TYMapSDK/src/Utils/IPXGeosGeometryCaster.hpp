//
//  IPXGeosGeometryCaster.hpp
//  MapProject
//
//  Created by innerpeacer on 15/11/20.
//  Copyright © 2015年 innerpeacer. All rights reserved.
//

#ifndef IPXGeosGeometryCaster_hpp
#define IPXGeosGeometryCaster_hpp

#include <stdio.h>
#include <vector>
#include <geos.h>

namespace Innerpeacer {
    namespace MapSDK {
        namespace GeosGeometryCaster {
            geos::geom::Point *CastedPoint(geos::geom::Geometry *g);
            geos::geom::MultiPoint *CastedMultiPoint(geos::geom::Geometry *g);
            geos::geom::LineString *CastedLineString(geos::geom::Geometry *g);
            geos::geom::MultiLineString *CastedMultiLineString(geos::geom::Geometry *g);
            geos::geom::Polygon *CastedPolygon(geos::geom::Geometry *g);
            geos::geom::MultiPolygon *CastedMultiPolygon(geos::geom::Geometry *g);
        }
    }
}


#endif /* IPXGeosGeometryCaster_hpp */
