package ca.ulaval.glo2004.domaine;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
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

    public List<Accessoire.AccessoireDTO> getAccessoires() {
        List<Accessoire.AccessoireDTO> accessoires = new ArrayList<>();
        for (Mur mur: this.projectActif.getChalet().getMurs()) {
            for (Accessoire accessoire: mur.getAccessoires()) {
                accessoires.add(accessoire.toDTO());
            }
        }

        return accessoires;
    }
    public void setChalet(Chalet.ChaletDTO chalet) {
        this.pcs.firePropertyChange(EventType.CHALET, this.projectActif.getChalet().toDTO(), chalet);
        projectActif.getChalet().updateChalet(chalet);
    }

    public void setAccessoire(TypeMur mur,Accessoire.AccessoireDTO accessoireDTO){
        this.pcs.firePropertyChange(EventType.ACCESSOIRE, this.getAccessoire(accessoireDTO.accessoireId), accessoireDTO);// à vérifier si ok
        projectActif.getChalet().getMur(mur).getAccessoire(accessoireDTO.accessoireId).updateAccessoire(accessoireDTO);
    }

    public PreferencesUtilisateur.PreferencesUtilisateurDTO getPreferencesUtilisateur() {
        return projectActif.getPreferencesUtilisateur().toDTO();
    }

    public void setPreferencesUtilisateur(PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateur) {
        projectActif.getPreferencesUtilisateur().update(preferencesUtilisateur);
    }

    public void ajouterAccessoire(TypeMur mur, TypeAccessoire typeAcc, double[] position, double[] dimension) {
        Accessoire accessoire = projectActif.getChalet().getMur(mur).AjouterAcessoire(typeAcc, position, dimension);
        this.pcs.firePropertyChange(EventType.AJOUTER_ACCESSOIRE, null, accessoire.toDTO());
    }

    public void supprimerAccessoire(TypeMur mur, UUID uuid){
        
        Accessoire accessoire = projectActif.getChalet().getMur(mur).retirerAccessoire(uuid);
        this.pcs.firePropertyChange(EventType.SUPPRIMER_ACCESSOIRE, null, accessoire.toDTO());
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

    public static class EventType {
        public static final String CHALET = "chalet";
        public static final String ACCESSOIRE = "accessoire";
        public static final String AJOUTER_ACCESSOIRE = "ajouterAccessoire";
        public static final String SUPPRIMER_ACCESSOIRE = "supprimerAccessoire";
    }
}
