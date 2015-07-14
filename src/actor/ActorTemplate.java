package actor;

import actor.attribute.AttributeRange;
import game.physical.Appearance;
import game.physical.PhysicalFlag;

import java.util.EnumSet;
import java.util.List;

/**
 * A stored Prototype from which prefab Actors can be produced.
 */
public class ActorTemplate {

  final String name;
  final Appearance appearance;
  final EnumSet<PhysicalFlag> flags;
  final List<AttributeRange> baseAttributeRanges;
  final String naturalWeaponID;


  public ActorTemplate(String name, Appearance appearance, EnumSet<PhysicalFlag> flags,
      List<AttributeRange> baseAttributeRanges, String naturalWeaponID) {
    this.name = name;
    this.appearance = appearance;
    this.flags = flags;
    this.baseAttributeRanges = baseAttributeRanges;
    this.naturalWeaponID = naturalWeaponID;
  }

  public String getName() {
    return name;
  }
}