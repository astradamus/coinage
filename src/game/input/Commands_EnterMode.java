package game.input;

import game.Game;
import game.TimeMode;
import game.display.Event;
import game.display.EventLog;

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
      if (Game.getTimeMode() == TimeMode.PRECISION) {
        return "SPACE: Exit precision time.";
      } else {
        return "SPACE: Enter precision time.";
      }
    }

    @Override
    public void execute() {

      TimeMode next;
      TimeMode current = Game.getTimeMode();

      if (current == TimeMode.PRECISION) {
        Game.revertTimeMode();
        next = Game.getTimeMode();
      } else {
        next = TimeMode.PRECISION;
        Game.setTimeMode(next);
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
      Game.getActiveInputSwitch().enterMode(GameMode.ATTACK);
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
