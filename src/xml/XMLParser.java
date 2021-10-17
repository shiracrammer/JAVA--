package xml;

import elements.AmbientLight;
import geometries.Geometries;

// importing XML-related packages
import geometries.Sphere;
import geometries.Triangle;
import org.w3c.dom.*;
import primitives.Color;
import primitives.Point3D;
import scene.Scene;

import javax.xml.parsers.*;
import java.io.*;

/**
 * Class XMLParser, to parse xml files describing a Scene object.
 * Supposing that the XML file meets the Scene specifications.
 * @author Deborah Lellouche
 */
public class XMLParser {
    final String _xmlpath;
    final Scene _scene;
    final Geometries _geometries;

    /**
     * XMLParser constructor, setting the members of the Scene object "scene" in parameter
     * using the data in the XML file at the String object "xmlpath" in parameter.
     * @param scene the Scene object whose members are to be set.
     * @param xmlpath the path of the XML file.
     */
    public XMLParser(Scene scene, String xmlpath) {
        if (xmlpath == null) {
            throw new IllegalArgumentException("path of xml file is null");
        }

        _xmlpath = xmlpath;
        _scene = scene;
        _geometries = new Geometries();

        try {
            // making a DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // making a Document from the XML file
            Document document = builder.parse(new File(_xmlpath));

            // normalizing the XML Structure; It's just too important !!
            document.getDocumentElement().normalize();

            // extracting the root element
            Element root = document.getDocumentElement();

            // getting all subnodes
            NodeList rootChildren = root.getChildNodes();

            // setting the backgroundColor of _scene
            int[] bcArray = getIntFromString(root.getAttribute("background-color"));
            Color backgroundColor = new Color(bcArray[0], bcArray[1], bcArray[2]);
            _scene.setBackground(backgroundColor);

            for (int i = 0; i < rootChildren.getLength(); i++) {
                Node node = rootChildren.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    // setting the ambientLight of _scene (Ka is not given)
                    if (node.getNodeName().equals("ambient-light")) {
                        int[] alcArray =
                                getIntFromString(node.getAttributes().getNamedItem("color").getNodeValue());
                        Color ambientLightColor = new Color(alcArray[0], alcArray[1], alcArray[2]);
                        AmbientLight ambientLight = new AmbientLight(ambientLightColor, 1d);
                        _scene.setAmbientLight(ambientLight);
                    }

                    // setting the geometries of _scene
                    if (node.getNodeName().equals("geometries")) {
                        NodeList geoChildren = node.getChildNodes();

                        for (int j = 0; j < geoChildren.getLength(); j++) {
                            Node geo = geoChildren.item(j);
                            if (geo.getNodeType() == Node.ELEMENT_NODE) {

                                // adding a sphere to _geometries
                                if (geo.getNodeName().equals("sphere")) {
                                    NamedNodeMap m = geo.getAttributes();
                                    int[] centerArray =
                                            getIntFromString(m.getNamedItem("center").getNodeValue());
                                    Point3D center = new Point3D(centerArray[0], centerArray[1], centerArray[2]);

                                    int radius = Integer.parseInt(m.getNamedItem("radius").getNodeValue());

                                    Sphere sphere = new Sphere(radius, center);
                                    _geometries.add(sphere);
                                }

                                // adding a triangle to _geometries
                                if (geo.getNodeName().equals("triangle")) {
                                    NamedNodeMap m = geo.getAttributes();
                                    int[] p0Array =
                                            getIntFromString(m.getNamedItem("p0").getNodeValue());
                                    Point3D p0 = new Point3D(p0Array[0], p0Array[1], p0Array[2]);

                                    int[] p1Array =
                                            getIntFromString(m.getNamedItem("p1").getNodeValue());
                                    Point3D p1 = new Point3D(p1Array[0], p1Array[1], p1Array[2]);

                                    int[] p2Array =
                                            getIntFromString(m.getNamedItem("p2").getNodeValue());
                                    Point3D p2 = new Point3D(p2Array[0], p2Array[1], p2Array[2]);

                                    Triangle triangle = new Triangle(p0, p1, p2);
                                    _geometries.add(triangle);
                                }


                            }

                            // eventually, setting the geometries of _scene
                            _scene.setGeometries(_geometries);
                        }
                    }
                }
            }
        }
        catch(Exception e){
                e.printStackTrace();
        }

    }

    /**
     * To return an array of integers from a String containing integers separated one from another
     * by white spaces.
     * @param s of the form : "word1 word2 ... wordn"
     * @return an array of the integers in s.
     */
    private int[] getIntFromString(String s) {
        String[] stringArray = s.split("\\s+");
        int[] intArray = new int[stringArray.length];

        for (int i = 0; i < stringArray.length; i++) {
            try {
                intArray[i] = Integer.parseInt(stringArray[i]);
            } catch(NumberFormatException e) {
                return null;
            }
        }

        return intArray;
    }

}

