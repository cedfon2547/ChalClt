package ca.ulaval.glo2004.domaine.utils;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo2004.domaine.Accessoire;
import ca.ulaval.glo2004.domaine.Chalet;
import ca.ulaval.glo2004.domaine.TypeMur;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.Triangle;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMesh;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMeshGroup;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.shapes.RectCuboid;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Cette classe contient une méthode statique pour construire un mur en 3D à
 * partir d'une position et d'une dimension données.
 * La méthode retourne une liste de triangles représentant les différentes faces
 * du mur.
 */
public class PanelHelper {

    /**
     * Cette méthode construit un mur en 3D à partir d'une position et d'une
     * dimension données.
     *
     * @param position  un tableau de 3 doubles représentant la position du mur dans
     *                  l'espace (x, y, z)
     * @param dimension un tableau de 3 doubles représentant la dimension du mur
     *                  dans l'espace (largeur, hauteur, profondeur)
     * @param truncate  un booléen indiquant si le mur doit être tronqué (true) ou
     *                  non (false)
     * @return une liste de tableaux de 3 doubles représentant les triangles formant
     *         les différentes faces du mur
     */
    public static List<double[][]> buildWall(double[] position, Dimension dimension, double epaisseurMur,
            double margeSupplementaire, boolean truncate) {
        double x0 = position[0], y0 = position[1], z0 = position[2];
        double largeur = dimension.getWidth(), hauteur = dimension.getHeight();
        double d = epaisseurMur;
        double m = margeSupplementaire / 2;

        // Si truncate == vrai, marge pleine retirée sur une partie de la rainure
        if (truncate) {
            largeur -= d;

        }

        double[] p1 = new double[] { x0, y0, z0 };
        double[] p2 = new double[] { x0, y0, z0 + d / 2 + m };
        double[] p3 = new double[] { x0 + d / 2 - m, y0, z0 + d / 2 + m };
        double[] p4 = new double[] { x0 + d / 2 - m, y0, z0 + d };
        double[] p5 = new double[] { x0 + largeur - d / 2 + m, y0, z0 + d };
        double[] p6 = new double[] { x0 + largeur - d / 2 + m, y0, z0 + d / 2 + m };
        double[] p7 = new double[] { x0 + largeur, y0, z0 + d / 2 + m };
        double[] p8 = new double[] { x0 + largeur, y0, z0 };

        double[] p9 = new double[] { x0, y0 + hauteur, z0 };
        double[] p10 = new double[] { x0, y0 + hauteur, z0 + d / 2 + m };
        double[] p11 = new double[] { x0 + d / 2 - m, y0 + hauteur, z0 + d / 2 + m };
        double[] p12 = new double[] { x0 + d / 2 - m, y0 + hauteur, z0 + d };
        double[] p13 = new double[] { x0 + largeur - d / 2 + m, y0 + hauteur, z0 + d };
        double[] p14 = new double[] { x0 + largeur - d / 2 + m, y0 + hauteur, z0 + d / 2 + m };
        double[] p15 = new double[] { x0 + largeur, y0 + hauteur, z0 + d / 2 + m };
        double[] p16 = new double[] { x0 + largeur, y0 + hauteur, z0 };

        List<double[][]> triangles = new ArrayList<>();
        if (truncate) {

            p1 = new double[] { x0 + m / 2, y0, z0 };
            p2 = new double[] { x0 + m / 2, y0, z0 + d / 2 - m / 2 };
            p3 = new double[] { x0 + d / 2 + m, y0, z0 + d / 2 - m / 2 };
            p4 = new double[] { x0 + d / 2 + m, y0, z0 + d };
            p5 = new double[] { x0 + largeur - d / 2 - m, y0, z0 + d };
            p6 = new double[] { x0 + largeur - d / 2 - m, y0, z0 + d / 2 - m / 2 };
            p7 = new double[] { x0 + largeur - m / 2, y0, z0 + d / 2 - m / 2 };
            p8 = new double[] { x0 + largeur - m / 2, y0, z0 };
            p9 = new double[] { x0 + m / 2, y0 + hauteur, z0 };
            p10 = new double[] { x0 + m / 2, y0 + hauteur, z0 + d / 2 - m / 2 };
            p11 = new double[] { x0 + d / 2 + m, y0 + hauteur, z0 + d / 2 - m / 2 };
            p12 = new double[] { x0 + d / 2 + m, y0 + hauteur, z0 + d };
            p13 = new double[] { x0 + largeur - d / 2 - m, y0 + hauteur, z0 + d };
            p14 = new double[] { x0 + largeur - d / 2 - m, y0 + hauteur, z0 + d / 2 - m / 2 };
            p15 = new double[] { x0 + largeur - m / 2, y0 + hauteur, z0 + d / 2 - m / 2 };
            p16 = new double[] { x0 + largeur - m / 2, y0 + hauteur, z0 };

        } else {
            p1 = new double[] { x0, y0, z0 };
            p2 = new double[] { x0, y0, z0 + d / 2 - m / 2 };
            p3 = new double[] { x0 + d / 2 + m / 2, y0, z0 + d / 2 - m / 2 };
            p4 = new double[] { x0 + d / 2 + m / 2, y0, z0 + d };
            p5 = new double[] { x0 + largeur - d / 2 - m / 2, y0, z0 + d };
            p6 = new double[] { x0 + largeur - d / 2 - m / 2, y0, z0 + d / 2 - m / 2 };
            p7 = new double[] { x0 + largeur, y0, z0 + d / 2 - m / 2 };
            p8 = new double[] { x0 + largeur, y0, z0 };
            p9 = new double[] { x0, y0 + hauteur, z0 };
            p10 = new double[] { x0, y0 + hauteur, z0 + d / 2 - m / 2 };
            p11 = new double[] { x0 + d / 2 + m / 2, y0 + hauteur, z0 + d / 2 - m / 2 };
            p12 = new double[] { x0 + d / 2 + m / 2, y0 + hauteur, z0 + d };
            p13 = new double[] { x0 + largeur - d / 2 - m / 2, y0 + hauteur, z0 + d };
            p14 = new double[] { x0 + largeur - d / 2 - m / 2, y0 + hauteur, z0 + d / 2 - m / 2 };
            p15 = new double[] { x0 + largeur, y0 + hauteur, z0 + d / 2 - m / 2 };
            p16 = new double[] { x0 + largeur, y0 + hauteur, z0 };
        }

        // Top (2 faces, 4 triangles)
        triangles.add(new double[][] { p1, p2, p8 });
        triangles.add(new double[][] { p2, p7, p8 });
        triangles.add(new double[][] { p3, p4, p6 });
        triangles.add(new double[][] { p4, p5, p6 });

        // Bottom (2 faces, 4 triangles)
        triangles.add(new double[][] { p9, p10, p16 });
        triangles.add(new double[][] { p10, p15, p16 });
        triangles.add(new double[][] { p11, p12, p14 });
        triangles.add(new double[][] { p12, p13, p14 });

        // Front (1 face, 2 triangles)
        triangles.add(new double[][] { p9, p1, p16 });
        triangles.add(new double[][] { p1, p8, p16 });

        // Back (1 face, 2 triangles)
        triangles.add(new double[][] { p4, p13, p5 });
        triangles.add(new double[][] { p12, p13, p4 });

        // Faces (3 faces, 6 triangles) Rainure 1
        triangles.add(new double[][] { p10, p2, p9 });
        triangles.add(new double[][] { p2, p1, p9 });
        triangles.add(new double[][] { p12, p4, p11 });
        triangles.add(new double[][] { p4, p3, p11 });
        triangles.add(new double[][] { p10, p2, p11 });
        triangles.add(new double[][] { p2, p3, p11 });

        // Faces (3) rainure 2
        triangles.add(new double[][] { p13, p5, p14 });
        triangles.add(new double[][] { p5, p6, p14 });
        triangles.add(new double[][] { p15, p7, p16 });
        triangles.add(new double[][] { p7, p8, p16 });
        triangles.add(new double[][] { p14, p6, p15 });
        triangles.add(new double[][] { p6, p7, p15 });

        return triangles;
    }

    // bye old version

    public static TriangleMeshGroup buildWindow(double width, double height, Vector3D position, double frameWidth) {
        double x0 = position.x, y0 = position.y, z0 = position.z;

        // this.scene.addMeshes(meshes);
        RectCuboid fenetreFrame1 = new RectCuboid(new Vector3D(x0, y0, z0),
                new Vector3D(frameWidth, height, frameWidth));
        RectCuboid fenetreFrame2 = new RectCuboid(new Vector3D(x0 + width, y0, z0),
                new Vector3D(frameWidth, height, frameWidth));
        RectCuboid fenetreFrame3 = new RectCuboid(new Vector3D(x0, y0, z0),
                new Vector3D(width, frameWidth, frameWidth));
        RectCuboid fenetreFrame4 = new RectCuboid(new Vector3D(x0, y0 + height - frameWidth, z0),
                new Vector3D(width, frameWidth, frameWidth));
        RectCuboid fenetreFrame5 = new RectCuboid(new Vector3D(x0 + width / 2, y0, z0),
                new Vector3D(frameWidth, height, frameWidth));
        RectCuboid fenetreFrame6 = new RectCuboid(new Vector3D(x0, y0 + height / 2 - frameWidth / 2, z0),
                new Vector3D(width, frameWidth, frameWidth));
        RectCuboid fenetreVitre = new RectCuboid(
                new Vector3D(x0 + frameWidth / 2, y0 + frameWidth / 2 / 2, z0 + 1),
                new Vector3D(width - frameWidth / 2, height - frameWidth / 2, 1));

        TriangleMesh fenetreFrame = new TriangleMesh(new TriangleMesh[] {
                fenetreFrame1,
                fenetreFrame2,
                fenetreFrame3,
                fenetreFrame4,
                fenetreFrame5,
                fenetreFrame6
        });

        fenetreFrame.getMaterial().setColor(new Color(192, 192, 192));
        fenetreVitre.getMaterial().setColor(new Color(76, 144, 168));

        return new TriangleMeshGroup(new TriangleMesh[] {
                fenetreFrame,
                fenetreVitre
        });
    }

