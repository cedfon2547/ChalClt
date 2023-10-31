package ca.ulaval.glo2004.domaine.utils;

import java.util.regex.Pattern;

/**
 * La classe ImperialDimension représente une dimension impériale, c'est-à-dire
 * une mesure de longueur utilisée dans le système impérial britannique.
 * Cette classe permet de manipuler des dimensions impériales en pieds, pouces
 * et fraction de pouces.
 * 
 * Les dimensions impériales sont représentées sous la forme suivantes :
 * - pieds : le nombre de pieds dans la dimension
 * - pouces : le nombre de pouces dans la dimension
 * - numerateur : le numérateur de la fraction de pouces dans la dimension
 * - denominateur : le dénominateur de la fraction de pouces dans la dimension
 * 
 * Permet de manipuler des dimensions impériales (addition, soustraction,
 * multiplication, division).
 * 
 * TODO: N'a pas été testé à 100%. Il est possible qu'il y ait des bugs. Il va
 * être important de bien tester cette classe afin d'éviter que des erreurs se
 * propagent dans la suite de nos calculs.
 */
public class ImperialDimension {

    /**
     * Le nombre de pieds dans la dimension.
     */
    private int pieds;

    /**
     * Le nombre de pouces dans la dimension.
     */
    private int pouces;

    /**
     * Le numérateur de la fraction de pouces dans la dimension.
     */
    private int numerateur;

    /**
     * Le dénominateur de la fraction de pouces dans la dimension.
     */
    private int denominateur;

    /**
     * Constructeur de la classe ImperialDimension.
     * 
     * @param pieds        le nombre de pieds dans la dimension
     * @param pouces       le nombre de pouces dans la dimension
     * @param numerateur   le numérateur de la fraction de pouces dans la dimension
     * @param denominateur le dénominateur de la fraction de pouces dans la
     *                     dimension
     */
    public ImperialDimension(int pieds, int pouces, int numerateur, int denominateur) {
        this.setPieds(pieds);
        this.setPouces(pouces);
        this.setDenominateur(denominateur);
        this.setNumerateur(numerateur);
    }

    /**
     * Retourne le nombre de pieds dans la dimension.
     * 
     * @return le nombre de pieds dans la dimension
     */
    public int getPieds() {
        return pieds;
    }

    /**
     * Retourne le nombre de pouces dans la dimension.
     * 
     * @return le nombre de pouces dans la dimension
     */
    public int getPouces() {
        return pouces;
    }

    /**
     * Retourne le numérateur de la fraction de pouces dans la dimension.
     * 
     * @return le numérateur de la fraction de pouces dans la dimension
     */
    public int getNumerateur() {
        return numerateur;
    }

    /**
     * Retourne le dénominateur de la fraction de pouces dans la dimension.
     * 
     * @return le dénominateur de la fraction de pouces dans la dimension
     */
    public int getDenominateur() {
        return denominateur;
    }

    /**
     * Modifie le nombre de pieds dans la dimension.
     * 
     * @param pieds le nouveau nombre de pieds dans la dimension
     */
    public void setPieds(int pieds) {
        this.pieds = pieds;
    }

    /**
     * Modifie le nombre de pouces dans la dimension. Si le nombre de pouces est
     * supérieur à 12, le nombre de pieds est ajusté en conséquence.
     * 
     * @param pouces le nouveau nombre de pouces dans la dimension
     */
    public void setPouces(int pouces) {
        // Considérer le cas où pouces est supérieur à 12.
        this.pouces = pouces % 12;
        this.pieds += Math.floor(pouces / 12);
    }

    /**
     * Modifie le numérateur de la fraction de pouces dans la dimension. Si le
     * numérateur est supérieur ou égal au dénominateur, le nombre de pouces est
     * ajusté en conséquence.
     * 
     * @param numerateur le nouveau numérateur de la fraction de pouces dans la
     *                   dimension
     */
    public void setNumerateur(int numerateur) {
        // Considérer le cas où numerateur est supérieur au dénominateur.
        this.numerateur = numerateur % denominateur;
        this.pouces += Math.floor(numerateur / denominateur);
        this.setPouces(this.pouces);
    }

