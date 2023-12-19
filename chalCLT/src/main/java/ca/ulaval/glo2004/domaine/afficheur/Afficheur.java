package ca.ulaval.glo2004.domaine.afficheur;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

class RoofPanelBuilder {
    public static TriangleMeshGroup buildRoofTopPanel(double largeur, double longueur, double epaisseur, double angle,
            double marge) {
        double x0 = 0;
        double y0 = 0;
        double z0 = 0;

        double a = epaisseur / 2 / Math.cos(Math.toRadians(angle));
        double b = epaisseur / 2 * Math.tan(Math.toRadians(angle));
        // double c = epaisseur / 2 * Math.sin(Math.toRadians(angle));
        double height = Math.tan(Math.toRadians(angle)) * longueur;
        double depth = epaisseur;
        // double m = marge;

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

        TriangleMeshGroup group = new TriangleMeshGroup();
        TriangleMesh mesh = TriangleMesh.fromDoubleList(triangles);
        group.addMesh(mesh);
        return group;
    }

    public static TriangleMeshGroup buildRoofLeftGable(double largeur, double epaisseur, double angle, double marge) {
        TriangleMeshGroup pignonGroup = new TriangleMeshGroup();

        double a = (epaisseur / 2) / Math.cos(Math.toRadians(angle));
        double b = (epaisseur / 2) * Math.tan(Math.toRadians(angle));

        double x0 = 0;
        double y0 = 0;
        double z0 = 0;

        double m = marge;
        double d = epaisseur;

        double h = (largeur - m / 2) * Math.tan(Math.toRadians(angle)) - a - b;

        largeur = largeur - d;

        double[] p1 = new double[] { x0 + m / 2, y0, z0 };
        double[] p2 = new double[] { x0 + m / 2, y0, z0 + d / 2 - m / 2 };
        double[] p3 = new double[] { x0 + d / 2 + m, y0, z0 + d / 2 - m / 2 };
        double[] p4 = new double[] { x0 + d / 2 + m, y0, z0 + d };
        double[] p5 = new double[] { x0 + largeur - d / 2 - m, y0, z0 + d };
        double[] p6 = new double[] { x0 + largeur - d / 2 - m, y0, z0 + d / 2 - m / 2 };
        double[] p7 = new double[] { x0 + largeur - m / 2, y0, z0 + d / 2 - m / 2 };
        double[] p8 = new double[] { x0 + largeur - m / 2, y0, z0 };

        double[] p9 = new double[] { x0 + m / 2, y0 - h, z0 };
        double[] p10 = new double[] { x0 + m / 2, y0 - h, z0 + d / 2 - m / 2 };
        double[] p11 = new double[] { x0 + d / 2 + m, y0 - h + (d / 2 + m / 2) * Math.tan(Math.toRadians(angle)),
                z0 + d / 2 - m / 2 };

        double[] p12 = new double[] { x0 + d / 2 + m, y0 - h + (d / 2 + m / 2) * Math.tan(Math.toRadians(angle)) + b,
                z0 + d / 2 - m / 2 };
        double[] p13 = new double[] { x0 + d / 2 + m, y0 - h + (d / 2 + m / 2) * Math.tan(Math.toRadians(angle)) + b,
                z0 + d };

        List<double[][]> triangles = new ArrayList<double[][]>();

        triangles.add(new double[][] { p1, p2, p8 });
        triangles.add(new double[][] { p2, p7, p8 });
        triangles.add(new double[][] { p3, p4, p6 });
        triangles.add(new double[][] { p4, p5, p6 });

        triangles.add(new double[][] { p1, p2, p9 });
        triangles.add(new double[][] { p2, p10, p9 });

        triangles.add(new double[][] { p2, p3, p10 });
        triangles.add(new double[][] { p3, p11, p10 });

        triangles.add(new double[][] { p3, p4, p12 });
        triangles.add(new double[][] { p4, p13, p12 });

        triangles.add(new double[][] { p4, p13, p5 });
        triangles.add(new double[][] { p5, p13, p6 });
        triangles.add(new double[][] { p6, p13, p12 });

        triangles.add(new double[][] { p7, p8, p9 });
        triangles.add(new double[][] { p7, p9, p10 });

        triangles.add(new double[][] { p7, p10, p11 });
        triangles.add(new double[][] { p6, p7, p11 });
        triangles.add(new double[][] { p6, p11, p12 });

        triangles.add(new double[][] { p1, p8, p9 });

        TriangleMesh pignonMesh = TriangleMesh.fromDoubleList(triangles);

        pignonGroup.addMesh(pignonMesh);
        return pignonGroup;
    }

    public static TriangleMeshGroup buildRoofRightGable(double largeur, double epaisseur, double angle, double marge) {
        TriangleMeshGroup pignonGroup = new TriangleMeshGroup();

        double a = (epaisseur / 2) / Math.cos(Math.toRadians(angle));
        double b = (epaisseur / 2) * Math.tan(Math.toRadians(angle));

        double x0 = 0;
        double y0 = 0;
        double z0 = 0;

        double m = marge;
        double d = epaisseur;

        double h = (largeur - m / 2) * Math.tan(Math.toRadians(angle)) - a - b;

        largeur = largeur - d;

        double[] p1 = new double[] { x0 + m / 2, y0, z0 };
        double[] p2 = new double[] { x0 + m / 2, y0, z0 - d / 2 + m / 2 };
        double[] p3 = new double[] { x0 + d / 2 + m, y0, z0 - d / 2 + m / 2 };
        double[] p4 = new double[] { x0 + d / 2 + m, y0, z0 - d };
        double[] p5 = new double[] { x0 + largeur - d / 2 - m, y0, z0 - d };
        double[] p6 = new double[] { x0 + largeur - d / 2 - m, y0, z0 - d / 2 + m / 2 };
        double[] p7 = new double[] { x0 + largeur - m / 2, y0, z0 - d / 2 + m / 2 };
        double[] p8 = new double[] { x0 + largeur - m / 2, y0, z0 };

        double[] p9 = new double[] { x0 + m / 2, y0 - h, z0 };
        double[] p10 = new double[] { x0 + m / 2, y0 - h, z0 - d / 2 + m / 2 };
        double[] p11 = new double[] { x0 + d / 2 + m, y0 - h + (d / 2 + m / 2) * Math.tan(Math.toRadians(angle)),
                z0 - d / 2 + m / 2 };

        double[] p12 = new double[] { x0 + d / 2 + m, y0 - h + (d / 2 + m / 2) * Math.tan(Math.toRadians(angle)) + b,
                z0 - d / 2 + m / 2 };
        double[] p13 = new double[] { x0 + d / 2 + m, y0 - h + (d / 2 + m / 2) * Math.tan(Math.toRadians(angle)) + b,
                z0 - d };

        List<double[][]> triangles = new ArrayList<double[][]>();

        triangles.add(new double[][] { p1, p2, p8 });
        triangles.add(new double[][] { p2, p7, p8 });
        triangles.add(new double[][] { p3, p4, p6 });
        triangles.add(new double[][] { p4, p5, p6 });

        triangles.add(new double[][] { p1, p2, p9 });
        triangles.add(new double[][] { p2, p10, p9 });

        triangles.add(new double[][] { p2, p3, p10 });
        triangles.add(new double[][] { p3, p11, p10 });

        triangles.add(new double[][] { p3, p4, p12 });
        triangles.add(new double[][] { p4, p13, p12 });

        triangles.add(new double[][] { p4, p13, p5 });
        triangles.add(new double[][] { p5, p13, p6 });
        triangles.add(new double[][] { p6, p13, p12 });

        triangles.add(new double[][] { p7, p8, p9 });
        triangles.add(new double[][] { p7, p9, p10 });

        triangles.add(new double[][] { p7, p10, p11 });
        triangles.add(new double[][] { p6, p7, p11 });
        triangles.add(new double[][] { p6, p11, p12 });

        triangles.add(new double[][] { p1, p8, p9 });

        TriangleMesh pignonMesh = TriangleMesh.fromDoubleList(triangles);

        pignonGroup.addMesh(pignonMesh);
        return pignonGroup;
    }

