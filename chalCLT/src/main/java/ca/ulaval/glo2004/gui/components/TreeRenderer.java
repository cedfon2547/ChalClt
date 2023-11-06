package ca.ulaval.glo2004.gui.components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.util.UUID;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.TypeAccessoire;
import ca.ulaval.glo2004.gui.MainWindow;
import difflib.Delta.TYPE;

public class TreeRenderer extends DefaultTreeCellRenderer {

    private MainWindow mainWindow;

    private ImageIcon chaletIcon;

    private ImageIcon murIcon;

    private ImageIcon fenetreIcon;

    private ImageIcon porteIcon;

    public TreeRenderer(MainWindow mainWindow) {
        super();
        this.mainWindow = mainWindow;
        this.fenetreIcon = new ImageIcon(getClass().getClassLoader().getResource("\\icons\\fenetre_tree.png"));
        this.fenetreIcon.setImage(this.fenetreIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        this.porteIcon = new ImageIcon(getClass().getClassLoader().getResource("\\icons\\porte_tree.png"));
        this.porteIcon.setImage(this.porteIcon.getImage().getScaledInstance(22, 22, Image.SCALE_DEFAULT));
        this.murIcon = new ImageIcon(getClass().getClassLoader().getResource("\\icons\\mur_icon.png"));
        this.murIcon.setImage(this.murIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        this.chaletIcon = new ImageIcon(getClass().getClassLoader().getResource("\\icons\\chalet_icon.png"));
        this.chaletIcon.setImage(this.chaletIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));

    }

    public void setChaletIcon(ImageIcon chaletIcon) {
        this.chaletIcon = chaletIcon;
    }

    public void setMurIcon(ImageIcon murIcon) {
        this.murIcon = murIcon;
    }

    public void setFenetreIcon(ImageIcon fenetreIcon) {
        this.fenetreIcon = fenetreIcon;
    }

    public void setPorteIcon(ImageIcon porteIcon) {
        this.porteIcon = porteIcon;
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
            boolean isLeaf, int row, boolean focused) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, isLeaf, row, focused);

        try {
            // AccessoireTreeNode tempAccessoireTreeNode = (AccessoireTreeNode) value;
            

            if (value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode tempDefaultMutableTreeNode = (DefaultMutableTreeNode) value;
                if(tempDefaultMutableTreeNode.isRoot()){
                    setIcon(chaletIcon);
                }
                switch (tempDefaultMutableTreeNode.getUserObject().toString()) {
                    case ArbreDesComposantesChalet._STRING_MUR_FACADE:
                        setIcon(murIcon);
                        break;
                    case ArbreDesComposantesChalet._STRING_MUR_ARRIERE:
                        setIcon(murIcon);
                        break;
                    case ArbreDesComposantesChalet._STRING_MUR_DROIT:
                        setIcon(murIcon);
                        break;
                    case ArbreDesComposantesChalet._STRING_MUR_GAUCHE:
                        setIcon(murIcon);
                        break;
                    default:
                        break;

                }
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) tempDefaultMutableTreeNode.getParent();

                if (parent != null) {
                    System.out.println(parent.getUserObject());

                    // AccessoireTreeNode tempAccessoireTreeNode = (AccessoireTreeNode) value;
                    switch (parent.getUserObject().toString()) {
                        case ArbreDesComposantesChalet._STRING_MUR_FACADE:
                            System.out.println("2Acc Mur Facade");
                            setIcon(fenetreIcon);
                            break;
                        case ArbreDesComposantesChalet._STRING_MUR_ARRIERE:
                            System.out.println("2Acc Mur Arriere");
                            setIcon(fenetreIcon);
                            break;
                        case ArbreDesComposantesChalet._STRING_MUR_DROIT:
                            System.out.println("2Acc Mur droit");
                            setIcon(fenetreIcon);
                            break;
                        case ArbreDesComposantesChalet._STRING_MUR_GAUCHE:
                            System.out.println("2Acc Mur gauche");
                            setIcon(fenetreIcon);
                            break;
                        default:

                            break;
                    }
                }
                if (tempDefaultMutableTreeNode.getUserObject() instanceof Accessoire.AccessoireDTO) {
                    System.out.println("n'importe quoi");
                    System.out.println(parent.getUserObject());
                    Accessoire.AccessoireDTO accDto = mainWindow.getControleur().getAccessoire(
                            ((Accessoire.AccessoireDTO) tempDefaultMutableTreeNode.getUserObject()).accessoireId);
                    ImageIcon icon;
                    if (accDto.accessoireType == TypeAccessoire.Fenetre) {
                        icon = fenetreIcon;
                    } else {
                        icon = porteIcon;
                    }
                    // AccessoireTreeNode tempAccessoireTreeNode = (AccessoireTreeNode) value;
                    switch (parent.getUserObject().toString()) {
                        case ArbreDesComposantesChalet._STRING_MUR_FACADE:
                            System.out.println("2Acc Mur Facade");
                            setIcon(icon);
                            break;
                        case ArbreDesComposantesChalet._STRING_MUR_ARRIERE:
                            System.out.println("2Acc Mur Arriere");
                            setIcon(icon);
                            break;
                        case ArbreDesComposantesChalet._STRING_MUR_DROIT:
                            System.out.println("2Acc Mur droit");
                            setIcon(icon);
                            break;
                        case ArbreDesComposantesChalet._STRING_MUR_GAUCHE:
                            System.out.println("2Acc Mur gauche");
                            setIcon(icon);
                            break;
                        default:

                            break;
                    }

                    this.setText(accDto.accessoireNom);
                    this.setPreferredSize(new Dimension(100, 30));
                }
            }

            // if(tempAccessoireTreeNode.getTypeAccessoire()==TypeAccessoire.Fenetre){
            // System.out.println("in");
            // setIcon(fenetreIcon);
            // }
        } finally {
        }

        return this;

    }

}
