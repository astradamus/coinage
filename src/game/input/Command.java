package game.input;


/**
 *
 */
public interface Command {

  int getHotKeyCode();
  String getControlText();

  void execute();

}