    public static TriangleMeshGroup buildRoofRightGableRemoval(double largeur, double epaisseur, double angle, double marge) {
        TriangleMeshGroup pignonGroup = new TriangleMeshGroup();

        double a = (epaisseur / 2) / Math.cos(Math.toRadians(angle));
        double b = (epaisseur / 2) * Math.tan(Math.toRadians(angle));

        double x0 = 0;
        double y0 = 0;
        double z0 = 0;

        double m = marge;
        double d = epaisseur;

        double h = (largeur - m / 2) * Math.tan(Math.toRadians(angle)) - a - b;

        largeur = largeur - d;

        double[] p1 = new double[] { x0 + m / 2, y0 - h, z0 };
        double[] p2 = new double[] { x0 + largeur - m / 2, y0 - h, z0 };
        double[] p3 = new double[] { x0 + largeur - m / 2, y0, z0 };

        double[] p4 = new double[] { x0 + m / 2, y0 - h, z0 - d / 2 + m / 2 };
        double[] p5 = new double[] { x0 + largeur - m / 2, y0 - h, z0 - d / 2 + m / 2 };
        double[] p6 = new double[] { x0 + largeur - m / 2, y0, z0 - d / 2 + m / 2 };

        // Left removal
        double[] p7 = new double[] { x0 + m / 2, y0, z0 - d / 2 + m / 2 };
        double[] p8 = new double[] { x0 + m / 2, y0, z0 - d };
        double[] p9 = new double[] { x0 + d/2 + m, y0, z0 - d };
        double[] p10 = new double[] { x0 + d/2 + m, y0, z0 - d / 2 + m / 2 };

        double[] p11 = new double[] { x0 + m / 2, y0 - h, z0 - d / 2 + m / 2 };
        double[] p12 = new double[] { x0 + m / 2, y0 - h, z0 - d };
        double[] p13 = new double[] { x0 + d/2 + m, y0 - h, z0 - d };
        double[] p14 = new double[] { x0 + d/2 + m, y0 - h, z0 - d / 2 + m / 2 };

        // Right removal
        double[] p15 = new double[] { x0 + largeur - m / 2, y0, z0 - d / 2 + m / 2 };
        double[] p16 = new double[] { x0 + largeur - m / 2, y0, z0 - d };
        double[] p17 = new double[] { x0 + largeur - d/2 - m, y0, z0 - d };
        double[] p18 = new double[] { x0 + largeur - d/2 - m, y0, z0 - d / 2 + m / 2 };

        double[] p19 = new double[] { x0 + largeur - m / 2, y0 - h, z0 - d / 2 + m / 2 };
        double[] p20 = new double[] { x0 + largeur - m / 2, y0 - h, z0 - d };
        double[] p21 = new double[] { x0 + largeur - d/2 - m, y0 - h, z0 - d };
        double[] p22 = new double[] { x0 + largeur - d/2 - m, y0 - h, z0 - d / 2 + m / 2 };

        // Second triangle removal
        double[] p23 = new double[] { x0 + d / 2 + m, y0 - h + (d / 2 + m / 2) * Math.tan(Math.toRadians(angle)),
                z0 - d / 2 + m / 2 };

        double[] p24 = new double[] { x0 + d / 2 + m, y0 - h + (d / 2 + m / 2) * Math.tan(Math.toRadians(angle)) + b,
                z0 - d / 2 + m / 2 };
        double[] p25 = new double[] { x0 + d / 2 + m, y0 - h + (d / 2 + m / 2) * Math.tan(Math.toRadians(angle)) + b,
                z0 - d };
        List<double[][]> triangles = new ArrayList<double[][]>();
        
        // Main triangle removal
        triangles.add(new double[][] { p1, p2, p3 });
        triangles.add(new double[][] { p4, p5, p6 });
        triangles.add(new double[][] { p1, p2, p4 });
        triangles.add(new double[][] { p2, p5, p4 });
        triangles.add(new double[][] { p2, p3, p5 });
        triangles.add(new double[][] { p3, p6, p5 });
        triangles.add(new double[][] { p1, p3, p4 });
        triangles.add(new double[][] { p3, p6, p4 });

        // Left removal
        triangles.add(new double[][] { p7, p8, p9 });
        triangles.add(new double[][] { p7, p9, p10 });

        triangles.add(new double[][] { p11, p12, p13 });
        triangles.add(new double[][] { p11, p13, p14 });

        triangles.add(new double[][] { p7, p8, p11 });
        triangles.add(new double[][] { p8, p12, p11 });

        triangles.add(new double[][] { p8, p9, p12 });
        triangles.add(new double[][] { p9, p13, p12 });

        triangles.add(new double[][] { p9, p10, p13 });
        triangles.add(new double[][] { p10, p14, p13 });

        triangles.add(new double[][] { p10, p7, p14 });
        triangles.add(new double[][] { p7, p11, p14 });

        // Right removal
        triangles.add(new double[][] { p15, p16, p17 });
        triangles.add(new double[][] { p15, p17, p18 });

        triangles.add(new double[][] { p19, p20, p21 });
        triangles.add(new double[][] { p19, p21, p22 });

        triangles.add(new double[][] { p15, p16, p19 });
        triangles.add(new double[][] { p16, p20, p19 });

        triangles.add(new double[][] { p16, p17, p20 });
        triangles.add(new double[][] { p17, p21, p20 });

        triangles.add(new double[][] { p17, p18, p21 });
        triangles.add(new double[][] { p18, p22, p21 });

        triangles.add(new double[][] { p18, p15, p22 });
        triangles.add(new double[][] { p15, p19, p22 });

        // Second triangle removal
        triangles.add(new double[][] { p17, p18, p25 });
        triangles.add(new double[][] { p18, p24, p25 });

        triangles.add(new double[][] { p13, p22, p21 });
        triangles.add(new double[][] { p13, p14, p22 });

        triangles.add(new double[][] { p13, p17, p21 });
        triangles.add(new double[][] { p25, p17, p13 });

        triangles.add(new double[][] { p23, p24, p18 });
        triangles.add(new double[][] { p22, p19, p23 });

        TriangleMesh pignonMesh = TriangleMesh.fromDoubleList(triangles);

        pignonGroup.addMesh(pignonMesh);
        return pignonGroup;
    }

