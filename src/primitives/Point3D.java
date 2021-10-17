package primitives;

import java.util.Objects;

/**
 * Class Point3D to represent basic 3D-points for RayTracing project
 * @author Deborah Lellouche
 */
public class Point3D {
    /**
     * Coordinate object representing the coordinate in the X axis.
     */
    final Coordinate _x;

    /**
     * Coordinate object representing the coordinate in the Y axis.
     */
    final Coordinate _y;

    /**
     * Coordinate object representing the coordinate in the Z axis.
     */
    final Coordinate _z;

    /**
     * Constant representing a point at the origin.
     */
    final public static Point3D ZERO = new Point3D(0d, 0d, 0d);

    /**
     * Point3D constructor receiving 3 coordinate values as Coordinate objects
     * @param x Coordinate object for X axis
     * @param y Coordinate object for Y axis
     * @param z Coordinate object for Z axis
     */
    public Point3D(Coordinate x, Coordinate y, Coordinate z) {
        this(x.coord, y.coord, z.coord);
    }

    /**
     * Point3D constructor receiving 3 coordinate values as double values
     * @param x double value for X axis
     * @param y double value for Y axis
     * @param z double value for Z axis
     */
    public Point3D(double x, double y, double z) {
        _x = new Coordinate(x);
        _y = new Coordinate(y);
        _z = new Coordinate(z);
    }

    /**
     * Pseudo-getter.
     * @return the value of the attribute coord of _x.
     */
    public double getX() {
        return _x.coord;
    }

    /**
     * Pseudo-getter.
     * @return the value of the attribute coord of _y.
     */
    public double getY() {
        return _y.coord;
    }

    /**
     * Pseudo-getter.
     * @return the value of the attribute coord of _z.
     */
    public double getZ() {
        return _z.coord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point3D point3D = (Point3D) o;
        return _x.equals(point3D._x) && _y.equals(point3D._y) && _z.equals(point3D._z);
    }

    @Override
    public String toString() {
        return "(" + _x + ",  " + _y + ",  " + _z + ")";
    }

    /**
     * To partly calculate the distance between two Point3D objects.
     * Not meant to be used as itself, but used by the method distance(Point3D).
     * @param other the other Point3D object to calculate the distance with
     * @return the square of the distance with other : (x2 - x1)^2 + (y2 - y1)^2 + (z2 - z1)^2
     */
    private double distanceSquared(Point3D other) {
        final double x1 = _x.coord;                 // coord is "package-friendly"
        final double y1 = _y.coord;
        final double z1 = _z.coord;
        final double x2 = other._x.coord;
        final double y2 = other._y.coord;
        final double z2 = other._z.coord;

        return ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1) * (z2 - z1));
    }

    /**
     * To calculate the distance between two Point3D objects.
     * @param other the other Point3D object to calculate the distance with
     * @return the distance with other : sqrt[(x2 - x1)^2 + (y2 - y1)^2 + (z2 - z1)^2]
     */
    public double distance(Point3D other) {
        return Math.sqrt(distanceSquared(other));
    }

    /**
     * To add a Vector object to a Point3D object.
     * @param vector the Vector object to add.
     * @return a Point3D
     */
    public Point3D add(Vector vector) {
        return new Point3D(
                _x.coord + vector.getHead()._x.coord,
                _y.coord + vector.getHead()._y.coord,
                _z.coord + vector.getHead()._z.coord);
    }

    /**
     * To effectuate a vectorial substraction between two Point3D objects.
     * @param p2 the other Point3D object.
     * @return a Vector object representing a vector with direction from p2 to this.
     */
    public Vector subtract(Point3D p2) {
        if (p2.equals(this)) {
            throw new IllegalArgumentException("cannot make Vector with Point3D(0,0,0) as _head");
        }
        return new Vector(new Point3D(
                _x.coord - p2._x.coord,
                _y.coord - p2._y.coord,
                _z.coord - p2._z.coord));
    }


}
