package elements;

import primitives.*;

/**
 * Interface LightSource, implemented by classes representing light sources.
 * @author Deborah Lellouche
 */
public interface LightSource {

    /**
     * To get the intensity, the color at point p.
     * @param p the point to get its color.
     * @return the color at the point p.
     */
    public Color getIntensity(Point3D p);

    /**
     *
     * @param p
     * @return
     */
    public Vector getL(Point3D p);

    /**
     * To get the distance between the light source and the point.
     * @param point the point to check the distance with.
     * @return the distance between the light source and the point.
     */
    public double getDistance(Point3D point);

    public double getSquareEdgeSize();


}
