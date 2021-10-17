package renderer;

import org.junit.jupiter.api.Test;

import elements.*;
import geometries.*;
import primitives.*;
import scene.Scene;
import xml.XMLParser;


/**
 * Test rendering a basic image
 *
 * @author Dan
 * with changes
 */
public class RenderTests {
    private Camera camera = new Camera(Point3D.ZERO, new Vector(0, 0, -1), new Vector(0, -1, 0)) //
            .setDistance(100) //
            .setViewPlaneSize(500, 500);

    /**
     * Produce a scene with basic 3D model and render it into a jpeg image with a
     * grid
     */
    @Test
    public void basicRenderTwoColorTest() {

        Scene scene = new Scene("Test scene")//
                .setAmbientLight(new AmbientLight(new Color(255, 191, 191), 1)) //
                .setBackground(new Color(75, 127, 90));

        scene._geometries.add(new Sphere(50, new Point3D(0, 0, -100)),
                new Triangle(new Point3D(-100, 0, -100), new Point3D(0, 100, -100), new Point3D(-100, 100, -100)), // up left
                new Triangle(new Point3D(100, 0, -100), new Point3D(0, 100, -100), new Point3D(100, 100, -100)), // up right
                new Triangle(new Point3D(-100, 0, -100), new Point3D(0, -100, -100), new Point3D(-100, -100, -100)), // down left
                new Triangle(new Point3D(100, 0, -100), new Point3D(0, -100, -100), new Point3D(100, -100, -100))); // down right

        ImageWriter imageWriter = new ImageWriter("base render test", 1000, 1000);
        Render render = new Render() //
                .setImageWriter(imageWriter) //
                .setCamera(camera) //
                .setRayTracer(new BasicRayTracer(scene));

        render.renderImage();
        render.printGrid(100, new Color(java.awt.Color.YELLOW));
        render.writeToImage();
    }


    /**
     * Test for XML based scene - for bonus
     */
    @Test
    public void basicRenderXml() {
        Scene scene = new Scene("XML Test scene");

        // xmlpath
        String xmlpath = "C:\\Users\\Deborah\\Desktop\\Semestre5781\\java_project\\basicRenderTestTwoColors.xml";

        // making an XMLParser object, whose constructor sets the members of scene
        XMLParser xmlparser = new XMLParser(scene, xmlpath);

        ImageWriter imageWriter = new ImageWriter("xml render test", 1000, 1000);
        Render render = new Render() //
                .setImageWriter(imageWriter) //
                .setCamera(camera) //
                .setRayTracer(new BasicRayTracer(scene));

        render.renderImage();
        render.printGrid(100, new Color(java.awt.Color.YELLOW));
        render.writeToImage();
    }


    @Test
    public void basicRenderMultiColorTest() {
        Scene scene = new Scene("Test scene")//
                .setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.2)); //

        scene._geometries.add(
                new Sphere(50, new Point3D(0, 0, -100))
                        .setEmission(new Color(java.awt.Color.CYAN)), //
                // up left
                new Triangle(new Point3D(-100, 0, -100),
                            new Point3D(0, 100, -100),
                            new Point3D(-100, 100, -100))
                            .setEmission(new Color(java.awt.Color.GREEN)),
                // up right
                new Triangle(new Point3D(100, 0, -100),
                            new Point3D(0, 100, -100),
                            new Point3D(100, 100, -100)),
                // down left
                new Triangle(new Point3D(-100, 0, -100),
                        new Point3D(0, -100, -100),
                        new Point3D(-100, -100, -100))
                        .setEmission(new Color(java.awt.Color.RED)),
                // down right
                new Triangle(new Point3D(100, 0, -100),
                        new Point3D(0, -100, -100),
                        new Point3D(100, -100, -100))
                        .setEmission(new Color(java.awt.Color.BLUE)));

        ImageWriter imageWriter = new ImageWriter("color render test", 1000, 1000);
        Render render = new Render() //
                .setImageWriter(imageWriter) //
                .setCamera(camera) //
                .setRayTracer(new BasicRayTracer(scene));

        render.renderImage();
        render.printGrid(100, new Color(java.awt.Color.WHITE));
        render.writeToImage();
    }



}

