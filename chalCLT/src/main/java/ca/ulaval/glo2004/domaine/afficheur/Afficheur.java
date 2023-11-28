package ca.ulaval.glo2004.domaine.afficheur;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.Chalet;
import ca.ulaval.glo2004.domaine.Controleur;
import ca.ulaval.glo2004.domaine.PreferencesUtilisateur;
import ca.ulaval.glo2004.domaine.TypeMur;
import ca.ulaval.glo2004.domaine.TypeSensToit;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.Rasterizer;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.Triangle;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMesh;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMeshGroup;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene.Scene;
import ca.ulaval.glo2004.domaine.utils.PanelHelper.MurTriangleMeshGroup;
import ca.ulaval.glo2004.domaine.utils.PanelHelper.OutputType;
import ca.ulaval.glo2004.domaine.utils.PanelHelper;
import ca.ulaval.glo2004.domaine.utils.STLTools;

public class Afficheur {
    // private BufferedImage bufImage = new BufferedImage(500, 500,
    // BufferedImage.TYPE_INT_RGB);
    private Controleur controleur;

    private Dimension dimension;
    Scene scene;
    Rasterizer rasterizer;
    TypeDeVue vueActive = TypeDeVue.Dessus;

    public MurTriangleMeshGroup murFacadeGroup;
    public MurTriangleMeshGroup murArriereGroup;
    public MurTriangleMeshGroup murDroitGroup;
    public MurTriangleMeshGroup murGaucheGroup;

    public OutputType renduVisuel = OutputType.Fini;

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

        boolean sideTruncate = chaletDTO.sensToit == TypeSensToit.Nord || chaletDTO.sensToit == TypeSensToit.Sud;

        List<Accessoire.AccessoireDTO> murFacadeAccessoires = getControleur().getAccessoires(TypeMur.Facade);
        List<Accessoire.AccessoireDTO> murArriereAccessoires = getControleur().getAccessoires(TypeMur.Arriere);
        List<Accessoire.AccessoireDTO> murDroitAccessoires = getControleur().getAccessoires(TypeMur.Droit);
        List<Accessoire.AccessoireDTO> murGaucheAccessoires = getControleur().getAccessoires(TypeMur.Gauche);

        murFacadeGroup = new MurTriangleMeshGroup(chaletDTO, TypeMur.Facade, murFacadeAccessoires, !sideTruncate,
                false);
        murArriereGroup = new MurTriangleMeshGroup(chaletDTO, TypeMur.Arriere, murArriereAccessoires, !sideTruncate,
                false);
        murDroitGroup = new MurTriangleMeshGroup(chaletDTO, TypeMur.Droit, murDroitAccessoires, sideTruncate, false);
        murGaucheGroup = new MurTriangleMeshGroup(chaletDTO, TypeMur.Gauche, murGaucheAccessoires, sideTruncate, false);

        // murFacadeGroup.setIdentifier(Constants._STRING_MUR_FACADE);
        // murArriereGroup.setIdentifier(Constants._STRING_MUR_ARRIERE);
        // murDroitGroup.setIdentifier(Constants._STRING_MUR_DROIT);
        // murGaucheGroup.setIdentifier(Constants._STRING_MUR_GAUCHE);

        // Si true, des trous sont formés dans les murs pour les accessoires
        // Présentement à false puisque les accessoires sont des objets 3D
        // Nécessaire pour les exportations

        murFacadeGroup.setComputeHoles(false);
        murArriereGroup.setComputeHoles(false);
        murDroitGroup.setComputeHoles(false);
        murGaucheGroup.setComputeHoles(false);

        murFacadeGroup.setActiveOuput(this.renduVisuel);
        murArriereGroup.setActiveOuput(this.renduVisuel);
        murGaucheGroup.setActiveOuput(this.renduVisuel);
        murDroitGroup.setActiveOuput(this.renduVisuel);

        this.scene.addMesh(murFacadeGroup);
        this.scene.addMesh(murArriereGroup);
        this.scene.addMesh(murDroitGroup);
        this.scene.addMesh(murGaucheGroup);

