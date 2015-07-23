package actor.skill;

import actor.attribute.Attribute;
import utils.IntegerRange;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 */
public enum Skill {

  WOOD_CHOPPER(Attribute.MUSCLE, Attribute.TALENT);

  public static final BigDecimal PRIMARY_ATTRIBUTE_BONUS = BigDecimal.valueOf(0.10);
  public static final BigDecimal SECONDARY_ATTRIBUTE_BONUS = BigDecimal.valueOf(0.05);

  final Attribute primaryAttribute;
  final Attribute secondaryAttribute;


  Skill(Attribute primaryAttribute, Attribute secondaryAttribute) {
    this.primaryAttribute = primaryAttribute;
    this.secondaryAttribute = secondaryAttribute;
  }


  public static IntegerRange calculateRollRange(int skillRank, int primaryAttributeRank,
      int secondaryAttributeRank) {

    final int adjustedRank = skillRank + calculateAttributeBonus(skillRank, primaryAttributeRank,
        secondaryAttributeRank);

    final int mastery = SkillMastery.fromRank(skillRank).ordinal();

    final int min = mastery == 0 ? 1
        : (int) (adjustedRank * mastery * SkillMastery.PORTION_OF_RANKS_GUARANTEED_PER_MASTERY);

    return new IntegerRange(min, adjustedRank);
  }


  private static int calculateAttributeBonus(int skillRank, int primaryAttributeRank,
      int secondaryAttributeRank) {

    final BigDecimal primaryBonus =
        PRIMARY_ATTRIBUTE_BONUS.multiply(BigDecimal.valueOf(primaryAttributeRank));
    final BigDecimal secondaryBonus =
        SECONDARY_ATTRIBUTE_BONUS.multiply(BigDecimal.valueOf(secondaryAttributeRank));
    final BigDecimal totalBonus = primaryBonus.add(secondaryBonus);

    return BigDecimal.valueOf(skillRank).multiply(totalBonus).setScale(0, RoundingMode.HALF_UP)
        .intValueExact();
  }
}