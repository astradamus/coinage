package thing;

import game.Game;

import java.awt.*;
import java.util.HashMap;

/**
 * A stored Prototype from which prefab Things can be produced. Currently uses a hard-coded
 * static set for its library, will eventually use raw text resources.
 */
public class ThingTemplate {

  String name;
  char[] appearances;
  Color[] colors;
  Double weight;
  boolean isImmobile;
  boolean isBlocking;

  public ThingTemplate(String name, char[] appearances, Color[] colors, Double weight,
                       boolean isImmobile, boolean isBlocking) {
    this.name = name;
    this.appearances = appearances;
    this.colors = colors;
    this.weight = weight;
    this.isImmobile = isImmobile;
    this.isBlocking = isBlocking;
  }

  char getRandomAppearance() {
    return appearances[Game.RANDOM.nextInt(appearances.length)];
  }

  Color getRandomColor() {
    return colors[Game.RANDOM.nextInt(colors.length)];
  }



  public static HashMap<String,ThingTemplate> LIB = new HashMap<>();
  static {
    LIB.put("TREE", new ThingTemplate(
        "Tree",
        new char[] {'T','F','V','W'},
        new Color[] {new Color(0,145,0), new Color(132,205,132), new Color(143,205,37)},
        4500.0,
        true,
        true
    ));
    LIB.put("BOULDER", new ThingTemplate(
        "Boulder",
        new char[] {'a','o','0'},
        new Color[] {new Color(192,192,192), new Color(169,169,169), new Color(109,106,103)},
        3400.0,
        false,
        true
    ));
  }

}
