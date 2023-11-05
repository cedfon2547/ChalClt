package ca.ulaval.glo2004.gui.components;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;

import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.Chalet;
import ca.ulaval.glo2004.domaine.TypeAccessoire;
import ca.ulaval.glo2004.domaine.TypeMur;
import ca.ulaval.glo2004.domaine.afficheur.Afficheur;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.Rasterizer;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMesh;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMeshGroup;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene.Camera;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene.Scene;
import ca.ulaval.glo2004.domaine.utils.PanelHelper;
import ca.ulaval.glo2004.gui.MainWindow;

public class DrawingPanel extends javax.swing.JPanel {
    MainWindow mainWindow;
    Afficheur afficheur;
    Scene scene;
    Rasterizer rasterizer;
    Object[][] btns = new Object[][] {
            { "Dessus", TypeDeVue.Dessus.toString(), null },
            { "Façade", TypeDeVue.Facade.toString(), null },
            { "Arrière", TypeDeVue.Arriere.toString(), null },
            { "Droite", TypeDeVue.Droite.toString(), null },
            { "Gauche", TypeDeVue.Gauche.toString(), null },
    };
    TypeDeVue vueActive = TypeDeVue.Dessus;
    javax.swing.JToolBar barreOutils;

    public DrawingPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.afficheur = new Afficheur(this);
        this.scene = new Scene();
        this.rasterizer = new Rasterizer(this.scene);

        this.scene.getLight().setPosition(new Vector3D(getWidth(), 200, 0));
        this.scene.getCamera().setDirection(TypeDeVue.vueDessus());

        initComponents();
        rechargerAffichage();
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    public Scene getScene() {
        return scene;
    }

    public Rasterizer getRasterizer() {
        return rasterizer;
    }

    private void initComponents() {
        setBackground(java.awt.Color.BLACK);

        addMouseListener(this.mouseListener());
        addMouseWheelListener(this.mouseWheelListener());
        addKeyListener(this.keyListener());
        addMouseMotionListener(this.mouseMotionListener());

        buildToolbar();
    }

