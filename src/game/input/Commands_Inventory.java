package game.input;

import controller.action.Placing;
import controller.player.PlayerController;
import game.Game;
import game.physical.Physical;
import world.Coordinate;

import java.awt.event.KeyEvent;
import java.util.List;

/**
 *
 */
public enum Commands_Inventory implements Command {



  DROP {
    @Override
    public int getHotKeyCode() {
      return KeyEvent.VK_P;
    }

    @Override
    public String getControlText() {
      return "P: Place item.";
    }

    @Override
    public void execute() {

      PlayerController playerController = Game.getActivePlayer();

      // Determine what we are dropping.
      Integer listSelectIndex = Game.getActiveInputSwitch().getPlayerSelection();
      List<Physical> itemsHeld = playerController.getActor().getInventory().getItemsHeld();

      if (listSelectIndex != null && !itemsHeld.isEmpty()) {

        Physical placing = itemsHeld.get(listSelectIndex);

        // Prompt player to select a location and drop the item there.
        Game.getActiveInputSwitch().beginSelectingCoordinate(
            new Selector<>("PLACE WHERE?", playerController.getActor().getCoordinate(), 1,
                new SelectCallback<Coordinate>() {
                  @Override
                  public void execute(Coordinate selected) {

                    playerController.attemptAction(
                        new Placing(playerController, selected, placing)
                    );

                    Game.getActiveInputSwitch().enterMode(GameMode.EXPLORE);

                  }
                }
            )
        );

      }

    }

  }


}
