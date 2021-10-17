package elements;

import primitives.Color;

/**
 * Class AmbientLight, representing an ambient light source,
 * read a fixed-intensity and fixed-color light source that affects all objects in the scene equally.
 * Ia * Ka, at all points.
 * Extends Light.
 * @author Deborah Lellouche
 */
public class AmbientLight extends Light {

    /**
     * AmbientLight default constructor.
     * To set the intensity to Color.BLACK.
     */
    public AmbientLight() { super(Color.BLACK); }

    /**
     * AmbientLight constructor, receiving 2 parameters.
     * Using Light constructor to initialize its _intensity, primitives.Color object to represent
     * the intensity Ip of the ambient light source.
     * Ip = Ka * Ia (Ia, Ka are parameters of the constructor)
     * @param Ia the original intensity of the ambient light source.
     * @param Ka the absorption coefficient for the ambient light source.
     */
    public AmbientLight(Color Ia, double Ka) {
        super(Ia.scale(Ka));
    }

}
