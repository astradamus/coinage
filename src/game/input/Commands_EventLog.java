package game.input;

import actor.Actor;
import controller.action.Attacking;
import controller.action.PickingUp;
import controller.player.PlayerController;
import game.Game;
import game.display.Event;
import game.display.EventLog;
import game.physical.Physical;

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

  }

}
