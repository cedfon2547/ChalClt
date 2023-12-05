package ca.ulaval.glo2004.domaine.afficheur;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
import ca.ulaval.glo2004.domaine.utils.ObjectImporter;
import ca.ulaval.glo2004.domaine.utils.PanelHelper;
import ca.ulaval.glo2004.domaine.utils.STLTools;
import java.awt.Color;
import java.util.Objects;
import java.util.UUID;

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
            }

            @Override
            public void componentMoved(java.awt.event.ComponentEvent evt) {
            }

            @Override
            public void componentShown(java.awt.event.ComponentEvent evt) {
            }

            @Override
            public void componentHidden(java.awt.event.ComponentEvent evt) {
            }
        });
    }

    private void initialize() {
        // this.controleur.addPropertyChangeListener(Controleur.EventType.PREFERENCES_UTILISATEUR,
        // new java.beans.PropertyChangeListener() {
        // @Override
        // public void propertyChange(java.beans.PropertyChangeEvent evt) {
        // PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO =
        // (PreferencesUtilisateur.PreferencesUtilisateurDTO) evt
        // .getNewValue();
        // toggleShowGrid(preferencesUtilisateurDTO.afficherGrille);
        // scene.getConfiguration().setGridStep(preferencesUtilisateurDTO.gridSpacing);
        // drawingPanel.repaint();
        // }
        // });
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

    public void rechargerAffichage() {
        System.out.println("RENDERING");

        Chalet.ChaletDTO chaletDTO = this.getControleur().getChalet();
        PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO = this.getControleur()
                .getPreferencesUtilisateur();
        getScene().getConfiguration().setGridStep(preferencesUtilisateurDTO.gridSpacing);
        toggleShowGrid(preferencesUtilisateurDTO.afficherGrille);

        getScene().clearMeshes();

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

        // TODO: For test purpose. Was trying to define resizable bounding element
        // around meshes. To continue...
        // Vector3D[] bounding = murFacadeGroup.getBounding();
        // Vector3D diff = bounding[1].sub(bounding[0]);

        // Vector3D pos1 = new Vector3D(bounding[1].x + 10, bounding[0].y - 4,
        // bounding[0].z + diff.z / 2);

        // TriangleMesh boundingCuboid1 = new RectCuboid(pos1, new Vector3D(4, 4, 4));
        // TriangleMesh boundingCuboid2 = new RectCuboid(pos1, new Vector3D(4, 4, 4));
        // TriangleMesh boundingCuboid3 = new RectCuboid(pos1, new Vector3D(4, 4, 4));

        // TriangleMeshGroup boundingGroup1 = new TriangleMeshGroup(new TriangleMesh[] {
        // boundingCuboid1 });
        // TriangleMeshGroup boundingGroup2 = new TriangleMeshGroup(new TriangleMesh[] {
        // boundingCuboid2 }).translate(new Vector3D(0, diff.y / 2 - 2, 0));
        // TriangleMeshGroup boundingGroup3 = new TriangleMeshGroup(new TriangleMesh[] {
        // boundingCuboid3 }).translate(new Vector3D(0, diff.y + 2, 0));

        // afficheur.getScene().addMesh(boundingGroup1);
        // afficheur.getScene().addMesh(boundingGroup2);
        // afficheur.getScene().addMesh(boundingGroup3);

        // afficheur.getEventSupport().addMeshMouseListener(new MeshMouseListener() {
        // @Override
        // public void meshHovered(MeshMouseMotionEvent e) {
        // System.out.println("Mesh Hovered " + e.getMesh().ID);
        // }

        // @Override
        // public void meshClicked(MeshMouseEvent e) {
        // // TODO Auto-generated method stub

        // }

        // @Override
        // public void meshDragEnd(MeshMouseMotionEvent e) {
        // // TODO Auto-generated method stub

        // }

        // @Override
        // public void meshDragStart(MeshMouseMotionEvent e) {
        // // TODO Auto-generated method stub

        // }

        // @Override
        // public void meshDragged(MeshMouseMotionEvent e) {
        // // TODO Auto-generated method stub

        // }

        // @Override
        // public void mouseEnterMesh(MeshMouseMotionEvent e) {
        // // TODO Auto-generated method stub
        // System.out.println("MouseEnterMesh " + e.getMesh().ID);
        // if (e.getMesh().ID == boundingGroup1.ID) {
        // setCursor(new java.awt.Cursor(java.awt.Cursor.E_RESIZE_CURSOR));
        // } else if (e.getMesh().ID == boundingGroup2.id) {
        // setCursor(new java.awt.Cursor(java.awt.Cursor.N_RESIZE_CURSOR));
        // } else if (e.getMesh() == boundingGroup3) {
        // setCursor(new java.awt.Cursor(java.awt.Cursor.S_RESIZE_CURSOR));
        // } else {
        // setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        // }

        // }

        // @Override
        // public void mouseExitMesh(MeshMouseMotionEvent e) {
        // // TODO Auto-generated method stub
        // System.out.println("MouseExitMesh " + e.getMesh().ID);
        // setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        // }
        // });

        // TriangleMeshGroup pignonMesh = PanelHelper.buildPanneauToit(chaletDTO.largeur, chaletDTO.longueur,
        //         chaletDTO.epaisseurMur,
        //         chaletDTO.angleToit, new Vector3D(0, 0, 0));
        // TriangleMeshGroup rallongeVerticale = PanelHelper.buildRallongeVertical(chaletDTO.largeur, chaletDTO.hauteur, chaletDTO.epaisseurMur, chaletDTO.angleToit, 0, new Vector3D(0, 0, 0));

        // getScene().addMesh(pignonMesh);
        // getScene().addMesh(rallongeVerticale);

        getScene().addMesh(murFacadeGroup);
        getScene().addMesh(murArriereGroup);
        getScene().addMesh(murDroitGroup);
        getScene().addMesh(murGaucheGroup);

        getScene().getMeshes().addAll(murFacadeGroup.getAccessoiresMeshes());
        getScene().getMeshes().addAll(murArriereGroup.getAccessoiresMeshes());
        getScene().getMeshes().addAll(murDroitGroup.getAccessoiresMeshes());
        getScene().getMeshes().addAll(murGaucheGroup.getAccessoiresMeshes());

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
            TriangleMesh mesh = retraitMesh.stream().filter((retrait) -> Objects.equals(retrait.ID, acc.accessoireId.toString())).findFirst().orElse(null);
            if (mesh != null) {
                return new RetraitTest(acc, mesh);
            } else {
                return null;
            }
        }).filter((value) -> value != null).collect(java.util.stream.Collectors.toList());

        for(RetraitTest retraitTest: retraits){
            String facadeRetraitsFileName = String.format("\\%s_Retrait_%s_%s.stl", nomChalet, retraitTest.accessoireDTO.typeMur, retraitTest.accessoireDTO.accessoireNom);
            List<STLTools.Triangle> stltriangle = PanelHelper.convertMeshTrianglesToStlTriangles(retraitTest.mesh.getTriangles());
            STLTools.writeSTL(stltriangle, directoryPath + facadeRetraitsFileName);
        }


        TriangleMesh rainure1_Facade = murFacadeGroup.getMeshRetraits().getMesh("rainure1");
        String facadeRetraitsFileFacade1 = String.format("\\%s_Retrait_F_rainure1.stl", nomChalet);
        List<STLTools.Triangle> stl_rainure1_Facade = PanelHelper.convertMeshTrianglesToStlTriangles(rainure1_Facade.getTriangles());
        STLTools.writeSTL(stl_rainure1_Facade, directoryPath + facadeRetraitsFileFacade1);

        TriangleMesh rainure2_Facade = murFacadeGroup.getMeshRetraits().getMesh("rainure2");
        String facadeRetraitsFileFacade2 = String.format("\\%s_Retrait_F_rainure2.stl", nomChalet);
        List<STLTools.Triangle> stl_rainure2_Facade = PanelHelper.convertMeshTrianglesToStlTriangles(rainure2_Facade.getTriangles());
        STLTools.writeSTL(stl_rainure2_Facade, directoryPath + facadeRetraitsFileFacade2);

        TriangleMesh rainure1_Arriere = murArriereGroup.getMeshRetraits().getMesh("rainure1");
        String facadeRetraitsFileArriere1 = String.format("\\%s_Retrait_A_rainure1.stl", nomChalet);
        List<STLTools.Triangle> stl_rainure1_Arriere = PanelHelper.convertMeshTrianglesToStlTriangles(rainure1_Arriere.getTriangles());
        STLTools.writeSTL(stl_rainure1_Arriere, directoryPath + facadeRetraitsFileArriere1);

        TriangleMesh rainure2_Arriere = murArriereGroup.getMeshRetraits().getMesh("rainure2");
        String facadeRetraitsFileArriere2 = String.format("\\%s_Retrait_A_rainure2.stl", nomChalet);
        List<STLTools.Triangle> stl_rainure2_Arriere = PanelHelper.convertMeshTrianglesToStlTriangles(rainure2_Arriere.getTriangles());
        STLTools.writeSTL(stl_rainure2_Arriere, directoryPath + facadeRetraitsFileArriere2);

        TriangleMesh rainure1_Gauche = murGaucheGroup.getMeshRetraits().getMesh("rainure1");
        String facadeRetraitsFileGauche1 = String.format("\\%s_Retrait_G_rainure1.stl", nomChalet);
        List<STLTools.Triangle> stl_rainure1_Gauche = PanelHelper.convertMeshTrianglesToStlTriangles(rainure1_Gauche.getTriangles());
        STLTools.writeSTL(stl_rainure1_Gauche, directoryPath + facadeRetraitsFileGauche1);

        TriangleMesh rainure2_Gauche = murGaucheGroup.getMeshRetraits().getMesh("rainure2");
        String facadeRetraitsFileGauche2 = String.format("\\%s_Retrait_G_rainure2.stl", nomChalet);
        List<STLTools.Triangle> stl_rainure2_Gauche = PanelHelper.convertMeshTrianglesToStlTriangles(rainure2_Gauche.getTriangles());
        STLTools.writeSTL(stl_rainure2_Gauche, directoryPath + facadeRetraitsFileGauche2);

        TriangleMesh rainure1_Droit = murDroitGroup.getMeshRetraits().getMesh("rainure1");
        String facadeRetraitsFileDroit1 = String.format("\\%s_Retrait_D_rainure1.stl", nomChalet);
        List<STLTools.Triangle> stl_rainure1_Droit = PanelHelper.convertMeshTrianglesToStlTriangles(rainure1_Droit.getTriangles());
        STLTools.writeSTL(stl_rainure1_Droit, directoryPath + facadeRetraitsFileDroit1);

        TriangleMesh rainure2_Droit = murDroitGroup.getMeshRetraits().getMesh("rainure2");
        String facadeRetraitsFileDroit2 = String.format("\\%s_Retrait_D_rainure2.stl", nomChalet);
        List<STLTools.Triangle> stl_rainure2_Droit = PanelHelper.convertMeshTrianglesToStlTriangles(rainure2_Droit.getTriangles());
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
        this.rasterizer.draw(g, dimension);

        // Draw a 3D cube

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

        // this.getScene().getCamera().setPosition(new
        // Vector3D(this.scene.getCamera().getPosition().x,
        // (vueActive == TypeDeVue.Dessus) ? 0 : controleur.getChalet().hauteur / 2,
        // this.scene.getCamera().getPosition().z));

        drawingPanel.invalidate();
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

    private MouseListener mouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // drawingPanel.grabFocus();

                TriangleMesh clickedMesh = getRasterizer().getMeshFromPoint(evt.getPoint());

                // if (clickedMesh == null) {
                // deselectAllMeshed();
                // eventSupport.dispatchSelectionChanged(new
                // AfficheurEventSupport.MeshSelectionEvent(getSelection()));
                // drawingPanel.repaint();
                // return;
                // }

                if (clickedMesh != null && evt.getClickCount() == 2) {

                    // pcs.firePropertyChange(AfficheurEvent.MeshDoubleClicked.toString(), null,
                    // clickedMesh);

                    if (clickedMesh instanceof PanelHelper.MurTriangleMeshGroup) {
                        // System.out.println("Double clicked on a wall");
                        switch (((PanelHelper.MurTriangleMeshGroup) clickedMesh).getTypeMur()) {
                            case Facade:
                                changerVue(Afficheur.TypeDeVue.Facade);
                                break;
                            case Arriere:
                                changerVue(Afficheur.TypeDeVue.Arriere);
                                break;
                            case Droit:
                                changerVue(Afficheur.TypeDeVue.Droite);
                                break;
                            case Gauche:
                                changerVue(Afficheur.TypeDeVue.Gauche);
                                break;
                            default:
                                // nop, fall through
                        }
                    }

                    // if (clickedMesh.getSelectable()) {
                    // clickedMesh.setSelected(true);
                    // }

                    updateViewGrid();
                    // drawingPanel.repaint();
                    // eventSupport.dispatchMeshClicked(new
                    // AfficheurEventSupport.MeshMouseEvent(evt, clickedMesh));
                    // eventSupport.dispatchViewChanged(new
                    // AfficheurEventSupport.ViewChangedEvent(getVueActive()));
                    return;
                } else if (clickedMesh != null && clickedMesh.getSelectable()) {
                    // pcs.firePropertyChange(AfficheurEvent.SelectionChanged.toString(), null,
                    // clickedMesh);
                    // pcs.firePropertyChange(AfficheurEvent.MeshClicked.toString(), null,
                    // clickedMesh);
                    // if (!evt.isControlDown()) {
                    // deselectAllMeshed();
                    // }

                    // clickedMesh.setSelected(!clickedMesh.getSelected());
                    // eventSupport.dispatchMeshClicked(new
                    // AfficheurEventSupport.MeshMouseEvent(evt, clickedMesh));
                    // eventSupport.dispatchSelectionChanged(new
                    // AfficheurEventSupport.MeshSelectionEvent(getSelection()));
                }

                drawingPanel.repaint();
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                // drawingPanel.grabFocus();
                TriangleMesh clickedMesh = getRasterizer().getMeshFromPoint(evt.getPoint());
                if (clickedMesh != null) {
                    System.out.println("Mouse pressed on mesh: " + clickedMesh.ID);

                    if (evt.isControlDown()) {
                        clickedMesh.setSelected(!clickedMesh.getSelected());
                    } else {
                        deselectAllMeshed();
                        clickedMesh.setSelected(true);
                    }
                } else {
                    deselectAllMeshed();
                }

                eventSupport.dispatchSelectionChanged(new AfficheurEventSupport.MeshSelectionEvent(getSelection()));
                drawingPanel.repaint();
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
            boolean isDragging = false;
            Vector3D initialDragCamPosition = null;
            Vector3D initialDragCamDirection = null;
            Point2D initialPoint = null;
            Vector3D initialDragMeshPosition = null;
            TriangleMesh lastMouseEnteredMesh = null;
            TriangleMesh lastDraggedMesh = null;
            boolean dragStarted = false;

            MouseListener mouseListener = new MouseListener() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {

                }

                @Override
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    isDragging = true;
                    initialPoint = evt.getPoint();
                    initialDragCamPosition = scene.getCamera().getPosition();
                    initialDragCamDirection = scene.getCamera().getDirection();

                    TriangleMesh clickedMesh = getRasterizer().getMeshFromPoint(evt.getPoint());
                    if (clickedMesh != null && clickedMesh.getDraggable()) {
                        initialDragMeshPosition = clickedMesh.getPosition();
                        lastDraggedMesh = clickedMesh;
                        dragStarted = false;
                    }
                }

                @Override
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    isDragging = false;
                    initialPoint = null;
                    initialDragCamPosition = null;
                    initialDragCamDirection = null;

                    if (lastDraggedMesh != null && dragStarted) {
                        dragStarted = false;
                        lastDraggedMesh.setIsDragged(false);

                        lastDraggedMesh.setSelected(true);
                        getEventSupport().dispatchMeshDragEnd(new AfficheurEventSupport.MeshMouseMotionEvent(evt,
                                lastDraggedMesh, lastDraggedMesh.getPosition()));
                    }

                    lastDraggedMesh = null;
                    drawingPanel.repaint();
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
                    drawingPanel.addMouseListener(mouseListener);
                    initialized = true;
                    isDragging = true;
                    initialDragCamDirection = scene.getCamera().getDirection();
                    initialDragCamPosition = scene.getCamera().getPosition();
                    initialPoint = evt.getPoint();

                    TriangleMesh clickedMesh = getRasterizer().getMeshFromPoint(evt.getPoint());
                    if (clickedMesh != null && clickedMesh.getDraggable()) {
                        initialDragMeshPosition = clickedMesh.getPosition();
                        lastDraggedMesh = clickedMesh;
                        getScene().clearAllSelection();
                        clickedMesh.setIsDragged(true);
                        // getEventSupport().dispatchMeshDragStart(new
                        // AfficheurEventSupport.MeshMouseMotionEvent(evt, lastDraggedMesh,
                        // lastDraggedMesh.getPosition()));
                    }
                }

                if (lastDraggedMesh != null) {
                    if (dragStarted == false) {
                        dragStarted = true;
                        getScene().clearAllSelection();
                        lastDraggedMesh.setIsDragged(true);
                        getEventSupport().dispatchMeshDragStart(new AfficheurEventSupport.MeshMouseMotionEvent(evt,
                                lastDraggedMesh, lastDraggedMesh.getPosition()));
                    }

                    Vector3D diff = new Vector3D(evt.getX(), evt.getY(), 0)
                            .sub(new Vector3D(initialPoint.getX(), initialPoint.getY(), 0))
                            .multiply(scene.getCamera().getInverseRotationTransformation());

                    if (!lastDraggedMesh.getDraggableX()) {
                        diff.x = 0;
                    }

                    if (!lastDraggedMesh.getDraggableY()) {
                        diff.y = 0;
                    }

                    if (!lastDraggedMesh.getDraggableZ()) {
                        diff.z = 0;
                    }
                    Vector3D position = initialDragMeshPosition.add(diff);

                    // System.out.println("Dragged mesh position: " + position);
                    lastDraggedMesh.setPosition(position);
                    eventSupport.dispatchMeshDragged(new AfficheurEventSupport.MeshMouseMotionEvent(evt,
                            lastDraggedMesh, initialDragMeshPosition, diff));
                    drawingPanel.repaint();
                    return;
                }

                if (isDragging && initialPoint != null && initialDragCamDirection != null
                        && initialDragCamPosition != null) {
                    double diffX = evt.getPoint().x - initialPoint.getX();
                    double diffY = evt.getPoint().y - initialPoint.getY();

                    // Rotating when shift is pressed
                    if (evt.isShiftDown()) {
                        double rotateStep = Math.toRadians(1);
                        double rotateX = rotateStep * -diffY / 3; // negated Ydiff to fix the inverted y axis
                        double rotateY = rotateStep * diffX / 3;

                        Vector3D direction = initialDragCamDirection.add(new Vector3D(rotateX, rotateY, 0));

                        // Constraint cam direction
                        // if (direction.x > 0) {
                        //     direction.x = 0;
                        // } else if (direction.x < -Math.PI / 2) {
                        //     direction.x = -Math.PI / 2;
                        // }

                        // pcs.firePropertyChange(AfficheurEvent.CameraDirectionChanged.toString(),
                        // getScene().getCamera().getDirection(), direction);
                        getScene().getCamera().setDirection(direction);
                        updateViewGrid();
                    } else {
                        Vector3D position = initialDragCamPosition.add(new Vector3D(diffX, diffY, 0));
                        // pcs.firePropertyChange(AfficheurEvent.CameraPositionChanged.toString(),
                        // getScene().getCamera().getPosition(), position);
                        getScene().getCamera().setPosition(position);
                    }

                    drawingPanel.repaint();
                }
            }

            @Override
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                TriangleMesh mouseEnteredMesh = getRasterizer().getMeshFromPoint(evt.getPoint());

                if (mouseEnteredMesh != null) {
                    if (lastMouseEnteredMesh != null && mouseEnteredMesh != lastMouseEnteredMesh) {
                        // pcs.firePropertyChange(AfficheurEvent.MouseExitMesh.toString(), null,
                        // lastMouseEnteredMesh);
                        eventSupport.dispatchMouseExitMesh(
                                new AfficheurEventSupport.MeshMouseMotionEvent(evt, lastMouseEnteredMesh,
                                        lastMouseEnteredMesh.getPosition()));
                    }

                    // pcs.firePropertyChange(AfficheurEvent.MouseEnterMesh.toString(),
                    // lastMouseEnteredMesh,
                    // mouseEnteredMesh);
                    eventSupport.dispatchMouseEnterMesh(
                            new AfficheurEventSupport.MeshMouseMotionEvent(evt, mouseEnteredMesh,
                                    mouseEnteredMesh.getPosition()));
                    lastMouseEnteredMesh = mouseEnteredMesh;
                } else {
                    if (lastMouseEnteredMesh != null) {
                        // pcs.firePropertyChange(AfficheurEvent.MouseExitMesh.toString(), null,
                        // lastMouseEnteredMesh);
                        eventSupport.dispatchMouseExitMesh(
                                new AfficheurEventSupport.MeshMouseMotionEvent(evt, lastMouseEnteredMesh,
                                        lastMouseEnteredMesh.getPosition()));
                        lastMouseEnteredMesh = null;
                    }
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

    public void deselectAllMeshed() {
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
