package actor;

import actor.attribute.AttributeRange;
import game.io.ResourceParser;
import game.physical.Appearance;
import game.physical.PhysicalFlag;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/**
 * A stored Prototype from which prefab Actors can be produced.
 */
public class ActorTemplate {

  final String name;
  final EnumSet<PhysicalFlag> flags;
  final List<AttributeRange> baseAttributeRanges;
  final String naturalWeaponID;
  final Appearance appearance;


  /**
   * Attempts to produce an ActorTemplate from the given map.
   *
   * @throws IOException If any invalid input is encountered.
   */
  public ActorTemplate(Map<String, String> templateMap) throws IOException {
    this.name = templateMap.get("name");
    this.flags = ResourceParser.Physicals.parseFlags(templateMap.get("flags"));
    this.baseAttributeRanges = ResourceParser.Actors.buildAttributeRanges(templateMap);
    this.naturalWeaponID = templateMap.get("natural_weapon_id");
    this.appearance = ResourceParser.Actors.buildActorAppearance(templateMap);
  }
}