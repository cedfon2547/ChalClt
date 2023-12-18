package ca.ulaval.glo2004.domaine.afficheur;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ca.ulaval.glo2004.App;
import ca.ulaval.glo2004.domaine.*;
import ca.ulaval.glo2004.domaine.ControleurEventSupport.UserPreferencesEvent;
import ca.ulaval.glo2004.domaine.ControleurEventSupport.UserPreferencesEventListener;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.Rasterizer;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMesh;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMeshGroup;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene.Camera;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene.Scene;
import ca.ulaval.glo2004.domaine.utils.PanelHelper.MurTriangleMeshGroup;
import ca.ulaval.glo2004.domaine.utils.PanelHelper.OutputType;
import ca.ulaval.glo2004.domaine.utils.PanelHelper.RectPlane2D;
import ca.ulaval.glo2004.domaine.utils.ImperialDimension;
import ca.ulaval.glo2004.domaine.utils.ObjectImporter;
import ca.ulaval.glo2004.domaine.utils.PanelHelper;
import ca.ulaval.glo2004.domaine.utils.STLTools;
import java.awt.Color;
import java.util.Objects;


public class Afficheur {
    private AfficheurEventSupport eventSupport = new AfficheurEventSupport();

    private Controleur controleur;

    Scene scene;
    Rasterizer rasterizer;
    TypeDeVue vueActive = TypeDeVue.Dessus;

    public MurTriangleMeshGroup murFacadeGroup;
    public MurTriangleMeshGroup murArriereGroup;
    public MurTriangleMeshGroup murDroitGroup;
    public MurTriangleMeshGroup murGaucheGroup;

    public OutputType renduVisuel = OutputType.Fini;
    private JPanel drawingPanel;

    public Afficheur(Controleur controleur, JPanel drawingPanel) {
        this.drawingPanel = drawingPanel;
        this.controleur = controleur;
        this.scene = new Scene();
        this.rasterizer = new Rasterizer(this.scene);
        this.scene.getLight().setPosition(new Vector3D(0, 0, 1000));
        this.scene.getCamera().setDirection(TypeDeVue.vueDessus());
        this.scene.getCamera().setScale(2);

        this.initDrawingPanel();
        initialize();
    }

    private void initDrawingPanel() {
        this.drawingPanel.addMouseWheelListener(this.mouseWheelListener());
        this.drawingPanel.addMouseListener(this.mouseListener());
        this.drawingPanel.addMouseMotionListener(this.mouseMotionListener());

        this.drawingPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            Dimension oldSize = null;

            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                if (oldSize != null) {
                    int diffX = drawingPanel.getWidth() - oldSize.width;
                    int diffY = drawingPanel.getHeight() - oldSize.height;

                    // Positionning the camera considering the new frame size
                    Camera camera = getScene().getCamera();
                    camera.setPosition(new Vector3D(camera.getPosition().x + diffX / 2,
                            camera.getPosition().y + diffY / 2, camera.getPosition().z));
                }

                oldSize = drawingPanel.getSize();

                rasterizer.resizeImage(drawingPanel.getSize());
            }

