package ca.ulaval.glo2004.domaine;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class ChalCLTProjet implements Serializable {
    private String nom;
    private Chalet chalet;
    private PreferencesUtilisateur preferencesUtilisateur;

    public ChalCLTProjet(String nom, Chalet chalet, PreferencesUtilisateur preferencesUtilisateur) {
        this.nom = nom;
        this.chalet = chalet;
        this.preferencesUtilisateur = preferencesUtilisateur;
    }

    public ChalCLTProjet(ChalCLTProjetDTO chalCLTDTO) {
        this.nom = chalCLTDTO.nom;
        this.chalet = new Chalet(chalCLTDTO.chalet);
        this.preferencesUtilisateur = new PreferencesUtilisateur(chalCLTDTO.preferencesUtilisateur);
    }

    public ChalCLTProjet(String nom) {
        this(nom, new Chalet(), new PreferencesUtilisateur());
    }

    public ChalCLTProjet() {
        this("NouveauProjet", new Chalet(), new PreferencesUtilisateur());
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

    public void setChalet(Chalet chalet) {
        this.chalet = chalet;
    }
    
    public static class ChalCLTProjetDTO implements Serializable {
        public String nom;
        public Chalet.ChaletCompletDTO chalet;
        public PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateur;

        public ChalCLTProjetDTO(ChalCLTProjet chalCLTProjet) {
            this.nom = chalCLTProjet.nom;
            this.chalet = new Chalet.ChaletCompletDTO(chalCLTProjet.chalet);
        }
         

        
        public void writeObject(ObjectOutputStream oos) {
            try {
                oos.writeObject(this.nom);
                oos.writeObject(this.chalet);
              }
            catch (IOException e) {
                // jsp
            }
        }

        public void readObject(ObjectInputStream ois) {
            try {
                this.nom = (String) ois.readObject();
                this.chalet = (Chalet.ChaletCompletDTO) ois.readObject();
              }
            catch (IOException | ClassNotFoundException e) {
                // jsp
         
            }
        }
        
    }
}
