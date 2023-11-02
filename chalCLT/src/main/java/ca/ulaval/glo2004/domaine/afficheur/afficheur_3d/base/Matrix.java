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

    public Matrix transpose() {
        return transpose(this);
    }

    public Matrix inverse() {
        return inverse(this);
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

    public static Matrix transpose(Matrix m) {
        int rows = m.values.length;
        int cols = m.values[0].length;

        double[][] result = new double[cols][rows];

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++)
                result[i][j] = m.values[j][i];
        }

        return new Matrix(result);
    }

    public static Matrix inverse(Matrix m) {
        int size = m.values.length;
        double[][] augmentedMatrix = new double[size][size * 2];

        // Create an augmented matrix by appending the identity matrix to the right of
        // the original matrix
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                augmentedMatrix[i][j] = m.values[i][j];
            }
            augmentedMatrix[i][size + i] = 1;
        }

        // Perform row operations to transform the left side of the augmented matrix
        // into the identity matrix
        for (int i = 0; i < size; i++) {
            // Find the pivot element (the first non-zero element) in the current row
            double pivot = augmentedMatrix[i][i];
            int pivotRow = i;
            for (int j = i + 1; j < size; j++) {
                if (Math.abs(augmentedMatrix[j][i]) > Math.abs(pivot)) {
                    pivot = augmentedMatrix[j][i];
                    pivotRow = j;
                }
            }

            // Swap the current row with the row containing the pivot element
            if (pivotRow != i) {
                double[] temp = augmentedMatrix[i];
                augmentedMatrix[i] = augmentedMatrix[pivotRow];
                augmentedMatrix[pivotRow] = temp;
            }

            // Scale the current row so that the pivot element is 1
            double scale = augmentedMatrix[i][i];
            for (int j = i; j < size * 2; j++) {
                augmentedMatrix[i][j] /= scale;
            }

            // Use the current row to eliminate the pivot element in the other rows
            for (int j = 0; j < size; j++) {
                if (j != i) {
                    double factor = augmentedMatrix[j][i];
                    for (int k = i; k < size * 2; k++) {
                        augmentedMatrix[j][k] -= factor * augmentedMatrix[i][k];
                    }
                }
            }
        }

        // Extract the inverse matrix from the right side of the augmented matrix
        double[][] inverseValues = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                inverseValues[i][j] = augmentedMatrix[i][size + j];
            }
        }

        return new Matrix(inverseValues);
    }

    public static Matrix identityMatrix(int size) {
        double[][] values = new double[size][size];

        for (int i = 0; i < size; i++) {
            values[i][i] = 1;
        }

        return new Matrix(values);
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

    public static Matrix axisAngleRotationMatrix(Vector3D axis, double angle) {
        double[][] values = new double[][] {
                { Math.cos(angle) + Math.pow(axis.x, 2) * (1 - Math.cos(angle)),
                        axis.x * axis.y * (1 - Math.cos(angle)) - axis.z * Math.sin(angle),
                        axis.x * axis.z * (1 - Math.cos(angle)) + axis.y * Math.sin(angle), 0 },
                { axis.y * axis.x * (1 - Math.cos(angle)) + axis.z * Math.sin(angle),
                        Math.cos(angle) + Math.pow(axis.y, 2) * (1 - Math.cos(angle)),
                        axis.y * axis.z * (1 - Math.cos(angle)) - axis.x * Math.sin(angle), 0 },
                { axis.z * axis.x * (1 - Math.cos(angle)) - axis.y * Math.sin(angle),
                        axis.z * axis.y * (1 - Math.cos(angle)) + axis.x * Math.sin(angle),
                        Math.cos(angle) + Math.pow(axis.z, 2) * (1 - Math.cos(angle)), 0 },
                { 0, 0, 0, 1 },
        };

        return new Matrix(values);
    }
}
