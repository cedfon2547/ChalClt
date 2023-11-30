package ca.ulaval.glo2004.domaine.afficheur;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMeshGroup;

public class AfficheurEventSupport {
    private List<ZoomEventListener> zoomEventListeners = new ArrayList<ZoomEventListener>();
    private List<MeshMouseListener> meshMouseListeners = new ArrayList<MeshMouseListener>();
    private List<MeshSelectionListener> meshSelectionListeners = new ArrayList<MeshSelectionListener>();
    private List<ViewChangedListener> viewChangedListeners = new ArrayList<ViewChangedListener>();
    private List<CameraListener> cameraListeners = new ArrayList<CameraListener>();

    public void addZoomEventListener(ZoomEventListener listener) {
        zoomEventListeners.add(listener);
    }

    public void addMeshMouseListener(MeshMouseListener listener) {
        meshMouseListeners.add(listener);
    }

    public void addMeshSelectionListener(MeshSelectionListener listener) {
        meshSelectionListeners.add(listener);
    }

    public void addViewChangedListener(ViewChangedListener listener) {
        viewChangedListeners.add(listener);
    }

    public void addCameraListener(CameraListener listener) {
        cameraListeners.add(listener);
    }

    public void dispatchZoomIn() {
        for (ZoomEventListener listener : zoomEventListeners) {
            listener.zoomIn();
        }
    }

    public void dispatchZoomOut() {
        for (ZoomEventListener listener : zoomEventListeners) {
            listener.zoomOut();
        }
    }

    public void dispatchMeshClicked(MeshMouseEvent e) {
        for (MeshMouseListener listener : meshMouseListeners) {
            listener.meshClicked(e);
        }
    }

    public void dispatchMeshHovered(MeshMouseMotionEvent e) {
        for (MeshMouseListener listener : meshMouseListeners) {
            listener.meshHovered(e);
        }
    }

    public void dispatchMeshDragged(MeshMouseMotionEvent e) {
        for (MeshMouseListener listener : meshMouseListeners) {
            listener.meshDragged(e);
        }
    }

    public void dispatchMouseEnterMesh(MeshMouseMotionEvent e) {
        for (MeshMouseListener listener : meshMouseListeners) {
            listener.mouseEnterMesh(e);
        }
    }

    public void dispatchMouseExitMesh(MeshMouseMotionEvent e) {
        for (MeshMouseListener listener : meshMouseListeners) {
            listener.mouseExitMesh(e);
        }
    }

    public void dispatchSelectionChanged(MeshSelectionEvent e) {
        for (MeshSelectionListener listener : meshSelectionListeners) {
            listener.selectionChanged(e);
        }
    }

    public void dispatchViewChanged(ViewChangedEvent e) {
        for (ViewChangedListener listener : viewChangedListeners) {
            listener.viewChanged(e);
        }
    }

    public void dispatchCameraDirectionChanged(CameraEvent e) {
        for (CameraListener listener : cameraListeners) {
            listener.cameraDirectionChanged(e);
        }
    }

    public void dispatchCameraPositionChanged(CameraEvent e) {
        for (CameraListener listener : cameraListeners) {
            listener.cameraPositionChanged(e);
        }
    }

    public void removeZoomEventListener(ZoomEventListener listener) {
        zoomEventListeners.remove(listener);
    }

    public void removeMeshMouseListener(MeshMouseListener listener) {
        meshMouseListeners.remove(listener);
    }

    public void removeMeshSelectionListener(MeshSelectionListener listener) {
        meshSelectionListeners.remove(listener);
    }

    public void removeViewChangedListener(ViewChangedListener listener) {
        viewChangedListeners.remove(listener);
    }

    public void removeCameraListener(CameraListener listener) {
        cameraListeners.remove(listener);
    }

    public static enum AfficheurEvent {
        Zoom,
        MeshClicked,
        MeshHovered,
        MeshDragged,
        SelectionChanged,
        VueChanged,
        CameraDirectionChanged,
        CameraPositionChanged,
        MouseEnterMesh,
        MouseExitMesh;
    }