    /**
     * Modifie le dénominateur de la fraction de pouces dans la dimension. Si le
     * dénominateur est égal à 0, il est remplacé par 1. Si le numérateur est
     * supérieur ou égal au dénominateur, le nombre de pouces est ajusté en
     * conséquence.
     * 
     * @param denominateur le nouveau dénominateur de la fraction de pouces dans la
     *                     dimension
     */
    public void setDenominateur(int denominateur) {
        // Considérer le cas où denominateur est égal à 0.
        this.denominateur = denominateur == 0 ? 1 : denominateur;
        this.setNumerateur(this.numerateur);
    }

    /**
     * Effectue l'addition de deux dimensions impériales.
     * 
     * @param other la dimension impériale à ajouter à la dimension courante
     * @return une nouvelle dimension impériale représentant la somme des deux
     *         dimensions
     */
    public ImperialDimension addition(ImperialDimension other) {
        int pieds = this.getPieds() + other.getPieds();
        int pouces = this.getPouces() + other.getPouces();
        int numerateur = this.getNumerateur() * other.denominateur + other.getNumerateur() * this.getDenominateur();
        int denominateur = this.getDenominateur() * other.getDenominateur();

        return format(pieds, pouces, numerateur, denominateur);
    }

    /**
     * Effectue la soustraction de deux dimensions impériales.
     * 
     * @param other la dimension impériale à soustraire de la dimension courante
     * @return une nouvelle dimension impériale représentant la différence des deux
     *         dimensions
     */
    public ImperialDimension soustraction(ImperialDimension other) {
        int pieds = this.getPieds() - other.getPieds();
        int pouces = this.getPouces() - other.getPouces();
        int numerateur = this.getNumerateur() * other.denominateur - other.getNumerateur() * this.getDenominateur();
        int denominateur = this.getDenominateur() * other.getDenominateur();

        return format(pieds, pouces, numerateur, denominateur);
    }

    /**
     * Effectue la multiplication de deux dimensions impériales.
     * 
     * @param other la dimension impériale à multiplier à la dimension courante
     * @return une nouvelle dimension impériale représentant le produit des deux
     *         dimensions
     */
    public ImperialDimension multiplication(ImperialDimension other) {
        int pied = this.getPieds() * other.getPieds();
        int pouces = this.getPouces() * other.getPouces();
        int numerateur = this.getNumerateur() * other.getNumerateur();
        int denominateur = this.getDenominateur() * other.getDenominateur();

        return format(pied, pouces, numerateur, denominateur);
    }

    /**
     * Effectue la division de deux dimensions impériales.
     * 
     * @param value la dimension impériale à diviser à la dimension courante
     * @return une nouvelle dimension impériale représentant le quotient des deux
     *         dimensions
     */
    public ImperialDimension division(ImperialDimension value) {
        int pied = this.getPieds() / value.getPieds();
        int pouces = this.getPouces() / value.getPouces();
        int numerateur = this.getNumerateur() / value.getNumerateur();
        int denominateur = this.getDenominateur() / value.getDenominateur();

        return format(pied, pouces, numerateur, denominateur);
    }

    /**
     * Convertit la dimension impériale en pouces.
     * 
     * @return la dimension impériale convertie en pouces
     */
    public double toInches() {
        return convertToInches(this);
    }

    /**
     * Retourne une chaîne de caractères représentant la dimension impériale.
     * 
     * @return une chaîne de caractères représentant la dimension impériale
     */
    public String toString() {
        return this.pieds + "' " + this.pouces + "\" " + this.numerateur + "/" + this.denominateur;
    }

    /**
     * Crée une nouvelle dimension impériale à partir d'une longueur en pouces.
     * 
     * @param lengthInInches la longueur en pouces à convertir en dimension
     *                       impériale
     * @return une nouvelle dimension impériale représentant la longueur en pouces
     */
    public static ImperialDimension format(double lengthInInches) {
        return format(convertToImperial(lengthInInches));

    }

