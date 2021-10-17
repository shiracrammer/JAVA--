package renderer;

import primitives.Color;
import primitives.Ray;
import scene.Scene;

/**
 * Abstract class RayTracerBase, representing the base for a ray tracer.
 * That is to say, a calculator to simulate intersections between rays and the 3D objects in the scene
 * and returning the resulting colors, in order to produce an image looking like a photography in color.
 * @author Deborah Lellouche
 * */
public abstract class RayTracerBase {
    /**
     * Scene object representing the scene of the RayTracerBase object (by inheritance).
     */
    protected Scene _scene;

    /**
     * RayTracerBase constructor, receiving 1 parameter.
     * @param scene the scene of the RayTracerBase.
     * @throws IllegalArgumentException if scene is null.
     */
    public RayTracerBase(Scene scene) {
        if (scene == null) {
            throw new IllegalArgumentException("scene must be not null");
        }
        _scene = scene;
    }

    /**
     * To return the color of the first point that ray has an intersection with.
     * @param ray the Ray to find its closest point of intersection.
     * @return the color of the point that ray has an intersection with and that is the closest to the origin
     * of the ray (or return the background color if there is no point of intersection).
     */
    public abstract Color traceRay(Ray ray);


}