        this.scene.getMeshes().addAll(murFacadeGroup.getAccessoiresMeshes());
        this.scene.getMeshes().addAll(murArriereGroup.getAccessoiresMeshes());
        this.scene.getMeshes().addAll(murDroitGroup.getAccessoiresMeshes());
        this.scene.getMeshes().addAll(murGaucheGroup.getAccessoiresMeshes());

        // murFacadeGroup.rebuild();
        // murArriereGroup.rebuild();
        // **La responsabilité du rendu à été transferer vers la classe
        // MurTriangleMeshGroup**

        // PreferencesUtilisateur.PreferencesUtilisateurDTO preferUser =
        // getControleur().getPreferencesUtilisateur();
        // Vector3D direction = scene.getCamera().getDirection();
        // System.out.println(direction);
        //
        // if (preferUser.afficherGrille) {
        // if (direction.equals(TypeDeVue.getDirection(TypeDeVue.Facade)) ||
        // direction.equals(TypeDeVue.getDirection(TypeDeVue.Arriere)) ||
        // direction.equals(TypeDeVue.getDirection(TypeDeVue.Droite))
        // ||direction.equals(TypeDeVue.getDirection(TypeDeVue.Gauche))) {
        // getScene().getConfiguration().setShowGridXZ(true);
        // getScene().getConfiguration().setShowGridYZ(true);
        // getScene().getConfiguration().setShowGridXY(true);
        // }
        // else {
        // getScene().getConfiguration().setShowGridYZ(false);
        // getScene().getConfiguration().setShowGridXY(false);
        // }
        // }
        // else {
        // getScene().getConfiguration().setShowGridXZ(false);
        // getScene().getConfiguration().setShowGridYZ(false);
        // getScene().getConfiguration().setShowGridXY(false);
        // }

        // Material murFacadeMaterial = new Material();
        // Material murArriereMaterial = new Material();
        // Material murDroitMaterial = new Material();
        // Material murGaucheMaterial = new Material();

        // murFacadeMaterial.setColor(java.awt.Color.RED);
        // murArriereMaterial.setColor(java.awt.Color.BLUE);
        // murDroitMaterial.setColor(java.awt.Color.GREEN);
        // murGaucheMaterial.setColor(java.awt.Color.YELLOW);

        // sideTruncate indique que si le toit pointe vers les murs Façade ou Arrière,
        // ce sont les autres murs qu'il faut truncate et vice versa

        // murFacadeGroup = new TriangleMeshGroup(new TriangleMesh[] {
        // TriangleMesh.fromDoubleList(PanelHelper.buildWall(new double[] { 0, 0, 0 },
        // new Dimension((int) chaletDTO.largeur, (int) chaletDTO.hauteur),
        // chaletDTO.epaisseurMur,
        // chaletDTO.margeSupplementaireRetrait, !sideTruncate), murFacadeMaterial),
        // });
        // murArriereGroup = new TriangleMeshGroup(new TriangleMesh[] {
        // TriangleMesh.fromDoubleList(PanelHelper.buildWall(new double[] { 0, 0, 0 },
        // new Dimension((int) chaletDTO.largeur, (int) chaletDTO.hauteur),
        // chaletDTO.epaisseurMur,
        // chaletDTO.margeSupplementaireRetrait, !sideTruncate), murArriereMaterial),
        // });
        // murDroitGroup = new TriangleMeshGroup(new TriangleMesh[] {
        // TriangleMesh.fromDoubleList(PanelHelper.buildWall(new double[] { 0, 0, 0 },
        // new Dimension((int) chaletDTO.longueur, (int) chaletDTO.hauteur),
        // chaletDTO.epaisseurMur,
        // chaletDTO.margeSupplementaireRetrait, sideTruncate), murDroitMaterial),
        // });
        // murGaucheGroup = new TriangleMeshGroup(new TriangleMesh[] {
        // TriangleMesh.fromDoubleList(
        // PanelHelper.buildWall(new double[] { 0, 0, 0 },
        // new Dimension((int) chaletDTO.longueur, (int) chaletDTO.hauteur),
        // chaletDTO.epaisseurMur, chaletDTO.margeSupplementaireRetrait, sideTruncate),
        // murGaucheMaterial),
        // });

