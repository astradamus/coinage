package game.input;

import game.Game;
import world.World;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 */
public class ModeCommandInterpreter implements KeyListener {

  private final World world;

  public ModeCommandInterpreter(World world) {
    this.world = world;
  }


  @Override
  public void keyPressed(KeyEvent e) {

    for (Command command : Game.getActiveInputSwitch().getGameMode().getModeCommands()) {
      if (command.getHotKeyCode() == e.getKeyCode()) {
        command.execute(world);
      }
    }

  }


  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void keyReleased(KeyEvent e) {}

}
