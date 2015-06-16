package game.display;

import actor.Actor;
import controller.ActorController;
import game.Direction;
import game.Game;
import world.Coordinate;

import java.awt.*;

/**
 *
 */
public class ActionOverlay {


  public static final int SQUARE_SIZE = GameDisplay.SQUARE_SIZE;


  public static void drawOverlay(Graphics g) {

    // Draw an overlay on actors indicating the direction they are facing.
    Game.getActiveControllers().getControllersByArea(ActorController.class,
        Game.getActivePlayer().getLocality())
        .forEach(controller -> drawFacingOverlay(g, (ActorController) controller, 3));

  }

  private static void drawFacingOverlay(Graphics g, ActorController controller, int thickness) {

    Actor actor = controller.getActor();

    Direction facing = actor.getFacing();

    g.setColor(actor.getAppearance().getColor());

    Coordinate actorAt = actor.getCoordinate();

    int[] drawX = new int[6];
    int[] drawY = new int[6];

    int originX = actorAt.localX * SQUARE_SIZE;
    int originY = actorAt.localY * SQUARE_SIZE;

    Point[] points = new Point[] {
      getOffsetFor(facing.getLeftNeighbor(),0),
      getOffsetFor(facing,0),
      getOffsetFor(facing.getRightNeighbor(),0),

      getOffsetFor(facing.getRightNeighbor(),thickness),
      getOffsetFor(facing,thickness),
      getOffsetFor(facing.getLeftNeighbor(),thickness)
    };

    for (int i2 = 0; i2 < points.length; i2++) {
      Point point = points[i2];
      drawX[i2] = originX + point.x;
      drawY[i2] = originY + point.y;
    }

    g.fillPolygon(drawX,drawY,6);


  }



  private static Point getOffsetFor(Direction direction, int gap) {
    Point offset = new Point(-gap,-gap);

    switch (direction) {

      case NORTH:
        offset.x = SQUARE_SIZE/2;
        break;
      case NORTH_EAST:
        offset.x = SQUARE_SIZE + gap;
        break;
      case EAST:
        offset.x = SQUARE_SIZE + gap;
        offset.y = SQUARE_SIZE/2;
        break;
      case SOUTH_EAST:
        offset.x = SQUARE_SIZE + gap;
        offset.y = SQUARE_SIZE + gap;
        break;
      case SOUTH:
        offset.x = SQUARE_SIZE/2;
        offset.y = SQUARE_SIZE + gap;
        break;
      case SOUTH_WEST:
        offset.y = SQUARE_SIZE + gap;
        break;
      case WEST:
        offset.y = SQUARE_SIZE/2;
        break;
      case NORTH_WEST:
        break;

    }
    return offset;
  }

}