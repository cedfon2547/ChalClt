package ca.ulaval.glo2004.domaine;

/*
 * La classe Retrait représente un retrait dans une pièce. Un retrait est une zone où il n'y a pas de mur,
 * de colonne ou d'obstacle. Cette classe contient des informations sur le type de retrait, les dimensions
 * et la position du retrait dans la pièce.
 */
public class Retrait {

    /**
     * Le type de retrait.
     */
    private TypeRetrait type;

    /**
     * Les dimensions du retrait.
     */
    private double[] dimensions;

    /**
     * La position du retrait.
     */
    private double[] position;

    /**
     * Constructeur de la classe Retrait.
     * @param type Le type de retrait.
     * @param dimensions Les dimensions du retrait.
     * @param position La position du retrait.
     */
    public Retrait(TypeRetrait type, double[] position, double[] dimensions) {
        this.type = type;
        this.dimensions = dimensions;
        this.position = position;
    }

    /**
     * Constructeur de la classe Retrait à partir d'un objet RetraitDTO.
     * @param dto L'objet RetraitDTO à partir duquel créer le retrait.
     */
    public Retrait(RetraitDTO dto) {
        this(dto.type, dto.dimensions, dto.position);
    }

    /**
     * Retourne le type de retrait.
     * @return Le type de retrait.
     */
    public TypeRetrait getType() {
        return type;
    }

    /**
     * Retourne les dimensions du retrait.
     * @return Les dimensions du retrait.
     */
    public double[] getDimension() {
        return dimensions;
    }

    /**
     * Retourne la position du retrait.
     * @return La position du retrait.
     */
    public double[] getPosition() {
        return position;
    }

    /**
     * Modifie le type de retrait.
     * @param type Le nouveau type de retrait.
     */
    public void setType(TypeRetrait type) {
        this.type = type;
    }

    /**
     * Modifie les dimensions du retrait.
     * @param dimensions Les nouvelles dimensions du retrait.
     */
    public void setDimensions(double[] dimensions) {
        this.dimensions = dimensions;
    }

    /**
     * Modifie la position du retrait.
     * @param position La nouvelle position du retrait.
     */
    public void setPosition(double[] position) {
        this.position = position;
    }

    /**
     * Convertit le retrait en un objet RetraitDTO.
     * @return L'objet RetraitDTO correspondant au retrait.
     */
    public RetraitDTO toDTO() {
        return new RetraitDTO(this);
    }

    /**
     * La classe RetraitDTO représente un objet de transfert de données pour un retrait.
     */
    public static class RetraitDTO implements java.io.Serializable {

        /**
         * Le type de retrait.
         */
        public TypeRetrait type;

        /**
         * Les dimensions du retrait.
         */
        public double[] dimensions;

        /**
         * La position du retrait.
         */
        public double[] position;

        /**
         * Constructeur de la classe RetraitDTO.
         * @param retrait Le retrait à partir duquel créer l'objet RetraitDTO.
         */
        public RetraitDTO(Retrait retrait) {
            this.type = retrait.type;
            this.dimensions = retrait.dimensions;
            this.position = retrait.position;
        }

        public RetraitDTO(RetraitDTO retraitDTO) {
            this.type = retraitDTO.type;
            this.dimensions = retraitDTO.dimensions;
            this.position = retraitDTO.position;
        }
    }

    /**
     * Convertit un objet RetraitDTO en un retrait.
     * @param dto L'objet RetraitDTO à partir duquel créer le retrait.
     * @return Le retrait correspondant à l'objet RetraitDTO.
     */
    public static Retrait fromDTO(RetraitDTO dto) {
        return new Retrait(dto);
    }
}