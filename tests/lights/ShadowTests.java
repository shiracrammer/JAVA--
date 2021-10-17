package lights;

import org.junit.jupiter.api.Test;

import elements.*;
import geometries.*;
import primitives.*;
import renderer.*;
import scene.Scene;

/**
 * Testing basic shadows
 *
 * @author Dan
 * with changes
 */
public class ShadowTests {
    private Scene scene = new Scene("Test scene");
    private Camera camera = new Camera
            (new Point3D(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
            .setViewPlaneSize(200, 200).setDistance(1000);

    /**
     * Produce a picture of a sphere and triangle with point light and shade
     */
    @Test
    public void sphereTriangleInitial() {
        scene._geometries.add( //
                new Sphere(60, new Point3D(0, 0, -200)) //
                        .setEmission(new Color(java.awt.Color.BLUE)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)), //
                new Triangle(new Point3D(-70, -40, 0), new Point3D(-40, -70, 0),
                        new Point3D(-68, -68, -4)) //
                        .setEmission(new Color(java.awt.Color.BLUE)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)) //
        );
        scene._lights.add( //
                new SpotLight(new Color(400, 240, 0), new Point3D(-100, -100, 200),
                        new Vector(1, 1, -3)) //
                        .setKl(1E-5).setKq(1.5E-7));

        Render render = new Render(). //
                setImageWriter(new ImageWriter("shadowSphereTriangleInitial", 400, 400)) //
                .setCamera(camera) //
                .setRayTracer(new BasicRayTracer(scene));
        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a two triangles lighted by a spot light with a Sphere
     * producing a shading
     */
    @Test
    public void trianglesSphere() {
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        scene._geometries.add( //
                new Triangle(new Point3D(-150, -150, -115), new Point3D(150, -150, -135),
                        new Point3D(75, 75, -150)) //
                        .setMaterial(new Material().setKs(0.8).setShininess(60)), //
                new Triangle(new Point3D(-150, -150, -115), new Point3D(-70, 70, -140),
                        new Point3D(75, 75, -150)) //
                        .setMaterial(new Material().setKs(0.8).setShininess(60)), //
                new Sphere(30, new Point3D(0, 0, -115)) //
                        .setEmission(new Color(java.awt.Color.BLUE)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)) //
        );
        scene._lights.add( //
                new SpotLight(new Color(700, 400, 400), new Point3D(40, 40, 115),
                        new Vector(-1, -1, -4)) //
                        .setKl(4E-4).setKq(2E-5));

        Render render = new Render() //
                .setImageWriter(new ImageWriter("shadowTrianglesSphereSOFT2_0.05_0.4", 600, 600)) //
                .setCamera(camera) //
                .setRayTracer(new BasicRayTracer(scene).setSoftShadows(11));
        render.renderImage();
        render.writeToImage();
    }

