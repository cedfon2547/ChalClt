package ca.ulaval.glo2004.domaine;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * La classe Mur représente un mur du chalet. Un mur a un type, une hauteur, une
 * largeur et une liste d'accessoires.
 */
public class Mur {
    /**
     * Le type de mur.
     */
    private final TypeMur type;
    private final Chalet chalet;

    /**
     * La liste des accessoires du mur.
     */
    private List<Accessoire> accessoires = new ArrayList<Accessoire>();

    /**
     * Les dimensions du mur (hauteur et largeur).
     */
    // private double[] dimensions = new double[2];

    /**
     * Constructeur de la classe Mur.
     *
     * @param type    Le type de mur.
     * @param hauteur La hauteur du mur.
     * @param largeur La largeur du mur.
     */
    public Mur(TypeMur type, Chalet chalet) {
        this.type = type;
        this.chalet = chalet;
        // this.dimensions = new double[] { hauteur, largeur };
    }

    /**
     * Constructeur de la classe Mur.
     *
     * @param type        Le type de mur.
     * @param hauteur     La hauteur du mur.
     * @param largeur     La largeur du mur.
     * @param accessoires La liste des accessoires du mur.
     */
    public Mur(TypeMur type, Chalet chalet, List<Accessoire> accessoires) {
        this(type, chalet);
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
    public Accessoire getAccessoire(UUID accessoireUuid) {

        List<Accessoire> listAccessoires = this.getAccessoires();
        if (listAccessoires == null) {
            throw new IllegalArgumentException("pas d'accessoire");
        }
        for (Accessoire accessoire : accessoires) {
            if (accessoire.getAccessoireId() == accessoireUuid) {
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
        Chalet.ChaletDTO chaletDTO = this.chalet.toDTO();
        chaletDTO.hauteur = hauteur;
        this.chalet.updateChalet(chaletDTO);
    }

    /**
     * Modifie la largeur du mur.
     *
     * @param largeur La nouvelle largeur du mur.
     */
    public void setLargeur(double largeur) {
        Chalet.ChaletDTO chaletDTO = this.chalet.toDTO();
        chaletDTO.largeur = largeur;
        this.chalet.updateChalet(chaletDTO);
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
     * La classe MurDTO représente un objet de transfert de données (DTO) pour un
     * mur.
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
            // this.hauteur = mur.getHauteur();
            // this.largeur = mur.getLargeur();
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
    // public static Mur fromDTO(MurDTO dto) {
    // List<Accessoire> accessoires = new ArrayList<Accessoire>();
    // for (Accessoire.AccessoireDTO accessoireDTO : dto.accessoires) {
    // accessoires.add(Accessoire.fromDTO(accessoireDTO));
    // }

    // return new Mur(dto.type, dto.hauteur, dto.largeur, accessoires);
    // }

    /**
     * Ajoute un accessoire
     *
     * @param p_type      qui représente le type de l'accessoire
     * @param p_position  qui représente la pôsition de l'objet
     * @param p_dimension qui représente la dimensions de l'objet
     * @return
     */
    public Accessoire ajouterAccessoire(Accessoire accessoire) {
        double[] position = accessoire.getPosition(), dimension = accessoire.getDimension();

        if (position.length != 2) {
            throw new IllegalArgumentException("mauvais nombre de position");
        }
        if (dimension.length != 2) {
            throw new IllegalArgumentException("mauvais nombre de dimension");
        }
        for (int i = 0; i < 2; i++) {
            if (position[i] < 0) {
                throw new IllegalArgumentException("position negative");
            }

            if (dimension[i] < 0) {
                throw new IllegalArgumentException("dimensions negative ou 0");
            }
        }

        accessoires.add(accessoire);

        this.verifierValiditeAccessoire(accessoire, chalet.getMargeAccessoire(), chalet.getEpaisseurMur());
        return accessoire;
    }

    public Accessoire ajouterAccessoire(TypeAccessoire p_type, double[] p_position, double[] p_dimension) {
        Accessoire accessoire = new Accessoire(p_type, this.type, p_position, p_dimension);
        return ajouterAccessoire(accessoire);
    }

    public Accessoire updateAccessoire(Accessoire.AccessoireDTO accessoireDTO) {
        Accessoire accessoire = this.getAccessoire(accessoireDTO.accessoireId);
        accessoire.updateAccessoire(accessoireDTO);

        if (accessoireDTO.accessoireType == TypeAccessoire.Porte) {
            double newPositionY = this.chalet.getHauteur() - accessoireDTO.dimensions[1];
            accessoire.setPosition(new double[] { accessoireDTO.position[0], newPositionY - chalet.getMargeAccessoire()});
        }

        return accessoire;
    }

    /**
     * Retire un accessoire
     *
     * @param uuid DTO à partir duquel créer le mur.
     * @return Le mur correspondant au DTO.
     */
    public Accessoire retirerAccessoire(UUID uuid) {
        for (Accessoire accessoire : accessoires) {
            if (accessoire.getAccessoireId().equals(uuid)) {
                accessoires.remove(accessoire);
                return accessoire;
            }
        }

        return null;
    }

    /**
     * Permet de voir la validité de l'accessoire dont les paramètres passent en
     * entrée par rapport aux autres accessoires
     * 
     * @param position  de l'objet qu'on veut verifier
     * @param dimension de l'objet qu'on veut verifier
     * @return boolean qui represente sa validite
     */
    public boolean verifierCollisionAcc(Accessoire.AccessoireDTO accessoire, double margeAcc) {
        double[] position = accessoire.position;
        double[] dimension = accessoire.dimensions;

        Rectangle2D accRect = new Rectangle2D.Double((position[0] - margeAcc), (position[1] - margeAcc),
                (dimension[0] + 2 * margeAcc), (dimension[1] + 2 * margeAcc));

        for (Accessoire otherAcc : accessoires) {
            if (accessoire.accessoireId == otherAcc.getAccessoireId())
                continue;

            Rectangle2D otherAccRectangle2d = new Rectangle2D.Double(otherAcc.getPosition()[0],
                    otherAcc.getPosition()[1],
                    otherAcc.getDimension()[0], otherAcc.getDimension()[1]);

            boolean intersect = accRect.intersects(otherAccRectangle2d);

            if (intersect)
                return true;
        }

        return false;
    }

    public boolean verifierCollisionAcc(Accessoire accessoire, double margeAcc) {
        return this.verifierCollisionAcc(accessoire.toDTO(), margeAcc);
    }

    /**
     * Permet de voir la validité de l'accessoire dont les paramètres passent en
     * entrée par rapport aux autres accessoires
     * 
     * @param position  de l'objet qu'on veut verifier
     * @param dimension de l'objet qu'on veut verifier
     * @return boolean qui represente sa validite
     */
    public boolean verifierCollisionAcc(double[] position, double[] dimension, double margeAcc) {
        Rectangle2D accrec = new Rectangle2D.Double((position[0] - margeAcc), (position[1] - margeAcc),
                (dimension[0] + 2 * margeAcc), (dimension[1] + 2 * margeAcc));

        for (Accessoire accessoire : accessoires) {
            Rectangle2D inters = new Rectangle2D.Double(accessoire.getPosition()[0], accessoire.getPosition()[1],
                    accessoire.getDimension()[0], accessoire.getDimension()[1]);

            boolean intersect = accrec.intersects(inters);
            if (intersect)
                return true;
        }

        return false;
    }

    /**
     * Meme chose que verifierCollisionAcc mais avec une marge de 0 si on ne le
     * passe pas en parametre
     * 
     * @param p_position
     * @param p_dimension
     * @return
     */
    public boolean verifierCollisionAcc(double[] p_position, double[] p_dimension) {
        return this.verifierCollisionAcc(p_position, p_dimension, 0);
    }

    /**
     * Permet de voir si l'accessoire est dans une position valide par rapport au
     * mur et sa marge
     * 
     * @param p_position   position de l'accessoire
     * @param p_dimension  dimensions de l'accessoire
     * @param margeMinimal marge minimale du projet
     * @return la validité
     */
    public boolean verifierMargeAcc(Accessoire.AccessoireDTO accessoire, double margeMinimal) {
        double[] position = accessoire.position;
        double[] dimension = accessoire.dimensions;

        Rectangle2D murLessMarginRect = new Rectangle2D.Double(margeMinimal, margeMinimal,
                this.chalet.getLargeur() - 2 * margeMinimal, this.chalet.getHauteur() - 2 * margeMinimal);

        Rectangle2D accRect = new Rectangle2D.Double(position[0], position[1], dimension[0], dimension[1]);

        return murLessMarginRect.contains(accRect);

        // return (p_position[0] - margeMinimal < 0 && p_dimension[0] + p_position[0] +
        // margeMinimal > this.getLargeur())
        // && (p_position[1] - margeMinimal < 0
        // && p_dimension[1] + p_position[1] + margeMinimal > this.getHauteur());
    }

    public boolean verifierMargeAcc(Accessoire accessoire, double margeMinimal) {
        return this.verifierMargeAcc(accessoire.toDTO(), margeMinimal);
    }

    public double[] getAccessoireMargin(Accessoire accessoire) {
        return accessoire.getMarginWithRect(new double[] { 0, 0 },
                new double[] { this.chalet.getLargeur(), this.chalet.getHauteur() });
    }

    public double[] getAccessoireMargin(Accessoire.AccessoireDTO accessoireDTO) {
        return Accessoire.fromDTO(accessoireDTO).getMarginWithRect(new double[] { 0, 0 },
                new double[] { this.chalet.getLargeur(), this.chalet.getHauteur() });
    }

    public boolean verifierValiditeAccessoire(Accessoire accessoire, double margeMinimal,
            double epaisseurMur) {
        double largeur = this.type == TypeMur.Facade || this.type == TypeMur.Arriere ? this.chalet.getLargeur()
                : this.chalet.getLongueur();
        boolean isColliding = this.verifierCollisionAcc(accessoire, margeMinimal);
        if (accessoire.getAccessoireType() == TypeAccessoire.Porte) {
            // Mettre a jour la valeur de la position Y afin que l'accessoire soit aligné
            // avec le bas du mur.
            double newPositionY = this.chalet.getHauteur() - accessoire.getDimension()[1];
            accessoire.setPosition(
                    new double[] { accessoire.getPosition()[0], newPositionY - chalet.getMargeAccessoire() });
        }

        if (isColliding) {
            return false; // Invalide
        }

        double[] margins = Accessoire.getMarginWithRect(accessoire.toDTO(), new double[] { 0, 0 },
                new double[] { largeur, this.chalet.getHauteur() });

        // if (accessoire.getAccessoireType() == TypeAccessoire.Porte) {
        // // Bottom margin is always 0 for a door
        // // Only need to check left, top and right margins
        // if (margins[0] < margeMinimal + epaisseurMur || margins[2] < margeMinimal +
        // epaisseurMur
        // || margins[1] < margeMinimal) {
        // return false; // Invalide
        // }
        // Condition pour exterieur du mur a retouche au besion
        if (margins[0] > largeur - epaisseurMur - accessoire.getDimension()[0]) {
            return false;
        }
        // revoir l'utilite de
        // If not a door, check all margins
        if (margins[0] < margeMinimal + epaisseurMur || margins[2] < margeMinimal + epaisseurMur
                || margins[1] < margeMinimal || margins[3] < margeMinimal)

        {
            return false; // Invalide
        }

        if (margins[0] > largeur - epaisseurMur - accessoire.getDimension()[0]
                || margins[1] > this.chalet.getHauteur() - accessoire.getDimension()[1]) {
            return false;
        }

        return true;
    }

    public void updateValiditeAccessoires(double margeMinimal, double epaisseurMur) {
        for (Accessoire accessoire : accessoires) {
            accessoire.setValide(verifierValiditeAccessoire(accessoire, margeMinimal, epaisseurMur));
        }
    }

}