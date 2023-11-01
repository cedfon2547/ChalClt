package ca.ulaval.glo2004.domaine;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



/**
 * La classe Mur représente un mur du chalet. Un mur a un type, une hauteur, une largeur et une liste d'accessoires.
 */
public class Mur {

    /**
     * Le type de mur.
     */
    private final TypeMur type;

    /**
     * La liste des accessoires du mur.
     */
    private List<Accessoire> accessoires = new ArrayList<Accessoire>();

    /**
     * Les dimensions du mur (hauteur et largeur).
     */
    private double[] dimensions = new double[2];


    /**
     * Constructeur de la classe Mur.
     *
     * @param type    Le type de mur.
     * @param hauteur La hauteur du mur.
     * @param largeur La largeur du mur.
     */
    public Mur(TypeMur type, double hauteur, double largeur) {
        this.type = type;
        this.dimensions = new double[]{hauteur, largeur};
    }

    /**
     * Constructeur de la classe Mur.
     *
     * @param type        Le type de mur.
     * @param hauteur     La hauteur du mur.
     * @param largeur     La largeur du mur.
     * @param accessoires La liste des accessoires du mur.
     */
    public Mur(TypeMur type, double hauteur, double largeur, List<Accessoire> accessoires) {
        this(type, hauteur, largeur);
        this.accessoires = accessoires;
    }

    /**
     * Retourne le type de mur.
     *
     * @return Le type de mur.
     */
    public TypeMur getType() {
        return type;
    }

    /**
     * Retourne la hauteur du mur.
     *
     * @return La hauteur du mur.
     */
    public double getHauteur() {
        return dimensions[0];
    }

    /**
     * Retourne la largeur du mur.
     *
     * @return La largeur du mur.
     */
    public double getLargeur() {
        return dimensions[1];
    }

    /**
     * Retourne la liste des accessoires du mur.
     *
     * @return La liste des accessoires du mur.
     */
    public List<Accessoire> getAccessoires() {
        return accessoires;
    }

    /**
     * Modifie la hauteur du mur.
     *
     * @param hauteur La nouvelle hauteur du mur.
     */
    public void setHauteur(double hauteur) {
        dimensions[0] = hauteur;
    }

    /**
     * Modifie la largeur du mur.
     *
     * @param largeur La nouvelle largeur du mur.
     */
    public void setLargeur(double largeur) {
        dimensions[1] = largeur;
    }

    /**
     * Modifie la liste des accessoires du mur.
     *
     * @param accessoires La nouvelle liste des accessoires du mur.
     */
    public void setAccessoires(List<Accessoire> accessoires) {
        this.accessoires = accessoires;
    }

    /**
     * Convertit le mur en un objet de transfert de données (DTO).
     *
     * @return Le DTO correspondant au mur.
     */
    public MurDTO toDTO() {
        return new MurDTO(this);
    }

    /**
     * La classe MurDTO représente un objet de transfert de données (DTO) pour un mur.
     */
    public static class MurDTO implements java.io.Serializable {

        /**
         * Le type de mur.
         */
        public TypeMur type;

        /**
         * La hauteur du mur.
         */
        public double hauteur;

        /**
         * La largeur du mur.
         */
        public double largeur;

        /**
         * La liste des accessoires du mur.
         */
        public List<Accessoire.AccessoireDTO> accessoires;

        /**
         * Constructeur de la classe MurDTO.
         *
         * @param mur Le mur à partir duquel créer le DTO.
         */
        public MurDTO(Mur mur) {
            this.type = mur.type;
            this.hauteur = mur.getHauteur();
            this.largeur = mur.getLargeur();
            this.accessoires = new ArrayList<Accessoire.AccessoireDTO>();
            for (Accessoire accessoire : mur.getAccessoires()) {
                this.accessoires.add(accessoire.toDTO());
            }
        }
    }

    /**
     * Convertit un objet de transfert de données (DTO) en un mur.
     *
     * @param dto Le DTO à partir duquel créer le mur.
     * @return Le mur correspondant au DTO.
     */
    public static Mur fromDTO(MurDTO dto) {
        List<Accessoire> accessoires = new ArrayList<Accessoire>();
        for (Accessoire.AccessoireDTO accessoireDTO : dto.accessoires) {
            accessoires.add(Accessoire.fromDTO(accessoireDTO));
        }
        return new Mur(dto.type, dto.hauteur, dto.largeur, accessoires);
    }

    /**
     * Ajoute un accessoire
     *
     * @param p_type      qui représente le type de l'accessoire
     * @param p_position  qui représente la pôsition de l'objet
     * @param p_dimension qui représente la dimensions de l'objet
     * @return
     */
    public void AjouterAcessoire(TypeAccessoire p_type, double[] p_position, double[] p_dimension) {

        // Test de depart juste pour double verification
        if (p_position.length != 3) {
            throw new IllegalArgumentException("mauvais nombre de position");
        }
        if (p_dimension.length != 3) {
            throw new IllegalArgumentException("mauvais nombre de dimension");
        }
        for (int i = 0; i < 3; i++) {
            if (p_position[i] < 0) {
                throw new IllegalArgumentException("position negative");
            }
            if (p_dimension[i] <= 0) {
                throw new IllegalArgumentException("dimensions negative ou 0");
            }
        }// fin du test
        Accessoire p_accessoire = new Accessoire(p_type, p_position, p_dimension);
        accessoires.add(p_accessoire);
    }


    /**
     * Retire un accessoire
     *
     * @param uuid DTO à partir duquel créer le mur.
     * @return Le mur correspondant au DTO.
     */
    public void retirerAccessoire(UUID uuid) {
        for(Accessoire accessoire: accessoires){
            if (accessoire.getAccessoireId().equals(uuid)) {
                accessoires.remove(accessoire);
                return;
            }
        }
        throw new IllegalAccessError("Ne contient pas cet accessoire");
    }

    /**
     *
     * @param p_position de l'objet qu'on veut verifier
     * @param p_dimension de l'objet qu'on veut verifier
     * @return boolean qui represente sa validite
     */
    public boolean VerifierCollisionAcc(double[] p_position, double[] p_dimension){

        Rectangle accrec = new Rectangle((int)p_position[0], (int)p_position[1], (int)p_dimension[0], (int)p_dimension[1]);
        for (Accessoire accessoire: accessoires){
            Rectangle inters = new Rectangle((int)accessoire.getPosition()[0], (int)accessoire.getPosition()[1],(int)accessoire.getDimensions()[0], (int)accessoire.getDimensions()[1]);
            boolean intersect = accrec.intersects(inters);
            if (intersect) return true;
        }
        return false;
    }


}