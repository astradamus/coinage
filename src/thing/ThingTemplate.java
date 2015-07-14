package thing;

import game.Game;
import game.io.ResourceParser;
import game.physical.Appearance;
import game.physical.PhysicalFlag;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/**
 * A stored Prototype from which Things can be fabricated.
 */
public class ThingTemplate {

  final String name;
  private final List<Appearance> appearances;
  final EnumSet<PhysicalFlag> flags;
  final WeaponComponent weaponComponent;


  /**
   * Attempts to produce a ThingTemplate from the given map.
   *
   * @throws IOException If any invalid input is encountered.
   */
  public ThingTemplate(Map<String, String> templateMap) throws IOException {
    this.name = templateMap.get("name");
    this.appearances = ResourceParser.Things.buildThingAppearances(templateMap);
    this.flags = ResourceParser.Physicals.parseFlags(templateMap.get("flags"));
    this.weaponComponent = ResourceParser.Things.buildWeaponComponent(templateMap);
  }


  Appearance getRandomAppearance() {
    return appearances.get(Game.RANDOM.nextInt(appearances.size()));
  }
}