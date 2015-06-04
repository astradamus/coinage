package game.display;

import game.Game;
import game.input.InputMode;
import game.Physical;
import world.Coordinate;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class SidePanel extends JPanel {


  {
    setBackground(Color.BLACK);
    setFont(MAP_FONT);
  }


  public static final int SP_SQUARE_SIZE = 30;
  public static final int SP_TEXT_SIZE = SP_SQUARE_SIZE*3/7;

  public static final int SP_SQUARES_WIDE = 13;
  public static final int SP_BORDER_SQUARES_X = 1;
  public static final int SP_BORDER_SQUARES_Y = 2;
  public static final int SP_LEFT_EDGE = SP_BORDER_SQUARES_X*SP_SQUARE_SIZE;

  public static final Font MAP_FONT = new Font("SansSerif", Font.BOLD, SP_SQUARE_SIZE);
  public static final Font LARGE_TEXT = new Font("Serif", Font.BOLD, SP_SQUARE_SIZE);
  public static final Font SMALL_TEXT = new Font("Monospaced", Font.PLAIN, SP_TEXT_SIZE);

  public static final Appearance MAP_UNEXPLORED_SQUARE =
      new Appearance('?',new Color(25,25,25),new Color(11,11,11));

  public static final int MAP_RADIUS_SQUARES = 5;
  public static final int MAP_SIZE_SQUARES = MAP_RADIUS_SQUARES * 2 + 1;
  public static final int MAP_SIZE_PIXELS = MAP_SIZE_SQUARES * SP_SQUARE_SIZE;



  @Override
  public void paint(Graphics g) {
    super.paint(g);

    // draw the world map
    drawWorldMap(g);

    // switch out of MAP_FONT
    g.setFont(SMALL_TEXT);


    // how far down do we start? (under world map)
    int pixelsDown = (MAP_SIZE_SQUARES+SP_BORDER_SQUARES_Y)*SP_SQUARE_SIZE;

    // if we're in look mode, draw the look list for the selected square
    InputMode inputMode = Game.getActiveInputSwitch().getInputMode();

    drawPanelText(g, pixelsDown,
        inputMode.sidePanelControlTexts,
        inputMode.sidePanelHeaderTexts,
        inputMode.getSidePanelPhysicals());

  }

  private int drawPanelText(Graphics g, int pixelsDown, List<String> controls,
                            List<String> headers, List<Physical> physicals) {

    if (controls != null) {
      for (int i = 0; i < controls.size(); i++) {
        String control = controls.get(i);

        // if printing multiple control texts, pull the target up to account for smaller font
        if (i > 0) {
          pixelsDown -= SP_TEXT_SIZE;
        }

        pixelsDown += drawControl(g, pixelsDown, control);

      }
    }

    g.setFont(LARGE_TEXT);

    if (headers != null) {
      for (String header : headers) {
        pixelsDown += drawHeader(g, pixelsDown, header);
      }
    }

    if (physicals != null) {
      drawPhysicalsList(g, pixelsDown, physicals);
    }

    return pixelsDown;

  }

  private int drawControl(Graphics g, int pixelsDown, String control) {

    SquareDrawer.drawString(g,
        control,
        new Color(93, 93, 93),
        SP_LEFT_EDGE, pixelsDown);

    return SP_SQUARE_SIZE;

  }

  private int drawHeader(Graphics g, int pixelsDown, String header) {

    SquareDrawer.drawString(g, header,
        new Color(150, 119, 0),
        SP_LEFT_EDGE, pixelsDown);

    return SP_SQUARE_SIZE;

  }

  private int drawPhysicalsList(Graphics g, int pixelsDown, List<Physical> physicals) {

    int linesLong = physicals.size();

    for (int i = 0; i < linesLong; i++) {
      int adjustedY = pixelsDown + i * SP_SQUARE_SIZE;

      Physical physical = physicals.get(i);
      g.setColor(physical.getAppearance().getColor());
      g.drawString(physical.getName(), SP_LEFT_EDGE+SP_SQUARE_SIZE, adjustedY);

      // Draw a marker beside the selected physical in the list.
      Integer listSelectIndex = Game.getActiveInputSwitch().getListSelectIndex();
      if (listSelectIndex != null && listSelectIndex == i) {
        int markerWidth = SP_SQUARE_SIZE / 2;
        g.fillOval(SP_LEFT_EDGE,adjustedY-SP_TEXT_SIZE, markerWidth, markerWidth/2);
      }

    }

    return linesLong * SP_SQUARE_SIZE;

  }

  private void drawWorldMap(Graphics g) {

    Coordinate playerAt = Game.getActivePlayer().getCoordinate();

    // draw world map outline
    g.drawRect(29, 33, MAP_SIZE_PIXELS+1, MAP_SIZE_PIXELS+1);

    // draw the zone of Areas surrounding the player's current area, using a blank 'unknown'
    //   token for Areas that have not yet been explored by the player, and skipping any null Areas
    //   (indexes outside of the World map), so that they are left blank.
    for (int y = 0; y < MAP_SIZE_SQUARES; y++) {
      for (int x = 0; x < MAP_SIZE_SQUARES; x++) {

        Coordinate thisCoordinate = Game.getActiveWorld().offsetCoordinateByAreas
            (playerAt, x - MAP_RADIUS_SQUARES, y - MAP_RADIUS_SQUARES);

        if (thisCoordinate == null) {
          continue; // skip null areas
        }

        int placeX = (x + SP_BORDER_SQUARES_X) * SP_SQUARE_SIZE;
        int placeY = (y + SP_BORDER_SQUARES_Y) * SP_SQUARE_SIZE;



        Appearance appearance;

        if (Game.getActivePlayer().getWorldMapRevealedComponent().getAreaIsRevealed
            (thisCoordinate)) {

          // if this square is revealed, use the biome's worldMapAppearance...
          appearance = thisCoordinate.area.getBiome().worldMapAppearance;

        } else {

          // ...otherwise, use a generic 'unexplored' appearance
          appearance = MAP_UNEXPLORED_SQUARE;

        }

        // draw the square
        SquareDrawer.drawSquare(g, appearance, SP_SQUARE_SIZE, placeX, placeY);

        // draw a selection symbol over the area occupied by the player
        if (Game.getActivePlayer().getCoordinate().area == thisCoordinate.area) {
          SquareDrawer.drawOval(g, GameDisplay.CURSOR, SP_SQUARE_SIZE, placeX, placeY);
        }

      }
    }
  }

}
