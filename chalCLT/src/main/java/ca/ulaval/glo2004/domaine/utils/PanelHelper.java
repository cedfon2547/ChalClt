package ca.ulaval.glo2004.domaine.utils;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMesh;

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
    public static List<double[][]> buildWall(double[] position, double[] dimension, boolean truncate) {
        List<double[][]> triangles = new ArrayList<double[][]>();
        int x1 = (int) position[0];
        int y1 = (int) position[1];
        int z1 = (int) position[2];

        int d = (int) dimension[2];
        int w = (int) dimension[0] - d;
        int h = (int) dimension[1];

        if (truncate) {
            w -= d;
        }

        // Top face
        // Rainure 1
        triangles.add(new double[][] {
                { x1, y1, z1 },
                { x1, y1, z1 + (d / 2) },
                { x1 + (d / 2), y1, z1 + (d / 2) }
        });

        triangles.add(new double[][] {
                { x1, y1, z1 },
                { x1 + (d / 2), y1, z1 + (d / 2) },
                { x1 + (d / 2), y1, z1 }
        });

        // Rainure 2
        triangles.add(new double[][] {
                { x1 + w, y1, z1 },
                { x1 + w - (d / 2), y1, z1 + (d / 2) },
                { x1 + w, y1, z1 + (d / 2) }
        });

        triangles.add(new double[][] {
                { x1 + w, y1, z1 },
                { x1 + w - (d / 2), y1, z1 },
                { x1 + w - (d / 2), y1, z1 + (d / 2) }
        });

        // Center
        triangles.add(new double[][] {
                { x1 + (d / 2), y1, z1 },
                { x1 + w - (d / 2), y1, z1 + d },
                { x1 + w - (d / 2), y1, z1 } });

        triangles.add(new double[][] {
                { x1 + (d / 2), y1, z1 },
                { x1 + (d / 2), y1, z1 + d },
                { x1 + w - (d / 2), y1, z1 + d } });

        // Bottom face
        // Rainure 1
        triangles.add(new double[][] {
                { x1, y1 + h, z1 },
                { x1, y1 + h, z1 + (d / 2) },
                { x1 + (d / 2), y1 + h, z1 + (d / 2) }
        });

        triangles.add(new double[][] {
                { x1, y1 + h, z1 },
                { x1 + (d / 2), y1 + h, z1 + (d / 2) },
                { x1 + (d / 2), y1 + h, z1 }
        });

        // Rainure 2
        triangles.add(new double[][] {
                { x1 + w, y1 + h, z1 },
                { x1 + w - (d / 2), y1 + h, z1 + (d / 2) },
                { x1 + w, y1 + h, z1 + (d / 2) }
        });

        triangles.add(new double[][] {
                { x1 + w, y1 + h, z1 },
                { x1 + w - (d / 2), y1 + h, z1 },
                { x1 + w - (d / 2), y1 + h, z1 + (d / 2) }
        });

        // Center
        triangles.add(new double[][] {
                { x1 + (d / 2), y1 + h, z1 },
                { x1 + w - (d / 2), y1 + h, z1 + d },
                { x1 + w - (d / 2), y1 + h, z1 }
        });

        triangles.add(new double[][] {
                { x1 + (d / 2), y1 + h, z1 },
                { x1 + (d / 2), y1 + h, z1 + d },
                { x1 + w - (d / 2), y1 + h, z1 + d }
        });

        // Back face
        triangles.add(new double[][] {
                { x1, y1, z1 },
                { x1 + w, y1, z1 },
                { x1 + w, y1 + h, z1 }
        });

        triangles.add(new double[][] {
                { x1, y1, z1 },
                { x1 + w, y1 + h, z1 },
                { x1, y1 + h, z1 }
        });

        // Front face
        triangles.add(new double[][] {
                { x1 + (d / 2), y1, z1 + d },
                { x1 + w - (d / 2), y1, z1 + d },
                { x1 + w - (d / 2), y1 + h, z1 + d }
        });

        triangles.add(new double[][] {
                { x1 + (d / 2), y1, z1 + d },
                { x1 + w - (d / 2), y1 + h, z1 + d },
                { x1 + (d / 2), y1 + h, z1 + d } });
        
        // Front rainure face
        triangles.add(new double[][] {
                { x1, y1, z1 + (d / 2) },
                { x1 + (d / 2), y1, z1 + (d / 2) },
                { x1 + (d / 2), y1 + h, z1 + (d / 2) } });

        triangles.add(new double[][] {
                { x1, y1, z1 + (d / 2) },
                { x1 + (d / 2), y1 + h, z1 + (d / 2) },
                { x1, y1 + h, z1 + (d / 2) }
        });

        // Back rainure face
        triangles.add(new double[][] {
                { x1 + w, y1, z1 + (d / 2) },
                { x1 + w - (d / 2), y1, z1 + (d / 2) },
                { x1 + w - (d / 2), y1 + h, z1 + (d / 2) }
        });

        triangles.add(new double[][] {
                { x1 + w, y1, z1 + (d / 2) },
                { x1 + w - (d / 2), y1 + h, z1 + (d / 2) },
                { x1 + w, y1 + h, z1 + (d / 2) }
        });

        // Left rainure face
        triangles.add(new double[][] {
                { x1, y1, z1 },
                { x1, y1, z1 + (d / 2) },
                { x1, y1 + h, z1 + (d / 2) }
        });

        triangles.add(new double[][] {
                { x1, y1, z1 },
                { x1, y1 + h, z1 + (d / 2) },
                { x1, y1 + h, z1 }
        });

        triangles.add(new double[][] {
                { x1 + (d / 2), y1, z1 + (d / 2) },
                { x1 + (d / 2), y1, z1 + d },
                { x1 + (d / 2), y1 + h, z1 + d }
        });

        triangles.add(new double[][] {
                { x1 + (d / 2), y1, z1 + (d / 2) },
                { x1 + (d / 2), y1 + h, z1 + d },
                { x1 + (d / 2), y1 + h, z1 + (d / 2) }
        });

        // Right rainure face
        triangles.add(new double[][] {
                { x1 + w, y1, z1 },
                { x1 + w, y1, z1 + (d / 2) },
                { x1 + w, y1 + h, z1 + (d / 2) }
        });

        triangles.add(new double[][] {
                { x1 + w, y1, z1 },
                { x1 + w, y1 + h, z1 + (d / 2) },
                { x1 + w, y1 + h, z1 }
        });

        triangles.add(new double[][] {
                { x1 + w - (d / 2), y1, z1 + (d / 2) },
                { x1 + w - (d / 2), y1, z1 + d },
                { x1 + w - (d / 2), y1 + h, z1 + d }
        });

        triangles.add(new double[][] {
                { x1 + w - (d / 2), y1, z1 + (d / 2) },
                { x1 + w - (d / 2), y1 + h, z1 + d },
                { x1 + w - (d / 2), y1 + h, z1 + (d / 2) }
        });

        return triangles;
    }

    public static TriangleMesh[] generateMeshMurs(double largeur, double hauteur, double longueur,
            double epaisseurMur) {
        double[] position = new double[] { 0, 0, 0 };

        List<double[][]> trianglesFacade = PanelHelper.buildWall(position,
                new double[] { largeur, hauteur, epaisseurMur }, false);
        List<double[][]> trianglesArriere = PanelHelper.buildWall(position,
                new double[] { largeur, hauteur, epaisseurMur }, false);
        List<double[][]> trianglesDroite = PanelHelper.buildWall(position,
                new double[] { longueur, hauteur, epaisseurMur }, true);
        List<double[][]> trianglesGauche = PanelHelper.buildWall(position,
                new double[] { longueur, hauteur, epaisseurMur }, true);

        TriangleMesh murFacade = TriangleMesh.fromDoubleList(trianglesFacade);
        TriangleMesh murArriere = TriangleMesh.fromDoubleList(trianglesArriere);
        TriangleMesh murDroite = TriangleMesh.fromDoubleList(trianglesDroite);
        TriangleMesh murGauche = TriangleMesh.fromDoubleList(trianglesGauche);

        murFacade.setHandle("facade");
        murArriere.setHandle("arriere");
        murDroite.setHandle("droite");
        murGauche.setHandle("gauche");

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

        murFacade = murFacade.translate(new Vector3D(0, 0, -longueur / 2 + epaisseurMur));
        murArriere = murArriere.translate(new Vector3D(0, 0, longueur / 2 - epaisseurMur));
        murDroite = murDroite.translate(new Vector3D(-largeur / 2 + epaisseurMur, 0, 0));
        murGauche = murGauche.translate(new Vector3D(largeur / 2 - epaisseurMur, 0, 0));

        return new TriangleMesh[] { murFacade, murArriere, murDroite, murGauche };
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
                    new double[] { this.dimension[0] - (position[0] - this.position[0]) - dimension[0], dimension[1] },
                    this.axis);
            RectPlane2D rect4 = new RectPlane2D(new double[] { this.position[0], position[1] + dimension[1] },
                    new double[] { this.dimension[0],
                            this.dimension[1] - (position[1] - this.position[1]) - dimension[1] },
                    this.axis);
            
            rect1.triangles.addAll(rect2.triangles);
            rect1.triangles.addAll(rect3.triangles);
            rect1.triangles.addAll(rect4.triangles);

            return rect1;
        }
    }
}
