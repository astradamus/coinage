package game.io.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 */
public class ModeCommandInterpreter implements KeyListener {

  @Override
  public void keyPressed(KeyEvent e) {

    for (Command command : GameInput.getGameMode().getModeCommands()) {
      if (command.getHotKeyCode() == e.getKeyCode()) {
        command.execute();
        return;
      }
    }

  }


  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void keyReleased(KeyEvent e) {}

}