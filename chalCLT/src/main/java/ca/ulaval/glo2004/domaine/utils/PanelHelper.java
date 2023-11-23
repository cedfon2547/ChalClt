package ca.ulaval.glo2004.domaine.utils;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMesh;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMeshGroup;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.shapes.RectCuboid;
import ca.ulaval.glo2004.gui.Constants;

import java.awt.Color;
import java.awt.Dimension;

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
                double d = epaisseurMur, m = margeSupplementaire;

                // Si truncate, la longueur du mur est diminuée de l'epaisseur du mur
                // et on décale le mur de d/2
                if (truncate) {
                        largeur -= d;
                        // x0 += d / 2;
                }

                // Si on doit retirer la marge, on fait -m, sinon on fait +m
                m = -m;
                // largeur -= m * 2;

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
                triangles.add(new double[][] { p4, p13, p5  });
                triangles.add(new double[][] { p12, p13, p4  });

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
}