        // // get mesh
        // murFacadeGroup.getMesh(0).setHandle(Constants._STRING_MUR_FACADE);
        // murArriereGroup.getMesh(0).setHandle(Constants._STRING_MUR_ARRIERE);
        // murDroitGroup.getMesh(0).setHandle(Constants._STRING_MUR_DROIT);
        // murGaucheGroup.getMesh(0).setHandle(Constants._STRING_MUR_GAUCHE);

        // // uh??
        // murFacadeGroup =
        // murFacadeGroup.translate(murFacadeGroup.getCenter().multiply(-1));
        // murArriereGroup =
        // murArriereGroup.translate(murArriereGroup.getCenter().multiply(-1));
        // murDroitGroup =
        // murDroitGroup.translate(murDroitGroup.getCenter().multiply(-1));
        // murGaucheGroup =
        // murGaucheGroup.translate(murGaucheGroup.getCenter().multiply(-1));

        // // rotate walls all around
        // murArriereGroup = murArriereGroup.rotate(0, Math.PI, 0);
        // murDroitGroup = murDroitGroup.rotate(0, Math.PI / 2, 0);
        // murGaucheGroup = murGaucheGroup.rotate(0, -Math.PI / 2, 0);

        // // shift the walls into position
        // murFacadeGroup = murFacadeGroup.translate(new Vector3D(0, 0,
        // -chaletDTO.longueur / 2));
        // murArriereGroup = murArriereGroup.translate(new Vector3D(0, 0,
        // chaletDTO.longueur / 2));
        // murDroitGroup = murDroitGroup.translate(new Vector3D(-chaletDTO.largeur / 2,
        // 0, 0));
        // murGaucheGroup = murGaucheGroup.translate(new Vector3D(chaletDTO.largeur / 2,
        // 0, 0));

        // // Connecter les murs entre eux
        // // if (sideTruncate) {
        // murDroitGroup = murDroitGroup.translate(new Vector3D(chaletDTO.epaisseurMur /
        // 2, 0, 0));
        // murGaucheGroup = murGaucheGroup.translate(new
        // Vector3D(-chaletDTO.epaisseurMur / 2, 0, 0));
        // // }

        // // else {
        // murFacadeGroup = murFacadeGroup.translate(new Vector3D(0, 0,
        // chaletDTO.epaisseurMur / 2));
        // murArriereGroup = murArriereGroup.translate(new Vector3D(0, 0,
        // -chaletDTO.epaisseurMur / 2));
        // // }

        // // mettre le chalet sur le plancher
        // murFacadeGroup = murFacadeGroup.translate(new Vector3D(0, -chaletDTO.hauteur
        // / 2, 0));
        // murArriereGroup = murArriereGroup.translate(new Vector3D(0,
        // -chaletDTO.hauteur / 2, 0));
        // murDroitGroup = murDroitGroup.translate(new Vector3D(0, -chaletDTO.hauteur /
        // 2, 0));
        // murGaucheGroup = murGaucheGroup.translate(new Vector3D(0, -chaletDTO.hauteur
        // / 2, 0));

        // // little detour through STL land
        // // in order for this to show up properly in e.g. blender, we're just gonna
        // set x=-x, y=z, z=-y; it makes sense ok
        // List<STLTools.Triangle> stlTriangles = new ArrayList<>();
        // for (Triangle tri : murFacadeGroup.getMesh(0).getTriangles()) {
        // double[] normal = tri.getNormal().toArray();
        // double[] vertex1 = tri.getVertice(0).toArray();
        // double[] vertex2 = tri.getVertice(1).toArray();
        // double[] vertex3 = tri.getVertice(2).toArray();

        // stlTriangles
        // .add(new STLTools.Triangle(new float[] { -(float) normal[0], (float)
        // normal[2], -(float) normal[1] },
        // new float[] { -(float) vertex1[0], (float) vertex1[2], -(float) vertex1[1] },
        // new float[] { -(float) vertex2[0], (float) vertex2[2], -(float) vertex2[1] },
        // new float[] { -(float) vertex3[0], (float) vertex3[2], -(float) vertex3[1]
        // }));
        // }

