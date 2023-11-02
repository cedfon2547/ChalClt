package ca.ulaval.glo2004.domaine;

public class ChalCLTProjet {
    private String nom;
    private Chalet chalet;
    private PreferencesUtilisateur preferencesUtilisateur;

    public ChalCLTProjet(String nom, Chalet chalet, PreferencesUtilisateur preferencesUtilisateur) {
        this.nom = nom;
        this.chalet = chalet;
        this.preferencesUtilisateur = preferencesUtilisateur;
    }

    public ChalCLTProjet(String nom) {
        this(nom, new Chalet(), new PreferencesUtilisateur());
    }

    public String getNom() {
        return nom;
    }

    public Chalet getChalet() {
        return chalet;
    }

    public PreferencesUtilisateur getPreferencesUtilisateur() {
        return preferencesUtilisateur;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPreferencesUtilisateur(PreferencesUtilisateur preferencesUtilisateur) {
        this.preferencesUtilisateur = preferencesUtilisateur;
    }

    public static class ChalCLTProjetDTO {
        public String nom;
        public Chalet.ChaletDTO chalet;
        public PreferencesUtilisateur preferencesUtilisateur;

        public ChalCLTProjetDTO(ChalCLTProjet chalCLTProjet) {
            this.nom = chalCLTProjet.nom;
            this.chalet = chalCLTProjet.chalet.toDTO();
            this.preferencesUtilisateur = chalCLTProjet.preferencesUtilisateur;
        }
    }
}