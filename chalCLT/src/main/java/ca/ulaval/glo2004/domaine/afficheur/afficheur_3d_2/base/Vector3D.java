package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d_2.base;
/**
 * A class representing a 3D vector with x, y, z and w components.
 */
public class Vector3D {
    public double x, y, z, w;

    /**
     * Constructs a new Vector3D with the given x, y, z and w components.
     * 
     * @param x the x component of the vector
     * @param y the y component of the vector
     * @param z the z component of the vector
     * @param w the w component of the vector
     */
    public Vector3D(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Constructs a new Vector3D with the given x, y and z components and a default
     * w component of 1.
     * 
     * @param x the x component of the vector
     * @param y the y component of the vector
     * @param z the z component of the vector
     */
    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 1;
    }

    /**
     * Constructs a new Vector3D with the same x, y, z and w components as the given
     * vector.
     * 
     * @param vec the vector to copy
     */
    public Vector3D(Vector3D vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
        this.w = vec.w;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    /**
     * Returns a copy of this vector.
     * 
     * @return a copy of this vector
     */
    public Vector3D copy() {
        return new Vector3D(this);
    }

    public String toString() {
        return toString(this);
    }

    /**
     * Prints this vector to the console.
     */
    public void print() {
        System.out.println(toString(this));
    }

    public Matrix toMatrix() {
        return toMatrix(this);
    }

    public double[] toArray() {
        return toArray(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector3D) {
            Vector3D vec = (Vector3D) obj;
            return x == vec.x && y == vec.y && z == vec.z && w == vec.w;
        }
        return false;
    }

    /**
     * Adds the given vector to this vector and returns the result.
     * 
     * @param vec the vector to add
     * @return the result of the addition
     */
    public Vector3D add(Vector3D vec) {
        return add(this, vec);
    }

    /**
     * Subtracts the given vector from this vector and returns the result.
     * 
     * @param vec the vector to subtract
     * @return the result of the subtraction
     */
    public Vector3D sub(Vector3D vec) {
        return sub(this, vec);
    }

    /**
     * Multiplies this vector by the given scalar and returns the result.
     * 
     * @param scalar the scalar to multiply by
     * @return the result of the multiplication
     */
    public Vector3D multiplyScalar(double scalar) {
        return multiplyScalar(this, scalar);
    }

    public double dotProduct(Vector3D vec) {
        return dotProduct(this, vec);
    }

    /**
     * 
     * @param vec
     * @return
     */
    public Vector3D crossProduct(Vector3D vec) {
        return crossProduct(this, vec);
    }

    /**
     * Returns the length of this vector.
     * 
     * @return the length of this vector
     */
    public double getNormLength() {
        return getNormLength(this);
    }

    /**
     * Returns a normalized version of this vector.
     * 
     * @return a normalized version of this vector
     */
    public Vector3D normalize() {
        return normalize(this);
    }

    public Vector3D multiplyMatrix(Matrix matrix) {
        return multiplyMatrix(matrix, this);
    }

    /**
     * Rotates this vector around the X axis by the given angle and returns the
     * result.
     * 
     * @param angle the angle to rotate by
     * @return the result of the rotation
     */
    public Vector3D rotateAroundXAxis(double angle) {
        return rotateAroundX(this, angle);
    }

    /**
     * Rotates this vector around the Y axis by the given angle and returns the
     * result.
     * 
     * @param angle the angle to rotate by
     * @return the result of the rotation
     */
    public Vector3D rotateAroundYAxis(double angle) {
        return rotateAroundY(this, angle);
    }

    /**
     * Rotates this vector around the Z axis by the given angle and returns the
     * result.
     * 
     * @param angle the angle to rotate by
     * @return the result of the rotation
     */
    public Vector3D rotateAroundZAxis(double angle) {
        return rotateAroundZ(this, angle);
    }

    /**
     * Rotates this vector around the given axis by the given angle and returns the
     * result.
     * 
     * @param axis  the axis to rotate around
     * @param angle the angle to rotate by
     * @return the result of the rotation
     */
    // public Vector3D axisAngleRotation(Vector3D axis, double angle) {
    // return axisAngleRotation(axis, angle, this);
    // }

    /**
     * Translates this vector by the given x, y and z values and returns the result.
     * 
     * @param x the x value to translate by
     * @param y the y value to translate by
     * @param z the z value to translate by
     * @return the result of the translation
     */
    public Vector3D translate(double x, double y, double z) {
        return translate(this, x, y, z);
    }

    /**
     * Scales this vector by the given x, y and z values and returns the result.
     * 
     * @param x the x value to scale by
     * @param y the y value to scale by
     * @param z the z value to scale by
     * @return the result of the scaling
     */
    public Vector3D scale(double x, double y, double z) {
        return scale(this, x, y, z);
    }

    /**
     * Reflects this vector around the given normal and returns the result.
     * 
     * @param normal the normal to reflect around
     * @return the result of the reflection
     */
    public Vector3D reflect(Vector3D normal) {
        return reflect(this, normal);
    }

    /* ================= Static Methods ================= */
    public static Vector3D fromArray(double[] array) {
        return new Vector3D(array[0], array[1], array[2], array[3]);
    }

    public static double[] toArray(Vector3D vec) {
        return new double[] { vec.x, vec.y, vec.z, vec.w };
    }

    public static Matrix toMatrix(Vector3D vec) {
        return new Matrix(new double[][] {
                { vec.x },
                { vec.y },
                { vec.z },
                { vec.w },
        });
    }

    public static Vector3D fromMatrix(Matrix matrix) {
        double[][] values = matrix.values;
        return new Vector3D(values[0][0], values[1][0], values[2][0], values[3][0]);
    }

    public static String toString(Vector3D vec) {
        return "(" + vec.x + ", " + vec.y + ", " + vec.z + ", " + vec.w + ")";
    }

    public static Vector3D add(Vector3D a, Vector3D b) {
        return new Vector3D(
                a.x + b.x,
                a.y + b.y,
                a.z + b.z);
    }

    public static Vector3D sub(Vector3D a, Vector3D b) {
        return new Vector3D(
                a.x - b.x,
                a.y - b.y,
                a.z - b.z);
    }

    public static Vector3D multiplyScalar(Vector3D vec, double scalar) {
        return new Vector3D(
                vec.x * scalar,
                vec.y * scalar,
                vec.z * scalar);
    }

    public static double dotProduct(Vector3D v1, Vector3D v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    public static Vector3D crossProduct(Vector3D v1, Vector3D v2) {
        return new Vector3D(v1.y * v2.z - v1.z * v2.y,
                v1.z * v2.x - v1.x * v2.z,
                v1.x * v2.y - v1.y * v2.x);
    }

    public static double getNormLength(Vector3D vec) {
        return Math.sqrt(vec.x * vec.x + vec.y * vec.y + vec.z * vec.z);
    }

    public static Vector3D normalize(Vector3D vec) {
        double normLength = getNormLength(vec);
        return multiplyScalar(vec, 1 / normLength);
    }

    public static Vector3D multiplyMatrix(Matrix matrix, Vector3D vec) {
        double[][] values = matrix.values;
        double x = vec.x;
        double y = vec.y;
        double z = vec.z;
        double w = vec.w;

        double _x = values[0][0] * x + values[0][1] * y + values[0][2] * z + values[0][3] * w;
        double _y = values[1][0] * x + values[1][1] * y + values[1][2] * z + values[1][3] * w;
        double _z = values[2][0] * x + values[2][1] * y + values[2][2] * z + values[2][3] * w;
        double _w = values[3][0] * x + values[3][1] * y + values[3][2] * z + values[3][3] * w;

        return new Vector3D(_x, _y, _z, _w);
    }

    public static Vector3D getNormal(Vector3D a, Vector3D b, Vector3D c) {
        Vector3D v1v2 = sub(b, a);
        Vector3D v1v3 = sub(c, a);
        return normalize(crossProduct(v1v2, v1v3));
    }

    public static Vector3D rotateAroundX(Vector3D vec, double angle) {
        Matrix rotationMatrix = Matrix.rotationXMatrix(angle);
        return multiplyMatrix(rotationMatrix, vec);
    }

    public static Vector3D rotateAroundY(Vector3D vec, double angle) {
        Matrix rotationMatrix = Matrix.rotationYMatrix(angle);
        return multiplyMatrix(rotationMatrix, vec);
    }

    public static Vector3D rotateAroundZ(Vector3D vec, double angle) {
        Matrix rotationMatrix = Matrix.rotationZMatrix(angle);
        return multiplyMatrix(rotationMatrix, vec);
    }

    // public static Vector3D axisAngleRotation(Vector3D axis, double angle,
    // Vector3D vec) {
    // Matrix rotationMatrix = Matrix.axisAngleRotationMatrix(axis, angle);
    // return multiplyMatrix(rotationMatrix, vec);
    // }

    public static Vector3D translate(Vector3D vec, double x, double y, double z) {
        Matrix translationMatrix = Matrix.translationMatrix(x, y, z);
        return multiplyMatrix(translationMatrix, vec);
    }

    public static Vector3D scale(Vector3D vec, double x, double y, double z) {
        Matrix scaleMatrix = Matrix.scaleMatrix(x, y, z);
        return multiplyMatrix(scaleMatrix, vec);
    }

    public static Vector3D reflect(Vector3D vec, Vector3D normal) {
        return sub(vec, multiplyScalar(normal, 2 * dotProduct(vec, normal)));
    }

    // A method to calculate the angle between two vectors in radians
    public static double angle(Vector3D v1, Vector3D v2) {
        return Math.acos(dotProduct(v1, v2) / (getNormLength(v1) * getNormLength(v2)));
    }

    // A method to calculate the cross product of two vectors
    public static Vector3D cross(Vector3D v1, Vector3D v2) {
        return new Vector3D(v1.y * v2.z - v1.z * v2.y,
                v1.z * v2.x - v1.x * v2.z,
                v1.x * v2.y - v1.y * v2.x);
    }

    // A method to calculate the dot product of two vectors
    public static double dot(Vector3D v1, Vector3D v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    // A method to calculate the magnitude of a vector
    public static double magnitude(Vector3D v) {
        return Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
    }
}
