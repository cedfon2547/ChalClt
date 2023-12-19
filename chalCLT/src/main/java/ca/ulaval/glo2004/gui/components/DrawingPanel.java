package ca.ulaval.glo2004.gui.components;

import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import ca.ulaval.glo2004.domaine.PreferencesUtilisateur;
import ca.ulaval.glo2004.domaine.TypeMur;
import ca.ulaval.glo2004.domaine.Chalet.ChaletDTO;
import ca.ulaval.glo2004.domaine.ControleurEventSupport.AccessoireEvent;
import ca.ulaval.glo2004.domaine.ControleurEventSupport.UserPreferencesEvent;
import ca.ulaval.glo2004.domaine.ControleurEventSupport.UserPreferencesEventListener;
import ca.ulaval.glo2004.domaine.afficheur.Afficheur;
import ca.ulaval.glo2004.domaine.afficheur.AfficheurEventSupport;
import ca.ulaval.glo2004.domaine.afficheur.AfficheurEventSupport.MeshMouseMotionEvent;
import ca.ulaval.glo2004.domaine.afficheur.AfficheurEventSupport.MeshSelectionEvent;
import ca.ulaval.glo2004.domaine.afficheur.AfficheurEventSupport.MeshSelectionListener;
import ca.ulaval.glo2004.domaine.afficheur.TypeDeVue;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMesh;
import ca.ulaval.glo2004.domaine.utils.PanelHelper.MurTriangleMeshGroup;
import ca.ulaval.glo2004.gui.MainWindow;
import ca.ulaval.glo2004.App;
import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.Controleur;
import ca.ulaval.glo2004.domaine.ControleurEventSupport;

class TestHoverComponant extends JPanel {
}

class GridStepSpinner extends JSpinner {
    public GridStepSpinner() {
        this(1.0f);
    }

    public GridStepSpinner(float value) {
        super();
        setValue(value);
        this.setModel(new SpinnerNumberModel((float) value, 0.1f, 100.0f, 0.5f));
        ((SpinnerNumberModel) this.getModel()).setStepSize(1.0);
        ((JSpinner.DefaultEditor) this.getEditor()).getTextField().setColumns(3);
        ((JSpinner.DefaultEditor) this.getEditor()).getTextField().setHorizontalAlignment(JLabel.CENTER);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        JFormattedTextField spin = ((JSpinner.DefaultEditor) this.getEditor()).getTextField();
        spin.setEditable(enabled);
    }

    @Override
    public void setValue(Object value) {
        if (this.isEnabled()) {
            super.setValue(value);
        }
    }
}

public class DrawingPanel extends javax.swing.JPanel {
    private MainWindow mainWindow;
    public Afficheur afficheur;

    public static final Color activeBtnColor = Color.DARK_GRAY;
    public static final Color inactiveBtnColor = Color.BLACK;

    Object[][] btns = new Object[][] {
            { "Dessus", TypeDeVue.Dessus.toString(), null },
            { "Façade", TypeDeVue.Facade.toString(), null },
            { "Arrière", TypeDeVue.Arriere.toString(), null },
            { "Droite", TypeDeVue.Droite.toString(), null },
            { "Gauche", TypeDeVue.Gauche.toString(), null },
    };
    private SwitchToggleButton toggleGridSwitch;
    private SwitchToggleButton toggleVoisinSwitch;
    private GridStepSpinner gridStepSpinner;

    private javax.swing.JToolBar barreOutilsVue;
    private MesureBruteContainer mesureBruteContainer;

    public DrawingPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.afficheur = new Afficheur(this.mainWindow.getControleur(), this);

