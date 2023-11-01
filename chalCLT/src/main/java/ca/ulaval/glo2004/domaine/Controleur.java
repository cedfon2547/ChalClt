package ca.ulaval.glo2004.domaine;

import java.util.UUID;

public class Controleur {
    private static Controleur instance = null;
    private ChalCLTProjet projectActif;
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
        projectActif.getChalet().updateChalet(chalet);
    }

    public PreferencesUtilisateur getPreferencesUtilisateur() {
        return projectActif.getPreferencesUtilisateur();
    }

    public void setPreferencesUtilisateur(PreferencesUtilisateur preferencesUtilisateur) {
        projectActif.setPreferencesUtilisateur(preferencesUtilisateur);
    }
}
