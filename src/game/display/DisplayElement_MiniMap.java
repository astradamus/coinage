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
    return FULL_SIZE_IN_PIXELS + SQUARE_SIZE;
  }


  @Override
  public void drawTo(Graphics g, int originX, int originY, int width) {

    Coordinate playerAt = Game.getActivePlayer().getActor().getCoordinate();

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
        int placeY = originY + BORDER_THICKNESS + (y * SQUARE_SIZE);

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

    // Draw map outline and clock text (if applicable) in the appropriate color for the time mode.
    g.setColor(Game.getTimeMode().getIndicatorColor());
    g.drawRect(originX, originY, FULL_SIZE_IN_PIXELS, FULL_SIZE_IN_PIXELS);

    String gameClockLabel = Game.getTimeMode().getGameClockLabel();
    if (gameClockLabel != null) {
      g.setFont(SidePanel.CONTROLS_FONT);
      int x = originX + FULL_SIZE_IN_PIXELS / 2 - g.getFontMetrics().stringWidth(gameClockLabel)/2;
      int y = getHeight() + SQUARE_SIZE/2;
      g.drawString(gameClockLabel, x, y);
    }

  }

}
