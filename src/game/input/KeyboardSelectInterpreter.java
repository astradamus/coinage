package game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 */
public class KeyboardSelectInterpreter implements KeyListener {

  private final SelectListener selectListener;

  public KeyboardSelectInterpreter(SelectListener selectListener) {
    this.selectListener = selectListener;
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {

    switch (e.getKeyCode()) {
      case KeyEvent.VK_ADD:
        selectListener.receiveSelectScroll(1);
        break;
      case KeyEvent.VK_SUBTRACT:
        selectListener.receiveSelectScroll(-1);
        break;
      case KeyEvent.VK_ENTER:
        selectListener.receiveSubmitSelection();
        break;
    }

  }

  @Override
  public void keyReleased(KeyEvent e) {

  }

}
