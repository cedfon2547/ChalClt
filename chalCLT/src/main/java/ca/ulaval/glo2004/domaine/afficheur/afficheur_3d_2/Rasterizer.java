package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d_2;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d_2.base.Matrix;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d_2.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d_2.mesh.Triangle;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d_2.mesh.TriangleMesh;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d_2.scene.Light;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d_2.scene.Scene;

import com.google.common.collect.Lists;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class Rasterizer {
    private Scene scene;
    List<TriangleMesh> tMeshes = Lists.newArrayList();

    public Rasterizer() {
        this(new Scene());
    }

    public Rasterizer(Scene scene) {
        this.scene = scene;
    }

    public void clear(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

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

        g2.setColor(scene.getConfiguration().getBackgroundColor());
        g2.fillRect(0, 0, 10000, 10000);
    }

    private Color getPixelColor(TriangleMesh object, Color color, Vector3D norm, Vector3D pixelPoint) {
        if (object.getMaterial() == null) {
            return color;
        }

        Color objectColor = color != null ? color : object.getMaterial().getColor();

        double ka = object.getMaterial().getAmbient() + scene.getLight().getAmbientIntensity() + scene.getLight().getIntensity();
        double kd = object.getMaterial().getDiffuse();
        double ks = object.getMaterial().getSpecular();
        double n = object.getMaterial().getShininess();

        // Suppose we only have one light
        Light light = scene.getLight();

        // Blend the object color and the light color using linear interpolation
        Color blendedColor = new Color(
                (int) Light.Lerp(objectColor.getRed(), light.getColor().getRed(),
                        light.getBlenderFactor()),
                (int) Light.Lerp(objectColor.getGreen(), light.getColor().getGreen(),
                        light.getBlenderFactor()),
                (int) Light.Lerp(objectColor.getBlue(), light.getColor().getBlue(),
                        light.getBlenderFactor()),
                (int) objectColor.getAlpha());

        // Calculate the ambient component of the lighting model using the blended color
        Color ambientColor = new Color(
                (int) Math.min(255, (ka * blendedColor.getRed())),
                (int) Math.min(255, ka * blendedColor.getGreen()),
                (int) Math.min(255, ka * blendedColor.getBlue()),
                (int) objectColor.getAlpha());

        Vector3D lightPosition = light.getPosition();

        // The direction from the point to the light source
        Vector3D lightDir = lightPosition.sub(pixelPoint).normalize();
        // The distance from the point to the light source
        double lightDistance = lightDir.getNormLength();

        // Calculate the diffuse component of the lighting model using the blended color
        double diffuseFactor = Math.max(0, norm.dotProduct(lightDir));
        if (diffuseFactor < 0) {
            return ambientColor;
        }

        // Calculate the specular component of the lighting model using the Phong
        // reflection model
        // Vector3D viewDir = scene.getCamera().getPosition().sub(pixelPoint).normalize();
        // Vector3D reflectDir = norm.multiplyScalar(2 * norm.dotProduct(lightDir)).sub(lightDir).normalize();
        double specularFactor = 0; // Math.pow(Math.max(0, viewDir.dotProduct(reflectDir)), n);
        if (specularFactor < 0) {
            specularFactor = 0;
        }

        // Color specularColor = new Color(
        //         (int) (ks * specularFactor * blendedColor.getRed()),
        //         (int) (ks * specularFactor * blendedColor.getGreen()),
        //         (int) (ks * specularFactor * blendedColor.getBlue()),
        //         (int) (ks * ks));

        // Calculate the attenuation based on the distance to the light source
        double attenuation = 1 / (1 + 0.5 * lightDistance + 0.01 * lightDistance * lightDistance);

        // Modify the diffuse and specular calculations to include the attenuation
        Color diffuseColor = new Color(
                (int) Math.min(255, (kd * diffuseFactor * blendedColor.getRed() * attenuation)),
                (int) Math.min(255, (kd * diffuseFactor * blendedColor.getGreen() * attenuation)),
                (int) Math.min(255, kd * diffuseFactor * blendedColor.getBlue() * attenuation),
                (int) objectColor.getAlpha());

        Color specularColor = new Color(
                (int) (ks * specularFactor * blendedColor.getRed() * attenuation),
                (int) (ks * specularFactor * blendedColor.getGreen() * attenuation),
                (int) (ks * specularFactor * blendedColor.getBlue() * attenuation),
                (int) (ks * ks));

        // Calculate the final color
        // Add up the ambient, diffuse and specular components to get the final color

        int finalRed = (int) Math.min(255,
                ambientColor.getRed() + diffuseColor.getRed() + specularColor.getRed());
        int finalGreen = (int) Math.min(255,
                ambientColor.getGreen() + diffuseColor.getGreen() + specularColor.getGreen());
        int finalBlue = (int) Math.min(255,
                ambientColor.getBlue() + diffuseColor.getBlue() + specularColor.getBlue());
        int finalAlpha = (int) Math.min(255,
                ambientColor.getAlpha() + diffuseColor.getAlpha() + specularColor.getAlpha());

        Color finalColor = new Color(finalRed, finalGreen, finalBlue, finalAlpha);

        return finalColor;
    }

    private BufferedImage rasterize(Dimension panelDimension) {
        BufferedImage canvasBuffer = new BufferedImage((int) panelDimension.getWidth(),
                (int) panelDimension.getHeight(),
                BufferedImage.TYPE_4BYTE_ABGR);
        double[] zBuffer = new double[canvasBuffer.getWidth() * canvasBuffer.getHeight()];
        Arrays.fill(zBuffer, Double.NEGATIVE_INFINITY);

        this.tMeshes.clear();

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

        
        if (scene.getConfiguration().getShowAxis()) {
            this.drawAxes(canvasBuffer, transform);
        }

        for (TriangleMesh obj : this.scene.getMeshes()) {
            List<Triangle> transformedTriangles = new ArrayList<Triangle>();
            for (Triangle triangle : obj.getTriangles()) {
                Vector3D[] vertices = triangle.getVertices();

                Vector3D vertex1 = new Vector3D(vertices[0]);
                Vector3D vertex2 = new Vector3D(vertices[1]);
                Vector3D vertex3 = new Vector3D(vertices[2]);

                vertex1 = vertex1.multiplyMatrix(transform);
                vertex2 = vertex2.multiplyMatrix(transform);
                vertex3 = vertex3.multiplyMatrix(transform);

                int minX = (int) Math.max(0,
                        Math.ceil(Math.min(vertex1.x, Math.min(vertex2.x, vertex3.x))));
                int maxX = (int) Math.min(canvasBuffer.getWidth() - 1,
                        Math.floor(Math.max(vertex1.x, Math.max(vertex2.x, vertex3.x))));
                int minY = (int) Math.max(0,
                        Math.ceil(Math.min(vertex1.y, Math.min(vertex2.y, vertex3.y))));
                int maxY = (int) Math.min(canvasBuffer.getHeight() - 1,
                        Math.floor(Math.max(vertex1.y, Math.max(vertex2.y, vertex3.y))));

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

                        boolean isInside = b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0
                                && b3 < 1;

                        // Check if the scene ground intersect with the triangle

                        if (isInside) {
                            double depth = b1 * (vertex1.z
                                    - scene.getCamera().getPosition().z)
                                    + b2 * (vertex2.z - scene.getCamera()
                                            .getPosition().z)
                                    + b3 * (vertex3.z - scene.getCamera()
                                            .getPosition().z);
                            int zIndex = y * canvasBuffer.getWidth() + x;

                            if (zBuffer[zIndex] < depth) {
                                zBuffer[zIndex] = depth;

                                Color finalColor = getPixelColor2(obj,
                                        obj.getMaterial().getColor(), norm,
                                        new Vector3D(x, y, depth));

                                canvasBuffer.setRGB(x, y, finalColor.getRGB());

                            }
                        }
                    }
                }
            }

            TriangleMesh transformed = new TriangleMesh(transformedTriangles);
            transformed.ID = obj.ID;
            tMeshes.add(transformed);
        }

        return canvasBuffer;
    }

    private Color getPixelColor2(TriangleMesh object, Color color, Vector3D norm, Vector3D pixelPoint) {
        // Calculate ambient light
        double ambientIntensity = scene.getLight().getAmbientIntensity();

        int red = (int) Math.min(255, color.getRed() * ambientIntensity);
        int green = (int) Math.min(255, color.getGreen() * ambientIntensity);
        int blue = (int) Math.min(255, color.getBlue() * ambientIntensity);

        // Calculate diffuse light
        Light light = scene.getLight();
        Vector3D lightDir = light.getPosition().sub(pixelPoint).normalize();
        double dotProduct = Math.max(0, norm.dotProduct(lightDir));
        double diffuseIntensity = 0.8;
        red += (int) (color.getRed() * dotProduct * diffuseIntensity);
        green += (int) (color.getGreen() * dotProduct * diffuseIntensity);
        blue += (int) (color.getBlue() * dotProduct * diffuseIntensity);

        // Calculate specular light
        Vector3D viewDir = scene.getCamera().getPosition().sub(pixelPoint).normalize();
        Vector3D reflectDir = norm.multiplyScalar(2).multiplyScalar(norm.dotProduct(lightDir)).sub(lightDir)
                .normalize();
        double specularIntensity = 0.5;
        double specularFactor = Math.pow(Math.max(0, reflectDir.dotProduct(viewDir)), 32);
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
        this.clear(g);

        BufferedImage canvasBuffer = rasterize(panelDimension);
        drawCameraDetails(g, new Point(10, 20));
        g.drawImage(canvasBuffer, 0, 0, null);
    }

    private void drawAxes(BufferedImage image, Matrix transform) {
        Graphics2D g2 = (Graphics2D) image.getGraphics();

        Vector3D origin = new Vector3D(0, 0, 0);
        Vector3D xAxis1 = new Vector3D(-500, 0, 0);
        Vector3D xAxis2 = new Vector3D(500, 0, 0);

        Vector3D yAxis1 = new Vector3D(0, -500, 0);
        Vector3D yAxis2 = new Vector3D(0, 500, 0);

        Vector3D zAxis1 = new Vector3D(0, 0, -500);
        Vector3D zAxis2 = new Vector3D(0, 0, 500);

        // Draw axis
        origin = origin.multiplyMatrix(transform);

        xAxis1 = xAxis1.multiplyMatrix(transform);
        xAxis2 = xAxis2.multiplyMatrix(transform);
        yAxis1 = yAxis1.multiplyMatrix(transform);
        yAxis2 = yAxis2.multiplyMatrix(transform);
        zAxis1 = zAxis1.multiplyMatrix(transform);
        zAxis2 = zAxis2.multiplyMatrix(transform);

        g2.setColor(Color.RED);
        g2.drawLine((int) xAxis1.x, (int) xAxis1.y, (int) xAxis2.x, (int) xAxis2.y);
        g2.setColor(Color.GREEN);
        g2.drawLine((int) yAxis1.x, (int) yAxis1.y, (int) yAxis2.x, (int) yAxis2.y);
        g2.setColor(Color.BLUE);
        g2.drawLine((int) zAxis1.x, (int) zAxis1.y, (int) zAxis2.x, (int) zAxis2.y);
    }

    public void drawCameraDetails(Graphics g, Point position) {
        Vector3D cameraPosition = scene.getCamera().getPosition();
        Vector3D cameraDirection = scene.getCamera().getDirection();
        double cameraScale = scene.getCamera().scale;

        g.setColor(Color.WHITE);
        g.drawString(
                String.format("Camera Position: (%s, %s, %s)", cameraPosition.x, cameraPosition.y,
                        cameraPosition.z),
                position.x, position.y);
        g.drawString(String.format("Camera Direction: (%s, %s, %s)", cameraDirection.x, cameraDirection.y,
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
}
