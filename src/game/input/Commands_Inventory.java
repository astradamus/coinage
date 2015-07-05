package game.input;

import actor.Actor;
import controller.action.EquipWeapon;
import controller.action.Placing;
import game.Game;
import game.physical.Physical;
import world.Coordinate;
import world.World;

import java.awt.event.KeyEvent;

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
    public void execute(World world) {

      final Physical equipping = Command.getPlayerSelectedPhysical();

      if (equipping != null) {

        final Actor player = Game.getActivePlayerActor();
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
    public void execute(World world) {


      final Physical placing = Command.getPlayerSelectedPhysical();

      if (placing != null) {

        final Actor player = Game.getActivePlayerActor();

        // Prompt player to select a location and drop the item there.
        Game.getActiveInputSwitch().beginSelectingCoordinate(
            new Selector<>("PLACE WHERE?", player.getCoordinate(), 1,
                new SelectCallback<Coordinate>() {
                  @Override
                  public void execute(Coordinate selected) {

                    player.attemptAction(new Placing(player, selected, placing));

                    Game.getActiveInputSwitch().enterMode(GameMode.EXPLORE);

                  }
                }
            )
        );

      }

    }

  }


}