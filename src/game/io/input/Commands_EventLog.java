package game.io.input;

import game.Game;
import game.io.display.EventLog;
import game.io.input.Command;
import game.io.input.GameInput;
import game.io.input.GameMode;

import java.awt.event.KeyEvent;

/**
 *
 */
public enum Commands_EventLog implements Command {


  SCROLL_DOWN {

    @Override
    public int getHotKeyCode() {
      return KeyEvent.VK_OPEN_BRACKET;
    }

    @Override
    public String getControlText() {
      return "[: Scroll down.";
    }

    @Override
    public void execute() {
      EventLog.scrollLogDown();
    }

  },

  SCROLL_UP {

    @Override
    public int getHotKeyCode() {
      return KeyEvent.VK_CLOSE_BRACKET;
    }

    @Override
    public String getControlText() {
      return "]: Scroll up.";
    }

    @Override
    public void execute() {
      EventLog.scrollLogUp();
    }

  },

  TOGGLE_MODE {

    @Override
    public int getHotKeyCode() {
      return KeyEvent.VK_F;
    }

    @Override
    public String getControlText() {
      if (EventLog.getExpandedMode() == 1) {
        return "F: Minimize overlay";
      }
      else {
        return "F: Maximize overlay.";
      }

    }

    @Override
    public void execute() {
      EventLog.toggleLogMode();
      GameInput.enterMode(GameMode.EXPLORE);
    }

  }


}
