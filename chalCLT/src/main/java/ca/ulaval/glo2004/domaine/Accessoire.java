package ca.ulaval.glo2004.domaine;

import java.util.UUID;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;


/**
 * Représente un accessoire qui peut être ajouté à un chalet.
 * Hérite de {@link ca.ulaval.glo2004.domaine.Retrait}.
 */
public class Accessoire extends Retrait {
    private TypeAccessoire accessoireType;
    private TypeMur typeMur;
    private String accessoireNom;
    private UUID accessoireId;
    private boolean valide;

    /**
     * Construit un objet Accessoire avec le type, la position et les dimensions spécifiés.
     * @param accessoireType le type de l'accessoire
     * @param position la position de l'accessoire
     * @param dimensions les dimensions de l'accessoire
     */
    public Accessoire(TypeAccessoire accessoireType,TypeMur typeMur, double[] position, double[] dimensions, boolean valide) {
        super(TypeRetrait.Accessoire, position, dimensions);
        this.setAccessoireType(accessoireType);
        this.setTypeMur(typeMur);
        this.setAccessoireNom(accessoireNom);
        this.setValide(valide);
        this.setAccessoireId(accessoireId);
        
    }

    /**
     * Construit un objet Accessoire à partir d'un objet AccessoireDTO.
     * @param dto l'objet AccessoireDTO à partir duquel construire
     */
    public Accessoire(AccessoireDTO dto) {
        this(dto.accessoireType, dto.typeMur, dto.position, dto.dimensions, dto.valide);
        this.setAccessoireId(accessoireId);
        this.setAccessoireNom(accessoireNom);
    }

    /**
     * Retourne le type de l'accessoire.
     * @return le type de l'accessoire
     */
    public TypeAccessoire getAccessoireType() {
        return accessoireType;
    }

    /**
     * Retourne l'ID de l'accessoire.
     * @return l'ID de l'accessoire
     */
    public UUID getAccessoireId() {
        return accessoireId;
    }

    /**
     * Retourne le nom de l'accessoire.
     * @return le nom de l'accessoire
     */
    public String getAccessoireNom(){
        return accessoireNom;
    }

    /**
     * Retourne le mur sur lequel l'accessoire est placé
     * @return Le mur sur lequel l'accessoire est placé (Arrière, Droit, Façade, Gauche)
     */
    public TypeMur getTypeMur() {
        return typeMur;
    }

    /**
     * Retourne la validité de l'accessoire
     * @return vrai si l'accessoire est placé à une position valide
     */
    public boolean getValide(){
        return valide;
    }

    public boolean isValide() {
        return valide == true;
    }

    /**
     * mis à jour de l'accessoire. 
     * @param accessoireDTO l'accessoire mis à jour
     */
    public void updateAccessoire(AccessoireDTO accessoireDTO){
        setAccessoireId(accessoireDTO.accessoireId);
        setAccessoireNom(accessoireDTO.accessoireNom);
        setAccessoireType(accessoireDTO.accessoireType);
        setTypeMur(accessoireDTO.typeMur);
        setValide(accessoireDTO.valide);
        setDimensions(accessoireDTO.dimensions.clone());
        setPosition(accessoireDTO.position.clone());
    }

    /**
     * Définit le type de l'accessoire.
     * @param accessoireType le nouveau type de l'accessoire
     */
    public void setAccessoireType(TypeAccessoire accessoireType) {
        this.accessoireType = accessoireType;
    }

    /**
     * Définit l'ID de l'accessoire.
     * @param accessoireId le nouvel ID de l'accessoire
     */
    public void setAccessoireId(UUID accessoireId) {
        this.accessoireId = accessoireId;
    }

    /**
     * Définit le nom de l'accessoire.
     * @param accessoireNom le nouveau nom de l'accessoire
     */
    public void setAccessoireNom(String accessoireNom){
        this.accessoireNom = accessoireNom;
    }

    /**
     *  Definit la validité de l'accessoire
     * @param p_validiterEtat true si l'accessoire est valide
     */
    public void setValide(boolean p_validiterEtat){this.valide = p_validiterEtat;}

