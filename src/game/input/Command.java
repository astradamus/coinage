package game.input;


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

    // Determine what we are equipping.
    Integer listSelectIndex = Game.getActiveInputSwitch().getPlayerSelection();
    List<Physical> itemsHeld = Game.getActivePlayerActor().getInventory().getItemsHeld();

    if (listSelectIndex != null && !itemsHeld.isEmpty()) {

      return itemsHeld.get(listSelectIndex);

    }

    return null;

  }

}