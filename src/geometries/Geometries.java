package geometries;

import primitives.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Class Geometries, composite class representing a group of 3D-objects.
 * Implements Intersectable.
 * @author Deborah Lellouche
 */
public class Geometries implements Intersectable {
    /**
     * List of Intersectable objects representing the 3D-objects of the group.
     */
    private List<Intersectable> _intersectables;


    /**
     * Geometries constructor, receiving no parameter.
     */
    public Geometries() {
        _intersectables = new LinkedList<>();
    }

    /**
     * Geometries constructor, receiving one or more parameters.
     * @param intersectables one or more Intersectable objects representing 3D-objects of the group.
     */
    public Geometries(Intersectable... intersectables) {
        _intersectables = new LinkedList<>();
        add(intersectables);
    }

    /**
     * To return the list of the intersection GeoPoints between a ray and _intersectables of this.
     * Adding the intersection GeoPoints for each Intersectable, one Intersectable after another.
     * @param ray the Ray object to find the intersections between it and _intersectables of this.
     * @param maxDistance
     * @return  List<GeoPoint> of the intersection GeoPoints between ray and _intersectables of this.
     */
    @Override
    public List<GeoPoint> findGeoIntersections(Ray ray) {
        List<GeoPoint> result = null;

        // to find the intersection GeoPoints between ray and each element of _intersectable, one one
        for (Intersectable element : _intersectables) {
            List<GeoPoint> elementList = element.findGeoIntersections(ray);
            if (elementList != null) {
                if (result == null) {
                    result = new LinkedList<>();
                }
                result.addAll(elementList); // both are LinkedList
            }
        }

        return result;
    }

    /**
     * To add 3D-objects to the group, that is to add Intersectable object(s) to this._intersectables.
     * @param intersectables the Intersectable objects to add.
     */
    public void add(Intersectable... intersectables) {
        for (Intersectable current : intersectables) {
            _intersectables.add(current);
        }
    }
}
