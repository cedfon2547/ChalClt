package ca.ulaval.glo2004.domaine.afficheur;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;

import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.Chalet;
import ca.ulaval.glo2004.domaine.Controleur;
import ca.ulaval.glo2004.domaine.PreferencesUtilisateur;
import ca.ulaval.glo2004.domaine.TypeAccessoire;
import ca.ulaval.glo2004.domaine.TypeMur;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.Rasterizer;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.Triangle;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMesh;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMeshGroup;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene.Scene;
import ca.ulaval.glo2004.domaine.utils.PanelHelper;


public class Afficheur {
    // private BufferedImage bufImage = new BufferedImage(500, 500,
    // BufferedImage.TYPE_INT_RGB);
    private Controleur controleur;

    private Dimension dimension;
    Scene scene;
    Rasterizer rasterizer;
    TypeDeVue vueActive = TypeDeVue.Dessus;

    public Afficheur(Controleur controleur, Dimension dimension) {
        this.controleur = controleur;
        this.dimension = dimension;
        this.scene = new Scene();
        this.rasterizer = new Rasterizer(this.scene);
        this.scene.getLight().setPosition(new Vector3D(dimension.getWidth(), 200, 0));
        this.scene.getCamera().setDirection(TypeDeVue.vueDessus());
        this.scene.getCamera().setScale(2.01);
    }

    public Scene getScene() {
        return scene;
    }

    public Rasterizer getRasterizer() {
        return rasterizer;
    }

