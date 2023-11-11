package ca.ulaval.glo2004.domaine.afficheur.afficheur_3d.base;

public class Matrix {
    public double[][] values = new double[][] {
            { 0, 0, 0, 0 },
            { 0, 0, 0, 0 },
            { 0, 0, 0, 0 },
            { 0, 0, 0, 0 },
    };

    public Matrix() {

    }

    public Matrix(double[][] values) {
        this.values = values;
    }

    public Matrix(Matrix matrix) {
        this.values = matrix.values.clone();
    }

    public double getValue(int row, int col) {
        return this.values[row][col];
    }

    public void setValue(int row, int col, double value) {
        this.values[row][col] = value;
    }

    public double[][] getValues() {
        return this.values;
    }

    public void setValues(double[][] values) {
        this.values = values;
    }

    public Matrix multiply(Matrix matrix) {
        return multiply(this, matrix);
    }

    /* Static Methods */
    public static Matrix multiply(Matrix m1, Matrix m2) {
        int size = m1.values.length;
        double[][] result = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = m1.values[i][0] * m2.values[0][j] + m1.values[i][1] * m2.values[1][j]
                        + m1.values[i][2] * m2.values[2][j] + m1.values[i][3] * m2.values[3][j];
            }
        }

        return new Matrix(result);
    }

    public static Matrix translationMatrix(double x, double y, double z) {
        double[][] values = new double[][] {
                { 1, 0, 0, x },
                { 0, 1, 0, y },
                { 0, 0, 1, z },
                { 0, 0, 0, 1 },
        };

        return new Matrix(values);
    }

    public static Matrix scaleMatrix(double x, double y, double z) {
        double[][] values = new double[][] {
                { x, 0, 0, 0 },
                { 0, y, 0, 0 },
                { 0, 0, z, 0 },
                { 0, 0, 0, 1 },
        };

        return new Matrix(values);
    }

    public static Matrix rotationXMatrix(double angle) {
        double[][] values = new double[][] {
                { 1, 0, 0, 0 },
                { 0, Math.cos(angle), -Math.sin(angle), 0 },
                { 0, Math.sin(angle), Math.cos(angle), 0 },
                { 0, 0, 0, 1 },
        };

        return new Matrix(values);
    }

    public static Matrix rotationYMatrix(double angle) {
        double[][] values = new double[][] {
                { Math.cos(angle), 0, Math.sin(angle), 0 },
                { 0, 1, 0, 0 },
                { -Math.sin(angle), 0, Math.cos(angle), 0 },
                { 0, 0, 0, 1 },
        };

        return new Matrix(values);
    }

    public static Matrix rotationZMatrix(double angle) {
        double[][] values = new double[][] {
                { Math.cos(angle), -Math.sin(angle), 0, 0 },
                { Math.sin(angle), Math.cos(angle), 0, 0 },
                { 0, 0, 1, 0 },
                { 0, 0, 0, 1 },
        };

        return new Matrix(values);
    }

    public static Matrix rotationMatrix(double angleX, double angleY, double angleZ) {
        return rotationXMatrix(angleX).multiply(rotationYMatrix(angleY)).multiply(rotationZMatrix(angleZ));
    }

    public static Matrix rotationMatrix(double angleX, double angleY, double angleZ, Vector3D axis) {
        Matrix translation = translationMatrix(-axis.x, -axis.y, -axis.z);
        Matrix inverseTranslation = translationMatrix(axis.x, axis.y, axis.z);
        Matrix rotation = rotationXMatrix(angleX).multiply(rotationYMatrix(angleY)).multiply(rotationZMatrix(angleZ));

        return translation.multiply(rotation).multiply(inverseTranslation);
    }
}
