package actor.action;

import actor.Actor;
import actor.skill.Skill;
import actor.skill.SkillCheck;
import game.io.display.Event;
import game.io.display.EventLog;
import game.physical.Physical;
import game.physical.PhysicalFlag;
import thing.Thing;
import thing.ThingFactory;
import world.Coordinate;
import world.Square;
import world.World;

import java.awt.Color;

/**
 * Actors perform chops in order to fell trees and turn them into logs.
 */
public class Chopping extends Action {

  private Physical choppingWhat;
  private int recoveryDelay;


  public Chopping(Actor actor, Coordinate choppingWhere, Physical choppingWhat) {
    super(actor, choppingWhere);
    this.choppingWhat = choppingWhat;
  }


  @Override
  public Color getIndicatorColor() {
    return Color.YELLOW;
  }


  @Override
  public int calcDelayToPerform() {
    return 25;
  }


  @Override
  public int calcDelayToRecover() {
    return recoveryDelay;
  }


  /**
   * Chop wood will fail if the equipped weapon lacks the CAN_CHOP flag, or the target
   * physical somehow loses (or never had) the CHOPPABLE flag.
   */
  @Override
  protected boolean validate(World world) {

    // Fail if our weapon lacks CAN_CHOP.
    final Thing equipped = getActor().getInventory().getWeapon();
    if (!equipped.hasFlag(PhysicalFlag.CAN_CHOP)) {
      if (hasFlag(ActionFlag.PLAYER_IS_ACTOR)) {
        EventLog.registerEvent(Event.FAILURE,
            "You can't chop with your " + equipped.getName() + ".");
      }
      return false;
    }

    // Fail if the target cannot be chopped.
    if (!choppingWhat.hasFlag(PhysicalFlag.CHOPPABLE)) {
      if (hasFlag(ActionFlag.PLAYER_IS_ACTOR)) {
        EventLog.registerEvent(Event.FAILURE, "You can't chop " + choppingWhat.getName());
      }
      return false;
    }

    return true;
  }


  /**
   * Attempt a skill check, and if passed, destroy the target and replace it with wood. When
   * materials are in place this will return types of wood (or other choppable materials?), and
   * different materials can offer different difficulty values. When qualities are in place this
   * will return different qualities of wood.
   */
  @Override
  protected void apply(World world) {

    final SkillCheck skillCheck =
        getActor().getSkillComponent().performSkillCheck(Skill.WOOD_CHOPPER, 25);

    final int outcomeOrdinal = skillCheck.getOutcome().ordinal();
    recoveryDelay = outcomeOrdinal*10;

    final Square targetSquare = world.getSquare(getTarget());

    targetSquare.pull(choppingWhat);


    int i = 0;
    if (outcomeOrdinal < 4) {
      for (i = 0; i < (5-outcomeOrdinal)/2; i++) {
        targetSquare.put(ThingFactory.makeThing("WOOD"));
      }

    }

    if (hasFlag(ActionFlag.PLAYER_IS_ACTOR)) {

      Color color = Event.SUCCESS;
      String messageA = "Your ";
      String messageB = "chopping produces ";
      String messageC = "log";

      if (i == 2) {
        messageA += "masterful ";
        messageB += "2 ";
        messageC += "s";
      }
      else if (i == 1) {
        messageA += "passable ";
        messageB += "1 ";
      }
      else {
        messageA += "terrible ";
        messageB += "no ";
        messageC += "s";
        color = Event.FAILURE;
      }

      EventLog.registerEvent(color, messageA + messageB + messageC + ".");
    }

  }
}