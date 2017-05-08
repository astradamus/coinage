package utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class Utils {

    public static int clamp(int clamping, int min, int max) {
        if (clamping < min) {
            return min;
        }
        else if (clamping > max) {
            return max;
        }
        return clamping;
    }

    public static double clamp(double clamping, double min, double max) {
        if (clamping < min) {
            return min;
        }
        else if (clamping > max) {
            return max;
        }
        return clamping;
    }

    public static int modulus(int value, int by) {
        int mod = value % by;
        if (mod < 0) {
            mod += by;
        }
        return mod;
    }


    public static boolean getPointsAreAdjacent(int x1, int y1, int x2, int y2) {

        final int deltaX = x2 - x1;
        final int deltaY = y2 - y1;

        return (deltaX <= 1 && deltaX >= -1 && deltaY <= 1 && deltaY >= -1);

    }


    public static String indefiniteArticle(String of) {

        if (VOWELS.contains(of.toLowerCase().charAt(0))) {
            return "an";
        }
        else {
            return "a";
        }

    }

    private static final List<Character> VOWELS =
            Collections.unmodifiableList(Arrays.asList('a', 'e', 'i', 'o', 'u'));

}