        centeringLight();
        initComponents();
        afficheur.rechargerAffichage();
    }

    public void centeringLight() {
        this.afficheur.getScene().getLight().setPosition(new Vector3D((int) (this.getWidth() / 2), 500, 0));
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    private void initComponents() {
        setBackground(java.awt.Color.BLACK);
        this.mesureBruteContainer = new MesureBruteContainer(mainWindow.getControleur().getChalet());
        this.mainWindow.getControleur()
                .addAccessoireEventListener(new ControleurEventSupport.AccessoireEventListener() {
                    @Override
                    public void add(AccessoireEvent event) {
                        afficheur.rechargerAffichage();
                    }

                    @Override
                    public void remove(AccessoireEvent event) {
                        afficheur.rechargerAffichage();
                    }

                    @Override
                    public void change(AccessoireEvent event) {
                        afficheur.rechargerAffichage(); // turns out this is pretty critical
                    }
                });

        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                Vector3D lightPosition = afficheur.getScene().getLight().getPosition();
                int step = 100;

                switch (e.getKeyCode()) {
                    case java.awt.event.KeyEvent.VK_UP:
                        lightPosition.y -= step;
                        break;
                    case java.awt.event.KeyEvent.VK_DOWN:
                        lightPosition.y += step;
                        break;
                    case java.awt.event.KeyEvent.VK_LEFT:
                        lightPosition.x -= step;
                        break;
                    case java.awt.event.KeyEvent.VK_RIGHT:
                        lightPosition.x += step;
                        break;
                    case java.awt.event.KeyEvent.VK_PAGE_UP:
                        lightPosition.z += step;
                        break;
                    case java.awt.event.KeyEvent.VK_PAGE_DOWN:
                        lightPosition.z -= step;
                        break;
                    case java.awt.event.KeyEvent.VK_EQUALS:
                        afficheur.getScene().getLight()
                                .setIntensity(afficheur.getScene().getLight().getIntensity() + 0.01);
                        break;
                    case java.awt.event.KeyEvent.VK_MINUS:
                        afficheur.getScene().getLight()
                                .setIntensity(afficheur.getScene().getLight().getIntensity() - 0.01);
                        break;
                    default:
                        break;
                }

                afficheur.getScene().getLight().setPosition(lightPosition);
                repaint();
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {

            }

            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {

            }
        });

        this.afficheur.getEventSupport().addZoomEventListener(new AfficheurEventSupport.ZoomEventListener() {
            @Override
            public void zoomIn() {

            }

            @Override
            public void zoomOut() {

            }
        });

        this.afficheur.getEventSupport().addMeshMouseListener(new AfficheurEventSupport.MeshMouseListener() {
            Accessoire.AccessoireDTO initialAccessoireDTO;

            @Override
            public void meshClicked(AfficheurEventSupport.MeshMouseEvent evt) {
                // System.out.println("Mesh Clicked " + mesh.ID);
                if (evt.getClickCount() == 2) {
                    // System.out.println("Mesh Double Clicked " + evt.getMesh().ID);
                    if (evt.getMesh() instanceof MurTriangleMeshGroup) {
                        switch (((MurTriangleMeshGroup) evt.getMesh()).getTypeMur()) {
                            case Facade:
                                changerVue(TypeDeVue.Facade);
                                break;
                            case Arriere:
                                changerVue(TypeDeVue.Arriere);
                                break;
                            case Droit:
                                changerVue(TypeDeVue.Droite);
                                break;
                            case Gauche:
                                changerVue(TypeDeVue.Gauche);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            @Override
            public void meshDragged(AfficheurEventSupport.MeshMouseMotionEvent evt) {
                // System.out.println("Mesh Dragged " + evt.getMesh().ID);

                if (initialAccessoireDTO == null) {
                    if (evt.getMesh().ID.length() != 36)
                        return;

                    Accessoire.AccessoireDTO accessoireDTO = mainWindow.getControleur()
                            .getAccessoire(UUID.fromString(evt.getMesh().ID));
                    if (accessoireDTO == null)
                        return;

                    initialAccessoireDTO = accessoireDTO;
                }

                Accessoire.AccessoireDTO copy = initialAccessoireDTO.copy();

                switch (initialAccessoireDTO.typeMur) {
                    case Facade:
                        copy.position[0] -= evt.getDiffMeshPosition().x;
                        copy.position[1] += evt.getDiffMeshPosition().y;
                        break;
                    case Arriere:
                        copy.position[0] += evt.getDiffMeshPosition().x;
                        copy.position[1] += evt.getDiffMeshPosition().y;
                        break;
                    case Droit:
                        copy.position[0] += evt.getDiffMeshPosition().z;
                        copy.position[1] += evt.getDiffMeshPosition().y;
                        break;
                    case Gauche:
                        copy.position[0] -= evt.getDiffMeshPosition().z;
                        copy.position[1] += evt.getDiffMeshPosition().y;
                        break;
                }

                mainWindow.getControleur().setAccessoire(copy);
                Accessoire.AccessoireDTO accDto = mainWindow.getControleur()
                        .getAccessoire(UUID.fromString(evt.getMesh().ID));
                List<Accessoire.AccessoireDTO> accessoireDTOs = mainWindow.getControleur()
                        .getAccessoires(initialAccessoireDTO.typeMur);

                for (Accessoire.AccessoireDTO accessoireDTO : accessoireDTOs) {
                    TriangleMesh mesh = afficheur.getScene().getMesh(accessoireDTO.accessoireId.toString());
                    if (mesh != null) {
                        mesh.setValid(accessoireDTO.valide);
                    }
                }

                evt.getMesh().setValid(accDto.valide);
                evt.getMesh().setSelected(true);
                // evt.getMesh().setIsDragged(true);

                repaint();
            }

            @Override
            public void meshDragStart(MeshMouseMotionEvent evt) {
                // System.out.println("MeshDragStart " + evt.getMesh().ID);
                TriangleMesh clickedMesh = evt.getMesh();
                Accessoire.AccessoireDTO accDto = mainWindow.getControleur()
                        .getAccessoire(UUID.fromString(clickedMesh.ID));

                if (accDto != null) {
                    initialAccessoireDTO = accDto;
                    mainWindow.showAccessoireTable(accDto);
                    // clickedMesh.setSelected(true);
                    clickedMesh.setIsDragged(true);
                }

                mesureBruteContainer.hide();
            }

            @Override
            public void meshDragEnd(MeshMouseMotionEvent evt) {
                initialAccessoireDTO = null;
            }

            @Override
            public void meshHovered(AfficheurEventSupport.MeshMouseMotionEvent evt) {
                mesureBruteContainer.show(evt.getLocationOnScreen());
                if (evt.getMesh() == null) return;

                TriangleMesh hovered = evt.getMesh();
                if (hovered == null) return;

                if (hovered.ID.length() != 36) return; // not UUID format

                Accessoire.AccessoireDTO accDto = mainWindow.getControleur()
                        .getAccessoire(UUID.fromString(hovered.ID));

                if (accDto != null) {
                    setCursor(new Cursor(Cursor.MOVE_CURSOR));
                }
            }

            @Override
            public void mouseEnterMesh(AfficheurEventSupport.MeshMouseMotionEvent evt) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));

                if(evt.getMesh() instanceof MurTriangleMeshGroup){
                    ChaletDTO tempChaletDTO = mainWindow.getControleur().getChalet();
                    double tempLargeur = tempChaletDTO.largeur;
                    double tempLongueur = tempChaletDTO.longueur;
                    if (((MurTriangleMeshGroup) evt.getMesh()).getTypeMur() == TypeMur.Facade || ((MurTriangleMeshGroup) evt.getMesh()).getTypeMur() == TypeMur.Arriere){
                        mesureBruteContainer.updateValeurs(tempChaletDTO, tempLargeur);
                    }
                    else{
                        mesureBruteContainer.updateValeurs(tempChaletDTO, tempLongueur);
                    }
                }
                
                mesureBruteContainer.show(evt.getLocationOnScreen());
            }

            @Override
            public void mouseExitMesh(AfficheurEventSupport.MeshMouseMotionEvent evt) {
                mesureBruteContainer.hide();
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        this.afficheur.getEventSupport().addMeshSelectionListener(new AfficheurEventSupport.MeshSelectionListener() {
            @Override
            public void selectionChanged(AfficheurEventSupport.MeshSelectionEvent evt) {
                boolean isRoofOrWall = evt.getSelectedMeshIDs().stream().allMatch((id) -> {
                    TriangleMesh mesh = afficheur.getScene().getMesh(id);

                    if (mesh instanceof MurTriangleMeshGroup || mesh == afficheur.panneauToit
                            || mesh == afficheur.pignonDroitToit || mesh == afficheur.pignonGaucheToit
                            || mesh == afficheur.rallongeVerticaleToit) {
                        return true;
                    }

                    mainWindow.arbreDesComposantesChalet
                            .setSelectedAccessoire(mainWindow.getAccessoiresSelectionnees());

                    return false;
                });

                if (isRoofOrWall) {
                    System.out.println("Only Wall Selected");
                    mainWindow.clearAccessoiresSelectionnees();
                    mainWindow.showChaletTable();
                    return;
                }

                if (evt.getSelectedMeshIDs().size() == 0) {
                    mainWindow.clearAccessoiresSelectionnees();
                    mainWindow.showChaletTable();
                } else {
                    mainWindow.clearAccessoiresSelectionnees();

                    for (String id : evt.getSelectedMeshIDs()) {
                        Accessoire.AccessoireDTO accessoireDTO = mainWindow.getControleur()
                                .getAccessoire(UUID.fromString(id));

                        if (accessoireDTO != null) {
                            mainWindow.ajouterAccessoireSelectionnee(accessoireDTO);
                        } else {
                            mainWindow.retirerAccessoireSelectionnee(accessoireDTO);
                        }
                    }

                    if (mainWindow.getAccessoiresSelectionnees().size() > 0) {
                        mainWindow.showAccessoireTable(mainWindow.getAccessoiresSelectionnees()
                                .get(mainWindow.getAccessoiresSelectionnees().size() - 1));
                    }
                }

                mainWindow.arbreDesComposantesChalet.setSelectedAccessoire(mainWindow.getAccessoiresSelectionnees());
            }
        });

        this.afficheur.getScene().getConfiguration().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                // System.out.println("Scene Configuration changed");
            }
        });

        this.afficheur.getEventSupport().addCameraListener(new AfficheurEventSupport.CameraListener() {
            @Override
            public void cameraDirectionChanged(AfficheurEventSupport.CameraEvent evt) {
                // Normalizing angles
                double angle = evt.getDirection().getY() % (Math.PI * 2);

                if (angle < 0) {
                    angle += Math.PI * 2;
                }

                if (evt.getDirection().getX() < -Math.PI / 4) {
                    weakChangerVue(TypeDeVue.Dessus);
                } else if (angle < (Math.PI + Math.PI / 4) && angle > (Math.PI - Math.PI / 4)) {
                    weakChangerVue(TypeDeVue.Facade);
                } else if (angle < (0 + Math.PI / 4) && angle > (0 - Math.PI / 4)) {
                    weakChangerVue(TypeDeVue.Arriere);
                } else if (angle < (Math.PI / 2 + Math.PI / 4) && angle > (Math.PI / 2 - Math.PI / 4)) {
                    weakChangerVue(TypeDeVue.Droite);
                } else if (angle < (Math.PI * 3 / 2 + Math.PI / 4) && angle > (Math.PI * 3 / 2 - Math.PI / 4)) {
                    weakChangerVue(TypeDeVue.Gauche);
                }

                mesureBruteContainer.hide();
            }

            @Override
            public void cameraPositionChanged(AfficheurEventSupport.CameraEvent evt) {
                // System.out.println("Camera Position Changed");
                mesureBruteContainer.hide();
            }
        });

        this.afficheur.getEventSupport().addViewChangedListener(new AfficheurEventSupport.ViewChangedListener() {
            @Override
            public void viewChanged(AfficheurEventSupport.ViewChangedEvent evt) {
                System.out.println("View Changed");
                Vector3D positionDefault = afficheur.getScene().getCamera().getPosition();
                positionDefault.x = getWidth() / 2;
                positionDefault.y = getHeight() / 2;
                afficheur.getScene().getCamera().setPosition(positionDefault);
                updateToolbarBtns();
                repaint();
            }
        });

        // String appImgPath = "/icons/dark/rotate_icon.png";
        // URL appImgURL = App.class.getResource(appImgPath);
        // Image appImg = Toolkit.getDefaultToolkit().getImage(appImgURL).getScaledInstance(300, 300, Image.SCALE_SMOOTH);

        // Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(appImg, new Point(16, 16), "cursor");

        // this.addMouseMotionListener(new MouseAdapter() {
        //     @Override
        //     public void mouseMoved(MouseEvent e) {
        //         setCursor(c);
        //     }
        // });

        buildViewToolbar();
        // buildMurBruteValue();

    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();

        SwingUtilities.invokeLater(() -> {
            // System.out.println("InvokeAndWait " + getSize());
            afficheur.rechargerAffichage();

            afficheur.getScene().getCamera().setPosition(new Vector3D(getWidth() / 2, getHeight() / 2, -1));
            afficheur.getScene().getLight().setPosition(new Vector3D(10000, 10000, 10000));
            afficheur.getScene().getLight().setIntensity(0.5);
        });
    }

    private void buildViewToolbar() {
        barreOutilsVue = new javax.swing.JToolBar("Barre d'outils");

        barreOutilsVue.setFloatable(false);
        barreOutilsVue.setRollover(true);
        barreOutilsVue.setBorder(new EmptyBorder(0, 0, 0, 0));
        barreOutilsVue.setLayout(new GridLayout());
        barreOutilsVue.setOpaque(false);

        for (Object[] obj : btns) {
            final String label = (String) obj[0];
            final String name = (String) obj[1];

            final javax.swing.JButton btn = new javax.swing.JButton(label);
            obj[2] = btn;

            btn.setFocusable(false);
            btn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            btn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
            btn.setName(name);
            btn.setOpaque(true);

            btn.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    // System.out.println("Vue de " + label + " activée");
                    for (Object[] obj : btns) {
                        if (obj[2] == btn) {
                            btn.setBackground(activeBtnColor);
                            afficheur.setVueActive(TypeDeVue.valueOf((String) obj[1]));
                            changerVue(afficheur.getVueActive());
                        } else {
                            ((javax.swing.JButton) obj[2]).setBackground(inactiveBtnColor);
                        }

                    }
                    afficheur.updateViewGrid();
                    invalidate();
                    repaint();
                }
            });

            if (btn.getName() == afficheur.getVueActive().toString())
                btn.setBackground(activeBtnColor);

            barreOutilsVue.add(btn);

            invalidate();
            repaint();
        }

        JPanel toolsSwitchesContainer = new JPanel();
        toolsSwitchesContainer.setOpaque(false);

        if (MainWindow.SHOW_DRAWING_PANEL_TOOLS_SWITCHES) {
            PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO = mainWindow.getControleur()
                    .getPreferencesUtilisateur();

            JPanel gridContainer = new JPanel();
            JLabel gridLabel = new JLabel("Grille: ");
            toggleGridSwitch = new SwitchToggleButton(SwitchToggleButton.Size.XSMALL,
                    preferencesUtilisateurDTO.afficherGrille);

            JPanel gridStepContainer = new JPanel();
            JLabel gridStepLabel = new JLabel("Taille(\"): ");
            gridStepSpinner = new GridStepSpinner();

            JPanel voisinContainer = new JPanel();
            JLabel voisinLabel = new JLabel("Voisin: ");
            toggleVoisinSwitch = new SwitchToggleButton(SwitchToggleButton.Size.XSMALL,
                    preferencesUtilisateurDTO.afficherVoisinSelection);

            toggleGridSwitch.setEnabled(true);
            toggleVoisinSwitch.setEnabled(true);

            gridContainer.setOpaque(false);
            voisinContainer.setOpaque(false);
            gridStepContainer.setOpaque(false);

            gridContainer.setLayout(new BorderLayout());
            voisinContainer.setLayout(new BorderLayout());
            gridStepContainer.setLayout(new BorderLayout());

            gridContainer.add(gridLabel, BorderLayout.WEST);
            gridContainer.add(toggleGridSwitch, BorderLayout.EAST);

            voisinContainer.add(voisinLabel, BorderLayout.WEST);
            voisinContainer.add(toggleVoisinSwitch, BorderLayout.EAST);

            gridStepContainer.add(gridStepLabel, BorderLayout.WEST);
            gridStepContainer.add(gridStepSpinner, BorderLayout.EAST);

            gridStepSpinner.setValue(preferencesUtilisateurDTO.gridSpacing);
            gridStepSpinner.setPreferredSize(new Dimension(50, 20));
            gridStepSpinner.setEnabled(!preferencesUtilisateurDTO.afficherGrille);

            gridStepSpinner.setEnabled(preferencesUtilisateurDTO.afficherGrille);

            gridStepSpinner.addChangeListener((evt) -> {
                // System.out.println("Grid Step Changed: " + grilleStepSpinner.getValue());
                PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO2 = mainWindow.getControleur()
                        .getPreferencesUtilisateur();

                preferencesUtilisateurDTO2.gridSpacing = (double) gridStepSpinner.getValue();
                mainWindow.getControleur().setPreferencesUtilisateur(preferencesUtilisateurDTO2);
                afficheur.getScene().getConfiguration().setGridStep(preferencesUtilisateurDTO2.gridSpacing);
                afficheur.updateViewGrid();
                // mainWindow.drawingPanel.rechargerAffichage();
            });

            toggleGridSwitch.addEventSelected((evt) -> {
                // System.out.println("Grid Selected: " + switchGrille.isSelected());
                PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO2 = mainWindow.getControleur()
                        .getPreferencesUtilisateur();

                preferencesUtilisateurDTO2.afficherGrille = toggleGridSwitch.isSelected();
                mainWindow.getControleur().setPreferencesUtilisateur(preferencesUtilisateurDTO2);
                // afficheur.toggleShowGrid(switchGrid.isSelected());
                // afficheur.updateViewGrid();
                // mainWindow.drawingPanel.rechargerAffichage();
            });

            toggleVoisinSwitch.setEnabled(false);

            afficheur.getEventSupport().addMeshSelectionListener((evt) -> {
                if (evt.getSelectedMeshIDs().size() == 0 && !toggleVoisinSwitch.isSelected()) {
                    toggleVoisinSwitch.setEnabled(false);
                } else {
                    toggleVoisinSwitch.setEnabled(true);
                }
            });

            toggleVoisinSwitch.addEventSelected((evt) -> {
                // System.out.println("Voisin Selected: " + toggleVoisinSwitch.isSelected());

                if (afficheur.getSelection().size() != 0 && toggleVoisinSwitch.isSelected()) {
                    for (TriangleMesh mesh : afficheur.getScene().getMeshes()) {
                        if (!mesh.getSelected()) {
                            mesh.setHidden(true);
                        }
                    }
                } else {
                    // toggleVoisinSwitch.setSelected(false);
                    if (afficheur.getSelection().size() == 0) {
                        toggleVoisinSwitch.setEnabled(false);
                    }

                    for (TriangleMesh mesh : afficheur.getScene().getMeshes()) {
                        mesh.setHidden(false);
                    }
                }

                PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO2 = mainWindow.getControleur()
                        .getPreferencesUtilisateur();

                preferencesUtilisateurDTO2.afficherVoisinSelection = toggleVoisinSwitch.isSelected();
                mainWindow.getControleur().setPreferencesUtilisateur(preferencesUtilisateurDTO2);
            });

            JPanel mouseControlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
            JToggleButton rotateControlBtn = new JToggleButton();
            JToggleButton translateControlBtn = new JToggleButton();

            
            String rotateIconPath = "/icons/dark/rotate_icon.png";
            String moveIconPath = "/icons/dark/move_icon.png";

            URL rotateIconURL = App.class.getResource(rotateIconPath);
            URL moveIconURL = App.class.getResource(moveIconPath);

            Image rotateIcon = Toolkit.getDefaultToolkit().getImage(rotateIconURL).getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            Image moveIcon = Toolkit.getDefaultToolkit().getImage(moveIconURL).getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            
            rotateControlBtn.setToolTipText("Contrôle de la direction");
            translateControlBtn.setToolTipText("Contrôle de la position");

            rotateControlBtn.setIcon(new ImageIcon(rotateIcon));
            translateControlBtn.setIcon(new ImageIcon(moveIcon));

            translateControlBtn.setSelected(true);

            rotateControlBtn.addActionListener((evt) -> {
                rotateControlBtn.setSelected(true);
                translateControlBtn.setSelected(false);

                afficheur.setMouseControl(Afficheur.MouseControl.Rotate);
            });

            translateControlBtn.addActionListener((evt) -> {
                rotateControlBtn.setSelected(false);
                translateControlBtn.setSelected(true);

                afficheur.setMouseControl(Afficheur.MouseControl.Move);
            });

            rotateControlBtn.setFocusable(false);
            translateControlBtn.setFocusable(false);

            mouseControlsPanel.setOpaque(false);

            mouseControlsPanel.add(translateControlBtn);
            mouseControlsPanel.add(rotateControlBtn);

            toolsSwitchesContainer.add(gridContainer);
            toolsSwitchesContainer.add(gridStepContainer);
            toolsSwitchesContainer.add(voisinContainer);
            toolsSwitchesContainer.add(mouseControlsPanel);

            afficheur.getEventSupport().addMouseControlListener((mouseControl) -> {
                // System.out.println("MouseControl Changed " + mouseControl);
                switch (mouseControl) {
                    case Move:
                        translateControlBtn.setSelected(true);
                        rotateControlBtn.setSelected(false);
                        break;
                    case Rotate:
                        translateControlBtn.setSelected(false);
                        rotateControlBtn.setSelected(true);
                        break;
                }
            });


            mainWindow.getControleur().addUserPreferencesEventListener(new UserPreferencesEventListener() {
                @Override
                public void change(UserPreferencesEvent event) {
                    PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO2 = event
                            .getPreferencesUtilisateurDTO();

                    toggleGridSwitch.setSelected(preferencesUtilisateurDTO2.afficherGrille);
                    toggleVoisinSwitch.setSelected(preferencesUtilisateurDTO2.afficherVoisinSelection);
                    gridStepSpinner.setValue(preferencesUtilisateurDTO2.gridSpacing);
                    gridStepSpinner.setEnabled(preferencesUtilisateurDTO2.afficherGrille);

                    mainWindow.drawingPanel.afficheur.getScene().getConfiguration()
                            .setGridStep(preferencesUtilisateurDTO2.gridSpacing);
                    afficheur.updateViewGrid();
                }
            });
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 263, Short.MAX_VALUE)
                                .addComponent(barreOutilsVue, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(toolsSwitchesContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 100,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(toolsSwitchesContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 150,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 321,
                                        Short.MAX_VALUE)
                                .addComponent(barreOutilsVue, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
    }

    public void updateToolbarBtns() {
        for (Object[] obj : btns) {
            final String name = (String) obj[1];
            final javax.swing.JButton btn = (JButton) obj[2];

            if (name == afficheur.getVueActive().toString())
                btn.setBackground(activeBtnColor);
            else
                btn.setBackground(inactiveBtnColor);
        }

        PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO = mainWindow.getControleur()
                .getPreferencesUtilisateur();

        toggleGridSwitch.setSelected(preferencesUtilisateurDTO.afficherGrille);
        toggleVoisinSwitch.setSelected(preferencesUtilisateurDTO.afficherVoisinSelection);
        gridStepSpinner.setValue(preferencesUtilisateurDTO.gridSpacing);
        gridStepSpinner.setEnabled(preferencesUtilisateurDTO.afficherGrille);

    }

    public Controleur getControleur() {
        return mainWindow.getControleur();
    }

    @Override
    public void paintComponent(java.awt.Graphics g) {
        // super.paintComponent(g);
        this.afficheur.getRasterizer().draw(this.getSize());
        g.drawImage(this.afficheur.getRasterizer().getImage(), 0, 0, null);
    }

    public void changerVue(TypeDeVue vue) {
        afficheur.changerVue(vue);
        afficheur.rechargerAffichage();
        mainWindow.menu.activerVue(afficheur.getVueActive());
        updateToolbarBtns();
    }

    public void weakChangerVue(TypeDeVue vue) {
        afficheur.weakChangerVue(vue); // update seulement les flags
        mainWindow.menu.activerVue(afficheur.getVueActive());
        updateToolbarBtns();

        // if (vue == TypeDeVue.Dessus) {
        //     mainWindow.topButtonPanel.creerFenetreBtn.setEnabled(false);
        //     mainWindow.topButtonPanel.creerPorteBtn.setEnabled(false);
        // } else {
        //     mainWindow.topButtonPanel.creerFenetreBtn.setEnabled(true);
        //     mainWindow.topButtonPanel.creerPorteBtn.setEnabled(true);
        // }
    }

    public class MesureBruteContainer {
        JDialog dialogContainer = new JDialog();
        MesureBrutePanel mesurePanel;
        ChaletDTO chaletDTO;

        public MesureBruteContainer(ChaletDTO chaletDTO) {
            this.chaletDTO = chaletDTO;
            initComponents();
        }

        private void initComponents() {
            mesurePanel = new MesureBrutePanel(chaletDTO);
            dialogContainer.setLocationRelativeTo(null);
            dialogContainer.setUndecorated(true);
            dialogContainer.pack();
            dialogContainer.setMinimumSize(new Dimension(100, 80));
            // dialogContainer.validate();
            dialogContainer.setResizable(false);
            dialogContainer.setFocusable(false);
            dialogContainer.setFocusableWindowState(false);

            mesurePanel.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {

                    Component child = e.getComponent();
                    Component parent = child.getParent();
                    MouseEvent parentMouseEvent = new MouseEvent(parent, MouseEvent.MOUSE_MOVED, e.getWhen(),
                            e.getModifiersEx(), e.getX(), e.getY(), e.getClickCount(), false);
                    afficheur.getEventSupport()
                            .dispatchMeshHovered(new MeshMouseMotionEvent(parentMouseEvent, null, null));
                    super.mouseMoved(e);
                }
            });
            dialogContainer.add(mesurePanel);
        }

        public void updateValeurs(ChaletDTO chaletDTO, double largeur) {
            this.chaletDTO = chaletDTO;
            mesurePanel.updatePanel(this.chaletDTO, largeur);
            dialogContainer.pack();
        }

        public void show(Point point) {
            if (point.getX() <= getLocationOnScreen().getX() + 20) {
                dialogContainer.setVisible(false);
                return;
            } else if (point.getX() >= getLocationOnScreen().getX() + getWidth() - 20) {
                dialogContainer.setVisible(false);
                return;
            } else if (point.getY() <= getLocationOnScreen().getY() + 20) {
                dialogContainer.setVisible(false);
                return;
            } else if (point.getY() >= getLocationOnScreen().getY() + getHeight() - 20) {
                dialogContainer.setVisible(false);
                return;
            }

            dialogContainer.setLocation((int) (point.getX() - mesurePanel.getWidth()),
                    (int) (point.getY() - mesurePanel.getHeight()));
            if (dialogContainer.isVisible() == false) {
                dialogContainer.setVisible(true);
            }
        }

        public void hide() {
            dialogContainer.setVisible(false);
        }
    }
}