    /**
     *  Definit le mur sur lequel l'accessoire est placé
     * @param typeMur Type du mur
     */
    public void setTypeMur(TypeMur typeMur) {
        this.typeMur = typeMur;
    }

    /**
     * Retourne la marge entre cet accessoire et un rectangle spécifié.
     * @param position Position du rectangle
     * @param dimension Dimensions du rectangle
     * @return liste de 4 doubles, représentant respectivement la marge à gauche, la marge en haut, la marge à droite et la marge en bas.
     * [gauche, haut, droite, bas]
     */
    public double[] getMarginWithRect(double[] position, double[] dimension) {
        double marginLeft = Math.abs(position[0] - this.getPosition()[0]);
        double marginTop = Math.abs(position[1] - this.getPosition()[1]);
        double marginRight = Math.abs((this.getPosition()[0] + this.getDimension()[0]) - (position[0] + dimension[0]));
        double marginBottom = Math.abs((this.getPosition()[1] + this.getDimension()[1]) - (position[1] + dimension[1]));

        return new double[] { marginLeft, marginTop, marginRight, marginBottom };
    }

    /**
     * Retourne la marge entre l'accessoire spécifié et un rectangle spécifié.
     * @param accessoireDTO Accessoire dont on veut obtenir les marges
     * @param position Position du rectangle
     * @param dimension Dimensions du rectangle
     * @return liste de 4 doubles, représentant respectivement la marge à gauche, la marge en haut, la marge à droite et la marge en bas.
     * [gauche, haut, droite, bas]
     */
    public static double[] getMarginWithRect(Accessoire.AccessoireDTO accessoireDTO, double[] position, double[] dimension) {
        double marginLeft = Math.abs(position[0] - accessoireDTO.position[0]);
        double marginTop = Math.abs(position[1] - accessoireDTO.position[1]);
        double marginRight = Math.abs((accessoireDTO.position[0] + accessoireDTO.dimensions[0]) - (position[0] + dimension[0]));
        double marginBottom = Math.abs((accessoireDTO.position[1] + accessoireDTO.dimensions[1]) - (position[1] + dimension[1]));

        return new double[] { marginLeft, marginTop, marginRight, marginBottom };
    }


    /**
     * Retourne un objet AccessoireDTO représentant cet objet Accessoire.
     * @return un objet AccessoireDTO représentant cet objet Accessoire
     */
    public AccessoireDTO toDTO() {
        return new AccessoireDTO(this);
    }

    /**
     * Représente un objet de transfert de données pour un objet Accessoire.
     * Hérite de {@link ca.ulaval.glo2004.domaine.Retrait.RetraitDTO}.
     */
    public static class AccessoireDTO extends Retrait.RetraitDTO {
        public TypeAccessoire accessoireType;
        public String accessoireNom;
        public UUID accessoireId;
        public TypeMur typeMur;
        public boolean valide;

        /**
         * Construit un objet AccessoireDTO à partir d'un objet Accessoire.
         * @param accessoire l'objet Accessoire à partir duquel construire
         */
        public AccessoireDTO(Accessoire accessoire) {
            super(accessoire);
            this.accessoireNom = accessoire.accessoireNom;
            this.accessoireType = accessoire.accessoireType;
            this.accessoireId = accessoire.accessoireId;
            this.typeMur = accessoire.typeMur;
            this.valide = accessoire.valide;
        }

        /**
         * Construit un objet AccessoireDTO à partir d'un objet AccessoireDTO.
         * @param accessoireDTO l'objet AccessoireDTO à partir duquel construire
         */
        public AccessoireDTO(AccessoireDTO accessoireDTO) {
            super(new Retrait.RetraitDTO(accessoireDTO));
            this.accessoireNom = accessoireDTO.accessoireNom;
            this.accessoireType = accessoireDTO.accessoireType;
            this.accessoireId = accessoireDTO.accessoireId;
            this.typeMur = accessoireDTO.typeMur;
            this.valide = accessoireDTO.valide;
        }

        /**
         * Construit une copie de l'instance d'objet AccessoireDTO.
         * @return Copie de l'objet AccessoireDTO
         */
        public AccessoireDTO copy() {
            AccessoireDTO copy = new AccessoireDTO(this);
            copy.position = this.position.clone();
            copy.dimensions = this.dimensions.clone();
            return copy;
        }
        
