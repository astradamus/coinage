package actor;

import actor.attribute.Attribute;
import actor.attribute.AttributeRange;
import actor.attribute.Rank;
import actor.inventory.Inventory;
import actor.stats.Health;
import game.Direction;
import game.Game;
import game.physical.Physical;
import game.physical.PhysicalFlag;
import thing.Thing;
import thing.ThingFactory;
import world.Coordinate;

import java.awt.Color;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Actors are subjects. They act upon Things and other Actors in the world. The Actor class only
 * handles the physical state of the Actor. Behavior and actions are handled by a Controller.
 */
public class Actor extends Physical {

  public static final EnumSet<PhysicalFlag> STANDARD_FLAGS =
                                          EnumSet.of(PhysicalFlag.BLOCKING, PhysicalFlag.IMMOVABLE);


  private final Health health;
  private final Map<Attribute, Rank> attributes;
  private final Inventory inventory;

  private Thing naturalWeapon;
  private Thing equippedWeapon;


  private Coordinate coordinate;
  private Direction facing = Direction.getRandom();



  Actor(ActorTemplate aT) {
    super(aT.name, aT.appearance);

    // Add standard actor flags and template-specific flags.
    STANDARD_FLAGS.forEach(this::addFlag);
    aT.flags.forEach(this::addFlag);


    naturalWeapon = ThingFactory.makeThing(aT.naturalWeaponID);

    attributes = new HashMap<>();

    for (Attribute attribute : Attribute.values()) {
      AttributeRange attributeRange = aT.baseAttributeRanges.get(attribute.ordinal());
      attributes.put(attribute, attributeRange.getRandomWithin(Game.RANDOM));
    }

    health = new Health(this);
    inventory = new Inventory();

  }

  public Thing getActiveWeapon() {
    return equippedWeapon != null ? equippedWeapon : naturalWeapon;
  }

  public void setEquippedWeapon(Thing validatedWeapon) {
    equippedWeapon = validatedWeapon;
  }



  public void die() {
    if (hasFlag(PhysicalFlag.DEAD)) {
      return; // Already dead!
    }
    removeFlag(PhysicalFlag.BLOCKING);
    removeFlag(PhysicalFlag.IMMOVABLE);
    addFlag(PhysicalFlag.DEAD);
  }

  public Health getHealth() {
    return health;
  }

  public Rank readAttributeLevel(Attribute attribute) {
    return attributes.get(attribute);
  }

  public Inventory getInventory() {
    return inventory;
  }




  public void setFacing(Direction facing) {
    this.facing = facing;
  }

  public void setCoordinate(Coordinate coordinate) {
    this.coordinate = coordinate;
  }

  public Direction getFacing() {
    return facing;
  }

  public Coordinate getCoordinate() {
    return coordinate;
  }


  @Override
  public String getName() {

    if (hasFlag(PhysicalFlag.DEAD)) {
      return super.getName() + "'s corpse";
    }

    return super.getName();

  }

  @Override
  public Color getColor() {

    if (hasFlag(PhysicalFlag.DEAD)) {
      return Color.DARK_GRAY;
    }

    return super.getColor();

  }

}
