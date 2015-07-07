package utils;


/**
 *
 */
public class IntegerRange extends Range<Integer> {

  public IntegerRange(Integer minimum, Integer maximum) {
    super(i -> i, i -> i, minimum, maximum);
  }

}