    public static TriangleMeshGroup buildDoor(double width, double height, Vector3D position, double frameWidth) {
        double x0 = position.x, y0 = position.y, z0 = position.z;

        RectCuboid doorFrame1 = new RectCuboid(new Vector3D(x0, y0, z0),
                new Vector3D(frameWidth, height, frameWidth));
        RectCuboid doorFrame2 = new RectCuboid(new Vector3D(x0 + width, y0, z0),
                new Vector3D(frameWidth, height, frameWidth));
        RectCuboid doorFrame3 = new RectCuboid(new Vector3D(x0, y0, z0),
                new Vector3D(width, frameWidth, frameWidth));
        RectCuboid doorFrame4 = new RectCuboid(new Vector3D(x0, y0 + height - frameWidth, z0),
                new Vector3D(width, frameWidth, frameWidth));
        RectCuboid doorFrame5 = new RectCuboid(new Vector3D(x0, y0 + height - frameWidth, z0),
                new Vector3D(width, frameWidth, frameWidth));
        RectCuboid doorFrame6 = new RectCuboid(
                new Vector3D(x0 + frameWidth / 2, y0 + frameWidth / 2, z0 + frameWidth / 2),
                new Vector3D(width - frameWidth / 2, height - frameWidth / 2, 1));
        RectCuboid doorFrame7 = new RectCuboid(new Vector3D(x0 + 1, y0 + height / 2, z0 - frameWidth),
                new Vector3D(2, 2, 5));
        RectCuboid doorFrame8 = new RectCuboid(new Vector3D(x0, y0 + height / 2 - 1, z0 - frameWidth - 3),
                new Vector3D(4, 4, 4));

        RectCuboid doorPanel = new RectCuboid(
                new Vector3D(x0 + frameWidth / 2, y0 + frameWidth / 2 / 2, z0 + 1),
                new Vector3D(width - frameWidth / 2, height - frameWidth / 2, 1));

        doorFrame1.getMaterial().setColor(new Color(139, 69, 19));
        doorFrame2.getMaterial().setColor(new Color(139, 69, 19));
        doorFrame3.getMaterial().setColor(new Color(139, 69, 19));
        doorFrame4.getMaterial().setColor(new Color(139, 69, 19));
        doorFrame5.getMaterial().setColor(new Color(139, 69, 19));
        doorFrame6.getMaterial().setColor(new Color(222, 184, 135));
        doorFrame7.getMaterial().setColor(Color.GRAY);
        doorFrame8.getMaterial().setColor(Color.GRAY);
        doorPanel.getMaterial().setColor(new Color(222, 184, 135));

        TriangleMesh doorFrame = new TriangleMesh(new TriangleMesh[] {
                doorFrame1,
                doorFrame2,
                doorFrame3,
                doorFrame4,
                doorFrame5
        });

        doorFrame.getMaterial().setColor(new Color(139, 69, 19));

        TriangleMeshGroup group = new TriangleMeshGroup(new TriangleMesh[] {
                // piece1,
                doorFrame,
                doorPanel,
                doorFrame6,
                doorFrame7,
                doorFrame8
        });

        // group = group.translate(group.getCenter().multiply(-1));
        return group;
    }

    public static TriangleMeshGroup buildRallongeVertical(double largeur, double longueur, double depth, double angle,
            double m, Vector3D position) {
        TriangleMeshGroup rallongeVertGroup = new TriangleMeshGroup();

        double x0 = position.x;
        double y0 = position.y;
        double z0 = position.z;

        double a = (depth / 2) / Math.cos(Math.toRadians(angle)); //depth / 2 * Math.cos(Math.toRadians(angle));
        double b = depth / 2 * Math.tan(Math.toRadians(angle));

        System.out.println((a - b) + " " + (b - a));
        double hauteur = (longueur * Math.tan(Math.toRadians(angle)));

        double[] p1 = new double[] { x0, y0, z0 };
        double[] p2 = new double[] { x0, y0 + b, z0 + depth / 2 - m / 2 };
        double[] p3 = new double[] { x0 + largeur, y0 + b, z0 + depth / 2 - m / 2 };
        double[] p4 = new double[] { x0 + largeur, y0, z0 };

        double[] p5 = new double[] { x0 + depth / 2 + m / 2, y0 + b + b, z0 + depth / 2 - m / 2 };
        double[] p6 = new double[] { x0 + depth / 2 + m / 2, y0 + b + b + b, z0 + depth };
        double[] p7 = new double[] { x0 + largeur - depth / 2 - m / 2, y0 + b + b + b, z0 + depth };
        double[] p8 = new double[] { x0 + largeur - depth / 2 - m / 2, y0 + b + b, z0 + depth / 2 - m / 2 };

        double[] p9 = new double[] { x0, y0 + hauteur, z0 };
        double[] p10 = new double[] { x0, y0 + hauteur, z0 + depth / 2 - m / 2 };
        double[] p11 = new double[] { x0 + depth / 2 + m / 2, y0 + hauteur, z0 + depth / 2 - m / 2 };
        double[] p12 = new double[] { x0 + depth / 2 + m / 2, y0 + hauteur, z0 + depth };
        double[] p13 = new double[] { x0 + largeur - depth / 2 - m / 2, y0 + hauteur, z0 + depth };
        double[] p14 = new double[] { x0 + largeur - depth / 2 - m / 2, y0 + hauteur, z0 + depth / 2 - m / 2 };
        double[] p15 = new double[] { x0 + largeur, y0 + hauteur, z0 + depth / 2 - m / 2 };
        double[] p16 = new double[] { x0 + largeur, y0 + hauteur, z0 };

        List<double[][]> triangles = new ArrayList<double[][]>();

        triangles.add(new double[][] { p1, p2, p3 });
        triangles.add(new double[][] { p1, p3, p4 });

        triangles.add(new double[][] { p1, p4, p9 });
        triangles.add(new double[][] { p4, p16, p9 });

        triangles.add(new double[][] { p5, p6, p7 });
        triangles.add(new double[][] { p5, p7, p8 });

        triangles.add(new double[][] { p2, p5, p8 });
        triangles.add(new double[][] { p2, p8, p3 });

        triangles.add(new double[][] { p7, p12, p13 });
        triangles.add(new double[][] { p6, p12, p7 });

        triangles.add(new double[][] { p13, p8, p7 });
        triangles.add(new double[][] { p13, p14, p8 });

        triangles.add(new double[][] { p14, p15, p8 });
        triangles.add(new double[][] { p15, p3, p8 });

        triangles.add(new double[][] { p12, p6, p5 });
        triangles.add(new double[][] { p12, p5, p11 });

        triangles.add(new double[][] { p10, p5, p2 });
        triangles.add(new double[][] { p10, p11, p5 });

        triangles.add(new double[][] { p9, p10, p2 });
        triangles.add(new double[][] { p9, p2, p1 });

        triangles.add(new double[][] { p15, p16, p3 });
        triangles.add(new double[][] { p4, p3, p16 });

        triangles.add(new double[][] { p9, p10, p16 });
        triangles.add(new double[][] { p10, p15, p16 });
        triangles.add(new double[][] { p11, p12, p14 });
        triangles.add(new double[][] { p12, p13, p14 });

        TriangleMesh mesh = TriangleMesh.fromDoubleList(triangles);
        mesh = mesh.translate(new Vector3D(0, (b - a) - 1, 0));
        rallongeVertGroup.addMesh(mesh);

        return rallongeVertGroup;
    };

    public static TriangleMeshGroup buildPignonGauche(double largeur, double depth, double angle, Vector3D position) {
        TriangleMeshGroup pignonGroup = new TriangleMeshGroup();

        double height = largeur * Math.tan(Math.toRadians(angle)) - depth / 2 * Math.tan(Math.toRadians(angle));
        
        largeur = largeur - depth;

        double x0 = position.x;
        double y0 = position.y;
        double z0 = position.z;

        double diffHeight = depth / 2 * Math.tan(Math.toRadians(angle));

        double[] p1 = new double[] { x0, y0, z0 };
        double[] p2 = new double[] { x0, y0 - height, z0 };
        double[] p3 = new double[] { x0 + largeur, y0, z0 };

        double[] p4 = new double[] { x0, y0, z0 + depth / 2 };
        double[] p5 = new double[] { x0, y0 - height + diffHeight, z0 + depth / 2 };
        double[] p6 = new double[] { x0, y0 - height, z0 + depth / 2 };

        double[] p7 = new double[] { x0 + depth / 2, y0, z0 + depth / 2 };
        double[] p8 = new double[] { x0 + depth / 2, y0 - ((largeur - depth) * Math.tan(Math.toRadians(angle))), z0 + depth / 2 };
        double[] p9 = new double[] { x0 + depth / 2, y0 - ((largeur - depth / 2) * Math.tan(Math.toRadians(angle))), z0 + depth / 2 };

        double[] p10 = new double[] { x0 + depth / 2, y0, z0 + depth };
        double[] p11 = new double[] { x0 + depth / 2, y0 - ((largeur - depth) * Math.tan(Math.toRadians(angle))), z0 + depth };
        
        double[] p12 = new double[] { x0 + largeur, y0, z0 + depth / 2 };
        double[] p13 = new double[] { x0 + largeur - depth / 2, y0, z0 + depth / 2};
        double[] p14 = new double[] { x0 + largeur - depth / 2, y0, z0 + depth };
        
        List<double[][]> triangles = new ArrayList<double[][]>();
        
        triangles.add(new double[][] { p1, p3, p2 });

        triangles.add(new double[][] { p1, p2, p4 });
        triangles.add(new double[][] { p2, p6, p4 });

        triangles.add(new double[][] { p4, p5, p7 });
        triangles.add(new double[][] { p7, p5, p8 });

        triangles.add(new double[][] { p9, p5, p6 });
        triangles.add(new double[][] { p5, p9, p8 });

        triangles.add(new double[][] { p7, p10, p8 });
        triangles.add(new double[][] { p10, p11, p8  });
        
        triangles.add(new double[][] { p2, p6, p3 });
        triangles.add(new double[][] { p3, p6, p12 });
        
        triangles.add(new double[][] { p1, p12, p3 });
        triangles.add(new double[][] { p1, p4, p12 });

        triangles.add(new double[][] { p7, p10, p13 });
        triangles.add(new double[][] { p10, p14, p13 });

        triangles.add(new double[][] { p8, p14, p11 });
        triangles.add(new double[][] { p8, p13, p14 });
        triangles.add(new double[][] { p10, p14, p11 });

        triangles.add(new double[][] { p13, p8, p12 });
        triangles.add(new double[][] { p8, p9, p12 });

        TriangleMesh pignonMesh = TriangleMesh.fromDoubleList(triangles);
        pignonGroup.addMesh(pignonMesh);
        return pignonGroup;
    }

