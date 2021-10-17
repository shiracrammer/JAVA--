package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.isZero;

/**
 * Class Tube, representing an infinite cylinder in 3D.
 * Extends Geometry.
 * @author Deborah Lellouche
 */
public class Tube extends Geometry {
    /**
     * Ray object representing the ray at the axis of the tube.
     */
    final Ray _axisRay; // protected

    /**
     * Double value representing the radius of the tube.
     */
    final double _radius;

    /**
     * Tube constructor, receiving 2 parameters.
     * @param axisRay a Ray object representing the ray at the axis of the tube.
     * @param radius a double value representing the radius of the tube.
     * @throws IllegalArgumentException if radius <= 0.
     */
    public Tube(Ray axisRay, double radius) {
        if (radius <= 0) {
            throw new IllegalArgumentException("radius of a Tube object must be > 0");
        }
        _axisRay = axisRay;
        _radius = radius;
    }

    /**
     * Getter of _axisRay.
     * @return _axisRay itself.
     */
    public Ray getAxisRay() {
        return _axisRay;
    }

    /**
     * Getter of _radius.
     * @return the value of _radius.
     */
    public double getRadius() {
        return _radius;
    }

    @Override
    public String toString() {
        return "Tube{" +
                "_axisRay=" + _axisRay +
                ", _radius=" + _radius +
                '}';
    }

    @Override
    public Vector getNormal(Point3D point) {
        Point3D p0 = _axisRay.getP0();
        Vector v = _axisRay.getDir();
        // projection of P-O on the ray
        double t = point.subtract(p0).dotProduct(v);
        if (!isZero(t)) {   // if it is close to 0, zero vector exception
            p0 = p0.add(v.scale(t));
        }

        return point.subtract(p0).normalize();
    }

    // not implemented
    @Override
    public List<GeoPoint> findGeoIntersections(Ray ray) {
        return null;
    }
}
