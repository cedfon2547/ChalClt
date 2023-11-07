package ca.ulaval.glo2004.gui.components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.TypeAccessoire;
import ca.ulaval.glo2004.gui.MainWindow;


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

        DefaultMutableTreeNode tempDefaultMutableTreeNode = (DefaultMutableTreeNode) value;

        if (tempDefaultMutableTreeNode.isRoot()) {
            setIcon(chaletIcon);
        }
        
        try {
            if (value instanceof MurTreeNode) {
                setIcon(murIcon);
            } else if (value instanceof AccessoireTreeNode) {
                AccessoireTreeNode accNode = (AccessoireTreeNode) value;
                if (accNode.getParent() instanceof MurTreeNode) {
                    if (accNode.getTypeAccessoire() == TypeAccessoire.Fenetre) {
                        setIcon(fenetreIcon);
                    } else {
                        setIcon(porteIcon);
                    }
                    // System.out.println("changement de nom " + accNode.getAccessoireDTO());

                    Accessoire.AccessoireDTO accDto = mainWindow.getControleur().getAccessoire(accNode.getAccessoireDTO().accessoireId);                    
                    this.setText(accDto.accessoireNom);
                    this.setPreferredSize(new Dimension(100, 20));
                }

            }
        } finally {
        }

        return this;

    }

}
