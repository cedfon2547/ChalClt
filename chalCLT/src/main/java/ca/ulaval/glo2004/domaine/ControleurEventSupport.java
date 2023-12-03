package ca.ulaval.glo2004.domaine;

import java.util.ArrayList;
import java.util.List;

public class ControleurEventSupport {
    private List<UserPreferencesEventListener> userPreferencesEventListeners = new ArrayList<>();
    private List<ChaletEventListener> chaletEventListeners = new ArrayList<>();
    private List<AccessoireEventListener> accessoireEventListeners = new ArrayList<>();
    private List<ProjectEventListener> projectEventListeners = new ArrayList<>();
    private List<AccessoirValidityEventListener> accessoirValidityEventListeners = new ArrayList<>();

    public void addUserPreferencesEventListener(UserPreferencesEventListener listener) {
        userPreferencesEventListeners.add(listener);
    }

    public void addChaletEventListener(ChaletEventListener listener) {
        chaletEventListeners.add(listener);
    }

    public void addAccessoireEventListener(AccessoireEventListener listener) {
        accessoireEventListeners.add(listener);
    }

    public void addProjectEventListener(ProjectEventListener listener) {
        projectEventListeners.add(listener);
    }

    public void addAccessoirValidityEventListener(AccessoirValidityEventListener listener) {
        accessoirValidityEventListeners.add(listener);
    }

    public void removeUserPreferencesEventListener(UserPreferencesEventListener listener) {
        userPreferencesEventListeners.remove(listener);
    }

    public void removeChaletEventListener(ChaletEventListener listener) {
        chaletEventListeners.remove(listener);
    }

    public void removeAccessoireEventListener(AccessoireEventListener listener) {
        accessoireEventListeners.remove(listener);
    }

    public void removeProjectEventListener(ProjectEventListener listener) {
        projectEventListeners.remove(listener);
    }

    public void removeAccessoirValidityEventListener(AccessoirValidityEventListener listener) {
        accessoirValidityEventListeners.remove(listener);
    }

    public void dispatchUserPreferencesChangeEvent(
            PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO) {
        UserPreferencesEvent event = new UserPreferencesEvent(preferencesUtilisateurDTO);
        for (UserPreferencesEventListener listener : userPreferencesEventListeners) {
            listener.change(event);
        }
    }

    public void dispatchChaletChangeEvent(Chalet.ChaletDTO chaletDTO) {
        ChaletEvent event = new ChaletEvent(chaletDTO);
        for (ChaletEventListener listener : chaletEventListeners) {
            listener.change(event);
        }
    }

    public void dispatchAccessoireChangeEvent(Accessoire.AccessoireDTO accessoireDTO) {
        AccessoireEvent event = new AccessoireEvent(accessoireDTO);
        for (AccessoireEventListener listener : accessoireEventListeners) {
            listener.change(event);
        }
    }

    public void dispatchAccessoireAddEvent(Accessoire.AccessoireDTO accessoireDTO) {
        AccessoireEvent event = new AccessoireEvent(accessoireDTO);
        for (AccessoireEventListener listener : accessoireEventListeners) {
            listener.add(event);
        }
    }

    public void dispatchAccessoireRemoveEvent(Accessoire.AccessoireDTO accessoireDTO) {
        AccessoireEvent event = new AccessoireEvent(accessoireDTO);
        for (AccessoireEventListener listener : accessoireEventListeners) {
            listener.remove(event);
        }
    }

    public void dispatchProjectCreateEvent(String projectName) {
        ProjectEvent event = new ProjectEvent(projectName);
        for (ProjectEventListener listener : projectEventListeners) {
            listener.create(event);
        }
    }

    public void dispatchProjectCloseEvent(String projectName) {
        ProjectEvent event = new ProjectEvent(projectName);
        for (ProjectEventListener listener : projectEventListeners) {
            listener.close(event);
        }
    }

    public void dispatchAccessoirValidityChangeEvent(List<Accessoire.AccessoireDTO> accessoireDTOs) {
        AccessoirValidityEvent event = new AccessoirValidityEvent(accessoireDTOs);
        for (AccessoirValidityEventListener listener : accessoirValidityEventListeners) {
            listener.change(event);
        }
    }

    public static enum ControleurEvent {
        UpdateUserPreferences,
        UpdateChalet,
        UpdateAccessoire,
        AddAccessoire,
        RemoveAccessoire,
        CreateProject,
        CloseProject,
        InvalidAccessoires,
    }

    public static class UserPreferencesEvent {
        public PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO;

        public UserPreferencesEvent(PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO) {
            this.preferencesUtilisateurDTO = preferencesUtilisateurDTO;
        }

        public PreferencesUtilisateur.PreferencesUtilisateurDTO getPreferencesUtilisateurDTO() {
            return preferencesUtilisateurDTO;
        }
    }

    public static class ChaletEvent {
        public Chalet.ChaletDTO chaletDTO;

        public ChaletEvent(Chalet.ChaletDTO chaletDTO) {
            this.chaletDTO = chaletDTO;
        }

        public Chalet.ChaletDTO getChaletDTO() {
            return chaletDTO;
        }
    }

    public static class AccessoireEvent {
        public Accessoire.AccessoireDTO accessoireDTO;

        public AccessoireEvent(Accessoire.AccessoireDTO accessoireDTO) {
            this.accessoireDTO = accessoireDTO;
        }

        public Accessoire.AccessoireDTO getAccessoireDTO() {
            return accessoireDTO;
        }
    }

    public static class ProjectEvent {
        String projectName;

        public ProjectEvent(String projectName) {
            this.projectName = projectName;
        }

        public String getProjectName() {
            return projectName;
        }
    }

    public static class AccessoirValidityEvent {
        public List<Accessoire.AccessoireDTO> accessoireDTOs;

        public AccessoirValidityEvent(List<Accessoire.AccessoireDTO> accessoireDTOs) {
            this.accessoireDTOs = accessoireDTOs;
        }

        public List<Accessoire.AccessoireDTO> getAccessoireDTOs() {
            return accessoireDTOs;
        }
    }

    public interface UserPreferencesEventListener {
        public void change(UserPreferencesEvent event);
    }

    public interface ChaletEventListener {
        public void change(ChaletEvent event);
    }

    public interface AccessoireEventListener {
        public void change(AccessoireEvent event);

        public void add(AccessoireEvent event);

        public void remove(AccessoireEvent event);
    }

    public interface ProjectEventListener {
        public void create(ProjectEvent event);

        public void close(ProjectEvent event);
    }

    public interface AccessoirValidityEventListener {
        public void change(AccessoirValidityEvent event);
    }
}
