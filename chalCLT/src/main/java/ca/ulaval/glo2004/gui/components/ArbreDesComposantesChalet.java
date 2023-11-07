package ca.ulaval.glo2004.gui.components;

import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.Position;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.beans.PropertyChangeEvent;

import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.Chalet;
import ca.ulaval.glo2004.gui.MainWindow;

public class ArbreDesComposantesChalet extends javax.swing.JPanel {
    MainWindow mainWindow;

    public ArbreDesComposantesChalet(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initComponents();
    }

    public javax.swing.JTree arbreComposantesChalet;
    private javax.swing.JScrollPane treeScrollPane;
    javax.swing.border.TitledBorder titledBorder;
    javax.swing.tree.DefaultMutableTreeNode chaletNode;
    javax.swing.tree.DefaultMutableTreeNode mursNode;
    javax.swing.tree.DefaultMutableTreeNode toitNode;
    javax.swing.tree.DefaultMutableTreeNode murFacadeNode;
    javax.swing.tree.DefaultMutableTreeNode murArriereNode;
    javax.swing.tree.DefaultMutableTreeNode murDroitNode;
    javax.swing.tree.DefaultMutableTreeNode murGaucheNode;
    javax.swing.tree.DefaultMutableTreeNode toitPanneauSuperieurNode;
    javax.swing.tree.DefaultMutableTreeNode toitRallongeVerticaleNode;
    javax.swing.tree.DefaultMutableTreeNode toitPignonDroitNode;
    javax.swing.tree.DefaultMutableTreeNode toitPignonGaucheNode;
    TreeRenderer treeRenderer;

    public static final String _STRING_MURS = "Murs";
    public static final String _STRING_TOIT = "Toit";
    public static final String _STRING_MUR_FACADE = "Mur façade";
    public static final String _STRING_MUR_ARRIERE = "Mur arrière";
    public static final String _STRING_MUR_DROIT = "Mur droit";
    public static final String _STRING_MUR_GAUCHE = "Mur gauche";
    public static final String _STRING_PANNEAU_SUPERIEUR = "Panneau supérieur";
    public static final String _STRING_RALLONGE_VERTICALE = "Rallonge verticale";
    public static final String _STRING_PIGNON_DROIT = "Pignon droit";
    public static final String _STRING_PIGNON_GAUCHE = "Pignon gauche";

    public List<AccessoireTreeNode> accessoireNodes = new ArrayList<AccessoireTreeNode>();

