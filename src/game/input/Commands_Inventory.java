package game.input;

import controller.player.PlayerController;
import game.Game;
import game.Physical;
import world.Coordinate;

import java.awt.event.KeyEvent;

/**
 *
 */
public enum Commands_Inventory implements Command {



  DROP {
    @Override
    public int getHotKeyCode() {
      return KeyEvent.VK_D;
    }

    @Override
    public String getControlText() {
      return "D: Drop item.";
    }

    @Override
    public void execute() {

      PlayerController playerController = Game.getActivePlayer();

      // Determine what we are dropping.
      Integer listSelectIndex = Game.getActiveInputSwitch().getPlayerSelection();

      if (listSelectIndex != null) {

        Physical dropping = playerController.getActor().getInventory().getItemsHeld()
            .get(listSelectIndex);

        // Prompt player to select a location and drop the item there.
        Game.getActiveInputSwitch().beginSelectingCoordinate(
            new Selector<>("DROP WHERE?", playerController.getCoordinate(), 1,
                new SelectCallback<Coordinate>() {
                  @Override
                  public void execute(Coordinate selected) {
                    playerController.startDropping(dropping, selected);
                    Game.getActiveInputSwitch().enterMode(GameMode.EXPLORE);
                  }
                }
            )
        );

      }

    }

  }


}
