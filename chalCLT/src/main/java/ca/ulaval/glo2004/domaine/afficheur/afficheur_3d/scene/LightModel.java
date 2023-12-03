package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.Material;
import java.awt.Color;

public class LightModel {
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

    public static class PhongLightModel extends LightModel {
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
            Vector3D lightPos = light.getPosition(); // camera.getPosition(); //.add(new Vector3D(0, 0, 0));
                                                     // //light.getPosition();
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
            double specularFactor = reflectDir.dot(viewDir); // Compute it without using the power operation to avoid
                                                             // using
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

}
