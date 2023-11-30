package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GradientPaint;
import java.awt.Paint;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Matrix;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.Material;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.Triangle;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMesh;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMeshGroup;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.shapes.RectCuboid;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene.Camera;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene.Light;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene.Scene;

class LightModel {
    Color calculateColor(Material material, Camera camera, Vector3D normal, Vector3D intersectionPoint, Light light) {
        throw new UnsupportedOperationException();
    }

    Color blendColors(Color color1, Color color2) {
        int red = Math.min((color1.getRed() + color2.getRed()) / 2, 255);
        int green = Math.min((color1.getGreen() + color2.getGreen()) / 2, 255);
        int blue = Math.min((color1.getBlue() + color2.getBlue()) / 2, 255);
        int alpha = Math.min((color1.getAlpha() + color2.getAlpha()) / 2, 255);
        return new Color(red, green, blue, alpha);
    }

    Color multiplyColors(Color color1, Color color2) {
        int red = (int) (color1.getRed() * (color2.getRed() / 255.0));
        int green = (int) (color1.getGreen() * (color2.getGreen() / 255.0));
        int blue = (int) (color1.getBlue() * (color2.getBlue() / 255.0));
        int alpha = color1.getAlpha(); // (int) (color1.getAlpha() * (color2.getAlpha() / 255.0));
        return new Color(red, green, blue, alpha);
    }

    Color multiplyColor(Color color1, double factor) {
        int red = (int) (color1.getRed() * factor);
        int green = (int) (color1.getGreen() * factor);
        int blue = (int) (color1.getBlue() * factor);
        int alpha = color1.getAlpha(); // (int) (color1.getAlpha() * factor);
        red = Math.min(red, 255);
        green = Math.min(green, 255);
        blue = Math.min(blue, 255);
        alpha = Math.min(alpha, 255);
        return new Color(red, green, blue, alpha);
    }

    Color addColors(Color color1, Color color2) {
        int red = Math.min(color1.getRed() + color2.getRed(), 255);
        int green = Math.min(color1.getGreen() + color2.getGreen(), 255);
        int blue = Math.min(color1.getBlue() + color2.getBlue(), 255);
        int alpha = Math.min(color1.getAlpha() + color2.getAlpha(), 255);
        return new Color(red, green, blue, alpha);
    }
}

class PhongLightModel extends LightModel {
    private static final double AMBIENT_INTENSITY = 0.3;
    private static final double SHININESS = 10.0;
    private static final double SPECULAR_INTENSITY = 0.5;
    private static final double DIFFUSE_INTENSITY = 0.5;

    @Override
    public Color calculateColor(Material material, Camera camera, Vector3D normal, Vector3D intersectionPoint,
            Light light) {
        Color color = material.getColor();

        // Calculate ambient light
        double ambientIntensity = light.getIntensity() + material.getAmbient();
        // double ambientIntensity = light.getIntensity() * material.getAmbient();

        int red = (int) Math.min(255, color.getRed() * ambientIntensity);
        int green = (int) Math.min(255, color.getGreen() * ambientIntensity);
        int blue = (int) Math.min(255, color.getBlue() * ambientIntensity);
        int alpha = color.getAlpha();

        // Calculate diffuse light
        // Light light = scene.getLight();
        Vector3D lightPos = light.getPosition(); // camera.getPosition(); //.add(new Vector3D(0, 0, 0)); //light.getPosition();
        Vector3D lightDir = lightPos.sub(intersectionPoint).normalize();
        double dotProduct = Math.max(0, normal.dot(lightDir));
        double diffuseIntensity = material.getDiffuse();
        red += (int) (color.getRed() * dotProduct * diffuseIntensity);
        green += (int) (color.getGreen() * dotProduct * diffuseIntensity);
        blue += (int) (color.getBlue() * dotProduct * diffuseIntensity);

        // Calculate specular light
        Vector3D viewDir = camera.getPosition().sub(intersectionPoint).normalize();
        Vector3D reflectDir = normal.multiply(2).multiply(normal.dot(lightDir)).sub(lightDir)
                .normalize();
        double specularIntensity = material.getSpecular();
        double specularFactor = reflectDir.dot(viewDir); // Compute it without using the power operation to avoid using
                                                         // too much CPU. Run much smoother and the result is pretty
                                                         // good.
        // Math.pow(Math.max(0, reflectDir.dot(viewDir)), 32);

        red += (int) (color.getRed() * specularFactor * specularIntensity);
        green += (int) (color.getGreen() * specularFactor * specularIntensity);
        blue += (int) (color.getBlue() * specularFactor * specularIntensity);

        // Validate and constrain color values
        red = Math.max(0, Math.min(255, red));
        green = Math.max(0, Math.min(255, green));
        blue = Math.max(0, Math.min(255, blue));

        return new Color(red, green, blue, alpha);
    }
}

