package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d_2;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.AbstractUndoableEdit;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d_2.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d_2.mesh.Triangle;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d_2.mesh.TriangleMesh;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d_2.scene.Camera;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d_2.scene.Light;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d_2.scene.Scene;
import ca.ulaval.glo2004.domaine.utils.PanelHelper;
import ca.ulaval.glo2004.domaine.utils.Utils;
import ca.ulaval.glo2004.domaine.Chalet;
import ca.ulaval.glo2004.domaine.Controleur;

class RectFace2D {
    List<Triangle> triangles = new ArrayList<Triangle>();
    int axis = 0; // 0 = x, 1 = y, 2 = z
    Point position;
    Dimension dimension;

    public RectFace2D(Point position, Dimension dimension, int axis) {
        this.axis = axis;
        this.position = position;
        this.dimension = dimension;
        if (axis == 0) {
            triangles.add(new Triangle(
                    new Vector3D(position.getX(), position.getY(), 0),
                    new Vector3D(position.getX() + dimension.getWidth(), position.getY(), 0),
                    new Vector3D(position.getX() + dimension.getWidth(), position.getY() + dimension.getHeight(), 0)));

            triangles.add(new Triangle(
                    new Vector3D(position.getX(), position.getY(), 0),
                    new Vector3D(position.getX() + dimension.getWidth(), position.getY() + dimension.getHeight(), 0),
                    new Vector3D(position.getX(), position.getY() + dimension.getHeight(), 0)));
        } else if (axis == 1) {
            triangles.add(new Triangle(
                    new Vector3D(0, position.getY(), position.getY()),
                    new Vector3D(0, position.getY() + dimension.getHeight(), position.getX()),
                    new Vector3D(0, position.getY() + dimension.getHeight(), position.getX() + dimension.getWidth())));

            triangles.add(new Triangle(
                    new Vector3D(0, position.getY(), position.y),
                    new Vector3D(0, position.getY() + dimension.getHeight(), position.getX() + dimension.getWidth()),
                    new Vector3D(0, position.getY(), position.getX() + dimension.getWidth())));
        } else if (axis == 2) {
            triangles.add(new Triangle(
                    new Vector3D(position.getX(), 0, position.getY()),
                    new Vector3D(position.getX() + dimension.getWidth(), 0, position.getY()),
                    new Vector3D(position.getX() + dimension.getWidth(), 0, position.getY() + dimension.getHeight())));

            triangles.add(new Triangle(
                    new Vector3D(position.getX(), 0, position.getY()),
                    new Vector3D(position.getX() + dimension.getWidth(), 0, position.getY() + dimension.getHeight()),
                    new Vector3D(position.getX(), 0, position.getY() + dimension.getHeight())));
        }
    }

    public List<Triangle> getTriangles() {
        return triangles;
    }

    public RectFace2D remove(Point position, Dimension dimension) {
        position = new Point(position.x + this.position.x, position.y + this.position.y);
        RectFace2D rect1 = new RectFace2D(new Point(this.position.x, this.position.y),
                new Dimension(this.dimension.width, position.y - this.position.y), this.axis);
        RectFace2D rect2 = new RectFace2D(new Point(this.position.x, position.y),
                new Dimension(position.x - this.position.x, dimension.height), this.axis);
        RectFace2D rect3 = new RectFace2D(new Point(position.x + dimension.width, position.y), new Dimension(
                this.dimension.width - (position.x - this.position.x) - dimension.width, dimension.height), this.axis);
        RectFace2D rect4 = new RectFace2D(new Point(this.position.x, position.y + dimension.height),
                new Dimension(this.dimension.width,
                        this.dimension.height - (position.y - this.position.y) - dimension.height),
                this.axis);

        rect1.triangles.addAll(rect2.triangles);
        rect1.triangles.addAll(rect3.triangles);
        rect1.triangles.addAll(rect4.triangles);

        return rect1;
    }
}