    private void buildToolbar() {
        final Color activeBtnColor = Color.LIGHT_GRAY;

        barreOutils = new javax.swing.JToolBar("Barre d'outils");

        barreOutils.setFloatable(false);
        barreOutils.setRollover(true);
        barreOutils.setBorder(new EmptyBorder(0, 0, 0, 0));
        barreOutils.setLayout(new GridLayout());
        barreOutils.setOpaque(false);

        for (Object[] obj : btns) {
            final String label = (String) obj[0];
            final String name = (String) obj[1];

            final javax.swing.JButton btn = new javax.swing.JButton(label);
            obj[2] = btn;

            btn.setFocusable(false);
            btn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            btn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
            btn.setName(name);
            btn.setOpaque(true);

            btn.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    System.out.println("Vue de " + label + " activée");
                    for (Object[] obj : btns) {
                        if (obj[2] == btn) {
                            btn.setBackground(activeBtnColor);
                            vueActive = TypeDeVue.valueOf((String) obj[1]);
                            changerVue(vueActive);
                        } else {
                            ((javax.swing.JButton) obj[2]).setBackground(null);
                        }

                    }
                    invalidate();
                    repaint();
                }
            });

            if (btn.getName() == vueActive.toString())
                btn.setBackground(activeBtnColor);

            barreOutils.add(btn);

            invalidate();
            repaint();
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(barreOutils, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(barreOutils, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));

    }

    public void updateToolbarBtns() {
        for (Object[] obj : btns) {
            final String name = (String) obj[1];
            final javax.swing.JButton btn = (JButton) obj[2];

            if (name == vueActive.toString())
                btn.setBackground(Color.LIGHT_GRAY);
            else
                btn.setBackground(null);
        }
    }

    private MouseWheelListener mouseWheelListener() {
        return new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {
                Camera camera = scene.getCamera();

                if (e.getPreciseWheelRotation() < 0) {
                    camera.zoomInDirection(e.getPoint(), getSize());
                } else {
                    camera.zoomOutDirection(e.getPoint(), getSize());
                }

                repaint();
            }
        };
    }

    private MouseListener mouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                grabFocus();
                System.out.println("Mouse Clicked");

                TriangleMesh mesh = rasterizer.getMeshFromPoint(e.getPoint());
                rasterizer.deselectAllMeshes();

                if (mesh != null) {
                    // System.out.println(mesh.getID());
                    System.out.println(mesh.getHandle());
                    mesh.setSelected(true);
                }

                repaint();
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
            }
        };
    }

    private MouseMotionListener mouseMotionListener() {
        return new MouseMotionListener() {
            boolean initialized = false;
            boolean isDragging = false;
            Vector3D initialDragCamPosition = null;
            Vector3D initialDragCamDirection = null;
            Point initialPoint = null;

            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                if (!initialized) {
                    MouseListener mouseListener = new MouseListener() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent e) {
                        }

                        @Override
                        public void mousePressed(java.awt.event.MouseEvent e) {
                            isDragging = true;
                            initialPoint = e.getPoint();
                            initialDragCamPosition = scene.getCamera().getPosition();
                            initialDragCamDirection = scene.getCamera().getDirection();
                        }

                        @Override
                        public void mouseReleased(java.awt.event.MouseEvent e) {
                            System.out.println("Mouse Released " + e.getPoint().toString());
                            isDragging = false;
                            initialPoint = null;
                            initialDragCamPosition = null;
                            initialDragCamDirection = null;
                        }

                        @Override
                        public void mouseEntered(java.awt.event.MouseEvent e) {

                        }

                        @Override
                        public void mouseExited(java.awt.event.MouseEvent e) {
                        }
                    };

                    addMouseListener(mouseListener);
                    isDragging = true;
                    initialDragCamDirection = scene.getCamera().getDirection();
                    initialDragCamPosition = scene.getCamera().getPosition();
                    initialPoint = e.getPoint();
                    initialized = true;
                }

                if (isDragging && initialPoint != null && initialDragCamDirection != null
                        && initialDragCamPosition != null) {
                    int diffX = e.getPoint().x - initialPoint.x;
                    int diffY = e.getPoint().y - initialPoint.y;

                    if (e.isShiftDown()) {
                        // Rotating instead of translate
                        // Convert the diffX and diffY to radians
                        double rotateStep = Math.toRadians(1);
                        double rotateX = rotateStep * diffY / 3;
                        double rotateY = rotateStep * diffX / 3;

                        Vector3D direction = initialDragCamDirection.add(new Vector3D(rotateX, rotateY, 0));
                        if (direction.x > 0) {
                            direction.x = 0;
                        } else if (direction.x < -Math.PI / 2) {
                            direction.x = -Math.PI / 2;
                        }

                        scene.getCamera().setDirection(direction);
                    } else {
                        scene.getCamera().setPosition(initialDragCamPosition.add(new Vector3D(diffX, diffY, 0)));
                    }

                    repaint();
                }
            }

            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                // TODO Auto-generated method stub
            }
        };
    }

    public KeyListener keyListener() {
        return new KeyListener() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                // TODO Auto-generated method stub
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                double rotateStep = Math.toRadians(5);
                Camera camera = scene.getCamera();

                switch (evt.getKeyCode()) {
                    case java.awt.event.KeyEvent.VK_LEFT:
                        if (evt.isShiftDown()) {
                            camera.rotateX(rotateStep);
                        } else {
                            camera.moveLeft(-10);
                        }
                        break;
                    case java.awt.event.KeyEvent.VK_RIGHT:
                        if (evt.isShiftDown()) {
                            camera.rotateX(-rotateStep);
                        } else {
                            camera.moveLeft(10);
                        }
                        break;
                    case java.awt.event.KeyEvent.VK_UP:
                        if (evt.isShiftDown()) {
                            camera.rotateY(rotateStep);
                        } else {
                            camera.moveUp(10);
                        }
                        break;
                    case java.awt.event.KeyEvent.VK_DOWN:
                        if (evt.isShiftDown()) {
                            camera.rotateY(-rotateStep);
                        } else {
                            camera.moveUp(-10);
                        }
                        break;
                    case java.awt.event.KeyEvent.VK_Z:
                        if (evt.isShiftDown()) {
                            camera.rotateZ(rotateStep);
                        } else {
                            camera.moveForward(10);
                        }
                        break;
                    case java.awt.event.KeyEvent.VK_X:
                        if (evt.isShiftDown()) {
                            camera.rotateZ(-rotateStep);
                        } else {
                            camera.moveForward(-10);
                        }
                        break;
                    case java.awt.event.KeyEvent.VK_EQUALS:
                        if (evt.isShiftDown()) {
                            scene.getLight().setIntensity(scene.getLight().getIntensity() + 0.1);
                        } else {
                            camera.zoomInDirection(new java.awt.Point(getWidth() / 2, getHeight() / 2), getSize());
                        }
                        break;
                    case java.awt.event.KeyEvent.VK_MINUS:
                        if (evt.isShiftDown()) {
                            scene.getLight().setIntensity(scene.getLight().getIntensity() - 0.1);
                        } else {
                            camera.zoomOutDirection(new java.awt.Point(getWidth() / 2, getHeight() / 2), getSize());
                        }
                        break;
                    case java.awt.event.KeyEvent.VK_R:
                        scene.getCamera().setDirection(new Vector3D(0, 0, 0));
                        scene.getCamera().setPosition(new Vector3D(0, 0, -1000));
                        break;
                    case java.awt.event.KeyEvent.VK_A:
                        scene.getLight().setPosition(scene.getLight().getPosition().add(new Vector3D(-10, 0, 0)));
                        break;
                    case java.awt.event.KeyEvent.VK_D:
                        scene.getLight().setPosition(scene.getLight().getPosition().add(new Vector3D(10, 0, 0)));
                        break;
                    case java.awt.event.KeyEvent.VK_W:
                        scene.getLight().setPosition(scene.getLight().getPosition().add(new Vector3D(0, 10, 0)));
                        break;
                    case java.awt.event.KeyEvent.VK_S:
                        scene.getLight().setPosition(scene.getLight().getPosition().add(new Vector3D(0, -10, 0)));
                        break;
                    case java.awt.event.KeyEvent.VK_Q:
                        scene.getLight().setPosition(scene.getLight().getPosition().add(new Vector3D(0, 0, 10)));
                        break;
                    case java.awt.event.KeyEvent.VK_E:
                        scene.getLight().setPosition(scene.getLight().getPosition().add(new Vector3D(0, 0, -10)));
                        break;
                    case java.awt.event.KeyEvent.VK_P:
                        if (evt.isShiftDown()) {
                            scene.getLight().setAmbientIntensity(scene.getLight().getAmbientIntensity() - 0.1);
                        } else {
                            scene.getLight().setAmbientIntensity(scene.getLight().getAmbientIntensity() + 0.1);
                        }
                }

                repaint();
            }
        };
    }

    @Override
    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        this.rasterizer.draw(g, getSize());
    }

    public void changerVue(TypeDeVue vue) {
        this.vueActive = vue;

        if (vueActive == TypeDeVue.Dessus) {
            scene.getCamera().setDirection(TypeDeVue.vueDessus());
        } else if (vueActive == TypeDeVue.Facade) {
            scene.getCamera().setDirection(TypeDeVue.vueFacade());
        } else if (vueActive == TypeDeVue.Arriere) {
            scene.getCamera().setDirection(TypeDeVue.vueArriere());
        } else if (vueActive == TypeDeVue.Droite) {
            scene.getCamera().setDirection(TypeDeVue.vueDroite());
        } else if (vueActive == TypeDeVue.Gauche) {
            scene.getCamera().setDirection(TypeDeVue.vueGauche());
        }

        mainWindow.menu.activerVue(vueActive);
        updateToolbarBtns();
        invalidate();
        repaint();
    }

    public void rechargerAffichage() {
        Chalet.ChaletDTO chaletDTO = this.mainWindow.getControleur().getChalet();
        scene.clearMeshes();

        TriangleMesh[] meshes = PanelHelper.generateMeshMurs(chaletDTO.largeur, chaletDTO.hauteur, chaletDTO.longueur,
                chaletDTO.epaisseurMur);

        List<Accessoire.AccessoireDTO> accessoires = this.mainWindow.getControleur().getAccessoires();

        for (Accessoire.AccessoireDTO accessoireDTO : accessoires) {
            TypeMur typeMur = accessoireDTO.typeMur;

            TriangleMeshGroup accMesh = null;

            if (accessoireDTO.accessoireType == TypeAccessoire.Fenetre) {
                accMesh = PanelHelper.buildWindow(accessoireDTO.dimensions[0], accessoireDTO.dimensions[1],
                        new Vector3D(0, 0, 0), 5);
            } else if (accessoireDTO.accessoireType == TypeAccessoire.Porte) {
                accMesh = PanelHelper.buildDoor(accessoireDTO.dimensions[0], accessoireDTO.dimensions[1],
                        new Vector3D(0, 0, 0), 4);
            }

            switch (typeMur) {
                case Facade:
                    // Moving the door mesh to the top left corner of the wall
                    accMesh = accMesh.translate(meshes[0].getCenter());
                    accMesh = accMesh.translate(new Vector3D(meshes[0].getWidth() / 2 - accMesh.getWidth(),
                            -meshes[0].getHeight() / 2, -chaletDTO.epaisseurMur));
                    break;
                case Arriere:
                    accMesh = accMesh.rotateY(Math.PI);
                    accMesh = accMesh.translate(meshes[1].getCenter());
                    accMesh = accMesh.translate(new Vector3D(
                            -meshes[1].getWidth() / 2 + accMesh.getWidth() / 2,
                            -meshes[1].getHeight() / 2 + accMesh.getHeight() / 2, chaletDTO.epaisseurMur + 2));
                    break;
                case Gauche:
                    accMesh = accMesh.rotateY(-Math.PI / 2);
                    accMesh = accMesh.translate(meshes[3].getCenter());
                    accMesh = accMesh.translate(new Vector3D(chaletDTO.epaisseurMur + 2,
                            -meshes[3].getHeight() / 2 + accMesh.getHeight() / 2,
                            meshes[3].getDepth() / 2 - accMesh.getDepth() / 2));
                    break;
                case Droit:
                    accMesh = accMesh.rotateY(Math.PI / 2);
                    accMesh = accMesh.translate(meshes[2].getCenter());
                    accMesh = accMesh.translate(new Vector3D(-chaletDTO.epaisseurMur - 2,
                            -meshes[2].getHeight() / 2 + accMesh.getHeight() / 2,
                            -meshes[2].getDepth() / 2 + accMesh.getDepth() / 2));
                    break;
            }

            this.scene.addMeshes(accMesh.getMeshes());
            repaint();
        }

        meshes[0].getMaterial().setColor(java.awt.Color.RED);
        meshes[1].getMaterial().setColor(java.awt.Color.BLUE);
        meshes[2].getMaterial().setColor(java.awt.Color.GREEN);
        meshes[3].getMaterial().setColor(java.awt.Color.YELLOW);

        scene.addMeshes(meshes);
        repaint();
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
}
