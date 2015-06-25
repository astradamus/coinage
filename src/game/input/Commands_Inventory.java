package game.input;

import controller.action.EquipWeapon;
import controller.action.Placing;
import controller.player.PlayerController;
import game.Game;
import game.display.EventLog;
import game.physical.Physical;
import world.Coordinate;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 *
 */
public enum Commands_Inventory implements Command {

  EQUIP {
    @Override
    public int getHotKeyCode() {
      return KeyEvent.VK_E;
    }

    @Override
    public String getControlText() {
      return "E: Equip item.";
    }

    @Override
    public void execute() {

      final Physical equipping = Command.getPlayerSelectedPhysical();

      if (equipping != null) {

        final PlayerController player = Game.getActivePlayer();
        player.attemptAction(new EquipWeapon(player, equipping));

        Game.getActiveInputSwitch().enterMode(GameMode.EXPLORE);

      }

    }

  },

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


      final Physical placing = Command.getPlayerSelectedPhysical();

      if (placing != null) {

        final PlayerController playerController = Game.getActivePlayer();

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
