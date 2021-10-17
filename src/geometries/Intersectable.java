package geometries;

import primitives.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Interface Intersectable, to represent intersection of rays and objects in 3D.
 * @author Deborah Lellouche
 */
public interface Intersectable {

    /**
     * Class GeoPoint.
     * Helper class. Inner class of Interface Intersectable.
     * Passive Data Structure (Plain Old Data) :
     * only public data members and no methods (or just simple getters)
     */
    public static class GeoPoint {
        public Geometry _geometry;
        public Point3D _point;

        /**
         * GeoPoint constructor, receiving 2 parameters.
         * @param geometry the Geometry.
         * @param point the Point3D.
         */
        public GeoPoint(Geometry geometry, Point3D point) {
            if (geometry == null || point == null) {
                throw new IllegalArgumentException("geometry and point must be not null");
            }
            _geometry = geometry;
            _point = point;
        }

        /**
         * To check if _geometry of this and _geometry of o are the SAME object
         * AND if _point of this and _point of o are equals.
         * @param o the GeoPoint to checks its 'equality' with this.
         * @return if this and o are 'equals'.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GeoPoint gp = (GeoPoint) o;
            return (_geometry == gp._geometry) && _point.equals(gp._point);
        }

    }

    /**
     * To return the list of the intersection points between a ray and this.
     * After the add of findGeoIntersections, default "implementation" in the interface only
     * (no implementation in the classes which implement it).
     * @param ray the Ray object to find the intersections between it and this.
     * @return List<Point3D> of the intersection points between ray and this.
     */
    default List<Point3D> findIntersections(Ray ray) {
        List<GeoPoint> gpList = findGeoIntersections(ray);
        return gpList == null ? null :
                gpList.stream()
                        .map(gp->gp._point)
                        .collect(Collectors.toList());
    }

    /**
     * As for findIntersections, but using Intersectable.GeoPoint instead of Point3D.
     *
     * To return the list of the intersection GeoPoints between a ray and this.
     * @param ray the Ray object to find the intersections between it and this.
     * @return List<GeoPoint> of the intersection GeoPoints between ray and this.
     */
    List<GeoPoint> findGeoIntersections(Ray ray);


}
