package game;

import java.awt.event.KeyEvent;

/**
 *
 */
public enum GameMode {

  EXPLORE (KeyEvent.VK_ESCAPE),
  LOOK    (KeyEvent.VK_L);

  final int hotkeyCode;

  GameMode(int hotkeyCode) {
    this.hotkeyCode = hotkeyCode;
  }

}
