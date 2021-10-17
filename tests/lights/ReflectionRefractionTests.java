
package lights;

import org.junit.jupiter.api.Test;

import elements.*;
import geometries.Sphere;
import geometries.Triangle;
import primitives.*;
import renderer.*;
import scene.Scene;

import java.util.Random;

/**
 * Tests for reflection and transparency functionality, test for partial shadows
 * (with transparency)
 *
 * @author dzilb
 * with changes
 */
public class ReflectionRefractionTests {
    private Scene scene = new Scene("Test scene");

    /**
     * Produce a picture of a sphere lighted by a spot light
     */
    @Test
    public void twoSpheres() {
        Camera camera = new Camera(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setViewPlaneSize(150, 150).setDistance(1000);

        scene._geometries.add( //
                new Sphere(50, new Point3D(0, 0, -50)) //
                        .setEmission(new Color(java.awt.Color.BLUE)) //
                        .setMaterial(new Material().setKd(0.4).setKs(0.3).setShininess(100).setKt(0.3)),
                new Sphere(25, new Point3D(0, 0, -50)) //
                        .setEmission(new Color(java.awt.Color.RED)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)));
        scene._lights.add( //
                new SpotLight(new Color(1000, 600, 0), new Point3D(-100, -100, 500),
                        new Vector(-1, -1, -2)) //
                        .setKl(0.0004).setKq(0.0000006));

        Render render = new Render() //
                .setImageWriter(new ImageWriter("refractionTwoSpheres", 500, 500)) //
                .setCamera(camera) //
                .setRayTracer(new BasicRayTracer(scene));
        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a sphere lighted by a spot light
     */
    @Test
    public void twoSpheresOnMirrors() {
        Camera camera = new Camera(new Point3D(0, 0, 10000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setViewPlaneSize(2500, 2500).setDistance(10000); //

        scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));

        scene._geometries.add( //
                new Sphere(400, new Point3D(-950, -900, -1000)) //
                        .setEmission(new Color(0, 0, 100)) //
                        .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20).setKt(0.5)),
                new Sphere(200, new Point3D(-950, -900, -1000)) //
                        .setEmission(new Color(100, 20, 20)) //
                        .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)),
                new Triangle(new Point3D(1500, -1500, -1500), new Point3D(-1500, 1500, -1500),
                        new Point3D(670, 670, 3000)) //
                        .setEmission(new Color(20, 20, 20)) //
                        .setMaterial(new Material().setKr(1)),
                new Triangle(new Point3D(1500, -1500, -1500), new Point3D(-1500, 1500, -1500),
                        new Point3D(-1500, -1500, -2000)) //
                        .setEmission(new Color(20, 20, 20)) //
                        .setMaterial(new Material().setKr(0.5)));

        scene._lights.add(new SpotLight(new Color(1020, 400, 400),
                new Point3D(-750, -750, -150), new Vector(-1, -1, -4)) //
                .setKl(0.00001).setKq(0.000005));

        ImageWriter imageWriter = new ImageWriter("reflectionTwoSpheresMirrored", 500, 500);
        Render render = new Render() //
                .setImageWriter(imageWriter) //
                .setCamera(camera) //
                .setRayTracer(new BasicRayTracer(scene));

        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a two triangles lighted by a spot light with a partially
     * transparent Sphere producing partial shadow
     */
    @Test
    public void trianglesTransparentSphere() {
        Camera camera = new Camera(new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setViewPlaneSize(200, 200).setDistance(1000);

        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        scene._geometries.add( //
                new Triangle(new Point3D(-150, -150, -115), new Point3D(150, -150, -135), new Point3D(75, 75, -150)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60)), //
                new Triangle(new Point3D(-150, -150, -115), new Point3D(-70, 70, -140), new Point3D(75, 75, -150)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60)), //
                new Sphere(30, new Point3D(60, 50, -50)) //
                        .setEmission(new Color(java.awt.Color.BLUE)) //
                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.6)));

        scene._lights.add(new SpotLight(new Color(700, 400, 400), new Point3D(60, 50, 0), new Vector(0, 0, -1)) //
                .setKl(4E-5).setKq(2E-7));

        ImageWriter imageWriter = new ImageWriter("refractionShadow", 600, 600);
        Render render = new Render() //
                .setImageWriter(imageWriter) //
                .setCamera(camera) //
                .setRayTracer(new BasicRayTracer(scene));

        render.renderImage();
        render.writeToImage();
    }

    @Test
    public void twoSpheresTwoTriangles() {
        Camera camera = new Camera
                (new Point3D(0, 0, 10000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setViewPlaneSize(2500, 2500).setDistance(10000); //

        scene._background = new Color(50, 50, 100);
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        scene._geometries.add( //
                new Sphere(400, new Point3D(-200, -500, -1000)) //
                        .setEmission(new Color(50, 100, 150)) //
                        .setMaterial(new Material().setKd(0.7).setKs(0.5).setShininess(20).setKt(0.5)),
                new Sphere(100, new Point3D(300, 200, 500)) //
                        .setEmission(new Color(100, 100, 0)) //
                        .setMaterial(new Material().setKd(0.25).setKs(0.25).setKr(0.5)),
                new Triangle(new Point3D(-1500, -1000, 0), new Point3D(-1500, 1000, 0),
                        new Point3D(600, 0, -5000)) //
                        .setEmission(new Color(20, 20, 20)) //
                        .setMaterial(new Material().setKr(1)),
                new Triangle(new Point3D(1800, -950, -5000), new Point3D(-1200, -950, 2000),
                        new Point3D(-1200, -950, -5000)) //
                        .setEmission(new Color(50, 100, 50)) //
                        .setMaterial(new Material().setKd(0.7).setKs(0.8).setShininess(60)));

        scene._lights.add(new SpotLight(new Color(1020, 400, 400),
                new Point3D(40, 40, 115), new Vector(1, -1, -4)) //
                .setKl(0.00001).setKq(0.000005));

        ImageWriter imageWriter = new ImageWriter("twoSpherestwoTriangles", 500, 500);
        Render render = new Render() //
                .setImageWriter(imageWriter) //
                .setCamera(camera) //
                .setRayTracer(new BasicRayTracer(scene));

        render.renderImage();
        render.writeToImage();
    }

    @Test
    public void twoSpheresTwoTriangles2() {
        Camera camera = new Camera
                (new Point3D(0, 0, 10000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setViewPlaneSize(2500, 2500).setDistance(10000); //

        scene._background = new Color(50, 50, 100);
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        scene._geometries.add( //
                new Sphere(400, new Point3D(-200, -500, -1000)) //
                        .setEmission(new Color(50, 100, 150)) //
                        .setMaterial(new Material().setKd(0.7).setKs(0.5).setShininess(20).setKt(0.5)),
                new Sphere(100, new Point3D(300, 200, 500)) //
                        .setEmission(new Color(100, 100, 0)) //
                        .setMaterial(new Material().setKd(0.25).setKs(0.25).setKr(0.5)),
                new Triangle(new Point3D(-1500, -1000, 0), new Point3D(-1500, 1000, 0),
                        new Point3D(600, 0, -5000)) //
                        .setEmission(new Color(20, 20, 20)) //
                        .setMaterial(new Material().setKr(1)),
                new Triangle(new Point3D(1800, -950, -5000), new Point3D(-1200, -950, 2000),
                        new Point3D(-1200, -950, -5000)) //
                        .setEmission(new Color(50, 100, 50)) //
                        .setMaterial(new Material().setKd(0.7).setKs(0.8).setShininess(60)));

        scene._lights.add(new SpotLight(new Color(1020, 400, 400),
                new Point3D(40, 40, 115), new Vector(1, -1, -4)) //
                .setKl(0.00001).setKq(0.000005));

         scene._lights.add(new SpotLight(new Color(400, 400, 1020),
               new Point3D(-40, 500, 600), new Vector(1, -1, -4)) //
             .setKl(0.00001).setKq(0.000005));
         scene._lights.add(
               new DirectionalLight(new Color(150, 150, 0), new Vector(-20, -20, -20)));

        ImageWriter imageWriter = new ImageWriter("twoSpherestwoTriangles2", 500, 500);
        Render render = new Render() //
                .setImageWriter(imageWriter) //
                .setCamera(camera) //
                .setRayTracer(new BasicRayTracer(scene));

        render.renderImage();
        render.writeToImage();
    }



    @Test
    public void twoSpheresTwoTriangles3() {
        Camera camera = new Camera
                (new Point3D(0, 0, 10000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setViewPlaneSize(2500, 2500).setDistance(10000); //

        scene._background = new Color(50, 50, 100);
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        scene._geometries.add( //
                new Sphere(400, new Point3D(-200, -500, -1000)) //
                        .setEmission(new Color(50, 100, 150)) //
                        .setMaterial(new Material().setKd(0.8).setKs(0.5).setShininess(20).setKt(0.01)),
                new Sphere(100, new Point3D(300, 200, 500)) //
                        .setEmission(new Color(100, 100, 0)) //
                        .setMaterial(new Material().setKd(0.25).setKs(0.25).setKr(0.5)),
                new Triangle(new Point3D(-1500, -1000, 0), new Point3D(-1500, 1000, 0),
                        new Point3D(600, 0, -5000)) //
                        .setEmission(new Color(20, 20, 20)) //
                        .setMaterial(new Material().setKr(1)),
                new Triangle(new Point3D(1800, -950, -5000), new Point3D(-1200, -950, 2000),
                        new Point3D(-1200, -950, -5000)) //
                        .setEmission(new Color(50, 100, 50)) //
                        .setMaterial(new Material().setKd(0.7).setKs(0.8).setShininess(60)));

        scene._lights.add(new SpotLight(new Color(1020, 400, 400),
                new Point3D(40, 40, 115), new Vector(1, -1, -4)) //
                .setKl(0.00001).setKq(0.000005));

        scene._lights.add(new SpotLight(new Color(400, 400, 1020),
                new Point3D(-40, 500, 600), new Vector(1, -1, -4)) //
                .setKl(0.00001).setKq(0.000005));

        scene._lights.add(
             new DirectionalLight(new Color(100, 100, 0), new Vector(4, -8, -8)));
        scene._lights.add(
                new DirectionalLight(new Color(100, 100, 0), new Vector(0, -8, -8)));
         scene._lights.add(
               new DirectionalLight(new Color(100, 100, 0), new Vector(-8, -8, -8)));

        ImageWriter imageWriter = new ImageWriter("twoSpherestwoTriangles3", 1000, 1000);
        Render render = new Render() //
                .setImageWriter(imageWriter) //
                .setCamera(camera) //
                .setRayTracer(new BasicRayTracer(scene));

        render.renderImage();
        render.writeToImage();
    }


    @Test
    public void basicTestForAA4() {
        Camera camera = new Camera
                (new Point3D(-1600, 1900, 1600),
                        new Vector(1, -1, -1), new Vector(1, 2, -1)) //
                .setViewPlaneSize(2500, 2500).setDistance(7000); //

        scene._background = new Color(100, 100, 200);
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        scene._geometries.add( //
                // sphere
                new Sphere(100, new Point3D(-50, 110, -100)) //
                        .setEmission(new Color(250, 250, 100))
                        .setMaterial(new Material().setKd(0.7).setKs(0.5).setShininess(50).setKt(0.2)));

        scene._lights.add(
                new DirectionalLight(new Color(50, 50, 50), new Vector(-1, -1, -1)));

        ImageWriter imageWriter = new ImageWriter("basicTestForAA4_8", 1000, 1000);
        Render render = new Render() //
                .setImageWriter(imageWriter) //
                .setCamera(camera) //
                .setRayTracer((new BasicRayTracer(scene)));

        render.setMultithreading(2);
        render.setAntiAliasing();
        render.renderImage();
        render.writeToImage();

    }





    @Test
    public void bubbles() {
        Camera camera = new Camera(new Point3D(0, -2500, 8000),
                new Vector(0, 0.17, -1), new Vector(0, 1, 0.17))
                .setViewPlaneSize(2500, 2500).setDistance(4000);

        scene._background = new Color(60, 80, 100);
        scene._ambientLight = new AmbientLight(new Color(100, 200, 250), 0.10);

        // water points
        Point3D B1 = new Point3D(-5000, 0, 5000);
        Point3D B2 = new Point3D(5000, 0, 5000);
        Point3D B3 = new Point3D(5000, 0, -5000);
        Point3D B4 = new Point3D(-5000, 0, -5000);
        Color waterColor = new Color(20, 80, 150);
        Material waterMaterial = new Material().setKd(0.2).setKt(0.8);

        scene._geometries.add(
                // water : triangle B1B2B3 & triangle B1B3B4
                new Triangle(B1, B2, B3)
                        .setEmission(waterColor)
                        .setMaterial(waterMaterial),

                new Triangle(B1, B3, B4)
                        .setEmission(waterColor)
                        .setMaterial(waterMaterial));

        // sand points
        Point3D C1 = new Point3D(-5000, -3000, 5000);
        Point3D C2 = new Point3D(5000, -3000, 5000);
        Point3D C3 = new Point3D(5000, -3000, -5000);
        Point3D C4 = new Point3D(-5000, -3000, -5000);
        Color sandColor = new Color(150, 150, 20);
        Material sandMaterial = (new Material().setKd(0.5).setKs(0.5).setShininess(20));

        scene._geometries.add(
                // sand : triangle C1C2C3 & triangle C1C3C4
                new Triangle(C1, C2, C3)
                .setEmission(sandColor)
                .setMaterial(sandMaterial),

                new Triangle(C1, C3, C4)
                        .setEmission(sandColor)
                        .setMaterial(sandMaterial));

        Random random = new Random();
        // bubbles
        Color bubbleColor = new Color(50, 50, 150);
        Material bubbleMaterial = new Material().setKd(0.1).setKs(0.2).setKt(0.9);
       for (int i = 0; i < 200; i++) {
           int x = random.nextInt(300) + 700;
           int y = random.nextInt(1700) + 1000;
           int z = random.nextInt(2000) + 2000;
           int radius = y < 1500 ? 10 + 9000 / y : 10 + 3000 / y;
            scene._geometries.add(
                    new Sphere(radius, new Point3D(x, -y, z)) //
                            .setEmission(bubbleColor)
                            .setMaterial(bubbleMaterial)
            );
        }

        // fish
        Color fishColor = new Color(100, 100, 100);
        Material fishMaterial = new Material().setKd(0.2).setKs(0.4).setShininess(5).setKt(0.2);
        for (int i = 0; i < 300; i++) {
            int x = random.nextInt(2000);
            int y = random.nextInt(1000)
                    + (int) (x / ((double) random.nextInt(2000) + 500) * random.nextInt(2000));
            int z = random.nextInt(1000) + 500;

            scene._geometries.add(
                    new Sphere(20, new Point3D(-x, -y, z))
                            .setEmission(fishColor)
                            .setMaterial(fishMaterial),
                    new Triangle(new Point3D(-x+20, -y, z),
                            new Point3D(-x+40, -y+20, z),
                            new Point3D(-x+40, -y-20, z))
                            .setEmission(fishColor)
                            .setMaterial(fishMaterial));
        }

        // light fish
        Color lfishColor = new Color(75, 50, 40);
        Material lfishMaterial = new Material().setKd(0.2).setKs(0.4).setShininess(5).setKt(0.2);
        int x = -2000;
        int y = -2500;
        int z = -3000;
        scene._geometries.add(
                new Sphere(150, new Point3D(x, y, z))
                        .setEmission(lfishColor)
                        .setMaterial(lfishMaterial),
                new Triangle(new Point3D(x+150, y, z),
                        new Point3D(x+250, y+100, z),
                        new Point3D(x+250, y-100, z))
                        .setEmission(fishColor)
                        .setMaterial(fishMaterial),
                new Triangle(new Point3D(x, y, z+10),
                        new Point3D(x, y-20, z-10),
                        new Point3D(x-250, y+300, z+20))
                        .setEmission(fishColor)
                        .setMaterial(fishMaterial),
                new Sphere(20, new Point3D(x-250, y+300, z+20))
                        .setEmission(new Color(50, 100, 75))
                        .setMaterial(new Material().setKt(0.8)));

        scene._lights.add(new PointLight(new Color(75,  100, 50), new Point3D(x-250, y+300, z)));

        // waves
        Color wavesColor = new Color(50, 50, 150);
        Material wavesMaterial = new Material().setKd(0.2).setKt(0.2);
        for (int i = 0; i < 300; i++) {
            x = random.nextInt(6000) - 3000;
            y = random.nextInt(100) + 25;
            z = random.nextInt(10000) - 5000;

            scene._geometries.add(
                    new Triangle(new Point3D(x, y, z),
                            new Point3D(x, y, z+60),
                            new Point3D(x-400, y-20, z+80))
                                .setEmission(wavesColor)
                                .setMaterial(wavesMaterial),
                    new Triangle(new Point3D(x, y, z),
                            new Point3D(x, y, z+60),
                            new Point3D(x+400, y-20, z+80))
                            .setEmission(wavesColor)
                            .setMaterial(wavesMaterial));
        }

        // gold sphere
        scene._geometries.add(
                new Sphere(200, new Point3D(-500, -2799, 1000))
                        .setEmission(new Color(50, 50, 20))
                        .setMaterial(new Material().setKd(0.7).setKs(0.5).setShininess(25)));

        // box
        // cube points
        Point3D A = new Point3D(300, -2899, -100);
        Point3D B = new Point3D(700, -2899, -400);
        Point3D C = new Point3D(300, -2899, -700);
        Point3D D = new Point3D(100, -2899, -400);
        Point3D E = new Point3D(300, -2550, -100);
        Point3D F = new Point3D(700, -2500, -400);
        Point3D G = new Point3D(450, -2450, -700);
        Point3D H = new Point3D(100, -2500, -700);
        Color boxColor = new Color(33, 22, 16);
        Material boxMaterial = new Material().setKd(0.5).setKs(0.5).setShininess(50).setKt(0.01);

        scene._geometries.add(
                // front side : triangle ABF & triangle AFE
                new Triangle(A, B, F)
                .setEmission(boxColor).setMaterial(boxMaterial),
                new Triangle(A, F, E).setEmission(boxColor).setMaterial(boxMaterial),
                // right side : triangle FBC & triangle FCG
                new Triangle(F, B, C).setEmission(boxColor).setMaterial(boxMaterial),
                new Triangle(F, C, G).setEmission(boxColor).setMaterial(boxMaterial),
                // left side : triangle EAD & triangle EDH
                new Triangle(E, A, D).setEmission(boxColor).setMaterial(boxMaterial),
                new Triangle(E, D, H).setEmission(boxColor).setMaterial(boxMaterial),
                // back side : triangle HGD & triangle GDC
                new Triangle(H, G, D).setEmission(boxColor).setMaterial(boxMaterial),
                new Triangle(G, D, C).setEmission(boxColor).setMaterial(boxMaterial));

        scene._lights.add(
                new DirectionalLight(new Color(15, 25, 30), new Vector(-1, -1, -1)));

        scene._lights.add(new SpotLight(new Color(1000, 5000, 1000),
                new Point3D(0, 100, 1500), new Vector(-0.2, -1, 0))
                .setKl(0.00001).setKq(0.00001));
        scene._lights.add(new SpotLight(new Color(1000, 1000, 1000),
                new Point3D(0, 100, 1500), new Vector(-0.1, -1, -0.5))
                .setKl(0.00001).setKq(0.00001));
        scene._lights.add(new SpotLight(new Color(1000, 1000, 1000),
                new Point3D(0, 100, 1500), new Vector(0.5, -1, 0.1))
                .setKl(0.00001).setKq(0.00001));
        scene._lights.add(new SpotLight(new Color(1000, 1000, 1000),
                new Point3D(0, 100, 1500), new Vector(0.1, -1, -0.2))
                .setKl(0.00001).setKq(0.00001));

        scene._lights.add(new SpotLight(new Color(1000, 1000, 1000),
                new Point3D(300, -2700, 1500), new Vector(0.1, 0, -1))
                .setKl(0.00001).setKq(0.00001));

        ImageWriter imageWriter = new ImageWriter("bubbles_1000_AA4", 1000,  1000);
        Render render = new Render() //
                .setImageWriter(imageWriter) //
                .setCamera(camera) //
                .setRayTracer((new BasicRayTracer(scene)));

        render.setMultithreading(2);
        render.setAntiAliasing();
        render.renderImage();
        render.writeToImage();

    }


}