        // for (Triangle tri : murArriereGroup.getMesh(0).getTriangles()) {
        // double[] normal = tri.getNormal().toArray();
        // double[] vertex1 = tri.getVertice(0).toArray();
        // double[] vertex2 = tri.getVertice(1).toArray();
        // double[] vertex3 = tri.getVertice(2).toArray();

        // stlTriangles
        // .add(new STLTools.Triangle(new float[] { -(float) normal[0], (float)
        // normal[2], -(float) normal[1] },
        // new float[] { -(float) vertex1[0], (float) vertex1[2], -(float) vertex1[1] },
        // new float[] { -(float) vertex2[0], (float) vertex2[2], -(float) vertex2[1] },
        // new float[] { -(float) vertex3[0], (float) vertex3[2], -(float) vertex3[1]
        // }));
        // }

        // for (Triangle tri : murDroitGroup.getMesh(0).getTriangles()) {
        // double[] normal = tri.getNormal().toArray();
        // double[] vertex1 = tri.getVertice(0).toArray();
        // double[] vertex2 = tri.getVertice(1).toArray();
        // double[] vertex3 = tri.getVertice(2).toArray();

        // stlTriangles
        // .add(new STLTools.Triangle(new float[] { -(float) normal[0], (float)
        // normal[2], -(float) normal[1] },
        // new float[] { -(float) vertex1[0], (float) vertex1[2], -(float) vertex1[1] },
        // new float[] { -(float) vertex2[0], (float) vertex2[2], -(float) vertex2[1] },
        // new float[] { -(float) vertex3[0], (float) vertex3[2], -(float) vertex3[1]
        // }));
        // }

        // for (Triangle tri : murGaucheGroup.getMesh(0).getTriangles()) {
        // double[] normal = tri.getNormal().toArray();
        // double[] vertex1 = tri.getVertice(0).toArray();
        // double[] vertex2 = tri.getVertice(1).toArray();
        // double[] vertex3 = tri.getVertice(2).toArray();

        // stlTriangles
        // .add(new STLTools.Triangle(new float[] { -(float) normal[0], (float)
        // normal[2], -(float) normal[1] },
        // new float[] { -(float) vertex1[0], (float) vertex1[2], -(float) vertex1[1] },
        // new float[] { -(float) vertex2[0], (float) vertex2[2], -(float) vertex2[1] },
        // new float[] { -(float) vertex3[0], (float) vertex3[2], -(float) vertex3[1]
        // }));
        // }

        // STLTools.writeSTL(stlTriangles, "test.stl");

        // // murFacadeGroup.setVisible(false);
        // // murArriereGroup.setVisible(false);

        // scene.addMesh(murFacadeGroup);
        // scene.addMesh(murArriereGroup);
        // scene.addMesh(murDroitGroup);
        // scene.addMesh(murGaucheGroup);

        // List<Accessoire.AccessoireDTO> accessoires =
        // this.getControleur().getAccessoires();

        // for (Accessoire.AccessoireDTO accessoireDTO : accessoires) {
        // TypeMur typeMur = accessoireDTO.typeMur;

        // TriangleMeshGroup accMesh = null;

        // if (accessoireDTO.accessoireType == TypeAccessoire.Fenetre) {
        // accMesh = PanelHelper.buildWindow(accessoireDTO.dimensions[0] - 2,
        // accessoireDTO.dimensions[1],
        // new Vector3D(0, 0, -2), 2);
        // } else if (accessoireDTO.accessoireType == TypeAccessoire.Porte) {
        // accMesh = PanelHelper.buildDoor(accessoireDTO.dimensions[0] - 4,
        // accessoireDTO.dimensions[1],
        // new Vector3D(0, 0, 0), 4);

        // // System.out.println((accMesh.getBounding()[1].y) + " "
        // // + (chaletDTO.hauteur - accMesh.getHeight() - chaletDTO.margeAccessoire));

        // }

        // if (accMesh == null)
        // throw new Exception("Accessory id is null"); // TODO update exception type

        // switch (typeMur) {
        // case Facade:

