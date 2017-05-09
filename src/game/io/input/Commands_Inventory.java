package game.io.input;

import controller.action.Action_EquipWeapon;
import controller.action.Action_Place;
import controller.player.PlayerAgent;
import game.io.display.Event;
import game.io.display.EventLog;
import game.physical.Physical;
import thing.Thing;
import world.GlobalCoordinate;

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

            final Physical equip = getPlayerSelectedInventoryPhysical();

            if (equip != null) {

                if (equip.getClass() != Thing.class || ((Thing) equip).getWeaponComponent() == null) {
                    EventLog.registerEvent(Event.INVALID_INPUT, "You can't equip " + equip.getName() + ".");
                    return;
                }

                final PlayerAgent player = GameInput.getRunningGame().getPlayerAgent();
                player.attemptAction(new Action_EquipWeapon(player.getActor(), equip));

                GameInput.enterMode(GameMode.EXPLORE);
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

            final Physical placing = getPlayerSelectedInventoryPhysical();

            if (placing != null) {

                final PlayerAgent player = GameInput.getRunningGame().getPlayerAgent();

                // Prompt player to select a location and drop the item there.
                GameInput.beginSelectingCoordinate(
                        new Selector<>("PLACE WHERE?", player.getActor().getGlobalCoordinate(), 1,
                                       new SelectCallback<GlobalCoordinate>() {
                                           @Override
                                           public void execute(GlobalCoordinate selected) {

                                               player.attemptAction(new Action_Place(player.getActor(), selected, placing));

                                               GameInput.enterMode(GameMode.EXPLORE);
                                           }
                                       }));
            }
        }
    };


    static Physical getPlayerSelectedInventoryPhysical() {

        // Determine what inventory item the player has highlighted.
        Integer listSelectIndex = GameInput.getPlayerSelection();
        List<Physical> itemsHeld =
                GameInput.getRunningGame().getActivePlayerActor().getInventory().getItemsHeld();

        if (listSelectIndex != null && !itemsHeld.isEmpty()) {

            return itemsHeld.get(listSelectIndex);
        }

        return null;
    }

}