package game.io.better_ui;

import controller.action.TurnThenMove;
import game.Direction;
import game.Game;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 */
public class GameFrame extends JFrame implements DirectionListener {

  private final int tileSize;
  private final Game game;

  private final GamePanel gamePanel;


  public GameFrame(Game game, int tileSize) {
    super("Coinage");
    this.tileSize = tileSize;

    this.game = game;
    this.gamePanel = new GamePanel(game, tileSize);

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    getContentPane().add(gamePanel);
    pack();
    setVisible(true);

    addKeyListener(new WASDDirectionInterpreter(this));
    addKeyListener(gamePanel.getMouseControl());
  }


  @Override
  public void receiveDirection(Direction direction) {
    // TODO Should call some static method somewhere, perhaps in TurnThenMove, that will evaluate
    // for us whether or not we need to turn before moving, so we can just say "TTM
    // .turnIfNecessary(...)" and it won't perform turns if it doesn't have to, it will just skip
    // to moving.
    game.getPlayerAgent().attemptAction(new TurnThenMove(game.getActivePlayerActor(), direction, false));
  }


  @Override
  public void receiveDirectionsCleared() {
    game.getPlayerAgent().getActor().doNotRepeatAction(); // TODO Only stop repeating MOVEMENTS.
  }
}