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

    public TypeMur getTypeMur() {
        return typeMur;
    }

    /**
     * Retourne la validité de l'accessoire
     * @return valide
     */
    public boolean getValide(){return valide;}

    /**
     * mis à jour de l'accessoire. 
     * @param accessoireDTO l'accessoire mis à jour
     */
    public void updateAccessoire(AccessoireDTO accessoireDTO){
        this.accessoireId = accessoireDTO.accessoireId;
        this.accessoireNom = accessoireDTO.accessoireNom;
        this.accessoireType = accessoireDTO.accessoireType;
        this.typeMur = accessoireDTO.typeMur;
        this.valide = accessoireDTO.valide;
        this.update(accessoireDTO);
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

    public void setTypeMur(TypeMur typeMur) {
        this.typeMur = typeMur;
    }

    /**
     * Update la position de l'accessoire
     * @param accessoireDTO
     */
    public void update(AccessoireDTO accessoireDTO){
        this.setDimensions(accessoireDTO.dimensions.clone());
        this.setPosition(accessoireDTO.position.clone());
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


