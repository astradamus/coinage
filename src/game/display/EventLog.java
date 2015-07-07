package game.display;

import game.Game;
import utils.Dimension;
import utils.Utils;
import world.Coordinate;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
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
  public static final int LINE_HEIGHT = GameDisplay.SQUARE_SIZE;
  public static final int LINE_SPACER = LINE_HEIGHT/3;

  // VALUES IN LINES
  public static final int MINIMUM_HEIGHT = 5;
  public static final int MAXIMUM_HEIGHT = 10;
  public static final int MAX_LOG_LINES = 25;
  public static final int MIN_LOG_LINES = 5;
  public static final Font SMALL_TEXT = new Font("Monospaced", Font.PLAIN, LINE_HEIGHT);
  private static int startingEvent = 1;

  private static int expandedMode = 0;  // 0 = off, 1 = Max size paused, 2 = Min size running
  private static int liveEventsIndex = 0;
  private static final List<Event> events = new ArrayList<>();

  public static void scrollLogDown() {
    startingEvent--;
    if (startingEvent < 1) {
      startingEvent = 1;
    }
  }

  public static void scrollLogUp() {
    startingEvent++;
    if (startingEvent > events.size()) {
      startingEvent = events.size();
    }
  }
  private static int getLinesTall() {
    return Utils.clamp(events.size() - liveEventsIndex,MINIMUM_HEIGHT,MAXIMUM_HEIGHT);
  }

  private static boolean findLiveEvents() {
    // Search for live events and push live events index forward accordingly
    boolean anyLive = false;
    for (int i = liveEventsIndex; i < events.size(); i++) {
      if (System.currentTimeMillis() - events.get(i).timePosted < EVENT_LIFESPAN) {
        liveEventsIndex = i;
        anyLive = true;
        break;
      }
    }
    return anyLive;
  }

  public static void registerEvent(Color color, String message) {
    events.add(new Event(color,message));
  }

  public static void registerEventIfPlayerIsNear(Coordinate nearTo, Color color, String message) {
    if (Game.getActivePlayer().getActor().getCoordinate().area == nearTo.area) {
      registerEvent(color, message);
    }
  }

  public static void hideEventLog() {
    if (expandedMode == 1) {
      expandedMode = 0;
    }
    return;
  }

  public static void showEventLog() {
    expandedMode = 1;
    return;
  }

  public static void minimizeEventLog() {
    expandedMode = 2;
    return;
  }

  public static int toggleLogMode() {
    expandedMode = 3 - expandedMode;
    return expandedMode;
  }

  public static int getExpandedMode() { return expandedMode; }

  public static void drawOverlay(Graphics2D g2d) {

    if (Game.RANDOM.nextInt(100) < 1) {
      registerEvent(Color.BLUE, "Random Event " + Double.toString(Game.RANDOM.nextDouble()));
    }

    // Look for live events. if there are none and we're not in expanded mode then exit.
    if (!findLiveEvents() && expandedMode == 0) {
      return;
    }
    g2d.setFont(SMALL_TEXT);

    Coordinate playerAt = Game.getActivePlayer().getActor().getCoordinate();

    Dimension areaSizeInSquares = Game.getActiveWorld().getAreaSizeInSquares();

    final int areaHeight = areaSizeInSquares.getHeight();

    boolean drawingTop = (playerAt.localY > areaHeight * TOP_OR_BOTTOM_SWAP_LINE);
    int linesTall = getLinesTall();

    switch (expandedMode) {
      case 1: linesTall = MAX_LOG_LINES;
              break;
      case 2: linesTall = MIN_LOG_LINES;
    }

    int drawWidth = (int) (LINE_HEIGHT * areaSizeInSquares.getWidth() * 0.65);
    int drawHeight = LINE_HEIGHT * linesTall + LINE_SPACER;
    int drawX = areaSizeInSquares.getWidth() / 2 * LINE_HEIGHT - drawWidth / 2;
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
    g2d.drawRect(drawX, drawY, drawWidth, drawHeight);
    if (expandedMode > 0) {
      drawEventLog(g2d, linesTall, drawX, drawY, drawHeight);
    }
    else {
      drawLiveEvents(g2d, linesTall, drawX, drawY, drawHeight);
    }

  }

  private static void drawLiveEvents(Graphics2D g2d, int linesTall, int drawX, int drawY, int drawHeight) {

    // draw live event messages, bottom-up, for as long as there's room
    for (int i = 0; i < events.size() - liveEventsIndex && i < linesTall; i++) {

      Event event = events.get(events.size() - 1 - i);

      g2d.setColor(event.color);
      g2d.drawString(event.message, drawX + 10, drawY + drawHeight - LINE_HEIGHT * i - LINE_SPACER);
    }
  }

  public static void drawEventLog(Graphics2D g2d, int linesTall, int drawX, int drawY, int drawHeight) {

    // Draw linesTall number of lines.
    for (int i = 0; i < linesTall; i++) {

      int eventsIndex = events.size() - i - startingEvent;
      if (eventsIndex < 0) {
        break;
      }
      Event event = events.get(eventsIndex);
      g2d.setColor(event.color);
      String message;
      if (eventsIndex >= liveEventsIndex) {
        message = ">" + event.message;
      }
      else {
        message = " " + event.message;
      }
      g2d.drawString(message, drawX + 10, drawY + drawHeight - LINE_HEIGHT * i - LINE_SPACER);
    }
  }
}