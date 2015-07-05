package game.input;

import actor.Actor;
import controller.action.Attacking;
import game.Game;
import game.display.Event;
import game.display.EventLog;
import world.Coordinate;
import world.World;

import java.awt.event.KeyEvent;

/**
 *
 */
public enum Commands_Attack implements Command {


  STRIKE {

    @Override
    public int getHotKeyCode() {
      return KeyEvent.VK_S;
    }

    @Override
    public String getControlText() {
      return "S: Strike an enemy.";
    }

    @Override
    public void execute(World world) {

      Coordinate playerTarget = Game.getActiveInputSwitch().getPlayerTarget();

      Actor playerActor = Game.getActivePlayerActor();

      if (playerTarget.equalTo(playerActor.getCoordinate())) {
        EventLog.registerEvent(Event.INVALID_ACTION, "You smack yourself upside the head.");
      } else {

        playerActor.attemptAction(new Attacking(playerActor,
            Game.getActiveInputSwitch().getPlayerTarget()));

      }

      Game.getActiveInputSwitch().enterMode(GameMode.EXPLORE);

    }

  }


}