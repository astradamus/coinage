package actor;

import actor.action.ActionComponent;
import actor.attribute.AttributeComponent;
import actor.inventory.Inventory;
import actor.skill.SkillComponent;
import actor.stats.Health;
import controller.ActorObserver;
import game.Direction;
import game.physical.Physical;
import game.physical.PhysicalFlag;
import thing.ThingFactory;
import world.Coordinate;

import java.awt.Color;
import java.util.EnumSet;

/**
 * Actors are subjects. They represent animate beings and characters in the world, as opposed to
 * things, which represent inanimate objects.
 */
public class Actor extends Physical {

  /** Defines physical flags that all actors are initialized with. */
  private static final EnumSet<PhysicalFlag> STANDARD_FLAGS =
      EnumSet.of(PhysicalFlag.BLOCKING, PhysicalFlag.IMMOVABLE);

  private final AttributeComponent attributeComponent;
  private final SkillComponent skillComponent;
  private final Health health;
  private final Inventory inventory;
  private final ActionComponent actionComponent;

  private ActorObserver actorObserver;

  private Coordinate coordinate;
  private Direction facing = Direction.getRandom();


  /**
   * Constructs an actor from the given template, and includes any flags in STANDARD_FLAGS.
   */
  Actor(ActorTemplate aT) {
    super(aT.name, aT.appearance);

    // Add standard actor flags and template-specific flags.
    STANDARD_FLAGS.forEach(this::addFlag);

    if (aT.flags != null) {
      aT.flags.forEach(this::addFlag);
    }

    // Construct components.
    attributeComponent = new AttributeComponent(aT);
    skillComponent = new SkillComponent(this);
    health = new Health(this);
    inventory = new Inventory(ThingFactory.makeThing(aT.naturalWeaponID));
    actionComponent = new ActionComponent(this);
  }


  /**
   * Called when the actor has been killed. Upon dying, actors retain their actor state, but behave
   * in almost all respects like things--that is, they no longer block the square they occupy and
   * they can be picked up and carried. They also disconnect from their actorObserver, which means
   * they stop acting and become lifeless.
   */
  public final void die() {
    if (!hasFlag(PhysicalFlag.DEAD)) {
      removeFlag(PhysicalFlag.BLOCKING);
      removeFlag(PhysicalFlag.IMMOVABLE);
      addFlag(PhysicalFlag.DEAD);
      actorObserver.disconnectActorObserver();
    }
  }


  /**
   * Returns this actor's attribute component.
   */
  public AttributeComponent getAttributeComponent() {
    return attributeComponent;
  }


  public SkillComponent getSkillComponent() {
    return skillComponent;
  }


  /**
   * @return This actor's health component.
   */
  public final Health getHealth() {
    return health;
  }


  /**
   * @return This actor's inventory component.
   */
  public final Inventory getInventory() {
    return inventory;
  }


  public ActionComponent getActionComponent() {
    return actionComponent;
  }


  public final Direction getFacing() {
    return facing;
  }


  public final void setFacing(Direction facing) {
    this.facing = facing;
  }


  public final Coordinate getCoordinate() {
    return coordinate;
  }


  public final void setCoordinate(Coordinate coordinate) {
    this.coordinate = coordinate;
  }


  public ActorObserver getActorObserver() {
    return actorObserver;
  }


  public final void setActorObserver(ActorObserver actorObserver) {
    if (this.actorObserver != null) {
      this.actorObserver.disconnectActorObserver();
    }
    this.actorObserver = actorObserver;
  }


  @Override
  public final String getName() {
    if (hasFlag(PhysicalFlag.DEAD)) {
      return super.getName() + "'s corpse";
    }
    else {
      return super.getName();
    }
  }


  @Override
  public final Color getColor() {
    if (hasFlag(PhysicalFlag.DEAD)) {
      return Color.DARK_GRAY;
    }
    else {
      return super.getColor();
    }
  }
}