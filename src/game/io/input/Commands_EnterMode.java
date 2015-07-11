package game.io.input;

import game.TimeMode;
import game.io.GameEngine;
import game.io.display.Event;
import game.io.display.EventLog;

import java.awt.event.KeyEvent;

/**
 *
 */
public enum Commands_EnterMode implements Command {


  TOGGLE_PRECISION_TIME {

    @Override
    public int getHotKeyCode() {
      return KeyEvent.VK_SPACE;
    }

    @Override
    public String getControlText() {
      if (GameEngine.getTimeMode() == TimeMode.PRECISION) {
        return "SPACE: Exit precision time.";
      } else {
        return "SPACE: Enter precision time.";
      }
    }

    @Override
    public void execute() {

      TimeMode next;
      TimeMode current = GameEngine.getTimeMode();

      if (current == TimeMode.PRECISION) {
        GameEngine.revertTimeMode();
        next = GameEngine.getTimeMode();
      } else {
        next = TimeMode.PRECISION;
        GameEngine.setTimeMode(next);
      }
      EventLog.registerEvent(Event.INVALID_ACTION, next.getEnterText());

    }

  },

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
      GameInput.enterMode(GameMode.EXPLORE);
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
      GameInput.enterMode(GameMode.LOOK);
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
      GameInput.enterMode(GameMode.INTERACT);
    }

  },



  ENTER_MODE_ATTACK {

    @Override
    public int getHotKeyCode() {
      return KeyEvent.VK_A;
    }

    @Override
    public String getControlText() {
      return "A: Attack.";
    }

    @Override
    public void execute() {
      GameInput.enterMode(GameMode.ATTACK);
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
      GameInput.enterMode(GameMode.INVENTORY);
    }

  },

  ENTER_MODE_EVENTLOG {

    @Override
    public int getHotKeyCode() {
      return KeyEvent.VK_E;
    }

    @Override
    public String getControlText() {
      return "E: Open event log.";
    }

    @Override
    public void execute() {
      Game.getActiveInputSwitch().enterMode(GameMode.EVENTLOG);
    }

  }

}
