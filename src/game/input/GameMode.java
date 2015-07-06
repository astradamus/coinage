package game.input;

import game.Game;
import game.TimeMode;
import game.display.DisplayElement;
import game.display.DisplayElement_Text;
import world.Coordinate;
import world.World;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public enum GameMode {



  EXPLORE {

    @Override
    public void onEnter(World world) {

      if (Game.getTimeMode() == TimeMode.PAUSED) {
        Game.revertTimeMode();
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
          Commands_EnterMode.ENTER_MODE_INVENTORY
      );
    }

    @Override
    public List<DisplayElement> getDisplayElements(World world) {

      return Arrays.asList(
          DisplayElement.MINIMAP,
          DisplayElement.makeControlsList(getModeCommands(), 0)
      );

    }

  },


  ATTACK {

    @Override
    public void onEnter(World world) {
      Game.getActiveInputSwitch().setTargetCursor(TargetCursor.makeSquareTargeter(world, Game.getActiveInputSwitch().getPlayerController().getActor().getCoordinate(), 1));

      if (Game.getTimeMode() == TimeMode.LIVE || Game.getTimeMode() == TimeMode.PRECISION) {
        Game.setTimeMode(TimeMode.PAUSED);
      }
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
    public List<DisplayElement> getDisplayElements(World world) {

      return Arrays.asList(
          DisplayElement.MINIMAP,
          DisplayElement.makeControlsList(getModeCommands(), 0)
      );

    }

  },



  LOOK {

    @Override
    public void onEnter(World world) {
      Game.getActiveInputSwitch().setTargetCursor(TargetCursor.makeSquareTargeter(world, Game.getActiveInputSwitch().getPlayerController().getActor().getCoordinate(), null));

      if (Game.getTimeMode() == TimeMode.LIVE || Game.getTimeMode() == TimeMode.PRECISION) {
        Game.setTimeMode(TimeMode.PAUSED);
      }
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
    public List<DisplayElement> getDisplayElements(World world) {
      return Arrays.asList(
          DisplayElement.MINIMAP,
          DisplayElement.CONTROL_ESCAPE,
          DisplayElement.makeCurrentPrompt(),
          DisplayElement.makePhysicalsList(world.getSquare(Game.getActiveInputSwitch()
              .getPlayerTarget()).getAll(), false),
          DisplayElement.makeControlsList(getModeCommands(), 1)
      );
    }

  },



  INTERACT {

    @Override
    public void onEnter(World world) {
      Game.getActiveInputSwitch().setTargetCursor(TargetCursor.makeSquareAndListTargeter(world, Game.getActiveInputSwitch().getPlayerController().getActor().getCoordinate(), 1));

      if (Game.getTimeMode() == TimeMode.LIVE || Game.getTimeMode() == TimeMode.PRECISION) {
        Game.setTimeMode(TimeMode.PAUSED);
      }
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
    public List<DisplayElement> getDisplayElements(World world) {

      // If there is a target, create an element for the physicals there.
      Coordinate playerTarget = Game.getActiveInputSwitch().getPlayerTarget();
      DisplayElement_Text physicalsAtTarget = (playerTarget == null) ? null :
          DisplayElement.makePhysicalsList(world.getSquare(playerTarget).getAll(), true);

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
    public void onEnter(World world) {
      Game.getActiveInputSwitch().setTargetCursor(TargetCursor.makeListTargeter(world, Game.getActiveInputSwitch().getPlayerController().getActor().getInventory().getItemsHeld().size()));

      if (Game.getTimeMode() == TimeMode.LIVE || Game.getTimeMode() == TimeMode.PRECISION) {
        Game.setTimeMode(TimeMode.PAUSED);
      }
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
    public List<DisplayElement> getDisplayElements(World world) {

      DisplayElement_Text itemsHeld =
          DisplayElement.makePhysicalsList(Game.getActiveInputSwitch().getPlayerController().getActor().getInventory().getItemsHeld(),true);

      return Arrays.asList(
          DisplayElement.MINIMAP,
          DisplayElement.CONTROL_ESCAPE,
          DisplayElement.makeCurrentPrompt(),
          itemsHeld,
          DisplayElement.makeControlsList(getModeCommands(), 1)
      );

    }

  };


  public abstract void onEnter(World world);
  abstract String getPrompt();
  public abstract List<Command> getModeCommands();

  public abstract List<DisplayElement> getDisplayElements(World world);

}