    public static class MeshMouseEvent extends MouseEvent {
        private TriangleMeshGroup mesh;
        public MeshMouseEvent(MouseEvent e, TriangleMeshGroup mesh) {
            super(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(),
                    e.isPopupTrigger(), e.getButton());
            this.mesh = mesh;
        }

        public TriangleMeshGroup getMesh() {
            return mesh;
        }

    }

    public static class MeshMouseMotionEvent extends MouseEvent {
        private TriangleMeshGroup mesh;
        public MeshMouseMotionEvent(MouseEvent e, TriangleMeshGroup mesh) {
            super(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(),
                    e.isPopupTrigger(), e.getButton());
            this.mesh = mesh;
        }

        public TriangleMeshGroup getMesh() {
            return mesh;
        }
    }

    public static class MeshSelectionEvent {
        private List<String> selectedMeshIDs = new ArrayList<String>();

        public MeshSelectionEvent(List<TriangleMeshGroup> selectedMeshes) {
            for (TriangleMeshGroup mesh : selectedMeshes) {
                selectedMeshIDs.add(mesh.ID);
            }
        }

        public List<String> getSelectedMeshIDs() {
            return selectedMeshIDs;
        }
    }

    public static class ViewChangedEvent {
        private Afficheur.TypeDeVue vue;

        public ViewChangedEvent(Afficheur.TypeDeVue vue) {
            this.vue = vue;
        }

        public Afficheur.TypeDeVue getVue() {
            return vue;
        }
    }

    public static class CameraEvent {
        private Vector3D position;
        private Vector3D direction;

        public CameraEvent(Vector3D position, Vector3D direction) {
            this.position = position;
            this.direction = direction;
        }

        public Vector3D getPosition() {
            return position;
        }

        public Vector3D getDirection() {
            return direction;
        }
    }

    public static interface ZoomEventListener {
        public void zoomIn();
        public void zoomOut();
    }

    public static interface MeshMouseListener {
        public void meshClicked(MeshMouseEvent e);
        public void meshHovered(MeshMouseMotionEvent e);
        public void meshDragged(MeshMouseMotionEvent e);
        public void mouseEnterMesh(MeshMouseMotionEvent e);
        public void mouseExitMesh(MeshMouseMotionEvent e);
    }

    public static interface MeshSelectionListener {
        public void selectionChanged(MeshSelectionEvent e);
    }

    public static interface ViewChangedListener {
        public void viewChanged(ViewChangedEvent e);
    }

    public static interface CameraListener {
        public void cameraDirectionChanged(CameraEvent e);
        public void cameraPositionChanged(CameraEvent e);
    }

    @FunctionalInterface
    public static interface ZoomInCb {
        public void call();
    }

    @FunctionalInterface
    public static interface ZoomOutCb {
        public void call();
    }

    @FunctionalInterface
    public static interface MeshClickedCb {
        public void call(MeshMouseEvent e);
    }

    @FunctionalInterface
    public static interface MeshHoveredCb {
        public void call(MeshMouseMotionEvent e);
    }

    @FunctionalInterface
    public static interface MeshDraggedCb {
        public void call(MeshMouseMotionEvent e);
    }

    @FunctionalInterface
    public static interface MouseEnterMeshCb {
        public void call(MeshMouseMotionEvent e);
    }

    @FunctionalInterface
    public static interface MouseExitMeshCb {
        public void call(MeshMouseMotionEvent e);
    }

    @FunctionalInterface
    public static interface SelectionChangedCb {
        public void call(MeshSelectionEvent e);
    }

    @FunctionalInterface
    public static interface ViewChangedCb {
        public void call(ViewChangedEvent e);
    }

    @FunctionalInterface
    public static interface CameraDirectionChangedCb {
        public void call(CameraEvent e);
    }

    @FunctionalInterface
    public static interface CameraPositionChangedCb {
        public void call(CameraEvent e);
    }
}

