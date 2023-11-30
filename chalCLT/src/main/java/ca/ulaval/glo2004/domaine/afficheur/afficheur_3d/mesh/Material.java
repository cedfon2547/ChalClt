package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh;
import java.awt.Color;

public class Material {
    private Color color = Color.ORANGE;
    private double ambient = 0.25;
    private double diffuse = 0.25;
    private double specular = 0.25;
    private double shininess = 10;

    public Material() {
    }

    public Material(Color color, double ambient, double diffuse, double specular, double shininess) {
        this.color = color;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }

    public Color getColor() {
        return color;
    }

    public double getAmbient() {
        return ambient;
    }

    public double getDiffuse() {
        return diffuse;
    }

    public double getSpecular() {
        return specular;
    }

    public double getShininess() {
        return shininess;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setAmbient(double ambient) {
        this.ambient = ambient;
    }

    public void setDiffuse(double diffuse) {
        this.diffuse = diffuse;
    }

    public void setSpecular(double specular) {
        this.specular = specular;
    }

    public void setShininess(double shininess) {
        this.shininess = shininess;
    }

    public Material copy() {
        return new Material(color, ambient, diffuse, specular, shininess);
    }    
}
