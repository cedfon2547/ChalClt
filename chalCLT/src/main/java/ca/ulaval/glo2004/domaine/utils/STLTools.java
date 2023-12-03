package ca.ulaval.glo2004.domaine.utils;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class STLTools {
    public static class Triangle {
        public float[] normal;
        public float[] vertex1;
        public float[] vertex2;
        public float[] vertex3;

        public Triangle(float[] normal, float[] vertex1, float[] vertex2, float[] vertex3) {
            this.normal = normal;
            this.vertex1 = vertex1;
            this.vertex2 = vertex2;
            this.vertex3 = vertex3;
        }
    }

    public static ArrayList<Triangle> readSTL(String filename) {
        ArrayList<Triangle> triangles = new ArrayList<Triangle>();

        try {
            FileInputStream file = new FileInputStream(filename);
            DataInputStream in = new DataInputStream(file);

            byte[] header = new byte[80];

            in.read(header);

            int nbTriangles = Integer.reverseBytes(in.readInt());

            for (int i = 0; i < nbTriangles; i++) {
                float[] normal = new float[3];
                float[][] vertices = new float[3][3];

                for (int j = 0; j < 3; j++) {
                    normal[j] = Float.intBitsToFloat(Integer.reverseBytes(in.readInt()));
                }

                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < 3; k++) {
                        vertices[j][k] = Float.intBitsToFloat(Integer.reverseBytes(in.readInt()));
                    }
                }

                Triangle triangle = new Triangle(
                        normal,
                        vertices[0], vertices[1], vertices[2]);

                triangles.add(triangle);
            }

            in.close();
            file.close();
            return triangles;
        } catch (Exception err) {

        }

        return triangles;
    }

    public static void writeSTL(List<Triangle> triangles, String filePath) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(filePath))) {
            // Write a 80-character header (usually ignored)
            dos.write(new byte[80]);

            // Write the number of triangles as a little-endian 32-bit unsigned integer
            dos.writeInt(Integer.reverseBytes(triangles.size()));

            for (Triangle triangle : triangles) {
                // Write normal vector
                for (float value : triangle.normal) {
                    dos.writeFloat(value);
                }

                // Write vertices
                writeVertex(dos, triangle.vertex1);
                writeVertex(dos, triangle.vertex2);
                writeVertex(dos, triangle.vertex3);

                // Write attribute byte count (should be zero)
                dos.writeShort(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeVertex(DataOutputStream dos, float[] vertex) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4 * vertex.length);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        for (float value : vertex) {
            buffer.putFloat(value);
        }

        dos.write(buffer.array());
    }

    // A method to write a list of triangles to an STL file in ASCII format
    // Rubbish
    public static void writeAsciiSTL(String filename, List<Triangle> triangles) throws IOException {
        // Create a file writer
        FileWriter fw = new FileWriter(filename);

        // Create a buffered writer
        BufferedWriter bw = new BufferedWriter(fw);

        // Write the header line
        bw.write("solid " + filename + "\n");

        // Write each triangle
        for (Triangle t : triangles) {
            // Write the normal vector

            bw.write("facet normal " + t.normal[0] + " " + t.normal[1] + " " + t.normal[2] + "\n");

            // Write the outer loop
            bw.write("outer loop\n");

            // Write the three vertices
            bw.write("vertex " + t.vertex1[0] + " " + t.vertex1[1] + " " + t.vertex1[2] + "\n");
            bw.write("vertex " + t.vertex2[0] + " " + t.vertex2[1] + " " + t.vertex2[2] + "\n");
            bw.write("vertex " + t.vertex3[0] + " " + t.vertex3[1] + " " + t.vertex3[2] + "\n");

            // Write the endloop and endfacet
            bw.write("endloop\n");
            bw.write("endfacet\n");
        }

        // Write the end solid line
        bw.write("endsolid " + filename + "\n");

        // Close the writers
        bw.close();
        fw.close();
    }
}