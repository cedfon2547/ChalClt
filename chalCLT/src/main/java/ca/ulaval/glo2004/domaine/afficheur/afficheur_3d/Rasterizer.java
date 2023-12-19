package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.Timer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GradientPaint;
import java.awt.Paint;

import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Matrix;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base.Vector3D;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.Triangle;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMesh;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.mesh.TriangleMeshGroup;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene.Light;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene.Scene;
import ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.scene.LightModel.PhongLightModel;

public class Rasterizer {
    private Scene scene; // Contains the scene to draw
    BufferedImage image; // Contains the final image
    double[] zBuffer; // Contains the depth of the pixel
    String[] idBuffer; // Contains the id of the mesh that is drawn at the pixel
    public Matrix cameraTransformMatrix = new Matrix(); // Contains the transform matrix of the camera
    PhongLightModel lightModel = new PhongLightModel(); // Represent the light model used to calculate the color of the
                                                        // pixel

    private int nThreads = Runtime.getRuntime().availableProcessors(); // Set to 0 in order to not use
    // multithreading
    private ExecutorService executor; // Executor used to run the rasterization in parallel
    private List<Future<BufferedImage>> futures = new ArrayList<>(); // Contains the futures of the rasterization for
                                                                     // each parts of the image

    // Used to calculate the framerate
    private Timer timer;
    private int fps = 0;
    private int frameCount = 0;

    public Rasterizer() {
        this(new Scene(), Runtime.getRuntime().availableProcessors());
    }

    public Rasterizer(Scene scene) {
        this(scene, Runtime.getRuntime().availableProcessors());
    }

    public Rasterizer(Scene scene, int nThread) {
        this.nThreads = nThread;
        this.scene = scene;

        this.executor = this.nThreads > 0 ? Executors.newFixedThreadPool(this.nThreads) : null;

        this.toggleRecordFramerate(true);
    }

    public void toggleRecordFramerate(boolean toggle) {
        if (toggle) {
            if (this.timer == null) {
                this.timer = new Timer(1000, (e) -> {
                    this.fps = this.frameCount;
                    this.resetFrameCount();
                });

                this.timer.setRepeats(true);
                this.timer.setInitialDelay(1000);
                this.timer.start();

                return;
            }

            this.timer.restart();
        } else {
            if (this.timer == null)
                return;

            this.timer.stop();
        }
    }

    private void incrementFrameCount() {
        this.frameCount++;
    }

    private void resetFrameCount() {
        this.frameCount = 0;
    }

    private BufferedImage rasterizePart(int part, Dimension dimension, int totalParts, int startY, int endY) {
        if (image == null) {
            image = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
        }

        BufferedImage partImage = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) partImage.getGraphics();
        // g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING,
        // RenderingHints.VALUE_ANTIALIAS_ON));

        // g2.setComposite(AlphaComposite.SrcOver);
        
