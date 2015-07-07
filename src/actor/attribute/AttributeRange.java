package actor.attribute;

import utils.Range;

/**
 *
 */
public class AttributeRange extends Range<Rank> {

  public AttributeRange(Rank minimum, Rank maximum) {
    super(Rank::ordinal, ordinal -> Rank.values()[ordinal], minimum, maximum);
  }


  public static AttributeRange fromRank(Rank rank, int range) {

    int min = rank.ordinal() - range;
    int max = rank.ordinal() + range;
    if (min < 0) {
      min = 0;
    }
    if (max >= Rank.values().length) {
      max = Rank.values().length - 1;
    }

    return new AttributeRange(Rank.values()[min], Rank.values()[max]);
  }
}
