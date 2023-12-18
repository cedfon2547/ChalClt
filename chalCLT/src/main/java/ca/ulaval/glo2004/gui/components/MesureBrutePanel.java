package ca.ulaval.glo2004.gui.components;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import ca.ulaval.glo2004.App;
import ca.ulaval.glo2004.domaine.Chalet.ChaletDTO;
import ca.ulaval.glo2004.domaine.utils.ImperialDimension;

public class MesureBrutePanel extends JPanel {
    ChaletDTO chaletDTO;
    String largeurValueStr;
    String hauteurValueStr;
    String epaisseurValueStr;
    
    JLabel largeur = new JLabel(" largeur :" + largeurValueStr);
    JLabel hauteur = new JLabel(" hauteur :" + hauteurValueStr);
    JLabel epaisseur = new JLabel("épaisseur :" + epaisseurValueStr);

    public MesureBrutePanel(ChaletDTO chaletDTO) {
        this.chaletDTO = chaletDTO;
        this.setLayout(new BoxLayout(this, 1));
        this.setAlignmentX(CENTER_ALIGNMENT);
        String pathLargeurIcon = "/icons/light/horizontal.png";
        String pathHauteurIcon = "/icons/light/vertical.png";
        String pathEpaisseurIcon = "/icons/light/epaisseur.png";

        URL largeurIconURL = App.class.getResource(pathLargeurIcon);
        URL hauteurIconURL = App.class.getResource(pathHauteurIcon);
        URL epaisseurIconURL = App.class.getResource(pathEpaisseurIcon);
        Toolkit tk = Toolkit.getDefaultToolkit();

        Image largeurImg = tk.getImage(largeurIconURL);
        Image hauteurImg = tk.getImage(hauteurIconURL);
        Image epaisseurImg = tk.getImage(epaisseurIconURL);

        ImageIcon largeurIcon = new ImageIcon(largeurImg);
        ImageIcon hauteurIcon = new ImageIcon(hauteurImg);
        ImageIcon epaisseurIcon = new ImageIcon(epaisseurImg);
        
        largeurIcon.setImage(largeurIcon.getImage().getScaledInstance(26, 26, Image.SCALE_DEFAULT));
        hauteurIcon.setImage(hauteurIcon.getImage().getScaledInstance(26, 26, Image.SCALE_DEFAULT));
        epaisseurIcon.setImage(epaisseurIcon.getImage().getScaledInstance(26, 26, Image.SCALE_DEFAULT));

        largeurValueStr = ImperialDimension.format(chaletDTO.largeur).toString();
        hauteurValueStr = ImperialDimension.format(chaletDTO.hauteur).toString();
        epaisseurValueStr = ImperialDimension.format(chaletDTO.epaisseurMur).toString();

        largeur.setForeground(Color.LIGHT_GRAY);
        hauteur.setForeground(Color.LIGHT_GRAY);
        epaisseur.setForeground(Color.LIGHT_GRAY);

        largeur.setIcon(largeurIcon);
        hauteur.setIcon(hauteurIcon);
        epaisseur.setIcon(epaisseurIcon);
        
        largeur.setAlignmentX(CENTER_ALIGNMENT);
        hauteur.setAlignmentX(CENTER_ALIGNMENT);
        epaisseur.setAlignmentX(CENTER_ALIGNMENT);

hauteur.setIconTextGap(6);
        
        this.setBackground(Color.DARK_GRAY);
        this.add(largeur);
        this.add(hauteur);
        this.add(epaisseur);
    }

    public void updatePanel(ChaletDTO chaletDTO, double largeur) {
        this.chaletDTO = chaletDTO;
    this.largeurValueStr = ImperialDimension.format(largeur).toString();
        this.hauteurValueStr = ImperialDimension.format(chaletDTO.hauteur).toString();
        this.epaisseurValueStr = ImperialDimension.format(chaletDTO.epaisseurMur).toString();
        this.largeur.setText(" largeur : " + largeurValueStr);
        this.hauteur.setText(" hauteur : " + hauteurValueStr);
        this.epaisseur.setText("épaisseur : " + epaisseurValueStr);
        this.repaint();
    }
}