            @Override
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                drawingPanel.repaint();
            }

            @Override
            public void componentShown(java.awt.event.ComponentEvent evt) {
                drawingPanel.repaint();
            }

            @Override
            public void componentHidden(java.awt.event.ComponentEvent evt) {
            }
        });
    }

    private void initialize() {
        this.controleur.addUserPreferencesEventListener(new UserPreferencesEventListener() {
            @Override
            public void change(UserPreferencesEvent event) {
                PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO = event
                        .getPreferencesUtilisateurDTO();
                toggleShowGrid(preferencesUtilisateurDTO.afficherGrille);
                scene.getConfiguration().setGridStep(preferencesUtilisateurDTO.gridSpacing);
                drawingPanel.repaint();
            }
        });

        this.scene.getCamera().addPropertyChangeListener("direction", new PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                eventSupport.dispatchCameraDirectionChanged(new AfficheurEventSupport.CameraEvent(
                        scene.getCamera().getPosition(), scene.getCamera().getDirection()));

            }
        });

        this.scene.getCamera().addPropertyChangeListener("position", new PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                eventSupport.dispatchCameraPositionChanged(new AfficheurEventSupport.CameraEvent(
                        scene.getCamera().getPosition(), scene.getCamera().getDirection()));
            }
        });
    }

    public AfficheurEventSupport getEventSupport() {
        return eventSupport;
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

    public TypeDeVue getVueActive() {
        return vueActive;
    }

    public void setControleur(Controleur controleur) {
        this.controleur = controleur;
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

    public void switchOutputType(OutputType outputType) {
        this.murFacadeGroup.setActiveOuput(outputType);
        this.murArriereGroup.setActiveOuput(outputType);
        this.murDroitGroup.setActiveOuput(outputType);
        this.murGaucheGroup.setActiveOuput(outputType);

        this.renduVisuel = outputType;
        drawingPanel.repaint();
    }

    public void initializeView() {
        // System.out.println("Initialize View");

        this.scene.clearMeshes();

        Chalet.ChaletDTO chaletDTO = this.getControleur().getChalet();
        PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO = this.getControleur()
                .getPreferencesUtilisateur();
        getScene().getConfiguration().setGridStep(preferencesUtilisateurDTO.gridSpacing);
        toggleShowGrid(preferencesUtilisateurDTO.afficherGrille);

        // getScene().clearMeshes();

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

        getScene().addMesh(murFacadeGroup);
        getScene().addMesh(murArriereGroup);
        getScene().addMesh(murDroitGroup);
        getScene().addMesh(murGaucheGroup);

        getScene().getMeshes().addAll(murFacadeGroup.getAccessoiresMeshes());
        getScene().getMeshes().addAll(murArriereGroup.getAccessoiresMeshes());
        getScene().getMeshes().addAll(murDroitGroup.getAccessoiresMeshes());
        getScene().getMeshes().addAll(murGaucheGroup.getAccessoiresMeshes());

        TriangleMeshGroup pignonGaucheToit = PanelHelper.buildPignonGauche(chaletDTO.longueur, chaletDTO.epaisseurMur,
                chaletDTO.angleToit, new Vector3D(0, 0, 0));
        TriangleMeshGroup pignonDroitToit = PanelHelper.buildPignonDroite(chaletDTO.longueur, chaletDTO.epaisseurMur,
                chaletDTO.angleToit, new Vector3D(0, 0, 0));
        TriangleMeshGroup panneauToit = PanelHelper.buildPanneauToit2(chaletDTO.largeur, chaletDTO.longueur,
                chaletDTO.epaisseurMur, chaletDTO.angleToit, chaletDTO.margeSupplementaireRetrait,
                new Vector3D(0, 0, 0));
        TriangleMeshGroup rallongeVerticaleToit = PanelHelper.buildRallongeVertical(chaletDTO.largeur,
                chaletDTO.longueur, chaletDTO.epaisseurMur, chaletDTO.angleToit, chaletDTO.margeSupplementaireRetrait,
                new Vector3D(0, 0, 0));

        pignonGaucheToit = pignonGaucheToit.rotateY(-Math.PI / 2);
        pignonDroitToit = pignonDroitToit.rotateY(-Math.PI / 2);

        pignonGaucheToit = pignonGaucheToit.translate(pignonGaucheToit.getCenter().multiply(-1));
        pignonDroitToit = pignonDroitToit.translate(pignonDroitToit.getCenter().multiply(-1));

        pignonGaucheToit = pignonGaucheToit.translate(new Vector3D(chaletDTO.largeur / 2 - chaletDTO.epaisseurMur / 2,
                -chaletDTO.hauteur - pignonGaucheToit.getHeight() / 2, 0));
        pignonDroitToit = pignonDroitToit.translate(new Vector3D(-chaletDTO.largeur / 2 + chaletDTO.epaisseurMur / 2,
                -chaletDTO.hauteur - pignonDroitToit.getHeight() / 2, 0));

        panneauToit = panneauToit.translate(new Vector3D(0,
                -chaletDTO.longueur * Math.tan(Math.toRadians(chaletDTO.angleToit)) - chaletDTO.hauteur, 0));
        panneauToit = panneauToit.translate(new Vector3D(-chaletDTO.largeur / 2, 0, -chaletDTO.longueur / 2));

        rallongeVerticaleToit = rallongeVerticaleToit.translate(new Vector3D(-chaletDTO.largeur / 2,
                -chaletDTO.longueur * Math.tan(Math.toRadians(chaletDTO.angleToit)) - chaletDTO.hauteur,
                -chaletDTO.longueur / 2));
        rallongeVerticaleToit = rallongeVerticaleToit.translate(
                new Vector3D(0, chaletDTO.epaisseurMur / 2 * Math.cos(Math.toRadians(chaletDTO.angleToit)), 0));

        panneauToit.getMesh(0).getMaterial().setColor(Color.LIGHT_GRAY);
        rallongeVerticaleToit.getMesh(0).getMaterial().setColor(Color.LIGHT_GRAY);

        panneauToit.setDraggable(false);
        rallongeVerticaleToit.setDraggable(false);
        pignonGaucheToit.setDraggable(false);
        pignonDroitToit.setDraggable(false);

        getScene().addMesh(panneauToit);
        getScene().addMesh(rallongeVerticaleToit);
        getScene().addMesh(pignonGaucheToit);
        getScene().addMesh(pignonDroitToit);

        // // Create mesh representing a rule to measure the chalet
        // double ruleWidth = 200;
        // int stepNb = 20;

        // TriangleMeshGroup ruleGroup = new TriangleMeshGroup(new ArrayList<>());
        // TriangleMesh mainRect = new RectCuboid(new Vector3D(0, 0, 100), new Vector3D(ruleWidth, 2, 12));

        // for (int i = 0; i <= stepNb; i++) {
        //     TriangleMesh step = new RectCuboid(new Vector3D(i * ruleWidth / stepNb, -2, 100), new Vector3D(2, 2, 12));
        //     step.getMaterial().setColor(Color.BLACK);
        //     // step = step.translate(new Vector3D(-i * ruleWidth / stepNb, 0, 0));
        //     ruleGroup.addMesh(step);
        // }

        // ruleGroup.addMesh(mainRect);
        // ruleGroup = ruleGroup.translate(new Vector3D(-ruleWidth / 2, 0, 0));

        // getScene().addMesh(ruleGroup);

        // // Pour tester l'importation d'objets à partir de fichiers .obj
        if (getControleur().getPreferencesUtilisateur().afficherPlancher) {
            try {
                URI url = App.class.getResource("/objets/floor_single.obj").toURI();
                TriangleMesh mesh = ObjectImporter.importObject(url); // shaep
                // mesh = mesh.scale(new Vector3D(1, 1, 1));
                mesh.getMaterial().setColor(new Color(114, 114, 114, 255));
                // mesh.getMaterial().setShininess(0);
                // mesh.getMaterial().setSpecular(0);
                mesh.getMaterial().setAmbient(0.5);

                TriangleMeshGroup meshGroup = new TriangleMeshGroup(new TriangleMesh[] { mesh });
                meshGroup = meshGroup.scale(new Vector3D(1, 1, -1)); // flip the z axis the right way around
                meshGroup.setSelectable(false);

                meshGroup.setDraggable(false);
                getScene().addMesh(meshGroup);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void rechargerAffichage() {
        if (murFacadeGroup == null || murArriereGroup == null || murDroitGroup == null || murGaucheGroup == null) {
            initializeView();
            drawingPanel.repaint();
            return;
        }

        // System.out.println("Reloading View");

        List<Accessoire.AccessoireDTO> murFacadeAccessoires = getControleur().getAccessoires(TypeMur.Facade);
        List<Accessoire.AccessoireDTO> murArriereAccessoires = getControleur().getAccessoires(TypeMur.Arriere);
        List<Accessoire.AccessoireDTO> murDroitAccessoires = getControleur().getAccessoires(TypeMur.Droit);
        List<Accessoire.AccessoireDTO> murGaucheAccessoires = getControleur().getAccessoires(TypeMur.Gauche);

        murFacadeGroup.updateAccessoires(murFacadeAccessoires);
        murArriereGroup.updateAccessoires(murArriereAccessoires);
        murDroitGroup.updateAccessoires(murDroitAccessoires);
        murGaucheGroup.updateAccessoires(murGaucheAccessoires);

        getScene().clearMeshes();

        getScene().addMesh(murFacadeGroup);
        getScene().addMesh(murArriereGroup);
        getScene().addMesh(murDroitGroup);
        getScene().addMesh(murGaucheGroup);

        getScene().getMeshes().addAll(murFacadeGroup.getAccessoiresMeshes());
        getScene().getMeshes().addAll(murArriereGroup.getAccessoiresMeshes());
        getScene().getMeshes().addAll(murDroitGroup.getAccessoiresMeshes());
        getScene().getMeshes().addAll(murGaucheGroup.getAccessoiresMeshes());

        Chalet.ChaletDTO chaletDTO = this.getControleur().getChalet();
        
        murFacadeGroup.update(chaletDTO);
        murArriereGroup.update(chaletDTO);
        murDroitGroup.update(chaletDTO);
        murGaucheGroup.update(chaletDTO);

        // a bit jank but i'll keep it I guess
        if(this.vueActive!=TypeDeVue.Dessus && !this.scene.getCamera().getDirection().equals(TypeDeVue.vueDessus())) {
            TriangleMeshGroup pignonGaucheToit = PanelHelper.buildPignonGauche(chaletDTO.longueur, chaletDTO.epaisseurMur,
                    chaletDTO.angleToit, new Vector3D(0, 0, 0));
            TriangleMeshGroup pignonDroitToit = PanelHelper.buildPignonDroite(chaletDTO.longueur, chaletDTO.epaisseurMur,
                    chaletDTO.angleToit, new Vector3D(0, 0, 0));
            TriangleMeshGroup panneauToit = PanelHelper.buildPanneauToit2(chaletDTO.largeur, chaletDTO.longueur,
                    chaletDTO.epaisseurMur, chaletDTO.angleToit, chaletDTO.margeSupplementaireRetrait,
                    new Vector3D(0, 0, 0));
            TriangleMeshGroup rallongeVerticaleToit = PanelHelper.buildRallongeVertical(chaletDTO.largeur,
                    chaletDTO.longueur, chaletDTO.epaisseurMur, chaletDTO.angleToit, chaletDTO.margeSupplementaireRetrait,
                    new Vector3D(0, 0, 0));

            pignonGaucheToit = pignonGaucheToit.rotateY(-Math.PI / 2);
            pignonDroitToit = pignonDroitToit.rotateY(-Math.PI / 2);

            pignonGaucheToit = pignonGaucheToit.translate(pignonGaucheToit.getCenter().multiply(-1));
            pignonDroitToit = pignonDroitToit.translate(pignonDroitToit.getCenter().multiply(-1));

            pignonGaucheToit = pignonGaucheToit.translate(new Vector3D(chaletDTO.largeur / 2 - chaletDTO.epaisseurMur / 2,
                    -chaletDTO.hauteur - pignonGaucheToit.getHeight() / 2, 0));
            pignonDroitToit = pignonDroitToit.translate(new Vector3D(-chaletDTO.largeur / 2 + chaletDTO.epaisseurMur / 2,
                    -chaletDTO.hauteur - pignonDroitToit.getHeight() / 2, 0));

            panneauToit = panneauToit.translate(new Vector3D(0,
                    -chaletDTO.longueur * Math.tan(Math.toRadians(chaletDTO.angleToit)) - chaletDTO.hauteur, 0));
            panneauToit = panneauToit.translate(new Vector3D(-chaletDTO.largeur / 2, 0, -chaletDTO.longueur / 2));

            rallongeVerticaleToit = rallongeVerticaleToit.translate(new Vector3D(-chaletDTO.largeur / 2,
                    -chaletDTO.longueur * Math.tan(Math.toRadians(chaletDTO.angleToit)) - chaletDTO.hauteur,
                    -chaletDTO.longueur / 2));
            rallongeVerticaleToit = rallongeVerticaleToit.translate(
                    new Vector3D(0, chaletDTO.epaisseurMur / 2 * Math.cos(Math.toRadians(chaletDTO.angleToit)), 0));

            panneauToit.getMesh(0).getMaterial().setColor(Color.LIGHT_GRAY);
            rallongeVerticaleToit.getMesh(0).getMaterial().setColor(Color.LIGHT_GRAY);

            panneauToit.setDraggable(false);
            rallongeVerticaleToit.setDraggable(false);
            pignonGaucheToit.setDraggable(false);
            pignonDroitToit.setDraggable(false);

            getScene().addMesh(panneauToit);
            getScene().addMesh(rallongeVerticaleToit);
            getScene().addMesh(pignonGaucheToit);
            getScene().addMesh(pignonDroitToit);
        }

        PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO = this.controleur
                .getPreferencesUtilisateur();
        getScene().getConfiguration().setGridStep(preferencesUtilisateurDTO.gridSpacing);
        toggleShowGrid(preferencesUtilisateurDTO.afficherGrille);

        updateViewGrid();
        drawingPanel.repaint();
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

        List<STLTools.Triangle> facadeStlTriangles = PanelHelper
                .convertMeshTrianglesToStlTriangles(murFacadeGroup.getMeshFini().getTriangles());
        List<STLTools.Triangle> arriereStlTriangles = PanelHelper
                .convertMeshTrianglesToStlTriangles(murArriereGroup.getMeshFini().getTriangles());
        List<STLTools.Triangle> gaucheStlTriangles = PanelHelper
                .convertMeshTrianglesToStlTriangles(murGaucheGroup.getMeshFini().getTriangles());
        List<STLTools.Triangle> droitStlTriangles = PanelHelper
                .convertMeshTrianglesToStlTriangles(murDroitGroup.getMeshFini().getTriangles());

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

    public static class RetraitTest {
        Accessoire.AccessoireDTO accessoireDTO;
        TriangleMesh mesh;

        public RetraitTest(Accessoire.AccessoireDTO accessoireDTO, TriangleMesh mesh) {
            this.accessoireDTO = accessoireDTO;
            this.mesh = mesh;
        }
    };

    public void exportStlBrut(String directoryPath, String nomChalet) {
        List<STLTools.Triangle> facadeStlTriangles = PanelHelper
                .convertMeshTrianglesToStlTriangles(murFacadeGroup.getMeshBrut().getTriangles());
        List<STLTools.Triangle> arriereStlTriangles = PanelHelper
                .convertMeshTrianglesToStlTriangles(murArriereGroup.getMeshBrut().getTriangles());
        List<STLTools.Triangle> gaucheStlTriangles = PanelHelper
                .convertMeshTrianglesToStlTriangles(murGaucheGroup.getMeshBrut().getTriangles());
        List<STLTools.Triangle> droitStlTriangles = PanelHelper
                .convertMeshTrianglesToStlTriangles(murDroitGroup.getMeshBrut().getTriangles());

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
        List<Accessoire.AccessoireDTO> retraitDTOs = new ArrayList<>();
        List<TriangleMesh> retraitMesh = new ArrayList<>();

        retraitDTOs.addAll(murFacadeGroup.getAccessoireDTOs());
        retraitDTOs.addAll(murArriereGroup.getAccessoireDTOs());
        retraitDTOs.addAll(murDroitGroup.getAccessoireDTOs());
        retraitDTOs.addAll(murGaucheGroup.getAccessoireDTOs());

        retraitMesh.addAll(murFacadeGroup.getMeshRetraits().getMeshes());
        retraitMesh.addAll(murArriereGroup.getMeshRetraits().getMeshes());
        retraitMesh.addAll(murDroitGroup.getMeshRetraits().getMeshes());
        retraitMesh.addAll(murGaucheGroup.getMeshRetraits().getMeshes());

        List<RetraitTest> retraits = retraitDTOs.stream().map((acc) -> {
            TriangleMesh mesh = retraitMesh.stream()
                    .filter((retrait) -> Objects.equals(retrait.ID, acc.accessoireId.toString())).findFirst()
                    .orElse(null);
            if (mesh != null) {
                return new RetraitTest(acc, mesh);
            } else {
                return null;
            }
        }).filter((value) -> value != null).collect(java.util.stream.Collectors.toList());

        for (RetraitTest retraitTest : retraits) {
            String facadeRetraitsFileName = String.format("\\%s_Retrait_%s_%s.stl", nomChalet,
                    retraitTest.accessoireDTO.typeMur, retraitTest.accessoireDTO.accessoireNom);
            List<STLTools.Triangle> stltriangle = PanelHelper
                    .convertMeshTrianglesToStlTriangles(retraitTest.mesh.getTriangles());
            STLTools.writeSTL(stltriangle, directoryPath + facadeRetraitsFileName);
        }

        TriangleMesh rainure1_Facade = murFacadeGroup.getMeshRetraits().getMesh("rainure1");
        String facadeRetraitsFileFacade1 = String.format("\\%s_Retrait_F_rainure1.stl", nomChalet);
        List<STLTools.Triangle> stl_rainure1_Facade = PanelHelper
                .convertMeshTrianglesToStlTriangles(rainure1_Facade.getTriangles());
        STLTools.writeSTL(stl_rainure1_Facade, directoryPath + facadeRetraitsFileFacade1);

        TriangleMesh rainure2_Facade = murFacadeGroup.getMeshRetraits().getMesh("rainure2");
        String facadeRetraitsFileFacade2 = String.format("\\%s_Retrait_F_rainure2.stl", nomChalet);
        List<STLTools.Triangle> stl_rainure2_Facade = PanelHelper
                .convertMeshTrianglesToStlTriangles(rainure2_Facade.getTriangles());
        STLTools.writeSTL(stl_rainure2_Facade, directoryPath + facadeRetraitsFileFacade2);

        TriangleMesh rainure1_Arriere = murArriereGroup.getMeshRetraits().getMesh("rainure1");
        String facadeRetraitsFileArriere1 = String.format("\\%s_Retrait_A_rainure1.stl", nomChalet);
        List<STLTools.Triangle> stl_rainure1_Arriere = PanelHelper
                .convertMeshTrianglesToStlTriangles(rainure1_Arriere.getTriangles());
        STLTools.writeSTL(stl_rainure1_Arriere, directoryPath + facadeRetraitsFileArriere1);

        TriangleMesh rainure2_Arriere = murArriereGroup.getMeshRetraits().getMesh("rainure2");
        String facadeRetraitsFileArriere2 = String.format("\\%s_Retrait_A_rainure2.stl", nomChalet);
        List<STLTools.Triangle> stl_rainure2_Arriere = PanelHelper
                .convertMeshTrianglesToStlTriangles(rainure2_Arriere.getTriangles());
        STLTools.writeSTL(stl_rainure2_Arriere, directoryPath + facadeRetraitsFileArriere2);

        TriangleMesh rainure1_Gauche = murGaucheGroup.getMeshRetraits().getMesh("rainure1");
        String facadeRetraitsFileGauche1 = String.format("\\%s_Retrait_G_rainure1.stl", nomChalet);
        List<STLTools.Triangle> stl_rainure1_Gauche = PanelHelper
                .convertMeshTrianglesToStlTriangles(rainure1_Gauche.getTriangles());
        STLTools.writeSTL(stl_rainure1_Gauche, directoryPath + facadeRetraitsFileGauche1);

        TriangleMesh rainure2_Gauche = murGaucheGroup.getMeshRetraits().getMesh("rainure2");
        String facadeRetraitsFileGauche2 = String.format("\\%s_Retrait_G_rainure2.stl", nomChalet);
        List<STLTools.Triangle> stl_rainure2_Gauche = PanelHelper
                .convertMeshTrianglesToStlTriangles(rainure2_Gauche.getTriangles());
        STLTools.writeSTL(stl_rainure2_Gauche, directoryPath + facadeRetraitsFileGauche2);

        TriangleMesh rainure1_Droit = murDroitGroup.getMeshRetraits().getMesh("rainure1");
        String facadeRetraitsFileDroit1 = String.format("\\%s_Retrait_D_rainure1.stl", nomChalet);
        List<STLTools.Triangle> stl_rainure1_Droit = PanelHelper
                .convertMeshTrianglesToStlTriangles(rainure1_Droit.getTriangles());
        STLTools.writeSTL(stl_rainure1_Droit, directoryPath + facadeRetraitsFileDroit1);

        TriangleMesh rainure2_Droit = murDroitGroup.getMeshRetraits().getMesh("rainure2");
        String facadeRetraitsFileDroit2 = String.format("\\%s_Retrait_D_rainure2.stl", nomChalet);
        List<STLTools.Triangle> stl_rainure2_Droit = PanelHelper
                .convertMeshTrianglesToStlTriangles(rainure2_Droit.getTriangles());
        STLTools.writeSTL(stl_rainure2_Droit, directoryPath + facadeRetraitsFileDroit2);

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
        this.rasterizer.draw(dimension);
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

        this.getEventSupport().dispatchViewChanged(new AfficheurEventSupport.ViewChangedEvent(this.vueActive));
        updateViewGrid();
        drawingPanel.repaint();
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

    private MouseWheelListener mouseWheelListener() {
        return new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                if (evt.getPreciseWheelRotation() < 0) {
                    scene.getCamera().zoomInDirection(evt.getPoint(), ((JPanel) evt.getSource()).getSize(),
                            evt.isShiftDown());
                    // pcs.firePropertyChange(AfficheurEvent.ZoomIn.toString(), null, null);
                    eventSupport.dispatchZoomIn();
                } else {
                    scene.getCamera().zoomOutDirection(evt.getPoint(), ((JPanel) evt.getSource()).getSize(),
                            evt.isShiftDown());
                    // pcs.firePropertyChange(AfficheurEvent.ZoomOut.toString(), null, null);
                    eventSupport.dispatchZoomOut();
                }

                drawingPanel.repaint();
            }
        };
    }

    // this does absolutely nothing btw
    private MouseListener mouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // drawingPanel.grabFocus();

                // TriangleMesh clickedMesh = getRasterizer().getMeshFromPoint(evt.getPoint());

                // // if (clickedMesh == null) {
                // // deselectAllMeshed();
                // // eventSupport.dispatchSelectionChanged(new
                // // AfficheurEventSupport.MeshSelectionEvent(getSelection()));
                // // drawingPanel.repaint();
                // // return;
                // // }

                // if (clickedMesh != null && evt.getClickCount() == 2) {

                // // pcs.firePropertyChange(AfficheurEvent.MeshDoubleClicked.toString(), null,
                // // clickedMesh);

                // if (clickedMesh instanceof PanelHelper.MurTriangleMeshGroup) {
                // // System.out.println("Double clicked on a wall");
                // switch (((PanelHelper.MurTriangleMeshGroup) clickedMesh).getTypeMur()) {
                // case Facade:
                // changerVue(Afficheur.TypeDeVue.Facade);
                // break;
                // case Arriere:
                // changerVue(Afficheur.TypeDeVue.Arriere);
                // break;
                // case Droit:
                // changerVue(Afficheur.TypeDeVue.Droite);
                // break;
                // case Gauche:
                // changerVue(Afficheur.TypeDeVue.Gauche);
                // break;
                // default:
                // // nop, fall through
                // }
                // }

                // // if (clickedMesh.getSelectable()) {
                // // clickedMesh.setSelected(true);
                // // }

                // updateViewGrid();
                // // drawingPanel.repaint();
                // // eventSupport.dispatchMeshClicked(new
                // // AfficheurEventSupport.MeshMouseEvent(evt, clickedMesh));
                // eventSupport.dispatchViewChanged(new
                // AfficheurEventSupport.ViewChangedEvent(getVueActive()));
                // return;
                // } else if (clickedMesh != null && clickedMesh.getSelectable()) {
                // // pcs.firePropertyChange(AfficheurEvent.SelectionChanged.toString(), null,
                // // clickedMesh);
                // // pcs.firePropertyChange(AfficheurEvent.MeshClicked.toString(), null,
                // // clickedMesh);
                // // if (!evt.isControlDown()) {
                // // deselectAllMeshed();
                // // }

                // // clickedMesh.setSelected(!clickedMesh.getSelected());
                // // eventSupport.dispatchMeshClicked(new
                // // AfficheurEventSupport.MeshMouseEvent(evt, clickedMesh));
                // // eventSupport.dispatchSelectionChanged(new
                // // AfficheurEventSupport.MeshSelectionEvent(getSelection()));
                // }

                // drawingPanel.repaint();
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                // System.out.println("Normal: Mouse pressed");
                // drawingPanel.grabFocus();
                // TriangleMesh clickedMesh = getRasterizer().getMeshFromPoint(evt.getPoint());
                // if (clickedMesh != null) {
                // // System.out.println("Mouse pressed on mesh: " + clickedMesh.ID);

                // if (evt.isControlDown()) {
                // clickedMesh.setSelected(!clickedMesh.getSelected());
                // } else {
                // deselectAllMeshes();
                // clickedMesh.setSelected(true);
                // }
                // } else {
                // deselectAllMeshes();
                // }

                // eventSupport.dispatchSelectionChanged(new
                // AfficheurEventSupport.MeshSelectionEvent(getSelection()));
                // drawingPanel.repaint();
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
            }
        };
    }

    private MouseMotionListener mouseMotionListener() {
        return new MouseMotionListener() {
            boolean initialized = false;
            Point2D initialPoint = null;

            boolean isDragging = false;

            Vector3D initialDragCamPosition = null;
            Vector3D initialDragCamDirection = null;

            // TriangleMesh lastMouseEnteredMesh = null;
            // TriangleMesh lastDraggedMesh = null;
            TriangleMesh focusedMesh = null;
            Vector3D initialFocusedMeshPosition = null;

            boolean isShiftDown = false;

            protected void reset() {
                initialPoint = null;
                isDragging = false;
                initialDragCamPosition = null;
                initialDragCamDirection = null;
                focusedMesh = null;
                initialFocusedMeshPosition = null;
                // lastMouseEnteredMesh = null;
                // lastDraggedMesh = null;
                isShiftDown = false;
            }

            protected void handleMousePressed(java.awt.event.MouseEvent evt) {
                if (initialPoint != null || isDragging) {
                    reset();
                }

                initialPoint = evt.getPoint();
                isShiftDown = evt.isShiftDown();
                initialDragCamDirection = scene.getCamera().getDirection();
                initialDragCamPosition = scene.getCamera().getPosition();

                TriangleMesh clickedMesh = getRasterizer().getMeshFromPoint(evt.getPoint());
                if (clickedMesh != null) {
                    focusedMesh = clickedMesh;

                    if (focusedMesh.getSelectable()) {
                        if (evt.isShiftDown()) {
                            focusedMesh.setSelected(!focusedMesh.getSelected());
                            getEventSupport().dispatchSelectionChanged(new AfficheurEventSupport.MeshSelectionEvent(
                                    getSelection()));
                        } else {
                            scene.clearAllSelection();
                            focusedMesh.setSelected(true);
                            getEventSupport().dispatchSelectionChanged(new AfficheurEventSupport.MeshSelectionEvent(
                                    getSelection()));
                        }
                    }

                    initialFocusedMeshPosition = focusedMesh.getPosition();

                    getEventSupport().dispatchMeshClicked(new AfficheurEventSupport.MeshMouseEvent(evt, clickedMesh));
                } else {
                    scene.clearAllSelection();
                }

                drawingPanel.repaint();
            }

            protected void handleInitialization(java.awt.event.MouseEvent evt) {
                if (!initialized) {
                    drawingPanel.addMouseListener(mouseListener);
                    initialized = true;

                    handleMousePressed(evt);
                }
            }

            MouseListener mouseListener = new MouseListener() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {

                }

                @Override
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    // System.out.println("Motion: Mouse pressed");
                    handleMousePressed(evt);
                }

                @Override
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    if (focusedMesh != null && isDragging) {
                        focusedMesh.setIsDragged(false);

                        if (focusedMesh.getSelectable()) {
                            focusedMesh.setSelected(true);
                        }

                        Vector3D diff = new Vector3D(evt.getX(), evt.getY(), 0)
                                .sub(new Vector3D(initialPoint.getX(), initialPoint.getY(), 0))
                                .multiply(scene.getCamera().getInverseRotationTransformation());

                        getEventSupport().dispatchMeshDragEnd(new AfficheurEventSupport.MeshMouseMotionEvent(evt,
                                focusedMesh, focusedMesh.getPosition(), diff));
                    }

                    reset();
                }

                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {

                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {

                }
            };

            @Override
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                if (!initialized) {
                    handleInitialization(evt);
                }

                if (focusedMesh != null && focusedMesh.getDraggable()) {
                    if (!isDragging) {
                        isDragging = true;
                        getScene().clearAllSelection();
                        focusedMesh.setIsDragged(true);
                        getEventSupport().dispatchMeshDragStart(new AfficheurEventSupport.MeshMouseMotionEvent(evt,
                                focusedMesh, focusedMesh.getPosition()));
                    }

                    Vector3D diff = new Vector3D(evt.getX(), evt.getY(), 0)
                            .sub(new Vector3D(initialPoint.getX(), initialPoint.getY(), 0))
                            .multiply(scene.getCamera().getInverseRotationTransformation());

                    if (!focusedMesh.getDraggableX()) {
                        diff.x = 0;
                    }

                    if (!focusedMesh.getDraggableY()) {
                        diff.y = 0;
                    }

                    if (!focusedMesh.getDraggableZ()) {
                        diff.z = 0;
                    }

                    Vector3D position = initialFocusedMeshPosition.add(diff);

                    focusedMesh.setPosition(position);

                    getEventSupport().dispatchMeshDragged(new AfficheurEventSupport.MeshMouseMotionEvent(evt,
                            focusedMesh, initialFocusedMeshPosition, diff));

                    drawingPanel.repaint();
                    return;
                }

                if (initialPoint != null && initialDragCamDirection != null
                        && initialDragCamPosition != null) {
                    if (evt.isShiftDown() != isShiftDown) {
                        initialDragCamDirection = scene.getCamera().getDirection();
                        initialDragCamPosition = scene.getCamera().getPosition();
                        initialPoint = evt.getPoint();

                    }
                    double diffX = evt.getPoint().x - initialPoint.getX();
                    double diffY = evt.getPoint().y - initialPoint.getY();

                    // Rotating when shift is pressed
                    if (evt.isShiftDown()) {
                        double rotateStep = Math.toRadians(1);
                        double rotateX = rotateStep * -diffY / 3; // negated Ydiff to fix the inverted y axis
                        double rotateY = rotateStep * diffX / 3;

                        Vector3D direction = initialDragCamDirection.add(new Vector3D(rotateX, rotateY, 0));

                        // Constraint cam direction
                        if (direction.x > 0) {
                            direction.x = 0;
                        } else if (direction.x < -Math.PI / 2) {
                            direction.x = -Math.PI / 2;
                        }

                        getScene().getCamera().setDirection(direction);
                        updateViewGrid();
                    } else {
                        Vector3D position = initialDragCamPosition.add(new Vector3D(diffX, diffY, 0));
                        getScene().getCamera().setPosition(position);
                    }
                    isShiftDown = evt.isShiftDown();
                    drawingPanel.repaint();
                }
            }

            @Override
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                if (!initialized) {
                    handleInitialization(evt);
                }

                TriangleMesh mesh = getRasterizer().getMeshFromPoint(evt.getPoint());

                if (mesh == null && !isDragging) {
                    if (focusedMesh != null) {
                        getEventSupport().dispatchMouseExitMesh(new AfficheurEventSupport.MeshMouseMotionEvent(evt,
                                focusedMesh, focusedMesh.getPosition()));
                        focusedMesh = null;
                    }

                    return;
                }

                if (focusedMesh == null || !mesh.ID.equals(focusedMesh.ID)) {
                    focusedMesh = mesh;
                    initialFocusedMeshPosition = focusedMesh.getPosition();

                    getEventSupport().dispatchMouseEnterMesh(new AfficheurEventSupport.MeshMouseMotionEvent(evt,
                            focusedMesh, focusedMesh.getPosition()));

                    return;
                }

                if (mesh.ID.equals(focusedMesh.ID)) {
                    getEventSupport().dispatchMeshHovered(new AfficheurEventSupport.MeshMouseMotionEvent(evt,
                            focusedMesh, focusedMesh.getPosition()));

                    return;
                }
            }
        };
    }

    public void updateViewGrid() {
        if (!getScene().getConfiguration().getShowGridXY() && !getScene().getConfiguration().getShowGridXZ()
                && !getScene().getConfiguration().getShowGridYZ()) {
            return;
        }

        Vector3D direction = getScene().getCamera().getDirection();

        if (!direction.equals(TypeDeVue.getDirection(TypeDeVue.Facade))
                && !direction.equals(TypeDeVue.getDirection(TypeDeVue.Arriere))
                && !direction.equals(TypeDeVue.getDirection(TypeDeVue.Droite))
                && !direction.equals(TypeDeVue.getDirection(TypeDeVue.Gauche))) {
            getScene().getConfiguration().setShowGridXZ(true);
            getScene().getConfiguration().setShowGridYZ(false);
            getScene().getConfiguration().setShowGridXY(false);
        } else {
            getScene().getConfiguration().setShowGridXZ(true);
            getScene().getConfiguration().setShowGridYZ(true);
            getScene().getConfiguration().setShowGridXY(true);
        }

        drawingPanel.repaint();
    }

    public void toggleShowGrid(boolean showGrid) {
        Vector3D direction = getScene().getCamera().getDirection();

        getScene().getConfiguration().setShowGridXZ(showGrid);

        if (direction.equals(TypeDeVue.getDirection(TypeDeVue.Facade))
                || direction.equals(TypeDeVue.getDirection(TypeDeVue.Arriere))
                || direction.equals(TypeDeVue.getDirection(TypeDeVue.Droite))
                || direction.equals(TypeDeVue.getDirection(TypeDeVue.Gauche))) {
            // getScene().getConfiguration().setShowGridXZ(showGrid);
            getScene().getConfiguration().setShowGridYZ(showGrid);
            getScene().getConfiguration().setShowGridXY(showGrid);
        } else {
            getScene().getConfiguration().setShowGridYZ(false);
            getScene().getConfiguration().setShowGridXY(false);
        }

        drawingPanel.repaint();
    }

    public void deselectAllMeshes() {
        for (TriangleMeshGroup mesh : this.scene.getMeshes()) {
            mesh.setSelected(false);
        }
    }

    public void setSelectedMeshes(List<String> ids) {
        for (TriangleMeshGroup mesh : this.scene.getMeshes()) {
            mesh.setSelected(ids.contains(mesh.ID));
        }
        this.drawingPanel.repaint();
    }

    public enum TypeDeVue {
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

        public static TypeDeVue getTypeFromDirection(Vector3D direction) {
            if (direction.equals(vueDessus())) {
                return TypeDeVue.Dessus;
            } else if (direction.equals(vueFacade())) {
                return TypeDeVue.Facade;
            } else if (direction.equals(vueArriere())) {
                return TypeDeVue.Arriere;
            } else if (direction.equals(vueDroite())) {
                return TypeDeVue.Droite;
            } else if (direction.equals(vueGauche())) {
                return TypeDeVue.Gauche;
            }

            return null;
        }
    }
}
