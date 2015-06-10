package actor;

import actor.attribute.Attribute;
import actor.attribute.AttributeRange;
import actor.attribute.Rank;
import actor.inventory.Inventory;
import game.Game;
import game.Physical;
import game.display.Appearance;

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

  public Rank readAttributeLevel(Attribute attribute) {
    return attributes.get(attribute);
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

}
