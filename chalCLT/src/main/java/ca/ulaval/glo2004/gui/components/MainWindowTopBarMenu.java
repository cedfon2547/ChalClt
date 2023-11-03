package ca.ulaval.glo2004.gui.components;

import ca.ulaval.glo2004.gui.MainWindow;

public class MainWindowTopBarMenu extends javax.swing.JMenuBar {
    private javax.swing.JMenu affichageMenu;
    private javax.swing.JCheckBoxMenuItem afficherGrilleItem;
    private javax.swing.JCheckBoxMenuItem afficherMursVoisinsItem;
    private javax.swing.JMenuItem annulerItem;
    private javax.swing.JMenu changerVueMenu;
    private javax.swing.JMenu editionMenu;
    private javax.swing.JMenuItem enregistrerItem;
    private javax.swing.JMenu fichierMenu;
    private javax.swing.JMenuItem ajouterAccessoire;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem nouveauProjetItem;
    private javax.swing.JMenuItem ouvrirFichierItem;
    private javax.swing.JMenuItem retablirItem;
    private javax.swing.JMenu selectionMenu;
    private javax.swing.JRadioButtonMenuItem vueArriereItem;
    private javax.swing.JRadioButtonMenuItem vueDroitItem;
    private javax.swing.JRadioButtonMenuItem vueFacadeItem;
    private javax.swing.JRadioButtonMenuItem vueGaucheItem;
    private javax.swing.JRadioButtonMenuItem vueHautItem;

    private MainWindow mainWindow;
    /**
     * Creates main TopBarMenu component
     */
    public MainWindowTopBarMenu(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initComponents();
    }

