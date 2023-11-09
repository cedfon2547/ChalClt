package ca.ulaval.glo2004.gui.components;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;

import ca.ulaval.glo2004.domaine.afficheur.Afficheur;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMesh;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMeshGroup;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene.Camera;
import ca.ulaval.glo2004.gui.MainWindow;

public class DrawingPanel extends javax.swing.JPanel {
    MainWindow mainWindow;
    Afficheur afficheur;
    
    Object[][] btns = new Object[][] {
            { "Dessus", Afficheur.TypeDeVue.Dessus.toString(), null },
            { "Façade", Afficheur.TypeDeVue.Facade.toString(), null },
            { "Arrière", Afficheur.TypeDeVue.Arriere.toString(), null },
            { "Droite", Afficheur.TypeDeVue.Droite.toString(), null },
            { "Gauche", Afficheur.TypeDeVue.Gauche.toString(), null },
    };
    //Afficheur.TypeDeVue vueActive = Afficheur.TypeDeVue.Dessus;
    javax.swing.JToolBar barreOutils;

    public DrawingPanel(MainWindow mainWindow) {

        this.mainWindow = mainWindow;
        this.afficheur = new Afficheur(this.mainWindow.getControleur(),this.getSize());
        initComponents();
        rechargerAffichage();
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    // public Scene getScene() {
    //     return scene;
    // }

    // public Rasterizer getRasterizer() {
    //     return rasterizer;
    // }

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
                    // System.out.println("Vue de " + label + " activée");
                    for (Object[] obj : btns) {
                        if (obj[2] == btn) {
                            btn.setBackground(activeBtnColor);
                            afficheur.setVueActive(Afficheur.TypeDeVue.valueOf((String) obj[1]));
                            changerVue(afficheur.getVueActive());
                        } else {
                            ((javax.swing.JButton) obj[2]).setBackground(null);
                        }

                    }
                    invalidate();
                    repaint();
                }
            });

            if (btn.getName() == afficheur.getVueActive().toString())
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

            if (name == afficheur.getVueActive().toString())
                btn.setBackground(Color.LIGHT_GRAY);
            else
                btn.setBackground(null);
        }
    }

    private MouseWheelListener mouseWheelListener() {
        return new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {
                Camera camera = afficheur.getScene().getCamera();

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
                // System.out.println("Mouse Clicked");

                TriangleMeshGroup mesh = afficheur.getRasterizer().getMeshFromPoint(e.getPoint());
                
                afficheur.getRasterizer().deselectAllMeshes();

                if (mesh != null) {
                    System.out.println(mesh.getID() + " selected");
                    afficheur.getScene().setSelected(mesh.getID(), true);
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
                            initialDragCamPosition = afficheur.getScene().getCamera().getPosition();
                            initialDragCamDirection = afficheur.getScene().getCamera().getDirection();
                        }

                        @Override
                        public void mouseReleased(java.awt.event.MouseEvent e) {
                            // System.out.println("Mouse Released " + e.getPoint().toString());
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
                    initialDragCamDirection = afficheur.getScene().getCamera().getDirection();
                    initialDragCamPosition = afficheur.getScene().getCamera().getPosition();
                    initialPoint = e.getPoint();
                    initialized = true;
                }

                if (isDragging && initialPoint != null && initialDragCamDirection != null
                        && initialDragCamPosition != null) {
                    int diffX = e.getPoint().x - initialPoint.x;
                    int diffY = e.getPoint().y - initialPoint.y;

                    if (e.isShiftDown()) {
                        // todo DO NOT DELETE
                        // Rotating instead of translate
                        // Convert the diffX and diffY to radians
                        // double rotateStep = Math.toRadians(1);
                        // double rotateX = rotateStep * diffY / 3;
                        // double rotateY = rotateStep * diffX / 3;

                        // Vector3D direction = initialDragCamDirection.add(new Vector3D(rotateX, rotateY, 0));
                        // if (direction.x > 0) {
                        //     direction.x = 0;
                        // } else if (direction.x < -Math.PI / 2) {
                        //     direction.x = -Math.PI / 2;
                        // }

                        // this.afficheur.getScene().getCamera().setDirection(direction);
                    } else {
                        afficheur.getScene().getCamera().setPosition(initialDragCamPosition.add(new Vector3D(diffX, diffY, 0)));
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

                // todo DO NOT DELETE
                // double rotateStep = Math.toRadians(5);
                // Camera camera = afficheur.getScene().getCamera();

                // switch (evt.getKeyCode()) {
                //     case java.awt.event.KeyEvent.VK_LEFT:
                //         if (evt.isShiftDown()) {
                //             camera.rotateX(rotateStep);
                //         } else {
                //             camera.moveLeft(-10);
                //         }
                //         break;
                //     case java.awt.event.KeyEvent.VK_RIGHT:
                //         if (evt.isShiftDown()) {
                //             camera.rotateX(-rotateStep);
                //         } else {
                //             camera.moveLeft(10);
                //         }
                //         break;
                //     case java.awt.event.KeyEvent.VK_UP:
                //         if (evt.isShiftDown()) {
                //             camera.rotateY(rotateStep);
                //         } else {
                //             camera.moveUp(10);
                //         }
                //         break;
                //     case java.awt.event.KeyEvent.VK_DOWN:
                //         if (evt.isShiftDown()) {
                //             camera.rotateY(-rotateStep);
                //         } else {
                //             camera.moveUp(-10);
                //         }
                //         break;
                //     case java.awt.event.KeyEvent.VK_Z:
                //         if (evt.isShiftDown()) {
                //             camera.rotateZ(rotateStep);
                //         } else {
                //             camera.moveForward(10);
                //         }
                //         break;
                //     case java.awt.event.KeyEvent.VK_X:
                //         if (evt.isShiftDown()) {
                //             camera.rotateZ(-rotateStep);
                //         } else {
                //             camera.moveForward(-10);
                //         }
                //         break;
                //     case java.awt.event.KeyEvent.VK_EQUALS:
                //         if (evt.isShiftDown()) {
                //             this.afficheur.getScene().getLight().setIntensity(this.afficheur.getScene().getLight().getIntensity() + 0.1);
                //         } else {
                //             camera.zoomInDirection(new java.awt.Point(getWidth() / 2, getHeight() / 2), getSize());
                //         }
                //         break;
                //     case java.awt.event.KeyEvent.VK_MINUS:
                //         if (evt.isShiftDown()) {
                //             this.afficheur.getScene().getLight().setIntensity(this.afficheur.getScene().getLight().getIntensity() - 0.1);
                //         } else {
                //             camera.zoomOutDirection(new java.awt.Point(getWidth() / 2, getHeight() / 2), getSize());
                //         }
                //         break;
                //     case java.awt.event.KeyEvent.VK_R:
                //         this.afficheur.getScene().getCamera().setDirection(new Vector3D(0, 0, 0));
                //         this.afficheur.getScene().getCamera().setPosition(new Vector3D(0, 0, -1000));
                //         break;
                //     case java.awt.event.KeyEvent.VK_A:
                //         this.afficheur.getScene().getLight().setPosition(this.afficheur.getScene().getLight().getPosition().add(new Vector3D(-10, 0, 0)));
                //         break;
                //     case java.awt.event.KeyEvent.VK_D:
                //         this.afficheur.getScene().getLight().setPosition(this.afficheur.getScene().getLight().getPosition().add(new Vector3D(10, 0, 0)));
                //         break;
                //     case java.awt.event.KeyEvent.VK_W:
                //         this.afficheur.getScene().getLight().setPosition(this.afficheur.getScene().getLight().getPosition().add(new Vector3D(0, 10, 0)));
                //         break;
                //     case java.awt.event.KeyEvent.VK_S:
                //         this.afficheur.getScene().getLight().setPosition(this.afficheur.getScene().getLight().getPosition().add(new Vector3D(0, -10, 0)));
                //         break;
                //     case java.awt.event.KeyEvent.VK_Q:
                //         this.afficheur.getScene().getLight().setPosition(this.afficheur.getScene().getLight().getPosition().add(new Vector3D(0, 0, 10)));
                //         break;
                //     case java.awt.event.KeyEvent.VK_E:
                //         this.afficheur.getScene().getLight().setPosition(this.afficheur.getScene().getLight().getPosition().add(new Vector3D(0, 0, -10)));
                //         break;
                //     case java.awt.event.KeyEvent.VK_P:
                //         if (evt.isShiftDown()) {
                //             this.afficheur.getScene().getLight().setAmbientIntensity(this.afficheur.getScene().getLight().getAmbientIntensity() - 0.1);
                //         } else {
                //             this.afficheur.getScene().getLight().setAmbientIntensity(this.afficheur.getScene().getLight().getAmbientIntensity() + 0.1);
                //         }
                // }

                repaint();
            }
        };
    }

    @Override
    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        this.afficheur.draw(g, getSize());
    }

    public void changerVue(Afficheur.TypeDeVue vue) {
        afficheur.changerVue(vue);
        mainWindow.menu.activerVue(afficheur.getVueActive());
        updateToolbarBtns();
        invalidate();
        repaint();
    }


    public void rechargerAffichage() {
        try{
            afficheur.rechargerAffichage();}
        catch(Exception e){} // TODO manage error

        repaint();
    }

    
}
