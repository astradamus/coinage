package utils;

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

}
