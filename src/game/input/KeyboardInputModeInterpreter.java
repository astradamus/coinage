package game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 */
public class KeyboardInputModeInterpreter implements KeyListener {

  private final ModeListener modeListener;

  public KeyboardInputModeInterpreter(ModeListener modeListener) {
    this.modeListener = modeListener;
  }

  @Override
  public void keyPressed(KeyEvent e) {
    for (InputMode inputMode : InputMode.values()) {
      if (inputMode.hotkeyCode == e.getKeyCode()) {
        modeListener.receiveMode(inputMode);
      }
    }
  }


  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void keyReleased(KeyEvent e) {}

}