    public static TriangleMeshGroup buildPignonDroite(double largeur, double depth, double angle, Vector3D position) {
        TriangleMeshGroup pignonGroup = new TriangleMeshGroup();

        double height = largeur * Math.tan(Math.toRadians(angle)) - depth / 2 * Math.tan(Math.toRadians(angle));
        
        largeur = largeur - depth;

        double x0 = position.x;
        double y0 = position.y;
        double z0 = position.z;

        double diffHeight = depth / 2 * Math.tan(Math.toRadians(angle));

        double[] p1 = new double[] { x0, y0, z0 };
        double[] p2 = new double[] { x0, y0 - height, z0 };
        double[] p3 = new double[] { x0 + largeur, y0, z0 };

        double[] p4 = new double[] { x0, y0, z0 - depth / 2 };
        double[] p5 = new double[] { x0, y0 - height + diffHeight, z0 - depth / 2 };
        double[] p6 = new double[] { x0, y0 - height, z0 - depth / 2 };

        double[] p7 = new double[] { x0 + depth / 2, y0, z0 - depth / 2 };
        double[] p8 = new double[] { x0 + depth / 2, y0 - ((largeur - depth) * Math.tan(Math.toRadians(angle))), z0 - depth / 2 };
        double[] p9 = new double[] { x0 + depth / 2, y0 - ((largeur - depth / 2) * Math.tan(Math.toRadians(angle))), z0 - depth / 2 };

        double[] p10 = new double[] { x0 + depth / 2, y0, z0 - depth };
        double[] p11 = new double[] { x0 + depth / 2, y0 - ((largeur - depth) * Math.tan(Math.toRadians(angle))), z0 - depth };
        
        double[] p12 = new double[] { x0 + largeur, y0, z0 - depth / 2 };
        double[] p13 = new double[] { x0 + largeur - depth / 2, y0, z0 - depth / 2};
        double[] p14 = new double[] { x0 + largeur - depth / 2, y0, z0 - depth };
        
        List<double[][]> triangles = new ArrayList<double[][]>();
        
        triangles.add(new double[][] { p1, p3, p2 });

        triangles.add(new double[][] { p1, p2, p4 });
        triangles.add(new double[][] { p2, p6, p4 });

        triangles.add(new double[][] { p4, p5, p7 });
        triangles.add(new double[][] { p7, p5, p8 });

        triangles.add(new double[][] { p9, p5, p6 });
        triangles.add(new double[][] { p5, p9, p8 });

        triangles.add(new double[][] { p7, p10, p8 });
        triangles.add(new double[][] { p10, p11, p8  });
        
        triangles.add(new double[][] { p2, p6, p3 });
        triangles.add(new double[][] { p3, p6, p12 });
        
        triangles.add(new double[][] { p1, p12, p3 });
        triangles.add(new double[][] { p1, p4, p12 });

        triangles.add(new double[][] { p7, p10, p13 });
        triangles.add(new double[][] { p10, p14, p13 });

        triangles.add(new double[][] { p8, p14, p11 });
        triangles.add(new double[][] { p8, p13, p14 });
        triangles.add(new double[][] { p10, p14, p11 });

        triangles.add(new double[][] { p13, p8, p12 });
        triangles.add(new double[][] { p8, p9, p12 });

        TriangleMesh pignonMesh = TriangleMesh.fromDoubleList(triangles);
        pignonGroup.addMesh(pignonMesh);
        return pignonGroup;
    }

    @FunctionalInterface
    public static interface Fn {
        public double apply(double x);
    }
    
    public static TriangleMeshGroup buildPanneauToit2(double largeur, double longueur, double depth, double angle, double marge, Vector3D position) {
        TriangleMeshGroup panneauToitGroup = new TriangleMeshGroup();

        double x0 = position.x;
        double y0 = position.y;
        double z0 = position.z;


        double a = (depth / 2) / Math.cos(Math.toRadians(angle)); //depth / 2 * Math.cos(Math.toRadians(angle));
        double b = depth / 2 * Math.tan(Math.toRadians(angle));
        
        double height = longueur * Math.tan(Math.toRadians(angle)) + a;

        double p1[] = new double[] { x0, y0, z0 };
        double p2[] = new double[] { x0 + largeur, y0, z0 };
        double p3[] = new double[] { x0 + largeur, y0 + a, z0 };
        double p4[] = new double[] { x0, y0 + a, z0 };

        double p5[] = new double[] { x0 + depth / 2, y0 + a + b, z0 + depth / 2 };
        double p6[] = new double[] { x0 + largeur - depth / 2, y0 + a + b, z0 + depth / 2 };

        double p7[] = new double[] { x0 + depth / 2, y0 + a + b + b, z0 + depth / 2 };
        double p8[] = new double[] { x0 + largeur - depth / 2, y0 + a + b + b, z0 + depth / 2 };

        double p9[] = new double[] { x0 + depth / 2, y0 + height, z0 + longueur - depth };
        double p10[] = new double[] { x0 + largeur - depth / 2, y0 + height, z0 + longueur - depth };

        double p11[] = new double[] { x0 + depth / 2, y0 + height, z0 + longueur - depth / 2 };
        double p12[] = new double[] { x0 + largeur - depth / 2, y0 + height, z0 + longueur - depth / 2 };

        double p13[] = new double[] { x0, y0 + height, z0 + longueur - depth / 2 };
        double p14[] = new double[] { x0 + largeur, y0 + height, z0 + longueur - depth / 2 };

        double p15[] = new double[] { x0, y0 + height, z0 + longueur };
        double p16[] = new double[] { x0 + largeur, y0 + height, z0 + longueur };

        double p17[] = new double[] { x0, y0 + height - a + b, z0 + longueur };
        double p18[] = new double[] { x0 + largeur, y0 + height - a + b, z0 + longueur };
        

        List<double[][]> triangles = new ArrayList<double[][]>();

        triangles.add(new double[][] { p1, p2, p3 });
        triangles.add(new double[][] { p1, p3, p4 });

        triangles.add(new double[][] { p5, p4, p6 });
        triangles.add(new double[][] { p4, p3, p6 });

        triangles.add(new double[][] { p5, p6, p7 });
        triangles.add(new double[][] { p6, p8, p7 });

        triangles.add(new double[][] { p7, p8, p9 });
        triangles.add(new double[][] { p8, p10, p9 });

        triangles.add(new double[][] { p9, p10, p11 });
        triangles.add(new double[][] { p10, p12, p11 });

        triangles.add(new double[][] { p13, p14, p15 });
        triangles.add(new double[][] { p15, p14, p16 });

        triangles.add(new double[][] { p15, p16, p17 });
        triangles.add(new double[][] { p16, p18, p17 });

        triangles.add(new double[][] { p1, p2, p17 });
        triangles.add(new double[][] { p2, p18, p17 });

        triangles.add(new double[][] { p4, p5, p11 });
        triangles.add(new double[][] { p13, p4, p11 });

        triangles.add(new double[][] { p3, p12, p6 });
        triangles.add(new double[][] { p3, p14, p12 });

        triangles.add(new double[][] { p1, p17, p15 });
        triangles.add(new double[][] { p4, p1, p15 });
        triangles.add(new double[][] { p13, p4, p15 });

        triangles.add(new double[][] { p2, p18, p16 });
        triangles.add(new double[][] { p2, p16, p14 });
        triangles.add(new double[][] { p3, p2, p14 });

        triangles.add(new double[][] { p9, p11, p5 });
        triangles.add(new double[][] { p9, p5, p7 });

        triangles.add(new double[][] { p10, p12, p6 });
        triangles.add(new double[][] { p10, p6, p8 });


        TriangleMesh panneauToitMesh = TriangleMesh.fromDoubleList(triangles);
        panneauToitMesh = panneauToitMesh.translate(new Vector3D(0, -a, 0));
        panneauToitGroup.addMesh(panneauToitMesh);
        return panneauToitGroup;

    }

