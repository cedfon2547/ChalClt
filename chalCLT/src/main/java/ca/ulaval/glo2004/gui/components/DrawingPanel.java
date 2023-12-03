package ca.ulaval.glo2004.gui.components;

import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

import ca.ulaval.glo2004.domaine.PreferencesUtilisateur;
import ca.ulaval.glo2004.domaine.ControleurEventSupport.AccessoireEvent;
import ca.ulaval.glo2004.domaine.ControleurEventSupport.UserPreferencesEvent;
import ca.ulaval.glo2004.domaine.ControleurEventSupport.UserPreferencesEventListener;
import ca.ulaval.glo2004.domaine.afficheur.Afficheur;
import ca.ulaval.glo2004.domaine.afficheur.AfficheurEventSupport;
import ca.ulaval.glo2004.domaine.afficheur.AfficheurEventSupport.MeshMouseMotionEvent;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMesh;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMeshGroup;
import ca.ulaval.glo2004.domaine.utils.PanelHelper.MurTriangleMeshGroup;
import ca.ulaval.glo2004.gui.MainWindow;
import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.Controleur;
import ca.ulaval.glo2004.domaine.ControleurEventSupport;

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
    public Afficheur afficheur;

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
        afficheur.rechargerAffichage();
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

        this.mainWindow.getControleur().addAccessoireEventListener(new ControleurEventSupport.AccessoireEventListener() {
            @Override
            public void add(AccessoireEvent event) {
                // TODO Auto-generated method stub
                afficheur.rechargerAffichage();
            }

            @Override
            public void remove(AccessoireEvent event) {
                afficheur.rechargerAffichage();
            }

            @Override
            public void change(AccessoireEvent event) {
                // TODO Auto-generated method stub
                // rechargerAffichage();
            }
        });

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
            Accessoire.AccessoireDTO initialAccessoireDTO;

            @Override
            public void meshClicked(AfficheurEventSupport.MeshMouseEvent evt) {
                // System.out.println("Mesh Clicked " + mesh.ID);
            }

            @Override
            public void meshHovered(AfficheurEventSupport.MeshMouseMotionEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void meshDragged(AfficheurEventSupport.MeshMouseMotionEvent evt) {
                if (initialAccessoireDTO == null) {
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
                List<Accessoire.AccessoireDTO> accessoireDTOs = mainWindow.getControleur().getAccessoires(initialAccessoireDTO.typeMur);

                for (Accessoire.AccessoireDTO accessoireDTO: accessoireDTOs) {
                    TriangleMesh mesh = afficheur.getScene().getMesh(accessoireDTO.accessoireId.toString());
                    if (mesh != null) {
                        mesh.setValid(accessoireDTO.valide);
                    }
                }

                evt.getMesh().setValid(accDto.valide);
                repaint();
            }

            @Override
            public void meshDragStart(MeshMouseMotionEvent evt) {
                // TODO Auto-generated method stub
                System.out.println("MeshDragStart " + evt.getMesh().ID);
                TriangleMeshGroup clickedMesh = (TriangleMeshGroup) evt.getMesh();
                Accessoire.AccessoireDTO accDto = mainWindow.getControleur()
                        .getAccessoire(UUID.fromString(clickedMesh.ID));

                if (accDto != null) {
                    initialAccessoireDTO = accDto;
                    mainWindow.showAccessoireTable(accDto);
                    clickedMesh.setSelected(true);
                }
            }

            @Override
            public void meshDragEnd(MeshMouseMotionEvent e) {
                // TODO Auto-generated method stub
                System.out.println("MeshDragEnd " + e.getMesh().ID);
                initialAccessoireDTO = null;
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
                boolean isOnlyWall = evt.getSelectedMeshIDs().stream().allMatch((id) -> {
                    TriangleMesh mesh = afficheur.getScene().getMesh(id);
                    if (mesh instanceof MurTriangleMeshGroup) {
                        return true;
                    }
                    return false;
                });

                if (isOnlyWall) {
                    mainWindow.clearAccessoiresSelectionnees();
                    mainWindow.showChaletTable();
                }
                
                if (evt.getSelectedMeshIDs().size() == 0) {
                    mainWindow.clearAccessoiresSelectionnees();
                    mainWindow.showChaletTable();
                } else {
                    mainWindow.clearAccessoiresSelectionnees();
                    List<String> selections = mainWindow.getAccessoiresSelectionnees().stream()
                            .map((acc) -> acc.accessoireId.toString()).collect(Collectors.toList());

                    for (String id : evt.getSelectedMeshIDs()) {
                        Accessoire.AccessoireDTO accessoireDTO = mainWindow.getControleur()
                                .getAccessoire(UUID.fromString(id));
                        // System.out.println("Selection Changed " + accDto);

                        if (accessoireDTO != null && !selections.contains(id)) {
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

        // this.mainWindow.getControleur().addPropertyChangeListener(Controleur.EventType.AJOUTER_ACCESSOIRE, (evt) -> {
        //     System.out.println("Accessoire added " + evt.getNewValue());
        // });

        // this.mainWindow.getControleur().addPropertyChangeListener(Controleur.EventType.RETIRER_ACCESSOIRE, (evt) -> {

        // });

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
                afficheur.rechargerAffichage();
            });

            toolsSwitchesContainer.add(gridContainer);
            toolsSwitchesContainer.add(gridStepContainer);
            toolsSwitchesContainer.add(voisinContainer);

            mainWindow.getControleur().addUserPreferencesEventListener(new UserPreferencesEventListener() {
                @Override
                public void change(UserPreferencesEvent event) {
                    // System.out.println("Preferences Utilisateur changed");
                    PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO2 = event.getPreferencesUtilisateurDTO();

                    switchGrid.setSelected(preferencesUtilisateurDTO2.afficherGrille);
                    switchVoisin.setSelected(preferencesUtilisateurDTO2.afficherVoisinSelection);
                    gridStepSpinner.setValue(preferencesUtilisateurDTO2.gridSpacing);
                    gridStepSpinner.setEnabled(preferencesUtilisateurDTO2.afficherGrille);

                    mainWindow.drawingPanel.afficheur.getScene().getConfiguration()
                            .setGridStep(preferencesUtilisateurDTO2.gridSpacing);
                    afficheur.updateViewGrid();
                    // mainWindow.drawingPanel.rechargerAffichage();
                }
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

    public Controleur getControleur() {
        return mainWindow.getControleur();
    }

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

    // public MurTriangleMeshGroup murFacadeGroup;
    // public MurTriangleMeshGroup murArriereGroup;
    // public MurTriangleMeshGroup murDroitGroup;
    // public MurTriangleMeshGroup murGaucheGroup;
    // public OutputType renduVisuel = OutputType.Fini;

    // public void rechargerAffichage() {
    //     System.out.println("RENDERING");

    //     Chalet.ChaletDTO chaletDTO = this.getControleur().getChalet();
    //     PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO = this.getControleur()
    //             .getPreferencesUtilisateur();
    //     afficheur.getScene().getConfiguration().setGridStep(preferencesUtilisateurDTO.gridSpacing);
    //     afficheur.toggleShowGrid(preferencesUtilisateurDTO.afficherGrille);

    //     afficheur.getScene().clearMeshes();

    //     boolean sideTruncate = chaletDTO.sensToit == TypeSensToit.Nord || chaletDTO.sensToit == TypeSensToit.Sud;

    //     List<Accessoire.AccessoireDTO> murFacadeAccessoires = getControleur().getAccessoires(TypeMur.Facade);
    //     List<Accessoire.AccessoireDTO> murArriereAccessoires = getControleur().getAccessoires(TypeMur.Arriere);
    //     List<Accessoire.AccessoireDTO> murDroitAccessoires = getControleur().getAccessoires(TypeMur.Droit);
    //     List<Accessoire.AccessoireDTO> murGaucheAccessoires = getControleur().getAccessoires(TypeMur.Gauche);

    //     murFacadeGroup = new MurTriangleMeshGroup(chaletDTO, TypeMur.Facade, murFacadeAccessoires, !sideTruncate,
    //             false);
    //     murArriereGroup = new MurTriangleMeshGroup(chaletDTO, TypeMur.Arriere, murArriereAccessoires, !sideTruncate,
    //             false);
    //     murDroitGroup = new MurTriangleMeshGroup(chaletDTO, TypeMur.Droit, murDroitAccessoires, sideTruncate, false);
    //     murGaucheGroup = new MurTriangleMeshGroup(chaletDTO, TypeMur.Gauche, murGaucheAccessoires, sideTruncate, false);

    //     // Si true, des trous sont formés dans les murs pour les accessoires
    //     // Présentement à false puisque les accessoires sont des objets 3D
    //     // Nécessaire pour les exportations
    //     murFacadeGroup.setComputeHoles(false);
    //     murArriereGroup.setComputeHoles(false);
    //     murDroitGroup.setComputeHoles(false);
    //     murGaucheGroup.setComputeHoles(false);

    //     murFacadeGroup.setActiveOuput(this.renduVisuel);
    //     murArriereGroup.setActiveOuput(this.renduVisuel);
    //     murGaucheGroup.setActiveOuput(this.renduVisuel);
    //     murDroitGroup.setActiveOuput(this.renduVisuel);

    //     // TODO: For test purpose. Was trying to define resizable bounding element around meshes. To continue...
    //     // Vector3D[] bounding = murFacadeGroup.getBounding();
    //     // Vector3D diff = bounding[1].sub(bounding[0]);

    //     // Vector3D pos1 = new Vector3D(bounding[1].x + 10, bounding[0].y - 4, bounding[0].z + diff.z / 2);
        
    //     // TriangleMesh boundingCuboid1 = new RectCuboid(pos1, new Vector3D(4, 4, 4));
    //     // TriangleMesh boundingCuboid2 = new RectCuboid(pos1, new Vector3D(4, 4, 4));
    //     // TriangleMesh boundingCuboid3 = new RectCuboid(pos1, new Vector3D(4, 4, 4));

    //     // TriangleMeshGroup boundingGroup1 = new TriangleMeshGroup(new TriangleMesh[] { boundingCuboid1 });
    //     // TriangleMeshGroup boundingGroup2 = new TriangleMeshGroup(new TriangleMesh[] { boundingCuboid2 }).translate(new Vector3D(0, diff.y / 2 - 2, 0));
    //     // TriangleMeshGroup boundingGroup3 = new TriangleMeshGroup(new TriangleMesh[] { boundingCuboid3 }).translate(new Vector3D(0, diff.y + 2, 0));
        
    //     // afficheur.getScene().addMesh(boundingGroup1);
    //     // afficheur.getScene().addMesh(boundingGroup2);
    //     // afficheur.getScene().addMesh(boundingGroup3);
        
    //     // afficheur.getEventSupport().addMeshMouseListener(new MeshMouseListener() {
    //     //     @Override
    //     //     public void meshHovered(MeshMouseMotionEvent e) {
    //     //         System.out.println("Mesh Hovered " + e.getMesh().ID);
    //     //     }

    //     //     @Override
    //     //     public void meshClicked(MeshMouseEvent e) {
    //     //         // TODO Auto-generated method stub
                
    //     //     }

    //     //     @Override
    //     //     public void meshDragEnd(MeshMouseMotionEvent e) {
    //     //         // TODO Auto-generated method stub
                
    //     //     }

    //     //     @Override
    //     //     public void meshDragStart(MeshMouseMotionEvent e) {
    //     //         // TODO Auto-generated method stub
                
    //     //     }

    //     //     @Override
    //     //     public void meshDragged(MeshMouseMotionEvent e) {
    //     //         // TODO Auto-generated method stub
                
    //     //     }

    //     //     @Override
    //     //     public void mouseEnterMesh(MeshMouseMotionEvent e) {
    //     //         // TODO Auto-generated method stub
    //     //         System.out.println("MouseEnterMesh " + e.getMesh().ID);
    //     //         if (e.getMesh().ID == boundingGroup1.ID) {
    //     //             setCursor(new java.awt.Cursor(java.awt.Cursor.E_RESIZE_CURSOR));
    //     //         } else if (e.getMesh().ID == boundingGroup2.id) {
    //     //             setCursor(new java.awt.Cursor(java.awt.Cursor.N_RESIZE_CURSOR));
    //     //         } else if (e.getMesh() == boundingGroup3) {
    //     //             setCursor(new java.awt.Cursor(java.awt.Cursor.S_RESIZE_CURSOR));
    //     //         } else {
    //     //             setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    //     //         }

                
    //     //     }

    //     //     @Override
    //     //     public void mouseExitMesh(MeshMouseMotionEvent e) {
    //     //         // TODO Auto-generated method stub
    //     //         System.out.println("MouseExitMesh " + e.getMesh().ID);
    //     //         setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    //     //     }
    //     // });

    //     // TriangleMeshGroup pignonMesh = PanelHelper.buildPignon(chaletDTO.largeur, chaletDTO.epaisseurMur,
    //     //         chaletDTO.angleToit, new Vector3D(0, 0, 0));
        
    //     // afficheur.getScene().addMesh(pignonMesh);

    //     afficheur.getScene().addMesh(murFacadeGroup);
    //     afficheur.getScene().addMesh(murArriereGroup);
    //     afficheur.getScene().addMesh(murDroitGroup);
    //     afficheur.getScene().addMesh(murGaucheGroup);

    //     afficheur.getScene().getMeshes().addAll(murFacadeGroup.getAccessoiresMeshes());
    //     afficheur.getScene().getMeshes().addAll(murArriereGroup.getAccessoiresMeshes());
    //     afficheur.getScene().getMeshes().addAll(murDroitGroup.getAccessoiresMeshes());
    //     afficheur.getScene().getMeshes().addAll(murGaucheGroup.getAccessoiresMeshes());

    //     // // Pour tester l'importation d'objets à partir de fichiers .obj
    //     if (getControleur().getPreferencesUtilisateur().afficherPlancher) {
    //         try {
    //             URI url = App.class.getResource("/objets/floor_single.obj").toURI();
    //             TriangleMesh mesh = ObjectImporter.importObject(url); // shaep
    //             // mesh = mesh.scale(new Vector3D(1, 1, 1));
    //             mesh.getMaterial().setColor(new Color(114, 114, 114, 255));
    //             // mesh.getMaterial().setShininess(0);
    //             // mesh.getMaterial().setSpecular(0);
    //             mesh.getMaterial().setAmbient(0.5);

    //             TriangleMeshGroup meshGroup = new TriangleMeshGroup(new TriangleMesh[] { mesh });
    //             meshGroup = meshGroup.scale(new Vector3D(1, 1, -1)); // flip the z axis the right way around
    //             meshGroup.setSelectable(false);

    //             meshGroup.setDraggable(false);
    //             afficheur.getScene().addMesh(meshGroup);
    //         } catch (Exception e) {
    //             e.printStackTrace();
    //         }
    //     }
    // }
}
