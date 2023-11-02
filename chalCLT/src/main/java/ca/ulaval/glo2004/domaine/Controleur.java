package ca.ulaval.glo2004.domaine;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.UUID;

public class Controleur {
    private static Controleur instance = null;
    private ChalCLTProjet projectActif;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    // private UndoRedoManager undoRedoManager;

    private Controleur() {
        this.projectActif = new ChalCLTProjet(null);
        // this.undoRedoManager = new UndoRedoManager();
    }

    public static Controleur getInstance() {
        if (instance == null) {
            instance = new Controleur();
        }
        return instance;
    }


    public Chalet.ChaletDTO getChalet() {
        return projectActif.getChalet().toDTO(); /* Return DTO */
    }

    public Accessoire.AccessoireDTO getAccessoire(UUID uuid){
        for (Mur mur: this.projectActif.getChalet().getMurs()){
            for(Accessoire accessoire: mur.getAccessoires()){
                if(accessoire.getAccessoireId().equals(uuid)){
                    return accessoire.toDTO();
                }
            }
        }
        return null;
    }

    public void setChalet(Chalet.ChaletDTO chalet) {
        // undoRedoManager.push(projectActif.getChalet());
        this.pcs.firePropertyChange("chalet", this.projectActif.getChalet().toDTO(), chalet);
        projectActif.getChalet().updateChalet(chalet);
    }

    public PreferencesUtilisateur.PreferencesUtilisateurDTO getPreferencesUtilisateur() {
        return projectActif.getPreferencesUtilisateur().toDTO();
    }

    public void setPreferencesUtilisateur(PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateur) {
        projectActif.getPreferencesUtilisateur().update(preferencesUtilisateur);
    }

    public void ajouterAccessoire(TypeMur mur, TypeAccessoire typeAcc, double[] position, double[] dimension) {
        projectActif.getChalet().getMur(mur).AjouterAcessoire(typeAcc, position, dimension);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(property, listener);
    }

    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(property, listener);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
}