        for (TriangleMeshGroup group : scene.getMeshes()) {
            if (!group.getVisible() || group.getHidden())
                continue;
            
            for (TriangleMesh obj : group.getMeshes()) {
                if (!obj.getVisible() || obj.getHidden())
                    continue;
                
                for (Triangle triangle : obj.getTriangles()) {
                    Vector3D[] vertices = triangle.getVertices();

                    Vector3D vertex1 = vertices[0];
                    Vector3D vertex2 = vertices[1];
                    Vector3D vertex3 = vertices[2];

                    vertex1 = vertex1.add(group.getPosition()).multiply(cameraTransformMatrix);
                    vertex2 = vertex2.add(group.getPosition()).multiply(cameraTransformMatrix);
                    vertex3 = vertex3.add(group.getPosition()).multiply(cameraTransformMatrix);

                    int minY = (int) Math.max(0,
                            Math.ceil(Math.min(vertex1.y, Math.min(vertex2.y, vertex3.y))));
                    int maxY = (int) Math.min(partImage.getHeight() - 1,
                            Math.floor(Math.max(vertex1.y,
                                    Math.max(vertex2.y, vertex3.y))));

                    // Calculate the bounding box
                    int minX = (int) Math.max(0, Math.min(Math.min(vertex1.x, vertex2.x), vertex3.x));
                    int maxX = (int) Math.min(dimension.width - 1, Math.max(Math.max(vertex1.x, vertex2.x), vertex3.x));

                    if (minY < startY)
                        minY = startY;
                    if (maxY > endY)
                        maxY = endY;
                    if (maxY < startY || minY > endY) {
                        // System.out.println("SKIP A");
                        continue;
                    }

                    double triangleArea = (vertex1.y - vertex3.y) * (vertex2.x - vertex3.x)
                            + (vertex2.y - vertex3.y) * (vertex3.x - vertex1.x);
                    Vector3D normal = triangle.getNormal().normalize();

                    for (int y = minY; y <= maxY; y++) {
                        for (int x = minX; x <= maxX; x++) {
                            double b1 = ((y - vertex3.y) * (vertex2.x - vertex3.x)
                                    + (vertex2.y - vertex3.y) * (vertex3.x - x))
                                    / triangleArea;
                            double b2 = ((y - vertex1.y) * (vertex3.x - vertex1.x)
                                    + (vertex3.y - vertex1.y) * (vertex1.x - x))
                                    / triangleArea;
                            double b3 = ((y - vertex2.y) * (vertex1.x - vertex2.x)
                                    + (vertex1.y - vertex2.y) * (vertex2.x - x))
                                    / triangleArea;

                            boolean isInside = b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1
                                    && b3 >= 0
                                    && b3 < 1;

                            if (isInside) {
                                double depth = b1 * (vertex1.z
                                        - scene.getCamera().getPosition().z)
                                        + b2 * (vertex2.z - scene.getCamera()
                                                .getPosition().z)
                                        + b3 * (vertex3.z - scene.getCamera()
                                                .getPosition().z);
                                int zIndex = y * image.getWidth() + x;

                                if (zIndex >= zBuffer.length) {
                                    return null;
                                }

                                if (zBuffer[zIndex] < depth) {
                                    zBuffer[zIndex] = depth;
                                    idBuffer[zIndex] = group.getIdentifier();

                                    Color finalColor = lightModel.calculateColor(obj.getMaterial(),
                                            scene.getCamera(),
                                            normal, new Vector3D(x, y, depth), scene.getLight());
                                    g2.setColor(finalColor);
                                    g2.fillRect(x, y, 1, 1);
                                }

                                if (zBuffer[zIndex] < depth) {
                                    zBuffer[zIndex] = depth;
                                    idBuffer[zIndex] = group.getIdentifier();

                                }
                            }
                        }
                    }
                }
            }
        }

        // System.out.println("Rasterize time: " + (t2 - t1) + "ms");
        return partImage;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void resizeImage(Dimension dimension) {
        image = new BufferedImage(dimension.width, dimension.height,
                BufferedImage.TYPE_INT_ARGB);
        zBuffer = new double[image.getWidth() * image.getHeight()];
        Arrays.fill(zBuffer, Double.NEGATIVE_INFINITY);

        idBuffer = new String[image.getWidth() * image.getHeight()];
        Arrays.fill(idBuffer, null);
    }

    private void clearPreviousFutures() {
        for (Future<BufferedImage> future : futures) {
            future.cancel(true);
        }

        futures.clear();
    }

    private void clearImage() {
        if (image == null) {
            return;
        }

        image.getGraphics().clearRect(0, 0, image.getWidth(), image.getHeight());

        zBuffer = new double[image.getWidth() * image.getHeight()];
        Arrays.fill(zBuffer, Double.NEGATIVE_INFINITY);

        idBuffer = new String[image.getWidth() * image.getHeight()];
        Arrays.fill(idBuffer, null);
    }

