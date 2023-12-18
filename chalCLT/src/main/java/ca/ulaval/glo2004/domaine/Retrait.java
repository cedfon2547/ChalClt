package ca.ulaval.glo2004.domaine;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

/*
 * La classe Retrait représente un retrait dans une pièce. Un retrait est une zone où il n'y a pas de mur,
 * de colonne ou d'obstacle. Cette classe contient des informations sur le type de retrait, les dimensions
 * et la position du retrait dans la pièce.
 */
public class Retrait implements Serializable {

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
        this.setType(type);
        this.setDimensions(dimensions);
        this.setPosition(position);
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
     * @param dimension Les nouvelles dimensions du retrait.
     */
    public void setDimensions(double[] dimension) {
        if(dimension[0] > Integer.MAX_VALUE){
            dimension[0] = Integer.MAX_VALUE;
        }
        if(dimension[1] > Integer.MAX_VALUE){
            dimension[1] = Integer.MAX_VALUE;
        }
        this.dimensions = dimension;
    }

    /**
     * Modifie la position du retrait.
     * @param position La nouvelle position du retrait.
     */
    public void setPosition(double[] position) {
        if(position[0] > Integer.MAX_VALUE){
            position[0] = Integer.MAX_VALUE;
        }
        if(position[1] > Integer.MAX_VALUE){
            position[1] = Integer.MAX_VALUE;
        }
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
    public static class RetraitDTO implements Serializable{

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
        
        public void writeObject(ObjectOutputStream oos) {
            try {
                String typeString;
                switch (type) {
                    case Rainure:
                        typeString = "Rainure";
                        break;
                    case Accessoire:
                        typeString = "Accessoire";
                        break;
                    default:
                        typeString = "";
                        break;
                }
                oos.writeObject(typeString);
                oos.writeObject(position);
                oos.writeObject(dimensions);
            }
            catch (IOException e) {
                // jsp
            }
        }
        
        public void readObject(ObjectInputStream ois) {
            try {
                String typeString = (String) ois.readObject();
                switch (typeString) {
                    case "Rainure":
                        type = TypeRetrait.Rainure;
                        break;
                    case "Accessoire":
                        type = TypeRetrait.Accessoire;
                        break;
                    default:
                        break;
                }
                position = (double[]) ois.readObject();
                dimensions = (double[]) ois.readObject();
            }
            catch (IOException | ClassNotFoundException e) {
                // jsp
            }
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