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
  boolean isImmovable;
  boolean isBlocking;

  public ThingTemplate(String name, char[] appearances, Color[] colors, Double weight,
                       boolean isImmovable, boolean isBlocking) {
    this.name = name;
    this.appearances = appearances;
    this.colors = colors;
    this.weight = weight;
    this.isImmovable = isImmovable;
    this.isBlocking = isBlocking;
  }

  char getRandomCharacter() {
    return appearances[Game.RANDOM.nextInt(appearances.length)];
  }

  Color getRandomColor() {
    return colors[Game.RANDOM.nextInt(colors.length)];
  }



  public static HashMap<String,ThingTemplate> LIB = new HashMap<>();
  static {
    LIB.put("OVERGROWTH", new ThingTemplate(
        "Overgrowth",
        new char[] {'#'},
        new Color[] {
            new Color(0, 49,0),
            new Color(13, 47, 0),
            new Color(11, 44, 0)
        },
        100.0,
        true,
        false
    ));
    LIB.put("TREE", new ThingTemplate(
        "Tree",
        new char[] {916,8710}, // ?,?
        new Color[] {
            new Color(0, 117,0),
            new Color(96, 149, 96),
            new Color(107, 154, 27)
        },
        4500.0,
        true,
        true
    ));
    LIB.put("TREE_SWAMP", new ThingTemplate(
        "Swamp Willow Tree",
        new char[] {8593,8607,8670}, // ?,?,?
        new Color[] {
            new Color(0, 132, 43),
            new Color(121, 174, 31)
        },
        4500.0,
        true,
        true
    ));
    LIB.put("SHARKWEED", new ThingTemplate(
        "Sharkweed",
        new char[] {8472,8706},
        new Color[] {
            new Color(83, 48, 93),
            new Color(60, 55, 84),
            new Color(74, 44, 67)
        },
        4500.0,
        true,
        true
    ));
    LIB.put("CACTUS", new ThingTemplate(
        "Cactus",
        new char[] {10013,8992}, // ?,?
        new Color[] {
            new Color(63, 165,0),
            new Color(115, 219, 59),
            new Color(170, 228, 75)
        },
        4500.0,
        true,
        true
    ));

    LIB.put("OOZE", new ThingTemplate(
        "Putrid Ooze",
        new char[] {8776}, // ?,?
        new Color[] {
            new Color(165, 58, 155),
            new Color(163, 93, 185)
        },
        4500.0,
        true,
        false
    ));


    LIB.put("BOULDER", new ThingTemplate(
        "Boulder",
        new char[] {'a','o','0'},
        new Color[] {
            new Color(173, 173, 173),
            new Color(142, 142, 142),
            new Color(105, 103, 99)
        },
        3400.0,
        false,
        true
    ));

    LIB.put("DUNE", new ThingTemplate(
        "Dune",
        new char[] {'~',8765}, // ?
        new Color[] {
            new Color(112, 115, 30),
            new Color(82, 59, 1),
            new Color(77, 55, 16),
            new Color(99, 80, 0)
        },
        4000.0,
        true,
        false
    ));
    LIB.put("BOULDER_SANDSTONE", new ThingTemplate(
        "Sandstone Boulder",
        new char[] {'a','o','0'},
        new Color[] {
            new Color(164, 84, 80),
            new Color(166, 108, 100),
            new Color(185, 138, 95),
        },
        3400.0,
        false,
        true
    ));
  }

}
