

package scene;

import elements.LightSource;
import primitives.Color;
import elements.AmbientLight;
import geometries.Geometries;

import java.util.LinkedList;
import java.util.List;

/**
 * Class Scene, representing a scene in 3D.
 *
 * Passive Data Structure (Plain Old Data) :
 * only public data members and no methods (or just simple getters)
 *
 * Element of Builder Design Pattern
 * https://www.geeksforgeeks.org/builder-design-pattern/
 *
 * @author Deborah Lellouche
 */
public class Scene {
    /**
     * String object representing the name of the scene.
     */
    public final String _name;

    /**
     * primitives.Color object representing the background's color of the scene.
     */
    public Color _background;

    /**
     * AmbientLight object representing the ambient light of the scene.
     */
    public AmbientLight _ambientLight;

    /**
     * The LightSource of the scene.
     */
    public List<LightSource> _lights;

    /**
     * Geometries object representing the group of 3D objects that are in the scene.
     */
    public Geometries _geometries;

    /**
     * Scene constructor, receiving 1 parameter.
     * @param name a String representing the name of the scene.
     */
    public Scene(String name) {
        _name = name;
        _background = Color.BLACK;
        _ambientLight = new AmbientLight();
        _lights = new LinkedList<>();
        _geometries = new Geometries();
    }


    /**
     * Setter for _background.
     * Chaining method (return this).
     * @param background the new value for _background.
     * @return this.
     */
    public Scene setBackground(Color background) {
        _background = background;
        return this;
    }

    /**
     * Setter for _ambientLight.
     * Chaining method.
     * @param ambientLight the new value for _ambientLight.
     * @return this.
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        _ambientLight = ambientLight;
        return this;
    }

    /**
     * Setter for _lights.
     * Chaining method.
     * @param lights the list of the LightSource of the scene.
     * @return this.
     */
    public Scene setLights(List<LightSource> lights) {
        _lights = new LinkedList<>();
        if (lights != null) {
            for (LightSource light : lights) {
                _lights.add(light);
            }
        }
        return this;
    }

    /**
     * Setter for _geometries.
     * Chaining method.
     * @param geometries the new value for _geometries.
     * @return this.
     */
    public Scene setGeometries(Geometries geometries) {
        _geometries = geometries;
        return this;
    }
}
