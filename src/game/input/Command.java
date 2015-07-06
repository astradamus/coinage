package game.input;


import game.Game;
import game.physical.Physical;
import world.World;

import java.util.List;

/**
 *
 */
public interface Command {

  int getHotKeyCode();
  String getControlText();

  void execute(World world);


  static Physical getPlayerSelectedPhysical() {

    // Determine what we are equipping.
    Integer listSelectIndex = Game.getActiveInputSwitch().getPlayerSelection();
    List<Physical> itemsHeld = Game.getActiveInputSwitch().getPlayerController().getActor().getInventory().getItemsHeld();

    if (listSelectIndex != null && !itemsHeld.isEmpty()) {

      return itemsHeld.get(listSelectIndex);

    }

    return null;

  }

}