package game.io.input;

import controller.action.Attacking;
import controller.player.PlayerAgent;
import game.io.better_ui.Event;
import game.io.better_ui.EventLog;
import world.Coordinate;

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
    public void execute() {

      Coordinate playerTarget = GameInput.getPlayerTarget();

      PlayerAgent playerAgent = GameInput.getRunningGame().getPlayerAgent();

      if (playerTarget.equalTo(playerAgent.getActor().getCoordinate())) {
        EventLog.registerEvent(Event.INVALID_INPUT, "You smack yourself upside the head.");
      } else {

        playerAgent.attemptAction(new Attacking(playerAgent.getActor(),
            GameInput.getPlayerTarget()));

      }

      GameInput.enterMode(GameMode.EXPLORE);

    }

  }


}