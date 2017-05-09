package game.io.input;

import game.Game;
import game.TimeMode;
import game.io.GameEngine;
import game.io.display.DisplayElement;
import game.io.display.DisplayElement_Text;
import game.io.display.EventLog;
import world.GlobalCoordinate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public enum GameMode {


    EXPLORE {
        @Override
        public void onEnter() {

            // Unpause if paused.
            if (GameEngine.getTimeMode() == TimeMode.PAUSED) {
                GameEngine.revertTimeMode();
            }

            // Collapse event log if expanded.
            if (EventLog.getIsDisplayModeEnabled(EventLog.DisplayMode.EXPANDED)) {
                EventLog.toggleDisplayMode(EventLog.DisplayMode.EXPANDED);
            }
        }

        @Override
        public String getPrompt() {
            return null;
        }

        @Override
        public List<Command> getModeCommands() {
            return Arrays.asList(
                    Commands_EnterMode.TOGGLE_PRECISION_TIME,
                    Commands_EnterMode.ENTER_MODE_LOOK,
                    Commands_EnterMode.ENTER_MODE_INTERACT,
                    Commands_EnterMode.ENTER_MODE_ATTACK,
                    Commands_EnterMode.ENTER_MODE_INVENTORY,
                    Commands_EnterMode.ENTER_MODE_EVENT_LOG
            );
        }

        @Override
        public List<DisplayElement> getDisplayElements() {

            return Arrays.asList(
                    DisplayElement.MINIMAP,
                    DisplayElement.makeControlsList(getModeCommands(), 0)
            );

        }

    },


    ATTACK {
        @Override
        public void onEnter() {
            final Game runningGame = GameInput.getRunningGame();
            GameInput.setTargetCursor(TargetCursor.makeSquareTargeter(runningGame.getWorld(),
                                                                      runningGame.getActivePlayerActor()
                                                                              .getGlobalCoordinate(), 1));

            pauseIfUnpaused();
        }

        @Override
        public String getPrompt() {
            return "ATTACK WHAT?";
        }

        @Override
        public List<Command> getModeCommands() {
            return Arrays.asList(
                    Commands_EnterMode.ENTER_MODE_EXPLORE,
                    Commands_Attack.STRIKE
            );
        }

        @Override
        public List<DisplayElement> getDisplayElements() {

            return Arrays.asList(
                    DisplayElement.MINIMAP,
                    DisplayElement.makeControlsList(getModeCommands(), 0)
            );

        }

    },


    LOOK {
        @Override
        public void onEnter() {
            final Game runningGame = GameInput.getRunningGame();
            GameInput.setTargetCursor(TargetCursor.makeSquareTargeter(runningGame.getWorld(),
                                                                      runningGame.getActivePlayerActor()
                                                                              .getGlobalCoordinate(), null));

            pauseIfUnpaused();
        }

        @Override
        public String getPrompt() {
            return "YOU SEE:";
        }

        @Override
        public List<Command> getModeCommands() {
            return Collections.singletonList(
                    Commands_EnterMode.ENTER_MODE_EXPLORE
            );
        }

        @Override
        public List<DisplayElement> getDisplayElements() {
            return Arrays.asList(
                    DisplayElement.MINIMAP,
                    DisplayElement.CONTROL_ESCAPE,
                    DisplayElement.makeCurrentPrompt(),
                    DisplayElement.makePhysicalsList(GameInput.getRunningGame()
                                                             .getWorld()
                                                             .getSquare(GameInput
                                                                                .getPlayerTarget())
                                                             .getAll(), false),
                    DisplayElement.makeControlsList(getModeCommands(), 1)
            );
        }

    },


    INTERACT {
        @Override
        public void onEnter() {
            final Game runningGame = GameInput.getRunningGame();
            GameInput.setTargetCursor(TargetCursor.makeSquareAndListTargeter(runningGame.getWorld(),
                                                                             runningGame.getActivePlayerActor()
                                                                                     .getGlobalCoordinate(), 1));

            pauseIfUnpaused();
        }

        @Override
        public String getPrompt() {
            return "INTERACT HOW?";
        }

        @Override
        public List<Command> getModeCommands() {
            return Arrays.asList(
                    Commands_EnterMode.ENTER_MODE_EXPLORE,
                    Commands_Interact.COLLECT
            );
        }

        @Override
        public List<DisplayElement> getDisplayElements() {

            // If there is a target, create an element for the physicals there.
            GlobalCoordinate playerTarget = GameInput.getPlayerTarget();
            DisplayElement_Text physicalsAtTarget = (playerTarget == null) ? null :
                    DisplayElement.makePhysicalsList(GameInput.getRunningGame()
                                                             .getWorld()
                                                             .getSquare(playerTarget)
                                                             .getAll(), true);

            return Arrays.asList(
                    DisplayElement.MINIMAP,
                    DisplayElement.CONTROL_ESCAPE,
                    DisplayElement.makeCurrentPrompt(),
                    physicalsAtTarget,
                    DisplayElement.makeControlsList(getModeCommands(), 1)
            );

        }

    },


    INVENTORY {
        @Override
        public void onEnter() {
            final Game runningGame = GameInput.getRunningGame();
            GameInput.setTargetCursor(TargetCursor.makeListTargeter(runningGame.getWorld(),
                                                                    runningGame.getActivePlayerActor()
                                                                            .getInventory()
                                                                            .getItemsHeld()
                                                                            .size()));

            pauseIfUnpaused();
        }

        @Override
        public String getPrompt() {
            return "YOU ARE CARRYING:";
        }

        @Override
        public List<Command> getModeCommands() {
            return Arrays.asList(
                    Commands_EnterMode.ENTER_MODE_EXPLORE,
                    Commands_Inventory.EQUIP,
                    Commands_Inventory.DROP
            );
        }

        @Override
        public List<DisplayElement> getDisplayElements() {

            DisplayElement_Text itemsHeld =
                    DisplayElement.makePhysicalsList(GameInput.getRunningGame()
                                                             .getActivePlayerActor().getInventory().getItemsHeld(),
                                                     true);

            return Arrays.asList(
                    DisplayElement.MINIMAP,
                    DisplayElement.CONTROL_ESCAPE,
                    DisplayElement.makeCurrentPrompt(),
                    itemsHeld,
                    DisplayElement.makeControlsList(getModeCommands(), 1)
            );

        }

    },

    EVENT_LOG {
        @Override
        public void onEnter() {
            EventLog.toggleDisplayMode(EventLog.DisplayMode.EXPANDED);

            pauseIfUnpaused();
        }

        @Override
        public String getPrompt() {
            return "VIEWING EVENT LOG...";
        }

        @Override
        public List<Command> getModeCommands() {
            return Arrays.asList(
                    Commands_EnterMode.ENTER_MODE_EXPLORE,
                    Commands_EventLog.SCROLL_BACKWARD,
                    Commands_EventLog.SCROLL_FORWARD,
                    Commands_EventLog.TOGGLE_MODE
            );
        }

        @Override
        public List<DisplayElement> getDisplayElements() {

            return Arrays.asList(
                    DisplayElement.MINIMAP,
                    DisplayElement.CONTROL_ESCAPE,
                    DisplayElement.makeCurrentPrompt(),
                    DisplayElement.makeControlsList(getModeCommands(), 1)
            );

        }

    };


    private static void pauseIfUnpaused() {
        if (GameEngine.getTimeMode() == TimeMode.LIVE || GameEngine.getTimeMode() == TimeMode.PRECISION) {
            GameEngine.setTimeMode(TimeMode.PAUSED);
        }
    }


    public abstract void onEnter();

    abstract String getPrompt();

    public abstract List<Command> getModeCommands();

    public abstract List<DisplayElement> getDisplayElements();

}