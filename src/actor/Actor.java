package actor;

import actor.attribute.Attribute;
import actor.attribute.AttributeRange;
import actor.attribute.Rank;
import actor.inventory.Inventory;
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

  private final Map<Attribute, Rank> attributes;
  private final Inventory inventory;



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

    inventory = new Inventory();

  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Appearance getAppearance() {
    return appearance;
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
    return true;
  }

  @Override
  public boolean isBlocking() {
    return true;
  }


  public Inventory getInventory() {
    return inventory;
  }

  public Rank readAttributeLevel(Attribute attribute) {
    return attributes.get(attribute);
  }


  public void setFacing(Direction facing) {
    this.facing = facing;
  }

  public void setCoordinate(Coordinate coordinate) {
    this.coordinate = coordinate;
  }

  public Coordinate getCoordinate() {
    return coordinate;
  }

  public Direction getFacing() {
    return facing;
  }

  public void addBeatsToRecover(int addBeats) {
    this.beatsToRecover += addBeats;
  }

  /**
   * Returns true if this Actor is ready to act, or returns false and deducts one from this actor's
   * recovery time if it is still recovering.
   */
  public boolean getIsReadyThisBeat() {
    if (beatsToRecover > 0) {
      beatsToRecover--;
      return false;
    }
    return true;
  }

  public boolean attemptMoveTo(Coordinate newCoordinate) {
    if (newCoordinate != null && Game.getActiveWorld().move(this, coordinate, newCoordinate)) {
      coordinate = newCoordinate;
      return true;
    }
    return false;
  }

}