    private void initComponents() {
        // Création des composantes du menu
        // `Fichier` menu
        fichierMenu = new javax.swing.JMenu("Fichier");
        // `Fichier` menu subitem
        nouveauProjetItem = new javax.swing.JMenuItem("Nouveau");
        ouvrirFichierItem = new javax.swing.JMenuItem("Ouvrir");
        enregistrerItem = new javax.swing.JMenuItem("Enregistrer");
        // `Edition` menu
        editionMenu = new javax.swing.JMenu("Edition");
        // `Edition` menu subitem
        annulerItem = new javax.swing.JMenuItem("Annuler");
        retablirItem = new javax.swing.JMenuItem("Rétablir");
        // `Affichage` menu
        affichageMenu = new javax.swing.JMenu("Affichage");
        // `Affichage` menu subitem
        // `Change de vue` submenu
        changerVueMenu = new javax.swing.JMenu("Change de vue");
        // `Change de vue` radio button submenu subitems.
        // Seulement un peut être sélectionné à la fois.
        vueHautItem = new javax.swing.JRadioButtonMenuItem("Haut");
        vueFacadeItem = new javax.swing.JRadioButtonMenuItem("Façade");
        vueArriereItem = new javax.swing.JRadioButtonMenuItem("Arrière");
        vueDroitItem = new javax.swing.JRadioButtonMenuItem("Côté droit");
        vueGaucheItem = new javax.swing.JRadioButtonMenuItem("Côté gauche");
        afficherGrilleItem = new javax.swing.JCheckBoxMenuItem("Afficher grille");
        afficherMursVoisinsItem = new javax.swing.JCheckBoxMenuItem("Afficher murs voisins");
        // `Selection` menu
        selectionMenu = new javax.swing.JMenu("Sélection");
        // `Selection` menu subitems
        ajouterAccessoire = new javax.swing.JMenuItem("Ajouter un accessoire");
        jMenuItem2 = new javax.swing.JMenuItem("Retirer tous les accessoires");

        // Add menu items to the menu
        fichierMenu.add(nouveauProjetItem);
        fichierMenu.add(ouvrirFichierItem);
        fichierMenu.add(enregistrerItem);
        editionMenu.add(annulerItem);
        editionMenu.add(retablirItem);
        changerVueMenu.add(vueHautItem);
        changerVueMenu.add(vueFacadeItem);
        changerVueMenu.add(vueArriereItem);
        changerVueMenu.add(vueDroitItem);
        changerVueMenu.add(vueGaucheItem);
        affichageMenu.add(changerVueMenu);
        affichageMenu.add(afficherGrilleItem);
        affichageMenu.add(afficherMursVoisinsItem);
        selectionMenu.add(ajouterAccessoire);
        selectionMenu.add(jMenuItem2);

        setTooltips();
        setAccessibility();
        setKeyboardShortcuts();

        // Set default selected menu items (Vue du haut par défaut)
        vueHautItem.setSelected(true);
        vueFacadeItem.setSelected(false);
        vueArriereItem.setSelected(false);
        vueDroitItem.setSelected(false);
        vueGaucheItem.setSelected(false);
        afficherGrilleItem.setSelected(true);
        afficherMursVoisinsItem.setSelected(true);

        // Add menu to the menu bar
        add(fichierMenu);
        add(editionMenu);
        add(affichageMenu);
        add(selectionMenu);

        // Add action listener for each menu item
        nouveauProjetItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nouveauProjetItemActionPerformed(evt);
            }
        });

        ouvrirFichierItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ouvrirFichierItemActionPerformed(evt);
            }
        });

        enregistrerItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enregistrerItemActionPerformed(evt);
            }
        });

        vueHautItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vueHautItemActionPerformed(evt);
            }
        });

        vueFacadeItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vueFacadeItemActionPerformed(evt);
            }
        });

        vueArriereItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vueArriereItemActionPerformed(evt);
            }
        });

        vueDroitItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vueDroitItemActionPerformed(evt);
            }
        });

        vueGaucheItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vueGaucheItemActionPerformed(evt);
            }
        });

        afficherGrilleItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                afficherGrilleItemActionPerformed(evt);
            }
        });

        afficherMursVoisinsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                afficherMursVoisinsItemActionPerformed(evt);
            }
        });

        annulerItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                annulerItemActionPerformed(evt);
            }
        });

        retablirItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retablirItemActionPerformed(evt);
            }
        });

        ajouterAccessoire.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // jMenuItem1ActionPerformed(evt);
            }
        });

        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // jMenuItem2ActionPerformed(evt);
            }
        });
    }

    private void setTooltips() {
        // Set tooltip for each menu item
        setToolTipText("Menu");
        fichierMenu.setToolTipText("Fichier");
        nouveauProjetItem.setToolTipText("Nouveau");
        ouvrirFichierItem.setToolTipText("Ouvrir");
        enregistrerItem.setToolTipText("Enregistrer");
        editionMenu.setToolTipText("Edition");
        annulerItem.setToolTipText("Annuler");
        retablirItem.setToolTipText("Rétablir");
        affichageMenu.setToolTipText("Affichage");
        changerVueMenu.setToolTipText("Change de vue");
        vueHautItem.setToolTipText("Haut");
        vueFacadeItem.setToolTipText("Façade");
        vueArriereItem.setToolTipText("Arrière");
        vueDroitItem.setToolTipText("Côté droit");
        vueGaucheItem.setToolTipText("Côté gauche");
        afficherGrilleItem.setToolTipText("Afficher grille");
        afficherMursVoisinsItem.setToolTipText("Afficher murs voisins");
        selectionMenu.setToolTipText("Sélection");
        ajouterAccessoire.setToolTipText("Item1");
        jMenuItem2.setToolTipText("Item2");
    }

    private void setAccessibility() {
        // Set accessibility for each menu item
        getAccessibleContext().setAccessibleName("Menu");
        getAccessibleContext().setAccessibleDescription("Menu");

        fichierMenu.getAccessibleContext().setAccessibleName("Fichier");
        fichierMenu.getAccessibleContext().setAccessibleDescription("Fichier");

        nouveauProjetItem.getAccessibleContext().setAccessibleName("Nouveau");
        nouveauProjetItem.getAccessibleContext().setAccessibleDescription("Nouveau");

        ouvrirFichierItem.getAccessibleContext().setAccessibleName("Ouvrir");
        ouvrirFichierItem.getAccessibleContext().setAccessibleDescription("Ouvrir");

        enregistrerItem.getAccessibleContext().setAccessibleName("Enregistrer");
        enregistrerItem.getAccessibleContext().setAccessibleDescription("Enregistrer");

        editionMenu.getAccessibleContext().setAccessibleName("Edition");
        editionMenu.getAccessibleContext().setAccessibleDescription("Edition");

        annulerItem.getAccessibleContext().setAccessibleName("Annuler");
        annulerItem.getAccessibleContext().setAccessibleDescription("Annuler");

        retablirItem.getAccessibleContext().setAccessibleName("Rétablir");
        retablirItem.getAccessibleContext().setAccessibleDescription("Rétablir");

        affichageMenu.getAccessibleContext().setAccessibleName("Affichage");
        affichageMenu.getAccessibleContext().setAccessibleDescription("Affichage");

        changerVueMenu.getAccessibleContext().setAccessibleName("Change de vue");
        changerVueMenu.getAccessibleContext().setAccessibleDescription("Change de vue");

        vueHautItem.getAccessibleContext().setAccessibleName("Haut");
        vueHautItem.getAccessibleContext().setAccessibleDescription("Haut");

        vueFacadeItem.getAccessibleContext().setAccessibleName("Façade");
        vueFacadeItem.getAccessibleContext().setAccessibleDescription("Façade");

        vueArriereItem.getAccessibleContext().setAccessibleName("Arrière");
        vueArriereItem.getAccessibleContext().setAccessibleDescription("Arrière");

        vueDroitItem.getAccessibleContext().setAccessibleName("Côté droit");
        vueDroitItem.getAccessibleContext().setAccessibleDescription("Côté droit");

        vueGaucheItem.getAccessibleContext().setAccessibleName("Côté gauche");
        vueGaucheItem.getAccessibleContext().setAccessibleDescription("Côté gauche");

        afficherGrilleItem.getAccessibleContext().setAccessibleName("Afficher grille");
        afficherGrilleItem.getAccessibleContext().setAccessibleDescription("Afficher grille");

        afficherMursVoisinsItem.getAccessibleContext().setAccessibleName("Afficher murs voisins");
        afficherMursVoisinsItem.getAccessibleContext().setAccessibleDescription("Afficher murs voisins");

        selectionMenu.getAccessibleContext().setAccessibleName("Sélection");
        selectionMenu.getAccessibleContext().setAccessibleDescription("Sélection");

        ajouterAccessoire.getAccessibleContext().setAccessibleName("Item1");
        ajouterAccessoire.getAccessibleContext().setAccessibleDescription("Item1");

        jMenuItem2.getAccessibleContext().setAccessibleName("Item2");
        jMenuItem2.getAccessibleContext().setAccessibleDescription("Item2");
    }

    private void setKeyboardShortcuts() {
        // Set keyboard shortcut for each menu item
        nouveauProjetItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N,
                java.awt.event.InputEvent.CTRL_DOWN_MASK));
        ouvrirFichierItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O,
                java.awt.event.InputEvent.CTRL_DOWN_MASK));
        enregistrerItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S,
                java.awt.event.InputEvent.CTRL_DOWN_MASK));
        annulerItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z,
                java.awt.event.InputEvent.CTRL_DOWN_MASK));
        retablirItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z,
                java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        vueHautItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1,
                java.awt.event.InputEvent.CTRL_DOWN_MASK));
        vueFacadeItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2,
                java.awt.event.InputEvent.CTRL_DOWN_MASK));
        vueArriereItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3,
                java.awt.event.InputEvent.CTRL_DOWN_MASK));
        vueDroitItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_4,
                java.awt.event.InputEvent.CTRL_DOWN_MASK));
        vueGaucheItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_5,
                java.awt.event.InputEvent.CTRL_DOWN_MASK));
        afficherGrilleItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G,
                java.awt.event.InputEvent.CTRL_DOWN_MASK));
        afficherMursVoisinsItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H,
                java.awt.event.InputEvent.CTRL_DOWN_MASK));
    }

    private void ouvrirFichierItemActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void enregistrerItemActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void nouveauProjetItemActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void vueHautItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.drawingPanel.changerVue(DrawingPanel.TypeDeVue.Dessus);
    }

    private void vueFacadeItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.drawingPanel.changerVue(DrawingPanel.TypeDeVue.Facade);
    }

    private void vueArriereItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.drawingPanel.changerVue(DrawingPanel.TypeDeVue.Arriere);
    }

    private void vueDroitItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.drawingPanel.changerVue(DrawingPanel.TypeDeVue.Droite);
    }

    private void vueGaucheItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.drawingPanel.changerVue(DrawingPanel.TypeDeVue.Gauche);
    }

    private void afficherGrilleItemActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void afficherMursVoisinsItemActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void annulerItemActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void retablirItemActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    public void activerVue(DrawingPanel.TypeDeVue vue) {
        System.out.println("activerVue");

        switch (vue) {
            case Dessus:
                vueHautItem.setSelected(true);
                vueFacadeItem.setSelected(false);
                vueArriereItem.setSelected(false);
                vueDroitItem.setSelected(false);
                vueGaucheItem.setSelected(false);
                break;
            case Facade:
                vueHautItem.setSelected(false);
                vueFacadeItem.setSelected(true);
                vueArriereItem.setSelected(false);
                vueDroitItem.setSelected(false);
                vueGaucheItem.setSelected(false);
                break;
            case Arriere:
                vueHautItem.setSelected(false);
                vueFacadeItem.setSelected(false);
                vueArriereItem.setSelected(true);
                vueDroitItem.setSelected(false);
                vueGaucheItem.setSelected(false);
                break;
            case Droite:
                vueHautItem.setSelected(false);
                vueFacadeItem.setSelected(false);
                vueArriereItem.setSelected(false);
                vueDroitItem.setSelected(true);
                vueGaucheItem.setSelected(false);
                break;
            case Gauche:
                vueHautItem.setSelected(false);
                vueFacadeItem.setSelected(false);
                vueArriereItem.setSelected(false);
                vueDroitItem.setSelected(false);
                vueGaucheItem.setSelected(true);
                break;
        }
    }
}