    public static TriangleMeshGroup buildPanneauToit(double width, double longueur, double depth, double angle, double marge, Vector3D position) {
        TriangleMeshGroup panneauToitGroup = new TriangleMeshGroup();

        double height = (longueur) * Math.tan(Math.toRadians(angle));

        double x0 = position.x;
        double y0 = position.y;
        double z0 = position.z;

        double a = depth / 2 * Math.tan(Math.toRadians(angle));
        double b = Math.sqrt(Math.pow(depth / 2, 2) + Math.pow(a, 2)) * 2;
        double b1 = b/2;
        double b2 = depth/2 * Math.sin(Math.toRadians(angle));

        double[] p1 = new double[] { x0, y0, z0 };
        double[] p2 = new double[] { x0 + width, y0, z0 };
        double[] p3 = new double[] { x0 + width, y0 + b1, z0 };
        double[] p4 = new double[] { x0, y0 + b1, z0 };

        double[] p5 = new double[] { x0 + depth / 2, y0 + b1 + a, z0 + depth / 2 };
        double[] p6 = new double[] { x0 + width - depth / 2, y0 + b1 + a, z0 + depth / 2 };
        
        double[] p7 = new double[] { x0 + depth / 2, y0 + b1 + b2 + a, z0 + depth / 2 };
        double[] p8 = new double[] { x0 + width - depth / 2, y0 + b1 + b2 + a, z0 + depth / 2 };

        double[] p9 = new double[] { x0, y0 + height, z0 + longueur - depth / 2 };
        double[] p10 = new double[] { x0 + width, y0 + height, z0 + longueur - depth / 2 };

        double m = (p9[1] - p7[1]) / (p9[2] - p7[2]);
        Fn fn = new Fn() {
            @Override
            public double apply(double x) {
                return m * (x - p9[2]) + p9[1];
            }
        };

        double[] p11 = new double[] { x0, y0 + fn.apply(z0 + longueur) - b1 - b2 + a, z0 + longueur };
        double[] p12 = new double[] { x0 + width, + fn.apply(z0 + longueur) - b1 - b2 + a, z0 + longueur };

        double[] p13 = new double[] { x0, y0 + height, z0 + longueur };
        double[] p14 = new double[] { x0 + width, y0 + height, z0 + longueur };

        double[] p17 = new double[] { x0 + depth / 2, y0 + height, z0 + longueur - depth };
        double[] p18 = new double[] { x0 + width -  depth / 2, y0 + height, z0 + longueur - depth };

        double[] p19 = new double[] { x0 + depth / 2, y0 + height, z0 + longueur - depth / 2 };
        double[] p20 = new double[] { x0 + width - depth / 2, y0 + height, z0 + longueur - depth / 2 };

        double[] p21 = new double[] { x0, y0 + b1 + a, z0 + depth / 2 };
        double[] p22 = new double[] { x0 + width, y0 + b1 + a, z0 + depth / 2 };

        List<double[][]> triangles = new ArrayList<double[][]>();
        

        triangles.add(new double[][] { p1, p2, p3 });
        triangles.add(new double[][] { p1, p3, p4 });

        triangles.add(new double[][] { p5, p4, p6 });
        triangles.add(new double[][] { p4, p3, p6 });

        triangles.add(new double[][] { p5, p6, p7 });
        triangles.add(new double[][] { p6, p8, p7 });

        // Top
        triangles.add(new double[][] { p2, p11, p12 });
        triangles.add(new double[][] { p1, p11, p2 });

        triangles.add(new double[][] { p9, p10, p13 });
        triangles.add(new double[][] { p13, p10, p14 });

        triangles.add(new double[][] {p11, p12, p13});
        triangles.add(new double[][] {p13, p12, p14});


        triangles.add(new double[][] { p17, p18, p19 });
        triangles.add(new double[][] { p18, p20, p19 });

        triangles.add(new double[][] { p19, p20, p9 });
        triangles.add(new double[][] { p9, p20, p10 });

        // Bottom
        triangles.add(new double[][] { p7, p8, p17 });
        triangles.add(new double[][] { p17, p8, p18 });

        triangles.add(new double[][] { p4, p5, p21 });
        triangles.add(new double[][] { p6, p3, p22 });

        triangles.add(new double[][] { p9, p21, p19 });
        triangles.add(new double[][] { p19, p21, p5 });

        triangles.add(new double[][] { p22, p10, p20 });
        triangles.add(new double[][] { p6, p22, p20 });

        triangles.add(new double[][] { p13, p9, p11 });
        triangles.add(new double[][] { p14, p10, p12 });

        triangles.add(new double[][] { p4, p1, p21 });
        triangles.add(new double[][] { p3, p2, p22 });

        triangles.add(new double[][] { p11, p9, p21 });
        triangles.add(new double[][] { p12, p10, p22 });

        triangles.add(new double[][] { p1, p11, p21 });
        triangles.add(new double[][] { p2, p12, p22 });

        triangles.add(new double[][] { p17, p7, p5 });
        triangles.add(new double[][] { p19, p17, p5 });

        triangles.add(new double[][] { p18, p8, p6 });
        triangles.add(new double[][] { p20, p18, p6 });


        TriangleMesh pignonMesh = TriangleMesh.fromDoubleList(triangles);
        pignonMesh.getMaterial().setColor(Color.GRAY);
        panneauToitGroup.addMesh(pignonMesh);
        return panneauToitGroup;
    }

    public static class RectPlane2D {
        public List<double[][]> triangles = new ArrayList<double[][]>();
        int axis = 0; // 0 = x, 1 = y, 2 = z
        double[] position;
        double[] dimension;

        public RectPlane2D(double[] position, double[] dimension, int axis) {
            this.position = position;
            this.dimension = dimension;
            this.axis = axis;

            if (axis == 0) {
                triangles.add(new double[][] {
                        { position[0], position[1], 0 },
                        { position[0] + dimension[0], position[1] + dimension[1], 0 },
                        { position[0] + dimension[0], position[1], 0 }
                });

                triangles.add(new double[][] {
                        { position[0], position[1], 0 },
                        { position[0], position[1] + dimension[1], 0 },
                        { position[0] + dimension[0], position[1] + dimension[1], 0 },
                });
            } else if (axis == 1) {
                triangles.add(new double[][] {
                        { position[0], 0, position[1] },
                        { position[0] + dimension[0], 0, position[1] },
                        { position[0] + dimension[0], 0, position[1] + dimension[1] }
                });

                triangles.add(new double[][] {
                        { position[0], 0, position[1] },
                        { position[0] + dimension[0], 0, position[1] + dimension[1] },
                        { position[0], 0, position[1] + dimension[1] }
                });
            } else if (axis == 2) {
                triangles.add(new double[][] {
                        { position[0], position[1], 0 },
                        { position[0] + dimension[0], position[1], 0 },
                        { position[0] + dimension[0], position[1] + dimension[1], 0 }
                });

                triangles.add(new double[][] {
                        { position[0], position[1], 0 },
                        { position[0] + dimension[0], position[1] + dimension[1], 0 },
                        { position[0], position[1] + dimension[1], 0 }
                });
            } else {
                throw new IllegalArgumentException("Invalid axis");
            }
        }

        public RectPlane2D remove(double[] position, double[] dimension) {
            RectPlane2D rect1 = new RectPlane2D(new double[] { this.position[0], this.position[1] },
                    new double[] { this.dimension[0], position[1] }, this.axis);
            RectPlane2D rect2 = new RectPlane2D(new double[] { this.position[0], position[1] },
                    new double[] { position[0] - this.position[0], dimension[1] }, this.axis);
            RectPlane2D rect3 = new RectPlane2D(new double[] { position[0] + dimension[0], position[1] },
                    new double[] { this.dimension[0] - (position[0] - this.position[0])
                            - dimension[0], dimension[1] },
                    this.axis);
            RectPlane2D rect4 = new RectPlane2D(
                    new double[] { this.position[0], position[1] + dimension[1] },
                    new double[] { this.dimension[0],
                            this.dimension[1] - (position[1] - this.position[1])
                                    - dimension[1] },
                    this.axis);

            rect1.triangles.addAll(rect2.triangles);
            rect1.triangles.addAll(rect3.triangles);
            rect1.triangles.addAll(rect4.triangles);

            return rect1;
        }
    }

    public static class MurTriangleMeshGroup extends TriangleMeshGroup {
        Chalet.ChaletDTO chaletDTO;
        final TypeMur typeMur;
        List<Accessoire.AccessoireDTO> accessoireDTOs = new ArrayList<Accessoire.AccessoireDTO>();
        TriangleMesh meshBrut;
        TriangleMesh meshFini;
        TriangleMeshGroup meshRetraits;
        List<TriangleMeshGroup> meshAccessoires = new ArrayList<TriangleMeshGroup>();

        boolean truncate = false;
        OutputType activeOutput = OutputType.Fini;
        boolean computeHoles = false;

        public MurTriangleMeshGroup(Chalet.ChaletDTO chaletDTO, TypeMur typeMur,
                List<Accessoire.AccessoireDTO> accessoireDTOs) {
            this(chaletDTO, typeMur, accessoireDTOs, false, false);
        }

        public MurTriangleMeshGroup(Chalet.ChaletDTO chaletDTO, TypeMur typeMur,
                List<Accessoire.AccessoireDTO> accessoireDTOs, boolean truncate, boolean computeHoles) {
            this.typeMur = typeMur;
            this.chaletDTO = chaletDTO;
            this.truncate = truncate;
            this.accessoireDTOs = accessoireDTOs;
            this.computeHoles = computeHoles;

            this.setDraggable(false);
            this.rebuild();
        }

        public List<TriangleMeshGroup> getAccessoiresMeshes() {
            return meshAccessoires;
        }

        public boolean getComputeHoles() {
            return computeHoles;
        }

        public TypeMur getTypeMur() {
            return typeMur;
        }

        public OutputType getActiveOutput() {
            return activeOutput;
        }

        public TriangleMesh getMeshBrut() {
            return meshBrut;
        }

        public TriangleMesh getMeshFini() {
            return meshFini;
        }

        public TriangleMeshGroup getMeshRetraits() {
            return meshRetraits;
        }

        public List<Accessoire.AccessoireDTO> getAccessoireDTOs() {
            return accessoireDTOs;
        }

        public void setChaletDTO(Chalet.ChaletDTO chaletDTO) {
            this.chaletDTO = chaletDTO;
        }

        public void setComputeHoles(boolean computeHoles) {
            this.computeHoles = computeHoles;
            this.rebuild();
        }

