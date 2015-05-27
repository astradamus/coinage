package game.input;

import java.awt.event.KeyEvent;

/**
 *
 */
public enum InputMode {

  EXPLORE (KeyEvent.VK_ESCAPE),
  LOOK    (KeyEvent.VK_L);

  final int hotkeyCode;

  InputMode(int hotkeyCode) {
    this.hotkeyCode = hotkeyCode;
  }

}
