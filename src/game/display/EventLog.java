package game.display;

import game.Game;
import utils.Dimension;
import utils.Utils;
import world.Coordinate;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class EventLog {

  public static final double TOP_OR_BOTTOM_SWAP_LINE = 0.75;


  // VALUES IN MILLISECONDS
  public static final long EVENT_LIFESPAN = 5000;

  // VALUES IN PIXELS
  public static final int LINE_HEIGHT = 15;
  public static final int LINE_SPACER = LINE_HEIGHT/3;

  // VALUES IN LINES
  public static final int MINIMUM_HEIGHT = 5;
  public static final int MAXIMUM_HEIGHT = 10;


  public static final Font SMALL_TEXT = new Font("Monospaced", Font.PLAIN, LINE_HEIGHT);




  private static int liveEventsIndex = 0;
  private static final List<Event> events = new ArrayList<>();



  private static int getLinesTall() {
    return Utils.clamp(events.size() - liveEventsIndex,MINIMUM_HEIGHT,MAXIMUM_HEIGHT);
  }



  public static void registerEvent(Color color, String message) {
    events.add(new Event(color,message));
  }



  public static void drawOverlay(Graphics2D g2d) {

    g2d.setFont(SMALL_TEXT);


    // check for expired events and push live events index forward accordingly
    boolean allDead = true;
    for (int i = liveEventsIndex; i < events.size(); i++) {
      if (System.currentTimeMillis() - events.get(i).timePosted < EVENT_LIFESPAN) {
        liveEventsIndex = i;
        allDead = false;
        break;
      }
    }

    if (allDead) {
      return; // draw nothing if there's no messages.
    }


    Coordinate playerAt = Game.getActivePlayer().getActor().getCoordinate();

    Dimension areaSizeInSquares = Game.getActiveWorld().getAreaSizeInSquares();

    final int areaHeight = areaSizeInSquares.getHeight();


    boolean drawingTop = (playerAt.localY > areaHeight * TOP_OR_BOTTOM_SWAP_LINE);

    int linesTall = getLinesTall();

    int drawWidth = (int) (LINE_HEIGHT*areaSizeInSquares.getWidth()*0.65);
    int drawHeight = LINE_HEIGHT * linesTall + LINE_SPACER;
    int drawX = areaSizeInSquares.getWidth()/2*LINE_HEIGHT - drawWidth/2;
    int drawY;

    if (drawingTop) {
      drawY = LINE_SPACER;
    }
    else {
      drawY = GameDisplay.SQUARE_SIZE * areaHeight - drawHeight;
    }

    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
    g2d.setColor(Color.BLACK);
    g2d.fillRect(drawX, drawY, drawWidth, drawHeight);
    g2d.setColor(Color.DARK_GRAY);
    g2d.drawRect(drawX,drawY,drawWidth,drawHeight);


    // draw live event messages, bottom-up, for as long as there's room

    for (int i = 0; i < events.size()-liveEventsIndex && i < linesTall; i++) {

      Event event = events.get(events.size()-1-i);

      g2d.setColor(event.color);
      g2d.drawString(event.message,drawX + 10,drawY+drawHeight-LINE_HEIGHT*i-LINE_SPACER);

    }

  }

  public static void registerEventIfPlayerIsNear(Coordinate nearTo, Color color, String message) {
    if (Game.getActivePlayer().getActor().getCoordinate().area == nearTo.area) {
      registerEvent(color, message);
    }
  }

}