    public static TriangleMeshGroup buildRoofVerticalPanel(double largeur, double longueur, double epaisseur, double angle, double marge) {
        TriangleMeshGroup vertPanelGroup = new TriangleMeshGroup();

        double x0 = 0;
        double y0 = 0;
        double z0 = 0;

        double m = marge;
        double d = epaisseur;

        double a = (d / 2) / Math.cos(Math.toRadians(angle));
        double b = (d / 2 - m / 2) * Math.tan(Math.toRadians(angle));
        double c = (d / 2 + m / 2) * Math.tan(Math.toRadians(angle));

        double h = longueur * Math.tan(Math.toRadians(angle)) - a;


        double[] p1 = new double[] { x0, y0, z0 };
        double[] p2 = new double[] { x0, y0, z0 + d / 2 - m / 2 };
        double[] p3 = new double[] { x0 + d / 2 + m / 2, y0, z0 + d / 2 - m / 2 };
        double[] p4 = new double[] { x0 + d / 2 + m / 2, y0, z0 + d };
        double[] p5 = new double[] { x0 + largeur - d / 2 - m / 2, y0, z0 + d };
        double[] p6 = new double[] { x0 + largeur - d / 2 - m / 2, y0, z0 + d / 2 - m / 2 };
        double[] p7 = new double[] { x0 + largeur, y0, z0 + d / 2 - m / 2 };
        double[] p8 = new double[] { x0 + largeur, y0, z0 };

        double[] p9 = new double[] { x0, y0 - h, z0 };
        double[] p10 = new double[] { x0 + largeur, y0 - h, z0 };

        double[] p11 = new double[] { x0, y0 - h + b, z0 + d / 2 - m / 2 };
        double[] p12 = new double[] { x0 + largeur, y0 - h + b, z0 + d / 2 - m / 2 };

        double[] p13 = new double[] { x0 + d / 2 + m / 2, y0 - h + b, z0 + d / 2 - m / 2 };
        double[] p14 = new double[] { x0 + largeur - d / 2 - m / 2, y0 - h + b, z0 + d / 2 - m / 2 };

        double[] p15 = new double[] { x0 + d / 2 + m / 2, y0 - h + b + b, z0 + d / 2 - m / 2 };
        double[] p16 = new double[] { x0 + largeur - d / 2 - m / 2, y0 - h + b + b, z0 + d / 2 - m / 2 };

        double[] p17 = new double[] { x0 + d / 2 + m / 2, y0 - h + b + b + c, z0 + d };
        double[] p18 = new double[] { x0 + largeur - d / 2 - m / 2, y0 - h + b + b + c, z0 + d };

        List<double[][]> triangles = new ArrayList<double[][]>();

        triangles.add(new double[][] { p1, p2, p8 });
        triangles.add(new double[][] { p2, p7, p8 });
        triangles.add(new double[][] { p3, p4, p6 });
        triangles.add(new double[][] { p4, p5, p6 });

        triangles.add(new double[][] { p1, p9, p10 });
        triangles.add(new double[][] { p8, p1, p10 });

        triangles.add(new double[][] { p9, p10, p11 });
        triangles.add(new double[][] { p10, p12, p11 });

        triangles.add(new double[][] { p1, p2, p11 });
        triangles.add(new double[][] { p1, p11, p9 });

        triangles.add(new double[][] { p2, p3, p11 });
        triangles.add(new double[][] { p3, p13, p11 });

        triangles.add(new double[][] { p8, p7, p12 });
        triangles.add(new double[][] { p8, p12, p10 });

        triangles.add(new double[][] { p7, p6, p14 });
        triangles.add(new double[][] { p7, p14, p12 });

        triangles.add(new double[][] { p13, p14, p15 });
        triangles.add(new double[][] { p14, p16, p15 });
        
        triangles.add(new double[][] { p15, p16, p17 });
        triangles.add(new double[][] { p16, p18, p17 });

        triangles.add(new double[][] { p4, p17, p15 });
        triangles.add(new double[][] { p3, p4, p15 });

        triangles.add(new double[][] { p4, p5, p17 });
        triangles.add(new double[][] { p5, p18, p17 });

        triangles.add(new double[][] { p5, p6, p18 });
        triangles.add(new double[][] { p6, p16, p18 });

        TriangleMesh vertPanelMesh = TriangleMesh.fromDoubleList(triangles);

        vertPanelMesh.getMaterial().setColor(Color.RED);
        vertPanelGroup.addMesh(vertPanelMesh);

        return vertPanelGroup;
    }

