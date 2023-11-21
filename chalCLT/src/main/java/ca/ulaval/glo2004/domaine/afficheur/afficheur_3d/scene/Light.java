package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene;
import java.awt.Color;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;

public class Light {
    private double intensity = 1;
    private double ambientIntensity = 0.5;
    private Vector3D position = new Vector3D(150, 250, 50);
    private Vector3D direction = new Vector3D(150, 150, -100);
    private Color color = Color.WHITE;
    private double blenderFactor = 0.1;

    public Light() {

    }

    public Light(Vector3D position, Color color, double intensity) {
        this.position = position;
        this.color = color;
        this.intensity = intensity;
    }

    public Color getColor() {
        return color;
    }

    public double getIntensity() {
        return intensity;
    }

    public double getAmbientIntensity() {
        return ambientIntensity;
    }

    public Vector3D getPosition() {
        return position;
    }

    public Vector3D getDirection() {
        return direction;
    }

    public double getBlenderFactor() {
        return blenderFactor;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setIntensity(double intensity) {
        if (intensity >= 0 && intensity <= 1) {
        }
        this.intensity = intensity;
    }

    public void setAmbientIntensity(double ambientIntensity) {
        if (ambientIntensity >= 0 && ambientIntensity <= 1) {
            this.ambientIntensity = ambientIntensity;
        }
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public void setBlenderFactor(double blenderFactor) {
        if (blenderFactor >= 0 && blenderFactor <= 1) {
            this.blenderFactor = blenderFactor;
        }
    }

    public void setDirection(Vector3D direction) {
        this.direction = direction;
    }

    public static double Lerp(double a, double b, double t) {
        // Check if t is within the range [0, 1]
        if (t < 0 || t > 1) {
            throw new IllegalArgumentException("t must be between 0 and 1");
        }
        // Return the interpolated value
        return a + (b - a) * t;
    }

    
}
