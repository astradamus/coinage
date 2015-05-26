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
    setFont(new Font("Monospace", Font.BOLD, TILE_SIZE));
  }

  public static final int TILE_SIZE = GameDisplay.TILE_SIZE*2;
  public static final int TILES_WIDE = 13;
  public static final int MAP_FRAME_RADIUS = 5;

  @Override
  public void paint(Graphics g) {
    super.paint(g);

    World world = Game.getActive().WORLD;
    PlayerController pC = Game.getActive().CONTROLLERS.getPlayerController();

    int playerX = pC.getX() / world.areaWidth;
    int playerY = pC.getY() / world.areaHeight;

//    int centerMapOnX = playerX;
//    int centerMapOnY = playerY;

    // stop scrolling world map before any void would show

//    int leftCameraWall = MAP_FRAME_RADIUS+1;
//    int rightCameraWall = world.worldAreasWide - MAP_FRAME_RADIUS-1;
//    int topCameraWall = MAP_FRAME_RADIUS+1;
//    int bottomCameraWall = world.worldAreasTall - MAP_FRAME_RADIUS-1;
//
//    if (playerX < leftCameraWall) {
//      centerMapOnX = leftCameraWall;
//    } else if (playerX > rightCameraWall) {
//      centerMapOnX = rightCameraWall;
//    }
//    if (playerY < topCameraWall) {
//      centerMapOnY = topCameraWall;
//    } else if (playerY > bottomCameraWall) {
//      centerMapOnY = bottomCameraWall;
//    }



    Area playerAt = world.getAreaAt(playerX, playerY);

    int size = (MAP_FRAME_RADIUS * 2 + 1) * TILE_SIZE;
    g.drawRect(24,63, size+1, size+1);

    for (int y = 0; y < MAP_FRAME_RADIUS*2+1; y++) {
      for (int x = 0; x < MAP_FRAME_RADIUS*2+1; x++) {


        int worldX = playerX + x - MAP_FRAME_RADIUS;
        int worldY = playerY + y - MAP_FRAME_RADIUS;


        Area thisArea = world.getAreaAt(worldX, worldY);
        if (thisArea == null) {
          continue;
        }
        Biome biome = thisArea.getBiome();

        int placeX = (x + 1) * TILE_SIZE;
        int placeY = (y + 2 + 1) * TILE_SIZE;


        Color bgColor = biome.worldMapBGColor;
        if (bgColor != null) {
          g.setColor(bgColor);
          g.fillRect((placeX - TILE_SIZE / 6), (int) (placeY - TILE_SIZE * 0.85), TILE_SIZE,
              TILE_SIZE);
        }

        Color color = biome.worldMapColor;
        g.setColor(color);
        g.drawChars(new char[]{biome.worldMapChar}, 0, 1, placeX, placeY);

        if (playerAt == thisArea) {
          g.setColor(Color.WHITE);
          g.drawOval((placeX - TILE_SIZE / 6), (int) (placeY - TILE_SIZE * 0.85), TILE_SIZE,
              TILE_SIZE);
          g.drawChars(new char[]{'X'}, 0, 1, placeX, placeY);
        }

      }
    }
  }

}
