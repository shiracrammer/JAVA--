package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point3D;
import primitives.Vector;

/**
 * Abstract class Geometry, implemented by classes representing geometric objects.
 * Implements Intersectable.
 * @author Deborah Lellouche
 */
public abstract class Geometry implements Intersectable {
    /**
     * The color of the light that emits the Geometry.
     */
    protected Color _emission = Color.BLACK;

    /**
     * The material composing the Geometry.
     */
    protected Material _material = new Material();

    /**
     * To return the normal to a specific point on the surface of a geometric object.
     * @param point the Point3D object
     * @return the normal to the geometric object at point.
     */
    public abstract Vector getNormal(Point3D point);

    /**
     * Getter of _emission.
     * @return the value of _emission.
     */
    public Color getEmission() {
        return _emission;
    }

    /**
     * Setter of _emission.
     * Chaining method.
     * @param emission the new value of the color of the light emitted by the Geometry.
     * @return this.
     */
    public Geometry setEmission(Color emission) {
        _emission = emission;
        return this;
    }

    /**
     * Getter of _material.
     * @return _material.
     */
    public Material getMaterial() {
        return _material;
    }

    /**
     * Setter of _material.
     * Chaining method.
     * @param material the new value of the material composing the Geometry.
     * @return this.
     */
    public Geometry setMaterial(Material material) {
        _material = material;
        return this;
    }
}