        private void buildBrut() {
            double largeur = this.typeMur == TypeMur.Facade || this.typeMur == TypeMur.Arriere
                    ? this.chaletDTO.largeur
                    : this.chaletDTO.longueur;
            Vector3D position = new Vector3D(0, 0, 0);
            Vector3D dimension = new Vector3D(largeur, this.chaletDTO.hauteur, this.chaletDTO.epaisseurMur);

            Vector3D p1 = new Vector3D(position.x, position.y, position.z);
            Vector3D p2 = new Vector3D(position.x + dimension.x, position.y, position.z);
            Vector3D p3 = new Vector3D(position.x + dimension.x, position.y + dimension.y, position.z);
            Vector3D p4 = new Vector3D(position.x, position.y + dimension.y, position.z);
            Vector3D p5 = new Vector3D(position.x, position.y, position.z + dimension.z);
            Vector3D p6 = new Vector3D(position.x + dimension.x, position.y, position.z + dimension.z);
            Vector3D p7 = new Vector3D(position.x + dimension.x, position.y + dimension.y,
                    position.z + dimension.z);
            Vector3D p8 = new Vector3D(position.x, position.y + dimension.y, position.z + dimension.z);

            List<Triangle> triangles = new ArrayList<Triangle>();

            // Front
            triangles.add(new Triangle(p1, p2, p3));
            triangles.add(new Triangle(p1, p3, p4));

            // Back
            triangles.add(new Triangle(p5, p7, p6));
            triangles.add(new Triangle(p5, p8, p7));

            // Left
            triangles.add(new Triangle(p1, p5, p8));
            triangles.add(new Triangle(p1, p8, p4));

            // Right
            triangles.add(new Triangle(p2, p6, p7));
            triangles.add(new Triangle(p2, p7, p3));

            // Top
            triangles.add(new Triangle(p4, p8, p7));
            triangles.add(new Triangle(p4, p7, p3));

            // Bottom
            triangles.add(new Triangle(p1, p2, p6));
            triangles.add(new Triangle(p1, p6, p5));

            TriangleMesh meshBrut = new TriangleMesh(triangles);

            this.meshBrut = meshBrut;
        }

        private void buildFini() {
            double[] position = new double[] { 0, 0, 0 };
            double x0 = position[0], y0 = position[1], z0 = position[2];
            double largeur = this.typeMur == TypeMur.Facade || this.typeMur == TypeMur.Arriere
                    ? this.chaletDTO.largeur
                    : this.chaletDTO.longueur;
            double hauteur = this.chaletDTO.hauteur;
            double d = this.chaletDTO.epaisseurMur;
            double m = this.chaletDTO.margeSupplementaireRetrait;

            // Si truncate == vrai, marge pleine retirée sur une partie de la rainure
            if (this.truncate) {
                largeur -= d;
            }

            double[] p1 = new double[] { x0, y0, z0 };
            double[] p2 = new double[] { x0, y0, z0 + d / 2 + m };
            double[] p3 = new double[] { x0 + d / 2 - m, y0, z0 + d / 2 + m };
            double[] p4 = new double[] { x0 + d / 2 - m, y0, z0 + d };
            double[] p5 = new double[] { x0 + largeur - d / 2 + m, y0, z0 + d };
            double[] p6 = new double[] { x0 + largeur - d / 2 + m, y0, z0 + d / 2 + m };
            double[] p7 = new double[] { x0 + largeur, y0, z0 + d / 2 + m };
            double[] p8 = new double[] { x0 + largeur, y0, z0 };

            double[] p9 = new double[] { x0, y0 + hauteur, z0 };
            double[] p10 = new double[] { x0, y0 + hauteur, z0 + d / 2 + m };
            double[] p11 = new double[] { x0 + d / 2 - m, y0 + hauteur, z0 + d / 2 + m };
            double[] p12 = new double[] { x0 + d / 2 - m, y0 + hauteur, z0 + d };
            double[] p13 = new double[] { x0 + largeur - d / 2 + m, y0 + hauteur, z0 + d };
            double[] p14 = new double[] { x0 + largeur - d / 2 + m, y0 + hauteur, z0 + d / 2 + m };
            double[] p15 = new double[] { x0 + largeur, y0 + hauteur, z0 + d / 2 + m };
            double[] p16 = new double[] { x0 + largeur, y0 + hauteur, z0 };

            if (truncate) {
                p1 = new double[] { x0 + m / 2, y0, z0 };
                p2 = new double[] { x0 + m / 2, y0, z0 + d / 2 - m / 2 };
                p3 = new double[] { x0 + d / 2 + m, y0, z0 + d / 2 - m / 2 };
                p4 = new double[] { x0 + d / 2 + m, y0, z0 + d };
                p5 = new double[] { x0 + largeur - d / 2 - m, y0, z0 + d };
                p6 = new double[] { x0 + largeur - d / 2 - m, y0, z0 + d / 2 - m / 2 };
                p7 = new double[] { x0 + largeur - m / 2, y0, z0 + d / 2 - m / 2 };
                p8 = new double[] { x0 + largeur - m / 2, y0, z0 };
                p9 = new double[] { x0 + m / 2, y0 + hauteur, z0 };
                p10 = new double[] { x0 + m / 2, y0 + hauteur, z0 + d / 2 - m / 2 };
                p11 = new double[] { x0 + d / 2 + m, y0 + hauteur, z0 + d / 2 - m / 2 };
                p12 = new double[] { x0 + d / 2 + m, y0 + hauteur, z0 + d };
                p13 = new double[] { x0 + largeur - d / 2 - m, y0 + hauteur, z0 + d };
                p14 = new double[] { x0 + largeur - d / 2 - m, y0 + hauteur, z0 + d / 2 - m / 2 };
                p15 = new double[] { x0 + largeur - m / 2, y0 + hauteur, z0 + d / 2 - m / 2 };
                p16 = new double[] { x0 + largeur - m / 2, y0 + hauteur, z0 };

            } else {
                p1 = new double[] { x0, y0, z0 };
                p2 = new double[] { x0, y0, z0 + d / 2 - m / 2 };
                p3 = new double[] { x0 + d / 2 + m / 2, y0, z0 + d / 2 - m / 2 };
                p4 = new double[] { x0 + d / 2 + m / 2, y0, z0 + d };
                p5 = new double[] { x0 + largeur - d / 2 - m / 2, y0, z0 + d };
                p6 = new double[] { x0 + largeur - d / 2 - m / 2, y0, z0 + d / 2 - m / 2 };
                p7 = new double[] { x0 + largeur, y0, z0 + d / 2 - m / 2 };
                p8 = new double[] { x0 + largeur, y0, z0 };
                p9 = new double[] { x0, y0 + hauteur, z0 };
                p10 = new double[] { x0, y0 + hauteur, z0 + d / 2 - m / 2 };
                p11 = new double[] { x0 + d / 2 + m / 2, y0 + hauteur, z0 + d / 2 - m / 2 };
                p12 = new double[] { x0 + d / 2 + m / 2, y0 + hauteur, z0 + d };
                p13 = new double[] { x0 + largeur - d / 2 - m / 2, y0 + hauteur, z0 + d };
                p14 = new double[] { x0 + largeur - d / 2 - m / 2, y0 + hauteur, z0 + d / 2 - m / 2 };
                p15 = new double[] { x0 + largeur, y0 + hauteur, z0 + d / 2 - m / 2 };
                p16 = new double[] { x0 + largeur, y0 + hauteur, z0 };
            }

            List<double[][]> triangles = new ArrayList<>();

            // Top (2 faces, 4 triangles)
            triangles.add(new double[][] { p1, p2, p8 });
            triangles.add(new double[][] { p2, p7, p8 });
            triangles.add(new double[][] { p3, p4, p6 });
            triangles.add(new double[][] { p4, p5, p6 });

            // Bottom (2 faces, 4 triangles)
            triangles.add(new double[][] { p9, p10, p16 });
            triangles.add(new double[][] { p10, p15, p16 });
            triangles.add(new double[][] { p11, p12, p14 });
            triangles.add(new double[][] { p12, p13, p14 });

            // Front (1 face, 2 triangles)
            // triangles.add(new double[][] { p9, p1, p16 });
            // triangles.add(new double[][] { p1, p8, p16 });

            // Back (1 face, 2 triangles)
            // triangles.add(new double[][] { p4, p13, p5 });
            // triangles.add(new double[][] { p12, p13, p4 });

            double faceWidth = p16[0] - p9[0];
            double backWidth = p13[0] - p4[0];

            List<Rectangle2D> holes = new ArrayList<>();
            if (this.computeHoles) {
                for (Accessoire.AccessoireDTO accessoireDTO : this.accessoireDTOs) {
                    double x = largeur - accessoireDTO.dimensions[0] - accessoireDTO.position[0];
                    if (truncate) {
                        x += d / 2;
                    }
                    double y = accessoireDTO.position[1];
                    double w = accessoireDTO.dimensions[0];
                    double h = accessoireDTO.dimensions[1];

                    holes.add(new java.awt.geom.Rectangle2D.Double(x, y, w, h));
                }
            }

            RectWithHoles faceRect = new PanelHelper.RectWithHoles(p1[0], p1[1], faceWidth, hauteur, holes);
            RectWithHoles backRect = new PanelHelper.RectWithHoles(p4[0], p4[1], backWidth, hauteur, holes);

            triangles.addAll(faceRect.getTriangles(0));
            triangles.addAll(backRect.getTriangles((int) d));

            // Faces (3 faces, 6 triangles) Rainure 1
            triangles.add(new double[][] { p10, p2, p9 });
            triangles.add(new double[][] { p2, p1, p9 });
            triangles.add(new double[][] { p12, p4, p11 });
            triangles.add(new double[][] { p4, p3, p11 });
            triangles.add(new double[][] { p10, p2, p11 });
            triangles.add(new double[][] { p2, p3, p11 });

            // Faces (3) rainure 2
            triangles.add(new double[][] { p13, p5, p14 });
            triangles.add(new double[][] { p5, p6, p14 });
            triangles.add(new double[][] { p15, p7, p16 });
            triangles.add(new double[][] { p7, p8, p16 });
            triangles.add(new double[][] { p14, p6, p15 });
            triangles.add(new double[][] { p6, p7, p15 });

            TriangleMesh meshFini = TriangleMesh.fromDoubleList(triangles);
            this.meshFini = meshFini;
        }

