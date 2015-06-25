package thing;

import java.awt.Color;

/**
 *
 */
public class WeaponTemplates {

  static void load() {

    ThingTemplate.LIB.put("CLUB",
        new ThingTemplate("an oaken club", (char) 1634, Color.yellow,
            new WeaponComponent(

                4,      // DAMAGE
                1.0,    // BONUS MULTIPLIER FROM MUSCLE
                0.50,   // DAMAGE CONSISTENCY

                4,      // ATTACK SPEED
                0.10,   // SPEED BONUS FROM REFLEX

                2,      // RECOVERY SPEED
                0.10    // RECOVERY BONUS FROM REFLEX

            )));

    ThingTemplate.LIB.put("SWORD",
        new ThingTemplate("a sword", (char) 134, Color.yellow,
            new WeaponComponent(

                6,      // DAMAGE
                1.0,    // BONUS MULTIPLIER FROM MUSCLE
                0.80,   // DAMAGE CONSISTENCY

                3,      // ATTACK SPEED
                0.15,   // SPEED BONUS FROM REFLEX

                2,      // RECOVERY SPEED
                0.10    // RECOVERY BONUS FROM REFLEX

            )));

    ThingTemplate.LIB.put("AXE",
        new ThingTemplate("an axe", (char) 1006, Color.yellow,
            new WeaponComponent(

                10,     // DAMAGE
                1.25,   // BONUS MULTIPLIER FROM MUSCLE
                0.90,   // DAMAGE CONSISTENCY

                5,      // ATTACK SPEED
                0.15,   // SPEED BONUS FROM REFLEX

                3,      // RECOVERY SPEED
                0.15    // RECOVERY BONUS FROM REFLEX

            )));

    ThingTemplate.LIB.put("DAGGER",
        new ThingTemplate("a dagger", (char) 647, Color.yellow,
            new WeaponComponent(

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
