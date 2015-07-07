package utils;

import java.security.InvalidParameterException;
import java.util.Random;
import java.util.function.Function;

/**
 *
 */
public abstract class Range<E> {

  private final Function<E, Integer> getOrdinal;
  private final Function<Integer, E> getTypeValue;

  private final Integer minimum;
  private final Integer maximum;

  protected Range(Function<E, Integer> getOrdinal, Function<Integer, E> getTypeValue, E minimum,
                  E maximum) {
    this.getOrdinal = getOrdinal;
    this.getTypeValue = getTypeValue;
    this.minimum = getOrdinal.apply(minimum);
    this.maximum = getOrdinal.apply(maximum);
    if (this.minimum == null || this.maximum == null) {
      throw new InvalidParameterException("Range initiation failed to produce valid min/max ordinals.");
    }
  }

  public E clamp(E clamping) {
    Integer ordinal = getOrdinal.apply(clamping);
    if (ordinal < minimum) {
      return getTypeValue.apply(minimum);
    } else if (ordinal > maximum) {
      return getTypeValue.apply(maximum);
    }
    return clamping;
  }

  public E getRandomWithin(Random random) {

    // The +1 here is necessary to make the maximum value inclusive.
    return getTypeValue.apply(random.nextInt(1+maximum-minimum)+minimum);

  }

}
