package ca.ulaval.glo2004.domaine.afficheur;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo2004.App;
import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.Chalet;
import ca.ulaval.glo2004.domaine.Controleur;
import ca.ulaval.glo2004.domaine.PreferencesUtilisateur;
import ca.ulaval.glo2004.domaine.TypeAccessoire;
import ca.ulaval.glo2004.domaine.TypeMur;
import ca.ulaval.glo2004.domaine.TypeSensToit;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.Rasterizer;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.Material;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.Triangle;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMesh;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMeshGroup;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene.Scene;
import ca.ulaval.glo2004.domaine.utils.ObjectImporter;
import ca.ulaval.glo2004.domaine.utils.PanelHelper;
import ca.ulaval.glo2004.domaine.utils.STLTools;
import ca.ulaval.glo2004.gui.Constants;


public class Afficheur {
    // private BufferedImage bufImage = new BufferedImage(500, 500,
    // BufferedImage.TYPE_INT_RGB);
    private Controleur controleur;

    private Dimension dimension;
    Scene scene;
    Rasterizer rasterizer;
    TypeDeVue vueActive = TypeDeVue.Dessus;

    public TriangleMeshGroup murFacadeGroup;
    public TriangleMeshGroup murArriereGroup;
    public TriangleMeshGroup murDroitGroup;
    public TriangleMeshGroup murGaucheGroup;

