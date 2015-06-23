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
    } else if (clamping > max) {
      return max;
    }
    return clamping;
  }

  public static double clamp(double clamping, double min, double max) {
    if (clamping < min) {
      return min;
    } else if (clamping > max) {
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


  public static String indefiniteArticle(String of) {

    if (VOWELS.contains(of.toLowerCase().charAt(0))) {
      return "an";
    } else {
      return "a";
    }

  }

  public static final List<Character> VOWELS =
      Collections.unmodifiableList(Arrays.asList('a', 'e', 'i', 'o', 'u'));

}
