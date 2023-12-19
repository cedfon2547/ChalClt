package ca.ulaval.glo2004.domaine;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class Chalet implements Serializable {
    private String nom = "ChaletDefault";
    private double margeSupplementaireRetrait = 3.0;
    private double hauteur = 96;
    private double largeur = 120;
    private double longueur = 120;
    private double epaisseurMur = 12.0; // Test purpose
    private TypeSensToit sensToit = TypeSensToit.Nord;
    private double angleToit = 15.0;
    private double margeAccessoire = 3.0;


    // TODO: rallon, pignon
    private Mur[] murs = new Mur[]{new Mur(TypeMur.Facade, this), new Mur(TypeMur.Arriere, this), new Mur(TypeMur.Droit, this), new Mur(TypeMur.Gauche, this)};
    // private Toit toit = new Toit();

    public Chalet() {
    }

    public Chalet(String nom, double hauteur, double largeur, double longueur, double epaisseurMur, TypeSensToit sensToit, double angleToit, double margeAccessoire, double margeSupplementaireRetrait) {
        this.nom = nom;
        this.hauteur = hauteur;
        this.largeur = largeur;
        this.longueur = longueur;
        this.epaisseurMur = epaisseurMur;
        this.sensToit = sensToit;
        this.angleToit = angleToit;
        this.margeAccessoire = margeAccessoire;
        this.margeSupplementaireRetrait = margeSupplementaireRetrait;
    }


    public Chalet(ChaletDTO dto) {
        this(dto.nom, dto.hauteur, dto.largeur, dto.longueur, dto.epaisseurMur, dto.sensToit, dto.angleToit, dto.margeAccessoire, dto.margeSupplementaireRetrait);
    }

    public Chalet(ChaletDTO dto, List<Accessoire.AccessoireDTO> accessoireDTOs) {
        this(dto.nom, dto.hauteur, dto.largeur, dto.longueur, dto.epaisseurMur, dto.sensToit, dto.angleToit, dto.margeAccessoire, dto.margeSupplementaireRetrait);

        for (Accessoire.AccessoireDTO accessoireDTO : accessoireDTOs) {
            Accessoire accessoire = new Accessoire(accessoireDTO);
            getMur(accessoire.getTypeMur()).ajouterAccessoire(accessoire);
        }
    }

    public Chalet(ChaletCompletDTO completDTO) {
        this(completDTO.nom, completDTO.hauteur, completDTO.largeur, completDTO.longueur, completDTO.epaisseurMur, completDTO.sensToit, completDTO.angleToit, completDTO.margeAccessoire, completDTO.margeSupplementaireRetrait);

        for (Mur.MurDTO murDTO : completDTO.murs) {
            Mur mur = new Mur(murDTO, this);
            // mur.getAccessoires().clear();
            // for (Accessoire.AccessoireDTO accessoireDTO : murDTO.accessoires) {
            //     Accessoire accessoire = new Accessoire(accessoireDTO);
            //     mur.ajouterAccessoire(accessoire);
            // }
            this.murs[mur.getType().ordinal()] = mur;
        }
    }

    /**
     * Setter et Accesseur
     */
    public Mur getMur(TypeMur type) {
        for (Mur mur : murs) {
            if (mur.getType() == type) {
                return mur;
            }
        }
        return null;
    }

    public Mur[] getMurs() {
        return murs;
    }

    public double getAngleToit() {
        return angleToit;
    }

    public double getEpaisseurMur() {
        return epaisseurMur;
    }

    public double getHauteur() {
        return hauteur;
    }

    public double getLargeur() {
        return largeur;
    }

    public double getLongueur() {
        return longueur;
    }
    
    public double getMargeAccessoire() {
        return margeAccessoire;
    }

    public double getMargeSupplementaireRetrait() {
        return margeSupplementaireRetrait;
    }
    
    public String getNom() {
        return nom;
    }
    
    public TypeSensToit getSensToit() {
        return sensToit;
    }

    public void setAngleToit(double angleToit) {
        this.angleToit = angleToit;
    }

    public void setEpaisseurMur(double epaisseurMur) {
        this.epaisseurMur = epaisseurMur;
    }

    public void setHauteur(double hauteur) {
        this.hauteur = hauteur;
    }

    public void setLargeur(double largeur) {
        if (sensToit == TypeSensToit.Est || sensToit == TypeSensToit.Ouest){
            if (largeur < 3*epaisseurMur) {
                largeur = 3*epaisseurMur;
            }
        }
        else if ((sensToit == TypeSensToit.Nord || sensToit == TypeSensToit.Sud)) {
            if (largeur < 2*epaisseurMur) {
                largeur = 2*epaisseurMur;
            }
        }
        this.largeur = largeur;
    }

    public void setLongueur(double longueur) {
        if (sensToit == TypeSensToit.Est || sensToit == TypeSensToit.Ouest){
            if (longueur < 2*epaisseurMur) {
                longueur = 2*epaisseurMur;
            }
        }
        else if ((sensToit == TypeSensToit.Nord || sensToit == TypeSensToit.Sud)) {
            if (longueur < 3*epaisseurMur) {
                longueur = 3*epaisseurMur;
            }
        }
        
        this.longueur = longueur;
    }

    public void setMargeAccessoire(double margeAccessoire) {
        this.margeAccessoire = margeAccessoire;
    }

    public void setMargeSupplementaireRetrait(double margeSupplementaireRetrait) {
        if (margeSupplementaireRetrait > epaisseurMur) {
            margeSupplementaireRetrait = epaisseurMur;
        }
        this.margeSupplementaireRetrait = margeSupplementaireRetrait;
    }
    
    public void setMurs(Mur[] murs) {
        this.murs = murs;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setSensToit(TypeSensToit sensToit) {
        this.sensToit = sensToit;
        this.setLongueur(longueur);
        this.setLargeur(largeur);
        
    }
    
    public void updateChalet(ChaletDTO dtoChalet) {
        double oldLargeur = this.largeur;
        double oldLongueur = this.longueur;
        double oldHauteur = this.hauteur;
        
        this.setNom(dtoChalet.nom);
        this.setHauteur(dtoChalet.hauteur);
        this.setLargeur(dtoChalet.largeur);
        this.setLongueur(dtoChalet.longueur);
        this.setEpaisseurMur(dtoChalet.epaisseurMur);
        this.setSensToit(dtoChalet.sensToit);
        this.setAngleToit(dtoChalet.angleToit);
        this.setMargeAccessoire(dtoChalet.margeAccessoire);
        this.setMargeSupplementaireRetrait(dtoChalet.margeSupplementaireRetrait);
        
        if (this.longueur - oldLongueur != 0.0) {
            for (Accessoire a : this.getMur(TypeMur.Gauche).getAccessoires())
                a.updateWithLongueur(oldLongueur, this.longueur);
            for (Accessoire a : this.getMur(TypeMur.Droit).getAccessoires())
                a.updateWithLongueur(oldLongueur, this.longueur);
        }
        
        if (this.largeur - oldLargeur != 0.0) {
            for (Accessoire a : this.getMur(TypeMur.Facade).getAccessoires())
                a.updateWithLongueur(oldLargeur, this.largeur);
            for (Accessoire a : this.getMur(TypeMur.Arriere).getAccessoires())
                a.updateWithLongueur(oldLargeur, this.largeur);
        }
        
        if (this.hauteur - oldHauteur != 0.0) {
            for (Mur m : this.getMurs())
                for (Accessoire a : m.getAccessoires())
                    if (a.getAccessoireType() != TypeAccessoire.Porte) // Les portes restent collées au sol
                        a.updateWithHauteur(oldHauteur, this.hauteur);
        }

        for (Mur mur : this.getMurs()) {
            mur.updateValiditeAccessoires(margeAccessoire, epaisseurMur);
        }
    }

    public Accessoire getAccessoire(UUID uuid) {
        for (Mur mur : this.murs) {
            for (Accessoire accessoire : mur.getAccessoires()) {
                if (accessoire.getAccessoireId().equals(uuid)) {
                    return accessoire;
                }
            }
        }
        return null;
    }

    public List<Accessoire> getAccessoires() {
        List<Accessoire> accessoires = new ArrayList<Accessoire>();
        for (Mur mur : this.murs) {
            for (Accessoire accessoire : mur.getAccessoires()) {
                accessoires.add(accessoire);
            }
        }

        return accessoires;
    }

    public List<Accessoire> getAccessoires(TypeMur typeMur) {
        Mur mur = this.getMur(typeMur);
        List<Accessoire> accessoires = new ArrayList<Accessoire>();
        for (Accessoire accessoire : mur.getAccessoires()) {
            accessoires.add(accessoire);
        }

        return accessoires;
    }

    public List<Accessoire.AccessoireDTO> getAccessoireDTOs() {
        List<Accessoire.AccessoireDTO> accessoireDTOs = new ArrayList<Accessoire.AccessoireDTO>();
        for (Mur mur : this.murs) {
            for (Accessoire accessoire : mur.getAccessoires()) {
                accessoireDTOs.add(accessoire.toDTO());
            }
        }

        return accessoireDTOs;
    }

    public List<Accessoire.AccessoireDTO> getAccessoireDTOs(TypeMur typeMur) {
        Mur mur = this.getMur(typeMur);
        List<Accessoire.AccessoireDTO> accessoireDTOs = new ArrayList<Accessoire.AccessoireDTO>();
        for (Accessoire accessoire : mur.getAccessoires()) {
            accessoireDTOs.add(accessoire.toDTO());
        }

        return accessoireDTOs;
    }


    /**
     * Permet d'ajouter un accessoire au chalet
     *
     * @param typeMur typpe du mur
     * @param typeAccessoire type du mur
     * @param position position de l'accessoire
     * @param dimension dimension de l'accesoire
     * @return l'accessoire en question
     */
    public Accessoire ajouterAccessoire(TypeMur typeMur, TypeAccessoire typeAccessoire, double[] position, double[] dimension) {
        Accessoire accessoire = new Accessoire(typeAccessoire, typeMur, position, dimension, true);
        accessoire.setAccessoireNom(typeAccessoire.toString());
        accessoire.setAccessoireId(UUID.randomUUID());

        // By default, translate the accessory to the margin specified in the chalet
        accessoire.setPosition(new double[]{margeAccessoire + this.epaisseurMur, margeAccessoire});

        Mur mur = getMur(accessoire.getTypeMur());

        mur.ajouterAccessoire(accessoire);
        mur.updateValiditeAccessoires(margeAccessoire, epaisseurMur);

        return accessoire;
    }

    /**
     * Permet de modifier un accessoire
     *
     * @param accessoireDTO le DTO de l'accessoire qu'on veut modifier
     * @return l'accessoire en question
     */
    public Accessoire updateAccessoire(Accessoire.AccessoireDTO accessoireDTO) {
        Mur mur = getMur(accessoireDTO.typeMur);
        Accessoire accessoire = mur.updateAccessoire(accessoireDTO);
        mur.updateValiditeAccessoires(margeAccessoire, epaisseurMur);

        return accessoire;
    }

    /**
     * Permet de retirer un accessoire au chalet
     *
     * @param typeMur typpe du mur
     * @param uuid uuid de l'accessoire
     * @return l'accessoire qui a été retiré
     */
    public Accessoire retirerAccessoire(TypeMur typeMur, UUID uuid) {
        Mur mur = getMur(typeMur);
        Accessoire accessoire = mur.retirerAccessoire(uuid);
        mur.updateValiditeAccessoires(margeAccessoire, epaisseurMur);

        return accessoire;
    }

    public Accessoire retirerAccessoire(UUID uuid) {
        Accessoire accessoire = getAccessoire(uuid);
        return retirerAccessoire(accessoire.getTypeMur(), uuid);
    }

    public List<Accessoire> retirerAccessoires(List<Accessoire.AccessoireDTO> accessoireDTOs) {
        List<Accessoire> accessoires = new ArrayList<Accessoire>();

        for (Accessoire.AccessoireDTO accessoireDTO : accessoireDTOs) {
            Accessoire removed = getMur(accessoireDTO.typeMur).retirerAccessoire(accessoireDTO.accessoireId);
            if (removed != null) {
                accessoires.add(removed);
            }
        }

        for (Mur mur : getMurs()) {
            mur.updateValiditeAccessoires(margeAccessoire, epaisseurMur);
        }

        return accessoires;
    }

    public ChaletDTO toDTO() {
        return new ChaletDTO(this);
    }

    public static class ChaletDTO {
        public String nom;
        public double hauteur;
        public double largeur;
        public double longueur;
        public double epaisseurMur;
        public TypeSensToit sensToit;
        public double angleToit;
        public double margeAccessoire;
        public double margeSupplementaireRetrait;

        public ChaletDTO(Chalet chalet) {
            this.nom = chalet.nom;
            this.hauteur = chalet.hauteur;
            this.largeur = chalet.largeur;
            this.longueur = chalet.longueur;
            this.epaisseurMur = chalet.epaisseurMur;
            this.sensToit = chalet.sensToit;
            this.angleToit = chalet.angleToit;
            this.margeAccessoire = chalet.margeAccessoire;
            this.margeSupplementaireRetrait = chalet.margeSupplementaireRetrait;
        }   
    }

    /* Inclus aussi les DTOs pour les murs, ainsi que leurs accessoires. */
    public static class ChaletCompletDTO implements Serializable {
        public String nom;
        public double hauteur;
        public double largeur;
        public double longueur;
        public double epaisseurMur;
        public TypeSensToit sensToit;
        public double angleToit;
        public double margeAccessoire;
        public double margeSupplementaireRetrait;
        public Mur.MurDTO[] murs;

        public ChaletCompletDTO(Chalet chalet) {
            this.nom = chalet.nom;
            this.hauteur = chalet.hauteur;
            this.largeur = chalet.largeur;
            this.longueur = chalet.longueur;
            this.epaisseurMur = chalet.epaisseurMur;
            this.sensToit = chalet.sensToit;
            this.angleToit = chalet.angleToit;
            this.margeAccessoire = chalet.margeAccessoire;
            this.margeSupplementaireRetrait = chalet.margeSupplementaireRetrait;
            this.murs = new Mur.MurDTO[chalet.murs.length];
            for (int i = 0; i < chalet.murs.length; i++) {
                this.murs[i] = chalet.murs[i].toDTO();
            }
        }
        
        public void writeObject(ObjectOutputStream oos) {
            try {
                oos.writeObject(nom);
                oos.writeObject(hauteur);
                oos.writeObject(largeur);
                oos.writeObject(longueur);
                oos.writeObject(epaisseurMur);
                String sensToitString;
                switch (sensToit) {
                    case Nord:
                        sensToitString = "Nord";
                        break;
                    case Sud:
                        sensToitString = "Sud";
                        break;
                    case Est:
                        sensToitString = "Est";
                        break;
                    case Ouest:
                        sensToitString = "Ouest";
                        break;
                    default:
                        sensToitString = "";
                        break;
                }
                oos.writeObject(sensToitString);
                oos.writeObject(angleToit);
                oos.writeObject(margeAccessoire);
                oos.writeObject(margeSupplementaireRetrait);
            }
            catch (IOException e) {
            }
        }
        
        public void readObject(ObjectInputStream ois) {
            try {
                nom = (String) ois.readObject();
                hauteur = (double) ois.readObject();
                largeur = (double) ois.readObject();
                longueur = (double) ois.readObject();
                epaisseurMur = (double) ois.readObject();
                String sensToitString = (String) ois.readObject();
                switch (sensToitString) {
                    case "Nord":
                        sensToit = TypeSensToit.Nord;
                        break;
                    case "Sud":
                        sensToit = TypeSensToit.Sud;
                        break;
                    case "Est":
                        sensToit = TypeSensToit.Est;
                        break;
                    case "Ouest":
                        sensToit = TypeSensToit.Ouest;
                        break;
                    default:
                        break;
                }
                angleToit = (double) ois.readObject();
                margeAccessoire = (double) ois.readObject();
                margeSupplementaireRetrait = (double) ois.readObject();
            }
            catch (IOException | ClassNotFoundException e) {
            }
        }
    }

    public static Chalet fromDTO(ChaletDTO dto) {
        return new Chalet(dto);
    }

    public static Chalet fromDTO(ChaletCompletDTO dto) {
        return new Chalet(dto);
    }
}