public class Rasterizer {
    private Scene scene;
    // private List<TriangleMeshGroup> tMeshGroups = Lists.newArrayList();
    BufferedImage image;
    double[] zBuffer;
    String[] idBuffer;
    public Matrix transformMatrix = new Matrix();

    PhongLightModel lightModel = new PhongLightModel();

    public Rasterizer() {
        this(new Scene());
    }

    public Rasterizer(Scene scene) {
        this.scene = scene;
    }

    private BufferedImage rasterize(Graphics2D g2, Dimension panelDimension) {
        image = new BufferedImage((int) panelDimension.getWidth(),
                (int) panelDimension.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        zBuffer = new double[image.getWidth() * image.getHeight()];
        Arrays.fill(zBuffer, Double.NEGATIVE_INFINITY);

        idBuffer = new String[image.getWidth() * image.getHeight()];
        Arrays.fill(idBuffer, null);

        // Graphics2D g2 = (Graphics2D) image.createGraphics();

        g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));

        g2.setComposite(AlphaComposite.SrcOver);

        // g2.setColor(java.awt.Color.WHITE);
        // g2.fillRect(0, 0, image.getWidth(), image.getHeight());

        // TODO gradient sky
        Color skyColorTransparent = new Color(116, 147, 170, 255);
        Color skyColorOpaque = new Color(49, 73, 111, 255);
        Paint skyColor = new GradientPaint(180.0f, 0.0f, skyColorTransparent,
                image.getWidth(), image.getHeight(), skyColorOpaque, true);
        g2.setPaint(skyColor);
        g2.fillRect(0, 0, image.getWidth(), image.getHeight());

        Matrix transform = scene.getCamera().getTransformation();
        transformMatrix = transform;

        // potential inverse transform matrix
        // [ux uy uz -dot(u,t)]
        // [vx vy vz -dot(v,t)]
        // [wx wy wz -dot(w,t)]
        // [ 0 0 0 1 ]
        // Vector3D modifiedLightPos =
        // scene.getLight().getPosition().multiply(transform);
        Vector3D modifiedLightPos = scene.getLight().getPosition();

        if (scene.getConfiguration().getShowGridXY()) {
            this.drawGridXY(g2, transform);
        }

        if (scene.getConfiguration().getShowGridXZ()) {
            this.drawGridXZ(g2, transform);
        }

        if (scene.getConfiguration().getShowGridYZ()) {
            this.drawGridYZ(g2, transform);
        }
        if (scene.getConfiguration().getShowAxis()) {
            this.drawAxes(g2, transform);
        }