    private void initComponents() {
        titledBorder = javax.swing.BorderFactory.createTitledBorder("Composantes");
        setBorder(titledBorder);

        String nom = this.mainWindow.getControleur().getChalet().nom;
        treeScrollPane = new javax.swing.JScrollPane();
        arbreComposantesChalet = new javax.swing.JTree();

        arbreComposantesChalet.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        chaletNode = new javax.swing.tree.DefaultMutableTreeNode(nom);
        mursNode = new javax.swing.tree.DefaultMutableTreeNode(_STRING_MURS);
        toitNode = new javax.swing.tree.DefaultMutableTreeNode(_STRING_TOIT);
        murFacadeNode = new MurTreeNode(_STRING_MUR_FACADE);
        murArriereNode = new MurTreeNode(_STRING_MUR_ARRIERE);
        murDroitNode = new MurTreeNode(_STRING_MUR_DROIT);
        murGaucheNode = new MurTreeNode(_STRING_MUR_GAUCHE);
        toitPanneauSuperieurNode = new javax.swing.tree.DefaultMutableTreeNode(_STRING_PANNEAU_SUPERIEUR);
        toitRallongeVerticaleNode = new javax.swing.tree.DefaultMutableTreeNode(_STRING_RALLONGE_VERTICALE);
        toitPignonDroitNode = new javax.swing.tree.DefaultMutableTreeNode(_STRING_PIGNON_DROIT);
        toitPignonGaucheNode = new javax.swing.tree.DefaultMutableTreeNode(_STRING_PIGNON_GAUCHE);
        treeRenderer = new TreeRenderer(mainWindow);
        arbreComposantesChalet.setCellRenderer(treeRenderer);

        arbreComposantesChalet.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreePath[] treePaths = arbreComposantesChalet.getSelectionPaths();
                
                if (treePaths == null)
                    return;

                mainWindow.clearAccessoiresSelectionnees();
                for (TreePath path: treePaths) {
                    if (path.getLastPathComponent() instanceof AccessoireTreeNode) {
                        AccessoireTreeNode nodeAcc = (AccessoireTreeNode) path.getLastPathComponent();
                        mainWindow.ajouterAccessoireSelectionnee(nodeAcc.getAccessoireDTO());
                    }
                }
            }
        });

        this.mainWindow.getControleur().addPropertyChangeListener("chalet", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("chalet")) {
                    Chalet.ChaletDTO chaletDTO = (Chalet.ChaletDTO) evt.getNewValue();
                    chaletNode.setUserObject(chaletDTO.nom);
                    ((DefaultTreeModel) arbreComposantesChalet.getModel()).nodeChanged(chaletNode);
                }
            }
        });

        this.mainWindow.getControleur().addPropertyChangeListener("accessoire", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("accessoire")) {
                    Accessoire.AccessoireDTO accDto = (Accessoire.AccessoireDTO) evt.getNewValue();

                    System.out.println(accDto.accessoireNom);
                    
                    AccessoireTreeNode node = null;
                    for (AccessoireTreeNode accNode: accessoireNodes) {
                        if (accNode.getAccessoireDTO().accessoireId == accDto.accessoireId) {
                            node = accNode;
                            break;
                        }
                    }

                    AccessoireTreeNode accessoireTreeNode = (AccessoireTreeNode) arbreComposantesChalet
                            .getLastSelectedPathComponent();
                    TreePath treePath = arbreComposantesChalet.getNextMatch(
                            ((Accessoire.AccessoireDTO) evt.getOldValue()).accessoireNom, 0, Position.Bias.Forward);
                    if (treePath != null) {
                        System.out.println(treePath);
                        accessoireTreeNode.setUserObject(accDto);
                        accessoireTreeNode.setAccessoireDTO(accDto);
                        accessoireTreeNode.setNom(accDto.accessoireNom);
                        ((DefaultTreeModel) arbreComposantesChalet.getModel()).reload(accessoireTreeNode);
                    }
                }
            }
        });

        this.mainWindow.getControleur().addPropertyChangeListener("supprimerAccessoire", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println("Retirer Accessoire " + ((Accessoire.AccessoireDTO) evt.getNewValue()).accessoireNom);
                mainWindow.showChaletTable();
            }
        });

        this.mainWindow.getControleur().addPropertyChangeListener("ajouterAccessoire", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println("ajouterAccessoire");
                Accessoire.AccessoireDTO accDto = (Accessoire.AccessoireDTO) evt.getNewValue();
                AccessoireTreeNode accessoireNode = new AccessoireTreeNode(accDto);

                accessoireNodes.add(accessoireNode);

                switch (accDto.typeMur) {
                    case Facade:
                        murFacadeNode.add(accessoireNode);
                        accessoireNode.setUserObject(accDto);
                        ((DefaultTreeModel) arbreComposantesChalet.getModel()).nodeStructureChanged(murFacadeNode);
                        break;
                    case Arriere:
                        murArriereNode.add(accessoireNode);
                        accessoireNode.setUserObject(accDto);
                        ((DefaultTreeModel) arbreComposantesChalet.getModel()).nodeStructureChanged(murArriereNode);
                        break;
                    case Droit:
                        murDroitNode.add(accessoireNode);
                        accessoireNode.setUserObject(accDto);
                        ((DefaultTreeModel) arbreComposantesChalet.getModel()).nodeStructureChanged(murDroitNode);
                        break;
                    case Gauche:
                        murGaucheNode.add(accessoireNode);
                        accessoireNode.setUserObject(accDto);
                        ((DefaultTreeModel) arbreComposantesChalet.getModel()).nodeStructureChanged(murGaucheNode);

                        break;
                    default:
                        return;
                }

                arbreComposantesChalet.invalidate();
                arbreComposantesChalet.repaint();
            }
        });

        mursNode.add(murFacadeNode);
        mursNode.add(murArriereNode);
        mursNode.add(murDroitNode);
        mursNode.add(murGaucheNode);

        toitNode.add(toitPanneauSuperieurNode);
        toitNode.add(toitRallongeVerticaleNode);
        toitNode.add(toitPignonDroitNode);
        toitNode.add(toitPignonGaucheNode);

        chaletNode.add(mursNode);
        chaletNode.add(toitNode);

        arbreComposantesChalet.setModel(new javax.swing.tree.DefaultTreeModel(chaletNode));
        arbreComposantesChalet.setToolTipText("Table des composantes");
        arbreComposantesChalet.setEditable(true);
        arbreComposantesChalet.setName("tableDesComposantes"); // NOI18N
        arbreComposantesChalet.setShowsRootHandles(true);
        arbreComposantesChalet.getAccessibleContext().setAccessibleName("ArbreDesComposantes");
        arbreComposantesChalet.getAccessibleContext().setAccessibleDescription("Arbre des composantes");
        arbreComposantesChalet.setToggleClickCount(0);

        treeScrollPane.setViewportView(arbreComposantesChalet);

        arbreComposantesChalet.addMouseListener(getTreeMouseListener());
        arbreComposantesChalet.addFocusListener(this.getFocusListener());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(treeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(treeScrollPane)
                                .addGap(0, 0, 0)));
    }

    public MouseAdapter getTreeMouseListener() {
        return new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TreePath path = arbreComposantesChalet.getPathForLocation(evt.getX(), evt.getY());

                if (path == null)
                    return;

                if (path.getPath().length == 1) {
                    System.out.println("Projet");
                    mainWindow.showChaletTable();
                    return;
                }

                TreePath parent = path.getParentPath();
                // node utilisé pour vérifier si le type du node selectionné est
                // AccessoireTreeNode

                if (parent != null) {
                    String parentStr = parent.getLastPathComponent().toString();

                    if (parentStr != null) {
                        if (path.getLastPathComponent() instanceof AccessoireTreeNode) {
                            AccessoireTreeNode nodeAcc = (AccessoireTreeNode) path.getLastPathComponent();
                            Accessoire.AccessoireDTO accDto = mainWindow.getControleur()
                                    .getAccessoire(nodeAcc.getUuid());

                            mainWindow.showAccessoireTable(accDto);
                        } else {
                            mainWindow.showChaletTable();
                        }

                    }
                }

                if (evt.getClickCount() == 2 && evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
                    switch (path.getLastPathComponent().toString()) {
                        case _STRING_MUR_FACADE:
                            System.out.println("Mur Facade");
                            mainWindow.drawingPanel.changerVue(DrawingPanel.TypeDeVue.Facade);
                            break;
                        case _STRING_MUR_ARRIERE:
                            System.out.println("Mur Arriere");
                            mainWindow.drawingPanel.changerVue(DrawingPanel.TypeDeVue.Arriere);
                            break;
                        case _STRING_MUR_DROIT:
                            System.out.println("Mur droit");
                            mainWindow.drawingPanel.changerVue(DrawingPanel.TypeDeVue.Droite);
                            break;
                        case _STRING_MUR_GAUCHE:
                            System.out.println("Mur gauche");
                            mainWindow.drawingPanel.changerVue(DrawingPanel.TypeDeVue.Gauche);
                            break;
                        case _STRING_PANNEAU_SUPERIEUR:
                            System.out.println("Panneau supérieur");
                            break;
                        case _STRING_RALLONGE_VERTICALE:
                            System.out.println("Rallonge verticale");
                            break;
                        case _STRING_PIGNON_DROIT:
                            System.out.println("Pignon droit");
                            break;
                        case _STRING_PIGNON_GAUCHE:
                            System.out.println("Pignon gauche");
                            break;
                    }

                    mainWindow.drawingPanel.updateToolbarBtns();
                    mainWindow.drawingPanel.invalidate();
                    mainWindow.drawingPanel.repaint();
                }
            }
        };
    }

    private FocusListener getFocusListener() {
        return new FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                System.out.println("focusGained");
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                System.out.println("focusLost");
                // arbreComposantesChalet.clearSelection();
            }
        };
    }

    public void rechargerNoeudAccessoire(Accessoire.AccessoireDTO accessoireDTO) {
        for (AccessoireTreeNode accNode: accessoireNodes) {
            if (accNode.getAccessoireDTO().accessoireId == accessoireDTO.accessoireId) {
                // accNode.setUserObject(accessoireDTO);
                ((DefaultTreeModel) arbreComposantesChalet.getModel()).reload(accNode);
                break;
            }
        }
    }
}