    public Controleur getControleur() {
        return controleur;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public TypeDeVue getVueActive() {
        return vueActive;
    }

    public void setControleur(Controleur controleur) {
        this.controleur = controleur;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public void setRasterizer(Rasterizer rasterizer) {
        this.rasterizer = rasterizer;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setVueActive(TypeDeVue vueActive) {
        this.vueActive = vueActive;
    }

    public void rechargerAffichage() {

        Chalet.ChaletDTO chaletDTO = this.getControleur().getChalet();
        PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO = this.getControleur()
                .getPreferencesUtilisateur();
        scene.getConfiguration().setShowGridXY(preferencesUtilisateurDTO.afficherGrille);
        scene.getConfiguration().setShowGridYZ(preferencesUtilisateurDTO.afficherGrille);
        scene.getConfiguration().setShowGridXZ(preferencesUtilisateurDTO.afficherGrille);
        scene.clearMeshes();

        TriangleMesh[] meshes = PanelHelper.generateMeshMurs(chaletDTO.largeur, chaletDTO.hauteur, chaletDTO.longueur,
                chaletDTO.epaisseurMur);

        List<TriangleMeshGroup> groups = new java.util.ArrayList<TriangleMeshGroup>();
        for (TriangleMesh mesh : meshes) {
            groups.add(new TriangleMeshGroup(new TriangleMesh[] { mesh }));
        }

        List<Accessoire.AccessoireDTO> accessoires = this.getControleur().getAccessoires();

        for (Accessoire.AccessoireDTO accessoireDTO : accessoires) {
            TypeMur typeMur = accessoireDTO.typeMur;

            TriangleMeshGroup accMesh = null;

            if (accessoireDTO.accessoireType == TypeAccessoire.Fenetre) {
                accMesh = PanelHelper.buildWindow(accessoireDTO.dimensions[0], accessoireDTO.dimensions[1],
                        new Vector3D(0, 0, 0), 2);
            } else if (accessoireDTO.accessoireType == TypeAccessoire.Porte) {
                accMesh = PanelHelper.buildDoor(accessoireDTO.dimensions[0], accessoireDTO.dimensions[1],
                        new Vector3D(0, 0, 0), 4);

                // System.out.println((accMesh.getBounding()[1].y) + " "
                //         + (chaletDTO.hauteur - accMesh.getHeight() - chaletDTO.margeAccessoire));

            }

            double margeAccessoire = this.getControleur().getChalet().margeAccessoire;
            switch (typeMur) {
                case Facade:

                    // Moving the door mesh to the top left corner of the wall
                    accMesh = accMesh.translate(meshes[0].getCenter());
                    accMesh = accMesh.translate(new Vector3D(meshes[0].getWidth() / 2 - accMesh.getWidth(),
                            -meshes[0].getHeight() / 2, -chaletDTO.epaisseurMur));
                    accMesh = accMesh.translate(new Vector3D(-margeAccessoire - accessoireDTO.position[0],
                            margeAccessoire + accessoireDTO.position[1], 0));
                    if (accessoireDTO.accessoireType == TypeAccessoire.Porte) {
                        accMesh = accMesh.translate(new Vector3D(0,
                                chaletDTO.hauteur - accMesh.getHeight() - chaletDTO.margeAccessoire, 0));
                    }

                    break;
                case Arriere:

                    accMesh = accMesh.rotateY(Math.PI);
                    accMesh = accMesh.translate(meshes[1].getCenter());
                    if (accessoireDTO.accessoireType == TypeAccessoire.Fenetre) {
                        accMesh = accMesh.translate(new Vector3D(0, 0, -accMesh.getDepth() - 1));
                    }
                    accMesh = accMesh.translate(new Vector3D(
                            -meshes[1].getWidth() / 2 + accMesh.getWidth() / 2 + margeAccessoire
                                    + accessoireDTO.position[0],
                            -meshes[1].getHeight() / 2 + accMesh.getHeight() / 2 + margeAccessoire
                                    + accessoireDTO.position[1],
                            chaletDTO.epaisseurMur + 2));
                    if (accessoireDTO.accessoireType == TypeAccessoire.Porte) {
                        accMesh = accMesh.translate(new Vector3D(0,
                                chaletDTO.hauteur - accMesh.getHeight() - chaletDTO.margeAccessoire, 0));
                    }
                    break;
                case Gauche:

                    accMesh = accMesh.rotateY(-Math.PI / 2);
                    accMesh = accMesh.translate(meshes[3].getCenter());
                    if (accessoireDTO.accessoireType == TypeAccessoire.Fenetre) {
                        accMesh = accMesh.translate(new Vector3D(-accMesh.getWidth() - 1, 0, 0));
                    }
                    accMesh = accMesh.translate(new Vector3D(chaletDTO.epaisseurMur + 2,
                            -meshes[3].getHeight() / 2 + accMesh.getHeight() / 2 + margeAccessoire
                                    + accessoireDTO.position[1],
                            meshes[3].getDepth() / 2 - accMesh.getDepth() / 2 - margeAccessoire
                                    - accessoireDTO.position[0]));
                    if(accessoireDTO.accessoireType == TypeAccessoire.Porte){
                        accMesh = accMesh.translate(new Vector3D(0,chaletDTO.hauteur - accMesh.getHeight() - chaletDTO.margeAccessoire,0));
                    }
                    break;
                case Droit:

                    accMesh = accMesh.rotateY(Math.PI / 2);
                    accMesh = accMesh.translate(meshes[2].getCenter());
                    if (accessoireDTO.accessoireType == TypeAccessoire.Fenetre) {
                        accMesh = accMesh.translate(new Vector3D(accMesh.getWidth() + 1, 0, 0));
                    }
                    accMesh = accMesh.translate(new Vector3D(-chaletDTO.epaisseurMur - 2,
                            -meshes[2].getHeight() / 2 + accMesh.getHeight() / 2 + margeAccessoire
                                    + accessoireDTO.position[1],
                            -meshes[2].getDepth() / 2 + accMesh.getDepth() / 2 + margeAccessoire
                                    + accessoireDTO.position[0]));
                    if(accessoireDTO.accessoireType == TypeAccessoire.Porte){
                        accMesh = accMesh.translate(new Vector3D(0,chaletDTO.hauteur - accMesh.getHeight() - chaletDTO.margeAccessoire,0));
                    }
                    break;
            }

            this.scene.addMesh(accMesh);
            //repaint();
        }
        
        meshes[0].getMaterial().setColor(java.awt.Color.RED);
        meshes[1].getMaterial().setColor(java.awt.Color.BLUE);
        meshes[2].getMaterial().setColor(java.awt.Color.GREEN);
        meshes[3].getMaterial().setColor(java.awt.Color.YELLOW);

        scene.addMeshes(groups);
        //repaint();
    }

    public void draw(Graphics g, Dimension dimension){
        this.rasterizer.draw(g, dimension);
    }

    public void changerVue(TypeDeVue vue){
        this.vueActive = vue;

        if (vueActive == TypeDeVue.Dessus) {
            this.getScene().getCamera().setDirection(TypeDeVue.vueDessus());
        } else if (vueActive == TypeDeVue.Facade) {
            this.getScene().getCamera().setDirection(TypeDeVue.vueFacade());
        } else if (vueActive == TypeDeVue.Arriere) {
            this.getScene().getCamera().setDirection(TypeDeVue.vueArriere());
        } else if (vueActive == TypeDeVue.Droite) {
            this.getScene().getCamera().setDirection(TypeDeVue.vueDroite());
        } else if (vueActive == TypeDeVue.Gauche) {
            this.getScene().getCamera().setDirection(TypeDeVue.vueGauche());
        }
    }

    public static enum TypeDeVue {
        Dessus,
        Facade,
        Arriere,
        Droite,
        Gauche;

        public static Vector3D vueDessus() {
            return new Vector3D(-Math.PI / 2, Math.PI, 0);
        }

        public static Vector3D vueFacade() {
            return new Vector3D(0, Math.PI, 0);
        }

        public static Vector3D vueArriere() {
            return new Vector3D(0, 0, 0);
        }

        public static Vector3D vueDroite() {
            return new Vector3D(0, Math.PI / 2, 0);
        }

        public static Vector3D vueGauche() {
            return new Vector3D(0, -Math.PI / 2, 0);
        }
    }
    // public Image getImage() {
    // return bufImage.getScaledInstance(drawingPanel.getWidth(),
    // drawingPanel.getHeight(), Image.SCALE_SMOOTH);
    // }

    // public void drawGrid(Graphics g) {
    // Dimension panelDimension = this.drawingPanel.getSize();
    // PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateur =
    // this.drawingPanel.getMainWindow().getControleur().getPreferencesUtilisateur();
    // int gridSpacing = preferencesUtilisateur.gridSpacing;
    // Color gridColor = preferencesUtilisateur.gridColor;
    // Color backgroundColor = preferencesUtilisateur.backgroundColor;

    // g.setColor(backgroundColor);
    // g.fillRect(0, 0, (int) panelDimension.getWidth(), (int)
    // panelDimension.getHeight());

    // g.setColor(gridColor);
    // double gridSpacingHeightNormalized = gridSpacing /
    // panelDimension.getHeight();
    // double gridSpacingWidthNormalized = gridSpacing / panelDimension.getWidth();

    // for (double x = 0; x <= 1; x += gridSpacingWidthNormalized) {
    // int x1 = (int) (x * panelDimension.getWidth());
    // g.drawLine(x1, 0, x1, (int) panelDimension.getHeight());
    // }

    // for (double y = 0; y <= 1; y += gridSpacingHeightNormalized) {
    // int y1 = (int) (y * panelDimension.getHeight());
    // g.drawLine(0, y1, (int) panelDimension.getWidth(), y1);
    // }
    // }

    // public void drawMur(Graphics g) {

    // throw new UnsupportedOperationException();
    // }

    // public void drawToit(Graphics g) {
    // throw new UnsupportedOperationException();
    // }
}
