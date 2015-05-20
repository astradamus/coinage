package game;

import actor.Actor;
import actor.ActorFactory;
import controller.AnimalController;
import controller.PlayerController;
import world.WorldFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Prototypical rendering engine.
 */
public class Main {

  public static final int TILE_SIZE = 15;


  private static final JFrame window = new JFrame("Coinage") {
    {
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
  };
  public static final JPanel panel = new JPanel() {
    @Override
    public void paint(Graphics g) {
      super.paint(g);
      Graphics2D g2d = (Graphics2D) g;
      for (int y = 0; y < Game.WORLD.getHeight(); y++) {
        for (int x = 0; x < Game.WORLD.getWidth(); x++) {

          Physical visible = Game.WORLD.getPriorityPhysical(x, y);

          int placeX = x * TILE_SIZE;
          int placeY = y * TILE_SIZE;

          Color bgColor = visible.getBGColor();
          Color color = visible.getColor();
          if (bgColor != null) {
            g2d.setColor(bgColor);
            g2d.fillRect((placeX-TILE_SIZE/6), (int) (placeY-TILE_SIZE*0.85),TILE_SIZE,
                TILE_SIZE);
          }

          g2d.setColor(color);
          g2d.drawChars(new char[] {visible.getAppearance()},0,1,placeX,placeY);
        }
      }
    }
  };

  public static void main(String[] args) {
    panel.setBackground(Color.BLACK);
    panel.setFont(new Font("Droid Sans Mono",Font.BOLD,TILE_SIZE));
    window.add(panel);
    window.setSize((Game.WORLD.getWidth()+1)*TILE_SIZE, (Game.WORLD.getHeight()+1)*TILE_SIZE);
    window.setVisible(true);
  }


  static {
    Game.WORLD = WorldFactory.standardGeneration(75, 75);
    for (int i = 0; i < 20; i++) {
      createActor("DOG", Game.RANDOM.nextInt(Game.WORLD.getWidth()), Game.RANDOM.nextInt(Game.WORLD
          .getHeight()));
      createActor("CAT", Game.RANDOM.nextInt(Game.WORLD.getWidth()), Game.RANDOM.nextInt(Game.WORLD
          .getHeight()));
    }

    Actor actor = ActorFactory.makeActor("HUMAN");
    Point point = new Point(Game.WORLD.getWidth()/2,Game.WORLD.getHeight()/2);
    Game.WORLD.placePhysical(actor, point.x, point.y);
    PlayerController playerController = new PlayerController(actor, point);
    Game.register(playerController);
    Game.register(playerController.getKeyListener());

    Game.Engine.start(window);

    window.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        System.out.println(Integer.toString(e.getKeyCode()));
      }

      @Override
      public void keyPressed(KeyEvent e) {

      }

      @Override
      public void keyReleased(KeyEvent e) {

      }
    });
  }

  static void createActor(String ID, int x, int y) {
    Actor actor = ActorFactory.makeActor(ID);
    Point point = new Point(x,y);
    Game.WORLD.placePhysical(actor, point.x, point.y);
    Game.register(new AnimalController(actor, point));
  }
}