        private void buildRetraits() {
            double largeur = this.typeMur == TypeMur.Facade || this.typeMur == TypeMur.Arriere ? this.chaletDTO.largeur
                    : this.chaletDTO.longueur;
            double hauteur = this.chaletDTO.hauteur;
            double d = this.chaletDTO.epaisseurMur;
            double m = this.chaletDTO.margeSupplementaireRetrait / 2;

            TriangleMeshGroup meshRetraits = new TriangleMeshGroup(new TriangleMesh[] {});

            for (Accessoire.AccessoireDTO accessoireDTO : this.accessoireDTOs) {
                // System.out.println("Accessoire : " + accessoireDTO.accessoireId);

                double x0 = largeur - accessoireDTO.position[0] - accessoireDTO.dimensions[0];
                // if (truncate) {
                // x0 -= d / 2;
                // }

                double y0 = accessoireDTO.position[1] - hauteur;
                double z0 = 0;
                double largeurAcc = accessoireDTO.dimensions[0];
                double hauteurAcc = accessoireDTO.dimensions[1];
                double dAcc = this.chaletDTO.epaisseurMur;

                TriangleMesh rectCuboid = new RectCuboid(new Vector3D(x0, y0, z0),
                        new Vector3D(largeurAcc, hauteurAcc, dAcc));

                rectCuboid.setSelectable(true);
                rectCuboid.setDraggable(true);
                rectCuboid.ID = accessoireDTO.accessoireId.toString();
                rectCuboid.setHandle(accessoireDTO.accessoireId.toString());

                meshRetraits.addMesh(rectCuboid);
            }

            // Rainures
            if (truncate) {
                double[] p1 = new double[] { 0, 0, 0 };
                double[] p2 = new double[] { 0, 0, d };
                double[] p3 = new double[] { d / 2 + m, 0, d };
                double[] p4 = new double[] { d + m, 0, d };
                double[] p5 = new double[] { d + m, 0, d / 2 - m };
                double[] p6 = new double[] { d / 2 + m, 0, d / 2 - m };
                double[] p7 = new double[] { d / 2 + m, 0, 0 };

                double[] p8 = new double[] { 0, -hauteur, 0 };
                double[] p9 = new double[] { 0, -hauteur, d };
                double[] p10 = new double[] { d / 2 + m, -hauteur, d };
                double[] p11 = new double[] { d + m, -hauteur, d };
                double[] p12 = new double[] { d + m, -hauteur, d / 2 - m };
                double[] p13 = new double[] { d / 2 + m, -hauteur, d / 2 - m };
                double[] p14 = new double[] { d / 2 + m, -hauteur, 0 };

                List<double[][]> triangles = new ArrayList<>();

                // bottom (2 faces, 4 triangles)
                triangles.add(new double[][] { p1, p7, p2 });
                triangles.add(new double[][] { p7, p3, p2 });
                triangles.add(new double[][] { p6, p5, p3 });
                triangles.add(new double[][] { p4, p3, p5 });

                // top (2 faces, 4 triangles)
                triangles.add(new double[][] { p14, p8, p9 });
                triangles.add(new double[][] { p9, p10, p14 });
                triangles.add(new double[][] { p12, p13, p10 });
                triangles.add(new double[][] { p10, p11, p12 });

                triangles.add(new double[][] { p8, p1, p2 });
                triangles.add(new double[][] { p2, p9, p8 });

                triangles.add(new double[][] { p4, p9, p2 });
                triangles.add(new double[][] { p11, p9, p4 });

                triangles.add(new double[][] { p4, p11, p5 });
                triangles.add(new double[][] { p5, p11, p12 });

                triangles.add(new double[][] { p5, p12, p6 });
                triangles.add(new double[][] { p6, p12, p13 });

                triangles.add(new double[][] { p6, p13, p7 });
                triangles.add(new double[][] { p7, p13, p14 });

                triangles.add(new double[][] { p14, p7, p1 });
                triangles.add(new double[][] { p14, p1, p8 });

                TriangleMesh rainure1 = TriangleMesh.fromDoubleList(triangles);
                TriangleMesh rainure2 = rainure1.rotateZOnPlace(Math.PI)
                        .translate(new Vector3D(largeur - d - m, 0, 0));

                rainure1.ID = "rainure1";
                rainure2.ID = "rainure2";
                meshRetraits.addMesh(rainure1);
                meshRetraits.addMesh(rainure2);
            } else {
                double[] p1 = { 0, 0, d / 2 - m };
                double[] p2 = { 0, 0, d };
                double[] p3 = { d / 2 + m, 0, d };
                double[] p4 = { d / 2 + m, 0, d / 2 - m };

                double[] p5 = { 0, -hauteur, d / 2 - m };
                double[] p6 = { 0, -hauteur, d };
                double[] p7 = { d / 2 + m, -hauteur, d };
                double[] p8 = { d / 2 + m, -hauteur, d / 2 - m };

                List<double[][]> triangles = new ArrayList<>();

                // bottom (1 face, 2 triangles)
                triangles.add(new double[][] { p3, p1, p2 });
                triangles.add(new double[][] { p4, p1, p3 });

                // top (1 face, 2 triangles)
                triangles.add(new double[][] { p7, p5, p6 });
                triangles.add(new double[][] { p8, p5, p7 });

                // front (1 face, 2 triangles)
                triangles.add(new double[][] { p2, p5, p1 });
                triangles.add(new double[][] { p6, p5, p2 });

                // back (1 face, 2 triangles)
                triangles.add(new double[][] { p3, p7, p4 });
                triangles.add(new double[][] { p4, p7, p8 });

                // left (1 face, 2 triangles)
                triangles.add(new double[][] { p5, p1, p4 });
                triangles.add(new double[][] { p5, p4, p8 });

                // right (1 face, 2 triangles)
                triangles.add(new double[][] { p2, p6, p3 });
                triangles.add(new double[][] { p6, p7, p3 });

                TriangleMesh rainure1 = TriangleMesh.fromDoubleList(triangles);
                TriangleMesh rainure2 = rainure1.translate(new Vector3D(largeur - d / 2 - m, 0, 0));

                rainure1.ID = "rainure1";
                rainure2.ID = "rainure2";
                meshRetraits.addMesh(rainure1);
                meshRetraits.addMesh(rainure2);
            }

            // meshRetraits = meshRetraits.translate(meshRetraits.getCenter().multiply(-1));
            // meshRetraits = meshRetraits
            // .translate(new Vector3D(-chaletDTO.longueur / 2, 0, -largeur / 2));
            this.meshRetraits = meshRetraits;
        }

        private void buildAccessoires() {
            List<TriangleMesh> prevMeshes = new ArrayList<TriangleMesh>();

            for (TriangleMeshGroup meshAccessoire : this.meshAccessoires) {
                prevMeshes.add(meshAccessoire.copy());
            }

            this.meshAccessoires.clear();

            for (Accessoire.AccessoireDTO accessoireDTO : this.accessoireDTOs) {
                TriangleMeshGroup accMesh = null;

                switch (accessoireDTO.accessoireType) {
                    case Fenetre:
                        accMesh = PanelHelper.buildWindow(accessoireDTO.dimensions[0] - 2,
                                accessoireDTO.dimensions[1],
                                new Vector3D(0, 0, -2), 2);
                        break;
                    case Porte:
                        accMesh = PanelHelper.buildDoor(accessoireDTO.dimensions[0] - 4,
                                accessoireDTO.dimensions[1],
                                new Vector3D(0, 0, 0), 4);
                        accMesh.setDraggableY(false);
                        break;
                }

                if (accMesh == null) {
                    throw new IllegalArgumentException("Invalid accessoire type");
                }

                accMesh.ID = accessoireDTO.accessoireId.toString();
                accMesh.setValid(accessoireDTO.valide);

                TriangleMesh _mesh = accMesh;
                TriangleMesh prevMesh = prevMeshes.stream().filter(m -> m.ID.equals(_mesh.ID)).findFirst().orElse(null);
                if (prevMesh != null) {
                    accMesh.setIsDragged(prevMesh.getIsDragged());
                    accMesh.setSelected(prevMesh.getSelected());
                    accMesh.setDraggableX(prevMesh.getDraggableX());
                    accMesh.setDraggableY(prevMesh.getDraggableY());
                    accMesh.setDraggableZ(prevMesh.getDraggableZ());
                    accMesh.setDraggable(prevMesh.getDraggable());
                }

                switch (typeMur) {
                    case Facade:
                        accMesh.setDraggableZ(false);
                        accMesh = accMesh.translate(accMesh.getCenter().multiply(-1));
                        accMesh = accMesh.translate(new Vector3D(0, 0, -chaletDTO.longueur / 2));
                        accMesh = accMesh.translate(new Vector3D(0, 0, -accMesh.getDepth() / 2));
                        accMesh = accMesh
                                .translate(new Vector3D(chaletDTO.largeur / 2 - accessoireDTO.dimensions[0] / 2,
                                        -chaletDTO.hauteur / 2 + accessoireDTO.dimensions[1] / 2, 0));

                        accMesh = accMesh
                                .translate(new Vector3D(-accessoireDTO.position[0], accessoireDTO.position[1], 0));

                        break;
                    case Arriere:
                        accMesh.setDraggableZ(false);
                        accMesh = accMesh.rotate(0, Math.PI, 0);
                        accMesh = accMesh.translate(accMesh.getCenter().multiply(-1));
                        accMesh = accMesh.translate(new Vector3D(0, 0, accMesh.getDepth() / 2));
                        accMesh = accMesh
                                .translate(new Vector3D(0, 0, chaletDTO.longueur / 2 + chaletDTO.epaisseurMur / 2));
                        accMesh = accMesh.translate(new Vector3D(
                                -chaletDTO.largeur / 2 + accessoireDTO.dimensions[0] / 2,
                                -chaletDTO.hauteur / 2 + accessoireDTO.dimensions[1] / 2, -chaletDTO.epaisseurMur / 2));

                        accMesh = accMesh
                                .translate(new Vector3D(accessoireDTO.position[0], accessoireDTO.position[1], 0));

                        break;
                    case Gauche:
                        accMesh.setDraggableX(false);
                        accMesh = accMesh.rotate(0, -Math.PI / 2, 0);
                        accMesh = accMesh.translate(accMesh.getCenter().multiply(-1));
                        accMesh = accMesh.translate(new Vector3D(accMesh.getWidth() / 2, 0, 0));
                        accMesh = accMesh
                                .translate(new Vector3D(chaletDTO.largeur / 2 - chaletDTO.epaisseurMur / 2, 0, 0));
                        accMesh = accMesh
                                .translate(new Vector3D(chaletDTO.epaisseurMur / 2,
                                        -chaletDTO.hauteur / 2 + accessoireDTO.dimensions[1] / 2,
                                        chaletDTO.longueur / 2 - accessoireDTO.dimensions[0] / 2));

                        accMesh = accMesh
                                .translate(new Vector3D(0, accessoireDTO.position[1], -accessoireDTO.position[0]));

                        break;
                    case Droit:
                        accMesh.setDraggableX(false);
                        accMesh = accMesh.rotate(0, Math.PI / 2, 0);
                        accMesh = accMesh.translate(accMesh.getCenter().multiply(-1));
                        accMesh = accMesh.translate(new Vector3D(-accMesh.getWidth() / 2, 0, 0));
                        accMesh = accMesh
                                .translate(new Vector3D(-chaletDTO.largeur / 2 + chaletDTO.epaisseurMur / 2, 0, 0));
                        accMesh = accMesh
                                .translate(new Vector3D(-chaletDTO.epaisseurMur / 2,
                                        -chaletDTO.hauteur / 2 + accessoireDTO.dimensions[1] / 2,
                                        -chaletDTO.longueur / 2 + accessoireDTO.dimensions[0] / 2));

                        accMesh = accMesh
                                .translate(new Vector3D(0, accessoireDTO.position[1], accessoireDTO.position[0]));

                        break;
                }

                accMesh = accMesh.translate(new Vector3D(0, -chaletDTO.hauteur / 2, 0));
                this.meshAccessoires.add(accMesh);
            }
        }

