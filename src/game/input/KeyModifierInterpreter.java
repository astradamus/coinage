package game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyModifierInterpreter implements KeyListener {

  private KeyModifier lastModifier;


  public KeyModifier getLatestModifier() {
    return lastModifier;
  }


  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
      lastModifier = KeyModifier.SHIFT;
    }
    else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
      lastModifier = KeyModifier.CTRL;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if ((lastModifier == KeyModifier.SHIFT && e.getKeyCode() == KeyEvent.VK_SHIFT)
    || (lastModifier == KeyModifier.CTRL && e.getKeyCode() == KeyEvent.VK_CONTROL)) {
      lastModifier = null;
    }
  }

}