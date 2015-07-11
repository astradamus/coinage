package game.io.input;

import game.io.display.Event;
import game.io.display.EventLog;

import java.awt.event.KeyEvent;

/**
 *
 */
public enum Commands_EventLog implements Command {


  SCROLL_BACKWARD {

    @Override
    public int getHotKeyCode() {
      return KeyEvent.VK_OPEN_BRACKET;
    }

    @Override
    public String getControlText() {
      return "[: Scroll backwards.";
    }

    @Override
    public void execute() {
      EventLog.scrollLogBackwards();
    }

  },

  SCROLL_FORWARD {

    @Override
    public int getHotKeyCode() {
      return KeyEvent.VK_CLOSE_BRACKET;
    }

    @Override
    public String getControlText() {
      return "]: Scroll forwards.";
    }

    @Override
    public void execute() {
      EventLog.scrollLogForwards();
    }

  },

  TOGGLE_MODE {

    @Override
    public int getHotKeyCode() {
      return KeyEvent.VK_P;
    }

    @Override
    public String getControlText() {
      String text = "P: ";
      text += EventLog.getIsDisplayModeEnabled(EventLog.DisplayMode.AUTO_HIDE) ? "Pin" : "Unpin";
      return text + " event log on all menus.";
    }

    @Override
    public void execute() {
      EventLog.toggleDisplayMode(EventLog.DisplayMode.PINNED);

      String message = "You have ";
      String messageB = " the event log ";
      if (EventLog.getIsDisplayModeEnabled(EventLog.DisplayMode.PINNED)) {
        message = message + "pinned" + messageB + "to";
      }
      else {
        message = message + "unpinned" + messageB + "from";
      }

      message = message + " the screen.";

      EventLog.registerEvent(Event.ALERT_MINOR, message);
    }

  }


}