public class Main {
    public static void main(String[] args) {
        Controleur controleur = Controleur.getInstance();

        Chalet.ChaletDTO chaletDTO = controleur.getChalet();
        Scene scene = new Scene();

        scene.getLight().setPosition(new Vector3D(100, 100, 100));
        scene.getLight().setIntensity(0.6);
        scene.getCamera().setDirection(new Vector3D(0, 0, 0));

        double[] position = new double[] { 0, 0, 0 }; //{ -chaletDTO.longueur / 2 + chaletDTO.epaisseurMur, 0, -chaletDTO.epaisseurMur / 2 };

        List<double[][]> trianglesFacade = PanelHelper.buildWall(position, new double[] { chaletDTO.largeur, chaletDTO.hauteur, chaletDTO.epaisseurMur }, false);
        List<double[][]> trianglesArriere = PanelHelper.buildWall(position, new double[] { chaletDTO.largeur, chaletDTO.hauteur, chaletDTO.epaisseurMur }, false);
        List<double[][]> trianglesDroite = PanelHelper.buildWall(position, new double[] { chaletDTO.longueur, chaletDTO.hauteur, chaletDTO.epaisseurMur }, true);
        List<double[][]> trianglesGauche = PanelHelper.buildWall(position, new double[] { chaletDTO.longueur, chaletDTO.hauteur, chaletDTO.epaisseurMur }, true);
        
        TriangleMesh murFacade = TriangleMesh.fromDoubleList(trianglesFacade);
        TriangleMesh murArriere = TriangleMesh.fromDoubleList(trianglesArriere);
        TriangleMesh murDroite = TriangleMesh.fromDoubleList(trianglesDroite);
        TriangleMesh murGauche = TriangleMesh.fromDoubleList(trianglesGauche);

        Vector3D murFacadeCenter = murFacade.getCenter().copy();
        Vector3D murArriereCenter = murArriere.getCenter().copy();
        Vector3D murDroiteCenter = murDroite.getCenter().copy();
        Vector3D murGaucheCenter = murGauche.getCenter().copy();

        murFacade = murFacade.translate(murFacadeCenter.multiplyScalar(-1));
        murArriere = murArriere.translate(murArriereCenter.multiplyScalar(-1));
        murDroite = murDroite.translate(murDroiteCenter.multiplyScalar(-1));
        murGauche = murGauche.translate(murGaucheCenter.multiplyScalar(-1));

        murArriere = murArriere.rotateY(Math.toRadians(-180));
        murDroite = murDroite.rotateY(Math.toRadians(90));
        murGauche = murGauche.rotateY(Math.toRadians(270));

        murFacade = murFacade.translate(new Vector3D(0, 0, -chaletDTO.longueur / 2 + chaletDTO.epaisseurMur));
        murArriere = murArriere.translate(new Vector3D(0, 0, chaletDTO.longueur / 2 - chaletDTO.epaisseurMur));
        murDroite = murDroite.translate(new Vector3D(-chaletDTO.largeur / 2 + chaletDTO.epaisseurMur, 0, 0));
        murGauche = murGauche.translate(new Vector3D(chaletDTO.largeur / 2 - chaletDTO.epaisseurMur, 0, 0));

        murFacade.getMaterial().setColor(Color.RED);
        murArriere.getMaterial().setColor(Color.BLUE);
        murDroite.getMaterial().setColor(Color.GREEN);
        murGauche.getMaterial().setColor(Color.YELLOW);

        scene.addMesh(murFacade);
        scene.addMesh(murArriere);
        scene.addMesh(murDroite);
        scene.addMesh(murGauche);

        // TriangleMesh test = ObjectImporter
        // .importObject("src\\main\\java\\ca\\ulaval\\glo2004\\model\\Affichage\\Affichage3D\\objects\\CW1a.obj");

        // test.scale(new Vector3D(100, 100, 100));

        // bunny.scale(new Vector3D(-1000, -1000, -1000));

        // bunny = bunny.subdivideTriangles(1);

        // bunny.translate(new Vector3D(100, 100, 1));

        // scene.addObject(bunny);
        // scene.addObject(test);

        Rasterizer afficheur = new Rasterizer(scene);

        JPanel canvas = new JPanel() {
            @Override
            public void paintComponent(java.awt.Graphics g) {
                // super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHints(new java.awt.RenderingHints(java.awt.RenderingHints.KEY_ANTIALIASING,
                        java.awt.RenderingHints.VALUE_ANTIALIAS_ON));

                afficheur.draw(g, getSize());
            }
        };

        JFrame frame = new JFrame();
        UndoManager manager = new UndoManager();

        manager.addEdit(new AbstractUndoableEdit() {
            @Override
            public String getUndoPresentationName() {
                return "Undo";
            }

            @Override
            public String getPresentationName() {
                return "Undo";
            }

            @Override
            public String getRedoPresentationName() {
                return "NAME";
            }

            @Override
            public void undo() {
                System.out.println("Undo");
            }

            @Override
            public boolean canUndo() {
                return true;
            }

            @Override
            public void redo() throws CannotRedoException {
                System.out.print("REDO");
            }
        });

        manager.undo();

        canvas.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                canvas.grabFocus();
                // System.out.println("Mouse Clicked");

                canvas.repaint();
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                // TODO Auto-generated method stub

            }
        });

        canvas.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {
                Camera camera = scene.getCamera();

                if (e.getPreciseWheelRotation() < 0) {
                    camera.zoomInDirection(e.getPoint(), canvas.getSize());
                } else {
                    camera.zoomOutDirection(e.getPoint(), canvas.getSize());
                }

                canvas.repaint();
            }
        });

        canvas.addKeyListener(new KeyListener() {
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
                Camera camera = scene.getCamera();
                double rotateStep = Math.toRadians(5);

                switch (evt.getKeyCode()) {
                    case java.awt.event.KeyEvent.VK_LEFT:
                        if (evt.isShiftDown()) {
                            camera.rotateX(rotateStep);
                        } else {
                            camera.moveLeft(10);
                        }
                        break;
                    case java.awt.event.KeyEvent.VK_RIGHT:
                        if (evt.isShiftDown()) {
                            camera.rotateX(-rotateStep);
                        } else {
                            camera.moveLeft(-10);
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
                        camera.zoomInDirection(new Point(canvas.getWidth() / 2, canvas.getHeight() / 2),
                                canvas.getSize());
                        break;
                    case java.awt.event.KeyEvent.VK_MINUS:
                        camera.zoomOutDirection(new Point(canvas.getWidth() / 2, canvas.getHeight() / 2),
                                canvas.getSize());
                        break;
                    case java.awt.event.KeyEvent.VK_1:
                        // Top
                        scene.getCamera().setDirection(new Vector3D(Math.PI / 2, 0, 0));
                        scene.getCamera().setPosition(new Vector3D(0, 0, -1000));
                        break;
                    case java.awt.event.KeyEvent.VK_2:
                        // Front
                        scene.getCamera().setDirection(new Vector3D(0, Math.PI / 2, 0));
                        scene.getCamera().setPosition(new Vector3D(0, 0, -1000));
                        break;
                    case java.awt.event.KeyEvent.VK_3:
                        // Back
                        scene.getCamera().setDirection(new Vector3D(0, -Math.PI / 2, 0));
                        scene.getCamera().setPosition(new Vector3D(0, 0, -1000));
                        break;
                    case java.awt.event.KeyEvent.VK_4:
                        // Left
                        scene.getCamera().setDirection(new Vector3D(0, 0, 0));
                        scene.getCamera().setPosition(new Vector3D(0, 0, -1000));
                        break;
                    case java.awt.event.KeyEvent.VK_5:
                        // Right
                        scene.getCamera().setDirection(new Vector3D(0, Math.PI, 0));
                        scene.getCamera().setPosition(new Vector3D(0, 0, -1000));
                        break;
                    case java.awt.event.KeyEvent.VK_R:
                        scene.getCamera().setDirection(new Vector3D(0, 0, 0));
                        scene.getCamera().setPosition(new Vector3D(0, 0, -1000));
                        break;
                }

                canvas.repaint();
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = frame.getContentPane();

        contentPane.add(canvas);

        // frame.setMinimumSize(new Dimension((int) canvas.getDimension().getWidth() +
        // 50, (int) canvas.getDimension().getHeight() + 50));
        // frame.getContentPane().add(canvas);
        frame.setSize(700, 700);
        frame.setVisible(true);
    }
}

// Read the file "/objects/bunny.obj" and create a TriangleMesh object from it.
// class BunnyObject {
// public static TriangleMesh getBunny() {
// List<Vector3D> vertices = new ArrayList<Vector3D>();
// List<Triangle> triangles = new ArrayList<Triangle>();

// // try {
// // File file = new
// File("src\\main\\java\\ca\\ulaval\\glo2004\\model\\Affichage\\Affichage3D\\objects\\bunny.obj");
// // Scanner scanner = new Scanner(file);

// // while (scanner.hasNextLine()) {
// // String line = scanner.nextLine();
// // String[] lineSplit = line.split(" ");

// // if (lineSplit[0].equals("v")) {
// // vertices.add(new Vector3D(Double.parseDouble(lineSplit[1]),
// Double.parseDouble(lineSplit[2]),
// // Double.parseDouble(lineSplit[3])));
// // } else if (lineSplit[0].equals("f")) {
// // triangles.add(new Triangle(vertices.get(Integer.parseInt(lineSplit[1]) -
// 1),
// // vertices.get(Integer.parseInt(lineSplit[2]) - 1),
// // vertices.get(Integer.parseInt(lineSplit[3]) - 1)));
// // }
// // }

// // scanner.close();
// // } catch (FileNotFoundException e) {
// // e.printStackTrace();
// // }

// // return new TriangleMesh(triangles);
// }
// }