    @Test
    public void sphereTriangleMoveTriangle1() {
        scene._geometries.add( //
                new Sphere(60, new Point3D(0, 0, -200)) //
                        .setEmission(new Color(java.awt.Color.BLUE)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)), //
               // new Triangle(new Point3D(-70, -40, -100), new Point3D(-40, -70, 0),
                //        new Point3D(-68, -68, -4)) //
                new Triangle(new Point3D(-65, -35, 0), new Point3D(-35, -65, 0),
                        new Point3D(-63, -63, -4)) //
                        .setEmission(new Color(java.awt.Color.BLUE)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)) //
        );
        scene._lights.add( //
                new SpotLight(new Color(400, 240, 0), new Point3D(-100, -100, 200),
                        new Vector(1, 1, -3)) //
                        .setKl(1E-5).setKq(1.5E-7));

        Render render = new Render(). //
                setImageWriter(new ImageWriter("shadowSphereTriangleMoveTriangle1", 400, 400)) //
                .setCamera(camera) //
                .setRayTracer(new BasicRayTracer(scene));
        render.renderImage();
        render.writeToImage();
    }

    @Test
    public void sphereTriangleMoveTriangle2() {
        scene._geometries.add( //
                new Sphere(60, new Point3D(0, 0, -200)) //
                        .setEmission(new Color(java.awt.Color.BLUE)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)), //
                // new Triangle(new Point3D(-70, -40, -100), new Point3D(-40, -70, 0),
                //        new Point3D(-68, -68, -4)) //
                new Triangle(new Point3D(-50, -18, 0), new Point3D(-20, -48, 0),
                        new Point3D(-50, -48, -4)) //
                        .setEmission(new Color(java.awt.Color.BLUE)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)) //
        );
        scene._lights.add( //
                new SpotLight(new Color(400, 240, 0), new Point3D(-100, -100, 200),
                        new Vector(1, 1, -3)) //
                        .setKl(1E-5).setKq(1.5E-7));

        Render render = new Render(). //
                setImageWriter(new ImageWriter("shadowSphereTriangleMoveTriangle2", 400, 400)) //
                .setCamera(camera) //
                .setRayTracer(new BasicRayTracer(scene));
        render.renderImage();
        render.writeToImage();
    }

    @Test
    public void sphereTriangleMoveLight1() {
        scene._geometries.add( //
                new Sphere(60, new Point3D(0, 0, -200)) //
                        .setEmission(new Color(java.awt.Color.BLUE)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)), //
                new Triangle(new Point3D(-70, -40, 0), new Point3D(-40, -70, 0),
                        new Point3D(-68, -68, -4)) //
                        .setEmission(new Color(java.awt.Color.BLUE)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)) //
        );
        scene._lights.add( //
                new SpotLight(new Color(400, 240, 0), new Point3D(-90, -90, 175),
                        new Vector(1, 1, -3)) //
                        .setKl(1E-5).setKq(1.5E-7));

        Render render = new Render(). //
                setImageWriter(new ImageWriter("shadowSphereTriangleMoveLight1", 400, 400)) //
                .setCamera(camera) //
                .setRayTracer(new BasicRayTracer(scene));
        render.renderImage();
        render.writeToImage();
    }

    @Test
    public void sphereTriangleMoveLight2() {
        scene._geometries.add( //
                new Sphere(60, new Point3D(0, 0, -200)) //
                        .setEmission(new Color(java.awt.Color.BLUE)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)), //
                new Triangle(new Point3D(-70, -40, 0), new Point3D(-40, -70, 0),
                        new Point3D(-68, -68, -4)) //
                        .setEmission(new Color(java.awt.Color.BLUE)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)) //
        );
        scene._lights.add( //
                new SpotLight(new Color(400, 240, 0), new Point3D(-73, -73, 60),
                        new Vector(1, 1, -3)) //
                        .setKl(1E-5).setKq(1.5E-7));

        Render render = new Render(). //
                setImageWriter(new ImageWriter("shadowSphereTriangleMoveLight2", 400, 400)) //
                .setCamera(camera) //
                .setRayTracer(new BasicRayTracer(scene));
        render.renderImage();
        render.writeToImage();
    }

    @Test
    public void CubeSphere() {
        Camera camera = new Camera
                (new Point3D(-1600, 1900, 1600),
                        new Vector(1, -1, -1), new Vector(1, 2, -1)) //
                .setViewPlaneSize(2500, 2500).setDistance(7000); //

        scene._background = new Color(java.awt.Color.WHITE);
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        // cube points
        Point3D A = new Point3D(100, 10, -100);
        Point3D B = new Point3D(400, 10, -100);
        Point3D C = new Point3D(400, 10, -400);
        Point3D D = new Point3D(100, 10, -400);
        Point3D E = new Point3D(100, 300, -100);
        Point3D F = new Point3D(400, 300, -100);
        Point3D G = new Point3D(400, 300, -400);
        Point3D H = new Point3D(100, 300, -400);

        // base points
        Point3D B1 = new Point3D(-2000, 0, 5000);//-1000
        Point3D B2 = new Point3D(2000, 0, 5000);
        Point3D B3 = new Point3D(2000, 0, -5000);
        Point3D B4 = new Point3D(-2000, 0, -5000);

        scene._geometries.add( //
                // sphere
                new Sphere(100, new Point3D(-50, 110, -100)) //
                        .setEmission(new Color(50, 50, 20))
                        .setMaterial(new Material().setKd(0.7).setKs(0.5).setShininess(50).setKt(0.2)),//

                // cube
                // base of the cube : triangle ABC & triangle ACD
                new Triangle(A, B, C)
                        .setEmission(new Color(33, 22, 16))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(50).setKt(0.01)),
                new Triangle(A, C, D)
                        .setEmission(new Color(33, 22, 16)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(50).setKt(0.01)),
                // upper side : triangle EFG & triangle EGH
                //new Triangle(E, F, G)
                //        .setEmission(new Color(33, 22, 16)) //
                //        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(50).setKt(0.01)),
                //new Triangle(E, G, H)
                //        .setEmission(new Color(33, 22, 16)) //
                //        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(50).setKt(0.01)),
                // front side : triangle ABF & triangle AFE
                new Triangle(A, B, F)
                        .setEmission(new Color(50, 100, 150)) //
                        .setMaterial(new Material().setKd(0.2).setKs(0.1).setShininess(20).setKt(1.0)),
                new Triangle(A, F, E)
                        .setEmission(new Color(50, 100, 150)) //
                        .setMaterial(new Material().setKd(0.2).setKs(0.1).setShininess(20).setKt(1.0)),
                // right side : triangle FBC & triangle FCG
                new Triangle(F, B, C)
                        .setEmission(new Color(33, 22, 16)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(50).setKt(0.01)),
                new Triangle(F, C, G)
                        .setEmission(new Color(33, 22, 16)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(50).setKt(0.01)),
                // left side : triangle EAD & triangle EDH
                new Triangle(E, A, D)
                        .setEmission(new Color(33, 22, 16)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(50).setKt(0.01)),
                new Triangle(E, D, H)
                        .setEmission(new Color(33, 22, 16)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(50).setKt(0.01)),
                // back side : triangle HGD & triangle GDC
                new Triangle(H, G, D)
                        .setEmission(new Color(33, 22, 16)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(50).setKt(0.01)),
                new Triangle(G, D, C)
                        .setEmission(new Color(33, 22, 16)) //
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(50).setKt(0.01)),

                // base : triangle B1B2B3 & triangle B2B3B4
                new Triangle(B1, B2, B3)
                        .setEmission(new Color(20, 50, 50)) //
                        .setMaterial(new Material().setKd(0.2).setKs(0.2)),

                new Triangle(B1, B3, B4)
                        .setEmission(new Color(20, 50, 50)) //
                        .setMaterial(new Material().setKd(0.2).setKs(0.2)));

        scene._lights.add(
                new DirectionalLight(new Color(25, 25, 25), new Vector(-1, -1, -1)));

        scene._lights.add(new SpotLight(new Color(250, 400, 250),
                new Point3D(0, 250, 550), new Vector(-0.9, -1, -1)) //
                .setKl(0.00001).setKq(0.000005));

        scene._lights.add(new SpotLight(new Color(175, 175, 150),
                new Point3D(160, 600, 550), new Vector(-2.0, -0.25, -0.5)) //
                .setKl(0.00002).setKq(0.000004));
        scene._lights.add(new SpotLight(new Color(175, 175, 150),
                new Point3D(170, 620, 550), new Vector(-3.0, -0.25, -0.5)) //
                .setKl(0.00002).setKq(0.000004));
        scene._lights.add(new SpotLight(new Color(175, 175, 150),
                new Point3D(180, 640, 550), new Vector(-4.0, -0.25, -0.5)) //
                .setKl(0.00002).setKq(0.000004));
        scene._lights.add(new SpotLight(new Color(175, 175, 150),
                new Point3D(190, 660, 550), new Vector(-5.0, -0.25, -0.5)) //
                .setKl(0.00002).setKq(0.000004));
        scene._lights.add(new SpotLight(new Color(175, 175, 150),
                new Point3D(200, 680, 550), new Vector(-6.0, -0.25, -0.5)) //
                .setKl(0.00002).setKq(0.000004));

        ImageWriter imageWriter = new ImageWriter("CubeSphere", 1000, 1000);
        Render render = new Render() //
                .setImageWriter(imageWriter) //
                .setCamera(camera) //
                .setRayTracer((new BasicRayTracer(scene)).setSoftShadows());

        render.renderImage();
        render.writeToImage();
    }
}