        for (TriangleMeshGroup group : scene.getMeshes()) {
            if (!group.getVisible())
                continue;

            TriangleMeshGroup transformedGroup = new TriangleMeshGroup();
            Matrix translationMatrix = Matrix.translationMatrix(group.getPosition().getX(), group.getPosition().getY(),
                    group.getPosition().getZ());
            Matrix _transform = transform.multiply(translationMatrix);

            for (TriangleMesh obj : group.getMeshes()) {
                // this.scene.formatTriangles(obj);
                for (Triangle triangle : obj.getTriangles()) {
                    Vector3D[] vertices = triangle.getVertices();

                    Vector3D vertex1 = new Vector3D(vertices[0]);
                    Vector3D vertex2 = new Vector3D(vertices[1]);
                    Vector3D vertex3 = new Vector3D(vertices[2]);

                    vertex1 = vertex1.multiply(_transform);
                    vertex2 = vertex2.multiply(_transform);
                    vertex3 = vertex3.multiply(_transform);

                    int minX = (int) Math.max(0,
                            Math.ceil(Math.min(vertex1.x, Math.min(vertex2.x, vertex3.x))));
                    int maxX = (int) Math.min(image.getWidth() - 1,
                            Math.floor(Math.max(vertex1.x,
                                    Math.max(vertex2.x, vertex3.x))));
                    int minY = (int) Math.max(0,
                            Math.ceil(Math.min(vertex1.y, Math.min(vertex2.y, vertex3.y))));
                    int maxY = (int) Math.min(image.getHeight() - 1,
                            Math.floor(Math.max(vertex1.y,
                                    Math.max(vertex2.y, vertex3.y))));

                    double triangleArea = (vertex1.y - vertex3.y) * (vertex2.x - vertex3.x)
                            + (vertex2.y - vertex3.y) * (vertex3.x - vertex1.x);

                    Vector3D norm = triangle.getNormal().normalize();

                    // transformedTriangles.add(new Triangle(vertex1, vertex2, vertex3));

                    for (int y = minY; y <= maxY; y++) {
                        for (int x = minX; x <= maxX; x++) {

                            double b1 = ((y - vertex3.y) * (vertex2.x - vertex3.x)
                                    + (vertex2.y - vertex3.y) * (vertex3.x - x))
                                    / triangleArea;
                            double b2 = ((y - vertex1.y) * (vertex3.x - vertex1.x)
                                    + (vertex3.y - vertex1.y) * (vertex1.x - x))
                                    / triangleArea;
                            double b3 = ((y - vertex2.y) * (vertex1.x - vertex2.x)
                                    + (vertex1.y - vertex2.y) * (vertex2.x - x))
                                    / triangleArea;

                            boolean isInside = b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1
                                    && b3 >= 0
                                    && b3 < 1;

                            // Check if the scene ground intersect with the triangle

                            if (isInside) {
                                double depth = b1 * (vertex1.z
                                        - scene.getCamera().getPosition().z)
                                        + b2 * (vertex2.z - scene.getCamera()
                                                .getPosition().z)
                                        + b3 * (vertex3.z - scene.getCamera()
                                                .getPosition().z);
                                int zIndex = y * image.getWidth() + x;

                                if (zBuffer[zIndex] < depth) {
                                    zBuffer[zIndex] = depth;
                                    idBuffer[zIndex] = group.getIdentifier();

                                    // Color finalColor = phongModel(obj,
                                    // obj.getMaterial().getColor(),
                                    // norm,
                                    // new Vector3D(x, y, depth), scene.getLight().getPosition());

                                    Color finalColor = lightModel.calculateColor(obj.getMaterial(),
                                            scene.getCamera(),
                                            norm, new Vector3D(x, y, depth), scene.getLight());
                                    // image.setRGB(x, y, finalColor.getRGB());
                                    g2.setColor(finalColor); // g2.setColor(new Color(finalColor.getRed(),
                                                             // finalColor.getGreen(), finalColor.getBlue(), 50));
                                    g2.fillRect(x, y, 1, 1);
                                }
                            }
                        }
                    }
                }

                // TriangleMesh transformed = new TriangleMesh(transformedTriangles,
                // obj.getMaterial());
                // transformedGroup.addMesh(transformed);
            }

            transformedGroup.setSelected(group.getSelected());
            transformedGroup.setValid(group.getValid());
            transformedGroup.setIdentifier(group.getIdentifier());
            // tMeshGroups.add(transformedGroup);
        }

