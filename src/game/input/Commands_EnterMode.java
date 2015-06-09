package game.input;

import game.Game;

import java.awt.event.KeyEvent;

/**
 *
 */
public enum Commands_EnterMode implements Command {



  ENTER_MODE_EXPLORE {
    @Override
    public int getHotKeyCode() {
      return KeyEvent.VK_ESCAPE;
    }

    @Override
    public String getControlText() {
      return "(press ESC to return)";
    }

    @Override
    public void execute() {
      Game.getActiveInputSwitch().enterMode(GameMode.EXPLORE);
    }

  },



  ENTER_MODE_LOOK {

    @Override
    public int getHotKeyCode() {
      return KeyEvent.VK_L;
    }

    @Override
    public String getControlText() {
      return "L: Look around.";
    }

    @Override
    public void execute() {
      Game.getActiveInputSwitch().enterMode(GameMode.LOOK);
    }

  },



  ENTER_MODE_INTERACT {

    @Override
    public int getHotKeyCode() {
      return KeyEvent.VK_I;
    }

    @Override
    public String getControlText() {
      return "I: Interact.";
    }

    @Override
    public void execute() {
      Game.getActiveInputSwitch().enterMode(GameMode.INTERACT);
    }

  },



  ENTER_MODE_INVENTORY {

    @Override
    public int getHotKeyCode() {
      return KeyEvent.VK_O;
    }

    @Override
    public String getControlText() {
      return "O: Open inventory.";
    }

    @Override
    public void execute() {
      Game.getActiveInputSwitch().enterMode(GameMode.INVENTORY);
    }

  }



}
