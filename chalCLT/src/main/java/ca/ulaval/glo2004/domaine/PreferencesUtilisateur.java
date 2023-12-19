package ca.ulaval.glo2004.domaine;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class PreferencesUtilisateur implements Serializable {
    private boolean afficherGrille = true;
    private boolean afficherVoisinSelection = false;
    private double gridSpacing = 50;
    private boolean afficherPlancher = false;

    public PreferencesUtilisateur() {}

    public PreferencesUtilisateur(boolean afficherGrille, boolean afficherVoisinSelection) {
        this.afficherGrille = afficherGrille;
        this.afficherVoisinSelection = afficherVoisinSelection;
    }

    public PreferencesUtilisateur(PreferencesUtilisateurDTO preferencesUtilisateurDTO) {
        this.afficherGrille = preferencesUtilisateurDTO.afficherGrille;
        this.afficherVoisinSelection = preferencesUtilisateurDTO.afficherVoisinSelection;
        this.gridSpacing = preferencesUtilisateurDTO.gridSpacing;
        this.afficherPlancher = preferencesUtilisateurDTO.afficherPlancher;
    }

    /**
     * Getter et setter
     */
    public boolean getAfficherPlancher(){return afficherPlancher;}

    public void setAfficherPlancher(boolean afficherPlancher) {
        this.afficherPlancher = afficherPlancher;
    }

    public boolean getAfficherGrille() {
        return afficherGrille;
    }

    public void setAfficherGrille(boolean afficherGrille) {
        this.afficherGrille = afficherGrille;
    }

    public boolean getAfficherVoisinSelection() {
        return afficherVoisinSelection;
    }

    public void setAfficherVoisinSelection(boolean afficherVoisinSelection) {
        this.afficherVoisinSelection = afficherVoisinSelection;
    }

    public double getGridSpacing() {
        return gridSpacing;
    }

    public void setGridSpacing(double gridSpacing) {
        this.gridSpacing = gridSpacing;
    }

    public void update(PreferencesUtilisateurDTO preferencesUtilisateurDTO) {
        this.afficherGrille = preferencesUtilisateurDTO.afficherGrille;
        this.afficherVoisinSelection = preferencesUtilisateurDTO.afficherVoisinSelection;
        this.gridSpacing = preferencesUtilisateurDTO.gridSpacing;
    }

    public PreferencesUtilisateurDTO toDTO() {
        return new PreferencesUtilisateurDTO(this);
    }

    public static class PreferencesUtilisateurDTO implements Serializable {
        public boolean afficherGrille = false;
        public boolean afficherVoisinSelection = false;
        public double gridSpacing = 30;
        public boolean afficherPlancher = false;

        public PreferencesUtilisateurDTO(PreferencesUtilisateur preferencesUtilisateur) {
            this.afficherGrille = preferencesUtilisateur.afficherGrille;
            this.afficherVoisinSelection = preferencesUtilisateur.afficherVoisinSelection;
            this.gridSpacing = preferencesUtilisateur.gridSpacing;
            this.afficherPlancher = preferencesUtilisateur.afficherPlancher;
        }
        
        public void writeObject(ObjectOutputStream oos) {
            try {
                oos.defaultWriteObject();
            }
            catch (IOException e) {
                // jsp
            }
        }
        
        public void readObject(ObjectInputStream ois) {
            try {
                ois.defaultReadObject();
            }
            catch (IOException | ClassNotFoundException e) {
                // jsp
            }
        }
    }
}
