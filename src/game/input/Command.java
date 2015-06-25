package game.input;


import controller.player.PlayerController;
import game.Game;
import game.physical.Physical;

import java.util.List;

/**
 *
 */
public interface Command {

  int getHotKeyCode();
  String getControlText();

  void execute();


  static Physical getPlayerSelectedPhysical() {

    PlayerController playerController = Game.getActivePlayer();

    // Determine what we are equipping.
    Integer listSelectIndex = Game.getActiveInputSwitch().getPlayerSelection();
    List<Physical> itemsHeld = playerController.getActor().getInventory().getItemsHeld();

    if (listSelectIndex != null && !itemsHeld.isEmpty()) {

      return itemsHeld.get(listSelectIndex);

    }

    return null;

  }

}