        public void transformMesh() {
            this.meshBrut = this.meshBrut.translate(this.meshBrut.getCenter().multiply(-1));
            this.meshFini = this.meshFini.translate(this.meshFini.getCenter().multiply(-1));

            this.meshBrut = this.meshBrut.translate(new Vector3D(0, -this.chaletDTO.hauteur / 2, 0));
            this.meshFini = this.meshFini.translate(new Vector3D(0, -this.chaletDTO.hauteur / 2, 0));

            switch (this.typeMur) {
                case Facade:
                    this.meshBrut = this.meshBrut.translate(
                            new Vector3D(0, 0, -this.chaletDTO.longueur / 2));

                    this.meshFini = this.meshFini.translate(
                            new Vector3D(0, 0, -this.chaletDTO.longueur / 2 + this.chaletDTO.epaisseurMur / 2));

                    this.meshRetraits = this.meshRetraits
                            .translate(new Vector3D(-this.chaletDTO.largeur / 2, 0, -this.chaletDTO.longueur / 2));
                    break;
                case Arriere:
                    this.meshBrut = this.meshBrut.rotateYOnPlace(Math.PI);
                    this.meshBrut = this.meshBrut.translate(
                            new Vector3D(0, 0, this.chaletDTO.longueur / 2));

                    this.meshFini = this.meshFini.rotateYOnPlace(Math.PI);
                    this.meshFini = this.meshFini.translate(
                            new Vector3D(0, 0, this.chaletDTO.longueur / 2 - this.chaletDTO.epaisseurMur / 2));

                    this.meshRetraits = this.meshRetraits.rotate(0, Math.PI, 0);
                    this.meshRetraits = this.meshRetraits
                            .translate(new Vector3D(this.chaletDTO.largeur / 2, 0, this.chaletDTO.longueur / 2));

                    break;
                case Droit:
                    this.meshBrut = this.meshBrut.rotateYOnPlace(Math.PI / 2);
                    this.meshBrut = this.meshBrut.translate(
                            new Vector3D(-this.chaletDTO.largeur / 2, 0, 0));

                    this.meshFini = this.meshFini.rotateYOnPlace(Math.PI / 2);
                    this.meshFini = this.meshFini.translate(
                            new Vector3D(-this.chaletDTO.largeur / 2 + this.chaletDTO.epaisseurMur / 2, 0, 0));

                    this.meshRetraits = this.meshRetraits.rotate(0, Math.PI / 2, 0);
                    this.meshRetraits = this.meshRetraits
                            .translate(new Vector3D(-this.chaletDTO.largeur / 2, 0, this.chaletDTO.longueur / 2));
                    break;
                case Gauche:
                    this.meshBrut = this.meshBrut.rotateYOnPlace(Math.PI / 2);
                    this.meshBrut = this.meshBrut.translate(
                            new Vector3D(this.chaletDTO.largeur / 2, 0, 0));

                    this.meshFini = this.meshFini.rotateYOnPlace(-Math.PI / 2);
                    this.meshFini = this.meshFini.translate(
                            new Vector3D(this.chaletDTO.largeur / 2 - this.chaletDTO.epaisseurMur / 2, 0, 0));

                    this.meshRetraits = this.meshRetraits.rotate(0, -Math.PI / 2, 0);
                    this.meshRetraits = this.meshRetraits
                            .translate(new Vector3D(this.chaletDTO.largeur / 2, 0, -this.chaletDTO.longueur / 2));
                    break;
            }
        }

        private void setMaterial() {
            switch (this.typeMur) {
                case Facade:
                    this.meshBrut.getMaterial().setColor(new Color(255, 0, 0));
                    this.meshFini.getMaterial().setColor(new Color(255, 0, 0));

                    for (TriangleMesh mesh : this.meshRetraits.getMeshes()) {
                        mesh.getMaterial().setColor(
                                this.meshFini.getMaterial().getColor().darker());
                    }

                    break;
                case Arriere:
                    this.meshBrut.getMaterial().setColor(new Color(0, 0, 255));
                    this.meshFini.getMaterial().setColor(new Color(0, 0, 255));

                    for (TriangleMesh mesh : this.meshRetraits.getMeshes()) {
                        mesh.getMaterial().setColor(
                                this.meshFini.getMaterial().getColor().darker());
                    }

                    break;
                case Droit:
                    this.meshBrut.getMaterial().setColor(new Color(0, 255, 0));
                    this.meshFini.getMaterial().setColor(new Color(0, 255, 0));

                    for (TriangleMesh mesh : this.meshRetraits.getMeshes()) {
                        mesh.getMaterial().setColor(
                                this.meshFini.getMaterial().getColor().darker());
                    }

                    break;
                case Gauche:
                    this.meshBrut.getMaterial().setColor(new Color(255, 255, 0));
                    this.meshFini.getMaterial().setColor(new Color(255, 255, 0));

                    for (TriangleMesh mesh : this.meshRetraits.getMeshes()) {
                        mesh.getMaterial().setColor(
                                this.meshFini.getMaterial().getColor().darker());
                    }

                    break;
            }
        }

        public void rebuild() {
            this.buildBrut();
            this.buildFini();
            this.buildRetraits();
            this.buildAccessoires();
            this.setMaterial();
            this.transformMesh();
            this.setActiveOuput(this.activeOutput);
        }

        public void setActiveOuput(OutputType outputType) {
            switch (outputType) {
                case Brut:
                    this.getMeshes().clear();
                    this.getMeshes().add(this.meshBrut);
                    break;
                case Fini:
                    this.getMeshes().clear();
                    this.getMeshes().add(this.meshFini);
                    break;
                case Retraits:
                    this.getMeshes().clear();
                    this.getMeshes().addAll(this.meshRetraits.getMeshes());
                    this.getMeshes().addAll(this.meshAccessoires);
                    break;
                // case FiniWithRetraits:
                // this.getMeshes().clear();
                // this.getMeshes().addAll(this.meshRetraits.getMeshes());
                // this.getMeshes().add(this.meshFini);
                // break;
                // case FiniWithAccessoires:
                // this.getMeshes().clear();
                // this.getMeshes().add(this.meshFini);
                // System.out.println("Accessoires : " + this.meshAccessoires.size());
                // this.getMeshes().addAll(this.meshAccessoires);
                // break;
                default:
                    break;
            }
        }

        public void updateAccessoires(List<Accessoire.AccessoireDTO> accessoireDTOs) {
            this.accessoireDTOs = accessoireDTOs;
            this.buildAccessoires();
            this.setActiveOuput(this.activeOutput);
        }

        public void update(Chalet.ChaletDTO chaletDTO) {
            this.chaletDTO = chaletDTO;
            this.rebuild();
        }
    }
    

    public static enum OutputType {
        Brut,
        Fini,
        Retraits,
    }

    /*
     * Used in order to calculate intersection points for the holes created by the
     * accessories
     */
    public static class Line {
        double x1 = 0;
        double y1 = 0;
        double x2 = 0;
        double y2 = 0;

        public Line(double x1, double y1, double x2, double y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public boolean intersects(Line line) {
            if (line.x1 == line.x2) {
                return (x1 <= line.x1 && x2 >= line.x1) || (x1 >= line.x1 && x2 <= line.x1);
            } else {
                return (y1 <= line.y1 && y2 >= line.y1) || (y1 >= line.y1 && y2 <= line.y1);
            }
        }

        public Point2D intersectionPoint(Line line) {
            // First, check if lines intersect each other
            if (!intersects(line)) {
                return null;
            }

            // If they do, calculate the intersection point
            double x = 0;
            double y = 0;

            if (line.x1 == line.x2) {
                x = line.x1;
                y = y1;
            } else {
                x = x1;
                y = line.y1;
            }

            return new Point2D.Double(x, y);
        }
    }

    public static class RectWithHoles {
        double x = 0;
        double y = 0;
        double width = 0;
        double height = 0;

