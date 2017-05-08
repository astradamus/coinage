package game.io.input;


/**
 *
 */
public interface Command {

    int getHotKeyCode();

    String getControlText();

    void execute();

}