        /**
         * Écrit les informations de l'accessoireDTO en format serializable dans le fichier de sauvegarde
         * @param oos Objet en charge de l'écriture dans le fichier de sauvegarde
         */
        public void writeObject(ObjectOutputStream oos) {
            try {
                super.writeObject(oos);
                oos.writeObject(accessoireNom);
                String typeString;
                switch (accessoireType) {
                    case Porte:
                        typeString = "Porte";
                        break;
                    case Fenetre:
                        typeString = "Fenetre";
                        break;
                    default:
                        typeString = "";
                        break;
                }
                oos.writeObject(typeString);
                oos.writeObject(accessoireId);
                String typeMurString;
                switch (typeMur) {
                    case Facade:
                        typeMurString = "Facade";
                        break;
                    case Arriere:
                        typeMurString = "Arriere";
                        break;
                    case Droit:
                        typeMurString = "Droit";
                        break;
                    case Gauche:
                        typeMurString = "Gauche";
                        break;
                    default:
                        typeMurString = "";
                        break;
                }
                oos.writeObject(typeMurString);
                oos.writeObject(valide);
            }
            catch (IOException e) {
                // jsp
            }
        }
        
        /**
         * Construit un objet AccessoireDTO selon les informations lues dans le fichier de sauvegarde
         * @param ois Objet en charge de la lecture dans le fichier de sauvegarde
         */
        public void readObject(ObjectInputStream ois) {
            try {
                super.readObject(ois);
                accessoireNom = (String) ois.readObject();
                String typeString = (String) ois.readObject();
                switch (typeString) {
                    case "Porte":
                        accessoireType = TypeAccessoire.Porte;
                        break;
                    case "Fenetre":
                        accessoireType = TypeAccessoire.Fenetre;
                        break;
                    default:
                        break;
                }
                accessoireId = (UUID) ois.readObject();
                String typeMurString = (String) ois.readObject();
                switch (typeMurString) {
                    case "Facade":
                        typeMur = TypeMur.Facade;
                        break;
                    case "Arriere":
                        typeMur = TypeMur.Arriere;
                        break;
                    case "Droit":
                        typeMur = TypeMur.Droit;
                        break;
                    case "Gauche":
                        typeMur = TypeMur.Gauche;
                        break;
                    default:
                        break;
                }
                valide = (boolean) ois.readObject();
            }
            catch (IOException | ClassNotFoundException e) {
                // jsp
            }
        }
    }

    /**
     * Construit un objet Accessoire à partir d'un objet AccessoireDTO.
     * @param dto l'objet AccessoireDTO à partir duquel construire
     * @return un objet Accessoire construit à partir de l'objet AccessoireDTO
     */
    public static Accessoire fromDTO(AccessoireDTO dto) {
        return new Accessoire(dto);
    }

    /**
     * Ajuste la position verticale de l'accessoire avec la nouvelle hauteur du mur
     * @param oldHauteur Ancienne hauteur du mur
     * @param newHauteur Nouvelle hauteur du mur
     */
    public void updateWithHauteur (double oldHauteur, double newHauteur) {
        double posY = this.getPosition()[1];
        double h = this.getDimension()[1];
        
        double posWithinWall = (posY + (h/2)) / oldHauteur;
        double newPosY = (posWithinWall * newHauteur) - (h/2);
        double[] newPos = {this.getPosition()[0], newPosY};
        this.setPosition(newPos);
    }
    
    /**
     * Ajuste la position horizontale de l'accessoire avec la nouvelle longueur du mur
     * @param oldLongueur Ancienne longueur du mur
     * @param newHauteur Nouvelle longueur du mur
     */
    public void updateWithLongueur (double oldLongueur, double newLongueur) {
        double posX = this.getPosition()[0];
        double l = this.getDimension()[0];
        
        double posWithinWall = (posX + (l/2)) / oldLongueur;
        double newPosX = (posWithinWall * newLongueur) - (l/2);
        double[] newPos = {newPosX, this.getPosition()[1]};
        this.setPosition(newPos);
    }
}