    public static TriangleMeshGroup buildRoofVerticalPanelRemoval(double largeur, double longueur, double epaisseur, double angle, double marge) {
        TriangleMeshGroup vertPanelGroup = new TriangleMeshGroup();

        double x0 = 0;
        double y0 = 0;
        double z0 = 0;

        double m = marge;
        double d = epaisseur;

        double a = (d / 2) / Math.cos(Math.toRadians(angle));
        double b = (d / 2 - m / 2) * Math.tan(Math.toRadians(angle));
        double c = (d / 2 + m / 2) * Math.tan(Math.toRadians(angle));

        double h = longueur * Math.tan(Math.toRadians(angle)) - a;

        // Right removal
        double[] p1 = { x0, y0, z0 + d };
        double[] p2 = { x0 + d / 2 + m / 2, y0, z0 + d };
        double[] p3 = { x0 + d / 2 + m / 2, y0, z0 + d / 2 - m / 2 };
        double[] p4 = { x0, y0, z0 + d / 2 - m / 2 };

        double[] p5 = { x0, y0 - h, z0 + d };
        double[] p6 = { x0 + d / 2 + m / 2, y0 - h, z0 + d };
        double[] p7 = { x0 + d / 2 + m / 2, y0 - h, z0 + d / 2 - m / 2 };
        double[] p8 = { x0, y0 - h, z0 + d / 2 - m / 2 };

        // Left removal
        double[] p9 = { x0 + largeur - d/2 - m/2, y0, z0 + d };
        double[] p10 = { x0 + largeur, y0, z0 + d };
        double[] p11 = { x0 + largeur, y0, z0 + d / 2 - m / 2 };
        double[] p12 = { x0 + largeur - d/2 - m/2, y0, z0 + d / 2 - m / 2 };

        double[] p13 = { x0 + largeur - d/2 - m/2, y0 - h, z0 + d };
        double[] p14 = { x0 + largeur, y0 - h, z0 + d };
        double[] p15 = { x0 + largeur, y0 - h, z0 + d / 2 - m / 2 };
        double[] p16 = { x0 + largeur - d/2 - m/2, y0 - h, z0 + d / 2 - m / 2 };
        
        // Top removal
        double[] p17 = { x0, y0 - h, z0 };
        double[] p18 = { x0 + largeur, y0 - h, z0 };
        double[] p19 = { x0 + largeur, y0 - h, z0 + d / 2 - m / 2 };
        double[] p20 = { x0, y0 - h, z0 + d / 2 - m / 2 };

        double[] p21 = { x0, y0 - h + b, z0 + d / 2 - m / 2 };
        double[] p22 = { x0 + largeur, y0 - h + b, z0 + d / 2 - m / 2 };
        
        // Second Top removal
        double[] p23 = new double[] { x0 + d / 2 + m / 2, y0 - h + b + b, z0 + d / 2 - m / 2 };
        double[] p24 = new double[] { x0 + largeur - d / 2 - m / 2, y0 - h + b + b, z0 + d / 2 - m / 2 };

        double[] p25 = new double[] { x0 + d / 2 + m / 2, y0 - h + b + b + c, z0 + d };
        double[] p26 = new double[] { x0 + largeur - d / 2 - m / 2, y0 - h + b + b + c, z0 + d };
        
        List<double[][]> triangles = new ArrayList<double[][]>();

        // Right removal
        triangles.add(new double[][] { p1, p2, p3 });
        triangles.add(new double[][] { p1, p3, p4 });

        triangles.add(new double[][] { p5, p6, p7 });
        triangles.add(new double[][] { p5, p7, p8 });

        triangles.add(new double[][] { p1, p2, p5 });
        triangles.add(new double[][] { p2, p6, p5 });

        triangles.add(new double[][] { p2, p3, p6 });
        triangles.add(new double[][] { p3, p7, p6 });

        triangles.add(new double[][] { p3, p4, p7 });
        triangles.add(new double[][] { p4, p8, p7 });

        triangles.add(new double[][] { p4, p1, p8 });
        triangles.add(new double[][] { p1, p5, p8 });

        // Left removal
        triangles.add(new double[][] { p9, p10, p11 });
        triangles.add(new double[][] { p9, p11, p12 });

        triangles.add(new double[][] { p13, p14, p15 });
        triangles.add(new double[][] { p13, p15, p16 });

        triangles.add(new double[][] { p9, p10, p13 });
        triangles.add(new double[][] { p10, p14, p13 });

        triangles.add(new double[][] { p10, p11, p14 });
        triangles.add(new double[][] { p11, p15, p14 });

        triangles.add(new double[][] { p11, p12, p15 });
        triangles.add(new double[][] { p12, p16, p15 });

        triangles.add(new double[][] { p12, p9, p16 });
        triangles.add(new double[][] { p9, p13, p16 });
        
        // Top removal
        triangles.add(new double[][] { p17, p18, p19 });
        triangles.add(new double[][] { p17, p19, p20 });

        triangles.add(new double[][] { p21, p22, p19 });
        triangles.add(new double[][] { p21, p19, p20 });

        triangles.add(new double[][] { p17, p18, p21 });
        triangles.add(new double[][] { p18, p22, p21 });

        triangles.add(new double[][] { p17, p21, p20 });
        triangles.add(new double[][] { p18, p19, p22 });
        
        // Second Top removal
        triangles.add(new double[][] { p23, p24, p26 });
        triangles.add(new double[][] { p23, p26, p25 });

        triangles.add(new double[][] { p6, p7, p23 });
        triangles.add(new double[][] { p6, p23, p25 });

        triangles.add(new double[][] { p13, p26, p24 });
        triangles.add(new double[][] { p13, p24, p16 });

        triangles.add(new double[][] { p6, p13, p7 });
        triangles.add(new double[][] { p13, p16, p7 });

        triangles.add(new double[][] { p6, p13, p25 });
        triangles.add(new double[][] { p25, p13, p26 });

        triangles.add(new double[][] {p23, p7, p24 });
        triangles.add(new double[][] {p7, p16, p24 });
        
        TriangleMesh vertPanelMesh = TriangleMesh.fromDoubleList(triangles);

        vertPanelMesh.getMaterial().setColor(Color.RED);
        vertPanelGroup.addMesh(vertPanelMesh);
        return vertPanelGroup;
    }

    
}

public class Afficheur {
    public static enum MouseControl {
        Move,
        Rotate,
    }

    private AfficheurEventSupport eventSupport = new AfficheurEventSupport();

    private Controleur controleur;

    private Scene scene;
    private Rasterizer rasterizer;
    private TypeDeVue vueActive = TypeDeVue.Dessus;

    public MurTriangleMeshGroup murFacadeGroup;
    public MurTriangleMeshGroup murArriereGroup;
    public MurTriangleMeshGroup murDroitGroup;
    public MurTriangleMeshGroup murGaucheGroup;
    public TriangleMeshGroup panneauToit;
    public TriangleMeshGroup rallongeVerticaleToit;
    public TriangleMeshGroup pignonGaucheToit;
    public TriangleMeshGroup pignonDroitToit;

    public OutputType renduVisuel = OutputType.Fini;
    private JPanel drawingPanel;
    MouseControl mouseControl = MouseControl.Move;

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
        // this.drawingPanel.addMouseListener(this.mouseListener());
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