        public List<Rectangle2D> holes = new ArrayList<Rectangle2D>();

        public RectWithHoles(double x, double y, double width, double height, List<Rectangle2D> holes) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.holes = holes;
        }

        public RectWithHoles(double x, double y, double width, double height) {
            this(x, y, width, height, new ArrayList<Rectangle2D>());
        }

        public List<double[][]> getTriangles(int z) {
            List<Line> lines = new ArrayList<Line>();
            List<Point2D> points = new ArrayList<>();

            for (Rectangle2D acc : this.holes) {
                int extendLineBy = 1000;
                Line line1 = new Line(acc.getX() - extendLineBy, acc.getY(), acc.getX() + extendLineBy,
                        acc.getY());
                Line line2 = new Line(acc.getX() - extendLineBy, acc.getY() + acc.getHeight(),
                        acc.getX() + extendLineBy, acc.getY() + acc.getHeight());
                Line line3 = new Line(acc.getX(), acc.getY() - extendLineBy, acc.getX(),
                        acc.getY() + extendLineBy);
                Line line4 = new Line(acc.getX() + acc.getWidth(), acc.getY() - extendLineBy,
                        acc.getX() + acc.getWidth(), acc.getY() + extendLineBy);

                lines.add(line1);
                lines.add(line2);
                lines.add(line3);
                lines.add(line4);

                for (Line _line : lines) {
                    Point2D p = line1.intersectionPoint(_line);
                    if (p != null) {
                        if (!points.contains(p) && p.getX() > 0 && p.getY() > 0) {
                            points.add(p);
                            points.add(new Point2D.Double(this.x, p.getY()));
                            points.add(new Point2D.Double(this.x + this.width, p.getY()));
                            points.add(new Point2D.Double(p.getX(), this.y));
                            points.add(new Point2D.Double(p.getX(), this.y + this.height));
                        }
                    }

                    Point2D p2 = line2.intersectionPoint(_line);
                    if (p2 != null) {
                        if (!points.contains(p2) && p2.getX() > 0 && p2.getY() > 0) {
                            points.add(p2);
                            points.add(new Point2D.Double(this.x, p2.getY()));
                            points.add(new Point2D.Double(this.x + this.width, p2.getY()));
                            points.add(new Point2D.Double(p2.getX(), this.y));
                            points.add(new Point2D.Double(p2.getX(), this.y + this.height));
                        }
                    }

                    Point2D p3 = line3.intersectionPoint(_line);
                    if (p3 != null) {
                        if (!points.contains(p3) && p3.getX() > 0 && p3.getY() > 0) {
                            points.add(p3);
                            points.add(new Point2D.Double(this.x, p3.getY()));
                            points.add(new Point2D.Double(this.x + this.width, p3.getY()));
                            points.add(new Point2D.Double(p3.getX(), this.y));
                            points.add(new Point2D.Double(p3.getX(), this.y + this.height));

                        }
                    }

                    Point2D p4 = line4.intersectionPoint(_line);
                    if (p4 != null) {
                        if (!points.contains(p4) && p4.getX() > 0 && p4.getY() > 0) {
                            points.add(p4);
                            points.add(new Point2D.Double(this.x, p4.getY()));
                            points.add(new Point2D.Double(this.x + this.width, p4.getY()));
                            points.add(new Point2D.Double(p4.getX(), this.y));
                            points.add(new Point2D.Double(p4.getX(), this.y + this.height));
                        }
                    }
                }
            }

            List<Point2D> pointsWithoutDoublon = new ArrayList<Point2D>();

            for (Point2D p : points) {
                // Compare x & y values
                boolean found = false;
                for (Point2D p2 : pointsWithoutDoublon) {
                    if (p.getX() == p2.getX() && p.getY() == p2.getY()) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    pointsWithoutDoublon.add(p);
                }
            }

            pointsWithoutDoublon.add(new Point2D.Double(this.x, this.y));
            pointsWithoutDoublon.add(new Point2D.Double(this.x + this.width, this.y));
            pointsWithoutDoublon.add(new Point2D.Double(this.x, this.y + this.height));
            pointsWithoutDoublon.add(new Point2D.Double(this.x + this.width, this.y + this.height));

            pointsWithoutDoublon.sort((p1, p2) -> (int) (p1.getX() - p2.getX()));
            pointsWithoutDoublon.sort((p1, p2) -> (int) (p1.getY() - p2.getY()));

            List<Rectangle2D> faces = new ArrayList<Rectangle2D>();

            for (int i = 0; i < pointsWithoutDoublon.size(); i++) {
                Point2D p1 = pointsWithoutDoublon.get(i);

                // Find the closest point on the right of p1
                Point2D p2 = null;
                for (int j = i + 1; j < pointsWithoutDoublon.size(); j++) {
                    Point2D p = pointsWithoutDoublon.get(j);
                    if (p.getX() > p1.getX()) {
                        p2 = p;
                        break;
                    }
                }

                // Find the closest point on the bottom of p1
                Point2D p3 = null;
                for (int j = i + 1; j < pointsWithoutDoublon.size(); j++) {
                    Point2D p = pointsWithoutDoublon.get(j);
                    if (p.getY() > p1.getY()) {
                        p3 = p;
                        break;
                    }
                }

                // Find the closest point the bottom right of p1
                Point2D p4 = null;
                for (int j = i + 1; j < pointsWithoutDoublon.size(); j++) {
                    Point2D p = pointsWithoutDoublon.get(j);
                    if (p.getX() > p1.getX() && p.getY() > p1.getY()) {
                        p4 = p;
                        break;
                    }
                }

                if ((p3 == null && p4 == null) || (p2 == null && p4 == null)) {
                    continue;
                }

                double diffX = p2.getX() - p1.getX();
                double diffY = p3.getY() - p1.getY();

                faces.add(new Rectangle2D.Double(p1.getX(), p1.getY(), diffX, diffY));
            }

            List<Rectangle2D> filteredRectangles = new ArrayList<Rectangle2D>();
            for (Rectangle2D rect : faces) {
                // Check if the rect is contain inside of an acc
                boolean found = false;
                for (Rectangle2D acc : holes) {
                    if (acc.contains(rect)) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    filteredRectangles.add(rect);
                }
            }

            List<double[][]> tris = new ArrayList<double[][]>();
            for (Rectangle2D rect : filteredRectangles) {
                tris.add(new double[][] {
                        { rect.getX(), rect.getY(), z },
                        { rect.getX() + rect.getWidth(), rect.getY(), z },
                        { rect.getX(), rect.getY() + rect.getHeight(), z }
                });

                tris.add(new double[][] {
                        { rect.getX() + rect.getWidth(), rect.getY(), z },
                        { rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight(), z },
                        { rect.getX(), rect.getY() + rect.getHeight(), z }
                });
            }

            int depth = z;

            for (Rectangle2D acc : holes) {
                tris.add(new double[][] {
                        { acc.getX(), acc.getY(), 0 },
                        { acc.getX(), acc.getY(), depth },
                        { acc.getX() + acc.getWidth(), acc.getY(), 0 }
                });
                tris.add(new double[][] {
                        { acc.getX() + acc.getWidth(), acc.getY(), 0 },
                        { acc.getX(), acc.getY(), depth },
                        { acc.getX() + acc.getWidth(), acc.getY(), depth }
                });
                tris.add(new double[][] {
                        { acc.getX(), acc.getY(), 0 },
                        { acc.getX(), acc.getY(), depth },
                        { acc.getX(), acc.getY() + acc.getHeight(), 0 }
                });
                tris.add(new double[][] {
                        { acc.getX(), acc.getY() + acc.getHeight(), 0 },
                        { acc.getX(), acc.getY(), depth },
                        { acc.getX(), acc.getY() + acc.getHeight(), depth }
                });
                tris.add(new double[][] {
                        { acc.getX(), acc.getY() + acc.getHeight(), 0 },
                        { acc.getX(), acc.getY() + acc.getHeight(), depth },
                        { acc.getX() + acc.getWidth(), acc.getY() + acc.getHeight(), 0 }
                });
                tris.add(new double[][] {
                        { acc.getX() + acc.getWidth(), acc.getY() + acc.getHeight(), 0 },
                        { acc.getX(), acc.getY() + acc.getHeight(), depth },
                        { acc.getX() + acc.getWidth(), acc.getY() + acc.getHeight(), depth }
                });
                tris.add(new double[][] {
                        { acc.getX() + acc.getWidth(), acc.getY(), 0 },
                        { acc.getX() + acc.getWidth(), acc.getY(), depth },
                        { acc.getX() + acc.getWidth(), acc.getY() + acc.getHeight(), 0 }
                });
                tris.add(new double[][] {
                        { acc.getX() + acc.getWidth(), acc.getY() + acc.getHeight(), 0 },
                        { acc.getX() + acc.getWidth(), acc.getY(), depth },
                        { acc.getX() + acc.getWidth(), acc.getY() + acc.getHeight(), depth }
                });
            }

            return tris;
        }
    }

    public static List<STLTools.Triangle> convertMeshTrianglesToStlTriangles(List<Triangle> meshTriangles) {
        List<STLTools.Triangle> stlTriangles = new ArrayList<>();

        for (Triangle tri : meshTriangles) {
            double[] normal = tri.getNormal().toArray();
            double[] vertex1 = tri.getVertice(0).toArray();
            double[] vertex2 = tri.getVertice(1).toArray();
            double[] vertex3 = tri.getVertice(2).toArray();

            stlTriangles
                    .add(new STLTools.Triangle(
                            new float[] { -(float) normal[0], (float) normal[2], -(float) normal[1] },
                            new float[] { -(float) vertex1[0], (float) vertex1[2], -(float) vertex1[1] },
                            new float[] { -(float) vertex2[0], (float) vertex2[2], -(float) vertex2[1] },
                            new float[] { -(float) vertex3[0], (float) vertex3[2], -(float) vertex3[1] }));
        }

        return stlTriangles;
    }
}
