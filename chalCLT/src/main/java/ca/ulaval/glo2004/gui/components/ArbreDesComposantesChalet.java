package ca.ulaval.glo2004.gui.components;

import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.Chalet;
import ca.ulaval.glo2004.domaine.ControleurEventSupport.AccessoireEvent;
import ca.ulaval.glo2004.domaine.ControleurEventSupport.AccessoireEventListener;
import ca.ulaval.glo2004.domaine.ControleurEventSupport.ChaletEventListener;
import ca.ulaval.glo2004.gui.Constants;
import ca.ulaval.glo2004.gui.MainWindow;
import ca.ulaval.glo2004.domaine.afficheur.Afficheur;

public class ArbreDesComposantesChalet extends javax.swing.JPanel {
    MainWindow mainWindow;

    Chalet.ChaletDTO chaletDTO = null;
    List<Accessoire.AccessoireDTO> accessoireDTOs = new ArrayList<>();

    public ArbreDesComposantesChalet(MainWindow mainWindow, Chalet.ChaletDTO chaletDTO,
            List<Accessoire.AccessoireDTO> accessoireDTOs) {
        this.mainWindow = mainWindow;
        this.chaletDTO = chaletDTO;
        this.accessoireDTOs = accessoireDTOs;

        initComponent();

        this.mainWindow.getControleur().addChaletEventListener(new ChaletEventListener() {
            public void change(ca.ulaval.glo2004.domaine.ControleurEventSupport.ChaletEvent event) {
                chaletNode.setUserObject(event.getChaletDTO().nom);
                ((DefaultTreeModel) arbreComposantesChalet.getModel()).nodeChanged(chaletNode);
            };
        });

        this.mainWindow.getControleur().addAccessoireEventListener(new AccessoireEventListener() {
            @Override
            public void change(AccessoireEvent event) {
                // TODO Auto-generated method stub
                Accessoire.AccessoireDTO accessoireDTO = event.getAccessoireDTO();
                updateNoeudAccessoire(accessoireDTO);

            }

            @Override
            public void add(AccessoireEvent event) {
                // TODO Auto-generated method stub
                Accessoire.AccessoireDTO accDto = event.getAccessoireDTO();
                ajouterNoeudAccessoire(accDto);

            }

            public void remove(AccessoireEvent event) {
                retirerNoeudAccessoire(event.getAccessoireDTO());
                mainWindow.showChaletTable();
            };
        });
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

    public List<AccessoireTreeNode> accessoireNodes = new ArrayList<AccessoireTreeNode>();

    private void initComponent() {
        titledBorder = javax.swing.BorderFactory.createTitledBorder("Composantes");
        setBorder(titledBorder);

        treeScrollPane = new javax.swing.JScrollPane();
        arbreComposantesChalet = new javax.swing.JTree();

        arbreComposantesChalet.addFocusListener(this.getFocusListener());

        arbreComposantesChalet.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        chaletNode = new javax.swing.tree.DefaultMutableTreeNode();
        mursNode = new javax.swing.tree.DefaultMutableTreeNode(Constants._STRING_MURS);
        toitNode = new javax.swing.tree.DefaultMutableTreeNode(Constants._STRING_TOIT);
        murFacadeNode = new MurTreeNode(Constants._STRING_MUR_FACADE);
        murArriereNode = new MurTreeNode(Constants._STRING_MUR_ARRIERE);
        murDroitNode = new MurTreeNode(Constants._STRING_MUR_DROIT);
        murGaucheNode = new MurTreeNode(Constants._STRING_MUR_GAUCHE);
        toitPanneauSuperieurNode = new javax.swing.tree.DefaultMutableTreeNode(Constants._STRING_PANNEAU_SUPERIEUR);
        toitRallongeVerticaleNode = new javax.swing.tree.DefaultMutableTreeNode(Constants._STRING_RALLONGE_VERTICALE);
        toitPignonDroitNode = new javax.swing.tree.DefaultMutableTreeNode(Constants._STRING_PIGNON_DROIT);
        toitPignonGaucheNode = new javax.swing.tree.DefaultMutableTreeNode(Constants._STRING_PIGNON_GAUCHE);
        treeRenderer = new TreeRenderer(mainWindow);

        arbreComposantesChalet.setCellEditor(new DefaultTreeCellEditor(arbreComposantesChalet,
                (DefaultTreeCellRenderer) arbreComposantesChalet.getCellRenderer()) {
            @Override
            public boolean isCellEditable(EventObject event) {
                return false;
            }
        });

        arbreComposantesChalet.setCellRenderer(treeRenderer);

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
        arbreComposantesChalet.setToolTipText("Arbre des composantes");
        arbreComposantesChalet.setEditable(true);
        arbreComposantesChalet.setName("arbreDesComposantes"); // NOI18N
        arbreComposantesChalet.setShowsRootHandles(true);
        arbreComposantesChalet.getAccessibleContext().setAccessibleName("ArbreDesComposantes");
        arbreComposantesChalet.getAccessibleContext().setAccessibleDescription("Arbre des composantes");
        arbreComposantesChalet.setToggleClickCount(0);
        arbreComposantesChalet.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);

        treeRenderer.setClosedIcon(null);
        treeRenderer.setOpenIcon(null);
        treeRenderer.setIcon(null);
        treeRenderer.setIconTextGap(4);

        treeScrollPane.setViewportView(arbreComposantesChalet);

        arbreComposantesChalet.addMouseListener(this.getTreeMouseListener());
        arbreComposantesChalet.addFocusListener(this.getFocusListener());
        arbreComposantesChalet.addTreeSelectionListener(this.getTreeSelectionListener());

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

        reloadTree(chaletDTO, accessoireDTOs);
    }

