package game.input;

import game.Game;
import game.Physical;
import world.Coordinate;

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

    }

    @Override
    public String getPrompt() {
      return null;
    }

    @Override
    public List<Command> getModeCommands() {
      return Arrays.asList(
          Commands_EnterMode.ENTER_MODE_LOOK,
          Commands_EnterMode.ENTER_MODE_INTERACT,
          Commands_EnterMode.ENTER_MODE_INVENTORY
      );
    }

    @Override
    public List<Physical> getSidePanelPhysicals() {
      return null;
    }

  },



  LOOK {

    @Override
    public void onEnter() {
      Game.getActiveInputSwitch().setTargetCursor(TargetCursor.makeSquareTargeter(Game
          .getActivePlayer().getCoordinate(), null));
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
    public List<Physical> getSidePanelPhysicals() {
      return Game.getActiveInputSwitch().getPlayerTarget().getSquare().getAll();
    }

  },



  INTERACT {

    @Override
    public void onEnter() {

    }

    @Override
    public String getPrompt() {
      return "INTERACT HOW?";
    }

    @Override
    public List<Command> getModeCommands() {
      return Arrays.asList(
          Commands_EnterMode.ENTER_MODE_EXPLORE,
          Commands_Interact.PICK_UP
      );
    }

    @Override
    public List<Physical> getSidePanelPhysicals() {
      Coordinate playerTarget = Game.getActiveInputSwitch().getPlayerTarget();
      return (playerTarget == null) ? null : playerTarget.getSquare().getAll();
    }

  },



  INVENTORY {

    @Override
    public void onEnter() {
      Game.getActiveInputSwitch().setTargetCursor(TargetCursor.makeListTargeter(Game
          .getActivePlayer().getActor().getInventory().getItemsHeld().size()));
    }

    @Override
    public String getPrompt() {
      return "YOU ARE CARRYING:";
    }

    @Override
    public List<Command> getModeCommands() {
      return Arrays.asList(
          Commands_EnterMode.ENTER_MODE_EXPLORE,
          Commands_Inventory.DROP
      );
    }

    @Override
    public List<Physical> getSidePanelPhysicals() {
      return Game.getActivePlayer().getActor().getInventory().getItemsHeld();
    }

  };



  public abstract void onEnter();
  public abstract String getPrompt();
  public abstract List<Command> getModeCommands();

  public abstract List<Physical> getSidePanelPhysicals();

}