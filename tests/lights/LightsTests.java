package lights;

import org.junit.jupiter.api.Test;

import elements.*;
import geometries.*;
import primitives.*;
import renderer.*;
import scene.Scene;

/**
 * Test rendering a basic image
 *
 * @author Dan
 * with changes
 */
public class LightsTests {
    private Scene scene1 = new Scene("Test scene");
    private Scene scene2 = new Scene("Test scene") //
            .setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));
    private Camera camera1 = new Camera(new Point3D(0, 0, 1000),
            new Vector(0, 0, -1), new Vector(0, 1, 0)) //
            .setViewPlaneSize(150, 150) //
            .setDistance(1000);
    private Camera camera2 = new Camera(new Point3D(0, 0, 1000),
            new Vector(0, 0, -1), new Vector(0, 1, 0)) //
            .setViewPlaneSize(200, 200) //
            .setDistance(1000);

    private static Geometry triangle1 = new Triangle( //
            new Point3D(-150, -150, -150),
            new Point3D(150, -150, -150),
            new Point3D(75, 75, -150));
    private static Geometry triangle2 = new Triangle( //
            new Point3D(-150, -150, -150),
            new Point3D(-70, 70, -50),
            new Point3D(75, 75, -150));
    private static Geometry sphere = new Sphere(50, new Point3D(0, 0, -50)) //
            .setEmission(new Color(java.awt.Color.BLUE)) //
            .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100));

    /**
     * Produce a picture of a sphere lighted by a directional light
     */
    @Test
    public void sphereDirectional() {
        scene1._geometries.add(sphere);
        scene1._lights.add(new DirectionalLight(new Color(500, 300, 0), new Vector(1, 1, -1)));

        ImageWriter imageWriter = new ImageWriter("sphereDirectional", 500, 500);
        Render render = new Render()//
                .setImageWriter(imageWriter) //
                .setCamera(camera1) //
                .setRayTracer(new BasicRayTracer(scene1));
        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a sphere lighted by a point light
     */
    @Test
    public void spherePoint() {
        scene1._geometries.add(sphere);
        scene1._lights.add((new PointLight(new Color(500, 300, 0), new Point3D(-50, -50, 50))
                .setKc(1).setKl(0.00001).setKq(0.000001)));

        ImageWriter imageWriter = new ImageWriter("spherePoint", 500, 500);
        Render render = new Render()//
                .setImageWriter(imageWriter) //
                .setCamera(camera1) //
                .setRayTracer(new BasicRayTracer(scene1));
        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a sphere lighted by a spot light
     */
    @Test
    public void sphereSpot() {
        scene1._geometries.add(sphere);
        scene1._lights.add((new SpotLight(new Color(500, 300, 0), new Point3D(-50, -50, 50),
                new Vector(1, 1, -2))
                .setKc(1).setKl(0.00001).setKq(0.00000001)));

        ImageWriter imageWriter = new ImageWriter("sphereSpot", 500, 500);
        Render render = new Render()//
                .setImageWriter(imageWriter) //
                .setCamera(camera1) //
                .setRayTracer(new BasicRayTracer(scene1));
        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a two triangles lighted by a directional light
     */
    @Test
    public void trianglesDirectional() {
        scene2._geometries.add(triangle1.setMaterial(new Material().setKd(0.8).setKs(0.2).setShininess(300)), //
                triangle2.setMaterial(new Material().setKd(0.8).setKs(0.2).setShininess(300)));
        scene2._lights.add(new DirectionalLight(new Color(300, 150, 150), new Vector(0, 0, -1)));

        ImageWriter imageWriter = new ImageWriter("trianglesDirectional", 500, 500);
        Render render = new Render()//
                .setImageWriter(imageWriter) //
                .setCamera(camera2) //
                .setRayTracer(new BasicRayTracer(scene2));
        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a two triangles lighted by a point light
     */
    @Test
    public void trianglesPoint() {
        scene2._geometries.add(triangle1.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(300)), //
                triangle2.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(300)));
        scene2._lights.add((new PointLight(new Color(500, 250, 250), new Point3D(10, -10, -130))
                .setKc(1).setKl(0.0005).setKq(0.0005)));

        ImageWriter imageWriter = new ImageWriter("trianglesPoint", 500, 500);
        Render render = new Render()//
                .setImageWriter(imageWriter) //
                .setCamera(camera2) //
                .setRayTracer(new BasicRayTracer(scene2));
        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a two triangles lighted by a spot light
     */
    @Test
    public void trianglesSpot() {
        scene2._geometries.add(triangle1.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(300)),
                triangle2.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(300)));
        scene2._lights.add((new SpotLight(new Color(500, 250, 250), new Point3D(10, -10, -130),
                new Vector(-2, -2, -1))
                .setKc(1).setKl(0.0001).setKq(0.000005)));

        ImageWriter imageWriter = new ImageWriter("trianglesSpot", 500, 500);
        Render render = new Render()//
                .setImageWriter(imageWriter) //
                .setCamera(camera2) //
                .setRayTracer(new BasicRayTracer(scene2));
        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a sphere lighted by a narrow spot light
     */
    @Test
    public void sphereSpotSharp() {
        scene1._geometries.add(sphere);
        scene1._lights.add((new SpotLight(new Color(500, 300, 0), new Point3D(-50, -50, 50),
                new Vector(1, 1, -2))
                .setKc(1).setKl(0.000005).setKq(0.00000025))); //5

        ImageWriter imageWriter = new ImageWriter("sphereSpotSharp", 500, 500);
        Render render = new Render()//
                .setImageWriter(imageWriter) //
                .setCamera(camera1) //
                .setRayTracer(new BasicRayTracer(scene1));
        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a two triangles lighted by a narrow spot light
     */
    @Test
    public void trianglesSpotSharp() {
        scene2._geometries.add(triangle1.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(300)),
                triangle2.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(300)));
        scene2._lights.add((new SpotLight(new Color(800, 400, 400), new Point3D(10, -10, -130),
                new Vector(-2, -2, -1))
                .setKc(1).setKl(0.00005).setKq(0.0000025))); //5

        ImageWriter imageWriter = new ImageWriter("trianglesSpotSharp", 500, 500);
        Render render = new Render()//
                .setImageWriter(imageWriter) //
                .setCamera(camera2) //
                .setRayTracer(new BasicRayTracer(scene2));
        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a sphere lighted by a directional light, a point light and a spot light.
     */
    @Test
    public void sphereAllLights() {
        scene1._geometries.add(sphere);
        scene1._lights.add(new DirectionalLight(new Color(50, 200, 50), new Vector(1, 1, -1)));
        scene1._lights.add((new PointLight(new Color(0, 250, 250),
                new Point3D(20, -20, 30))
                .setKc(1).setKl(0.0001).setKq(0.0005)));
        scene1._lights.add((new SpotLight(new Color(250, 150, 150),
                new Point3D(0, 10, 30),
                new Vector(1, 1, -2))
                .setKc(1).setKl(0.001).setKq(0.00005)));

        ImageWriter imageWriter = new ImageWriter("sphereAllLights", 500, 500);
        Render render = new Render()//
                .setImageWriter(imageWriter) //
                .setCamera(camera1) //
                .setRayTracer(new BasicRayTracer(scene1));
        render.renderImage();
        render.writeToImage();

    }

    /**
     * Produce a picture of a two triangles lighted by a directional light, a point light and a spot light.
     */
    @Test
    public void trianglesAllLights() {
        scene2._geometries.add(triangle1.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(300)),
                triangle2.setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(300)));
        scene2._lights.add(new DirectionalLight(new Color(150, 250, 150), new Vector(0, 0, -1)));
        scene2._lights.add((new PointLight(new Color(250, 0, 250), new Point3D(-50, -50, -130))
                .setKc(0.8).setKl(0.00005).setKq(0.00005)));
        scene2._lights.add((new SpotLight(new Color(500, 250, 0),
                new Point3D(50, 50, -130),
                new Vector(-2, -2, -1))
                .setKc(0.9).setKl(0.001).setKq(0.00005)));

        ImageWriter imageWriter = new ImageWriter("trianglesAllLights", 500, 500);
        Render render = new Render()//
                .setImageWriter(imageWriter) //
                .setCamera(camera2) //
                .setRayTracer(new BasicRayTracer(scene2));
        render.renderImage();
        render.writeToImage();

    }


}
