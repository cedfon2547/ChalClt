package ca.ulaval.glo2004.domaine;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ca.ulaval.glo2004.domaine.utils.UndoRedoManager;

public class Controleur {
    private static Controleur instance = null;
    private ChalCLTProjet projectActif;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private UndoRedoManager undoRedoManager = new UndoRedoManager();

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
        Chalet.ChaletDTO chaletDTO = this.projectActif.getChalet().toDTO();
        this.pcs.firePropertyChange(EventType.CHALET, chaletDTO, chalet);

        undoRedoManager.addUndoRedoAction(() -> {
            this.projectActif.getChalet().updateChalet(chaletDTO);
        }, () -> {
            this.projectActif.getChalet().updateChalet(chalet);
        });

       
        projectActif.getChalet().updateChalet(chalet);
    }

    public void setAccessoire(TypeMur mur,Accessoire.AccessoireDTO accessoireDTO){
        this.pcs.firePropertyChange(EventType.ACCESSOIRE, this.getAccessoire(accessoireDTO.accessoireId), accessoireDTO);// à vérifier si ok
        projectActif.getChalet().getMur(mur).getAccessoire(accessoireDTO.accessoireId).updateAccessoire(accessoireDTO);
        // undoRedoManager.addUndoRedoAction(() -> {
            
        // }, () -> {

        // });

    }

    public PreferencesUtilisateur.PreferencesUtilisateurDTO getPreferencesUtilisateur() {
        return projectActif.getPreferencesUtilisateur().toDTO();
    }

    public void setPreferencesUtilisateur(PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateur) {
        projectActif.getPreferencesUtilisateur().update(preferencesUtilisateur);
    }

    public void ajouterAccessoire(TypeMur mur, TypeAccessoire typeAcc, double[] position, double[] dimension) {
        Accessoire accessoire = projectActif.getChalet().getMur(mur).ajouterAccessoire(typeAcc, position, dimension);
        this.pcs.firePropertyChange(EventType.AJOUTER_ACCESSOIRE, null, accessoire.toDTO());

        // undoRedoManager.addUndoRedoAction(() -> {
        //     projectActif.getChalet().getMur(mur).retirerAccessoire(accessoire.getAccessoireId());
        //     pcs.firePropertyChange(EventType.SUPPRIMER_ACCESSOIRE, null, accessoire.toDTO());
        // }, () -> {
        //     projectActif.getChalet().getMur(mur).ajouterAccessoire(accessoire);
        //     pcs.firePropertyChange(EventType.AJOUTER_ACCESSOIRE, null, accessoire.toDTO());
        // });
    }

    public void supprimerAccessoire(TypeMur mur, UUID uuid){   
        Accessoire accessoire = projectActif.getChalet().getMur(mur).retirerAccessoire(uuid);
        this.pcs.firePropertyChange(EventType.SUPPRIMER_ACCESSOIRE, null, accessoire.toDTO());
    }

    public void supprimerAccessoires(List<Accessoire.AccessoireDTO> accessoires) {
        for (Accessoire.AccessoireDTO accessoire : accessoires) {
            this.projectActif.getChalet().getMur(accessoire.typeMur).retirerAccessoire(accessoire.accessoireId);
        }

        this.pcs.firePropertyChange(EventType.SUPPRIMER_ACCESSOIRES, null, accessoires);
    }

    public void creeProjet(){
        if(this.projectActif != null){
            fermerProjet();
        }
        this.projectActif = new ChalCLTProjet(null);
        this.pcs.firePropertyChange(EventType.CREE_PROJET, null, this.getChalet());
    }

    public void fermerProjet(){
        //TODO
        
    }

    public void undo() {
        this.undoRedoManager.undo();
    }

    public void redo() {
        this.undoRedoManager.redo();
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
        public static final String SUPPRIMER_ACCESSOIRES = "supprimerAccessoires";
        public static final String CREE_PROJET = "creeProjet";
        public static final String FERMER_PROJET = "fermerProjet";
    }
}
