package game.input;

import actor.Actor;
import controller.action.Attacking;
import controller.player.PlayerAgent;
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

      PlayerAgent playerAgent = Game.getActiveInputSwitch().getPlayerController();

      if (playerTarget.equalTo(playerAgent.getActor().getCoordinate())) {
        EventLog.registerEvent(Event.INVALID_ACTION, "You smack yourself upside the head.");
      } else {

        playerAgent.attemptAction(new Attacking(playerAgent.getActor(),
            Game.getActiveInputSwitch().getPlayerTarget()));

      }

      Game.getActiveInputSwitch().enterMode(GameMode.EXPLORE);

    }

  }


}