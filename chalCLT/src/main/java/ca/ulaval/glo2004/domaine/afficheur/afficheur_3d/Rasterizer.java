package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import com.google.common.collect.Lists;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Matrix;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.Triangle;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMesh;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMeshGroup;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene.Light;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene.Scene;

public class Rasterizer {
    private Scene scene;
    private List<TriangleMeshGroup> tMeshGroups = Lists.newArrayList();
    BufferedImage image;
    double[] zBuffer;
    String[] idBuffer;

    public Rasterizer() {
        this(new Scene());
    }

    public Rasterizer(Scene scene) {
        this.scene = scene;
    }

    private BufferedImage rasterize(Dimension panelDimension) {
        image = new BufferedImage((int) panelDimension.getWidth(),
                (int) panelDimension.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        zBuffer = new double[image.getWidth() * image.getHeight()];
        Arrays.fill(zBuffer, Double.NEGATIVE_INFINITY);

        idBuffer = new String[image.getWidth() * image.getHeight()];
        Arrays.fill(idBuffer, null);

        Graphics2D g2 = (Graphics2D) image.createGraphics();

        g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));
        g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY));
        g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC));
        g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY));
        g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY));
        g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_DITHERING,
                RenderingHints.VALUE_DITHER_ENABLE));

        g2.setComposite(AlphaComposite.SrcOver);

        // TODO gradient sky
        g2.setColor(scene.getConfiguration().getBackgroundColor());
        g2.fillRect(0, 0, image.getWidth(), image.getHeight());

        this.tMeshGroups.clear();

        Matrix translateToOrigin = Matrix.translationMatrix(panelDimension.getWidth() / 2,
                panelDimension.getHeight() / 2, 0);
        Matrix camRotateX = Matrix.rotationXMatrix(scene.getCamera().getDirection().x);
        Matrix camRotateY = Matrix.rotationYMatrix(scene.getCamera().getDirection().y);
        Matrix camRotateZ = Matrix.rotationZMatrix(scene.getCamera().getDirection().z);
        Matrix camTranslate = Matrix.translationMatrix(
                scene.getCamera().getPosition().x,
                scene.getCamera().getPosition().y,
                scene.getCamera().getPosition().z);
        Matrix camScale = Matrix.scaleMatrix(
                scene.getCamera().scale,
                scene.getCamera().scale,
                scene.getCamera().scale);
        Matrix camTransform = camTranslate.multiply(camScale).multiply(camRotateX).multiply(camRotateY)
                .multiply(camRotateZ);

        Matrix transform = translateToOrigin.multiply(camTransform);

        if (scene.getConfiguration().getShowGridXY()) {
            this.drawGridXY(image, transform);
        }

        if (scene.getConfiguration().getShowGridXZ()) {
            this.drawGridXZ(image, transform);
        }

        if (scene.getConfiguration().getShowGridYZ()) {
            this.drawGridYZ(image, transform);
        }
        if (scene.getConfiguration().getShowAxis()) {
            this.drawAxes(image, transform);
        }

        for (TriangleMeshGroup group : scene.getMeshes()) {
            TriangleMeshGroup transformedGroup = new TriangleMeshGroup();
            Matrix translationMatrix = Matrix.translationMatrix(group.getPosition().getX(), group.getPosition().getY(),
                    group.getPosition().getZ());
            Matrix _transform = transform.multiply(translationMatrix);

            for (TriangleMesh obj : group.getMeshes()) {
                List<Triangle> transformedTriangles = new ArrayList<Triangle>();

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

                    transformedTriangles.add(new Triangle(vertex1, vertex2, vertex3));

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

                                    Color finalColor = phongModel(obj,
                                            obj.getMaterial().getColor(),
                                            norm,
                                            new Vector3D(x, y, depth));

                                    // image.setRGB(x, y, finalColor.getRGB());
                                    g2.setColor(finalColor); // g2.setColor(new Color(finalColor.getRed(),
                                                             // finalColor.getGreen(), finalColor.getBlue(), 50));
                                    g2.fillRect(x, y, 1, 1);
                                }
                            }
                        }
                    }
                }

                TriangleMesh transformed = new TriangleMesh(transformedTriangles, obj.getMaterial());
                transformedGroup.addMesh(transformed);
            }

            transformedGroup.setSelected(group.getSelected());
            transformedGroup.setValid(group.getValid());
            transformedGroup.setIdentifier(group.getIdentifier());
            tMeshGroups.add(transformedGroup);
        }

        // g2.setColor(scene.getConfiguration().getSelectionColor());
        // TODO: Do not remove
        // for (int y = 0; y < image.getHeight(); y++) {
        //     for (int x = 0; x < image.getWidth(); x++) {
        //         final int depth = y * image.getWidth() + x;
        //         if (idBuffer[depth] != null && scene.getMesh(idBuffer[depth]).getSelected()) {
        //             // Check if the id of the pixel is the same on top, left, right and bottom.
        //             // If not, it means that the pixel is on the edge of a mesh.
        //             // So we draw the pixel in order to create a boundary
        //             if (depth <= 0)
        //                 continue;
        //             if (depth + 1 > image.getWidth() * image.getHeight() - 1)
        //                 continue;

        //             int topPixelDepth = (y - 1) * image.getWidth() + x;
        //             int leftPixelDepth = y * image.getWidth() + (x - 1);
        //             int rightPixelDepth = y * image.getWidth() + (x + 1);
        //             int bottomPixelDepth = (y + 1) * image.getWidth() + x;

        //             if (topPixelDepth >= 0 && idBuffer[topPixelDepth] != idBuffer[depth]) {
        //                 g2.drawRect(x, y, 2, 2);
        //             }

        //             if (leftPixelDepth >= 0 && leftPixelDepth <= idBuffer.length
        //                     && idBuffer[leftPixelDepth] != idBuffer[depth]) {
        //                 g2.drawRect(x, y, 2, 2);
        //             }

        //             if (idBuffer[rightPixelDepth] != idBuffer[depth]) {
        //                 g2.drawRect(x, y, 2, 2);
        //             }

        //             if (bottomPixelDepth < idBuffer.length && idBuffer[bottomPixelDepth] != idBuffer[depth]) {
        //                 g2.drawRect(x, y, 2, 2);
        //             }
        //         }

        //         // if (idBuffer[depth] != idBuffer[depth - 1] || idBuffer[depth] !=
        //         // idBuffer[depth + 1]) {
        //         // g2.drawRect(x, y, 2, 2);
        //         // }
        //     }
        // }

        this.drawInvalidMeshBounding(g2);
        this.drawSelectedMeshBounding(g2);

        g2.dispose();

        return image;
    }

    private void drawSelectedMeshBounding(Graphics2D g2) {
        g2.setStroke(new BasicStroke((int) scene.getConfiguration().getSelectionStrokeWidth()));
        g2.setColor(scene.getConfiguration().getSelectionColor());

        for (TriangleMeshGroup obj : tMeshGroups) {
            if (obj.getSelected()) {
                Vector3D[] bounds = obj.getBounding();
                g2.drawRect((int) bounds[0].getX() - 6, (int) bounds[0].getY() - 6,
                        (int) obj.getWidth() + 12,
                        (int) obj.getHeight() + 12);
            }
        }
    }

    private void drawInvalidMeshBounding(Graphics2D g2) {
        g2.setStroke(new BasicStroke(scene.getConfiguration().getSelectionStrokeWidth()));

        int diff = 6 - scene.getConfiguration().getSelectionStrokeWidth();
        g2.setColor(new Color(255, 0, 0, 150));

        for (TriangleMeshGroup obj : tMeshGroups) {
            if (!obj.getValid()) {
                Vector3D[] bounds = obj.getBounding();
                int x = (int) bounds[0].getX() - diff;
                int y = (int) bounds[0].getY() - diff;
                int w = (int) obj.getWidth() + diff * 2;
                int h = (int) obj.getHeight() + diff * 2;

                g2.drawRect(x, y, w, h);
                g2.drawLine(x, y, x + w, y + h);
                g2.drawLine(x + w, y, x, y + h);

                g2.setColor(new Color(255, 0, 0, 75));
                g2.fillRect((int) bounds[0].getX() - diff, (int) bounds[0].getY() - diff,
                        (int) obj.getWidth() + diff * 2,
                        (int) obj.getHeight() + diff * 2);
            }
        }
    }

    private Color phongModel(TriangleMesh object, Color color, Vector3D norm, Vector3D pixelPoint) {
        // Calculate ambient light
        double ambientIntensity = scene.getLight().getAmbientIntensity() * object.getMaterial().getAmbient();

        int red = (int) Math.min(255, color.getRed() * ambientIntensity);
        int green = (int) Math.min(255, color.getGreen() * ambientIntensity);
        int blue = (int) Math.min(255, color.getBlue() * ambientIntensity);

        // Calculate diffuse light
        Light light = scene.getLight();
        Vector3D lightDir = light.getPosition().sub(pixelPoint).normalize();
        double dotProduct = Math.max(0, norm.dot(lightDir));
        double diffuseIntensity = object.getMaterial().getDiffuse();
        red += (int) (color.getRed() * dotProduct * diffuseIntensity);
        green += (int) (color.getGreen() * dotProduct * diffuseIntensity);
        blue += (int) (color.getBlue() * dotProduct * diffuseIntensity);

        // Calculate specular light
        Vector3D viewDir = scene.getCamera().getPosition().sub(pixelPoint).normalize();
        Vector3D reflectDir = norm.multiply(2).multiply(norm.dot(lightDir)).sub(lightDir)
                .normalize();
        double specularIntensity = object.getMaterial().getSpecular();
        double specularFactor = Math.pow(Math.max(0, reflectDir.dot(viewDir)), 32);
        red += (int) (color.getRed() * specularFactor * specularIntensity);
        green += (int) (color.getGreen() * specularFactor * specularIntensity);
        blue += (int) (color.getBlue() * specularFactor * specularIntensity);

        // Validate and constrain color values
        red = Math.max(0, Math.min(255, red));
        green = Math.max(0, Math.min(255, green));
        blue = Math.max(0, Math.min(255, blue));

        return new Color(red, green, blue);
    }

    public void draw(Graphics g, Dimension panelDimension) {
        BufferedImage canvasBuffer = rasterize(panelDimension);

        g.drawImage(canvasBuffer, 0, 0, null);
        drawCameraDetails(g, new Point(10, 20));
    }

    private void drawAxes(BufferedImage image, Matrix transform) {
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setStroke(new BasicStroke(scene.getConfiguration().getAxisStrokeWidth()));

        Vector3D origin = new Vector3D(0, 0, 0);
        Vector3D xAxis1 = new Vector3D(-500, 0, 0);
        Vector3D xAxis2 = new Vector3D(500, 0, 0);

        Vector3D yAxis1 = new Vector3D(0, -500, 0);
        Vector3D yAxis2 = new Vector3D(0, 500, 0);

        Vector3D zAxis1 = new Vector3D(0, 0, -500);
        Vector3D zAxis2 = new Vector3D(0, 0, 500);

        // Draw axis
        origin = origin.multiply(transform);

        xAxis1 = xAxis1.multiply(transform);
        xAxis2 = xAxis2.multiply(transform);
        yAxis1 = yAxis1.multiply(transform);
        yAxis2 = yAxis2.multiply(transform);
        zAxis1 = zAxis1.multiply(transform);
        zAxis2 = zAxis2.multiply(transform);

        g2.setColor(Color.RED);
        g2.drawLine((int) xAxis1.x, (int) xAxis1.y, (int) xAxis2.x, (int) xAxis2.y);
        g2.setColor(Color.GREEN);
        g2.drawLine((int) yAxis1.x, (int) yAxis1.y, (int) yAxis2.x, (int) yAxis2.y);
        g2.setColor(Color.BLUE);
        g2.drawLine((int) zAxis1.x, (int) zAxis1.y, (int) zAxis2.x, (int) zAxis2.y);
    }

    public void drawGridXY(BufferedImage image, Matrix transform) {
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setColor(scene.getConfiguration().getGridColor());
        g2.setStroke(new BasicStroke(scene.getConfiguration().getGridStrokeWidth()));

        int gridStep = scene.getConfiguration().getGridStep();
        int axisLength = gridStep * scene.getConfiguration().getStepCounts();

        for (int i = -axisLength; i <= axisLength; i += gridStep) {
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

    public void drawGridXZ(BufferedImage image, Matrix transform) {
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setColor(scene.getConfiguration().getGridColor());
        g2.setStroke(new BasicStroke(scene.getConfiguration().getGridStrokeWidth()));

        int gridStep = scene.getConfiguration().getGridStep();
        int axisLength = gridStep * scene.getConfiguration().getStepCounts();

        for (int i = -axisLength; i <= axisLength; i += gridStep) {
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

    public void drawGridYZ(BufferedImage image, Matrix transform) {
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setColor(scene.getConfiguration().getGridColor());
        g2.setStroke(new BasicStroke(scene.getConfiguration().getGridStrokeWidth()));

        int gridStep = scene.getConfiguration().getGridStep();
        int axisLength = gridStep * scene.getConfiguration().getStepCounts();

        for (int i = -axisLength; i <= axisLength; i += gridStep) {
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
        double cameraScale = scene.getCamera().scale;

        g.setColor(Color.WHITE);
        g.drawString(
                String.format("Camera Position: (%.2f, %.2f, %.2f)", cameraPosition.x, cameraPosition.y,
                        cameraPosition.z),
                position.x, position.y);
        g.drawString(String.format("Camera Direction: (%.2f, %.2f, %.2f)", cameraDirection.x, cameraDirection.y,
                cameraDirection.z), position.x, position.y + 20);
        g.drawString(String.format("Camera Scale: %.2f", cameraScale), position.x, position.y + 40);
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
        if (idBuffer[depth] != null) {
            return scene.getMesh(idBuffer[depth]);
        }
        return null;
    }

    public void deselectAllMeshes() {
        for (TriangleMeshGroup mesh : scene.getMeshes())
            mesh.setSelected(false);

    }
}
