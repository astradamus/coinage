package actor;

import actor.attribute.AttributeRange;
import actor.attribute.Rank;
import game.Game;
import game.physical.Appearance;
import game.physical.PhysicalFlag;

import java.awt.Color;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

/**
 * A stored Prototype from which prefab Actors can be produced. Currently uses a hard-coded
 * static set for its library, will eventually use raw text resources.
 */
public class ActorTemplate {

  final String name;
  final Appearance appearance;

  final EnumSet<PhysicalFlag> flags;
  final List<AttributeRange> baseAttributeRanges;

  public ActorTemplate(String name, char appearance, Color color, Color bgColor,
                       List<AttributeRange> baseAttributeRanges, EnumSet<PhysicalFlag> flags) {
    this.name = name;
    this.appearance = new Appearance(appearance,color,bgColor, Game.VISUAL_PRIORITY__ACTORS);
    this.baseAttributeRanges = baseAttributeRanges;
    this.flags = flags;
  }

  public ActorTemplate(String name, char appearance, Color color, Color bgColor,
                       List<AttributeRange> baseAttributeRanges) {
    this(name, appearance, color, bgColor, baseAttributeRanges, EnumSet.noneOf(PhysicalFlag.class));
  }

  public static HashMap<String, ActorTemplate> LIB = new HashMap<>();
  static {

    LIB.put("HUMAN", new ActorTemplate(

        "a human", 'H', new Color(168, 109, 60),
        new Color(56, 37, 18),

        Arrays.asList(
            AttributeRange.fromRank(Rank.R05_AVERAGE, 1),        // MUSCLE
            AttributeRange.fromRank(Rank.R05_AVERAGE, 1),        // GRIT
            AttributeRange.fromRank(Rank.R05_AVERAGE, 1),        // REFLEX
            AttributeRange.fromRank(Rank.R05_AVERAGE, 1),        // TALENT
            AttributeRange.fromRank(Rank.R05_AVERAGE, 1),        // PERCEPTION
            AttributeRange.fromRank(Rank.R05_AVERAGE, 1)         // CHARM
        )

    ));

    LIB.put("WOLF", new ActorTemplate(

        "a wolf", 'w', new Color(139, 129, 122),
        new Color(57, 45, 36),

        Arrays.asList(
          AttributeRange.fromRank(Rank.R08_OUTSTANDING, 1),               // MUSCLE
          AttributeRange.fromRank(Rank.R08_OUTSTANDING, 1),               // GRIT
          AttributeRange.fromRank(Rank.R08_OUTSTANDING, 1),               // REFLEX
          new AttributeRange(Rank.R03_INFERIOR,Rank.R04_BELOW_AVERAGE),   // TALENT
          new AttributeRange(Rank.R10_MASTERFUL,Rank.R11_HEROIC),         // PERCEPTION
          AttributeRange.fromRank(Rank.R03_INFERIOR,1)                    // CHARM
        ),

        EnumSet.of(PhysicalFlag.FOUR_LEGGED)

    ));

    LIB.put("COUGAR", new ActorTemplate(

        "a cougar", 'c', new Color(130, 90, 10), new Color(37, 26, 6),

        Arrays.asList(
          AttributeRange.fromRank(Rank.R10_MASTERFUL, 1),                 // MUSCLE
          AttributeRange.fromRank(Rank.R10_MASTERFUL, 1),                 // GRIT
          AttributeRange.fromRank(Rank.R11_HEROIC, 1),                    // REFLEX
          new AttributeRange(Rank.R03_INFERIOR,Rank.R04_BELOW_AVERAGE),   // TALENT
          new AttributeRange(Rank.R09_EXCEPTIONAL,Rank.R10_MASTERFUL),    // PERCEPTION
          AttributeRange.fromRank(Rank.R03_INFERIOR,1)                    // CHARM
        ),

        EnumSet.of(PhysicalFlag.FOUR_LEGGED)

    ));

    LIB.put("MUSKRAT", new ActorTemplate(

        "a muskrat", 'r', new Color(127, 129, 53),
        new Color(37, 23, 0),

        Arrays.asList(
          AttributeRange.fromRank(Rank.R03_INFERIOR,1),                   // MUSCLE
          new AttributeRange(Rank.R03_INFERIOR,Rank.R04_BELOW_AVERAGE),   // GRIT
          AttributeRange.fromRank(Rank.R03_INFERIOR,1),                   // REFLEX
          AttributeRange.fromRank(Rank.R01_ABYSMAL,0),                    // TALENT
          AttributeRange.fromRank(Rank.R05_AVERAGE,1),                    // PERCEPTION
          AttributeRange.fromRank(Rank.R01_ABYSMAL,0)                     // CHARM
        ),

        EnumSet.of(PhysicalFlag.FOUR_LEGGED, PhysicalFlag.TIMID)

    ));

  }

}