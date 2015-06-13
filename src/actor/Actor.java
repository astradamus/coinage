package actor;

import actor.attribute.Attribute;
import actor.attribute.AttributeRange;
import actor.attribute.Rank;
import actor.inventory.Inventory;
import actor.stats.Health;
import game.Direction;
import game.Game;
import game.Physical;
import game.display.Appearance;
import world.Coordinate;

import java.util.HashMap;
import java.util.Map;

/**
 * Actors are subjects. They act upon Things and other Actors in the world. The Actor class only
 * handles the physical state of the Actor. Behavior and actions are handled by a Controller.
 */
public class Actor implements Physical {

  private final String name;
  private final Appearance appearance;
  private final Double weight;


  private final Health health;
  private final Map<Attribute, Rank> attributes;
  private final Inventory inventory;


  private ActorPhysicalState physicalState = ActorPhysicalState.ALIVE;

  private Coordinate coordinate;
  private Direction facing;

  private int beatsToRecover = 0;


  Actor(ActorTemplate aT) {

    name = aT.name;
    appearance = aT.appearance;
    weight = aT.weight;

    attributes = new HashMap<>();

    for (Attribute attribute : Attribute.values()) {
      AttributeRange attributeRange = aT.baseAttributeRanges.get(attribute.ordinal());
      attributes.put(attribute, attributeRange.getRandomWithin(Game.RANDOM));
    }

    health = new Health(this);
    inventory = new Inventory();

  }

  @Override
  public String getName() {
    return physicalState.getQualifiedName(name);
  }

  @Override
  public Appearance getAppearance() {
    return physicalState.getAdjustedAppearance(appearance);
  }

  @Override
  public Double getWeight() {
    return weight;
  }

  @Override
  public int getVisualPriority() {
    return Game.VISUAL_PRIORITY__ACTORS;
  }

  @Override
  public boolean isImmovable() {
    return physicalState.actorIsImmovable();
  }

  @Override
  public boolean isBlocking() {
    return physicalState.actorIsBlocking();
  }



  public void die() {
    physicalState = ActorPhysicalState.DEAD;
  }

  public boolean isDead() {
    return physicalState == ActorPhysicalState.DEAD;
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



  public void addBeatsToRecover(int addBeats) {
    this.beatsToRecover += addBeats;
  }

  /**
   * Returns true if this Actor is ready to act, or returns false and deducts one from this actor's
   * recovery time if it is still recovering.
   */
  public boolean isReadyToAct() {
    return beatsToRecover <= 0;
  }

  public void decrementRecoveryTimer() {
    beatsToRecover--;
  }


  public boolean attemptMoveTo(Coordinate newCoordinate) {
    if (newCoordinate != null && Game.getActiveWorld().move(this, coordinate, newCoordinate)) {
      coordinate = newCoordinate;
      return true;
    }
    return false;
  }

}
