package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 */
public class ModeHotkeyInterpreter implements KeyListener {

  private final ModeHotkeyListener modeHotkeyListener;

  public ModeHotkeyInterpreter(ModeHotkeyListener modeHotkeyListener) {
    this.modeHotkeyListener = modeHotkeyListener;
  }

  @Override
  public void keyPressed(KeyEvent e) {
    for (GameMode gameMode : GameMode.values()) {
      if (gameMode.hotkeyCode == e.getKeyCode()) {
        modeHotkeyListener.receiveMode(gameMode);
      }
    }
  }


  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void keyReleased(KeyEvent e) {}

}