    public void draw(Dimension panelDimension) {
        // long t1 = System.nanoTime();
        this.incrementFrameCount();

        cameraTransformMatrix = this.scene.getCamera().perspectiveMatrix.multiply(scene.getCamera().transform());

        if (panelDimension.getWidth() == 0 || panelDimension.getHeight() == 0) {
            return;
        }

        if (image == null && panelDimension.width != 0 && panelDimension.height != 0) {
            // If the BufferedImage was not initialized and the panel dimension values are
            // not 0, we can initialize the image.
            image = new BufferedImage(panelDimension.width, panelDimension.height, BufferedImage.TYPE_INT_RGB);
        } else if (image.getWidth() != panelDimension.width || image.getHeight() != panelDimension.height) {
            // If the BufferedImage was initialized but the panel dimension values was
            // changed, we need to resize the image.
            image = new BufferedImage(panelDimension.width, panelDimension.height,
                    BufferedImage.TYPE_INT_RGB);
        } else {
            this.clearImage();
        }

        if (zBuffer == null) {
            zBuffer = new double[panelDimension.width * panelDimension.height];
            Arrays.fill(zBuffer, Double.NEGATIVE_INFINITY);
        }

        if (idBuffer == null) {
            idBuffer = new String[panelDimension.width * panelDimension.height];
            Arrays.fill(idBuffer, null);
        }

        if (nThreads > 0) {
            if (executor == null) {
                executor = Executors.newFixedThreadPool(nThreads);
            } else {
                // Cancel already running futures
                clearPreviousFutures();
            }

            // List containing the images of each parts of the main image
            List<BufferedImage> images = new ArrayList<>();

            // The parts are split on the y axis. Each part are the width of the main
            // image but the height is divided by the number of threads.
            int partHeight = panelDimension.height / (nThreads);

            // For each part, we create a future that will run the rasterization for the
            // specific image part.
            for (int part = 0; part < nThreads; part++) {
                int startY = part * partHeight;
                int endY = (part + 1) * partHeight - 1;

                // If the part is the last one, we need to set the endY to the height of the
                // main image.
                if (part == nThreads -1) {
                    endY = panelDimension.height;
                }

                final int _part = part;
                final int _endY = endY;

                // We submit the rasterization of the part to the executor.
                Future<BufferedImage> future = executor
                        .submit(() -> rasterizePart(_part, panelDimension, nThreads, startY, _endY));
                futures.add(future); // We add the future to the list of futures.
            }

            // We wait for each future to complete and we add the subimage representing the
            // part to the list of images.
            for (Future<BufferedImage> future : futures) {
                try {
                    if (future == null) {
                        continue;
                    }

                    // Not really useful since the main thread is waiting for the future to
                    // complete.
                    BufferedImage partImage = future.get(); // We wait for the future to complete.

                    if (partImage == null) {
                        continue;
                    }

                    images.add(partImage);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            Graphics2D g2 = (Graphics2D) image.getGraphics();

            Color skyColorTransparent = new Color(50, 50, 50, 255);
            Color skyColorOpaque = new Color(75, 75, 75, 255);
            Paint skyColor = new GradientPaint(180.0f, 0.0f, skyColorTransparent,
                    image.getWidth(), image.getHeight(), skyColorOpaque, true);
            g2.setPaint(skyColor);
            g2.fillRect(0, 0, (int) panelDimension.getWidth(), (int) panelDimension.getHeight());

            try {
                if (this.scene.getConfiguration().getShowGridYZ()) {
                    this.drawGridYZ((Graphics2D) g2, cameraTransformMatrix);
                }

                if (this.scene.getConfiguration().getShowGridXY()) {
                    this.drawGridXY((Graphics2D) g2, cameraTransformMatrix);
                }

                if (this.scene.getConfiguration().getShowGridXZ()) {
                    this.drawGridXZ((Graphics2D) g2, cameraTransformMatrix);
                }

                if (this.scene.getConfiguration().getShowAxis()) {
                    this.drawAxes(g2, cameraTransformMatrix);
                }

                images.forEach((img) -> {
                    image.getGraphics().drawImage(img, 0, 0, null);
                });

                this.drawInvalidMeshBounding(g2);
                this.drawSelectedMeshBounding(g2);
                this.drawDraggedMeshBounding(g2);
            } catch (Exception e) {

            }
        } else {
            // If the number of threads is 0, we rasterize the image in the main thread.
            image = this.rasterizePart(0, panelDimension, 1, 0, panelDimension.height);
            Graphics2D g2 = (Graphics2D) image.getGraphics();

            if (this.scene.getConfiguration().getShowGridXY()) {
                this.drawGridXZ((Graphics2D) g2, cameraTransformMatrix);
            }

            if (this.scene.getConfiguration().getShowGridYZ()) {
                this.drawGridYZ((Graphics2D) g2, cameraTransformMatrix);
            }

            if (this.scene.getConfiguration().getShowGridXY()) {
                this.drawGridXY((Graphics2D) g2, cameraTransformMatrix);
            }

            if (this.scene.getConfiguration().getShowAxis()) {
                this.drawAxes(g2, cameraTransformMatrix);
            }

            Color skyColorTransparent = new Color(116, 147, 170, 255);
            Color skyColorOpaque = new Color(49, 73, 111, 255);
            Paint skyColor = new GradientPaint(180.0f, 0.0f, skyColorTransparent,
                    image.getWidth(), image.getHeight(), skyColorOpaque, true);
            g2.setPaint(skyColor);
            g2.fillRect(0, 0, (int) panelDimension.getWidth(), (int) panelDimension.getHeight());

            this.drawInvalidMeshBounding(g2);
            this.drawSelectedMeshBounding(g2);
            this.drawDraggedMeshBounding(g2);
        }
    }

    private void drawSelectedMeshBounding(Graphics2D g2) {
        int stroke = 1; // (int) Math.floor(1 * scene.getCamera().getScale());
        g2.setColor(scene.getConfiguration().getSelectionColor());

        if (stroke >= image.getWidth() && stroke >= image.getHeight()) {
            g2.fillRect(0, 0, stroke, stroke);
            return;
        }

        for (int y = 0; y < image.getHeight(); y++) {
            // We do not draw pixel on the edge of the image
            if (y == 0 || y >= image.getHeight())
                continue;

            for (int x = 0; x < image.getWidth(); x++) {
                if (x == 0 || x >= image.getWidth() - 1)
                    continue; // We do not draw pixel on the edge of the image

                final int depth = y * image.getWidth() + x;
                TriangleMesh mesh = scene.getMesh(idBuffer[depth]);

                if (idBuffer[depth] != null && mesh.getSelected()) {
                    if (!mesh.getValid()) {
                        g2.setColor(new Color(255, 0, 0, 150));
                    } else if (mesh.getIsDragged()) {
                       g2.setColor(Color.GREEN);
                    } else {
                        g2.setColor(scene.getConfiguration().getSelectionColor());
                    }
                    // Check if the id of the pixel is the same on top, left, right and bottom.
                    // If not, it means that the pixel is on the edge of a mesh.
                    // So we draw the pixel in order to create a boundary
                    if (depth <= 0)
                        continue;
                    if (depth + stroke > image.getWidth() * image.getHeight() - 1)
                        continue;

                    int topPixelDepth = (y - 1) * image.getWidth() + x;
                    int leftPixelDepth = y * image.getWidth() + (x - 1);
                    int rightPixelDepth = y * image.getWidth() + (x + 1);
                    int bottomPixelDepth = (y + 1) * image.getWidth() + x;

                    if (topPixelDepth >= 0 && idBuffer[topPixelDepth] != idBuffer[depth]) {
                        g2.drawRect(x, y, stroke, stroke);
                    }

                    if (leftPixelDepth >= 0 && leftPixelDepth <= idBuffer.length
                            && idBuffer[leftPixelDepth] != idBuffer[depth]) {
                        g2.drawRect(x, y, stroke, stroke);
                        continue;
                    }

                    if (idBuffer[rightPixelDepth] != idBuffer[depth]) {
                        g2.drawRect(x, y, stroke, stroke);
                        continue;
                    }

                    if (bottomPixelDepth < idBuffer.length && idBuffer[bottomPixelDepth] != idBuffer[depth]) {
                        g2.drawRect(x, y, stroke, stroke);
                        continue;
                    }
                }
            }
        }
    }

    private void drawInvalidMeshBounding(Graphics2D g2) {
        int stroke = 1;
        // stroke = (int) Math.round(1 * scene.getCamera().getScale());

        for (int y = 0; y < image.getHeight(); y++) {
            if (y < stroke || y > image.getHeight() + stroke)
                continue; // We do not draw pixel on the edge of the image

            for (int x = 0; x < image.getWidth(); x++) {
                if (x == 0 || x > image.getWidth() + stroke)
                    continue; // We do not draw pixel on the edge of the image

                final int depth = y * image.getWidth() + x;

                if (depth <= 0 || depth + 1 > image.getWidth() * image.getHeight() - 1 || idBuffer[depth] == null)
                    continue;

                TriangleMesh mesh = scene.getMesh(idBuffer[depth]);

                if (idBuffer[depth] != null && mesh != null && !mesh.getValid()) {

                    // fill invalid accessories
                    g2.setColor(new Color(255, 0, 0, 100));
                    g2.fillRect(x, y, 1, 1);
                    if (mesh.getSelected())
                        continue;

                    // Check if the id of the pixel is the same on top, left, right and bottom.
                    // If not, it means that the pixel is on the edge of a mesh.
                    // So we draw the pixel in order to create a boundary
                    int topPixelDepth = (y - 1) * image.getWidth() + x;
                    int leftPixelDepth = y * image.getWidth() + (x - 1);
                    int rightPixelDepth = y * image.getWidth() + (x + 1);
                    int bottomPixelDepth = (y + 1) * image.getWidth() + x;

                    // draw border
                    g2.setColor(new Color(255, 0, 0, 150));

                    if (topPixelDepth >= 0 && idBuffer[topPixelDepth] != idBuffer[depth]) {
                        g2.drawRect(x, y, stroke, stroke);
                        continue;
                    }

                    if (leftPixelDepth >= 0 && leftPixelDepth <= idBuffer.length
                            && idBuffer[leftPixelDepth] != idBuffer[depth]) {
                        g2.drawRect(x, y, stroke, stroke);
                        continue;
                    }

                    if (idBuffer[rightPixelDepth] != idBuffer[depth]) {
                        g2.drawRect(x, y, stroke, stroke);
                        continue;
                    }

                    if (bottomPixelDepth < idBuffer.length && idBuffer[bottomPixelDepth] != idBuffer[depth]) {
                        g2.drawRect(x, y, stroke, stroke);
                        continue;
                    }
                }
            }
        }
    }

    private void drawDraggedMeshBounding(Graphics2D g2) {
        int stroke = 1;
        // stroke = (int) Math.round(1 * scene.getCamera().getScale());

        for (int y = 0; y < image.getHeight(); y++) {
            if (y < stroke || y > image.getHeight() + stroke)
                continue; // We do not draw pixel on the edge of the image

            for (int x = 0; x < image.getWidth(); x++) {
                if (x == 0 || x > image.getWidth() + stroke)
                    continue; // We do not draw pixel on the edge of the image

                final int depth = y * image.getWidth() + x;

                if (depth <= 0 || depth + 1 > image.getWidth() * image.getHeight() - 1 || idBuffer[depth] == null)
                    continue;

                TriangleMesh mesh = scene.getMesh(idBuffer[depth]);

                if (idBuffer[depth] != null && mesh != null && mesh.getIsDragged()) {

                    // fill? dragged accessory
                    g2.setColor(new Color(0, 255, 0, 100));
                    g2.fillRect(x, y, 1, 1);

                    // Check if the id of the pixel is the same on top, left, right and bottom.
                    // If not, it means that the pixel is on the edge of a mesh.
                    // So we draw the pixel in order to create a boundary
                    int topPixelDepth = (y - 1) * image.getWidth() + x;
                    int leftPixelDepth = y * image.getWidth() + (x - 1);
                    int rightPixelDepth = y * image.getWidth() + (x + 1);
                    int bottomPixelDepth = (y + 1) * image.getWidth() + x;

                    // draw border
                    g2.setColor(new Color(0, 255, 0, 150));

                    if (topPixelDepth >= 0 && idBuffer[topPixelDepth] != idBuffer[depth]) {
                        g2.drawRect(x, y, stroke, stroke);
                        continue;
                    }

                    if (leftPixelDepth >= 0 && leftPixelDepth <= idBuffer.length
                            && idBuffer[leftPixelDepth] != idBuffer[depth]) {
                        g2.drawRect(x, y, stroke, stroke);
                        continue;
                    }

                    if (idBuffer[rightPixelDepth] != idBuffer[depth]) {
                        g2.drawRect(x, y, stroke, stroke);
                        continue;
                    }

                    if (bottomPixelDepth < idBuffer.length && idBuffer[bottomPixelDepth] != idBuffer[depth]) {
                        g2.drawRect(x, y, stroke, stroke);
                        continue;
                    }
                }
            }
        }
    }

    private int MAX_AXIS_LENGTH = 5000;

    private void drawAxes(Graphics2D g2, Matrix transform) {
        int stepCounts = (int) (MAX_AXIS_LENGTH / scene.getConfiguration().getGridStep());
        int axisLength = (int) Math
                .floor(scene.getConfiguration().getGridStep() * stepCounts);

        g2.setStroke(new BasicStroke(scene.getConfiguration().getAxisStrokeWidth()));

        Vector3D xAxis1 = new Vector3D(-axisLength, 0, 0);
        Vector3D xAxis2 = new Vector3D(axisLength, 0, 0);

        Vector3D zAxis1 = new Vector3D(0, 0, -axisLength);
        Vector3D zAxis2 = new Vector3D(0, 0, axisLength);

        // Transform axis
        xAxis1 = xAxis1.multiply(transform);
        xAxis2 = xAxis2.multiply(transform);
        zAxis1 = zAxis1.multiply(transform);
        zAxis2 = zAxis2.multiply(transform);

        g2.setColor(Color.RED);
        g2.drawLine((int) xAxis1.x, (int) xAxis1.y, (int) xAxis2.x, (int) xAxis2.y);

        g2.setColor(Color.BLUE);
        g2.drawLine((int) zAxis1.x, (int) zAxis1.y, (int) zAxis2.x, (int) zAxis2.y);
    }

    public void drawGridXY(Graphics2D g2, Matrix transform) {
        g2.setColor(scene.getConfiguration().getGridColor());
        g2.setStroke(new BasicStroke(scene.getConfiguration().getGridStrokeWidth()));

        double gridStep = scene.getConfiguration().getGridStep();
        int stepCounts = (int) (MAX_AXIS_LENGTH / gridStep);    
        int axisLength = (int) (gridStep * stepCounts);

        for (double i = 0; i <= axisLength; i += gridStep) {
            Vector3D x3 = new Vector3D(i, -axisLength, 0);
            Vector3D x4 = new Vector3D(i, axisLength, 0);
            Vector3D y3 = new Vector3D(-axisLength, i, 0);
            Vector3D y4 = new Vector3D(axisLength, i, 0);

            x3 = x3.multiply(transform);
            x4 = x4.multiply(transform);
            y3 = y3.multiply(transform);
            y4 = y4.multiply(transform);

            g2.drawLine((int) x3.x, (int) x3.y, (int) x4.x, (int) x4.y);
            g2.drawLine((int) y3.x, (int) y3.y, (int) y4.x, (int) y4.y);
        }

        for (double i = 0; i >= -axisLength; i -= gridStep) {
            Vector3D x3 = new Vector3D(i, -axisLength, 0);
            Vector3D x4 = new Vector3D(i, axisLength, 0);
            Vector3D y3 = new Vector3D(-axisLength, i, 0);
            Vector3D y4 = new Vector3D(axisLength, i, 0);

            x3 = x3.multiply(transform);
            x4 = x4.multiply(transform);
            y3 = y3.multiply(transform);
            y4 = y4.multiply(transform);

            g2.drawLine((int) x3.x, (int) x3.y, (int) x4.x, (int) x4.y);
            g2.drawLine((int) y3.x, (int) y3.y, (int) y4.x, (int) y4.y);
        }
    }

    public void drawGridXZ(Graphics2D g2, Matrix transform) {
        g2.setColor(scene.getConfiguration().getGridColor());
        g2.setStroke(new BasicStroke(scene.getConfiguration().getGridStrokeWidth()));

        double gridStep = scene.getConfiguration().getGridStep();
        int stepCounts = (int) (MAX_AXIS_LENGTH / gridStep);
        int axisLength = (int) (gridStep * stepCounts);

        for (double i = 0; i <= axisLength; i += gridStep) {
            Vector3D x1 = new Vector3D(i, 0, -axisLength);
            Vector3D x2 = new Vector3D(i, 0, axisLength);
            Vector3D y1 = new Vector3D(-axisLength, 0, i);
            Vector3D y2 = new Vector3D(axisLength, 0, i);

            x1 = x1.multiply(transform);
            x2 = x2.multiply(transform);
            y1 = y1.multiply(transform);
            y2 = y2.multiply(transform);

            g2.drawLine((int) x1.x, (int) x1.y, (int) x2.x, (int) x2.y);
            g2.drawLine((int) y1.x, (int) y1.y, (int) y2.x, (int) y2.y);
        }

        for (double i = 0; i >= -axisLength; i -= gridStep) {
            Vector3D x1 = new Vector3D(i, 0, -axisLength);
            Vector3D x2 = new Vector3D(i, 0, axisLength);
            Vector3D y1 = new Vector3D(-axisLength, 0, i);
            Vector3D y2 = new Vector3D(axisLength, 0, i);

            x1 = x1.multiply(transform);
            x2 = x2.multiply(transform);
            y1 = y1.multiply(transform);
            y2 = y2.multiply(transform);

            g2.drawLine((int) x1.x, (int) x1.y, (int) x2.x, (int) x2.y);
            g2.drawLine((int) y1.x, (int) y1.y, (int) y2.x, (int) y2.y);
        }
    }

    public void drawGridYZ(Graphics2D g2, Matrix transform) {
        g2.setColor(scene.getConfiguration().getGridColor());
        g2.setStroke(new BasicStroke(scene.getConfiguration().getGridStrokeWidth()));

        double gridStep = scene.getConfiguration().getGridStep();
        int stepCounts = (int) (MAX_AXIS_LENGTH / gridStep);
        int axisLength = (int) (gridStep * stepCounts);

        for (double i = 0; i <= axisLength; i += gridStep) {
            Vector3D x1 = new Vector3D(0, -axisLength, i);
            Vector3D x2 = new Vector3D(0, axisLength, i);
            Vector3D y1 = new Vector3D(0, i, -axisLength);
            Vector3D y2 = new Vector3D(0, i, axisLength);

            x1 = x1.multiply(transform);
            x2 = x2.multiply(transform);
            y1 = y1.multiply(transform);
            y2 = y2.multiply(transform);

            g2.drawLine((int) x1.x, (int) x1.y, (int) x2.x, (int) x2.y);
            g2.drawLine((int) y1.x, (int) y1.y, (int) y2.x, (int) y2.y);
        }

        for (double i = 0; i >= -axisLength; i -= gridStep) {
            Vector3D x1 = new Vector3D(0, -axisLength, i);
            Vector3D x2 = new Vector3D(0, axisLength, i);
            Vector3D y1 = new Vector3D(0, i, -axisLength);
            Vector3D y2 = new Vector3D(0, i, axisLength);

            x1 = x1.multiply(transform);
            x2 = x2.multiply(transform);
            y1 = y1.multiply(transform);
            y2 = y2.multiply(transform);

            g2.drawLine((int) x1.x, (int) x1.y, (int) x2.x, (int) x2.y);
            g2.drawLine((int) y1.x, (int) y1.y, (int) y2.x, (int) y2.y);
        }
    }

    public void drawCameraDetails(Graphics g, Point position) {
        Vector3D cameraPosition = scene.getCamera().getPosition();
        Vector3D cameraDirection = scene.getCamera().getDirection();
        double cameraScale = scene.getCamera().getScale();

        g.setColor(Color.WHITE);
        g.drawString(
                String.format("Camera Position: (%.2f, %.2f, %.2f)", cameraPosition.x, cameraPosition.y,
                        cameraPosition.z),
                position.x, position.y);
        g.drawString(String.format("Camera Direction: (%.2f, %.2f, %.2f)", cameraDirection.x, cameraDirection.y,
                cameraDirection.z), position.x, position.y + 20);
        g.drawString(String.format("Camera Scale: (%.2f, %.2f, %.2f)", cameraScale, cameraScale, cameraScale),
                position.x, position.y + 40);
    }

    public void drawLightDetails(Graphics g, Point position) {
        Light light = scene.getLight();

        g.setColor(Color.WHITE);
        g.drawString("Light Position: " + light.getPosition(), position.x, position.y);
        g.drawString("Light Color: " + light.getColor(), position.x, position.y + 20);
        g.drawString("Light Intensity: " + light.getIntensity(), position.x, position.y + 40);
    }

    public void drawFPSDetails(Graphics g, Point position) {
        g.setColor(Color.WHITE);
        g.drawString("FPS: " + this.fps, position.x, position.y);
    }

    public TriangleMesh getMeshFromPoint(Point point) {
        int depth = point.y * image.getWidth() + point.x;

        if (depth < 0 || depth >= idBuffer.length)
            return null;

        if (idBuffer[depth] != null) {
            return scene.getMesh(idBuffer[depth]);
        }
        return null;
    }

}