package game.io.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 */
class ListSelectInterpreter implements KeyListener {

  private final ListSelectionListener listSelectionListener;

  public ListSelectInterpreter(ListSelectionListener listSelectionListener) {
    this.listSelectionListener = listSelectionListener;
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {

    switch (e.getKeyCode()) {
      case KeyEvent.VK_ADD:
      case KeyEvent.VK_EQUALS:
        listSelectionListener.receiveSelectScroll(1);
        break;
      case KeyEvent.VK_SUBTRACT:
      case KeyEvent.VK_MINUS:
        listSelectionListener.receiveSelectScroll(-1);
        break;
      case KeyEvent.VK_ENTER:
        listSelectionListener.receiveSubmitSelection();
        break;
    }

  }

  @Override
  public void keyReleased(KeyEvent e) {

  }

}
