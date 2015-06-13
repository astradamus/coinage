package game.display;

import game.Game;
import world.Coordinate;

import java.awt.*;

/**
 *
 */
public class DisplayElement_MiniMap implements DisplayElement {

  public static final int SQUARE_SIZE = 30;

  public static final Appearance MAP_UNEXPLORED_SQUARE =
      new Appearance('?',new Color(25,25,25),new Color(11,11,11));

  public static final int BORDER_THICKNESS = 1;

  public static final int MAP_RADIUS_SQUARES = 5;
  public static final int SIZE_IN_SQUARES = MAP_RADIUS_SQUARES * 2 + 1;
  public static final int SIZE_IN_PIXELS = SIZE_IN_SQUARES * SQUARE_SIZE;

  public static final int FULL_SIZE_IN_PIXELS = SIZE_IN_PIXELS + BORDER_THICKNESS;


  @Override
  public int getHeight() {
    return FULL_SIZE_IN_PIXELS + SQUARE_SIZE; // Adds a one-square bottom border.
  }


  @Override
  public void drawTo(Graphics g, int originX, int originY, int width) {

    Coordinate playerAt = Game.getActivePlayer().getActor().getCoordinate();

    // draw world map outline
    g.drawRect(originX, originY+4, FULL_SIZE_IN_PIXELS, FULL_SIZE_IN_PIXELS);

    // draw the zone of Areas surrounding the player's current area, using a blank 'unknown'
    //   token for Areas that have not yet been explored by the player, and skipping any null Areas
    //   (indexes outside of the World map), so that they are left blank.
    for (int y = 0; y < SIZE_IN_SQUARES; y++) {
      for (int x = 0; x < SIZE_IN_SQUARES; x++) {

        Coordinate thisCoordinate = Game.getActiveWorld().offsetCoordinateByAreas
            (playerAt, x - MAP_RADIUS_SQUARES, y - MAP_RADIUS_SQUARES);

        if (thisCoordinate == null) {
          continue; // skip null areas
        }

        int placeX = originX + BORDER_THICKNESS + (x * SQUARE_SIZE);
        int placeY = originY + BORDER_THICKNESS + ((y+1) * SQUARE_SIZE);

        Appearance appearance;


        boolean areaIsRevealed =
            Game.getActivePlayer().getWorldMapRevealedComponent().getAreaIsRevealed(thisCoordinate);

        if (areaIsRevealed) {
          appearance = thisCoordinate.area.getBiome().worldMapAppearance;
        } else {
          appearance = MAP_UNEXPLORED_SQUARE;
        }

        // draw the square
        SquareDrawer.drawSquare(g, appearance, SQUARE_SIZE, placeX, placeY);

        // draw a selection symbol over the area occupied by the player
        if (playerAt.area == thisCoordinate.area) {
          SquareDrawer.drawOval(g, GameDisplay.CURSOR, SQUARE_SIZE, placeX, placeY);
        }

      }
    }


  }

}