        this.drawInvalidMeshBounding(g2);
        this.drawSelectedMeshBounding(g2);

        // g2.dispose();

        return image;
    }

    private void drawSelectedMeshBounding(Graphics2D g2) {
        int stroke = 2; // (int) Math.floor(1 * scene.getCamera().getScale());
        g2.setColor(scene.getConfiguration().getSelectionColor());

        if (stroke >= image.getWidth() && stroke >= image.getHeight()) {
            g2.fillRect(0, 0, stroke, stroke);
            return;
        }

        for (int y = 0; y < image.getHeight(); y++) {
            // We do not draw pixel on the edge of the image
            if (y == 0 || y >= image.getHeight())
                continue;

            for (int x = 0; x < image.getWidth(); x++) {
                if (x == 0 || x >= image.getWidth() - 1)
                    continue; // We do not draw pixel on the edge of the image

                final int depth = y * image.getWidth() + x;
                if (idBuffer[depth] != null && scene.getMesh(idBuffer[depth]).getSelected()) {
                    // Check if the id of the pixel is the same on top, left, right and bottom.
                    // If not, it means that the pixel is on the edge of a mesh.
                    // So we draw the pixel in order to create a boundary
                    if (depth <= 0)
                        continue;
                    if (depth + stroke > image.getWidth() * image.getHeight() - 1)
                        continue;

                    int topPixelDepth = (y - 1) * image.getWidth() + x;
                    int leftPixelDepth = y * image.getWidth() + (x - 1);
                    int rightPixelDepth = y * image.getWidth() + (x + 1);
                    int bottomPixelDepth = (y + 1) * image.getWidth() + x;
                    // int bottomLeftPixelDepth = (y + 1) * image.getWidth() + (x - 1);
                    // int bottomRightPixelDepth = (y + 1) * image.getWidth() + (x + 1);
                    // int topLeftPixelDepth = (y - 1) * image.getWidth() + (x - 1);
                    // int topRightPixelDepth = (y - 1) * image.getWidth() + (x + 1);

                    if (topPixelDepth >= 0 && idBuffer[topPixelDepth] != idBuffer[depth]) {
                        g2.drawRect(x, y, stroke, stroke);
                    }

                    if (leftPixelDepth >= 0 && leftPixelDepth <= idBuffer.length
                            && idBuffer[leftPixelDepth] != idBuffer[depth]) {
                        g2.drawRect(x, y, stroke, stroke);
                        continue;
                    }

                    if (idBuffer[rightPixelDepth] != idBuffer[depth]) {
                        g2.drawRect(x, y, stroke, stroke);
                        continue;
                    }

                    if (bottomPixelDepth < idBuffer.length && idBuffer[bottomPixelDepth] != idBuffer[depth]) {
                        g2.drawRect(x, y, stroke, stroke);
                        continue;
                    }
                    // if (bottomLeftPixelDepth >= 0 && bottomLeftPixelDepth < idBuffer.length
                    // && idBuffer[bottomLeftPixelDepth] != idBuffer[depth]) {
                    // g2.fillRect(x - stroke, y - stroke, stroke, stroke);
                    // }

                    // if (bottomRightPixelDepth >= 0 && bottomRightPixelDepth < idBuffer.length
                    // && idBuffer[bottomRightPixelDepth] != idBuffer[depth]) {
                    // g2.fillRect(x - 1, y - stroke, stroke, stroke);
                    // }

                    // if (topLeftPixelDepth >= 0 && topLeftPixelDepth < idBuffer.length
                    // && idBuffer[topLeftPixelDepth] != idBuffer[depth]) {
                    // g2.fillRect(x - stroke, y - 1, stroke, stroke);
                    // }

                    // if (topRightPixelDepth >= 0 && topRightPixelDepth < idBuffer.length
                    // && idBuffer[topRightPixelDepth] != idBuffer[depth]) {
                    // g2.fillRect(x - 1, y - 1, stroke, stroke);
                    // }

                }
            }
        }
    }

    private void drawInvalidMeshBounding(Graphics2D g2) {
        int stroke = 1;
        // stroke = (int) Math.round(1 * scene.getCamera().getScale());

        for (int y = 0; y < image.getHeight(); y++) {
            if (y < stroke || y > image.getHeight() + stroke)
                continue; // We do not draw pixel on the edge of the image

            for (int x = 0; x < image.getWidth(); x++) {
                if (x == 0 || x > image.getWidth() + stroke)
                    continue; // We do not draw pixel on the edge of the image

                final int depth = y * image.getWidth() + x;

                if (depth <= 0 || depth + 1 > image.getWidth() * image.getHeight() - 1 || idBuffer[depth] == null)
                    continue;

                TriangleMesh mesh = scene.getMesh(idBuffer[depth]);

                if (idBuffer[depth] != null && mesh != null && !mesh.getValid()) {

                    // fill invalid accessories
                    g2.setColor(new Color(255, 0, 0, 100));
                    g2.fillRect(x, y, 1, 1);
                    if (mesh.getSelected())
                        continue;

                    // Check if the id of the pixel is the same on top, left, right and bottom.
                    // If not, it means that the pixel is on the edge of a mesh.
                    // So we draw the pixel in order to create a boundary
                    int topPixelDepth = (y - 1) * image.getWidth() + x;
                    int leftPixelDepth = y * image.getWidth() + (x - 1);
                    int rightPixelDepth = y * image.getWidth() + (x + 1);
                    int bottomPixelDepth = (y + 1) * image.getWidth() + x;

                    // draw border
                    g2.setColor(new Color(255, 0, 0, 150));

                    if (topPixelDepth >= 0 && idBuffer[topPixelDepth] != idBuffer[depth]) {
                        g2.drawRect(x, y, stroke, stroke);
                        continue;
                    }

                    if (leftPixelDepth >= 0 && leftPixelDepth <= idBuffer.length
                            && idBuffer[leftPixelDepth] != idBuffer[depth]) {
                        g2.drawRect(x, y, stroke, stroke);
                        continue;
                    }

                    if (idBuffer[rightPixelDepth] != idBuffer[depth]) {
                        g2.drawRect(x, y, stroke, stroke);
                        continue;
                    }

                    if (bottomPixelDepth < idBuffer.length && idBuffer[bottomPixelDepth] != idBuffer[depth]) {
                        g2.drawRect(x, y, stroke, stroke);
                        continue;
                    }
                }
            }
        }
    }

    public void draw(Graphics g, Dimension panelDimension) {
        image = rasterize((Graphics2D) g, panelDimension);
        // g.drawImage(canvasBuffer, 0, 0, null);
        drawCameraDetails(g, new Point(10, 20));
        drawLightDetails(g, new Point(10, 80));

        // Draw cube in the bottom right corner indicating the camera direction
        // drawCameraDirectionCube(g, image.getWidth(), image.getHeight() - 80, 40);
    }

    private void drawCameraDirectionCube(Graphics g, int cubeX, int cubeY, int size) {
        // TODO: Re implement this method
        int cubeSize = size;
        int padding = 10;
        Vector3D cubePosition = new Vector3D(cubeX, cubeY, 1000);
        Vector3D cubeDimension = new Vector3D(cubeSize, cubeSize, cubeSize);
        RectCuboid cube = new RectCuboid(cubePosition, cubeDimension);
        cube.getMaterial().setColor(Color.WHITE);

        // scene.formatTriangles(cube);

        cube.getMaterial().setAmbient(0.1);
        double[] cubeZBuffer = new double[zBuffer.length];
        Arrays.fill(cubeZBuffer, Double.NEGATIVE_INFINITY);

        for (Triangle triangle : cube.getTriangles()) {
            triangle = triangle.translate(cube.getCenter().multiply(-1))
                    .transform(Matrix.rotationMatrix(scene.getCamera().getDirection().getX(),
                            scene.getCamera().getDirection().getY(), scene.getCamera().getDirection().getZ()));
            triangle = triangle
                    .translate(new Vector3D(image.getWidth() - cubeSize - padding,
                            image.getHeight() - cubeSize - padding, 0));
            Vector3D[] vertices = triangle.getVertices();

            Vector3D vertex1 = new Vector3D(vertices[0]);
            Vector3D vertex2 = new Vector3D(vertices[1]);
            Vector3D vertex3 = new Vector3D(vertices[2]);

            // vertex1 = vertex1.multiply(transform);
            // vertex2 = vertex2.multiply(transform);
            // vertex3 = vertex3.multiply(transform);

            int minX = (int) Math.max(0,
                    Math.ceil(Math.min(vertex1.x, Math.min(vertex2.x, vertex3.x))));
            int maxX = (int) Math.min(image.getWidth() - 1,
                    Math.floor(Math.max(vertex1.x,
                            Math.max(vertex2.x, vertex3.x))));
            int minY = (int) Math.max(0,
                    Math.ceil(Math.min(vertex1.y, Math.min(vertex2.y, vertex3.y))));
            int maxY = (int) Math.min(image.getHeight() - 1,
                    Math.floor(Math.max(vertex1.y,
                            Math.max(vertex2.y, vertex3.y))));

            double triangleArea = (vertex1.y - vertex3.y) * (vertex2.x - vertex3.x)
                    + (vertex2.y - vertex3.y) * (vertex3.x - vertex1.x);

            Vector3D norm = triangle.getNormal().normalize();

            for (int y = minY; y <= maxY; y++) {
                for (int x = minX; x <= maxX; x++) {

                    double b1 = ((y - vertex3.y) * (vertex2.x - vertex3.x)
                            + (vertex2.y - vertex3.y) * (vertex3.x - x))
                            / triangleArea;
                    double b2 = ((y - vertex1.y) * (vertex3.x - vertex1.x)
                            + (vertex3.y - vertex1.y) * (vertex1.x - x))
                            / triangleArea;
                    double b3 = ((y - vertex2.y) * (vertex1.x - vertex2.x)
                            + (vertex1.y - vertex2.y) * (vertex2.x - x))
                            / triangleArea;

                    boolean isInside = b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1
                            && b3 >= 0
                            && b3 < 1;

                    // Check if the scene ground intersect with the triangle

                    if (isInside) {
                        double depth = b1 * (vertex1.z
                                - scene.getCamera().getPosition().z)
                                + b2 * (vertex2.z - scene.getCamera()
                                        .getPosition().z)
                                + b3 * (vertex3.z - scene.getCamera()
                                        .getPosition().z);
                        int zIndex = y * image.getWidth() + x;

                        if (cubeZBuffer[zIndex] < depth) {
                            cubeZBuffer[zIndex] = depth;

                            Color finalColor = lightModel.calculateColor(cube.getMaterial(),
                                    scene.getCamera(),
                                    norm, new Vector3D(x, y, depth), scene.getLight());
                            g.setColor(finalColor);
                            g.fillRect(x, y, 1, 1);
                        }
                    }
                }
            }
        }
    }

    private void drawAxes(Graphics2D g2, Matrix transform) {
        int axisLength = (int) Math
                .floor(scene.getConfiguration().getGridStep() * scene.getConfiguration().getStepCounts());

        g2.setStroke(new BasicStroke(scene.getConfiguration().getAxisStrokeWidth()));

        Vector3D xAxis1 = new Vector3D(-axisLength, 0, 0);
        Vector3D xAxis2 = new Vector3D(axisLength, 0, 0);

        // Vector3D yAxis1 = new Vector3D(0, -axisLength, 0);
        // Vector3D yAxis2 = new Vector3D(0, axisLength, 0);

        Vector3D zAxis1 = new Vector3D(0, 0, -axisLength);
        Vector3D zAxis2 = new Vector3D(0, 0, axisLength);

        // Transform axis
        xAxis1 = xAxis1.multiply(transform);
        xAxis2 = xAxis2.multiply(transform);
        // yAxis1 = yAxis1.multiply(transform);
        // yAxis2 = yAxis2.multiply(transform);
        zAxis1 = zAxis1.multiply(transform);
        zAxis2 = zAxis2.multiply(transform);

        g2.setColor(Color.RED);
        g2.drawLine((int) xAxis1.x, (int) xAxis1.y, (int) xAxis2.x, (int) xAxis2.y);

        // g2.setColor(Color.GREEN);
        // g2.drawLine((int) yAxis1.x, (int) yAxis1.y, (int) yAxis2.x, (int) yAxis2.y);

        g2.setColor(Color.BLUE);
        g2.drawLine((int) zAxis1.x, (int) zAxis1.y, (int) zAxis2.x, (int) zAxis2.y);
    }

    public void drawGridXY(Graphics2D g2, Matrix transform) {
        g2.setColor(scene.getConfiguration().getGridColor());
        g2.setStroke(new BasicStroke(scene.getConfiguration().getGridStrokeWidth()));

        double gridStep = scene.getConfiguration().getGridStep();
        int axisLength = (int) (gridStep * scene.getConfiguration().getStepCounts());

        for (double i = 0; i <= axisLength; i += gridStep) {
            Vector3D x3 = new Vector3D(i, -axisLength, 0);
            Vector3D x4 = new Vector3D(i, axisLength, 0);
            Vector3D y3 = new Vector3D(-axisLength, i, 0);
            Vector3D y4 = new Vector3D(axisLength, i, 0);

            x3 = x3.multiply(transform);
            x4 = x4.multiply(transform);
            y3 = y3.multiply(transform);
            y4 = y4.multiply(transform);

            g2.drawLine((int) x3.x, (int) x3.y, (int) x4.x, (int) x4.y);
            g2.drawLine((int) y3.x, (int) y3.y, (int) y4.x, (int) y4.y);
        }

        for (double i = 0; i >= -axisLength; i -= gridStep) {
            Vector3D x3 = new Vector3D(i, -axisLength, 0);
            Vector3D x4 = new Vector3D(i, axisLength, 0);
            Vector3D y3 = new Vector3D(-axisLength, i, 0);
            Vector3D y4 = new Vector3D(axisLength, i, 0);

            x3 = x3.multiply(transform);
            x4 = x4.multiply(transform);
            y3 = y3.multiply(transform);
            y4 = y4.multiply(transform);

            g2.drawLine((int) x3.x, (int) x3.y, (int) x4.x, (int) x4.y);
            g2.drawLine((int) y3.x, (int) y3.y, (int) y4.x, (int) y4.y);
        }
    }

    public void drawGridXZ(Graphics2D g2, Matrix transform) {
        g2.setColor(scene.getConfiguration().getGridColor());
        g2.setStroke(new BasicStroke(scene.getConfiguration().getGridStrokeWidth()));

        double gridStep = scene.getConfiguration().getGridStep();
        int axisLength = (int) (gridStep * scene.getConfiguration().getStepCounts());

        for (double i = 0; i <= axisLength; i += gridStep) {
            Vector3D x1 = new Vector3D(i, 0, -axisLength);
            Vector3D x2 = new Vector3D(i, 0, axisLength);
            Vector3D y1 = new Vector3D(-axisLength, 0, i);
            Vector3D y2 = new Vector3D(axisLength, 0, i);

            x1 = x1.multiply(transform);
            x2 = x2.multiply(transform);
            y1 = y1.multiply(transform);
            y2 = y2.multiply(transform);

            g2.drawLine((int) x1.x, (int) x1.y, (int) x2.x, (int) x2.y);
            g2.drawLine((int) y1.x, (int) y1.y, (int) y2.x, (int) y2.y);
        }

        for (double i = 0; i >= -axisLength; i -= gridStep) {
            Vector3D x1 = new Vector3D(i, 0, -axisLength);
            Vector3D x2 = new Vector3D(i, 0, axisLength);
            Vector3D y1 = new Vector3D(-axisLength, 0, i);
            Vector3D y2 = new Vector3D(axisLength, 0, i);

            x1 = x1.multiply(transform);
            x2 = x2.multiply(transform);
            y1 = y1.multiply(transform);
            y2 = y2.multiply(transform);

            g2.drawLine((int) x1.x, (int) x1.y, (int) x2.x, (int) x2.y);
            g2.drawLine((int) y1.x, (int) y1.y, (int) y2.x, (int) y2.y);
        }
    }

    public void drawGridYZ(Graphics2D g2, Matrix transform) {
        g2.setColor(scene.getConfiguration().getGridColor());
        g2.setStroke(new BasicStroke(scene.getConfiguration().getGridStrokeWidth()));

        double gridStep = scene.getConfiguration().getGridStep();
        int axisLength = (int) (gridStep * scene.getConfiguration().getStepCounts());

        for (double i = 0; i <= axisLength; i += gridStep) {
            Vector3D x1 = new Vector3D(0, -axisLength, i);
            Vector3D x2 = new Vector3D(0, axisLength, i);
            Vector3D y1 = new Vector3D(0, i, -axisLength);
            Vector3D y2 = new Vector3D(0, i, axisLength);

            x1 = x1.multiply(transform);
            x2 = x2.multiply(transform);
            y1 = y1.multiply(transform);
            y2 = y2.multiply(transform);

            g2.drawLine((int) x1.x, (int) x1.y, (int) x2.x, (int) x2.y);
            g2.drawLine((int) y1.x, (int) y1.y, (int) y2.x, (int) y2.y);
        }

        for (double i = 0; i >= -axisLength; i -= gridStep) {
            Vector3D x1 = new Vector3D(0, -axisLength, i);
            Vector3D x2 = new Vector3D(0, axisLength, i);
            Vector3D y1 = new Vector3D(0, i, -axisLength);
            Vector3D y2 = new Vector3D(0, i, axisLength);

            x1 = x1.multiply(transform);
            x2 = x2.multiply(transform);
            y1 = y1.multiply(transform);
            y2 = y2.multiply(transform);

            g2.drawLine((int) x1.x, (int) x1.y, (int) x2.x, (int) x2.y);
            g2.drawLine((int) y1.x, (int) y1.y, (int) y2.x, (int) y2.y);
        }
    }

    public void drawCameraDetails(Graphics g, Point position) {
        Vector3D cameraPosition = scene.getCamera().getPosition();
        Vector3D cameraDirection = scene.getCamera().getDirection();
        double cameraScale = scene.getCamera().getScale();

        g.setColor(Color.WHITE);
        g.drawString(
                String.format("Camera Position: (%.2f, %.2f, %.2f)", cameraPosition.x, cameraPosition.y,
                        cameraPosition.z),
                position.x, position.y);
        g.drawString(String.format("Camera Direction: (%.2f, %.2f, %.2f)", cameraDirection.x, cameraDirection.y,
                cameraDirection.z), position.x, position.y + 20);
        g.drawString(String.format("Camera Scale: (%.2f, %.2f, %.2f)", cameraScale, cameraScale, cameraScale),
                position.x, position.y + 40);
    }

    public void drawLightDetails(Graphics g, Point position) {
        Light light = scene.getLight();

        g.setColor(Color.WHITE);
        g.drawString("Light Position: " + light.getPosition(), position.x, position.y);
        g.drawString("Light Color: " + light.getColor(), position.x, position.y + 20);
        g.drawString("Light Intensity: " + light.getIntensity(), position.x, position.y + 40);
    }

    public TriangleMeshGroup getMeshFromPoint(Point point) {
        int depth = point.y * image.getWidth() + point.x;

        if (depth < 0 || depth >= idBuffer.length)
            return null;

        if (idBuffer[depth] != null) {
            return scene.getMesh(idBuffer[depth]);
        }
        return null;
    }
}
