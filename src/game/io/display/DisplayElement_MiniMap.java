package game.io.display;

import game.TimeMode;
import game.io.GameEngine;
import game.physical.Appearance;
import utils.Dimension;
import world.Area;
import world.Coordinate;
import world.MapCoordinate;
import world.World;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 */
public class DisplayElement_MiniMap implements DisplayElement {

    private static final int SQUARE_SIZE = 30;

    private static final Appearance MAP_UNEXPLORED_SQUARE =
            new Appearance('?', new Color(25, 25, 25), new Color(11, 11, 11));

    private static final int BORDER_THICKNESS = 1;

    private static final int MAP_RADIUS_SQUARES = 5;
    private static final int SIZE_IN_SQUARES = MAP_RADIUS_SQUARES * 2 + 1;
    private static final int SIZE_IN_PIXELS = SIZE_IN_SQUARES * SQUARE_SIZE;

    private static final int FULL_SIZE_IN_PIXELS = SIZE_IN_PIXELS + BORDER_THICKNESS;


    @Override
    public int getHeight() {
        return FULL_SIZE_IN_PIXELS + SQUARE_SIZE;
    }


    @Override
    public void drawTo(Graphics g, int originX, int originY, int width) {

        final World world = GameDisplay.getRunningGame().getWorld();
        Coordinate playerAt = GameDisplay.getRunningGame().getActivePlayerActor().getCoordinate();

        // draw the zone of Areas surrounding the player's current area, using a blank 'unknown'
        //   token for Areas that have not yet been explored by the player, and skipping any null Areas
        //   (indexes outside of the World map), so that they are left blank.
        for (int y = 0; y < SIZE_IN_SQUARES; y++) {
            for (int x = 0; x < SIZE_IN_SQUARES; x++) {

                final MapCoordinate thisCoordinate = world.convertToMapCoordinate(playerAt)
                        .offset(x - MAP_RADIUS_SQUARES, y - MAP_RADIUS_SQUARES);
                final Area area = world.getArea(thisCoordinate);

                if (area == null) {
                    continue; // skip null areas
                }

                int placeX = originX + BORDER_THICKNESS + (x * SQUARE_SIZE);
                int placeY = originY + BORDER_THICKNESS + (y * SQUARE_SIZE);

                Appearance appearance;

                boolean areaIsRevealed =
                        GameDisplay.getRunningGame().getWorldMapAreaIsRevealed(thisCoordinate);

                if (areaIsRevealed) {
                    appearance = area.getBiome().worldMapAppearance;
                }
                else {
                    appearance = MAP_UNEXPLORED_SQUARE;
                }

                // draw the square
                SquareDrawer.drawSquare(g, appearance, SQUARE_SIZE, placeX, placeY);

                // draw a selection symbol over the area occupied by the player
                if (world.getArea(playerAt) == area) {
                    SquareDrawer.drawOval(g, GameDisplay.CURSOR, SQUARE_SIZE, placeX, placeY);
                }
            }
        }

        // Draw map outline and clock text (if applicable) in the appropriate color for the time mode.
        final TimeMode timeMode = GameEngine.getTimeMode();
        g.setColor(timeMode.getIndicatorColor());
        g.drawRect(originX, originY, FULL_SIZE_IN_PIXELS, FULL_SIZE_IN_PIXELS);

        String gameClockLabel = timeMode.getGameClockLabel();
        if (gameClockLabel != null) {
            g.setFont(SidePanel.CONTROLS_FONT);
            int x =
                    originX + FULL_SIZE_IN_PIXELS / 2 - g.getFontMetrics().stringWidth(gameClockLabel) / 2;
            int y = getHeight() + SQUARE_SIZE / 2;
            g.drawString(gameClockLabel, x, y);
        }
    }
}