    /**
     * Crée une nouvelle dimension impériale à partir d'un nombre de pieds, de
     * pouces, d'un numérateur et d'un dénominateur.
     * 
     * @param pieds        le nombre de pieds dans la dimension
     * @param pouces       le nombre de pouces dans la dimension
     * @param numerateur   le numérateur de la fraction de pouces dans la dimension
     * @param denominateur le dénominateur de la fraction de pouces dans la
     *                     dimension
     * @return une nouvelle dimension impériale représentant les informations
     *         données
     */
    public static ImperialDimension format(int pieds, int pouces, int numerateur, int denominateur) {
        return format(new ImperialDimension(pieds, pouces, numerateur, denominateur));
    }

    /**
     * Crée une nouvelle dimension impériale à partir d'une dimension impériale
     * donnée. Cette méthode permet de simplifier la dimension impériale en ajustant
     * le nombre de pouces et la fraction de pouces si nécessaire.
     * 
     * @param dim la dimension impériale à formater
     * @return une nouvelle dimension impériale représentant la dimension impériale
     *         donnée, simplifiée si nécessaire
     */
    public static ImperialDimension format(ImperialDimension dim) {
        int pieds = dim.getPieds();
        int pouces = dim.getPouces();
        int numerateur = dim.getNumerateur();
        int denominateur = dim.getDenominateur();

        if (denominateur == 0) {
            denominateur = 1;
        }

        if (numerateur >= denominateur) {
            pouces += numerateur / denominateur;
            numerateur = numerateur % denominateur;
        }

        // Simplifier la fraction
        int pgcd = getGCD(numerateur, denominateur);
        numerateur /= pgcd;
        denominateur /= pgcd;

        if (pouces >= 12) {
            pieds += pouces / 12;
            pouces = pouces % 12;
        }

        return new ImperialDimension(pieds, pouces, numerateur, denominateur);
    }

    /**
     * Convertit une longueur en pouces en une dimension impériale.
     * 
     * @param lengthInInches la longueur en pouces à convertir
     * @return une nouvelle dimension impériale représentant la longueur en pouces
     */
    public static ImperialDimension convertToImperial(double lengthInInches) {
        int pieds = (int) (lengthInInches / 12);
        int pouces = (int) (lengthInInches % 12);
        int numerateur = (int) ((lengthInInches - pieds * 12 - pouces) * 16);
        int denominateur = 16;
        int pgcd = getGCD(numerateur, denominateur);
        numerateur /= pgcd;
        denominateur /= pgcd;

        return format(pieds, pouces, numerateur, denominateur);
    }

    /**
     * Convertit une dimension impériale en pouces.
     * 
     * @param imperial la dimension impériale à convertir
     * @return la dimension impériale convertie en pouces
     */
    public static double convertToInches(ImperialDimension imperial) {
        int piedsInInches = imperial.getPieds() * 12;
        int inches = imperial.getPouces();
        int numerateur = imperial.getNumerateur();
        int denominateur = imperial.getDenominateur();

        // Calculer la longueur totale en pouces
        double lengthInInches = piedsInInches + inches + (double) numerateur / denominateur;

        return lengthInInches;
    }

    /**
     * Calcule le PGCD (le plus grand commun diviseur) de deux nombres entiers.
     * 
     * @param a le premier nombre entier
     * @param b le deuxième nombre entier
     * @return le PGCD des deux nombres entiers
     */
    private static int getGCD(int a, int b) {
        if (b == 0) {
            return a;
        }
        return getGCD(b, a % b);
    }

    /**
     * Convertit une chaîne de caractères représentant une dimension impériale en
     * une instance de la classe ImperialDimension.
     * 
     * @param str La chaîne de caractères à convertir.
     * @return L'instance de la classe ImperialDimension correspondant à la chaîne
     *         de caractères, ou null si la chaîne n'est pas au bon format.
     */
    public static ImperialDimension parseFromString(String str) {
        // Format is: 7' 3" 1/2
        Pattern pattern = Pattern.compile("([0-9]+)\' +([0-9]+)\" +([0-9]+)/([0-9]+)");
        java.util.regex.Matcher matcher = pattern.matcher(str);

        if (!matcher.find()) {
            return null;
        }

        int pieds = Integer.parseInt(matcher.group(1));
        int pouces = Integer.parseInt(matcher.group(2));
        int numerateur = Integer.parseInt(matcher.group(3));
        int denominateur = Integer.parseInt(matcher.group(4));

        return format(pieds, pouces, numerateur, denominateur);
    }

    public static void main(String[] args) {

    }
}
