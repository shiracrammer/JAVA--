package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

/**
 * Class Cylinder, representing a finite cylinder in 3D.
 * Extends Class Tube, and so :
 * has 2 additional instance members (_axisRay and _radius)
 * and implements Geometry (and getsNormal(Point3D) method).
 * @author Deborah Lellouche
 */
public class Cylinder extends Tube {
    /**
     * Double value representing the height of the cylinder.
     */
    final double _height;

    /**
     * Cylinder constructor, receiving 3 parameters.
     * @param axisRay a Ray object representing the axis of the cylinder.
     * @param radius a double value representing the radius of the cylinder.
     * @param height a double value representing the height of the cylinder.
     * @throws IllegalArgumentException from super if radius <= 0.
     * @throws IllegalArgumentException if height <= 0.
     */
    public Cylinder(Ray axisRay, double radius, double height) {
        super(axisRay, radius);
        if (height <= 0) {
            throw new IllegalArgumentException("height of a Cylinder object must be > 0");
        }
        _height = height;
    }

    /**
     * Getter of _height.
     * @return the value of _height.
     */
    public double getHeight() {
        return _height;
    }

    @Override
    public String toString() {
        return "Cylinder{" +
                "_axisRay=" + _axisRay +
                ", _radius=" + _radius +
                "_height=" + _height +
                '}';
    }

}