        // if (murFacadeGroup.getMesh(accessoireDTO.accessoireId.toString()) != null) {
        // murFacadeGroup.removeMesh(murFacadeGroup.getMesh(accessoireDTO.accessoireId.toString()));
        // }

        // accMesh = accMesh.translate(accMesh.getCenter().multiply(-1));
        // accMesh = accMesh.translate(new Vector3D(0, 0, -chaletDTO.longueur / 2));
        // accMesh = accMesh.translate(new Vector3D(0, 0, -accMesh.getDepth() / 2));
        // accMesh = accMesh.translate(new Vector3D(chaletDTO.largeur / 2 -
        // accessoireDTO.dimensions[0] / 2,
        // -chaletDTO.hauteur / 2 + accessoireDTO.dimensions[1] / 2, 0));

        // accMesh = accMesh.translate(new Vector3D(-accessoireDTO.position[0],
        // accessoireDTO.position[1], 0));

        // break;
        // case Arriere:
        // if (murArriereGroup.getMesh(accessoireDTO.accessoireId.toString()) != null) {
        // murArriereGroup.removeMesh(murArriereGroup.getMesh(accessoireDTO.accessoireId.toString()));
        // }

        // accMesh = accMesh.rotate(0, Math.PI, 0);
        // accMesh = accMesh.translate(accMesh.getCenter().multiply(-1));
        // accMesh = accMesh.translate(new Vector3D(0, 0, accMesh.getDepth() / 2));
        // accMesh = accMesh
        // .translate(new Vector3D(0, 0, chaletDTO.longueur / 2 + chaletDTO.epaisseurMur
        // / 2));
        // accMesh = accMesh.translate(new Vector3D(-chaletDTO.largeur / 2 +
        // accessoireDTO.dimensions[0] / 2,
        // -chaletDTO.hauteur / 2 + accessoireDTO.dimensions[1] / 2,
        // -chaletDTO.epaisseurMur / 2));

        // accMesh = accMesh.translate(new Vector3D(accessoireDTO.position[0],
        // accessoireDTO.position[1], 0));

        // break;
        // case Gauche:
        // if (murGaucheGroup.getMesh(accessoireDTO.accessoireId.toString()) != null) {
        // murGaucheGroup.removeMesh(murGaucheGroup.getMesh(accessoireDTO.accessoireId.toString()));
        // }
        // accMesh = accMesh.rotate(0, -Math.PI / 2, 0);
        // accMesh = accMesh.translate(accMesh.getCenter().multiply(-1));
        // accMesh = accMesh.translate(new Vector3D(accMesh.getWidth() / 2, 0, 0));
        // accMesh = accMesh.translate(new Vector3D(chaletDTO.largeur / 2 -
        // chaletDTO.epaisseurMur / 2, 0, 0));
        // accMesh = accMesh
        // .translate(new Vector3D(chaletDTO.epaisseurMur / 2,
        // -chaletDTO.hauteur / 2 + accessoireDTO.dimensions[1] / 2,
        // chaletDTO.longueur / 2 - accessoireDTO.dimensions[0] / 2));

        // accMesh = accMesh.translate(new Vector3D(0, accessoireDTO.position[1],
        // -accessoireDTO.position[0]));

        // break;
        // case Droit:
        // if (murDroitGroup.getMesh(accessoireDTO.accessoireId.toString()) != null) {
        // murDroitGroup.removeMesh(murDroitGroup.getMesh(accessoireDTO.accessoireId.toString()));
        // }

        // accMesh = accMesh.rotate(0, Math.PI / 2, 0);
        // accMesh = accMesh.translate(accMesh.getCenter().multiply(-1));
        // accMesh = accMesh.translate(new Vector3D(-accMesh.getWidth() / 2, 0, 0));
        // accMesh = accMesh
        // .translate(new Vector3D(-chaletDTO.largeur / 2 + chaletDTO.epaisseurMur / 2,
        // 0, 0));
        // accMesh = accMesh
        // .translate(new Vector3D(-chaletDTO.epaisseurMur / 2,
        // -chaletDTO.hauteur / 2 + accessoireDTO.dimensions[1] / 2,
        // -chaletDTO.longueur / 2 + accessoireDTO.dimensions[0] / 2));

