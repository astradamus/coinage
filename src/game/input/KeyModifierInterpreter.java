package game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Stack;

public class KeyModifierInterpreter implements KeyListener {

  private Stack<KeyModifier> heldModifiers = new Stack<>();


  public KeyModifier getLatestModifier() {

    if (heldModifiers.isEmpty()) {
      return null;
    }

    return heldModifiers.peek();

  }


  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
      heldModifiers.push(KeyModifier.SHIFT);
    }
    else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
      heldModifiers.push(KeyModifier.CTRL);
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
      heldModifiers.remove(KeyModifier.SHIFT);
    }
    else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
      heldModifiers.remove(KeyModifier.CTRL);
    }
  }

}
