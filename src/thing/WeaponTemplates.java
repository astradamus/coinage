package thing;

import actor.stats.DamageType;

import java.awt.Color;

/**
 *
 */
public class WeaponTemplates {

  public static final String WP_NATURAL_FISTS = "WP_NATURAL_FISTS";
  public static final String WP_NATURAL_FANGS = "WP_NATURAL_FANGS";
  public static final String WP_NATURAL_CLAWS = "WP_NATURAL_CLAWS";

  public static final String WP_CLUB = "WP_CLUB";
  public static final String WP_SWORD = "WP_SWORD";
  public static final String WP_AXE = "WP_AXE";
  public static final String WP_DAGGER = "WP_DAGGER";


  static void loadNaturalWeapons() {

    ThingTemplate.LIB.put(WP_NATURAL_FISTS,
        new ThingTemplate("fist", 'N', Color.yellow,
            new WeaponComponent(
                DamageType.STRIKING,

                1,      // DAMAGE
                2.25,   // BONUS MULTIPLIER FROM MUSCLE
                0.25,   // DAMAGE CONSISTENCY

                2,      // ATTACK SPEED
                0.10,   // SPEED BONUS FROM REFLEX

                2,      // RECOVERY SPEED
                0.15    // RECOVERY BONUS FROM REFLEX

            )));

    ThingTemplate.LIB.put(WP_NATURAL_FANGS,
        new ThingTemplate("fangs", 'N', Color.yellow,
            new WeaponComponent(
                DamageType.BITING,

                5,      // DAMAGE
                1.0,    // BONUS MULTIPLIER FROM MUSCLE
                0.50,   // DAMAGE CONSISTENCY

                2,      // ATTACK SPEED
                0.10,   // SPEED BONUS FROM REFLEX

                2,      // RECOVERY SPEED
                0.15    // RECOVERY BONUS FROM REFLEX

            )));

    ThingTemplate.LIB.put(WP_NATURAL_CLAWS,
        new ThingTemplate("claws", 'N', Color.yellow,
            new WeaponComponent(
                DamageType.SHREDDING,

                4,      // DAMAGE
                0.75,   // BONUS MULTIPLIER FROM MUSCLE
                0.30,   // DAMAGE CONSISTENCY

                2,      // ATTACK SPEED
                0.15,   // SPEED BONUS FROM REFLEX

                2,      // RECOVERY SPEED
                0.20    // RECOVERY BONUS FROM REFLEX

            )));

  }

  static void loadStandardWeapons() {

    ThingTemplate.LIB.put(WP_CLUB,
        new ThingTemplate("oaken club", (char) 1634, Color.yellow,
            new WeaponComponent(
                DamageType.CRUSHING,

                4,      // DAMAGE
                1.0,    // BONUS MULTIPLIER FROM MUSCLE
                0.50,   // DAMAGE CONSISTENCY

                4,      // ATTACK SPEED
                0.10,   // SPEED BONUS FROM REFLEX

                2,      // RECOVERY SPEED
                0.10    // RECOVERY BONUS FROM REFLEX

            )));

    ThingTemplate.LIB.put(WP_SWORD,
        new ThingTemplate("sword", (char) 134, Color.yellow,
            new WeaponComponent(
                DamageType.SLASHING,

                6,      // DAMAGE
                1.0,    // BONUS MULTIPLIER FROM MUSCLE
                0.80,   // DAMAGE CONSISTENCY

                3,      // ATTACK SPEED
                0.15,   // SPEED BONUS FROM REFLEX

                2,      // RECOVERY SPEED
                0.10    // RECOVERY BONUS FROM REFLEX

            )));

    ThingTemplate.LIB.put(WP_AXE,
        new ThingTemplate("axe", (char) 1006, Color.yellow,
            new WeaponComponent(
                DamageType.CLEAVING,

                10,     // DAMAGE
                1.25,   // BONUS MULTIPLIER FROM MUSCLE
                0.90,   // DAMAGE CONSISTENCY

                5,      // ATTACK SPEED
                0.15,   // SPEED BONUS FROM REFLEX

                3,      // RECOVERY SPEED
                0.15    // RECOVERY BONUS FROM REFLEX

            )));

    ThingTemplate.LIB.put(WP_DAGGER,
        new ThingTemplate("dagger", (char) 647, Color.yellow,
            new WeaponComponent(
                DamageType.STABBING,

                18,     // DAMAGE
                0.05,   // BONUS MULTIPLIER FROM MUSCLE
                0.30,   // DAMAGE CONSISTENCY

                3,      // ATTACK SPEED
                0.17,   // SPEED BONUS FROM REFLEX

                2,      // RECOVERY SPEED
                0.25    // RECOVERY BONUS FROM REFLEX

            )));


  }


}