package thing;

import game.Game;
import game.physical.Appearance;
import game.physical.PhysicalFlag;

import java.awt.Color;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

/**
 * A stored Prototype from which prefab Things can be produced. Currently uses a hard-coded
 * static set for its library, will eventually use raw text resources.
 */
public class ThingTemplate {

  final String name;
  final List<Appearance> appearances = new ArrayList<>();
  final EnumSet<PhysicalFlag> flags;

  final WeaponComponent weaponComponent;

  public ThingTemplate(String name, char[] chars, Color[] colors, EnumSet<PhysicalFlag> flags) {

    this.name = name;

    for (char character : chars) {
      for (Color color : colors) {
        appearances.add(new Appearance(character, color, null, Appearance.VISUAL_PRIORITY__THINGS));
      }
    }

    this.flags = flags;

    this.weaponComponent = null;

  }

  public ThingTemplate(String name, char mapSymbol, Color color, WeaponComponent weaponComponent) {
    this.name = name;
    this.appearances.add(new Appearance(mapSymbol,color, Appearance.VISUAL_PRIORITY__THINGS));
    this.weaponComponent = weaponComponent;
    this.flags = EnumSet.noneOf(PhysicalFlag.class);
  }

  Appearance getRandomAppearance() {
    return appearances.get(Game.RANDOM.nextInt(appearances.size()));
  }



  public static HashMap<String,ThingTemplate> LIB = new HashMap<>();

  public static void loadThings() {
    LIB.put("UNDERGROWTH", new ThingTemplate(
        "undergrowth",
        new char[] {'#'},
        new Color[] {
            new Color(0, 49,0),
            new Color(13, 47, 0),
            new Color(11, 44, 0)
        }, EnumSet.of(PhysicalFlag.IMMOVABLE)
    ));
    LIB.put("TREE", new ThingTemplate(
      "a tree",
        new char[] {916,8710},
        new Color[] {
            new Color(0, 117,0),
            new Color(96, 149, 96),
            new Color(107, 154, 27)
        }, EnumSet.of(PhysicalFlag.BLOCKING,PhysicalFlag.IMMOVABLE)
    ));
    LIB.put("TREE_SWAMP", new ThingTemplate(
      "a swamp willow tree",
        new char[] {8593,8607,8670},
        new Color[] {
            new Color(0, 132, 43),
            new Color(121, 174, 31)
        }, EnumSet.of(PhysicalFlag.BLOCKING,PhysicalFlag.IMMOVABLE)
    ));
    LIB.put("SHARKWEED", new ThingTemplate(
      "a sharkweed thicket",
        new char[] {8472,8706},
        new Color[] {
            new Color(83, 48, 93),
            new Color(60, 55, 84),
            new Color(74, 44, 67)
        }, EnumSet.of(PhysicalFlag.BLOCKING,PhysicalFlag.IMMOVABLE)
    ));
    LIB.put("CACTUS", new ThingTemplate(
      "a cactus",
        new char[] {10013,8992},
        new Color[] {
            new Color(63, 165,0),
            new Color(115, 219, 59),
            new Color(170, 228, 75)
        }, EnumSet.of(PhysicalFlag.BLOCKING,PhysicalFlag.IMMOVABLE)
    ));

    LIB.put("OOZE", new ThingTemplate(
        "putrid ooze",
        new char[] {8776},
        new Color[] {
            new Color(165, 58, 155),
            new Color(163, 93, 185)
        }, EnumSet.of(PhysicalFlag.IMMOVABLE)
    ));


    LIB.put("BOULDER", new ThingTemplate(
        "a boulder",
        new char[] {1501,'o','0'},
        new Color[] {
            new Color(173, 173, 173),
            new Color(142, 142, 142),
            new Color(105, 103, 99)
        }, EnumSet.of(PhysicalFlag.BLOCKING)
    ));

    LIB.put("DUNE", new ThingTemplate(
        "a dune",
        new char[] {'~',8765},
        new Color[] {
            new Color(112, 115, 30),
            new Color(82, 59, 1),
            new Color(77, 55, 16),
            new Color(99, 80, 0)
        }, EnumSet.of(PhysicalFlag.IMMOVABLE)
    ));
    LIB.put("BOULDER_SANDSTONE", new ThingTemplate(
        "a sandstone boulder",
        new char[] {'a','o','0'},
        new Color[] {
            new Color(164, 84, 80),
            new Color(166, 108, 100),
            new Color(185, 138, 95),
        }, EnumSet.of(PhysicalFlag.BLOCKING)
    ));



    LIB.put("STONE", new ThingTemplate(
        "a small stone",
        new char[] {1566},
        new Color[] {
            new Color(94, 94, 94),
            new Color(79, 79, 79),
            new Color(63, 63, 63)
        }, EnumSet.noneOf(PhysicalFlag.class)
    ));

    WeaponTemplates.loadStandardWeapons();
    WeaponTemplates.loadNaturalWeapons();

  }

}
