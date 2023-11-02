package ca.ulaval.glo2004.domaine;

public class Chalet {
    private String nom = "ChaletDefault";
    private double margeSupplementaireRetrait = 0.0;
    private double hauteur = 96;
    private double largeur = 120;
    private double longueur = 120;
    private double epaisseurMur = 6.0;
    private TypeSensToit sensToit = TypeSensToit.Nord;
    private double angleToit = 20.0;
    private double margeAccessoire = 3.0;


    // TODO: rallon, pignon
    private Mur[] murs = new Mur[] { new Mur(TypeMur.Facade, hauteur, largeur), new Mur(TypeMur.Arriere, hauteur, largeur), new Mur(TypeMur.Droit, hauteur, longueur), new Mur(TypeMur.Gauche, hauteur, longueur) };
    // private Toit toit = new Toit();

    public Chalet() {}

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

    public void updateChalet(ChaletDTO dtoChalet) {
        this.nom = dtoChalet.nom;
        this.hauteur = dtoChalet.hauteur;
        this.largeur = dtoChalet.largeur;
        this.longueur = dtoChalet.longueur;
        this.epaisseurMur = dtoChalet.epaisseurMur;
        this.sensToit = dtoChalet.sensToit;
        this.angleToit = dtoChalet.angleToit;
        this.margeAccessoire = dtoChalet.margeAccessoire;
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
