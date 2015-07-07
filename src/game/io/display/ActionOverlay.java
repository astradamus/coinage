package game.io.display;

import actor.Actor;
import game.Direction;
import game.Game;
import game.TimeMode;
import game.io.GameEngine;
import game.physical.Appearance;
import world.AreaCoordinate;
import world.Coordinate;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Set;

/**
 *
 */
class ActionOverlay {

  private static final int SQUARE_SIZE = GameDisplay.SQUARE_SIZE;
  private static final Font ACTION_OVERLAY_FONT =
      new Font("Monospaced", Font.BOLD, SQUARE_SIZE * 5 / 7);


  public static void drawOverlay(Graphics2D g) {

    final Game runningGame = GameDisplay.getRunningGame();
    final Actor playerActor = runningGame.getActivePlayerActor();

    // Draw an overlay on actors indicating the direction they are facing.
    Set<Actor> localActors = runningGame.getGameControllers()
        .getActorsInArea(runningGame.getWorld().getArea(playerActor.getCoordinate()));

    localActors.add(playerActor);

    g.setFont(ACTION_OVERLAY_FONT);
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
    for (Actor actor : localActors) {
      if (GameEngine.getTimeMode() == TimeMode.PRECISION
          || GameEngine.getTimeMode() == TimeMode.PAUSED) {
        drawActionIndicator(g, actor);
      }
    }
    for (Actor actor : localActors) {
      drawFacingOverlay(g, actor, 6);
    }
  }


  private static void drawFacingOverlay(Graphics g, Actor actor, int thickness) {

    Direction facing = actor.getFacing();

    g.setColor(actor.getColor());

    AreaCoordinate actorAt =
        GameDisplay.getRunningGame().getWorld().convertToAreaCoordinate(actor.getCoordinate());

    int[] drawX = new int[6];
    int[] drawY = new int[6];

    int originX = actorAt.areaX * SQUARE_SIZE;
    int originY = actorAt.areaY * SQUARE_SIZE;

    Point[] points = new Point[] {
        getOffsetFor(facing.getLeftNeighbor(), 0), getOffsetFor(facing, 0),
        getOffsetFor(facing.getRightNeighbor(), 0),
        getOffsetFor(facing.getRightNeighbor(), thickness), getOffsetFor(facing, thickness),
        getOffsetFor(facing.getLeftNeighbor(), thickness)
    };

    for (int i2 = 0; i2 < points.length; i2++) {
      Point point = points[i2];
      drawX[i2] = originX + point.x;
      drawY[i2] = originY + point.y;
    }

    g.fillPolygon(drawX, drawY, 6);
  }


  private static void drawActionIndicator(Graphics2D g, Actor actor) {

    Color color = actor.getActionIndicatorColor();
    final Coordinate actionTarget = actor.getActionTarget();
    if (actionTarget != null) {

      final Game runningGame = GameDisplay.getRunningGame();

      AreaCoordinate actionTargetAC = runningGame.getWorld().convertToAreaCoordinate(actionTarget);

      int actionDelay = actor.getTotalActionDelay() + 1;

      int drawX = actionTargetAC.areaX * SQUARE_SIZE;
      int drawY = actionTargetAC.areaY * SQUARE_SIZE;

      char character = '+';

      if (runningGame.getPlayerAgent().getActor().isFreeToAct()) {
        if (actionDelay < 10) {
          character = Integer.toString(actionDelay).charAt(0);
        }
      }

      Appearance appearance = new Appearance(character, color, color);

      SquareDrawer.drawOval(g, appearance, SQUARE_SIZE, drawX, drawY);
    }
  }


  private static Point getOffsetFor(Direction direction, int gap) {

    Point offset = new Point(-gap, -gap);

    switch (direction) {

      case NORTH:
        offset.x = SQUARE_SIZE / 2;
        break;
      case NORTH_EAST:
        offset.x = SQUARE_SIZE + gap;
        break;
      case EAST:
        offset.x = SQUARE_SIZE + gap;
        offset.y = SQUARE_SIZE / 2;
        break;
      case SOUTH_EAST:
        offset.x = SQUARE_SIZE + gap;
        offset.y = SQUARE_SIZE + gap;
        break;
      case SOUTH:
        offset.x = SQUARE_SIZE / 2;
        offset.y = SQUARE_SIZE + gap;
        break;
      case SOUTH_WEST:
        offset.y = SQUARE_SIZE + gap;
        break;
      case WEST:
        offset.y = SQUARE_SIZE / 2;
        break;
      case NORTH_WEST:
        break;
    }
    return offset;
  }
}