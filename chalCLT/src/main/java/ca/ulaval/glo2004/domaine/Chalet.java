package ca.ulaval.glo2004.domaine;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Chalet {
    private String nom = "ChaletDefault";
    private double margeSupplementaireRetrait = 1.0;
    private double hauteur = 96;
    private double largeur = 120;
    private double longueur = 120;
    private double epaisseurMur = 12.0; // Test purpose
    private TypeSensToit sensToit = TypeSensToit.Nord;
    private double angleToit = 20.0;
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
    
    public void updateChalet(ChaletDTO dtoChalet) {
        this.nom = dtoChalet.nom;
        this.hauteur = dtoChalet.hauteur;
        this.largeur = dtoChalet.largeur;
        this.longueur = dtoChalet.longueur;
        this.epaisseurMur = dtoChalet.epaisseurMur;
        this.sensToit = dtoChalet.sensToit;
        this.angleToit = dtoChalet.angleToit;
        this.margeAccessoire = dtoChalet.margeAccessoire;
        this.margeSupplementaireRetrait = dtoChalet.margeSupplementaireRetrait;

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

    public Accessoire ajouterAccessoire(TypeMur typeMur, TypeAccessoire typeAccessoire, double[] position, double[] dimension) {
        Accessoire accessoire = new Accessoire(typeAccessoire, typeMur, position, dimension);

        // By default, translate the accessory to the margin specified in the chalet
        accessoire.setPosition(new double[]{margeAccessoire + this.epaisseurMur, margeAccessoire});

        Mur mur = getMur(accessoire.getTypeMur());

        mur.ajouterAccessoire(accessoire);
        mur.updateValiditeAccessoires(margeAccessoire, epaisseurMur);

        return accessoire;
    }

    public Accessoire updateAccessoire(Accessoire.AccessoireDTO accessoireDTO) {
        Mur mur = getMur(accessoireDTO.typeMur);
        Accessoire accessoire = mur.updateAccessoire(accessoireDTO);
        mur.updateValiditeAccessoires(margeAccessoire, epaisseurMur);

        return accessoire;
    }

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

    public static class ChaletDTO implements java.io.Serializable {
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
    public static class ChaletCompletDTO implements java.io.Serializable {
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
        // public Toit.ToitDTO toit; TODO: a completer

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
            // this.toit = chalet.toit.toDTO();
        }
    }

    public static Chalet fromDTO(ChaletDTO dto) {
        return new Chalet(dto);
    }
}

