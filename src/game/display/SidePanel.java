package game.display;

import controller.player.PlayerController;
import game.Game;
import game.input.InputMode;
import game.Physical;
import world.Area;
import world.Biome;
import world.World;

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
  public static final int SP_TEXT_SIZE = SP_SQUARE_SIZE*2/5;

  public static final int SP_SQUARES_WIDE = 13;
  public static final int SP_BORDER_SQUARES_X = 1;
  public static final int SP_BORDER_SQUARES_Y = 2;

  public static final Font MAP_FONT = new Font("Monospace", Font.BOLD, SP_SQUARE_SIZE);
  public static final Font LARGE_TEXT = new Font("Serif", Font.BOLD, SP_SQUARE_SIZE);
  public static final Font SMALL_TEXT = new Font("Monospace", Font.PLAIN, SP_TEXT_SIZE);

  public static final Appearance MAP_UNEXPLORED_SQUARE =
      new Appearance('?',new Color(25,25,25),new Color(11,11,11));

  public static final int MAP_RADIUS_SQUARES = 5;
  public static final int MAP_SIZE_SQUARES = MAP_RADIUS_SQUARES * 2 + 1;
  public static final int MAP_SIZE_PIXELS = MAP_SIZE_SQUARES * SP_SQUARE_SIZE;

  private static final int UNDERMAP_START_X = SP_SQUARE_SIZE;
  private static final int UNDERMAP_START_Y =
      (SP_BORDER_SQUARES_Y + 1) * SP_SQUARE_SIZE + MAP_SIZE_PIXELS;


  @Override
  public void paint(Graphics g) {
    super.paint(g);

    World world = Game.getActive().WORLD;
    PlayerController playerController = Game.getActive().CONTROLLERS.getPlayerController();

    Point playerWorldXY = world.getWorldCoordinateFromGlobalCoordinate(
                            playerController.getGlobalCoordinate());

    Area playerAt = world.getAreaByWorldCoordinate(playerWorldXY.x, playerWorldXY.y);


    // draw the world map
    drawWorldMap(g, world, playerController, playerWorldXY, playerAt);

    // switch out of MAP_FONT
    g.setFont(SMALL_TEXT);

    // if we're in look mode, draw the look list for the selected square
    InputMode inputMode = Game.getActive().INPUT_SWITCH.getInputMode();
    if (inputMode == InputMode.LOOK) {
      drawLookList(g, playerAt);
    } else if(inputMode == InputMode.INVENTORY) {
      drawInventoryList(g);
    } else {
      SquareDrawer.drawStringList(g, OPTIONS, new Color(93, 93, 93), SP_TEXT_SIZE, UNDERMAP_START_X,
          UNDERMAP_START_Y);
    }

  }

  public static final List<String> OPTIONS = Arrays.asList(
      "L: Look Around.",
      "I: Inventory."
  );


  private void drawLookList(Graphics g, Area playerAt) {

    SquareDrawer.drawString(g,"(press ESC to resume)", new Color(93, 93, 93),UNDERMAP_START_X,
        UNDERMAP_START_Y);
    g.setFont(LARGE_TEXT);
    SquareDrawer.drawString(g,"YOU SEE:", new Color(150, 119, 0),UNDERMAP_START_X,
        UNDERMAP_START_Y+SP_SQUARE_SIZE);

    // determine where the player is 'looking'
    Point cursorLocalTarget = Game.getActive().INPUT_SWITCH.getCursorTarget();

    // determine what's there
    List<Physical> allPhysicalsAt = playerAt.getPhysicalsComponent().getAllPhysicalsAt(cursorLocalTarget);

    // draw what we've found as a list under the world map
    SquareDrawer.drawPhysicalsList(g, allPhysicalsAt, SP_SQUARE_SIZE, UNDERMAP_START_X+SP_SQUARE_SIZE,
        UNDERMAP_START_Y+SP_SQUARE_SIZE*2);

  }

  private void drawInventoryList(Graphics g) {

    SquareDrawer.drawString(g,"(press ESC to resume)", new Color(93, 93, 93),UNDERMAP_START_X,
        UNDERMAP_START_Y);
    g.setFont(LARGE_TEXT);
    SquareDrawer.drawString(g,"YOU ARE CARRYING:", new Color(150, 119, 0),UNDERMAP_START_X,
        UNDERMAP_START_Y+SP_SQUARE_SIZE);

    // determine what's there
    List<Physical> heldItems = Game.getActive().CONTROLLERS
        .getPlayerController().getActor().getInventory().getItemsHeld();

    // draw what we've found as a list under the world map
    SquareDrawer.drawPhysicalsList(g, heldItems, SP_SQUARE_SIZE, UNDERMAP_START_X+SP_SQUARE_SIZE,
        UNDERMAP_START_Y+SP_SQUARE_SIZE*2);

  }

  private void drawWorldMap(Graphics g, World world, PlayerController pC, Point playerWorldXY,
                            Area playerAt) {


    // draw world map outline
    g.drawRect(29, 33, MAP_SIZE_PIXELS+1, MAP_SIZE_PIXELS+1);

    // draw the zone of Areas surrounding the player's current area, using a blank 'unknown'
    //   token for Areas that have not yet been explored by the player, and skipping any null Areas
    //   (indexes outside of the World map), so that they are left blank.
    for (int y = 0; y < MAP_SIZE_SQUARES; y++) {
      for (int x = 0; x < MAP_SIZE_SQUARES; x++) {

        int worldX = playerWorldXY.x + x - MAP_RADIUS_SQUARES;
        int worldY = playerWorldXY.y + y - MAP_RADIUS_SQUARES;

        Area thisArea = world.getAreaByWorldCoordinate(worldX, worldY);
        if (thisArea == null) {
          continue; // skip null areas
        }

        int placeX = (x + SP_BORDER_SQUARES_X) * SP_SQUARE_SIZE;
        int placeY = (y + SP_BORDER_SQUARES_Y) * SP_SQUARE_SIZE;



        Appearance appearance;

        if (pC.getWorldMapRevealedComponenet().getAreaIsRevealed(worldX,worldY)) {

          // if this square is revealed, use the biome's worldMapAppearance...
          appearance = thisArea.getBiome().worldMapAppearance;

        } else {

          // ...otherwise, use a generic 'unexplored' appearance
          appearance = MAP_UNEXPLORED_SQUARE;

        }

        // draw the square
        SquareDrawer.drawSquare(g, appearance, SP_SQUARE_SIZE, placeX, placeY);

        // draw a selection symbol over the area occupied by the player
        if (playerAt == thisArea) {
          SquareDrawer.drawOval(g, GameDisplay.CURSOR, SP_SQUARE_SIZE, placeX, placeY);
        }

      }
    }
  }

}
