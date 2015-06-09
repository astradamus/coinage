package game.input;

import controller.player.PlayerController;
import game.Game;
import game.Physical;
import world.Coordinate;

import java.awt.event.KeyEvent;

/**
 *
 */
public enum Commands_Interact implements Command {



  PICK_UP {

    @Override
    public int getHotKeyCode() {
      return KeyEvent.VK_P;
    }

    @Override
    public String getControlText() {
      return "P: Pick up item.";
    }

    @Override
    public void execute() {

      PlayerController playerController = Game.getActivePlayer();

      // Have the player choose what to pick up, then start picking it up.
      Game.getActiveInputSwitch().beginSelectingPhysical(
          new Selector<>("PICK UP WHAT?", playerController.getCoordinate(), 1,
              new SelectCallback<Physical>() {
                @Override
                public void execute(Physical selected) {
                  playerController.startGrabbing(selected, Game.getActiveInputSwitch().getPlayerTarget());
                  Game.getActiveInputSwitch().enterMode(GameMode.EXPLORE);
                }
              }
          )
      );



    }

  }


}