    public Afficheur(Controleur controleur, Dimension dimension) {
        this.controleur = controleur;
        this.dimension = dimension;
        this.scene = new Scene();
        this.rasterizer = new Rasterizer(this.scene);
        this.scene.getLight().setPosition(new Vector3D(0, 0, 1000));
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

    public void rechargerAffichage() throws Exception {
        // System.out.println("RENDERING");

        Chalet.ChaletDTO chaletDTO = this.getControleur().getChalet();
        PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO = this.getControleur()
                .getPreferencesUtilisateur();
        // scene.getConfiguration().setShowGridXY(preferencesUtilisateurDTO.afficherGrille);
        // scene.getConfiguration().setShowGridYZ(preferencesUtilisateurDTO.afficherGrille);
        scene.getConfiguration().setShowGridXZ(preferencesUtilisateurDTO.afficherGrille);
        scene.clearMeshes();

        Material murFacadeMaterial = new Material();
        Material murArriereMaterial = new Material();
        Material murDroitMaterial = new Material();
        Material murGaucheMaterial = new Material();

        murFacadeMaterial.setColor(java.awt.Color.RED);
        murArriereMaterial.setColor(java.awt.Color.BLUE);
        murDroitMaterial.setColor(java.awt.Color.GREEN);
        murGaucheMaterial.setColor(java.awt.Color.YELLOW);

        boolean sideTruncate = chaletDTO.sensToit == TypeSensToit.Nord || chaletDTO.sensToit == TypeSensToit.Sud;

        murFacadeGroup = new TriangleMeshGroup(new TriangleMesh[] {
                TriangleMesh.fromDoubleList(PanelHelper.buildWall(new double[] { 0, 0, 0 },
                        new Dimension((int) chaletDTO.largeur, (int) chaletDTO.hauteur), chaletDTO.epaisseurMur,
                        chaletDTO.margeSupplementaireRetrait, !sideTruncate), murFacadeMaterial),
        });
        murArriereGroup = new TriangleMeshGroup(new TriangleMesh[] {
                TriangleMesh.fromDoubleList(PanelHelper.buildWall(new double[] { 0, 0, 0 },
                        new Dimension((int) chaletDTO.largeur, (int) chaletDTO.hauteur), chaletDTO.epaisseurMur,
                        chaletDTO.margeSupplementaireRetrait, !sideTruncate), murArriereMaterial),
        });
        murDroitGroup = new TriangleMeshGroup(new TriangleMesh[] {
                TriangleMesh.fromDoubleList(PanelHelper.buildWall(new double[] { 0, 0, 0 },
                        new Dimension((int) chaletDTO.longueur, (int) chaletDTO.hauteur), chaletDTO.epaisseurMur,
                        chaletDTO.margeSupplementaireRetrait, sideTruncate), murDroitMaterial),
        });
        murGaucheGroup = new TriangleMeshGroup(new TriangleMesh[] {
                TriangleMesh.fromDoubleList(
                        PanelHelper.buildWall(new double[] { 0, 0, 0 },
                                new Dimension((int) chaletDTO.longueur, (int) chaletDTO.hauteur),
                                chaletDTO.epaisseurMur, chaletDTO.margeSupplementaireRetrait, sideTruncate),
                        murGaucheMaterial),
        });

        murFacadeGroup.getMesh(0).setHandle(Constants._STRING_MUR_FACADE);
        murArriereGroup.getMesh(0).setHandle(Constants._STRING_MUR_ARRIERE);
        murDroitGroup.getMesh(0).setHandle(Constants._STRING_MUR_DROIT);
        murGaucheGroup.getMesh(0).setHandle(Constants._STRING_MUR_GAUCHE);

        murFacadeGroup = murFacadeGroup.translate(murFacadeGroup.getCenter().multiply(-1));
        murArriereGroup = murArriereGroup.translate(murArriereGroup.getCenter().multiply(-1));
        murDroitGroup = murDroitGroup.translate(murDroitGroup.getCenter().multiply(-1));
        murGaucheGroup = murGaucheGroup.translate(murGaucheGroup.getCenter().multiply(-1));

        murArriereGroup = murArriereGroup.rotate(0, Math.PI, 0);
        murDroitGroup = murDroitGroup.rotate(0, Math.PI / 2, 0);
        murGaucheGroup = murGaucheGroup.rotate(0, -Math.PI / 2, 0);

        murFacadeGroup = murFacadeGroup.translate(new Vector3D(0, 0, -chaletDTO.longueur / 2));
        murArriereGroup = murArriereGroup.translate(new Vector3D(0, 0, chaletDTO.longueur / 2));
        murDroitGroup = murDroitGroup.translate(new Vector3D(-chaletDTO.largeur / 2, 0, 0));
        murGaucheGroup = murGaucheGroup.translate(new Vector3D(chaletDTO.largeur / 2, 0, 0));

        // Connecter les murs entre eux
        murFacadeGroup = murFacadeGroup.translate(new Vector3D(0, 0, chaletDTO.epaisseurMur / 2));
        murArriereGroup = murArriereGroup.translate(new Vector3D(0, 0, -chaletDTO.epaisseurMur / 2));
        murDroitGroup = murDroitGroup.translate(new Vector3D(chaletDTO.epaisseurMur / 2, 0, 0));
        murGaucheGroup = murGaucheGroup.translate(new Vector3D(-chaletDTO.epaisseurMur / 2, 0, 0));

        // mettre le chalet sur le plancher
        murFacadeGroup = murFacadeGroup.translate(new Vector3D(0, -chaletDTO.hauteur / 2, 0));
        murArriereGroup = murArriereGroup.translate(new Vector3D(0, -chaletDTO.hauteur / 2, 0));
        murDroitGroup = murDroitGroup.translate(new Vector3D(0, -chaletDTO.hauteur / 2, 0));
        murGaucheGroup = murGaucheGroup.translate(new Vector3D(0, -chaletDTO.hauteur / 2, 0));

        List<STLTools.Triangle> stlTriangles = new ArrayList<STLTools.Triangle>();
        for (Triangle tri : murFacadeGroup.getMesh(0).getTriangles()) {
             double[] normal = tri.getNormal().toArray();
             double[] vertex1 = tri.getVertice(0).toArray();
             double[] vertex2 = tri.getVertice(1).toArray();
             double[] vertex3 = tri.getVertice(2).toArray();

             stlTriangles
                     .add(new STLTools.Triangle(new float[] { (float) normal[0], (float) normal[1], (float) normal[2] },
                             new float[] { (float) vertex1[0], (float) vertex1[1], (float) vertex1[2] },
                             new float[] { (float) vertex2[0], (float) vertex2[1], (float) vertex2[2] },
                             new float[] { (float) vertex3[0], (float) vertex3[1], (float) vertex3[2] }));
         }

         for (Triangle tri : murArriereGroup.getMesh(0).getTriangles()) {
             double[] normal = tri.getNormal().toArray();
             double[] vertex1 = tri.getVertice(0).toArray();
             double[] vertex2 = tri.getVertice(1).toArray();
             double[] vertex3 = tri.getVertice(2).toArray();

             stlTriangles
                     .add(new STLTools.Triangle(new float[] { (float) normal[0], (float) normal[1], (float) normal[2] },
                             new float[] { (float) vertex1[0], (float) vertex1[1], (float) vertex1[2] },
                             new float[] { (float) vertex2[0], (float) vertex2[1], (float) vertex2[2] },
                             new float[] { (float) vertex3[0], (float) vertex3[1], (float) vertex3[2] }));
         }

         for (Triangle tri : murDroitGroup.getMesh(0).getTriangles()) {
             double[] normal = tri.getNormal().toArray();
             double[] vertex1 = tri.getVertice(0).toArray();
             double[] vertex2 = tri.getVertice(1).toArray();
             double[] vertex3 = tri.getVertice(2).toArray();

             stlTriangles
                     .add(new STLTools.Triangle(new float[] { (float) normal[0], (float) normal[1], (float) normal[2] },
                             new float[] { (float) vertex1[0], (float) vertex1[1], (float) vertex1[2] },
                             new float[] { (float) vertex2[0], (float) vertex2[1], (float) vertex2[2] },
                             new float[] { (float) vertex3[0], (float) vertex3[1], (float) vertex3[2] }));
         }

         for (Triangle tri : murGaucheGroup.getMesh(0).getTriangles()) {
             double[] normal = tri.getNormal().toArray();
             double[] vertex1 = tri.getVertice(0).toArray();
             double[] vertex2 = tri.getVertice(1).toArray();
             double[] vertex3 = tri.getVertice(2).toArray();

             stlTriangles
                     .add(new STLTools.Triangle(new float[] { (float) normal[0], (float) normal[1], (float) normal[2] },
                             new float[] { (float) vertex1[0], (float) vertex1[1], (float) vertex1[2] },
                             new float[] { (float) vertex2[0], (float) vertex2[1], (float) vertex2[2] },
                             new float[] { (float) vertex3[0], (float) vertex3[1], (float) vertex3[2] }));
         }

         STLTools.writeSTL(stlTriangles, "test.stl");

        // murFacadeGroup.setVisible(false);
        // murArriereGroup.setVisible(false);

        scene.addMesh(murFacadeGroup);
        scene.addMesh(murArriereGroup);
        scene.addMesh(murDroitGroup);
        scene.addMesh(murGaucheGroup);

        List<Accessoire.AccessoireDTO> accessoires = this.getControleur().getAccessoires();

        for (Accessoire.AccessoireDTO accessoireDTO : accessoires) {
            TypeMur typeMur = accessoireDTO.typeMur;

            TriangleMeshGroup accMesh = null;

            if (accessoireDTO.accessoireType == TypeAccessoire.Fenetre) {
                accMesh = PanelHelper.buildWindow(accessoireDTO.dimensions[0] - 2, accessoireDTO.dimensions[1],
                        new Vector3D(0, 0, -2), 2);
            } else if (accessoireDTO.accessoireType == TypeAccessoire.Porte) {
                accMesh = PanelHelper.buildDoor(accessoireDTO.dimensions[0] - 4, accessoireDTO.dimensions[1],
                        new Vector3D(0, 0, 0), 4);

                // System.out.println((accMesh.getBounding()[1].y) + " "
                // + (chaletDTO.hauteur - accMesh.getHeight() - chaletDTO.margeAccessoire));

            }

            if (accMesh == null)
                throw new Exception("Accessory id is null"); // TODO update exception type

            switch (typeMur) {
                case Facade:

                    if (murFacadeGroup.getMesh(accessoireDTO.accessoireId.toString()) != null) {
                        murFacadeGroup.removeMesh(murFacadeGroup.getMesh(accessoireDTO.accessoireId.toString()));
                    }

                    accMesh = accMesh.translate(accMesh.getCenter().multiply(-1));
                    accMesh = accMesh.translate(new Vector3D(0, 0, -chaletDTO.longueur / 2));
                    accMesh = accMesh.translate(new Vector3D(0, 0, -accMesh.getDepth() / 2));
                    accMesh = accMesh.translate(new Vector3D(chaletDTO.largeur / 2 - accessoireDTO.dimensions[0] / 2,
                            -chaletDTO.hauteur / 2 + accessoireDTO.dimensions[1] / 2, 0));

                    accMesh = accMesh.translate(new Vector3D(-accessoireDTO.position[0], accessoireDTO.position[1], 0));

                    break;
                case Arriere:
                    if (murArriereGroup.getMesh(accessoireDTO.accessoireId.toString()) != null) {
                        murArriereGroup.removeMesh(murArriereGroup.getMesh(accessoireDTO.accessoireId.toString()));
                    }

                    accMesh = accMesh.rotate(0, Math.PI, 0);
                    accMesh = accMesh.translate(accMesh.getCenter().multiply(-1));
                    accMesh = accMesh.translate(new Vector3D(0, 0, accMesh.getDepth() / 2));
                    accMesh = accMesh
                            .translate(new Vector3D(0, 0, chaletDTO.longueur / 2 + chaletDTO.epaisseurMur / 2));
                    accMesh = accMesh.translate(new Vector3D(-chaletDTO.largeur / 2 + accessoireDTO.dimensions[0] / 2,
                            -chaletDTO.hauteur / 2 + accessoireDTO.dimensions[1] / 2, -chaletDTO.epaisseurMur / 2));

                    accMesh = accMesh.translate(new Vector3D(accessoireDTO.position[0], accessoireDTO.position[1], 0));

                    break;
                case Gauche:
                    if (murGaucheGroup.getMesh(accessoireDTO.accessoireId.toString()) != null) {
                        murGaucheGroup.removeMesh(murGaucheGroup.getMesh(accessoireDTO.accessoireId.toString()));
                    }
                    accMesh = accMesh.rotate(0, -Math.PI / 2, 0);
                    accMesh = accMesh.translate(accMesh.getCenter().multiply(-1));
                    accMesh = accMesh.translate(new Vector3D(accMesh.getWidth() / 2, 0, 0));
                    accMesh = accMesh.translate(new Vector3D(chaletDTO.largeur / 2 - chaletDTO.epaisseurMur / 2, 0, 0));
                    accMesh = accMesh
                            .translate(new Vector3D(chaletDTO.epaisseurMur / 2,
                                    -chaletDTO.hauteur / 2 + accessoireDTO.dimensions[1] / 2,
                                    chaletDTO.longueur / 2 - accessoireDTO.dimensions[0] / 2));

                    accMesh = accMesh.translate(new Vector3D(0, accessoireDTO.position[1], -accessoireDTO.position[0]));

                    break;
                case Droit:
                    if (murDroitGroup.getMesh(accessoireDTO.accessoireId.toString()) != null) {
                        murDroitGroup.removeMesh(murDroitGroup.getMesh(accessoireDTO.accessoireId.toString()));
                    }

                    accMesh = accMesh.rotate(0, Math.PI / 2, 0);
                    accMesh = accMesh.translate(accMesh.getCenter().multiply(-1));
                    accMesh = accMesh.translate(new Vector3D(-accMesh.getWidth() / 2, 0, 0));
                    accMesh = accMesh
                            .translate(new Vector3D(-chaletDTO.largeur / 2 + chaletDTO.epaisseurMur / 2, 0, 0));
                    accMesh = accMesh
                            .translate(new Vector3D(-chaletDTO.epaisseurMur / 2,
                                    -chaletDTO.hauteur / 2 + accessoireDTO.dimensions[1] / 2,
                                    -chaletDTO.longueur / 2 + accessoireDTO.dimensions[0] / 2));

                    accMesh = accMesh.translate(new Vector3D(0, accessoireDTO.position[1], accessoireDTO.position[0]));

                    break;
            }
            accMesh = accMesh.translate(new Vector3D(0, -chaletDTO.hauteur / 2, 0));

            accMesh.setIdentifier(accessoireDTO.accessoireId.toString());
            accMesh.setValid(accessoireDTO.valide);

            this.scene.addMesh(accMesh);
            this.scene.setValid(accMesh.getIdentifier(), accessoireDTO.valide);

        }

        // Pour tester l'importation d'objets à partir de fichiers .obj
        URI url = App.class.getResource("/objets/floor.obj").toURI();
        System.out.println(url);
        TriangleMesh mesh = ObjectImporter.importObject(url); // bnnuy
        mesh = mesh.scale(new Vector3D(15, 15, 15));
        mesh.getMaterial().setColor(Color.GRAY);
        mesh.getMaterial().setShininess(0);
        mesh.getMaterial().setSpecular(0);
        mesh.getMaterial().setAmbient(2);

        TriangleMeshGroup meshGroup = new TriangleMeshGroup(new TriangleMesh[] { mesh });
        meshGroup = meshGroup.rotateZ(Math.toRadians(180));
        meshGroup.setSelectable(false);

        scene.addMesh(meshGroup);

    }

    public void draw(Graphics g, Dimension dimension) {
        this.rasterizer.draw(g, dimension);
    }

    public void changerVue(TypeDeVue vue) {
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

        this.getScene().getCamera().setPosition(new Vector3D(this.scene.getCamera().getPosition().x,
                (vueActive == TypeDeVue.Dessus) ? 0 : controleur.getChalet().hauteur / 2,
                this.scene.getCamera().getPosition().z));
    }

    public void weakChangerVue(TypeDeVue vue) {
        this.vueActive = vue;
    }

    public List<TriangleMeshGroup> getSelection() {
        List<TriangleMeshGroup> selection = new ArrayList<TriangleMeshGroup>();

        for (TriangleMeshGroup meshGroup : this.scene.getMeshes()) {
            if (meshGroup.getSelected()) {
                selection.add(meshGroup);
            }
        }

        return selection;
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

        public static Vector3D getDirection(TypeDeVue vue) {
            if (vue == TypeDeVue.Dessus) {
                return vueDessus();
            } else if (vue == TypeDeVue.Facade) {
                return vueFacade();
            } else if (vue == TypeDeVue.Arriere) {
                return vueArriere();
            } else if (vue == TypeDeVue.Droite) {
                return vueDroite();
            } else if (vue == TypeDeVue.Gauche) {
                return vueGauche();
            }

            return null;
        }
    }
}
