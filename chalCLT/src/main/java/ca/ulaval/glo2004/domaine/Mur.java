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
     * Retourne un accessoire spécifique du mur.
     * 
     * @param accessoireUuid le UUID de l'accessoire recherché.
     * @return L'accessoire recherché.
     */
    public Accessoire getAccessoire(UUID accessoireUuid){
        
        List<Accessoire> listAccessoires = this.getAccessoires();
        if(listAccessoires == null){
            throw new IllegalArgumentException("pas d'accessoire");
        }
        for(Accessoire accessoire : accessoires ){
            if (accessoire.getAccessoireId() == accessoireUuid){
                return accessoire;
            }
        }
        throw new IllegalArgumentException("L'accessoire n'est pas dans ce mur");
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
    public Accessoire AjouterAcessoire(TypeAccessoire p_type, double[] p_position, double[] p_dimension) {

        // Test de depart juste pour double verification
        if (p_position.length != 2) {
            throw new IllegalArgumentException("mauvais nombre de position");
        }
        if (p_dimension.length != 2) {
            throw new IllegalArgumentException("mauvais nombre de dimension");
        }
        for (int i = 0; i < 2; i++) {
            if (p_position[i] < 0) {
                throw new IllegalArgumentException("position negative");
            }

            if (p_dimension[i] < 0) {
                throw new IllegalArgumentException("dimensions negative ou 0");
            }
        }// fin du test
        Accessoire p_accessoire = new Accessoire(p_type,this.type, p_position, p_dimension);
        accessoires.add(p_accessoire);

        return p_accessoire;
    }


    /**
     * Retire un accessoire
     *
     * @param uuid DTO à partir duquel créer le mur.
     * @return Le mur correspondant au DTO.
     */
    public Accessoire retirerAccessoire(UUID uuid) {
        for(Accessoire accessoire: accessoires){
            if (accessoire.getAccessoireId().equals(uuid)) {
                accessoires.remove(accessoire);
                 return accessoire;
            }
        }
        throw new IllegalAccessError("Ne contient pas cet accessoire");
    }

    /**
     *Permet de voir la validité de l'accessoire par rapport au autre accessoire
     * @param p_position de l'objet qu'on veut verifier
     * @param p_dimension de l'objet qu'on veut verifier
     * @return boolean qui represente sa validite
     */
    public boolean verifierCollisionAcc(double[] p_position, double[] p_dimension, double margeacc){

        Rectangle accrec = new Rectangle((int)(p_position[0]-margeacc), (int)(p_position[1]-margeacc), (int)(p_dimension[0] + 2*margeacc), (int)(p_dimension[1]+ 2*margeacc));
        for (Accessoire accessoire: accessoires){
            Rectangle inters = new Rectangle((int)accessoire.getPosition()[0], (int)accessoire.getPosition()[1],(int)accessoire.getDimensions()[0], (int)accessoire.getDimensions()[1]);
            boolean intersect = accrec.intersects(inters);
            if (intersect) return true;
        }
        return false;
    }

    /**
     * Meme chose que verifierCollisionAcc mais avec une marge de 0 si on ne le passe pas en parametre
     * @param p_position
     * @param p_dimension
     * @return
     */
    public boolean verifierCollisionAcc(double[] p_position, double[] p_dimension){
        return this.verifierCollisionAcc(p_position, p_dimension, 0);
    }

    /**
     *Permet de voir si l'accessoire est dans une position valide par rapport au mur et sa marge
     * @param p_position position de l'accessoire
     * @param p_dimension dimensions de l'accessoire
     * @param margeminimal marge minimale du projet
     * @return la validité
     */
    public boolean VerifierMargeAcc(double[] p_position, double[] p_dimension, double margeminimal){
        return (p_position[0] - margeminimal < 0 && p_dimension[0] + p_position[0] + margeminimal > this.getLargeur()) && (p_position[1] - margeminimal < 0 && p_dimension[1] + p_position[1] + margeminimal > this.getHauteur());
    }
    /* pour affichage si jamais
    public static double[] getPosition(TypeMur type, double hauteur, double largeur, double longeur){
        switch(type){
            // Les positions sont relatif sont au chalet sans les margess
            case Droit:
                return new double[]{largeur, hauteur, 0};
            case Gauche:
                return new double[]{0, hauteur, longeur};
            case Facade:
                return new double[]{0,hauteur,0};
            case Arriere:
                return new double[]{largeur, hauteur, longeur};
            default:
                throw new IllegalArgumentException("Type de mur invalide");
        }
    }*/


}