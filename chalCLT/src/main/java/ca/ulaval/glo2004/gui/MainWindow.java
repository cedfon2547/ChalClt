package ca.ulaval.glo2004.gui;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.Chalet;
import ca.ulaval.glo2004.domaine.Controleur;
import ca.ulaval.glo2004.domaine.ControleurEventSupport.AccessoireEvent;
import ca.ulaval.glo2004.domaine.ControleurEventSupport.AccessoireEventListener;
import ca.ulaval.glo2004.domaine.ControleurEventSupport.ChaletEvent;
import ca.ulaval.glo2004.domaine.ControleurEventSupport.ChaletEventListener;
import ca.ulaval.glo2004.domaine.ControleurEventSupport.UndoRedoEvent;
import ca.ulaval.glo2004.domaine.ControleurEventSupport.UndoRedoEventListener;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMesh;
import ca.ulaval.glo2004.gui.NotificationManager.NotificationType;
import ca.ulaval.glo2004.gui.components.ArbreDesComposantesChalet;
import ca.ulaval.glo2004.gui.components.DrawingPanel;
import ca.ulaval.glo2004.gui.components.MainWindowTopBarMenu;
import ca.ulaval.glo2004.gui.components.TableAccessoireV2;
import ca.ulaval.glo2004.gui.components.TopButtonPanel;
import ca.ulaval.glo2004.gui.components.TableChaletV2;

/*
 * UI main layout
            MainWindowSplitPane

             _________________________________
            |       |                         |
            | Sider |                         |
            | Top   |                         |
            |       |                         |
Sider       |       |         Main            |
SplitPane   ---------         Section         |
            |       |                         |
            | Sider |                         |
            | Bottom|                         |
            |       |                         |
            |_______|_________________________|
 */
public class MainWindow extends javax.swing.JFrame {
    // ===== OPTIONS =====
    public static final boolean ACTIVE_NOTIFICATION_ALERT = true;
    public static final boolean SHOW_DRAWING_PANEL_TOOLS_SWITCHES = true;

    private Controleur controleur;

    public javax.swing.JPanel mainSection;
    public javax.swing.JPanel sideSection;
    public javax.swing.JSplitPane mainWindowSplitPane;
    public javax.swing.JSplitPane sidePanelSplitPane;
    public javax.swing.JPanel sidePanelTopSection;
    public javax.swing.JPanel sidePanelBottomSection;

    public TableChaletV2 tableProprietesChalet;
    public TableAccessoireV2 tableProprietesAccessoire;
    public JScrollPane tableContainer;
    public DrawingPanel drawingPanel;
    public ArbreDesComposantesChalet arbreDesComposantesChalet;
    public MainWindowTopBarMenu menu;
    public TopButtonPanel topButtonPanel;

    public List<Accessoire.AccessoireDTO> accessoiresSelectionnees = new ArrayList<Accessoire.AccessoireDTO>();

    public NotificationManager notificationManager = new NotificationManager(this);

    public MainWindow(Controleur controleur) {
        super("ChalCLT - Créateur de chalets");
        this.controleur = controleur;
        this.menu = new MainWindowTopBarMenu(this);

        setJMenuBar(menu); // Add the menu bar to the window
        initComponents();
    }

    private void initComponents() {
        Chalet.ChaletDTO chaletDTO = controleur.getChalet();
        List<Accessoire.AccessoireDTO> accessoireDTOs = controleur.getAccessoires();

        mainSection = new javax.swing.JPanel();
        sideSection = new javax.swing.JPanel();
        mainWindowSplitPane = new javax.swing.JSplitPane();
        sidePanelSplitPane = new javax.swing.JSplitPane() {
            @Override
            public void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
            }
        };

        sidePanelTopSection = new javax.swing.JPanel();
        sidePanelBottomSection = new javax.swing.JPanel();
        drawingPanel = new DrawingPanel(this);
        arbreDesComposantesChalet = new ArbreDesComposantesChalet(this, chaletDTO, accessoireDTOs);
        tableContainer = new JScrollPane();

        javax.swing.GroupLayout sideSectionLayout = new javax.swing.GroupLayout(sideSection);
        javax.swing.GroupLayout sidePanelTopSectionLayout = new javax.swing.GroupLayout(sidePanelTopSection);
        javax.swing.GroupLayout sidePanelBottomSectionLayout = new javax.swing.GroupLayout(sidePanelBottomSection);

        mainSection.setLayout(new BorderLayout(10, 10));
        mainSection.add(drawingPanel, BorderLayout.CENTER);

        mainWindowSplitPane.setResizeWeight(0.25);
        sidePanelSplitPane.setResizeWeight(0.5);

        mainWindowSplitPane.setDividerSize(15);
        sidePanelSplitPane.setDividerSize(15);

        mainWindowSplitPane.setOneTouchExpandable(true);
        sidePanelSplitPane.setOneTouchExpandable(false);

