package game.display;

import controller.PlayerController;
import game.Game;
import world.Area;
import world.Biome;
import world.World;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class SidePanel extends JPanel {

  {
    setBackground(Color.BLACK);
    setFont(new Font("Monospace", Font.BOLD, SQUARE_SIZE));
  }

  public static final int SQUARE_SIZE = GameDisplay.TILE_SIZE*2;
  public static final int TILES_WIDE = 13;
  public static final int MAP_FRAME_RADIUS = 5;

  @Override
  public void paint(Graphics g) {
    super.paint(g);

    World world = Game.getActive().WORLD;
    PlayerController pC = Game.getActive().CONTROLLERS.getPlayerController();

    Point playerWorldXY = world.getWorldCoordinateFromGlobalCoordinate(pC.getGlobalCoordinate());

    Area playerAt = world.getAreaByWorldCoordinate(playerWorldXY.x, playerWorldXY.y);

    int worldMapSizeInSquares = MAP_FRAME_RADIUS * 2 + 1;
    int worldMapSizeInPixels = (worldMapSizeInSquares) * SQUARE_SIZE;

    // draw world map outline
    g.drawRect(24,63, worldMapSizeInPixels+1, worldMapSizeInPixels+1);

    // draw the zone of Areas surrounding the player's current area, using a blank 'unknown'
    //   token for Areas that have not yet been explored by the player, and skipping any null Areas
    //   (indexes outside of the World map).
    for (int y = 0; y < worldMapSizeInSquares; y++) {
      for (int x = 0; x < worldMapSizeInSquares; x++) {

        int worldX = playerWorldXY.x + x - MAP_FRAME_RADIUS;
        int worldY = playerWorldXY.y + y - MAP_FRAME_RADIUS;

        Area thisArea = world.getAreaByWorldCoordinate(worldX, worldY);
        if (thisArea == null) {
          continue; // skip null areas
        }

        int placeX = (x + 1) * SQUARE_SIZE;
        int placeY = (y + 2 + 1) * SQUARE_SIZE;


        Biome biome = thisArea.getBiome();

        Color mapBGColor;
        Color mapColor;
        char mapChar;

        if (pC.getWorldMapRevealedComponenet().getAreaIsRevealed(worldX,worldY)) {
          mapBGColor = biome.worldMapBGColor;
          mapColor = biome.worldMapColor;
          mapChar = biome.worldMapChar;
        } else {
          mapBGColor = new Color(11,11,11);
          mapColor = new Color(25,25,25);
          mapChar = '?';
        }

        if (mapBGColor != null) {
          g.setColor(mapBGColor);
          g.fillRect((placeX - SQUARE_SIZE / 6), (int) (placeY - SQUARE_SIZE * 0.85), SQUARE_SIZE,
              SQUARE_SIZE);
        }

        g.setColor(mapColor);
        g.drawChars(new char[]{mapChar}, 0, 1, placeX, placeY);

        if (playerAt == thisArea) {
          g.setColor(Color.WHITE);
          g.drawOval((placeX - SQUARE_SIZE / 6), (int) (placeY - SQUARE_SIZE * 0.85), SQUARE_SIZE,
              SQUARE_SIZE);
          g.drawChars(new char[]{'X'}, 0, 1, placeX, placeY);
        }

      }
    }
  }

}