    private TreeSelectionListener getTreeSelectionListener() {
        return new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                // mainWindow.clearAccessoiresSelectionnees();

                TreePath[] treePaths = arbreComposantesChalet.getSelectionPaths();

                if (treePaths == null)
                    return;

                for (TreePath path : treePaths) {
                    if (path.getLastPathComponent() instanceof AccessoireTreeNode) {
                        AccessoireTreeNode nodeAcc = (AccessoireTreeNode) path.getLastPathComponent();
                        if (nodeAcc != null) {
                            mainWindow.ajouterAccessoireSelectionnee(nodeAcc.getAccessoireDTO());
                        }
                    }
                }
            }
        };
    }

    private MouseAdapter getTreeMouseListener() {
        return new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TreePath path = arbreComposantesChalet.getPathForLocation(evt.getX(), evt.getY());

                if (path == null)
                    return;

                if (path.getPath().length == 1) {
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
                        case Constants._STRING_MUR_FACADE:
                            // System.out.println("Mur Facade");
                            mainWindow.drawingPanel.changerVue(Afficheur.TypeDeVue.Facade);
                            break;
                        case Constants._STRING_MUR_ARRIERE:
                            // System.out.println("Mur Arriere");
                            mainWindow.drawingPanel.changerVue(Afficheur.TypeDeVue.Arriere);
                            break;
                        case Constants._STRING_MUR_DROIT:
                            // System.out.println("Mur droit");
                            mainWindow.drawingPanel.changerVue(Afficheur.TypeDeVue.Droite);
                            break;
                        case Constants._STRING_MUR_GAUCHE:
                            // System.out.println("Mur gauche");
                            mainWindow.drawingPanel.changerVue(Afficheur.TypeDeVue.Gauche);
                            break;
                        case Constants._STRING_PANNEAU_SUPERIEUR:
                            // System.out.println("Panneau supérieur");
                            break;
                        case Constants._STRING_RALLONGE_VERTICALE:
                            // System.out.println("Rallonge verticale");
                            break;
                        case Constants._STRING_PIGNON_DROIT:
                            // System.out.println("Pignon droit");
                            break;
                        case Constants._STRING_PIGNON_GAUCHE:
                            // System.out.println("Pignon gauche");
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
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                // System.out.println("Focus lost "
                //         + (evt.getOppositeComponent() == mainWindow.topButtonPanel.supprimerAccessoireBtn));
                if (evt.getOppositeComponent() != mainWindow.topButtonPanel.supprimerAccessoireBtn) {
                    mainWindow.clearAccessoiresSelectionnees();
                    setSelectedAccessoire(mainWindow.accessoiresSelectionnees);
                }
            }
        };
    }

    public void rechargerNoeudAccessoire(Accessoire.AccessoireDTO accessoireDTO) {
        for (AccessoireTreeNode accNode : accessoireNodes) {
            if (accNode.getAccessoireDTO().accessoireId == accessoireDTO.accessoireId) {
                // accNode.setUserObject(accessoireDTO);
                ((DefaultTreeModel) arbreComposantesChalet.getModel()).reload(accNode);
                break;
            }
        }
    }

    public void updateNoeudAccessoire(Accessoire.AccessoireDTO accessoireDTO) {
        for (AccessoireTreeNode accNode : accessoireNodes) {
            if (accNode.getAccessoireDTO().accessoireId == accessoireDTO.accessoireId) {
                accNode.setUserObject(accessoireDTO);
                accNode.setAccessoireDTO(accessoireDTO);
                ((DefaultTreeModel) arbreComposantesChalet.getModel()).reload(accNode);
                break;
            }
        }
    }

    public void ajouterNoeudAccessoire(Accessoire.AccessoireDTO accessoireDTO) {
        AccessoireTreeNode accessoireNode = new AccessoireTreeNode(accessoireDTO);
        accessoireNodes.add(accessoireNode);

        switch (accessoireDTO.typeMur) {
            case Facade:
                murFacadeNode.insert(accessoireNode, murFacadeNode.getChildCount());
                accessoireNode.setUserObject(accessoireDTO);

                // Expand the node by default
                arbreComposantesChalet.expandPath(new TreePath(murFacadeNode.getPath()));
                // Dispatch the event to update the tree
                ((DefaultTreeModel) arbreComposantesChalet.getModel()).nodeStructureChanged(murFacadeNode);
                break;
            case Arriere:
                murArriereNode.insert(accessoireNode, murArriereNode.getChildCount());
                accessoireNode.setUserObject(accessoireDTO);
                arbreComposantesChalet.expandPath(new TreePath(murArriereNode.getPath()));
                ((DefaultTreeModel) arbreComposantesChalet.getModel()).nodeStructureChanged(murArriereNode);
                break;
            case Droit:
                murDroitNode.insert(accessoireNode, murDroitNode.getChildCount());
                accessoireNode.setUserObject(accessoireDTO);
                arbreComposantesChalet.expandPath(new TreePath(murDroitNode.getPath()));
                ((DefaultTreeModel) arbreComposantesChalet.getModel()).nodeStructureChanged(murDroitNode);
                break;
            case Gauche:
                murGaucheNode.insert(accessoireNode, murGaucheNode.getChildCount());
                accessoireNode.setUserObject(accessoireDTO);
                arbreComposantesChalet.expandPath(new TreePath(murGaucheNode.getPath()));
                ((DefaultTreeModel) arbreComposantesChalet.getModel()).nodeStructureChanged(murGaucheNode);
                break;
            default:
                return;
        }

        // Scroll to the newly added node
        arbreComposantesChalet.scrollPathToVisible(new TreePath(accessoireNode.getPath()));
    }

    public void retirerNoeudAccessoire(Accessoire.AccessoireDTO accessoireDTO) {
        for (AccessoireTreeNode accNode : accessoireNodes) {
            if (accNode.getAccessoireDTO().accessoireId == accessoireDTO.accessoireId) {
                ((DefaultTreeModel) arbreComposantesChalet.getModel()).removeNodeFromParent(accNode);
                accessoireNodes.remove(accNode);
                break;
            }
        }
    }

    public void removeAllNoeudAccessoire() {
        for (AccessoireTreeNode accNode : accessoireNodes) {
            ((DefaultTreeModel) arbreComposantesChalet.getModel()).removeNodeFromParent(accNode);
        }

        this.accessoireNodes.clear();
    }

    public void rechargerNoeudsAccessoire(List<Accessoire.AccessoireDTO> accessoireDTOs) {
        DefaultTreeModel model = (DefaultTreeModel) arbreComposantesChalet.getModel();

        List<UUID> newAccessoireNodeIds = accessoireDTOs.stream()
                .map((accessoireDTO) -> accessoireDTO.accessoireId)
                .collect(Collectors.toList());

        List<UUID> currentAccessoireNodeIds = accessoireNodes.stream().map((accTreeNode) -> accTreeNode.getUuid())
                .collect(Collectors.toList());

        List<Accessoire.AccessoireDTO> toAdd = accessoireDTOs.stream()
                .filter((accessoireDTO) -> !currentAccessoireNodeIds.contains(accessoireDTO.accessoireId))
                .collect(Collectors.toList());
        List<AccessoireTreeNode> toRemove = accessoireNodes.stream()
                .filter((accTreeNode) -> !newAccessoireNodeIds.contains(accTreeNode.getUuid()))
                .collect(Collectors.toList());

        for (AccessoireTreeNode accTreeNode : toRemove) {
            model.removeNodeFromParent(accTreeNode);
            accessoireNodes.remove(accTreeNode);
        }

        for (Accessoire.AccessoireDTO accessoireDTO : toAdd) {
            ajouterNoeudAccessoire(accessoireDTO);
        }
    }

    public void reloadTree(Chalet.ChaletDTO chalet, List<Accessoire.AccessoireDTO> accessoireDTOs) {
        removeAllNoeudAccessoire();

        this.chaletDTO = chalet;
        this.accessoireDTOs = accessoireDTOs;

        chaletNode.setUserObject(chaletDTO.nom);
        rechargerNoeudsAccessoire(accessoireDTOs);

        ((DefaultTreeModel) arbreComposantesChalet.getModel()).reload(chaletNode);

        arbreComposantesChalet.expandPath(new TreePath(mursNode.getPath()));
    }

    public void recharger() {
        // accessoireDTOs.clear();
        // accessoireNodes.clear();

        Chalet.ChaletDTO chaletDTO = mainWindow.getControleur().getChalet();
        List<Accessoire.AccessoireDTO> accessoireDTOs = mainWindow.getControleur().getAccessoires();

        reloadTree(chaletDTO, accessoireDTOs);

        for (int i = 0; i < arbreComposantesChalet.getRowCount(); i++) {
            TreePath rowPath = arbreComposantesChalet.getPathForRow(i);

            // Preventing expending `Toit` node for the moment.
            if (rowPath.toString().contains("Toit")) {
                continue;
            }

            arbreComposantesChalet.expandPath(rowPath);
        }
    }

    public void setSelectedAccessoire(List<Accessoire.AccessoireDTO> accessoireDTOs) {
        List<TreePath> paths = new ArrayList<TreePath>();

        for (Accessoire.AccessoireDTO accDto : accessoireDTOs) {
            for (AccessoireTreeNode accNode : accessoireNodes) {
                if (accNode.getAccessoireDTO().accessoireId == accDto.accessoireId) {
                    paths.add(new TreePath(accNode.getPath()));
                    break;
                }
            }
        }

        arbreComposantesChalet.setSelectionPaths(paths.toArray(new TreePath[paths.size()]));
    }
}