        sideSection.setLayout(sideSectionLayout);
        sidePanelTopSection.setLayout(sidePanelTopSectionLayout);
        sidePanelBottomSection.setLayout(sidePanelBottomSectionLayout);

        sideSectionLayout.setHorizontalGroup(
                sideSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE));

        sideSectionLayout.setVerticalGroup(
                sideSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE));

        sidePanelTopSectionLayout.setHorizontalGroup(
                sidePanelTopSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(100, 100, Short.MAX_VALUE)
                        .addComponent(tableContainer));

        sidePanelTopSectionLayout.setVerticalGroup(
                sidePanelTopSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(100, 100, Short.MAX_VALUE)
                        .addComponent(tableContainer));

        sidePanelBottomSectionLayout.setHorizontalGroup(
                sidePanelBottomSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(arbreDesComposantesChalet));

        sidePanelBottomSectionLayout.setVerticalGroup(
                sidePanelBottomSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(arbreDesComposantesChalet));

        sidePanelSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        sidePanelSplitPane.setTopComponent(sidePanelTopSection);
        sidePanelSplitPane.setRightComponent(sidePanelBottomSection);

        mainWindowSplitPane.setRightComponent(mainSection);
        mainWindowSplitPane.setLeftComponent(sidePanelSplitPane);

        mainWindowSplitPane.setDividerLocation(200);
        sidePanelSplitPane.setDividerLocation(200);

        setLayout(new BorderLayout());
        topButtonPanel = new TopButtonPanel(this);
        add(mainWindowSplitPane);
        add(topButtonPanel, BorderLayout.BEFORE_FIRST_LINE);

        tableProprietesChalet = new TableChaletV2(controleur.getChalet());
        tableProprietesChalet.getPcs().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                Chalet.ChaletDTO maxChaletDTO = tableProprietesChalet.getChaletDTO();
                getControleur().setChalet(maxChaletDTO);
            }
        });

        this.getControleur().addChaletEventListener((evt) -> {
            recharger();
        });

        this.getControleur().addAccessoireEventListener(new AccessoireEventListener() {
            @Override
            public void add(AccessoireEvent event) {
                recharger();
            }

            @Override
            public void remove(AccessoireEvent event) {
                recharger();
            }

            @Override
            public void change(AccessoireEvent event) {
                if (tableProprietesAccessoire != null) {
                    tableProprietesAccessoire.updateTable(event.getAccessoireDTO());
                    // drawingPanel.afficheur.rechargerAffichage();
                    // recharger();
                }
            }
        });

        this.getControleur().addUndoRedoEventListener(new UndoRedoEventListener() {
            @Override
            public void undo(UndoRedoEvent event) {
                recharger();
            }

            @Override
            public void redo(UndoRedoEvent event) {
                recharger();
            }
        });

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (tableProprietesChalet.getCellEditor() != null) {
                    tableProprietesChalet.getCellEditor().stopCellEditing();
                }
            }
        });

        drawingPanel.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }
        });

        this.controleur.addChaletEventListener(new ChaletEventListener() {
            @Override
            public void change(ChaletEvent event) {
                recharger();
            }
        });
        showChaletTable();
        this.getContentPane().requestFocusInWindow();
    }

    public Controleur getControleur() {
        return controleur;
    }

    /**
     * Affiche la table des propriétés du chalet.
     */

    public void showChaletTable() {
        TitledBorder border = javax.swing.BorderFactory.createTitledBorder("Propriétés du chalet");
        tableContainer.setBorder(border);
        tableContainer.add(tableProprietesChalet.getTableHeader());
        tableContainer.setViewportView(tableProprietesChalet);

        tableContainer.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                // System.out.println("Focus gained");
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                // System.out.println("Focus lost");
            }
        });
        // tableContainer.setBorder(tableProprietesChalet.getTitledBorder());
        // tableContainer.add(tableProprietesChalet.getTableHeader());
        // tableContainer.setViewportView(tableProprietesChalet);
    }

    /**
     * Affiche la table des propriétés d'un accessoire.
     * 
     * @param dtoAccessoire le DTO de l'accessoire à afficher
     */
    public void showAccessoireTable(Accessoire.AccessoireDTO dtoAccessoire) {
        if (dtoAccessoire == null) {
            return;
        }

        if (tableProprietesAccessoire == null) {
            tableProprietesAccessoire = new TableAccessoireV2(dtoAccessoire);
            tableProprietesAccessoire.getPcs().addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(java.beans.PropertyChangeEvent evt) {
                    // System.out.println("Property changed: " + evt.getPropertyName() + " " +
                    // evt.getNewValue());
                    getControleur().setAccessoire(tableProprietesAccessoire.getAccessoireDTO());
                }
            });
        } else {
            tableProprietesAccessoire.updateTable(dtoAccessoire);
        }

        TitledBorder border = javax.swing.BorderFactory.createTitledBorder("Propriétés de l'accessoire");
        tableContainer.setBorder(border);
        tableContainer.add(tableProprietesAccessoire.getTableHeader());
        tableContainer.setViewportView(tableProprietesAccessoire);

    }

    public List<Accessoire.AccessoireDTO> getAccessoiresSelectionnees() {
        return accessoiresSelectionnees;
    }

    public void ajouterAccessoireSelectionnee(Accessoire.AccessoireDTO accessoireDTO) {
        accessoiresSelectionnees.add(accessoireDTO);
        topButtonPanel.recharger();
        TriangleMesh mesh = drawingPanel.afficheur.getScene().getMesh(accessoireDTO.accessoireId.toString());

        if (mesh != null) {
            mesh.setSelected(true);
            drawingPanel.repaint();
        }
    }

    public void retirerAccessoireSelectionnee(Accessoire.AccessoireDTO accessoireDTO) {
        if (accessoireDTO == null) {
            return;
        }
        accessoiresSelectionnees.remove(accessoireDTO);
        topButtonPanel.recharger();
        TriangleMesh mesh = drawingPanel.afficheur.getScene().getMesh(accessoireDTO.accessoireId.toString());
        if (mesh != null) {
            mesh.setSelected(false);
            drawingPanel.repaint();
        }
    }

    public void clearAccessoiresSelectionnees() {
        // System.out.println("Clearing selected accessories");
        for (Accessoire.AccessoireDTO accessoireDTO : accessoiresSelectionnees) {
            TriangleMesh mesh = drawingPanel.afficheur.getScene().getMesh(accessoireDTO.accessoireId.toString());
            if (mesh != null) {
                mesh.setSelected(false);
            }
        }

        accessoiresSelectionnees.clear();
        topButtonPanel.recharger();
        drawingPanel.repaint();
    }

    /**
     * Supprime tous les accessoires sélectionnés dans l'arbre des composantes du
     * chalet.
     * Cette méthode supprime les accessoires sélectionnés de la liste des
     * accessoires sélectionnés,
     * supprime les nœuds correspondants de l'arbre des composantes du chalet et
     * recharge l'affichage.
     */
    public void deleteAllAccessoiresSelectionnees() {
        this.getControleur().retirerAccessoires(accessoiresSelectionnees);
        recharger();
    }

    public void reloadArbreComposantes() {
        Chalet.ChaletDTO chaletDTO = controleur.getChalet();
        List<Accessoire.AccessoireDTO> accessoireDTOs = controleur.getAccessoires();

        arbreDesComposantesChalet.reloadTree(chaletDTO, accessoireDTOs);
    }

    public void recharger() {
        Chalet.ChaletDTO chaletDTO = controleur.getChalet();
        List<Accessoire.AccessoireDTO> accessoireDTOs = controleur.getAccessoires();

        clearAccessoiresSelectionnees();
        if (tableProprietesChalet != null) {
            tableProprietesChalet.updateTable(chaletDTO);
            // tableProprietesChalet.recharger();
        }

        if (tableProprietesAccessoire != null) {
            Accessoire.AccessoireDTO accessoireDTO = tableProprietesAccessoire.getAccessoireDTO();
            Accessoire.AccessoireDTO newAccessoireDTO = accessoireDTOs.stream()
                    .filter(accessoire -> accessoire.accessoireId == accessoireDTO.accessoireId).findFirst()
                    .orElse(null);
            if (newAccessoireDTO != null) {
                tableProprietesAccessoire.updateTable(newAccessoireDTO);
            }
            // tableProprietesAccessoire.recharger();
        }

        if (arbreDesComposantesChalet != null) {
            arbreDesComposantesChalet.recharger();
        }

        if (drawingPanel != null) {
            drawingPanel.afficheur.rechargerAffichage();
        }

        if (topButtonPanel != null) {
            topButtonPanel.recharger();
        }

        if (menu != null) {
            menu.recharger();
        }

        drawingPanel.updateToolbarBtns();

        // List<String> selectedIDs = accessoiresSelectionnees.stream().map(accessoire
        // ->
        // accessoire.accessoireId.toString()).collect(java.util.stream.Collectors.toList());
        // System.out.println("Accessoires sélectionnés: " + selectedIDs.toString());
        // // drawingPanel.afficheur.setSelectedMeshes(selectedIDs);
    }

    public void dispatchNotificationAlert(String title, String message, NotificationType type) {
        if (ACTIVE_NOTIFICATION_ALERT) {
            notificationManager.createNotification(title, message, type);
        }
    }

    public void dispatchNotificationAlert(String title, String message, NotificationType type, int duration) {
        if (ACTIVE_NOTIFICATION_ALERT) {
            notificationManager.createNotification(title, message, type).setTimer(duration);
        }
    }
}