        // accMesh = accMesh.translate(new Vector3D(0, accessoireDTO.position[1],
        // accessoireDTO.position[0]));

        // break;
        // }
        // accMesh = accMesh.translate(new Vector3D(0, -chaletDTO.hauteur / 2, 0));

        // accMesh.setIdentifier(accessoireDTO.accessoireId.toString());
        // accMesh.setValid(accessoireDTO.valide);

        // this.scene.addMesh(accMesh);
        // this.scene.setValid(accMesh.getIdentifier(), accessoireDTO.valide);
        // }

        // // Pour tester l'importation d'objets à partir de fichiers .obj
        // if(getControleur().getPreferencesUtilisateur().afficherPlancher){
        // URI url = App.class.getResource("/objets/floor_single.obj").toURI();
        // System.out.println(url);
        // TriangleMesh mesh = ObjectImporter.importObject(url); // shaep
        // //mesh = mesh.scale(new Vector3D(1, 1, 1));
        // mesh.getMaterial().setColor(new Color(114, 114, 114, 255));
        // mesh.getMaterial().setShininess(0);
        // mesh.getMaterial().setSpecular(0);
        // mesh.getMaterial().setAmbient(2);

        // TriangleMeshGroup meshGroup = new TriangleMeshGroup(new TriangleMesh[] { mesh
        // });
        // meshGroup = meshGroup.scale(new Vector3D(1,1,-1)); // flip the z axis the
        // right way around
        // meshGroup.setSelectable(false);

        // scene.addMesh(meshGroup);
        // }

