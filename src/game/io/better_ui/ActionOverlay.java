package game.io.better_ui;

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
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Set;

/**
 *
 */
class ActionOverlay {

  public static void drawOverlay(Graphics2D g, int tileSize, Game game) {

    final Actor playerActor = game.getActivePlayerActor();

    // Draw an overlay on actors indicating the direction they are facing.
    Set<Actor> localActors = game.getGameControllers()
        .getActorsInArea(game.getWorld().getArea(playerActor.getCoordinate()));

    localActors.add(playerActor);

    Composite initialComposite = g.getComposite();

    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
    for (Actor actor : localActors) {
      if (GameEngine.getTimeMode() == TimeMode.PRECISION
          || GameEngine.getTimeMode() == TimeMode.PAUSED) {
        drawActionIndicator(g, tileSize, game, actor);
      }
    }
    for (Actor actor : localActors) {
      drawFacingOverlay(g, tileSize, game, actor, 6);
    }

    // Restore the initial alpha composite.
    g.setComposite(initialComposite);
  }


  private static void drawFacingOverlay(Graphics g, int tileSize, Game game, Actor actor,
      int thickness) {

    Direction facing = actor.getFacing();

    g.setColor(actor.getColor());

    AreaCoordinate actorAt = game.getWorld().convertToAreaCoordinate(actor.getCoordinate());

    int[] drawX = new int[6];
    int[] drawY = new int[6];

    int originX = actorAt.areaX * tileSize;
    int originY = actorAt.areaY * tileSize;

    Point[] points = new Point[] {
        getOffsetFor(tileSize, facing.getLeftNeighbor(), 0), getOffsetFor(tileSize, facing, 0),
        getOffsetFor(tileSize, facing.getRightNeighbor(), 0),
        getOffsetFor(tileSize, facing.getRightNeighbor(), thickness), getOffsetFor(tileSize, facing, thickness),
        getOffsetFor(tileSize, facing.getLeftNeighbor(), thickness)
    };

    for (int i2 = 0; i2 < points.length; i2++) {
      Point point = points[i2];
      drawX[i2] = originX + point.x;
      drawY[i2] = originY + point.y;
    }

    g.fillPolygon(drawX, drawY, 6);
  }


  private static void drawActionIndicator(Graphics2D g, int tileSize, Game game, Actor actor) {

    Color color = actor.getActionIndicatorColor();
    final Coordinate actionTarget = actor.getActionTarget();
    if (actionTarget != null) {

      AreaCoordinate actionTargetAC = game.getWorld().convertToAreaCoordinate(actionTarget);

      int actionDelay = actor.getTotalActionDelay() + 1;

      int drawX = actionTargetAC.areaX * tileSize;
      int drawY = actionTargetAC.areaY * tileSize;

      char character = '+';

      if (game.getPlayerAgent().getActor().isFreeToAct()) {
        if (actionDelay < 10) {
          character = Integer.toString(actionDelay).charAt(0);
        }
      }

      Appearance appearance = new Appearance(character, color, color);

      SquareDrawer.drawOval(g, appearance, tileSize, drawX, drawY);
    }
  }


  private static Point getOffsetFor(int tileSize, Direction direction, int gap) {

    Point offset = new Point(-gap, -gap);

    switch (direction) {

      case NORTH:
        offset.x = tileSize / 2;
        break;
      case NORTH_EAST:
        offset.x = tileSize + gap;
        break;
      case EAST:
        offset.x = tileSize + gap;
        offset.y = tileSize / 2;
        break;
      case SOUTH_EAST:
        offset.x = tileSize + gap;
        offset.y = tileSize + gap;
        break;
      case SOUTH:
        offset.x = tileSize / 2;
        offset.y = tileSize + gap;
        break;
      case SOUTH_WEST:
        offset.y = tileSize + gap;
        break;
      case WEST:
        offset.y = tileSize / 2;
        break;
      case NORTH_WEST:
        break;
    }
    return offset;
  }
}