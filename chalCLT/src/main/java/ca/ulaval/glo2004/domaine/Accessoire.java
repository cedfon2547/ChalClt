package ca.ulaval.glo2004.domaine;

import java.util.UUID;


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
    public Accessoire(TypeAccessoire accessoireType,TypeMur typeMur, double[] position, double[] dimensions) {
        super(TypeRetrait.Accessoire, position, dimensions);
        this.accessoireType = accessoireType;
        this.typeMur = typeMur;
        this.accessoireNom = accessoireType.toString();
        this.valide = true;
        this.accessoireId = UUID.randomUUID();
    }

    /**
     * Construit un objet Accessoire à partir d'un objet AccessoireDTO.
     * @param dto l'objet AccessoireDTO à partir duquel construire
     */
    public Accessoire(AccessoireDTO dto) {
        this(dto.accessoireType,dto.typeMur, dto.position, dto.dimensions);
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
     * @param p_validiterEtat
     */
    public void setValide(boolean p_validiterEtat){this.valide = p_validiterEtat;}
    
    /**
     * Vérifie si l'accessoire est placé à une position valide
     * @return vrai si la position de l'accessoire est valide
     */
    public boolean validationSurMur() {
        double posX = this.getPosition()[0];
        double posY = this.getPosition()[1];
        double largeur = this.getDimension()[0];
        double hauteur = this.getDimension()[1];
        double largeurMur;
        if ((this.typeMur == TypeMur.Facade) || (this.typeMur == TypeMur.Arriere))
            largeurMur = Controleur.getInstance().getChalet().largeur;
        else
            largeurMur = Controleur.getInstance().getChalet().longueur;
        double hauteurMur = Controleur.getInstance().getChalet().hauteur;
        if ((posX > 0) && (posX + largeur < largeurMur))
            if ((posY > 0) && (posY + hauteur < hauteurMur))
                return true;
        return false;
    }
    
    /**
     * Vérifie si l'accessoire est placé à une position ou un autre accessoire est situé
     * @return vrai si aucun autre accessoire ne bloque l'accessoire en question
     */
    public boolean validationSurAutres() {
        double posX = this.getPosition()[0];
        double posY = this.getPosition()[1];
        double largeur = this.getDimension()[0];
        double hauteur = this.getDimension()[1];
        for (AccessoireDTO other : Controleur.getInstance().getAccessoires()) {
            if (other.typeMur == this.typeMur) {
                double otherX = other.position[0];
                double otherY = other.position[1];
                double otherL = other.dimensions[0];
                double otherH = other.dimensions[1];
                if (((posX <= otherX) && (posX + largeur >= otherX)) || 
                        ((posX <= otherX + otherL) && (posX + largeur >= otherX + otherL)))
                    if (((posY <= otherY) && (posY + hauteur >= otherY)) || 
                        ((posY <= otherY + otherH) && (posX + hauteur >= otherY + otherH)))
                        return false;
            }
        }
        return true;
    }

    public void setTypeMur(TypeMur typeMur) {
        this.typeMur = typeMur;
    }

    /**
     * Retourne la marge entre cet accessoire et un rectangle spécifié.
     * 
     * La valeur retournée est une liste de 4 doubles, représentant respectivement la marge à gauche, la marge en haut, la marge à droite et la marge en bas.
     * [gauche, haut, droite, bas]
     * 
     * @param position
     * @param dimension
     * @return
     */
    public double[] getMarginWithRect(double[] position, double[] dimension) {
        double marginLeft = Math.abs(position[0] - this.getPosition()[0]);
        double marginTop = Math.abs(position[1] - this.getPosition()[1]);
        double marginRight = Math.abs((this.getPosition()[0] + this.getDimension()[0]) - (position[0] + dimension[0]));
        double marginBottom = Math.abs((this.getPosition()[1] + this.getDimension()[1]) - (position[1] + dimension[1]));

        return new double[] { marginLeft, marginTop, marginRight, marginBottom };
    }

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
    }

    /**
     * Construit un objet Accessoire à partir d'un objet AccessoireDTO.
     * @param dto l'objet AccessoireDTO à partir duquel construire
     * @return un objet Accessoire construit à partir de l'objet AccessoireDTO
     */
    public static Accessoire fromDTO(AccessoireDTO dto) {
        return new Accessoire(dto);
    }

}


