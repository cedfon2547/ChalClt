package ca.ulaval.glo2004.gui.components;

import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeListener;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;

import ca.ulaval.glo2004.domaine.PreferencesUtilisateur;
import ca.ulaval.glo2004.domaine.afficheur.Afficheur;
import ca.ulaval.glo2004.domaine.afficheur.AfficheurEventSupport;
import ca.ulaval.glo2004.domaine.afficheur.Afficheur.TypeDeVue;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMesh;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMeshGroup;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene.Camera;
import ca.ulaval.glo2004.domaine.utils.PanelHelper;
import ca.ulaval.glo2004.gui.MainWindow;
import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.Controleur;

class GridStepSpinner extends JSpinner {
    public GridStepSpinner() {
        this(0.1f);
    }

    public GridStepSpinner(float value) {
        super();
        // this.setModel(new javax.swing.SpinnerNumberModel((float) value, 0.0f, 100.0f,
        // 0.5f));
        setValue(value);
        this.setModel(new SpinnerNumberModel((float) value, 0.1f, 100.0f, 0.5f));
        ((SpinnerNumberModel) this.getModel()).setStepSize(0.1);
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
    MainWindow mainWindow;
    Afficheur afficheur;

    public static final Color activeBtnColor = Color.DARK_GRAY;
    public static final Color inactiveBtnColor = Color.BLACK;

    Object[][] btns = new Object[][] {
            { "Dessus", Afficheur.TypeDeVue.Dessus.toString(), null },
            { "Façade", Afficheur.TypeDeVue.Facade.toString(), null },
            { "Arrière", Afficheur.TypeDeVue.Arriere.toString(), null },
            { "Droite", Afficheur.TypeDeVue.Droite.toString(), null },
            { "Gauche", Afficheur.TypeDeVue.Gauche.toString(), null },
    };
    // Afficheur.TypeDeVue vueActive = Afficheur.TypeDeVue.Dessus;
    javax.swing.JToolBar barreOutils;

    public DrawingPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.afficheur = new Afficheur(this.mainWindow.getControleur(), this);

        centeringLight();
        initComponents();
        rechargerAffichage();
    }

