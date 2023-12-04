package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene;

import java.awt.Color;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;

abstract class AbstractLight {
    protected Color color;
    protected double intensity = 0.5;

    public AbstractLight(Color color, double intensity) {
        this.color = color;
        this.intensity = intensity;
    }

    public Color getColor() {
        return color;
    }

    public double getIntensity() {
        return intensity;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
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

public class Light extends AbstractLight {
    private Vector3D position = new Vector3D(150, 250, 50);

    public Light() {
        this(new Vector3D(150, 250, 50), Color.YELLOW, 0.1);
    }

    public Light(Vector3D position, Color color, double intensity) {
        super(color, intensity);
        this.position = position;
    }

    public Vector3D getPosition() {
        return position;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setAmbientIntensity(double ambientIntensity) {
        if (ambientIntensity >= 0 && ambientIntensity <= 1) {
            this.setIntensity(ambientIntensity);
        }
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }
}
