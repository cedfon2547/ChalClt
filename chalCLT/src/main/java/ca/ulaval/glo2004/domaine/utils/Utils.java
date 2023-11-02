package ca.ulaval.glo2004.domaine.utils;

public class Utils {
    public static double normalizeAngle(double angle) {
        while (angle > Math.PI || angle < -Math.PI) {
            if (angle > Math.PI) {
                angle -= 2 * Math.PI;
            } else if (angle < -Math.PI) {
                angle += 2 * Math.PI;
            }
        }

        return angle;
    }
}