        // TODO: Ajouter le plancher
    }

    public void exportStlFini(String directoryPath, String nomChalet) {
        boolean facadeInitialComputeHoles = murFacadeGroup.getComputeHoles();
        boolean arriereInitialComputeHoles = murArriereGroup.getComputeHoles();
        boolean gaucheInitialComputeHoles = murGaucheGroup.getComputeHoles();
        boolean droitInitialComputeHoles = murDroitGroup.getComputeHoles();

        murFacadeGroup.setComputeHoles(true);
        murArriereGroup.setComputeHoles(true);
        murGaucheGroup.setComputeHoles(true);
        murDroitGroup.setComputeHoles(true);

        List<STLTools.Triangle> facadeStlTriangles = PanelHelper.convertMeshTrianglesToStlTriangles(murFacadeGroup.getMeshFini().getTriangles());
        List<STLTools.Triangle> arriereStlTriangles = PanelHelper.convertMeshTrianglesToStlTriangles(murArriereGroup.getMeshFini().getTriangles());
        List<STLTools.Triangle> gaucheStlTriangles = PanelHelper.convertMeshTrianglesToStlTriangles(murGaucheGroup.getMeshFini().getTriangles());
        List<STLTools.Triangle> droitStlTriangles = PanelHelper.convertMeshTrianglesToStlTriangles(murDroitGroup.getMeshFini().getTriangles());

        murFacadeGroup.setComputeHoles(facadeInitialComputeHoles);
        murArriereGroup.setComputeHoles(arriereInitialComputeHoles);
        murGaucheGroup.setComputeHoles(gaucheInitialComputeHoles);
        murDroitGroup.setComputeHoles(droitInitialComputeHoles);

        String facadeFileName = String.format("\\%s_Fini_F.stl", nomChalet);
        String arriereFileName = String.format("\\%s_Fini_A.stl", nomChalet);
        String gaucheFileName = String.format("\\%s_Fini_G.stl", nomChalet);
        String droitFileName = String.format("\\%s_Fini_D.stl", nomChalet);

        STLTools.writeSTL(facadeStlTriangles, directoryPath + facadeFileName);
        STLTools.writeSTL(arriereStlTriangles, directoryPath + arriereFileName);
        STLTools.writeSTL(gaucheStlTriangles, directoryPath + gaucheFileName);
        STLTools.writeSTL(droitStlTriangles, directoryPath + droitFileName);
    }

    public void exportStlBrut(String directoryPath, String nomChalet) {
        List<STLTools.Triangle> facadeStlTriangles = PanelHelper.convertMeshTrianglesToStlTriangles(murFacadeGroup.getMeshBrut().getTriangles());
        List<STLTools.Triangle> arriereStlTriangles = PanelHelper.convertMeshTrianglesToStlTriangles(murArriereGroup.getMeshBrut().getTriangles());
        List<STLTools.Triangle> gaucheStlTriangles = PanelHelper.convertMeshTrianglesToStlTriangles(murGaucheGroup.getMeshBrut().getTriangles());
        List<STLTools.Triangle> droitStlTriangles = PanelHelper.convertMeshTrianglesToStlTriangles(murDroitGroup.getMeshBrut().getTriangles());

        String facadeBrutFileName = String.format("\\%s_Brut_F.stl", nomChalet);
        String arriereBrutFileName = String.format("\\%s_Brut_A.stl", nomChalet);
        String gaucheBrutFileName = String.format("\\%s_Brut_G.stl", nomChalet);
        String droitBrutFileName = String.format("\\%s_Brut_D.stl", nomChalet);

        STLTools.writeSTL(facadeStlTriangles, directoryPath + facadeBrutFileName);
        STLTools.writeSTL(arriereStlTriangles, directoryPath + arriereBrutFileName);
        STLTools.writeSTL(gaucheStlTriangles, directoryPath + gaucheBrutFileName);
        STLTools.writeSTL(droitStlTriangles, directoryPath + droitBrutFileName);
    }

    public void exportStlRetraits(String directoryPath, String nomChalet) {
        List<STLTools.Triangle> facadeStlTriangles = new ArrayList<>();
        List<STLTools.Triangle> arriereStlTriangles = new ArrayList<>();
        List<STLTools.Triangle> gaucheStlTriangles = new ArrayList<>();
        List<STLTools.Triangle> droitStlTriangles = new ArrayList<>();

        for (TriangleMesh mesh: murFacadeGroup.getMeshRetraits().getMeshes()) {
            facadeStlTriangles.addAll(PanelHelper.convertMeshTrianglesToStlTriangles(mesh.getTriangles()));
        }
        for (TriangleMesh mesh: murArriereGroup.getMeshRetraits().getMeshes()) {
            arriereStlTriangles.addAll(PanelHelper.convertMeshTrianglesToStlTriangles(mesh.getTriangles()));
        }
        for (TriangleMesh mesh: murGaucheGroup.getMeshRetraits().getMeshes()) {
            gaucheStlTriangles.addAll(PanelHelper.convertMeshTrianglesToStlTriangles(mesh.getTriangles()));
        }
        for (TriangleMesh mesh: murDroitGroup.getMeshRetraits().getMeshes()) {
            droitStlTriangles.addAll(PanelHelper.convertMeshTrianglesToStlTriangles(mesh.getTriangles()));
        }

        String facadeRetraitsFileName = String.format("\\%s_Retrait_F.stl", nomChalet);
        String arriereRetraitsFileName = String.format("\\%s_Retrait_A.stl", nomChalet);
        String gaucheRetraitsFileName = String.format("\\%s_Retrait_G.stl", nomChalet);
        String droitRetraitsFileName = String.format("\\%s_Retrait_D.stl", nomChalet);

        STLTools.writeSTL(facadeStlTriangles, directoryPath + facadeRetraitsFileName);
        STLTools.writeSTL(arriereStlTriangles, directoryPath + arriereRetraitsFileName);
        STLTools.writeSTL(gaucheStlTriangles, directoryPath + gaucheRetraitsFileName);
        STLTools.writeSTL(droitStlTriangles, directoryPath + droitRetraitsFileName);
    }

    public void exportSTL(String directoryPath, PanelHelper.OutputType exportType) {
        Chalet.ChaletDTO chaletDTO = this.getControleur().getChalet();

        switch (exportType) {
            case Fini:
                exportStlFini(directoryPath, chaletDTO.nom);
                break;
            case Brut:
                exportStlBrut(directoryPath, chaletDTO.nom);
                break;
            case Retraits:
                exportStlRetraits(directoryPath, chaletDTO.nom);
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + exportType);
        }
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
