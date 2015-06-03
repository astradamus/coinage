package game.input;

import java.awt.event.KeyEvent;

/**
 *
 */
public enum InputMode {

  EXPLORE   (KeyEvent.VK_ESCAPE),
  LOOK      (KeyEvent.VK_L),
  INVENTORY (KeyEvent.VK_I),
  GRAB      (KeyEvent.VK_G),
  DROP      (KeyEvent.VK_D);

  final int hotkeyCode;

  InputMode(int hotkeyCode) {
    this.hotkeyCode = hotkeyCode;
  }

}