        this.drawingPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // System.out.println("keyPressed " + (e.getKeyCode() == KeyEvent.VK_SHIFT));                
            }
        });

        this.drawingPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                drawingPanel.requestFocusInWindow();
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

    public void setMouseControl(MouseControl mouseControl) {
        if (this.mouseControl == mouseControl) {
            return;
        }

        this.mouseControl = mouseControl;
        this.eventSupport.dispatchMouseControlChanged(mouseControl);
    }

    public MouseControl getMouseControl() {
        return mouseControl;
    }

    public void switchOutputType(OutputType outputType) {
        this.murFacadeGroup.setActiveOuput(outputType);
        this.murArriereGroup.setActiveOuput(outputType);
        this.murDroitGroup.setActiveOuput(outputType);
        this.murGaucheGroup.setActiveOuput(outputType);

        this.renduVisuel = outputType;
        drawingPanel.repaint();
    }
    
    private void sharedRoofCode(Chalet.ChaletDTO chaletDTO) {
        double longueur = chaletDTO.longueur;
        double largeur = chaletDTO.largeur;

        Color panneauToitColor = new Color(255,146, 146);
        Color rallongeVerticaleToitColor = new Color(255,146, 146);
        Color pignonDroitToitColor = new Color(255,146, 146).darker();
        Color pignonGaucheToitColor = new Color(255,146, 146).darker();

        TriangleMeshGroup oldPanneauToit = this.panneauToit;
        TriangleMeshGroup oldRallongeVerticaleToit = this.rallongeVerticaleToit;
        TriangleMeshGroup oldPignonDroitToit = this.pignonDroitToit;
        TriangleMeshGroup oldPignonGaucheToit = this.pignonGaucheToit;

        switch (chaletDTO.sensToit) {
            case Nord:
                panneauToit = RoofPanelBuilder.buildRoofTopPanel(largeur, longueur, chaletDTO.epaisseurMur,
                        chaletDTO.angleToit, chaletDTO.margeSupplementaireRetrait);
                panneauToit = panneauToit.translate(
                        new Vector3D(-largeur / 2, -chaletDTO.hauteur - panneauToit.getHeight(), -longueur / 2));

                rallongeVerticaleToit = RoofPanelBuilder.buildRoofVerticalPanel(largeur, longueur, chaletDTO.epaisseurMur,
                        chaletDTO.angleToit, chaletDTO.margeSupplementaireRetrait);

                rallongeVerticaleToit = rallongeVerticaleToit.translate(new Vector3D(-largeur / 2,
                        -chaletDTO.hauteur, -longueur / 2));

                pignonDroitToit = RoofPanelBuilder.buildRoofRightGable(longueur, chaletDTO.epaisseurMur, chaletDTO.angleToit, chaletDTO.margeSupplementaireRetrait);
                pignonDroitToit = pignonDroitToit.rotate(0, -Math.PI / 2, 0);
                pignonDroitToit = pignonDroitToit.translate(
                        new Vector3D(-largeur / 2, -chaletDTO.hauteur, -longueur / 2 + chaletDTO.epaisseurMur / 2));

                pignonGaucheToit = RoofPanelBuilder.buildRoofLeftGable(longueur, chaletDTO.epaisseurMur, chaletDTO.angleToit, chaletDTO.margeSupplementaireRetrait);
                pignonGaucheToit = pignonGaucheToit.rotate(0, -Math.PI / 2, 0);
                pignonGaucheToit = pignonGaucheToit.translate(
                        new Vector3D(largeur / 2, -chaletDTO.hauteur, -longueur / 2 + chaletDTO.epaisseurMur / 2));
                

                // TriangleMeshGroup m = RoofPanelBuilder.buildRoofVerticalPanelRemoval(largeur, longueur, chaletDTO.epaisseurMur, chaletDTO.angleToit, chaletDTO.margeSupplementaireRetrait);
                // m = m.translate(new Vector3D(-largeur / 2,
                //         -chaletDTO.hauteur, -longueur / 2));

                // TriangleMeshGroup m2 = RoofPanelBuilder.buildRoofRightGableRemoval(longueur, chaletDTO.epaisseurMur, chaletDTO.angleToit, chaletDTO.margeSupplementaireRetrait);

                // m2 = m2.rotate(0, -Math.PI / 2, 0);
                // m2 = m2.translate(
                //         new Vector3D(-largeur / 2, -chaletDTO.hauteur, -longueur / 2 + chaletDTO.epaisseurMur / 2));
                    
                // getScene().addMesh(m);
                // getScene().addMesh(m2);

                break;
            case Sud:
                panneauToit = RoofPanelBuilder.buildRoofTopPanel(largeur, longueur, chaletDTO.epaisseurMur, chaletDTO.angleToit, chaletDTO.margeSupplementaireRetrait);
                panneauToit = panneauToit.translate(
                        new Vector3D(-largeur / 2, -chaletDTO.hauteur - panneauToit.getHeight(), -longueur / 2));

                rallongeVerticaleToit = RoofPanelBuilder.buildRoofVerticalPanel(largeur, longueur, chaletDTO.epaisseurMur, chaletDTO.angleToit, chaletDTO.margeSupplementaireRetrait);
                rallongeVerticaleToit = rallongeVerticaleToit.translate(new Vector3D(-largeur / 2,
                        -chaletDTO.hauteur, -longueur / 2));

                pignonDroitToit = RoofPanelBuilder.buildRoofRightGable(longueur, chaletDTO.epaisseurMur, chaletDTO.angleToit, chaletDTO.margeSupplementaireRetrait);
                pignonDroitToit = pignonDroitToit.rotate(0, -Math.PI / 2, 0);
                pignonDroitToit = pignonDroitToit.translate(
                        new Vector3D(-largeur / 2, -chaletDTO.hauteur, -longueur / 2 + chaletDTO.epaisseurMur / 2));

                pignonGaucheToit = RoofPanelBuilder.buildRoofLeftGable(longueur, chaletDTO.epaisseurMur, chaletDTO.angleToit, chaletDTO.margeSupplementaireRetrait);
                pignonGaucheToit = pignonGaucheToit.rotate(0, -Math.PI / 2, 0);
                pignonGaucheToit = pignonGaucheToit.translate(
                        new Vector3D(largeur / 2, -chaletDTO.hauteur, -longueur / 2 + chaletDTO.epaisseurMur / 2));

                panneauToit = panneauToit.rotate(0, Math.PI, 0);
                rallongeVerticaleToit = rallongeVerticaleToit.rotate(0, Math.PI, 0);
                pignonDroitToit = pignonDroitToit.rotate(0, Math.PI, 0);
                pignonGaucheToit = pignonGaucheToit.rotate(0, Math.PI, 0);
                
                break;
            case Est:
                panneauToit = RoofPanelBuilder.buildRoofTopPanel(longueur, largeur, chaletDTO.epaisseurMur,
                        chaletDTO.angleToit, chaletDTO.margeSupplementaireRetrait);
                panneauToit = panneauToit.translate(
                        new Vector3D(-longueur / 2, -chaletDTO.hauteur - panneauToit.getHeight(), -largeur / 2));

                rallongeVerticaleToit = RoofPanelBuilder.buildRoofVerticalPanel(longueur, largeur, chaletDTO.epaisseurMur,
                        chaletDTO.angleToit, chaletDTO.margeSupplementaireRetrait);
                rallongeVerticaleToit = rallongeVerticaleToit.translate(new Vector3D(-longueur / 2,
                        -chaletDTO.hauteur, -largeur / 2));

                pignonDroitToit = RoofPanelBuilder.buildRoofRightGable(largeur, chaletDTO.epaisseurMur, chaletDTO.angleToit, chaletDTO.margeSupplementaireRetrait);
                pignonDroitToit = pignonDroitToit.rotate(0, -Math.PI / 2, 0);
                pignonDroitToit = pignonDroitToit.translate(
                        new Vector3D(-longueur / 2, -chaletDTO.hauteur, -largeur / 2 + chaletDTO.epaisseurMur / 2));

                pignonGaucheToit = RoofPanelBuilder.buildRoofLeftGable(largeur, chaletDTO.epaisseurMur, chaletDTO.angleToit, chaletDTO.margeSupplementaireRetrait);
                pignonGaucheToit = pignonGaucheToit.rotate(0, -Math.PI / 2, 0);
                pignonGaucheToit = pignonGaucheToit.translate(
                        new Vector3D(longueur / 2, -chaletDTO.hauteur, -largeur / 2 + chaletDTO.epaisseurMur / 2));

                panneauToit = panneauToit.rotate(0, Math.PI / 2, 0);
                rallongeVerticaleToit = rallongeVerticaleToit.rotate(0, Math.PI / 2, 0);
                pignonDroitToit = pignonDroitToit.rotate(0, Math.PI / 2, 0);
                pignonGaucheToit = pignonGaucheToit.rotate(0, Math.PI / 2, 0);
                
                break;
            case Ouest:
                panneauToit = RoofPanelBuilder.buildRoofTopPanel(longueur, largeur, chaletDTO.epaisseurMur,
                        chaletDTO.angleToit, chaletDTO.margeSupplementaireRetrait);
                panneauToit = panneauToit.translate(
                        new Vector3D(-longueur / 2, -chaletDTO.hauteur - panneauToit.getHeight(), -largeur / 2));

                rallongeVerticaleToit = RoofPanelBuilder.buildRoofVerticalPanel(longueur, largeur, chaletDTO.epaisseurMur,
                        chaletDTO.angleToit, chaletDTO.margeSupplementaireRetrait);
                rallongeVerticaleToit = rallongeVerticaleToit.translate(new Vector3D(-longueur / 2,
                        -chaletDTO.hauteur, -largeur / 2));

                pignonDroitToit = RoofPanelBuilder.buildRoofRightGable(largeur, chaletDTO.epaisseurMur, chaletDTO.angleToit, chaletDTO.margeSupplementaireRetrait);
                pignonDroitToit = pignonDroitToit.rotate(0, -Math.PI / 2, 0);
                pignonDroitToit = pignonDroitToit.translate(
                        new Vector3D(-longueur / 2, -chaletDTO.hauteur, -largeur / 2 + chaletDTO.epaisseurMur / 2));

                pignonGaucheToit = RoofPanelBuilder.buildRoofLeftGable(largeur, chaletDTO.epaisseurMur, chaletDTO.angleToit, chaletDTO.margeSupplementaireRetrait);
                pignonGaucheToit = pignonGaucheToit.rotate(0, -Math.PI / 2, 0);
                pignonGaucheToit = pignonGaucheToit.translate(
                        new Vector3D(longueur / 2, -chaletDTO.hauteur, -largeur / 2 + chaletDTO.epaisseurMur / 2));

                panneauToit = panneauToit.rotate(0, -Math.PI / 2, 0);
                rallongeVerticaleToit = rallongeVerticaleToit.rotate(0, -Math.PI / 2, 0);
                pignonDroitToit = pignonDroitToit.rotate(0, 3*Math.PI / 2, 0);
                pignonGaucheToit = pignonGaucheToit.rotate(0, 3*Math.PI / 2, 0);
                
                break;
        }

        panneauToit.setDraggable(false);
        rallongeVerticaleToit.setDraggable(false);
        pignonDroitToit.setDraggable(false);
        pignonGaucheToit.setDraggable(false);

        panneauToit.ID = "panneauToit";
        rallongeVerticaleToit.ID = "rallongeVerticaleToit";
        pignonDroitToit.ID = "pignonDroitToit";
        pignonGaucheToit.ID = "pignonGaucheToit";

        panneauToit.getMesh(0).getMaterial().setColor(panneauToitColor);
        rallongeVerticaleToit.getMesh(0).getMaterial().setColor(rallongeVerticaleToitColor);
        pignonDroitToit.getMesh(0).getMaterial().setColor(pignonDroitToitColor);
        pignonGaucheToit.getMesh(0).getMaterial().setColor(pignonGaucheToitColor);

        if (oldPanneauToit != null) {
            panneauToit.setVisible(oldPanneauToit.getVisible());
            panneauToit.setDraggable(oldPanneauToit.getDraggable());
            panneauToit.setSelectable(oldPanneauToit.getSelectable());
            panneauToit.setHidden(oldPanneauToit.getHidden());
            panneauToit.setSelected(oldPanneauToit.getSelected());
        }

        if (oldRallongeVerticaleToit != null) {
            rallongeVerticaleToit.setVisible(oldRallongeVerticaleToit.getVisible());
            rallongeVerticaleToit.setDraggable(oldRallongeVerticaleToit.getDraggable());
            rallongeVerticaleToit.setSelectable(oldRallongeVerticaleToit.getSelectable());
            rallongeVerticaleToit.setHidden(oldRallongeVerticaleToit.getHidden());
            rallongeVerticaleToit.setSelected(oldRallongeVerticaleToit.getSelected());
        }

        if (oldPignonDroitToit != null) {
            pignonDroitToit.setVisible(oldPignonDroitToit.getVisible());
            pignonDroitToit.setDraggable(oldPignonDroitToit.getDraggable());
            pignonDroitToit.setSelectable(oldPignonDroitToit.getSelectable());
            pignonDroitToit.setHidden(oldPignonDroitToit.getHidden());
            pignonDroitToit.setSelected(oldPignonDroitToit.getSelected());
        }

        if (oldPignonGaucheToit != null) {
            pignonGaucheToit.setVisible(oldPignonGaucheToit.getVisible());
            pignonGaucheToit.setDraggable(oldPignonGaucheToit.getDraggable());
            pignonGaucheToit.setSelectable(oldPignonGaucheToit.getSelectable());
            pignonGaucheToit.setHidden(oldPignonGaucheToit.getHidden());
            pignonGaucheToit.setSelected(oldPignonGaucheToit.getSelected());
        }

        // Set visible by default since the initial view is from the top
        if (getScene().getCamera().getDirection().x != -Math.PI / 2) {
            panneauToit.setVisible(true);
            rallongeVerticaleToit.setVisible(true);
            pignonDroitToit.setVisible(true);
            pignonGaucheToit.setVisible(true);
        } else {
            panneauToit.setVisible(false);
            rallongeVerticaleToit.setVisible(false);
            pignonDroitToit.setVisible(false);
            pignonGaucheToit.setVisible(false);
        }
        
        getScene().addMesh(panneauToit);
        getScene().addMesh(rallongeVerticaleToit);
        getScene().addMesh(pignonDroitToit);
        getScene().addMesh(pignonGaucheToit);
    }

    public void initializeView() {
        MurTriangleMeshGroup oldFacadeGroup = this.murFacadeGroup;
        MurTriangleMeshGroup oldArriereGroup = this.murArriereGroup;
        MurTriangleMeshGroup oldDroitGroup = this.murDroitGroup;
        MurTriangleMeshGroup oldGaucheGroup = this.murGaucheGroup;

        this.scene.clearMeshes();

        Chalet.ChaletDTO chaletDTO = this.getControleur().getChalet();
        PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO = this.getControleur()
                .getPreferencesUtilisateur();
        getScene().getConfiguration().setGridStep(preferencesUtilisateurDTO.gridSpacing);
        toggleShowGrid(preferencesUtilisateurDTO.afficherGrille);

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

        // Fix of last minute in order to transfert the previous state to the new meshes
        if (oldFacadeGroup != null) {
            murFacadeGroup.setVisible(oldFacadeGroup.getVisible());
            murFacadeGroup.setDraggable(oldFacadeGroup.getDraggable());
            murFacadeGroup.setSelectable(oldFacadeGroup.getSelectable());
            murFacadeGroup.setHidden(oldFacadeGroup.getHidden());
            murFacadeGroup.setSelected(oldFacadeGroup.getSelected());
        }

        if (oldArriereGroup != null) {
            murArriereGroup.setVisible(oldArriereGroup.getVisible());
            murArriereGroup.setDraggable(oldArriereGroup.getDraggable());
            murArriereGroup.setSelectable(oldArriereGroup.getSelectable());
            murArriereGroup.setHidden(oldArriereGroup.getHidden());
            murArriereGroup.setSelected(oldArriereGroup.getSelected());
        }

        if (oldDroitGroup != null) {
            murDroitGroup.setVisible(oldDroitGroup.getVisible());
            murDroitGroup.setDraggable(oldDroitGroup.getDraggable());
            murDroitGroup.setSelectable(oldDroitGroup.getSelectable());
            murDroitGroup.setHidden(oldDroitGroup.getHidden());
            murDroitGroup.setSelected(oldDroitGroup.getSelected());
        }

        if (oldGaucheGroup != null) {
            murGaucheGroup.setVisible(oldGaucheGroup.getVisible());
            murGaucheGroup.setDraggable(oldGaucheGroup.getDraggable());
            murGaucheGroup.setSelectable(oldGaucheGroup.getSelectable());
            murGaucheGroup.setHidden(oldGaucheGroup.getHidden());
            murGaucheGroup.setSelected(oldGaucheGroup.getSelected());
        }


        getScene().addMesh(murFacadeGroup);
        getScene().addMesh(murArriereGroup);
        getScene().addMesh(murDroitGroup);
        getScene().addMesh(murGaucheGroup);

        if (!controleur.getPreferencesUtilisateur().afficherVoisinSelection) {
            getScene().getMeshes().addAll(murFacadeGroup.getAccessoiresMeshes());
            getScene().getMeshes().addAll(murArriereGroup.getAccessoiresMeshes());
            getScene().getMeshes().addAll(murDroitGroup.getAccessoiresMeshes());
            getScene().getMeshes().addAll(murGaucheGroup.getAccessoiresMeshes());
        }

        // don't show roof when looking from the top
        // if (this.scene.getCamera().getDirection().x != -Math.PI / 2)
        sharedRoofCode(chaletDTO); // I split this out because it was a straight copy-paste

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

        MurTriangleMeshGroup oldFacadeGroup = this.murFacadeGroup;
        MurTriangleMeshGroup oldArriereGroup = this.murArriereGroup;
        MurTriangleMeshGroup oldDroitGroup = this.murDroitGroup;
        MurTriangleMeshGroup oldGaucheGroup = this.murGaucheGroup;


        Chalet.ChaletDTO chaletDTO = this.getControleur().getChalet();

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

        murFacadeGroup.updateAccessoires(murFacadeAccessoires);
        murArriereGroup.updateAccessoires(murArriereAccessoires);
        murDroitGroup.updateAccessoires(murDroitAccessoires);
        murGaucheGroup.updateAccessoires(murGaucheAccessoires);

        getScene().clearMeshes();

        // Fix of last minute in order to transfert the previous state to the new meshes
        if (oldFacadeGroup != null) {
            murFacadeGroup.setVisible(oldFacadeGroup.getVisible());
            murFacadeGroup.setDraggable(oldFacadeGroup.getDraggable());
            murFacadeGroup.setSelectable(oldFacadeGroup.getSelectable());
            murFacadeGroup.setHidden(oldFacadeGroup.getHidden());
            murFacadeGroup.setSelected(oldFacadeGroup.getSelected());
        }

        if (oldArriereGroup != null) {
            murArriereGroup.setVisible(oldArriereGroup.getVisible());
            murArriereGroup.setDraggable(oldArriereGroup.getDraggable());
            murArriereGroup.setSelectable(oldArriereGroup.getSelectable());
            murArriereGroup.setHidden(oldArriereGroup.getHidden());
            murArriereGroup.setSelected(oldArriereGroup.getSelected());
        }

        if (oldDroitGroup != null) {
            murDroitGroup.setVisible(oldDroitGroup.getVisible());
            murDroitGroup.setDraggable(oldDroitGroup.getDraggable());
            murDroitGroup.setSelectable(oldDroitGroup.getSelectable());
            murDroitGroup.setHidden(oldDroitGroup.getHidden());
            murDroitGroup.setSelected(oldDroitGroup.getSelected());
        }

        if (oldGaucheGroup != null) {
            murGaucheGroup.setVisible(oldGaucheGroup.getVisible());
            murGaucheGroup.setDraggable(oldGaucheGroup.getDraggable());
            murGaucheGroup.setSelectable(oldGaucheGroup.getSelectable());
            murGaucheGroup.setHidden(oldGaucheGroup.getHidden());
            murGaucheGroup.setSelected(oldGaucheGroup.getSelected());
        }
        

        getScene().addMesh(murFacadeGroup);
        getScene().addMesh(murArriereGroup);
        getScene().addMesh(murDroitGroup);
        getScene().addMesh(murGaucheGroup);

        // getScene().getMeshes().addAll(murFacadeGroup.getAccessoiresMeshes());
        // getScene().getMeshes().addAll(murArriereGroup.getAccessoiresMeshes());
        // getScene().getMeshes().addAll(murDroitGroup.getAccessoiresMeshes());
        // getScene().getMeshes().addAll(murGaucheGroup.getAccessoiresMeshes());

        if (!controleur.getPreferencesUtilisateur().afficherVoisinSelection) {
            getScene().getMeshes().addAll(murFacadeGroup.getAccessoiresMeshes());
            getScene().getMeshes().addAll(murArriereGroup.getAccessoiresMeshes());
            getScene().getMeshes().addAll(murDroitGroup.getAccessoiresMeshes());
            getScene().getMeshes().addAll(murGaucheGroup.getAccessoiresMeshes());
        }

        murFacadeGroup.update(chaletDTO);
        murArriereGroup.update(chaletDTO);
        murDroitGroup.update(chaletDTO);
        murGaucheGroup.update(chaletDTO);

        // a bit jank but i'll keep it I guess
        // if (this.scene.getCamera().getDirection().x != -Math.PI / 2)
        //     sharedRoofCode(chaletDTO);

        sharedRoofCode(chaletDTO);

        PreferencesUtilisateur.PreferencesUtilisateurDTO preferencesUtilisateurDTO = this.controleur
                .getPreferencesUtilisateur();
        getScene().getConfiguration().setGridStep(preferencesUtilisateurDTO.gridSpacing);
        toggleShowGrid(preferencesUtilisateurDTO.afficherGrille);

        updateViewGrid();
        drawingPanel.repaint();
    }

    public void exportStlToit(String directoryPath, String nomChalet) {
        List<STLTools.Triangle> pignonDroitStlTriangles = PanelHelper.convertMeshTrianglesToStlTriangles(pignonDroitToit.getMesh(0).getTriangles());
        List<STLTools.Triangle> pignonGaucheStlTriangles = PanelHelper.convertMeshTrianglesToStlTriangles(pignonGaucheToit.getMesh(0).getTriangles());
        List<STLTools.Triangle> panneauStlTriangles = PanelHelper.convertMeshTrianglesToStlTriangles(panneauToit.getMesh(0).getTriangles());
        List<STLTools.Triangle> rallongeVerticaleStlTriangles = PanelHelper.convertMeshTrianglesToStlTriangles(rallongeVerticaleToit.getMesh(0).getTriangles());

        String pignonDroitFileName = String.format("\\%s_Fini_PignonDroit.stl", nomChalet);
        String pignonGaucheFileName = String.format("\\%s_Fini_PignonGauche.stl", nomChalet);
        String panneauFileName = String.format("\\%s_Fini_Panneau.stl", nomChalet);
        String rallongeVerticaleFileName = String.format("\\%s_Fini_RallongeVerticale.stl", nomChalet);
        
        STLTools.writeSTL(pignonDroitStlTriangles, directoryPath + pignonDroitFileName);
        STLTools.writeSTL(pignonGaucheStlTriangles, directoryPath + pignonGaucheFileName);
        STLTools.writeSTL(panneauStlTriangles, directoryPath + panneauFileName);
        STLTools.writeSTL(rallongeVerticaleStlTriangles, directoryPath + rallongeVerticaleFileName);
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

        exportStlToit(directoryPath, nomChalet);
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
                    eventSupport.dispatchZoomIn();
                } else {
                    scene.getCamera().zoomOutDirection(evt.getPoint(), ((JPanel) evt.getSource()).getSize(),
                            evt.isShiftDown());
                    eventSupport.dispatchZoomOut();
                }

                drawingPanel.repaint();
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

            TriangleMesh focusedMesh = null;
            Vector3D initialFocusedMeshPosition = null;

            boolean isShiftDown = false;
            boolean isTempRotate = false;

            protected void reset() {
                initialPoint = null;
                isDragging = false;
                initialDragCamPosition = null;
                initialDragCamDirection = null;
                focusedMesh = null;
                initialFocusedMeshPosition = null;
                isShiftDown = false;
                isTempRotate = false;
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
                    getEventSupport().dispatchSelectionChanged(new AfficheurEventSupport.MeshSelectionEvent(
                                    getSelection()));
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
                    handleMousePressed(evt);
                }

                @Override
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    if (isTempRotate) {
                        setMouseControl(MouseControl.Move);
                        isTempRotate = false;
                    }

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

                    if (evt.isShiftDown() && mouseControl != MouseControl.Rotate) {
                        isTempRotate = true;
                        setMouseControl(MouseControl.Rotate);
                    } else if (isTempRotate && !evt.isShiftDown()) {
                        setMouseControl(MouseControl.Move);
                        isTempRotate = false;
                    }

                    // Rotating when shift is pressed
                    if (mouseControl == MouseControl.Rotate) {
                        // if (evt.isShiftDown() && mouseControl != MouseControl.Rotate) setMouseControl(MouseControl.Rotate);

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
                        if (direction.x == -Math.PI / 2) {
                            panneauToit.setVisible(false);
                            rallongeVerticaleToit.setVisible(false);
                            pignonDroitToit.setVisible(false);
                            pignonGaucheToit.setVisible(false);
                        } else {
                            panneauToit.setVisible(true);
                            rallongeVerticaleToit.setVisible(true);
                            pignonDroitToit.setVisible(true);
                            pignonGaucheToit.setVisible(true);
                        }
                    } else {
                        if (!evt.isShiftDown() && mouseControl != MouseControl.Move) setMouseControl(MouseControl.Move);
                        
                        Vector3D position = initialDragCamPosition.add(new Vector3D(diffX, diffY, 0));
                        getScene().getCamera().setPosition(position);
                    }
                    isShiftDown = evt.isShiftDown();
                    drawingPanel.repaint();
                }
            }

            @Override
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                if (evt.isShiftDown()) {
                    setMouseControl(MouseControl.Rotate);
                    isTempRotate = true;
                } else if (isTempRotate) {
                    setMouseControl(MouseControl.Move);
                    isTempRotate = false;
                }

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

}