    public void centeringLight() {
        this.afficheur.getScene().getLight().setPosition(new Vector3D((int) (this.getWidth() / 2), 500, 0));
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    // public Scene getScene() {
    // return scene;
    // }

    // public Rasterizer getRasterizer() {
    // return rasterizer;
    // }

    private void initComponents() {
        setBackground(java.awt.Color.BLACK);

        // addMouseListener(this.mouseListener());
        // addMouseWheelListener(this.mouseWheelListener());
        // addKeyListener(this.keyListener());
        // addMouseMotionListener(this.mouseMotionListener());

        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                // System.out.println("Key Pressed");
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
                    default:
                        break;
                }

                afficheur.getScene().getLight().setPosition(lightPosition);
                repaint();
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                // System.out.println("Key Released");
            }

            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                // System.out.println("Key Typed");
            }
        });
        
        this.afficheur.getEventSupport().addZoomEventListener(new AfficheurEventSupport.ZoomEventListener() {
            @Override
            public void zoomIn() {
                // System.out.println("ZoomIn");
            }

            @Override
            public void zoomOut() {
                // System.out.println("ZoomOut");
            }
        });

        this.afficheur.getEventSupport().addMeshMouseListener(new AfficheurEventSupport.MeshMouseListener() {
            @Override
            public void meshClicked(AfficheurEventSupport.MeshMouseEvent evt) {
                // System.out.println("Mesh Clicked " + mesh.ID);
                // System.out.println("Mesh Clicked " + evt.getMesh().ID);

                TriangleMeshGroup clickedMesh = (TriangleMeshGroup) evt.getMesh();
                Accessoire.AccessoireDTO accDto = mainWindow.getControleur()
                        .getAccessoire(UUID.fromString(clickedMesh.getIdentifier()));

                if (accDto != null) {
                    mainWindow.clearAccessoiresSelectionnees();
                    mainWindow.ajouterAccessoireSelectionnee(accDto);
                    mainWindow.showAccessoireTable(accDto);
                } else {
                    mainWindow.clearAccessoiresSelectionnees();
                    mainWindow.showChaletTable();
                }
            }

            @Override
            public void meshHovered(AfficheurEventSupport.MeshMouseMotionEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void meshDragged(AfficheurEventSupport.MeshMouseMotionEvent evt) {
                System.out.println("MeshDragged " + evt.getMesh().ID + " " + evt.getPoint());
            }

            @Override
            public void mouseEnterMesh(AfficheurEventSupport.MeshMouseMotionEvent evt) {
                // System.out.println("MouseEnterMesh " + evt.getMesh().ID);
                repaint();
            }

            @Override
            public void mouseExitMesh(AfficheurEventSupport.MeshMouseMotionEvent evt) {
                // System.out.println("MouseExitMesh " + evt.getMesh().ID);
                repaint();
            }
        });

        this.afficheur.getEventSupport().addMeshSelectionListener(new AfficheurEventSupport.MeshSelectionListener() {
            @Override
            public void selectionChanged(AfficheurEventSupport.MeshSelectionEvent evt) {
                if (evt.getSelectedMeshIDs().size() == 0) {
                    mainWindow.clearAccessoiresSelectionnees();
                    mainWindow.showChaletTable();
                } else {
                    mainWindow.clearAccessoiresSelectionnees();
                    for (String id : evt.getSelectedMeshIDs()) {
                        Accessoire.AccessoireDTO accessoireDTO = mainWindow.getControleur().getAccessoire(UUID.fromString(id));
                        // System.out.println("Selection Changed " + accDto);
                        
                        if (accessoireDTO != null) {
                            mainWindow.ajouterAccessoireSelectionnee(accessoireDTO);
                        }
                    }

                    if (mainWindow.getAccessoiresSelectionnees().size() > 0) {
                        mainWindow.showAccessoireTable(mainWindow.getAccessoiresSelectionnees().get(0));
                    }
                    // Accessoire.AccessoireDTO accDto =
                    // mainWindow.getControleur().getAccessoire(UUID.fromString(evt.getSelectedMeshIDs().get(0)));

                    // if (accDto != null) {
                    // mainWindow.clearAccessoiresSelectionnees();
                    // mainWindow.ajouterAccessoireSelectionnee(accDto);
                    // mainWindow.showAccessoireTable(accDto);
                    // }
                }
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
                // System.out.println("Camera Direction Changed");
            }

            @Override
            public void cameraPositionChanged(AfficheurEventSupport.CameraEvent evt) {
                // System.out.println("Camera Position Changed");
            }
        });

        this.afficheur.getEventSupport().addViewChangedListener(new AfficheurEventSupport.ViewChangedListener() {
            @Override
            public void viewChanged(AfficheurEventSupport.ViewChangedEvent evt) {
                // System.out.println("View Changed");
                updateToolbarBtns();
            }
        });

        // this.afficheur.addPropertyChangeListener(Afficheur.AfficheurEvent.MeshClicked,
        // (evt) -> {
        // System.out.println("Mesh Clicked " + ((TriangleMesh) evt.getNewValue()).ID);

        // TriangleMeshGroup clickedMesh = (TriangleMeshGroup) evt.getNewValue();
        // Accessoire.AccessoireDTO accDto = mainWindow.getControleur()
        // .getAccessoire(UUID.fromString(clickedMesh.getIdentifier()));

        // if (accDto != null) {
        // mainWindow.clearAccessoiresSelectionnees();
        // mainWindow.ajouterAccessoireSelectionnee(accDto);
        // mainWindow.showAccessoireTable(accDto);
        // } else {
        // mainWindow.clearAccessoiresSelectionnees();
        // mainWindow.showChaletTable();
        // }
        // });

        // this.afficheur.addPropertyChangeListener(Afficheur.AfficheurEvent.SelectionChanged,
        // (evt) -> {
        // if (evt.getNewValue() == null) {
        // mainWindow.clearAccessoiresSelectionnees();
        // mainWindow.showChaletTable();
        // } else {
        // TriangleMeshGroup clickedMesh = (TriangleMeshGroup) evt.getNewValue();
        // Accessoire.AccessoireDTO accDto = mainWindow.getControleur()
        // .getAccessoire(UUID.fromString(clickedMesh.getIdentifier()));

        // if (accDto != null) {
        // mainWindow.clearAccessoiresSelectionnees();
        // mainWindow.ajouterAccessoireSelectionnee(accDto);
        // mainWindow.showAccessoireTable(accDto);
        // }
        // }
        // });

        // this.afficheur.addPropertyChangeListener(Afficheur.AfficheurEvent.MouseEnterMesh,
        // (evt) -> {
        // System.out.println("MouseEnterMesh " + ((TriangleMeshGroup)
        // evt.getNewValue()).ID);
        // ((TriangleMeshGroup) evt.getNewValue()).setSelected(true);
        // repaint();
        // });

        // this.afficheur.addPropertyChangeListener(Afficheur.AfficheurEvent.MouseExitMesh,
        // (evt) -> {
        // System.out.println("MouseExitMesh " + ((TriangleMeshGroup)
        // evt.getNewValue()).ID);
        // ((TriangleMeshGroup) evt.getNewValue()).setSelected(false);
        // repaint();
        // });

        // this.afficheur.addPropertyChangeListener(Afficheur.AfficheurEvent.MeshDragged,
        // (evt) -> {
        // Afficheur.MeshDraggedEvent _evt = (Afficheur.MeshDraggedEvent)
        // evt.getNewValue();
        // System.out.println("MeshDragged " + _evt.getMesh().ID + " " +
        // _evt.getPoint());
        // Accessoire.AccessoireDTO accessoireDTO = mainWindow.getControleur()
        // .getAccessoire(UUID.fromString(_evt.getMesh().getIdentifier()));
        // System.out.println(accessoireDTO);
        // if (accessoireDTO != null) {
        // double diffX = _evt.getPoint().getX() - _evt.getInitialPoint().getX();
        // double diffY = _evt.getPoint().getY() - _evt.getInitialPoint().getY();

        // _evt.getMesh().setPosition(new Vector3D(_evt.getInitialPoint().getX() -
        // diffX,
        // _evt.getInitialPoint().getY() + diffY, 0));

        // repaint();
        // }
        // });

        // this.afficheur.addPropertyChangeListener(Afficheur.AfficheurEvent.MeshDoubleClicked,
        // (evt) -> {
        // System.out.println("Mesh Double Clicked " + ((TriangleMesh)
        // evt.getNewValue()).ID);
        // mainWindow.clearAccessoiresSelectionnees();
        // mainWindow.showChaletTable();
        // });

        // this.afficheur.addPropertyChangeListener(Afficheur.AfficheurEvent.SelectionCleared,
        // (evt) -> {
        // System.out.println("Selection Cleared");
        // mainWindow.clearAccessoiresSelectionnees();
        // mainWindow.showChaletTable();
        // });

        // this.afficheur.addPropertyChangeListener(Afficheur.AfficheurEvent.ZoomIn,
        // (evt) -> {
        // System.out.println("ZoomIn");
        // });

        // this.afficheur.addPropertyChangeListener(Afficheur.AfficheurEvent.ZoomOut,
        // (evt) -> {
        // System.out.println("ZoomOut");
        // });

        addHierarchyListener(new HierarchyListener() {

            @Override
            public void hierarchyChanged(HierarchyEvent e) {
                if ((e.getChangeFlags() & HierarchyEvent.PARENT_CHANGED) != 0) {
                    if (getParent() == e.getChangedParent()) {
                        System.out.println("*** Added to parent " + e.getChangedParent());
                    }
                }
            }
        });

        buildToolbar();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocus();
        
        SwingUtilities.invokeLater(() -> {
            System.out.println("InvokeAndWait " + getSize());
            afficheur.getScene().getCamera().setPosition(new Vector3D(getWidth() / 2, getHeight() / 2, -1));
            afficheur.getScene().getLight().setPosition(new Vector3D(10000, 10000, 10000));
            afficheur.getScene().getLight().setIntensity(0.5);

        });
    }

    private void buildToolbar() {
        barreOutils = new javax.swing.JToolBar("Barre d'outils");

        barreOutils.setFloatable(false);
        barreOutils.setRollover(true);
        barreOutils.setBorder(new EmptyBorder(0, 0, 0, 0));
        barreOutils.setLayout(new GridLayout());
        barreOutils.setOpaque(false);

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
                            afficheur.setVueActive(Afficheur.TypeDeVue.valueOf((String) obj[1]));
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

            barreOutils.add(btn);

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
            SwitchToggleButton switchGrid = new SwitchToggleButton(SwitchToggleButton.Size.XSMALL,
                    preferencesUtilisateurDTO.afficherGrille);

            JPanel gridStepContainer = new JPanel();
            JLabel gridStepLabel = new JLabel("Taille: ");
            GridStepSpinner gridStepSpinner = new GridStepSpinner();

            JPanel voisinContainer = new JPanel();
            JLabel voisinLabel = new JLabel("Voisin: ");
            SwitchToggleButton switchVoisin = new SwitchToggleButton(SwitchToggleButton.Size.XSMALL,
                    preferencesUtilisateurDTO.afficherVoisinSelection);

            switchGrid.setEnabled(true);
            switchVoisin.setEnabled(true);

            gridContainer.setOpaque(false);
            voisinContainer.setOpaque(false);
            gridStepContainer.setOpaque(false);

            gridContainer.setLayout(new BorderLayout());
            voisinContainer.setLayout(new BorderLayout());
            gridStepContainer.setLayout(new BorderLayout());

            gridContainer.add(gridLabel, BorderLayout.WEST);
            gridContainer.add(switchGrid, BorderLayout.EAST);

            voisinContainer.add(voisinLabel, BorderLayout.WEST);
            voisinContainer.add(switchVoisin, BorderLayout.EAST);

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

            switchGrid.addEventSelected((evt) -> {
                // System.out.println("Grid Selected: " + switchGrille.isSelected());
                PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO2 = mainWindow.getControleur()
                        .getPreferencesUtilisateur();

                preferencesUtilisateurDTO2.afficherGrille = switchGrid.isSelected();
                mainWindow.getControleur().setPreferencesUtilisateur(preferencesUtilisateurDTO2);
                // afficheur.toggleShowGrid(switchGrid.isSelected());
                // afficheur.updateViewGrid();
                // mainWindow.drawingPanel.rechargerAffichage();
            });

            switchVoisin.addEventSelected((evt) -> {
                // System.out.println("Voisin Selected: " + switchVoisin.isSelected());
                PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO2 = mainWindow.getControleur()
                        .getPreferencesUtilisateur();

                preferencesUtilisateurDTO2.afficherVoisinSelection = switchVoisin.isSelected();
                mainWindow.getControleur().setPreferencesUtilisateur(preferencesUtilisateurDTO2);
                mainWindow.drawingPanel.rechargerAffichage();
            });

            toolsSwitchesContainer.add(gridContainer);
            toolsSwitchesContainer.add(gridStepContainer);
            toolsSwitchesContainer.add(voisinContainer);

            mainWindow.getControleur().addPropertyChangeListener(Controleur.EventType.PREFERENCES_UTILISATEUR,
                    (evt) -> {
                        // System.out.println("Preferences Utilisateur changed");
                        PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO2 = (PreferencesUtilisateur.PreferencesUtilisateurDTO) evt
                                .getNewValue();
                        switchGrid.setSelected(preferencesUtilisateurDTO2.afficherGrille);
                        switchVoisin.setSelected(preferencesUtilisateurDTO2.afficherVoisinSelection);
                        gridStepSpinner.setValue(preferencesUtilisateurDTO2.gridSpacing);
                        gridStepSpinner.setEnabled(preferencesUtilisateurDTO2.afficherGrille);

                        mainWindow.drawingPanel.afficheur.getScene().getConfiguration()
                                .setGridStep(preferencesUtilisateurDTO2.gridSpacing);
                        afficheur.updateViewGrid();
                        // mainWindow.drawingPanel.rechargerAffichage();
                    });
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 263, Short.MAX_VALUE)
                                .addComponent(barreOutils, javax.swing.GroupLayout.PREFERRED_SIZE,
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
                                .addComponent(barreOutils, javax.swing.GroupLayout.PREFERRED_SIZE,
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
    }

    // private MouseWheelListener mouseWheelListener() {
    // return new MouseWheelListener() {

    // @Override
    // public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {
    // Camera camera = afficheur.getScene().getCamera();

    // if (e.getPreciseWheelRotation() < 0) {
    // camera.zoomInDirection(e.getPoint(), getSize(), e.isShiftDown());
    // } else {
    // camera.zoomOutDirection(e.getPoint(), getSize(), e.isShiftDown());
    // }

    // repaint();
    // }
    // };
    // }

    // private MouseListener mouseListener() {
    // return new MouseListener() {
    // @Override
    // public void mouseClicked(java.awt.event.MouseEvent e) {
    // grabFocus();
    // // System.out.println("Mouse Clicked");

    // TriangleMeshGroup mesh =
    // afficheur.getRasterizer().getMeshFromPoint(e.getPoint());

    // if (mesh != null && e.getClickCount() == 2) {
    // if (mesh instanceof PanelHelper.MurTriangleMeshGroup) {
    // // System.out.println("Double clicked on a wall");
    // switch (((PanelHelper.MurTriangleMeshGroup) mesh).getTypeMur()) {
    // case Facade:
    // changerVue(Afficheur.TypeDeVue.Facade);
    // return;
    // case Arriere:
    // changerVue(Afficheur.TypeDeVue.Arriere);
    // return;
    // case Droit:
    // changerVue(Afficheur.TypeDeVue.Droite);
    // return;
    // case Gauche:
    // changerVue(Afficheur.TypeDeVue.Gauche);
    // return;
    // default:
    // // nop, fall through
    // }
    // }

    // // String handle = mesh.getMesh(0).getHandle();
    // // Vector3D currentDirection =
    // afficheur.getScene().getCamera().getDirection();
    // // // check if we are in an intended view direction
    // // boolean strongChange = false;
    // // if(currentDirection.equals(Afficheur.TypeDeVue.vueArriere()) ||
    // // currentDirection.equals(Afficheur.TypeDeVue.vueFacade()) ||
    // // currentDirection.equals(Afficheur.TypeDeVue.vueDroite()) ||
    // // currentDirection.equals(Afficheur.TypeDeVue.vueGauche()) ||
    // // currentDirection.equals(Afficheur.TypeDeVue.vueDessus()))
    // // strongChange = true;

    // // switch(handle) {
    // // case Constants._STRING_MUR_FACADE:
    // // if(strongChange)
    // // changerVue(Afficheur.TypeDeVue.Facade);
    // // else
    // // weakChangerVue(Afficheur.TypeDeVue.Facade);
    // // return;
    // // case Constants._STRING_MUR_ARRIERE:
    // // if(strongChange)
    // // changerVue(Afficheur.TypeDeVue.Arriere);
    // // else
    // // weakChangerVue(Afficheur.TypeDeVue.Arriere);
    // // return;
    // // case Constants._STRING_MUR_DROIT:
    // // if(strongChange)
    // // changerVue(Afficheur.TypeDeVue.Droite);
    // // else
    // // weakChangerVue(Afficheur.TypeDeVue.Droite);
    // // return;
    // // case Constants._STRING_MUR_GAUCHE:
    // // if(strongChange)
    // // changerVue(Afficheur.TypeDeVue.Gauche);
    // // else
    // // weakChangerVue(Afficheur.TypeDeVue.Gauche);
    // // return;
    // // default:
    // // // nop, fall through
    // // }
    // }

    // afficheur.getRasterizer().deselectAllMeshes();

    // if (mesh != null) {
    // Accessoire.AccessoireDTO accDto = mainWindow.getControleur()
    // .getAccessoire(UUID.fromString(mesh.getIdentifier()));

    // if (accDto != null) {
    // mainWindow.clearAccessoiresSelectionnees();
    // mainWindow.ajouterAccessoireSelectionnee(accDto);
    // mainWindow.showAccessoireTable(accDto);
    // } else {
    // mainWindow.clearAccessoiresSelectionnees();
    // mainWindow.showChaletTable();
    // }
    // if (mesh.getSelectable()) {
    // // System.out.println(mesh.getIdentifier() + " selected");
    // afficheur.getScene().setSelected(mesh.getIdentifier(), true);
    // }
    // } else {
    // mainWindow.clearAccessoiresSelectionnees();
    // mainWindow.showChaletTable();
    // }

    // repaint();
    // }

    // @Override
    // public void mousePressed(java.awt.event.MouseEvent e) {
    // }

    // @Override
    // public void mouseReleased(java.awt.event.MouseEvent e) {
    // }

    // @Override
    // public void mouseEntered(java.awt.event.MouseEvent e) {
    // }

    // @Override
    // public void mouseExited(java.awt.event.MouseEvent e) {
    // }
    // };
    // }

    // private MouseMotionListener mouseMotionListener() {
    // return new MouseMotionListener() {
    // boolean initialized = false;
    // boolean isDragging = false;
    // Vector3D initialDragCamPosition = null;
    // Vector3D initialDragCamDirection = null;
    // Point initialPoint = null;

    // @Override
    // public void mouseDragged(java.awt.event.MouseEvent e) {
    // if (!initialized) {
    // MouseListener mouseListener = new MouseListener() {
    // @Override
    // public void mouseClicked(java.awt.event.MouseEvent e) {
    // }

    // @Override
    // public void mousePressed(java.awt.event.MouseEvent e) {
    // isDragging = true;
    // initialPoint = e.getPoint();
    // initialDragCamPosition = afficheur.getScene().getCamera().getPosition();
    // initialDragCamDirection = afficheur.getScene().getCamera().getDirection();
    // }

    // @Override
    // public void mouseReleased(java.awt.event.MouseEvent e) {
    // // System.out.println("Mouse Released " + e.getPoint().toString());
    // isDragging = false;
    // initialPoint = null;
    // initialDragCamPosition = null;
    // initialDragCamDirection = null;
    // }

    // @Override
    // public void mouseEntered(java.awt.event.MouseEvent e) {

    // }

    // @Override
    // public void mouseExited(java.awt.event.MouseEvent e) {
    // }
    // };

    // addMouseListener(mouseListener);
    // isDragging = true;
    // initialDragCamDirection = afficheur.getScene().getCamera().getDirection();
    // initialDragCamPosition = afficheur.getScene().getCamera().getPosition();
    // initialPoint = e.getPoint();
    // initialized = true;
    // }

    // if (isDragging && initialPoint != null && initialDragCamDirection != null
    // && initialDragCamPosition != null) {
    // double diffX = e.getPoint().x - initialPoint.x;
    // double diffY = e.getPoint().y - initialPoint.y;

    // if (e.isShiftDown()) {
    // // todo DO NOT DELETE
    // // Rotating instead of translate
    // // Convert the diffX and diffY to radians
    // double rotateStep = Math.toRadians(1);
    // double rotateX = rotateStep * -diffY / 3; // negated Ydiff to fix the
    // inverted y axis
    // double rotateY = rotateStep * diffX / 3;

    // Vector3D direction = initialDragCamDirection.add(new Vector3D(rotateX,
    // rotateY, 0));
    // if (direction.x > 0) {
    // direction.x = 0;
    // } else if (direction.x < -Math.PI / 2) {
    // direction.x = -Math.PI / 2;
    // }
    // afficheur.getScene().getCamera().setDirection(direction);
    // afficheur.updateViewGrid();

    // } else {
    // afficheur.getScene().getCamera()
    // .setPosition(initialDragCamPosition.add(new Vector3D(diffX, diffY, 0)));
    // }

    // repaint();
    // }
    // }

    // @Override
    // public void mouseMoved(java.awt.event.MouseEvent e) {
    // // TODO Auto-generated method stub
    // }
    // };
    // }

    // public void updateViewGrid() {
    // PreferencesUtilisateur.PreferencesUtilisateurDTO preferUser =
    // mainWindow.getControleur()
    // .getPreferencesUtilisateur();
    // Vector3D direction = afficheur.getScene().getCamera().getDirection();

    // if (preferUser.afficherGrille) {
    // if (direction.equals(TypeDeVue.getDirection(TypeDeVue.Facade))
    // || direction.equals(TypeDeVue.getDirection(TypeDeVue.Arriere))
    // || direction.equals(TypeDeVue.getDirection(TypeDeVue.Droite))
    // || direction.equals(TypeDeVue.getDirection(TypeDeVue.Gauche))) {
    // afficheur.getScene().getConfiguration().setShowGridXZ(true);
    // afficheur.getScene().getConfiguration().setShowGridYZ(true);
    // afficheur.getScene().getConfiguration().setShowGridXY(true);
    // } else {
    // afficheur.getScene().getConfiguration().setShowGridYZ(false);
    // afficheur.getScene().getConfiguration().setShowGridXY(false);
    // }
    // } else {
    // afficheur.getScene().getConfiguration().setShowGridXZ(false);
    // afficheur.getScene().getConfiguration().setShowGridYZ(false);
    // afficheur.getScene().getConfiguration().setShowGridXY(false);
    // }
    // }

    // public KeyListener keyListener() {
    // return new KeyListener() {
    // @Override
    // public void keyTyped(java.awt.event.KeyEvent evt) {
    // // TODO Auto-generated method stub
    // }

    // @Override
    // public void keyReleased(java.awt.event.KeyEvent e) {
    // // TODO Auto-generated method stub
    // }

    // @Override
    // public void keyPressed(java.awt.event.KeyEvent evt) {
    // Vector3D lightPosition = afficheur.getScene().getLight().getPosition();

    // switch (evt.getKeyCode()) {
    // case java.awt.event.KeyEvent.VK_UP:
    // lightPosition.y += 1;
    // afficheur.getScene().getLight().setPosition(lightPosition);
    // break;
    // case java.awt.event.KeyEvent.VK_DOWN:
    // lightPosition.y -= 1;
    // afficheur.getScene().getLight().setPosition(lightPosition);
    // break;
    // case java.awt.event.KeyEvent.VK_LEFT:
    // lightPosition.x -= 1;
    // afficheur.getScene().getLight().setPosition(lightPosition);
    // break;
    // case java.awt.event.KeyEvent.VK_RIGHT:
    // lightPosition.x += 1;
    // afficheur.getScene().getLight().setPosition(lightPosition);
    // break;
    // case java.awt.event.KeyEvent.VK_Z:
    // lightPosition.z += 1;
    // afficheur.getScene().getLight().setPosition(lightPosition);
    // break;
    // case java.awt.event.KeyEvent.VK_X:
    // lightPosition.z -= 1;
    // afficheur.getScene().getLight().setPosition(lightPosition);
    // break;
    // }
    // repaint();
    // }
    // };
    // }

    @Override
    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        // Enleve le controle de la lumiere
        // centeringLight();
        this.afficheur.draw(g, getSize());
    }

    public void changerVue(Afficheur.TypeDeVue vue) {
        afficheur.changerVue(vue);
        mainWindow.menu.activerVue(afficheur.getVueActive());
        updateToolbarBtns();
    }

    public void weakChangerVue(Afficheur.TypeDeVue vue) {
        afficheur.weakChangerVue(vue); // update seulement les flags
        mainWindow.menu.activerVue(afficheur.getVueActive());
        updateToolbarBtns();
        // invalidate(); // redundant when not updating camera
        // repaint();
    }

    public void rechargerAffichage() {
        try {
            afficheur.rechargerAffichage();
        } catch (Exception e) {
            // System.out.println("Error rendering main panel");
            e.printStackTrace();
        } // TODO manage error

        repaint();
    }
}
